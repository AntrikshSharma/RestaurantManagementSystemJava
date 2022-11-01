/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;

import java.util.List;

/**
 *
 * @author archi
 */
public class Customer implements Person {

    String Name;
    String Email;
    long PhoneNum;
    
    
    Customer(String name, String email, long phn) {
        Name = name;
        Email  = email;
        PhoneNum = phn;
    }
    
    @Override
    public String getName() {
        return Name;
    }

    @Override
    public String getEmail() {
        return Email;
    }
    
    public long getPhone() {
        return PhoneNum;
    }
    
    @Override
    public void save() {
        
    }

    @Override
    public List fetchAll() {
        return null;
    }
    
}
