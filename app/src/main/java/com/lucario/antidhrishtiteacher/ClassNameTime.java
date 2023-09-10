package com.lucario.antidhrishtiteacher;

import java.io.Serializable;
import java.util.Date;

public class ClassNameTime implements Serializable {
    private String className;
    private Date startTime;
    ClassNameTime(String className, Date startTime){
        this.className = className;
        this.startTime = startTime;
    }

    public String getClassName(){
        return className;
    }

    public Date getStartTime(){
        return startTime;
    }
}
