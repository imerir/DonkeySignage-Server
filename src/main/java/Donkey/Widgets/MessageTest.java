package Donkey.Widgets;

import Donkey.MainClass;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

public class MessageTest implements WidgetInterface{
    Logger logger = LogManager.getLogger();

    @Override
    public String getId() {
        return "TEST";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public String getTemplate() throws IOException {
        ClassLoader classLoader = MainClass.class.getClassLoader();
        File file = new File(classLoader.getResource("widgets/test.html").getFile());

        return new String(Files.readAllBytes(file.toPath()));
    }

    @JsonIgnore
    @Override
    public HashMap<String, Object> getParam() {
        HashMap<String, Object> param = new HashMap<>();
        param.put("message", "");
        return param;
    }
}
