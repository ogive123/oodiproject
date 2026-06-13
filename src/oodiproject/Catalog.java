
package oodiproject;

import java.util.ArrayList;

public class Catalog implements java.io.Serializable{

    private ArrayList<Book> books;

    public Catalog() {

        books = new ArrayList<>();
    }
    
    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {

        books.add(book);
    }

    public void searchByTitle(String title) {
       
        boolean found = false;
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                System.out.println(book.getDetails());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books found with the title " + title );
        }
    }

    public void searchByAuthor(String author) {

        boolean found = false;
        for (Book book : books) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                System.out.println(book.getDetails());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books found with the author " + author);
        }
    }

    public void filterByGenre(String genre) {

        boolean found = false;
        for (Book book : books) {
            if (book.getGenre().equalsIgnoreCase(genre)) {
                System.out.println(book.getDetails());
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books found with the genre " + genre);
        }
    }


}
