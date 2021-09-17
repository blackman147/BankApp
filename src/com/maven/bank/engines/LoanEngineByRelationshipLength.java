package com.maven.bank.engines;

import com.maven.bank.entities.Account;
import com.maven.bank.entities.Customer;
import com.maven.bank.exceptions.MavenBankLoanException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class LoanEngineByRelationshipLength implements LoanEngine{

    @Override
    public BigDecimal calculateAmountAutoApproved(Customer customer, Account accountSeekingLoan) throws MavenBankLoanException {
        validateLoanRequest (accountSeekingLoan);
        LocalDateTime openingDate =accountSeekingLoan.getStartDate ();
        LocalDateTime currentDate = LocalDateTime.now ();
        long period = ChronoUnit.MONTHS.between (openingDate.toLocalDate (), currentDate.toLocalDate ());
        if (period < 0) {
            throw new MavenBankLoanException ( "Loan cannot be approved this period" );
        }
        BigDecimal loanAmountApprovedAutomatically;
        BigDecimal totalCustomerBalance = getTotalCustomerBalance(customer);
        BigDecimal totalBalancePercentage;
        long lengthOfRelationship = Long.valueOf(period).longValue();
        totalBalancePercentage = getRelationshipPercentage(lengthOfRelationship);
        loanAmountApprovedAutomatically = totalCustomerBalance.multiply(totalBalancePercentage);

        return loanAmountApprovedAutomatically;
//        for(Account customerAccount : customer.getAccounts ( )) {
//            totalCustomerBalance = totalCustomerBalance.add (customerAccount.getBalance ( ));
//        }
//        if (period > 23){
//                return totalCustomerBalance.multiply (BigDecimal.valueOf (0.1));
//        }else if(period >= 18){
//            return totalCustomerBalance.multiply (BigDecimal.valueOf (0.08));
//        }else if(period >= 12){
//            return totalCustomerBalance.multiply (BigDecimal.valueOf (0.06));
//        }else if(period >= 6){
//            return totalCustomerBalance.multiply (BigDecimal.valueOf (0.04));
//        }else if(period >= 3){
//            return totalCustomerBalance.multiply (BigDecimal.valueOf (0.02));
//        }else{
//         return  BigDecimal.valueOf (0);
//        }
    }

    private BigDecimal getRelationshipPercentage(long lengthOfRelationship){
        int relationshipLength = Long.valueOf(lengthOfRelationship).intValue();
        BigDecimal totalBalancePercentage;

        switch (relationshipLength){
            case 1: case 2:
                return BigDecimal.ZERO;
            case 3: case 4: case 5:
                totalBalancePercentage = BigDecimal.valueOf(0.02);
                break;
            case 6: case 7: case 8: case 9: case 10: case 11:
                totalBalancePercentage = BigDecimal.valueOf(0.04);
                break;
            case 12: case 13: case 14: case 15: case 16: case 17:
                totalBalancePercentage = BigDecimal.valueOf(0.06);
                break;
            case 18: case 19: case 20: case 21: case 22: case 23:
                totalBalancePercentage = BigDecimal.valueOf(0.08);
                break;
            default:
                totalBalancePercentage = BigDecimal.valueOf(0.10);

        }
        return totalBalancePercentage;
    }


}
