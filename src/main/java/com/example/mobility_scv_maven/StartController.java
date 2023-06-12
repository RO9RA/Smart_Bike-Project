package com.example.mobility_scv_maven;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class StartController {
    @FXML private Label btStatelbl;
    public void OnConnectAction(MouseEvent e) throws IOException, InterruptedException {
        RemoteDevice.RemoteDeviceDiscovery();
        if(RemoteDevice.ConnectDevice("98D33790AEE5","1")){
            Node node=(Node) e.getSource();
            Stage stage=(Stage) node.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("Page_main.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }else{
            btStatelbl.setText("< Not Connected Please Retry > ");
            System.out.println("Not Connected");
        }
    }
}


