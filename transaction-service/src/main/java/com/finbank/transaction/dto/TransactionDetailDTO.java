package com.finbank.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDetailDTO {
    private Long transactionId;
    private Double amount;
    private String description;
    private String fromAccountNumber;
    private String toAccountNumber;
    private String customerName;
    private String customerEmail;
    private String status;
}