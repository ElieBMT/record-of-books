package library;

import book.*;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/** 
 * Handles interactions with all collected books
 * @author EB Mitoumba-Tindy
 * @version Version 1
 */
public class Library{
    // ----- Attributes -----
    private Scanner systemInput;
    private String name;
    private int numBooks;
    private Book[] ListOfBooks;

    // ----- Constructors -----
    public Library(){} // Determine if any default data is needed
    public Library(String name, int numBooks, Scanner systemInput){
        setName(name);
        setNumBooks(numBooks);
        setInputScanner(systemInput);
        initialiseArray();
    }

    // ----- Getters -----
    public String getName(){
        return name;
    }

    public int getNumBooks(){
        return numBooks;
    }

    public Book getBookAtIndex(int index){
        return ListOfBooks[index];
    }

    // ----- Setters -----
    private void setInputScanner(Scanner systemInput){
        this.systemInput = systemInput;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setNumBooks(int numBooks){
        this.numBooks = numBooks;
    }

    public void setBookAtIndex(int index, String title, String author, long ISBN, int numPages, int yearRead, EBookCategory category){
        ListOfBooks[index] = new Book(title, author, ISBN, numPages, yearRead, category);
    }
    public void setBookAtIndex(int index, String title, String author, long ISBN, int numPages, int yearRead, String category){
        ListOfBooks[index] = new Book(title, author, ISBN, numPages, yearRead, category);
    }

    // ----- Methods -----
    private void initialiseArray(){
        if (numBooks <= 0){
            numBooks = 0;
            ListOfBooks = null;
            return;
        }
        // Execute if the number of books is greater than 0
        ListOfBooks = new Book[numBooks];
        for (int b = 0; b < numBooks; b++){
            ListOfBooks[b] = new Book();
        }
    }

    private void increaseArraySizeBy1(){
        // Create a new temporary array with an increased size
        Book[] newListOfBooks = new Book[numBooks + 1];

        // Copy over values from the old array
        for (int i = 0; i < numBooks; i++){
            newListOfBooks[i] = ListOfBooks[i];
        }

        // Switch pointer to the new array and allow GC to collect no-longer-needed memory
        ListOfBooks = newListOfBooks;
    }

    private void reduceArraySizeBy1(int index){ // Validation is done in the calling method
        // Create a new temporary array
        Book[] newListOfBooks = new Book[numBooks - 1];

        // Copy all books before the selected index (if any)
        for (int b = 0; b < index; b++){
            newListOfBooks[b] = ListOfBooks[b];
        }

        // Copy all books after the selected index (if any)
        for (int b = index + 1; b < numBooks; b++){
            newListOfBooks[b - 1] = ListOfBooks[b];
        }

        // Switch pointer and let the GC collect unneeded memory
        ListOfBooks = newListOfBooks;
    }

    public void addBook(String title, String author, long ISBN, int numPages, int yearRead, String category){
        if (ListOfBooks == null) {
            numBooks++; // Particular positioning of 'numBooks' increment is necessary for prepping intialisation logic
            initialiseArray();
            setBookAtIndex(numBooks - 1, title, author, ISBN, numPages, yearRead, category);
        } 
        else{
            increaseArraySizeBy1();  
            setBookAtIndex(numBooks, title, author, ISBN, numPages, yearRead, category); // The old value of 'numBooks' is used as the index for simple placement in the free index at the end of the extended array
            numBooks++; // Incrementation left at the end to simplify indexing logic
        }
    }

    public void removeBook(int index){
        // This check must execute first. Exit if there are no books to delete, to protect against errors and exceptions
        if (numBooks == 0){
            System.out.println("No books to delete.");
            return;
        }

        // Perform data validation on the provided index
        while (index < 0 || index >= numBooks){
            System.err.printf("Invalid index. Must be in range: [0,%d]", numBooks - 1);
            System.out.println("Enter a valid index: ");
            index = Integer.parseInt(systemInput.nextLine());
        }

        // Proceed if there are books to delete (index validation done in the function)
        reduceArraySizeBy1(index);
        numBooks--;
    }

    // To avoid extra newline chars and related errors, perhaps I shouldn't use 'println' and should instead use 'print' - where every line after the first is prefixed by \n
    public void saveToFile(File fileHandle){
        try(PrintWriter pw = new PrintWriter(fileHandle)){
            // Save the value of library's name and number of books
            pw.println(getName());
            pw.println(getNumBooks());

            // Save each book's data as a line in the file
            if (numBooks > 0 && ListOfBooks != null){
                for (Book book : ListOfBooks){
                    pw.printf("%s;%s;%d;%d;%d;%s%n", // %n used instead of \n, to ensure the OS-specific newline char is used (protection for older windows systems)
                    book.getTitle(), 
                    book.getAuthor(), 
                    book.getISBN(), 
                    book.getNumPages(), 
                    book.getYearRead(), 
                    book.getCategoryString());
                }
            }
        }
        catch (IOException ioEx){ // Too broad?
            ioEx.printStackTrace();
        }
    }

    public void readFromFile(File fileHandle){
        try(Scanner fileScanner = new Scanner(fileHandle)){
            // File exists, but is empty, make no changes - Determine if this is safe
            if (fileHandle.exists() && !fileScanner.hasNext()){
                System.out.println("File found, but empty. No data has been retrieved.");
                fileScanner.close(); // Since I don't reach the rest of the code. Is 'break' more suitable?
                return;
            }

            // 'if' is used over 'while' to ensure a leftover '\n' does not cause false-positive errors
            if (fileScanner.hasNext()){
                // Perhaps the file header (name and numBooks) should have a regex expression to determine if I can safely read in books          
                // Library-level reading in
                setName(fileScanner.nextLine());
                setNumBooks(Integer.parseInt(fileScanner.nextLine()));
                initialiseArray();

                // Book-level reading in
                if (ListOfBooks == null || numBooks <= 0) return;
                for (Book book : ListOfBooks){
                    // Read in the entire line containing book-specific data
                    String bookData = fileScanner.nextLine();
                    StringTokenizer st = new StringTokenizer(bookData, ";");
                    
                    // Break the string up, and parse accordingly. Would regex be safer?
                    if (st.countTokens() == 6){
                        book.setTitle(st.nextToken());
                        book.setAuthor(st.nextToken());
                        book.setISBN(Long.parseLong(st.nextToken()));
                        book.setNumPages(Integer.parseInt(st.nextToken()));
                        book.setYearRead(Integer.parseInt(st.nextToken()));
                        book.setCategoryWithString(st.nextToken());
                    }
                }   
            }
        }
        catch(IOException ioEx){ // Too broad?
            ioEx.printStackTrace();
            System.err.println("An error occured. No changes have been made. Exiting.");
            System.exit(1);
        }
    }

    public void displayBookMetaData(int index){
        System.out.printf("%d) %s [%s|%d|%s]%n", 
        index + 1,
        ListOfBooks[index].getTitle(),
        ListOfBooks[index].getAuthor(),
        ListOfBooks[index].getYearRead(),
        ListOfBooks[index].getCategoryString());
    }
}