package com.example.double_entry_ledger.service;
import com.example.double_entry_ledger.model.Account;
import com.example.double_entry_ledger.model.AccountType;
import com.example.double_entry_ledger.repository.AccountRepository;
import com.example.double_entry_ledger.repository.LedgerEntryRepository;
import com.example.double_entry_ledger.model.LedgerEntry;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final LedgerEntryRepository ledgerEntryRepository;
    public AccountService(
            AccountRepository accountRepository,
            LedgerEntryRepository ledgerEntryRepository) {
        this.accountRepository = accountRepository;
        this.ledgerEntryRepository = ledgerEntryRepository;
    }
    public Account createAccount(
            String accountNumber,
            String name,
            AccountType accountType) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new RuntimeException("Account number cannot be blank");
        }
        if (name == null || name.isBlank()) {
            throw new RuntimeException("Account name cannot be blank");
        }
        if (accountType == null) {
            throw new RuntimeException("Account type is required");
        }
        if (accountRepository.existsByAccountNumber(accountNumber)) {
            throw new RuntimeException("Account number already exists");
        }
        Account account = new Account(
                accountNumber,
                name,
                accountType
        );
        return accountRepository.save(account);
    }
    public Account getAccount(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
    public BigDecimal getBalance(UUID accountId) {
        getAccount(accountId);
        return ledgerEntryRepository.calculateBalance(accountId);
    }
    public List<LedgerEntry> getLedgerHistory(UUID accountId) {
        getAccount(accountId);
        return ledgerEntryRepository.findByAccountId(accountId);
    }
}