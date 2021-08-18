package com.maven.bank.services;

import com.maven.bank.Account;
import com.maven.bank.Customer;
import com.maven.bank.datastore.AccountType;
import com.maven.bank.datastore.CustomerRepo;
import com.maven.bank.exceptions.MavenBankException;
import com.maven.bank.exceptions.MavenBankTransactionException;

import java.math.BigDecimal;

public class AccountServiceImpl implements AccountService{
    @Override
    public long openAccount(Customer theCustomer, AccountType type) throws MavenBankException {
        if (theCustomer == null || type == null){
            throw new MavenBankException ( "customer and account type required to open new account" );
        }
        if (accountTypeExists (theCustomer, type )){
            throw new MavenBankException ( "Customer already has the requested account type " );
        }

        Account newAccount = new Account ();
        newAccount.setAccountNumber (BankService.generateAccountNumber ());
        newAccount.setTypeOfAccount (type);
        theCustomer.getAccounts ().add (newAccount);
        CustomerRepo.getCustomers ().put (theCustomer.getBvn ( ), theCustomer);
        return  newAccount.getAccountNumber ();
    }

    @Override
    public BigDecimal deposit(BigDecimal amount, long accountNumber) throws MavenBankException {
        if (amount.compareTo (BigDecimal.ZERO) < 0){
            throw new MavenBankTransactionException ( "Deposit amount cannot be Negative!!" );
        }
        BigDecimal newBalance = BigDecimal.ZERO;
        Account depositAccount = findAccount (accountNumber);
        newBalance = depositAccount.getBalance ().add (amount);
        depositAccount.setBalance (newBalance);
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
    public BigDecimal withdraw(BigDecimal amount, long accountNumber, String pin) throws MavenBankException {
        if (amount.compareTo (BigDecimal.ZERO) < BigDecimal.ONE.intValue ()){
            throw new MavenBankTransactionException ( "Withdrawal amount cannot be Negative!!" );
        }
        BigDecimal newBalance = BigDecimal.ZERO;
        Account withdrawalAccount = findAccount (accountNumber);
        newBalance =  withdrawalAccount.getBalance ().subtract (amount);
        withdrawalAccount.setBalance (newBalance);
        return newBalance;

    }

    private boolean accountTypeExists(Customer aCustomer, AccountType type){
        boolean accountTypeExists = false;
        for(Account customerAccount : aCustomer.getAccounts ()){
            if (customerAccount.getTypeOfAccount () == type){
                accountTypeExists = true;
                break;
            }
        }
        return accountTypeExists;
    }


}
