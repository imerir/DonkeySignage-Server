package Donkey.Database.Repository;

import Donkey.Database.Entity.ScreenRegister;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScreenRegisterRepository extends CrudRepository<ScreenRegister,Integer> {
    ScreenRegister getScreenRegisterByUuid(String uuid);
}
