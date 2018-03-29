/**
 * 
 */
package com.gcit.lms.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.gcit.lms.entity.Borrower;


/**
 * @author Aaron
 *
 */
@Component
public class BorrowerDAO extends BaseDAO<Borrower> implements ResultSetExtractor<List<Borrower>> {

	
	public void createBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("insert into tbl_borrower (name,address,phone) VALUES (?,?,?)", new Object[] { borrower.getName(),borrower.getAddress(),borrower.getPhone() });
	}
	public void updateBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("update tbl_borrower set name=?, address=?, phone=? where cardNo=?",new Object[] {borrower.getName(),borrower.getAddress(),borrower.getPhone()});
	}
	public void deleteBorrower(Borrower borrower) throws ClassNotFoundException, SQLException {
		jdbcTemplate.update("delete  from tbl_borrower where cardNo=?",new Object[] {borrower.getCardNo()});
	}
	public Borrower readBorrowersByCardNo(Borrower borrower ) throws ClassNotFoundException, SQLException{
		List<Borrower> borrowers = jdbcTemplate.query("select * from tbl_borrower where cardNo=?", new Object[] {borrower.getCardNo()},this);
		if(borrowers != null)
		{
			return borrowers.get(0);
		}
		return null;
		// ask for that error
		/*Borrower a = new Borrower();
		ConnectionUtilities conUtil = new ConnectionUtilities();
		Connection conn = conUtil.getConnection();
		PreparedStatement stmt=conn.prepareStatement("select * from tbl_borrower where cardNo=?");
		stmt.setInt(1,borrower.getCardNo());
		ResultSet rs = stmt.executeQuery();
		// check empty or not for result 
		if(!rs.isBeforeFirst()) {
			return null;
		}
		else {
			while(rs.next()){
				a.setCardNo(rs.getInt("cardNo"));
				a.setName(rs.getString("name"));
				a.setAddress(rs.getString("address"));
				a.setPhone(rs.getString("Phone"));
			}
			return a;
		}*/
		// if(rs.next())
		
	}
	public List<Borrower> readBorrowers(Borrower borrower) throws ClassNotFoundException, SQLException{
		String name ;
		if(borrower.getName() !=null && !borrower.getName().isEmpty()){
			name = "%"+borrower.getName()+"%";
			return jdbcTemplate.query("select * from tbl_borrower where name like ?", new Object[]{name},this);
		}else{
			return jdbcTemplate.query("select * from tbl_borrower", this);
		}
		
	}
	@Override
	public List<Borrower> extractData(ResultSet rs) throws SQLException {
		// TODO Auto-generated method stub
		List<Borrower> borrowers = new ArrayList<>();
		if (rs.next()) {
			
			while (rs.next()) {
				Borrower a = new Borrower();
				a.setCardNo(rs.getInt("cardNo"));
				a.setName(rs.getString("name"));
				a.setAddress(rs.getString("address"));
				borrowers.add(a);
			}
			
		}
		// check error for cardno one
		return borrowers;
	}
}
