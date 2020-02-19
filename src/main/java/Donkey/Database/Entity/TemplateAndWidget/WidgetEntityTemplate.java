package Donkey.Database.Entity.TemplateAndWidget;


import Donkey.Database.Entity.TemplateEntity;
import Donkey.Database.Entity.WidgetConfigEntity;

import javax.persistence.*;

@Entity
@IdClass(WidgetEntityTemplateId.class)
public class WidgetEntityTemplate {

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private WidgetConfigEntity widgetConfigEntity;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private TemplateEntity templateEntity;

    private Integer posX;
    private Integer posY;

    public WidgetConfigEntity getWidgetConfigEntity() {
        return widgetConfigEntity;
    }

    public void setWidgetConfigEntity(WidgetConfigEntity widgetConfigEntity) {
        this.widgetConfigEntity = widgetConfigEntity;
    }

    public TemplateEntity getTemplateEntity() {
        return templateEntity;
    }

    public void setTemplateEntity(TemplateEntity templateEntity) {
        this.templateEntity = templateEntity;
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

}
