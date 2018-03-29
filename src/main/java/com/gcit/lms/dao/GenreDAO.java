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

import com.gcit.lms.entity.Genre;

/**
 * @author Aaron
 *
 */
@Component
public class GenreDAO extends BaseDAO<Genre> implements ResultSetExtractor<List<Genre>> {

	
	
	public void creatGenre(Genre g) throws ClassNotFoundException, SQLException { 
		jdbcTemplate.update("insert into tbl_genre(genre_name) values(?)", new Object[] {g.getGenre_id()});
	}
	public void updateGenre(Genre g) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("update tbl_genre set genre_name= ? where genre_id =?", new Object[] {g.getGenre_name(),g.getGenre_id()});
	}
	public void deleteGenre(Genre g) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("delete  from tbl_genre where genre_id=?",new Object[] {g.getGenre_id()});
	}
	public List<Genre> readGenres(String genreName) throws ClassNotFoundException,SQLException {
		if(genreName !=null && !genreName.isEmpty()){
			genreName = "%"+genreName+"%";
			return jdbcTemplate.query("select * from tbl_genre where genre_name like ?", new Object[]{genreName},this);
		}else{
			return jdbcTemplate.query("select * from tbl_genre", this);
		}
		
	}

	@Override
	public List<Genre> extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<Genre> genres = new ArrayList<>();
		while(rs.next()) {
			Genre g = new Genre();
			g.setGenre_id(rs.getInt("genre_id"));
			g.setGenre_name(rs.getString("genre_name"));
			genres.add(g);
		}
		return genres;
	}

}
