package com.baturu.simpleDemo.rpc.exampleImpl;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by xuran on 16/4/19.
 */
public class RpcTest {
    public static void main(String[] args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RpcExporter.exporter("localhost", 8088);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        RpcImporter<EchoService> importer = new RpcImporter<EchoService>();
        EchoService echo = importer.importer("echoService", EchoService.class, new InetSocketAddress("localhost", 8088));
        System.out.println(echo.echo("Are you ok ?"));
        System.out.println(echo.echo("Where are you ?"));

        final int length=100;
        String host="localhost";
        int port=8088;

        Socket[] sockets=new Socket[length];
        for(int i = 0; i < 100; i++){
            sockets[i]=new Socket(host, port);
            System.out.println("第"+(i+1)+"次连接成功");
        }
        Thread.sleep(3000);
        for(int i = 0; i < 100; i++){
            sockets[i].close();      //断开连接
        }
    }
}
