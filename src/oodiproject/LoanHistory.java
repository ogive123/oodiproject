package oodiproject;

import java.util.ArrayList;
import java.util.List;

public class LoanHistory {
    private int patronID;
    private List<Loan> loans;
    
    public LoanHistory (int ID){
        this.patronID = ID;
        this.loans = new ArrayList<>();
    }

    //patronID
    public int getPatronID() {
        return patronID;
    }

    public void setPatronID(int patronID) {
        this.patronID = patronID;
    }
    
    //loans
    public List<Loan> getLoans() {
        return loans;
    }

    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }
    
    
    public void addLoanRecord(Loan loan){
        this.loans.add(loan);
    }
    
    public double calculateUnpaidFines(){
        double fine = 0;
        
        for (Loan selectedLoan : this.loans){
            fine = fine + selectedLoan.calculateFine();
        }
        return fine;
    }
    
}
