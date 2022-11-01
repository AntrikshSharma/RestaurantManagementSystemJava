/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Interfaces;


abstract class Employee implements Person {
    
    String Username;
    String Password;
    long PhoneNum;
    
    
    public String getUsername() {
        return Username;
    }
    
    public String getPassword() {
        return Password;
    }
    
    public long getPhone() {
        return PhoneNum;
    }
    
}
