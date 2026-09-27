package com.example.double_entry_ledger.service;

import com.example.double_entry_ledger.model.Account;
import com.example.double_entry_ledger.model.AccountType;
import com.example.double_entry_ledger.model.LedgerEntry;
import com.example.double_entry_ledger.exception.AccountNotFoundException;
import com.example.double_entry_ledger.exception.DuplicateAccountException;
import com.example.double_entry_ledger.repository.AccountRepository;
import com.example.double_entry_ledger.repository.LedgerEntryRepository;
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
            throw new IllegalArgumentException(
                    "Account number cannot be blank"
            );
        }

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Account name cannot be blank"
            );
        }

        if (accountType == null) {
            throw new IllegalArgumentException(
                    "Account type cannot be null"
            );
        }

        if (accountRepository.existsByAccountNumber(accountNumber)) {
            throw new DuplicateAccountException(
                    "Account with number " +
                            accountNumber +
                            " already exists"
            );
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
                .orElseThrow(() ->
                        new AccountNotFoundException(
                                "Account not found with id: " + accountId
                        )
                );
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