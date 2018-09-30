package org.freda.thrones.registry.consul.model;

import com.google.common.base.Preconditions;
import com.orbitz.consul.model.agent.ImmutableRegistration;
import com.orbitz.consul.model.agent.Registration;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static java.util.Collections.unmodifiableSet;

/**
 * consul registration
 * <p>
 * Create on 2018/9/29 16:28
 */
@Getter
public final class ConsulService {

    private final String id;

    private final String name;

    private final String address;

    private final Integer port;

    // additional info
    private final Set<String> tags;

    // heartbeat check time
    private final int interval;

    private ConsulService(ConsulServiceBuilder builder) {
        this.name = builder.name;
        this.id = builder.id != null ? builder.id : name + ":" + UUID.randomUUID().toString();
        this.address = builder.address;
        this.port = builder.port;
        this.tags = unmodifiableSet(new HashSet<String>(builder.tags));
        this.interval = builder.interval;
    }

    public Registration newConsulService() {
        return ImmutableRegistration.builder()
                .id(this.id)
                .name(this.name)
                .port(this.port)
                .check(Registration.RegCheck.ttl(interval))
                .tags(this.tags)
                .meta(Collections.singletonMap("version", "1.0"))
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConsulService that = (ConsulService) o;

        if (interval != that.interval) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (address != null ? !address.equals(that.address) : that.address != null) return false;
        if (port != null ? !port.equals(that.port) : that.port != null) return false;
        return tags != null ? tags.equals(that.tags) : that.tags == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + interval;
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("name", name)
                .append("address", address)
                .append("port", port)
                .append("tags", tags)
                .append("interval", interval)
                .toString();
    }

    public static final class ConsulServiceBuilder {
        private String id;
        private String name;
        private String address;
        private Integer port;
        // additional info
        private Set<String> tags;
        // heartbeat check time
        private int interval;

        private ConsulServiceBuilder() {
        }

        public static ConsulServiceBuilder of() {
            return new ConsulServiceBuilder();
        }

        public ConsulServiceBuilder id(String id) {
            this.id = id;
            return this;
        }

        public ConsulServiceBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ConsulServiceBuilder address(String address) {
            this.address = address;
            return this;
        }

        public ConsulServiceBuilder port(Integer port) {
            this.port = port;
            return this;
        }

        public ConsulServiceBuilder tags(Set<String> tags) {
            this.tags = tags;
            return this;
        }

        public ConsulServiceBuilder interval(int interval) {
            this.interval = interval;
            return this;
        }

        public ConsulService build() {
            Preconditions.checkArgument(StringUtils.isNotBlank(this.name),
                    "consul service name cannot be null");
            Preconditions.checkArgument(StringUtils.isNotBlank(this.id),
                    "consul service id cannot be null");
            return new ConsulService(this);
        }
    }
}
