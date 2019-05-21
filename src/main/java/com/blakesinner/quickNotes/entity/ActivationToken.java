package com.blakesinner.quickNotes.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "ActivationToken")
@Table(name = "activation_tokens")
public class ActivationToken {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private User user;

    @Column(name = "creation_date", insertable = false)
    private LocalDateTime creationDate;
//    id INT NOT NULL PRIMARY KEY
//    , user_id INT NOT NULL
//    , creation_date DATETIME DEFAULT CURRENT_TIMESTAMP
}
