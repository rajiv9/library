package edu.sjsu.cmpe.library.repository;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import edu.sjsu.cmpe.library.domain.Author;
import edu.sjsu.cmpe.library.domain.Book;
import edu.sjsu.cmpe.library.domain.Review;

public class BookRepository implements BookRepositoryInterface {
    /** In-memory map to store books. (Key, Value) -> (ISBN, Book) */
    private final ConcurrentHashMap<Long, Book> bookInMemoryMap;
    //private final AuthorRepositoryInterface authorRepository;
    //private final ReviewRepositoryInterface reviewRepository;
    
    /** Never access this key directly; instead use generateISBNKey() */
    private long isbnKey;
    
    public BookRepository(ConcurrentHashMap<Long, Book> bookMap/*, 
    						AuthorRepositoryInterface authorRepository,
    						ReviewRepositoryInterface reviewRepository*/) {
	checkNotNull(bookMap, "bookMap must not be null for BookRepository");
	bookInMemoryMap = bookMap;
	isbnKey = 0;
	//this.authorRepository = authorRepository;
	//this.reviewRepository = reviewRepository;
    }

    /**
     * This should be called if and only if you are adding new books to the
     * repository.
     * 
     * @return a new incremental ISBN number
     */
    private final Long generateISBNKey() {
	// increment existing isbnKey and return the new value
	return Long.valueOf(++isbnKey);
    }
    

    /**
     * This will auto-generate unique ISBN for new books.
     */
    @Override
    public Book saveBook(Book newBook) {
	checkNotNull(newBook, "newBook instance must not be null");
	// Generate new ISBN
	Long isbn = generateISBNKey();
	newBook.setIsbn(isbn);
	// TODO: create and associate other fields such as author
	/*List<Author> authors = newBook.getAuthors();
	newBook.setAuthors(authorRepository.generateAuthorIdKey(authors));*/

	// Finally, save the new book into the map
	bookInMemoryMap.putIfAbsent(isbn, newBook);
	return newBook;
    }

    /**
     * @see edu.sjsu.cmpe.library.repository.BookRepositoryInterface#getBookByISBN(java.lang.Long)
     */
    @Override
    public Book getBookByISBN(Long isbn) {
	checkArgument(isbn > 0,
		"ISBN was %s but expected greater than zero value", isbn);
	return bookInMemoryMap.get(isbn);
    }
    
    public boolean deleteBookByISBN(Long isbn) {
    	//checkNotNull(newBook, "newBook instance must not be null");
    	if (bookInMemoryMap.containsKey(isbn)) {
    		bookInMemoryMap.remove(isbn);
    		return true;
    	}
    	else
    		return false;
    	
    }
    
    public boolean updateBookStatusByISBN(Long isbn, String status){
    	Book book = new Book();
    	if (bookInMemoryMap.containsKey(isbn)) {
    		book = bookInMemoryMap.get(isbn);
    		book.setStatus(status);
    		bookInMemoryMap.replace(isbn, book);
    		return true;
    	}
    	
    	return false;
    	}

/*    public Book saveReviews(Book newBook) {
    	List<Review> reviews = newBook.getReviews();
    	newBook.setReviews(reviewRepository.generateReviewIdKey(reviews));
    	return newBook;
    	
    }*/
	/* (non-Javadoc)
	 * @see edu.sjsu.cmpe.library.repository.BookRepositoryInterface#saveReview(java.lang.Long, edu.sjsu.cmpe.library.domain.Review)
	 */
/*	@Override
	public Review saveReview(Long long1, Review review) {
		// TODO Auto-generated method stub
		return null;
	}
*/
}
