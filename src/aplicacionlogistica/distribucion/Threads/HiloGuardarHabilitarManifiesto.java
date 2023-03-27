/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.distribucion.consultas.FHabilitarManifiesto;
import aplicacionlogistica.distribucion.objetos.CFacturasDescargadas;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductosPorFacturaDescargados;
import aplicacionlogistica.distribucion.objetos.CRecogidasPorManifiesto;
import java.awt.HeadlessException;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarHabilitarManifiesto implements Runnable {

    public static boolean band = false;
    private final int tiempo = 5;
    Inicio ini;

    FHabilitarManifiesto form;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarHabilitarManifiesto(FHabilitarManifiesto form) {

        this.form = form;
        this.ini = this.form.getIni();

    }

    public HiloGuardarHabilitarManifiesto(Inicio ini, JInternalFrame form) {

    }

    @Override
    public void run() {

        this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.form.estaOcupadoGrabando = true;

        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarHabilitarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            this.form.manifiestoActual.habilitarManifiesto();
            

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this.form, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            this.form.btnNuevo.setEnabled(true);

        }

        //
        this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/grabar.png"))); // NOI18N
    }

}
