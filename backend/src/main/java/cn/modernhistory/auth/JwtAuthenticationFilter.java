package cn.modernhistory.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwt; private final UserRepository users;
    public JwtAuthenticationFilter(JwtService jwt, UserRepository users) { this.jwt=jwt; this.users=users; }
    @Override protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
        String header=req.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")) try {
            var user=users.findByEmail(jwt.subject(header.substring(7))).orElseThrow();
            var authorities=user.getRoles().stream().map(r -> new SimpleGrantedAuthority("ROLE_"+r.getCode().name())).collect(Collectors.toSet());
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities));
        } catch (RuntimeException ignored) { SecurityContextHolder.clearContext(); }
        chain.doFilter(req,res);
    }
}
