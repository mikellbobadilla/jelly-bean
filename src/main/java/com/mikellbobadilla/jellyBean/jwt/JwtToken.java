package com.mikellbobadilla.jellyBean.jwt;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtToken {

  public String createToken(UserDetails userDetails){

    return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24)))
                .signWith(getSignInKey())
                .compact();
  }
  
  public Boolean isTokenValid(String token, UserDetails userDetails){
    final String username = getSubject(token);
    final Date expiration = getAllClaims(token).getExpiration();
    return (username.equals(userDetails.getUsername()) && !expiration.before(new Date()));
  }

  public String getSubject(String token){
    return getAllClaims(token).getSubject();
  }

  public Claims getAllClaims(String token){
    return Jwts
            .parserBuilder()
            .setSigningKey(getSignInKey())
            .build()
            .parseClaimsJws(token)
            .getBody();
  }

  private Key getSignInKey(){
    String secretKey = "mb5ejncUQ/aZz+ixLZzxoMcUPX2AJ6pgZ+EneNmgAnA=";
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
