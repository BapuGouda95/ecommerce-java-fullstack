package com.shopsphere.auth;
import javax.crypto.Mac; import javax.crypto.spec.SecretKeySpec; import java.nio.charset.StandardCharsets; import java.time.Instant; import java.util.Base64; import org.springframework.beans.factory.annotation.Value; import org.springframework.stereotype.Service;
@Service public class JwtService{
 private final String secret; private final long expirationSeconds;
 public JwtService(@Value("${app.jwt.secret}") String secret,@Value("${app.jwt.expiration-seconds:86400}") long expirationSeconds){this.secret=secret;this.expirationSeconds=expirationSeconds;}
 public String generate(Long userId,String email,String role){long now=Instant.now().getEpochSecond();String h=base64("{"alg":"HS256","typ":"JWT"}");String p=base64("{"sub":""+userId+"","email":""+escape(email)+"","role":""+role+"","iat":"+now+","exp":"+(now+expirationSeconds)+"}");String s=h+"."+p;return s+"."+sign(s);}
 public String getSubject(String token){String[] p=parts(token);String json=payload(p);long exp=Long.parseLong(json.replaceAll(".*\"exp\":(\\d+).*","$1"));if(exp<Instant.now().getEpochSecond())throw new IllegalArgumentException("Token expired");return json.replaceAll(".*\"sub\":\"([^\"]+)\".*","$1");}
 public String getRole(String token){return payload(parts(token)).replaceAll(".*\"role\":\"([^\"]+)\".*","$1");}
 private String[] parts(String token){String[] p=token.split("\\.");if(p.length!=3||!sign(p[0]+"."+p[1]).equals(p[2]))throw new IllegalArgumentException("Invalid token");return p;}
 private String payload(String[] p){return new String(Base64.getUrlDecoder().decode(p[1]),StandardCharsets.UTF_8);}
 private String base64(String s){return Base64.getUrlEncoder().withoutPadding().encodeToString(s.getBytes(StandardCharsets.UTF_8));}
 private String escape(String s){return s.replace("\\","\\\\").replace(""","\\"");}
 private String sign(String v){try{Mac m=Mac.getInstance("HmacSHA256");m.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),"HmacSHA256"));return Base64.getUrlEncoder().withoutPadding().encodeToString(m.doFinal(v.getBytes(StandardCharsets.UTF_8)));}catch(Exception e){throw new IllegalStateException(e);}}
}