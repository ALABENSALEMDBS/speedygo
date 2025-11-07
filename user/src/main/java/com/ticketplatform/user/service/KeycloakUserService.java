package com.ticketplatform.user.service;

import com.ticketplatform.user.entity.User;
import com.ticketplatform.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeycloakUserService {

    @Autowired
    private UserRepository userRepository;

    public User getOrCreateUser(String userId, String firstName, String lastName, String email, String username, List<String> roles) {
        return userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setActive(true);
                    newUser.setRoles(roles);
                    return userRepository.save(newUser);
                });
    }
}

