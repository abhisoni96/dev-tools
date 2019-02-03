package com.abhi.exportentity.api;

import java.util.List;

public interface FlatDataExportService<E> extends ExportService<E> {

    void setLineWriterProvider(LineWriterProvider lineWriterProvider);

    void exportHeader() throws Exception;

    void exportHeader(List<String> customHeaders, boolean isAppend) throws Exception;

    void exportFooter() throws Exception;

    public static interface LineWriter {

        void init() throws Exception;

        void write(String value, int colIndex) throws Exception;
    }

    public static interface LineWriterProvider {
        LineWriter getHeaderWriter();

        LineWriter getEntityWriter();

        LineWriter getFooterWriter();

        void release() throws Exception;
    }
}
