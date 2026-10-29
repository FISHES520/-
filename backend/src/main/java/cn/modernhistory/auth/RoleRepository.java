package cn.modernhistory.auth;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class RoleRepository {
    public Optional<RoleEntity> findByCode(Role role) {
        return Optional.of(new RoleEntity(role));
    }
}