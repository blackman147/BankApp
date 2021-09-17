package com.maven.bank.entities;

import com.maven.bank.enums.LoanRequestStatus;
import com.maven.bank.enums.LoanType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LoanRequest {
    private BigDecimal loanAmount;
    private LoanType typeOfLoan;
    private LocalDateTime applyDate;
    private LocalDateTime startDate;
    private int tenor;
    private double interestRate;
    private LoanRequestStatus Status;

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public LoanType getTypeOfLoan() {
        return typeOfLoan;
    }

    public void setTypeOfLoan(LoanType typeOfLoan) {
        this.typeOfLoan = typeOfLoan;
    }

    public LocalDateTime getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(LocalDateTime applyDate) {
        this.applyDate = applyDate;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public int getTenor() {
        return tenor;
    }

    public void setTenor(int tenor) {
        this.tenor = tenor;
    }

    public double getInterestRate(double v) {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public LoanRequestStatus getStatus() {
        return Status;
    }

    public void setStatus(LoanRequestStatus status) {
        Status = status;
    }
}
