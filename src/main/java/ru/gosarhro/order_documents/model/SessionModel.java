package ru.gosarhro.order_documents.model;

import lombok.Data;
import ru.gosarhro.order_documents.util.DocumentsFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
public class SessionModel {
    private String readerFullName;
    private String executorLastName;
    private List<DocumentModel> documentModels;
    private List<File> documentFiles;
    private DocumentsFilter documentsFilter;

    public SessionModel(String readerFullName, String executorLastName){
        this.readerFullName = readerFullName;
        this.executorLastName = executorLastName;
        documentModels = new ArrayList<>();
        documentFiles = new ArrayList<>();
        documentsFilter = new DocumentsFilter();
    }
}
