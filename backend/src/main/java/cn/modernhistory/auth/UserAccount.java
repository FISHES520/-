package cn.modernhistory.auth;

import java.util.Set;

public class UserAccount {
    private Long id;
    private final String email, displayName, passwordHash;
    private final Set<RoleEntity> roles;
    private volatile boolean enabled = true;

    public UserAccount(String email, String displayName, String passwordHash, Set<RoleEntity> roles) {
        this.email = email;
        this.displayName = displayName;
        this.passwordHash = passwordHash;
        this.roles = Set.copyOf(roles);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Set<RoleEntity> getRoles() {
        return roles;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean value) {
        enabled = value;
    }

    public boolean has(Role role) {
        return roles.stream().anyMatch(r -> r.getCode() == role);
    }
}
