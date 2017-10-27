package com.nas.pizzalania;

public class Bill {
    private String operator="";
    private String chrgType="";
    private String price="";
    private String phoneNum="";

    public Bill() {
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getChrgType() {
        return chrgType;
    }

    public void setChrgType(String chrgType) {
        this.chrgType = chrgType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
}
