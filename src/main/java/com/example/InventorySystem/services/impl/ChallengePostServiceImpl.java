package com.example.InventorySystem.services.impl;

import com.example.InventorySystem.models.ChallengePost;
import com.example.InventorySystem.models.User;
import com.example.InventorySystem.repos.ChallengePostRepo;
import com.example.InventorySystem.repos.UserRepo;
import com.example.InventorySystem.services.ChallengePostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ChallengePostServiceImpl implements ChallengePostService {

    private static final Logger log = LoggerFactory.getLogger(ChallengePostServiceImpl.class);
    private final ChallengePostRepo challengePostRepo;
    private final UserRepo userRepo;

    @Autowired
    public ChallengePostServiceImpl(ChallengePostRepo challengePostRepo, UserRepo userRepo) {
        this.challengePostRepo = challengePostRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<ChallengePost> getAllPosts() {
        log.info("Fetching all challenge posts");
        return challengePostRepo.findAll();
    }

    @Override
    public Optional<ChallengePost> getPostById(int postId) {
        log.info("Fetching challenge post with id: {}", postId);
        return challengePostRepo.findById(postId);
    }

    @Override
    public ChallengePost createPost(ChallengePost post, int userId) {
        log.info("Creating challenge post for user id: {}", userId);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        post.setUser(user);
        ChallengePost savedPost = challengePostRepo.save(post);
        log.info("Challenge post created successfully with id: {}", savedPost.getId());
        return savedPost;
    }

    @Override
    @Transactional
    public ChallengePost updatePost(int postId, ChallengePost postDetails) {
        log.info("Updating challenge post with id: {}", postId);
        ChallengePost post = challengePostRepo.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + postId));

        post.setText(postDetails.getText());
        post.setImageUrl(postDetails.getImageUrl());
        ChallengePost updatedPost = challengePostRepo.save(post);
        log.info("Challenge post with id: {} updated successfully", postId);
        return updatedPost;
    }

    @Override
    public void deletePostById(int postId) {
        log.info("Deleting challenge post with id: {}", postId);
        challengePostRepo.deleteById(postId);
        log.info("Challenge post with id: {} deleted successfully", postId);
    }
}
