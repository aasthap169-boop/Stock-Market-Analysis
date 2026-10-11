package com.stockmarket.analysis.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.stockmarket.analysis.entity.User;
import com.stockmarket.analysis.repository.UserRepository;

@Repository
public class UserDao {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email);
    }

    public boolean isEmailExists(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }
}