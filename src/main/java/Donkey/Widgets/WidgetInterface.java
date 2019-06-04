package Donkey.Widgets;

import Donkey.Database.Entity.WidgetConfigEntity;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface WidgetInterface {
    String getName();
    String getId();
    String getVersion();
    String getTemplate() throws IOException;
    String convertParam(String paramStr) throws IOException;
    List<WidgetConfDefinition> getParam();
    Map<String, WidgetConfDefinition> getParam(String jsonValue) throws IOException;
    boolean needUpdate(WidgetConfigEntity widgetConf);




}
