package com.example.inventorysystem.controllers;

import com.example.inventorysystem.models.Inventory;
import com.example.inventorysystem.models.Lending;
import com.example.inventorysystem.models.Person;
import com.example.inventorysystem.services.impl.InventoryServiceImpl;
import com.example.inventorysystem.services.impl.LendingServiceImpl;
import com.example.inventorysystem.services.impl.PersonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(MockitoExtension.class)
class LendingControllerTest {

    @InjectMocks
    private LendingController lendingController;

    @Mock
    private LendingServiceImpl lendingService;

    @Mock
    private InventoryServiceImpl inventoryService;

    @Mock
    private PersonServiceImpl personService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private RedirectAttributes redirectAttributes;

    private MockMvc mockMvc;

    private List<Lending> lendingList;
    private List<Inventory> inventoryList;
    private List<Person> personList;

    @BeforeEach
    void setUp() {
        lendingList = new ArrayList<>();
        Inventory inventory = new Inventory();
        Person borrower = new Person();
        Person lender = new Person();
        Lending lending1 = new Lending(LocalDate.now(), inventory, borrower, lender, LocalDate.now(), false, false, "Comments 1");
        Lending lending2 = new Lending(LocalDate.now(), inventory, borrower, lender, LocalDate.now(), false, false, "Comments 2");
        lendingList.add(lending1);
        lendingList.add(lending2);

        inventoryList = new ArrayList<>();
        inventoryList.add(inventory);

        personList = new ArrayList<>();
        personList.add(borrower);
        personList.add(lender);

        mockMvc = MockMvcBuilders.standaloneSetup(lendingController).build();
    }

    @Test
    void testShowAllLending() {
        when(lendingService.getAll()).thenReturn(lendingList);

        String viewName = lendingController.showAllLending(model);

        verify(model).addAttribute(eq("lendings"), eq(lendingList));
        assertEquals("show-all-lending", viewName);
    }

    @Test
    void testShowOneLending() throws Exception {
        Lending lending = new Lending(LocalDate.now(), new Inventory(), new Person(), new Person(), LocalDate.now(), false, false, "Comments");
        lending.setLendingId(1);

        when(lendingService.getLendingById(anyInt())).thenReturn(Optional.of(lending));

        mockMvc.perform(get("/lending/{lendingId}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("show-one-lending"))
                .andExpect(model().attribute("lending", Optional.of(lending)));
    }

    @Test
    void testCreateLendingForm() {
        when(inventoryService.getAll()).thenReturn(inventoryList);
        when(personService.getAll()).thenReturn(personList);

        String viewName = lendingController.createLendingForm(model);

        verify(model).addAttribute(eq("lending"), any(Lending.class));
        verify(model).addAttribute(eq("inventoryList"), eq(inventoryList));
        verify(model).addAttribute(eq("borrowerList"), eq(personList));
        verify(model).addAttribute(eq("lenderList"), eq(personList));

        assertEquals("create-lending", viewName);
    }

    @Test
    void testCreateLendingSuccess() {
        Lending lending = new Lending(LocalDate.now(), new Inventory(), new Person(), new Person(), LocalDate.now(), false, false, "Comments");
        when(bindingResult.hasErrors()).thenReturn(false);

        String viewName = lendingController.createLending(lending, bindingResult, redirectAttributes);

        assertEquals("redirect:/lending/", viewName);
        verify(lendingService).createLending(any(Lending.class));
    }

    @Test
    void testCreateLendingValidationFailed() {
        Lending lending = new Lending(LocalDate.now(), new Inventory(), new Person(), new Person(), LocalDate.now(), false, false, "Comments");
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = lendingController.createLending(lending, bindingResult, redirectAttributes);

        assertEquals("redirect:/lending/create", viewName);
        verify(redirectAttributes).addFlashAttribute(eq("org.springframework.validation.BindingResult.lending"), any(BindingResult.class));
        verify(redirectAttributes).addFlashAttribute(eq("lending"), eq(lending));
    }

    @Test
    void testShowUpdateForm() {
        int lendingId = 1;
        Lending lending = new Lending(LocalDate.now(), new Inventory(), new Person(), new Person(), LocalDate.now(), false, false, "Comments");
        Mockito.when(lendingService.getLendingById(anyInt())).thenReturn(Optional.of(lending));
        Mockito.when(inventoryService.getAll()).thenReturn(inventoryList);
        Mockito.when(personService.getAll()).thenReturn(personList);

        String result = lendingController.showUpdateForm(lendingId, model);

        assertEquals("lending-update-page", result);
        verify(model).addAttribute(eq("lending"), eq(lending));
        verify(model).addAttribute(eq("inventoryList"), eq(inventoryList));
        verify(model).addAttribute(eq("borrowerList"), eq(personList));
        verify(model).addAttribute(eq("lenderList"), eq(personList));
    }

    @Test
    void testUpdateLendingValidationFailed() {
        int lendingId = 1;
        Lending updatedLending = new Lending(LocalDate.now(), new Inventory(), new Person(), new Person(), LocalDate.now(), false, false, "Comments");
        when(bindingResult.hasErrors()).thenReturn(true);

        String viewName = lendingController.updateLending(lendingId, updatedLending, bindingResult);

        assertEquals("lending-update-page", viewName);
    }

    @Test
    void testDeleteLending() {
        int lendingId = 1;

        String viewName = lendingController.deleteLending(lendingId);

        assertEquals("redirect:/lending/", viewName);
        verify(lendingService).deleteLendingById(lendingId);
    }
}