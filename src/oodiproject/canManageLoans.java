package oodiproject;

import java.util.List;
import java.util.Scanner;

public interface canManageLoans {
    default void manageLoan(){
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Select Action:");
        System.out.println("1. Borrow Book");
        System.out.println("2. Renew Book");
        System.out.println("3. Return Book");
        System.out.print("Enter preference: ");
        int choice = sc.nextInt();
        
        if (this instanceof Patron) {
            Patron patron = (Patron) this;
        
            if (choice == 1) {
                System.out.print("\nEnter the Book ID to borrow: ");
                int bookID = sc.nextInt();
                
                Book targetBook = null;
                for (Book b : LibraryBookBorrowingSystem.globalCatalog.getBooks()) {
                    if (b.getBookID() == bookID) {
                        targetBook = b;
                        break;
                    }
                }
                
                if (targetBook == null) {
                    System.out.println("[ERROR] No matching book profile found matching ID: " + bookID);
                    return;
                }
                if (!targetBook.isIsAvailable()) {
                    System.out.println("[ABORT] That book is currently out on loan to another patron.");
                    return;
                }
                
                if (patron.getCurrentFees() >= LibraryBookBorrowingSystem.globalSettings.getFineThreshold()) {
                    System.out.println("[BLOCKED] Transaction denied! Your outstanding fees (RM" + patron.getCurrentFees() + 
                                       ") meet or exceed the maximum allowed limit of RM" + 
                                       LibraryBookBorrowingSystem.globalSettings.getFineThreshold());
                    return;
                }
                
                if (patron.getBorrowingHistory().getLoans().size() >= LibraryBookBorrowingSystem.globalSettings.getMaxLoanLimit()) {
                    System.out.println("[BLOCKED] Transaction denied! Max active inventory cap reached (" + 
                                       LibraryBookBorrowingSystem.globalSettings.getMaxLoanLimit() + " books max allowed).");
                    return;
                }
                
                targetBook.updateStatus(false);
                Loan cleanLoanRecord = new Loan(targetBook);
                patron.getBorrowingHistory().addLoanRecord(cleanLoanRecord);
                System.out.println("[SUCCESS] Processing checkout complete. Enjoy reading: " + targetBook.getTitle());
            }
            
            else if (choice == 2) {
                List<Loan> activeLoans = patron.getBorrowingHistory().getLoans();
                if (activeLoans.isEmpty()) {
                    System.out.println("\nYou currently have no active ongoing book checkouts to renew.");
                    return;
                }
                
                System.out.println("\n--- Your Active Book Loans ---");
                for (int i = 0; i < activeLoans.size(); i++) {
                    Loan l = activeLoans.get(i);
                    System.out.println(i + ". " + l.getBorrowedBook().getTitle() + " [Renewals Used: " + l.getCurrentRenewalNo() + "]");
                }
                System.out.print("Enter selection index tracking reference number to extend: ");
                int loanIndex = sc.nextInt();
                
                if (loanIndex >= 0 && loanIndex < activeLoans.size()) {
                    Loan selectedLoan = activeLoans.get(loanIndex);
                    System.out.print("Enter timeline duration extension period (In Days): ");
                    int processingDays = sc.nextInt();
                    
                    if (selectedLoan.getCurrentRenewalNo() >= LibraryBookBorrowingSystem.globalSettings.getMaxRenewals()) {
                        System.out.println("[DENIED] Maximum system renewal capacity limits hit (" + 
                                           LibraryBookBorrowingSystem.globalSettings.getMaxRenewals() + " extensions max).");
                    } else if (processingDays < 1 || processingDays > LibraryBookBorrowingSystem.globalSettings.getMaxRenewalDuration()) {
                        System.out.println("[DENIED] Invalid timeline scope request. Window size allocation rule allows up to " + 
                                           LibraryBookBorrowingSystem.globalSettings.getMaxRenewalDuration() + " days.");
                    } else {
                        selectedLoan.renewLoan(processingDays, LibraryBookBorrowingSystem.globalSettings);
                        selectedLoan.setCurrentRenewalNo(selectedLoan.getCurrentRenewalNo() + 1);
                        System.out.println("[SUCCESS] New return due timeline configured for: " + selectedLoan.getBorrowedBook().getTitle());
                    }
                } else {
                    System.out.println("[ERROR] Selection target out of listing boundary indexes.");
                }
            }
            
    else if (choice == 3) {
        List<Loan> activeLoans = patron.getBorrowingHistory().getLoans();
        if (activeLoans.isEmpty()) {
            System.out.println("\nYou currently have no checked-out items to return.");
            return;
        }

        System.out.println("\n--- Select Book to Return ---");
        for (int i = 0; i < activeLoans.size(); i++) {
            Loan l = activeLoans.get(i);
            System.out.println(i + ". " + l.getBorrowedBook().getTitle() + " [Current Status: " + l.getLoanStatus() + "]");
        }
        System.out.print("Enter target list entry position selection code: ");
        int returnIndex = sc.nextInt();
    
        if (returnIndex >= 0 && returnIndex < activeLoans.size()) {
            Loan selectedLoan = activeLoans.get(returnIndex);
            Book selectedBook = selectedLoan.getBorrowedBook();
        
            System.out.print("Evaluate returned book condition (1. Good | 2. Damaged | 3. Missing): ");
            int conditionChoice = sc.nextInt();

            if (conditionChoice == 2) {
                selectedBook.setBookCondition("Damaged");
                selectedBook.updateStatus(true);
            } else if (conditionChoice == 3) {
                selectedBook.setBookCondition("Missing");
                selectedBook.updateStatus(false);
            } else {
                selectedBook.setBookCondition("Good");
                selectedBook.updateStatus(true);
            }
        
            selectedLoan.setLoanStatus("Returned");
        
            double transactionFine = selectedLoan.calculateFine();
            if (transactionFine > 0) {
                        patron.setCurrentFees(patron.getCurrentFees() + transactionFine);
                System.out.println("[FEES ASSESSED]: An automated fine of RM" + transactionFine + 
                                   " has been generated and charged to your account.");
            } else {
                System.out.println("[RETURN SUCCESSFUL]: Book returned on schedule in good condition. No fees incurred.");
            }
        
            activeLoans.remove(returnIndex);
        } else {
            System.out.println("[ERROR] Selection target out of listing boundary indexes.");
        }
    }
    }
}
    default void checkBorrowingHistory(){
        if (this instanceof Patron) {
            Patron patron = (Patron) this;
            List<Loan> internalLog = patron.getBorrowingHistory().getLoans();
            
            System.out.println("\n=========================================");
            System.out.println("    PERSONAL LOG TRANSACTION HISTORY ");
            System.out.println("=========================================");
            
            if (internalLog.isEmpty()) {
                System.out.println("No recorded historical book checkouts found linked to account ID: " + patron.getUserID());
            } else {
                for (Loan l : internalLog) {
                    System.out.println("- [" + l.getLoanStatus() + "] " + l.getBorrowedBook().getTitle() + 
                                       " | Due on: " + l.getDueDate());
                }
            }
        }
    }
}
