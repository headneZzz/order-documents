package ru.gosarcho.digitqueryspring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.gosarcho.digitqueryspring.model.Affair;
import ru.gosarcho.digitqueryspring.repository.AffairRepository;

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
}
