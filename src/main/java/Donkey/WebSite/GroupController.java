package Donkey.WebSite;

import Donkey.Api.GroupApiController;
import Donkey.Database.Entity.GroupEntity;
import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserScreenPrivilege;
import Donkey.Database.Repository.GroupRepository;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.Database.Repository.UserScreenPrivilegeRepository;
import Donkey.WebSite.FormClass.Group.AddGroupForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class GroupController {

    private final ScreenRepository screenRegRep;
    private final GroupRepository grpRep;
    private final GroupApiController groupApi;
    @Autowired
    private final UserScreenPrivilegeRepository privilegeRepository;
    private Logger log = LogManager.getLogger();

    @Autowired
    public GroupController(ScreenRepository screenRegRep, GroupRepository grpRep, GroupApiController groupApi, UserScreenPrivilegeRepository privilegeRepository) {
        this.screenRegRep = screenRegRep;
        this.grpRep = grpRep;
        this.groupApi = groupApi;
        this.privilegeRepository = privilegeRepository;
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
        GroupEntity group = grpRep.getGroupEntityById(id);
        if(id == -1){

            List<GroupEntity> groupList = grpRep.getGroupEntityByParentNull();
            List<ScreenEntity> screenList = getMyScreen(userEntity, null);
            model.addAttribute("groupList",groupList);
            model.addAttribute("screenList", screenList);

        }else if(group != null){

            List<GroupEntity> childrenList = group.getChildrens();
            model.addAttribute("group",group);
            model.addAttribute("screenList" , getMyScreen(userEntity, group));
            model.addAttribute("groupList" , childrenList);


        }else{
            throw new ErrorCode.ResourceNotFoundException();
        }

        return "Group/group";
    }


    private List<ScreenEntity> getMyScreen(UserEntity user, GroupEntity group){
        List<ScreenEntity> screenList  = new ArrayList<>();
        if(user.isAdmin()){
            if(group == null)
                screenList = screenRegRep.getScreenByGroupNull();
            else
                screenList = group.getScreenList();
        }
        else{
            List<UserScreenPrivilege> privileges = privilegeRepository.getByUserEntityAndScreenEntity_GroupOrderByScreenEntity_Name(user, group);
            for(UserScreenPrivilege priv : privileges){
                screenList.add(priv.getScreenEntity());
            }
        }
        return screenList;
    }
}
