package com.beluga.result_storage.config.conditionals.annotation;

import com.beluga.result_storage.config.conditionals.IsS3ResultStorageCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional annotation which checks if s3 is used for result storage.
 */
@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Documented
@Conditional(IsS3ResultStorageCondition.class)
public @interface IsS3ResultStorage {
}
