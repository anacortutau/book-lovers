package com.anuki.booklovers.services;

import com.anuki.booklovers.models.UserEntity;
import com.anuki.booklovers.repositories.UserRepository;
import com.anuki.booklovers.security.jwt.JwtTokenUtil;
import com.anuki.booklovers.security.utils.request.LoginRequest;
import com.anuki.booklovers.security.utils.response.JwtResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserRepository userRepository;

    public JwtResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

//        UserDetails userDetails = org.springframework.security.core.userdetails.User
//                .withUsername(user.getUsername())
//                .password(user.getPassword())
//                .authorities((GrantedAuthority) user.getRoles().stream()
//                        .map(role -> "ROLE_" + role.name())
//                        .collect(Collectors.toList()))
//                .accountExpired(false)
//                .accountLocked(false)
//                .credentialsExpired(false)
//                .disabled(false)
//                .build();
        String jwt = jwtTokenUtil.generateToken(userDetails);

        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + userDetails.getUsername()));

        return new JwtResponse(jwt, user.getId().toString(), user.getUsername(), user.getEmail());
    }
}
