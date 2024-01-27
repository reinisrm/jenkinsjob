package com.example.InventorySystem.config;

import com.example.InventorySystem.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import com.example.InventorySystem.repos.UserRepo;

import java.util.List;
@Service
public class UserDetailsManagerImpl implements UserDetailsManager {

    @Autowired
    private UserRepo userRepo;

    public List<User> allUsers() {

        return (List<User>) userRepo.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user != null) {
            MyUserDetails details = new MyUserDetails(user);
            return details;
        } else {
            throw new UsernameNotFoundException(username + "nav atrasts datubazee");
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
        if(user != null) { //eksistee tads lietotajs datubazee
            userRepo.delete(user);
        }
    }

    //TODO implementet kad tas ir nepieciesams
    @Override
    public void changePassword(String oldPassword, String newPassword) {
        // TODO Auto-generated method stub

    }
    @Override
    public boolean userExists(String username) {
        User user = userRepo.findByUsername(username);

        if(user != null) {
            return true;
        } else {
            return false;
        }


    }

}
