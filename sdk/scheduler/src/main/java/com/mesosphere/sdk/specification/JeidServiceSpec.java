package com.mesosphere.sdk.specification;

import java.util.List;
import java.util.ArrayList;
import com.mesosphere.sdk.config.ConfigurationComparator;
import com.mesosphere.sdk.config.ConfigurationFactory;
import com.mesosphere.sdk.config.SerializationUtils;
import com.mesosphere.sdk.config.TaskEnvRouter;
import com.mesosphere.sdk.dcos.DcosConstants;
import com.mesosphere.sdk.offer.evaluate.placement.*;
import com.mesosphere.sdk.scheduler.SchedulerConfig;
import com.mesosphere.sdk.specification.validation.UniquePodType;
import com.mesosphere.sdk.specification.validation.ValidationUtils;
import com.mesosphere.sdk.specification.yaml.RawServiceSpec;
import com.mesosphere.sdk.specification.yaml.YAMLToInternalMappers;
import com.mesosphere.sdk.state.ConfigStoreException;
import com.mesosphere.sdk.storage.StorageError.Reason;

public class JeidServiceSpec extends DefaultServiceSpec {
    public JeidServiceSpec(Builder builder) {
        super(
                builder.name,
                builder.role,
                builder.principal,
                builder.webUrl,
                builder.zookeeperConnection,
                builder.pods,
                builder.replacementFailurePolicy,
                builder.user);
    }
    public static Builder newBuilder(ServiceSpec copy, List<PodSpec> pods) {
        Builder builder = new Builder();
        builder.name = copy.getName();
        builder.role = copy.getRole();
        builder.principal = copy.getPrincipal();
        builder.zookeeperConnection = copy.getZookeeperConnection();
        builder.webUrl = copy.getWebUrl();
        builder.pods = pods;
        builder.replacementFailurePolicy = copy.getReplacementFailurePolicy().orElse(null);
        builder.user = copy.getUser();
        System.out.println("made copy of servicespec");
        return builder;
    }
    public static final class Builder {
        private String name;
        private String role;
        private String principal;
        private String webUrl;
        private String zookeeperConnection;
        private List<PodSpec> pods = new ArrayList<>();
        private ReplacementFailurePolicy replacementFailurePolicy;
        private String user;

        private Builder() {
        }

        /**
         * Sets the {@code name} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param name the {@code name} to set
         * @return a reference to this Builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the {@code role} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param role the {@code role} to set
         * @return a reference to this Builder
         */
        public Builder role(String role) {
            this.role = role;
            return this;
        }

        /**
         * Sets the {@code principal} and returns a reference to this Builder so that the methods can be chained
         * together.
         *
         * @param principal the {@code principal} to set
         * @return a reference to this Builder
         */
        public Builder principal(String principal) {
            this.principal = principal;
            return this;
        }

        /**
         * Sets the advertised web UI URL for the service and returns a reference to this Builder so that the methods
         * can be chained together.
         *
         * @param webUrl the web URL to set
         * @return a reference to this Builder
         */
        public Builder webUrl(String webUrl) {
            this.webUrl = webUrl;
            return this;
        }

        /**
         * Sets the {@code user} and returns a reference to this Builder so that the methods can be chained
         * together.
         *
         * @param user the {@code principal} to set
         * @return a reference to this Builder
         */
        public Builder user(String user) {
            this.user = user;
            return this;
        }

        /**
         * Sets the {@code zookeeperConnection} and returns a reference to this Builder so that the methods can be
         * chained together.
         *
         * @param zookeeperConnection the {@code zookeeperConnection} to set
         * @return a reference to this Builder
         */
        public Builder zookeeperConnection(String zookeeperConnection) {
            this.zookeeperConnection = zookeeperConnection;
            return this;
        }

        /**
         * Sets the {@code pods} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param pods the {@code pods} to set
         * @return a reference to this Builder
         */
        public Builder pods(List<PodSpec> pods) {
            this.pods = pods;
            return this;
        }

        /**
         * Adds the {@code pod} and returns a reference to this Builder so that the methods can be chained together.
         *
         * @param pod the {@code pod} to add
         * @return a reference to this Builder
         */
        public Builder addPod(PodSpec pod) {
            this.pods.add(pod);
            return this;
        }

        /**
         * Sets the {@code replacementFailurePolicy} and returns a reference to this Builder so that the methods can be
         * chained together.
         *
         * @param replacementFailurePolicy the {@code replacementFailurePolicy} to set
         * @return a reference to this Builder
         */
        public Builder replacementFailurePolicy(ReplacementFailurePolicy replacementFailurePolicy) {
            this.replacementFailurePolicy = replacementFailurePolicy;
            return this;
        }

        /**
         * Returns a {@code DefaultServiceSpec} built from the parameters previously set.
         *
         * @return a {@code DefaultServiceSpec} built with parameters of this {@code DefaultServiceSpec.Builder}
         */
        public JeidServiceSpec build() {
            return new JeidServiceSpec(this);
        }
    }
}
