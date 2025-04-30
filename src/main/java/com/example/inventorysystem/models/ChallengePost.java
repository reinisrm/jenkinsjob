package com.example.inventorysystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "challenge_post")
@NoArgsConstructor
public class ChallengePost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 255)
    private String title;

    @Column(length = 5000, columnDefinition = "LONGTEXT")
    private String text;

    @Transient
    private String formattedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();
    }

    public ChallengePost(String title, String text, User user, LocalDateTime createdDate) {
        this.title = title;
        this.text = text;
        this.user = user;
        this.createdDate = createdDate;
    }
}
