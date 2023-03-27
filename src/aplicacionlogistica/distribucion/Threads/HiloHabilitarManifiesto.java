/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.consultas.FHabilitarManifiesto;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloHabilitarManifiesto implements Runnable {

    public static boolean band = false;
    private final int tiempo = 5;
    Inicio ini;

    FHabilitarManifiesto form;

    /**
     * Constructor de clase
     *
     * @param form
     */
    public HiloHabilitarManifiesto(FHabilitarManifiesto form) {

        this.form = form;

    }

    @Override
    public void run() {
        boolean ok = false;
        this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (this.form.manifiestoActual.habilitarManifiesto()) {

            form.btnGrabar.setEnabled(false);

            form.txtManifiesto.setEditable(false);
            form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

            JOptionPane.showInternalMessageDialog(this.form, "Datos guardados perfectamente en la BBDD ", "Ok", JOptionPane.INFORMATION_MESSAGE);

        } else {
            JOptionPane.showInternalMessageDialog(this.form, "Error al guardar los Datos en la  BBDD ", "Error", JOptionPane.ERROR_MESSAGE);
            form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        }

    }
}
