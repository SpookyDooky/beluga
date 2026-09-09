package com.beluga.result_storage;

import com.beluga.execution.event.task.task_result.TaskResultEvent;
import com.beluga.properties.BelugaScraperProperties;
import com.beluga.properties.datastore.ResultStorageProperties;
import com.beluga.properties.datastore.S3Properties;
import org.instancio.Instancio;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static com.beluga.properties.datastore.DataStoreType.FILE_SYSTEM;
import static com.beluga.properties.datastore.DataStoreType.S3;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NamespaceFactoryTest {

    @Mock
    private BelugaScraperProperties belugaScraperProperties;

    @ParameterizedTest
    @MethodSource
    void shouldUseCorrectPrefixForTaskResultEvent(final ResultStorageProperties resultStorageProperties,
                                                  final String expectedPrefix) {
        when(belugaScraperProperties.getResultStorage()).thenReturn(resultStorageProperties);
        final NamespaceFactory namespaceFactory = new NamespaceFactory(belugaScraperProperties);

        final TaskResultEvent event = Instancio.create(TaskResultEvent.class);

        final String namespace = namespaceFactory.create(event);

        assertEquals(
                expectedPrefix + "/" + event.getJobDefinitionId() + "/" + event.getJobId() + "/" + event.getTaskId(),
                namespace
        );
    }

    static Stream<Arguments> shouldUseCorrectPrefixForTaskResultEvent() {
        return Stream.of(
                Arguments.of(
                        Instancio.of(ResultStorageProperties.class)
                                .set(field(ResultStorageProperties::getType), S3)
                                .set(
                                        field(ResultStorageProperties::getS3),
                                        Instancio.of(S3Properties.class)
                                                .set(field(S3Properties::getPrefix), "s3")
                                                .create()
                                )
                                .create(),
                        "s3"
                ),
                Arguments.of(
                        Instancio.of(ResultStorageProperties.class)
                                .set(field(ResultStorageProperties::getType), FILE_SYSTEM)
                                .create(),
                        "/app/data"
                )
        );
    }
}