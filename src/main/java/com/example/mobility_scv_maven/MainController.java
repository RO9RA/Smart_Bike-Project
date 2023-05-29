package com.example.mobility_scv_maven;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.shape.Arc;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController implements Initializable {
    @FXML private BorderPane pane;
    @FXML private Label lblDate,lblSpeed,lblDistance,maxSpeed;
    @FXML private JFXButton str_btn,end_btn,stp_btn;
    @FXML private Arc speedArc;

    Thread threadData,threadChart,threadTime;
    TimeTask timeTask;
    DataTask dataTask;
    ChartTask chartTask;

    @Override
    public void initialize(URL location, ResourceBundle resources){

        end_btn.setDisable(true);
        stp_btn.setDisable(true);

        timeTask = new TimeTask(lblDate);
        threadTime = new Thread(timeTask);
        threadTime.setDaemon(true);
        threadTime.start();

        dataTask = new DataTask();
        threadData = new Thread(dataTask);
        chartTask = new ChartTask(lblSpeed,lblDistance,maxSpeed,speedArc);
        threadChart = new Thread(chartTask);
    }

    @FXML
    private void click_start() throws IOException {

        DataTask.dataSignal='o';
        //멀티 스래드를 사용
        //background Service 지정 - Data receive Thread 시작
        threadData.setDaemon(true);
        threadData.start();
        threadChart.setDaemon(true);
        threadChart.start();
        //btn state
        str_btn.setDisable(true);
        stp_btn.setDisable(false);
        end_btn.setDisable(true);
    }

    @FXML
    private void click_stop() throws IOException{
        DataTask.dataSignal='x';
        dataTask.cancel();
        chartTask.cancel();
        end_btn.setDisable(false);
        System.out.println("stop");
    }

    @FXML
    private void click_end() throws IOException {
        DBHandler.close();
        RemoteDevice.Disconnect();
    }
}