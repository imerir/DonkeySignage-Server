package Donkey.Database.Repository;

import Donkey.Database.Entity.GroupEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupRepository extends CrudRepository<GroupEntity,Integer> {
    GroupEntity getGroupEntityById(int id);
    GroupEntity getGroupEntityByNameAndParent_Id(String name, int parent);
}
