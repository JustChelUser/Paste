package org.example.paste.service;

import org.example.paste.Model.User;
import org.springframework.security.core.userdetails.UserDetailsService;



public interface UserService {
    User save(User user);
    User create(User user);
    User getByUserName(String username);
    UserDetailsService userDetailsService();
}
