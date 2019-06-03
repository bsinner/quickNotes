package com.blakesinner.quickNotes.entity;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "RefreshToken")
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;

    @ManyToOne
    private User user;

    @Column(name = "expire_date", insertable = false)
    private LocalDateTime expireDate;

    public RefreshToken() {
    }

    public RefreshToken(User user) {
        this.user = user;
    }
}
