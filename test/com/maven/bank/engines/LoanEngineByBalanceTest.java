package com.maven.bank.engines;

import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.enums.LoanRequestStatus;
import com.maven.bank.enums.LoanType;
import com.maven.bank.entities.Account;
import com.maven.bank.entities.Customer;
import com.maven.bank.entities.LoanRequest;
import com.maven.bank.entities.SavingsAccount;
import com.maven.bank.exceptions.MavenBankException;
import com.maven.bank.exceptions.MavenBankLoanException;
import com.maven.bank.services.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LoanEngineByBalanceTest {
    private LoanRequest johnLoanRequest;
    LoanEngine loanEngine;
    private AccountService accountService;
    private Customer john;

    @BeforeEach
    void setUp() throws MavenBankException {
        accountService = new AccountServiceImpl ( );
        loanEngine = new LoanEngineByBalance ();

        johnLoanRequest = new LoanRequest ();
        johnLoanRequest.setApplyDate (LocalDateTime.now());
        johnLoanRequest.getInterestRate (0.1);
        johnLoanRequest.setStatus (LoanRequestStatus.NEW);
        johnLoanRequest.setTenor (25);
        johnLoanRequest.setTypeOfLoan (LoanType.SME);
        Optional<Customer> optionalCustomer = CustomerRepo.getCustomers ().values ().stream ().findFirst ();
        john = optionalCustomer.orElse(null);
        assertNotNull (john);
    }

    @AfterEach
    void tearDown() {
        BankService.reset ();
        CustomerRepo.reset ();
    }

    @Test
    void approveLoanRequestWithoutCustomer(){

        assertThrows (MavenBankLoanException.class,
                () -> loanEngine.calculateAmountAutoApproved (null, new SavingsAccount (  ))) ;
    }

    @Test
    void approveLoanRequestWithNullAccount(){
        assertThrows (MavenBankLoanException.class, () -> loanEngine.calculateAmountAutoApproved (john, null));
    }

    @Test
    void calculateAmountAutoApproved(){
        try{
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            johnLoanRequest.setLoanAmount (BigDecimal.valueOf (9000000));
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (10090000.0, amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }

    @Test
    void calculateAmountAutoApprovedForCustomerWithNegativeBalance(){
        try{
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            johnCurrentAccount.setBalance (BigDecimal.valueOf (-900000));

            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (0, amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }

}