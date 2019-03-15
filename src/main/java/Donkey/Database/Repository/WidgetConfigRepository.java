package Donkey.Database.Repository;

import Donkey.Database.Entity.WidgetConfigEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WidgetConfigRepository extends CrudRepository<WidgetConfigEntity, Integer> {
    WidgetConfigEntity getById(int id);
    List<WidgetConfigEntity> getAllByTemplateId(int id);
}
