package com.baturu.simpleDemo.net.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by xuran on 16/4/23.
 */
public class SocketTest {

    static Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static void main(String[] args) throws InterruptedException, IOException {

        ServerSocket server = new ServerSocket(8088, 3);
//        server.bind(new InetSocketAddress("localhost", 8088));


        final int length=100;
        String host="localhost";
        int port=8088;

        Socket[] sockets=new Socket[length];
        for(int i = 0; i < 100; i++){
            try {
                sockets[i]=new Socket(host, port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("第"+(i+1)+"次连接成功");
        }
        Thread.sleep(1000);
        for(int i = 0; i < 100; i++){
            sockets[i].close();      //断开连接
        }
    }
}
