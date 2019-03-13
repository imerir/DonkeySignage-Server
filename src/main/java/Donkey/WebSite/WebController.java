package Donkey.WebSite;

import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class WebController {

    private Logger log = LogManager.getLogger();

    public WebController(){

    }

    @RequestMapping(value ="/",method = RequestMethod.GET)
    public String index(Model model){
        return "index";
    }
}
