package Donkey.Api;

import Donkey.Api.JSON.UserScreenPrivilege.UserScreenPrivilegeJson;
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

    /**
     * Get all privilege of screen
     * @param usrScreenPrivilegeJson
     * @return ResponseEntity
     */
    @RequestMapping(value = "" , method = RequestMethod.GET)
    public ResponseEntity<?> getPrivileges (@RequestBody UserScreenPrivilegeJson usrScreenPrivilegeJson){
        if(usrScreenPrivilegeJson.screenId != null && usrScreenPrivilegeJson.userId != null){
            if(userRepository.getUserEntityById(usrScreenPrivilegeJson.userId) != null && screenRepository.getScreenEntityById(usrScreenPrivilegeJson.screenId) != null){
                return new ResponseEntity<>(userScreenPrivilegeRepository.getByUserEntityAndScreenEntity(userRepository.getUserEntityById(usrScreenPrivilegeJson.userId),screenRepository.getScreenEntityById(usrScreenPrivilegeJson.screenId)), HttpStatus.OK);
            }else{
                log.info("[/api/UserScreenPrivilege GET] No screen with id : " + usrScreenPrivilegeJson.screenId + ", or no user with id : " + usrScreenPrivilegeJson.userId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else if(usrScreenPrivilegeJson.screenId != null && usrScreenPrivilegeJson.userId == null){
            if(screenRepository.getScreenEntityById(usrScreenPrivilegeJson.screenId) != null){
                return new ResponseEntity<>(userScreenPrivilegeRepository.getByScreenEntity(screenRepository.getScreenEntityById(usrScreenPrivilegeJson.screenId)),HttpStatus.OK);
            }else{
                log.info("[/api/UserScreenPrivilege GET] No screen with id : " + usrScreenPrivilegeJson.screenId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else if(usrScreenPrivilegeJson.screenId == null && usrScreenPrivilegeJson.userId != null){
            if(userRepository.getUserEntityById(usrScreenPrivilegeJson.userId) != null){
                return new ResponseEntity<>(userScreenPrivilegeRepository.getByUserEntity(userRepository.getUserEntityById(usrScreenPrivilegeJson.userId)),HttpStatus.OK);
            }else{
                log.info("[/api/UserScreenPrivilege GET] No user with id : " + usrScreenPrivilegeJson.userId);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else{
            log.info("[/api/UserScreenPrivilege GET] screen id -1 and user id = -1");
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete a user screen privilege
     * @param usrScreenPrivilegeJson
     * @return
     */
    @RequestMapping(value = "" , method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteUserPrivilegeOnScreen (@RequestBody UserScreenPrivilegeJson usrScreenPrivilegeJson){
        if(usrScreenPrivilegeJson.screenId != null && usrScreenPrivilegeJson.userId != null
                && screenRepository.getScreenEntityById(usrScreenPrivilegeJson.screenId) != null
                && userRepository.getUserEntityById(usrScreenPrivilegeJson.userId) != null){
            userScreenPrivilegeRepository.deleteUserScreenPrivilegeByUserEntityAndScreenEntityAndPrivilege(userRepository.getUserEntityById(usrScreenPrivilegeJson.userId),screenRepository.getScreenEntityById(usrScreenPrivilegeJson.screenId),usrScreenPrivilegeJson.privilege);
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            log.info("[/api/UserScreenPrivilege PUT] no user with id : " + usrScreenPrivilegeJson.userId + ", or no screen with id : " + usrScreenPrivilegeJson.screenId);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    /**
     * Modify a user screen privilege
     * @param usrScreenPrivilegeJson
     * @return
     */
    @RequestMapping(value = "" , method = RequestMethod.POST)
    public ResponseEntity<?> addUserPriviligeOnScreen (@RequestBody UserScreenPrivilegeJson usrScreenPrivilegeJson){
        if(usrScreenPrivilegeJson.screenId != null && usrScreenPrivilegeJson.userId != null
                && screenRepository.getScreenEntityById(usrScreenPrivilegeJson.screenId) != null
                && userRepository.getUserEntityById(usrScreenPrivilegeJson.userId) != null){
            log.info("[/api/UserScreenPrivilege PUT] user with id : " + usrScreenPrivilegeJson.userId + ", screen with id : " + usrScreenPrivilegeJson.screenId + " privilege add : " + usrScreenPrivilegeJson.privilege);
            log.info("[/api/UserScreenPrivilege PUT] user entity " + userRepository.getUserEntityById(usrScreenPrivilegeJson.userId) + ", screen entity: " + screenRepository.getScreenEntityById(usrScreenPrivilegeJson.screenId));
            UserScreenPrivilege newPrivilege = new UserScreenPrivilege();
            UserEntity user = userRepository.getUserEntityById(usrScreenPrivilegeJson.userId);
            ScreenEntity screen = screenRepository.getScreenEntityById(usrScreenPrivilegeJson.screenId);
            newPrivilege.setScreenEntity(screen);
            newPrivilege.setUserEntity(user);
            newPrivilege.setPrivilege(usrScreenPrivilegeJson.privilege);
            user.getScreenPrivileges().add(newPrivilege);
            userRepository.save(user);
            return new ResponseEntity<>(newPrivilege,HttpStatus.OK);
        }else{
            log.info("[/api/UserScreenPrivilege PUT] no user with id : " + usrScreenPrivilegeJson.userId + ", or no screen with id : " + usrScreenPrivilegeJson.screenId);
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }
}
