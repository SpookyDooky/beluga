package com.beluga.model.result;

import org.instancio.Instancio;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResultFileTest {

    @Test
    void shouldGetResourceIdentifier() {
        final ResultFile resultFile = Instancio.create(ResultFile.class);

        final String resourceIdentifier = resultFile.getResourceIdentifier();

        assertEquals(
                resultFile.getNamespace() + "/" + resultFile.getKey(),
                resourceIdentifier
        );
    }
}