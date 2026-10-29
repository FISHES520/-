package cn.modernhistory.auth;

public class RoleEntity {
    private final Role code;

    public RoleEntity(Role code) {
        this.code = code;
    }

    public Role getCode() {
        return code;
    }
}
