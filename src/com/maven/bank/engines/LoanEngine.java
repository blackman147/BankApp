package com.maven.bank.engines;

import com.maven.bank.entities.Account;
import com.maven.bank.entities.Customer;
import com.maven.bank.exceptions.MavenBankLoanException;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface LoanEngine {
    BigDecimal calculateAmountAutoApproved(Customer customer, Account accountSeekingLoan) throws MavenBankLoanException;
    public BigDecimal getLoanPercentage(long determinant);

    default void validateLoanRequest(Customer customer, Account accountSeekingLoan) throws MavenBankLoanException {
        if (customer == null){
            throw new MavenBankLoanException ( "An account is required to process loan request" );
        }
        LocalDateTime today = LocalDateTime.now();

        if (today.isBefore(customer.getRelationshipStartDate())){
            throw new MavenBankLoanException("Customer relationship start date is after today");
        }
        validateLoanRequest (accountSeekingLoan);
    }

   default void validateLoanRequest( Account accountSeekingLoan) throws MavenBankLoanException{
        if (accountSeekingLoan == null){
            throw new MavenBankLoanException ( "An account is required to process loan request" );
        }
        if (accountSeekingLoan.getAccountLoanRequest () == null){
            throw new MavenBankLoanException ( "No loan request provided for processing" );
        }
    }

    default BigDecimal getTotalCustomerBalance(Customer customer){
        BigDecimal totalCustomerBalance = BigDecimal.ZERO;
        for(Account customerAccount : customer.getAccounts ( )) {
            totalCustomerBalance = totalCustomerBalance.add (customerAccount.getBalance ( ));
        }
        return totalCustomerBalance;
    }



}
