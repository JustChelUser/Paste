package org.example.paste.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.paste.Model.User;
import org.example.paste.exception.UserEmailAlreadyInUseException;
import org.example.paste.exception.UserNotFoundException;
import org.example.paste.exception.UsernameAlreadyInUseException;
import org.example.paste.repository.UserRepository;
import org.example.paste.service.UserService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UsernameAlreadyInUseException("User with username " + user.getUsername() + " already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserEmailAlreadyInUseException("User with email " + user.getEmail() + " already exists");
        }
        return save(user);
    }

    @Override
    public User getByUserName(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    }

    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUserName;
    }
}
