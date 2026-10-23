package com.marco.stockordermanager.exceptions;

public class RegraDeNegocioException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RegraDeNegocioException(String msg) {
        super(msg);
    }
}