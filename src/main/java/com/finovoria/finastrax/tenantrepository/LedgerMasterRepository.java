package com.finovoria.finastrax.tenantrepository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finovoria.finastrax.tenantentity.LedgerMaster;

@Repository
public interface LedgerMasterRepository extends JpaRepository<LedgerMaster, UUID> {

    Optional<LedgerMaster> findByLedgerCode(String ledgerCode);

    boolean existsByLedgerCode(String ledgerCode);
}