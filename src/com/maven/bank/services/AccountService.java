package com.maven.bank.services;

import com.maven.bank.datastore.LoanRequestStatus;
import com.maven.bank.entities.Account;
import com.maven.bank.entities.BankTransaction;
import com.maven.bank.entities.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.exceptions.MavenBankException;
import com.maven.bank.exceptions.MavenBankInsufficientFundsException;
import com.maven.bank.exceptions.MavenBankTransactionException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface AccountService {
    public long openAccount(Customer theCustomer, AccountType type) throws MavenBankException;

    public long openSavingsAccount(Customer theCustomer) throws MavenBankException;

    public long openCurrentAccount(Customer theCustomer) throws  MavenBankException;

    public BigDecimal deposit(BigDecimal amount, long accountNumber) throws MavenBankException;

    public Account findAccount(long accountNumber) throws MavenBankException;

    public Account findAccount(Customer customer, long accountNumber) throws MavenBankException;

    BigDecimal withdraw(BigDecimal amount, long accountNumber, String pin) throws MavenBankInsufficientFundsException, MavenBankException;

    public void applyForOverdraft(Account theAccount);

    public LoanRequestStatus applyForLoans(Account theAccount);

    public LocalDateTime openingYear(Account theAccount, LocalDate year);

    public void addBankTransaction(BankTransaction transaction, Account account) throws MavenBankTransactionException;


}