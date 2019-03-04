package Donkey.Database.Repository;

import Donkey.Database.Entity.GroupEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface GroupRepository extends CrudRepository<GroupEntity,Integer> {
    GroupEntity getGroupEntityById(int id);
    GroupEntity getGroupEntityByNameAndParent(String name, GroupEntity parent);
    List<GroupEntity> getGroupEntityByParentNull();
    List<GroupEntity> getGroupEntityByParent_Id(int id);
    List<GroupEntity> getAllBy();

    void delete(GroupEntity group);
}
