package com.example.authenticate;

public class Attendance {

    private String Name,Attended ,Total;

    public Attendance(){}

    public Attendance(String name, String attended, String total) {
        Name = name;
        Attended = attended;
        Total = total;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAttended() {
        return Attended;
    }

    public void setAttended(String attended) {
        Attended = attended;
    }

    public String getTotal() {
        return Total;
    }

    public void setTotal(String total) {
        Total = total;
    }
}
