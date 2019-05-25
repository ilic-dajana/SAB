package student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jdbc2.DB;
import operations.ArticleOperations;

public class id150325_ArticleOperations implements ArticleOperations {

	@Override
	public int createArticle(int shopId, String articleName, int articlePrice) {
		Connection con = DB.getInstance().getConnection();
		
		try {
			String q = "INSERT INTO Artikal(Ime, Cena, Kolicina, IdProdavnica) VALUES(?,?,?,?)";			
			PreparedStatement ps = con.prepareStatement(q);
			
			ps.setString(1, articleName);
			ps.setInt(2, articlePrice);
			ps.setInt(3, 0);
			ps.setInt(4, shopId);
			
			ps.executeUpdate();
			
			q = "SELECT MAX(IdArtikal) AS ID From Artikal WHERE Ime = ? AND Cena=? AND IdProdavnica=?";
			ps = con.prepareStatement(q);
			
			ps.setString(1, articleName);
			ps.setInt(2, articlePrice);
			ps.setInt(4, shopId);
			
			ResultSet rs = ps.executeQuery();
			if(rs.next())
				return rs.getInt("ID");
			return -1;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

}
