package org.example.coursemanagementsystem.security;

import lombok.RequiredArgsConstructor;
import org.example.coursemanagementsystem.entity.User;
import org.example.coursemanagementsystem.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailAndIsActiveTrue(email)
                .orElseThrow(() -> new UsernameNotFoundException("İstifadəçi tapılmadı: " + email));
        return new UserDetailsImpl(user);
    }
}