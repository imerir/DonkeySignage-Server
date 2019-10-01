/*
 * Developed by Sebastien CLEMENT.
 * File created on 9/30/19, 9:44 AM.
 */

package Donkey.Widgets;

import Donkey.Database.Entity.WidgetConfigEntity;
import Donkey.Tools.FilesTools;
import Donkey.Tools.HttpTools;
import Donkey.Tools.Json;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Header implements WidgetInterface{
    /**
     * Return the human friendly name.
     *
     * @return name
     */
    @Override
    public String getName() {
        return "Header";
    }

    /**
     * Return the ID of this widget.
     *
     * @return id
     */
    @Override
    public String getId() {
        return "HEADER";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    /**
     * Return the HTML CSS JAVASCRIPT structure of the widget.
     * Put your html into <i>resources/widgets</i> and you can use this to return it:
     * <pre>
     *     public String getTemplate() throws IOException {
     *
     *         return FilesTools.getINSTANCE().getFileContent("widgets/YOUR_FILE.html");
     *     }
     * </pre>
     *
     * @return The code send to screen that define the widget.
     * @throws IOException
     */
    @Override
    public String getTemplate() throws IOException {
        return FilesTools.getINSTANCE().getFileContent("widgets/header.html");
    }

    /**
     * This method is used for sending the widget config, if you need to convert the configuration, do it here and return the converted configuration.
     *
     * @param paramStr The JSON configuration stored in database.
     * @return The converted (or not) configuration in JSON String.
     * @throws IOException
     */
    @Override
    public String convertParam(String paramStr) throws IOException {
        HashMap<String, Object> conf = Json.loadObject(paramStr);
        String key = System.getenv().get("OPEN_WEATHER_API_KEY");
        conf.put("api_key", key);
        return Json.stringify(conf);
    }

    /**
     * Used to define the configuration page (Edit widget or add widget). You need to use {@link WidgetConfDefinition}
     *
     * @return A list of {@link WidgetConfDefinition}
     * @see WidgetConfDefinition
     */
    @Override
    public List<WidgetConfDefinition> getParam() {
        WidgetConfDefinition text = new WidgetConfDefinition("text", "Text", ConfType.TEXT, true, false, false, "", "",  null);
        WidgetConfDefinition text_font_size = new WidgetConfDefinition("text_font_size", "Text Font Size", ConfType.NUMBER, true, false, false, "50", "",null);
        WidgetConfDefinition text_color = new WidgetConfDefinition("text_color", "Text Color", ConfType.COLOR, false, false, false, "#000000", "",null);

        WidgetConfDefinition clock_font_size = new WidgetConfDefinition("clock_font_size", "Clock Font Size", ConfType.NUMBER, true, false, false, "30", "",null);
        WidgetConfDefinition clock_color = new WidgetConfDefinition("clock_color", "Text Color", ConfType.COLOR, false, false, false, "#000000", "",null);

        WidgetConfDefinition weather_city = new WidgetConfDefinition("weather_city", "Weather City Name", ConfType.TEXT, true, false, false, "", "",  null);

        WidgetConfDefinition border = new WidgetConfDefinition("border", "Border", ConfType.BOOL, false, false, false, "", "",null);

        return Arrays.asList(text, text_font_size, text_color, clock_font_size, clock_color, weather_city, border);
    }

    /**
     * Return the configuration for the given JSON
     *
     * @param jsonValue Saved parameters in db as JSON
     * @return A map where KEY is the configuration name et value is the {@link WidgetConfDefinition}
     * @throws IOException
     * @see WidgetConfDefinition
     */
    @Override
    public Map<String, WidgetConfDefinition> getParam(String jsonValue) throws IOException {
        HashMap<String, Object> parsed = Json.loadObject(jsonValue);
        Map<String, WidgetConfDefinition> map = new HashMap<>();


        map.put("text", new WidgetConfDefinition("text", "Text", ConfType.TEXT, true, false, false, parsed.get("text"), (String) parsed.get("text"),  null));
        map.put("text_font_size", new WidgetConfDefinition("text_font_size", "Text Font Size", ConfType.NUMBER, true, false, false, parsed.get("text_font_size"), ((Integer)parsed.get("text_font_size")).toString(),null));

        map.put("text_color", new WidgetConfDefinition("text_color", "Text Color", ConfType.COLOR, false, false, false, parsed.get("color"), (String) parsed.get("color"),null));

        map.put("clock_font_size", new WidgetConfDefinition("clock_font_size", "Clock Font Size", ConfType.NUMBER, true, false, false, parsed.get("clock_font_size"), ((Integer)parsed.get("clock_font_size")).toString(),null));
        map.put("clock_color", new WidgetConfDefinition("clock_color", "Clock Color", ConfType.COLOR, false, false, false, parsed.get("clock_color"), (String) parsed.get("clock_color"),null));

        map.put("weather_city", new WidgetConfDefinition("weather_city", "Weather City Name", ConfType.TEXT, true, false, false, parsed.get("weather_city"), (String) parsed.get("weather_city"),  null));

        map.put("border", new WidgetConfDefinition("border", "Border", ConfType.BOOL, false, false, false, parsed.get("border"),  Boolean.toString((Boolean) parsed.get("border")),null));


        return map;
    }

    /**
     * Used to determine is the screen need to be updated. This method will be call periodically.
     *
     * @param widgetConf The {@link WidgetConfigEntity} that need to be checked.
     * @return True is the widget need update.
     */
    @Override
    public boolean needUpdate(WidgetConfigEntity widgetConf) {
        return false;
    }
}
