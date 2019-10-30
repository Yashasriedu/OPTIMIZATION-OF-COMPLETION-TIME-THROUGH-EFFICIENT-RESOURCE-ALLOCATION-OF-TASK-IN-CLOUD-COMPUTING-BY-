/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GENDLE;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 *
 * @author soft29
 */
public class FlushDataFile {

    static final String url = "jdbc:mysql://localhost:3306/";
    static final String driver = "com.mysql.jdbc.Driver";
    static final String dbname = "Gendle";
    static final String uname = "root";
    static final String upass = "";

    public static void main(String args[]) {
        try {
            Class.forName(driver);
            Connection con = DriverManager.getConnection(url + dbname, uname, upass);
            Statement st = con.createStatement();
            System.out.println("Got DB Connection");

            
            st.executeUpdate("delete from GADEMakespan");
            st.executeUpdate("delete from GADEExpense");

            st.executeUpdate("delete from SGAMakespan");
            st.executeUpdate("delete from SGAExpense");
            
            st.executeUpdate("delete from IGAMakespan");
            st.executeUpdate("delete from IGAExpense");
            
            con.close();
            st.close();
            JOptionPane.showMessageDialog(null, "The  records are deleted from all tables");

        } catch (Exception e) {
            System.out.println("DataBases Flush Error " + e);
        }
    }

}
