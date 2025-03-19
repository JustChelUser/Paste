package org.example.paste.service;

import org.example.paste.Model.Role;
import org.example.paste.Model.User;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Profile("test")
@Service
public class TestUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if ("testUser".equals(username)) {
            return new User(1L, "testUser", "password", "test@mail.ru", Role.ROLE_USER, null);
        }
        throw new UsernameNotFoundException("User not found");
    }
}
