package com.nas.pizzalania;

import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.Session;
import org.hibernate.Transaction;

@Entity
@Table(name = "eatable")
public class Eatable {

    @Id
    @Column(name = "id")
    private int Id;
    @Column(name = "name")
    private String Name = "";
    @Column(name = "price")
    private float price = 9999999;
    @Column(name = "des")
    private String Des;
    @Column(name = "type")
    private String type;

    public Eatable() {

    }

    public Eatable(int id, String name, float price, String Des, String type) {
        this.Id = id;
        this.Name = name;
        this.price = price;
        this.Des = Des;
        this.type = type;

    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getDes() {
        return Des;
    }

    public void setDes(String Des) {
        this.Des = Des;
    }

    public static ArrayList<Eatable> getEatableFromDbByType(int type) {
        ArrayList<Eatable> found;
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
                found = (ArrayList<Eatable>) session.createQuery("from Eatable where type =:type ").setParameter("type", type).list();
                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
        }

        return found;
    }

    public static Eatable getFoodFromDbById(String eatableId) {
        ArrayList<Eatable> found;
        final Session session = HibernateUtil.getSession();
        try {
            final Transaction transaction = session.beginTransaction();
            try {
                // The real work is here
            found = (ArrayList<Eatable>) session.createQuery("from Eatable where Id =:id ").setParameter("id", eatableId).list();
                transaction.commit();
            } catch (Exception ex) {
                // Log the exception here
                transaction.rollback();
                throw ex;
            }
        } finally {
            HibernateUtil.closeSession();
        }
        
        if (found.isEmpty()) {
            return null;
        } else {
            return found.get(0);
        }
    }

}
