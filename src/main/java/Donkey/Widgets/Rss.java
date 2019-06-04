package Donkey.Widgets;

import Donkey.Tools.FilesTools;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Rss implements WidgetInterface {
    private Logger logger = LogManager.getLogger();

    @Override
    public String getName() {
        return "RSS";
    }

    @Override
    public String getId() {
        return "RSS";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public String getTemplate() throws IOException {
        return FilesTools.getINSTANCE().getFileContent("widgets/rss.html");
    }

    @Override
    public String convertParam(String paramStr) throws IOException {
        return null;
    }

    @Override
    public List<WidgetConfDefinition> getParam() {
        return null;
    }

    @Override
    public Map<String, WidgetConfDefinition> getParam(String jsonValue) throws IOException {
        return null;
    }
}
