/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.integrador;

import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class JcProgressBarIntegrador implements Runnable {

    private JProgressBar jProgressBar;
    private int value;//retardo en milisegundos

    /**
     * Constructor de clase
     * @param jProgressBar
     * @param value
     */
    public JcProgressBarIntegrador(JProgressBar jProgressBar, int value) {
        this.jProgressBar = jProgressBar;
        this.value = value;
    }

    @Override
    public void run() {
       int i=0;;
       //mientra el trabajo en paralelo no finalice el jProgressBar continuara su animacion una y otra vez
        while (!FIntegrador.band) {
            //si llega al limite 100 comienza otra vez desde 1, sino incrementa i en +1
           i++;
            if(i>=100){
                i=0;
            }
            jProgressBar.setValue(i);
          
            try {
                Thread.sleep(this.value);
                
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                //JOptionPane.showMessageDialog(null, "Error thread CBackup:" + e, " Alerta, cerrar ventana", 1, null);

            }
            //si el trabajo en paralelo a terminado
            if (FIntegrador.band) {
                
                jProgressBar.setValue(100);
                
                // JOptionPane.showMessageDialog(null, "Trabajo finalizado...", "ok..", 1, null);
                //System.out.println("Trabajo finalizado...");
               
                break;//rompe ciclo 
            }
        }

    }

}
