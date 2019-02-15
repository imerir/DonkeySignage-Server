package Donkey.Database.Entity;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "template")
    private List<ScreenEntity> screen = new ArrayList<>();


    @OneToMany(fetch = FetchType.LAZY, mappedBy = "template")
    private List<WidgetConfig> widgetConfigs = new ArrayList<>();


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ScreenEntity> getScreen() {
        return screen;
    }

    public void setScreen(List<ScreenEntity> screen) {
        this.screen = screen;
    }

    public List<WidgetConfig> getWidgetConfigs() {
        return widgetConfigs;
    }

    public void setWidgetConfigs(List<WidgetConfig> widgetConfigs) {
        this.widgetConfigs = widgetConfigs;
    }
}
