/*package com.bmw.store.services;

import com.bmw.store.Repositories.UserRepository;
import com.bmw.store.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // Make it final

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) { // Constructor injection
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    public User registerUser(User user) {
        if (userRepository.findByemail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email déjà utilisé");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedDate(LocalDateTime.now());
        user.setStatus("active");
        user.setRole("VISITOR");
        user.setLastLogin(LocalDateTime.now());

        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByemail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Assuming 'role' is a String like "ADMIN", "VISITOR"
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_" + user.getRole());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities
        );
    }
}*/