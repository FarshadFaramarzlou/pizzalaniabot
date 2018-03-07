package com.nas.pizzalania;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(name = "num")
    private int num = 0;

    @Column(name = "id_bill_fk")
    private int id_bill_fk;

    @Transient
    private Eatable eatable;
    @Column(name = "id_eatable_fk")
    private int id_eatable_fk;

    public int getId_bill_fk() {
        return id_bill_fk;
    }

    public void setId_bill_fk(int id_bill_fk) {
        this.id_bill_fk = id_bill_fk;
    }

    public int getId_eatable_fk() {
        return id_eatable_fk;
    }

    public void setId_eatable_fk(int id_eatable_fk) {
        this.id_eatable_fk = id_eatable_fk;
    }

    public OrderItem() {
    }

    public int getId_basket_fk() {
        return id_bill_fk;
    }

    public void setId_basket_fk(int id_basket_fk) {
        this.id_bill_fk = id_basket_fk;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Eatable getEatable() {
        return eatable;
    }

    public void setEatable(Eatable eatable) {
        this.eatable = eatable;
        setId_eatable_fk(eatable.getId());
    }

    public double sumItem() {
        return num * eatable.getPrice();
    }

    public void selectEatable(String eatableId) {
       setEatable(Eatable.getFoodFromDbById(eatableId));

    }

}
