/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nas.pizzalania;

import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

@Entity
@Table(name = "customer")
public class Customer {

    @Transient
    private Basket basket = new Basket(getChat_id());

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public void showAccountKeyboard() {
        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
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

        MessageControler.setKeyboard(keyboard);
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
        } catch (Exception e) {
            factory.close();
        }

    }

    public void showAccountInfo() {
        StringBuilder stringBuilder = new StringBuilder("اطلاعات حساب:");
        stringBuilder.append("\n\n")
                .append("نام: " + getfName() + "\n")
                .append("نام خانوادگی: " + getlName() + "\n")
                .append("تلفن همراه: " + getPhone() + "\n")
                .append("معرف:" + getrPhone() + "\n")
                .append("آدرس: " + getAddress() + "\n");
        MessageControler.setStringBuilder(stringBuilder);

    }

    public void finish() {
        StringBuilder stringBuilder = new StringBuilder("سفارش شما ثبت نهایی شد");
        stringBuilder.append("\n\n")
                .append("مشتری محترم توجه فرمایید به زودی با شما تماس گرفته می شود !")
                .append("و سپس سفارش شما از طریق پیک موتوری ارسال می گردد ")
                .append("\n\n هزینه سفارش حضوری و در محل اخذ می گردد!")
                .append("تشکر از خرید شما!");
        MessageControler.setStringBuilder(stringBuilder);

    }

    public void showVerifyBasket() {
        ArrayList<KeyboardRow> keyboard = new ArrayList<KeyboardRow>();
        KeyboardRow row1 = new KeyboardRow();
        if (isInfoComplete()) {
            row1.add(Constants.FINISH);
        }
        row1.add(Constants.ACCOUNT);
        keyboard.add(row1);

        row1 = new KeyboardRow();
        row1.add(Constants.BACK);
        keyboard.add(row1);

        MessageControler.setKeyboard(keyboard);

        StringBuilder stringBuilder = new StringBuilder("اگر قبلا اطلاعات خود را کامل نکرده اید لطفا با انتخاب گزینه ");
        stringBuilder.append(Constants.ACCOUNT);
        stringBuilder.append(" اطلاعات خود را تکمیل نماید.\nدر غیر اینصورت ادامه خرید را کلیک کنید.");
        MessageControler.setStringBuilder(stringBuilder);
    }

    public void saveBasket() {

        System.out.println(basket.getId_customer_fk());

        SessionFactory sessionFactory = HibernateAnnotationUtil.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        Transaction tx = null;
        try {
            // start transaction
            tx = session.beginTransaction();
            // Save the Model object
            basket.setId_customer_fk(getChat_id());
            session.save(basket);
            int basket_id = basket.getId();
            for (OrderItem oi : basket.getOrderItems()) {
                oi.setId_bill_fk(basket_id);
                session.save(oi);
            }
            // Commit transaction
            tx.commit();

        } catch (Exception e) {
            System.out.println("Exception occured. " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (!sessionFactory.isClosed()) {
                //System.out.println("Closing SessionFactory");
                //sessionFactory.close();
            }
            //basket = new Basket(chat_id);
        }
    }
}
