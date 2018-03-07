package com.nas.pizzalania;

import java.util.ArrayList;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class Customers {

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
        final Session session = HibernateUtil.getSession();
        ArrayList<Customer> found;
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
            found = (ArrayList<Customer>) session.createQuery("from Customer where chat_id =:id ").setParameter("id", chat_id).list();
                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
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
            if (c.getChat_id() == chat_id) {
                return true;
            }
        }
        return false;
    }

    public boolean newCustomer(Customer cus) {
        
        final Session session = HibernateUtil.getSession();
        ArrayList<Customer> found;
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
            session.save(cus);
                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
        }
        
        return isCusExist(cus.getChat_id());
    }

}
