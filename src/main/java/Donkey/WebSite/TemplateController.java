package Donkey.WebSite;

import Donkey.Api.JSON.Template.AddTemplateJson;
import Donkey.Database.Entity.TemplateEntity;
import Donkey.Database.Repository.TemplateRepository;
import Donkey.Database.Repository.WidgetConfigRepository;
import Donkey.Storage.Service.StorageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.stream.Collectors;

@Controller
public class TemplateController {

    private Logger log = LogManager.getLogger();
    private final StorageService storageService;
    private final TemplateRepository templateRepository;
    private final WidgetConfigRepository widgetConfigRepository;

    @Autowired
    public TemplateController(StorageService storageService, TemplateRepository templateRepository, WidgetConfigRepository widgetConfigRepository){
        this.storageService = storageService;
        this.templateRepository = templateRepository;
        this.widgetConfigRepository = widgetConfigRepository;
    }

    @RequestMapping(value = "/media" ,method = RequestMethod.GET)
    public String getFileMedia(Model model, Authentication authentication){
        model.addAttribute("files", storageService.loadAll().map(
                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                        "serveFile", path.getFileName().toString()).build().toString())
                .collect(Collectors.toList()));
        model.addAttribute("template", templateRepository.getAllBy());
        return "Media/media";
    }

    @RequestMapping(value = "/addTemplate",method = RequestMethod.GET)
    public String addTemplate(Model model, Authentication authentication){
        model.addAttribute("addTemplateForm", new AddTemplateJson());
        return "Media/addTemplate";
    }

    @RequestMapping(value="/template", method = RequestMethod.GET)
    public String getTemplate (Model model, @RequestParam(name = "id")int id ){
        TemplateEntity template = templateRepository.getById(id);
        if(template == null){
            throw new ErrorCode.ResourceNotFoundException();
        }else{
            model.addAttribute("template",template);
            model.addAttribute("widgetConfList", widgetConfigRepository.getAllByTemplateId(id));
            return "Media/template";
        }
    }
}
