package com.example.inventorysystem.mappers;

import com.example.inventorysystem.models.dto.ChallengePostDTO;
import com.example.inventorysystem.models.ChallengePost;
import com.example.inventorysystem.models.User;

public class ChallengePostMapper {
    public static ChallengePostDTO toDTO(ChallengePost post) {
        ChallengePostDTO dto = new ChallengePostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setText(post.getText());
        dto.setFormattedDate(post.getFormattedDate());
        dto.setCreatedDate(post.getCreatedDate());
        if (post.getUser() != null) {
            dto.setUserId(post.getUser().getUserId());
        }
        return dto;
    }

    public static ChallengePost toEntity(ChallengePostDTO dto, User user) {
        ChallengePost post = new ChallengePost();
        post.setId(dto.getId());
        post.setTitle(dto.getTitle());
        post.setText(dto.getText());
        post.setCreatedDate(dto.getCreatedDate());
        post.setUser(user);
        return post;
    }
}
