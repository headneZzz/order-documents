package ru.gosarcho.order_documents.service;

import org.springframework.stereotype.Service;
import ru.gosarcho.order_documents.entity.Document;
import ru.gosarcho.order_documents.repository.DocumentRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class DocumentService {
    private final DocumentRepository repository;

    public DocumentService(DocumentRepository repository) {
        this.repository = repository;
    }

    public List<Document> getAll() {
        return repository.findAll();
    }

    public List<Document> getAllByFilter(LocalDate dateFrom, LocalDate dateTo, String reader, String executor) {
        return repository.findDocumentByReceiptDateBetweenAndReaderLikeAndExecutorLike(dateFrom, dateTo, "%" + reader + "%", "%" + executor + "%");
    }

    public void save(Document document) {
        repository.save(document);
    }
}
