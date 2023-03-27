/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.Threads;


import aplicacionlogistica.configuracion.Inicio;
import mtto.proveedores.IngresarProveedores;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarSucursal implements Runnable {

    public IngresarProveedores form;
    Inicio ini;
     /**
     * Constructor de clase
     * @param form
      */
    public HiloGuardarSucursal(IngresarProveedores form) {
       
        this.form=form;
        
        
    }

   

    @Override
    public void run() {
        
        this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configurcion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.form.habilitar(false);
        this.form.estaOcupadoGrabando = true;
//        this.form.asignarValoresProveedor();
        
        
       
        List<String> sql = new ArrayList();
//        sql = form.proveedor.getSentenciaInsertSQL_();

        if(form.ini.insertarBBDDRemota(sql,"")){
          
               
            new Thread(new HiloListadoDeProveedores(form.ini)).start();
            JOptionPane.showInternalMessageDialog(form, "El proveedor ha sido guardado perfectamente", "Registro guardado", JOptionPane.INFORMATION_MESSAGE);
            this.form.actualizarFoto = false; 
            this.form.btnGrabar.setEnabled(false);
             
          
        
        } else {
            JOptionPane.showInternalMessageDialog(form, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            this.form.btnGrabar.setEnabled(true);

        }
        
        this.form.estaOcupadoGrabando = false;
        this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configurcion/imagenes/grabar.png"))); // NOI18N


}
    
}
