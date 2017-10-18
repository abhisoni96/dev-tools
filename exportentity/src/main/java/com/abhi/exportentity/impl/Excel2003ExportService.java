package com.abhi.exportentity.impl;

import java.io.OutputStream;

import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
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
		LineWriter		headerWriter;
		XSSFWorkbook	workBook;
		OutputStream	outputStream;

		public DefaultLineWriterProvider(final OutputStream outputStream) {
			this.outputStream = outputStream;
			this.workBook = new XSSFWorkbook();
			final XSSFSheet workSheet = this.workBook.createSheet();
			this.lineWriter = new DefaultLineWriter(workSheet, false);
			this.headerWriter = new DefaultLineWriter(workSheet, true);
		}

		@Override
		public com.abhi.exportentity.api.FlatDataExportService.LineWriter getHeaderWriter() {
			return this.headerWriter;
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
			this.workBook.write(this.outputStream);
			this.workBook.close();
			this.outputStream.close();
		}
	}

	private static class DefaultLineWriter implements LineWriter {
		XSSFSheet	workSheet;
		XSSFRow		row;
		boolean		isHeader;

		public DefaultLineWriter(final XSSFSheet workSheet, final boolean isHeader) {
			this.workSheet = workSheet;
			this.isHeader = isHeader;
		}

		@Override
		public void init() {
			this.row = this.workSheet.createRow(this.workSheet.getPhysicalNumberOfRows());
			if (this.isHeader) {
				final XSSFCellStyle style = this.workSheet.getWorkbook().createCellStyle();
				final XSSFFont font = this.workSheet.getWorkbook().createFont();
				font.setBold(true);
				style.setFont(font);
				this.row.setRowStyle(style);
			}
		}

		@Override
		public void write(final String value, final int colIndex) {
			this.row.createCell(colIndex).setCellValue(value == null ? "" : value.toString());
		}
	}

}
