package Donkey.Database.Entity.TemplateAndWidget;

import Donkey.Database.Entity.UserAndPrivileges.UserScreenPrivilegeId;

import java.io.Serializable;
import java.util.Objects;

public class WidgetEntityTemplateId implements Serializable {
    private int widgetEntity;
    private int templateEntity;

    public WidgetEntityTemplateId(){

    }

    public WidgetEntityTemplateId(int widgetEntity, int templateEntity) {
        this.widgetEntity = widgetEntity;
        this.templateEntity = templateEntity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(widgetEntity, templateEntity);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if( obj == null || getClass() != obj.getClass())
            return false;

        WidgetEntityTemplateId that = (WidgetEntityTemplateId) obj;
        return Objects.equals(widgetEntity, that.widgetEntity) && Objects.equals(templateEntity, that.templateEntity);
    }

    public int getWidgetEntity() {
        return widgetEntity;
    }

    public void setWidgetEntity(int widgetEntity) {
        this.widgetEntity = widgetEntity;
    }

    public int getTemplateEntity() {
        return templateEntity;
    }

    public void setTemplateEntity(int templateEntity) {
        this.templateEntity = templateEntity;
    }
}
