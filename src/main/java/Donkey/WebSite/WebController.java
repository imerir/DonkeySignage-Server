package Donkey.WebSite;

import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Entity.TemporalScreenEntity;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.Database.Repository.TemporalScreenRepository;
import Donkey.Tools.ScreenTools;
import Donkey.WebSite.FormClass.ScreenRegisterForm;
import Donkey.WebSite.FormClass.TmpTokenForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {
    private final ScreenRepository screenRegRep;
    private final TemporalScreenRepository tmpRegisterRep;

    private Logger log = LogManager.getLogger();

    public WebController(ScreenRepository screenRegRep, TemporalScreenRepository tmpRegisterRep){
        this.screenRegRep = screenRegRep;
        this.tmpRegisterRep = tmpRegisterRep;
    }

    /**
     * Display screenRegister.html for gettigng temporary token of the screen
     * @param model
     * @param uuid
     * @return String
     */
    @RequestMapping(value = {"/screenRegister"}, method = RequestMethod.GET)
    public String registerScreenGet(Model model, @RequestParam(value = "uuid", defaultValue = "")String uuid) {
        TemporalScreenEntity tmpReg = tmpRegisterRep.getTemporalRegisterByUuid(uuid);
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

    /**
     *  Use temporary token for getting uuid of screen, and redirect on formScreenRegister.
     * @param model
     * @param tokenForm
     * @return String
     */
    @PostMapping(value = {"/screenRegister"})
    public String registerScreenPost(Model model, @ModelAttribute TmpTokenForm tokenForm){
        TemporalScreenEntity tmpReg = tmpRegisterRep.getTemporalRegisterByTempToken(tokenForm.getTempToken());
        log.debug("Post screenRegister, token : " + tokenForm.getTempToken());
        if(!tokenForm.getTempToken().isEmpty() && tmpReg != null){
            return "redirect:/screenRegister?uuid="+tmpReg.getUuid();
        }else{
            //TODO
            //Gestion erreur
            return "Error";
        }
    }

    /**
     * Display formScreenRegister, and adding this in database.
     * @param model
     * @param screenRegisterForm
     * @return String
     */
    @RequestMapping(value = {"/formScreenRegister"}, method = RequestMethod.POST)
    public String formScreenRegister(Model model, @ModelAttribute ScreenRegisterForm screenRegisterForm){
        if(screenRegisterForm.getUuid() != null && !screenRegisterForm.getUuid().isEmpty()){
            ScreenEntity newEntry = new ScreenEntity();
            if(screenRegRep.getScreenRegisterByUuid(screenRegisterForm.getUuid()) == null){
                model.addAttribute("screenRegisterForm",screenRegisterForm);
                TemporalScreenEntity tmpReg = tmpRegisterRep.getTemporalRegisterByUuid(screenRegisterForm.getUuid());
                newEntry.setIp(tmpReg.getIp());
                newEntry.setUuid(tmpReg.getUuid());
                newEntry.setName(screenRegisterForm.getName());
                newEntry.setToken(ScreenTools.getInstance().generateUuid());
            }else{
                ScreenEntity tmpScreen = screenRegRep.getScreenRegisterByUuid(screenRegisterForm.getUuid());
                newEntry.setName(tmpScreen.getName());
                newEntry.setUuid(tmpScreen.getUuid());
                newEntry.setIp(tmpScreen.getIp());
                newEntry.setToken(ScreenTools.getInstance().generateUuid());
            }
            screenRegRep.save(newEntry);
            tmpRegisterRep.delete(tmpRegisterRep.getTemporalRegisterByUuid(screenRegisterForm.getUuid()));
            return "index";
        }else{
            return "Error";
        }

    }
}
