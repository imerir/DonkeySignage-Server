package Donkey.Database.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class WidgetConfigEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    private TemplateEntity template;

    private String widgetId;
    private Integer posX;
    private Integer posY;
    private Integer sizeWidth;
    private Integer sizeHeight;

    private String param;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TemplateEntity getTemplate() {
        return template;
    }

    public void setTemplate(TemplateEntity template) {
        this.template = template;
    }

    public Integer getPosX() {
        return posX;
    }

    public void setPosX(Integer posX) {
        this.posX = posX;
    }

    public Integer getPosY() {
        return posY;
    }

    public void setPosY(Integer posY) {
        this.posY = posY;
    }

    public Integer getSizeWidth() {
        return sizeWidth;
    }

    public void setSizeWidth(Integer sizeWidth) {
        this.sizeWidth = sizeWidth;
    }

    public Integer getSizeHeight() {
        return sizeHeight;
    }

    public void setSizeHeight(Integer sizeHeight) {
        this.sizeHeight = sizeHeight;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getWidgetId() {
        return widgetId;
    }

    public void setWidgetId(String widgetId) {
        this.widgetId = widgetId;
    }

    public boolean checkConf(){
        return sizeWidth != null && sizeHeight != null && posY != null && posX != null && widgetId != null && param != null;
    }

    public void update(WidgetConfigEntity widgetConfigEntity){
        if(widgetConfigEntity.param != null)
            this.param = widgetConfigEntity.param;

        if(widgetConfigEntity.posX != null)
            this.posX = widgetConfigEntity.posX;

        if(widgetConfigEntity.posY != null)
            this.posY = widgetConfigEntity.posY;

        if(widgetConfigEntity.sizeHeight != null)
            this.sizeHeight = widgetConfigEntity.sizeHeight;

        if(widgetConfigEntity.sizeWidth != null)
            this.sizeWidth = widgetConfigEntity.sizeWidth;

    }
}
