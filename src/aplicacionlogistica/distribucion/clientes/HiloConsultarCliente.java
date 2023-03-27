/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.clientes;

import aplicacionlogistica.configuracion.Inicio;
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

    FGeoreferenciarClientes form;
    Inicio ini;

    CFacturas facturas;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param facturas
     */
    public HiloConsultarCliente(FGeoreferenciarClientes form) {

        this.form = form;
        this.ini=form.ini;
    }

    @Override
    public void run() {
        // limpiarTodo();
        this.form.lblCirculoDeProgreso.setVisible(true);
       // this.form.lblCargandoUnMomento.setVisible(true);

        try {
            if (!form.txtCoordenadas.getText().equals("")) {

                form.cliente = new CClientes(ini, form.txtcodigoCliente.getText().trim(), true);

                form.band = true;

                if (form.cliente.getCodigoInterno() != null) {
                    form.txtNombreCliente.setEditable(true);
                    form.txtDireccionCliente.setEditable(true);
                    form.txtBarriCliente.setEditable(true);
                    form.txtCiudadCliente.setEditable(true);

                    form.txtNombreCliente.setText(form.cliente.getNombreDeCliente());
                    form.txtDireccionCliente.setText(form.cliente.getDireccion());
                    form.txtBarriCliente.setText(form.cliente.getBarrio());
                    form.txtCiudadCliente.setText(form.cliente.getCiudad());

                    this.form.lblCirculoDeProgreso.setVisible(false);
                    this.form.btnGrabar.setEnabled(true);
                    //this.form.lblCargandoUnMomento.setVisible(false);
                }
            } else {
                this.form.lblCirculoDeProgreso.setVisible(false);
               // this.form.lblCargandoUnMomento.setVisible(false);
                
                JOptionPane.showMessageDialog(this.form, "NO hay registros para actualizar ", "Alerta", JOptionPane.INFORMATION_MESSAGE, null);

            }

        } catch (Exception ex) {
            this.form.lblCirculoDeProgreso.setVisible(false);
           // this.form.lblCargandoUnMomento.setVisible(false);
            Logger.getLogger(FGeoreferenciarClientes.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
