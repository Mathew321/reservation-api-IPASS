package org.hu.reservation.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    @Mock
    private UserDetails userDetails;

    @Value("${security.reservation.sign-key}")
    private String secret;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateToken_Success() {
        String token = jwtService.generateToken("john.doe");

        assertNotNull(token);
        assertTrue(token.startsWith("eyJhbGciOiJIUzI1NiJ9"));
    }

    @Test
    void extractUsername_ValidToken() {
        String token = createToken("john.doe", new Date(System.currentTimeMillis() + 1000 * 60 * 30));

        String username = jwtService.extractUsername(token);

        assertEquals("john.doe", username);
    }

    @Test
    void extractExpiration_ValidToken() {
        Date expiration = new Date(System.currentTimeMillis() + 1000 * 60 * 30);
        String token = createToken("john.doe", expiration);

        Date extractedExpiration = jwtService.extractExpiration(token);

        assertTrue(extractedExpiration.getTime() - expiration.getTime()<1000);
    }

    @Test
    void extractClaim_ValidToken() {
        String token = createToken("john.doe", new Date(System.currentTimeMillis() + 1000 * 60 * 30));

        String subject = jwtService.extractClaim(token, Claims::getSubject);

        assertEquals("john.doe", subject);
    }

//    @Test
//    void isTokenExpired_NotExpired() {
//        String token = createToken("john.doe", new Date(System.currentTimeMillis() + 1000 * 60 * 30));
//
//        assertFalse(jwtService.isTokenExpired(token));
//    }
//
//    @Test
//    void isTokenExpired_Expired() {
//        String token = createToken("john.doe", new Date(System.currentTimeMillis() - 1000 * 60 * 30));
//
//        assertTrue(jwtService.isTokenExpired(token));
//    }

    @Test
    void validateToken_ValidTokenAndUser() {
        String token = createToken("john.doe", new Date(System.currentTimeMillis() + 1000 * 60 * 30));
        when(userDetails.getUsername()).thenReturn("john.doe");

        assertTrue(jwtService.validateToken(token, userDetails));
    }

    @Test
    void validateToken_InvalidToken() {
        String token = createToken("john.doe", new Date(System.currentTimeMillis() - 1000 * 60 * 30));
        when(userDetails.getUsername()).thenReturn("john.doe");


        assertThrows(ExpiredJwtException.class, ()->{jwtService.validateToken(token, userDetails);});
    }

    @Test
    void validateToken_DifferentUser() {
        String token = createToken("john.doe", new Date(System.currentTimeMillis() + 1000 * 60 * 30));
        when(userDetails.getUsername()).thenReturn("jane.smith");

        assertFalse(jwtService.validateToken(token, userDetails));
    }

    private String createToken(String subject, Date expiration) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(expiration)
                .signWith(jwtService.getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
}
