/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.integrador.sincronizacion.threads;

//import sincronizacion.IniciarSincronizacion;

import aplicacionlogistica.distribucion.integrador.sincronizacion.IniciarSincronizacion;


/**
 *
 * @author Usuario
 */
public class HiloActualizarCerrarSincronizacion implements Runnable {

    public static boolean band = false;
    private final int tiempo = 5;
   

   IniciarSincronizacion ini;

    /**
     * Constructor de clase
     *
     * @param ini
     *
     */
    public  HiloActualizarCerrarSincronizacion(IniciarSincronizacion ini) {

      this.ini=ini;
       

    }

    @Override
    public void run() {
      ini.cerrarSincronizacion(true);

    }
    
    
   
}
