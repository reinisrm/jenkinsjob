package com.example.inventorysystem.models;

import java.util.ArrayList;
import java.util.Collection;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "Authority")
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "authority_Id")
    private int authorityId;

    @Column(name = "Title")
    @NotNull
    private String title;

    @ManyToMany
    @JoinTable(
            name = "Users_Authorities",
            joinColumns = @JoinColumn(name = "authority_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Collection<User> users = new ArrayList<>();

    public Collection<User> getUsers() {
        return users;
    }

    public void setUsers(Collection<User> users) {
        this.users = users;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorityId() {
        return authorityId;
    }

    public Authority() {

    }

    public Authority(String title) {
        setTitle(title);

    }

    public void addUser(User user) {
        if(!users.contains(user)) {
            users.add(user);
        }
    }
    public void removeUser(User user) {
        if(users.contains(user)) {
            users.remove(user);
        }
    }
}