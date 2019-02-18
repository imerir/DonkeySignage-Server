package Donkey.Database.Repository;

import Donkey.Database.Entity.TemplateEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TemplateRepository extends CrudRepository<TemplateEntity, Integer> {
    TemplateEntity getById(int id);
    TemplateEntity getByName(String name);
    List<TemplateEntity> getAllBy();
}
