package Donkey.Api;


import Donkey.Api.JSON.Error;
import Donkey.Database.Entity.UserAndPrivileges.RolesEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.Database.Repository.RoleRepository;
import Donkey.Database.Repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<?> createUser(@RequestBody UserEntity userEntity){
        logger.info("[api/user GET] Username " + userEntity.getUsername());
        logger.info("[api/user GET] Pass " + userEntity.getPassword());
        if(userEntity.getUsername() == null || userEntity.getPassword() == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if(userRepository.getUserEntityByUsername(userEntity.getUsername()) != null){
            return new ResponseEntity<>(new Error("Username already exist", "USER_EXIST"), HttpStatus.CONFLICT);
        }

        RolesEntity userRole = roleRepository.getRolesEntityByName("ROLE_USER");
        userEntity.setRoles(Collections.singletonList(userRole));
        userEntity = userRepository.save(userEntity);
        return new ResponseEntity<>(userEntity, HttpStatus.OK);
    }


    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@RequestParam(name = "id", defaultValue = "-1") int id){

        if(id == -1){
            List<UserEntity> users = userRepository.getAllBy();
            return new ResponseEntity<>(users, HttpStatus.OK);
        }
        UserEntity user = userRepository.getUserEntityById(id);
        if(user == null){
            logger.info("[api/user GET] User not found");
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

    //TODO PUT editUser

}
