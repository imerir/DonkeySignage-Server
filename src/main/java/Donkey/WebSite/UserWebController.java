package Donkey.WebSite;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserWebController {


    @RequestMapping("/login")
    public String login(){
        return "login";
    }
}
