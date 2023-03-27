/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import mtto.vehiculos.Administracion.FCrearMarcasVehiculos;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarLineaDeVehiculo implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    public FCrearMarcasVehiculos fCrearMarcasDeVehiculos;
    Inicio ini;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarLineaDeVehiculo(FCrearMarcasVehiculos form, Inicio ini) {
        this.ini = ini;
        this.fCrearMarcasDeVehiculos = form;

    }

    @Override
    public void run() {

        this.fCrearMarcasDeVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
       
        if (fCrearMarcasDeVehiculos.actualizar) {
            if (fCrearMarcasDeVehiculos.actualizarRegistroMarcasDeVehiculo()) {
                this.fCrearMarcasDeVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fCrearMarcasDeVehiculos.btnGrabar.setEnabled(false);

            } else {
                this.fCrearMarcasDeVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                JOptionPane.showInternalMessageDialog(fCrearMarcasDeVehiculos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
                this.fCrearMarcasDeVehiculos.btnGrabar.setEnabled(true);
            }
        } else {
            if (fCrearMarcasDeVehiculos.guardarRegistroMarcaDeVehiculo()) {
                this.fCrearMarcasDeVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fCrearMarcasDeVehiculos.btnGrabar.setEnabled(false);
            } else {
                this.fCrearMarcasDeVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                JOptionPane.showInternalMessageDialog(fCrearMarcasDeVehiculos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
                this.fCrearMarcasDeVehiculos.btnGrabar.setEnabled(true);
            }
        }

            }

}
