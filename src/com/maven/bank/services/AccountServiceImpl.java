package com.maven.bank.services;

import com.maven.bank.enums.LoanRequestStatus;
import com.maven.bank.entities.*;
import com.maven.bank.enums.AccountType;
import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.enums.BankTransactionType;
import com.maven.bank.exceptions.MavenBankException;
import com.maven.bank.exceptions.MavenBankInsufficientFundsException;
import com.maven.bank.exceptions.MavenBankTransactionException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class AccountServiceImpl implements AccountService{
    @Override
    public long openAccount(Customer theCustomer, AccountType type) throws MavenBankException {
        long accountNumber = BigDecimal.ZERO.longValue ();
        if (type == AccountType.SAVINGSACCOUNT){
            accountNumber = openSavingsAccount (theCustomer);
        }else if (type == AccountType.CURRENTACCOUNT){
           accountNumber = openCurrentAccount (theCustomer);
        }
        return accountNumber;
    }

    @Override
    public long openSavingsAccount(Customer theCustomer) throws MavenBankException {
        if (theCustomer == null ){
            throw new MavenBankException ( "customer and account type required to open new account" );
        }
        SavingsAccount newAccount = new SavingsAccount (  );
        if (accountTypeExists (theCustomer, newAccount.getClass ().getTypeName ())){
            throw new MavenBankException ( "Customer already has the requested account type " );
        }

        newAccount.setAccountNumber (BankService.generateAccountNumber ());
        theCustomer.getAccounts ().add (newAccount);

        CustomerRepo.getCustomers ().put (theCustomer.getBvn ( ), theCustomer);
        return  newAccount.getAccountNumber ();
    }


    @Override
    public long openCurrentAccount(Customer theCustomer) throws MavenBankException {
        if (theCustomer == null ){
            throw new MavenBankException ( "customer and account type required to open new account" );
        }
        CurrentAccount newAccount = new CurrentAccount (  );
        if (accountTypeExists (theCustomer, newAccount.getClass ().getTypeName ())){
            throw new MavenBankException ( "Customer already has the requested account type " );
        }


        newAccount.setAccountNumber (BankService.generateAccountNumber ());
        theCustomer.getAccounts ().add (newAccount);

        CustomerRepo.getCustomers ().put (theCustomer.getBvn ( ), theCustomer);
        return  newAccount.getAccountNumber ();
    }



    @Override
    public BigDecimal deposit(BigDecimal amount, long accountNumber) throws MavenBankException {
        Account account = findAccount (accountNumber);
        BankTransactionType typeOfTransaction = BankTransactionType.DEPOSIT;

        validateTransaction (amount, account);

        BigDecimal newBalance = BigDecimal.ZERO;
        newBalance = account.getBalance ().add (amount);
        account.setBalance (newBalance);
        return newBalance;
    }

    @Override
    public Account findAccount(long accountNumber) throws MavenBankException {
        Account foundAccount = null;
        boolean accountFound = false;

        for(Customer customer : CustomerRepo.getCustomers ().values ()){
            for (Account anAccount : customer.getAccounts ()) {
                if (anAccount.getAccountNumber () == accountNumber){
                    foundAccount = anAccount;
                    accountFound = true;
                    break;
                }
            }
            if (accountFound){
                break;
            }
        }
        return foundAccount;
    }

    @Override
    public Account findAccount(Customer customer, long accountNumber) throws MavenBankException {
        return null;
    }

    @Override
    public BigDecimal withdraw(BigDecimal amount, long accountNumber) throws MavenBankException {
        BankTransactionType typeOfTransaction = BankTransactionType.WITHDRAWAL;
        Account account = findAccount (accountNumber);
        validateTransaction (amount, account );
        try{
            checkForSufficientBalance (amount, account, BankTransactionType.WITHDRAWAL);
        } catch (MavenBankInsufficientFundsException insufficientFundsException) {
            this.applyForOverdraft (account);
            throw insufficientFundsException;

        }
        BigDecimal newBalance = debitAccount (amount, accountNumber);

        return newBalance;
    }

    @Override
    public void applyForOverdraft(Account theAccount) {
        //TODO
    }

    @Override
    public LoanRequestStatus applyForLoans(Account theAccount) {
    return null;
    }

    @Override
    public LocalDateTime openingYear(Account theAccount, LocalDate year) {
        CurrentAccount accountApplyingForLoan = new CurrentAccount (  );

        return null;
    }

    @Override
    public void addBankTransaction(BankTransaction transaction, Account account) throws MavenBankException {
        if (transaction == null || account == null){
            throw  new MavenBankTransactionException ( "Transaction and account are required to add transaction" );
        }

        if (transaction.getType () ==  BankTransactionType.DEPOSIT){
            deposit (transaction.getAmount (), account.getAccountNumber ( ));

        }else if (transaction.getType () == BankTransactionType.WITHDRAWAL){
            withdraw (transaction.getAmount (), account.getAccountNumber ());
        }
        account.getTransactions ().add (transaction);
    }

    public BigDecimal debitAccount(BigDecimal amount, long accountNumber) throws MavenBankException {

        Account theAccount = findAccount (accountNumber);
       BigDecimal newBalance =  theAccount.getBalance ().subtract (amount);
        theAccount.setBalance (newBalance);
        return newBalance;

    }

    private void validateTransaction(BigDecimal amount, Account account) throws MavenBankException {

                if (amount.compareTo (BigDecimal.ZERO) < 0){
                    throw new MavenBankTransactionException ( "Transaction amount cannot be negative" );
                }

                if (account == null){
                    throw new MavenBankTransactionException ( "Transaction account not found" );
                }

    }

    private void checkForSufficientBalance(BigDecimal amount, Account account, BankTransactionType withdrawal) throws MavenBankException {
                if (amount.compareTo (account.getBalance ()) > BigDecimal.ZERO.intValue ( )){
                    throw new MavenBankInsufficientFundsException ( "Insufficient Funds" );
                }
    }


    private boolean accountTypeExists(Customer aCustomer , String name){
        boolean accountTypeExists = false;
        for(Account customerAccount : aCustomer.getAccounts ()){
            if (customerAccount.getClass().getTypeName () == name){
                accountTypeExists = true;
                break;
            }
        }
        return accountTypeExists;
    }




}
