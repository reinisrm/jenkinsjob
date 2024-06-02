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

import java.time.LocalDate;
import java.util.*;

@Service
public class ChallengePostServiceImpl implements ChallengePostService {

    private static final Logger log = LoggerFactory.getLogger(ChallengePostServiceImpl.class);
    private final ChallengePostRepo challengePostRepo;
    private final UserRepo userRepo;

    private static final Map<Integer, String> MONTHS = new HashMap<>();

    static {
        MONTHS.put(1, "janvārī");
        MONTHS.put(2, "februārī");
        MONTHS.put(3, "martā");
        MONTHS.put(4, "aprīlī");
        MONTHS.put(5, "maijā");
        MONTHS.put(6, "jūnijā");
        MONTHS.put(7, "jūlijā");
        MONTHS.put(8, "augustā");
        MONTHS.put(9, "septembrī");
        MONTHS.put(10, "oktobrī");
        MONTHS.put(11, "novembrī");
        MONTHS.put(12, "decembrī");
    }

    @Autowired
    public ChallengePostServiceImpl(ChallengePostRepo challengePostRepo, UserRepo userRepo) {
        this.challengePostRepo = challengePostRepo;
        this.userRepo = userRepo;
    }

    @Override
    public List<ChallengePost> getAllPosts() {
        log.info("Fetching all challenge posts");
        List<ChallengePost> posts = challengePostRepo.findAll();
        posts.sort(Comparator.comparing(ChallengePost::getCreatedDate).reversed()); // Sort by date descending
        posts.forEach(post -> post.setFormattedDate(formatDateToLatvian(LocalDate.from(post.getCreatedDate()))));
        return posts;
    }

    @Override
    public Optional<ChallengePost> getPostById(int postId) {
        log.info("Fetching challenge post with id: {}", postId);
        Optional<ChallengePost> post = challengePostRepo.findById(postId);
        post.ifPresent(p -> {
            if (p.getCreatedDate() != null) {
                p.setFormattedDate(formatDateToLatvian(p.getCreatedDate().toLocalDate()));
            }
        });
        return post;
    }

    @Override
    public ChallengePost createPost(ChallengePost post, int userId) {
        log.info("Creating challenge post for user id: {}", userId);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        post.setUser(user);
        ChallengePost savedPost = challengePostRepo.save(post);
        log.info("Challenge post created successfully with id: {}", savedPost != null ? savedPost.getId() : "null");
        return savedPost;
    }

    @Override
    @Transactional
    public ChallengePost updatePost(int postId, ChallengePost postDetails) {
        log.info("Updating challenge post with id: {}", postId);
        ChallengePost post = challengePostRepo.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + postId));

        post.setTitle(postDetails.getTitle());
        post.setText(postDetails.getText());
        ChallengePost updatedPost = challengePostRepo.save(post);
        log.info("Challenge post with id: {} updated successfully", postId);
        return updatedPost;
    }

    @Override
    public void deletePostById(int postId) {
        log.info("Deleting challenge post with id: {}", postId);
        ChallengePost post = challengePostRepo.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + postId));
        challengePostRepo.deleteById(postId);
        log.info("Challenge post with id: {} deleted successfully", postId);
    }

    private String formatDateToLatvian(LocalDate date) {
        String day = String.valueOf(date.getDayOfMonth());
        String month = MONTHS.get(date.getMonthValue());
        String year = String.valueOf(date.getYear());
        return day + ". " + month + ", " + year + ".";
    }
}
