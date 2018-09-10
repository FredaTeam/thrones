package org.freda.thrones.framework.manager;

import java.io.Serializable;
import java.util.Map;

/**
 * Create on 2018/9/3 15:41
 */
public interface Result extends Serializable {

    /**
     * Get invoke result.
     *
     * @return result. if no result return null.
     */
    Object getValue();

    /**
     * Get exception.
     *
     * @return exception. if no exception return null.
     */
    Throwable getException();

    /**
     * Has exception.
     *
     * @return has exception.
     */
    boolean hasException();

    /**
     * throw exception if there is or return the result
     */
    Object recreate() throws Throwable;

    /**
     * get attachments.
     *
     * @return attachments.
     */
    Map<String, String> getAttachments();

    /**
     * add the specified map to existing attachments in this instance.
     */
    void addAttachments(Map<String, String> map);

    /**
     * replace the existing attachments with the specified param.
     */
    void setAttachments(Map<String, String> map);

    /**
     * get attachment by key.
     */
    String getAttachment(String key);

    /**
     * get attachment by key with default value.
     */
    String getAttachment(String key, String defaultValue);

    /**
     * set attachement by key & value
     */
    void setAttachment(String key, String value);
}
