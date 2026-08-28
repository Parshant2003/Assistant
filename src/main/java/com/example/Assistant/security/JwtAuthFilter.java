package com.example.Assistant.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * OncePerRequestFilter = ye code har request pe ek baar chalta hai,
 * controller tak pahunchne se pehle.
 *
 * Flow:
 * 1. Header me "Authorization: Bearer <token>" hai kya? Nahi -> aage badh jao
 *    (kuch endpoints jaise /api/auth/** public hain, unhe token ki zaroorat nahi)
 * 2. Token hai -> usse email nikalo
 * 3. Email se DB me user dhoondo
 * 4. Token valid hai (signature + expiry sahi) -> SecurityContext me
 *    "ye user authenticated hai" set kar do
 * 5. Ab request controller tak pahunchti hai, aur controller me
 *    @AuthenticationPrincipal se current user mil jaata hai
 */
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(7); // "Bearer " ke baad ka part
        final String email = jwtService.extractEmail(token);

        // Agar email mil gaya aur abhi tak koi authenticate nahi hua is request me
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtService.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}