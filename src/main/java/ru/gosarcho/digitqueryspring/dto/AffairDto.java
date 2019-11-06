package ru.gosarcho.digitqueryspring.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AffairDto {
    private String fond;
    private String op;
    private String affair;
    private String person;
    private String executor;
    private LocalDate receiptDate;
}
