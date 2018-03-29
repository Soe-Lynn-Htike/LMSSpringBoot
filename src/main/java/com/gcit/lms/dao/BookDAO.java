/**
 * 
 */
package com.gcit.lms.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;
import com.gcit.lms.entity.Genre;
import com.gcit.lms.entity.Publisher;


/**
 * @Book Aaron
 *
 */
@Component
public class BookDAO extends BaseDAO<Book> implements ResultSetExtractor<List<Book>> {
	
	public void createBook(Book book) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("insert  into tbl_book (title) values(?)", new Object[] { book.getTitle() });
	}

	public Integer createBookWithPK(Book book) throws ClassNotFoundException, SQLException {

		// return createWithPK("insert int tbl_book (title) values(?)", new Object[]
		// {book.getTitle()});
		String insertSql = "insert int tbl_book (title) values(?)";
		// this is the key holder
		GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

		// the name of the generated column (you can track more than one column)
		String id_column = "ID";

		// the update method takes an implementation of PreparedStatementCreator which
		// could be a lambda
		jdbcTemplate.update(con -> {
			PreparedStatement ps = con.prepareStatement(insertSql, new String[] { id_column });
			ps.setString(1, book.getTitle());
			return ps;
		}, keyHolder);

		// after the update executed we can now get the value of the generated ID
		BigDecimal id = (BigDecimal) keyHolder.getKeys().get(id_column);
		return id.intValue();

	}

	public void saveBookAuthor(Book book) throws ClassNotFoundException, SQLException {
		for (Author a : book.getAuthors()) {
			jdbcTemplate.update("insert into tbl_book_authors VALUES (?, ?)",
					new Object[] { book.getBookId(), a.getAuthorId() });
		}
	}

	public void saveBookGenre(Book book) throws ClassNotFoundException, SQLException {
		for (Genre genre : book.getGenres()) {
			jdbcTemplate.update("insert into tbl_book_genres VALUES (?, ?)",
					new Object[] { genre.getGenre_id(), book.getBookId() });
		}
	}

	public void updateBook(Book book) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("update  tbl_book set bookTitle=? where bookId=?",
				new Object[] { book.getTitle(), book.getBookId() });
	}

	public void deleteBook(Book book) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("delete tbl_book from where bookId = ?", new Object[] { book.getBookId() });
	}

	public List<Book> readBooksByBorrower(Branch branch, Borrower borrower)
			throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query(
				"select * from tbl_book where bookId IN (select bookId from tbl_book_loans where branchId =? and cardNo =?)",
				new Object[] { branch.getBranchId(), borrower.getCardNo() }, this);
	}


	public List<Book> readBooks(String bookTitle) throws ClassNotFoundException, SQLException {
		if(bookTitle !=null && !bookTitle.isEmpty()) {
			bookTitle = "%" + bookTitle + "%";
			return jdbcTemplate.query("select * from tbl_book where title like ?", new Object[] { bookTitle }, this);
		}else {
			return jdbcTemplate.query("select * from tbl_book", this);
		}
		
	}
	
	public List<Book> readBooksByAuthorId(Author author) throws ClassNotFoundException, SQLException {

		return jdbcTemplate.query(
				"select * from tbl_book where bookId IN(select bookId from tbl_book_authors where authorId=? )",
				new Object[] { author.getAuthorId() }, this);
	}

	public List<Book> readBooksByGenreId(Author author) throws ClassNotFoundException, SQLException {

		return jdbcTemplate.query(
				"select * from tbl_book where bookId IN(select bookId from tbl_book_genres where genre_id=?)",
				new Object[] { author.getAuthorId() }, this);
	}

	public List<Book> readBooksByBranch(Branch branch) throws ClassNotFoundException, SQLException {

		return jdbcTemplate.query(
				"select * from tbl_book b where Exists(select null from tbl_book_copies bc where b.bookId = bc.bookId and bc.noOfCopies >1 and bc.branchId=? )",
				new Object[] { branch.getBranchId() }, this);
	}

	public Book getBookByPK(Book book) {

		List<Book> books = jdbcTemplate.query("select * from tbl_book where bookId=?",
				new Object[] { book.getBookId() }, this);
		if (books != null) {
			return books.get(0);
		}

		return null;

	}

	@Override
	public List<Book> extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<Book> books = new ArrayList<>();
		while (rs.next()) {
			Book book = new Book();
			book.setBookId(rs.getInt("bookId"));
			book.setTitle(rs.getString("title"));
			books.add(book);
		}
		return books;
	}

}
