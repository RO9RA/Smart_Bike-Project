package com.example.mobility_scv_maven;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainController implements Initializable {
    @FXML private BorderPane pane;
    @FXML private Label lblDate,lblSpeed;
    @FXML private JFXButton export_btn,str_btn,stp_btn;
    @FXML private LineChart lineChart;

    Thread threadData,threadChart,threadTime;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        TimeTask timeTask = new TimeTask(lblDate);
        threadTime = new Thread(timeTask);
        threadTime.setDaemon(true);
        threadTime.start();

        DataTask dataTask = new DataTask();
        threadData = new Thread(dataTask);
        ChartTask chartTask = new ChartTask(lineChart,lblSpeed);
        threadChart = new Thread(chartTask);
    }

    @FXML
    private void click_start() throws IOException {

        DataTask.dataSignal=1;
        //멀티 스래드를 사용
        //background Service 지정 - Data receive Thread 시작
        threadData.setDaemon(true);
        threadData.start();
        threadChart.setDaemon(true);
        threadChart.start();
        //btn state
        str_btn.setDisable(true);
        export_btn.setDisable(true);
    }

    @FXML
    private void click_stop() throws IOException{
        //Thread_state=false;
        DataTask.dataSignal=0;

        str_btn.setDisable(false);
        export_btn.setDisable(false);
        System.out.println("stop");
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