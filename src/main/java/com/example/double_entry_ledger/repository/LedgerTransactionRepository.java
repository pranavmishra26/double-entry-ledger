package com.example.double_entry_ledger.repository;
import com.example.double_entry_ledger.model.LedgerTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface LedgerTransactionRepository extends JpaRepository<LedgerTransaction, UUID>{
}
