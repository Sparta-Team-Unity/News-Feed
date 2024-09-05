CREATE TABLE user (
                      id BIGINT auto_increment primary key ,
                      email VARCHAR(40) not null ,
                      password VARCHAR(80) not null ,
                      user_name VARCHAR(20) not null,
                      is_activated TINYINT(1) not null ,
                      create_at DATETIME not null ,
                      edit_at DATETIME not null
);

CREATE TABLE post (
                      id BIGINT auto_increment primary key ,
                      title VARCHAR(30) not null ,
                      content VARCHAR(100) not null ,
                      create_at DATETIME not null ,
                      edit_at DATETIME not null ,
                      user BIGINT not null ,
                      FOREIGN KEY (user) REFERENCES user(id)
);

CREATE TABLE friends (
                         id BIGINT auto_increment primary key ,
                         to_user BIGINT not null ,
                         from_user BIGINT not null ,
                         is_accepted TINYINT(1) not null ,
                         request_at DATETIME not null ,
                         FOREIGN KEY (to_user) REFERENCES user(id),
                         FOREIGN KEY (from_user) REFERENCES user(id)
);

CREATE TABLE token(
                      id BIGINT auto_increment primary key ,
                      access_token VARCHAR(255) not null,
                      refresh_token VARCHAR(255) not null,
                      FOREIGN KEY (user) REFERENCES user(id)
);

CREATE TABLE blacklist_token(
                                blacklist_token VARCHAR(255) primary key
);