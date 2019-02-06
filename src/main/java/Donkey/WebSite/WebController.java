package Donkey.WebSite;

import Donkey.Database.Entity.ScreenRegister;
import Donkey.Database.Entity.TemporalRegister;
import Donkey.Database.Repository.ScreenRegisterRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class WebController {
    private final ScreenRegisterRepository screenRegisterRepository;

    public WebController(ScreenRegisterRepository screenRegisterRepository){
        this.screenRegisterRepository = screenRegisterRepository;
    }

    @PostMapping(value = {"/formScreenRegister"})
    public String registerScreen(Model model, @ModelAttribute ScreenRegister finalRegistrationForm){
        //TODO
        ScreenRegister finalRegistration = new ScreenRegister();
        return "register";
    }
}
