package repos;

import models.Inventory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepo extends CrudRepository<Inventory, Integer> {
}
