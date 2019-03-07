package Donkey.Database.Repository;

import Donkey.Database.Entity.PersistentLoginEntity;
import org.springframework.data.repository.CrudRepository;

public interface PersistentTokenRepository extends CrudRepository<PersistentLoginEntity,Integer> {
    PersistentLoginEntity getBySeries(String series);
    void deleteByUsername(String username);
}
