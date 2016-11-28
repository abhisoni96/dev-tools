package com.abhi.exportentity;

import com.abhi.exportentity.api.AttributeFormatter;
import com.abhi.exportentity.api.FlatDataExportService;

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
		int colIndex = 0;
		for (final EntityAttribute field : this.getCache()) {
			this.lineWriterProvider.getHeaderWriter().write(field.attribute.headerLabel(), colIndex++);
		}
	}

	@Override
	public void exportFooter() throws Exception {

	}

	@Override
	public void export(final E entity) throws Exception {
		AttributeFormatter formatter = null;
		int colIndex = 0;
		this.lineWriterProvider.getEntityWriter().init();
		for (final EntityAttribute field : this.getCache()) {
			final Object fieldValue = field.field.get(entity);
			String value = null;
			if (fieldValue != null) {
				if (field.attribute.formatter() != null) {
					formatter = field.attribute.formatter().newInstance();
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

	@Override
	public void release() throws Exception {
		super.release();
		this.lineWriterProvider.release();
	}
}
