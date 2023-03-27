/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FIncidenciasSvC;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasPorCliente;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.personas.CClientes;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloConsultarCliente implements Runnable {

    FIncidenciasSvC  form;
    Inicio ini;
    

    CFacturas facturas;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param facturas
     */
    public  HiloConsultarCliente(Inicio ini,FIncidenciasSvC  form) {

        this.form = form;
       this.ini=ini;

    }

    @Override
    public void run() {
        // limpiarTodo();
        this.form.lblCirculoDeProgreso.setVisible(true);
         this.form.lblCargandoUnMomento.setVisible(true);

        try {
            /*Crea objeto desde la BBDD remota*/
            this.form.cliente = new CClientes( this.ini,  this.form.txtCodigoCliente.getText().trim(), true);
            try {

                if (!this.form.cliente.getCodigoInterno().equals("")) {
                    this.form.limpiarTablaFacturasPorCliente();

                    this.form.txtDireccionDelCliente1.setText(this.form.cliente.getDireccion());
                    this.form.txtBarrioCliente1.setText(this.form.cliente.getBarrio());
                    this.form.txtNombreDelCliente1.setText(this.form.cliente.getNombreDeCliente());
                    this.form.txtTelefonoDelCliente1.setText(this.form.cliente.getCelularCliente());

                    /*Consulta  las facturas del servidor remoto*/
                    this.form.cliente.setListaDeFacturasDescargadas(true);

                    this.form.llenarTablaFacturasPorCliente();
                    
                    /*Hilo que recupera los movimientos de la factura */
                    this.form.lblCirculoDeProgreso.setVisible(false);
                    this.form.lblCargandoUnMomento.setVisible(false);
                    //  new Thread(new HiloListadoConsultadeFacturaBitacora(ini, this)).start();
                } else {
                    JOptionPane.showMessageDialog(this.form, "Cliente no existe en el sistema", "Error", JOptionPane.WARNING_MESSAGE);

                }

            } catch (Exception ex) {

                Logger.getLogger(FConsultarFacturasPorCliente.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (Exception ex) {
            Logger.getLogger(FConsultarFacturasPorCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
      
         

    }
}
