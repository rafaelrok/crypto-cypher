CREATE TABLE IF NOT EXISTS roles
(
    id   INT AUTO_INCREMENT NOT NULL,
    name VARCHAR(20) NULL,
    CONSTRAINT pk_roles PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    username          VARCHAR(20)           NULL,
    full_name         VARCHAR(100)          NULL,
    email             VARCHAR(50)           NULL,
    password          VARCHAR(120)          NULL,
    is_verified       BIT(1)                NOT NULL,
    is_first_access   BIT(1)                NOT NULL,
    verification_code VARCHAR(255)          NULL,
    created_at        datetime              NULL,
    updated_at        datetime              NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT uc_74165e195b2f7b25de690d14a UNIQUE (email);

ALTER TABLE users
    ADD CONSTRAINT uc_77584fbe74cc86922be2a3560 UNIQUE (username);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_role FOREIGN KEY (role_id) REFERENCES roles (id);

ALTER TABLE user_roles
    ADD CONSTRAINT fk_userol_on_user FOREIGN KEY (user_id) REFERENCES users (id);

CREATE TABLE IF NOT EXISTS user_roles
(
    role_id INT    NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT pk_user_roles PRIMARY KEY (role_id, user_id)
);
