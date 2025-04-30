package com.example.inventorysystem.controllers;

import com.example.inventorysystem.models.Inventory;
import com.example.inventorysystem.services.impl.InventoryServiceImpl;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    private Inventory setUpCreateInventory() {
        return new Inventory("Laptop", "INV123", "Room1", "CabinetA");
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(inventoryController).build();
    }

    @Test
    void testShowAllInventory() {
        List<Inventory> inventories = new ArrayList<>();
        inventories.add(setUpCreateInventory());
        when(inventoryService.getAll()).thenReturn(inventories);

        String viewName = inventoryController.showAllInventory(model);

        verify(model).addAttribute("inventories", inventories);
        assertEquals("show-all-inventory", viewName);
    }

    @Test
    void testShowOneInventory() throws Exception {
        Inventory inventory = setUpCreateInventory();
        inventory.setInventoryId(1);

        when(inventoryService.getInventoryById(inventory.getInventoryId())).thenReturn(Optional.of(inventory));

        mockMvc.perform(get("/inventory/{inventoryId}", inventory.getInventoryId()))
                .andExpect(status().isOk())
                .andExpect(view().name("show-one-inventory"))
                .andExpect(model().attribute("inventory", Optional.of(inventory)));
    }

    @Test
    void testShowOneInventoryNonExistantId() throws Exception {
        int nonExistantInventoryId = 999;
        when(inventoryService.getInventoryById(nonExistantInventoryId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/inventory/{inventoryId}", nonExistantInventoryId))
                .andExpect(status().isOk())
                .andExpect(view().name("error"));

        verify(inventoryService).getInventoryById(nonExistantInventoryId);
        verify(model, never()).addAttribute(eq("inventory"), any());
    }

    @Test
    void testCreateInventoryForm() {
        String viewName = inventoryController.createInventoryForm(model);

        verify(model).addAttribute(eq("inventory"), any(Inventory.class));
        assertEquals("create-inventory", viewName);
    }

    @Test
    void testCreateInventorySuccess() throws Exception {
        Inventory inventory = setUpCreateInventory();

        mockMvc.perform(post("/inventory/create")
                        .param("device", inventory.getDevice())
                        .param("inventoryNumber", inventory.getInventoryNumber())
                        .param("room", inventory.getRoom())
                        .param("cabinet", inventory.getCabinet()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/inventory/"));

        verify(inventoryService).createInventory(any(Inventory.class));
    }

    @Test
    void testCreateInventoryValidationFailed() {
        Inventory inventory = setUpCreateInventory();
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = inventoryController.createInventory(inventory, bindingResult);

        assertEquals("create-inventory", viewName);
    }

    @Test
    void testShowUpdateForm() {
        Inventory inventory = setUpCreateInventory();
        inventory.setInventoryId(1);
        when(inventoryService.getInventoryById(inventory.getInventoryId())).thenReturn(Optional.of(inventory));

        String viewName = inventoryController.showUpdateForm(inventory.getInventoryId(), model);

        verify(model).addAttribute("inventory", inventory);
        assertEquals("inventory-update-page", viewName);
    }

    @Test
    void testUpdateInventoryByIdValidationFailed() {
        Inventory inventory = setUpCreateInventory();
        inventory.setInventoryId(1);
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = inventoryController.updateInventoryById(inventory.getInventoryId(), inventory, bindingResult);

        assertEquals("inventory-update-page", viewName);
    }

    @Test
    void testDeleteInventoryById() throws Exception {
        Inventory inventory = setUpCreateInventory();
        inventory.setInventoryId(1);
        when(inventoryService.getInventoryById(inventory.getInventoryId())).thenReturn(Optional.of(inventory));

        mockMvc.perform(post("/inventory/delete/{inventoryId}", inventory.getInventoryId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/inventory/"));

        verify(inventoryService).deleteInventoryById(inventory.getInventoryId());
    }

}

