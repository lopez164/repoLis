/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FIngresarDescuentoClientes;
import mtto.proveedores.IngresarProveedores;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarDescuentoPorFactura implements Runnable {

    public FIngresarDescuentoClientes fIngresarDescuentoClientes;
    Inicio ini;

    /**
     * Constructor de clase
     *
     * @param form
     */
    public HiloGuardarDescuentoPorFactura(FIngresarDescuentoClientes fIngresarDescuentoClientes) {

        this.fIngresarDescuentoClientes = fIngresarDescuentoClientes;

    }

    @Override
    public void run() {

        try {
            this.fIngresarDescuentoClientes.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
            this.fIngresarDescuentoClientes.habilitar(false);
            this.fIngresarDescuentoClientes.estaOcupadoGrabando = true;
            //this.fIngresarDescuentoClientes.asignarValoresProveedor();

            if (fIngresarDescuentoClientes.descuento.guardarDescuentoPorFactura()) {
                this.fIngresarDescuentoClientes.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fIngresarDescuentoClientes.btnGrabar.setEnabled(false);
                this.fIngresarDescuentoClientes.btnGrabar2.setEnabled(false);
                this.fIngresarDescuentoClientes.btnNuevo.setEnabled(true);
                this.fIngresarDescuentoClientes.btnNuevo2.setEnabled(true);
                
                JOptionPane.showInternalMessageDialog(fIngresarDescuentoClientes, "El Descuento ha sido guardado perfectamente", "Registro guardado", JOptionPane.INFORMATION_MESSAGE);

            } else {
                this.fIngresarDescuentoClientes.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                JOptionPane.showInternalMessageDialog(fIngresarDescuentoClientes, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);

            }

            /*Se trae la sentencia sql para grabar el proveedor y la sucursal principal */
            this.fIngresarDescuentoClientes.estaOcupadoGrabando = false;
            this.fIngresarDescuentoClientes.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

        } catch (Exception ex) {
            this.fIngresarDescuentoClientes.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
            Logger.getLogger(HiloGuardarDescuentoPorFactura.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
