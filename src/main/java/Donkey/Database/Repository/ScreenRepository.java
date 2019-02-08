package Donkey.Database.Repository;

import Donkey.Database.Entity.ScreenEntity;
import Donkey.WebSite.FormClass.ScreenRegisterForm;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScreenRepository extends CrudRepository<ScreenEntity,Integer> {
    ScreenEntity getScreenRegisterByUuid(String uuid);
    ScreenEntity getScreenEntityById(int id);
}
