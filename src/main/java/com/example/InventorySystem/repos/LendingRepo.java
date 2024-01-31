package com.example.InventorySystem.repos;

import com.example.InventorySystem.models.Lending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LendingRepo extends JpaRepository<Lending, Integer> {

}

