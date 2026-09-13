package com.finbank.account.controller;

import com.finbank.account.entity.Account;
import com.finbank.account.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/customer/{customerId}")
    public List<Account> getAccountsByCustomerId(@PathVariable("customerId") Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }
}