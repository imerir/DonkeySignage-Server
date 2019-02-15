package Donkey.WebSocket;

import Donkey.Widgets.WidgetInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class WebSocketUtils {

    private static WebSocketUtils INSTANCE = new WebSocketUtils();

    public static WebSocketUtils getINSTANCE(){ return INSTANCE;}

    private WebSocketUtils(){}

    private Logger logger = LogManager.getLogger();
    private HashMap<String, WidgetInterface> widgets = new HashMap<>();


    /**
     * List all class that implement {@link WidgetInterface WidgetInterface}.
     * @return Map of widget where the key is the id of the widget.
     */
    public HashMap<String, WidgetInterface> getWidgets(){
        if(widgets.isEmpty()){
            widgets.clear();
            ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider( false);
            provider.addIncludeFilter(new AssignableTypeFilter(WidgetInterface.class));
            Set<BeanDefinition> beans = provider.findCandidateComponents("Donkey");
            logger.info("Listing widgets...");
            for(BeanDefinition bean : beans){
                try {
                    WidgetInterface w = (WidgetInterface) Class.forName(bean.getBeanClassName(),true, Thread.currentThread().getContextClassLoader()).getConstructor().newInstance();
                    logger.debug("..." + w.getId());
                    widgets.put(w.getId(), w);

                } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException | ClassNotFoundException e) {
                    logger.catching(e);
                }
            }
        }
        return widgets;


    }





}
