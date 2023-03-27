/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *Este Hilo permite controlar el tiempo de espera
 * @author Usuario
 */
public class HiloControlRalenti implements Runnable {

    static boolean band;

   Inicio ini;
   
    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloControlRalenti(Inicio ini) {
        this.ini = ini;
       
    }

    @Override
    public void run() {
      int i=0;
       
        while (ini.getRalenti() < 120) {
          try {
              i++;
              ini.setRalenti(i); 
             System.out.println("Van " + i + " segundos");
              Thread.sleep(1000);
          } catch (InterruptedException ex) {
              Logger.getLogger(HiloControlRalenti.class.getName()).log(Level.SEVERE, null, ex);
          }
        }
        

        
        
    }
}
