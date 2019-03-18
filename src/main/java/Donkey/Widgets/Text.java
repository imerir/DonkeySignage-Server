package Donkey.Widgets;

import Donkey.MainClass;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class Text implements WidgetInterface{
    Logger logger = LogManager.getLogger();

    @Override
    public String getId() {
        return "TEXT";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public String getTemplate() throws IOException {
        ClassLoader classLoader = MainClass.class.getClassLoader();
        File file = new File(classLoader.getResource("widgets/text.html").getFile());

        return new String(Files.readAllBytes(file.toPath()));
    }

    @Override
    public String convertParam(String paramStr) {
        return paramStr;
    }


    @JsonIgnore
    @Override
    public List<WidgetConfDefinition> getParam() {
        WidgetConfDefinition message = new WidgetConfDefinition("message", WidgetConfDefinition.Type.TEXT, true, false, null, null);
        WidgetConfDefinition font_size = new WidgetConfDefinition("font_size", WidgetConfDefinition.Type.NUMBER, true, false, null, null);
        return Arrays.asList(message, font_size);
    }
}
