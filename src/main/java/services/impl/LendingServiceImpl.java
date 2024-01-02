package services.impl;

import models.Lending;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repos.LendingRepo;
import services.LendingService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class LendingServiceImpl implements LendingService {

    @Autowired
    private LendingRepo lendingRepo;

    @Override
    public List<Lending> getAll() {
        return (List<Lending>) lendingRepo.findAll();
    }

    @Override
    public Optional<Lending> getLendingById(int lendingId) {
        return lendingRepo.findById(lendingId);
    }

    @Override
    public void createLending(Lending lending) {
        if (lending != null && lending.getLendingId() == 0) {
            lendingRepo.save(lending);
        } else {
            throw new IllegalArgumentException("Invalid data or lending_id already exists");
        }
    }

    @Override
    public void updateLending(int lendingId, Lending updatedLendingData) {
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
                lendingRepo.save(existingLending);
            } else {
                throw new IllegalArgumentException("Invalid lending_id or data");
            }
        } else {
            throw new NoSuchElementException("Lending not found");
        }
    }

    @Override
    public void deleteLendingById(int lendingId) {
        Optional<Lending> existingLendingOptional = lendingRepo.findById(lendingId);
        if (existingLendingOptional.isPresent()) {
            lendingRepo.deleteById(lendingId);
        } else {
            throw new NoSuchElementException("Lending not found");
        }
    }
}

