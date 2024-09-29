package com.brian.nekoo.interceptor;

import com.brian.nekoo.service.UserDetailService;
import com.brian.nekoo.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailService userDetailService;
    private final JwtUtil jwtUtil;

    // JWT 驗證工具類
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (request.getCookies() != null) {
            try {
                String nekooToken = Arrays.stream(request.getCookies()).filter(
                        c -> c.getName().equals("nekooToken")
                    ).findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
                if (nekooToken != null) {
                    String email = jwtUtil.extractUsername(nekooToken);
                    UserDetails userDetails = userDetailService.loadUserByUsername(email);
                    UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 繼續執行過濾鏈
        filterChain.doFilter(request, response);
    }
}

