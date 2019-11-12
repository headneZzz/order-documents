package ru.gosarcho.digitqueryspring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gosarcho.digitqueryspring.entity.Affair;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AffairRepository extends JpaRepository<Affair, Long> {
    List<Affair> findAffairByReceiptDateBetweenAndReaderLikeAndExecutorLike(LocalDate dateFrom, LocalDate dateTo, String reader, String executor);
}
