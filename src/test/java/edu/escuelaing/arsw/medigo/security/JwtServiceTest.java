package edu.escuelaing.arsw.medigo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;
    private final String SECRET = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secretKey", SECRET);
    }

    private String generateToken(String subject, long expirationMs, Map<String, Object> extraClaims) {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    @Test
    void extractUsername_ShouldReturnCorrectUsername() {
        String token = generateToken("testuser", 1000 * 60, new HashMap<>());
        assertEquals("testuser", jwtService.extractUsername(token));
    }

    @Test
    void isTokenValid_ShouldReturnTrueForValidToken() {
        String token = generateToken("testuser", 1000 * 60, new HashMap<>());
        assertTrue(jwtService.isTokenValid(token));
    }

    @Test
    void isTokenValid_ShouldReturnFalseForExpiredToken() {
        String token = generateToken("testuser", -1000, new HashMap<>());
        assertFalse(jwtService.isTokenValid(token));
    }

    @Test
    void extractClaim_ShouldExtractCustomClaim() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "ADMIN");
        String token = generateToken("testuser", 1000 * 60, claims);
        
        String role = jwtService.extractClaim(token, c -> c.get("role", String.class));
        assertEquals("ADMIN", role);
    }
}
