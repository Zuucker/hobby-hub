/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jwtManager;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 *
 * @author Zuucker
 */
@Component
public class JWTManager {
    private static String jwtKey;
    private static Long jwtTimeToExpire;
    
    public JWTManager(){}
    
    @Autowired
    public JWTManager(@Value("${JWTKey}") String key, @Value("${JWTTime}") String time) {
        jwtKey = key;
        jwtTimeToExpire = Long.valueOf(time);
    }
    
    public String generateToken(String username){
        
        Date presentTime = new Date();
        Date expiryTime = new Date(presentTime.getTime() + jwtTimeToExpire);
        
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(presentTime)
                .setExpiration(expiryTime)
                .signWith(SignatureAlgorithm.HS256, jwtKey)
                .compact();
        
        return token;
    }
    
    private Date getExpirationDate(String token) {
        Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
        
        return claims.getExpiration();
    }   
    
    private String getSubject(String token){
        Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
        
        return claims.getSubject();
    }
    
    public boolean validatetoken(String token, String username){
        
        Date exiprationDateFromToken = getExpirationDate(token);
        
        return (exiprationDateFromToken.after(new Date()) && getSubject(token).equals(username));
    }
    
    
    
    
}
