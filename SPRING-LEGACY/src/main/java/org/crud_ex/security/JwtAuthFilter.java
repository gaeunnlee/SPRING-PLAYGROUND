package org.crud_ex.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String ctx = request.getContextPath();
        String path = (ctx != null && !ctx.isEmpty()) ? uri.substring(ctx.length()) : uri;

        if (!path.startsWith("/api/")) return true;
        if (path.startsWith("/api/admin/")) return true;
        return false;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest req,
            HttpServletResponse res,
            FilterChain chain
    ) throws ServletException, IOException {

        // 이미 인증된 경우(다른 인증이 세팅된 경우)면 그대로 통과
        Authentication existing = SecurityContextHolder.getContext().getAuthentication();
        if (existing != null && existing.isAuthenticated()) {
            chain.doFilter(req, res);
            return;
        }

        String token = extractAccessToken(req);

        // /api/** 인데 토큰이 없으면 → 401
        if (token == null || token.isBlank()) {
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            String email = jwtProvider.parse(token).getPayload().getSubject();
            UserDetails user = userDetailsService.loadUserByUsername(email);

            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(auth);

            chain.doFilter(req, res);

        } catch (Exception e) {
            SecurityContextHolder.clearContext();
            res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String extractAccessToken(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if ("accessToken".equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }
}
