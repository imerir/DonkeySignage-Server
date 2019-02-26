package Donkey.Api;


import Donkey.Api.JSON.Error;
import Donkey.Database.Entity.TemplateEntity;
import Donkey.Database.Entity.WidgetConfigEntity;
import Donkey.Database.Repository.TemplateRepository;
import Donkey.Database.Repository.WidgetConfigRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class TemplateApiController {

    //TODO Javadoc

    private Logger logger = LogManager.getLogger();
    private final TemplateRepository templateRepository;
    private final WidgetConfigRepository widgetConfigRepository;

    @Autowired
    public TemplateApiController(TemplateRepository templateRepository, WidgetConfigRepository widgetConfigRepository) {
        this.templateRepository = templateRepository;
        this.widgetConfigRepository = widgetConfigRepository;
    }


    @RequestMapping(value = "template", method = RequestMethod.GET)
    public ResponseEntity<?> getTemplate(@RequestParam(name = "id", defaultValue = "-1") int id){
        if(id == -1){
            List<TemplateEntity> templates = templateRepository.getAllBy();
            return new ResponseEntity<>(templates, HttpStatus.OK);
        }
        TemplateEntity template = templateRepository.getById(id);
        if(template == null){
            logger.info("[api/template GET] Template not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(template, HttpStatus.OK);
    }

    @RequestMapping(value = "template", method = RequestMethod.POST)
    public ResponseEntity<?> addTemplate(@RequestBody TemplateEntity template) {
        if(template.getName().isEmpty()){
            logger.info("[api/template POST] Name is null");
            return new ResponseEntity<>(new Error("Name can't be empty","NAME_NULL"), HttpStatus.BAD_REQUEST);
        }
        if(templateRepository.getByName(template.getName()) != null){
            logger.info("[api/template POST] Name already exist");
            return new ResponseEntity<>(new Error("Name already exist", "NAME_EXIST"), HttpStatus.CONFLICT);
        }
        template = templateRepository.save(template);

        return new ResponseEntity<>(template, HttpStatus.OK);
    }

    @RequestMapping(value = "template", method = RequestMethod.PUT)
    public ResponseEntity<?> editTemplate(@RequestParam(value = "id") int id, @RequestBody TemplateEntity template) {
        if(template.getName().isEmpty()){
            logger.info("[api/template PUT] Name is null");
            return new ResponseEntity<>(new Error("Name can't be empty","NAME_NULL"), HttpStatus.BAD_REQUEST);
        }

        TemplateEntity inDbByName = templateRepository.getByName(template.getName());
        if( inDbByName!= null && inDbByName.getId() != id){
            logger.info("[api/template PUT] Name already exist");
            return new ResponseEntity<>(new Error("Name already exist", "NAME_EXIST"), HttpStatus.CONFLICT);
        }

        TemplateEntity inDb = templateRepository.getById(id);

        if(inDb == null){
            logger.info("[api/template PUT] Template not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        inDb.setName(template.getName());

        inDb = templateRepository.save(inDb);

        return new ResponseEntity<>(inDb, HttpStatus.OK);
    }

    @RequestMapping(value = "template", method = RequestMethod.DELETE)
    public ResponseEntity<?> editTemplate(@RequestParam(value = "id") int id) {
        TemplateEntity inDb = templateRepository.getById(id);

        if(inDb == null){
            logger.info("[api/template DEL] Template not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        templateRepository.delete(inDb);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @RequestMapping(value = "template/{templateId}/widgetConf", method = RequestMethod.GET)
    public ResponseEntity<?> getWidgetConf(@PathVariable(value = "templateId") int templateId, @RequestParam(name = "id", defaultValue = "-1") int id){

        TemplateEntity templateEntity = templateRepository.getById(templateId);
        if(templateEntity == null){
            logger.info("[api/template/{}/widgetConf GET] Template not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if(id == -1){
            return new ResponseEntity<>(templateEntity.getWidgetConfigs(), HttpStatus.OK);
        }
        WidgetConfigEntity widgetConfigEntity = widgetConfigRepository.getById(id);
        if(widgetConfigEntity == null){
            logger.info("[api/template GET] WidgetConf not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(widgetConfigEntity, HttpStatus.OK);

    }



    @RequestMapping(value = "template/{templateId}/widgetConf", method = RequestMethod.POST)
    public ResponseEntity<?> addWidgetConf(@PathVariable(value = "templateId") int templateId, @RequestBody List<WidgetConfigEntity> widgetList){
        TemplateEntity templateEntity = templateRepository.getById(templateId);
        if(templateEntity == null){
            logger.info("[api/template/{}/widgetConf POST] Template not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        for(WidgetConfigEntity widget : widgetList){
            if(!widget.checkConf())
                return new ResponseEntity<>(new Error("Conf check fail", "CONF_ERROR"), HttpStatus.BAD_REQUEST);
            widget.setTemplate(templateEntity);
        }

        widgetConfigRepository.save(widgetList);
        return new ResponseEntity<>(templateRepository.getById(templateId), HttpStatus.CREATED);

    }

    @RequestMapping(value = "/widgetConf", method = RequestMethod.PUT)
    public ResponseEntity<?> editWidgetConf(@RequestParam(value = "id")int id, @RequestBody WidgetConfigEntity widget){
        WidgetConfigEntity widgetConfigEntity = widgetConfigRepository.getById(id);
        if(widgetConfigEntity == null){
            logger.info("[/widgetConf PUT] Widget not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        widgetConfigEntity.update(widget);

        widgetConfigEntity = widgetConfigRepository.save(widgetConfigEntity);
        return new ResponseEntity<>(widgetConfigEntity, HttpStatus.OK);

    }


    @RequestMapping(value = "/widgetConf", method = RequestMethod.DELETE)
    public ResponseEntity<?> delWidgetConf(@RequestParam(value = "id") int id){
        WidgetConfigEntity widgetConfigEntity = widgetConfigRepository.getById(id);
        if(widgetConfigEntity == null){
            logger.info("[/widgetConf DEL] Widget not found");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        widgetConfigRepository.delete(widgetConfigEntity);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }



}
