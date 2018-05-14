package com.jdsports.universityapp;

/**
 * Created by HP on 10/7/2017.
 */

public class ClassDetails
{
    String Course ;
    String Subject ;
    String Date ;
    String FromTime ;
    String ToTime ;
    String Room ;
    String Faculity ;


    public String getFaculity() {
        return Faculity;
    }

    public void setFaculity(String faculity) {
        Faculity = faculity;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getCourse() {
        return Course;
    }

    public void setCourse(String course) {
        Course = course;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getFromTime() {
        return FromTime;
    }

    public void setFromTime(String fromTime) {
        FromTime = fromTime;
    }

    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
    }

    public String getRoom() {
        return Room;
    }

    public void setRoom(String room) {
        Room = room;
    }
}
