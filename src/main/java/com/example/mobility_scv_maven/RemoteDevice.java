package com.example.mobility_scv_maven;

import javax.bluetooth.*;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import java.io.*;
import java.util.Vector;

public class RemoteDevice{
    public static final Vector<javax.bluetooth.RemoteDevice> devicesDiscovered = new Vector();
    static StreamConnection conn;
    static InputStream in;
    static OutputStream out;


    //Bluetooth DB Searching Code
    public static void RemoteDeviceDiscovery() throws IOException, InterruptedException {

        final Object inquiryCompletedEvent = new Object();

        devicesDiscovered.clear();
        DiscoveryListener listener = new DiscoveryListener() {
            @Override
            public void deviceDiscovered(javax.bluetooth.RemoteDevice remoteDevice, DeviceClass deviceClass) {
                System.out.println("Device " + remoteDevice.getBluetoothAddress() + "found");
                devicesDiscovered.addElement(remoteDevice);
                try {
                    System.out.println(" name " + remoteDevice.getFriendlyName(false));
                } catch (IOException cantGetDeviceName) {
                }
            }
            @Override
            public void inquiryCompleted(int i) {
                System.out.println("Device Inquiry completed!");
                synchronized (inquiryCompletedEvent) {
                    inquiryCompletedEvent.notify();
                }
            }
            @Override
            public void servicesDiscovered(int i, ServiceRecord[] serviceRecords) {
            }
            @Override
            public void serviceSearchCompleted(int i, int i1) {
            }
        };

        synchronized (inquiryCompletedEvent) {
            boolean started = LocalDevice.getLocalDevice().getDiscoveryAgent().startInquiry(DiscoveryAgent.GIAC, listener);
            if(started){
                System.out.println("wait for device inquiry to complete .....");
                inquiryCompletedEvent.wait();
                System.out.println(devicesDiscovered.size() + "   device(s) found");
            }
        }
    }

    //Device List Receive Code
    public static Vector<javax.bluetooth.RemoteDevice> ListItemDevice(){
        return devicesDiscovered;
    }

    //Device Connect Code
    public static Boolean ConnectDevice(String mac_address,String PortNum){
        Boolean Check;
        try{
            conn = (StreamConnection) Connector.open("btspp://" + mac_address +":"+PortNum);
            in = conn.openInputStream();
            out = conn.openOutputStream();
            Check = true;
        }catch (IOException e){
            System.out.println("not Connect");
            Check = false;
        }
        return Check;
    }

/*    public static void SendDB(byte[] bytes) throws IOException {
        Socket
        out.write(bytes);
        out.flush();
    }*/

    //Device Disconnect Code
    public static void Disconnect() throws IOException
    {
        in.close();
        out.close();
        conn.close();
        in = null;
        out = null;
        conn = null;
    }
}
