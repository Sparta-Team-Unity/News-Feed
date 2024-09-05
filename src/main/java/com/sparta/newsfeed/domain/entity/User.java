package com.sparta.newsfeed.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString
@Entity
@Getter
@NoArgsConstructor
public class User extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer userId;

    @Column(nullable = false)
    private String email;

    @Column(name = "user_name", nullable = false, length = 40)
    private String name;

    @Column(nullable = false, name = "password", length = 60)
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Token token;

    @Column(name = "is_activate", nullable = false)
    private boolean isActivate = true;

    //@OneToMany(mappedBy = "fromUser", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    //private List<Friend> friends;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Post> postList = new ArrayList<>();

    public User(String email, String password, String name, Token token) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public void giveToken(Token token) {
        this.token = token;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void signOut() {
        this.isActivate = false;
    }
}
