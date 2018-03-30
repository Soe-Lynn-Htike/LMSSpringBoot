/**
 * 
 */
package com.gcit.lms.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.gcit.lms.dao.AuthorDAO;
import com.gcit.lms.dao.BookCopiesDAO;
import com.gcit.lms.dao.BookDAO;
import com.gcit.lms.dao.BookLoanDAO;
import com.gcit.lms.dao.BorrowerDAO;
import com.gcit.lms.dao.BranchDAO;
import com.gcit.lms.dao.GenreDAO;
import com.gcit.lms.dao.PublisherDAO;
import com.gcit.lms.entity.Book;
import com.gcit.lms.entity.BookCopies;
import com.gcit.lms.entity.BookLoan;
import com.gcit.lms.entity.Borrower;
import com.gcit.lms.entity.Branch;
//import com.gcit.lms.jdbc.JDBCDemo;

/**
 * @author Aaron
 *
 */
public class BorrowerService {

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
	
	
	@Transactional
	public List<Branch> readBranch(String branchname) throws SQLException {

		try {
			return branchdao.readBranches(branchname);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	/*@Transactional
	public List<Book> readBooksCheckOut(Branch branch) throws SQLException {
		
		try {

			return bookdao.readBooksByBranch(branch);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	@Transactional
	public void checkOutBook(BookLoan bookLoan, BookCopies bookCopies) throws SQLException {

		try {

			bookloandao.creatBookLoan(bookLoan);
			bookCopiesdao.checkOutBookCopies(bookCopies);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	/*@Transactional
	public Borrower checkBorrowerByCardNo(Borrower borrower) throws ClassNotFoundException, SQLException {
		try {

			return borrowerdao.readBorrowersByCardNo(borrower);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}*/
	
	@Transactional
	public void returnBook(BookLoan bookLoan, BookCopies bookCopies) throws SQLException {
		
		try {
			bookloandao.updateBookLoanDateIn(bookLoan);
			bookCopiesdao.returnBookCopies(bookCopies);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Transactional
	public List<Book> readBooksReturn (Branch branch, Borrower borrower) throws SQLException {

		try {

			return bookdao.readBooksByBorrower(branch, borrower);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
