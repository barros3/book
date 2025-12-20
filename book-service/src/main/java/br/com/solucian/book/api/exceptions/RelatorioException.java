package br.com.solucian.book.api.exceptions;

public class RelatorioException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RelatorioException(String message) {
        super(message);
    }

    public RelatorioException(String message, Throwable cause) {
        super(message, cause);
    }

    public RelatorioException(Throwable cause) {
        super(cause);
    }
}