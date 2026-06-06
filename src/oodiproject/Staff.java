package oodiproject;

import java.util.ArrayList;

public class Staff extends User {
    
        private String officeRoom;
        
        public Staff() {
                super("", "", 0, false);
        }
    
        public Staff(String username, String password, int userID, String officeRoom, boolean loginStatus) {
                super(username, password, userID, loginStatus);
                this.officeRoom = officeRoom;
        }

        public String getOfficeRoom() {
                return officeRoom;
        }

        public void setOfficeRoom(String officeRoom) {
                this.officeRoom = officeRoom;
        }
    
        public void makeFinancesReport(Finances finances) {
                System.out.println("\n=== FINANCIAL ANALYSIS REPORT LOG ===");
                System.out.println("Tracked Revenue Balance: RM" + finances.getRevenue());
                System.out.println("Tracked Expenditures Balance: RM" + finances.getExpenditure());
                System.out.println("Net Operating Performance Margin: RM" +
                        (finances.getRevenue() - finances.getExpenditure()));
        }
    
        public void makeInvCatReport(Catalog catalog) {
                System.out.println("\n=== INVENTORY CATALOGUE REPORT LOG ===");
                System.out.println("Current Catalog Items Mapping status:");
                catalog.searchByTitle(""); 
        }
    
        public void makeUserReport(ArrayList<User> userDatabase) {
                System.out.println("\n=== ACTIVE USER METRICS PROFILE REPORT ===");
                System.out.println("Total Registered System Accounts: " + userDatabase.size());
                System.out.println("----------------------------------------");
                for (User u : userDatabase) {
                        System.out.println("User ID: " + u.getUserID() + " | Username: " + u.getUsername() + " | Type: " + u.getClass().getSimpleName());
                }
        }
}