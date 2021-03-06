/**
 * 
 */
package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.BookCopies;

/**
 * @author Aaron
 *
 */
@Component
public class BookCopiesDAO extends BaseDAO<BookCopies> implements ResultSetExtractor<List<BookCopies>> {

	
	
	public void createBookCopies(BookCopies bookcopies) throws ClassNotFoundException, SQLException {
		
		jdbcTemplate.update("insert into tbl_book_copies(bookId,branchId,noOfCopies) values(?,?,?)",new Object[] {bookcopies.getBookId(),bookcopies.getBranchId(),bookcopies.getNoOfCopies()});
	}
	public void updateBookCopies(BookCopies bookcopies) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("update tbl_book_copies set noOfCopies=? where bookId=? AND branchId=?", new Object[]{bookcopies.getNoOfCopies(),bookcopies.getBookId(),bookcopies.getBranchId()});	
	}
	public void deleteBookCopies(BookCopies bookcopies) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("delete from tbl_book_copies where bookId=? AND branchId=?",new Object[] {bookcopies.getBookId(),bookcopies.getBranchId()});
	}
	public List<BookCopies> readBookCopies() throws ClassNotFoundException, SQLException{
		return jdbcTemplate.query("select * from tbl_book_copies",this);
	}
	
	public List<BookCopies> readbyBranch(BookCopies bookCopies) throws ClassNotFoundException, SQLException {
		return jdbcTemplate.query("select * from tbl_book_copies where branchId=?", new Object[] {bookCopies.getBookId()},this);
	}
	public BookCopies getBookCopiesbyBranchbyBook(BookCopies bookCopies) throws ClassNotFoundException, SQLException {
		List<BookCopies> books = jdbcTemplate.query("select * FROM tbl_book_copies where branchId = ? and bookId = ?", new Object[] { bookCopies.getBranchId(), bookCopies.getBookId() },this) ;
		
		if(books != null)
		{
			return books.get(0);
		}
		return null;
	} 
	public void checkOutBookCopies(BookCopies bookCopies) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("update tbl_book_copies set noOfCopies = noOfCopies-1 where bookId = ? and branchId = ?",new Object[] { bookCopies.getBookId(), bookCopies.getBranchId() });		
	}
	
	public void returnBookCopies(BookCopies bookCopies) throws ClassNotFoundException,SQLException {
		jdbcTemplate.update("update tbl_book_copies set noOfCopies = noOfCopies+1 where bookId = ? and branchId = ?",new Object[] { bookCopies.getBookId(), bookCopies.getBranchId() });		
	}
	/*public BookCopies readOne(int bookId, int branchId) throws Exception { 
 		List<BookCopies> bookCopies = (List<BookCopies>) readAll("select * from tbl_book_copies where bookId = ? " 
 				+ "and branchId = ?", new Object[] {bookId, branchId}); 
 		if(bookCopies!=null && bookCopies.size()>0){ 
 			return bookCopies.get(0); 
 		} 
 		return null; 
	} */
	
	@Override
	public List<BookCopies> extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<BookCopies> bookcopies = new ArrayList<>();
		
		while(rs.next()) {
			BookCopies bc = new BookCopies();
			bc.setBookId(rs.getInt("bookId"));
		    bc.setBranchId(rs.getInt("branchId"));
		    bc.setNoOfCopies(rs.getInt("noOfCopies"));
		    bookcopies.add(bc);
		}
		return bookcopies;
	}
}
