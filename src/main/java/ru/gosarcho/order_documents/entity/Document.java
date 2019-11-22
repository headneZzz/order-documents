package ru.gosarcho.order_documents.entity;

import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "documents")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Document {
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "document_id", unique = true, nullable = false, precision = 20, scale = 0)
    private Long id;

    @Column(name = "fond")
    @CsvBindByPosition(position = 0)
    private String fond;

    @Column(name = "op")
    @CsvBindByPosition(position = 1)
    private String op;

    @Column(name = "document")
    @CsvBindByPosition(position = 2)
    private String document;

    @Column(name = "reader")
    @CsvBindByPosition(position = 3)
    private String reader;

    @Column(name = "executor")
    @CsvBindByPosition(position = 4)
    private String executor;

    @Column(name = "receipt_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @CsvBindByPosition(position = 5)
    private LocalDate receiptDate;
}
