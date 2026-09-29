package com.shopsphere.user;
import jakarta.persistence.*; import jakarta.validation.constraints.*; import java.time.LocalDateTime;
@Entity @Table(name="users") public class User{
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @NotBlank @Size(max=80) private String name;
 @Email @NotBlank @Column(unique=true,nullable=false) private String email; @NotBlank private String password;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Role role=Role.USER; private LocalDateTime createdAt;
 public User(){} public User(String name,String email,String password,Role role){this.name=name;this.email=email;this.password=password;this.role=role;}
 @PrePersist void onCreate(){createdAt=LocalDateTime.now();}
 public Long getId(){return id;} public String getName(){return name;} public String getEmail(){return email;} public String getPassword(){return password;} public Role getRole(){return role;} public LocalDateTime getCreatedAt(){return createdAt;}
 public void setName(String v){name=v;} public void setEmail(String v){email=v;} public void setPassword(String v){password=v;} public void setRole(Role v){role=v;}
}