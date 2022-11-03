package main.restaurantmanagesystem;
import LoadingScreen.SplashScreen;
import LoginScreen.LoginMain;

import java.util.logging.Level;
import java.util.logging.Logger;



        
public class RestaurantManageSystem {

    public static void main(String[] args) throws InterruptedException {
        SplashScreen LS = new SplashScreen();
        LS.setVisible(true);
        
        // ADD THE RESOURCE LOADING CODE HERE
        Thread.sleep(3000);
        LS.dispose();
//        SplashScreen LS = new SplashScreen();
//        LS.setVisible(true);
        
         //ADD THE RESOURCE LOADING CODE HERE
        
//        for ( int i=0; i<5; ++i)
//        {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException ex) {
//                System.out.println(ex.toString());
//            }
//
//        }
//        LS.dispose();
        LoginMain LP = new LoginMain();
        LP.setVisible(true);
        
        
    }
}
