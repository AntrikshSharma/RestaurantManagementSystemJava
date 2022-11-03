package Classes;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Beverage implements Item{
    private String name;
    private float price;
    private int qty;
    
    public Beverage() {
        this.name = "";
        this.price = 0.0f;
    }
    
    public Beverage (String n, float p){
        name = n;
        price = p;
    }
    
    public String getName(){
        return name;
    }
    
    public float getPrice(){
        return price;
    }
    
    public int getQty(){
        return qty;
    }
    
     public int save() { 
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/rest";
        String userName = "root";
        String password = "admin@123";
        try{
            conn = DriverManager.getConnection(url, userName, password);
            
            String query = 
                    "insert into rest.food_items(type, price, name) values(\"Beverage\", " + this.price + ", \""  + this.name + "\");";
            
            Statement stm = conn.createStatement();

            stm.execute(query);
            
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Food.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
   public int remove(String name) { 
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/rest";
        String userName = "root";
        String password = "admin@123";
        try{
            conn = DriverManager.getConnection(url, userName, password);
            
            String query = "delete from rest.food_items where name = \"" + name + "\";";
            
            Statement stm = conn.createStatement();

            stm.execute(query);
            
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Food.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    } 
public List getAll() {
        List<Beverage> obj = new ArrayList<>();
        Connection conn = null;
        String url = "jdbc:mysql://localhost:3306/rest";
        String userName = "root";
        String password = "admin@123";
        try{
            conn = DriverManager.getConnection(url, userName, password);
            
            String query = 
                    "select * from rest.food_items where type=\"Beverage\";";
            
            Statement stm = conn.createStatement();
            
            ResultSet result = stm.executeQuery(query);
            
            while ( result.next() ) {
                Beverage bev = new Beverage(result.getString(3), Float.parseFloat(result.getString(2)));
                obj.add(bev);
            }
            
//            stm.execute(query);
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Food.class.getName()).log(Level.SEVERE, null, ex);
            }
            return obj;
        }
    }
   
}

