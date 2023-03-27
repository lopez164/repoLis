/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;


import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarEmpleados;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarEmpleado implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    public IngresarEmpleados form;
    Inicio ini;
     /**
     * Constructor de clase
     * @param ini
     * @param form
      */
    public HiloGuardarEmpleado(IngresarEmpleados form) {
       
        this.form=form;
        
        
    }

   

    @Override
    public void run() {
        
        this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.form.habilitar(false);
        this.form.estaOcupadoGrabando = true;
        //this.form.asignarValoresEmpleado();
        
        
       
        ArrayList<String> sql = new ArrayList();
        sql = form.empleado.getSentenciaInsertSQL_();

        if(form.ini.insertarBBDDRemota(sql,"empleados")){
           if(form.empleado.insertarFofografia()){
               
            new Thread(new HiloListadoDeEmpleados(form.ini)).start();
            JOptionPane.showInternalMessageDialog(form, "El registro del empleado ha sido guardado perfectamente", "Registro guardado", 1);
            this.form.actualizarFoto = false; 
            this.form.btnGrabar.setEnabled(false);
           
             
           }
        
        } else {
            JOptionPane.showInternalMessageDialog(form, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            this.form.btnGrabar.setEnabled(true);

        }
        
        this.form.estaOcupadoGrabando = false;
        this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N


}
    
}
