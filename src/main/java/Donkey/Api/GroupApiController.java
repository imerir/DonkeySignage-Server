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

import java.util.List;

@RestController
@RequestMapping("/api/")
public class GroupApiController {
    private Logger log = LogManager.getLogger();
    private final GroupRepository groupRepository;
    private final ScreenRepository screenRepository;

    @Autowired
    public GroupApiController(GroupRepository groupRepository, ScreenRepository screenRepository) {
        this.groupRepository = groupRepository;
        this.screenRepository = screenRepository;

    }

    /**
     * Drop a row group in database
     *
     * @param groupJson
     * @return DeleteGroupJson
     */
    @PostMapping(value = {"/deleteGroup"})
    public DeleteGroupJson deleteGroup(@RequestBody GroupJson groupJson) {
        GroupEntity group = groupRepository.getGroupEntityByNameAndParent(groupJson.name, groupRepository.getGroupEntityById(groupJson.parent));
        log.debug("[api/deleteGroup] id : " + group.getId() + ", parent : " + groupRepository.getGroupEntityById(groupJson.parent));
        groupRepository.delete(group);
        if (groupJson.parent == -1)
            return new DeleteGroupJson(group.getId(), group.getName(), -1, "");
        else
            return new DeleteGroupJson(group.getId(), group.getName(), group.getParent().getId(), "");
    }

    /**
     * Modify a group with a json
     *
     * @param modifyGroupJson
     * @return GroupJson
     */
 /*   @PostMapping(value = {"/modifyGroup"})
    public GroupJson modifyGroup(@RequestBody ModifyGroupJson modifyGroupJson){
        GroupEntity groupNeedModification = groupRepository.getGroupEntityByNameAndParent(modifyGroupJson.oldName, groupRepository.getGroupEntityById(modifyGroupJson.oldParentId));
        if(groupNeedModification != null){
            if(modifyGroupJson.oldParentId == -1){
                if(modifyGroupJson.newParentId == -1){
                    groupNeedModification.setName(modifyGroupJson.newName);
                    groupNeedModification.setParent(null);
                    groupRepository.save(groupNeedModification);
                    return new GroupJson(groupNeedModification.getName(),-1);
                }else{
                    groupNeedModification.setName(modifyGroupJson.newName);
                    groupNeedModification.setParent(groupRepository.getGroupEntityById(modifyGroupJson.newParentId));
                    groupRepository.save(groupNeedModification);
                    GroupEntity grpNewParent = groupRepository.getGroupEntityById(modifyGroupJson.newParentId);
                    grpNewParent.getChildrens().add(groupNeedModification);
                    groupRepository.save(grpNewParent);
                    return new GroupJson(groupNeedModification.getName(),groupNeedModification.getParent().getId());
                }
            }else{
                if(modifyGroupJson.newParentId == -1){
                    GroupEntity grpOldParent = groupRepository.getGroupEntityById(modifyGroupJson.oldParentId);
                    grpOldParent.getChildrens().remove(groupNeedModification);
                    groupRepository.save(grpOldParent);
                    groupNeedModification.setName(modifyGroupJson.newName);
                    groupNeedModification.setParent(null);
                    groupRepository.save(groupNeedModification);
                    return new GroupJson(groupNeedModification.getName(),-1);
                }else{
                    GroupEntity grpOldParent = groupRepository.getGroupEntityById(modifyGroupJson.oldParentId);
                    grpOldParent.getChildrens().remove(groupNeedModification);
                    groupRepository.save(grpOldParent);
                    groupNeedModification.setName(modifyGroupJson.newName);
                    groupNeedModification.setParent(null);
                    groupRepository.save(groupNeedModification);
                    GroupEntity grpNewParent = groupRepository.getGroupEntityById(modifyGroupJson.newParentId);
                    grpNewParent.getChildrens().add(groupNeedModification);
                    groupRepository.save(grpNewParent);
                    return new GroupJson(groupNeedModification.getName(),groupNeedModification.getParent().getId());
                }
            }
        }else{
            //TODO
            log.debug("[api/modifyGroup] Group not exist ");
        }
        return new GroupJson();
    }*/


