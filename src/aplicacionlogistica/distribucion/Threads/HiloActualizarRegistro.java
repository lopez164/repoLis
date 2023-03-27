/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CFacturas;

/**
 *
 * @author Usuario
 */
public class HiloActualizarRegistro implements Runnable {

    public static boolean band = false;
    private final int tiempo = 5;
    Inicio ini;

    CFacturas facturas;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param facturas
     */
    public  HiloActualizarRegistro(Inicio ini, CFacturas  facturas) {

        this.facturas = facturas;
        this.ini = ini;

    }

    @Override
    public void run() {
        boolean ok=false;
        while(!ok){
            if(facturas.actualizarFactura()){
                  System.out.println(" se actualiza la Factura # " + facturas.getNumeroDeFactura() + " actualizada ");
                ok=true;
            }
            
        }
      
         

    }
}
