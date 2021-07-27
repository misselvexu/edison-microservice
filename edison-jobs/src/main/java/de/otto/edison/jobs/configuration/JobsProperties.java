package de.otto.edison.jobs.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration properties used to configure the behaviour of module edison-jobs.
 *
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "edison.jobs")
@Validated
public class JobsProperties {
    /**
     * Enables / disabled the support for external triggers (->Edison JobTrigger). If false, the job controllers are not available.
     */
    private boolean externalTrigger = true;
    /**
     * Number of threads available to run jobs.
     */
    @Min(1)
    private int threadCount = 10;
    /**
     * Properties used to configure clean-up strategies.
     */
    @Valid
    private Cleanup cleanup = new Cleanup();
    /**
     * Properties used to configure the reporting of job status using StatusDetailIndicators.
     */
    @Valid
    private Status status = new Status();

    @Valid
    private Mongo mongo = new Mongo();

    @Valid
    private S3Firehose s3Firehose = new S3Firehose();

    public boolean isExternalTrigger() {
        return externalTrigger;
    }

    public void setExternalTrigger(boolean externalTrigger) {
        this.externalTrigger = externalTrigger;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public Cleanup getCleanup() {
        return cleanup;
    }

    public void setCleanup(Cleanup cleanup) {
        this.cleanup = cleanup;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Mongo getMongo() {
        return mongo;
    }

    public void setMongo(Mongo mongo) {
        this.mongo = mongo;
    }

    public S3Firehose getS3Firehose() {
        return s3Firehose;
    }

    public void setS3Firehose(S3Firehose s3Firehose) {
        this.s3Firehose = s3Firehose;
    }

    public static class Cleanup {
        /**
         * The number of jobs to keep by strategies like KeepLastJobs to clean up old jobs.
         */
        @Min(1)
        private int numberOfJobsToKeep = 100;
        /**
         * The number of skipped jobs to keep by strategies like DeleteSkippedJobs to clean up old jobs.
         */
        @Min(1)
        private int numberOfSkippedJobsToKeep = 10;
        /**
         * Number of seconds without update after which a job is considered to be dead.
         */
        @Min(1)
        private int markDeadAfter = 30;

        public int getNumberOfJobsToKeep() {
            return numberOfJobsToKeep;
        }

        public void setNumberOfJobsToKeep(int numberOfJobsToKeep) {
            this.numberOfJobsToKeep = numberOfJobsToKeep;
        }

        public int getNumberOfSkippedJobsToKeep() {
            return numberOfSkippedJobsToKeep;
        }

        public void setNumberOfSkippedJobsToKeep(int numberOfSkippedJobsToKeep) {
            this.numberOfSkippedJobsToKeep = numberOfSkippedJobsToKeep;
        }

        public int getMarkDeadAfter() {
            return markDeadAfter;
        }

        public void setMarkDeadAfter(int markDeadAfter) {
            this.markDeadAfter = markDeadAfter;
        }
    }

    public static class Status {
        /**
         * Enable / disable StatusDetailIndicators for jobs.
         */
        private boolean enabled = true;

        /**
         * Configuration of the strategy used to map job state to StatusDetails. edison.jobs.status.calculator.default
         * is used to configure the default strategy.
         */
        @NotNull
        private Map<String, String> calculator = new HashMap<>();

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public Map<String, String> getCalculator() {
            return calculator;
        }

        public void setCalculator(final Map<String, String> calculator) {
            final Map<String, String> normalized = new HashMap<>();
            calculator.entrySet().forEach(entry -> {
                String key = entry.getKey().toLowerCase().replace(" ", "-");
                normalized.put(key, entry.getValue());
            });
            this.calculator = normalized;
        }
    }

    public static class Mongo {
        /**
         * Enable / disable mongo job repository.
         */
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

    }

    public static class S3Firehose {
        /**
         * Enable / disable s3firehose job repository.
         */
        private boolean enabled = false;

        private String bucketName = null;
        private String deliveryStream = null;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getDeliveryStream() {
            return deliveryStream;
        }

        public void setDeliveryStream(String deliveryStream) {
            this.deliveryStream = deliveryStream;
        }

        public String getBucketName() {
            return bucketName;
        }

        public void setBucketName(String bucketName) {
            this.bucketName = bucketName;
        }
    }
}
