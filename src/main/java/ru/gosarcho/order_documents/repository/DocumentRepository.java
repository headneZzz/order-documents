package ru.gosarcho.order_documents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gosarcho.order_documents.entity.Document;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
    List<Document> findAllByReceiptDateBetweenAndReaderLikeAndExecutorLikeOrderByReceiptDateDesc(LocalDate dateFrom, LocalDate dateTo, String reader, String executor);
}
