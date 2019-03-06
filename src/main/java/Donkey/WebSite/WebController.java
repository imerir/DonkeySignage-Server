package Donkey.WebSite;

import Donkey.Api.GroupApiController;
import Donkey.Api.JSON.Group.GroupJson;
import Donkey.Database.Entity.GroupEntity;
import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Entity.TemporalScreenEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.Database.Repository.GroupRepository;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.Database.Repository.TemporalScreenRepository;
import Donkey.Tools.ScreenTools;
import Donkey.WebSite.FormClass.Group.AddGroupForm;
import Donkey.WebSite.FormClass.Screen.ScreenRegisterForm;
import Donkey.WebSite.FormClass.Screen.TmpTokenForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


//TODO
//Remplacer les methodes POST par du js directement dans html #JSAWARE
//

@Controller
public class WebController {
    private final ScreenRepository screenRegRep;
    private final TemporalScreenRepository tmpRegisterRep;
    private final GroupRepository grpRep;
    private final GroupApiController groupApi;

    private Logger log = LogManager.getLogger();

    public WebController(ScreenRepository screenRegRep, TemporalScreenRepository tmpRegisterRep, GroupRepository grpRep, GroupApiController groupApi){
        this.screenRegRep = screenRegRep;
        this.tmpRegisterRep = tmpRegisterRep;
        this.grpRep = grpRep;
        this.groupApi = groupApi;
    }

    @RequestMapping(value ="/",method = RequestMethod.GET)
    public String index(Model model, Authentication authentication){
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        model.addAttribute("user", userEntity);
        return "index";
    }

    /**
     * Display screenRegister.html for gettigng temporary token of the screen
     * @param model
     * @param uuid
     * @return String
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/screenRegister"}, method = RequestMethod.GET)
    public String registerScreenGet(Model model, @RequestParam(value = "uuid", defaultValue = "")String uuid) {
        TemporalScreenEntity tmpReg = tmpRegisterRep.getTemporalRegisterByUuid(uuid);
        if (!uuid.isEmpty() && tmpReg!= null) {
            model.addAttribute("uuid",uuid);
            model.addAttribute("screenRegisterForm",new ScreenRegisterForm());
            return "Screen/formScreenRegister";
        } else {
            TmpTokenForm form = new TmpTokenForm();
            model.addAttribute("tmpTokenForm", form);
            return "Screen/screenRegister";
        }
    }

    /**
     *  Use temporary token for getting uuid of screen, and redirect on formScreenRegister.
     * @param model
     * @param tokenForm
     * @return String
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = {"/screenRegister"})
    public String registerScreenPost(Model model, @ModelAttribute TmpTokenForm tokenForm){
        TemporalScreenEntity tmpReg = tmpRegisterRep.getTemporalRegisterByTempToken(tokenForm.getTempToken());
        log.debug("Post screenRegister, token : " + tokenForm.getTempToken());
        if(!tokenForm.getTempToken().isEmpty() && tmpReg != null){
            return "redirect:/screenRegister?uuid="+tmpReg.getUuid();
        }else{
            //TODO
            //Gestion erreur
            return "Error";
        }
    }

    /**
     * Display formScreenRegister, and adding this in database.
     * @param model
     * @param screenRegisterForm
     * @return String
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/formScreenRegister"}, method = RequestMethod.POST)
    public String formScreenRegister(Model model, @ModelAttribute ScreenRegisterForm screenRegisterForm){
        if(screenRegisterForm.getUuid() != null && !screenRegisterForm.getUuid().isEmpty()){
            ScreenEntity newEntry = new ScreenEntity();
            if(screenRegRep.getScreenRegisterByUuid(screenRegisterForm.getUuid()) == null){
                model.addAttribute("screenRegisterForm",screenRegisterForm);
                TemporalScreenEntity tmpReg = tmpRegisterRep.getTemporalRegisterByUuid(screenRegisterForm.getUuid());
                newEntry.setIp(tmpReg.getIp());
                newEntry.setUuid(tmpReg.getUuid());
                newEntry.setName(screenRegisterForm.getName());
                newEntry.setToken(ScreenTools.getInstance().generateUuid());
                newEntry.setGroup(grpRep.getGroupEntityById(screenRegisterForm.getGroupId()));
            }else{
                ScreenEntity tmpScreen = screenRegRep.getScreenRegisterByUuid(screenRegisterForm.getUuid());
                newEntry.setName(tmpScreen.getName());
                newEntry.setUuid(tmpScreen.getUuid());
                newEntry.setIp(tmpScreen.getIp());
                newEntry.setToken(ScreenTools.getInstance().generateUuid());
                newEntry.setGroup(grpRep.getGroupEntityById(screenRegisterForm.getGroupId()));
            }
            screenRegRep.save(newEntry);
            tmpRegisterRep.delete(tmpRegisterRep.getTemporalRegisterByUuid(screenRegisterForm.getUuid()));
            return "redirect:/screen?id="+newEntry.getId();
        }else{
            return "Uuid null or empty";
        }
    }

    /**
     * Show a screen, with some information
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value="/screen", method = RequestMethod.GET)
    public String getScreen(Model model, @RequestParam(name = "id")int id){
        if(screenRegRep.getScreenEntityById(id) != null){
            ScreenEntity theScreen =  screenRegRep.getScreenEntityById(id);
            if(theScreen.getGroup() != null){
                model.addAttribute("groupId",theScreen.getGroup().getId());
                model.addAttribute("groupName",theScreen.getGroup().getName());
            }
            else
                model.addAttribute("groupId",-1);
            model.addAttribute("screen",theScreen);
            return "Screen/screen";
        }else{
            return "Error";
        }
    }

    /**
     * Show form for adding a group in db
     * @param model
     * @return
     */
    @RequestMapping(value="/addGroup", method = RequestMethod.GET)
    public String addGroup(Model model){
        model.addAttribute("addGroupForm", new AddGroupForm());
        return "Group/addGroup";
    }

