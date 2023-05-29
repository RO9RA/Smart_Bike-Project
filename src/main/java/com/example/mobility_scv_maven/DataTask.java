package com.example.mobility_scv_maven;
import javafx.concurrent.Task;

import java.io.*;

public class DataTask extends Task<Void> {
    static char dataSignal;
    //static int dataSignal;
    @Override
    protected Void call() throws Exception {
        //running() checking How to use - 05.11
        System.out.println("Start thread_data");

        // DB Connect-init Code
        try{
            // Connect DB
            DBHandler.connect();
            // Connect DBTable
            DBHandler.createTable();
            //Init DBTable
            DBHandler.InitTable();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        InputStreamReader isr = new InputStreamReader(RemoteDevice.in);
        BufferedReader br = new BufferedReader(isr);
        boolean check=false;

        while(!isCancelled()){
            if(dataSignal=='o'&&check){
                if(RemoteDevice.in.available()>0){
                    try {
                        //문자열 한줄 입력받기
                        String strRead = br.readLine();
                        if(!strRead.equals("o")){
                            String data[];
                            data = strRead.split(",");
                            System.out.println("dd"+data[0]);
                            System.out.println("cc"+data[1]);
                            DBHandler.InsertDataRead(data);
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }else{
                RemoteDevice.out.write(dataSignal);
                String strRead = br.readLine();
                System.out.println(strRead);
                if(strRead.equals("o")){
                    check = true;
                }else{
                    check=false;
                }
            }
        }
        System.out.println("error point dataTask");
        return null;
    }
}