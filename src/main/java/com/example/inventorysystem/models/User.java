package com.example.inventorysystem.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_table")
@Data
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private int userId;
    @Column(name = "username", unique = true)
    private String username;
    @Column(name = "password")
    private String password;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Collection<Authority> authorities = new ArrayList<>();


    @OneToOne(mappedBy = "user")
    private Person person;

    @OneToMany(mappedBy = "user")
    private List<ChallengePost> challengePosts;

    public Collection<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Collection<Authority> authorities) {
        this.authorities = authorities;
    }

    public User(String username, String password) {
        super();
        this.username = username;
        setPassword(password);
    }

    public void addAuthority(Authority authority) {
        if(!authorities.contains(authority)) {
            authorities.add(authority);
        }
    }
    public void removeAuthority(Authority authority) {
        if(authorities.contains(authority)) {
            authorities.remove(authority);
        }
    }
}