package com.maven.bank.services;

import com.maven.bank.enums.LoanRequestStatus;
import com.maven.bank.entities.Account;
import com.maven.bank.entities.Customer;
import com.maven.bank.entities.LoanRequest;
import com.maven.bank.exceptions.MavenBankLoanException;

import java.math.BigDecimal;

public class LoanServiceImpl implements LoanService{
    @Override
    public LoanRequest approveLoanRequest(Account accountSeekingLoan) throws MavenBankLoanException {
        validateLoanRequest (accountSeekingLoan);
        LoanRequest theLoanRequest = accountSeekingLoan.getAccountLoanRequest ();
        theLoanRequest.setStatus (decideOnLoanRequest (accountSeekingLoan));

        return theLoanRequest;
    }

    @Override
    public LoanRequest approveLoanRequest(Account accountSeekingLoan, Customer customer) throws MavenBankLoanException {
        this.validateLoanRequest (customer, accountSeekingLoan);
        LoanRequestStatus decision =  decideOnLoanRequestWithTotalCustomerBalance (customer, accountSeekingLoan);
        LoanRequest theLoanRequest = accountSeekingLoan.getAccountLoanRequest ();
        theLoanRequest.setStatus (decision);

        if(decision != LoanRequestStatus.APPROVED){
           theLoanRequest = approveLoanRequest (accountSeekingLoan);
        }
        return theLoanRequest;
    }

    public LoanRequestStatus decideOnLoanRequest(Account accountSeekingLoan) throws MavenBankLoanException {
        LoanRequestStatus decision = decideOnLoanRequestWithAccountBalance (accountSeekingLoan);

        return decision;
    }

    public LoanRequestStatus decideOnLoanRequestWithAccountBalance(Account accountSeekingLoan) throws MavenBankLoanException {
        LoanRequestStatus decision = LoanRequestStatus.PENDING;
        LoanRequest theLoanRequest = accountSeekingLoan.getAccountLoanRequest ();
        BigDecimal bankAccountBalancePercentage = BigDecimal.valueOf (0.2);

        BigDecimal loanAmountApprovedAutomatically = accountSeekingLoan.getBalance ().multiply (bankAccountBalancePercentage);
        if (theLoanRequest.getLoanAmount().compareTo(loanAmountApprovedAutomatically) < BigDecimal.ZERO.intValue ()){
            decision = LoanRequestStatus.APPROVED;
        }

        return decision;
    }


    public LoanRequestStatus decideOnLoanRequestWithTotalCustomerBalance(Customer customer, Account accountSeekingLoan) throws MavenBankLoanException {
        LoanRequestStatus decision = LoanRequestStatus.PENDING;
        BigDecimal relationshipVolumePercentage = BigDecimal.valueOf (0.2);

        BigDecimal totalCustomerBalance = BigDecimal.ZERO;
        if (customer.getAccounts ().size () > BigDecimal.ONE.intValue ()){
            for(Account customerAccount : customer.getAccounts ( )){
                totalCustomerBalance = totalCustomerBalance.add (customerAccount.getBalance ());
            }
        }
        BigDecimal loanAmountApprovedAutomatically = totalCustomerBalance.multiply (relationshipVolumePercentage);
        if (accountSeekingLoan.getAccountLoanRequest ().getLoanAmount ().compareTo (loanAmountApprovedAutomatically) < BigDecimal.ZERO.intValue ()){
            decision = LoanRequestStatus.APPROVED;
        }

        return decision;
    }


    private void validateLoanRequest( Account accountSeekingLoan) throws MavenBankLoanException{
        if (accountSeekingLoan == null){
            throw new MavenBankLoanException ( "An account is required to process loan request" );
        }
        if (accountSeekingLoan.getAccountLoanRequest () == null){
            throw new MavenBankLoanException ( "No loan request provided for processing" );
        }
    }

    private void validateLoanRequest(Customer customer, Account accountSeekingLoan) throws MavenBankLoanException{
        if (customer == null){
            throw new MavenBankLoanException ( "An account is required to process loan request" );
        }
       this.validateLoanRequest (accountSeekingLoan);
    }
}
