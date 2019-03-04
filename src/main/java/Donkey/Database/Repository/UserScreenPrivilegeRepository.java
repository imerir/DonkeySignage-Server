package Donkey.Database.Repository;

import Donkey.Database.Entity.ScreenEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserEntity;
import Donkey.Database.Entity.UserAndPrivileges.UserScreenPrivilege;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public interface UserScreenPrivilegeRepository extends CrudRepository<UserScreenPrivilege, Integer> {
    List<UserScreenPrivilege> getByUserEntityAndScreenEntity(UserEntity userEntity, ScreenEntity screenEntity);
    List<UserScreenPrivilege> getByUserEntity(UserEntity userEntity);
    List<UserScreenPrivilege> getByScreenEntity(ScreenEntity screenEntity);
    List<UserScreenPrivilege> getAllBy();
    void deleteUserScreenPrivilegeByUserEntityAndScreenEntity(UserEntity userEntity, ScreenEntity screenEntity);
}
