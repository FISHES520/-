package cn.modernhistory.auth;

import java.util.Map;
import java.util.Set;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final cn.modernhistory.platform.PlatformService platform;
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;
    private final JwtService jwt;

    public AuthService(
            UserRepository users,
            RoleRepository roles,
            PasswordEncoder encoder,
            AuthenticationManager authManager,
            JwtService jwt,
            cn.modernhistory.platform.PlatformService platform) {
        this.platform = platform;
        this.users = users;
        this.roles = roles;
        this.encoder = encoder;
        this.authManager = authManager;
        this.jwt = jwt;
    }

    public Map<String, Object> register(String email, String name, String password) {
        if (password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length > 72)
            throw new IllegalArgumentException("密码UTF-8长度不能超过72字节");
        String normalized = email.trim().toLowerCase();
        if (users.existsByEmail(normalized)) throw new IllegalArgumentException("该邮箱已注册");
        var student =
                roles.findByCode(Role.STUDENT).orElseThrow(() -> new IllegalStateException("角色数据未初始化"));
        var user =
                users.save(
                        new UserAccount(normalized, name.trim(), encoder.encode(password), Set.of(student)));
        platform.authAudit(user, "REGISTER");
        return session(user);
    }

    public Map<String, Object> login(String email, String password) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(email.trim().toLowerCase(), password));
        var user = users.findByEmail(email.trim().toLowerCase()).orElseThrow();
        platform.authAudit(user, "LOGIN");
        return session(user);
    }

    private Map<String, Object> session(UserAccount user) {
        return Map.of("token", jwt.issue(user), "user", UserView.from(user));
    }

    public record UserView(Long id, String email, String displayName, Set<String> roles) {
        public static UserView from(UserAccount user) {
            return new UserView(
                    user.getId(),
                    user.getEmail(),
                    user.getDisplayName(),
                    user.getRoles().stream()
                            .map(r -> r.getCode().name())
                            .collect(java.util.stream.Collectors.toSet()));
        }
    }
}
