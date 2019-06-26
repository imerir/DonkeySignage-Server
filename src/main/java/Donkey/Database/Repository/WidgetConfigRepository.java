/*
 * Developed by Sebastien CLEMENT.
 * File created on 6/25/19 3:06 PM.
 * Last edit 6/25/19 2:54 PM.
 */

package Donkey.Database.Repository;

import Donkey.Database.Entity.WidgetConfigEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WidgetConfigRepository extends CrudRepository<WidgetConfigEntity, Integer> {
    WidgetConfigEntity getById(int id);
    List<WidgetConfigEntity> getAllByTemplateId(int id);
    List<WidgetConfigEntity> getAllBy();
}
