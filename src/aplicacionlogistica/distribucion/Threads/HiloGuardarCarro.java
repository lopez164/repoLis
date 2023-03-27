/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.distribucion.formularios.administracion.IngresarCarros;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import mtto.vehiculos.CCarros;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarCarro implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    //public IngresoAlSistema_original ingSis;
    IngresarCarros form = null;

    CCarros car;
    CUsuarios usuario;

    /**
     * Constructor de clase
     *
     * @param tiempo
     * @param obj
     */
    public HiloGuardarCarro(int tiempo, CCarros obj) {
        this.tiempo = tiempo;
        this.car = obj;

    }

    public HiloGuardarCarro(IngresarCarros form) {

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
            if (form.carro.grabarCarros()) {
                this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                new Thread(new HiloListadoDeVehiculos(form.ini)).start();
                form.repaint();
                JOptionPane.showInternalMessageDialog(form, "El registro del vehiculo ha sido guardado perfectamente", "Registro guardado", 1);
                form.actualizarFoto = false;
                form.habilitar(false);
            } else {
                JOptionPane.showInternalMessageDialog(form, "Se presentó un error al guardar el registro", "Error al guardar", 0);

                form.btnGrabar.setEnabled(true);
            }
            Thread.sleep(1);

        } // fin try
        catch (Exception e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        }

    }
}
