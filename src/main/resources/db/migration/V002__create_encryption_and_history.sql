CREATE TABLE IF NOT EXISTS encryption
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    type       VARCHAR(50)          NULL,
    content    VARCHAR(4000)          NULL,
    file_type  VARCHAR(10)          NULL,
    created_at datetime              NULL,
    updated_at datetime              NULL,
    CONSTRAINT pk_encryption PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS encryption_history
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    user_id       BIGINT                NULL,
    encryption_id BIGINT                NULL,
    CONSTRAINT pk_encryption_history PRIMARY KEY (id)
);

ALTER TABLE encryption_history
    ADD CONSTRAINT FK_ENCRYPTION_HISTORY_ON_ENCRYPTION FOREIGN KEY (encryption_id) REFERENCES encryption (id);

ALTER TABLE encryption_history
    ADD CONSTRAINT FK_ENCRYPTION_HISTORY_ON_USER FOREIGN KEY (user_id) REFERENCES user (id);
