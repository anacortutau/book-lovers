package com.anuki.booklovers.controllers;

import com.anuki.booklovers.security.utils.request.LoginRequest;
import com.anuki.booklovers.security.utils.request.SignUpRequest;
import com.anuki.booklovers.security.utils.response.JwtResponse;
import com.anuki.booklovers.security.utils.response.MessageResponse;
import com.anuki.booklovers.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/book-lovers/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authenticationService.authenticateUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        MessageResponse messageResponse = authenticationService.registerUser(signUpRequest);
        return new ResponseEntity<>(messageResponse, HttpStatus.CREATED);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        authenticationService.logoutUser();
        return ResponseEntity.ok(new MessageResponse("Logout successful"));
    }
}
