package ru.practicum.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class HelloMessageService {
    private final String message;

    @Autowired
    public HelloMessageService(@Value("${message:Hello!}") String message) {
        this.message = message;
    }

    public String getHelloMessage() {
        return message;
    }
}