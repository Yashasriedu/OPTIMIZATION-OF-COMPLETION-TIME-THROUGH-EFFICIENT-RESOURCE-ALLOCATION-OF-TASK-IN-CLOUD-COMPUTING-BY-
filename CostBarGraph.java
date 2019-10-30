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
public class CostBarGraph extends ApplicationFrame {

    String[] numOftask1, numOftask2;
    double[] expense1, expense2;

    public CostBarGraph(String applicationTitle, String chartTitle) {
        super(applicationTitle);
        JFreeChart barChart = ChartFactory.createBarChart(
                chartTitle,
                "Number of Tasks",
                "Execution Cost",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);

        ChartPanel chartPanel = new ChartPanel(barChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(560, 400));
        setContentPane(chartPanel);
    }

    private CategoryDataset createDataset() {
        String[] Algorithm = new String[2];
        Algorithm[0] = "Iproved GA";
        Algorithm[1] = "GA with DE";

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
             * data extracted from Improved Genetic Algorithm to draw a graph
             */
            String str1 = "SELECT avg(NumberOfTasks),avg(cost) FROM IGAExpense GROUP BY NumberOfTasks";
            String xCount1 = "SELECT COUNT(DISTINCT NumberOfTasks) FROM IGAExpense";

            ResultSet rs1 = stmt.executeQuery(xCount1);
            int y = 0;
            if (rs1 != null) {
                while (rs1.next()) {
                    y = rs1.getInt(1);
                }
            }
            numOftask1 = new String[y];
            expense1 = new double[y];

            ResultSet Igars1 = stmt.executeQuery(str1);

            int j = 0;
            if (Igars1 != null) {
                while (Igars1.next()) {
                    numOftask1[j] = (Igars1.getString(1));
                    numOftask1[j] = numOftask1[j].substring(0, numOftask1[j].indexOf("."));
                    expense1[j] = Igars1.getDouble(2);
                    j++;
                }
            }

            /**
             * data segregation from proposed algorithm to draw a graph
             */
            String str2 = "SELECT avg(NumberOfTasks),avg(cost) FROM GADEExpense GROUP BY NumberOfTasks";
            String xCount2 = "SELECT COUNT(DISTINCT NumberOfTasks) FROM GADEExpense";

            ResultSet rs2 = stmt.executeQuery(xCount2);

            int x = 0;
            if (rs2 != null) {
                while (rs2.next()) {
                    x = rs2.getInt(1);
                }
            }
            numOftask2 = new String[x];
            expense2 = new double[x];

            ResultSet Gaders2 = stmt.executeQuery(str2);

            int i = 0;
            if (Gaders2 != null) {
                while (Gaders2.next()) {
                    numOftask2[i] = (Gaders2.getString(1));
                    numOftask2[i] = numOftask2[i].substring(0, numOftask2[i].indexOf("."));
                    expense2[i] = Gaders2.getDouble(2);
                    System.out.println("The number of task in GADE " + numOftask2[i]);
                    System.out.println("The cost of execution in GADE " + expense2[i]);
                    
                    i++;
                }
            }

            stmt.close();
            conn.close();

            /* Improved GA    */
            for (int b = 0; b < y; b++) {
                dataset.addValue(expense1[b], Algorithm[0], numOftask1[b]);
            }


            /* Proposed GA with DE   */
            for (int c = 0; c < x; c++) {
                dataset.addValue(expense2[c], Algorithm[1], numOftask2[c]);
            }

        } catch (Exception e) {
            System.out.println("database access Error " + e);
        }

        return dataset;
    }

    public static void main(String[] args) {
        CostBarGraph chart = new CostBarGraph("Comparision of Execution Cost",
                "Optimized Execution Time");
        chart.pack();
        RefineryUtilities.centerFrameOnScreen(chart);
        chart.setVisible(true);
    }

}
