package com.example.InventorySystem.services.impl;

import com.example.InventorySystem.models.Lending;
import com.example.InventorySystem.models.Person;
import com.example.InventorySystem.repos.LendingRepo;
import com.example.InventorySystem.services.LendingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LendingServiceImpl implements LendingService {

    private final Logger log = LoggerFactory.getLogger(LendingServiceImpl.class);

    @Autowired
    private LendingRepo lendingRepo;

    @Override
    public List<Lending> getAll() {
        List<Lending> lendingList = lendingRepo.findAll();
        return lendingList;
    }

    @Override
    public Optional<Lending> getLendingById(int lendingId) {
        Optional<Lending> personOptional = lendingRepo.findById(lendingId);
        if (personOptional.isEmpty()) {
            log.warn("Lending with id: {} is not found", lendingId);
        } else {
            log.info("Lending found with id: {}", lendingId);
        }
        return personOptional;
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
            throw e;
        }
    }

    @Override
    public void updateLending(int lendingId, Lending updatedLendingData) {
        try {
            Optional<Lending> existingLendingOptional = lendingRepo.findById(lendingId);
            if (existingLendingOptional.isPresent()) {
                Lending existingLending = existingLendingOptional.get();
                if (updatedLendingData != null && existingLending.getLendingId() == lendingId) {
                    // Update the properties of the existing Lending entity
                    existingLending.setDate(updatedLendingData.getDate());
                    existingLending.setEstimatedReturnDate(updatedLendingData.getEstimatedReturnDate());
                    existingLending.setReceived(updatedLendingData.isReceived());
                    existingLending.setReturned(updatedLendingData.isReturned());
                    existingLending.setComments(updatedLendingData.getComments());
                    existingLending.setInventory(updatedLendingData.getInventory());
                    existingLending.setBorrower(updatedLendingData.getBorrower());
                    existingLending.setLender(updatedLendingData.getLender());

                    // Save the updated Lending entity
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
            throw e;
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
            log.error("Error occurred while deleting lending with ID: {}", lendingId, e);
            throw e;
        }
    }
}
