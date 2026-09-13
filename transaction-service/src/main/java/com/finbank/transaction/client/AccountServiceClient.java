package com.finbank.transaction.client;

import com.finbank.transaction.dto.AccountDTO;
import com.finbank.transaction.dto.TransferRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "account-service", url = "${application.config.account-service-url}")
public interface AccountServiceClient {

    @GetMapping("/api/accounts/{accountNumber}")
    AccountDTO getAccountByNumber(@PathVariable("accountNumber") String accountNumber);

    @PostMapping("/api/accounts/update-balance")
    void updateBalance(@RequestBody TransferRequestDTO transferRequest);
}