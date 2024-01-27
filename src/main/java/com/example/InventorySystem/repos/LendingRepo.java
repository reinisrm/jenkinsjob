package com.example.InventorySystem.repos;

import com.example.InventorySystem.models.Lending;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LendingRepo extends CrudRepository<Lending, Integer> {

}
