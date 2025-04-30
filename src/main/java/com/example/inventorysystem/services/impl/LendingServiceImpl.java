package com.example.inventorysystem.services.impl;

import com.example.inventorysystem.models.Lending;
import com.example.inventorysystem.repos.LendingRepo;
import com.example.inventorysystem.services.LendingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LendingServiceImpl implements LendingService {

    private final Logger log = LoggerFactory.getLogger(LendingServiceImpl.class);

    private final LendingRepo lendingRepo;

    @Autowired
    public LendingServiceImpl(LendingRepo lendingRepo) {
        this.lendingRepo = lendingRepo;
    }

    @Override
    public List<Lending> getAll() {
        return lendingRepo.findAll();
    }

    @Override
    public Optional<Lending> getLendingById(int lendingId) {
        Optional<Lending> lendingOptional = lendingRepo.findById(lendingId);
        if (lendingOptional.isEmpty()) {
            log.warn("Lending with id: {} is not found", lendingId);
        } else {
            log.info("Lending found with id: {}", lendingId);
        }
        return lendingOptional;
    }

    @Override
    public void createLending(Lending lending) {
        try {
            if (lending != null && lending.getLendingId() == 0) {
                lendingRepo.save(lending);
                log.info("Lending created successfully: {}", lending);
            } else {
                throw new IllegalArgumentException("Invalid data or lending_id already exists");
            }
        } catch (Exception e) {
            log.error("Error occurred while creating lending", e);
        }
    }

    @Override
    public void updateLending(int lendingId, Lending updatedLendingData) {
        try {
            Optional<Lending> existingLendingOptional = lendingRepo.findById(lendingId);
            if (existingLendingOptional.isPresent()) {
                Lending existingLending = existingLendingOptional.get();
                if (updatedLendingData != null && existingLending.getLendingId() == lendingId) {
                    existingLending.setDate(updatedLendingData.getDate());
                    existingLending.setEstimatedReturnDate(updatedLendingData.getEstimatedReturnDate());
                    existingLending.setReceived(updatedLendingData.isReceived());
                    existingLending.setReturned(updatedLendingData.isReturned());
                    existingLending.setComments(updatedLendingData.getComments());
                    existingLending.setInventory(updatedLendingData.getInventory());
                    existingLending.setBorrower(updatedLendingData.getBorrower());
                    existingLending.setLender(updatedLendingData.getLender());
                    Lending updatedLending = lendingRepo.save(existingLending);
                    log.info("Lending with id: {} updated successfully: {}", lendingId, updatedLending);
                } else {
                    throw new IllegalArgumentException("Invalid lending_id or data");
                }
            } else {
                throw new NoSuchElementException("Lending not found");
            }
        } catch (Exception e) {
            log.error("Error occurred while updating lending with ID: {}", lendingId, e);
        }
    }

    @Override
    public void deleteLendingById(int lendingId) {
        try {
            Optional<Lending> existingLendingOptional = lendingRepo.findById(lendingId);
            if (existingLendingOptional.isPresent()) {
                lendingRepo.deleteById(lendingId);
                log.info("Lending with ID {} deleted successfully", lendingId);
            } else {
                throw new NoSuchElementException("Lending not found");
            }
        } catch (Exception e) {
            log.error("Error occurred while deleting lending, with ID: {}", lendingId, e);
        }
    }

    @Override
    public List<Lending> findLendingsByDateRange(LocalDate startDate, LocalDate endDate) {
        return lendingRepo.findByDateBetween(startDate, endDate);
    }
}
