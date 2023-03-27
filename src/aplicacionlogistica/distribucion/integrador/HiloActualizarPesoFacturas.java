/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.integrador;

import aplicacionlogistica.distribucion.Threads.*;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Esta clase es unhilo Thread, que perrmite guardar los registros en una BBDD
 * remota
 *
 *
 * @author Luis Eduardo López Casanova
 */
public class HiloActualizarPesoFacturas implements Runnable {

    Inicio ini;
    String fecha = "";
    String cadena = null;
    boolean local = false;

    /**
     * Método constructor sin parámetros
     *
     *
     */
    public HiloActualizarPesoFacturas() {

    }

    /**
     * Método constructor
     *
     * @param ini clase Inicio que contiene datos de la configuración del
     * sistema
     *
     */
    public HiloActualizarPesoFacturas(Inicio ini, String fecha) {
        this.ini = ini;
        this.fecha = fecha;

    }

    @Override
    public void run() {

       try {

            String sql = "update facturascamdun fc "
                    + "join productosporfactura pf on pf.factura=fc.numeroFactura "
                    + "set fc.pesofactura=(select sum(pf2.pesoProducto) from productosporfactura pf2 where "
                    + "pf2.factura= fc.numeroFactura group by fc.numeroFactura) "
                    + "where fc.fechaDeVenta='" + fecha + "';";

            ini.insertarDatosLocalmente(sql);
            ini.insertarDatosRemotamente(sql);

        } catch (SQLException ex) {
            Logger.getLogger(HiloIntegradorTNS.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean actualizarPesoFacturas() {
        boolean actualizado = false;

        try {

            String sql = "update facturascamdun fc "
                    + "join productosporfactura pf on pf.factura=fc.numeroFactura "
                    + "set fc.pesofactura=(select sum(pf2.pesoProducto) from productosporfactura pf2 where "
                    + "pf2.factura= fc.numeroFactura group by fc.numeroFactura) "
                    + "where fc.fechaDeVenta='" + fecha + "';";

            ini.insertarDatosLocalmente(sql);
            ini.insertarDatosRemotamente(sql);

        } catch (SQLException ex) {
            Logger.getLogger(HiloIntegradorTNS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return actualizado;
    }

}
