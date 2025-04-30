package com.example.inventorysystem.repos;

import com.example.inventorysystem.models.ChallengePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengePostRepo extends JpaRepository<ChallengePost, Integer> {
}