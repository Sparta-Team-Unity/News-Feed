package com.sparta.newsfeed.domain.entity;

import com.sparta.newsfeed.config.DateUtil;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
public class Timestamped {

    @CreatedDate
    @Column(name = "create_at", updatable = false, nullable = false, length = 20)
    protected LocalDateTime createAt;

    @LastModifiedDate
    @Column(name = "edit_at", nullable = false, length = 20)
    protected LocalDateTime editAt;

    /**
     *  데이터 삽입 전 진행되는 함수
     */
    @PrePersist
    public void prePersist() {
        this.createAt = LocalDateTime.parse(DateUtil.localDateTimeToString(LocalDateTime.now(DateUtil.getTimeZone())));
        this.editAt = this.createAt;
    }

    /**
     * 데이터 갱신 전 진행되는 함수
     */
    @PreUpdate
    public void preUpdate() {
        this.editAt = LocalDateTime.parse(DateUtil.localDateTimeToString(LocalDateTime.now(DateUtil.getTimeZone())));
    }
}
