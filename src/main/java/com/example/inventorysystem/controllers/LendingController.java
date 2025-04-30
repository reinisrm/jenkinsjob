package com.example.inventorysystem.controllers;

import com.example.inventorysystem.constants.ViewNames;
import com.example.inventorysystem.models.Inventory;
import com.example.inventorysystem.models.Lending;
import com.example.inventorysystem.models.Person;
import com.example.inventorysystem.services.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/lending")
public class LendingController {

    private final Logger log = LoggerFactory.getLogger(LendingController.class);

    private final LendingServiceImpl lendingService;
    private final InventoryServiceImpl inventoryService;
    private final PersonServiceImpl personService;

    @Autowired
    public LendingController(LendingServiceImpl lendingService, InventoryServiceImpl inventoryService,
                             PersonServiceImpl personService) {
        this.lendingService = lendingService;
        this.inventoryService = inventoryService;
        this.personService = personService;
    }


    @GetMapping("/")
    public String showAllLending(Model model) {
        log.info("Get lending list");
        try {
            List<Lending> lendings = lendingService.getAll();
            log.info("Lending list size: {}", lendings.size());
            model.addAttribute("lendings", lendings);
            return ViewNames.SHOW_ALL_LENDING;
        } catch (Exception e) {
            log.error("Error occurred while fetching all lendings", e);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/{lendingId}")
    public String showOneLending(@PathVariable("lendingId") int lendingId, Model model) {
        log.info("Get lending with id: {}", lendingId);
        Optional<Lending> lending = lendingService.getLendingById(lendingId);

        if (lending.isPresent()) {
            model.addAttribute(ViewNames.LENDING, lending);
            log.info("Lending with id: {} received successfully", lendingId);
            return ViewNames.SHOW_ONE_LENDING;
        } else {
            log.error("Lending with id: {} not found", lendingId);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/create")
    public String createLendingForm(Model model) {
        log.info("Creating new lending");
        try {
            model.addAttribute(ViewNames.LENDING, new Lending());
            List<Inventory> inventoryList = inventoryService.getAll();
            model.addAttribute("inventoryList", inventoryList);
            List<Person> borrowerList = personService.getAll();
            model.addAttribute("borrowerList", borrowerList);
            List<Person> lenderList = personService.getAll();
            model.addAttribute("lenderList", lenderList);
            return ViewNames.CREATE_LENDING;
        } catch (Exception e) {
            log.error("Error occurred while preparing create lending form", e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/create")
    public String createLending(@Valid Lending lending, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            log.error("Failed creating a new lending, error: {}", result);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.lending", result);
            attributes.addFlashAttribute(ViewNames.LENDING, lending);
            return "redirect:/lending/create";
        }
        try {
            lendingService.createLending(lending);
            log.info("Lending created successfully: {}", lending);
            return ViewNames.REDIRECT_LENDING;
        } catch (IllegalArgumentException e) {
            log.error("Error creating lending record: {}", e.getMessage());
            return ViewNames.ERROR;
        } catch (Exception e) {
            log.error("Unexpected error creating lending record", e);
            return ViewNames.ERROR;
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

            return ViewNames.LENDING_UPDATE;
        } catch (Exception e) {
            log.error("Error occurred while preparing update form for lending with ID: {}", lendingId, e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/update/{lendingId}")
    public String updateLending(@PathVariable("lendingId") int lendingId, @Valid Lending lending,
                                BindingResult result) {
        if (lendingId <= 0) {
            log.warn("The id can only be positive, negative value provided: {}", lendingId);
            return ViewNames.LENDING_UPDATE;
        }
        if (result.hasErrors()) {
            log.warn("Error updating lending: {}", result.getAllErrors());
            return ViewNames.LENDING_UPDATE;
        }
        try {
            lendingService.updateLending(lendingId, lending);
            log.info("Lending with id: {} has been updated", lendingId);
            return "redirect:/lending/{lendingId}";
        } catch (Exception e) {
            log.error("Error occurred while updating lending with ID: {}", lendingId, e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/delete/{lendingId}")
    public String deleteLending(@PathVariable("lendingId") int lendingId) {
        if (lendingId <= 0) {
            log.warn("The id can only be positive, negative value provided: {}", lendingId);
        }
        Optional<Lending> lending = lendingService.getLendingById(lendingId);
        if (!lending.isPresent()) {
            log.warn("Lending with id: {} for delete is not found", lendingId);
        }
        lendingService.deleteLendingById(lendingId);
        log.info("Lending with id: {} is deleted", lendingId);
        return ViewNames.REDIRECT_LENDING;
    }
}