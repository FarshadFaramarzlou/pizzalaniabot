/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nas.pizzalania;

import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * @author Farshad
 */
public class Customers {

    /**
     *
     */
    private ArrayList<Customer> cusList = new ArrayList<Customer>();
    private static Customers cusInstance;

    private Customers() {
    }

    public static Customers getCusInstance() {
        if (cusInstance != null) {
            return cusInstance;
        } else {
            cusInstance = new Customers();
            return cusInstance;
        }
    }

    public ArrayList<Customer> getCusList() {
        return cusList;
    }

    public Customer getCustumerFromList(long chat_id) {
        for (Customer c : cusList) {
            if (c.getChat_id() == chat_id) {
                return c;
            }
        }
        return null;
    }

    public boolean addCustumerToList(Customer cus) {
        for (Customer c : cusList) {
            if (c == cus) {
                return false;
            } else {
                this.cusList.add(cus);
                return true;
            }
        }
        return false;
    }

    public boolean isCusExist(long chat_id) {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class).buildSessionFactory();
        Session session = factory.getCurrentSession();
        ArrayList<Customer> found;
        try {
            session.beginTransaction();
            found = (ArrayList<Customer>) session.createQuery("from Customer where chat_id =:id ").setParameter("id", chat_id).list();
            session.getTransaction().commit();
        } finally {
            factory.close();
        }

        if (!found.isEmpty()) {
            cusList.add(found.get(0));
            return true;
        } else {
            return false;
        }
    }

    public boolean isCustomer(long chat_id) {
        for (Customer c : getCusList()) {
            System.out.println("Customer:" + c.getChat_id());
            if(c.getChat_id() == chat_id){
                return true;
            }
        }
        return false;
    }

    public boolean newCustomer(Customer cus) {
        SessionFactory factory = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Customer.class).buildSessionFactory();
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

}
