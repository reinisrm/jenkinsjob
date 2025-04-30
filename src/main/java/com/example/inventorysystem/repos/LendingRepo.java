package com.example.inventorysystem.repos;

import com.example.inventorysystem.models.Lending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface LendingRepo extends JpaRepository<Lending, Integer> {
    List<Lending> findByDateBetween(LocalDate startDate, LocalDate endDate);
}