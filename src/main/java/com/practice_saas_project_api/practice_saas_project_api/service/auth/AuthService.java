package com.practice_saas_project_api.practice_saas_project_api.service.auth;

import com.practice_saas_project_api.practice_saas_project_api.entity.User;
import com.practice_saas_project_api.practice_saas_project_api.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String generateToken(String subject) {
        SecretKey key = Keys.hmacShaKeyFor(secretKey.getBytes());

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(key)
                .compact();
    }

    public ResponseEntity<String> login(String email, String password, HttpServletResponse response) {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found with email: " + email);
            }

            User user = optionalUser.get();

            if (!passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.badRequest().body("Invalid password");
            }

            // ✅ Générer le token JWT
            String token = generateToken(email);

            // ✅ Créer un cookie sécurisé
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);  // Sécurise contre les scripts
            cookie.setSecure(true);    // Activer en HTTPS (mettre `false` en dev si nécessaire)
            cookie.setPath("/");       // Accessible dans tout le domaine
            cookie.setMaxAge((int) (jwtExpirationInMs / 1000)); // Expiration en secondes

            // ✅ Ajouter le cookie à la réponse
            response.addCookie(cookie);

            return ResponseEntity.ok("Login successful");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during login: " + e.getMessage());
        }
    }

}
