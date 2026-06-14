package oodiproject;

public abstract class User implements java.io.Serializable{
    private static final long serialVersionUID = 1L;
    
        private String username;
        private String password;
        private int userID;
        private boolean loginStatus;
        
        public User(String username,String password,int userID, boolean loginStatus){
               this.username = username;
               this.password = password;
               this.userID = userID;
               this.loginStatus = loginStatus;
        
    }
    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public int getUserID() {
        return userID;
    }


    public void setUserID(int userID) {
        this.userID = userID;
    }


    public boolean getLoginStatus() {
        return loginStatus;
    }


    public void setLoginStatus(boolean loginStatus) {
        this.loginStatus = loginStatus;
    } 
    
    public void logOut() {
        this.loginStatus = false;
        System.out.println("\n[SUCCESS] " + username + " has logged out safely.");
    }

    public boolean login(String inputUser, String inputPassword) {
        if (this.username.equals(inputUser) && this.password.equals(inputPassword)) {
            this.loginStatus = true;
            return true;
        }
            return false;
        }
    
    public boolean signUp(String user, String password) {
        if (user == null || !(user.trim().matches(".*[A-Z].*")) || password == null || password.trim().length() < 8) {
        System.out.println("\n[SYSTEM LOG]: Check Input -> Storing Unsuccessful.");
        return false;
        }
                
        this.username = user;
        this.password = password;
        System.out.println("\n[SYSTEM LOG]: Check Input -> Store data -> Storing Successful.");
        return true;
    }
}
