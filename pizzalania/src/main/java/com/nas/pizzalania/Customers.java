/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.nas.pizzalania;

import java.util.ArrayList;

/**
 *
 * @author Farshad
 */
public class Customers {

    public ArrayList<Customer> cusList = new ArrayList<Customer>();
    private static Customers cusInstance = new Customers();

    private Customers() {
    }

    public static Customers getCusInstance() {
        return cusInstance;

    }
    
    public int isCusExist(long chat_id){
        int i = 0;
        for(Customer c:cusList){
            if(c.getChat_id() == chat_id){
                return i;
            }
            i++;
        }
        return -1;
    }

}
