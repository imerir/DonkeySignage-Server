package Donkey.WebSite;

import Donkey.Api.GroupApiController;
import Donkey.Api.JSON.Group.AddGroupJson;
import Donkey.Database.Entity.GroupEntity;
import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Repository.GroupRepository;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.Database.Repository.TemporalScreenRepository;
import Donkey.WebSite.FormClass.Group.AddGroupForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GroupController {
    //TODO
    //Remplacer les methodes POST par du js directement dans html #JSAWARE

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
            AddGroupJson newEntry = new AddGroupJson();
            newEntry.name = groupForm.getName();
            if(groupForm.getParentId() != -1)
                newEntry.parent = groupForm.getParentId();
            else
                newEntry.parent = -1;
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
