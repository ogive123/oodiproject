package oodiproject;

public class Patron extends User implements canManageLoans {
    
    private double currentFees;
    private LoanHistory borrowingHistory;
    
    public Patron(){
        super("","",0,false);
    }
    
    public Patron(String username, String password, int userID, 
            boolean loginStatus, double currentFees) {
        super(username, password, userID, loginStatus);
        this.currentFees = currentFees;
        this.borrowingHistory = new LoanHistory(this.getUserID());
    }

    public double getCurrentFees() {
        return currentFees;
    }

    public void setCurrentFees(double currentFees) {
        this.currentFees = currentFees;
    }

    public LoanHistory getBorrowingHistory() {
        return borrowingHistory;
    }

    public void setBorrowingHistory(LoanHistory borrowingHistory) {
        this.borrowingHistory = borrowingHistory;
    }
    
        public void payFees(double amount) {

            
                if (amount <= 0) {
                        System.out.println("Payment must be positive.");
                        return;
                }
                if (amount >= currentFees) {
                        System.out.println("Cleared fees! Change: RM" + (amount - currentFees));
                        this.currentFees = 0.0;
                } else {
                        this.currentFees -= amount;
                        System.out.println("Paid: RM" + amount + " | Remaining Balance: RM" + this.currentFees);
                }
                
                
        }
}
