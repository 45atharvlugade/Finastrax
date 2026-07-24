package com.finovoria.finastrax.tenantrepository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finovoria.finastrax.tenantentity.GeneralLedger;

@Repository
public interface GeneralLedgerRepository extends JpaRepository<GeneralLedger, UUID> {

}