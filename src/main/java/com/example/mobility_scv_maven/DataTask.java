package com.example.mobility_scv_maven;

import com.sun.tools.javac.Main;
import javafx.concurrent.Task;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

public class DataTask extends Task<Void> {
    static String data[];
    static int dataSignal;

    @Override
    protected Void call() throws Exception {
        //running() checking How to use - 05.11
        System.out.println("Start thread_data");

        // DB Connect-init Code
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

        while(!isCancelled()){
            RemoteDevice.out.write(dataSignal);

            if(RemoteDevice.in.available()>=0){
                InputStreamReader isr = new InputStreamReader(RemoteDevice.in);
                BufferedReader br = new BufferedReader(isr);
                try {
                    //문자열 한줄 입력받기
                    String strRead = br.readLine();
                    data = strRead.split(",");
                    DBHandler.insertData(data);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
