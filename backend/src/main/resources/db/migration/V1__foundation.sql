CREATE TABLE roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  code VARCHAR(30) NOT NULL UNIQUE
);
INSERT INTO roles(code) VALUES ('STUDENT'), ('TEACHER'), ('ADMIN');
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(120) NOT NULL UNIQUE,
  display_name VARCHAR(80) NOT NULL,
  password_hash VARCHAR(100) NOT NULL,
  enabled BOOLEAN NOT NULL DEFAULT TRUE,
  created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  INDEX idx_users_display_name(display_name)
);
CREATE TABLE user_roles (
  user_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY(user_id, role_id),
  CONSTRAINT fk_user_roles_user FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE,
  CONSTRAINT fk_user_roles_role FOREIGN KEY(role_id) REFERENCES roles(id) ON DELETE CASCADE
);
CREATE TABLE audit_events (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  actor_user_id BIGINT NULL,
  action VARCHAR(100) NOT NULL,
  resource_type VARCHAR(80) NOT NULL,
  resource_id VARCHAR(100) NULL,
  created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  INDEX idx_audit_actor_time(actor_user_id, created_at),
  CONSTRAINT fk_audit_actor FOREIGN KEY(actor_user_id) REFERENCES users(id) ON DELETE SET NULL
);
