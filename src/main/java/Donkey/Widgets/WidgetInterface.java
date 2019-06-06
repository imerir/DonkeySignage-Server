package Donkey.Widgets;

import Donkey.Database.Entity.WidgetConfigEntity;
import Donkey.WebSocket.WebSocketUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * Interface used to create a widget.
 * Each class that implement the interface will be automatically loaded by {@link WebSocketUtils#getWidgets()}
 */
public interface WidgetInterface {
    /**
     * Return the human friendly name.
     * @return name
     */
    String getName();

    /**
     * Return the ID of this widget.
     * @return id
     */
    String getId();

    String getVersion();

    /**
     * Return the HTML CSS JAVASCRIPT structure of the widget.
     * Put your html into <i>resources/widgets</i> and you can use this to return it:
     * <pre>
     *     public String getTemplate() throws IOException {
     *
     *         return FilesTools.getINSTANCE().getFileContent("widgets/YOUR_FILE.html");
     *     }
     * </pre>
     * @return The code send to screen that define the widget.
     * @throws IOException
     */
    String getTemplate() throws IOException;

    /**
     * This method is used for sending the widget config, if you need to convert the configuration, do it here and return the converted configuration.
     * @param paramStr The JSON configuration stored in database.
     * @return The converted (or not) configuration in JSON String.
     * @throws IOException
     */
    String convertParam(String paramStr) throws IOException;

    /**
     * Used to define the configuration page (Edit widget or add widget). You need to use {@link WidgetConfDefinition}
     * @return A list of {@link WidgetConfDefinition}
     * @see WidgetConfDefinition
     */
    List<WidgetConfDefinition> getParam();

    /**
     * Return the configuration for the given JSON
     * @param jsonValue Saved parameters in db as JSON
     * @return A map where KEY is the configuration name et value is the {@link WidgetConfDefinition}
     * @throws IOException
     * @see WidgetConfDefinition
     */
    Map<String, WidgetConfDefinition> getParam(String jsonValue) throws IOException;

    /**
     * Used to determine is the screen need to be updated. This method will be call periodically.
     * @param widgetConf The {@link WidgetConfigEntity} that need to be checked.
     * @return True is the widget need update.
     */
    boolean needUpdate(WidgetConfigEntity widgetConf);




}
