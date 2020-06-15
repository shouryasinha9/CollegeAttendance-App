package com.example.authenticate;

import java.util.Locale;

public class Student {

    private String prediction;
    private String Attended , Total , Subject_Code;
    public Student(){}


    public Student(  String Attended, String Total, String Subject_Code) {

        this.Attended= Attended;
        this.Total= Total;
        this.Subject_Code = Subject_Code;
    }

    public String getAttended() {
        return Attended;
    }

    public String getTotal(){
        return Total;
    }

    public String getPrediction() {

                    int temp_att = Integer.parseInt(Attended);
                    int temp_total = Integer.parseInt(Total);

                    if(temp_att < (0.75*temp_total)){
                        while(true){
                            if(temp_att >= (0.75*temp_total)){
                                break;
                            }
                            else{
                                temp_att+=1;
                                temp_total+=1;
                            }
                        }
                        Integer diff = Math.abs(Integer.parseInt(Attended) - temp_att);

                        prediction = "You need to attend more " + diff +" classes to coverup.";

                    }
                    else if(temp_att >= (0.75*temp_total)){

                        while(true){
                            if(temp_att <= (0.75*temp_total)){
                                break;
                            }
                            else{
                                temp_total+=1;
                            }
                        }
                        Integer diff = Math.abs(Integer.parseInt(Total) - temp_total);

                        prediction = "You can miss more " + diff +" classes.";
                    }
                    if( temp_att == 0 && temp_total == 0){
                        prediction = "No classes yet";
                    }
        return prediction;
    }

    public String getSubject_Code() {
        return Subject_Code;
    }

    public int get_computablePercentage() {
        float percentage;
        if(Integer.parseInt(Attended)==0 && Integer.parseInt(Total) == 0){ percentage = 0;}
        else
        {percentage = ((float) Integer.parseInt(Attended) / Integer.parseInt(Total)) * 100;}
        return (int) percentage;
    }

    public String getPercentage() {
        float percentage;
        if(Integer.parseInt(Attended)==0 && Integer.parseInt(Total) == 0){ percentage = 0;}
        else
        {percentage = ((float)Integer.parseInt(Attended)/Integer.parseInt(Total))*100;}
        return String.format(Locale.getDefault(),"%.01f",percentage);
    }

}