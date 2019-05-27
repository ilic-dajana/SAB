package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdbc2.DB;
import operations.BuyerOperations;

public class id150325_BuyerOperations implements BuyerOperations {

	@Override
	public int createBuyer(String name, int cityId) {
		Connection con = DB.getInstance().getConnection();
		try{
			String q = "SELECT IdGrad FROM Grad WHERE IdGrad= ?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, cityId);
			ResultSet rs = ps.executeQuery();
			if(!rs.next())
				return -1;
			
			q = "INSERT INTO Kupac(Ime, novac, IdGrad)VALUES (?, ? , ?) ";
			ps = con.prepareStatement(q);
			ps.setInt(3, cityId);
			ps.setString(1, name);
			ps.setInt(2, 0);			
			ps.executeUpdate ();
			
			q = "SELECT MAX(IdKupac) FROM Kupac WHERE Ime =? and IdGrad = ?";
			ps = con.prepareStatement(q);
			ps.setInt(2, cityId);
			ps.setString(1, name);
			rs = ps.executeQuery();
			
			if(rs.next())
				return rs.getInt(1);
			else 
				return -1;
			
		}catch(SQLException e){
			e.printStackTrace();
			return -1;			
		}
	}

	@Override
	public int setCity(int buyerId, int cityId) {
		Connection con = DB.getInstance().getConnection();
		try{
			String q = "SELECT IdGrad FROM Grad WHERE IdGrad= ?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, cityId);
			ResultSet rs = ps.executeQuery();
			if(!rs.next())
				return -1;
			q = "SELECT IdKupac FROM Kupac WHERE IdKupac= ?";
			ps = con.prepareStatement(q);
			ps.setInt(1, buyerId);
			rs = ps.executeQuery();
			if(!rs.next())
				return -1;
			
			q = "UPDATE Kupac SET IdGrad = ? WHERE IdKupac = ?";
			ps = con.prepareStatement(q);
			ps.setInt(1, cityId);
			ps.setInt(2, buyerId);
			ps.executeUpdate();
			
			return 1;
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
			
		}
	}

	@Override
	public int getCity(int buyerId) {
		Connection con = DB.getInstance().getConnection();
		try{
			
			String q = "SELECT IdKupac FROM Kupac WHERE IdKupac= ?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, buyerId);
			ResultSet rs = ps.executeQuery();
			if(!rs.next())
				return -1;
			
			q = "SELECT IdGrad  FROM Kupac WHERE idKupac=?";
			ps = con.prepareStatement(q);			
			ps.setInt(1, buyerId);
			rs =ps.executeQuery();
			if(rs.next())
				return rs.getInt(1);
			return -1;
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
			
		}
	}

	@Override
	public BigDecimal increaseCredit(int buyerId, BigDecimal credit) {
		Connection con = DB.getInstance().getConnection();
		try{
			String q ="UPDATE Kupac SET novac = novac + ? WHERE IdKupac = ?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setBigDecimal(1, credit);
			ps.setInt(2, buyerId);
			ps.executeUpdate();
			
			q = "SELECT novac FROM Kupac WHERE idKupac=?";
			ps = con.prepareStatement(q);
			ps.setInt(1, buyerId);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getBigDecimal(1);
			return new BigDecimal(-1);
			
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int createOrder(int buyerId) {
		Connection con = DB.getInstance().getConnection();
		try{
			
			String q = "SELECT IdKupac FROM Kupac WHERE IdKupac= ?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, buyerId);
			ResultSet rs = ps.executeQuery();
			if(!rs.next())
				return -1;
			q = "INSERT INTO Porudzbina(stanje, IdKupac) VALUES (?, ?)";
			ps = con.prepareStatement(q);
			ps.setString(1, "create");
			ps.setInt(2, buyerId);
			ps.executeUpdate();
			
			q = "SELECT MAX(IdPorudzbina) AS P FROM Porudzbina WHERE IdKupac = ?";
			ps = con.prepareStatement(q);
			ps.setInt(1, buyerId);
			rs = ps.executeQuery();
			if(rs.next())
				return rs.getInt("P");
			return -1;			
			
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public List<Integer> getOrders(int buyerId) {
		Connection con = DB.getInstance().getConnection();
		try{
			String q = "SELECT IdPorudzbina AS IP FROM Porudzbina WHERE IdKupac =?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, buyerId);
			ResultSet rs = ps.executeQuery();
			List<Integer> ids = new ArrayList<>();
			while(rs.next()){
				ids.add(rs.getInt("IP"));
			}
			for(Integer i : ids)
				System.out.println("Orders: " + i);
			return ids;
		}catch(SQLException e){
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public BigDecimal getCredit(int buyerId) {
		Connection con = DB.getInstance().getConnection();
		try{
			String q = "SELECT novac FROM Kupac WHERE idKupac=?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, buyerId);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getBigDecimal(1);
			return null;
		}catch(SQLException e){
			e.printStackTrace();
			return null;
		}
	}

}
