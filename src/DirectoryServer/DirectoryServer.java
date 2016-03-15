/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DirectoryServer;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.ServerSocket;

/**
 *
 * @author Justin
 */
public class DirectoryServer {
    
    
    public static void main(String[] args) throws IOException {
        int port;

        try {
            port = Integer.parseInt(args[0]);

            try {
                ServerSocket tcpSocket = new ServerSocket(port);
                DatagramSocket udpSocket = new DatagramSocket(port);
                
                System.out.println("Directory Server Started on port: " + port);

                while (true) {
                    new DirectoryThread().start();
                }

            } catch (IOException e) {
                System.err.println("Could not listen on port: " + args[0]);
                System.err.println(e.getMessage());
                System.exit(-1);
            }
        } catch (NumberFormatException e) {
            System.out.println("Please supply a valid port number");
        }
    }
    
}
