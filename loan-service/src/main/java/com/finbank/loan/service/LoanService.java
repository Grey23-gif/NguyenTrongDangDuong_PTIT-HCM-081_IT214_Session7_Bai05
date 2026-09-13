package com.finbank.loan.service;

import com.finbank.loan.client.AccountServiceClient;
import com.finbank.loan.client.CustomerServiceClient;
import com.finbank.loan.dto.AccountDTO;
import com.finbank.loan.dto.CustomerDTO;
import com.finbank.loan.dto.LoanApplyRequest;
import com.finbank.loan.dto.LoanResponse;
import com.finbank.loan.entity.Loan;
import com.finbank.loan.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class LoanService {

    @Autowired
    private CustomerServiceClient customerServiceClient;

    @Autowired
    private AccountServiceClient accountServiceClient;

    @Autowired
    private LoanRepository loanRepository;

    public LoanResponse applyLoan(LoanApplyRequest request) {
        CustomerDTO customer = customerServiceClient.getCustomerById(request.getCustomerId());
        if (customer == null) {
            throw new RuntimeException("Khach hang khong ton tai!");
        }

        List<AccountDTO> accounts = accountServiceClient.getAccountsByCustomerId(request.getCustomerId());
        AccountDTO activeAccount = accounts.stream()
                .filter(acc -> "ACTIVE".equalsIgnoreCase(acc.getStatus()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Khach hang khong co tai khoan ACTIVE!"));

        BigDecimal annualRate = new BigDecimal("0.08");
        BigDecimal totalInterest = request.getAmount().multiply(annualRate).multiply(new BigDecimal(request.getTermMonths())).divide(new BigDecimal(12), 2, RoundingMode.HALF_UP);
        BigDecimal totalPayment = request.getAmount().add(totalInterest);
        BigDecimal monthlyPayment = totalPayment.divide(new BigDecimal(request.getTermMonths()), 2, RoundingMode.HALF_UP);

        Loan loan = new Loan();
        loan.setCustomerId(request.getCustomerId());
        loan.setAmount(request.getAmount());
        loan.setTermMonths(request.getTermMonths());
        loan.setPurpose(request.getPurpose());
        loan.setInterestRate(annualRate);
        loan.setMonthlyPayment(monthlyPayment);
        loan.setDisbursementAccount(activeAccount.getAccountNumber());
        loan.setStatus("PENDING");

        Loan savedLoan = loanRepository.save(loan);

        LoanResponse response = new LoanResponse();
        response.setLoanId(savedLoan.getId());
        response.setCustomerId(customer.getId());
        response.setCustomerName(customer.getFullName());
        response.setCustomerPhone(customer.getPhone());
        response.setDisbursementAccount(activeAccount.getAccountNumber());
        response.setAmount(savedLoan.getAmount());
        response.setTermMonths(savedLoan.getTermMonths());
        response.setInterestRate(annualRate);
        response.setMonthlyPayment(monthlyPayment);
        response.setStatus(savedLoan.getStatus());

        return response;
    }
}