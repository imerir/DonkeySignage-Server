package Donkey.Api;

import Donkey.Api.JSON.*;
import Donkey.Api.JSON.Error;
import Donkey.Api.JSON.Group.GroupJson;
import Donkey.Api.JSON.Screen.*;
import Donkey.Database.Entity.GroupEntity;
import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Entity.TemplateEntity;
import Donkey.Database.Entity.TemporalScreenEntity;
import Donkey.Database.Repository.GroupRepository;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.Database.Repository.TemporalScreenRepository;
import Donkey.Tools.IpTools;
import Donkey.Tools.ScreenTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *
 */
@RestController
@RequestMapping("/api/screen")
public class ScreenApiController {
    private final ScreenRepository screenRegisterRep;
    private final TemporalScreenRepository tmpRegisterRep;
    private final GroupRepository grpRep;
    private Logger log = LogManager.getLogger();

    @Autowired
    public ScreenApiController(TemporalScreenRepository tmpRegisterRep, ScreenRepository screenRegisterRep, GroupRepository grpRep) {
        this.tmpRegisterRep = tmpRegisterRep;
        this.screenRegisterRep = screenRegisterRep;
        this.grpRep = grpRep;
    }

    @RequestMapping(value = {"/test"},method = RequestMethod.GET)
    public String test(HttpServletRequest request){
        return IpTools.getInstance().getClientIpAddress(request);
    }

    /**
     * Request for obtain the temporary token, add screen in table TemporalScreen
     * @param request, for obtain the ip address
     * @param uuid, uniq for every screen
     * @return TemporalRegisterJson, json who contains all informations of screen
     */
    @PostMapping(value = {"/getToken"})
    public ResponseEntity<?> getToken(HttpServletRequest request, @RequestBody UuidJson uuid){
        TemporalScreenEntity newTmpRegister;
        //log.debug("Post on getToken, value of uuid : " + uuid.getUuid());
        if(tmpRegisterRep.getTemporalRegisterByUuid(uuid.getUuid()) == null){
            newTmpRegister = new TemporalScreenEntity(request.getRemoteAddr(), ScreenTools.getInstance().generateCheckToken(),uuid.getUuid(), ScreenTools.getInstance().generateExpirationDateLocalDate());
        }else
        {
            newTmpRegister = tmpRegisterRep.getTemporalRegisterByUuid(uuid.getUuid());
            newTmpRegister.setTempToken(ScreenTools.getInstance().generateCheckToken());
            newTmpRegister.setExpirationDate(ScreenTools.getInstance().generateExpirationDateLocalDate());
        }
        tmpRegisterRep.save(newTmpRegister);
        return new ResponseEntity<>(new TemporalRegisterJson(newTmpRegister.getTempToken(),newTmpRegister.getUuid(), ScreenTools.getInstance().generateExpirationDateStr()),HttpStatus.OK);
    }

