package com.finovoria.finastrax.entity;


import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(
    name="app_user",
    uniqueConstraints = {
        @UniqueConstraint(columnNames="email")
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(
        name="user_id",
        nullable=false,
        updatable=false
    )
    private UUID userId;



    @Column(
        name="username",
        nullable=false,
        unique=true
    )
    private String username;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name="tenant_id",
        nullable=false
    )
    private Tenant tenant;



    @Column(
        name="full_name",
        nullable=false
    )
    private String fullName;



    @Column(
        nullable=false,
        unique=true
    )
    private String email;



    @Column(
        nullable=false
    )
    private String password;



    @Column(nullable=false)
    private boolean enabled;



    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private Role role;



    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private UserStatus status;



    @CreationTimestamp
    @Column(
        name="created_at",
        nullable=false,
        updatable=false
    )
    private LocalDateTime createdAt;
    
    @UpdateTimestamp()
    @Column(name="updated_at",insertable = false)
    private LocalDateTime updatedAt;

}