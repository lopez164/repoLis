/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.consultas;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.personas.CClientes;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloConsultarFacturasPorCliente implements Runnable {

    FConsultarFacturasPorCliente fConsultarFacturasPorCliente;
    Inicio ini;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param facturas
     */
    public HiloConsultarFacturasPorCliente(Inicio ini, FConsultarFacturasPorCliente form) {

        this.fConsultarFacturasPorCliente = form;
        this.ini = ini;

    }

    @Override
    public void run() {

        this.fConsultarFacturasPorCliente.lblCirculoDeProgreso.setVisible(true);

        try {
            /*Crea objeto desde la BBDD remota*/
            this.fConsultarFacturasPorCliente.cliente = new CClientes(ini, this.fConsultarFacturasPorCliente.txtCodigoCliente.getText().trim(), true);
            try {

                if (this.fConsultarFacturasPorCliente.cliente.getCodigoInterno() != null) {
                    this.fConsultarFacturasPorCliente.limpiarTablaFacturasPorCliente();

                    this.fConsultarFacturasPorCliente.txtDireccionDelCliente.setText(this.fConsultarFacturasPorCliente.cliente.getDireccion());
                    this.fConsultarFacturasPorCliente.txtBarrioCliente.setText(this.fConsultarFacturasPorCliente.cliente.getBarrio());
                    this.fConsultarFacturasPorCliente.txtNombreDelCliente.setText(this.fConsultarFacturasPorCliente.cliente.getNombreDeCliente());
                    this.fConsultarFacturasPorCliente.txtTelefonoDelCliente.setText(this.fConsultarFacturasPorCliente.cliente.getCelularCliente());

                    /*Consulta  las facturas del servidor remoto*/
                    this.fConsultarFacturasPorCliente.cliente.setListaDeFacturasPorCliente(true);

                    this.fConsultarFacturasPorCliente.llenarTablaFacturasPorCliente();
                    /*Hilo que recupera los movimientos de la factura */
                    this.fConsultarFacturasPorCliente.lblCirculoDeProgreso.setVisible(false);
                    //  new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this)).start();

                    this.fConsultarFacturasPorCliente.txtCodigoCliente.setEnabled(false);
                    this.fConsultarFacturasPorCliente.txtParteDelNombreDelCliente.setEnabled(false);

                    this.fConsultarFacturasPorCliente.txtCodigoCliente.setEditable(false);
                    this.fConsultarFacturasPorCliente.txtParteDelNombreDelCliente.setEditable(false);

                } else {
                    JOptionPane.showMessageDialog(this.fConsultarFacturasPorCliente, "Cliente no existe en el sistema", "Error", JOptionPane.WARNING_MESSAGE);
                    this.fConsultarFacturasPorCliente.txtCodigoCliente.requestFocus();
                }

            } catch (Exception ex) {

                Logger.getLogger(FConsultarFacturasPorCliente.class.getName()).log(Level.SEVERE, null, ex);
                this.fConsultarFacturasPorCliente.txtCodigoCliente.requestFocus();
            }

        } catch (Exception ex) {
            Logger.getLogger(FConsultarFacturasPorCliente.class.getName()).log(Level.SEVERE, null, ex);
            this.fConsultarFacturasPorCliente.txtCodigoCliente.requestFocus();
        }

    }
}
