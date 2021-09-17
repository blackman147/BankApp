package com.maven.bank.engines;

import com.maven.bank.entities.Account;
import com.maven.bank.entities.Customer;
import com.maven.bank.exceptions.MavenBankLoanException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class LoanEngineByCustomerAge implements LoanEngine{
    @Override
    public BigDecimal calculateAmountAutoApproved(Customer customer, Account accountSeekingLoan) throws MavenBankLoanException {
        BigDecimal loanAmountApprovedAutomatically = BigDecimal.ZERO;
        validateLoanRequest(customer, accountSeekingLoan);
        LocalDate today = LocalDate.now();
        long age = ChronoUnit.YEARS.between(customer.getDateOfBirth(), today);
        BigDecimal agePercentage  = getAgePercentage(age);
        BigDecimal totalCustomerBalance = getTotalCustomerBalance(customer);
        loanAmountApprovedAutomatically = totalCustomerBalance.multiply(agePercentage);
        return loanAmountApprovedAutomatically;
    }

    private BigDecimal getAgePercentage(long age){
        BigDecimal agePercentage  = BigDecimal.ZERO;


        if ((age > 17 && age < 25) || (age > 65 && age < 75)){
            agePercentage = BigDecimal.valueOf(0.02);
        }else if(age > 24 && age < 31){
            agePercentage = BigDecimal.valueOf(0.04);
        }else if (age > 30 && age < 41){
            agePercentage = BigDecimal.valueOf(0.06);
        }else if (age > 40 && age < 56){
            agePercentage = BigDecimal.valueOf(0.10);
        }else  if (age > 55 && age < 66){
            agePercentage = BigDecimal.valueOf(0.05);
        }
        if (age<=17 || age >=75){
            agePercentage = BigDecimal.ZERO;
        }
        return agePercentage;
    }

}
