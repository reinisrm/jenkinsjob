package com.example.InventorySystem.repos;

import com.example.InventorySystem.models.Inventory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepo extends CrudRepository<Inventory, Integer> {
}
