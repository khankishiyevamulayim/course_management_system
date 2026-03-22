package org.example.coursemanagementsystem.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String userIdHeader = request.getHeader("X-User-Id");
            String roleHeader   = request.getHeader("X-User-Role");

            if (StringUtils.hasText(userIdHeader) && StringUtils.hasText(roleHeader)) {
                // FIX: NumberFormatException ayrıca handle edilir
                Long userId;
                try {
                    userId = Long.parseLong(userIdHeader);
                } catch (NumberFormatException e) {
                    log.warn("X-User-Id header rəqəm deyil: {}", userIdHeader);
                    filterChain.doFilter(request, response);
                    return;
                }

                String role = roleHeader.startsWith("ROLE_") ? roleHeader : "ROLE_" + roleHeader;
                setAuthentication(request, userId, role);
                log.debug("Header-dən authentication quruldu — userId: {}, role: {}", userId, role);

            } else {
                String jwt = getJwtFromRequest(request);
                if (StringUtils.hasText(jwt) && jwtUtils.validateToken(jwt)) {
                    Long userId = jwtUtils.getUserIdFromToken(jwt);
                    String role = "ROLE_" + jwtUtils.getRoleFromToken(jwt);
                    setAuthentication(request, userId, role);
                    log.debug("JWT-dən authentication quruldu — userId: {}", userId);
                }
            }
        } catch (Exception ex) {
            log.error("Authentication qurula bilmədi: {}", ex.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(HttpServletRequest request, Long userId, String role) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        userId, null, Collections.singletonList(authority));
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
