package menu;

import java.util.Scanner;
import java.io.File;
import library.Library;

/**
 * The senior-most (non-Main) class in my project.
 * Owns the 'System.in' scanner object - passed to dependent class (Library) as needed.
 * @author EB Mitoumba-Tindy
 * @version Version 1
 */
public class Menu{
    // ----- Attributes -----
    private static Scanner sc = new Scanner(System.in);
    private static File saveFile = new File("data/saveFile.txt");
    private static Library myLibrary = new Library("Elie's Books", 0, sc); // Default Library obj
    private static char mainOption = 'X';

    // ----- Methods -----     
    public static void displayMenu(){
        myLibrary.readFromFile(saveFile); // Repopulate library obj, if possible, else - carry on with the default obj

        do{
            clearScreen();

            // Display library metadata
            System.out.printf("Library name: %s%n", myLibrary.getName());
            System.out.printf("Number of books: %d%n%n", myLibrary.getNumBooks());

            // Display main menu
            System.out.println("A) Display all Books");
            System.out.println("B) Add a Book");
            System.out.println("C) Remove a Book");
            System.out.println("D) Modify a Book");
            System.out.println("X) Exit Program");
            System.out.print("Option: ");

            // Read user's selection
            char[] acceptedInputArray_mainMenu = {'A','B','C','D','X'};
            mainOption = userInput_menuChar(acceptedInputArray_mainMenu);

            // Perform the actions associated with the chosen option and save after each iteration
            runMenuOption();
            myLibrary.saveToFile(saveFile);

        } while(mainOption != 'X');

        // Close the system-level scanner at the true end of the program
        sc.close();
    }

    private static void runMenuOption(){
        System.out.println();
        switch (mainOption){
            case 'A':{
                clearScreen();

                // Will need to make more naunced later on (sorting/filtering)
                for (int i = 0; i < myLibrary.getNumBooks(); i++){
                    myLibrary.displayBookMetaData(i);
                }
                break;
            }
            case 'B':{
                clearScreen();

                // Read the book's title
                System.out.print("Enter the book's title: ");
                String title = sc.nextLine();

                // Read the book's author
                System.out.print("Enter the book's author: ");
                String author = sc.nextLine();

                // Read book's ISBN
                System.out.print("Enter the book's ISBN: ");
                long ISBN = 0;
                ISBN = userInput_Long();
            
                // Read book's number of pages
                System.out.print("Enter the book's number of pages: ");
                int numPages = 0;
                numPages = userInput_Int();

                // Read book's year read
                System.out.print("Enter the year the book was read in: ");
                int yearRead = 0;
                yearRead = userInput_Int();

                // Read book's category      
                String categoryString = null;
                int categoryOption = 0;
                
                System.out.print("Enter the book's category [1) Educational | 2) Recreational]: ");
                categoryOption = userInput_Int();

                while (!(categoryOption == 1 || categoryOption == 2)){
                    System.err.println("Invalid entry. Must be in range: [1,2].");
                    System.out.print("Enter the year the book was read in: ");
                    categoryOption = userInput_Int();
                }
                switch (categoryOption){
                    case 1:{
                        categoryString = "Educational";
                        break;
                    }
                    case 2:{
                        categoryString = "Recreational";
                        break;
                    }
                }

                // Finally, add the new book to the library
                myLibrary.addBook(title, author, ISBN, numPages, yearRead, categoryString);
                break;
            }
            case 'C':{
                clearScreen();

                // Display book metadata
                for (int i = 0; i < myLibrary.getNumBooks(); i++){
                    myLibrary.displayBookMetaData(i);
                }

                // Read in the (human-readable) index of the book to be deleted
                int index = 0;
                System.err.print("Enter the index of the book to be removed: ");
                index = userInput_Int();
                myLibrary.removeBook(index - 1); // 'index - 1' => To accomodate for the 1-indexing of the displayed books
                break;
            }
            case 'D':{
                clearScreen();

                // Display book metadata
                for (int i = 0; i < myLibrary.getNumBooks(); i++){
                    myLibrary.displayBookMetaData(i);
                }

                // Read in the index of the book to be modified
                int bookIndex = 0;
                bookIndex = userInput_Int() - 1; // '-1' to Change from 1-indexing to 0-indexing

                // Perform data validation
                while (bookIndex < 0 || bookIndex >= myLibrary.getNumBooks()){
                    System.err.printf("Invalid index. Must be in range: [1, %d)", myLibrary.getBookAtIndex(bookIndex));
                    bookIndex = userInput_Int();
                }

                
                char subMenuOption = 'A';
                do{
                    // Display sub-menu
                    System.out.printf("For the book [%s], which field would you like to modify?%n", myLibrary.getBookAtIndex(bookIndex).getTitle());
                    System.out.println("A) Book Title");
                    System.out.println("B) Book Author");
                    System.out.println("C) Book ISBN");
                    System.out.println("D) Book's number of pages");
                    System.out.println("E) Book's year read");
                    System.out.println("F) Book Category");
                    System.out.println("X) Exit Sub-Menu");
                    System.out.print("Option: ");

                    // Read in user input
                    char[] acceptedInputArray_subMenu = {'A','B','C','D','E','F','X'};
                    subMenuOption = userInput_menuChar(acceptedInputArray_subMenu);
                    System.out.println();

                    // Operate sub-menu
                    switch (subMenuOption){
                        case 'A':{
                            // Read in new title for chosen book
                            String oldTitle = myLibrary.getBookAtIndex(bookIndex).getTitle();
                            System.out.print("Enter a new title for the book: ");
                            String newtitle = sc.nextLine();
                            System.out.println('\n');

                            // Update the title of chosen book
                            myLibrary.getBookAtIndex(bookIndex).setTitle(newtitle);

                            // Notify user of the change
                            System.out.printf("'Title' successfully changed from [%s] to [%s]", oldTitle, newtitle);
                            break;
                        }
                        case 'B':{
                            // Read in new author for chosen book
                            String oldAuthor = myLibrary.getBookAtIndex(bookIndex).getAuthor();
                            System.out.print("Enter a new author for the book: ");
                            String newAuthor = sc.nextLine();
                            System.out.println('\n');

                            // Update the author of chosen book
                            myLibrary.getBookAtIndex(bookIndex).setAuthor(newAuthor);

                            // Notify user of the change
                            System.out.printf("'Author' successfully changed from [%s] to [%s]", oldAuthor, newAuthor);
                            break;
                        }
                        case 'C':{
                            // Read in new ISBN for the chosen book
                            long oldISBN = myLibrary.getBookAtIndex(bookIndex).getISBN();
                            System.out.print("Enter a new ISBN for the book: ");
                            long newISBN = 0;
                            newISBN = userInput_Long();
                            System.out.println('\n');

                            // Update the author of chosen book
                            myLibrary.getBookAtIndex(bookIndex).setISBN(newISBN);

                            // Notify user of the change
                            System.out.printf("'ISBN' successfully changed from [%d] to [%d]", oldISBN, newISBN);
                            break;
                        }
                        case 'D':{
                            // Read in new numPages for the chosen book
                            int oldNumPages = myLibrary.getBookAtIndex(bookIndex).getNumPages();
                            System.out.print("Enter a new number of pages for the book: ");
                            int newNumPages = 0;
                            newNumPages = userInput_Int();
                            System.out.println('\n');

                            // Update the author of chosen book
                            myLibrary.getBookAtIndex(bookIndex).setNumPages(newNumPages);

                            // Notify user of the change
                            System.out.printf("'Number of pages' successfully changed from [%d] to [%d]", oldNumPages, newNumPages);
                            break;
                        }
                        case 'E':{
                            // Read in new yearRead for the chosen book
                            int oldYearRead = myLibrary.getBookAtIndex(bookIndex).getYearRead();
                            System.out.print("Enter a new 'year read' for the book: ");
                            int newYearRead = 0;
                            newYearRead = userInput_Int();
                            System.out.println('\n');

                            // Update the author of chosen book
                            myLibrary.getBookAtIndex(bookIndex).setYearRead(newYearRead);

                            // Notify user of the change
                            System.out.printf("'Year Read' successfully changed from [%d] to [%d]", oldYearRead, newYearRead);
                            break;
                        }
                        case 'F':{
                            // Read in new category for chosen book
                            String oldCategory = myLibrary.getBookAtIndex(bookIndex).getCategoryString();
                            System.out.print("Enter a new category for the book: ");
                            String newCategory = sc.nextLine();
                            System.out.println('\n');

                            // Update the author of chosen book
                            myLibrary.getBookAtIndex(bookIndex).setCategoryWithString(newCategory);

                            // Notify user of the change
                            System.out.printf("'Category' successfully changed from [%s] to [%s]", oldCategory, newCategory);
                            break;
                        }
                        case 'X':{
                            System.out.println("Exiting to the main menu");
                            break;
                        }
                    }

                    pause();
                    clearScreen();
                } while(subMenuOption != 'X');

                System.out.println();
                break;
            }
            case 'X':{
                System.out.println("Exiting Program..."); // Actual save occurs in the main do-while loop
                break;
            }
        }
        // To ensure neatness and clarity
        pause();
    }

