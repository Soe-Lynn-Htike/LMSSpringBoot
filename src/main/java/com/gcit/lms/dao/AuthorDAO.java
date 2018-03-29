/**
 * 
 */
package com.gcit.lms.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.Author;
import com.gcit.lms.entity.Book;


/**
 * @author Aaron
 *
 */
@Component
public class AuthorDAO extends BaseDAO<Author> implements ResultSetExtractor<List<Author>> {
	
	public void createAuthor(Author author) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("INSERT INTO tbl_author(authorName) VALUES(?)", new Object[] { author.getAuthorName() });
	}

	public void saveBookAuthor(Author author, Book book) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("INSERT INTO tbl_book_authors VALUES (?, ?)",
				new Object[] { book.getBookId(), author.getAuthorId() });
	}

	public void updateAuthor(Author author) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("UPDATE  tbl_author SET authorName=? WHERE authorId=?",
				new Object[] { author.getAuthorName(), author.getAuthorId() });

	}

	public void deleteAuthor(Author author) throws ClassNotFoundException, SQLException {

		jdbcTemplate.update("DELETE  FROM tbl_author WHERE authorId =?", new Object[] { author.getAuthorId() });

	}

	public List<Author> readAuthors() throws ClassNotFoundException, SQLException {
		/*
		 * if(authorName !=null && !authorName.isEmpty()){ authorName =
		 * "%"+authorName+"%"; return
		 * readAll("select * from tbl_author where authorName like ?", new
		 * Object[]{authorName}); }else{ return readAll("select * from tbl_author",
		 * null); }
		 */
		return jdbcTemplate.query("select * from tbl_author", this);
	}
	
	public List<Author> readAuthorsByName(String searchString) throws ClassNotFoundException, SQLException {
		
		searchString = "%"+searchString+ "%";
		return jdbcTemplate.query("select * from tbl_author where authorName like ?",new Object[] {searchString}, this);
	}


	@Override
	public List<Author> extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<Author> authors = new ArrayList<>();
		while (rs.next()) {
			Author author = new Author();
			author.setAuthorId(rs.getInt("authorId"));
			author.setAuthorName(rs.getString("authorName"));
			authors.add(author);
		}
		return authors;
	}

}
