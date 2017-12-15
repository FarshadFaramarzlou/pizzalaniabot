/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nas.pizzalania;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "customer")
public class Customer {

    @Transient
    private Basket basket = new Basket();
    @Id
    @Column(name = "customer_id")
    private int customer_id;
    @Column(name = "chat_id")
    private long chat_id;
    @Column(name = "first_name")
    private String fName = "";
    @Column(name = "last_name")
    private String lName = "";
    @Column(name = "phone")
    private String phone = "";
    @Column(name = "address")
    private String address = "";
    @Column(name = "rPhone")
    private String rPhone = "";
    @Column(name = "p_is_c")
    private int p_is_c;

    public Customer() {
    }

    public Customer(long chat_id) {
        this.chat_id = chat_id;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public long getChat_id() {
        return chat_id;
    }

    public void setChat_id(long chat_id) {
        this.chat_id = chat_id;
    }

    public int getP_is_c() {
        return p_is_c;
    }

    public void setP_is_c(int p_is_c) {
        this.p_is_c = p_is_c;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getrPhone() {
        return rPhone;
    }

    public void setrPhone(String rPhone) {
        this.rPhone = rPhone;
    }

}
