package Donkey.Widgets;

import java.io.IOException;
import java.util.HashMap;

public interface WidgetInterface {

    String getId();
    String getVersion();
    String getTemplate() throws IOException;
    String convertParam(String paramStr) throws IOException;
    HashMap<String, Object> getParam();




}
