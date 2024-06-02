package com.example.InventorySystem.controllers;

import com.example.InventorySystem.models.Inventory;
import com.example.InventorySystem.models.Lending;
import com.example.InventorySystem.models.Person;
import com.example.InventorySystem.services.impl.AcceptanceActServiceImpl;
import com.example.InventorySystem.services.impl.InventoryServiceImpl;
import com.example.InventorySystem.services.impl.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.example.InventorySystem.services.impl.LendingServiceImpl;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Controller
@RequestMapping("/lending")
public class LendingController {

    private final Logger log = LoggerFactory.getLogger(LendingController.class);

    @Autowired
    LendingServiceImpl lendingService;

    @Autowired
    InventoryServiceImpl inventoryService;

    @Autowired
    PersonServiceImpl personService;

    @Autowired
    AcceptanceActServiceImpl acceptanceActService;

    @GetMapping("/")
    public String showAllLending(Model model) {
        log.info("Get lending list");
        try {
            List<Lending> lendings = lendingService.getAll();
            log.info("Lending list size: {}", lendings.size());
            model.addAttribute("lendings", lendings);
            return "show-all-lending";
        } catch (Exception e) {
            log.error("Error occurred while fetching all lendings", e);
            return "error";
        }
    }

    @GetMapping("/{lendingId}")
    public String showOneLending(@PathVariable("lendingId") int lendingId, Model model) {
        log.info("Get lending with id: {}", lendingId);
        Optional<Lending> lending = lendingService.getLendingById(lendingId);

        if (lending.isPresent()) {
            model.addAttribute("lending", lending);
            log.info("Lending with id: {} received successfully", lendingId);
            return "show-one-lending";
        } else {
            log.error("Lending with id: {} not found", lendingId);
            return "error";
        }
    }

    @GetMapping("/create")
    public String createLendingForm(Model model) {
        log.info("Creating new lending");
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
            log.error("Error occurred while preparing create lending form", e);
            return "error";
        }
    }

    @PostMapping("/create")
    public String createLending(@Valid Lending lending, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            log.error("Failed creating a new lending, error: {}", result);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.lending", result);
            attributes.addFlashAttribute("lending", lending);
            return "redirect:/lending/create";
        }

        try {
            lendingService.createLending(lending);
            log.info("Lending created successfully: {}", lending);
            return "redirect:/lending/";
        } catch (IllegalArgumentException e) {
            log.error("Error creating lending record: {}", e.getMessage());
            return "error";
        } catch (Exception e) {
            log.error("Unexpected error creating lending record", e);
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
            log.error("Error occurred while preparing update form for lending with ID: {}", lendingId, e);
            return "error";
        }
    }

    @PostMapping("/update/{lendingId}")
    public String updateLending(@PathVariable("lendingId") int lendingId, @Valid Lending lending,
                                BindingResult result) {
        if (lendingId <= 0) {
            log.warn("The id can only be positive, negative value provided: {}", lendingId);
            return "lending-update-page";
        }
        if (result.hasErrors()) {
            log.warn("Error updating lending: {}", result.getAllErrors());
            return "lending-update-page";
        }
        try {
            lendingService.updateLending(lendingId, lending);
            log.info("Lending with id: {} has been updated", lendingId);
            return "redirect:/lending/{lendingId}";
        } catch (Exception e) {
            log.error("Error occurred while updating lending with ID: {}", lendingId, e);
            return "error";
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
        return "redirect:/lending/";
    }

    @GetMapping("/generateAcceptanceAct/{lendingId}")
    public ResponseEntity<InputStreamResource> generateAcceptanceAct(@PathVariable("lendingId") int lendingId) {
        try {
            Lending lending = lendingService.getLendingById(lendingId)
                    .orElseThrow(() -> new NoSuchElementException("Lending not found with id: " + lendingId));
            byte[] document = acceptanceActService.generateAcceptanceAct(lending);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(document);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=acceptance_act.docx");

            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(inputStream));
        } catch (IOException e) {
            log.error("Error generating acceptance act for lending id: {}", lendingId, e);
            return ResponseEntity.status(500).build();
        } catch (NoSuchElementException e) {
            log.error("Lending not found with id: {}", lendingId, e);
            return ResponseEntity.notFound().build();
        }
    }

}