package ru.gosarcho.affairs_query.model;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
public class PersonModel {
    private String readerFullName;
    private String executorLastName;
    private List<AffairModel> affairModels;
    private List<File> affairFiles;

    public PersonModel(String readerFullName, String executorLastName){
        this.readerFullName = readerFullName;
        this.executorLastName = executorLastName;
        affairModels = new ArrayList<>();
        affairFiles = new ArrayList<>();
    }


}
