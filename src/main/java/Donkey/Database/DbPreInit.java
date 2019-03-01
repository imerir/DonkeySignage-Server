package Donkey.Database;

import Donkey.Database.Entity.UserAndPrivileges.RolesEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.Database.Repository.RoleRepository;
import Donkey.Database.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Component
public class DbPreInit {
    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Autowired
    public DbPreInit(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder encoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @PostConstruct
    public void init(){
        RolesEntity admin = roleRepository.getRolesEntityByName("ROLE_ADMIN");
        if( admin == null)
            admin = roleRepository.save(new RolesEntity("ROLE_ADMIN"));

        if(roleRepository.getRolesEntityByName("ROLE_USER") == null)
            roleRepository.save(new RolesEntity("ROLE_USER"));

        if(userRepository.getUserEntityByUsername("admin") == null){
            UserEntity user = new UserEntity();
            user.setUsername("admin");
            user.setPassword(encoder.encode("donkey"));
            user = userRepository.save(user);
            List<RolesEntity> temp = new ArrayList<>();
            temp.add(admin);
            user.setRoles(temp);
            userRepository.save(user);
        }
    }
}
