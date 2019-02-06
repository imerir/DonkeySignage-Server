package Donkey.WebSite;

import Donkey.Database.Repository.ScreenRegisterRepository;
import Donkey.Database.Repository.TemporalRegisterRepository;
import Donkey.WebSite.FormClass.TmpTokenForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {
    private final ScreenRegisterRepository screenRegisterRepository;
    private final TemporalRegisterRepository tmpRegisterRep;

    private Logger log = LogManager.getLogger();

    public WebController(ScreenRegisterRepository screenRegisterRepository, TemporalRegisterRepository tmpRegisterRep){
        this.screenRegisterRepository = screenRegisterRepository;
        this.tmpRegisterRep = tmpRegisterRep;
    }

    @RequestMapping(value = {"/screenRegister"}, method = RequestMethod.GET)
    public String registerScreen(Model model, @RequestParam(value = "tmpToken", defaultValue = "")String tmpToken) {
        //TODO
        if (!tmpToken.isEmpty()) {
            return "formScreenRegister";
        } else {
            TmpTokenForm form = new TmpTokenForm();
            model.addAttribute("tmpTokenForm", form);
            return "screenRegister";
        }
    }
}
