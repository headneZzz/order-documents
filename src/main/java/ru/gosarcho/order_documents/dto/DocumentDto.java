package ru.gosarcho.order_documents.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DocumentDto {
    private String fond;
    private String op;
    private String document;
    private String reader;
    private String executor;
    private LocalDate receiptDate;
}
