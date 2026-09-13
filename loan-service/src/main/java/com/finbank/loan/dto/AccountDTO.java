package com.finbank.loan.dto;

public class AccountDTO {
    private Long id;
    private Long customerId;
    private String accountNumber;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}