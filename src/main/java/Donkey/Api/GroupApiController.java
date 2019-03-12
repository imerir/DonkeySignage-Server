package Donkey.Api;


import Donkey.Api.JSON.Error;
import Donkey.Api.JSON.Group.GroupJson;
import Donkey.Database.Entity.GroupEntity;
import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Repository.GroupRepository;
import Donkey.Database.Repository.ScreenRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@RestController
@RequestMapping("/api/group")
public class GroupApiController {
    private Logger log = LogManager.getLogger();
    private final GroupRepository groupRepository;
    private final ScreenRepository screenRepository;

    @Autowired
    public GroupApiController(GroupRepository groupRepository, ScreenRepository screenRepository) {
        this.groupRepository = groupRepository;
        this.screenRepository = screenRepository;

    }


    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getGroups(@RequestParam(name = "id", defaultValue = "-1") int id){
        if(id == -1){
            return new ResponseEntity<>(convert(groupRepository.getAllBy()), HttpStatus.OK);
        }
        GroupEntity groupEntity = groupRepository.getGroupEntityById(id);
        if(groupEntity == null){
            log.info("[api/group GET] Group " + id + " not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new GroupJson(groupEntity), HttpStatus.OK);
    }


    /**
     * Drop a row group in database
     *
     * @param id
     * @return DeleteGroupJson
     */
    @PostAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteGroup(@RequestParam (name = "id") int id ) {
        GroupEntity grpToDelete = groupRepository.getGroupEntityById(id);
        if(grpToDelete != null){
            if(grpToDelete.getParent() != null){
                if(grpToDelete.getScreenList().size() != 0){
                    GroupEntity newParentForScreen = groupRepository.getGroupEntityById(groupRepository.getGroupEntityById(id).getParent().getId());
                    for(int i = 0 ; i < grpToDelete.getScreenList().size() ; i++){
                        ScreenEntity screenWithNewParent = grpToDelete.getScreenList().get(i);
                        screenWithNewParent.setGroup(newParentForScreen);
                        screenRepository.save(screenWithNewParent);
                    }
                }
                if(grpToDelete.getChildrens().size() != 0){
                    GroupEntity newParentForScreen = groupRepository.getGroupEntityById(groupRepository.getGroupEntityById(id).getParent().getId());
                    for(int i = 0 ; i < grpToDelete.getChildrens().size() ; i++){
                        GroupEntity grpWithNewParent = grpToDelete.getChildrens().get(i);
                        grpWithNewParent.setParent(newParentForScreen);
                        groupRepository.save(grpWithNewParent);
                    }
                }
                groupRepository.delete(grpToDelete);
            }else{
                if(grpToDelete.getScreenList().size() != 0){
                    for(int i = 0 ; i < grpToDelete.getScreenList().size() ; i++){
                        ScreenEntity screenWithNewParent = grpToDelete.getScreenList().get(i);
                        screenWithNewParent.setGroup(null);
                        screenRepository.save(screenWithNewParent);
                    }
                }
                if(grpToDelete.getChildrens().size() != 0){
                    for(int i = 0 ; i < grpToDelete.getChildrens().size() ; i++){
                        GroupEntity grpWithNewParent = grpToDelete.getChildrens().get(i);
                        grpWithNewParent.setParent(null);
                        groupRepository.save(grpWithNewParent);
                    }
                }
                groupRepository.delete(grpToDelete);
            }

            if(grpToDelete.getParent() == null)
                return new ResponseEntity<>(new GroupJson(grpToDelete.getId(),grpToDelete.getName(),-1),HttpStatus.NO_CONTENT);
            else
                return new ResponseEntity<>(new GroupJson(grpToDelete.getId(),grpToDelete.getName(),grpToDelete.getParent().getId()),HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Modify a group with a json
     *
     * @param modifyGroupJson
     * @return GroupJson
     */
    @PostAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public ResponseEntity<?> modifyGroup(@RequestBody GroupJson modifyGroupJson) {
        GroupEntity groupNeedModification = groupRepository.getGroupEntityById(modifyGroupJson.id);
        log.trace("[API/Group POST] Group need modification id : " + groupRepository.getGroupEntityById(modifyGroupJson.id));
        if (groupNeedModification != null) {
            if(modifyGroupJson.name != null && !modifyGroupJson.name.isEmpty())
                groupNeedModification.setName(modifyGroupJson.name);
            if (modifyGroupJson.parentId == -1)
                groupNeedModification.setParent(null);
            else{
                GroupEntity newGrpParent = groupRepository.getGroupEntityById(modifyGroupJson.parentId);
                groupNeedModification.setParent(newGrpParent);
            }
            groupNeedModification = groupRepository.save(groupNeedModification);
            return new ResponseEntity<>(new GroupJson(groupNeedModification.getId(),groupNeedModification.getName(), modifyGroupJson.parentId), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Error("No Group with id : " + modifyGroupJson.id,"ID_NOT_FOUND"), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create and adding a group in database
     *
     * @param addGroupJson
     * @return ResponseEntity with GroupJson
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addGroup(@RequestBody GroupJson addGroupJson) {
        log.debug("[api/group POST] groupJson : id :" + addGroupJson.id + ", name : " + addGroupJson.name + ", parentId : " + addGroupJson.parentId);
//        log.debug(addGroupJson);
        GroupEntity newGroup = new GroupEntity();
//        log.debug(groupRepository.getGroupEntityByNameAndParent(addGroupJson.name, groupRepository.getGroupEntityById(addGroupJson.parent)));
        if (groupRepository.getGroupEntityByNameAndParent(addGroupJson.name, groupRepository.getGroupEntityById(addGroupJson.parentId)) == null) {
            newGroup.setName(addGroupJson.name);
            if (addGroupJson.parentId == -1)
                newGroup.setParent(null);
            else {
                newGroup.setParent(groupRepository.getGroupEntityById(addGroupJson.parentId));
            }
            newGroup.getScreenList().clear();
            newGroup.getChildrens().clear();
        } else {
            return new ResponseEntity<>(new Error("This Group is already create","ID_ALREADY_USE"), HttpStatus.CONFLICT);
        }
        newGroup = groupRepository.save(newGroup);
        if (newGroup.getParent() == null)
            return new ResponseEntity<>(newGroup, HttpStatus.OK);
        else{
            GroupEntity groupNeedToChange = groupRepository.getGroupEntityById(addGroupJson.parentId);
            groupNeedToChange.getChildrens().add(newGroup);
            newGroup = groupRepository.save(newGroup);
            return new ResponseEntity<>(newGroup, HttpStatus.OK);
        }
    }

    /**
     * Send name and id of parent's group
     *
     * @param id
     * @return ResponseEntity with GroupJson
     */
    @RequestMapping(value = "/getParent", method = RequestMethod.GET)
    public ResponseEntity<?>  getParent(@RequestParam(name = "id") int id) {
        if(groupRepository.getGroupEntityById(id) != null)
            return new ResponseEntity<>(new GroupJson(groupRepository.getGroupEntityById(id)), HttpStatus.OK);
        else
            return new ResponseEntity<>(new Error("No group with id : " + id,"ID_ALREADY_USE"), HttpStatus.NOT_FOUND);

    }

    /**
     * Send name and id of parent's children
     *
     * @param id
     * @return ResponseEntity with list of children
     */
    @RequestMapping(value = "/getChildren", method = RequestMethod.GET)
    public ResponseEntity<?> getChildren(@RequestParam(name = "id") int id) {
        List<GroupEntity> raw;
        GroupEntity parent = groupRepository.getGroupEntityById(id);
        if(parent != null){
            raw = parent.getChildrens();

        }
        else if(id == -1){
            raw = groupRepository.getGroupEntityByParent_Id(null);
        }
        else
            return new ResponseEntity<>(new Error("No group with id : " + id,"ID_NOT_FOUND"), HttpStatus.NOT_FOUND);


        HashMap<String, Object> map = new HashMap<>();
        int parentId;
        boolean isRoot = false;
        if(id == -1){
            parentId = -1;
        }

        else{
            parentId = parent.getParent() == null ? -1 : parent.getParent().getId();
        }
        map.put("parentId", parentId);
        map.put("children", convert(raw));

        return new ResponseEntity<>(map , HttpStatus.OK);
    }

    /**
     * Send all screen of a group
     * @param id
     * @return ResponseEntity with List of screen
     */
    @RequestMapping(value="/getScreen",method = RequestMethod.GET)
    public ResponseEntity<?> getScreen(@RequestParam(name = "id") int id){
        if(groupRepository.getGroupEntityById(id) != null)
            return new ResponseEntity<>(groupRepository.getGroupEntityById(id).getScreenList(), HttpStatus.OK);
        else
            return new ResponseEntity<>(new Error("No group with id : " + id,"ID_ALREADY_USE"), HttpStatus.NOT_FOUND);
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


    private List<GroupJson> convert(List<GroupEntity> groupEntities){
        List<GroupJson> converted = new ArrayList<>();
        for(GroupEntity entity : groupEntities){
            converted.add(new GroupJson(entity));
        }
        return converted;
    }
}
