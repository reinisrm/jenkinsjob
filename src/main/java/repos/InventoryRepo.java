package repos;

import models.Inventory;
import org.springframework.data.repository.CrudRepository;

public interface InventoryRepo extends CrudRepository<Inventory, Integer> {
}
