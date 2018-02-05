/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nas.pizzalania;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

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
    @Transient
    private int chatState = 0;

    public Customer() {

    }

    public List<KeyboardRow> showAccountKeyboard() {
        List<KeyboardRow> keyboard = new ArrayList<>();
        if (getP_is_c() == 0) {
            KeyboardRow row1 = new KeyboardRow();

            if (getfName().equals("")) {
                row1.add(Constants.FNAME + Constants.WARN);
                System.out.println(getfName());
            } else {
                row1.add(Constants.FNAME);
            }

            if (getlName().equals("")) {
                row1.add(Constants.LNAME + Constants.WARN);
            } else {
                row1.add(Constants.LNAME);
            }

            keyboard.add(row1);

            row1 = new KeyboardRow();

            if (getPhone().equals("")) {
                row1.add(Constants.PHONE + Constants.WARN);
            } else {
                row1.add(Constants.PHONE);
            }

            if (getrPhone().equals("")) {
                row1.add(Constants.RPHONE + Constants.WARN);
            } else {
                row1.add(Constants.RPHONE);
            }
            keyboard.add(row1);

            row1 = new KeyboardRow();
            if (getAddress().equals("")) {
                row1.add(Constants.ADDRESS + Constants.WARN);
            } else {
                row1.add(Constants.ADDRESS);
            }
            row1.add(Constants.BACK);
            keyboard.add(row1);
            //sendMessageKB(chat_id, "اطلاعات شما تکمیل نیست!", keyboard);
        } else if (getP_is_c() == 1) {

        }
        return keyboard;

    }

    public boolean isInfoComplete() {
        return !(getfName().equals("") || getlName().equals("") || getPhone().equals("") || getAddress().equals(""));
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

    public int getChatState() {
        return chatState;
    }

    public void setChatState(int chatState) {
        this.chatState = chatState;
    }

    public void updateCustomer(Customer cus) {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class).buildSessionFactory();
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            session.update(cus);
            session.getTransaction().commit();
        } finally {
            factory.close();
        }

    }

    public String showAccountInfo() {
        StringBuilder stringBuilder = new StringBuilder("اطلاعات حساب:");
        stringBuilder.append("\n\n")
                .append("نام: " + getfName()+"\n")
                .append("نام خانوادگی: "+ getlName()+"\n")
                .append("تلفن همراه: "+getPhone()+"\n")
                .append("معرف:"+getrPhone()+"\n")
                .append("آدرس: "+getAddress()+"\n");
        return stringBuilder.toString();

    }

}
