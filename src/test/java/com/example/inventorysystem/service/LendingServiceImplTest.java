package com.example.inventorysystem.service;

import com.example.inventorysystem.models.Lending;
import com.example.inventorysystem.repos.LendingRepo;
import com.example.inventorysystem.services.impl.LendingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LendingServiceImplTest {

    @InjectMocks
    private LendingServiceImpl lendingService;

    @Mock
    private LendingRepo lendingRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAll() {
        List<Lending> expectedLendings = new ArrayList<>();
        expectedLendings.add(new Lending());
        when(lendingRepo.findAll()).thenReturn(expectedLendings);

        List<Lending> actualLendings = lendingService.getAll();

        assertEquals(expectedLendings, actualLendings);
    }

    @Test
    void testGetLendingById_ExistingId() {
        int lendingId = 1;
        Lending expectedLending = new Lending();
        when(lendingRepo.findById(lendingId)).thenReturn(Optional.of(expectedLending));

        Optional<Lending> actualLending = lendingService.getLendingById(lendingId);

        assertTrue(actualLending.isPresent());
        assertEquals(expectedLending, actualLending.get());
    }

    @Test
    void testGetLendingById_NonExistingId() {
        int lendingId = 999;
        when(lendingRepo.findById(lendingId)).thenReturn(Optional.empty());

        Optional<Lending> actualLending = lendingService.getLendingById(lendingId);

        assertTrue(actualLending.isEmpty());
    }

    @Test
    void testCreateLending_ValidLending() {
        Lending newLending = new Lending();
        newLending.setLendingId(0);

        lendingService.createLending(newLending);

        verify(lendingRepo).save(newLending);
    }

    @Test
    void testCreateLending_InvalidLending() {
        Lending invalidLending = new Lending();
        invalidLending.setLendingId(1);

        assertThrows(IllegalArgumentException.class, () -> lendingService.createLending(invalidLending));

        verify(lendingRepo, never()).save(any(Lending.class));
    }

    @Test
    void testUpdateLending_ExistingLending() {
        int lendingId = 1;
        Lending existingLending = new Lending();
        existingLending.setLendingId(lendingId);
        Lending updatedData = new Lending();
        when(lendingRepo.findById(lendingId)).thenReturn(Optional.of(existingLending));

        lendingService.updateLending(lendingId, updatedData);

        verify(lendingRepo).save(existingLending);
    }

    @Test
    void testUpdateLending_NonExistingLending() {
        int lendingId = 999;
        Lending updatedData = new Lending();
        when(lendingRepo.findById(lendingId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> lendingService.updateLending(lendingId, updatedData));

        verify(lendingRepo, never()).save(any(Lending.class));
    }

    @Test
    void testDeleteLendingById_ExistingId() {
        int lendingId = 1;
        when(lendingRepo.findById(lendingId)).thenReturn(Optional.of(new Lending()));

        lendingService.deleteLendingById(lendingId);

        verify(lendingRepo).deleteById(lendingId);
    }

    @Test
    void testDeleteLendingById_NonExistingId() {
        int lendingId = 999;
        when(lendingRepo.findById(lendingId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> lendingService.deleteLendingById(lendingId));

        verify(lendingRepo, never()).deleteById(lendingId);
    }

    @Test
    void testCreateLending_NullInput_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> lendingService.createLending(null));
        verify(lendingRepo, never()).save(any());
    }

}