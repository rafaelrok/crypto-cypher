INSERT INTO users (username, full_name, email, password) VALUES ('admin', 'Administrator', 'rafaelvieiradev@gmail.com', '123456');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO user_roles (role_id, user_id) VALUES (1, 1);
