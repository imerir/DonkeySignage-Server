package Donkey.Api;

import Donkey.Api.JSON.AddUserScreenPrivilegeJson;
import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserScreenPrivilege;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.Database.Repository.UserRepository;
import Donkey.Database.Repository.UserScreenPrivilegeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api/UserScreenPrivilege")
public class UserScreenPrivilegeApiController {
    private Logger log = LogManager.getLogger();
    private final ScreenRepository screenRepository;
    private final UserRepository userRepository;
    private final UserScreenPrivilegeRepository userScreenPrivilegeRepository;

    @Autowired
    public UserScreenPrivilegeApiController(ScreenRepository screenRepository, UserRepository userRepository, UserScreenPrivilegeRepository userScreenPrivilegeRepository){
        this.screenRepository = screenRepository;
        this.userRepository = userRepository;
        this.userScreenPrivilegeRepository = userScreenPrivilegeRepository;
    }

    @RequestMapping(value = "" , method = RequestMethod.GET)
    public ResponseEntity<?> getPrivileges (@RequestParam(name="screenId", defaultValue = "-1") int screenId,
                                                    @RequestParam(name="userId", defaultValue = "-1") int userId){
        if(screenId != -1 && userId != -1){
            if(userRepository.getUserEntityById(userId) != null && screenRepository.getScreenEntityById(screenId) != null){
                return new ResponseEntity<>(userScreenPrivilegeRepository.getByUserEntityAndScreenEntity(userRepository.getUserEntityById(userId),screenRepository.getScreenEntityById(screenId)), HttpStatus.OK);
            }else{
                log.info("[/api/UserScreenPrivilege GET] No screen with id : " + screenId + ", or no user with id : " + userId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else if(screenId != -1 && userId == -1){
            if(screenRepository.getScreenEntityById(screenId) != null){
                return new ResponseEntity<>(userScreenPrivilegeRepository.getByScreenEntity(screenRepository.getScreenEntityById(screenId)),HttpStatus.OK);
            }else{
                log.info("[/api/UserScreenPrivilege GET] No screen with id : " + screenId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else if(screenId == -1 && userId != -1){
            if(userRepository.getUserEntityById(userId) != null){
                return new ResponseEntity<>(userScreenPrivilegeRepository.getByUserEntity(userRepository.getUserEntityById(userId)),HttpStatus.OK);
            }else{
                log.info("[/api/UserScreenPrivilege GET] No user with id : " + userId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else{
            log.info("[/api/UserScreenPrivilege GET] screen id -1 and user id = -1");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "" , method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUserPrivilegeOnScreen (@RequestParam(name="screenId", defaultValue = "-1") int screenId,
                                                  @RequestParam(name="userId", defaultValue = "-1") int userId){
        if(screenId != -1 && userId != -1
                && screenRepository.getScreenEntityById(screenId) != null
                && userRepository.getUserEntityById(userId) != null){
            userScreenPrivilegeRepository.deleteUserScreenPrivilegeByUserEntityAndScreenEntity(userRepository.getUserEntityById(userId),screenRepository.getScreenEntityById(screenId));
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            log.info("[/api/UserScreenPrivilege PUT] no user with id : " + userId + ", or no screen with id : " + screenId);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "" , method = RequestMethod.POST)
    public ResponseEntity<?> addUserPriviligeOnScreen (@RequestParam(name="screenId", defaultValue = "-1") int screenId,
                                                       @RequestParam(name="userId", defaultValue = "-1") int userId,
                                                       @RequestBody AddUserScreenPrivilegeJson addUserScreenPrivilegeJson){
        if(screenId != -1 && userId != -1
                && screenRepository.getScreenEntityById(screenId) != null
                && userRepository.getUserEntityById(userId) != null){
            log.info("[/api/UserScreenPrivilege PUT] user with id : " + userId + ", screen with id : " + screenId + " privilege add : " + addUserScreenPrivilegeJson.privilege);
            log.info("[/api/UserScreenPrivilege PUT] user entity " + userRepository.getUserEntityById(userId) + ", screen entity: " + screenRepository.getScreenEntityById(screenId));
            UserScreenPrivilege newPrivilege = new UserScreenPrivilege();
            UserEntity user = userRepository.getUserEntityById(userId);
            ScreenEntity screen = screenRepository.getScreenEntityById(screenId);
            newPrivilege.setScreenEntity(screen);
            newPrivilege.setUserEntity(user);
            newPrivilege.setPrivilege(addUserScreenPrivilegeJson.privilege);
            user.getScreenPrivileges().add(newPrivilege);
            userRepository.save(user);
            return new ResponseEntity<>(newPrivilege,HttpStatus.OK);
        }else{
            log.info("[/api/UserScreenPrivilege PUT] no user with id : " + userId + ", or no screen with id : " + screenId);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
