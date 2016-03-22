/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PeerClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Justin
 */
public class PeerClientThread extends Thread {
    
    File[] files;
    String ipAddress;
    int port;
    OutputStreamWriter serverOut;
    BufferedReader serverIn;
    Socket socket;
    
     public PeerClientThread(File[] files, int port, String ipAddress) 
     {
        super("PeerClientThread");
        this.files = files;
        this.port = port;
        this.ipAddress = ipAddress;
     }

    @Override
    public void run() 
    {
        connectToDirectoryServer();
        
        while(true){
            //perform requests
        }
    }
    
    public String[] SearchFile(String fileName){
        try {
            if (socket.isConnected()) {

                serverOut.write("Search\n");
                serverOut.flush();
                serverOut.write(fileName);
                serverOut.write("\n");
                serverOut.flush();

                while (!serverIn.ready()) 
                {
                }
                String ips = serverIn.readLine();
                System.out.println(ips);
                
                
                return null;
                
            } else {
                return null;
            }
        } catch (IOException ioe) {
            return null;
        }
    }
    
    private void connectToDirectoryServer()
    {
        try{
	    socket = new Socket(InetAddress.getByName(ipAddress), port);
            serverOut = new OutputStreamWriter(socket.getOutputStream());
	    serverIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            // send list of files
	     serverOut.write(files.length + "");
	     serverOut.write("\n");
	     serverOut.flush();
            // send file names to server
	     for(File f : files){
	         serverOut.write(f.getName());
		 serverOut.write("\n");
		 serverOut.flush();
	     }
             
             //start message thread to directory server
            startUDPMessageThread(port, ipAddress);
        
            //start Peer Server
             new PeerServerThread().start();
            
	 }catch(Exception e){
	     e.printStackTrace();
	 } 
    }
    
    private void startUDPMessageThread(int port, String ipAddress){
        //schedule UDP Message threads
        try {
            final InetAddress ia = InetAddress.getByName(ipAddress);
            final int p = port;
            final DatagramSocket socket = new DatagramSocket();
            ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {

                    System.out.println("Sending UDP Message");

                    byte[] message = {'H', 'E', 'L', 'L', 'O'};
                    try{
                        DatagramPacket packet = new DatagramPacket(message, message.length, ia, p);
                        socket.send(packet);
                    }
                    catch(IOException ioe){
                        ioe.printStackTrace();
                    }
                    

                }
            }, 60, 60, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        
    }
}
