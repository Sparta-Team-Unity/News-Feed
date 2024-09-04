package com.sparta.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Friend extends Timestamped{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "to_user_user_id")
    private User toUser;

    @ManyToOne
    @JoinColumn(name = "from_user_user_id")
    private User fromUser;
    private boolean isAccepted;
    private LocalDateTime requestAt;

    public Friend( User toUser, User fromUser) {
        this.toUser = toUser;
        this.fromUser = fromUser;
        this.isAccepted = false;
        this.requestAt = LocalDateTime.now();
    }

}
