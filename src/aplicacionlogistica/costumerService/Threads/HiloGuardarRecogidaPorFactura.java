/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FIngresarRecogidasClientes;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarRecogidaPorFactura implements Runnable {

    public FIngresarRecogidasClientes fIngresarRecogidaClientes;
    Inicio ini;

    /**
     * Constructor de clase
     *
     * @param form
     */
    public HiloGuardarRecogidaPorFactura(FIngresarRecogidasClientes fIngresarRecogidaClientes) {

        this.fIngresarRecogidaClientes = fIngresarRecogidaClientes;

    }

    @Override
    public void run() {

        try {
            this.fIngresarRecogidaClientes.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
            this.fIngresarRecogidaClientes.habilitar(false);
            this.fIngresarRecogidaClientes.estaOcupadoGrabando = true;
            //this.fIngresarDescuentoClientes.asignarValoresProveedor();

            if (fIngresarRecogidaClientes.recogida.guardarRecogidaPorFactura()) {
                this.fIngresarRecogidaClientes.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fIngresarRecogidaClientes.btnGrabar.setEnabled(false);
                this.fIngresarRecogidaClientes.btnGrabar2.setEnabled(false);
                this.fIngresarRecogidaClientes.btnNuevo.setEnabled(true);
                this.fIngresarRecogidaClientes.btnNuevo2.setEnabled(true);
                
                JOptionPane.showInternalMessageDialog(fIngresarRecogidaClientes, "La Recogida ha sido guardado perfectamente", "Registro guardado", JOptionPane.INFORMATION_MESSAGE);

            } else {
                this.fIngresarRecogidaClientes.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                JOptionPane.showInternalMessageDialog(fIngresarRecogidaClientes, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);

            }

            /*Se trae la sentencia sql para grabar el proveedor y la sucursal principal */
            this.fIngresarRecogidaClientes.estaOcupadoGrabando = false;
            this.fIngresarRecogidaClientes.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

        } catch (Exception ex) {
            this.fIngresarRecogidaClientes.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
            Logger.getLogger(HiloGuardarRecogidaPorFactura.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
