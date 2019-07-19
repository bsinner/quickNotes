DELETE FROM users;

INSERT INTO users
    (id, username, email, password)
VALUES
    (1, "bsmith", "bsmith@example.com", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8")
    , (2, "admin", "admin@example.com", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8")
    , (3, "ldavis", "ldavis@example.com", "5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8");