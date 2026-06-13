package oodiproject;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Loan implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    
    private Book borrowedBook;
    private LocalDateTime dateOfLoan;
    private LocalDateTime dueDate;
    private String loanStatus;
    private int currentRenewalNo;
    
    public Loan (Book book){
        this.borrowedBook = book;
        this.dateOfLoan = LocalDateTime.now();
        this.dueDate = this.dateOfLoan.plusDays(7);
        this.loanStatus = "Due";//fix later
        this.currentRenewalNo = 0;
    }

    public Loan(Book borrowedBook, LocalDateTime dateOfLoan, LocalDateTime dueDate, int currentRenewalNo) {
        this.borrowedBook = borrowedBook;
        this.dateOfLoan = dateOfLoan;
        this.dueDate = dueDate;
        
        this.currentRenewalNo = currentRenewalNo;
        
        int overdueDays = (int) ChronoUnit.DAYS.between(this.dueDate, LocalDateTime.now());
        if (overdueDays >= 0){
            this.loanStatus = "Overdue";
        } else{
            this.loanStatus = "Due";
        }
    }

    //borrowedBook
    public Book getBorrowedBook() {
        return borrowedBook;
    }

    public void setBorrowedBook(Book borrowedBook) {
        this.borrowedBook = borrowedBook;
    }

    //dateOfLoan
    public LocalDateTime getDateOfLoan() {
        return dateOfLoan;
    }

    public void setDateOfLoan(LocalDateTime dateOfLoan) {
        this.dateOfLoan = dateOfLoan;
    }

    //duedate
    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    //loanStatus
    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    //currentRenewalNo
    public int getCurrentRenewalNo() {
        return currentRenewalNo;
    }

    public void setCurrentRenewalNo(int currentRenewalNo) {
        this.currentRenewalNo = currentRenewalNo;
    }
    
    public void renewLoan(int duration, SystemSettings settings){
        if (this.currentRenewalNo < 5){
            if(duration <= settings.getMaxRenewalDuration() && duration >= 1){
            this.dueDate = this.dueDate.plusDays(duration);
            this.currentRenewalNo++;
            } else{
                System.out.println("Invalid duration");
            }
            
        }
        else{
                System.out.println("Renewal Limit Reached");
        }
    }
    
    public double calculateFine(){
        double fine = 0;
        
        int overdueDays = (int) ChronoUnit.DAYS.between(this.dueDate, LocalDateTime.now());
        if (overdueDays >= 0){
            fine = overdueDays * LibraryBookBorrowingSystem.globalSettings.getFineRate();
        }
        
    String condition = this.borrowedBook.getBookCondition();
    if ("Damaged".equalsIgnoreCase(condition)) {
        fine = fine + 25.00;
    } else if ("Missing".equalsIgnoreCase(condition)) {
        fine = fine + 35.00;
    }
        
        return fine;
    }
}
