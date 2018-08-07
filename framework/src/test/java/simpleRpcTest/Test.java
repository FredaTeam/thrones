package simpleRpcTest;

import simpleRpcTest.client.RpcImporter;
import simpleRpcTest.server.EchoService;
import simpleRpcTest.server.EchoServiceImpl;
import simpleRpcTest.server.RpcExporter;

import java.net.InetSocketAddress;

/**
 * Create on 2018/8/7 19:20
 */
public class Test {

    public static void main(String[] args) {
        new Thread(() -> {
            try {
                RpcExporter.exporter("localhost", 8088);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        RpcImporter<EchoService> importer = new RpcImporter<EchoService>();
        EchoService echo = importer.importer(EchoServiceImpl.class, new InetSocketAddress("localhost", 8088));
        System.out.println(echo.echo("PING"));
    }
}
