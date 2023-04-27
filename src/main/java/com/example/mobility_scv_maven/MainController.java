package com.example.mobility_scv_maven;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.rmi.Remote;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController implements Initializable {
    @FXML private BorderPane pane;
    @FXML private Label lblDate,lblspeed;
    @FXML private JFXButton export_btn,str_btn,stp_btn;
    @FXML private LineChart LineChart;
    @FXML private NumberAxis xAxis;

    @FXML private ScrollPane scroll;


    Thread thread,thread_chart,thread_Data;
    long start_time,end_time;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        Thread thread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  /  HH : mm", Locale.KOREA);
            while(true) {
                String Time = sdf.format(new Date());
                Platform.runLater(()->{
                    lblDate.setText(Time);
                });
                try { Thread.sleep(100); } catch (InterruptedException e) {}
            }
        });
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void click_start(){
        start_time = System.currentTimeMillis();
        System.out.println("Start TIme : "+start_time);

        //멀티 스래드를 사용
        thread_Data = new Thread(DataTask.task);
        //background Service 지정
        thread_Data.setDaemon(true);
        //Data receive Thread 시작
        thread_Data.start();

        export_btn.setDisable(true);

        thread = new Thread(() -> {
            System.out.println("data thread start");
            while(!Thread.currentThread().isInterrupted()){
                Platform.runLater(()->{
                    lblspeed.setText(DBHandler.Max_data());
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        thread_chart = new Thread(() -> {
            System.out.println("chart thread start");
            XYChart.Series<Number, Number> series1 = new XYChart.Series<>();
            ArrayList<Integer> speed = DBHandler.speed_data();

            for(int i = 0; i < speed.size(); i++){
                series1.getData().add(new XYChart.Data<>(i+1, speed.get(i)));
            }

            Platform.runLater(()->{
                LineChart.getData().add(series1);
            });

            while(!Thread.currentThread().isInterrupted()){
                speed = DBHandler.speed_data();
                ArrayList<Integer> finalSpeed = speed;

                Platform.runLater(()->{

                    series1.getData().clear();

                    for(int i = 0; i < finalSpeed.size(); i++){
                        series1.getData().add(new XYChart.Data<>(i+1, finalSpeed.get(i)));
                    }

                    if(finalSpeed.size() < 30){
                        xAxis.setUpperBound(30);
                        LineChart.setPrefWidth(30*20);
                    }else{
                        xAxis.setUpperBound(finalSpeed.size()+3);
                        LineChart.setPrefWidth((finalSpeed.size()+3)*20);
                        scroll.setHvalue(1.0d);
                    }
                });

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
        thread.setDaemon(true);
        thread.start();
        thread_chart.setDaemon(true);
        thread_chart.start();
    }

    @FXML
    private void click_stop() throws IOException {
        //Thread.sleep(00);
        thread.stop();
        thread_chart.stop();
        thread_Data.stop();
        RemoteDevice.out.write(0);

        export_btn.setDisable(false);
        System.out.println("stop");
        end_time=System.currentTimeMillis();
    }

    @FXML
    private void click_Export() throws IOException {
        loadPage("Export");
        RemoteDevice.Disconnect();
        str_btn.setDisable(true);
        stp_btn.setDisable(true);

    }

    private void loadPage(String page) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(page + ".fxml"));
            pane.setCenter(root);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}