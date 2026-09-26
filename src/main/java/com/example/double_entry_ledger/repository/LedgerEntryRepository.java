package com.example.double_entry_ledger.repository;
import com.example.double_entry_ledger.model.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {
    @Query("""
        SELECT COALESCE(SUM(
            CASE
                WHEN e.entryType = com.example.double_entry_ledger.model.EntryType.DEBIT
                THEN e.amount
                ELSE -e.amount
            END
        ), 0)
        FROM LedgerEntry e
        WHERE e.account.id = :accountId
    """)
    BigDecimal calculateBalance(UUID accountId);
    List<LedgerEntry> findByAccountId(UUID accountId);
}
