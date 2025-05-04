package com.example.inventorysystem.services.impl;

import com.example.inventorysystem.models.ChallengePost;
import com.example.inventorysystem.models.User;
import com.example.inventorysystem.models.dto.ChallengePostDTO;
import com.example.inventorysystem.repos.ChallengePostRepo;
import com.example.inventorysystem.repos.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

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
    void testGetAllPostDTOs() {
        ChallengePost post = new ChallengePost();
        post.setCreatedDate(LocalDateTime.now());
        List<ChallengePost> posts = new ArrayList<>();
        posts.add(post);

        when(challengePostRepo.findAll()).thenReturn(posts);

        List<ChallengePostDTO> dtos = challengePostService.getAllPostDTOs();

        assertEquals(1, dtos.size());
        assertNotNull(dtos.get(0).getFormattedDate());
    }

    @Test
    void testGetPostDTOById_Existing() {
        int postId = 1;
        ChallengePost post = new ChallengePost();
        post.setCreatedDate(LocalDateTime.now());

        when(challengePostRepo.findById(postId)).thenReturn(Optional.of(post));

        Optional<ChallengePostDTO> result = challengePostService.getPostDTOById(postId);

        assertTrue(result.isPresent());
        assertNotNull(result.get().getFormattedDate());
    }

    @Test
    void testGetPostDTOById_NonExisting() {
        int postId = 999;
        when(challengePostRepo.findById(postId)).thenReturn(Optional.empty());

        Optional<ChallengePostDTO> result = challengePostService.getPostDTOById(postId);

        assertTrue(result.isEmpty());
    }

    @Test
    void testCreatePost_ValidUser() {
        int userId = 1;
        ChallengePostDTO dto = new ChallengePostDTO();
        dto.setTitle("New Title");
        dto.setText("New Text");

        User user = new User();
        ChallengePost saved = new ChallengePost();
        saved.setTitle(dto.getTitle());
        saved.setText(dto.getText());

        when(userRepo.findById(userId)).thenReturn(Optional.of(user));
        when(challengePostRepo.save(any(ChallengePost.class))).thenReturn(saved);

        ChallengePostDTO created = challengePostService.createPost(dto, userId);

        assertEquals("New Title", created.getTitle());
        assertEquals("New Text", created.getText());
    }

    @Test
    void testCreatePost_InvalidUser() {
        int userId = 999;
        ChallengePostDTO dto = new ChallengePostDTO();

        when(userRepo.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> challengePostService.createPost(dto, userId));
    }

    @Test
    void testUpdatePost_ExistingPost() {
        int postId = 1;
        ChallengePost existing = new ChallengePost();
        existing.setTitle("Old");
        existing.setText("Old text");

        ChallengePostDTO updateDTO = new ChallengePostDTO();
        updateDTO.setTitle("Updated");
        updateDTO.setText("Updated text");

        when(challengePostRepo.findById(postId)).thenReturn(Optional.of(existing));
        when(challengePostRepo.save(any(ChallengePost.class))).thenReturn(existing);

        ChallengePostDTO updated = challengePostService.updatePost(postId, updateDTO);

        assertEquals("Updated", updated.getTitle());
        assertEquals("Updated text", updated.getText());
    }

    @Test
    void testUpdatePost_NonExistingPost() {
        int postId = 999;
        ChallengePostDTO dto = new ChallengePostDTO();

        when(challengePostRepo.findById(postId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> challengePostService.updatePost(postId, dto));
    }

    @Test
    void testDeletePostById() {
        int postId = 1;

        challengePostService.deletePostById(postId);

        verify(challengePostRepo).deleteById(postId);
    }
}