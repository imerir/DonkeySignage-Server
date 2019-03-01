package Donkey.Database.Repository;

import Donkey.Database.Entity.ScreenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScreenRepository extends CrudRepository<ScreenEntity,Integer> {
    ScreenEntity getScreenRegisterByUuid(String uuid);
    ScreenEntity getScreenEntityById(int id);
    List<ScreenEntity> getScreenByGroupNull();
    List<ScreenEntity> getScreenEntityByGroupId(int id);
    List<ScreenEntity> getAllBy();

}
