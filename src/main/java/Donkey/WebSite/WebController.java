package Donkey.WebSite;

import Donkey.Database.Entity.TemporalRegister;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebController {

    @RequestMapping(value = {"/register"}, method = RequestMethod.GET)
    public String register(Model model, @ModelAttribute TemporalRegister firstRegistration ){
        //TODO

        return "register";
    }
}
