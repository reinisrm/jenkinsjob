package com.example.InventorySystem.controllers;

import com.example.InventorySystem.models.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.InventorySystem.services.impl.InventoryServiceImpl;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final Logger log = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    InventoryServiceImpl inventoryService;

    @GetMapping("/")
    public String showAllInventory(Model model) {
        log.info("Get inventory list");
        try {
            List<Inventory> inventories = inventoryService.getAll();
            log.info("Inventory list size: {}", inventories.size());
            model.addAttribute("inventories", inventories);
            return "show-all-inventory";
        } catch (Exception e) {
            log.error("Error occurred while fetching all inventories", e);
            return "error";
        }
    }

    @GetMapping("/{inventoryId}")
    public String showOneInventory(@PathVariable("inventoryId") int inventoryId, Model model) {
        log.info("Get inventory with id: {}", inventoryId);
        Optional<Inventory> inventory = inventoryService.getInventoryById(inventoryId);

        if(inventory.isPresent()) {
            model.addAttribute("inventory", inventory);
            log.info("Received inventory with inventory number: {}", inventory.get().getInventoryNumber());
            return "show-one-inventory";
        } else {
            log.info("Inventory with id: {} not found", inventoryId);
            return "error";
        }
    }

    @GetMapping("/create")
    public String createInventoryForm(Model model) {
        log.info("Creating new inventory");
        model.addAttribute("inventory", new Inventory());
        return "create-inventory";
    }

    @PostMapping("/create")
    public String createInventory(@Valid Inventory inventory, BindingResult result) {
        if (result.hasErrors()) {
            log.error("Failed creating a new inventory, error: {}", result);
            return "create-inventory";
        }

        try {
            Inventory temp = new Inventory();
            temp.setDevice(inventory.getDevice());
            temp.setInventoryNumber(inventory.getInventoryNumber());
            temp.setRoom(inventory.getRoom());
            temp.setCabinet(inventory.getCabinet());

            inventoryService.createInventory(temp);
            log.info("Inventory created successfully: {}", temp);
            return "redirect:/inventory/";
        } catch (Exception e) {
            log.error("Error occurred while creating inventory", e);
            return "error";
        }
    }

    @GetMapping("/update/{inventoryId}")
    public String showUpdateForm(@PathVariable("inventoryId") int inventoryId, Model model) {
        try {
            Optional<Inventory> inventory = inventoryService.getInventoryById(inventoryId);

            if (inventory.isPresent()) {
                model.addAttribute("inventory", inventory.get());
                model.addAttribute("inventory_id", inventoryId);
                return "inventory-update-page";
            } else {
                return "redirect:/inventory/";
            }
        } catch (Exception e) {
            log.error("Error occurred while preparing update form for inventory with ID: {}", inventoryId, e);
            return "error";
        }
    }

    @PostMapping("/update/{inventoryId}")
    public String updateInventoryById(@PathVariable("inventoryId") int inventoryId, @Valid Inventory inventory,
                                      BindingResult result) {

        if (inventoryId <= 0) {
            log.warn("The id can only be positive, negative value provided: {}", inventoryId);
            return "inventory-update-page";
        }
        if (result.hasErrors()) {
            log.warn("Error updating inventory: {}", result.getAllErrors());
            return "inventory-update-page";
        }
        try {
            inventoryService.updateInventoryById(inventoryId, inventory);
            log.info("Inventory with id: {} has been updated", inventoryId);
            return "redirect:/inventory/{inventoryId}";
        } catch (Exception e) {
            log.error("Error occurred while updating inventory with ID: {}", inventoryId, e);
            return "error";
        }
    }

    @PostMapping("/delete/{inventoryId}")
    public String deleteInventoryById(@PathVariable("inventoryId") int inventoryId) {

        if (inventoryId <= 0) {
            log.warn("The id can only be positive, negative value provided: {}", inventoryId);
            return "show-one-inventory"; //TODO check if endpoint=correct
        }
        Optional<Inventory> inventory = inventoryService.getInventoryById(inventoryId);
        if (!inventory.isPresent()) {
            log.warn("Inventory with id: {} for delete is not found", inventoryId);
            return "show-one-inventory"; //TODO same here
        }

        inventoryService.deleteInventoryById(inventoryId);
        log.info("Inventory with id: {} is deleted", inventoryId);
        return "redirect:/inventory/";
    }
}