    public static void clearScreen(){ // Extract method to a utility library
        // \033[H moves the cursor to the top-left corner
        // \033[2J clears the entire screen
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    private static void pause(){ // Extract method to a utility library
        System.out.println("Press Enter to continue...");
        sc.nextLine(); 
    }

    private static int userInput_Int(){ // Extract method to a utility library
        int int_Arg = 0;

        try{
            int_Arg = Integer.parseInt(sc.nextLine());
        }
        catch (NumberFormatException e){
            System.err.println("Invalid input type.\nExpected type: Integer.\n");
            System.out.print("Re-enter input: ");
            int_Arg = Integer.parseInt(sc.nextLine());
        }

        return int_Arg;
    }

    private static long userInput_Long(){// Extract method to a utility library
        long long_Arg = 0;

        try{
            long_Arg = Long.parseLong(sc.nextLine());
        }
        catch (NumberFormatException e){
            System.err.println("Invalid input type.\nExpected type: Long.\n");
            System.out.print("Re-enter input: ");
            long_Arg = Long.parseLong(sc.nextLine());
        }

        return long_Arg;
    }

    private static boolean containsAcceptedInput_char(char[] acceptedInputArray, char userInput){ // Extract method to a utility library
        for (char c : acceptedInputArray){
            if (c == userInput){
                return true;
            }
        }
        return false;
    }

    private static char userInput_menuChar(char[] acceptedInputArray){ // Extract method to a utility library
        char menuChar = Character.toUpperCase(sc.nextLine().charAt(0)); // Returns uppercase equivalent, if available. Otherwise, it returns the original value
        // Add validation for empty strings
        
        while (!containsAcceptedInput_char(acceptedInputArray, Character.toUpperCase(menuChar))){
            System.err.println("Invalid selection.");
            System.out.print("Re-enter menu option: ");
            menuChar = Character.toUpperCase(sc.nextLine().charAt(0));
        }

        return menuChar;
    }
}