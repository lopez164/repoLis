/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.administracion.hilos;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.Vst_empleados;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;

/**
 *
 * @author Usuario
 */
public class HiloConsultarEmpleado implements Runnable {

    Inicio ini;
    IngresarEmpleados fIngresarEmpleados;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param cedula id de la persona en el sistema
     */
    public HiloConsultarEmpleado(Inicio ini, String cedula) {

    }

    public HiloConsultarEmpleado(Inicio ini, JInternalFrame form) {

    }

    public HiloConsultarEmpleado(Inicio ini, IngresarEmpleados form) {
        this.ini = ini;
        this.fIngresarEmpleados = form;

    }

    @Override
    public void run() {

        try {
            consultarEmpleado();
        } catch (Exception ex) {
            Logger.getLogger(HiloConsultarEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void consultarEmpleado() throws Exception {
        this.fIngresarEmpleados.lblCirculoDeProgreso.setVisible(true);
        this.fIngresarEmpleados.txtCedula.setEnabled(false);
        CEmpleados empleado = new CEmpleados(ini);
        try {
            if (!this.fIngresarEmpleados.txtCedula.getText().isEmpty()) {
               
                
                for (CEmpleados obj : ini.getListaDeEmpleados()) {
                    if (obj.getCedula().equals(this.fIngresarEmpleados.txtCedula.getText().trim())) {
                        empleado = obj;
                    }
                }

                if (empleado.getNombres() == null) {

                    //empleado= new CEmpleados(ini);
                    this.fIngresarEmpleados.habilitar(true);

                    this.fIngresarEmpleados.txtNombres.setEnabled(true);
                    this.fIngresarEmpleados.txtNombres.setEditable(true);
                    this.fIngresarEmpleados.btnNuevo.setEnabled(false);
                     this.fIngresarEmpleados.jBtnNuevo.setEnabled(false);
                    
                    this.fIngresarEmpleados.empleado = new CEmpleados(ini);
                    //this.fIngresarEmpleados.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/perfil.jpg"))); // NOI18N
                    this.fIngresarEmpleados.empleado.setFormatoFotografia("nada");
                } else {
                    /*El empleado existe*/
                    this.fIngresarEmpleados.empleado = empleado;
                   // this.fIngresarEmpleados.llenarCamposDeTexto();
                    this.fIngresarEmpleados.habilitar(false);
                    this.fIngresarEmpleados.actualizar = true;
                    this.fIngresarEmpleados.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
                    this.fIngresarEmpleados.jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_32x32.png"))); // NOI18N
                    this.fIngresarEmpleados.btnNuevo.setText("Actualizar");
                }

            }

            this.fIngresarEmpleados.txtNombres.setEnabled(true);
            this.fIngresarEmpleados.txtNombres.setEditable(true);
            this.fIngresarEmpleados.lblCirculoDeProgreso.setVisible(false);
            this.fIngresarEmpleados.lblCirculoDeProgreso.setVisible(false);
            this.fIngresarEmpleados.txtNombres.requestFocus();
            
        } catch (Exception ex) {
            Logger.getLogger(IngresarEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        //this.fIngresarEmpleados.lblCirculoDeProgreso.setVisible(false);

    }

}
