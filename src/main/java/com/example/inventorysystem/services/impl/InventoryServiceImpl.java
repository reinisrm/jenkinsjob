package com.example.inventorysystem.services.impl;

import com.example.inventorysystem.mappers.InventoryMapper;
import com.example.inventorysystem.models.Inventory;
import com.example.inventorysystem.models.Lending;
import com.example.inventorysystem.models.dto.InventoryDTO;
import com.example.inventorysystem.repos.InventoryRepo;
import com.example.inventorysystem.services.InventoryService;
import com.example.inventorysystem.services.LendingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final Logger log = LoggerFactory.getLogger(InventoryServiceImpl.class);
    private final InventoryRepo inventoryRepo;
    private final LendingService lendingService;

    public InventoryServiceImpl(InventoryRepo inventoryRepo, LendingService lendingService) {
        this.inventoryRepo = inventoryRepo;
        this.lendingService = lendingService;
    }

    @Override
    public List<InventoryDTO> getAll() {
        List<Inventory> inventoryList = inventoryRepo.findAll();
        log.info("Fetched {} inventory items", inventoryList.size());
        return inventoryList.stream()
                .map(InventoryMapper::toDTO)
                .toList();
    }

    @Override
    public Optional<InventoryDTO> getInventoryById(int inventoryId) {
        Optional<Inventory> inventoryOptional = inventoryRepo.findById(inventoryId);
        if (inventoryOptional.isEmpty()) {
            log.warn("Inventory with ID {} not found", inventoryId);
            return Optional.empty();
        } else {
            log.info("Found inventory with ID {}", inventoryId);
            return inventoryOptional.map(InventoryMapper::toDTO);
        }
    }

    @Override
    public void createInventory(InventoryDTO inventoryDTO) {
        try {
            Inventory inventory = InventoryMapper.toEntity(inventoryDTO);
            if (inventory.getInventoryId() == 0) {
                inventoryRepo.save(inventory);
                log.info("Inventory created successfully: {}", inventory);
            } else {
                throw new IllegalArgumentException("Invalid inventory data or ID already exists");
            }
        } catch (Exception e) {
            log.error("Error creating inventory", e);
        }
    }

    @Override
    public void updateInventoryById(int inventoryId, InventoryDTO dto) {
        try {
            Optional<Inventory> optionalInventory = inventoryRepo.findById(inventoryId);
            if (optionalInventory.isPresent()) {
                Inventory inventory = optionalInventory.get();
                inventory.setDevice(dto.getDevice());
                inventory.setInventoryNumber(dto.getInventoryNumber());
                inventory.setRoom(dto.getRoom());
                inventory.setCabinet(dto.getCabinet());

                Inventory updatedInventory = inventoryRepo.save(inventory);

                for (Lending lending : updatedInventory.getLending()) {
                    lending.setInventory(updatedInventory);
                    lendingService.updateLending(lending.getLendingId(), lending);
                }

                log.info("Inventory updated successfully: {}", updatedInventory);
            } else {
                throw new NoSuchElementException("Inventory not found");
            }
        } catch (Exception e) {
            log.error("Error updating inventory with ID: {}", inventoryId, e);
        }
    }

    @Override
    public void deleteInventoryById(int inventoryId) {
        try {
            Optional<Inventory> optionalInventory = inventoryRepo.findById(inventoryId);
            if (optionalInventory.isPresent()) {
                Inventory inventory = optionalInventory.get();

                for (Lending lending : inventory.getLending()) {
                    lending.setInventory(null);
                }

                inventoryRepo.deleteById(inventoryId);
                log.info("Inventory deleted successfully with ID: {}", inventoryId);
            } else {
                throw new NoSuchElementException("Inventory not found");
            }
        } catch (Exception e) {
            log.error("Error deleting inventory with ID: {}", inventoryId, e);
        }
    }
}
