package com.example.InventorySystem.controllers;

import com.example.InventorySystem.models.Inventory;
import com.example.InventorySystem.models.Lending;
import com.example.InventorySystem.models.Person;
import com.example.InventorySystem.services.impl.InventoryServiceImpl;
import com.example.InventorySystem.services.impl.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.InventorySystem.services.impl.LendingServiceImpl;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/lending")
public class LendingController {

    private final Logger logger = LoggerFactory.getLogger(LendingController.class);

    @Autowired
    LendingServiceImpl lendingService;

    @Autowired
    InventoryServiceImpl inventoryService;

    @Autowired
    PersonServiceImpl personService;

    @GetMapping("/")
    public String showAllLending(Model model) {
        try {
            List<Lending> lendings = lendingService.getAll();
            model.addAttribute("lendings", lendings);
            return "show-all-lending";
        } catch (Exception e) {
            logger.error("Error occurred while fetching all lendings", e);
            return "error";
        }
    }

    @GetMapping("/{lendingId}")
    public String showOneLending(@PathVariable("lendingId") int lendingId, Model model) {
        try {
            Optional<Lending> lending = lendingService.getLendingById(lendingId);
            model.addAttribute("lending", lending);
            return "show-one-lending";
        } catch (Exception e) {
            logger.error("Error occurred while fetching lending with ID: {}", lendingId, e);
            return "error";
        }
    }

    @GetMapping("/create")
    public String createLendingForm(Model model) {
        try {
            model.addAttribute("lending", new Lending());
            List<Inventory> inventoryList = inventoryService.getAll();
            model.addAttribute("inventoryList", inventoryList);
            List<Person> borrowerList = personService.getAll();
            model.addAttribute("borrowerList", borrowerList);
            List<Person> lenderList = personService.getAll();
            model.addAttribute("lenderList", lenderList);
            return "create-lending";
        } catch (Exception e) {
            logger.error("Error occurred while preparing create lending form", e);
            return "error";
        }
    }

    @PostMapping("/create")
    public String createLending(@Valid Lending lending, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.lending", result);
            attributes.addFlashAttribute("lending", lending);
            return "redirect:/lending/create";
        }

        try {
            lendingService.createLending(lending);
            return "redirect:/lending/";
        } catch (IllegalArgumentException e) {
            logger.error("Error creating lending record: {}", e.getMessage());
            return "error";
        } catch (Exception e) {
            logger.error("Unexpected error creating lending record", e);
            return "error";
        }
    }

    @GetMapping("/update/{lendingId}")
    public String showUpdateForm(@PathVariable("lendingId") int lendingId, Model model) {
        try {
            Optional<Lending> lending = lendingService.getLendingById(lendingId);

            List<Inventory> inventoryList = inventoryService.getAll();
            List<Person> borrowerList = personService.getAll();
            List<Person> lenderList = personService.getAll();

            model.addAttribute("lending", lending.orElse(new Lending()));
            model.addAttribute("inventoryList", inventoryList);
            model.addAttribute("borrowerList", borrowerList);
            model.addAttribute("lenderList", lenderList);

            return "lending-update-page";
        } catch (Exception e) {
            logger.error("Error occurred while preparing update form for lending with ID: {}", lendingId, e);
            return "error";
        }
    }

    @PostMapping("/update/{lendingId}")
    public String updateLending(@PathVariable("lendingId") int lendingId, @Valid Lending lending,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "lending-update-page";
        }
        try {
            lendingService.updateLending(lendingId, lending);
            return "redirect:/lending/{lendingId}";
        } catch (Exception e) {
            logger.error("Error occurred while updating lending with ID: {}", lendingId, e);
            return "error";
        }
    }

    @PostMapping("/delete/{lendingId}")
    public String deleteLending(@PathVariable("lendingId") int lendingId) {
        try {
            lendingService.deleteLendingById(lendingId);
            return "redirect:/lending/";
        } catch (Exception e) {
            logger.error("Error occurred while deleting lending with ID: {}", lendingId, e);
            return "error";
        }
    }
}