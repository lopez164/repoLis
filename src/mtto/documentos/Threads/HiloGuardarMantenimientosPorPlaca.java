/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.*;
import mtto.Servicios.FAgregarMantenimientoVehiculo;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarMantenimientosPorPlaca implements Runnable {

    //public IngresoAlSistema_original ingSis;
    FAgregarMantenimientoVehiculo fAgregarMantenimientoVehiculo;
    Inicio ini;

    /**
     * Constructor de clase
     *
     * @param fAgregarMantenimientoVehiculo
     */
    public HiloGuardarMantenimientosPorPlaca(FAgregarMantenimientoVehiculo fAgregarMantenimientoVehiculo) {
        this.fAgregarMantenimientoVehiculo = fAgregarMantenimientoVehiculo;
        this.ini = fAgregarMantenimientoVehiculo.ini;

    }

    @Override
    public void run() {
        boolean ok = false;

        try {
   //        this.fAgregarMantenimientoVehiculo.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
//        this.form.habilitar(false);
//        this.form.estaOcupadoGrabando = true;
//        this.form.asignarValoresProveedor();
            this.fAgregarMantenimientoVehiculo.btnGrabarMantenimiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif"))); // NOI18N

            List<String> listaDeSentenciasSQL = new ArrayList();

            if (fAgregarMantenimientoVehiculo.actualizar) {

                actualizar(listaDeSentenciasSQL);

            } else {

                grabarNuevo();
            }

            this.fAgregarMantenimientoVehiculo.estaOcupadoGrabando = false;
            this.fAgregarMantenimientoVehiculo.btnGrabarMantenimiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

        } // fin try
        catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        }
    }

    private void grabarNuevo() {

        if (fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.guardarMantenimientoPorPlaca()) {
            this.fAgregarMantenimientoVehiculo.btnGrabarMantenimiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
            this.fAgregarMantenimientoVehiculo.btnGrabarMantenimiento.setEnabled(false);
            this.fAgregarMantenimientoVehiculo.setEnabled(true);
            this.fAgregarMantenimientoVehiculo.btnGrabarMantenimiento.setEnabled(false);

            JOptionPane.showInternalMessageDialog(fAgregarMantenimientoVehiculo, "La factura hasido guardado con exito", "Registro guardado", JOptionPane.INFORMATION_MESSAGE);

        } else {
            this.fAgregarMantenimientoVehiculo.btnGrabarMantenimiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fAgregarMantenimientoVehiculo, "Error al guardar los datos", "Error al guardar", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void actualizar(List<String> listaDeSentenciasSQL) {

    }


}
