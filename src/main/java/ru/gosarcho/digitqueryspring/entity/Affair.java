package ru.gosarcho.digitqueryspring.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "affairs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Affair {
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "affair_id", unique = true, nullable = false, precision = 20, scale = 0)
    private Long id;

    @Column(name = "fond")
    private String fond;

    @Column(name = "op")
    private String op;

    @Column(name = "affair")
    private String affair;

    @Column(name = "reader")
    private String reader;

    @Column(name = "executor")
    private String executor;

    @Column(name = "receipt_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate receiptDate;
}
