package com.finbank.transaction.service;

import com.finbank.transaction.client.AccountServiceClient;
import com.finbank.transaction.client.CustomerServiceClient;
import com.finbank.transaction.dto.AccountDTO;
import com.finbank.transaction.dto.CustomerDTO;
import com.finbank.transaction.dto.TransactionDetailDTO;
import com.finbank.transaction.dto.TransferRequestDTO;
import com.finbank.transaction.entity.Transaction;
import com.finbank.transaction.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountServiceClient accountServiceClient;
    private final CustomerServiceClient customerServiceClient;

    public Transaction transferMoney(TransferRequestDTO request) {
        // 1. Kiem tra tai khoan nguon
        AccountDTO fromAccount = accountServiceClient.getAccountByNumber(request.getFromAccountNumber());
        if (fromAccount == null || fromAccount.getBalance() < request.getAmount()) {
            throw new RuntimeException("Tai khoan nguon khong hop le hoac khong du so du");
        }

        // 2. Kiem tra tai khoan dich
        AccountDTO toAccount = accountServiceClient.getAccountByNumber(request.getToAccountNumber());
        if (toAccount == null) {
            throw new RuntimeException("Tai khoan dich khong ton tai");
        }

        // 3. Goi sang Account Service cap nhat so du
        accountServiceClient.updateBalance(request);

        // 4. Luu lich su giao dich
        Transaction transaction = new Transaction();
        transaction.setFromAccountNumber(request.getFromAccountNumber());
        transaction.setToAccountNumber(request.getToAccountNumber());
        transaction.setAmount(request.getAmount());
        transaction.setDescription(request.getDescription());
        transaction.setStatus("SUCCESS");
        transaction.setTimestamp(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    public TransactionDetailDTO getTransactionDetail(Long transactionId) {
        // 1. Lay thong tin giao dich
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Giao dich khong ton tai"));

        // 2. Lay thong tin tai khoan nguon qua FeignClient
        AccountDTO accountDTO = accountServiceClient.getAccountByNumber(transaction.getFromAccountNumber());

        // 3. Lay thong tin khach hang qua FeignClient
        CustomerDTO customerDTO = null;
        if (accountDTO != null && accountDTO.getCustomerId() != null) {
            customerDTO = customerServiceClient.getCustomerById(accountDTO.getCustomerId());
        }

        // 4. Tong hop du lieu tra ve DTO
        TransactionDetailDTO detailDTO = new TransactionDetailDTO();
        detailDTO.setTransactionId(transaction.getId());
        detailDTO.setAmount(transaction.getAmount());
        detailDTO.setDescription(transaction.getDescription());
        detailDTO.setFromAccountNumber(transaction.getFromAccountNumber());
        detailDTO.setToAccountNumber(transaction.getToAccountNumber());
        detailDTO.setStatus(transaction.getStatus());
        if (customerDTO != null) {
            detailDTO.setCustomerName(customerDTO.getFullName());
            detailDTO.setCustomerEmail(customerDTO.getEmail());
        }

        return detailDTO;
    }
}