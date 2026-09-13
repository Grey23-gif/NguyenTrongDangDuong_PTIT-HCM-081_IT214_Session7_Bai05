package com.finbank.loan.controller;

import com.finbank.loan.dto.LoanApplyRequest;
import com.finbank.loan.dto.LoanResponse;
import com.finbank.loan.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/apply")
    public ResponseEntity<LoanResponse> applyLoan(@RequestBody LoanApplyRequest request) {
        LoanResponse response = loanService.applyLoan(request);
        return ResponseEntity.ok(response);
    }
}