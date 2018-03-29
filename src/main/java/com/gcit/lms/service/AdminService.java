/**
 * 
 */
package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;

/**
 * @author Aaron
 *
 */
@RestController
public class AdminService extends BaseController {
	
	@Autowired
	 AuthorDAO adao;
	
	@Autowired
	 BookDAO bookdao;
	
	@Autowired
	GenreDAO genredao;
	
	@Autowired
	PublisherDAO publisherdao;
	
	@Autowired
	BookCopiesDAO bookCopiesdao;
	
	@Autowired
	BorrowerDAO borrowerdao;
	
	@Autowired
	BranchDAO branchdao;
	
	@Autowired
    BookLoanDAO bookloandao;
	
	//update author
	
	@RequestMapping(value="updateAuthor",method=RequestMethod.POST,consumes="application/json")
	@Transactional
	public void updateAuthor(@RequestBody Author author) throws SQLException {
		
		try {
			if (author.getAuthorId() != null && author.getAuthorName() != null) {
				adao.updateAuthor(author);
			} else if (author.getAuthorId() == null && author.getAuthorName()!= null) {
				adao.createAuthor(author);
			} else {
				adao.deleteAuthor(author);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); // log your stacktrace
			// display a meaningful user	
		}
	}
	
	//read author
	/*
	public void readAuthor(Author author) throws SQLException {
		
		try {

			adao.readAuthors(author.getAuthorName());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}*/
	
	@RequestMapping(value="readAuthors",method=RequestMethod.GET,produces="application/json")
	@Transactional
	public List<Author> readAllAuthors(){
		List<Author> authors = new ArrayList<>();
		try {
			authors = adao.readAuthors();
			for (Author a : authors) {
				a.setBooks(bookdao.readBooksByAuthorId(a));
			}
			return authors;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="readAuthorsByName/{searchString}",method=RequestMethod.GET,produces="application/json")
	@Transactional
	public List<Author> searchAuthorsByName(@PathVariable String searchString){
		List<Author> authors = new ArrayList<>();
		try {
			authors = adao.readAuthorsByName(searchString);
			for (Author a : authors) {
				a.setBooks(bookdao.readBooksByAuthorId(a));
			}
			return authors;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//update book
	
	@Transactional
	public void updateBook(Book book) throws SQLException {
		
		try {

			// PublisherDAO pdao = new PublisherDAO(conn);
			if (book.getBookId() != null && book.getTitle() != null) {
				bookdao.updateBook(book);

				// call update book genre
				// call update book author
			} else if (book.getBookId() == null && book.getTitle() != null) {
				// bdao.createBook(book);
				Integer bookId = bookdao.createBookWithPK(book);
				Book bookvalue = new Book();
				book.setBookId(bookId);
				book.setPublisherId(book.getPublisherId());
				// save in tbl_book_authors
				bookdao.saveBookAuthor(book);
				// save in tbl_book_genre
				bookdao.saveBookGenre(book);
				// add to book genre
				// add to publisher
			} else {
				bookdao.deleteBook(book);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@RequestMapping(value="readBooks",method=RequestMethod.GET,produces="application/json")
	@Transactional
	public List<Book> readBooks() throws SQLException {
		 try {
			return bookdao.readBooks();
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return null;
	}
	
	// read book by title
	@RequestMapping(value="readBooksByTitle/{searchTitle}",method=RequestMethod.GET,produces="application/json")
	@Transactional
	public List<Book> readBooksByTitle(@PathVariable String searchTitle) throws SQLException {
		 try {
			return bookdao.readBooksByTitle(searchTitle);
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return null;
	}
	
	// update Publisher
	@Transactional
	public void updatePublisher(Publisher publisher) throws SQLException {
	
		try {
			if (publisher.getPublisherId() != null && publisher.getPublisherName() != null
					&& publisher.getPublisherAddress() != null && publisher.getPublisherPhone() != null) {
				publisherdao.updatePublisher(publisher);
			} else if (publisher.getPublisherId() == null && publisher.getPublisherName() != null
					&& publisher.getPublisherAddress() != null && publisher.getPublisherPhone() != null) {
				publisherdao.createPublisher(publisher);
			} else {
				publisherdao.deletePublisher(publisher);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
		}
	}
	
	// read Publisher
	@Transactional
	public List<Publisher> readPublisher(Publisher publisher) throws SQLException{
		 
		try {
			return publisherdao.readPublishers(publisher.getPublisherName());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// update Genre
	
	@Transactional
	public void updateGenre(Genre genre) throws SQLException {
		try {
			if(genre.getGenre_id()!=null && genre.getGenre_name()!=null) {
				genredao.updateGenre(genre);
			}else if(genre.getGenre_id() == null && genre.getGenre_name() !=null) {
				genredao.creatGenre(genre);
			}else {
				genredao.deleteGenre(genre);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
	}
	
	// read Genre
	@Transactional
	public List<Genre> readGenre(Genre genre) throws SQLException{
	
		try {
			return genredao.readGenres(genre.getGenre_name());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	//update Borrower
	@Transactional
	public void updateBorrower(Borrower borrower) throws SQLException {
		try {
			if (borrower.getCardNo() != null && borrower.getName() != null && borrower.getAddress() != null
					&& borrower.getPhone() != null) {
				borrowerdao.updateBorrower(borrower);
			} else if (borrower.getCardNo() == null && borrower.getName() != null && borrower.getAddress() != null
					&& borrower.getPhone() != null) {
				borrowerdao.createBorrower(borrower);
			} else {
				borrowerdao.deleteBorrower(borrower);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
		}
	}
	
	// read Borrower
	@Transactional
	public List<Borrower> readBorrower(Borrower borrower) throws SQLException{
		try {
			return borrowerdao.readBorrowers(borrower);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// Library Branch
	@Transactional
	public void updateBranch(Branch branch) throws SQLException {
		try {
			if (branch.getBranchId() != null && branch.getBranchName() != null && branch.getBranchAddress() != null) {
				branchdao.updateBranch(branch);
			} else if (branch.getBranchId() == null && branch.getBranchName() != null
					&& branch.getBranchAddress() != null) {
				branchdao.createBranch(branch);
			} else {
				branchdao.deleteBranch(branch);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// read Branch
	@Transactional
	public List<Branch> readBranch(Branch branch) throws SQLException {
		try {
			return branchdao.readBranches(branch.getBranchName());
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//Book Loan
	@Transactional
	public void updateBookLoan(BookLoan bookLoan) throws SQLException {
		
		try {

			if (bookLoan.getBookId() != null && bookLoan.getBranchId() != null && bookLoan.getCardNo() != null
					&& bookLoan.getDateIn() == null) {
				bookloandao.creatBookLoan(bookLoan);
			} else if (bookLoan.getBookId() == null && bookLoan.getBranchId() == null && bookLoan.getCardNo() == null
					&& bookLoan.getDateIn() != null) {
				bookloandao.updateBookLoan(bookLoan);
			} else {
				bookloandao.deleteBookLoan(bookLoan);
			}

		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
	
	// override  Book Loan Due Date
	@Transactional
	public void overrideBookLoanDueDate(BookLoan bookLoan) throws SQLException{
		
		try {

			bookloandao.overrideDueDate(bookLoan);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();

		}
	}
	
	@Transactional
	public void updateBookLoanDueDate(BookLoan bookLoan) throws SQLException{
		
		try {

			bookloandao.updateBookLoanDueDate(bookLoan);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();

		}
	}

}
