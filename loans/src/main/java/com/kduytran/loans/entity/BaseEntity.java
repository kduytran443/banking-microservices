package com.kduytran.loans.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter @Setter
public abstract class BaseEntity {
    @Column
    @CreatedBy
    private String createdBy;

    @Column
    @CreatedDate
    private LocalDateTime createdAt;

    @Column
    @LastModifiedBy
    private String updatedBy;

    @Column
    @LastModifiedDate
    private LocalDateTime updatedAt;
}
