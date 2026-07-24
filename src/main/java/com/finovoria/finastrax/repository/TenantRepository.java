package com.finovoria.finastrax.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finovoria.finastrax.entity.Tenant;


@Repository
public interface TenantRepository 
        extends JpaRepository<Tenant, Long> {


    Optional<Tenant> findByBankCode(String bankCode);


    boolean existsByBankCode(String bankCode);


}