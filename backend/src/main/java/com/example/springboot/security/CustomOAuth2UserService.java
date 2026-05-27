package com.example.springboot.security;

import com.example.springboot.entities.Role;
import com.example.springboot.entities.User;
import com.example.springboot.repository.RoleRepository;
import com.example.springboot.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * CustomOAuth2UserService - Xử lý thông tin người dùng từ Google OAuth2
 * Sau khi Google xác thực thành công, Spring Security gọi service này để:
 * 1. Lấy thông tin user từ Google
 * 2. Tìm kiếm hoặc tạo mới user trong database
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOAuth2UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // Gọi Google API để lấy thông tin người dùng
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        String provider = userRequest.getClientRegistration().getRegistrationId(); // "google"
        String providerId = (String) attributes.get("sub");   // Google unique user ID
        String email      = (String) attributes.get("email");
        String name       = (String) attributes.get("name");
        String picture    = (String) attributes.get("picture");

        // Tìm user theo email hoặc tạo mới
        User user = userRepository.findByEmail(email)
                .map(existing -> updateExistingUser(existing, providerId, name, picture))
                .orElseGet(() -> createNewOAuth2User(provider, providerId, email, name, picture));

        return new CustomOAuth2UserDetails(user, attributes);
    }

    /**
     * Cập nhật thông tin user đã tồn tại (đăng nhập lại bằng Google)
     */
    private User updateExistingUser(User user, String providerId, String name, String picture) {
        user.setProviderId(providerId);
        user.setFullName(name);
        user.setAvatarUrl(picture);
        user.setUpdatedAt(System.currentTimeMillis());
        if (user.getProvider() == null) {
            user.setProvider("GOOGLE");
        }
        return userRepository.save(user);
    }

    /**
     * Tạo user mới từ thông tin Google (lần đầu đăng nhập bằng Google)
     */
    private User createNewOAuth2User(String provider, String providerId, String email, String name, String picture) {
        // Tạo username từ email (lấy phần trước @)
        String baseUsername = email.contains("@") ? email.split("@")[0] : email;
        String username = generateUniqueUsername(baseUsername);

        Role userRole = roleRepository.findByRoleName("USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setRoleName("USER");
                    newRole.setStatus("ACTIVE");
                    return roleRepository.save(newRole);
                });

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword("{oauth2}" + java.util.UUID.randomUUID()); // placeholder, không dùng để login
        newUser.setFullName(name);
        newUser.setAvatarUrl(picture);
        newUser.setProvider(provider.toUpperCase()); // "GOOGLE"
        newUser.setProviderId(providerId);
        newUser.setStatus("ACTIVE");
        newUser.setRoles(roles);

        return userRepository.save(newUser);
    }

    /**
     * Sinh username duy nhất (tránh trùng lặp)
     */
    private String generateUniqueUsername(String base) {
        String candidate = base;
        int suffix = 1;
        while (userRepository.existsByUsername(candidate)) {
            candidate = base + suffix++;
        }
        return candidate;
    }
}
