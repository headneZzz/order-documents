package ru.gosarcho.digitqueryspring.model;

import lombok.Data;

@Data
public class PersonModel {
    private String readerFullName;
    private String executorLastName;

    public PersonModel(String reader, String executor) {
        readerFullName = reader;
        executorLastName = executor;
    }
}
