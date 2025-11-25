package com.wikigroup.desarrolloweb.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Servicio principal que gestiona los tokens JWT.
 * Responsabilidades:
 * - Generación de tokens con claims personalizados
 * - Extracción de información del token
 * - Validación de tokens (firma y expiración)
 */
@Service
public class JwtService {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration.time}")
    private Long expirationTime;

    /**
     * Obtiene la clave de firma HMAC-SHA desde la configuración
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /**
     * Genera un token JWT con el email del usuario como subject
     * y claims adicionales (empresaId, rol)
     */
    public String generateToken(String email, Long empresaId, String rol) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("empresaId", empresaId);
        extraClaims.put("rol", rol);
        
        return buildToken(extraClaims, email);
    }

    /**
     * Genera un token JWT básico solo con el email
     */
    public String generateToken(String email) {
        return buildToken(new HashMap<>(), email);
    }

    /**
     * Construye el token JWT con los claims y tiempo de expiración
     */
    private String buildToken(Map<String, Object> extraClaims, String username) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el username (email) del token
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrae un claim específico del token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Extrae todos los claims del token
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Extrae el email del usuario del token
     */
    public String extractEmail(String token) {
        return extractUsername(token);
    }

    /**
     * Extrae la empresa ID del token
     */
    public Long extractEmpresaId(String token) {
        return extractClaim(token, claims -> claims.get("empresaId", Long.class));
    }

    /**
     * Extrae el rol del token
     */
    public String extractRol(String token) {
        return extractClaim(token, claims -> claims.get("rol", String.class));
    }

    /**
     * Valida si el token es válido (firma correcta y no expirado)
     */
    public boolean isTokenValid(String token) {
        try {
            extractAllClaims(token);
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verifica si el token ha expirado
     */
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrae la fecha de expiración del token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
}
