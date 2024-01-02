package controllers;

import models.Inventory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import services.impl.InventoryServiceImpl;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    InventoryServiceImpl inventoryService;

    @GetMapping("/")
    public String showAllInventory(Model model) {
        List<Inventory> inventories = inventoryService.getAll();
        model.addAttribute("inventories", inventories);
        return "show-all-inventory";
    }

    @GetMapping("/{inventoryId}")
    public String showOneInventory(@PathVariable("inventoryId") int inventoryId, Model model) {
        Optional<Inventory> inventory = inventoryService.getInventoryById(inventoryId);
        model.addAttribute("inventory", inventory);
        return "show-one-inventory";
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

        Inventory temp = new Inventory();
        temp.setDevice(inventory.getDevice());
        temp.setInventoryNumber(inventory.getInventoryNumber());
        temp.setRoom(inventory.getRoom());

        inventoryService.createInventory(temp);
        return "redirect:/inventory/";
    }

    @GetMapping("/update/{inventoryId}")
    public String showUpdateForm(@PathVariable("inventoryId") int inventoryId, Model model) {
        Optional<Inventory> inventory = inventoryService.getInventoryById(inventoryId);
        model.addAttribute("inventory", inventory);
        return "inventory-update-page";
    }

    @PostMapping("/update/{inventoryId}")
    public String updateInventoryById(@PathVariable("inventoryId") int inventoryId, @Valid Inventory inventory,
                                  BindingResult result) {
        if (result.hasErrors()) {
            return "inventory-update-page";
        }
        inventoryService.updateInventoryById(inventoryId, inventory);
        return "redirect:/inventory/{inventoryId}";
    }

    @DeleteMapping("/delete/{inventoryId}")
    public String deleteInventoryById(@PathVariable("inventoryId") int inventoryId) {
        inventoryService.deleteInventoryById(inventoryId);
        return "redirect:/inventory/";
    }
}

