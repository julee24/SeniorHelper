package com.yejija.myapplication;

public class SeniorCenterVO {
    private String CenterName;
    private String CenterAddress;
    private double CenterX;
    private double CenterY;

    public SeniorCenterVO(){}

    public SeniorCenterVO(String CenterName, String CenterAddress, double CenterX, double CenterY){
        this.CenterName = CenterName;
        this.CenterAddress = CenterAddress;
        this.CenterX = CenterX;
        this.CenterY = CenterY;
    }

    public String getCenterName(){
        return this.CenterName;
    }
    public void setCenterName(String CenterName){
        this.CenterName = CenterName;
    }

    public String getCenterAddress(){
        return this.CenterAddress;
    }
    public void setCenterAddress(String CenterAddress){
        this.CenterAddress = CenterAddress;
    }

    public double getCenterX(){
        return this.CenterX;
    }
    public void setCenterX(String a){
        this.CenterX = Double.parseDouble(a);
    }

    public double getCenterY(){
        return this.CenterY;
    }
    public void setCenterY(String a){
        this.CenterY = Double.parseDouble(a);
    }
}
