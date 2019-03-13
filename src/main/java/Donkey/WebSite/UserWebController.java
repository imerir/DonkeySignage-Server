package Donkey.WebSite;


import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.Database.Repository.RoleRepository;
import Donkey.WebSite.FormClass.User.AddUserForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserWebController {

    private Logger log = LogManager.getLogger();
    private final RoleRepository roleRep;

    @Autowired
    public UserWebController(RoleRepository roleRep) {
        this.roleRep = roleRep;
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String createUser(Model model){
        model.addAttribute("addUserForm", new AddUserForm());
        model.addAttribute("roleList", roleRep.getAllBy());
        return "User/addUser";
    }
}
