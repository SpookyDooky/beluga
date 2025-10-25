package com.x.scrape.persistence.config.conditionals;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Conditional annotation for persistence beans that are related to the filesystem.
 */
@ConditionalOnProperty(
		name = "x-scraper.persistence.type",
		havingValue = "FILE_SYSTEM"
)
public @interface IsFileSystem {
}
