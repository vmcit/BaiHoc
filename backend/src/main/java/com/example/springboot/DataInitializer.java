package com.example.springboot;

import com.example.springboot.entities.Role;
import com.example.springboot.entities.User;
import com.example.springboot.repository.RoleRepository;
import com.example.springboot.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * DataInitializer - Tạo tài khoản giả lập đăng nhập bằng SĐT khi ứng dụng khởi động.
 *
 * Tài khoản:  SĐT = 0968965682  |  Mật khẩu = 123456
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        createPhoneMockUser();
        createTotpMockUser();
    }

    private void createPhoneMockUser() {
        String phone = "0968965682";

        // Tránh tạo trùng
        if (userRepository.findByPhone(phone).isPresent()) {
            log.info("[DataInitializer] Tài khoản SĐT {} đã tồn tại, bỏ qua.", phone);
            return;
        }

        // Đảm bảo role USER tồn tại
        Role userRole = roleRepository.findByRoleName("USER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleName("USER");
                    r.setDescription("Default user role");
                    r.setStatus("ACTIVE");
                    return roleRepository.save(r);
                });

        User user = new User();
        user.setUsername("phone_" + phone);
        user.setEmail("phone_" + phone + "@mock.local");
        user.setPhone(phone);
        user.setFullName("Nguoi Dung SDT");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setStatus("ACTIVE");
        user.setProvider("LOCAL");

        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
        log.info("[DataInitializer] Da tao tai khoan gia lap: SDT={} / MK=123456", phone);
    }

    private void createTotpMockUser() {
        String phone = "0936352582";
        if (userRepository.findByPhone(phone).isPresent()) {
            log.info("[DataInitializer] Tai khoan TOTP {} da ton tai, bo qua.", phone);
            return;
        }

        Role userRole = roleRepository.findByRoleName("USER")
                .orElseGet(() -> {
                    Role r = new Role();
                    r.setRoleName("USER");
                    r.setDescription("Default user role");
                    r.setStatus("ACTIVE");
                    return roleRepository.save(r);
                });

        User user = new User();
        user.setUsername("totp_" + phone);
        user.setEmail("totp_" + phone + "@mock.local");
        user.setPhone(phone);
        user.setFullName("Nguoi Dung TOTP");
        user.setPassword(passwordEncoder.encode("123456"));
        user.setStatus("ACTIVE");
        user.setProvider("LOCAL");
        user.setTotpEnabled(false);

        java.util.Set<Role> roles = new java.util.HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);

        userRepository.save(user);
        log.info("[DataInitializer] Da tao tai khoan TOTP: SDT={} / MK=123456", phone);
    }
}
