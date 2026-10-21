package cn.modernhistory.auth;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.Set;

@Entity @Table(name = "users")
public class UserAccount {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 120) private String email;
    @Column(name = "display_name", nullable = false, length = 80) private String displayName;
    @Column(name = "password_hash", nullable = false) private String passwordHash;
    @Column(nullable = false) private boolean enabled = true;
    @Column(name = "created_at", nullable = false, updatable = false) private Instant createdAt;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;
    @PrePersist void created() { createdAt = Instant.now(); }
    protected UserAccount() {}
    public UserAccount(String email, String displayName, String passwordHash, Set<RoleEntity> roles) { this.email=email; this.displayName=displayName; this.passwordHash=passwordHash; this.roles=roles; }
    public Long getId() { return id; } public String getEmail() { return email; } public String getDisplayName() { return displayName; }
    public String getPasswordHash() { return passwordHash; } public boolean isEnabled() { return enabled; } public Set<RoleEntity> getRoles() { return roles; }
}
