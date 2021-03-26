package com.sohanf.crmsystem.service;

import com.sohanf.crmsystem.entity.User;
import com.sohanf.crmsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String subdomain) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findBySubdomain(subdomain);
        return user.map(CustomUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(subdomain + " doesn't exist"));
    }
}
