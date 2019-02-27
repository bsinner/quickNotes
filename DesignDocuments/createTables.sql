DROP TABLE IF EXISTS users, notes;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY
    , username VARCHAR(30) NOT NULL UNIQUE
    , email VARCHAR(100) NOT NULL UNIQUE
    , password VARCHAR(25) NOT NULL
);

CREATE TABLE notes (
    id INT AUTO_INCREMENT PRIMARY KEY
    , user_id INT NOT NULL
    , title VARCHAR(40)
    , contents TEXT
    , creation_date DATETIME DEFAULT CURRENT_TIMESTAMP
    , CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES users(id)
);
