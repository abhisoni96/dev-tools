package com.abhi.exportentity;

import java.lang.reflect.Field;
import java.util.SortedSet;
import java.util.TreeSet;

import com.abhi.exportentity.api.Attribute;
import com.abhi.exportentity.api.ExportService;

public abstract class AbstractExportService<E> implements ExportService<E> {

	private Class<E>					entityType;
	private SortedSet<EntityAttribute>	cache;

	public AbstractExportService() {

	}

	@Override
	public ExportService<E> initialize(final Class<E> entityType) {
		this.entityType = entityType;
		this.cache = this.intitializeCache(this.entityType);
		return this;
	}

	private SortedSet<EntityAttribute> intitializeCache(final Class<E> entityType) {
		final SortedSet<EntityAttribute> cache = new TreeSet<>();
		final Field[] declaredFields = entityType.getDeclaredFields();
		for (final Field field : declaredFields) {
			if (field.isAnnotationPresent(Attribute.class)) {
				field.setAccessible(true);
				cache.add(new EntityAttribute(field, field.getAnnotation(Attribute.class)));
			}
		}
		return cache;
	}

	@Override
	public void release() throws Exception {
		this.cache = null;
	}

	protected SortedSet<EntityAttribute> getCache() {
		return this.cache;
	}

	protected static class EntityAttribute implements Comparable<EntityAttribute> {
		public final Field		field;
		public final Attribute	attribute;

		public EntityAttribute(final Field field, final Attribute attribute) {
			super();
			this.field = field;
			this.attribute = attribute;
		}

		@Override
		public int compareTo(final EntityAttribute o) {
			return Integer.valueOf(this.attribute.order()).compareTo(o.attribute.order());
		}
	}
}
