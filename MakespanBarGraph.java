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
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

/**
 *
 * @author soft29
 */
public class MakespanBarGraph extends ApplicationFrame {

    String[] numOftask,numOftask1, numOftask2;
    double[] makespan,makespan1, makespan2;

    public MakespanBarGraph(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "Number of Tasks",
                "Makespan",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 400));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset() {
        String[] Algorithm = new String[4];
        Algorithm[0] = "Simple GA";
        Algorithm[1] = "Iproved GA";
        Algorithm[2] = "GA with DE";

        final DefaultCategoryDataset dataset
                = new DefaultCategoryDataset();

        try {
            /**
             * from line graph
             */

            Connection conn = null;
            String url = "jdbc:mysql://localhost:3306/";
            String dbName = "Gendle";
            String driver = "com.mysql.jdbc.Driver";
            String userName = "root";
            String dbpassword = "";
            Class.forName(driver).newInstance();
            conn = DriverManager.getConnection(url + dbName, userName, dbpassword);
            Statement stmt = conn.createStatement();

            /**
             * data retrieved from Simple Genetic Algorithm to draw a graph
             */
            String str = "SELECT avg(NumberOfTasks),avg(Makespan) FROM SGAMakespan GROUP BY NumberOfTasks";
            String xCount = "SELECT COUNT(DISTINCT NumberOfTasks) FROM SGAMakespan";

            ResultSet rs = stmt.executeQuery(xCount);
            int z = 0;
            if (rs != null) {
                while (rs.next()) {
                    z = rs.getInt(1);
                }
            }
            numOftask = new String[z];
            makespan = new double[z];

            ResultSet Sgars1 = stmt.executeQuery(str);
            
            int k = 0;
            if (Sgars1 != null) {
                while (Sgars1.next()) {
                    numOftask[k] = (Sgars1.getString(1));
                    numOftask[k] = numOftask[k].substring(0, numOftask[k].indexOf("."));
                    makespan[k] = Sgars1.getDouble(2);

                    k++;
                }
            }
            

            /**
             * data extracted from Improved Genetic Algorithm to draw a graph
             */
            String str1 = "SELECT avg(NumberOfTasks),avg(Makespan) FROM IGAMakespan GROUP BY NumberOfTasks";
            String xCount1 = "SELECT COUNT(DISTINCT NumberOfTasks) FROM IGAMakespan";

            ResultSet rs1 = stmt.executeQuery(xCount1);
            int y = 0;
            if (rs1 != null) {
                while (rs1.next()) {
                    y = rs1.getInt(1);
                }
            }
            numOftask1 = new String[y];
            makespan1 = new double[y];

            ResultSet Igars1 = stmt.executeQuery(str1);
            
            int j = 0;
            if (Igars1 != null) {
                while (Igars1.next()) {
                    numOftask1[j] = (Igars1.getString(1));
                    numOftask1[j] = numOftask1[j].substring(0, numOftask1[j].indexOf("."));
                    makespan1[j] = Igars1.getDouble(2);
                    j++;
                }
            }

            /**
             * data segregation from proposed algorithm to draw a graph
             */
            String str2 = "SELECT avg(NumberOfTasks),avg(Makespan) FROM GADEMakespan GROUP BY NumberOfTasks";
            String xCount2 = "SELECT COUNT(DISTINCT NumberOfTasks) FROM GADEMakespan";

            ResultSet rs2 = stmt.executeQuery(xCount2);

            int x = 0;
            if (rs2 != null) {
                while (rs2.next()) {
                    x = rs2.getInt(1);
                }
            }
            numOftask2 = new String[x];
            makespan2 = new double[x];

            ResultSet Gaders2 = stmt.executeQuery(str2);

            int i = 0;
            if (Gaders2 != null) {
                while (Gaders2.next()) {
                    numOftask2[i] = (Gaders2.getString(1));
                    numOftask2[i] = numOftask2[i].substring(0, numOftask2[i].indexOf("."));
                    makespan2[i] = Gaders2.getDouble(2);

                    i++;
                }
            }

            stmt.close();
            conn.close();

            /* Simple GA    */
            for (int a = 0; a < z; a++) {
                dataset.addValue(makespan[a], Algorithm[0], numOftask[a]);
            }

            
            /* Improved GA    */
            for (int b = 0; b < y; b++) {
                dataset.addValue(makespan1[b], Algorithm[1], numOftask1[b]);
            }


            /* Proposed GA with DE   */
            for (int c = 0; c < x; c++) {
                dataset.addValue(makespan2[c], Algorithm[2], numOftask2[c]);
            }

            
        } catch (Exception e) {
            System.out.println("database access Error " + e);
        }

        return dataset;
    }

    public static void main(String[] args) {
        MakespanBarGraph chart = new MakespanBarGraph("Comparision of Makespan",
                "Optimized Execution Time");
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }

}
