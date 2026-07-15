package book;

/**
 * Might need to create subclasses for specific kinds of books
 * Represents an individual book and its characteristics.
 * @author EB Mitoumba-Tindy
 * @version Version 1
 */
public class Book{
    // ----- Attributes -----
    private String title;
    private String author;
    private long ISBN; // 'long' is used for ISBN-13
    private int numPages;
    private int yearRead;
    private EBookCategory category;

    // ----- Constructors -----
    public Book(){}
    public Book(String title, String author, long ISBN, int numPages, int yearRead, EBookCategory category){
        setTitle(title);
        setAuthor(author);
        setISBN(ISBN);
        setNumPages(numPages);
        setYearRead(yearRead);
        setCategoryWithEnum(category);
    }
    public Book(String title, String author, long ISBN, int numPages, int yearRead, String category){
        setTitle(title);
        setAuthor(author);
        setISBN(ISBN);
        setNumPages(numPages);
        setYearRead(yearRead);
        setCategoryWithString(category);
    }

    // ----- Getters -----
    public String getTitle(){
        return title;
    }

    public String getAuthor(){
        return author;
    }

    public long getISBN(){
        return ISBN;
    }

    public int getNumPages(){
        return numPages;
    }

    public int getYearRead(){
        return yearRead;
    }

    public EBookCategory getCategoryEnum(){
        return category;
    }

    public String getCategoryString(){
        EBookCategory categoryEnum = getCategoryEnum();
        String categoryString = null;
        
        switch(categoryEnum){
            case EDUCATIONAL:{
                categoryString = "Educational";
                break;
            }
            case RECREATIONAL:{
                categoryString = "Recreational";
                break;
            }
        }
        return categoryString;
    }

    // ----- Setters -----
    public void setTitle(String title){
        this.title = title;
    }

    public void setAuthor(String author){
        this.author = author;
    }

    public void setISBN(long ISBN){
        this.ISBN = ISBN;
    }
    
    public void setNumPages(int numPages){
        this.numPages = numPages;
    }

    public void setYearRead(int yearRead){
        this.yearRead = yearRead;
    }

    public void setCategoryWithEnum(EBookCategory category){
        this.category = category;
    }

    public void setCategoryWithString(String category){
        switch(category){
            case "Educational":{
                setCategoryWithEnum(EBookCategory.EDUCATIONAL);
                break;
            }
            case "Recreational":{
                setCategoryWithEnum(EBookCategory.RECREATIONAL);
                break;
            }
        }
    }

    // ----- Methods -----
    // Default sorting logic may need to be defined here, as an override of CompareTo. Then call Collections.sort on the array
}