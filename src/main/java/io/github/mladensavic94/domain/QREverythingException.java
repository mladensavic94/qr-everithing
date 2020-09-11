package io.github.mladensavic94.domain;

public class QREverythingException extends RuntimeException{

    public QREverythingException(String message, Object... params) {
        super(String.format(message, params));
    }
}
