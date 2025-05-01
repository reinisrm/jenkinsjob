package com.example.inventorysystem.models.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ChallengePostDTO {
    private int id;
    private String title;
    private String text;
    private String formattedDate;
    private int userId;
    private LocalDateTime createdDate;
}
