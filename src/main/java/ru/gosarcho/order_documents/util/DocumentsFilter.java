package ru.gosarcho.order_documents.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DocumentsFilter {
    private String dateFrom;
    private String dateTo;
    private String reader;
    private String executor;
}
