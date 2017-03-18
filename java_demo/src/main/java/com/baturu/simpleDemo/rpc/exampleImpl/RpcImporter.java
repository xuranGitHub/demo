package com.baturu.simpleDemo.rpc.exampleImpl;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xuran on 16/4/19.
 */
public class RpcImporter<S> {

    private Map<String, String> interfaceImplMap = new HashMap<>();

    RpcImporter() {
        interfaceImplMap.put("echoService", "com.baturu.simpleDemo.rpc.exampleImpl.EchoServiceImpl");
    }

    public S importer(final String serviceName, final Class<?> interfaceClass, final InetSocketAddress address) {
        return (S) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        Socket socket = null;
                        ObjectOutputStream output = null;
                        ObjectInputStream input = null;
                        try {
                            socket = new Socket();
                            socket.connect(address);
                            output = new ObjectOutputStream(socket.getOutputStream());
                            output.writeUTF(interfaceImplMap.get(serviceName));
                            output.writeUTF(method.getName());
                            output.writeObject(method.getParameterTypes());
                            output.writeObject(args);
                            input = new ObjectInputStream(socket.getInputStream());
                            return input.readObject();
                        } finally {
                            if (socket != null) {
                                socket.close();
                            }
                            if (input != null) {
                                input.close();
                            }
                            if (output != null) {
                                output.close();
                            }
                        }
                    }
                });
    }
}
