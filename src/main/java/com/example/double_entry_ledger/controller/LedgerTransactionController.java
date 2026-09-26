package com.example.double_entry_ledger.controller;
import com.example.double_entry_ledger.model.LedgerEntry;
import com.example.double_entry_ledger.model.LedgerTransaction;
import com.example.double_entry_ledger.service.LedgerTransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@RestController
@RequestMapping("/transactions")
public class LedgerTransactionController {
    private final LedgerTransactionService ledgerTransactionService;
    public LedgerTransactionController(
            LedgerTransactionService ledgerTransactionService) {
        this.ledgerTransactionService = ledgerTransactionService;
    }
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(
            @RequestBody CreateTransactionRequest request) {
        LedgerTransaction transaction =
                ledgerTransactionService.createTransaction(
                        request.debitAccountId(),
                        request.creditAccountId(),
                        request.amount()
                );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toResponse(transaction));
    }
    private TransactionResponse toResponse(
            LedgerTransaction transaction) {
        List<LedgerEntryResponse> entries =
                transaction.getEntries()
                        .stream()
                        .map(entry -> new LedgerEntryResponse(
                                entry.getId(),
                                entry.getAccount().getId(),
                                entry.getEntryType(),
                                entry.getAmount()
                        ))
                        .toList();
        return new TransactionResponse(
                transaction.getId(),
                transaction.getCreatedAt(),
                entries
        );
    }
    public record CreateTransactionRequest(
            UUID debitAccountId,
            UUID creditAccountId,
            BigDecimal amount
    ) {
    }
    public record TransactionResponse(
            UUID id,
            LocalDateTime createdAt,
            List<LedgerEntryResponse> entries
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
