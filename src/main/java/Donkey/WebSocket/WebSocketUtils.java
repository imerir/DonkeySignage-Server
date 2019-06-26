package Donkey.WebSocket;

import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Entity.TemplateEntity;
import Donkey.Database.Entity.WidgetConfigEntity;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.SpringContext;
import Donkey.WebSocket.Data.WebSocketData;
import Donkey.Widgets.WidgetInterface;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
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
    private ScreenRepository screenRepository;
    private ObjectMapper objectMapper = new ObjectMapper();


    /**
     * List all class that implement {@link WidgetInterface WidgetInterface}.
     * @return Map of widget where the key is the id of the widget.
     */
    public HashMap<String, WidgetInterface> getWidgets(){
        if(widgets.isEmpty()){
            ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider( false);
            provider.addIncludeFilter(new AssignableTypeFilter(WidgetInterface.class));
            Set<BeanDefinition> beans = provider.findCandidateComponents("Donkey");
            logger.debug("Listing widgets...");
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


    public TemplateEntity templateConvertor(TemplateEntity templateEntity){
        List<WidgetConfigEntity> widgetsConf = templateEntity.getWidgetConfigs();
        for (WidgetConfigEntity aWidget : widgetsConf){
            try {
                aWidget.setParam(widgets.get(aWidget.getWidgetId()).convertParam(aWidget.getParam()));
            } catch (IOException e) {
                logger.catching(e);
                //TODO send error to widget to display a message ?????
            }

        }
        return templateEntity;
    }

    public void notifyScreen(ScreenEntity screenEntity) {
        SocketState state = SocketState.getINSTANCE();
        SocketState.Info socketInfo = state.logged.get(screenEntity.getUuid());
        if(socketInfo == null){
            logger.info("Can't notify screen " + screenEntity.getName() + ", screen OFFLINE.");
            return;
        }
        try {
            sendWidgetManifest(socketInfo.socket);
        } catch (IOException e) {
            logger.catching(e);
        }

    }

    public void sendWidgetManifest(WebSocketSession session) throws IOException {
        SocketState states = SocketState.getINSTANCE();
        SocketState.Info state = states.getLoggedBySocket(session);
        if(state != null){
            HashMap<String, WidgetInterface> widgets = WebSocketUtils.getINSTANCE().getWidgets();
            WebSocketData webSocketData = new WebSocketData();
            webSocketData.type = "MANIFEST";
            webSocketData.data = new HashMap<>();
            webSocketData.data.put("list", widgets.values());
            String strWidget = objectMapper.writeValueAsString(webSocketData);
            session.sendMessage(new TextMessage(strWidget));
        }



    }


    public void sendConfig(WebSocketSession session) throws IOException {
        SocketState states = SocketState.getINSTANCE();
        SocketState.Info state = states.getLoggedBySocket(session);
        if(state != null){
            WebSocketData webSocketData = new WebSocketData();
            webSocketData.type = "CONFIG";
            webSocketData.data = new HashMap<>();

            if(screenRepository == null){
                ApplicationContext context = SpringContext.getAppContext();
                screenRepository = (ScreenRepository) context.getBean("screenRepository");
            }
            ScreenEntity screen = screenRepository.getScreenRegisterByUuid(state.uuid);
            TemplateEntity template = screen.getTemplate();

            webSocketData.data.put("template", template == null ? null : WebSocketUtils.getINSTANCE().templateConvertor(template) );
            String screenStr = objectMapper.writeValueAsString(webSocketData);
            session.sendMessage(new TextMessage(screenStr));
        }
    }




}
