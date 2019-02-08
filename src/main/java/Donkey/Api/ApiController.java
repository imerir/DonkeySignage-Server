package Donkey.Api;

import Donkey.Api.JSON.GroupJson;
import Donkey.Database.Entity.GroupEntity;
import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Repository.GroupRepository;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.Database.Repository.TemporalScreenRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    @PostMapping("/addGroup")
    public GroupEntity addGroup(@ModelAttribute GroupJson groupJson){
        GroupEntity newGroup = new GroupEntity();
//        if(groupJson.parent == -1){
//            Group
//        }else{
//
//        }
//        if(entryInDB.size() == 0){
//            //TODO
//            //new entry
//            newGroup.setName(groupJson.name);
//
//            if(groupJson.parent == -1)
//                newGroup.setParent(null);
//            else
//                newGroup.setParent(groupRepository.getGroupEntityById(groupJson.parent));
//
//            newGroup.getScreenList().clear();
//            newGroup.getChildrens().clear();
//        }else if (entryInDB.size() == 1){
//            //TODO
//            newGroup.setName(entryInDB.get(0).getName());
//        }else {
//            //TODO
//            //check if
//        }
//        groupRepository.save(newGroup);
        return newGroup;
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
