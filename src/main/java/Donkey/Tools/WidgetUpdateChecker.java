/*
 * Developed by Sebastien CLEMENT.
 * File created on 6/25/19 3:07 PM.
 */

package Donkey.Tools;

import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Entity.WidgetConfigEntity;
import Donkey.Database.Repository.ScreenRepository;
import Donkey.Database.Repository.WidgetConfigRepository;
import Donkey.SpringContext;
import Donkey.WebSocket.WebSocketUtils;
import Donkey.Widgets.WidgetInterface;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class WidgetUpdateChecker {
    private Logger logger = LogManager.getLogger();
    private final WidgetConfigRepository widgetConfigRepository;
    private boolean ready = false;

    public WidgetUpdateChecker(WidgetConfigRepository widgetConfigRepository) {
        this.widgetConfigRepository = widgetConfigRepository;
    }

    @PostConstruct
    public void init(){
        ready = true;
    }



    @Transactional
    @Scheduled(fixedDelay = 30000)
    public void run() {
        if(!ready)
            return;
        logger.trace("Check for widget update...");
        HashMap<String, WidgetInterface> widgets = WebSocketUtils.getINSTANCE().getWidgets();
        List<WidgetConfigEntity> entites = widgetConfigRepository.getAllBy();
        List<ScreenEntity> toNotify = new ArrayList<>();
        for(WidgetConfigEntity conf : entites){
            boolean needUpdate = widgets.get(conf.getWidgetId()).needUpdate(conf);
            logger.trace("Need Update "+ conf.getId() + ": " + needUpdate);
            if(needUpdate){
                List<ScreenEntity> screens = conf.getTemplate().getScreen();
                for(ScreenEntity aScreen : screens){
                    if(!toNotify.contains(aScreen))
                        toNotify.add(aScreen);
                }
                conf.setLastUpdate(new Date());
            }
        }

        for(ScreenEntity screen : toNotify){
            WebSocketUtils.getINSTANCE().notifyScreen(screen);
        }
    }

}
