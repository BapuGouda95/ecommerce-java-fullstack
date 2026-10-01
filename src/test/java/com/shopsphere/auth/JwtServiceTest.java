package com.shopsphere.auth;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class JwtServiceTest {
 private final JwtService jwt=new JwtService("test-secret-that-is-long-enough-for-hs256-123456",3600);
 @Test void generatedTokenContainsExpectedClaims(){String token=jwt.generate(42L,"user@example.com","USER");assertEquals("42",jwt.getSubject(token));assertEquals("USER",jwt.getRole(token));}
 @Test void tamperedTokenIsRejected(){String token=jwt.generate(42L,"user@example.com","USER");String tampered=token.substring(0,token.length()-1)+"x";assertThrows(IllegalArgumentException.class,()->jwt.getSubject(tampered));}
}