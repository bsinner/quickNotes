DROP TABLE IF EXISTS users;

CREATE TABLE users (
  id int AUTO_INCREMENT PRIMARY KEY
  , username varchar(30) NOT NULL UNIQUE
  , email varchar(100) NOT NULL UNIQUE
  , password varchar(25) NOT NULL
  , isAdmin boolean DEFAULT false
);