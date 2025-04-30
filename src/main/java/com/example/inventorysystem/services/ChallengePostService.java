package com.example.inventorysystem.services;

import com.example.inventorysystem.models.ChallengePost;
import java.util.List;
import java.util.Optional;

public interface ChallengePostService {
    List<ChallengePost> getAllPosts();
    Optional<ChallengePost> getPostById(int postId);
    ChallengePost createPost(ChallengePost post, int userId);
    ChallengePost updatePost(int postId, ChallengePost post);
    void deletePostById(int postId);

}
