package student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import jdbc2.DB;
import operations.TransactionOperations;

public class id150325_TransactionOperations implements TransactionOperations {

	@Override
	public BigDecimal getShopTransactionsAmmount(int shopId) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int getTransactionForBuyersOrder(int orderId) {
		Connection con = DB.getInstance().getConnection();
		try {
			String q = "Select idTransakcija  From Transakcija where idPorudzbine = ?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getInt(1);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	@Override
	public BigDecimal getAmmountThatShopRecievedForOrder(int shopId, int orderId) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public BigDecimal getBuyerTransactionsAmmount(int buyerId) {
		Connection con  = DB.getInstance().getConnection();
		try {
			String q = "Select SUM(Iznos) as platio From Transakcija where idKupac = ?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, buyerId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getBigDecimal("platio");
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}



	@Override
	public List<Integer> getTransationsForBuyer(int buyerId) {
		Connection con = DB.getInstance().getConnection();
		String q = "Select idTransakcija From  Transakcija where IdKupac = ?";
		try {
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, buyerId);
			ResultSet rs = ps.executeQuery();
			List<Integer> buyerTrans = new ArrayList<>();
			while(rs.next()) {
				buyerTrans.add(rs.getInt(1));
			}
			return buyerTrans;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		return null;
	}

	

	@Override
	public int getTransactionForShopAndOrder(int orderId, int shopId) {
		Connection con = DB.getInstance().getConnection();
		String q = "SELECT top(1) IdTransakcija FROM Transakcija WHERE IdPorudzbina = ? AND IdProdavnica = ?";
		try {
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, orderId);
			ps.setInt(2, shopId);
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getInt(1);
			return -1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	@Override
	public List<Integer> getTransationsForShop(int shopId) {
		Connection con = DB.getInstance().getConnection();
		String q = "SELECT IdTransakcija FROM Transakcija WHERE IdProdavnica = ?";
		try {
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, shopId);
			ResultSet rs = ps.executeQuery();
			List<Integer> trans = new ArrayList<>();
			
			while(rs.next()) {
				trans.add(rs.getInt(1));
			}
			return trans;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Calendar getTimeOfExecution(int transactionId) {
		Connection con = DB.getInstance().getConnection();
		String q = "Select Time From  Transakcija where idTransakcija = ?";
		try {
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, transactionId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			
			Calendar c = new GregorianCalendar();
			c.setTime(rs.getDate(1));
			return c;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	@Override
	public BigDecimal getAmmountThatBuyerPayedForOrder(int orderId) {
		Connection con  = DB.getInstance().getConnection();
		try {
			String q = "Select SUM(Iznos) as platio From Transakcija where idPorudzbine = ?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, orderId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getBigDecimal("platio");
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public BigDecimal getTransactionAmount(int transactionId) {
		Connection con  = DB.getInstance().getConnection();
		try {
			String q = "Select Iznos From Transakcija where idTransakcija = ?";
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, transactionId);
			ResultSet rs = ps.executeQuery();
			rs.next();
			return rs.getBigDecimal(1);
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public BigDecimal getSystemProfit() {
		Connection con = DB.getInstance().getConnection();
		String q = "SELECT profit from Profit where idProfit = ?";
		try {
			PreparedStatement ps = con.prepareStatement(q);
			ps.setInt(1, 1);
			ResultSet rs = ps.executeQuery();
			if(rs.next()) {
				return rs.getBigDecimal(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

}
