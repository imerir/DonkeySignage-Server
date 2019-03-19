package Donkey.Widgets;

import Donkey.MainClass;
import Donkey.Tools.Base64;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
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
        ClassLoader classLoader = MainClass.class.getClassLoader();
        File file = new File(classLoader.getResource("widgets/image_slides.html").getFile());
        return new String(Files.readAllBytes(file.toPath()));
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

        WidgetConfDefinition images = new WidgetConfDefinition("images", ConfType.MEDIA, false, true, "", null);
        return Collections.singletonList(images);
    }




    public static class Params{
        public List<String> images = new ArrayList<>();


    }
}
