package com.example.InventorySystem.controllers;

import com.example.InventorySystem.models.ChallengePost;
import com.example.InventorySystem.services.impl.ChallengePostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ChallengePostControllerTest {

    @InjectMocks
    private ChallengePostController challengePostController;

    @Mock
    private ChallengePostServiceImpl challengePostService;

    @Mock
    private Model model;

    private MockMvc mockMvc;

    private ChallengePost setUpCreateChallengePost() {
        ChallengePost challengePost = new ChallengePost();
        challengePost.setTitle("Sample Title");
        challengePost.setText("Sample Text");
        return challengePost;
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(challengePostController).build();
    }

    @Test
    void testShowAllPosts() {
        List<ChallengePost> posts = new ArrayList<>();
        posts.add(setUpCreateChallengePost());
        when(challengePostService.getAllPosts()).thenReturn(posts);

        String viewName = challengePostController.showAllPosts(model);

        verify(model).addAttribute("posts", posts);
        assertEquals("show-all-challenges", viewName);
    }

    @Test
    void testShowOnePost() throws Exception {
        ChallengePost challengePost = setUpCreateChallengePost();
        challengePost.setId(1);

        when(challengePostService.getPostById(challengePost.getId())).thenReturn(Optional.of(challengePost));

        mockMvc.perform(get("/challenges/{postId}", challengePost.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("show-one-challenge"))
                .andExpect(model().attribute("post", challengePost));
    }

    @Test
    void testShowOnePostNonExistentId() throws Exception {
        int nonExistentPostId = 999;
        when(challengePostService.getPostById(nonExistentPostId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/challenges/{postId}", nonExistentPostId))
                .andExpect(status().isNotFound());

        verify(challengePostService).getPostById(nonExistentPostId);
    }

    @Test
    void testCreatePostSuccess() throws Exception { //todo fix this
        ChallengePost challengePost = setUpCreateChallengePost();
        challengePost.setId(1);

        when(challengePostService.createPost(any(ChallengePost.class), anyInt())).thenReturn(challengePost);

        mockMvc.perform(post("/challenges/create")
                        .param("title", challengePost.getTitle())
                        .param("text", challengePost.getText())
                        .param("userId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/challenges/"));

        verify(challengePostService).createPost(any(ChallengePost.class), anyInt());
    }

    @Test
    void testUpdatePostSuccess() throws Exception {
        ChallengePost challengePost = setUpCreateChallengePost();
        challengePost.setId(1);

        mockMvc.perform(post("/challenges/update/{postId}", challengePost.getId())
                        .flashAttr("challengePost", challengePost))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/challenges/{postId}"));

        verify(challengePostService).updatePost(eq(challengePost.getId()), any(ChallengePost.class));
    }

    @Test
    void testDeletePost() throws Exception {
        ChallengePost challengePost = setUpCreateChallengePost();
        challengePost.setId(1);

        mockMvc.perform(post("/challenges/delete/{postId}", challengePost.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/challenges/"));

        verify(challengePostService).deletePostById(challengePost.getId());
    }
}
