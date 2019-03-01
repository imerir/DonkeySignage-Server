package Donkey.Database.Repository;

import Donkey.Database.Entity.UserAndPrivileges.RolesEntity;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<RolesEntity, Integer> {
    RolesEntity getRolesEntityByName(String name);
}
