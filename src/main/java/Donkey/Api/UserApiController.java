package Donkey.Api;

import Donkey.Api.JSON.Error;
import Donkey.Api.JSON.User.ModifyMyAccountJson;
import Donkey.Api.JSON.User.UserJson;
import Donkey.Database.Entity.UserAndPrivileges.RolesEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.Database.Repository.RoleRepository;
import Donkey.Database.Repository.UserRepository;
import Donkey.Tools.ScreenTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/")
public class UserApiController {

    private Logger logger = LogManager.getLogger();

    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;


    @Autowired
    public UserApiController(UserRepository userRepository, PasswordEncoder encoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
    }


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public ResponseEntity<?> createUser(@RequestBody UserJson userJson){
        logger.info("[api/user POST] Username " + userJson.username);
        logger.info("[api/user POST] Pass " + userJson.password);

        if(userJson.username == null || userJson.password == null || userJson.roleId == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(userRepository.getUserEntityByUsername(userJson.username) != null){
            return new ResponseEntity<>(new Error("Username already exist", "USER_EXIST"), HttpStatus.CONFLICT);
        }
        RolesEntity role = roleRepository.getRolesEntityById(userJson.roleId);
        if(role == null){
            return new ResponseEntity<>(new Error("Role not found", "ROLE_404"), HttpStatus.NOT_FOUND);
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userJson.username);
        userEntity.setRoles(Collections.singletonList(role));
        userEntity.setPassword(encoder.encode(userJson.password));

        userEntity = userRepository.save(userEntity);
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }


    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@RequestParam(name = "id", defaultValue = "-1") int id, Authentication authentication){
        UserEntity loggedUser = (UserEntity) authentication.getPrincipal();
        if(id == -1 && loggedUser.isAdmin()){
            List<UserEntity> users = userRepository.getAllBy();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }

        if(loggedUser.getId() == id || id == -1)
            return new ResponseEntity<>(loggedUser, HttpStatus.OK);

        if(!loggedUser.isAdmin()){
            logger.info("[api/user GET] " + loggedUser.getUsername() + " access denied");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        UserEntity user = userRepository.getUserEntityById(id);
        if(user == null){
            logger.info("[api/user GET] User not found " + id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(user, HttpStatus.OK);

    }


    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/user", method = RequestMethod.DELETE)
    public ResponseEntity<?> delUser(@RequestParam(name = "id") int id){
        userRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping (value = {"/user"}, method = RequestMethod.PUT)
    public ResponseEntity<?> modifyUser (@RequestBody UserJson userJson, @RequestParam (name = "id") int id, Authentication authentication){
        UserEntity loggedUser = (UserEntity) authentication.getPrincipal();
        if(loggedUser.isAdmin() || loggedUser.getId() == id){
            UserEntity user = userRepository.getUserEntityById(id);
            if(user != null){
                if(userJson.username == null || userJson.username.isEmpty() ||
                        (userRepository.getUserEntityByUsername(userJson.username) != null && (userRepository.getUserEntityById(id).getUsername().compareTo(userJson.username) != 0))){
                    logger.debug("[api/user PUT] username already use or empty or null");
                    return new ResponseEntity<>(new Error("username already use or empty or null","USER_CONFLICT"),HttpStatus.CONFLICT);
                }else{
                    user.setUsername(userJson.username);
                    userRepository.save(user);
                    return new ResponseEntity<>(user, HttpStatus.OK);
                }
            }else{
                logger.info("[api/user PUT] User with id : " + id + " not found");
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }else{
            logger.info("[api/user PUT]" + loggedUser.getUsername() + " access denied !");
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.PUT)
    public ResponseEntity<?> resetPassword (@RequestParam(name = "id") int id){
        UserEntity usrNeedNewPwd = userRepository.getUserEntityById(id);
        if(usrNeedNewPwd == null){
            return new ResponseEntity<>(new Error("No user with this id","USER_NOT_FOUND"),HttpStatus.NOT_FOUND);
        }else{
            String newPassword = ScreenTools.getInstance().generateUuid().substring(0,8);
            usrNeedNewPwd.setPassword(encoder.encode(newPassword));
            userRepository.save(usrNeedNewPwd);
            return new ResponseEntity<>(newPassword,HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/modifyMyAccount", method = RequestMethod.PUT)
    public ResponseEntity<?> modifyMyAccount(@RequestBody ModifyMyAccountJson userJson, Authentication authentication){
        UserEntity loggedUser = (UserEntity) authentication.getPrincipal();
        if(!encoder.matches(userJson.currentPassword,loggedUser.getPassword())){
            logger.info("[api/modifyMyAccount PUT] wrong password access denied !");
            return new ResponseEntity<>(new Error("wrong password","WRONG_PASSWORD"),HttpStatus.UNAUTHORIZED);
        }else{
//            if(!loggedUser.getUsername().equals(userJson.username) &&
//                    userRepository.getUserEntityByUsername(userJson.username) == null &&
//                    userJson.newPassword.isEmpty())
//                loggedUser.setUsername(userJson.username);
//            else{
//                logger.debug("[api/user PUT] username already use");
//                return new ResponseEntity<>(new Error("username already use or empty or null","USER_CONFLICT"),HttpStatus.CONFLICT);
//            }
//            if(!encoder.matches(userJson.newPassword,loggedUser.getPassword()))
//                loggedUser.setPassword(encoder.encode(userJson.newPassword));

            if(userJson.newPassword.isEmpty()){
//                modifcation username
                if(!loggedUser.getUsername().equals(userJson.username) &&
                        userRepository.getUserEntityByUsername(userJson.username) == null )
                    loggedUser.setUsername(userJson.username);
                else{
                    logger.debug("[api/user PUT] username already use");
                    return new ResponseEntity<>(new Error("username already use or empty or null","USER_CONFLICT"),HttpStatus.CONFLICT);
                }
            }else
                loggedUser.setPassword(encoder.encode(userJson.newPassword));

            loggedUser = userRepository.save(loggedUser);
            return new ResponseEntity<>(loggedUser, HttpStatus.OK);
        }
    }
}
