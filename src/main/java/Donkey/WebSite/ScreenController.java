package Donkey.WebSite;

import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Entity.TemporalScreenEntity;
import Donkey.Database.Repository.GroupRepository;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.Database.Repository.TemplateRepository;
import Donkey.Database.Repository.TemporalScreenRepository;
import Donkey.Tools.ScreenTools;
import Donkey.WebSite.FormClass.Screen.ScreenRegisterForm;
import Donkey.WebSite.FormClass.Screen.TmpTokenForm;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ScreenController {

    private final ScreenRepository screenRegRep;
    private final TemplateRepository templateRepository;
    private final TemporalScreenRepository tmpRegisterRep;
    private final GroupRepository grpRep;
    private Logger log = LogManager.getLogger();

    @Autowired
    public ScreenController(ScreenRepository screenRegRep, TemplateRepository templateRepository, TemporalScreenRepository tmpRegisterRep, GroupRepository grpRep) {
        this.screenRegRep = screenRegRep;
        this.templateRepository = templateRepository;
        this.tmpRegisterRep = tmpRegisterRep;
        this.grpRep = grpRep;
    }

    /**
     * Display screenRegister.html for gettigng temporary token of the screen
     * @param model
     * @param uuid
     * @return String
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/screenRegister"}, method = RequestMethod.GET)
    public String registerScreenGet(Model model, @RequestParam(value = "uuid", defaultValue = "") String uuid) {
        TemporalScreenEntity tmpReg = tmpRegisterRep.getTemporalRegisterByUuid(uuid);
        if (!uuid.isEmpty() && tmpReg!= null) {
            model.addAttribute("uuid",uuid);
            model.addAttribute("screenRegisterForm",new ScreenRegisterForm());

            return "Screen/formScreenRegister";
        } else {
            TmpTokenForm form = new TmpTokenForm();
            model.addAttribute("tmpTokenForm", form);
            return "Screen/screenRegister";
        }
    }

    /**
     *  Use temporary token for getting uuid of screen, and redirect on formScreenRegister.
     * @param model
     * @param tokenForm
     * @return String
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/screenRegister", method = RequestMethod.POST)
    public String registerScreenPost(Model model, @ModelAttribute TmpTokenForm tokenForm) {
        TemporalScreenEntity tmpReg = tmpRegisterRep.getTemporalRegisterByTempToken(tokenForm.getTempToken());
        log.debug("Post screenRegister, token : " + tokenForm.getTempToken());
        if(!tokenForm.getTempToken().isEmpty() && tmpReg != null){
            return "redirect:/screenRegister?uuid="+tmpReg.getUuid();
        }else{
            model.addAttribute("error", true);
            return "Screen/screenRegister";
        }
    }

    /**
     * Display formScreenRegister, and adding this in database.
     * @param model
     * @param screenRegisterForm
     * @return String
     */
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = {"/formScreenRegister"}, method = RequestMethod.POST)
    public String formScreenRegister(Model model, @ModelAttribute ScreenRegisterForm screenRegisterForm) {
        if(screenRegisterForm.getUuid() != null && !screenRegisterForm.getUuid().isEmpty()){
            ScreenEntity newEntry = new ScreenEntity();
            if(screenRegRep.getScreenRegisterByUuid(screenRegisterForm.getUuid()) == null){
                model.addAttribute("screenRegisterForm",screenRegisterForm);
                TemporalScreenEntity tmpReg = tmpRegisterRep.getTemporalRegisterByUuid(screenRegisterForm.getUuid());
                newEntry.setIp(tmpReg.getIp());
                newEntry.setUuid(tmpReg.getUuid());
                newEntry.setName(screenRegisterForm.getName());
                newEntry.setToken(ScreenTools.getInstance().generateUuid());
                newEntry.setGroup(grpRep.getGroupEntityById(screenRegisterForm.getGroupId()));
            }else{
                ScreenEntity tmpScreen = screenRegRep.getScreenRegisterByUuid(screenRegisterForm.getUuid());
                newEntry.setName(tmpScreen.getName());
                newEntry.setUuid(tmpScreen.getUuid());
                newEntry.setIp(tmpScreen.getIp());
                newEntry.setToken(ScreenTools.getInstance().generateUuid());
                newEntry.setGroup(grpRep.getGroupEntityById(screenRegisterForm.getGroupId()));
            }
            screenRegRep.save(newEntry);
            tmpRegisterRep.delete(tmpRegisterRep.getTemporalRegisterByUuid(screenRegisterForm.getUuid()));
            return "redirect:/screen?id="+newEntry.getId();
        }else{
            //TODO replace dat
            throw new ErrorCode.ResourceNotFoundException();
        }
    }

    /**
     * Show a screen, with some information
     * @param model
     * @param id
     * @return
     */
    @PreAuthorize("hasPermission(#id,'screen', 'read')")
    @RequestMapping(value="/screen", method = RequestMethod.GET)
    public String getScreen(Model model, @RequestParam(name = "id") int id) {

        //TODO Can edit ?
        if(screenRegRep.getScreenEntityById(id) != null){

            ScreenEntity theScreen =  screenRegRep.getScreenEntityById(id);
            model.addAttribute("screen",theScreen);
            model.addAttribute("templateList",templateRepository.getAllBy());
            return "Screen/screen";
        }else{
            throw new ErrorCode.ResourceNotFoundException();
        }
    }




}
