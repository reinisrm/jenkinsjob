package com.example.InventorySystem.repos;

import com.example.InventorySystem.models.Authority;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepo extends CrudRepository<Authority, Integer> {

}
