package com.finovoria.finastrax.repository;


import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finovoria.finastrax.entity.AppUser;


@Repository
public interface AppUserRepository 
        extends JpaRepository<AppUser, UUID> {


    Optional<AppUser> findByEmail(String email);


    Optional<AppUser> findByUsername(String username);


    boolean existsByEmail(String email);


    boolean existsByUsername(String username);

}