package Donkey.Widgets;

import Donkey.Database.Entity.WidgetConfigEntity;
import Donkey.Tools.Base64;
import Donkey.Tools.FilesTools;
import Donkey.Tools.Json;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class ImageSlide implements WidgetInterface {
    private Logger logger = LogManager.getLogger();

    @Override
    public String getName() {
        return "Image Slides";
    }

    @Override
    public String getId() {
        return "IMAGE_SLIDE";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public String getTemplate() throws IOException {
        return FilesTools.getINSTANCE().getFileContent("widgets/image_slides.html");
    }

    @Override
    public String convertParam(String paramStr) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> converted = new ArrayList<>();
        Params param = objectMapper.readValue(paramStr, Params.class);
        try {

            for (String image : param.images){
                File file = new File("upload-dir/" + image);
                String encoded = Base64.encodeFileToBase64Binary(file);
                if(image.contains(".jpg")){
                    converted.add("jpg");
                }
                else if(image.contains(".mp4")){
                    converted.add("mp4");
                }
                converted.add(encoded);

            }
        } catch (FileNotFoundException e) {
            logger.info("File not found, ignore it.");
        }
        param.images = converted;



        return objectMapper.writeValueAsString(param);
    }

    @Override
    public List<WidgetConfDefinition> getParam() {

        WidgetConfDefinition images = new WidgetConfDefinition("images", "Images", ConfType.MEDIA, false, true, false,null, "", null);
        return Collections.singletonList(images);
    }

    @Override
    public Map<String, WidgetConfDefinition> getParam(String jsonValue) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,Object> parsed = objectMapper.readValue(jsonValue, new TypeReference<Map<String, Object>>() {});
        Map<String, WidgetConfDefinition> map = new HashMap<>();
        map.put("images", new WidgetConfDefinition("images", "Images", ConfType.MEDIA, false, true, false,parsed.get("images"), Json.stringify(parsed.get("images")), null));
        return map;

    }

    @Override
    public boolean needUpdate(WidgetConfigEntity widgetConf) {
        return false;
    }


    public static class Params{
        public List<String> images = new ArrayList<>();


    }
}
