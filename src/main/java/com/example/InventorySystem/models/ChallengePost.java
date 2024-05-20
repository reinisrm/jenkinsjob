package com.example.InventorySystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "challenge_post")
@NoArgsConstructor
@AllArgsConstructor
public class ChallengePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255)
    private String title;

    @Column(length = 5000)
    private String text;

    @Column
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    public ChallengePost(String title, String text, String imageUrl, User user, LocalDateTime createdDate) {
        this.title = title;
        this.text = text;
        this.imageUrl = imageUrl;
        this.user = user;
        this.createdDate = createdDate;
    }
}
