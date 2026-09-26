package com.example.double_entry_ledger.model;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;
@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "transaction_id", nullable = false)
    private LedgerTransaction transaction;
    @ManyToOne(optional = false)
    private Account account;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntryType entryType;
    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    public LedgerEntry() {
    }
    public LedgerEntry(LedgerTransaction transaction, Account account,
                       EntryType entryType, BigDecimal amount) {
        this.transaction = transaction;
        this.account = account;
        this.entryType = entryType;
        this.amount = amount;
    }
    public UUID getId() {
        return id;
    }
    public LedgerTransaction getTransaction() {
        return transaction;
    }
    public Account getAccount() {
        return account;
    }
    public EntryType getEntryType() {
        return entryType;
    }
    public BigDecimal getAmount() {
        return amount;
    }
}