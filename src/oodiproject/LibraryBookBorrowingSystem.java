
package oodiproject;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class LibraryBookBorrowingSystem {
    
         static Catalog globalCatalog = new Catalog();
         static SystemSettings globalSettings = new SystemSettings();
         static Finances globalFinances = new Finances();
         static ArrayList<User> userDatabase = new ArrayList<>();
         static User loggedInUser = null;
    
         private static final String storageFile = "storagefile.dat";
         
    public static void saveData() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile))) {
            
            oos.writeObject(userDatabase);
            oos.writeObject(globalCatalog);
            oos.writeObject(globalSettings);
            oos.writeObject(globalFinances);
            
            System.out.println("[SYSTEM DATA STORAGE]: All engine data successfully written to disk.");
        } catch (IOException e) {
            System.err.println("[STORAGE ERROR]: Failed to preserve application state: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public static void loadData() {
        File dataFile = new File(storageFile);
        
        if (!dataFile.exists()) {
            System.out.println("[SYSTEM INITIALIZATION]: No previous storage state detected, creating new file.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(dataFile))) {
            
            userDatabase = (ArrayList<User>) ois.readObject();
            globalCatalog = (Catalog) ois.readObject();
            globalSettings = (SystemSettings) ois.readObject();
            globalFinances = (Finances) ois.readObject();
            
            System.out.println("[SYSTEM INITIALIZATION]: Previous program state loaded successfully.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("[CRITICAL LOAD ERROR]: Local data corrupted or incompatible: " + e.getMessage());
        }
    }
         
    public static void signUp(){
        
        Scanner sc = new Scanner(System.in);
        
        System.out.println("\n--- SYSTEM REGISTRATION (SIGN UP) ---");
        
        while (true) {
            
            System.out.print("Create Username (Must have at least 1 uppercase letter): ");
            String regUser = sc.next();
            
            System.out.print("Create Password (Must have at least 8 characters): ");
            String regPassword = sc.next();
                                        
            int newID = 100 + userDatabase.size();
            Patron newPatron = new Patron("", "", newID, false, 0.00);
                                        
            boolean isSuccessful = newPatron.signUp(regUser, regPassword);
                                        
            if (isSuccessful) {
                userDatabase.add(newPatron);
                System.out.println("Display: Signed Up Successfully!");
                System.out.println("Display: Redirecting to 'Login' page...");
                break;
            } else {
               System.out.println("Does not meet constraints. Please try again.");
            }
        }
    }
    
    public static void login (){
        Scanner sc = new Scanner(System.in);
        
        while (loggedInUser == null) {
            System.out.println("\n--- SYSTEM LOGIN ---");
            System.out.print("Enter Username: ");
            String inputUser = sc.next();
            
            System.out.print("Enter Password: ");
            String inputPassword = sc.next();

            for (User user : userDatabase) {
                if (user.login(inputUser, inputPassword)) {
                loggedInUser = user; 
                break;
                }
            }

            if (loggedInUser != null) {
                System.out.println("Login Successful!");
            } else {
                System.out.println("Validation Unsuccessful.");
                System.out.println("Wrong Username or Password. Please try again.");
            }//else
            }//while
    }
    
    public static void displayCatalog(){
        
        Scanner sc = new Scanner(System.in);
        
                        System.out.println("Select Catalog Filter:");
                        System.out.println("1. Search by Title");
                        System.out.println("2. Search by Author");
                        System.out.println("3. Filter by Genre");
                        System.out.print("Enter preference: ");
                        int catalogChoice = sc.nextInt();
                        sc.nextLine();
                        
                        if (catalogChoice == 1){
                            System.out.print("Enter title: ");
                            String title = sc.nextLine();
                            globalCatalog.searchByTitle(title);
                        }
                        if (catalogChoice == 2){
                            System.out.print("Enter author: ");
                            String author = sc.nextLine();
                            globalCatalog.searchByAuthor(author);
                        }
                        if (catalogChoice == 3){
                            System.out.print("Enter genre (Fiction, Science or Fantasy): ");
                            String genreChoice = sc.next();
                            globalCatalog.filterByGenre(genreChoice);
                        }
    }
    
    public static void patronMenu() {
        Scanner sc = new Scanner(System.in);
        
        Patron currentPatron = (Patron) loggedInUser;
                    System.out.println("Select Action:");
                    System.out.println("1. View Library Catalog");
                    System.out.println("2. Access Pay Fines Module");
                    System.out.println("3. Manage Loans");
                    System.out.println("4. Check Borrowing History");
                    System.out.println("5. Log Out");
                    System.out.print("Enter preference: ");
                    int patronChoice = sc.nextInt();
                    sc.nextLine();
                    
                    if (patronChoice == 1) {
                        displayCatalog();
                    }

                    if (patronChoice == 2) {

                        System.out.println("\nTotal combined outstanding statement balance: RM" + currentPatron.getCurrentFees());
                                                
                        if (currentPatron.getCurrentFees() <= 0) {
                            System.out.println("Account tracking status clear. No outstanding collection required.");
                        } else {
                                                        
                            System.out.print("Enter payment method: ");
                            String paymentMethod = sc.nextLine();
                            
                            System.out.print("Enter payment transaction execution amount: RM");
                            double transactionInput = sc.nextDouble();
                            
                                                        
                            System.out.println("\n[Payment Gateway Router Connection]: Verifying external transaction tokens...");
                            System.out.println("[Payment Gateway Router Connection]: Making payment via " + paymentMethod + "...");
                            System.out.println("[Payment Gateway Router Connection]: Saving secure tracking log record...");
                                                        
                            double initialFees = currentPatron.getCurrentFees();
                            
                            currentPatron.payFees(transactionInput);
                            
                            double finalFees = currentPatron.getCurrentFees();
                            double actualPaidAmount = initialFees - finalFees;
                            
                            globalFinances.setRevenue(globalFinances.getRevenue() + actualPaidAmount);
                        }
                    } else if (patronChoice == 3) {
                        currentPatron.manageLoan();
                    } else if (patronChoice == 4) {
                        currentPatron.checkBorrowingHistory();
                    }
                    
                    
                    else if (patronChoice == 5) {
                        loggedInUser.logOut();
                        loggedInUser = null;
                    }
    }
    
        public static void adminMenu() {
                Scanner sc = new Scanner(System.in);
                Admin currentAdmin = (Admin) loggedInUser;
                System.out.println("Select Action:");
                System.out.println("1. Generate Finances Report");
                System.out.println("2. Generate Inventory Catalogue Report");
                System.out.println("3. Generate User Report");
                System.out.println("4. Edit System Settings");
                System.out.println("5. Change Selected User Role");
                System.out.println("6. Log Out");
                System.out.print("Enter preference: ");
                int adminChoice = sc.nextInt();

                if (adminChoice == 1) {
                        currentAdmin.makeFinancesReport(globalFinances);
                } else if (adminChoice == 2) {
                        currentAdmin.makeInvCatReport(globalCatalog);
                } else if (adminChoice == 3) {
                        currentAdmin.makeUserReport(userDatabase);
                }else if (adminChoice == 4) {
                        currentAdmin.editSystemSettings(globalSettings);
                }else if (adminChoice == 5) {
                        currentAdmin.changeUserRole();
                }else if (adminChoice == 6) {
                        loggedInUser.logOut();
                        loggedInUser = null;
                }
                
        }
    
        public static void librarianMenu() {
                Scanner sc = new Scanner(System.in);
                Librarian currentLibrarian = (Librarian) loggedInUser;
                System.out.println("Select Action:");
                System.out.println("1. Generate Finances Report");
                System.out.println("2. Generate Inventory Catalogue Report");
                System.out.println("3. Generate User Report");
                System.out.println("4. Manage Loans");
                System.out.println("5. Check a Patron's Borrowing History");
                System.out.println("6. Add Book");
                System.out.println("7. Edit Book");
                System.out.println("8. Delete Book");
                System.out.println("9. Log Out");
                System.out.print("Enter preference: ");
                int librarianChoice = sc.nextInt();

                if (librarianChoice == 1) {
                        currentLibrarian.makeFinancesReport(globalFinances); 
                } else if (librarianChoice == 2) {
                        currentLibrarian.makeInvCatReport(globalCatalog);     
                } else if (librarianChoice == 3) {
                        currentLibrarian.makeUserReport(userDatabase);   
                } else if (librarianChoice == 4) {
                        currentLibrarian.manageLoan();
                }else if (librarianChoice == 5) {
                    currentLibrarian.checkBorrowingHistory();
                }else if (librarianChoice == 6) {
                    currentLibrarian.addBook();
                }else if (librarianChoice == 7) {
                        currentLibrarian.editBook();
                }else if (librarianChoice == 8) {
                        currentLibrarian.deleteBook();
                } else if (librarianChoice == 9) {
                        loggedInUser.logOut();
                        loggedInUser = null;
                }
        }
    
    public static void main(String[] args) {
        
       loadData();
        
       Scanner sc = new Scanner(System.in);
       int choice = 0;
       
       NewJFrame jframe = new NewJFrame();
       
       jframe.startGUI();

       if (userDatabase.isEmpty()) {
           userDatabase.add(new Patron("john_doe", "pass123", 101, false, 0));
           userDatabase.add(new Patron("jane_smith", "pass456", 102, false, 0));
           userDatabase.add(new Librarian("mr_green", "lbrPass", 501, "Office 102", "LBR-01", false));
           userDatabase.add(new Librarian("ms_tan", "lbrPass2", 502, "Office 105", "LBR-02", false));
           userDatabase.add(new Admin("alice_w", "adminPass", 999, "Suite 501", "ADM-09", false));
           userDatabase.add(new Admin("bob_k", "adminPass2", 998, "Suite 502", "ADM-10", false));
       }
       
       if (globalCatalog.getBooks().isEmpty()) {
           globalCatalog.addBook(new Book(1001, "Fiction", "George Orwell", "1984", "A1"));
           globalCatalog.addBook(new Book(1002, "Science", "Stephen Hawking", "A Brief History of Time", "1"));
           globalCatalog.addBook(new Book(1003, "Fantasy", "J.K. Rowling", "Harry Potter", "C1"));
           globalCatalog.addBook(new Book(1004, "Fiction", "F. Scott Fitzgerald", "The Great Gatsby", "A2"));
           globalCatalog.addBook(new Book(1005, "Science", "Carl Sagan", "Cosmos", "B2"));
           globalCatalog.addBook(new Book(1006, "Fantasy", "J.R.R. Tolkien", "The Hobbit", "C2"));
           globalCatalog.addBook(new Book(1007, "Fiction", "H.G. Wells", "The Time Machine", "A3"));
           globalCatalog.addBook(new Book(1008, "Science", "Randall Munroe", "What If?", "B3"));
           globalCatalog.addBook(new Book(1009, "Fantasy", "J.R.R. Tolkien", "The Lord of the Rings", "C3", false, "Lost"));
           globalCatalog.addBook(new Book(1010, "Fiction", "Dan Brown", "The Da Vinci Code", "A4"));
           globalCatalog.addBook(new Book(1011, "Science", "Merlin Sheldrake", "Entangled Life", "B4", false, "Good"));
           globalCatalog.addBook(new Book(1012, "Fantasy", "George R.R. Martin", "A Game of Thrones", "C4", true, "Damaged"));

           Book book1 = globalCatalog.getBooks().get(3);
           Book book2 = globalCatalog.getBooks().get(6);
           Book book3 = globalCatalog.getBooks().get(7);
           Book book4 = globalCatalog.getBooks().get(0);

           for (User user : userDatabase) {
               if (user instanceof Patron) {
                   Patron patron = (Patron) user;
                   if (patron.getUserID() == 101) {
                       patron.getBorrowingHistory().addLoanRecord(new Loan(book1));
                       java.time.LocalDateTime pastDate = java.time.LocalDateTime.now().minusDays(10);
                       java.time.LocalDateTime pastDueDate = pastDate.plusDays(7);
                       patron.getBorrowingHistory().addLoanRecord(new Loan(book2, pastDate, pastDueDate, 3));
                   } else if (patron.getUserID() == 102) {
                       java.time.LocalDateTime pastDate = java.time.LocalDateTime.now().minusDays(10);
                       java.time.LocalDateTime pastDueDate = pastDate.plusDays(7);
                       patron.getBorrowingHistory().addLoanRecord(new Loan(book3, pastDate, pastDueDate, 3));
                       pastDate = java.time.LocalDateTime.now().minusDays(107);
                       pastDueDate = pastDate.plusDays(100);
                       patron.getBorrowingHistory().addLoanRecord(new Loan(book4, pastDate, pastDueDate, 0));
                   }
               }
           }
       }
       
       
       System.out.println("======================================");
       System.out.println("    LIBRARY BOOK BORROWING SYSTEM");
       System.out.println("======================================");
       
       while (true){
       
           loggedInUser = null;
           
            System.out.println("Select Option: \n1. Sign Up \n2. Login \n3. Exit System");
            System.out.print("Enter option:");
            choice = sc.nextInt();
            System.out.println();
       
            if (choice == 3){
                sc.close();
                saveData();
                System.out.println("\nShutting down library systems. Goodbye!");
                break;
            }//if
       
            if (choice == 1){
                signUp();
            }
            
            if (choice == 2){
                login();
            }
            
            if (choice != 1 && choice != 2 && choice != 3){
                System.out.print("Invalid input, ");
            }//if
       
            while (loggedInUser != null) {
                System.out.println("\n----------------------------------------");
                System.out.println("----------------------------------------");

                if (loggedInUser instanceof Admin) {
                    adminMenu();     
                } 
                
                else if (loggedInUser instanceof Librarian) {
                    librarianMenu();
                                        
                } else if (loggedInUser instanceof Patron) {
                    patronMenu();
                }
            }
            
       }//while
    }//main
    
}//LibraryBookBorrowingSystem
