package Donkey.Database.Repository;

import Donkey.Database.Entity.TemporalRegister;
import org.springframework.data.repository.CrudRepository;

public interface TemporalRegisterRepository extends CrudRepository <TemporalRegister,Integer> {
    TemporalRegister getTemporalRegisterByIp(String ip);
}
