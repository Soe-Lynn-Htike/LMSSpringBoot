/**
 * 
 */
package com.gcit.lms.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author Aaron
 *
 */
public class BookCopies implements Serializable {
	
	private Integer bookId;
	private Integer branchId;
	private Integer noOfCopies;
	private List<Book> books;
	private List<Branch> branch;
	
	public Integer getBookId() {
		return bookId;
	}
	public void setBookId(Integer bookId) {
		this.bookId = bookId;
	}
	public Integer getBranchId() {
		return branchId;
	}
	public void setBranchId(Integer branchId) {
		this.branchId = branchId;
	}
	public Integer getNoOfCopies() {
		return noOfCopies;
	}
	public void setNoOfCopies(Integer noOfCopies) {
		this.noOfCopies = noOfCopies;
	}
	public List<Book> getBooks() {
		return books;
	}
	public void setBooks(List<Book> book) {
		this.books = book;
	}
	public List<Branch> getBranch() {
		return branch;
	}
	public void setBranch(List<Branch> branch) {
		this.branch = branch;
	}
	
	
	

}
