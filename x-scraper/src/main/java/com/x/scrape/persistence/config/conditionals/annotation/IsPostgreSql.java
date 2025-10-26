package com.x.scrape.persistence.config.conditionals.annotation;

import com.x.scrape.persistence.config.conditionals.IsPostgreSqlCondition;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Conditional annotation for persistence beans that are related to the PostgreSQL.
 */
@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Documented
@Conditional(IsPostgreSqlCondition.class)
public @interface IsPostgreSql {
}
