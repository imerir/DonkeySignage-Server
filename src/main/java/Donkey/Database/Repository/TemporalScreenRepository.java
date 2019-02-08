package Donkey.Database.Repository;

import Donkey.Database.Entity.TemporalScreenEntity;
import org.springframework.data.repository.CrudRepository;

public interface TemporalScreenRepository extends CrudRepository <TemporalScreenEntity,Integer> {
    TemporalScreenEntity getTemporalRegisterByIp(String ip);
    TemporalScreenEntity getTemporalRegisterByUuid(String uuid);
    TemporalScreenEntity getTemporalRegisterByTempToken(String tmpToken);
}
