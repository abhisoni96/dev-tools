package com.abhi.exportentity.api;

import java.util.Collection;

public interface BulkExportService<E> extends ExportService<E> {

	public void export(Collection<E> entities);
}
