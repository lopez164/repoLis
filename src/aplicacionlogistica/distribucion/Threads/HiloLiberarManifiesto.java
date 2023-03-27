/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class HiloLiberarManifiesto implements Runnable {

    static boolean band;

    CManifiestosDeDistribucion manifiesto;
    boolean valor = false;

    /**
     * Constructor de clase
     *
     * @param manifiesto
     * @param valor
     */
    public HiloLiberarManifiesto(CManifiestosDeDistribucion manifiesto, boolean valor) {
        this.manifiesto = manifiesto;
    }

    @Override
    public void run() {
        manifiesto.liberarManifiesto(valor);

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloLiberarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
