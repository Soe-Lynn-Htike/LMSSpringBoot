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
import com.gcit.lms.entity.Branch;
//import com.gcit.lms.jdbc.JDBCDemo;

/**
 * @author Aaron
 *
 */
public class LibrianService {

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
	public List<Branch> readBranches(String input) throws SQLException {
		try {
			return branchdao.readBranches(input);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	@Transactional
	public void updateBranch(Branch branch) throws SQLException {
		
		try {
			if (branch.getBranchAddress().equals("N/A") && branch.getBranchName() != "N/A") {
				branchdao.updateBranchByName(branch);
			} else if (branch.getBranchName().equals("N/A") && branch.getBranchAddress() != "N/A") {
				branchdao.updateBranchByAddress(branch);
			} else {
				branchdao.updateBranch(branch);
			}
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Transactional
	public List<Book> readBooks(Branch branch) throws SQLException{
		 
		try {

			return bookdao.readBooksByBranch(branch);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@Transactional
	public void updateNoOfCopies(BookCopies bookcopies) throws SQLException {
		
		try {
			bookCopiesdao.updateBookCopies(bookcopies);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Transactional
	public BookCopies showNoOfCopies(BookCopies bookCopies) throws SQLException {
		
		try {
			return bookCopiesdao.getBookCopiesbyBranchbyBook(bookCopies);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
