package com.example.inventorysystem.controllers;

import com.example.inventorysystem.constants.ViewNames;
import com.example.inventorysystem.models.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.inventorysystem.services.impl.InventoryServiceImpl;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final Logger log = LoggerFactory.getLogger(InventoryController.class);

    private final InventoryServiceImpl inventoryService;

    @Autowired
    public InventoryController(InventoryServiceImpl inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/")
    public String showAllInventory(Model model) {
        log.info("Get inventory list");
        try {
            List<Inventory> inventories = inventoryService.getAll();
            log.info("Inventory list size: {}", inventories.size());
            model.addAttribute("inventories", inventories);
            return ViewNames.SHOW_ALL_INVENTORIES;
        } catch (Exception e) {
            log.error("Error occurred while fetching all inventories", e);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/{inventoryId}")
    public String showOneInventory(@PathVariable("inventoryId") int inventoryId, Model model) {
        log.info("Get inventory with id: {}", inventoryId);
        Optional<Inventory> inventory = inventoryService.getInventoryById(inventoryId);

        if(inventory.isPresent()) {
            model.addAttribute(ViewNames.INVENTORY, inventory);
            log.info("Received inventory with inventory number: {}", inventory.get().getInventoryNumber());
            return ViewNames.SHOW_ONE_INVENTORY;
        } else {
            log.info("Inventory with id: {} not found", inventoryId);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/create")
    public String createInventoryForm(Model model) {
        log.info("Creating new inventory");
        model.addAttribute(ViewNames.INVENTORY, new Inventory());
        return ViewNames.CREATE_INVENTORY;
    }

    @PostMapping("/create")
    public String createInventory(@Valid Inventory inventory, BindingResult result) {
        if (result.hasErrors()) {
            log.error("Failed creating a new inventory, error: {}", result);
            return ViewNames.CREATE_INVENTORY;
        }

        try {
            inventoryService.createInventory(inventory);
            log.info("Inventory created successfully: {}", inventory);
            return ViewNames.REDIRECT_INVENTORY;
        } catch (Exception e) {
            log.error("Error occurred while creating inventory", e);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/update/{inventoryId}")
    public String showUpdateForm(@PathVariable("inventoryId") int inventoryId, Model model) {
        try {
            Optional<Inventory> inventory = inventoryService.getInventoryById(inventoryId);

            if (inventory.isPresent()) {
                model.addAttribute(ViewNames.INVENTORY, inventory.get());
                model.addAttribute("inventory_id", inventoryId);
                return ViewNames.INVENTORY_UPDATE;
            } else {
                return ViewNames.REDIRECT_INVENTORY;
            }
        } catch (Exception e) {
            log.error("Error occurred while preparing update form for inventory with ID: {}", inventoryId, e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/update/{inventoryId}")
    public String updateInventoryById(@PathVariable("inventoryId") int inventoryId, @Valid Inventory inventory,
                                      BindingResult result) {

        if (inventoryId <= 0) {
            log.warn("The id can only be positive, negative value provided: {}", inventoryId);
            return ViewNames.INVENTORY_UPDATE;
        }
        if (result.hasErrors()) {
            log.warn("Error updating inventory: {}", result.getAllErrors());
            return ViewNames.INVENTORY_UPDATE;
        }
        try {
            inventoryService.updateInventoryById(inventoryId, inventory);
            log.info("Inventory with id: {} has been updated", inventoryId);
            return "redirect:/inventory/{inventoryId}";
        } catch (Exception e) {
            log.error("Error occurred while updating inventory with ID: {}", inventoryId, e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/delete/{inventoryId}")
    public String deleteInventoryById(@PathVariable("inventoryId") int inventoryId) {

        if (inventoryId <= 0) {
            log.warn("The id can only be positive, negative value provided: {}", inventoryId);
            return ViewNames.SHOW_ONE_INVENTORY;
        }
        Optional<Inventory> inventory = inventoryService.getInventoryById(inventoryId);
        if (!inventory.isPresent()) {
            log.warn("Inventory with id: {} for delete is not found", inventoryId);
            return ViewNames.SHOW_ONE_INVENTORY;
        }

        inventoryService.deleteInventoryById(inventoryId);
        log.info("Inventory with id: {} is deleted", inventoryId);
        return ViewNames.REDIRECT_INVENTORY;
    }
}