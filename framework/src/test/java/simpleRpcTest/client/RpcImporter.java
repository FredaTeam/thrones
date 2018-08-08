package simpleRpcTest.client;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Create on 2018/8/7 17:04
 */
public class RpcImporter<S> {

    @SuppressWarnings({"unchecked"})
    public S importer(final Class<?> serviceClass, InetSocketAddress address){
        return (S) Proxy.newProxyInstance(serviceClass.getClassLoader(),
                new Class<?>[]{serviceClass.getInterfaces()[0]},
                (proxy, method, args) -> {
                    Socket socket = null;
                    ObjectInputStream objectInputStream = null;
                    ObjectOutputStream objectOutputStream = null;
                    try {
                        socket = new Socket();
                        socket.connect(address);
                        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeUTF(serviceClass.getName());
                        objectOutputStream.writeUTF(method.getName());
                        objectOutputStream.writeObject(method.getParameterTypes());
                        objectOutputStream.writeObject(args);
                        objectInputStream = new ObjectInputStream(socket.getInputStream());
                        return objectInputStream.readObject();
                    } finally {
                        if(socket!=null){
                            socket.close();
                        }
                        if(objectOutputStream!=null){
                            objectOutputStream.close();
                        }
                        if(objectInputStream!=null){
                            objectInputStream.close();
                        }
                    }
                });
    }

}
