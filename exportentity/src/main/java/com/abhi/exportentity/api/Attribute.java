/**
 *
 */
package com.abhi.exportentity.api;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Objects;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(FIELD)
/**
 * @author abhisheksoni
 *
 */
public @interface Attribute {
    String headerLabel();

    Class<? extends AttributeFormatter> formatter() default DefaultAttributeFormatter.class;

    int order() default 0;

    class DefaultAttributeFormatter implements AttributeFormatter {
        @Override
        public String format(final Object object) {
            return Objects.toString(object, "");
        }
    }
}
