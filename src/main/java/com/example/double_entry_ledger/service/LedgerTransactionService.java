package com.example.double_entry_ledger.service;
import com.example.double_entry_ledger.model.Account;
import com.example.double_entry_ledger.model.EntryType;
import com.example.double_entry_ledger.model.LedgerEntry;
import com.example.double_entry_ledger.model.LedgerTransaction;
import com.example.double_entry_ledger.repository.LedgerTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.UUID;
@Service
public class LedgerTransactionService {
    private final LedgerTransactionRepository ledgerTransactionRepository;
    private final AccountService accountService;
    public LedgerTransactionService(
            LedgerTransactionRepository ledgerTransactionRepository,
            AccountService accountService) {
        this.ledgerTransactionRepository = ledgerTransactionRepository;
        this.accountService = accountService;
    }
    @Transactional
    public LedgerTransaction createTransaction(
            UUID debitAccountId,
            UUID creditAccountId,
            BigDecimal amount) {
        if (debitAccountId.equals(creditAccountId)) {
            throw new RuntimeException("Debit and credit accounts must be different");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }
        Account debitAccount = accountService.getAccount(debitAccountId);
        Account creditAccount = accountService.getAccount(creditAccountId);
        if (!debitAccount.isActive()) {
            throw new RuntimeException("Debit account is inactive");
        }
        if (!creditAccount.isActive()) {
            throw new RuntimeException("Credit account is inactive");
        }
        LedgerTransaction transaction = new LedgerTransaction();
        LedgerEntry debitEntry = new LedgerEntry(
                transaction,
                debitAccount,
                EntryType.DEBIT,
                amount
        );
        LedgerEntry creditEntry = new LedgerEntry(
                transaction,
                creditAccount,
                EntryType.CREDIT,
                amount
        );
        transaction.addEntry(debitEntry);
        transaction.addEntry(creditEntry);
        return ledgerTransactionRepository.save(transaction);
    }
}
