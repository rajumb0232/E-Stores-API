package com.devb.estores.security;

import com.devb.estores.model.User;
import com.devb.estores.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class AuthUtils {

    private final UserRepo userRepo;

    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getCurrentUsername() {
        return this.getAuthentication().getName();
    }

    public List<String> getAuthorities() {
        return this.getAuthentication().getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .toList();
    }

    public User getCurrentUser() throws UsernameNotFoundException {
        return userRepo.findByUsername(this.getCurrentUsername())
                .orElseThrow(() -> new UsernameNotFoundException("Failed to find user details"));
    }
}
