package oodiproject;

import java.util.Scanner;

public class Admin extends Staff {
    
        private String adminID;

        public Admin() {
                super("", "", 0, "", false);
        }
    
        public Admin(String username, String password, int userID, String officeRoom, String adminID, boolean loginStatus) {
                super(username, password, userID, officeRoom, loginStatus);
                this.adminID = adminID;
        }

        public String getAdminID() {
                return adminID;
        }

        public void setAdminID(String adminID) {
                this.adminID = adminID;
        }
    
        public void changeUserRole() {
        Scanner sc = new Scanner(System.in);

        java.util.ArrayList<User> db = LibraryBookBorrowingSystem.userDatabase;

        System.out.println("\n=========================================");
        System.out.println("        MANAGE USER ACCOUNT ROLES      ");
        System.out.println("=========================================");
        for (int i = 0; i < db.size(); i++) {
                User u = db.get(i);
                System.out.println(i + ". [ID: " + u.getUserID() + "] Username: " + u.getUsername() + 
                                   " | Active Role: " + u.getClass().getSimpleName());
        }

        System.out.print("\nEnter index reference number of the user to modify: ");
        int index = sc.nextInt();

        if (index < 0 || index >= db.size()) {
                System.out.println("[ABORT] Selection out of database collection boundary lines.");
                return;
        }

        User oldUser = db.get(index);
        
        System.out.println("\nSelect New Role for " + oldUser.getUsername() + ":");
        System.out.println("1. System Admin");
        System.out.println("2. Librarian");
        System.out.println("3. Library Patron");
        System.out.print("Enter option selection code (1-3): ");
        int roleChoice = sc.nextInt();
        sc.nextLine();

        User newUser = null;
        
        String username = oldUser.getUsername();
        String password = oldUser.getPassword();
        int userID = oldUser.getUserID();
        boolean loginStatus = oldUser.getLoginStatus();

        if (roleChoice == 1) {
                        System.out.print("Assign Office/Room Location Identifier: ");
                        String adminOffice = sc.nextLine();
                        System.out.print("Assign New Unique Admin ID String: ");
                        String adminID = sc.nextLine();
                        
                        newUser = new Admin(username, password, userID, adminOffice, adminID, loginStatus);
        }
        else if(roleChoice == 2){
                        System.out.print("Assign Office/Room Location Identifier: ");
                        String libOffice = sc.nextLine();
                        System.out.print("Assign New Unique Librarian ID String: ");
                        String librarianID = sc.nextLine();
                        
                        newUser = new Librarian(username, password, userID, libOffice, librarianID, loginStatus);
                }
                        
            else if(roleChoice == 3){
                        System.out.print("Set Initial Outstanding Statements Balance (RM): ");
                        double initialFees = sc.nextDouble();
                        
                        newUser = new Patron(username, password, userID, loginStatus, initialFees);
            } 
            else{
                        System.out.println("[ABORT] Invalid conversion sequence entry option code.");
            }

        db.set(index, newUser);
        System.out.println("\n[SUCCESS] " + username + "'s account privilege type has shifted to " + 
                           newUser.getClass().getSimpleName() + ".");
        }
    
        public void editSystemSettings(SystemSettings settings) {
                Scanner sc = new Scanner(System.in);
                System.out.println("\n--- CONFIGURING GLOBAL SYSTEM SETTINGS ---");
                System.out.println("Current Parameters:");
                System.out.println("1. Max Loan Limit: " + settings.getMaxLoanLimit() + " books");
                System.out.println("2. Max Renewals Permitted: " + settings.getMaxRenewals() + " times");
                System.out.println("3. Max Renewal Duration: " + settings.getMaxRenewalDuration() + " days");
                System.out.println("4. Overdue Fine Threshold Rate: RM" + settings.getFineRate() + "/day");
                
                System.out.print("\nSelect parameter index to modify (1-4) or any other number to cancel: ");
                int choice = sc.nextInt();
                
                if (choice == 1) {
                        System.out.print("Enter new maximum loan limit: ");
                        int val = sc.nextInt();
                        settings.setMaxLoanLimit(val);
                        System.out.println("Success: Maximum loan limit updated.");
                } else if (choice == 2) {
                        System.out.print("Enter new maximum renewals limit: ");
                        int val = sc.nextInt();
                        settings.setMaxRenewals(val);
                        System.out.println("Success: Maximum renewals parameter updated.");
                } else if (choice == 3) {
                        System.out.print("Enter new maximum renewal duration (days): ");
                        int val = sc.nextInt();
                        settings.setMaxRenewalDuration(val);
                        System.out.println("Success: Renewal window duration parameter updated.");
                } else if (choice == 4) {
                        System.out.print("Enter new daily fine rate: RM");
                        double val = sc.nextDouble();
                        settings.setFineRate(val);
                        System.out.println("Success: Daily fine threshold value updated.");
                } else {
                        System.out.println("Configuration menu exited. No changes applied.");
                }
        }
}