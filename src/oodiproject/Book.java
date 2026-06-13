
package oodiproject;

public class Book implements java.io.Serializable{

    private int bookID;
    private String genre;
    private String author;
    private String title;
    private boolean isAvailable;
    private String bookCondition;
    private String shelfLocation;

    public Book(int bookID, String genre, String author,
                String title, String shelfLocation) {

        this.bookID = bookID;
        this.genre = genre;
        this.author = author;
        this.title = title;
        this.shelfLocation = shelfLocation;

        isAvailable = true;
        bookCondition = "Good";
    }
    
    public Book(int bookID, String genre, String author,
                String title, String shelfLocation, boolean isAvailable, String bookCondition) {

        this.bookID = bookID;
        this.genre = genre;
        this.author = author;
        this.title = title;
        this.shelfLocation = shelfLocation;

        this.isAvailable = isAvailable;
        this.bookCondition = bookCondition;
    }
    
    public int getBookID() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID = bookID;
    }

    public boolean isIsAvailable() {
        return isAvailable;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public String getBookCondition() {
        return bookCondition;
    }

    public void setBookCondition(String bookCondition) {
        this.bookCondition = bookCondition;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }

    public void setShelfLocation(String shelfLocation) {
        this.shelfLocation = shelfLocation;
    }

    public void updateStatus(boolean status) {
        isAvailable = status;
    }

    public void updateCondition(String condition) {
        bookCondition = condition;
    }

    public String getDetails() {
        return "Book ID: " + bookID +
                "\nTitle: " + title +
                "\nAuthor: " + author +
                "\nGenre: " + genre +
                "\nAvailable: " + isAvailable +
                "\nCondition: " + bookCondition +
                "\nShelf Location: " + shelfLocation + "\n";
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }
}