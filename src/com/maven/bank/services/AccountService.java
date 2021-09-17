package com.maven.bank.services;

import com.maven.bank.enums.LoanRequestStatus;
import com.maven.bank.entities.Account;
import com.maven.bank.entities.BankTransaction;
import com.maven.bank.entities.Customer;
import com.maven.bank.enums.AccountType;
import com.maven.bank.exceptions.MavenBankException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AccountService {
    long openAccount(Customer theCustomer, AccountType type) throws MavenBankException;

    long openSavingsAccount(Customer theCustomer) throws MavenBankException;

    long openCurrentAccount(Customer theCustomer) throws  MavenBankException;

    BigDecimal deposit(BigDecimal amount, long accountNumber) throws MavenBankException;

    Account findAccount(long accountNumber) throws MavenBankException;

    Account findAccount(Customer customer, long accountNumber) throws MavenBankException;

    BigDecimal withdraw(BigDecimal amount, long accountNumber) throws MavenBankException;

    void applyForOverdraft(Account theAccount);

    LoanRequestStatus applyForLoans(Account theAccount);

    LocalDateTime openingYear(Account theAccount, LocalDate year);

    void addBankTransaction(BankTransaction transaction, Account account) throws MavenBankException;


}