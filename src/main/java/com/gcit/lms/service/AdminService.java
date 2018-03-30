/**
 * 
 */
package com.gcit.lms.service;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


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
			if (book.getBookId() != null && book.getTitle() != null) {
				bookdao.updateBook(book);

			} else if (book.getBookId() == null && book.getTitle() != null) {
				// bdao.createBook(book);
				Integer bookId = bookdao.createBookWithPK(book);
				book.setBookId(bookId);
				book.setPublisherId(book.getPublisherId());
				bookdao.saveBookAuthor(book);
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
		List<Book> books = new ArrayList<>();
		 try {
			 
			 books = bookdao.readBooks("");
			 for(Book b : books) {
				 b.setAuthors(adao.readAuthorsByBookId(b));
				 b.setGenres(genredao.getGenresByBookId(b));
				 b.setPublisher(publisherdao.getPublierbyBookId(b));
				 b.setBookcopies(bookCopiesdao.getBookCopiesByBookId(b));
			 }
			 
			 return books;
			
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
		List<Book> books = new ArrayList<>(); 
		try {
			 books =  bookdao.readBooks(searchTitle);
			 for(Book book : books) {
				 book.setAuthors(adao.readAuthorsByBookId(book));
				 book.setGenres(genredao.getGenresByBookId(book));
				 book.setPublisher(publisherdao.getPublierbyBookId(book));
				 book.setBookcopies(bookCopiesdao.getBookCopiesByBookId(book));
			 }
			 return books;
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 return null;
	}
	
	// update Publisher
	@RequestMapping(value="updatePublisher",method=RequestMethod.POST,consumes="application/json")
	@Transactional
	public void updatePublisher(@RequestBody  Publisher publisher) throws SQLException {
	
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
	@RequestMapping(value="readPublishers",method=RequestMethod.GET,produces="application/json")
	@Transactional
	public List<Publisher> readPublisher() throws SQLException{
		List<Publisher> publishers = new ArrayList<>();
		try {
			publishers = publisherdao.readPublishers("");
			for (Publisher p : publishers) {
				p.setBooks(bookdao.readBooksByPublisherId(p));
			}
			return publishers;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="readPublisherByName/{searchPublisher}",method=RequestMethod.GET,produces="application/json")
	@Transactional
	public List<Publisher> readPublisher(@PathVariable String searchPublisher) throws SQLException{
		List<Publisher> publishers = new ArrayList<>();
		try {
			publishers =publisherdao.readPublishers(searchPublisher);
			for (Publisher p : publishers) {
				p.setBooks(bookdao.readBooksByPublisherId(p));
			}
			return publishers;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	// update Genre
	@RequestMapping(value="updateGenre",method=RequestMethod.POST,consumes="application/json")
	@Transactional
	public void updateGenre(@RequestBody Genre genre) throws SQLException {
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
	@RequestMapping(value="readGenres",method=RequestMethod.GET,produces="application/json")
	public List<Genre> readGenres() throws SQLException {
			List<Genre> genres = new ArrayList<>();
		try {
				genres = genredao.readGenres("");
				for(Genre g : genres) {
					g.setBooks(bookdao.readBooksByGenreId(g));
				}
				return genres;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	@RequestMapping(value="readGenreByName/{searchGenre}",method=RequestMethod.GET,produces="application/json")
	@Transactional
	public List<Genre> readGenreByName(@PathVariable String searchGenre) throws SQLException {
		List<Genre> genres = new ArrayList<>();
		try {
			genres =genredao.readGenres(searchGenre);
			for (Genre g : genres) {
				g.setBooks(bookdao.readBooksByGenreId(g));
			}
			return genres;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
	
	
	//update Borrower
	@RequestMapping(value="updateBorrower",method=RequestMethod.POST,consumes="application/json")
	@Transactional
	public void updateBorrower(@RequestBody Borrower borrower) throws SQLException {
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
	@RequestMapping(value="readBorrowers",method=RequestMethod.GET,produces="application/json")
	@Transactional
	public List<Borrower> readBorrower() throws SQLException{
		List<Borrower> borrowers = new ArrayList<>();
		try {
			     borrowers =  borrowerdao.readBorrowers("");
			     for(Borrower b : borrowers) {
			    	 b.setBookLoans(bookloandao.getBookLoansByCardNo(b));
			     }
			     return borrowers;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping(value="readBorrowerByCardNo/{cardNo}",method=RequestMethod.GET,produces="application/json")
	@Transactional
	public Borrower readBorrowerByCardNo(@PathVariable String cardNo) throws SQLException{
		Borrower borrower = new Borrower();
		try {
			borrower = borrowerdao.readBorrowersByCardNo(Integer.parseInt(cardNo));
			borrower.setBookLoans(bookloandao.getBookLoansByCardNo(borrower));
			return borrower;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	@RequestMapping(value="readBorrowerByName/{searchName}",method=RequestMethod.GET,produces="application/json")
	@Transactional
	public List<Borrower> readBorrowerByName(@PathVariable String searchName) throws SQLException{
		List<Borrower> borrwers = new ArrayList<>();
		try {
			borrwers = borrowerdao.readBorrowers(searchName);
			for (Borrower b : borrwers) {
				b.setBookLoans(bookloandao.getBookLoansByCardNo(b));
			}
			return borrwers;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	// Library Branch
	@RequestMapping(value="updateBranch",method=RequestMethod.POST,consumes="application/json")
	@Transactional
	public void updateBranch(@RequestBody Branch branch) throws SQLException {
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
	@RequestMapping(value="readBranches",method=RequestMethod.GET,produces="application/json")
	@Transactional
	public List<Branch> readBranches() throws SQLException {

		List<Branch> branches = new ArrayList<>();
		try {
			branches = branchdao.readBranches("");
			for (Branch branch : branches) {
				branch.setBookcopies(bookCopiesdao.getBookCopiesByBranch(branch));
			}
			return branches;
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value="readBranchByName/{searchBranchName}",method=RequestMethod.GET,produces="application/json")
	@Transactional
	public List<Branch> readBranchByName(@PathVariable String searchBranchName) throws SQLException {
		List<Branch> branches = new ArrayList<>();
		try {
			branches = branchdao.readBranches("");
			for (Branch branch : branches) {
				branch.setBookcopies(bookCopiesdao.getBookCopiesByBranch(branch));
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	//Book Loan
	@RequestMapping(value="updateBookLoan",method=RequestMethod.POST,consumes="application/json")
	@Transactional
	public void updateBookLoan(@RequestBody BookLoan bookLoan) throws SQLException {
		
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
	@RequestMapping(value="overrideBookLoanDueDate",method=RequestMethod.POST,consumes="application/json")
	@Transactional
	public void overrideBookLoanDueDate(@RequestBody BookLoan bookLoan) throws SQLException{

		try {

			bookloandao.overrideDueDate(bookLoan);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();

		}
	}
	
	@RequestMapping(value="updateBookLoanDueDate",method=RequestMethod.POST,consumes="application/json")
	@Transactional
	public void updateBookLoanDueDate(@RequestBody BookLoan bookLoan) throws SQLException{

		try {

			bookloandao.updateBookLoanDueDate(bookLoan);

		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();

		}
	}

}
