package com.finbank.transaction.controller;

import com.finbank.transaction.dto.TransactionDetailDTO;
import com.finbank.transaction.dto.TransferRequestDTO;
import com.finbank.transaction.entity.Transaction;
import com.finbank.transaction.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestBody TransferRequestDTO request) {
        return ResponseEntity.ok(transactionService.transferMoney(request));
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<TransactionDetailDTO> getDetail(@PathVariable("id") Long id) {
        return ResponseEntity.ok(transactionService.getTransactionDetail(id));
    }
}