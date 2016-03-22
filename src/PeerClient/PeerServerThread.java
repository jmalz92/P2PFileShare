/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PeerClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Justin
 */
public class PeerServerThread extends Thread
{
    
    private static final int BUFFER_SIZE = 32768;

    public PeerServerThread() {
        super("PeerServerThread");
    }

    @Override
    public void run() {
        try {
            int port = 8000; //change this at some point

            try {

                ServerSocket socket = new ServerSocket(port);
                System.out.println("Started on port: " + port);
                
                while (true) {
                    //a download request has occurred
                    //new PeerServerThread(socket.accept()).start();
                    //start peer downloader handler
                }

            } catch (IOException e) {
                System.err.println("Could not listen on port: " + port);
                System.err.println(e.getMessage());
                System.exit(-1);
            }
        } catch (NumberFormatException e) {
            System.out.println("Please supply a valid port number");
        }
    }
}
