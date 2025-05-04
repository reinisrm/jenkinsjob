package com.example.inventorysystem.services;

import com.example.inventorysystem.models.Lending;
import com.example.inventorysystem.models.dto.LendingDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LendingService {
    List<LendingDTO> getAllAsDTOs();
    Optional<LendingDTO> getLendingDTOById(int lendingId);
    void createLendingFromDTO(LendingDTO dto);
    void updateLendingFromDTO(int lendingId, LendingDTO dto);
    void updateLending(int lendingId, Lending updatedLendingData);
    void deleteLendingById(int lendingId);
    List<Lending> findLendingsByDateRange(LocalDate startDate, LocalDate endDate);
}
