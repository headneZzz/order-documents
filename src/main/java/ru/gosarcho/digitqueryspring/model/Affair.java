package ru.gosarcho.digitqueryspring.model;

import lombok.Data;

@Data
public class Affair {

    private String fond;
    private String op;
    private String affair;

    public Affair(String fond, String op, String affair) {
        this.fond = fond;
        this.op = op;
        this.affair = affair;
    }

}
