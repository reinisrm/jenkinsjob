package com.example.InventorySystem.services;

import com.example.InventorySystem.models.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<Inventory> getAll();
    Optional<Inventory> getInventoryById(int inventoryId);
    void createInventory(Inventory inventory);
    void updateInventoryById(int inventoryId, Inventory updatedInventoryData);
    void deleteInventoryById(int inventoryId);
}
