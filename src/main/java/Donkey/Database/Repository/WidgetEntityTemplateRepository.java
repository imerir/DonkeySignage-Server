package Donkey.Database.Repository;

import Donkey.Database.Entity.TemplateAndWidget.WidgetEntityTemplate;
import Donkey.Database.Entity.TemplateEntity;
import Donkey.Database.Entity.WidgetConfigEntity;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface WidgetEntityTemplateRepository extends CrudRepository<WidgetEntityTemplate, Integer> {
    List<WidgetEntityTemplate> getByWidgetConfigEntityAndTemplateEntity(WidgetConfigEntity widgetEntityTemplate, TemplateEntity templateEntity);
    List<WidgetEntityTemplate> getByWidgetConfigEntity(WidgetConfigEntity widgetConfigEntity);
    List<WidgetEntityTemplate> getByTemplateEntity(TemplateEntity templateEntity);
    List<WidgetEntityTemplate> getAllBy();


}
