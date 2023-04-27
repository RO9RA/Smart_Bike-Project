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
        String sql = "CREATE TABLE IF NOT EXISTS USERDATA (Lat TEXT, Long TEXT, Speed TEXT, X TEXT, Y TEXT, Z TEXT);";
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

    public static void insertData(Queue<String> data) {
        String sql = "INSERT INTO USERDATA (Lat,Long,Speed,X,Y,Z) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement pstmt = DBconn.prepareStatement(sql)) {
            while(data.peek() != null){
                String data_ch[] = DataTask.data.remove().split(":");
                if (data_ch[0].equals("Lat")) {
                    //System.out.println("Lat OK "+data_ch[1]);
                    pstmt.setString(1, data_ch[1]);
                } else if (data_ch[0].equals("Long")) {
                    //System.out.println("Long OK "+data_ch[1]);
                    pstmt.setString(2, data_ch[1]);
                } else if (data_ch[0].equals("Speed(kmph)")) {
                    //System.out.println("Speed OK "+data_ch[1]);
                    pstmt.setString(3, data_ch[1]);
                } else if (data_ch[0].equals("X")) {
                    //System.out.println("X OK "+data_ch[1]);
                    pstmt.setString(4, data_ch[1]);
                } else if (data_ch[0].equals("Y")) {
                    //System.out.println("Y OK "+data_ch[1]);
                    pstmt.setString(5, data_ch[1]);
                } else if (data_ch[0].equals("Z")) {
                    //System.out.println("Z OK "+data_ch[1]);
                    pstmt.setString(6, data_ch[1]);
                }
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String Max_data(){
        String data = null;
        String sql = "select MAX(Y) from USERDATA;";
        try(Statement stmt = DBconn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            data = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(data);
        return data;
    }

    public static ArrayList<Integer> speed_data(){
        ArrayList<Integer> data = new ArrayList<>();
        String sql = "select Y from USERDATA;";
        try(Statement stmt = DBconn.createStatement();
            ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                data.add(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return data;
    }

/*    public static void close() {
        try {
            DBconn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("DB Close");
    }*/
}
