package Donkey.Database.Repository;


import Donkey.Database.Entity.TemplateAndWidget.WidgetEntityTemplate;
import Donkey.Database.Entity.TemplateEntity;
import Donkey.Database.Entity.WidgetConfigEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WidgetTemplateRepository extends CrudRepository<WidgetEntityTemplate, Integer> {
   /* List<WidgetEntityTemplate> getByWidgetTemplate(WidgetConfigEntity widgetConfigEntity, TemplateEntity templateEntity);
    List<WidgetEntityTemplate> getByWidget(WidgetConfigEntity widgetConfigEntity);
    List<WidgetEntityTemplate> getByTemplate(TemplateEntity templateEntity);*/
    List<WidgetEntityTemplate> getAllBy();

}
