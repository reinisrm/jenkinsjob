package com.example.inventorysystem.config;

import com.example.inventorysystem.models.User;
import com.example.inventorysystem.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsManagerImpl implements UserDetailsManager {

    @Autowired
    private UserRepo userRepo;

    public List<User> allUsers() {
        return userRepo.findAll();
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user != null) {
            return new MyUserDetails(user);
        } else {
            throw new UsernameNotFoundException(username + " not found in the database");
        }
    }

    @Override
    public void createUser(UserDetails user) {
        MyUserDetails myDetails = (MyUserDetails) user;
        User myUser = myDetails.getUser();
        userRepo.save(myUser);
    }

    @Override
    public void updateUser(UserDetails user) {
        MyUserDetails myDetails = (MyUserDetails) user;
        User myUser = myDetails.getUser();
        userRepo.save(myUser);
    }

    @Override
    public void deleteUser(String username) {
        User user = userRepo.findByUsername(username);
        if (user != null) {
            userRepo.delete(user);
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        //change password
    }

    @Override
    public boolean userExists(String username) {
        User user = userRepo.findByUsername(username);
        return user != null;
    }
}