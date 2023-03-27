/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class JcProgressBar implements Runnable {

    private JProgressBar jProgressBar;
    private int i = 1;
    private int value = 50;//retardo en milisegundos

    /**
     * Constructor de clase
     */
    public JcProgressBar(JProgressBar jProgressBar, int value) {
        this.jProgressBar = jProgressBar;
        this.value = value;
    }

    @Override
    public void run() {
        i = 1;
        //mientra el trabajo en paralelo no finalice el jProgressBar continuara su animacion una y otra vez
        while (!JcArchivoExcelProductosPorFacturaCamdun.band) {
            //si llega al limite 100 comienza otra vez desde 1, sino incrementa i en +1
            i = (i > 100) ? 1 : i + 1;
            //jProgressBar.setValue(i);
            // jProgressBar.repaint();  
            //retardo en milisegundos
            try {
                Thread.sleep(this.value);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread CBackup:" + e, " Alerta, cerrar ventana", 1, null);

            }
            //si el trabajo en paralelo a terminado
            if (JcArchivoExcelProductosPorFacturaCamdun.band) {
                
                jProgressBar.setValue(100);
                 JOptionPane.showMessageDialog(null, "Trabajo finalizado...", "ok..", 1, null);
                System.out.println("Trabajo finalizado...");
               
                break;//rompe ciclo 
            }
        }

    }

}
