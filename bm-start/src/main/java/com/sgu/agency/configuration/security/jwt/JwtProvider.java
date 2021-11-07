package com.sgu.agency.configuration.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    @Value("${grokonez.app.jwtSecret}")
    private String jwtSecret;

    @Value("${grokonez.app.jwtExpiration}")
    private int jwtExpiration;

    public String generateJwtToken(Authentication authentication) {

        UserPrinciple userPrincipal = (UserPrinciple) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpiration*1000*60))
                .signWith(SignatureAlgorithm.HS512, this.getJwtSecretBase64())
                .claim("password", userPrincipal.getPassword())
                .claim("permissions", userPrincipal.getAuthorities())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(this.getJwtSecretBase64())
                .parseClaimsJws(token)
                .getBody().getSubject();
    }

    public String getPasswordFromJwtToken(String token) {
        return (String)Jwts.parser()
                .setSigningKey(this.getJwtSecretBase64())
                .parseClaimsJws(token)
                .getBody().setSubject("password").get("password");
    }

    public String getUserModelFromJwtToken(String token) {
        return (String)Jwts.parser()
                .setSigningKey(this.getJwtSecretBase64())
                .parseClaimsJws(token)
                .getBody().setSubject("userModel").get("userModel");
    }

    public List<String> getPermissionsFromJwToken(String token) {
        List<GrantedAuthority> grantedAuthorities = (List<GrantedAuthority>)Jwts.parser()
                .setSigningKey(this.getJwtSecretBase64())
                .parseClaimsJws(token)
                .getBody().setSubject("permissions").get("permissions");

        List<String> permissions = new ArrayList<>();

        for(int i = 0; i < grantedAuthorities.size(); i++) {
            ObjectMapper oMapper = new ObjectMapper();

            HashMap<String, String> aut = oMapper.convertValue(grantedAuthorities.get(i), HashMap.class);
            permissions.add(aut.get("authority").replaceFirst("ROLE_", ""));
        }

        return permissions;
    }

    public String getCompanyIdFromJwtToken(String token) {
        return (String) Jwts.parser()
                .setSigningKey(this.getJwtSecretBase64())
                .parseClaimsJws(token)
                .getBody().setSubject("companyId").get("companyId");
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(this.getJwtSecretBase64()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature -> Message: {} ", e);
            throw e;
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token -> Message: {}", e);
            throw e;
        } catch (ExpiredJwtException e) {
            logger.error("Expired JWT token -> Message: {}", e);
            throw e;
        } catch (UnsupportedJwtException e) {
            logger.error("Unsupported JWT token -> Message: {}", e);
            throw e;
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty -> Message: {}", e);
            throw e;
        }
    }

    private String getJwtSecretBase64() {
        if (this.jwtSecret == null || this.jwtSecret.isEmpty()) {
            return null;
        }
        return Base64.getEncoder().encodeToString(this.jwtSecret.getBytes());
    }
}
