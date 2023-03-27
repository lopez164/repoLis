/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarUsuarios;
import java.awt.HeadlessException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarUsuario implements Runnable {
    
    public static boolean band = false;
    private final int tiempo = 5;
    Inicio ini;
    List<String> sentenciasSQL;
    
    IngresarUsuarios form;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarUsuario(Inicio ini, IngresarUsuarios form) {
        
        this.form = form;
        this.ini = ini;
        
    }
    
    @Override
    public void run() {
        this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.form.btnGrabar.setEnabled(false);
        this.form.estaOcupadoGrabando = true;
        try {
            if (form.guardarRegistroNuevoUsuario()) {
                new Thread(new HiloListadoDeUsuarios(ini)).start(); //ok
                JOptionPane.showInternalMessageDialog(this.form, "El registro del empleado ha sido guardado perfectamente", "Registro guardado", JOptionPane.INFORMATION_MESSAGE);
                form.habilitar(false);
            } else {
                this.form.btnGrabar.setEnabled(true);
                this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                JOptionPane.showInternalMessageDialog(this.form, "Se presentó un error al guardar el registro", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException | HeadlessException ex) {
             this.form.btnGrabar.setEnabled(true);
            this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showMessageDialog(this.form, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            Logger.getLogger(HiloGuardarUsuario.class.getName()).log(Level.SEVERE, null, ex);
            
        } catch (Exception ex) {
            this.form.btnGrabar.setEnabled(true);
            this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showMessageDialog(this.form, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);            
            Logger.getLogger(HiloGuardarUsuario.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        this.form.estaOcupadoGrabando = false;
        this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

    }
}
