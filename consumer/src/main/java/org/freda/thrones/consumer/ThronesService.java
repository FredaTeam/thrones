package org.freda.thrones.consumer;


import java.util.List;

/**
 * @author lixiaoyu15@meituan.com
 * @time 下午4:47
 * @Package org.freda.thrones.consumer
 * @Describtion
 * define the remote service will construct from the configuration files
 *
 * TODO need an Endpoint type and the Schema type
 * @file ThronesService.java
 */
public interface ThronesService {

    /**
     * define the service endpoint
     * @return
     */
    public String getEndpoint();

    /**
     * get the service name dotted split style
     * @return
     */
    public String getServiceName();

    /**
     * get the protocol name
     * @return
     */
    public String getSchema();

    /**
     * get all parameters
     * @return
     */
    public List<Parameter> getParameters();

    /**
     * get the signature
     * @return
     */
    public String getSignature();


    /**
     * define the parameter of the remote service
     */
    public class Parameter{

        private Object parameter;

        private Class parameterType;

        public Parameter(Object parameter, Class parameterType) {
            this.parameter = parameter;
            this.parameterType = parameterType;
        }

        public Object getParameter() {
            return parameter;
        }

        public Class getParameterType() {
            return parameterType;
        }
    }

}
