package com.yejija.myapplication;

public class User {
    public String userName;
    public String num;
    public String num1;
    public String num2;
    public String num3;
    public String year;
    public String month;
    public String day;

    public User() {

    }

    public User(String userName, String num1, String num2, String num3, String year, String month, String day, String num) {
        this.userName = userName;
        this.num1 = num1;
        this.num2 = num2;
        this.num3 = num3;
        this.year = year;
        this.month = month;
        this.day = day;
        this.num = num;
    }

    public String getuserName() {
        return userName;
    }

    public void setData(String userName) {
        this.userName = userName;
    }

    public String getNum1() {
        return num1;
    }

    public void setNum1(String num1) {
        this.num1 = num1;
    }

    public String getNum2() {
        return num2;
    }

    public void setNum2(String num2) {
        this.num2 = num2;
    }

    public String getNum3() {
        return num3;
    }

    public void setNum3(String num3) {
        this.num3 = num3;
    }

    public String getnum() {
        return num;
    }

    public void setnum(String num) {
        this.num = num;
    }

    public String getyear() {
        return year;
    }

    public void setyear(String year) {
        this.year = year;
    }

    public String getmonth() {
        return month;
    }

    public void setmonth(String month) {
        this.month = month;
    }

    public String getday() {
        return day;
    }

    public void setday(String day) {
        this.day = day;
    }
    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", num1='" + num1 + '\'' +
                ", num2='" + num2 + '\'' +
                ", num3='" + num3 + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", day='" + day + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
