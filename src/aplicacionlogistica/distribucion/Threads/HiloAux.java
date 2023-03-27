/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class HiloAux implements Runnable {

    static boolean band;

    JProgressBar barra;
    JLabel etiqueta;

    /**
     * Constructor de clase
     *
     * @param barra
     * @param etiqueta
     */
    public HiloAux(JProgressBar barra, JLabel etiqueta) {
        this.barra = barra;
        this.etiqueta = etiqueta;

    }

    @Override
    public void run() {
        this.etiqueta.setVisible(true);
       
        while (this.barra.getValue() < 100) {

            for (int i = 0; i <= 10; i++) {
                switch (i) {
                    case 0:
                        this.etiqueta.setText("Exportando a excel .");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HiloAux.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case 1:
                        this.etiqueta.setText("Exportando a excel ..");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HiloAux.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case 2:
                        this.etiqueta.setText("Exportando a excel ...");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HiloAux.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case 3:
                        this.etiqueta.setText("Exportando a excel ....");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HiloAux.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case 4:
                        this.etiqueta.setText("Exportando a excel .....");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HiloAux.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case 5:
                        this.etiqueta.setText("Exportando a excel ......");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HiloAux.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case 6:
                        this.etiqueta.setText("Exportando a excel .......");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HiloAux.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case 7:
                        this.etiqueta.setText("Exportando a excel ........");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HiloAux.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case 8:
                        this.etiqueta.setText("Exportando a excel .........");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HiloAux.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;
                    case 9:
                        this.etiqueta.setText("Exportando a excel ..........");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(HiloAux.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        break;

                }
            }

        }
        etiqueta.setText("Archivo exportado satisfactoriamente");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloAux.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
