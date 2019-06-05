package Donkey.Widgets;

import Donkey.Database.Entity.WidgetConfigEntity;
import Donkey.Tools.FilesTools;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Text implements WidgetInterface{
    Logger logger = LogManager.getLogger();

    @Override
    public String getName() {
        return "Text";
    }

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

        return FilesTools.getINSTANCE().getFileContent("widgets/text.html");
    }

    @Override
    public String convertParam(String paramStr) {
        return paramStr;
    }


    @JsonIgnore
    @Override
    public List<WidgetConfDefinition> getParam() {
        WidgetConfDefinition message = new WidgetConfDefinition("message", ConfType.TEXT, true, false, false, "", "",  null);
        WidgetConfDefinition font_size = new WidgetConfDefinition("font_size", ConfType.NUMBER, true, false, false, "12", "",null);
        return Arrays.asList(message, font_size);
    }

    @Override
    public Map<String, WidgetConfDefinition> getParam(String jsonValue) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,Object> parsed;
        parsed = objectMapper.readValue(jsonValue, new TypeReference<Map<String, Object>>(){});
        Map<String, WidgetConfDefinition> map = new HashMap<>();
        map.put("message", new WidgetConfDefinition("message", ConfType.TEXT, true, false, false, parsed.get("message"), (String) parsed.get("message"), null));
        map.put("font_size", new WidgetConfDefinition("font_size", ConfType.NUMBER, true, false, false, parsed.get("font_size"), ((Integer)parsed.get("font_size")).toString(),  null));
        return  map;
    }

    @Override
    public boolean needUpdate(WidgetConfigEntity widgetConf) {
        return false;
    }


}
