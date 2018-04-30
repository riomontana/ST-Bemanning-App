package com.example.nikolapajovic.stbemanning.model;

import java.io.Serializable;


/**
 * Model class for work shifts
 * @author Linus Forsberg
 */

public class WorkShift implements Serializable {
    private int workShiftId;
//    private int userId; todo behövs förmodligen inte
    private String shiftStart;
    private String shiftEnd;
    private String company;

    public WorkShift(int workShiftId, String shiftStart, String shiftEnd, String company) {
        this.workShiftId = workShiftId;
        this.shiftStart = shiftStart;
        this.shiftEnd = shiftEnd;
        this.company = company;
    }


//    public int getWorkShiftId() { return workShiftId; }

    public String getShiftStart() {
        return shiftStart;
    }

    public String getShiftEnd() {
        return shiftEnd;
    }

    public String getCompany() {
        return company;
    }
}
