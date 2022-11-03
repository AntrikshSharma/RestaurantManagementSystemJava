/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MainPage;

/**
 *
 * @author archi
 */
public class Run {
    
    public static void main(String args[]) throws InterruptedException {
        
        LoadingScreen Load = new LoadingScreen();
        Load.setVisible(true);
        Thread.sleep(3000);
        
        Load.dispose();
         
        LoginPage LP = new LoginPage();
        LP.setVisible(true);
        
    }
}
