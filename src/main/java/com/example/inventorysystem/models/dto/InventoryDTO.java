package com.example.inventorysystem.models.dto;

import lombok.Data;

@Data
public class InventoryDTO {
    private int inventoryId;
    private String device;
    private String inventoryNumber;
    private String room;
    private String cabinet;
}