    /**
     * Add group in db
     * @param model
     * @param groupForm
     * @return
     */
    @PostMapping(value = "/addGroup")
    public String addGroup(Model model, @ModelAttribute AddGroupForm groupForm){
        if(groupForm.getName() != null && ! groupForm.getName().isEmpty()){
            model.addAttribute("groupForm",groupForm);
            GroupJson newEntry = new GroupJson();
//            GroupEntity newEntry = new GroupEntity();
//            newEntry.setName(groupForm.getName());
            newEntry.name = groupForm.getName();
            if(groupForm.getParentId() != -1)
                newEntry.parent = groupForm.getParentId();
//                newEntry.parent = groupForm.getParentId();
            else
//                newEntry.setParent(null);
                newEntry.parent = -1;
//            newEntry.getChildrens().clear();
//            newEntry.getScreenList().clear();
//            grpRep.save(newEntry);
            groupApi.addGroup(newEntry);
            if(groupForm.getParentId() != -1)
                return "redirect:/group?id="+grpRep.getGroupEntityByNameAndParent(groupForm.getName(),grpRep.getGroupEntityById(groupForm.getParentId())).getId();
            else
                return "redirect:/group?id="+grpRep.getGroupEntityByNameAndParent(groupForm.getName(),null).getId();
        }else{
            return "error";
        }
    }

    /**
     * Show a unique group (with a request with id) or all group with parentId == null
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "/group")
    public String showGroup(Model model, @RequestParam(name = "id", defaultValue = "-1")int id){
        if(id == -1){
            List<GroupEntity> groupList = grpRep.getGroupEntityByParentNull();
            List<ScreenEntity> screenList = screenRegRep.getScreenByGroupNull();
            model.addAttribute("groupList",groupList);
            model.addAttribute("screenList", screenList);
            model.addAttribute("getWithId",0);
            return "Group/group";
        }
        if(grpRep.getGroupEntityById(id) != null){
            GroupEntity group =  grpRep.getGroupEntityById(id);
            List<ScreenEntity> screenList = screenRegRep.getScreenEntityByGroupId(id);
            List<GroupEntity> childrenList = grpRep.getGroupEntityByParent_Id(id);
            model.addAttribute("group",group);
            model.addAttribute("screenList" , screenList);
            model.addAttribute("childrenList" , childrenList);
            model.addAttribute("getWithId",1);
            return "Group/group";
        }else{
            return "Error";
        }
    }
}
