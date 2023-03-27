/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion.formularios;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author lelopez
 */
public class TimerApp {
     public static void main(String args[]) {
    // new JFrame().setVisible( true );
    ActionListener actionListener = new ActionListener() {
      @Override
        public void actionPerformed(ActionEvent actionEvent) {
        System.out.println( "expired" );
      }
    
    };
    Timer timer = new Timer( 30000, actionListener );
    timer.start();
  }
}
