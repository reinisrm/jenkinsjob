package com.example.inventorysystem.controllers;

import com.example.inventorysystem.config.MyUserDetails;
import com.example.inventorysystem.constants.ViewNames;
import com.example.inventorysystem.models.ChallengePost;
import com.example.inventorysystem.models.User;
import com.example.inventorysystem.services.impl.ChallengePostServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/challenges")
public class ChallengePostController {

    private final Logger log = LoggerFactory.getLogger(ChallengePostController.class);
    private final ChallengePostServiceImpl challengePostService;

    @Autowired
    public ChallengePostController(ChallengePostServiceImpl challengePostService) {
        this.challengePostService = challengePostService;
    }

    @GetMapping("/")
    public String showAllPosts(Model model) {
        log.info("Displaying all challenge posts");
        List<ChallengePost> posts = challengePostService.getAllPosts();
        model.addAttribute("posts", posts);
        return ViewNames.SHOW_ALL_CHALLENGES;
    }

    @GetMapping("/{postId}")
    public String showOnePost(@PathVariable("postId") int postId, Model model) {
        log.info("Displaying challenge post with id: {}", postId);
        ChallengePost post = challengePostService.getPostById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + postId));
        model.addAttribute("post", post);
        return ViewNames.SHOW_ONE_CHALLENGE;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("challengePost", new ChallengePost());
        return ViewNames.CREATE_CHALLENGE;
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute ChallengePost challengePost, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            log.info("Attempting to create a new challenge post");
            User user = ((MyUserDetails) userDetails).getUser();
            challengePostService.createPost(challengePost, user.getUserId());
            log.info("Challenge post created successfully");
            return ViewNames.REDIRECT_CHALLENGES;
        } catch (Exception e) {
            log.error("Error creating challenge post: {}", e.getMessage(), e);
            return ViewNames.ERROR;
        }
    }

    @GetMapping("/update/{postId}")
    public String showUpdateForm(@PathVariable("postId") int postId, Model model) {
        ChallengePost post = challengePostService.getPostById(postId)
                .orElseThrow(() -> new NoSuchElementException("Post not found with id: " + postId));
        model.addAttribute("challengePost", post);
        return ViewNames.CHALLENGE_UPDATE;
    }

    @PostMapping("/update/{postId}")
    public String updatePost(@PathVariable("postId") int postId, @ModelAttribute ChallengePost challengePost) {
        try {
            log.info("Attempting to update challenge post with id: {}", postId);
            challengePostService.updatePost(postId, challengePost);
            log.info("Challenge post with id: {} updated successfully", postId);
            return "redirect:/challenges/{postId}";
        } catch (Exception e) {
            log.error("Error updating challenge post: {}", e.getMessage(), e);
            return ViewNames.ERROR;
        }
    }

    @PostMapping("/delete/{postId}")
    public String deletePost(@PathVariable("postId") int postId) {
        try {
            log.info("Attempting to delete challenge post with id: {}", postId);
            challengePostService.deletePostById(postId);
            log.info("Challenge post with id: {} deleted successfully", postId);
            return ViewNames.REDIRECT_CHALLENGES;
        } catch (Exception e) {
            log.error("Error deleting challenge post: {}", e.getMessage(), e);
            return ViewNames.ERROR;
        }
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoSuchElementException(NoSuchElementException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return ViewNames.ERROR;
    }
}