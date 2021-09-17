package com.maven.bank.engines;

import com.maven.bank.entities.Account;
import com.maven.bank.entities.Customer;
import com.maven.bank.exceptions.MavenBankLoanException;

import java.math.BigDecimal;

public class LoanEngineByBalance implements LoanEngine {
    @Override
    public BigDecimal calculateAmountAutoApproved(Customer customer, Account accountSeekingLoan) throws MavenBankLoanException {
        validateLoanRequest (customer, accountSeekingLoan);
        BigDecimal totalBalancePercentage = BigDecimal.valueOf (0.2);
        BigDecimal totalCustomerBalance = BigDecimal.ZERO;
        if (customer.getAccounts ().size () > BigDecimal.ONE.intValue ()){
            for(Account customerAccount : customer.getAccounts ( )){
                totalCustomerBalance = totalCustomerBalance.add (customerAccount.getBalance ());
            }
        }
        BigDecimal loanAmountApprovedAutomatically =BigDecimal.ZERO;
        if (totalCustomerBalance.intValue () > BigDecimal.ZERO.intValue ()){
           loanAmountApprovedAutomatically = totalCustomerBalance.multiply (totalBalancePercentage);
        }
        return loanAmountApprovedAutomatically;
    }

    @Override
    public BigDecimal getLoanPercentage(long determinant) {
        return null;
    }

    @Override
    public void validateLoanRequest(Customer customer, Account accountSeekingLoan) throws MavenBankLoanException {
        LoanEngine.super.validateLoanRequest(customer, accountSeekingLoan);
    }

    @Override
    public void validateLoanRequest(Account accountSeekingLoan) throws MavenBankLoanException {
        LoanEngine.super.validateLoanRequest(accountSeekingLoan);
    }

    @Override
    public BigDecimal getTotalCustomerBalance(Customer customer) {
        return LoanEngine.super.getTotalCustomerBalance(customer);
    }
}
