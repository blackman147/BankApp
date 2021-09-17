package com.maven.bank.engines;

import com.maven.bank.entities.Account;
import com.maven.bank.entities.Customer;
import com.maven.bank.entities.Employment;
import com.maven.bank.exceptions.MavenBankLoanException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class LoanEngineByCareerHistory implements LoanEngine{
    @Override
    public BigDecimal calculateAmountAutoApproved(Customer customer, Account accountSeekingLoan) throws MavenBankLoanException {
        validateLoanRequest(customer, accountSeekingLoan);
        BigDecimal loanAmountApprovedAutomatically = BigDecimal.valueOf(-1);
        Employment firstJob = customer.getEmploymentHistory().first();
        LocalDateTime today = LocalDateTime.now();
        long period = ChronoUnit.YEARS.between(firstJob.getStartDate(), today);

        Employment currentJob = customer.getEmploymentHistory().last();
        BigDecimal loanPercentage = BigDecimal.ZERO;

        if (period >= 1 && period < 4){
            loanPercentage = BigDecimal.valueOf(0.005);
        }else if (period >= 4 && period < 11){
             loanPercentage = BigDecimal.valueOf(0.010);
        }else if (period >= 10 && period < 16) {
             loanPercentage = BigDecimal.valueOf(0.015);
        }else if (period > 15){
            loanPercentage = BigDecimal.valueOf(0.02);
        }
        loanAmountApprovedAutomatically = currentJob.getAnnualSalary().multiply(loanPercentage);
        return loanAmountApprovedAutomatically;
    }

    @Override
    public BigDecimal getLoanPercentage(long determinant) {
        return null;
    }

//    public long calculateLengthOfCareer(Customer customer){
//        int length = 0;
//
//        List<Employment> history = customer.getEmploymentHistory();
//
//        LocalDate start = history.get(0).getStartDate();
//        LocalDate end = LocalDate.now();
//        Employment latestEmployment = history.get(history.size() - 1);
//        if(latestEmployment.getEndDate() != null){
//            end = latestEmployment.getEndDate();
//        }
//        long careerLength;
//    }
}
