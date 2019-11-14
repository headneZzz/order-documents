package ru.gosarcho.affairs_query.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AffairModel {
    private String fond;
    private String op;
    private String affair;
}
