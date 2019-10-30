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
public class CreateDataFile {

    static final String url = "jdbc:mysql://localhost:3306/";
    static final String driver = "com.mysql.jdbc.Driver";
    static final String username = "root";
    static final String userpassword = "";
    static final String dbname = "Gendle";

    public static void main(String args[]) {
        Connection con = null;
        Statement st = null;
        try {
            Class.forName(driver);
            System.out.println("Connecting to DataBase");
            con = DriverManager.getConnection(url, username, userpassword);
//            System.out.println("Creating DataBase");
            st = con.createStatement();
            String sql = "create database Gendle";
                st.executeUpdate(sql);
            System.out.println("DataBase  Created....");
            System.out.println("*********************");
            String sql1 = "use " + dbname;
            st.executeUpdate(sql1);

            st.executeUpdate("create table SGAMakespan(NumberOfTasks int,Makespan double)");
            st.executeUpdate("create table SGAExpense(NumberOfTasks int,cost double)");
            st.executeUpdate("create table IGAMakespan(NumberOfTasks int,Makespan double)");
            st.executeUpdate("create table IGAExpense(NumberOfTasks int,cost double)");
            st.executeUpdate("create table GADEMakespan(NumberOfTasks int,Makespan double)");
            st.executeUpdate("create table GADEExpense(NumberOfTasks int,cost double)");
            
            

            JOptionPane.showMessageDialog(null, "The  Database File and tables created Succussfully!!");

        } catch (Exception e) {
            System.out.println("Error " + e);
        }

    }

}
