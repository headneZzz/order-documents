package ru.gosarhro.order_documents.service;

import org.springframework.stereotype.Service;
import ru.gosarhro.order_documents.entity.Document;
import ru.gosarhro.order_documents.repository.DocumentRepository;
import ru.gosarhro.order_documents.util.DocumentsFilter;

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

    public List<Document> getAllByFilter(DocumentsFilter df) {
        return repository.findAllByReceiptDateBetweenAndReaderLikeAndExecutorLikeOrderByReceiptDateDesc(
                LocalDate.parse(df.getDateFrom()),
                LocalDate.parse(df.getDateTo()),
                "%" + df.getReader() + "%",
                "%" + df.getExecutor() + "%");
    }

    public void save(Document document) {
        repository.save(document);
    }
}
