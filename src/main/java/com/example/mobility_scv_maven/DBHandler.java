package com.example.mobility_scv_maven;

import javafx.scene.chart.XYChart;

import java.sql.*;
import java.util.ArrayList;
import java.util.Queue;

public class DBHandler {
    private static Connection DBconn;
    static int rowid=0;

    public static Connection connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            DBconn = DriverManager.getConnection("jdbc:sqlite:Data.db");
            System.out.println("Connect DB");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void createTable() {
        String sqlSpeed = "CREATE TABLE IF NOT EXISTS READDATA (Speed TEXT,Distance TEXT);";
        try (Statement stmt = DBconn.createStatement()) {
            stmt.execute(sqlSpeed);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void InitTable(){
        String sql = "DELETE FROM READDATA";
        try (Statement stmt = DBconn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void InsertDataRead(String[] data) {
        String sql = "INSERT INTO READDATA (Speed,Distance) VALUES (?,?);";
        try (PreparedStatement pstmt = DBconn.prepareStatement(sql)) {
            pstmt.setString(1, data[0]);
            pstmt.setString(2,data[1]);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String Max_data(){
        String data = null;
        String sql = "select MAX(CAST(Speed AS DECIMAL)) from READDATA WHERE Speed IS NOT NULL;";
        try(Statement stmt = DBconn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            data = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println(data);
        return data;
    }


    public static ArrayList<String> speed_DIstance_data() throws SQLException {
        ArrayList<String> data=new ArrayList<>();
        String sql = "SELECT Speed,Distance FROM READDATA WHERE ROWID = (SELECT MAX(ROWID) FROM READDATA) AND Speed IS NOT NULL;";
        try(Statement stmt = DBconn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            data.add(rs.getString(1));
            data.add(rs.getString(2));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    public static void close() {
        try {
            DBconn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("DB Close");
    }
}
