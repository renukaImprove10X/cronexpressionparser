package com.deliveroo.renuka.models;

import java.util.Objects;

import static com.deliveroo.renuka.parsers.FieldType.*;

public class CronData{
    private String minutes;
    private String hours;
    private String dayOfMonth;
    private String month;
    private String dayOfWeek;
    private String command;

    public CronData(String minutes, String hours, String dayOfMonth, String month, String dayOfWeek, String command){
        this.minutes = minutes;
        this.hours = hours;
        this.dayOfMonth = dayOfMonth;
        this.month = month;
        this.dayOfWeek = dayOfWeek;
        this.command = command;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CronData cronData = (CronData) o;
        return Objects.equals(minutes, cronData.minutes) && Objects.equals(hours, cronData.hours) && Objects.equals(dayOfMonth, cronData.dayOfMonth) &&
                Objects.equals(month, cronData.month) && Objects.equals(dayOfWeek, cronData.dayOfWeek) && Objects.equals(command, cronData.command);
    }

    @Override
    public int hashCode() {
        return Objects.hash(minutes, hours, dayOfMonth, month, dayOfWeek, command);
    }

    @Override
    public String toString() {
        return "CronData{" +
                "minutes=" + minutes +
                ", hours=" + hours +
                ", daysOfMonth=" + dayOfMonth +
                ", months=" + month +
                ", daysOfWeek=" + dayOfWeek +
                ", command='" + command + '\'' +
                '}';
    }

    public String getMinutes() {
        return minutes;
    }

    public String getHours() {
        return hours;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public String getMonth() {
        return month;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public String getCommand() {
        return command;
    }

    public void print() {
        System.out.printf("%-14s%s\n", MINUTE.label, minutes);
        System.out.printf("%-14s%s\n", HOUR.label, hours);
        System.out.printf("%-14s%s\n", DAY_OF_MONTH.label, dayOfMonth);
        System.out.printf("%-14s%s\n", MONTH.label, month);
        System.out.printf("%-14s%s\n", DAY_OF_WEEK.label, dayOfWeek);
        System.out.printf("%-14s%s\n", COMMAND.label, command);
    }

}
