package com.beluga.instancio;

import com.beluga.api.job.dto.write.extraction_configuration.WriteExtractionConfigurationDto;
import com.beluga.api.job.dto.write.extraction_configuration.text.WriteTextExtractionConfigurationDto;
import org.instancio.Instancio;
import org.instancio.Node;
import org.instancio.generator.Generator;
import org.instancio.generator.GeneratorSpec;
import org.instancio.generators.Generators;
import org.instancio.spi.InstancioServiceProvider;

/**
 * Registers a generator for instancio specifically for generating {@link WriteExtractionConfigurationDto} instances.
 * This is due to the fact that Instancio does not automatically make use of subtypes.
 */
public class InstancioWriteExtractionConfigurationDtoProvider implements InstancioServiceProvider {

    @Override
    public GeneratorProvider getGeneratorProvider() {
        return new GeneratorProvider() {
            @Override
            public GeneratorSpec<?> getGenerator(final Node node, final Generators generators) {
                if (WriteExtractionConfigurationDto.class.equals(node.getTargetClass())) {
                    return (Generator<WriteExtractionConfigurationDto>) random -> {
                        try {
                            return Instancio.create(WriteTextExtractionConfigurationDto.class);
                        } catch (final Exception e) {
                            throw new RuntimeException(e);
                        }
                    };
                }

                return null;
            }
        };
    }
}
