package Donkey.WebSite;


import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.WebSite.FormClass.User.AddUserForm;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class UserWebController {


    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String createUser(Model model, Authentication authentication){
        UserEntity user = (UserEntity) authentication.getPrincipal();
        model.addAttribute("user",user);
        model.addAttribute("addUserForm", new AddUserForm());
        return "User/addUser";
    }
}
