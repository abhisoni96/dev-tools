package com.abhi.exportentity.exception;

public class ExportServiceException extends RuntimeException {
    public ExportServiceException() {
    }

    public ExportServiceException(final String message) {
        super(message);
    }

    public ExportServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ExportServiceException(final Throwable cause) {
        super(cause);
    }

    public ExportServiceException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
