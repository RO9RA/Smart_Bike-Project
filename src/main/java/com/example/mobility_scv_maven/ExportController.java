package com.example.mobility_scv_maven;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class ExportController {

    @FXML public ListView<String> Device_list;
    @FXML
    public Label lblname,lbladdress;
    public Vector<javax.bluetooth.RemoteDevice> rv_Device = new Vector();

    ArrayList<String> list=new ArrayList<>();

    public void Phone_Searching() throws IOException, InterruptedException {
        RemoteDevice.RemoteDeviceDiscovery();
        rv_Device = RemoteDevice.ListItemDevice();

        list.clear();
        for(javax.bluetooth.RemoteDevice Address: rv_Device) {
            try{
                list.add(Address.getFriendlyName(false));
            }catch (IOException e){
                list.add(Address.getBluetoothAddress());
            }
        }
        Device_list.setItems(FXCollections.observableArrayList(list));
        System.out.println("Finished Searching");
    }

    public void Phone_Conn() throws IOException {
        String name = Device_list.getSelectionModel().getSelectedItem();
        rv_Device = RemoteDevice.ListItemDevice();

        for(javax.bluetooth.RemoteDevice Address: rv_Device) {
            if(Address.getBluetoothAddress().contains(name) || Address.getFriendlyName(false).contains(name)){
                if(RemoteDevice.ConnectDevice(Address.getBluetoothAddress(),"2")){
                    //사용할 DB 넘기는 코드
                    lblname.setText(Address.getFriendlyName(false));
                    lbladdress.setText(Address.getBluetoothAddress());
                    System.out.println("Connected");
                    break;
                }else{
                    System.out.println("Not Connected");
                    break;
                }
            }else{
                System.out.println("no equal");
            }
        }
    }

    public void Phone_DisConn() throws IOException {
        RemoteDevice.Disconnect();
    }
}
