package ru.otus.hw.rest.dto;

public record ErrorDto(String description, String reason, long timestamp) {

    public ErrorDto(String description, String reason){
        this(description, reason, System.currentTimeMillis());
    }
}
