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

    private final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    @Autowired
    InventoryServiceImpl inventoryService;

    @GetMapping("/")
    public String showAllInventory(Model model) {
        try {
            List<Inventory> inventories = inventoryService.getAll();
            model.addAttribute("inventories", inventories);
            return "show-all-inventory";
        } catch (Exception e) {
            logger.error("Error occurred while fetching all inventories", e);
            return "error";
        }
    }

    @GetMapping("/{inventoryId}")
    public String showOneInventory(@PathVariable("inventoryId") int inventoryId, Model model) {
        try {
            Optional<Inventory> inventory = inventoryService.getInventoryById(inventoryId);
            model.addAttribute("inventory", inventory);
            return "show-one-inventory";
        } catch (Exception e) {
            logger.error("Error occurred while fetching inventory with ID: {}", inventoryId, e);
            return "error";
        }
    }

    @GetMapping("/create")
    public String createInventoryForm(Model model) {
        model.addAttribute("inventory", new Inventory());
        return "create-inventory";
    }

    @PostMapping("/create")
    public String createInventory(@Valid Inventory inventory, BindingResult result) {
        if (result.hasErrors()) {
            return "create-inventory";
        }

        try {
            Inventory temp = new Inventory();
            temp.setDevice(inventory.getDevice());
            temp.setInventoryNumber(inventory.getInventoryNumber());
            temp.setRoom(inventory.getRoom());
            temp.setCabinet(inventory.getCabinet());
            inventoryService.createInventory(temp);
            return "redirect:/inventory/";
        } catch (Exception e) {
            logger.error("Error occurred while creating inventory", e);
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
            logger.error("Error occurred while preparing update form for inventory with ID: {}", inventoryId, e);
            return "error";
        }
    }

    @PostMapping("/update/{inventoryId}")
    public String updateInventoryById(@PathVariable("inventoryId") int inventoryId, @Valid Inventory inventory,
                                      BindingResult result) {
        if (result.hasErrors()) {
            return "inventory-update-page";
        }
        try {
            inventoryService.updateInventoryById(inventoryId, inventory);
            return "redirect:/inventory/{inventoryId}";
        } catch (Exception e) {
            logger.error("Error occurred while updating inventory with ID: {}", inventoryId, e);
            return "error";
        }
    }

    @PostMapping("/delete/{inventoryId}")
    public String deleteInventoryById(@PathVariable("inventoryId") int inventoryId) {
        try {
            inventoryService.deleteInventoryById(inventoryId);
            return "redirect:/inventory/";
        } catch (Exception e) {
            logger.error("Error occurred while deleting inventory with ID: {}", inventoryId, e);
            return "error";
        }
    }
}

