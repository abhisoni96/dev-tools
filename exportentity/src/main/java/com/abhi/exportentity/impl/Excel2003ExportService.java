package com.abhi.exportentity.impl;

import java.io.OutputStream;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.abhi.exportentity.AbstractFlatDataExportService;

public class Excel2003ExportService<E> extends AbstractFlatDataExportService<E> {

	public Excel2003ExportService(final Class<E> entityType, final OutputStream outputStream) {
		super(entityType, new DefaultLineWriterProvider(outputStream));
	}

	private static class DefaultLineWriterProvider implements LineWriterProvider {
		LineWriter		lineWriter;
		XSSFWorkbook	woorkBook;
		OutputStream	outputStream;

		public DefaultLineWriterProvider(final OutputStream outputStream) {
			this.outputStream = outputStream;
			this.woorkBook = new XSSFWorkbook();
			final XSSFSheet workSheet = this.woorkBook.createSheet();
			this.lineWriter = new DefaultLineWriter(workSheet);
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
			this.woorkBook.write(this.outputStream);
		}
	}

	private static class DefaultLineWriter implements LineWriter {
		XSSFSheet	workSheet;
		XSSFRow		row;

		public DefaultLineWriter(final XSSFSheet workSheet) {
			this.workSheet = workSheet;
		}

		@Override
		public void init() {
			this.row = this.workSheet.createRow(this.workSheet.getLastRowNum() + 1);
		}

		@Override
		public void write(final String value, final int colIndex) {
			this.row.createCell(colIndex).setCellValue(value);
		}
	}

}
