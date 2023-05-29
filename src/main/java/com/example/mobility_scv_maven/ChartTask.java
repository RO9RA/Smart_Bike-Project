package com.example.mobility_scv_maven;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.shape.Arc;

import java.sql.SQLException;
import java.util.ArrayList;

public class ChartTask extends Task {
    private Label lblSpeed,lblDistance,maxSpeed;
    private Arc speedArc;
    ChartTask(Label lblSpeed,Label lblDistacne, Label maxSpeed,Arc speedArc) {
        this.lblSpeed = lblSpeed;
        this.lblDistance = lblDistacne;
        this.maxSpeed = maxSpeed;
        this.speedArc = speedArc;
    }

    @Override
    protected Object call() throws InterruptedException {

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("chart thread start");

        while (!isCancelled()) {
            Platform.runLater(() -> {
                ArrayList<String> data;
                try {
                    data = DBHandler.speed_DIstance_data();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                lblSpeed.setText(data.get(0));
                maxSpeed.setText(DBHandler.Max_data());
                lblDistance.setText(data.get(1));

                double length = Double.parseDouble(data.get(0))/50.0 * 270; // 진행 상태에 따른 길이 계산
                if(length != 0){
                    speedArc.setLength(length);
                }else{
                    speedArc.setLength(1.0);
                }
            });
            Thread.sleep(500);
        }

        return null;
    }
}

