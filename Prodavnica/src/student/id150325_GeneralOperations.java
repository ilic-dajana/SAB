package student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import jdbc2.DB;
import operations.GeneralOperations;

public class id150325_GeneralOperations implements GeneralOperations {	
	
	@Override
	public void setInitialTime(Calendar time) {
		Connection con = DB.getInstance().getConnection();
		
		String q = "{call Time_inital(?)}";
		try {
			CallableStatement cs = con.prepareCall(q);
			cs.setDate(1, new java.sql.Date(time.getTime().getTime()));
			cs.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		

	}

	@Override
	public Calendar time(int days) {
		Connection con = DB.getInstance().getConnection();
		Calendar cal = this.getCurrentTime();
		cal.add(Calendar.DAY_OF_YEAR, days);
		this.setInitialTime(cal);		
		return cal;
	}

	@Override
	public Calendar getCurrentTime() {
		Connection con = DB.getInstance().getConnection();
		String q = "SELECT current_time FROM Time_Operations WHERE IdGeneral = ?";
		try {
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, 1);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				Date date = rs.getDate(1);
				Calendar cal = new GregorianCalendar();
				cal.setTime(date);
				return cal;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public void eraseAll() {
		// TODO Auto-generated method stub

	}

}
