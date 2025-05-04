package com.example.inventorysystem.services.impl;

import com.example.inventorysystem.models.Inventory;
import com.example.inventorysystem.models.Lending;
import com.example.inventorysystem.models.dto.InventoryDTO;
import com.example.inventorysystem.repos.InventoryRepo;
import com.example.inventorysystem.services.LendingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class InventoryServiceImplTest {

    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Mock
    private InventoryRepo inventoryRepo;

    @Mock
    private LendingService lendingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<Inventory> inventories = List.of(
                new Inventory("Laptop", "INV123", "Room1", "CabinetA")
        );

        when(inventoryRepo.findAll()).thenReturn(inventories);

        List<InventoryDTO> result = inventoryService.getAll();

        assertEquals(1, result.size());
        assertEquals("Laptop", result.get(0).getDevice());
    }

    @Test
    void testGetInventoryById_Existing() {
        Inventory inventory = new Inventory("Monitor", "INV001", "RoomX", "CabinetY");
        inventory.setInventoryId(1);
        when(inventoryRepo.findById(1)).thenReturn(Optional.of(inventory));

        Optional<InventoryDTO> result = inventoryService.getInventoryById(1);

        assertTrue(result.isPresent());
        assertEquals("Monitor", result.get().getDevice());
    }

    @Test
    void testGetInventoryById_NonExisting() {
        when(inventoryRepo.findById(999)).thenReturn(Optional.empty());

        Optional<InventoryDTO> result = inventoryService.getInventoryById(999);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreateInventory_Valid() {
        InventoryDTO dto = new InventoryDTO();
        dto.setDevice("Printer");
        dto.setInventoryNumber("INV789");
        dto.setRoom("Room5");
        dto.setCabinet("CabinetZ");

        inventoryService.createInventory(dto);

        verify(inventoryRepo, times(1)).save(any(Inventory.class));
    }

    @Test
    void testCreateInventory_InvalidId() {
        InventoryDTO dto = new InventoryDTO();
        dto.setDevice("Phone");
        dto.setInventoryNumber("INV333");
        dto.setRoom("Room8");
        dto.setCabinet("CabinetX");
        dto.setInventoryId(99); // Should not be set during creation

        inventoryService.createInventory(dto);

        verify(inventoryRepo, never()).save(any());
    }

    @Test
    void testUpdateInventoryById_Existing() {
        int inventoryId = 1;
        Inventory inventory = new Inventory("OldDevice", "INV000", "OldRoom", "OldCabinet");
        inventory.setInventoryId(inventoryId);

        Lending lending = new Lending();
        lending.setLendingId(10);
        inventory.setLending(List.of(lending));

        when(inventoryRepo.findById(inventoryId)).thenReturn(Optional.of(inventory));
        when(inventoryRepo.save(any())).thenReturn(inventory);

        InventoryDTO dto = new InventoryDTO();
        dto.setDevice("UpdatedDevice");
        dto.setInventoryNumber("INV999");
        dto.setRoom("NewRoom");
        dto.setCabinet("NewCabinet");

        inventoryService.updateInventoryById(inventoryId, dto);

        assertEquals("UpdatedDevice", inventory.getDevice());
        assertEquals("INV999", inventory.getInventoryNumber());
        assertEquals("NewRoom", inventory.getRoom());
        assertEquals("NewCabinet", inventory.getCabinet());
        verify(lendingService).updateLending(10, lending);
    }

    @Test
    void testUpdateInventoryById_NonExisting() {
        when(inventoryRepo.findById(999)).thenReturn(Optional.empty());

        InventoryDTO dto = new InventoryDTO();
        dto.setDevice("NoUpdate");

        inventoryService.updateInventoryById(999, dto);

        verify(inventoryRepo, never()).save(any());
        verifyNoInteractions(lendingService);
    }

    @Test
    void testDeleteInventoryById_Existing() {
        int id = 1;
        Inventory inventory = new Inventory();
        Lending lending = new Lending();
        inventory.setLending(List.of(lending));

        when(inventoryRepo.findById(id)).thenReturn(Optional.of(inventory));

        inventoryService.deleteInventoryById(id);

        verify(inventoryRepo).deleteById(id);
    }

    @Test
    void testDeleteInventoryById_NonExisting() {
        when(inventoryRepo.findById(123)).thenReturn(Optional.empty());

        inventoryService.deleteInventoryById(123);

        verify(inventoryRepo, never()).deleteById(123);
        verifyNoInteractions(lendingService);
    }
}