/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.Threads;

import aplicacionlogistica.configuracion.formularios.IngresoAlSistema_LIS_ALD;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class JcProgressBar implements Runnable {

    private JProgressBar jProgressBar;
    private int value;//retardo en milisegundos
    IngresoAlSistema_LIS_ALD ingreso;
    int caso = 0;

    /**
     * Constructor de clase
     *
     * @param jProgressBar
     * @param value
     */
    public JcProgressBar(JProgressBar jProgressBar, int value) {
        this.jProgressBar = jProgressBar;
        this.value = value;
    }

    public JcProgressBar(IngresoAlSistema_LIS_ALD ingreso) {
        this.ingreso = ingreso;
        caso = 1;
    }

    @Override
    public void run() {
        switch (caso) {
            case 0:
                caso1();
                break;
            case 1:
                caso2();
                break;

        }

    }

    private void caso1() {
        //mientra el trabajo en paralelo no finalice el jProgressBar continuara su animacion una y otra vez
        while (FManifestarPedidosEnRuta.band) {
            //si llega al limite 100 comienza otra vez desde 1, sino incrementa i en +1

            jProgressBar.setValue(FManifestarPedidosEnRuta.valorDespBarraProgreso);

            try {
                Thread.sleep(this.value);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                //JOptionPane.showMessageDialog(null, "Error thread CBackup:" + e, " Alerta, cerrar ventana", 1, null);

            }
            //si el trabajo en paralelo a terminado
            if (!FManifestarPedidosEnRuta.band) {

                jProgressBar.setValue(100);
                // JOptionPane.showMessageDialog(null, "Trabajo finalizado...", "ok..", 1, null);
                //System.out.println("Trabajo finalizado...");

                break;//rompe ciclo 
            }
        }
    }

    private void caso2() {
        //mientra el trabajo en paralelo no finalice el jProgressBar continuara su animacion una y otra vez
        while (ingreso.band) {
            //si llega al limite 100 comienza otra vez desde 1, sino incrementa i en +1
            int i = 0;
            while (i <= 100) {
                try {
                    ingreso.barraInferior.setValue(i++);
                    Thread.sleep(200);
                    //si el trabajo en paralelo a terminado
                    if (!ingreso.band) {

                        ingreso.barraInferior.setValue(100);
                        // JOptionPane.showMessageDialog(null, "Trabajo finalizado...", "ok..", 1, null);
                        //System.out.println("Trabajo finalizado...");

                        break;//rompe ciclo 
                    }

                } catch (InterruptedException ex) {
                    Logger.getLogger(JcProgressBar.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }

    }
}


