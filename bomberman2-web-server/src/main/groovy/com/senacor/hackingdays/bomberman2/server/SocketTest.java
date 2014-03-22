package com.senacor.hackingdays.bomberman2.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author Jochen Mader
 */
public class SocketTest {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", 2000), 1000);
        socket.getOutputStream().write("hallo\n".getBytes());
        socket.getOutputStream().flush();
        BufferedReader in =
                new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
        String command = in.readLine();
        System.out.println("received: "+command);
    }
}
