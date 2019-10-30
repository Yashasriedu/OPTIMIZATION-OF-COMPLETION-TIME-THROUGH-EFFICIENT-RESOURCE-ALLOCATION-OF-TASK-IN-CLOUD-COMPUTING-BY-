/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GENDLE;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 *
 * @author soft29
 */
public class ResultAnalysis {

    static int[] numOftask1, numOftask2, numOftask3, numOftask4;
    static double[] makespan1, makespan2, makespan3, makespan4;

    public static void main(String args[]) {
        try {
            /**
             * from line graph
             */

            Connection conn = null;
            String url = "jdbc:mysql://localhost:3306/";
            String dbName = "HybridTaskSchedule";
            String driver = "com.mysql.jdbc.Driver";
            String userName = "root";
            String dbpassword = "";
//System.out.println("Host-Configurations");
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, dbpassword);
            Statement stmt = conn.createStatement();
//            System.out.println("Got DB Connection");

            String str = "SELECT avg(NumberOfTasks),avg(Makespan) FROM HeftUpwardMakespan GROUP BY NumberOfTasks";
            String xCount = "SELECT COUNT(DISTINCT NumberOfTasks) FROM HeftUpwardMakespan";

            ResultSet rs1 = stmt.executeQuery(xCount);

            int x = 0;
            if (rs1 != null) {
                while (rs1.next()) {
                    x = rs1.getInt(1);
                }
            }
            numOftask1 = new int[x];
            makespan1 = new double[x];
            System.out.println("The Number of different Tasks-HEFTUpward : " + x);

            ResultSet rs = stmt.executeQuery(str);

            int i = 0;
            if (rs != null) {
                while (rs.next()) {
                    numOftask1[i] = rs.getInt(1);
                    makespan1[i] = rs.getDouble(2);

                    System.out.println("Number of task " + numOftask1[i]);
                    System.out.println("Makespan : " + makespan1[i]);
//                    firefox.add(numOftask[0], makespan[0]);
                    i++;
                }
            }

            /**
             * HEFTDOWNWARDMAKESPAN
             */
            String str1 = "SELECT avg(NumberOfTasks),avg(Makespan) FROM HeftDownwardMakespan GROUP BY NumberOfTasks";
            String xCount1 = "SELECT COUNT(DISTINCT NumberOfTasks) FROM HeftDownwardMakespan";

            ResultSet rs2 = stmt.executeQuery(xCount1);

            int y = 0;
            if (rs2 != null) {
                while (rs2.next()) {
                    y = rs2.getInt(1);
                }
            }
            numOftask2 = new int[y];
            makespan2 = new double[y];
            System.out.println("The Number of different Tasks-HEFTDownward: " + y);

            ResultSet rs3 = stmt.executeQuery(str1);

            int j = 0;
            if (rs3 != null) {
                while (rs3.next()) {
                    numOftask2[j] = rs3.getInt(1);
                    makespan2[j] = rs3.getDouble(2);

                    System.out.println("Number of task " + numOftask2[j]);
                    System.out.println("Makespan : " + makespan2[j]);
//                    firefox.add(numOftask[0], makespan[0]);
                    j++;
                }
            }

            /**
             * HEFTLEVELRANKMAKESPAN
             */
            String str2 = "SELECT avg(NumberOfTasks),avg(Makespan) FROM HeftLevelMakespan GROUP BY NumberOfTasks";
            String xCount2 = "SELECT COUNT(DISTINCT NumberOfTasks) FROM HeftLevelMakespan";

            ResultSet rs4 = stmt.executeQuery(xCount2);

            int z = 0;
            if (rs4 != null) {
                while (rs4.next()) {
                    z = rs4.getInt(1);
                }
            }
            numOftask3 = new int[z];
            makespan3 = new double[z];
            System.out.println("The Number of different Tasks-HEFTLevelRank: " + z);

            ResultSet rs5 = stmt.executeQuery(str2);

            int k = 0;
            if (rs5 != null) {
                while (rs5.next()) {
                    numOftask2[k] = rs5.getInt(1);
                    makespan2[k] = rs5.getDouble(2);

                    System.out.println("Number of task " + numOftask2[k]);
                    System.out.println("Makespan : " + makespan2[k]);
//                    firefox.add(numOftask[0], makespan[0]);
                    k++;
                }
            }

            /**
             * GA-DE PROPOSED
             */
            String str3 = "SELECT avg(NumberOfTasks),avg(Makespan) FROM GADEMakespan GROUP BY NumberOfTasks";
            String xCount3 = "SELECT COUNT(DISTINCT NumberOfTasks) FROM GADEMakespan";

            ResultSet rs6 = stmt.executeQuery(xCount3);

            int p = 0;
            if (rs6 != null) {
                while (rs6.next()) {
                    p = rs6.getInt(1);
                }
            }
            numOftask4 = new int[p];
            makespan4 = new double[p];
            System.out.println("The Number of different Tasks-HEFTLevelRank: " + p);

            ResultSet rs7 = stmt.executeQuery(str3);

            int l = 0;
            if (rs7 != null) {
                while (rs7.next()) {
                    numOftask2[l] = rs7.getInt(1);
                    makespan2[l] = rs7.getDouble(2);

                    System.out.println("Number of task " + numOftask2[l]);
                    System.out.println("Makespan : " + makespan2[l]);
//                    firefox.add(numOftask[0], makespan[0]);
                    l++;
                }
            }
            
            stmt.close();
            conn.close();


        } catch (Exception e) {
            System.out.println("database access Error " + e);
        }
    }

}
