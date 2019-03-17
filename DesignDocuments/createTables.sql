DROP TABLE IF EXISTS notes;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
    , username VARCHAR(30) NOT NULL UNIQUE
    , email VARCHAR(100) NOT NULL UNIQUE
    , password VARCHAR(25) NOT NULL
);

CREATE TABLE notes (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
    , user_id INT NOT NULL
    , title VARCHAR(40) DEFAULT NULL
    , contents TEXT
    , creation_date DATETIME DEFAULT CURRENT_TIMESTAMP
    , CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES users(id)
        ON DELETE CASCADE ON UPDATE CASCADE
    , UNIQUE (user_id, title)
);

CREATE TABLE user_roles (
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY
    , username VARCHAR(30) NOT NULL UNIQUE
    , role ENUM ("REGULAR", "ADMIN") DEFAULT "REGULAR"
    , CONSTRAINT user_roles_fk FOREIGN KEY (username) REFERENCES users(username)
        ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO users
    (id, username, email, password)
VALUES
    (1, "bsmith", "bsmith@gmail.com", "password1")
    , (2, "zsmith", "zsmith@gmail.com", "password2")
    , (3, "ldavis", "ldavis@gmail.com", "password35");

INSERT INTO notes
    (user_id, title, contents)
VALUES
    (2, "Hello World Note", '{"ops":[{"insert":"hello "},{"attributes":{"bold":true},"insert":"world"},{"insert":"\\\\n"}]}')
    , (3, "Hello World Note", '{"ops":[{"insert":"hello "},{"attributes":{"bold":true},"insert":"world"},{"insert":"\\\\n"}]}')
    , (3, "Lorem Ipsum", '{"ops":[{"attributes":{"italic":true},"insert":"Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."},{"insert":"\\\\n"}]}');
