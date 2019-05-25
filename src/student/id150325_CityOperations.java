package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jdbc2.DB;
import operations.CityOperations;

public class id150325_CityOperations implements CityOperations {

	@Override
	public int createCity(String name) {
			Connection con = DB.getInstance().getConnection();
			String insert = "INSERT INTO Grad VALUES(?)";
			
			try {
				PreparedStatement stmt = con.prepareStatement(insert);
				stmt.setString(1, name);
				stmt.executeUpdate();
			
				PreparedStatement select = con.prepareStatement("SELECT MAX(IdGrad) AS Max FROM Grad WHERE Ime = ?");				
				select.setString(1, name);
				ResultSet rs = select.executeQuery();
				rs.next();
				Integer id = rs.getInt("Max");
				
				if(id > 0){					
					System.out.println(id);
					return id;
				}else{
					return -1;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		return 0;
	}

	@Override
	public List<Integer> getCities() {
			String select = "SELECT IdGrad AS id FROM Grad";
			Connection con = DB.getInstance().getConnection();
			
			try {
				PreparedStatement pst = con.prepareStatement(select);
				ResultSet rs = pst.executeQuery();
				List<Integer> ids = new ArrayList<Integer>();
				while(rs.next()){
					ids.add(rs.getInt("id"));
				}
				for(Integer i : ids){
					System.out.println(i);
				}
				return ids;
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			
		return null;
	}

	@Override
	public int connectCities(int cityId1, int cityId2, int distance) {
		if(cityId1 == cityId2)
			return -1;
		Connection con = DB.getInstance().getConnection();
		
		try {
			String check = "SELECT * FROM Linija WHERE IdGrad1 =? and IdGrad2 =? "
					+ "UNION "
					+ "SELECT * FROM Linija WHERE IdGrad1=? and IdGrad2=?";
			PreparedStatement ch = con.prepareStatement(check);
			ch.setInt(1,cityId1);
			ch.setInt(2,cityId2);
			ch.setInt(3,cityId2);
			ch.setInt(4,cityId1);
			ResultSet rss = ch.executeQuery();
			if(rss.next())
				return -1;
			
			String insert = "INSERT INTO Linija(IdGrad1, IdGrad2, udaljenost) VALUES(?,?,?)";			
			PreparedStatement stmt = con.prepareStatement(insert);
			stmt.setInt(1, cityId1);
			stmt.setInt(2, cityId2);
			stmt.setInt(3, distance);
			stmt.executeUpdate();
		
			PreparedStatement select = con.prepareStatement("SELECT MAX(IdLinija) AS Max FROM Linija WHERE IdGrad1 = ? AND IdGrad2 = ?");				
			select.setInt(1, cityId1);
			select.setInt(2, cityId2);
			
			ResultSet rs = select.executeQuery();
			rs.next();
			Integer id = rs.getInt("Max");
			
			if(id > 0){					
				System.out.println(id);
				return id;
			}else{
				return -1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public List<Integer> getConnectedCities(int cityId) {
		String select = "SELECT IdGrad1 AS id FROM Linija WHERE IdGrad2=?"
				+ " UNION "
				+ "SELECT IdGrad2 AS id FROM Linija WHERE idGrad1=?";
		Connection con = DB.getInstance().getConnection();
		
		try {
			PreparedStatement pst = con.prepareStatement(select);
			pst.setInt(1,cityId);
			pst.setInt(2,cityId);
			
			ResultSet rs = pst.executeQuery();
			List<Integer> ids = new ArrayList<Integer>();
			while(rs.next()){
				ids.add(rs.getInt("id"));
			}
			for(Integer i : ids){
				System.out.println(i);
			}
			return ids;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Integer> getShops(int cityId) {
		String select = "SELECT IdProdavnica AS id FROM Prodavnica WHERE IdGrad=?";
		Connection con = DB.getInstance().getConnection();
		
		try {
			PreparedStatement pst = con.prepareStatement(select);
			pst.setInt(1, cityId);
			ResultSet rs = pst.executeQuery();
			List<Integer> ids = new ArrayList<Integer>();
			while(rs.next()){
				ids.add(rs.getInt("id"));
			}
			for(Integer i : ids){
				System.out.println(i);
			}
			return ids;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
