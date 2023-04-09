package ru.practicum.exception;

public class ItemRetrieverException extends RuntimeException {
    public ItemRetrieverException(String s, Throwable e) {
        super(s + e.getMessage());
    }

    public ItemRetrieverException(String s) {
        super(s);
    }
}
