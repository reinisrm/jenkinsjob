package com.example.inventorysystem.services;

import com.example.inventorysystem.models.dto.InventoryDTO;

import java.util.List;
import java.util.Optional;

public interface InventoryService {
    List<InventoryDTO> getAll();
    Optional<InventoryDTO> getInventoryById(int inventoryId);
    void createInventory(InventoryDTO inventoryDTO);
    void updateInventoryById(int inventoryId, InventoryDTO dto);
    void deleteInventoryById(int inventoryId);
}
