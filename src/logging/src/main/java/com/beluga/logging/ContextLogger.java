package com.beluga.logging;

import org.apache.logging.log4j.Logger;
import org.slf4j.MDC;

import java.util.Map;

public class ContextLogger {

    private final Logger logger;

    public ContextLogger(final Logger logger) {
        this.logger = logger;
    }

    public CloseableContext with(final String... context) {
        configureContext(context);
        return new CloseableContext();
    }

    private void configureContext(final String... context) {
        for (int i = 0; (i + 1) < context.length; i += 2) {
            putContext(context[i], context[i + 1]);
        }
    }

    private void putContext(final String key,
                            final String value) {
        MDC.put(key, value);
    }

    /**
     * Add the context of a {@link ContextLoggable} object to the {@link MDC}.
     *
     * @param contextLoggable the {@link ContextLoggable} object.
     * @return a closable context to automatically remove values from the {@link MDC} when it gets closed.
     */
    public CloseableContext with(final ContextLoggable contextLoggable) {
        contextLoggable.loggingContext()
                .forEach(this::putContext);

        return new CloseableContext();
    }

    /**
     * Adds a key and value to the context of the {@link MDC}
     *
     * @param key   the key.
     * @param value the value.
     * @return a closable context to automatically remove values from the {@link MDC} when it gets closed.
     */
    public CloseableContext withKey(final String key, final String value) {
        putContext(key, value);

        return new CloseableContext();
    }

    public CloseableContext withKeys(final Map<String, String> keyValues) {
        keyValues.forEach(this::putContext);

        return new CloseableContext();
    }

    public void info(final String message,
                     final String... context) {
        configureContext(context);
        logger.info(message);
    }

    public void error(final String message) {
        logger.error(message);
    }

    public void error(final String message,
                      final Throwable throwable) {
        logger.error(message, throwable);
    }

    public void trace(final String message) {
        logger.trace(message);
    }
}
