package com.example.mobility_scv_maven;

import com.sun.tools.javac.Main;
import javafx.concurrent.Task;

import java.io.*;
import java.util.*;

public class DataTask {
    static Queue<String> data = new LinkedList<>();
    static Task<Void> task = new Task<>() {
        @Override
        protected Void call() throws IOException {
            try{
                // Connect DB
                DBHandler.connect();
                //Init DBTable
                DBHandler.InitTable();
                // Connect DBTable
                DBHandler.createTable();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            // Code
            System.out.println("Start thread_data");

            InputStreamReader isr = new InputStreamReader(RemoteDevice.in);
            BufferedReader br = new BufferedReader(isr);
            while(MainController.Thread_state){
                try {
                    //문자열 한줄 입력받기
                    String bytesRead = br.readLine();
                    if(bytesRead.equals(" ")){
                        DBHandler.insertData(data);
                    }else{
                        data.add(bytesRead);
                        //System.out.println("  : "+bytesRead);
                    }
                    RemoteDevice.conn.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return null;
        }
    };
}
