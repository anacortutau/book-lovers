package com.anuki.booklovers.security.jwt;

import com.anuki.booklovers.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");

        Optional<String> username = jwtTokenUtil.getUsernameFromToken(authorizationHeader.substring(7));
        username.ifPresent(un -> {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                validateAndLoadUserDetails(request, un, authorizationHeader.substring(7))
                        .ifPresent(authentication -> SecurityContextHolder.getContext().setAuthentication(authentication));
            }
        });
        chain.doFilter(request, response);
    }

    private Optional<String> getJwtFromRequest(String header) {
        return Optional.ofNullable(header)
                .filter(h -> h.startsWith("Bearer "))
                .map(h -> h.substring(7));
    }

    private Optional<String> extractUsernameFromJwt(String jwt) {
        return jwtTokenUtil.getUsernameFromToken(jwt);
    }

    private Optional<UsernamePasswordAuthenticationToken> validateAndLoadUserDetails(HttpServletRequest request, String username, String jwt) {
        return Optional.ofNullable(userService.loadUserByUsername(username))
                .filter(userDetails -> jwtTokenUtil.validateToken(jwt, userDetails).orElse(false))
                .map(userDetails -> new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()))
                .map(authentication -> {
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    return authentication;
                });
    }
}
