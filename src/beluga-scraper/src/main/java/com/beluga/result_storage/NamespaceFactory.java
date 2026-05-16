package com.beluga.result_storage;

import com.beluga.execution.event.task.task_result.TaskResultEvent;
import com.beluga.properties.BelugaScraperProperties;
import com.beluga.properties.datastore.DataStoreType;
import com.beluga.properties.datastore.ResultStorageProperties;
import org.springframework.stereotype.Component;

/**
 * Creates namespaces for files that need to be stored depending on the configured {@link DataStoreType}.
 */
@Component
public class NamespaceFactory {

    private final String namespacePrefix;

    public NamespaceFactory(final BelugaScraperProperties properties) {
        this.namespacePrefix = getNamespacePrefix(properties.getResultStorage());
    }

    private String getNamespacePrefix(final ResultStorageProperties properties) {
        return switch (properties.getType()) {
            case FILE_SYSTEM -> properties.getFileSystem().getPath();
            case S3 -> properties.getS3().getPrefix();
        };
    }

    /**
     * Creates the namespace for a {@link TaskResultEvent}.
     *
     * @param event event.
     * @return the namespace for the {@link TaskResultEvent}.
     */
    public String create(final TaskResultEvent event) {
        return namespacePrefix + "/" + event.getJobDefinitionId() + "/"
                + event.getJobId() + "/"
                + event.getTaskId() + "/";
    }
}
