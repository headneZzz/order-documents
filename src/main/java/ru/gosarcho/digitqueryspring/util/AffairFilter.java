package ru.gosarcho.digitqueryspring.util;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AffairFilter {
    private String dateFrom;
    private String dateTo;
    private String reader;
    private String executor;
}
