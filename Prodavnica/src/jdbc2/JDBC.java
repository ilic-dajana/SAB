/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jdbc2;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JDBC {

    public static int dodavanjeRadnika(){
        Connection connection=DB.getInstance().getConnection();
        String insertQuery="insert into Radnik values(?,?,?,?,1)";
        String getMaxLKNum="select max(BrLK) from Radnik";
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(getMaxLKNum);
            int brLk=0;
            if(resultSet.next())
            {
                brLk=resultSet.getInt(1);
            }
            brLk++;
            PreparedStatement ps=connection.prepareStatement(insertQuery);
            ps.setInt(1, brLk);
            ps.setString(2, "Tamara");
            ps.setString(3, "Sekularac");
            ps.setString(4, "0642914***");
            return ps.executeUpdate();           
        } catch (SQLException ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
    

    
    public static int brRadnikaSaImenom(String ime)
    {
        try {
            Connection connection=DB.getInstance().getConnection();
            String sql="{ call SPBrojRadnikaSaImenom (?,?) }";
            CallableStatement cs= connection.prepareCall(sql);
            cs.setString(1, ime);
            cs.registerOutParameter(2, java.sql.Types.INTEGER);
            cs.execute();
            return cs.getInt(2);
        } catch (SQLException ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
    
        
    public static void radniciSaImenom(String ime)
    {
        try {
            Connection connection=DB.getInstance().getConnection();
            String sql="{ call SPRadniciSaImenom (?) }";
            CallableStatement cs= connection.prepareCall(sql);
            cs.setString(1, ime);
            ResultSet rs=cs.executeQuery();
            System.out.println("---------------------------------");
            System.out.println("Radnici sa imenom "+ime);
            while(rs.next())
                System.out.println("BrLK:"+rs.getInt("BrLK")+
                        " prezime:"+rs.getString("Prezime"));
        } catch (SQLException ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
        
    public static void ispisSpiskaTabela(){
        Connection connection=DB.getInstance().getConnection();
        String tableQuery="select TABLE_NAME from INFORMATION_SCHEMA.TABLES";
        String columnQuery="select COLUMN_NAME from INFORMATION_SCHEMA.COLUMNS  where TABLE_NAME=?";
        try {
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(tableQuery);
            PreparedStatement columnStmt=connection.prepareStatement(columnQuery);
            while(resultSet.next())
            {
                System.out.println(resultSet.getString("TABLE_NAME"));
                columnStmt.setString(1, resultSet.getString("TABLE_NAME"));
                ResultSet columnRS=columnStmt.executeQuery();
                while(columnRS.next())
                    System.out.println("    -"+columnRS.getString("COLUMN_NAME"));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void ispisSpiskaProceduraIFunkcija(){
        Connection connection=DB.getInstance().getConnection();
        String functionQuery="SELECT NAME FROM sys.all_objects "
                    + "where type in ('FN','AF','FS','FT','IF','TF') and is_ms_shipped=0";
        String procedureQuery="SELECT NAME FROM sys.all_objects where type='P' and is_ms_shipped=0";
        try {
            System.out.println("Funkcije:");
            Statement statement=connection.createStatement();
            ResultSet resultSet=statement.executeQuery(functionQuery);
            while(resultSet.next())
            {
                System.out.println("    -"+resultSet.getString("NAME"));   
            }
            System.out.println("Procedure:");
            statement=connection.createStatement();
            resultSet=statement.executeQuery(procedureQuery);
            while(resultSet.next())
            {
                System.out.println("    -"+resultSet.getString("NAME"));   
            }
        } catch (SQLException ex) {
            Logger.getLogger(JDBC.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        //if(dodavanjeRadnika()>0)
         //   System.out.println("Uspesno smo dodali Radnika");
        radniciSaImenom("Vuk");
        
        System.out.println("broj radnika sa imenom Tamara:"+brRadnikaSaImenom("Tamara"));
        
        System.out.println("-----------------------");
        ispisSpiskaTabela();
        System.out.println("-----------------------");
        ispisSpiskaProceduraIFunkcija();
    }
    
}
