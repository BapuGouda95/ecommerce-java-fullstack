package com.shopsphere.auth;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final String secret;
    private final long expirationSeconds;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiration-seconds:86400}") long expirationSeconds) {
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("app.jwt.secret must be at least 32 characters");
        }
        this.secret = secret;
        this.expirationSeconds = expirationSeconds;
    }

    public String generate(Long userId, String email, String role) {
        long now = Instant.now().getEpochSecond();
        String header = base64("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = base64("{\"sub\":\"" + userId + "\",\"email\":\"" +
                escape(email) + "\",\"role\":\"" + role + "\",\"iat\":" + now +
                ",\"exp\":" + (now + expirationSeconds) + "}");
        String unsigned = header + "." + payload;
        return unsigned + "." + sign(unsigned);
    }

    public String getSubject(String token) {
        String json = verifiedPayload(token);
        long exp = Long.parseLong(json.replaceAll(".*\"exp\":(\\d+).*", "$1"));
        if (exp <= Instant.now().getEpochSecond()) throw new IllegalArgumentException("Token expired");
        return json.replaceAll(".*\"sub\":\"([^\"]+)\".*", "$1");
    }

    public String getRole(String token) {
        return verifiedPayload(token).replaceAll(".*\"role\":\"([^\"]+)\".*", "$1");
    }

    private String verifiedPayload(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3 || !sign(parts[0] + "." + parts[1]).equals(parts[2])) {
            throw new IllegalArgumentException("Invalid token");
        }
        return new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
    }

    private String base64(String value) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String sign(String value) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(mac.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to sign JWT", e);
        }
    }
}
