package ru.gosarcho.digitqueryspring.domain;

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
public class AffairModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "affair_id")
    private Long id;

    @Column(name = "fond")
    private String fond;

    @Column(name = "op")
    private String op;

    @Column(name = "affair")
    private String affair;

    @Column(name = "person")
    private String person;

    @Column(name = "executor")
    private String executor;

    @Column(name = "receipt_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate receiptDate;
}
