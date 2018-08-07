package simpleRpcTest.server;

import java.io.Serializable;

/**
 * Create on 2018/8/7 16:58
 */
public class EchoServiceImpl implements EchoService,Serializable {

    @Override
    public String echo(String ping) {
        return ping != null ? ping + " --> OK" : "OK";
    }
}
