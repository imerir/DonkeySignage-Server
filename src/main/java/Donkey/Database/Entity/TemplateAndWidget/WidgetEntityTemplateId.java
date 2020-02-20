package Donkey.Database.Entity.TemplateAndWidget;

import Donkey.Database.Entity.UserAndPrivileges.UserScreenPrivilegeId;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class WidgetEntityTemplateId implements Serializable {
    private int widgetConfigEntity;
    private int templateEntity;

    public WidgetEntityTemplateId(){

    }

    public WidgetEntityTemplateId(int widgetEntity, int templateEntity) {
        this.widgetConfigEntity = widgetEntity;
        this.templateEntity = templateEntity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(widgetConfigEntity, templateEntity);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if( obj == null || getClass() != obj.getClass())
            return false;

        WidgetEntityTemplateId that = (WidgetEntityTemplateId) obj;
        return Objects.equals(widgetConfigEntity, that.widgetConfigEntity) && Objects.equals(templateEntity, that.templateEntity);
    }

    public int getWidgetEntity() {
        return widgetConfigEntity;
    }

    public void setWidgetEntity(int widgetEntity) {
        this.widgetConfigEntity = widgetEntity;
    }

    public int getTemplateEntity() {
        return templateEntity;
    }

    public void setTemplateEntity(int templateEntity) {
        this.templateEntity = templateEntity;
    }
}
