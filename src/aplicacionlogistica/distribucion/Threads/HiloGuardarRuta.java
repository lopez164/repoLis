/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.administracion.logistica.FCrearRutasDeDistribucion;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarCarros;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import mtto.vehiculos.CCarros;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarRuta implements Runnable {

    //public IngresoAlSistema_original ingSis;
    FCrearRutasDeDistribucion form = null;

    CRutasDeDistribucion ruta;
    Inicio ini;
    CUsuarios usuario;

    /**
     * Constructor de clase
     *
     * @param tiempo
     * @param obj
     */
    public HiloGuardarRuta(int tiempo, CRutasDeDistribucion ruta) {

        this.ruta = ruta;

    }

    public HiloGuardarRuta(FCrearRutasDeDistribucion form) {
        this.ini = form.ini;
        this.form = form;

    }

    @Override
    public void run() {
        boolean ok = false;

        try {

            if (form != null) {
                this.form.btnGrabar.setEnabled(false);
                this.form.jBtnGrabar.setEnabled(false);
                this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
                this.form.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif")));
            }
            if (form.ruta.grabarRutasDeDistribucions()) {
                this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.form.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));
               
                new Thread(new HiloListadoDeRutasDeDistribucion(form.ini)).start();
                form.repaint();
                form.habilitar(false);

                JOptionPane.showInternalMessageDialog(form, "El registro del dato ha sido guardado perfectamente", "Registro guardado", 1);
                 this.form.llenarTabla();
            } else {
                
                this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.form.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));
                form.btnGrabar.setEnabled(true);
                form.jBtnGrabar.setEnabled(true);
                
                JOptionPane.showInternalMessageDialog(form, "Se presentó un error al guardar el registro", "Error al guardar", 0);
            }
            Thread.sleep(1);

        } // fin try
        catch (Exception e) {
            System.err.println(e.getMessage());
            
            this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.form.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));
            form.btnGrabar.setEnabled(true);
            form.jBtnGrabar.setEnabled(true);
            
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        }

    }
}
