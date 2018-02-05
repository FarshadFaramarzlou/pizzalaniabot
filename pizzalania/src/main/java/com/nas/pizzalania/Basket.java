package com.nas.pizzalania;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.KeyboardRow;

@Entity
@Table(name = "basket")
public class Basket {

    @Transient
    private ArrayList<OrderItem> orderItems;
    @Id
    @Column(name = "id")
    private int id;
    @Column(name = "date")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date date;
    //tahvilType p:peyk -- h:hozoori
    @Column(name = "tahvilType")
    private String tahvilType = "p";
    //payType n:interneti -- p:tahvilPeyk -- h:hozoori
    @Column(name = "payType")
    private String payType = "p";
    //private DoDate doDate;
    @Column(name = "moneySum")
    private double moneySum;
    @Column(name = "rebateSum")
    private double rebateSum;
    @Transient
    private ArrayList<KeyboardRow> keyboard = null;
    @Transient
    private StringBuilder stringBuilder = null;

    public Basket() {
        this.orderItems = new ArrayList<>();
    }

    public void sumItemMoney() {

        this.moneySum = 0;

    }

    public void sumItemRebate() {

        this.rebateSum = 0;

    }

    public ArrayList<OrderItem> getOrderItems() {
        return orderItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTahvilType() {
        return tahvilType;
    }

    public void setTahvilType(String tahvilType) {
        this.tahvilType = tahvilType;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public double getMoneySum() {
        return moneySum;
    }

    public void setMoneySum(double moneySum) {
        this.moneySum = moneySum;
    }

    public double getRebateSum() {
        return rebateSum;
    }

    public void setRebateSum(double rebateSum) {
        this.rebateSum = rebateSum;
    }

    public ArrayList<KeyboardRow> getKeyboard() {
        return keyboard;
    }

    public void setKeyboard(ArrayList<KeyboardRow> keyboard) {
        this.keyboard = keyboard;
    }

    public StringBuilder getStringBuilder() {
        return stringBuilder;
    }

    public void setStringBuilder(StringBuilder stringBuilder) {
        this.stringBuilder = stringBuilder;
    }

    public boolean newOrder(Customer cus) {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(OrderItem.class).buildSessionFactory();
        Session session = factory.getCurrentSession();
        try {
            session.beginTransaction();
            session.save(cus);
            session.getTransaction().commit();
        } finally {
            factory.close();
        }
        return true;
    }

    public void selectEatable(int eatableId) {
        OrderItem oi = new OrderItem();
        oi.selectEatable(eatableId);
        this.orderItems.add(oi);
    }

    public void selectNum(int num) {
        int oiSize = orderItems.size() - 1;
        this.orderItems.get(oiSize).setNum(num);
    }

    public void showBasket(long chat_id) {
        if (getOrderItems().isEmpty()) {
            keyboard = new ArrayList<KeyboardRow>();
            KeyboardRow row1 = new KeyboardRow();
            row1.add(Constants.SHOWLASTORDER);
            keyboard.add(row1);

            row1 = new KeyboardRow();
            row1.add(Constants.ADDTOBASKET);
            keyboard.add(row1);

            row1 = new KeyboardRow();
            row1.add(Constants.BACK);
            keyboard.add(row1);

            stringBuilder = new StringBuilder("سبد شما خالی است.");
            stringBuilder.append("\n\n");
            stringBuilder.append("با فشردن دکمه\" " + Constants.SHOWLASTORDER + "\"" + "می توانید آخرین خرید خود را تکرار کنید.");

        } else {

            keyboard = new ArrayList<KeyboardRow>();
            KeyboardRow row1 = new KeyboardRow();
            row1.add(Constants.FINISHBASKET);
            keyboard.add(row1);

            row1 = new KeyboardRow();
            row1.add(Constants.ADDTOBASKET);
            row1.add(Constants.EMPTYBASKET);
            keyboard.add(row1);

            row1 = new KeyboardRow();
            row1.add(Constants.BACK);
            keyboard.add(row1);
            int j = 1;
            stringBuilder = new StringBuilder("سبد خرید:");
            stringBuilder.append("\n");

            for (OrderItem o : getOrderItems()) {
                stringBuilder.append("\n").append(j)
                        .append("- ")
                        .append(o.getEatable().getName())
                        .append(" - قیمت:")
                        .append(o.getEatable().getPrice())
                        .append(" - تعداد:")
                        .append(o.getNum());
                j++;
            }
        }

    }
}
