package com.example.inventorysystem.repos;

import com.example.inventorysystem.models.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorityRepo extends JpaRepository<Authority, Integer> {
}