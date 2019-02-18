package Donkey.Database.Repository;

import Donkey.Database.Entity.WidgetConfigEntity;
import org.springframework.data.repository.CrudRepository;

public interface WidgetConfigRepository extends CrudRepository<WidgetConfigEntity, Integer> {
    WidgetConfigEntity getById(int id);
}
