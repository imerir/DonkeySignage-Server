package Donkey.WebSite;


import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.Database.Repository.RoleRepository;
import Donkey.Database.Repository.UserRepository;
import Donkey.WebSite.FormClass.User.AddUserForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserWebController {

    private Logger log = LogManager.getLogger();
    private final RoleRepository roleRep;
    private final UserRepository userRepository;

    @Autowired
    public UserWebController(RoleRepository roleRep, UserRepository userRepository) {
        this.roleRep = roleRep;
        this.userRepository = userRepository;
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/addUser", method = RequestMethod.GET)
    public String createUser(Model model){
        model.addAttribute("addUserForm", new AddUserForm());
        model.addAttribute("roleList", roleRep.getAllBy());
        return "User/addUser";
    }


    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String showUser(Model model, @RequestParam(name = "id", defaultValue = "-1") int id){
        if(id == -1){
            model.addAttribute("userList",userRepository.getAllBy());
        }else{
            model.addAttribute("displayUser",userRepository.getUserEntityById(id));
            model.addAttribute("roleList", roleRep.getAllBy());
        }
        return "User/user";
    }
}
