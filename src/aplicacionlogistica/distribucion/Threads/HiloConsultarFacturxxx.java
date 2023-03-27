/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasPorCliente;
import aplicacionlogistica.distribucion.formularios.reportes.FDashBoardFacturas;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteFacturasPendientes;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class HiloConsultarFacturxxx implements Runnable {

    FReporteFacturasPendientes fReporteFacturasPendientes;

    FConsultarFacturasPorCliente fConsultarFacturasPorCliente;
FDashBoardFacturas fDashBoardFacturas;
    Inicio ini;
    CFacturas facturas;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloConsultarFacturxxx(Inicio ini, FConsultarFacturasPorCliente form) {

        this.fConsultarFacturasPorCliente = form;
        this.ini = ini;

    }

    
     /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloConsultarFacturxxx(Inicio ini, FReporteFacturasPendientes fReporteFacturasPendientes) {

        this.fReporteFacturasPendientes = fReporteFacturasPendientes;
        this.ini = ini;

    }
    
     /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloConsultarFacturxxx(Inicio ini, FDashBoardFacturas fDashBoardFacturas) {

        this.fDashBoardFacturas = fDashBoardFacturas;
        this.ini = ini;

    }
    @Override
    public void run() {
        if (fConsultarFacturasPorCliente != null) {
            fConsultarFacturasPorCliente.lblCargando.setVisible(true);
            int filaSelleccionada = fConsultarFacturasPorCliente.tblFacturasPorCliente.getSelectedRow();
            String factura = fConsultarFacturasPorCliente.tblFacturasPorCliente.getValueAt(filaSelleccionada, 1).toString();

            fConsultarFacturasPorCliente.consultarLafactura(factura);
            fConsultarFacturasPorCliente.lblCargando.setVisible(false);

        }

        if (fReporteFacturasPendientes != null) {
            try {
                // fReporteFacturasPendientes.lblCargando.setVisible(true);
                int filaSelleccionada = fReporteFacturasPendientes.tablaRegistros.getSelectedRow();
                String numeroFactura = fReporteFacturasPendientes.tablaRegistros.getValueAt(filaSelleccionada, 1).toString();
                CFacturas facturaActual = new CFacturas(ini, numeroFactura);

                fReporteFacturasPendientes.consultarLafactura(facturaActual);
                // fReporteFacturasPendientes.lblCargando.setVisible(false);
            } catch (Exception ex) {
                Logger.getLogger(HiloConsultarFacturxxx.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
         if (fDashBoardFacturas != null) {
            try {
                // fReporteFacturasPendientes.lblCargando.setVisible(true);
                int filaSelleccionada = fDashBoardFacturas.tablaRegistros.getSelectedRow();
                String numeroFactura = fDashBoardFacturas.tablaRegistros.getValueAt(filaSelleccionada, 0).toString();
                CFacturas facturaActual = new CFacturas(ini, numeroFactura);

                fDashBoardFacturas.consultarLafactura(facturaActual);
                // fReporteFacturasPendientes.lblCargando.setVisible(false);
            } catch (Exception ex) {
                Logger.getLogger(HiloConsultarFacturxxx.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

}
