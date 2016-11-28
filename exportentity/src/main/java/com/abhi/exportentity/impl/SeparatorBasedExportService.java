package com.abhi.exportentity.impl;

import java.io.OutputStream;

import com.abhi.exportentity.AbstractFlatDataExportService;

public class SeparatorBasedExportService<E> extends AbstractFlatDataExportService<E> {

	public SeparatorBasedExportService(final String separator, final Class<E> entityType,
			final OutputStream outputStream) {
		super(entityType, new DefaultLineWriterProvider(separator, outputStream));
	}

	private static class DefaultLineWriterProvider implements LineWriterProvider {
		LineWriter		lineWriter;
		OutputStream	outputStream;

		public DefaultLineWriterProvider(final String separator, final OutputStream outputStream) {
			this.outputStream = outputStream;
			this.lineWriter = new DefaultLineWriter(separator, outputStream);
		}

		@Override
		public com.abhi.exportentity.api.FlatDataExportService.LineWriter getHeaderWriter() {
			return this.lineWriter;
		}

		@Override
		public com.abhi.exportentity.api.FlatDataExportService.LineWriter getEntityWriter() {
			return this.lineWriter;
		}

		@Override
		public com.abhi.exportentity.api.FlatDataExportService.LineWriter getFooterWriter() {
			return this.lineWriter;
		}

		@Override
		public void release() throws Exception {
			// this.outputStream.close();
		}
	}

	private static class DefaultLineWriter implements LineWriter {
		String			separator;
		OutputStream	dataWriter;
		boolean			start	= true;

		public DefaultLineWriter(final String separator, final OutputStream dataWriter) {
			this.separator = separator;
			this.dataWriter = dataWriter;
		}

		@Override
		public void init() throws Exception {
		}

		@Override
		public void write(final String value, final int colIndex) throws Exception {
			final String retValue = value == null ? "" : value;
			if (this.start) {
				this.start = false;
				this.dataWriter.write(retValue.getBytes());
				return;
			}
			if (colIndex == 0) {
				this.dataWriter.write("\n".getBytes());
			} else {
				this.dataWriter.write(this.separator.getBytes());
			}
			this.start = false;
			this.dataWriter.write(retValue.getBytes());
		}
	}

}
