/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class HiloControlDeConexion implements Runnable {

    boolean conectado=false;
    /**
     * Constructor de clase
     *
     * @param conectado
     */
    public HiloControlDeConexion(boolean conectado) {
       

    }

    @Override
    public void run() {
        try {
            
            int i = 0;
            while (i <= 10) {
               
                i++;
                Thread.sleep(1000);

            }

            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloControlDeConexion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