    @PostMapping(value = {"/modifyGroup"})
    public GroupJson modifyGroup(@RequestBody ModifyGroupJson modifyGroupJson) {
        GroupEntity groupNeedModification = groupRepository.getGroupEntityById(modifyGroupJson.id);
        if (groupNeedModification != null) {
            if (modifyGroupJson.parentId == -1) {
                if(modifyGroupJson.name != null && !modifyGroupJson.name.isEmpty())
                    groupNeedModification.setName(modifyGroupJson.name);
                groupNeedModification.setParent(null);
                groupRepository.save(groupNeedModification);
                return new GroupJson(groupNeedModification.getName(), -1, "");
            } else {
                GroupEntity newGrpParent = groupRepository.getGroupEntityById(modifyGroupJson.parentId);
                if(modifyGroupJson.name != null && !modifyGroupJson.name.isEmpty())
                    groupNeedModification.setName(modifyGroupJson.name);
                groupNeedModification.setParent(newGrpParent);
                groupNeedModification = groupRepository.save(groupNeedModification);
                return new GroupJson(groupNeedModification.getName(), -1, "");
            }
        } else {
            return new GroupJson("",-1,"Group not exist");
        }
    }

    /**
     * Create and adding a group in database
     *
     * @param groupJson
     * @return GroupJson
     */
    @PostMapping(value = {"/addGroup"})
    public GroupJson addGroup(@RequestBody GroupJson groupJson) {
        GroupEntity newGroup = new GroupEntity();
        //log.debug(groupRepository.getGroupEntityByNameAndParent(groupJson.name, groupRepository.getGroupEntityById(groupJson.parent)));
        if (groupRepository.getGroupEntityByNameAndParent(groupJson.name, groupRepository.getGroupEntityById(groupJson.parent)) == null) {
            newGroup.setName(groupJson.name);
            if (groupJson.parent == -1)
                newGroup.setParent(null);
            else {
                newGroup.setParent(groupRepository.getGroupEntityById(groupJson.parent));
            }
            newGroup.getScreenList().clear();
            newGroup.getChildrens().clear();
        } else {
            return new GroupJson("",-1, "This Group is already create");
        }
        groupRepository.save(newGroup);
        if (newGroup.getParent() == null)
            return new GroupJson(newGroup.getName(), -1, "");
        else{
            GroupEntity groupNeedToChange = groupRepository.getGroupEntityById(groupJson.parent);
            groupNeedToChange.getChildrens().add(newGroup);
            groupRepository.save(newGroup);
            return new GroupJson(newGroup.getName(), newGroup.getParent().getId(), "");
        }
    }

    /**
     * Send name and id of parent's group
     *
     * @param id
     * @return GroupJson
     */
    @RequestMapping(value = {"/getParent"}, method = RequestMethod.GET)
    public GroupJson getParent(@RequestParam(name = "id") int id) {
        return new GroupJson(groupRepository.getGroupEntityById(id).getName(), groupRepository.getGroupEntityById(id).getId(), "");
    }

    /**
     * Send name and id of parent's children
     *
     * @param id
     * @return
     */
    @RequestMapping(value = {"/getChildren"}, method = RequestMethod.GET)
    public List<GroupEntity> getChildren(@RequestParam(name = "id") int id) {
        return groupRepository.getGroupEntityById(id).getChildrens();
    }

    /**
     * Send all screen of a group
     * @param id
     * @return
     */
    @RequestMapping(value={"/getScreen"},method = RequestMethod.GET)
    public List<ScreenEntity> getScreen(@RequestParam(name = "id") int id){
        return groupRepository.getGroupEntityById(id).getScreenList();
    }

    @RequestMapping("/test")
    public GroupEntity test(@RequestParam(value = "id") String id) {
//        ScreenEntity screen = screenRepository.getScreenEntityById(Integer.parseInt(id));
//        return screen.getGroup();
        GroupEntity groupEntity = groupRepository.getGroupEntityById(Integer.parseInt(id));
        for(GroupEntity groupEntity1 : groupEntity.getChildrens()){
            log.debug(groupEntity1.getName());
        }
        return null;


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
