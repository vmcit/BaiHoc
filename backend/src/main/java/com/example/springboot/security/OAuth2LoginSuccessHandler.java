package com.example.springboot.security;

import com.example.springboot.entities.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * OAuth2LoginSuccessHandler
 * Sau khi Google xác thực thành công, handler này:
 * 1. Lấy thông tin user từ CustomOAuth2UserDetails
 * 2. Tạo JWT access token
 * 3. Redirect về frontend kèm token trong query param
 *
 * Frontend sẽ nhận token tại: http://localhost:5173/oauth2/callback?token=xxx
 */
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${app.oauth2.redirect-uri:http://localhost:5173/oauth2/callback}")
    private String redirectUri;

    public OAuth2LoginSuccessHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        CustomOAuth2UserDetails oAuth2User = (CustomOAuth2UserDetails) authentication.getPrincipal();
        User user = oAuth2User.getUser();

        // Tập hợp roles của user
        java.util.Set<String> roles = user.getRoles().stream()
                .map(role -> "ROLE_" + role.getRoleName())
                .collect(Collectors.toSet());

        // Tạo JWT access token
        String token = jwtTokenProvider.generateAccessToken(user.getUsername(), user.getId(), roles);

        // Redirect về frontend với token
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token)
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
