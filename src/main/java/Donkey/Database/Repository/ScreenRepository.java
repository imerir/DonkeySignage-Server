package Donkey.Database.Repository;

import Donkey.Database.Entity.ScreenEntity;
import org.springframework.data.repository.CrudRepository;

public interface ScreenRepository extends CrudRepository<ScreenEntity,Integer> {
    ScreenEntity getScreenRegisterByUuid(String uuid);
    ScreenEntity getScreenEntityById(int id);
}
