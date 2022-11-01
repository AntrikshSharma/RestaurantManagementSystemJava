/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Interfaces;

import java.util.List;


public interface Person {
    
    public String getName();
    public String getEmail();
    
    public void save();
    public List fetchAll();
}
