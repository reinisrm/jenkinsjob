package com.example.InventorySystem.services.impl;

import com.example.InventorySystem.models.Inventory;
import com.example.InventorySystem.models.Lending;
import com.example.InventorySystem.repos.InventoryRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.InventorySystem.services.InventoryService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final Logger logger = LoggerFactory.getLogger(InventoryServiceImpl.class);

    @Autowired
    private InventoryRepo inventoryRepo;

    @Override
    public List<Inventory> getAll() {
        try {
            return (List<Inventory>) inventoryRepo.findAll();
        } catch (Exception e) {
            logger.error("Error occurred while fetching all inventories", e);
            throw e;
        }
    }

    @Override
    public Optional<Inventory> getInventoryById(int inventoryId) {
        try {
            return inventoryRepo.findById(inventoryId);
        } catch (Exception e) {
            logger.error("Error occurred while fetching inventory with ID: {}", inventoryId, e);
            throw e;
        }
    }

    @Override
    public void createInventory(Inventory inventory) {
        try {
            if (inventory != null && inventory.getInventoryId() == 0) {
                inventoryRepo.save(inventory);
            } else {
                throw new IllegalArgumentException("Invalid data or inventory_id already exists");
            }
        } catch (Exception e) {
            logger.error("Error occurred while creating inventory", e);
            throw e;
        }
    }

    @Override
    public void updateInventoryById(int inventoryId, Inventory updatedInventoryData) {
        try {
            Optional<Inventory> existingInventoryOptional = inventoryRepo.findById(inventoryId);
            if (existingInventoryOptional.isPresent()) {
                Inventory existingInventory = existingInventoryOptional.get();

                // Remove existingInventory from the lending list of associated Lending entities
                for (Lending lending : existingInventory.getLending()) {
                    lending.setInventory(null);
                }

                // Update existingInventory properties
                existingInventory.setDevice(updatedInventoryData.getDevice());
                existingInventory.setInventoryNumber(updatedInventoryData.getInventoryNumber());
                existingInventory.setRoom(updatedInventoryData.getRoom());

                // Save the updated Inventory
                inventoryRepo.save(existingInventory);

                // Update references in the associated Lending entities
                for (Lending lending : existingInventory.getLending()) {
                    lending.setInventory(existingInventory);
                }
            } else {
                throw new NoSuchElementException("Inventory not found");
            }
        } catch (Exception e) {
            logger.error("Error occurred while updating inventory with ID: {}", inventoryId, e);
            throw e;
        }
    }

    @Override
    public void deleteInventoryById(int inventoryId) {
        try {
            Optional<Inventory> existingInventoryOptional = inventoryRepo.findById(inventoryId);
            if (existingInventoryOptional.isPresent()) {
                inventoryRepo.deleteById(inventoryId);
            } else {
                throw new NoSuchElementException("Inventory not found");
            }
        } catch (Exception e) {
            logger.error("Error occurred while deleting inventory with ID: {}", inventoryId, e);
            throw e;
        }
    }
}
