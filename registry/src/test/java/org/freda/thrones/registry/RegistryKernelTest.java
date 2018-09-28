package org.freda.thrones.registry;

import com.google.common.collect.Lists;
import org.freda.thrones.framework.manager.Kernel;
import org.freda.thrones.framework.manager.support.ThronesKernel;
import org.freda.thrones.common.utils.WrapperUtils;
import org.junit.Test;

/**
 * Create on 2018/9/26 17:54
 */
public class RegistryKernelTest {

    @Test
    public void test_export() throws Throwable {

        System.out.println(RegistryKernel.class.getPackage().getName());
        Kernel kernel = WrapperUtils.createInstance(ThronesKernel.class, Lists.newArrayList("org.freda.thrones.registry"));
        kernel.export(null);

    }
}