package com.beluga.instancio;

import org.instancio.Node;
import org.instancio.generator.Generator;
import org.instancio.generator.GeneratorSpec;
import org.instancio.generators.Generators;
import org.instancio.spi.InstancioServiceProvider;

import java.net.URL;
import java.util.UUID;

/**
 * Registers a generator for instancio specifically for generating {@link URL} instances.
 * This is due to Instancio being rather slow at this due to it not knowing what it is supposed to look like.
 */
public class InstancioUrlProvider implements InstancioServiceProvider {
	
	@Override
	public GeneratorProvider getGeneratorProvider() {
		return new GeneratorProvider() {
			@Override
			public GeneratorSpec<?> getGenerator(final Node node, final Generators generators) {
				if (URL.class.equals(node.getTargetClass())) {
					return (Generator<URL>) random -> {
						try {
							return new URL("http://localhost:1234/" + UUID.randomUUID());
						} catch (Exception e) {
							throw new RuntimeException(e);
						}
					};
				}
				
				return null;
			}
		};
	}
}
