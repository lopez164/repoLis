/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class HiloActualizarPrecioUnitario implements Runnable {

    Double valor;
    String codigoProducto;
    Inicio ini;
    String numeroFactura;

   

    /**
     * Constructor de clase
     *
     * @param ini
     * @param valor
     * @param codigoProducto
     * @param numeroFactura
     */
    public  HiloActualizarPrecioUnitario(Inicio ini, String valor, String codigoProducto,String numeroFactura) {

        this.valor=Double.parseDouble(valor);
        this.ini = ini;
        this.codigoProducto=codigoProducto;
        this.numeroFactura=numeroFactura;
    }

    @Override
    public void run() {
        String sql1, sql2;
        try {
            if (valor > 1) {
                sql1 = "update productoscamdun set valorUnitario='" + valor + "' where codigoProducto='" + codigoProducto + "'; ";
                sql2 = "update productosporfactura set valorUnitario='" + valor + "' where codigoProducto='" + codigoProducto + "' and factura='" + numeroFactura + "';";
                
                System.out.println(sql1);

                ini.insertarDatosLocalmente(sql1);
                ini.insertarDatosRemotamente(sql1);
                ini.insertarDatosLocalmente(sql2);
                ini.insertarDatosRemotamente(sql2);
            }
            
        } catch (Exception ex) {
            Logger.getLogger(HiloActualizarPrecioUnitario.class.getName()).log(Level.SEVERE, null, ex);
        }
         

    }
}
