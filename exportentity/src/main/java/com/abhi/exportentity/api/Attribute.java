/**
 *
 */
package com.abhi.exportentity.api;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
/**
 * @author abhisheksoni
 *
 */
public @interface Attribute {
	String headerLabel();

	Class<? extends AttributeFormatter> formatter() default DefaultAttributeFormatter.class;

	int order();

	public static class DefaultAttributeFormatter implements AttributeFormatter {
		@Override
		public String format(final Object object) {
			return object == null ? "" : object.toString();
		}
	}
}
