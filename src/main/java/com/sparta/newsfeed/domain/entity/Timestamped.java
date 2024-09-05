package com.sparta.newsfeed.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Timestamped {

    @CreatedDate
    @Column(name = "create_at", updatable = false, nullable = false, length = 20)
    protected LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "edit_at", nullable = false, length = 20)
    protected LocalDateTime editAt;

}
