package com.example.authenticate;

public class subject {

    private String Sub_name;
    private String Semester;

    public subject(){}

    public subject(String sub_name, String semester) {
        Sub_name = sub_name;
        Semester = semester;
    }

    public String getSemester() {
        return Semester;
    }

    public String getSub_name() {
        return Sub_name;
    }
}
