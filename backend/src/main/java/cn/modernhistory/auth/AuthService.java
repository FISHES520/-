package cn.modernhistory.auth;

import java.util.Map;
import java.util.Set;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {
    private final UserRepository users; private final RoleRepository roles; private final PasswordEncoder encoder; private final AuthenticationManager authManager; private final JwtService jwt;
    public AuthService(UserRepository users, RoleRepository roles, PasswordEncoder encoder, AuthenticationManager authManager, JwtService jwt) { this.users=users; this.roles=roles; this.encoder=encoder; this.authManager=authManager; this.jwt=jwt; }
    @Transactional
    public Map<String,Object> register(String email, String name, String password) {
        String normalized = email.trim().toLowerCase();
        if(users.existsByEmail(normalized)) throw new IllegalArgumentException("该邮箱已注册");
        var student = roles.findByCode(Role.STUDENT).orElseThrow(() -> new IllegalStateException("角色数据未初始化"));
        var user = users.save(new UserAccount(normalized, name.trim(), encoder.encode(password), Set.of(student)));
        return session(user);
    }
    public Map<String,Object> login(String email, String password) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(email.trim().toLowerCase(), password));
        return session(users.findByEmail(email.trim().toLowerCase()).orElseThrow());
    }
    private Map<String,Object> session(UserAccount user) { return Map.of("token", jwt.issue(user), "user", UserView.from(user)); }
    public record UserView(Long id, String email, String displayName, Set<String> roles) {
        static UserView from(UserAccount user) { return new UserView(user.getId(), user.getEmail(), user.getDisplayName(), user.getRoles().stream().map(r -> r.getCode().name()).collect(java.util.stream.Collectors.toSet())); }
    }
}
