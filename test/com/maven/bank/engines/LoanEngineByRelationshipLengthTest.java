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
import com.maven.bank.services.AccountService;
import com.maven.bank.services.AccountServiceImpl;
import com.maven.bank.services.BankService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class LoanEngineByRelationshipLengthTest {

    private LoanRequest johnLoanRequest;
    private LoanEngine loanEngine;
    private AccountService accountService;
    private Customer john;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl ( );
        loanEngine = new LoanEngineByRelationshipLength ();

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
    void calculateAmountAutoApprovedFromThreeMonths(){
        try{
            Account johnSavingsAccount = accountService.findAccount (1000110001);
            assertEquals (BigDecimal.valueOf (450000), johnSavingsAccount.getBalance ());
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            assertEquals (BigDecimal.valueOf (50000000), johnCurrentAccount.getBalance ());
            johnCurrentAccount.setStartDate (johnCurrentAccount.getStartDate ().minusMonths (3));
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (1009000, amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }

    @Test
    void calculateAmountAutoApprovedForAlmostThreeMonths(){
        try{
            Account johnSavingsAccount = accountService.findAccount (1000110001);
            assertEquals (BigDecimal.valueOf (450000), johnSavingsAccount.getBalance ());
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            assertEquals (BigDecimal.valueOf (50000000), johnCurrentAccount.getBalance ());
            johnCurrentAccount.setStartDate (johnCurrentAccount.getStartDate ().minusDays (75));
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (BigDecimal.ZERO.intValue(), amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }


    @Test
    void calculateAmountAutoApprovedFromThreeMonthsButLessSixMonths(){
        try{
            Account johnSavingsAccount = accountService.findAccount (1000110001);
            assertEquals (BigDecimal.valueOf (450000), johnSavingsAccount.getBalance ());
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            assertEquals (BigDecimal.valueOf (50000000), johnCurrentAccount.getBalance ());
            johnCurrentAccount.setStartDate (johnCurrentAccount.getStartDate ().minusMonths (4));
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (1009000, amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }

    @Test
    void calculateAmountAutoApprovedFromSixMonthsButLessTwelveMonths(){
        try{
            Account johnSavingsAccount = accountService.findAccount (1000110001);
            assertEquals (BigDecimal.valueOf (450000), johnSavingsAccount.getBalance ());
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            assertEquals (BigDecimal.valueOf (50000000), johnCurrentAccount.getBalance ());
            johnCurrentAccount.setStartDate (johnCurrentAccount.getStartDate ().minusMonths (11));
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (2018000, amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }

    @Test
    void calculateAmountAutoApprovedFromTwelveMonthsButLessEighteenMonths(){
        try{
            Account johnSavingsAccount = accountService.findAccount (1000110001);
            assertEquals (BigDecimal.valueOf (450000), johnSavingsAccount.getBalance ());
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            assertEquals (BigDecimal.valueOf (50000000), johnCurrentAccount.getBalance ());
            johnCurrentAccount.setStartDate (johnCurrentAccount.getStartDate ().minusMonths (17));
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (3027000, amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }

    @Test
    void calculateAmountAutoApprovedFromEighteenMonthsButLessTwentyFourMonths(){
        try{
            Account johnSavingsAccount = accountService.findAccount (1000110001);
            assertEquals (BigDecimal.valueOf (450000), johnSavingsAccount.getBalance ());
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            assertEquals (BigDecimal.valueOf (50000000), johnCurrentAccount.getBalance ());
            johnCurrentAccount.setStartDate (johnCurrentAccount.getStartDate ().minusMonths (23));
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (4036000, amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }

    @Test
    void calculateAmountAutoApprovedFromTwentyFourMonthsAndAbove(){
        try{
            Account johnSavingsAccount = accountService.findAccount (1000110001);
            assertEquals (BigDecimal.valueOf (450000), johnSavingsAccount.getBalance ());
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            assertEquals (BigDecimal.valueOf (50000000), johnCurrentAccount.getBalance ());
            johnCurrentAccount.setStartDate (johnCurrentAccount.getStartDate ().minusMonths (320));
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount);
            assertEquals (5045000, amountApproved.intValue ());
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }


    @Test
    void calculateAmountAutoApprovedWithNegativeMonths(){
        try{
            Account johnSavingsAccount = accountService.findAccount (1000110001);
            assertEquals (BigDecimal.valueOf (450000), johnSavingsAccount.getBalance ());
            Account johnCurrentAccount = accountService.findAccount (1000110002);
            assertEquals (BigDecimal.valueOf (50000000), johnCurrentAccount.getBalance ());
            johnCurrentAccount.setStartDate (johnCurrentAccount.getStartDate ().minusMonths (-320));
            johnCurrentAccount.setAccountLoanRequest (johnLoanRequest);
            assertThrows (MavenBankLoanException.class, () -> loanEngine.calculateAmountAutoApproved (john, johnCurrentAccount));
        } catch (MavenBankException ex) {
            ex.printStackTrace ( );
        }
    }

}