package ru.gosarcho.order_documents.model;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
public class PersonModel {
    private String readerFullName;
    private String executorLastName;
    private List<DocumentModel> documentModels;
    private List<File> documentFiles;

    public PersonModel(String readerFullName, String executorLastName){
        this.readerFullName = readerFullName;
        this.executorLastName = executorLastName;
        documentModels = new ArrayList<>();
        documentFiles = new ArrayList<>();
    }
}
