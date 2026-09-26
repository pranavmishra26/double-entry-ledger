package com.example.double_entry_ledger.model;
import jakarta.persistence.*;
import java.util.UUID;
@Entity
@Table(name="accounts")
public class Account {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable=false,unique=true)
    private String accountNumber;
    @Column(nullable=false)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable=false)
    private AccountType accountType;
    @Column(nullable=false)
    private boolean active = true;
    public Account() {}
    public Account(String accountNumber, String name, AccountType type) {
        this.accountNumber = accountNumber;
        this.name = name;
        this.accountType = type;
    }
    public UUID getId() {
        return id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getName() {
        return name;
    }

    public AccountType getType() {
        return accountType;
    }

    public boolean isActive() {
        return active;
    }
    public void deactivate() {
        this.active = false;
    }
}
