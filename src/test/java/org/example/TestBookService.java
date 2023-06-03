package org.example;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)

public class TestBookService {
    @Mock
    private List<Book> bookDatabase;
    @InjectMocks
    private BookService bookService;
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        System.out.println("Start test for method with @Before");
    }
    @BeforeClass
    public static void setClass() {
        System.out.println("Set up @BeforeClass");
    }
    @AfterClass
    public static void tearDownClass() {
        System.out.println("Compile class after testing with @AfterClass");
    }
    @After
    public void tearDown() {
        System.out.println("Execute clean up op. after each test with @After");
    }
    //test searchBook()
    @Test
    public void searchBookTitleMatch() {
        Book book1 = new Book("Title1", "Author1", "Genre1", 15.50);
        Book book2 = new Book("2", "Author2", "Fantasy", 13.95);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookDatabase.stream()).thenReturn(books.stream());          //set up mock behavior for book database
        List<Book> results = bookService.searchBook("Title1");   //search for book by title
        assertEquals(1, results.size());                        //verify search results
        assertTrue(results.contains(book1));
    }

    @Test
    public void searchBookAuthorMatch() {
        Book book1 = new Book("Title1", "Author1", "Genre1", 15.50);
        Book book2 = new Book("Title2", "Author2", "Fantasy", 13.95);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookDatabase.stream()).thenReturn(books.stream());           //set up mock behavior for the book database
        List<Book> results = bookService.searchBook("Author2");  //search for book by author
        assertEquals(1, results.size());                         //verify search results
        assertTrue(results.contains(book2));
    }
    @Test
    public void searchBookGenreMatch() {
        Book book1 = new Book("Title1", "Author1", "Genre1", 15.50);
        Book book2 = new Book("Title2", "Author2", "Fantasy", 13.95);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookDatabase.stream()).thenReturn(books.stream());         //set up mock behavior for book database
        List<Book> results = bookService.searchBook("Genre1"); //search for book by genre
        assertEquals(1, results.size());                       //verify search results
        assertTrue(results.contains(book1));
    }
    @Test
    public void searchBookNoMatch() {
        Book book1 = new Book("Title1", "Author1", "Genre1", 15.50);
        Book book2 = new Book("Title2", "Author2", "Fantasy", 13.95);
        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        when(bookDatabase.stream()).thenReturn(books.stream());                                      //set up mock behavior for book database
        List<Book> searchResult = bookService.searchBook("A Court Of Thrones And Roses");   //search for book with no match
        assertTrue(searchResult.isEmpty());                                                          //verify search result empty
    }
    //test purchaseBook()
    @Test
    public void purchaseBookExists() {
        Book book1 = new Book("Title1", "Author1", "Genre1");
        User user = new User("email", "password", "email@yahoo.com");
        when(bookDatabase.contains(book1)).thenReturn(true);    //set up mock behavior for book database
        boolean result = bookService.purchaseBook(user, book1);    //make book purchase
        assertTrue(result);                                        //verify successful purchase
    }
    @Test
    public void purchaseBookDoesNotExist() {
        Book book1 = new Book("Title1", "Author1", "Genre1", 15.50);
        User user = new User("email", "password", "email@yahoo.com");
        when(bookDatabase.contains(book1)).thenReturn(false);   //set up mock behavior for the book database
        boolean result = bookService.purchaseBook(user, book1);    //make book purchase
        assertFalse(result);                                       //verify that the purchase was unsuccessful
    }
    @Test
    public void purchaseBookInsufficientBalance() {
        Book book1 = new Book("Title1", "Author1", "Genre1", 15.50);
        User user = new User("email", "password", "email@yahoo.com", 15.30);
        when(bookDatabase.contains(book1)).thenReturn(true);    //set up mock behavior for the book database
        boolean result = bookService.purchaseBook(user, book1);    //make book purchase
        assertFalse(result);                                       //verify purchase was unsuccessful
    }
    //test addBook()
    @Test
    public void addBookPositive() {
        Book book1 = new Book("Title1", "Author1", "Genre1", 15.50);
        when(bookDatabase.contains(book1)).thenReturn(false);  //mock behavior for book database
        boolean result = bookService.addBook(book1);             //add book
        assertTrue(result);                                      //verify the book add is successful
        verify(bookDatabase).add(book1);
    }
    @Test
    public void addBookNegativeBookInDatabaseAlready() {
        Book book1 = new Book("Title1", "Author1", "Genre1", 15.50);
        when(bookDatabase.contains(book1)).thenReturn(true); //set up mock behavior in book database
        boolean result = bookService.addBook(book1);           //add book
        assertFalse(result);                                   //verify add book was unsuccessful
        verify(bookDatabase, never()).add(book1);
    }
    @Test
    public void addBookEdge() {
        Book book1 = new Book("Title1", "Author1", "Genre1", 15.50);
        when(bookDatabase.contains(book1)).thenReturn(true); //set up mock behavior in book database
        boolean result = bookService.addBook(book1);           //add book
        assertFalse(result);                                   //verify book was not successful
        verify(bookDatabase, never()).add(book1);

        // Test adding a new book to the empty database
        Book book2 = new Book("Title2", "Author2", "Fantasy", 13.95);
        when(bookDatabase.contains(book2)).thenReturn(false);  //set up mock behavior in book database
        boolean result2 = bookService.addBook(book2);             //add book
        assertTrue(result2);                                      //verify book add was successful
        verify(bookDatabase).add(book2);
    }
    //test removeBook()
    @Test
    public void removeBookPositive() {
        Book book1 = new Book("Title1", "Author1", "Genre1", 15.50);
        when(bookDatabase.remove(book1)).thenReturn(true);  //set up mock behavior in book database
        boolean result = bookService.removeBook(book1);        //remove book
        assertTrue(result);                                    //verify book removal was successful
        verify(bookDatabase).remove(book1);
    }
    @Test
    public void removeBookNegativeBookNotInDatabase() {
        Book book1 = new Book("Title1", "Author1", "Genre1", 15.50);
        when(bookDatabase.remove(book1)).thenReturn(false); //set up mock behavior in book database
        boolean result = bookService.removeBook(book1);        //remove book
        assertFalse(result);                                   //verify book removal wasn't successful
        verify(bookDatabase).remove(book1);
    }
    @Test
    public void removeBookEdge() {
        // Test removing a book that is not in the database
        Book book1 = new Book("Title1", "Author1", "Genre1", 15.50);
        when(bookDatabase.remove(book1)).thenReturn(false);       //set up mock behavior in book database
        boolean result = bookService.removeBook(book1);              //remove book
        assertFalse(result);                                         //verify book removal was unsuccessful
        verify(bookDatabase).remove(book1);

        // Test removing a book that is in the database
        Book book2 = new Book("Title2", "Author2", "Fantasy", 13.95);
        when(bookDatabase.remove(book2)).thenReturn(true);  //set up mock behavior in book database
        boolean result2 = bookService.removeBook(book2);       //remove book
        assertTrue(result2);                                   //verify book removal was successful
        verify(bookDatabase).remove(book2);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Ignore
    @Test
    public void addBookReviewPositive() {
        List<Book> books = new ArrayList<>();
        Book book = new Book("Title1", "Author1", "Genre1", 15.50);
        books.add(book);
        User user = new User("email", "password", "email@yahoo.com", books);
        user.purchaseBook(book);
        String review = "Great book!";
        boolean result = bookService.addBookReview(user, book, review); //add book review
        assertTrue(result);                                             //verify adding review was successful
        assertTrue(book.getReviews().contains(review));
    }
    @Test
    public void addBookReviewNegativeUserHasNotPurchasedBook() {
        List<Book> books = new ArrayList<>();
        Book book = new Book("Title1", "Author1", "Genre1", 15.50);
        User user = new User("email", "password", "email@yahoo.com", books);
        String review = "";
        boolean result = bookService.addBookReview(user, book, review); //add book review
        assertFalse(result);                                            //verify review add was unsuccessful
        assertFalse(book.getReviews().contains(review));
    }
    @Test
    public void addBookReviewEdgeCase() {
        Book book = new Book("Title1", "Author1", "Genre1", 15.50);
        User user = new User("email", "password", "email@yahoo.com", 50.34);
        String review = "bueno";
        boolean result = bookService.addBookReview(user, book, review); //add book review
        assertFalse(result);                                            //verify review add is unsuccessful
        assertFalse(book.getReviews().contains(review));

        // Purchase the book and try adding the review again
        user.purchaseBook(book);                                            //make sure user purchases book
        boolean result2 = bookService.addBookReview(user, book, review);    //add review again after buying
        assertTrue(result2);                                                 //verify review add was successful
        assertTrue(book.getReviews().contains(review));
    }
}
