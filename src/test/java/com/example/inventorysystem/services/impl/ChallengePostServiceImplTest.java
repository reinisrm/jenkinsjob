package com.example.inventorysystem.services.impl;

import com.example.inventorysystem.models.ChallengePost;
import com.example.inventorysystem.models.User;
import com.example.inventorysystem.repos.ChallengePostRepo;
import com.example.inventorysystem.repos.UserRepo;
import com.example.inventorysystem.services.impl.ChallengePostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChallengePostServiceImplTest {

    @InjectMocks
    private ChallengePostServiceImpl challengePostService;

    @Mock
    private ChallengePostRepo challengePostRepo;

    @Mock
    private UserRepo userRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPosts() {
        List<ChallengePost> expectedPosts = new ArrayList<>();
        ChallengePost post = new ChallengePost();
        post.setCreatedDate(LocalDateTime.now());
        expectedPosts.add(post);

        when(challengePostRepo.findAll()).thenReturn(expectedPosts);

        List<ChallengePost> actualPosts = challengePostService.getAllPosts();

        assertEquals(expectedPosts.size(), actualPosts.size());
        assertEquals(expectedPosts.get(0).getCreatedDate(), actualPosts.get(0).getCreatedDate());
    }

    @Test
    void testGetPostById_ExistingId() {
        int postId = 1;
        ChallengePost expectedPost = new ChallengePost();
        expectedPost.setCreatedDate(LocalDateTime.now());
        when(challengePostRepo.findById(postId)).thenReturn(Optional.of(expectedPost));

        Optional<ChallengePost> actualPostOptional = challengePostService.getPostById(postId);
        assertTrue(actualPostOptional.isPresent());
        assertEquals(expectedPost.getCreatedDate(), actualPostOptional.get().getCreatedDate());
    }

    @Test
    void testGetPostById_NonExistingId() {
        int postId = 999;
        when(challengePostRepo.findById(postId)).thenReturn(Optional.empty());

        Optional<ChallengePost> actualPostOptional = challengePostService.getPostById(postId);
        assertTrue(actualPostOptional.isEmpty());
    }

    @Test
    void testCreatePost_WithValidUser() {
        ChallengePost post = new ChallengePost();
        post.setCreatedDate(LocalDateTime.now());
        int userId = 1;
        User user = new User();
        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(challengePostRepo.save(post)).thenReturn(post);

        ChallengePost createdPost = challengePostService.createPost(post, userId);

        verify(challengePostRepo).save(post);
        assertEquals(user, post.getUser());
    }

    @Test
    void testCreatePost_WithNonExistingUser() {
        ChallengePost post = new ChallengePost();
        int userId = 999;
        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> challengePostService.createPost(post, userId));
        verify(challengePostRepo, never()).save(post);
    }

    @Test
    void testUpdatePost_ExistingId() {
        int postId = 1;
        ChallengePost existingPost = new ChallengePost();
        existingPost.setCreatedDate(LocalDateTime.now());
        ChallengePost updatedPostData = new ChallengePost();
        updatedPostData.setTitle("Updated Title");
        updatedPostData.setText("Updated Text");

        when(challengePostRepo.findById(postId)).thenReturn(Optional.of(existingPost));
        when(challengePostRepo.save(any(ChallengePost.class))).thenReturn(existingPost);

        ChallengePost updatedPost = challengePostService.updatePost(postId, updatedPostData);

        verify(challengePostRepo).save(existingPost);
        assertEquals("Updated Title", existingPost.getTitle());
        assertEquals("Updated Text", existingPost.getText());
    }

    @Test
    void testUpdatePost_NonExistingId() {
        int postId = 999;
        ChallengePost updatedPostData = new ChallengePost();
        when(challengePostRepo.findById(postId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> challengePostService.updatePost(postId, updatedPostData));
    }

    @Test
    void testDeletePostById_ExistingId() {
        int postId = 1;
        ChallengePost post = new ChallengePost();
        when(challengePostRepo.findById(postId)).thenReturn(Optional.of(post));

        challengePostService.deletePostById(postId);

        verify(challengePostRepo).deleteById(postId);
    }

    @Test
    void testDeletePostById_NonExistingId() {
        int postId = 999;
        when(challengePostRepo.findById(postId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> challengePostService.deletePostById(postId));
        verify(challengePostRepo, never()).deleteById(postId);
    }
}