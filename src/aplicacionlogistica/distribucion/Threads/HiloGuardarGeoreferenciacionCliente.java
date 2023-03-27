/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.distribucion.clientes.FGeoreferenciarClientes;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarGeoreferenciacionCliente implements Runnable {

   // public static boolean band = false;
  //  private int tiempo = 5;
    //public IngresoAlSistema_original ingSis;
    FGeoreferenciarClientes form = null;

  //  CCarros car;
  //  CUsuarios usuario;

  

    public HiloGuardarGeoreferenciacionCliente(FGeoreferenciarClientes form) {

        this.form = form;

    }

    @Override
    public void run() {
        boolean ok = false;

        try {

            if (form != null) {
                this.form.btnGrabar.setEnabled(false);
                this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
            }

            if (form.cliente.actualizarClientes()) {
                this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

                JOptionPane.showMessageDialog(form, "Registro guardado con exitos ", "ok", JOptionPane.INFORMATION_MESSAGE, null);
                form.cancelar();
                 this.form.btnNuevo.requestFocus();
            } else {
                this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                JOptionPane.showMessageDialog(form, "Error al guardar los Datos ", "Alerta", JOptionPane.ERROR_MESSAGE, null);
                 this.form.btnNuevo.requestFocus();
            };

            Thread.sleep(1);

        } // fin try
        catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
                        

        }

    }
}
