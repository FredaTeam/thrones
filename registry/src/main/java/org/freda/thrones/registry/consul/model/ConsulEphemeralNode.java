package org.freda.thrones.registry.consul.model;

import com.orbitz.consul.model.session.ImmutableSession;
import com.orbitz.consul.model.session.Session;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.freda.thrones.framework.common.URL;
import org.freda.thrones.registry.consul.utils.ConsulUrlUtil;

/**
 * consul ephemeral node
 * Create on 2018/9/30 08:41
 */
public final class ConsulEphemeralNode {

    private static final String LOCK_DELAY = "15s";

    private final URL url;

    private final String interval;

    private final ConsulRoleType roleType;

    private ConsulEphemeralNode(ConsulEphemeralNodeBuilder builder) {
        this.url = builder.url;
        this.interval = builder.interval;
        this.roleType = builder.roleType;
    }

    public Session newConsulSession() {
        return ImmutableSession.builder()
                .name(getSessionName())
                .lockDelay(LOCK_DELAY)
                .behavior(ConsulBehavior.DELETE.getValue())
                .ttl(this.interval + "s")
                .build();
    }

    public String getSessionName() {
        return roleType.name() + "_" + url.getHost() + "_" + url.getPort();
    }

    public String getEphemeralNodeKey() {
        return ConsulUrlUtil.ephemralNodePath(url, roleType);
    }

    public String getEphemeralNodeValue() {
        return url.toFullString();
    }

    public static final class ConsulEphemeralNodeBuilder {
        private URL url;
        private String interval;
        private ConsulRoleType roleType;

        private ConsulEphemeralNodeBuilder() {
        }

        public static ConsulEphemeralNodeBuilder of() {
            return new ConsulEphemeralNodeBuilder();
        }

        public ConsulEphemeralNodeBuilder url(URL url) {
            this.url = url;
            return this;
        }

        public ConsulEphemeralNodeBuilder interval(String interval) {
            this.interval = interval;
            return this;
        }

        public ConsulEphemeralNodeBuilder roleType(ConsulRoleType roleType) {
            this.roleType = roleType;
            return this;
        }

        public ConsulEphemeralNode build() {
            return new ConsulEphemeralNode(this);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsulEphemeralNode that = (ConsulEphemeralNode) o;

        if (url != null ? !url.equals(that.url) : that.url != null) return false;
        if (interval != null ? !interval.equals(that.interval) : that.interval != null) return false;
        return roleType == that.roleType;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (interval != null ? interval.hashCode() : 0);
        result = 31 * result + (roleType != null ? roleType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("url", url)
                .append("interval", interval)
                .append("roleType", roleType)
                .toString();
    }
}
