package com.example.inventorysystem.services.impl;

import com.example.inventorysystem.mappers.ChallengePostMapper;
import com.example.inventorysystem.models.ChallengePost;
import com.example.inventorysystem.models.User;
import com.example.inventorysystem.models.dto.ChallengePostDTO;
import com.example.inventorysystem.repos.ChallengePostRepo;
import com.example.inventorysystem.repos.UserRepo;
import com.example.inventorysystem.services.ChallengePostService;
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

    public List<ChallengePostDTO> getAllPostDTOs() {
        List<ChallengePost> posts = challengePostRepo.findAll();
        posts.sort(Comparator.comparing(ChallengePost::getCreatedDate).reversed());
        posts.forEach(post -> post.setFormattedDate(formatDateToLatvian(LocalDate.from(post.getCreatedDate()))));
        return posts.stream().map(ChallengePostMapper::toDTO).toList();
    }

    public Optional<ChallengePostDTO> getPostDTOById(int postId) {
        Optional<ChallengePost> post = challengePostRepo.findById(postId);
        post.ifPresent(p -> {
            if (p.getCreatedDate() != null) {
                p.setFormattedDate(formatDateToLatvian(p.getCreatedDate().toLocalDate()));
            }
        });
        return post.map(ChallengePostMapper::toDTO);
    }

    public ChallengePostDTO createPost(ChallengePostDTO dto, int userId) {
        log.info("Creating challenge post for user id: {}", userId);
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId));
        ChallengePost post = ChallengePostMapper.toEntity(dto, user);
        ChallengePost savedPost = challengePostRepo.save(post);
        return ChallengePostMapper.toDTO(savedPost);
    }

    @Transactional
    public ChallengePostDTO updatePost(int postId, ChallengePostDTO dto) {
        ChallengePost post = challengePostRepo.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + postId));
        post.setTitle(dto.getTitle());
        post.setText(dto.getText());
        ChallengePost updatedPost = challengePostRepo.save(post);
        return ChallengePostMapper.toDTO(updatedPost);
    }

    public void deletePostById(int postId) {
        challengePostRepo.deleteById(postId);
    }

    private String formatDateToLatvian(LocalDate date) {
        String day = String.valueOf(date.getDayOfMonth());
        String month = MONTHS.get(date.getMonthValue());
        String year = String.valueOf(date.getYear());
        return day + ". " + month + ", " + year + ".";
    }
}
