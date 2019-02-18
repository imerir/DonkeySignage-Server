package Donkey.Api;


import Donkey.Api.JSON.Group.DeleteGroupJson;
import Donkey.Api.JSON.Group.GroupJson;
import Donkey.Api.JSON.Group.ModifyGroupJson;
import Donkey.Database.Entity.GroupEntity;
import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Repository.GroupRepository;
import Donkey.Database.Repository.ScreenRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;
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
     * @param deleteGroupJson
     * @return DeleteGroupJson
     */
    //TODO faire le test pour se retrouver avec un ecran sans groupe
    //TODO HTTP delete
    @DeleteMapping(value = {"/deleteGroup"})
    public ResponseEntity<?> deleteGroup(@RequestBody DeleteGroupJson deleteGroupJson) {
        GroupEntity grpToDelete = groupRepository.getGroupEntityById(deleteGroupJson.id);
        if(grpToDelete != null){
            if(grpToDelete.getParent() != null){
                if(grpToDelete.getScreenList().size() != 0){
                    //Deplacer les ecrans au groupe parent
                    GroupEntity newParentForScreen = groupRepository.getGroupEntityById(deleteGroupJson.parentId);
                    for(int i = 0 ; i < grpToDelete.getScreenList().size() ; i++){
                        ScreenEntity screenWithNewParent = grpToDelete.getScreenList().get(i);
                        screenWithNewParent.setGroup(newParentForScreen);
                        screenRepository.save(screenWithNewParent);
                    }
                }
                if(grpToDelete.getChildrens().size() != 0){
                    GroupEntity newParentForScreen = groupRepository.getGroupEntityById(deleteGroupJson.parentId);
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
                return new ResponseEntity<>(new DeleteGroupJson(grpToDelete.getId(),grpToDelete.getName(),-1),HttpStatus.OK);
            else
                return new ResponseEntity<>(new DeleteGroupJson(grpToDelete.getId(),grpToDelete.getName(),grpToDelete.getParent().getId()),HttpStatus.OK);
        }else{
            return new ResponseEntity<>(new Error("No Group with id : " + deleteGroupJson.id), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Modify a group with a json
     *
     * @param modifyGroupJson
     * @return GroupJson
     */
    @PutMapping(value = {"/modifyGroup"})
    //TODO HTTP put
    public ResponseEntity<?> modifyGroup(@RequestBody ModifyGroupJson modifyGroupJson) {
        GroupEntity groupNeedModification = groupRepository.getGroupEntityById(modifyGroupJson.id);
        if (groupNeedModification != null) {
            if (modifyGroupJson.parentId == -1) {
                if(modifyGroupJson.name != null && !modifyGroupJson.name.isEmpty())
                    groupNeedModification.setName(modifyGroupJson.name);
                groupNeedModification.setParent(null);
                groupRepository.save(groupNeedModification);
                return new ResponseEntity<>(new GroupJson(groupNeedModification.getName(), -1), HttpStatus.OK);
            } else {
                GroupEntity newGrpParent = groupRepository.getGroupEntityById(modifyGroupJson.parentId);
                if(modifyGroupJson.name != null && !modifyGroupJson.name.isEmpty())
                    groupNeedModification.setName(modifyGroupJson.name);
                groupNeedModification.setParent(newGrpParent);
                groupNeedModification = groupRepository.save(groupNeedModification);
                return new ResponseEntity<>(new GroupJson(groupNeedModification.getName(), -1), HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(new Error("No Group with id : " + modifyGroupJson.id), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Create and adding a group in database
     *
     * @param groupJson
     * @return ResponseEntity with GroupJson
     */
    @PostMapping(value = {"/addGroup"})
    public ResponseEntity<?> addGroup(@RequestBody GroupJson groupJson) {
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
            return new ResponseEntity<>(new Error("This Group is already create"), HttpStatus.CONFLICT);
        }
        groupRepository.save(newGroup);
        if (newGroup.getParent() == null)
            return new ResponseEntity<>(new GroupJson(newGroup.getName(), -1), HttpStatus.OK);
        else{
            GroupEntity groupNeedToChange = groupRepository.getGroupEntityById(groupJson.parent);
            groupNeedToChange.getChildrens().add(newGroup);
            groupRepository.save(newGroup);
            return new ResponseEntity<>(new GroupJson(newGroup.getName(), newGroup.getParent().getId()), HttpStatus.OK);
        }
    }

    /**
     * Send name and id of parent's group
     *
     * @param id
     * @return ResponseEntity with GroupJson
     */
    @RequestMapping(value = {"/getParent"}, method = RequestMethod.GET)
    public ResponseEntity<?>  getParent(@RequestParam(name = "id") int id) {
        if(groupRepository.getGroupEntityById(id) != null)
            return new ResponseEntity<>(new GroupJson(groupRepository.getGroupEntityById(id).getName(), groupRepository.getGroupEntityById(id).getId()), HttpStatus.OK);
        else
            return new ResponseEntity<>(new Error("No group with id : " + id), HttpStatus.NOT_FOUND);

    }

    /**
     * Send name and id of parent's children
     *
     * @param id
     * @return ResponseEntity with list of children
     */
    @RequestMapping(value = {"/getChildren"}, method = RequestMethod.GET)
    public ResponseEntity<?> getChildren(@RequestParam(name = "id") int id) {
        if(groupRepository.getGroupEntityById(id) != null)
            return new ResponseEntity<>(groupRepository.getGroupEntityById(id).getChildrens(), HttpStatus.OK);
        else
            return new ResponseEntity<>(new Error("No group with id : " + id), HttpStatus.NOT_FOUND);

    }

    /**
     * Send all screen of a group
     * @param id
     * @return ResponseEntity with List of screen
     */
    @RequestMapping(value={"/getScreen"},method = RequestMethod.GET)
    public ResponseEntity<?> getScreen(@RequestParam(name = "id") int id){
        if(groupRepository.getGroupEntityById(id) != null)
            return new ResponseEntity<>(groupRepository.getGroupEntityById(id).getScreenList(), HttpStatus.OK);
        else
            return new ResponseEntity<>(new Error("No group with id : " + id), HttpStatus.NOT_FOUND);
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
