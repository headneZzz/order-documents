package ru.gosarcho.affairs_query.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gosarcho.affairs_query.entity.Affair;
import ru.gosarcho.affairs_query.repository.AffairRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class AffairService {
    @Autowired
    private final AffairRepository repository;

    public AffairService(AffairRepository repository) {
        this.repository = repository;
    }

    public List<Affair> getAll() {
        return repository.findAll();
    }

    public List<Affair> getAllByFilter(LocalDate dateFrom, LocalDate dateTo, String reader, String executor) {
        return repository.findAffairByReceiptDateBetweenAndReaderLikeAndExecutorLike(dateFrom, dateTo, "%" + reader + "%", "%" + executor + "%");
    }

    public void save(Affair affair) {
        repository.save(affair);
    }
}
