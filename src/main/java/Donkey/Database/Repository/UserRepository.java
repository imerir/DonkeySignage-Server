package Donkey.Database.Repository;

import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity getUserEntityByUsername(String username);
    UserEntity getUserEntityById(int id);
    List<UserEntity> getAllBy();
}
