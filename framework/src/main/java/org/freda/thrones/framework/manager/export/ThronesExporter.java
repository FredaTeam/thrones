package org.freda.thrones.framework.manager.export;

import org.freda.thrones.framework.manager.invoke.Invoker;

import java.util.Map;

/**
 * Create on 2018/9/4 22:52
 */
public class ThronesExporter<T> extends AbstractExporter<T> {

    private final String serviceKey;

    private final Map<String, Exporter<?>> exporterMap;

    public ThronesExporter(Invoker<T> invoker, String serviceKey, Map<String, Exporter<?>> exporterMap) {
        super(invoker);
        this.serviceKey = serviceKey;
        this.exporterMap = exporterMap;
    }

    @Override
    public void unexport() {
        super.unexport();
        exporterMap.remove(serviceKey);
    }
}
