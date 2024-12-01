package com.trainticketbooking.app.Configs;

import com.trainticketbooking.app.Entities.OAuth2Connection;
import com.trainticketbooking.app.Entities.User;
import com.trainticketbooking.app.Entities.Role;
import com.trainticketbooking.app.Jwt.JwtTokenProvider;
import com.trainticketbooking.app.Services.impl.RoleService;
import com.trainticketbooking.app.Services.impl.UserService;
import com.trainticketbooking.app.Services.impl.OAuth2ConnectionService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final UserService userService;
    private final RoleService roleService;
    private final OAuth2ConnectionService oAuth2ConnectionService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        if ("github".equals(oAuth2AuthenticationToken.getAuthorizedClientRegistrationId())) {
            DefaultOAuth2User principal = (DefaultOAuth2User) authentication.getPrincipal();
            Map<String, Object> attributes = principal.getAttributes();

            String email = (String) attributes.getOrDefault("email", null);
            String name = (String) attributes.getOrDefault("name", "");
            String avatarUrl = (String) attributes.getOrDefault("avatar_url", null);
            String login = (String) attributes.getOrDefault("login", null);

            if (email == null || email.isEmpty()) {
                response.sendRedirect("http://localhost:3000/en/home?error=email_missing");
                return;
            }

            Optional<User> existingUser = userService.findByEmail(email);

            if (existingUser.isPresent()) {
                // Nếu người dùng đã tồn tại, cập nhật thông tin OAuth2Connection nếu chưa có
                User user = existingUser.get();
                if (!oAuth2ConnectionService.existsByUserAndProvider(user, "github")) {
                    // Lưu thông tin kết nối OAuth2 nếu chưa có
                    saveOAuth2Connection(user, "github", (String) attributes.get("id"));
                }
                // Tạo và trả về JWT
                String jwt = jwtTokenProvider.generateToken(user.getEmail());
                response.setHeader("Authorization", "Bearer " + jwt); // Trả về JWT trong header
                response.sendRedirect(frontendUrl + "en/home?success");
            } else {
                // Nếu người dùng chưa tồn tại, tạo mới người dùng và lưu OAuth2Connection
                createUserAndAuthenticate(authentication, email, name, avatarUrl, login, attributes, oAuth2AuthenticationToken, response);
            }
        }
    }

    private void createUserAndAuthenticate(Authentication authentication, String email, String name, String avatarUrl, String login, Map<String, Object> attributes, OAuth2AuthenticationToken oAuth2AuthenticationToken, HttpServletResponse response) {
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setFullName(name);
        newUser.setAvatarUrl(avatarUrl);
        newUser.setUsername(login != null ? login : email);
        newUser.setPassword(""); // Mật khẩu trống, sẽ yêu cầu thiết lập sau

        // Lấy role "ROLE_USER" từ cơ sở dữ liệu hoặc tạo mới nếu chưa có
        Role defaultRole = roleService.findByName("ROLE_USER");
        newUser.setRole(defaultRole);

        userService.save(newUser);

        // Lưu OAuth2Connection sau khi người dùng được tạo
        saveOAuth2Connection(newUser, "github", (String) attributes.get("id"));

        // Tạo và trả về JWT cho người dùng mới
        String jwt = jwtTokenProvider.generateToken(email);
        response.setHeader("Authorization", "Bearer " + jwt); // Trả về JWT trong header

        try {
            response.sendRedirect("http://localhost:3000/en/set-password?email=" + email);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Lưu thông tin OAuth2Connection
    private void saveOAuth2Connection(User user, String provider, String providerId) {
        OAuth2Connection connection = new OAuth2Connection();
        connection.setProvider(provider);
        connection.setProviderId(providerId);
        connection.setConnectedAt(LocalDateTime.now());
        connection.setUser(user);

        // Lưu thông tin kết nối vào cơ sở dữ liệu
        oAuth2ConnectionService.save(connection);
    }
}