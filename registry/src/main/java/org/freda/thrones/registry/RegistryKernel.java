package org.freda.thrones.registry;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.exceptions.RpcException;
import org.freda.thrones.framework.manager.Kernel;
import org.freda.thrones.framework.manager.export.Exporter;
import org.freda.thrones.framework.manager.invoke.Invoker;

/**
 * Create on 2018/9/26 17:50
 */
public class RegistryKernel implements Kernel {

    private final Kernel kernel;

    public RegistryKernel(Kernel kernel) {
        this.kernel = kernel;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        System.out.println("registry kernel export");
        return null;
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        System.out.println("registry kernel refer");
        return null;
    }

    @Override
    public void destroy() {

    }
}
