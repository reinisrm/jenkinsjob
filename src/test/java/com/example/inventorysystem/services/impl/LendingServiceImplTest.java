package com.example.inventorysystem.services.impl;

import com.example.inventorysystem.models.Inventory;
import com.example.inventorysystem.models.Lending;
import com.example.inventorysystem.models.Person;
import com.example.inventorysystem.models.dto.LendingDTO;
import com.example.inventorysystem.repos.InventoryRepo;
import com.example.inventorysystem.repos.LendingRepo;
import com.example.inventorysystem.repos.PersonRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LendingServiceImplTest {

    @InjectMocks
    private LendingServiceImpl lendingService;

    @Mock
    private LendingRepo lendingRepo;

    @Mock
    private InventoryRepo inventoryRepo;

    @Mock
    private PersonRepo personRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAsDTOs() {
        Lending lending = new Lending();
        when(lendingRepo.findAll()).thenReturn(List.of(lending));

        List<LendingDTO> result = lendingService.getAllAsDTOs();

        assertEquals(1, result.size());
    }

    @Test
    void testGetLendingDTOById_Found() {
        Lending lending = new Lending();
        when(lendingRepo.findById(1)).thenReturn(Optional.of(lending));

        Optional<LendingDTO> dto = lendingService.getLendingDTOById(1);

        assertTrue(dto.isPresent());
    }

    @Test
    void testGetLendingDTOById_NotFound() {
        when(lendingRepo.findById(1)).thenReturn(Optional.empty());

        Optional<LendingDTO> dto = lendingService.getLendingDTOById(1);

        assertTrue(dto.isEmpty());
    }

    @Test
    void testCreateLendingFromDTO_Success() {
        LendingDTO dto = new LendingDTO();
        dto.setInventoryId(1);
        dto.setBorrowerId(2);
        dto.setLenderId(3);

        when(inventoryRepo.findById(1)).thenReturn(Optional.of(new Inventory()));
        when(personRepo.findById(2)).thenReturn(Optional.of(new Person()));
        when(personRepo.findById(3)).thenReturn(Optional.of(new Person()));

        lendingService.createLendingFromDTO(dto);

        verify(lendingRepo, times(1)).save(any(Lending.class));
    }

    @Test
    void testCreateLendingFromDTO_InventoryNotFound() {
        LendingDTO dto = new LendingDTO();
        dto.setInventoryId(99);
        dto.setBorrowerId(2);
        dto.setLenderId(3);

        when(inventoryRepo.findById(99)).thenReturn(Optional.empty());

        lendingService.createLendingFromDTO(dto);

        verify(lendingRepo, never()).save(any());
    }

    @Test
    void testUpdateLendingFromDTO_Success() {
        LendingDTO dto = new LendingDTO();
        dto.setInventoryId(1);
        dto.setBorrowerId(2);
        dto.setLenderId(3);

        when(inventoryRepo.findById(1)).thenReturn(Optional.of(new Inventory()));
        when(personRepo.findById(2)).thenReturn(Optional.of(new Person()));
        when(personRepo.findById(3)).thenReturn(Optional.of(new Person()));

        lendingService.updateLendingFromDTO(1, dto);

        verify(lendingRepo).save(any(Lending.class));
    }

    @Test
    void testDeleteLendingById_Existing() {
        when(lendingRepo.findById(1)).thenReturn(Optional.of(new Lending()));

        lendingService.deleteLendingById(1);

        verify(lendingRepo).deleteById(1);
    }

    @Test
    void testDeleteLendingById_NotFound() {
        when(lendingRepo.findById(1)).thenReturn(Optional.empty());

        lendingService.deleteLendingById(1);

        verify(lendingRepo, never()).deleteById(anyInt());
    }

    @Test
    void testFindLendingsByDateRange() {
        LocalDate start = LocalDate.now().minusDays(7);
        LocalDate end = LocalDate.now();

        when(lendingRepo.findByDateBetween(start, end)).thenReturn(Collections.singletonList(new Lending()));

        List<Lending> result = lendingService.findLendingsByDateRange(start, end);

        assertEquals(1, result.size());
    }
}