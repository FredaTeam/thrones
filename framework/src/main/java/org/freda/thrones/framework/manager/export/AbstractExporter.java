package org.freda.thrones.framework.manager.export;

import com.google.common.base.Preconditions;
import org.freda.thrones.framework.manager.invoke.Invoker;

/**
 * Create on 2018/9/4 10:59
 */
public abstract class AbstractExporter<T> implements Exporter<T> {

    private final Invoker<T> invoker;

    private volatile boolean unexported = false;

    public AbstractExporter(Invoker<T> invoker) {
        Preconditions.checkNotNull(invoker, "invoker can not be null");
        Preconditions.checkNotNull(invoker.getInterface(), "interface can not be null");
        Preconditions.checkNotNull(invoker.getUrl(), "url can not be null");

        this.invoker = invoker;
    }

    @Override
    public Invoker<T> getInvoker() {
        return invoker;
    }

    @Override
    public void unexport() {
        if (unexported) {
            return;
        }
        unexported = true;
        getInvoker().destroy();
    }
}
