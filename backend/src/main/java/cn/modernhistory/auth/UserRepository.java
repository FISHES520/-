package cn.modernhistory.auth;

import java.util.*;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final Map<Long, UserAccount> data = new LinkedHashMap<>();
    private long next = 1;

    public synchronized Optional<UserAccount> findByEmail(String email) {
        return data.values().stream().filter(u -> u.getEmail().equals(email)).findFirst();
    }

    public synchronized Optional<UserAccount> findById(long id) {
        return Optional.ofNullable(data.get(id));
    }

    public synchronized boolean existsByEmail(String email) {
        return findByEmail(email).isPresent();
    }

    public synchronized List<UserAccount> findAll() {
        return List.copyOf(data.values());
    }

    public synchronized UserAccount save(UserAccount u) {
        if (u.getId() == null) {
            if (existsByEmail(u.getEmail())) throw new IllegalArgumentException("邮箱已注册");
            u.setId(next++);
        }
        data.put(u.getId(), u);
        return u;
    }
}
