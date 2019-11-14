package ru.gosarcho.affairs_query.util;

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
