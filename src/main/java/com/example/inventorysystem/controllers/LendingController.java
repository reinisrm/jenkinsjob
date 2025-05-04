package com.example.inventorysystem.controllers;

import com.example.inventorysystem.constants.ViewNames;
import com.example.inventorysystem.models.dto.LendingDTO;
import com.example.inventorysystem.services.impl.InventoryServiceImpl;
import com.example.inventorysystem.services.impl.LendingServiceImpl;
import com.example.inventorysystem.services.impl.PersonServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
            List<LendingDTO> lendingDTOs = lendingService.getAllAsDTOs();
            log.info("Lending list size: {}", lendingDTOs.size());
            model.addAttribute("lendings", lendingDTOs);
            return ViewNames.SHOW_ALL_LENDING;
        } catch (Exception e) {
            log.error("Error occurred while fetching all lendings", e);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/{lendingId}")
    public String showOneLending(@PathVariable("lendingId") int lendingId, Model model) {
        log.info("Get lending with id: {}", lendingId);
        Optional<LendingDTO> lendingDTO = lendingService.getLendingDTOById(lendingId);

        if (lendingDTO.isPresent()) {
            model.addAttribute(ViewNames.LENDING, lendingDTO.get());
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
            model.addAttribute(ViewNames.LENDING, new LendingDTO());
            model.addAttribute("inventoryList", inventoryService.getAll());
            model.addAttribute("borrowerList", personService.getAll());
            model.addAttribute("lenderList", personService.getAll());
            return ViewNames.CREATE_LENDING;
        } catch (Exception e) {
            log.error("Error occurred while preparing create lending form", e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/create")
    public String createLending(@Valid @ModelAttribute("lending") LendingDTO lendingDTO, BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            log.error("Failed creating a new lending, error: {}", result);
            attributes.addFlashAttribute("org.springframework.validation.BindingResult.lending", result);
            attributes.addFlashAttribute(ViewNames.LENDING, lendingDTO);
            return "redirect:/lending/create";
        }
        try {
            lendingService.createLendingFromDTO(lendingDTO);
            log.info("Lending created successfully: {}", lendingDTO);
            return ViewNames.REDIRECT_LENDING;
        } catch (Exception e) {
            log.error("Unexpected error creating lending record", e);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/update/{lendingId}")
    public String showUpdateForm(@PathVariable("lendingId") int lendingId, Model model) {
        try {
            Optional<LendingDTO> lendingDTO = lendingService.getLendingDTOById(lendingId);
            model.addAttribute("lending", lendingDTO.orElse(new LendingDTO()));
            model.addAttribute("inventoryList", inventoryService.getAll());
            model.addAttribute("borrowerList", personService.getAll());
            model.addAttribute("lenderList", personService.getAll());
            return ViewNames.LENDING_UPDATE;
        } catch (Exception e) {
            log.error("Error occurred while preparing update form for lending with ID: {}", lendingId, e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/update/{lendingId}")
    public String updateLending(@PathVariable("lendingId") int lendingId, @Valid @ModelAttribute("lending") LendingDTO lendingDTO,
                                BindingResult result) {
        if (lendingId <= 0) {
            log.warn("Invalid lendingId provided: {}", lendingId);
            return ViewNames.LENDING_UPDATE;
        }
        if (result.hasErrors()) {
            log.warn("Validation error updating lending: {}", result.getAllErrors());
            return ViewNames.LENDING_UPDATE;
        }
        try {
            lendingService.updateLendingFromDTO(lendingId, lendingDTO);
            log.info("Lending with id: {} has been updated", lendingId);
            return "redirect:/lending/" + lendingId;
        } catch (Exception e) {
            log.error("Error occurred while updating lending with ID: {}", lendingId, e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/delete/{lendingId}")
    public String deleteLending(@PathVariable("lendingId") int lendingId) {
        if (lendingId <= 0) {
            log.warn("Invalid lendingId provided: {}", lendingId);
        }
        lendingService.deleteLendingById(lendingId);
        log.info("Lending with id: {} is deleted", lendingId);
        return ViewNames.REDIRECT_LENDING;
    }
}