    /**
     * Check if the screen was add in the table screen_register
     * @param uuid
     * @return ResponseEntity, send a json and HTTP 200 if add, and 403 if not
     */
    @RequestMapping(value = {"/isRegistered"}, method = RequestMethod.GET)
    public ResponseEntity<ScreenRegisterJson> isRegistered (@CookieValue(value = "uuid")String uuid){
        log.trace("Uuid send by cookie : " + uuid);
        if(uuid != null && !uuid.equals("")){
            ScreenEntity newScreenRegister = screenRegisterRep.getScreenRegisterByUuid(uuid);
            log.trace("ScreenRegister get in db: " + newScreenRegister);
            if(newScreenRegister != null){
                ScreenRegisterJson newScreenRegisterJson = new ScreenRegisterJson(newScreenRegister.getToken(), newScreenRegister.getUuid(), "");
                log.trace("Send Json " + newScreenRegisterJson);
                return new ResponseEntity<>(newScreenRegisterJson, HttpStatus.OK);
            }else{
                log.debug("ScreenRegister not in db");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }else{
            log.trace("Bad Cookie");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }


    @PostAuthorize("hasPermission(returnObject, 'read')")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<?> getScreen(@RequestParam(name = "id", defaultValue = "-1") int id){
        if(id == -1){
            List<ScreenEntity> screenEntityList = screenRegisterRep.getAllBy();
            return new ResponseEntity<>(screenEntityList, HttpStatus.OK);
        }
        ScreenEntity screen = screenRegisterRep.getScreenEntityById(id);
        if(screen == null){
            log.info("[api/screen GET] Template not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(screen, HttpStatus.OK);
    }

    /**
     * Add screen to the database
     * @param screenJson
     * @return ScreenJson
     */
    @RequestMapping(value = {"/"}, method = RequestMethod.POST)
    public ResponseEntity<?> addScreen (@RequestBody ScreenJson screenJson){
        if(screenJson.uuid != null && !screenJson.uuid.isEmpty()){
            ScreenEntity newEntry = new ScreenEntity();
            if(screenRegisterRep.getScreenRegisterByUuid(screenJson.uuid) == null){
                TemporalScreenEntity tmpReg = tmpRegisterRep.getTemporalRegisterByUuid(screenJson.uuid);
                newEntry.setIp(tmpReg.getIp());
                newEntry.setUuid(tmpReg.getUuid());
                newEntry.setName(screenJson.name);
                newEntry.setToken(ScreenTools.getInstance().generateUuid());
                newEntry.setGroup(grpRep.getGroupEntityById(screenJson.groupId));
            }else{
                ScreenEntity tmpScreen = screenRegisterRep.getScreenRegisterByUuid(screenJson.uuid);
                newEntry.setName(tmpScreen.getName());
                newEntry.setUuid(tmpScreen.getUuid());
                newEntry.setIp(tmpScreen.getIp());
                newEntry.setToken(ScreenTools.getInstance().generateUuid());
                newEntry.setGroup(grpRep.getGroupEntityById(screenJson.groupId));
            }
            screenRegisterRep.save(newEntry);
            tmpRegisterRep.delete(tmpRegisterRep.getTemporalRegisterByUuid(screenJson.uuid));

            GroupEntity groupNeedChange = grpRep.getGroupEntityById(newEntry.getGroup().getId());
            groupNeedChange.getScreenList().add(newEntry);
            //Maybe erase before add
            grpRep.save(groupNeedChange);
            return new ResponseEntity<>(new ScreenJson(newEntry.getIp(),newEntry.getToken(),newEntry.getUuid(),newEntry.getName(),newEntry.getGroup().getId()),HttpStatus.OK );
        }else{
            return new ResponseEntity<>(new Error("Uuid send is null or empty !","UUID_EMPTY_OR_NULL"),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Delete a screen in a database
     * @param deleteScreenJson
     * @return DeleteScreenJson
     */
    @RequestMapping(value = {"/"}, method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteScreen(@RequestBody DeleteScreenJson deleteScreenJson){
        ScreenEntity screenToDelete = screenRegisterRep.getScreenEntityById(deleteScreenJson.id);
        if(screenToDelete != null){
            screenRegisterRep.delete(screenToDelete);
            if(deleteScreenJson.groupId != -1){
                return new ResponseEntity<>(new  DeleteScreenJson(screenToDelete.getId(),screenToDelete.getName(),screenToDelete.getGroup().getId()),HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new DeleteScreenJson(screenToDelete.getId(),screenToDelete.getName(),-1),HttpStatus.OK);
            }
        }else{
            return new ResponseEntity<>(new Error("Screen with id : " + deleteScreenJson.id + " not exist ! ", "SCREEN_NOT_FOUND"),HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Modify a screen
     * @param modifyScreenJson
     * @return ResponseEntity
     */
    @RequestMapping(value = {"/"}, method = RequestMethod.PUT)
    public ResponseEntity<?> modifyScreen(@RequestBody ModifyScreenJson modifyScreenJson){
        ScreenEntity screenNeedModification = screenRegisterRep.getScreenEntityById(modifyScreenJson.id);
        if (screenNeedModification != null) {
            if (modifyScreenJson.groupId == -1) {
                if(modifyScreenJson.name != null && !modifyScreenJson.name.isEmpty())
                    screenNeedModification.setName(modifyScreenJson.name);
                screenNeedModification.setGroup(null);
                screenRegisterRep.save(screenNeedModification);
                return new ResponseEntity<>(new ScreenJson(screenNeedModification.getIp(),screenNeedModification.getToken(),screenNeedModification.getUuid(),screenNeedModification.getName(),screenNeedModification.getGroup().getId()),HttpStatus.OK);
            } else {
                GroupEntity newGrpParent = grpRep.getGroupEntityById(modifyScreenJson.groupId);
                if(modifyScreenJson.name != null && !modifyScreenJson.name.isEmpty())
                    screenNeedModification.setName(modifyScreenJson.name);
                screenNeedModification.setGroup(newGrpParent);
                screenNeedModification = screenRegisterRep.save(screenNeedModification);
                return new ResponseEntity<>(new ScreenJson(screenNeedModification.getIp(),screenNeedModification.getToken(),screenNeedModification.getUuid(),screenNeedModification.getName(),screenNeedModification.getGroup().getId()),HttpStatus.OK);

            }
        } else {
            return new ResponseEntity<>(new Error("No Screen with id : " + modifyScreenJson.id,"ID_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Send name and id of parent's group
     *
     * @param id
     * @return GroupJson
     */
    @RequestMapping(value = {"/getGroup"}, method = RequestMethod.GET)
    public ResponseEntity<?> getGroup(@RequestParam(name = "id") int id) {
        if(screenRegisterRep.getScreenEntityById(id) != null){
            if(id != -1 )
                return new ResponseEntity<>(new GroupJson(grpRep.getGroupEntityById(screenRegisterRep.getScreenEntityById(id).getGroup().getId()).getName(), grpRep.getGroupEntityById(screenRegisterRep.getScreenEntityById(id).getGroup().getId()).getId()), HttpStatus.OK);
            else
                return new ResponseEntity<>(new GroupJson("", -1), HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new Error("No Screen with id : " + id,"ID_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }

    }
}
