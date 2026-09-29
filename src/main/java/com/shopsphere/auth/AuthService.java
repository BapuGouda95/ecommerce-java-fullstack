package com.shopsphere.auth;
import com.shopsphere.user.*; import org.springframework.security.crypto.password.PasswordEncoder; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
@Service public class AuthService{
 private final UserRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
 public AuthService(UserRepository users,PasswordEncoder encoder,JwtService jwt){this.users=users;this.encoder=encoder;this.jwt=jwt;}
 @Transactional public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest r){if(users.existsByEmailIgnoreCase(r.email()))throw new IllegalArgumentException("Email is already registered");User u=new User(r.name(),r.email().toLowerCase(),encoder.encode(r.password()),Role.USER);users.save(u);return response(u);}
 public AuthDtos.AuthResponse login(AuthDtos.LoginRequest r){User u=users.findByEmailIgnoreCase(r.email()).orElseThrow(()->new IllegalArgumentException("Invalid email or password"));if(!encoder.matches(r.password(),u.getPassword()))throw new IllegalArgumentException("Invalid email or password");return response(u);}
 private AuthDtos.AuthResponse response(User u){return new AuthDtos.AuthResponse(jwt.generate(u.getId(),u.getEmail(),u.getRole().name()),u.getId(),u.getName(),u.getEmail(),u.getRole());}
}