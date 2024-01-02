package controllers;

import models.Lending;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import services.impl.LendingServiceImpl;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/lending")
public class LendingController {

    @Autowired
    LendingServiceImpl lendingService;

    @GetMapping("/")
    public String showAllLending(Model model) {
        List<Lending> lendings = lendingService.getAll();
        model.addAttribute("lendings", lendings);
        return "show-all-lending";
    }

    @GetMapping("/{lendingId}")
    public String showOneLending(@PathVariable("lendingId") int lendingId, Model model) {
        Optional<Lending> lending = lendingService.getLendingById(lendingId);
        model.addAttribute("lending", lending);
        return "show-one-lending";
    }

    @GetMapping("/create")
    public String createLendingForm(Model model) {
        model.addAttribute("lending", new Lending());
        return "create-lending";
    }

    @PostMapping("/create")
    public String createLending(@Valid Lending lending, BindingResult result) {
        if (result.hasErrors()) {
            return "create-lending";
        }

        Lending temp = new Lending();
        temp.setDate(lending.getDate());
        temp.setEstimatedReturnDate(lending.getEstimatedReturnDate());
        temp.setReceived(lending.isReceived());
        temp.setReturned(lending.isReturned());
        temp.setComments(lending.getComments());
        temp.setInventory(lending.getInventory());
        temp.setBorrower(lending.getBorrower());
        temp.setLender(lending.getLender());

        lendingService.createLending(temp);
        return "redirect:/lending/";
    }

    @GetMapping("/update/{lendingId}")
    public String showUpdateForm(@PathVariable("lendingId") int lendingId, Model model) {
        Optional<Lending> lending = lendingService.getLendingById(lendingId);
        model.addAttribute("lending", lending);
        return "lending-update-page";
    }

    @PostMapping("/update/{lendingId}")
    public String updateLending(@PathVariable("lendingId") int lendingId, @Valid Lending lending,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "lending-update-page";
        }
        lendingService.updateLending(lendingId, lending);
        return "redirect:/lending/{lendingId}";
    }

    @DeleteMapping("/delete/{lendingId}")
    public String deleteLending(@PathVariable("lendingId") int lendingId) {
        lendingService.deleteLendingById(lendingId);
        return "redirect:/lending/";
    }
}

