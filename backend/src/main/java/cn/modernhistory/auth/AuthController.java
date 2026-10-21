package cn.modernhistory.auth;

import cn.modernhistory.common.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.Map;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/auth")
public class AuthController {
    private final AuthService auth; private final UserRepository users;
    public AuthController(AuthService auth, UserRepository users) { this.auth=auth; this.users=users; }
    @PostMapping("/register") public ApiResponse<Map<String,Object>> register(@Valid @RequestBody RegisterRequest request) { return ApiResponse.ok(auth.register(request.email(),request.displayName(),request.password())); }
    @PostMapping("/login") public ApiResponse<Map<String,Object>> login(@Valid @RequestBody LoginRequest request) { return ApiResponse.ok(auth.login(request.email(),request.password())); }
    @GetMapping("/me") public ApiResponse<AuthService.UserView> me(Authentication principal) { return ApiResponse.ok(AuthService.UserView.from(users.findByEmail(principal.getName()).orElseThrow())); }
    public record RegisterRequest(@Email @NotBlank String email, @NotBlank @Size(min=2,max=80) String displayName, @NotBlank @Size(min=8,max=100) String password) {}
    public record LoginRequest(@Email @NotBlank String email, @NotBlank String password) {}
}
