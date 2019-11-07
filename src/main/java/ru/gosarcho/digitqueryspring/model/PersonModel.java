package ru.gosarcho.digitqueryspring.model;

import lombok.Data;

@Data
public class PersonModel {
    private String personFullName;
    private String executorLastName;

    public PersonModel(String person, String executor) {
        personFullName = person;
        executorLastName = executor;
    }
}
