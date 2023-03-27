/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import java.sql.ResultSet;
import java.util.List;

/**
 *
 * @author Usuario
 */
public class HiloListadoDeProductosPorFactura implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    Inicio ini;
    CFacturas factura;
    List<CProductosPorFactura> arrProductosPorFactura = null;

    ResultSet rst = null;

    /**
     * Constructor de clase
     * @param ini
     * @param tiempo
     * @param factura
     */
    public HiloListadoDeProductosPorFactura(Inicio ini, int tiempo, CFacturas factura) {
        this.tiempo = tiempo;
        this.ini = ini;
       this.factura=factura;
    }

    @Override
    public void run() {
        
     factura.getListaCProductosPorFactura();
           
    }
}
