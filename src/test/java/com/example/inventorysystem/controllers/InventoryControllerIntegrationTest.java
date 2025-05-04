package com.example.inventorysystem.controllers;

import com.example.inventorysystem.models.dto.InventoryDTO;
import com.example.inventorysystem.repos.InventoryRepo;
import com.example.inventorysystem.services.InventoryService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class InventoryControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InventoryRepo inventoryRepo;

    @Autowired
    private InventoryService inventoryService;

    private InventoryDTO inventory;

    @BeforeEach
    void setup() {
        inventoryRepo.deleteAll();

        inventory = new InventoryDTO();
        inventory.setDevice("Laptop");
        inventory.setInventoryNumber("INV-123");
        inventory.setRoom("Room A");
        inventory.setCabinet("Cabinet 1");

        inventoryService.createInventory(inventory);
    }

    @Test
    @WithMockUser
    void testShowAllInventory() throws Exception {
        mockMvc.perform(get("/inventory/"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("INV-123")));
    }

    @Test
    @WithMockUser
    void testShowOneInventory() throws Exception {
        int id = inventoryService.getAll().get(0).getInventoryId();

        mockMvc.perform(get("/inventory/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Laptop")));
    }

    @Test
    @WithMockUser
    void testCreateInventoryForm() throws Exception {
        mockMvc.perform(get("/inventory/create"))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("form")));
    }

    @Test
    @WithMockUser
    void testCreateInventoryValid() throws Exception {
        mockMvc.perform(post("/inventory/create")
                        .param("device", "Monitor")
                        .param("inventoryNumber", "INV-456")
                        .param("room", "Room B")
                        .param("cabinet", "Cabinet 2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/inventory/"));
    }

    @Test
    @WithMockUser
    void testShowUpdateForm() throws Exception {
        int id = inventoryService.getAll().get(0).getInventoryId();

        mockMvc.perform(get("/inventory/update/" + id))
                .andExpect(status().isOk())
                .andExpect(content().string(Matchers.containsString("Laptop")));
    }

    @Test
    @WithMockUser
    void testUpdateInventoryValid() throws Exception {
        int id = inventoryService.getAll().get(0).getInventoryId();

        mockMvc.perform(post("/inventory/update/" + id)
                        .param("device", "Updated Laptop")
                        .param("inventoryNumber", "INV-123")
                        .param("room", "Room Z")
                        .param("cabinet", "Cabinet X"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/inventory/" + id));
    }

    @Test
    @WithMockUser
    void testDeleteInventoryValid() throws Exception {
        int id = inventoryService.getAll().get(0).getInventoryId();

        mockMvc.perform(post("/inventory/delete/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/inventory/"));
    }
}