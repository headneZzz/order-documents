package ru.gosarcho.affairs_query.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonModel {
    private String readerFullName;
    private String executorLastName;
}
