package com.example.inventorysystem.services;

import com.example.inventorysystem.models.ChallengePost;
import com.example.inventorysystem.models.dto.ChallengePostDTO;

import java.util.List;
import java.util.Optional;

public interface ChallengePostService {
    List<ChallengePostDTO> getAllPostDTOs();
    Optional<ChallengePostDTO> getPostDTOById(int postId);
    ChallengePostDTO createPost(ChallengePostDTO dto, int userId);
    ChallengePostDTO updatePost(int postId, ChallengePostDTO dto);
    void deletePostById(int postId);

}
