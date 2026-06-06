package oodiproject;

import java.util.List;
import java.util.Scanner;

public class Librarian extends Staff implements canManageLoans{

    private String librarianID;

    public Librarian(String username,String password,int userID, String officeRoom, String librarianID, boolean loginStatus) {
        super(username,password,userID, officeRoom, loginStatus);
        this.librarianID = librarianID;
    }

    public void addBook() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Book Title: ");
        String title = sc.nextLine();
        System.out.print("Enter Author Name: ");
        String author = sc.nextLine();
        System.out.print("Enter Genre Classification: ");
        String genre = sc.nextLine();
        System.out.print("Enter Library Shelf Location: ");
        String shelfLocation = sc.nextLine();
        System.out.print("Enter Purchase Cost for this Book (RM): ");
        double bookCost = sc.nextDouble();

        int newBookID = 1000 + LibraryBookBorrowingSystem.globalCatalog.getBooks().size();
        
        Book newBook = new Book(newBookID, genre, author, title, shelfLocation);
        LibraryBookBorrowingSystem.globalCatalog.addBook(newBook);
        
        double currentExpenditure = LibraryBookBorrowingSystem.globalFinances.getExpenditure();
        LibraryBookBorrowingSystem.globalFinances.setExpenditure(currentExpenditure + bookCost);
        
        System.out.println("\n[SUCCESS] \"" + title + "\" recorded successfully.");
    }

    public void deleteBook() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the Book ID to completely delete: ");
        int searchID = sc.nextInt();

        Book targetBook = null;
        for (Book book : LibraryBookBorrowingSystem.globalCatalog.getBooks()) {
            if (book.getBookID() == searchID) {
                targetBook = book;
                break;
            }
        }

        if (targetBook != null) {
            LibraryBookBorrowingSystem.globalCatalog.getBooks().remove(targetBook);
            System.out.println("[SUCCESS] Item record ID " + searchID + " (\"" + targetBook.getTitle() + "\") dropped from inventory.");
        } else {
            System.out.println("[ERROR] No item matches that Book ID in our system records.");
        }
    }

    public void editBook() {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter target Book ID to update: ");
        int searchID = sc.nextInt();
        sc.nextLine();

        Book targetBook = null;
        for (Book book : LibraryBookBorrowingSystem.globalCatalog.getBooks()) {
            if (book.getBookID() == searchID) {
                targetBook = book;
                break;
            }
        }

        if (targetBook == null) {
            System.out.println("[ERROR] Book ID profile not found.");
            return;
        }

        System.out.println("Modifying Book: " + targetBook.getTitle());
        System.out.println("Current Condition State: " + targetBook.getBookCondition());
        System.out.print("Enter updated condition profile string (e.g., Good, Damaged, Lost): ");
        String updateCondition = sc.nextLine();
        
        targetBook.updateCondition(updateCondition);
        System.out.println("[SUCCESS] Changes committed successfully.");
    }

    @Override
    public void manageLoan() {
        Scanner sc = new Scanner(System.in);

        System.out.println("1. Manually Override & Issue Loan to Patron");
        System.out.println("2. Process Manual Returned Book Drop-off");
        System.out.print("Enter preference choice code: ");
        int subChoice = sc.nextInt();

        System.out.print("\nEnter target Patron Account User ID: ");
        int targetPatronID = sc.nextInt();

        Patron patron = null;
        for (User u : LibraryBookBorrowingSystem.userDatabase) {
            if (u instanceof Patron && u.getUserID() == targetPatronID) {
                patron = (Patron) u;
                break;
            }
        }

        if (patron == null) {
            System.out.println("[ERROR] No active Patron profiles match User ID: " + targetPatronID);
            return;
        }

        if (subChoice == 1) {
            System.out.print("Enter Book ID to cross-reference check out: ");
            int bookID = sc.nextInt();

            Book targetBook = null;
            for (Book b : LibraryBookBorrowingSystem.globalCatalog.getBooks()) {
                if (b.getBookID() == bookID) {
                    targetBook = b;
                    break;
                }
            }

            if (targetBook == null || !targetBook.isIsAvailable()) {
                System.out.println("[ABORT] Item matching that target ID is unavailable or missing.");
                return;
            }

            targetBook.updateStatus(false);
            Loan authorizedOverrideLoan = new Loan(targetBook);
            patron.getBorrowingHistory().addLoanRecord(authorizedOverrideLoan);
            System.out.println("[OVERRIDE SUCCESS] Book \"" + targetBook.getTitle() + "\" manually attached to account ID: " + targetPatronID);
        } 
        else if (subChoice == 2) {
            List<Loan> activeLoans = patron.getBorrowingHistory().getLoans();
            if (activeLoans.isEmpty()) {
                System.out.println("This account profile currently holds no active outbound borrow assets.");
                return;
            }

            System.out.println("\n--- Current Active Patron Outbound Loans ---");
            for (int i = 0; i < activeLoans.size(); i++) {
                System.out.println(i + ". " + activeLoans.get(i).getBorrowedBook().getTitle());
            }
            System.out.print("Select index identification target to return: ");
            int returnIndex = sc.nextInt();

            if (returnIndex >= 0 && returnIndex < activeLoans.size()) {
                Loan targetLoan = activeLoans.get(returnIndex);
                
                targetLoan.getBorrowedBook().updateStatus(true);
                targetLoan.setLoanStatus("Returned");
                
                double fineCalculation = targetLoan.calculateFine();
                if (fineCalculation > 0) {
                    patron.setCurrentFees(patron.getCurrentFees() + fineCalculation);
                    System.out.println("[RETURN COMPLETE] Item overdue! Added fine balance penalty of RM" + fineCalculation);
                } else {
                    System.out.println("[RETURN COMPLETE] Item restored to inventory on schedule.");
                }
                activeLoans.remove(returnIndex);
            } else {
                System.out.println("[ERROR] Out-of-bounds collection pointer assignment mapping index.");
            }
        }
    }
    
    @Override
    public void checkBorrowingHistory() {
        Scanner sc = new Scanner(System.in);
        
        System.out.print("Enter target Patron Account User ID: ");
        int targetPatronID = sc.nextInt();

        Patron patron = null;
        for (User u : LibraryBookBorrowingSystem.userDatabase) {
            if (u instanceof Patron && u.getUserID() == targetPatronID) {
                patron = (Patron) u;
                break;
            }
        }

        if (patron == null) {
            System.out.println("[ERROR] No registered profile records match User ID: " + targetPatronID);
            return;
        }

        List<Loan> masterLog = patron.getBorrowingHistory().getLoans();
        if (masterLog.isEmpty()) {
            System.out.println("No recorded loan items found in this profile.");
        } else {
            for (Loan record : masterLog) {
                System.out.println("- [" + record.getLoanStatus() + "] Title: " + record.getBorrowedBook().getTitle() + 
                                   " | Due-by Target Timeline: " + record.getDueDate());
            }
        }
    }
}