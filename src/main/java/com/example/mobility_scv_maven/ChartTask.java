package com.example.mobility_scv_maven;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.sql.SQLException;
import java.util.ArrayList;

public class ChartTask extends Task {
    private LineChart lineChart;
    private Label lblSpeed;
    ChartTask(LineChart<String, Number> lineChart, Label lblSpeed) {
        this.lineChart = lineChart;
        this.lblSpeed = lblSpeed;
    }
    @Override
    protected Object call() throws SQLException {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("chart thread start");
        XYChart.Series series1 = new XYChart.Series();

        while(!isCancelled()) {
            ArrayList<Integer> finalSpeed = DBHandler.speed_data();

            Platform.runLater(() -> {
                if(finalSpeed.size() <= 30){
                    for (int i = 0; i < finalSpeed.size(); i++) {
                        series1.getData().add(new XYChart.Data<>(i + 1, finalSpeed.get(i)/75));
                    }
                }

                lineChart.setAnimated(true);
                if(lineChart.getData().isEmpty()) {
                    System.out.println("is Empty");
                    lineChart.getData().add(series1);
                    //this.lineChart.setAnimated(false);
                }
                series1.getData().clear();
                lineChart.setAnimated(false);

                lblSpeed.setText(DBHandler.Max_data());
            });

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}
