package com.example.inventorysystem.services.impl;

import com.example.inventorysystem.mappers.LendingMapper;
import com.example.inventorysystem.models.Inventory;
import com.example.inventorysystem.models.Lending;
import com.example.inventorysystem.models.Person;
import com.example.inventorysystem.models.dto.LendingDTO;
import com.example.inventorysystem.repos.InventoryRepo;
import com.example.inventorysystem.repos.LendingRepo;
import com.example.inventorysystem.repos.PersonRepo;
import com.example.inventorysystem.services.LendingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LendingServiceImpl implements LendingService {

    private final Logger log = LoggerFactory.getLogger(LendingServiceImpl.class);

    private final LendingRepo lendingRepo;
    private final InventoryRepo inventoryRepo;
    private final PersonRepo personRepo;

    @Autowired
    public LendingServiceImpl(LendingRepo lendingRepo, InventoryRepo inventoryRepo, PersonRepo personRepo) {
        this.lendingRepo = lendingRepo;
        this.inventoryRepo = inventoryRepo;
        this.personRepo = personRepo;
    }

    public List<LendingDTO> getAllAsDTOs() {
        return lendingRepo.findAll().stream().map(LendingMapper::toDTO).toList();
    }

    public Optional<LendingDTO> getLendingDTOById(int lendingId) {
        return lendingRepo.findById(lendingId).map(LendingMapper::toDTO);
    }

    public void createLendingFromDTO(LendingDTO dto) {
        try {
            Inventory inventory = inventoryRepo.findById(dto.getInventoryId())
                    .orElseThrow(() -> new NoSuchElementException("Inventory not found"));
            Person borrower = personRepo.findById(dto.getBorrowerId())
                    .orElseThrow(() -> new NoSuchElementException("Borrower not found"));
            Person lender = personRepo.findById(dto.getLenderId())
                    .orElseThrow(() -> new NoSuchElementException("Lender not found"));

            Lending lending = LendingMapper.toEntity(dto, inventory, borrower, lender);
            lendingRepo.save(lending);
            log.info("Lending created from DTO: {}", lending);
        } catch (Exception e) {
            log.error("Error creating lending from DTO", e);
        }
    }

    public void updateLendingFromDTO(int lendingId, LendingDTO dto) {
        try {
            Inventory inventory = inventoryRepo.findById(dto.getInventoryId())
                    .orElseThrow(() -> new NoSuchElementException("Inventory not found"));
            Person borrower = personRepo.findById(dto.getBorrowerId())
                    .orElseThrow(() -> new NoSuchElementException("Borrower not found"));
            Person lender = personRepo.findById(dto.getLenderId())
                    .orElseThrow(() -> new NoSuchElementException("Lender not found"));

            Lending updated = LendingMapper.toEntity(dto, inventory, borrower, lender);
            updated.setLendingId(lendingId); // Ensure ID consistency
            lendingRepo.save(updated);
            log.info("Lending with ID {} updated from DTO", lendingId);
        } catch (Exception e) {
            log.error("Error updating lending from DTO", e);
        }
    }

    @Override
    public void updateLending(int lendingId, Lending updatedLendingData) {
        updateLendingFromDTO(lendingId, LendingMapper.toDTO(updatedLendingData)); // Delegate to DTO method
    }

    @Override
    public void deleteLendingById(int lendingId) {
        try {
            Optional<Lending> existing = lendingRepo.findById(lendingId);
            if (existing.isPresent()) {
                lendingRepo.deleteById(lendingId);
                log.info("Lending with ID {} deleted successfully", lendingId);
            } else {
                log.warn("Lending with ID {} not found for deletion", lendingId);
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting lending with ID: {}", lendingId, e);
        }
    }

    @Override
    public List<Lending> findLendingsByDateRange(LocalDate startDate, LocalDate endDate) {
        return lendingRepo.findByDateBetween(startDate, endDate);
    }
}
