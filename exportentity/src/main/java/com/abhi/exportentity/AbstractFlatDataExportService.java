package com.abhi.exportentity;

import com.abhi.exportentity.api.AttributeFormatter;
import com.abhi.exportentity.api.CollectionAttribute;
import com.abhi.exportentity.api.ComplexAtrribute;
import com.abhi.exportentity.api.FlatDataExportService;

import java.util.*;
import java.util.Map.Entry;

public abstract class AbstractFlatDataExportService<E> extends AbstractExportService<E>
        implements FlatDataExportService<E> {

    protected LineWriterProvider lineWriterProvider;

    public AbstractFlatDataExportService(final Class<E> entityType, final LineWriterProvider lineWriterProvider) {
        super();
        this.initialize(entityType);
        this.setLineWriterProvider(lineWriterProvider);
    }

    @Override
    public void setLineWriterProvider(final LineWriterProvider lineWriterProvider) {
        this.lineWriterProvider = lineWriterProvider;
    }

    @Override
    public void exportHeader() throws Exception {
        this.lineWriterProvider.getHeaderWriter().init();
        this.handleHeader(this.getEntityType(), 0);
    }

    @Override
    public void exportHeader(final List<String> customHeaders, final boolean isAppend) throws Exception {
        int colIndex = 0;
        if (isAppend) {
            this.lineWriterProvider.getHeaderWriter().init();
            colIndex = this.handleHeader(this.getEntityType(), colIndex);
        }
        if (customHeaders != null) {
            for (final String header : customHeaders) {
                this.lineWriterProvider.getHeaderWriter().write(header, colIndex++);
            }
        }
    }

    private int handleHeader(final Class<?> type, int colIndex) throws Exception {
        final SortedSet<EntityAttribute> fieldCache = this.getCache().get(type);
        for (final EntityAttribute entityAttribute : fieldCache) {
            if (entityAttribute.field.isAnnotationPresent(ComplexAtrribute.class)) {
                colIndex = this.handleHeader(entityAttribute.field.getType(), colIndex);
            } else if (!entityAttribute.field.isAnnotationPresent(CollectionAttribute.class)) {
                this.lineWriterProvider.getHeaderWriter().write(entityAttribute.attribute.headerLabel(), colIndex++);
            }
        }
        return colIndex;
    }

    @Override
    public void exportFooter() throws Exception {

    }

    @Override
    public void export(final E entity) throws Exception {
        this.lineWriterProvider.getEntityWriter().init();
        this.handleEntityField(this.getEntityType(), 0, entity);
    }

    private int handleEntityField(final Class<?> type, int colIndex, final Object entity) throws Exception {
        final SortedSet<EntityAttribute> fieldCache = this.getCache().get(type);
        for (final EntityAttribute entityAttribute : fieldCache) {
            if (entityAttribute.field.isAnnotationPresent(ComplexAtrribute.class)) {
                colIndex = this.handleEntityField(entityAttribute.field.getType(), colIndex,
                        entityAttribute.field.get(entity));
            } else if (entityAttribute.field.isAnnotationPresent(CollectionAttribute.class)) {
                final Object fieldValue = entity == null ? null : entityAttribute.field.get(entity);
                if (fieldValue != null) {
                    if (fieldValue instanceof Collection) {
                        for (final Object collectionValue :
                                ((Collection) fieldValue)) {
                            colIndex = this.writeFieldValue(colIndex, entityAttribute.attribute.formatter(),
                                    collectionValue);
                        }
                    } else if (fieldValue instanceof Map) {
                        for (final Entry entry : (Set<Entry>) ((Map) fieldValue).entrySet()) {
                            colIndex = this.writeFieldValue(colIndex, entityAttribute.attribute.formatter(),
                                    entry.getValue());
                        }
                    }
                }
            } else {
                colIndex = this.writeFieldValue(colIndex, entityAttribute.attribute.formatter(),
                        entity == null ? null : entityAttribute.field.get(entity));
            }
        }
        return colIndex;
    }

    private int writeFieldValue(int colIndex, final Class<? extends AttributeFormatter> formatterType, final Object fieldValue) throws Exception {
        AttributeFormatter formatter = null;
        String value = null;
        if (fieldValue != null) {
            if (formatterType != null) {
                formatter = formatterType
                        .getDeclaredConstructor().newInstance();
            }
            if (formatter != null) {
                value = formatter.format(fieldValue);
            } else {
                value = String.valueOf(fieldValue);
            }
        }
        this.lineWriterProvider.getEntityWriter().write(value, colIndex++);
        return colIndex;
    }

    @Override
    public void release() throws Exception {
        super.release();
        this.lineWriterProvider.release();
    }
}
