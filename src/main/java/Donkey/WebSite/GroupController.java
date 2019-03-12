package Donkey.WebSite;

import Donkey.Api.GroupApiController;
import Donkey.Api.JSON.Group.GroupJson;
import Donkey.Database.Entity.GroupEntity;
import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.Database.Repository.GroupRepository;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.WebSite.FormClass.Group.AddGroupForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GroupController {

    private final ScreenRepository screenRegRep;
    private final GroupRepository grpRep;
    private final GroupApiController groupApi;
    private Logger log = LogManager.getLogger();

    @Autowired
    public GroupController(ScreenRepository screenRegRep, GroupRepository grpRep, GroupApiController groupApi) {
        this.screenRegRep = screenRegRep;
        this.grpRep = grpRep;
        this.groupApi = groupApi;
    }

    /**
     * Show form for adding a group in db
     * @param model
     * @return
     */
    @RequestMapping(value="/addGroup", method = RequestMethod.GET)
    public String addGroup(Model model, Authentication authentication){
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        model.addAttribute("user", userEntity);
        model.addAttribute("addGroupForm", new AddGroupForm());
        return "Group/addGroup";
    }

    /**
     * Show a unique group (with a request with id) or all group with parentId == null
     * @param model
     * @param id
     * @return
     */
    @RequestMapping(value = "/group", method = RequestMethod.GET)
    public String showGroup(Model model, @RequestParam(name = "id", defaultValue = "-1")int id, Authentication authentication){
        UserEntity userEntity = (UserEntity) authentication.getPrincipal();
        model.addAttribute("user", userEntity);
        if(id == -1){
            List<GroupEntity> groupList = grpRep.getGroupEntityByParentNull();
            List<ScreenEntity> screenList = screenRegRep.getScreenByGroupNull();
            model.addAttribute("groupList",groupList);
            model.addAttribute("screenList", screenList);
            model.addAttribute("getWithId",0);
            return "Group/group";
        }else if(grpRep.getGroupEntityById(id) != null){
            GroupEntity group =  grpRep.getGroupEntityById(id);
            List<ScreenEntity> screenList = screenRegRep.getScreenEntityByGroupId(id);
            List<GroupEntity> childrenList = grpRep.getGroupEntityByParent_Id(id);
            model.addAttribute("group",group);
            model.addAttribute("screenList" , screenList);
            model.addAttribute("childrenList" , childrenList);
            model.addAttribute("getWithId",1);
            if(group.getParent() != null){
                model.addAttribute("groupParentIsNull",0);
                model.addAttribute("groupParentId",group.getParent().getId());
                model.addAttribute("groupParentName",group.getParent().getName());
            }else
                model.addAttribute("groupParentIsNull",1);

            return "Group/group";
        }else{
            throw new ErrorCode.ResourceNotFoundException();
        }
    }
}
