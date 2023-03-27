/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;


import aplicacionlogistica.configuracion.Inicio;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase es unhilo Thread, que perrmite guardar los registros  en una BBDD remota
 * 
 * 
 * @author Luis Eduardo López Casanova
 */
public class HiloGuardarRegistro implements Runnable {

    Inicio ini;
     String sentenciaSQL;
     String mensaje;
     String rutaDelArchivo ;
      
    /**
     * Método constructor sin parámetros
     *
     * 
      */
    public HiloGuardarRegistro() {
        
    }
    
/**
     * Método constructor
     *
     * @param ini clase Inicio que contiene datos de la configuración del sistema
     * @param sentenciaSQL corresponde a cadena con la sentencia SQl que permite realizar la operacion en la BBDD
       * 
      */
     public HiloGuardarRegistro(Inicio ini,String sentenciaSQL ) {
        this.ini = ini;
        this.sentenciaSQL=sentenciaSQL;
             
    }
   
    @Override
    @SuppressWarnings("SleepWhileInLoop")
    public void run() {
      boolean ok=false;  
         String  val=null;
           while (!ok) {
            
            //System.out.println("acá  entra al while " + mensaje);
            try {
               
                ok=ini.insertarDatosRemotamente(sentenciaSQL,"HiloGuardarRegistro");
               
                Thread.sleep(3);
            } catch (InterruptedException ex) {
                 System.err.println(ex.getMessage());
                Logger.getLogger(HiloGuardarRegistro.class.getName()).log(Level.SEVERE, null, ex);
               
            }
        }
         //System.out.println("acá  sale del while " + mensaje);
 
}
    
}
