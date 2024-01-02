package services;

import models.Lending;

import java.util.List;
import java.util.Optional;

public interface LendingService {
    List<Lending> getAll();
    Optional<Lending> getLendingById(int lendingId);
    void createLending(Lending lending);
    void updateLending(int lendingId, Lending updatedLendingData);
    void deleteLendingById(int lendingId);
}
