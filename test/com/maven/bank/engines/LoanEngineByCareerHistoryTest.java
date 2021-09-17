package com.maven.bank.engines;

import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.entities.*;
import com.maven.bank.enums.EmploymentLevel;
import com.maven.bank.enums.LoanRequestStatus;
import com.maven.bank.enums.LoanType;
import com.maven.bank.exceptions.MavenBankException;
import com.maven.bank.exceptions.MavenBankLoanException;
import com.maven.bank.services.AccountService;
import com.maven.bank.services.AccountServiceImpl;
import com.maven.bank.services.BankService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

class LoanEngineByCareerHistoryTest {

    private LoanRequest johnLoanRequest;
    private LoanEngine loanEngine;
    private AccountService accountService;
    private Customer john;

    @BeforeEach
    void setUp() {
        accountService = new AccountServiceImpl();
        loanEngine = new LoanEngineByCareerHistory();

        johnLoanRequest = new LoanRequest();
        johnLoanRequest.setApplyDate(LocalDateTime.now());
        johnLoanRequest.getInterestRate(0.1);
        johnLoanRequest.setStatus(LoanRequestStatus.NEW);
        johnLoanRequest.setTenor(25);
        johnLoanRequest.setTypeOfLoan(LoanType.SME);
        Optional<Customer> optionalCustomer = CustomerRepo.getCustomers().values().stream().findFirst();
        john = optionalCustomer.orElse(null);
        assertNotNull(john);
    }

    @AfterEach
    void tearDown() {
        BankService.reset();
        CustomerRepo.reset();
    }


    @Test
    void approveLoanRequestWithoutCustomer() {

        assertThrows(MavenBankLoanException.class,
                () -> loanEngine.calculateAmountAutoApproved(null, new SavingsAccount()));
    }

    @Test
    void approveLoanRequestWithNullAccount() {
        assertThrows(MavenBankLoanException.class, () -> loanEngine.calculateAmountAutoApproved(john, null));
    }

    @Test
    void approveLoanRequestForVeryEarlCareer() {
        try {
            Account johnCurrentAccount = accountService.findAccount(1000110002);
            johnCurrentAccount.setAccountLoanRequest(johnLoanRequest);
            SortedSet<Employment> jobs = new TreeSet<>();
            Company odogwuAssociates = new Company("od908787", "Odogwu Enterprises");
            LocalDate start = LocalDate.now();
            start = start.minusMonths(6);
            Employment salesBoy = new Employment(odogwuAssociates, start, BigDecimal.valueOf(360000));
            salesBoy.setLevel(EmploymentLevel.ENTRY);
            jobs.add(salesBoy);
            john.setEmploymentHistory(jobs);

            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved(john, johnCurrentAccount);
            assertEquals(BigDecimal.ZERO.intValue(), amountApproved.intValue());
        } catch (MavenBankException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void approveLoanRequestForEarlCareer() {
        try {
            Account johnCurrentAccount = accountService.findAccount(1000110002);
            johnCurrentAccount.setAccountLoanRequest(johnLoanRequest);
            SortedSet<Employment> jobs = new TreeSet<>();
            Company odogwuAssociates = new Company("od908787", "Odogwu Enterprises");
            LocalDate start = LocalDate.now();
            start = start.minusMonths(20);
            Employment salesBoy = new Employment(odogwuAssociates, start, BigDecimal.valueOf(360000));
            salesBoy.setLevel(EmploymentLevel.ENTRY);
            jobs.add(salesBoy);
            john.setEmploymentHistory(jobs);

            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved(john, johnCurrentAccount);
            assertEquals(1800, amountApproved.intValue());
        } catch (MavenBankException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void approveLoanRequestForMidCareer() {
        try {
            Account johnCurrentAccount = accountService.findAccount(1000110002);
            johnCurrentAccount.setAccountLoanRequest(johnLoanRequest);
            SortedSet<Employment> jobs = new TreeSet<>();
            Company odogwuAssociates = new Company("od908787", "Odogwu Enterprises");
            LocalDate start = LocalDate.of(2016, Month.APRIL, 01);
            LocalDate end = LocalDate.of(2018, Month.MAY, 31);
            Employment salesBoy = new Employment(odogwuAssociates, start, end, BigDecimal.valueOf(360000));
            salesBoy.setLevel(EmploymentLevel.ENTRY);
            jobs.add(salesBoy);

            Company utiva = new Company("ut688989", "UTIVA");
            Employment customerSuccess = new Employment(utiva, LocalDate.of(2018, Month.JUNE, 12),
                    BigDecimal.valueOf(1200000));
            customerSuccess.setLevel(EmploymentLevel.INDIVIDUAL_CONTRIBUTOR);
            jobs.add(customerSuccess);
            john.setEmploymentHistory(jobs);

            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved(john, johnCurrentAccount);
            assertEquals(12000, amountApproved.intValue());
        } catch (MavenBankException ex) {
            ex.printStackTrace();
        }
    }

    @Test
    void approveLoanRequestForMidSeniorCareer() {
        try {
            Account johnCurrentAccount = accountService.findAccount(1000110002);
            johnCurrentAccount.setAccountLoanRequest(johnLoanRequest);

            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved(john, johnCurrentAccount);
            assertEquals(180000, amountApproved.intValue());
        } catch (MavenBankException ex) {
            ex.printStackTrace();
        }
    }


    @Test
    void approveLoanRequestForSeniorCareer() {
        try {
            Account johnCurrentAccount = accountService.findAccount(1000110002);
            johnCurrentAccount.setAccountLoanRequest(johnLoanRequest);
            Employment firstJob = john.getEmploymentHistory().first();
            LocalDate newStart = firstJob.getStartDate().withYear(2000);
            firstJob.setStartDate(newStart);
            john.getEmploymentHistory().add(firstJob);
            BigDecimal amountApproved = loanEngine.calculateAmountAutoApproved(john, johnCurrentAccount);
            assertEquals(240000, amountApproved.intValue());
        } catch (MavenBankException ex) {
            ex.printStackTrace();
        }


    }
}