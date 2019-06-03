package student;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jdbc2.DB;
import operations.OrderOperations;

public class id150325_OrderOperations implements OrderOperations {

	@Override
	public int addArticle(int orderId, int articleId, int count) {
		Connection con = DB.getInstance().getConnection();
		if(count < 0)
			return -1;
		
		CallableStatement cs;
		try {
			cs = con.prepareCall("{call addArtikal(?, ?, ?, ?)}");
			cs.setInt(1, orderId);
			cs.setInt(2, articleId);
			cs.setInt(3, count);
			cs.registerOutParameter(4, Types.INTEGER);
			cs.execute();
			return cs.getInt(4);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	@Override
	public int removeArticle(int orderId, int articleId) {
		Connection con = DB.getInstance().getConnection();
		
		CallableStatement cs;
		try {
			cs = con.prepareCall("{call removeArtikal(?, ?, ?)}");
			cs.setInt(1, orderId);
			cs.setInt(2, articleId);
			cs.registerOutParameter(3, Types.INTEGER);
			cs.execute();
			return cs.getInt(3);
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}		
	}

	@Override
	public List<Integer> getItems(int orderId) {
		Connection con = DB.getInstance().getConnection();
		String q = "SELECT IdNarArtikal FROM NaruceniArtikal WHERE IdPorudzbine = ?";
		
		PreparedStatement ps;
		try {
			ps = con.prepareStatement(q);
			ps.setInt(1, orderId);
			List<Integer> items = new ArrayList<>();
			ResultSet rs = ps.executeQuery();
			
			while(rs.next())
				items.add(rs.getInt(1));
			
			return items;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return null;
	}

	@Override
	public int completeOrder(int orderId) {
		Connection con = DB.getInstance().getConnection();
		id150325_CityOperations grad = new id150325_CityOperations();
		List<Integer> sviGradovi = grad.getCities();
		try {
			CallableStatement cs = con.prepareCall("call Grad_korisnik(?)");
			cs.setInt(1, orderId);
			ResultSet rs = cs.executeQuery();
			Integer idGradKorisnika;
			if(rs.next())
				 idGradKorisnika = rs.getInt(1);
			else
				return -1;
			
			cs = con.prepareCall("{call Gradovi_porudzbine(?)}");
			cs.setInt(1, orderId);
			rs = cs.executeQuery();
			List<Integer> gradoviProdavnica = new ArrayList<>();
			
			while(rs.next())
				gradoviProdavnica.add(rs.getInt(1));
			
			int n = sviGradovi.size();
			int matrica[][] = new int[n][n];
			
			for(int i = 0; i < n; i++){
				for(int j = 0; j < n; j++){
					matrica[i][j] = 0;
				}
			}
			
			int map[] = new int[n];
			Map<Integer, Integer> mapId = new HashMap<>();
			
			for(int i = 0; i < n; i++){
				mapId.put(sviGradovi.get(i), i);
			}
			
			for(int i = 0; i < n; i++){
				List<Integer> connected = grad.getConnectedCities(sviGradovi.get(i));
				for(Integer id: connected){
					int j = mapId.get(id);
					int dist = grad.getDistance(sviGradovi.get(i), id);
					matrica[i][j] = dist;
				}
			}
			
			//DIJKSTRA
			int[] shortestDistance = new int[n];
			boolean[] set = new boolean[n];
			
			for(int i = 0; i<n; i++){
				shortestDistance[i] = Integer.MAX_VALUE;
				set[i] = false;
			}
			int src = mapId.get(idGradKorisnika);
			shortestDistance[src] = 0;
			
			for(int count = 0; count < n - 1; count++){
				int min = Integer.MAX_VALUE;
				int minIndex = -1;
				for(int v = 0; v< n; v++){
					if(set[v] == false && shortestDistance[v]<=min){
						min = shortestDistance[v];
						minIndex = v;
					}
				}
				set[minIndex] = true;
				
				for(int v = 0; v < n; v++){
					if(!set[v] && matrica[minIndex][v] != 0 &&
							shortestDistance[minIndex] != Integer.MAX_VALUE && 
							(shortestDistance[minIndex] + matrica[minIndex][v] ) < shortestDistance[v]){
						shortestDistance[v] = shortestDistance[minIndex] + matrica[minIndex][v];
					}
				}
				
				int minProdId = -1, minDist = Integer.MAX_VALUE;
				
				for(Integer id : gradoviProdavnica){
					if(minProdId == -1 || (shortestDistance[mapId.get(id)] < minDist)){
						minProdId = id;
						minDist = shortestDistance[mapId.get(id)];
					}
				}
			}
			
			
			return 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public BigDecimal getFinalPrice(int orderId) {
		Connection con = DB.getInstance().getConnection();		
		CallableStatement cs;
		try {
			cs = con.prepareCall("{call SP_FINAL_PRICE(?, ?)}");
			cs.setInt(1, orderId);			
			cs.registerOutParameter(2, Types.DECIMAL);
			cs.execute();
			return cs.getBigDecimal(2);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public BigDecimal getDiscountSum(int orderId) {
		Connection con = DB.getInstance().getConnection();
				
		CallableStatement cs;
		try {
			cs = con.prepareCall("{call CalculateDiscount(?, ?)}");
			cs.setInt(1, orderId);			
			cs.registerOutParameter(2, Types.DECIMAL);
			cs.execute();
			return cs.getBigDecimal(2);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public String getState(int orderId) {
		Connection con = DB.getInstance().getConnection();
		try{
			String q = "SELECT Stanje FROM Porudzbina WHERE IdPorudzbina = ?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				return rs.getString(1);
			}else
				return " ";
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Calendar getSentTime(int orderId) {
		Connection con = DB.getInstance().getConnection();
		String q = "SELECT datum_sent FROM Porudzbina WHERE IdPorudzbina = ?";
		try {
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();
			Calendar c =  new GregorianCalendar();
			
						
			if(rs.next()){
				c.setTime(rs.getDate(1));
				return c;
			}

			return null;			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Calendar getRecievedTime(int orderId) {
		Connection con = DB.getInstance().getConnection();
		String q = "SELECT datum_received FROM Porudzbina WHERE IdPorudzbina = ?";
		try {
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();
			Calendar c =  new GregorianCalendar();
									
			if(rs.next()){
				c.setTime(rs.getDate(1));
				return c;
			}

			return null;			
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public int getBuyer(int orderId) {
		Connection con = DB.getInstance().getConnection();
		try{
			String q = "SELECT IdKupac FROM Porudzbina  WHERE IdPorudzbina = ?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1,orderId);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getInt(1);
			else
				return -1;
		}catch (SQLException e){
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getLocation(int orderId) {
		Connection con = DB.getInstance().getConnection();
		try{
			String q = "SELECT Lokacija FROM Porudzbina  WHERE IdPorudzbina = ?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1,orderId);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getInt(1);
			else
				return -1;
		}catch (SQLException e){
			e.printStackTrace();
		}
		return 0;
	}

}
