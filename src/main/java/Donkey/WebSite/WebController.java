package Donkey.WebSite;

import Donkey.Database.Entity.ScreenRegister;
import Donkey.Database.Entity.TemporalRegister;
import Donkey.Database.Repository.ScreenRegisterRepository;
import Donkey.Database.Repository.TemporalRegisterRepository;
import Donkey.Tools.UserTools;
import Donkey.WebSite.FormClass.ScreenRegisterForm;
import Donkey.WebSite.FormClass.TmpTokenForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class WebController {
    private final ScreenRegisterRepository screenRegRep;
    private final TemporalRegisterRepository tmpRegisterRep;

    private Logger log = LogManager.getLogger();

    public WebController(ScreenRegisterRepository screenRegRep, TemporalRegisterRepository tmpRegisterRep){
        this.screenRegRep = screenRegRep;
        this.tmpRegisterRep = tmpRegisterRep;
    }

    @RequestMapping(value = {"/screenRegister"}, method = RequestMethod.GET)
    public String registerScreenGet(Model model, @RequestParam(value = "uuid", defaultValue = "")String uuid) {
        TemporalRegister tmpReg = tmpRegisterRep.getTemporalRegisterByUuid(uuid);
        if (!uuid.isEmpty() && tmpReg!= null) {
            model.addAttribute("uuid",uuid);
            model.addAttribute("screenRegisterForm",new ScreenRegisterForm());
            return "formScreenRegister";
        } else {
            TmpTokenForm form = new TmpTokenForm();
            model.addAttribute("tmpTokenForm", form);
            return "screenRegister";
        }
    }

    @PostMapping(value = {"/screenRegister"})
    public String registerScreenPost(Model model, @ModelAttribute TmpTokenForm tokenForm){
        TemporalRegister tmpReg = tmpRegisterRep.getTemporalRegisterByTempToken(tokenForm.getTempToken());
        log.debug("Post screenRegister, token : " + tokenForm.getTempToken());
        if(!tokenForm.getTempToken().isEmpty() && tmpReg != null){
            return "redirect:/screenRegister?uuid="+tmpReg.getUuid();
        }else{
            //TODO
            //Gestion erreur
            return "Error";
        }
    }

    @RequestMapping(value = {"/formScreenRegister"}, method = RequestMethod.POST)
    public String formScreenRegister(Model model, @ModelAttribute ScreenRegisterForm screenRegisterForm){
        if(screenRegisterForm.getUuid() != null && !screenRegisterForm.getUuid().isEmpty()){
            ScreenRegister newEntry = new ScreenRegister();
            if(screenRegRep.getScreenRegisterByUuid(screenRegisterForm.getUuid()) == null){
                model.addAttribute("screenRegisterForm",screenRegisterForm);
                TemporalRegister tmpReg = tmpRegisterRep.getTemporalRegisterByUuid(screenRegisterForm.getUuid());
                newEntry.setIp(tmpReg.getIp());
                newEntry.setUuid(tmpReg.getUuid());
                newEntry.setName(screenRegisterForm.getName());
                newEntry.setToken(UserTools.getInstance().generateCheckToken(false));
            }else{
                ScreenRegister tmpScreen = screenRegRep.getScreenRegisterByUuid(screenRegisterForm.getUuid());
                newEntry.setName(tmpScreen.getName());
                newEntry.setUuid(tmpScreen.getUuid());
                newEntry.setIp(tmpScreen.getIp());
                newEntry.setToken(UserTools.getInstance().generateCheckToken(false));
            }
            screenRegRep.save(newEntry);
            tmpRegisterRep.delete(tmpRegisterRep.getTemporalRegisterByUuid(screenRegisterForm.getUuid()));
            return "index";
        }else{
            return "Error";
        }

    }
}
