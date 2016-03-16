/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PeerClient;

import java.net.Socket;

/**
 *
 * @author Justin
 */
public class PeerServerThread extends Thread
{
    
    private Socket socket = null;
    private static final int BUFFER_SIZE = 32768;

    public PeerServerThread(Socket socket) {
        super("PeerServerThread");
        this.socket = socket;
    }

    @Override
    public void run() {
    
    }
}
