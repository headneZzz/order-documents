package ru.gosarhro.order_documents.util;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class DocumentsFilter {
    private String dateFrom;
    private String dateTo;
    private String reader;
    private String executor;
}
