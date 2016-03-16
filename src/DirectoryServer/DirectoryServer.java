/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package DirectoryServer;

import java.io.IOException;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.Channel;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Justin
 * UDP and TCP Simultaneous listening logic referenced
 * http://www.java2s.com/Code/Java/Network-Protocol/HandlesTCPandUDPconnectionsandprovidesexceptionhandlinganderrorlogging.htm
 */
public class DirectoryServer {
    
    
   public static void main(String args[]) throws IOException {
   
      CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder();

      int port = 13; 
      if (args.length > 0)
        port = Integer.parseInt(args[0]);

      SocketAddress localport = new InetSocketAddress(port);

      //tcp channel
      ServerSocketChannel tcpserver = ServerSocketChannel.open();
      tcpserver.socket().bind(localport);

      //udp channel
      DatagramChannel udpserver = DatagramChannel.open();
      udpserver.socket().bind(localport);

      //selector object will do the blocking
      tcpserver.configureBlocking(false);
      udpserver.configureBlocking(false);

      Selector selector = Selector.open();

      tcpserver.register(selector, SelectionKey.OP_ACCEPT);
      udpserver.register(selector, SelectionKey.OP_READ);

      ByteBuffer receiveBuffer = ByteBuffer.allocate(0);

      // Now loop forever, processing client connections
      while (true) {
        try { 
            
          // Wait for a client to connect
          selector.select();

          String date = new java.util.Date().toString() + "\r\n";
          ByteBuffer response = encoder.encode(CharBuffer.wrap(date));

          // Get the SelectionKey objects for the channels that have
          // activity on them. These are the keys returned by the
          // register() methods above. They are returned in a
          // java.util.Set.
          Set keys = selector.selectedKeys();

          // Iterate through the Set of keys.
          for (Iterator i = keys.iterator(); i.hasNext();) {
            // Get a key from the set, and remove it from the set
            SelectionKey key = (SelectionKey) i.next();
            i.remove();

            // Get the channel associated with the key
            Channel c = (Channel) key.channel();

            // Now test the key and the channel to find out
            // whether something happend on the TCP or UDP channel
            if (key.isAcceptable() && c == tcpserver) {
              // A client has attempted to connect via TCP.
              // Accept the connection now.
              SocketChannel client = tcpserver.accept();
              // If we accepted the connection successfully,
              // the send our respone back to the client.
              if (client != null) {
                //TODO: TCP thread logic
                client.close(); // close connection
              }
            } else if (key.isReadable() && c == udpserver) {
              // A UDP datagram is waiting. Receive it now,
              // noting the address it was sent from.
              SocketAddress clientAddress = udpserver.receive(receiveBuffer);
              // If we got the datagram successfully, send
              // the date and time in a response packet.
              if (clientAddress != null)
                udpserver.send(response, clientAddress);
              //TODO: UDP thread logic
            }
          }
        } catch (java.io.IOException e) {
        } 
      }
    } 
  }

    
    

