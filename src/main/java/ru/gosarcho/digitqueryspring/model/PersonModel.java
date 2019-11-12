package ru.gosarcho.digitqueryspring.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonModel {
    private String readerFullName;
    private String executorLastName;
}
