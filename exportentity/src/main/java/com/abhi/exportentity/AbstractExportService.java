package com.abhi.exportentity;

import com.abhi.exportentity.api.Attribute;
import com.abhi.exportentity.api.CollectionAttribute;
import com.abhi.exportentity.api.ComplexAtrribute;
import com.abhi.exportentity.api.ExportService;
import com.abhi.exportentity.exception.ExportServiceException;

import java.lang.reflect.Field;
import java.util.*;

public abstract class AbstractExportService<E> implements ExportService<E> {

    private Class<E> entityType;
    private Map<Class<?>, SortedSet<EntityAttribute>> cache;

    public AbstractExportService() {

    }

    @Override
    public ExportService<E> initialize(final Class<E> entityType) {
        this.entityType = entityType;
        this.cache = this.initializeCache(this.entityType);
        return this;
    }

    protected Map<Class<?>, SortedSet<EntityAttribute>> initializeCache(final Class<?> entityType) {
        final Map<Class<?>, SortedSet<EntityAttribute>> cache = new HashMap<>();
        final SortedSet<EntityAttribute> attributeCache = new TreeSet<>();
        final Field[] declaredFields = entityType.getDeclaredFields();
        for (final Field field : declaredFields) {
            if (field.isAnnotationPresent(ComplexAtrribute.class)) {
                cache.putAll(this.initializeCache(field.getType()));
            }
            if (field.isAnnotationPresent(Attribute.class)) {
                // TODO: Remove below code of set accessible every field in enitites...
                field.setAccessible(true);
                attributeCache.add(new EntityAttribute(field, field.getAnnotation(Attribute.class)));
            }
            if (field.isAnnotationPresent(CollectionAttribute.class) &&
                    !(Collection.class.isAssignableFrom(field.getType()) ||
                            Map.class.isAssignableFrom(field.getType()))) {
                throw new ExportServiceException("Collection Attribute only supported on Collection or Map datatype!");
            }
        }
        cache.put(entityType, attributeCache);
        return cache;
    }

    @Override
    public void release() throws Exception {
        this.cache = null;
    }

    protected Map<Class<?>, SortedSet<EntityAttribute>> getCache() {
        return this.cache;
    }

    public Class<E> getEntityType() {
        return this.entityType;
    }

    protected static class EntityAttribute implements Comparable<EntityAttribute> {
        public final Field field;
        public final Attribute attribute;

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
