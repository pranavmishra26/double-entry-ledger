package com.example.double_entry_ledger.model;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "ledger_transactions")
public class LedgerTransaction {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LedgerEntry> entries = new ArrayList<>();
    public LedgerTransaction() {
        this.createdAt = LocalDateTime.now();
    }
    public UUID getId() {
        return id;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public List<LedgerEntry> getEntries() {
        return entries;
    }
    public void addEntry(LedgerEntry entry) {
        entries.add(entry);
    }
}