/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_ProductosPorFactura;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class HiloActualizarCantidadDeProducto implements Runnable {

    CFacturas factura;
    Inicio ini;
    Double nuevoValorFactura = 0.0;
    String codigo;

    List<CProductosPorFactura> listaDeProductosModificados;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param listaDeProductosModificados
     */
    public HiloActualizarCantidadDeProducto(Inicio ini, List<CProductosPorFactura> listaDeProductosModificados) {

        this.listaDeProductosModificados = listaDeProductosModificados;
        this.ini = ini;

    }

    @Override
    public void run() {
        String sql;
        double valor;
        try {
            for (CProductosPorFactura pxf : listaDeProductosModificados) {

                valor = pxf.getValorUnitarioConIva() * pxf.getCantidad();
               // pxf.setValorUnitarioTotalConIva(valor);
                sql = "update productosporfactura set cantidad='" + pxf.getCantidad() + "',valorUnitario='" + pxf.getValorUnitarioSinIva()  + "', valorTotalConIva='" + valor + "' "
                        + " where codigoProducto='" + pxf.getCodigoProducto() + "' and "
                        + "factura='" + pxf.getNumeroFactura() + "'; ";

                ini.insertarDatosRemotamente(sql);
                ini.insertarDatosLocalmente(sql);

                nuevoValorFactura += pxf.getValorTotalLiquidacionItem();
            }

            
//            sql = "UPDATE facturascamdun SET valorTotalFactura='" + nuevoValorFactura + "' WHERE numeroFactura='" + factura.getNumeroFactura() + "';";
//            ini.insertarDatosLocalmente(sql);
//            ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            Logger.getLogger(HiloActualizarCantidadDeProducto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
