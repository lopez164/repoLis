/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class JcProgresInfinitearBackUp implements Runnable{
 private PerformanceInfiniteProgressPanel jProgressBar;
 private int i = 1;
 private int value = 50;//retardo en milisegundos
    
     public JcProgresInfinitearBackUp( PerformanceInfiniteProgressPanel panel , int value ){
        this.jProgressBar = panel;
        this.value = value;
        //jProgressBar.setVisible(true);
    }
    
    @Override
    public void run() {
        int  i=1;  
       // jProgressBar.setVisible(true);
        while( !JcArchivoExcelClienteCamdum.band )
        {
           // i = (i > 100) ? 1 : i+1;
            //jProgressBar.setVisible(true);
            //jProgressBar.repaint();  
            //retardo en milisegundos
            try{
                Thread.sleep( this.value );
            } catch (InterruptedException e){
            System.err.println( e.getMessage() );
           JOptionPane.showMessageDialog(null, "Error thread JcProgresInfinitearBackUp :" + e, " Alerta, cerrar ventana", 1, null);

            jProgressBar.setVisible(false);
            }  
	  // jProgressBar.setVisible(false); 
          if( JcArchivoExcelClienteCamdum.band )
            {     
                jProgressBar.setVisible(false);
                System.out.println("Trabajo finalizado...");
                //JOptionPane.showMessageDialog(null,"Trabajo finalizado...","Error",1, null);
                
                break;//rompe ciclo 
            } 
        
        }
          
        
       
    }
    
}
