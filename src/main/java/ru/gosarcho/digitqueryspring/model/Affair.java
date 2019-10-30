package ru.gosarcho.digitqueryspring.model;

import lombok.Data;

@Data
public class Affair {

    private String fund;
    private String register;
    private String affair;

    public Affair(String fund, String register, String affair) {
        this.fund = fund;
        this.register = register;
        this.affair = affair;
    }

}
