/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nas.pizzalania;

/**
 *
 * @author Farshad
 */
public class OrderItem {
    private Eatable eatable;
    private int num =0;

    public OrderItem() {
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
    }
    
    
    public double sumItem(){
        return num*eatable.getPrice();
    }

    
    public void selectEatable(int eatableId){
        System.out.println(eatableId);
         this.eatable = Eatable.getFoodFromDbById(eatableId);
    }
    
}
