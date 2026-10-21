package cn.modernhistory.auth;

import jakarta.persistence.*;

@Entity @Table(name = "roles")
public class RoleEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Enumerated(EnumType.STRING) @Column(nullable = false, unique = true, length = 30) private Role code;
    protected RoleEntity() {}
    public Role getCode() { return code; }
}
