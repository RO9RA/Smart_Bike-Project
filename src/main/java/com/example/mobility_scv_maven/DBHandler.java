package com.example.mobility_scv_maven;

import java.sql.*;
import java.util.ArrayList;
import java.util.Queue;

public class DBHandler {
    private static Connection DBconn;

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
        String sql = "CREATE TABLE IF NOT EXISTS USERDATA (Speed TEXT, X TEXT, Y TEXT, Z TEXT);";
        try (Statement stmt = DBconn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void InitTable(){
        String sql = "DROP TABLE IF EXISTS USERDATA";
        try (Statement stmt = DBconn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertData(String data[]) {
        String sql = "INSERT INTO USERDATA (Speed,X,Y,Z) VALUES (?, ?, ?, ?);";
        try (PreparedStatement pstmt = DBconn.prepareStatement(sql)) {
            for(int i=0; i<data.length; i++){
                pstmt.setString(i+1, data[i]);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String Max_data(){
        String data = null;
        String sql = "select MAX(X) from USERDATA;";
        try(Statement stmt = DBconn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            data = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //System.out.println(data);
        return data;
    }

    public static ArrayList<Integer> speed_data() throws SQLException {
        ArrayList<Integer> data = new ArrayList<>();
        int dataCount = 0;
        String sql,sqlDataCount = "select count(X) from USERDATA";
        try(Statement stmt = DBconn.createStatement();
            ResultSet rsCount = stmt.executeQuery(sqlDataCount)){
            dataCount = rsCount.getInt(1);

            if(dataCount < 30){
                sql = "select X from USERDATA limit 30 offset 0";
            }else{
                sql = "select X from USERDATA limit 30 offset "+(dataCount-29);
            }

            try(ResultSet rs = stmt.executeQuery(sql)){
                while (rs.next()) {
                    data.add(rs.getInt(1));
                }
            }catch (SQLException e){
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

//    public static void close() {
//        try {
//            DBconn.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        System.out.println("DB Close");
//    }
}
