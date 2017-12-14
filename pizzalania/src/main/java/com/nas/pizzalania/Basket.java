package com.nas.pizzalania;

import java.util.ArrayList;
import java.util.Date;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Basket {

    private ArrayList<OrderItem> orderItems;

    private int id;
    private Date date;
    //tahvilType p:peyk -- h:hozoori
    private String tahvilType = "p";
    //payType n:interneti -- p:tahvilPeyk -- h:hozoori
    private String payType = "p";
    //private DoDate doDate;
    private double moneySum;
    private double rebateSum;

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
}
