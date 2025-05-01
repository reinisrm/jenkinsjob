package com.example.inventorysystem.mappers;

import com.example.inventorysystem.models.Inventory;
import com.example.inventorysystem.models.dto.InventoryDTO;

public class InventoryMapper {
    public static InventoryDTO toDTO(Inventory inventory) {
        InventoryDTO dto = new InventoryDTO();
        dto.setInventoryId(inventory.getInventoryId());
        dto.setDevice(inventory.getDevice());
        dto.setInventoryNumber(inventory.getInventoryNumber());
        dto.setRoom(inventory.getRoom());
        dto.setCabinet(inventory.getCabinet());
        return dto;
    }

    public static Inventory toEntity(InventoryDTO dto) {
        Inventory inventory = new Inventory();
        inventory.setInventoryId(dto.getInventoryId());
        inventory.setDevice(dto.getDevice());
        inventory.setInventoryNumber(dto.getInventoryNumber());
        inventory.setRoom(dto.getRoom());
        inventory.setCabinet(dto.getCabinet());
        return inventory;
    }
}
