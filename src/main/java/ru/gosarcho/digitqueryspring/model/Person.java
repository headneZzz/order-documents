package ru.gosarcho.digitqueryspring.model;

import lombok.Data;

@Data
public class Person {
    private String personFullName;
    private String executorLastName;

    public Person(String person, String executor) {
        personFullName = person;
        executorLastName = executor;
    }
}
