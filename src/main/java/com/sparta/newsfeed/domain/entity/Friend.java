package com.sparta.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "friends")
public class Friend extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "to_user", nullable = false)
    private User toUser;

    @ManyToOne
    @JoinColumn(name = "from_user", nullable = false)
    private User fromUser;

    @Column(name = "is_accepted", nullable = false)
    private boolean isAccepted;

    @Column(name = "request_at", nullable = false)
    private LocalDateTime requestAt;

    public Friend( User toUser, User fromUser) {
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.isAccepted = false;
        this.requestAt = LocalDateTime.now();
    }

}
