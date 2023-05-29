package com.example.mobility_scv_maven;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeTask extends Task {
    private Label lblDate;
    TimeTask(Label lblDate) {
        this.lblDate = lblDate;
    }
    @Override
    protected Object call(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  /  HH : mm", Locale.KOREA);
        while(true) {
            String Time = sdf.format(new Date());
            Platform.runLater(()->{
                lblDate.setText(Time);
            });
            try { Thread.sleep(100); } catch (InterruptedException e) {}
        }
    }
}
