package Donkey.Tools;

import Donkey.Widgets.WidgetConfDefinition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Json {
    public static String stringify(Object jsonObj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(jsonObj);
    }


    public static HashMap<String, Object> loadObject(String str) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<String,Object> parsed;
        parsed = objectMapper.readValue(str, new TypeReference<Map<String, Object>>(){});
        return parsed;
    }

}
