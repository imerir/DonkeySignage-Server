package Donkey.Api;

import Donkey.Api.JSON.DeleteGroupJson;
import Donkey.Api.JSON.GroupJson;
import Donkey.Api.JSON.ModifyGroupJson;
import Donkey.Database.Entity.GroupEntity;
import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Repository.GroupRepository;
import Donkey.Database.Repository.ScreenRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class ApiController {

    private Logger log = LogManager.getLogger();
    private final GroupRepository groupRepository;
    private final ScreenRepository screenRepository;

    @Autowired
    public ApiController(GroupRepository groupRepository, ScreenRepository screenRepository) {
        this.groupRepository = groupRepository;
        this.screenRepository = screenRepository;

    }

    /**
     * Drop a row group in database
     * @param groupJson
     * @return DeleteGroupJson
     */
    @PostMapping(value = {"/deleteGroup"})
    public DeleteGroupJson deleteGroup(@RequestBody GroupJson groupJson){
        GroupEntity group = groupRepository.getGroupEntityByNameAndParent(groupJson.name,groupRepository.getGroupEntityById(groupJson.parent));
        log.debug("[api/deleteGroup] id : " + group.getId() + ", parent : " + groupRepository.getGroupEntityById(groupJson.parent));
        groupRepository.delete(group);
        if(groupJson.parent == -1)
            return new DeleteGroupJson(group.getId(),group.getName(), -1);
        else
            return new DeleteGroupJson(group.getId(),group.getName(), group.getParent().getId());
    }

    /**
     * Modify a group with a json
     * @param modifyGroupJson
     * @return GroupJson
     */
    @PostMapping(value = {"/modifyGroup"})
    public GroupJson modifyGroup(@RequestBody ModifyGroupJson modifyGroupJson){
        GroupEntity groupNeedModification = groupRepository.getGroupEntityByNameAndParent(modifyGroupJson.oldName, groupRepository.getGroupEntityById(modifyGroupJson.oldParentId));
        if(groupNeedModification != null){
            if(modifyGroupJson.newParentId == -1){
                GroupEntity grpOldParent = groupRepository.getGroupEntityById(modifyGroupJson.oldParentId);
                grpOldParent.getChildrens().remove(groupNeedModification);
                groupNeedModification.setParent(null);
                groupRepository.save(grpOldParent);
                return new GroupJson(groupNeedModification.getName(),-1);
            }else{
                GroupEntity grpOldParent = groupRepository.getGroupEntityById(modifyGroupJson.oldParentId);
                grpOldParent.getChildrens().remove(groupNeedModification);
                GroupEntity grpNewParent = groupRepository.getGroupEntityById(modifyGroupJson.newParentId);
                groupNeedModification.setName(modifyGroupJson.newName);
                groupNeedModification.setParent(groupRepository.getGroupEntityById(modifyGroupJson.newParentId));
                grpNewParent.getChildrens().add(groupNeedModification);
                groupRepository.save(grpOldParent);
                groupRepository.save(grpNewParent);
                groupRepository.save(groupNeedModification);
                return new GroupJson(groupNeedModification.getName(),groupNeedModification.getParent().getId());
            }
        }else{
            //TODO
            log.debug("[api/modifyGroup] Group not exist ");
        }
        return new GroupJson();
    }

    /**
     * Create and adding a group in database
     * @param groupJson
     * @return GroupJson
     */
    @PostMapping(value = {"/addGroup"})
    public GroupJson addGroup(@RequestBody GroupJson groupJson){
        GroupEntity newGroup = new GroupEntity();
        log.debug(groupRepository.getGroupEntityByNameAndParent(groupJson.name, groupRepository.getGroupEntityById(groupJson.parent)));
        if (groupRepository.getGroupEntityByNameAndParent(groupJson.name, groupRepository.getGroupEntityById(groupJson.parent)) == null){
            newGroup.setName(groupJson.name);
            if(groupJson.parent == -1)
                newGroup.setParent(null);
            else{
                newGroup.setParent(groupRepository.getGroupEntityById(groupJson.parent));
            }
            newGroup.getScreenList().clear();
            newGroup.getChildrens().clear();
        }
        else{
            //TODO
            //Gestion erreur
            log.info("[api/addGroup] this group is already create");
        }
        groupRepository.save(newGroup);
        GroupEntity groupNeedToChange = groupRepository.getGroupEntityById(groupJson.parent);
        groupNeedToChange.getChildrens().add(newGroup);
        groupRepository.save(newGroup);
        if(newGroup.getParent() == null)
            return new GroupJson(newGroup.getName(),-1);
        else
            return new GroupJson(newGroup.getName(),newGroup.getParent().getId());
    }

    @RequestMapping("/test")
    public GroupEntity test(@RequestParam(value = "id") String id){
        ScreenEntity screen = screenRepository.getScreenEntityById(Integer.parseInt(id));
        return screen.getGroup();


//        GroupEntity group = groupRepository.getGroupEntityById(Integer.parseInt(id));
//
//        ScreenEntity se = new ScreenEntity("1", "1", "test2", group);
//        se = screenRepository.save(se);
//
//        group.getScreenList().add(se);
//        group = groupRepository.save(group);
//        return group.getScreenList();
    }



}
