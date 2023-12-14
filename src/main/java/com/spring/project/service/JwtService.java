package com.spring.project.service;

import com.spring.project.model.Client;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static String SECRET_KEY = "3ydqfkokUNlvGpjJL1CwHXPNbL+HjG0PF6VElzLhbabLmDAAYzUPKpXjTi+bn4kd";

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;
    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    public String extractClientUsername(String token){
        return extractClaim(token, Claims::getSubject);
    }

    public String extractClientRole(String token){
        return extractAllClaims(token).get("role", String.class);
    }

    public String generateToken(String email, String role){
        Map<String,Object> extraClaims = new HashMap<>();
        extraClaims.put("sub", email);
        extraClaims.put("role", role);
        return generateToken(extraClaims, email,jwtExpiration);
    }
    public String generateToken(Map<String, Object> extraClaims,
                                String email,
                                long jwtExpiration){

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(email).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email, String role){
        Map<String,Object> extraClaims = new HashMap<>();
        extraClaims.put("sub", email);
        extraClaims.put("role", role);
        return generateToken(extraClaims, email,refreshExpiration);
    }


    public boolean isTokenValid(String token, UserDetails clientDetails){
         final String email = extractClientUsername(token);
         return (email.equals(clientDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Collection<? extends GrantedAuthority> getAuthorities(String jwt) {
        String rolesClaim = extractClientRole(jwt);

        // Split the roles string into a list
        List<String> rolesList = Arrays.asList(rolesClaim.split(","));

        // Convert the list of roles to a list of GrantedAuthority objects
        return rolesList.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
