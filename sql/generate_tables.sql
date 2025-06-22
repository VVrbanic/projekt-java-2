CREATE TABLE statuses (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(255) NOT NULL
);
CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       first_name VARCHAR(100),
                       last_name VARCHAR(100),
                       user_name VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       is_admin BOOLEAN DEFAULT FALSE,
                       email VARCHAR(255) UNIQUE
);
CREATE TABLE conflicts (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           reporter_id BIGINT NOT NULL,
                           description VARCHAR(1000),
                           status_id BIGINT,
                           created_at DATETIME,
                           updated_at DATETIME,
                           updated_by BIGINT,
                           FOREIGN KEY (reporter_id) REFERENCES users(id),
                           FOREIGN KEY (status_id) REFERENCES statuses(id),
                           FOREIGN KEY (updated_by) REFERENCES users(id)
);
CREATE TABLE conflict_users (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                conflict_id BIGINT NOT NULL,
                                user_id BIGINT NOT NULL,
                                FOREIGN KEY (conflict_id) REFERENCES conflicts(id) ON DELETE CASCADE,
                                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

INSERT INTO statuses (name) VALUES
    ('New'),
    ('In Review'),
    ('Resolved');


ALTER TABLE conflicts
DROP COLUMN created_at;

ALTER TABLE conflicts
DROP COLUMN updated_at;

ALTER TABLE conflicts
DROP COLUMN updated_by;

ALTER TABLE conflicts ADD date DATE;

ALTER TABLE users
ADD active number;

UPDATE users
SET active = 1;

