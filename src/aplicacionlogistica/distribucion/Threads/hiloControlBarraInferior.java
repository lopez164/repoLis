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
public class hiloControlBarraInferior implements Runnable{
    private JProgressBar jProgressBar;
    private int val = 1;
    private int value = 50;//retardo en milisegundos

    /**
 * Constructor de clase
 */
    public hiloControlBarraInferior( JProgressBar jProgressBar , int value )
    {
        this.jProgressBar = jProgressBar;
        this.value = value;
    }

    @Override
    public void run() {
        val=1;        
        //mientra el trabajo en paralelo no finalice el jProgressBar continuara su animacion una y otra vez
        while(true ){
            //si llega al limite 100 comienza otra vez desde 1, sino incrementa i en +1
             val = (val > 100) ? 1 : val+1;
            jProgressBar.setValue(val);
            jProgressBar.repaint();  
            //retardo en milisegundos
            try{Thread.sleep( this.value );}            
            catch (InterruptedException e){ System.err.println( e.getMessage() );
             JOptionPane.showMessageDialog(null, "Error thread JcThread :" + e, " Alerta, cerrar ventana", 1, null);
            }            
            //si el trabajo en paralelo a terminado
            if( true )
            {
                while (val <= 100) {
                    jProgressBar.setValue(val);
                    jProgressBar.repaint();
                    try {
                        Thread.sleep(this.value);
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                        JOptionPane.showMessageDialog(null, "Error thread JcThread :" + e, " Alerta, cerrar ventana", 1, null);
                    }
                    val++;
                }
                jProgressBar.setValue(100);
                System.out.println("Trabajo finalizado...");
                //lopJOptionPane.showMessageDialog(null,"Trabajo finalizado...","Error",1, null);
                break;//rompe ciclo 
            }          
        }
    }

}
