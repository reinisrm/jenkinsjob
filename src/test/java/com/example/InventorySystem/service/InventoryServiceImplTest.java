package com.example.InventorySystem.service;

import com.example.InventorySystem.models.Inventory;
import com.example.InventorySystem.models.Lending;
import com.example.InventorySystem.repos.InventoryRepo;
import com.example.InventorySystem.services.LendingService;
import com.example.InventorySystem.services.impl.InventoryServiceImpl;
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
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAll() {
        List<Inventory> expectedInventories = new ArrayList<>();
        expectedInventories.add(new Inventory("Laptop", "INV123", "Room1", "CabinetA"));

        when(inventoryRepo.findAll()).thenReturn(expectedInventories);

        List<Inventory> actualInventories = inventoryService.getAll();

        assertEquals(expectedInventories, actualInventories);
    }

    @Test
    void testGetInventoryById_ExistingId() {
        int existingId = 1;
        Inventory expectedInventory = new Inventory("Laptop", "INV123", "Room1", "CabinetA");
        expectedInventory.setInventoryId(existingId);

        when(inventoryRepo.findById(existingId)).thenReturn(Optional.of(expectedInventory));

        Optional<Inventory> actualInventoryOptional = inventoryService.getInventoryById(existingId);
        assertTrue(actualInventoryOptional.isPresent());
        assertEquals(expectedInventory, actualInventoryOptional.get());
    }

    @Test
    void testGetInventoryById_NonExistingId() {
        int nonExistingId = 999;

        when(inventoryRepo.findById(nonExistingId)).thenReturn(Optional.empty());

        Optional<Inventory> actualInventoryOptional = inventoryService.getInventoryById(nonExistingId);
        assertTrue(actualInventoryOptional.isEmpty());
    }

    @Test
    void testCreateInventory() {
        Inventory inventoryToCreate = new Inventory("Laptop", "INV123", "Room1", "CabinetA");

        inventoryService.createInventory(inventoryToCreate);

        verify(inventoryRepo).save(inventoryToCreate);
    }

    @Test
    void testUpdateInventoryById_ExistingId() {
        int existingId = 1;
        Inventory existingInventory = new Inventory("Laptop", "INV123", "Room1", "CabinetA");
        existingInventory.setInventoryId(existingId);

        List<Lending> lendings = new ArrayList<>();
        Lending lending = new Lending();
        lendings.add(lending);
        existingInventory.setLending(lendings);

        Inventory updatedInventoryData = new Inventory("Desktop", "INV456", "Room2", "CabinetB");

        when(inventoryRepo.findById(existingId)).thenReturn(Optional.of(existingInventory));
        when(inventoryRepo.save(any(Inventory.class))).thenReturn(existingInventory);

        inventoryService.updateInventoryById(existingId, updatedInventoryData);

        verify(inventoryRepo, times(1)).save(existingInventory);
        assertEquals("Desktop", existingInventory.getDevice());
        assertEquals("INV456", existingInventory.getInventoryNumber());
        assertEquals("Room2", existingInventory.getRoom());
        assertEquals("CabinetB", existingInventory.getCabinet());
        verify(lendingService, times(lendings.size())).updateLending(anyInt(), any(Lending.class));
    }


    @Test
    void testUpdateInventoryById_NonExistingId() {
        int nonExistingId = 99;
        Inventory updatedInventoryData = new Inventory("Desktop", "INV456", "Room2", "CabinetB");

        when(inventoryRepo.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                inventoryService.updateInventoryById(nonExistingId, updatedInventoryData));
    }

    @Test
    void testDeleteInventoryById_ExistingInventory_Success() {
        int inventoryId = 1;
        Inventory inventory = new Inventory();
        inventory.setLending(Collections.emptyList());  // Ensuring lending list is not null but empty

        when(inventoryRepo.findById(inventoryId)).thenReturn(Optional.of(inventory));

        inventoryService.deleteInventoryById(inventoryId);

        verify(inventoryRepo).deleteById(inventoryId);
    }


    @Test
    void testDeleteInventoryById_NonExistingId() {
        int nonExistingId = 999;

        when(inventoryRepo.findById(nonExistingId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> inventoryService.deleteInventoryById(nonExistingId));

        verify(inventoryRepo, never()).deleteById(nonExistingId);

        verifyNoInteractions(lendingService);
    }
}
