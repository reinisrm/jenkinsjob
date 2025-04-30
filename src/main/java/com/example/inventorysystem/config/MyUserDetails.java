package com.example.inventorysystem.config;

import com.example.inventorysystem.models.Authority;
import com.example.inventorysystem.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class MyUserDetails implements UserDetails {

    private User user;

    public MyUserDetails(User user) {
        this.user = user;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Iterator<Authority> iter = user.getAuthorities().iterator();

        while(iter.hasNext()) {
            SimpleGrantedAuthority temp = new SimpleGrantedAuthority(iter.next().getTitle());
            authorities.add(temp);
        }

        return authorities;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
