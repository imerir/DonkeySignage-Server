package Donkey.Api;

import Donkey.Api.JSON.ScreenRegisterJson;
import Donkey.Api.JSON.TemporalRegisterJson;
import Donkey.Api.PostForm.UuidPostForm;
import Donkey.Database.Entity.ScreenRegister;
import Donkey.Database.Entity.TemporalRegister;
import Donkey.Database.Repository.ScreenRegisterRepository;
import Donkey.Database.Repository.TemporalRegisterRepository;
import Donkey.Tools.IpTools;
import Donkey.Tools.UserTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/screen")
public class ScreenApiController {
    private final ScreenRegisterRepository screenRegisterRep;
    private final TemporalRegisterRepository tmpRegisterRep;
    private Logger log = LogManager.getLogger();

    @Autowired
    public ScreenApiController(TemporalRegisterRepository tmpRegisterRep, ScreenRegisterRepository screenRegisterRep) {
        this.tmpRegisterRep = tmpRegisterRep;
        this.screenRegisterRep = screenRegisterRep;
    }

    @RequestMapping(value = {"/test"},method = RequestMethod.GET)
    public String test(HttpServletRequest request){
        return IpTools.getInstance().getClientIpAddress(request);
    }

    //To Modify
    @PostMapping(value = {"/getToken"})
    public TemporalRegisterJson getToken(HttpServletRequest request, @ModelAttribute UuidPostForm uuid){
        TemporalRegister newTmpRegister;
        log.debug("Post on getToken, value of uuid : " + uuid.getUuid());
        if(tmpRegisterRep.getTemporalRegisterByUuid(uuid.getUuid()) == null){
            newTmpRegister = new TemporalRegister(request.getRemoteAddr(), UserTools.getInstance().generateCheckToken(true),uuid.getUuid(),UserTools.getInstance().generateExpirationDateLocalDate());
        }else
        {
            newTmpRegister = tmpRegisterRep.getTemporalRegisterByUuid(uuid.getUuid());
            newTmpRegister.setTempToken(UserTools.getInstance().generateCheckToken(true));
            newTmpRegister.setExpirationDate(UserTools.getInstance().generateExpirationDateLocalDate());
        }
        tmpRegisterRep.save(newTmpRegister);
        return new TemporalRegisterJson(newTmpRegister.getTempToken(),newTmpRegister.getUuid(),UserTools.getInstance().generateExpirationDateStr());
    }

    @RequestMapping(value = {"/isRegistered"}, method = RequestMethod.GET)
    public ResponseEntity<ScreenRegisterJson> isRegistered (@CookieValue(value = "uuid")String uuid){
        log.debug("Uuid send by cookie : " + uuid);
        if(uuid != null && !uuid.equals("")){
            ScreenRegister newScreenRegister = screenRegisterRep.getScreenRegisterByUuid(uuid);
            log.debug("ScreenRegister get in db: " + newScreenRegister);
            if(newScreenRegister != null){
                ScreenRegisterJson newScreenRegisterJson = new ScreenRegisterJson(newScreenRegister.getToken(), newScreenRegister.getUuid());
                log.debug("Send Json " + newScreenRegisterJson);
                return new ResponseEntity<>(newScreenRegisterJson, HttpStatus.OK);
            }else{
                log.debug("ScreenRegister not in db");
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        }else{
            log.debug("Bad Cookie");
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
