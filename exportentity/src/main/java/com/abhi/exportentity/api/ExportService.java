package com.abhi.exportentity.api;

public interface ExportService<E> {

	ExportService<E> initialize(Class<E> entityType);

	void export(E entity) throws Exception;

	void release() throws Exception;
}
