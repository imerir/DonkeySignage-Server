package Donkey.Database;

import Donkey.Database.Entity.UserAndPrivileges.RolesEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.Database.Entity.WidgetConfigEntity;
import Donkey.Database.Repository.RoleRepository;
import Donkey.Database.Repository.UserRepository;
import Donkey.Database.Repository.WidgetConfigRepository;
import Donkey.Tools.Json;
import Donkey.Widgets.ColorMapValue;
import Donkey.Widgets.RaplaCalendar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DbPreInit {
    private final RoleRepository roleRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final WidgetConfigRepository widgetConfigRepository;
    private Logger logger = LogManager.getLogger();

    @Autowired
    public DbPreInit(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder encoder, WidgetConfigRepository widgetConfigRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.widgetConfigRepository = widgetConfigRepository;
    }

    @PostConstruct
    public void init(){
        RolesEntity admin = roleRepository.getRolesEntityByName("ROLE_ADMIN");
        if( admin == null)
            admin = roleRepository.save(new RolesEntity("ROLE_ADMIN"));

        if(roleRepository.getRolesEntityByName("ROLE_USER") == null)
            roleRepository.save(new RolesEntity("ROLE_USER"));

        if(userRepository.getUserEntityByUsername("admin") == null){
            UserEntity user = new UserEntity();
            user.setUsername("admin");
            user.setPassword(encoder.encode("donkey"));
            user = userRepository.save(user);
            List<RolesEntity> temp = new ArrayList<>();
            temp.add(admin);
            user.setRoles(temp);
            userRepository.save(user);
        }
        upgradeDb();
    }



    public void upgradeDb(){
        upgradeRaplaCal();
    }


    private void upgradeRaplaCal(){
        List<WidgetConfigEntity> widgets = widgetConfigRepository.getAllByWidgetId("RAPLA_CALENDAR");
        boolean needUpdate = false;
        for(WidgetConfigEntity widget : widgets){
            try {
                HashMap<String, Object> conf =  Json.loadObject(widget.getParam());
                Map<String, Object> urls = (Map<String, Object>) conf.get("URLS");
                for (Map.Entry<String, Object> entry : urls.entrySet()) {
                    if(entry.getValue() instanceof String){
                        logger.warn("Upgrading RAPLA config of widget " + widget.getId() + " (" + widget.getName() + ")");
                        String url = (String) entry.getValue();
                        entry.setValue(new ColorMapValue(url, "#32CD32"));
                        needUpdate = true;
                    }
                }
                if(needUpdate){
                    widget.setParam(Json.stringify(conf));
                    widgetConfigRepository.save(widget);
                }

            } catch (IOException e) {
                widgetConfigRepository.delete(widget);
            }
        }
    }
}
