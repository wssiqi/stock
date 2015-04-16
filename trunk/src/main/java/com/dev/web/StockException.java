package com.dev.web;

public class StockException extends RuntimeException {

    private static final long serialVersionUID = 8180817491919089841L;

    public StockException() {
    }

    public StockException(String message) {
        super(message);
    }

    public StockException(Throwable cause) {
        super(cause);
    }

    public StockException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
