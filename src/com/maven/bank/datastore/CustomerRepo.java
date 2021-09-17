package com.maven.bank.datastore;

import com.maven.bank.entities.*;
import com.maven.bank.enums.BankTransactionType;
import com.maven.bank.enums.EmploymentLevel;
import com.maven.bank.services.BankService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashMap;
import java.util.Map;

public class CustomerRepo {
    private static Map<Long, Customer> customers = new HashMap<> ();

    public static  Map<Long, Customer> getCustomers() {
        return customers;
    }

    private void setCustomers(Map<Long, Customer> customers) {
        CustomerRepo.customers = customers;
    }



    static{
        reset ();
    }

    public static void reset()  {
        Customer john = new Customer ();
        john.setBvn (BankService.generateBvn ());
        john.getEmail ("john@doe.com");
        john.setFirstName ("john");
        john.setSurname ("doe");
        john.setPhone ("12345678901");
        LocalDate dob = LocalDate.of(1991, Month.MAY, 12);
        john.setDateOfBirth(dob);

        Company odogwuAssociates = new Company("od908787", "Odogwu Enterprises");
        Employment salesBoy = new Employment(odogwuAssociates, LocalDate.of(2010, Month.APRIL, 01),
                LocalDate.of(2014, Month.AUGUST, 31) ,BigDecimal.valueOf(360000));
        salesBoy.setLevel(EmploymentLevel.ENTRY);
        john.getEmploymentHistory().add(salesBoy);



        Company utiva = new Company("ut688989", "UTIVA");
        Employment customerSuccess = new Employment(utiva, LocalDate.of(2018, Month.JUNE, 12),
                LocalDate.of(2020, Month.DECEMBER, 31) ,BigDecimal.valueOf(1200000));
        salesBoy.setLevel(EmploymentLevel.INDIVIDUAL_CONTRIBUTOR);
        john.getEmploymentHistory().add(customerSuccess);

        Company kpmg = new Company("kp234567", "KPMG");
        Employment forensicAnalyst = new Employment(kpmg, LocalDate.of(2021,Month.SEPTEMBER, 20), BigDecimal.valueOf(12000000));
        salesBoy.setLevel(EmploymentLevel.ENTRY);
        john.getEmploymentHistory().add(forensicAnalyst);

        Account johnSavingsAccount = new SavingsAccount  (1000110001);
        john.setRelationshipStartDate (johnSavingsAccount.getStartDate ());
        BankTransaction initialDeposit = new BankTransaction (BankTransactionType.DEPOSIT, BigDecimal.valueOf (300000));
        johnSavingsAccount.getTransactions ().add (initialDeposit);

        BankTransaction mayAllowance = new BankTransaction (BankTransactionType.DEPOSIT, BigDecimal.valueOf (50000));
        mayAllowance.setDateTime (LocalDateTime.now().minusMonths (3));
        johnSavingsAccount.getTransactions ().add (mayAllowance);

        BankTransaction juneAllowance = new BankTransaction (BankTransactionType.DEPOSIT, BigDecimal.valueOf (50000));
        juneAllowance.setDateTime (LocalDateTime.now().minusMonths (2));
        johnSavingsAccount.getTransactions ().add (juneAllowance);

        BankTransaction julyAllowance = new BankTransaction (BankTransactionType.DEPOSIT, BigDecimal.valueOf (50000));
        julyAllowance.setDateTime (LocalDateTime.now().minusMonths (1));
        johnSavingsAccount.getTransactions ().add (julyAllowance);
        johnSavingsAccount.setBalance (BigDecimal.valueOf (450000));

        john.getAccounts ().add (johnSavingsAccount);
        Account johnCurrentAccount = new CurrentAccount ( 1000110002, new BigDecimal (50000000));
        john.getAccounts ().add (johnCurrentAccount);
        customers.put (john.getBvn (), john);

        Customer jane = new Customer ();
        jane.setBvn (BankService.generateBvn ());
        jane.getEmail ("jane@blackie.com");
        jane.setFirstName ("jane");
        jane.setSurname ("blackie");
        jane.setPhone ("90876543211");
         dob = LocalDate.of(2000, Month.JANUARY, 21);
        john.setDateOfBirth(dob);

        Account janeSavingsAccount = new SavingsAccount ( 1000110003 );
        jane.setRelationshipStartDate (janeSavingsAccount.getStartDate ());
        jane.getAccounts ().add (janeSavingsAccount);
        customers.put (jane.getBvn (), jane);
    }
}
