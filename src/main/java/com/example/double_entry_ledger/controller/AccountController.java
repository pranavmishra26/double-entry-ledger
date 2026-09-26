package com.example.double_entry_ledger.controller;
import com.example.double_entry_ledger.model.Account;
import com.example.double_entry_ledger.model.AccountType;
import com.example.double_entry_ledger.model.LedgerEntry;
import com.example.double_entry_ledger.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(
            @RequestBody CreateAccountRequest request) {
        Account account = accountService.createAccount(
                request.accountNumber(),
                request.name(),
                request.accountType()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(account));
    }
    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccount(
            @PathVariable UUID id) {
        Account account = accountService.getAccount(id);
        return ResponseEntity.ok(toResponse(account));
    }
    @GetMapping("/{id}/balance")
    public ResponseEntity<BalanceResponse> getBalance(
            @PathVariable UUID id) {
        BigDecimal balance = accountService.getBalance(id);
        return ResponseEntity.ok(
                new BalanceResponse(id, balance)
        );
    }
    @GetMapping("/{id}/ledger")
    public ResponseEntity<List<LedgerEntryResponse>> getLedger(
            @PathVariable UUID id) {
        List<LedgerEntry> entries =
                accountService.getLedgerHistory(id);
        List<LedgerEntryResponse> response = entries.stream()
                .map(entry -> new LedgerEntryResponse(
                        entry.getId(),
                        entry.getAccount().getId(),
                        entry.getEntryType(),
                        entry.getAmount()
                ))
                .toList();
        return ResponseEntity.ok(response);
    }
    private AccountResponse toResponse(Account account) {
        return new AccountResponse(
                account.getId(),
                account.getAccountNumber(),
                account.getName(),
                account.getType(),
                account.isActive()
        );
    }
    public record CreateAccountRequest(
            String accountNumber,
            String name,
            AccountType accountType
    ) {
    }
    public record AccountResponse(
            UUID id,
            String accountNumber,
            String name,
            AccountType accountType,
            boolean active
    ) {
    }
    public record BalanceResponse(
            UUID accountId,
            BigDecimal balance
    ) {
    }
    public record LedgerEntryResponse(
            UUID id,
            UUID accountId,
            com.example.double_entry_ledger.model.EntryType entryType,
            BigDecimal amount
    ) {
    }
}
