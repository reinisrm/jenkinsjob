package com.example.inventorysystem.controllers;

import com.example.inventorysystem.constants.ViewNames;
import com.example.inventorysystem.models.dto.InventoryDTO;
import com.example.inventorysystem.services.InventoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final Logger log = LoggerFactory.getLogger(InventoryController.class);
    private final InventoryService inventoryService;

    @Autowired
    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/")
    public String showAllInventory(Model model) {
        log.info("Fetching all inventory items");
        try {
            List<InventoryDTO> inventories = inventoryService.getAll();
            log.info("Inventory list size: {}", inventories.size());
            model.addAttribute("inventories", inventories);
            return ViewNames.SHOW_ALL_INVENTORIES;
        } catch (Exception e) {
            log.error("Error fetching inventory list", e);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/{inventoryId}")
    public String showOneInventory(@PathVariable("inventoryId") int inventoryId, Model model) {
        log.info("Fetching inventory with ID: {}", inventoryId);
        Optional<InventoryDTO> inventoryDTO = inventoryService.getInventoryById(inventoryId);

        if (inventoryDTO.isPresent()) {
            model.addAttribute(ViewNames.INVENTORY, inventoryDTO);
            log.info("Inventory found: {}", inventoryDTO.get().getInventoryNumber());
            return ViewNames.SHOW_ONE_INVENTORY;
        } else {
            log.warn("Inventory with ID {} not found", inventoryId);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/create")
    public String createInventoryForm(Model model) {
        log.info("Creating form for new inventory");
        model.addAttribute(ViewNames.INVENTORY, new InventoryDTO());
        return ViewNames.CREATE_INVENTORY;
    }

    @PostMapping("/create")
    public String createInventory(@Valid @ModelAttribute("inventory") InventoryDTO inventoryDTO, BindingResult result) {
        if (result.hasErrors()) {
            log.warn("Validation errors while creating inventory: {}", result.getAllErrors());
            return ViewNames.CREATE_INVENTORY;
        }

        try {
            inventoryService.createInventory(inventoryDTO);
            log.info("Inventory created: {}", inventoryDTO);
            return ViewNames.REDIRECT_INVENTORY;
        } catch (Exception e) {
            log.error("Error creating inventory", e);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/update/{inventoryId}")
    public String showUpdateForm(@PathVariable("inventoryId") int inventoryId, Model model) {
        try {
            Optional<InventoryDTO> inventoryDTO = inventoryService.getInventoryById(inventoryId);

            if (inventoryDTO.isPresent()) {
                model.addAttribute(ViewNames.INVENTORY, inventoryDTO.get());
                model.addAttribute("inventory_id", inventoryId);
                return ViewNames.INVENTORY_UPDATE;
            } else {
                log.warn("Inventory with ID {} not found for update", inventoryId);
                return ViewNames.REDIRECT_INVENTORY;
            }
        } catch (Exception e) {
            log.error("Error preparing update form for inventory ID: {}", inventoryId, e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/update/{inventoryId}")
    public String updateInventoryById(@PathVariable("inventoryId") int inventoryId,
                                      @Valid @ModelAttribute("inventory") InventoryDTO inventoryDTO,
                                      BindingResult result) {

        if (inventoryId <= 0) {
            log.warn("Invalid inventory ID: {}", inventoryId);
            return ViewNames.INVENTORY_UPDATE;
        }

        if (result.hasErrors()) {
            log.warn("Validation errors while updating inventory: {}", result.getAllErrors());
            return ViewNames.INVENTORY_UPDATE;
        }

        try {
            inventoryService.updateInventoryById(inventoryId, inventoryDTO);
            log.info("Inventory updated with ID: {}", inventoryId);
            return "redirect:/inventory/{inventoryId}";
        } catch (Exception e) {
            log.error("Error updating inventory ID: {}", inventoryId, e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/delete/{inventoryId}")
    public String deleteInventoryById(@PathVariable("inventoryId") int inventoryId) {

        if (inventoryId <= 0) {
            log.warn("Invalid inventory ID for deletion: {}", inventoryId);
            return ViewNames.SHOW_ONE_INVENTORY;
        }

        Optional<InventoryDTO> inventory = inventoryService.getInventoryById(inventoryId);
        if (!inventory.isPresent()) {
            log.warn("Inventory ID {} not found for deletion", inventoryId);
            return ViewNames.SHOW_ONE_INVENTORY;
        }

        try {
            inventoryService.deleteInventoryById(inventoryId);
            log.info("Inventory deleted with ID: {}", inventoryId);
            return ViewNames.REDIRECT_INVENTORY;
        } catch (Exception e) {
            log.error("Error deleting inventory ID: {}", inventoryId, e);
            return ViewNames.ERROR;
        }
    }
}
