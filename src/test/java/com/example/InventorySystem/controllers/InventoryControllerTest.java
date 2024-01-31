package com.example.InventorySystem.controllers;

import com.example.InventorySystem.models.Inventory;
import com.example.InventorySystem.services.impl.InventoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class InventoryControllerTest {

    @InjectMocks
    private InventoryController inventoryController;

    @Mock
    private InventoryServiceImpl inventoryService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    private MockMvc mockMvc;

    private List<Inventory> inventoryList;

    @BeforeEach
    void setUp() {
        inventoryList = new ArrayList<>();
        Inventory inventory1 = new Inventory("Laptop", "INV123", "Room1", "CabinetA");
        Inventory inventory2 = new Inventory("Desktop", "INV456", "Room2", "CabinetB");
        inventoryList.add(inventory1);
        inventoryList.add(inventory2);
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
    }

    @Test
    void testShowAllInventory() {
        when(inventoryService.getAll()).thenReturn(inventoryList);

        String viewName = inventoryController.showAllInventory(model);

        verify(model).addAttribute(eq("inventories"), eq(inventoryList));
        assertEquals("show-all-inventory", viewName);
    }

    @Test
    void testShowOneInventory() throws Exception {
        // Create a sample inventory
        Inventory inventory = new Inventory();
        inventory.setInventoryId(1);
        inventory.setDevice("Sample Device");
        inventory.setInventoryNumber("INV-123");
        inventory.setRoom("Room A");
        inventory.setCabinet("Cabinet B");

        // Mock the service to return the sample inventory
        when(inventoryService.getInventoryById(anyInt())).thenReturn(Optional.of(inventory));

        // Perform the GET request and validate the model attribute
        mockMvc.perform(get("/inventory/{inventoryId}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("show-one-inventory"))
                .andExpect(model().attribute("inventory", Optional.of(inventory)));
    }

    @Test
    void testCreateInventoryForm() {
        String viewName = inventoryController.createInventoryForm(model);

        verify(model).addAttribute(eq("inventory"), any(Inventory.class));
        assertEquals("create-inventory", viewName);
    }

    @Test
    void testCreateInventorySuccess() {
        Inventory inventory = new Inventory("Laptop", "INV123", "Room1", "CabinetA");
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = inventoryController.createInventory(inventory, bindingResult);

        assertEquals("redirect:/inventory/", viewName);
        verify(inventoryService).createInventory(any(Inventory.class));
    }

    @Test
    void testCreateInventoryValidationFailed() {
        Inventory inventory = new Inventory();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = inventoryController.createInventory(inventory, bindingResult);

        assertEquals("create-inventory", viewName);
    }

    @Test
    void testShowUpdateForm() {
        int inventoryId = 1;
        Optional<Inventory> inventory = Optional.of(new Inventory());
        when(inventoryService.getInventoryById(inventoryId)).thenReturn(inventory);

        String viewName = inventoryController.showUpdateForm(inventoryId, model);

        verify(model).addAttribute("inventory", inventory.get());
        verify(model).addAttribute("inventory_id", inventoryId);
        assertEquals("inventory-update-page", viewName);
    }

    @Test
    void testUpdateInventoryByIdValidationFailed() {
        int inventoryId = 1;
        Inventory updatedInventory = new Inventory();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = inventoryController.updateInventoryById(inventoryId, updatedInventory, bindingResult);

        assertEquals("inventory-update-page", viewName);
    }

    @Test
    void testDeleteInventoryById() {
        int inventoryId = 1;

        String viewName = inventoryController.deleteInventoryById(inventoryId);

        assertEquals("redirect:/inventory/", viewName);
        verify(inventoryService).deleteInventoryById(inventoryId);
    }
}
