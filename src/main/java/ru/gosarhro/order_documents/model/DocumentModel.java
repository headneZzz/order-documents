package ru.gosarhro.order_documents.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DocumentModel {
    private String fond;
    private String op;
    private String document;
}
