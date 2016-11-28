package com.abhi.exportentity;

import java.util.SortedSet;

import com.abhi.exportentity.api.AttributeFormatter;
import com.abhi.exportentity.api.FlatDataExportService;
import com.abhi.exportentity.api.SubAttribute;

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

	private int handleHeader(final Class<?> type, int colIndex) throws Exception {
		final SortedSet<EntityAttribute> fieldCache = this.intitializeCache(type);
		for (final EntityAttribute entityAttribute : fieldCache) {
			if (entityAttribute.field.isAnnotationPresent(SubAttribute.class)) {
				colIndex = this.handleHeader(entityAttribute.field.getType(), colIndex);
			} else {
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
		final SortedSet<EntityAttribute> fieldCache = this.intitializeCache(type);
		for (final EntityAttribute entityAttribute : fieldCache) {
			if (entityAttribute.field.isAnnotationPresent(SubAttribute.class)) {
				colIndex = this.handleEntityField(entityAttribute.field.getType(), colIndex,
						entityAttribute.field.get(entity));
			} else {
				AttributeFormatter formatter = null;
				final Object fieldValue = entity == null ? null : entityAttribute.field.get(entity);
				String value = null;
				if (fieldValue != null) {
					if (entityAttribute.attribute.formatter() != null) {
						formatter = entityAttribute.attribute.formatter().newInstance();
					}
					if (formatter != null) {
						value = formatter.format(fieldValue);
					} else {
						value = fieldValue.toString();
					}
				}
				this.lineWriterProvider.getEntityWriter().write(value, colIndex++);
			}
		}
		return colIndex;
	}

	@Override
	public void release() throws Exception {
		super.release();
		this.lineWriterProvider.release();
	}
}
