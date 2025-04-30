package com.example.inventorysystem.services.impl;

import com.example.inventorysystem.models.Inventory;
import com.example.inventorysystem.models.Lending;
import com.example.inventorysystem.repos.InventoryRepo;
import com.example.inventorysystem.services.LendingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.inventorysystem.services.InventoryService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);

    private final InventoryRepo inventoryRepo;
    private final LendingService lendingService;

    @Autowired
    public InventoryServiceImpl(InventoryRepo inventoryRepo, LendingService lendingService) {
        this.inventoryRepo = inventoryRepo;
        this.lendingService = lendingService;
    }


    @Override
    public List<Inventory> getAll() {
        return inventoryRepo.findAll();
    }

    @Override
    public Optional<Inventory> getInventoryById(int inventoryId) {
        Optional<Inventory> inventoryOptional = inventoryRepo.findById(inventoryId);
        if (inventoryOptional.isEmpty()) {
            log.warn("Inventory with id: {} is not found", inventoryId);
        } else {
            log.info("Inventory found with id: {}", inventoryId);
        }
        return inventoryOptional;
    }

    @Override
    public void createInventory(Inventory inventory) {
        try {
            if (inventory != null && inventory.getInventoryId() == 0) {
                inventoryRepo.save(inventory);
                log.info("Inventory created successfully: {}", inventory);
            } else {
                throw new IllegalArgumentException("Invalid data or inventory_id already exists");
            }
        } catch (Exception e) {
            log.error("Error occurred while creating inventory", e);
        }
    }

    @Override
    public void updateInventoryById(int inventoryId, Inventory updatedInventoryData) {
        try {
            Optional<Inventory> existingInventoryOptional = inventoryRepo.findById(inventoryId);
            if (existingInventoryOptional.isPresent()) {
                Inventory existingInventory = existingInventoryOptional.get();

                // update existingInventory properties
                existingInventory.setDevice(updatedInventoryData.getDevice());
                existingInventory.setInventoryNumber(updatedInventoryData.getInventoryNumber());
                existingInventory.setRoom(updatedInventoryData.getRoom());
                existingInventory.setCabinet(updatedInventoryData.getCabinet());

                // save the updated Inventory
                Inventory updatedInventory = inventoryRepo.save(existingInventory);

                // update references in the associated Lending entities
                for (Lending lending : updatedInventory.getLending()) {
                    lending.setInventory(updatedInventory);
                    lendingService.updateLending(lending.getLendingId(), lending);
                }

                log.info("Inventory with ID {} updated successfully: {}", inventoryId, updatedInventory);
            } else {
                throw new NoSuchElementException("Inventory not found");
            }
        } catch (Exception e) {
            log.error("Error occurred while updating inventory with ID: {}", inventoryId, e);
        }
    }

    @Override
    public void deleteInventoryById(int inventoryId) {
        try {
            Optional<Inventory> existingInventoryOptional = inventoryRepo.findById(inventoryId);
            if (existingInventoryOptional.isPresent()) {
                Inventory existingInventory = existingInventoryOptional.get();

                // set the associated inventories in lendings to null
                for (Lending lending : existingInventory.getLending()) {
                    lending.setInventory(null);
                }

                inventoryRepo.deleteById(inventoryId);
                log.info("Inventory with id: {} deleted successfully", inventoryId);
            } else {
                throw new NoSuchElementException("Inventory not found");
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting inventory with ID: {}", inventoryId, e);
        }
    }
}