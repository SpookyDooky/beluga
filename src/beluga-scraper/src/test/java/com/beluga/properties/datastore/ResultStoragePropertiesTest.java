package com.beluga.properties.datastore;

import org.junit.jupiter.api.Test;

import static com.beluga.properties.datastore.DataStoreType.FILE_SYSTEM;
import static com.beluga.properties.datastore.DataStoreType.S3;
import static org.junit.jupiter.api.Assertions.*;

class ResultStoragePropertiesTest {

    @Test
    void defaultShouldBeFileSystem() {
        final ResultStorageProperties properties = new ResultStorageProperties();

        assertEquals(FILE_SYSTEM, properties.getType());
        assertNotNull(properties.getFileSystem());
    }

    @Test
    void shouldRemoveDefaultIfFileSystemNotUsed() {
        final ResultStorageProperties properties = new ResultStorageProperties();
        properties.setType(S3);

        assertNull(properties.getFileSystem());
    }
}