package Donkey.Widgets;

import java.io.IOException;
import java.util.List;

public interface WidgetInterface {

    String getId();
    String getVersion();
    String getTemplate() throws IOException;
    String convertParam(String paramStr) throws IOException;
    List<WidgetConfDefinition> getParam();




}
