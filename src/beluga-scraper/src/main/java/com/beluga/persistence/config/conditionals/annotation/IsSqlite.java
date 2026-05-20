package com.beluga.persistence.config.conditionals.annotation;

import com.beluga.persistence.config.conditionals.IsSqliteCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional annotation for persistence beans that are related to the SQL Lite.
 */
@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Documented
@Conditional(IsSqliteCondition.class)
public @interface IsSqlite {
}
