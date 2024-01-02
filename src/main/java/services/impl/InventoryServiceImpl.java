package services.impl;

import models.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repos.InventoryRepo;
import services.InventoryService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    @Autowired
    private InventoryRepo inventoryRepo;

    @Override
    public List<Inventory> getAll() {
        return (List<Inventory>) inventoryRepo.findAll();
    }

    @Override
    public Optional<Inventory> getInventoryById(int inventoryId) {
        return inventoryRepo.findById(inventoryId);
    }

    @Override
    public void createInventory(Inventory inventory) {
        if (inventory != null && inventory.getInventoryId() == 0) {
            inventoryRepo.save(inventory);
        } else {
            throw new IllegalArgumentException("Invalid data or inventory_id already exists");
        }
    }

    @Override
    public void updateInventoryById(int inventoryId, Inventory updatedInventoryData) {
        Optional<Inventory> existingInventoryOptional = inventoryRepo.findById(inventoryId);
        if (existingInventoryOptional.isPresent()) {
            Inventory existingInventory = existingInventoryOptional.get();
            if (updatedInventoryData != null && existingInventory.getInventoryId() == inventoryId) {
                existingInventory.setDevice(updatedInventoryData.getDevice());
                existingInventory.setInventoryNumber(updatedInventoryData.getInventoryNumber());
                existingInventory.setRoom(updatedInventoryData.getRoom());
                inventoryRepo.save(existingInventory);
            } else {
                throw new IllegalArgumentException("Invalid inventory_id or data");
            }
        } else {
            throw new NoSuchElementException("Inventory not found");
        }
    }

    @Override
    public void deleteInventoryById(int inventoryId) {
        Optional<Inventory> existingInventoryOptional = inventoryRepo.findById(inventoryId);
        if (existingInventoryOptional.isPresent()) {
            inventoryRepo.deleteById(inventoryId);
        } else {
            throw new NoSuchElementException("Inventory not found");
        }
    }
}

