package Donkey.Database.Repository;

import Donkey.Database.Entity.UserAndPrivileges.RolesEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<RolesEntity, Integer> {
    RolesEntity getRolesEntityByName(String name);
    RolesEntity getRolesEntityById(int id);
    List<RolesEntity> getAllBy();
}
