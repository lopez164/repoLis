/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.Administracion;

import mtto.vehiculos.Administracion.*;
import aplicacionlogistica.configuracion.Inicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mtto.ingresoDeRegistros.objetos.CCuentaSecundariaLogistica;
import mtto.ingresoDeRegistros.objetos.CCuentasPrincipalesLogistica;

/**
 *
 * @author Usuario
 */
public class HiloFcrearCuentasSecundarias implements Runnable {

    Inicio ini = null;
    FCrearCuentasSecundarias fCrearCuentasSecundarias = null;
    String caso;
    CCuentaSecundariaLogistica cuentaSec = null;
    int idCuentaSec=0;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearCuentasSecundarias(Inicio ini) {
        this.ini = ini;
        

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fCrearCuentasSecundarias
     * @param comando
     */
    public HiloFcrearCuentasSecundarias(Inicio ini, FCrearCuentasSecundarias fCrearCuentasSecundarias, String comando) {
        this.ini = ini;
        this.fCrearCuentasSecundarias = fCrearCuentasSecundarias;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeCuentasPrincipales":
                        listaDeCuentasPrincipales();
                        break;

                    case "guardar":
                        if (fCrearCuentasSecundarias.actualizar) {
                            actualizar();
                        } else {
                            guardar();
                        }

                        break;
                    case "llenarJtable":
                       
                        llenarJtable();
                        break;
                    case "seleccionarFila":
                        // fCrearCuentasSecundarias.marcaVehiculo = ini.listaDeMarcasDeVehiculos.get(fCrearCuentasSecundarias.indice);
                        seleccionarFila();
                        break;

                }
            }
          /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */  
          fCrearCuentasSecundarias.iniciado=true;
          
        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearCuentasSecundarias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void guardar() {
        this.fCrearCuentasSecundarias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroCuentaSecundaria()) {
            this.fCrearCuentasSecundarias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fCrearCuentasSecundarias.btnGrabar.setEnabled(false);
        } else {
            this.fCrearCuentasSecundarias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearCuentasSecundarias, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            this.fCrearCuentasSecundarias.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        this.fCrearCuentasSecundarias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroCuentasSecundarias()) {
            this.fCrearCuentasSecundarias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fCrearCuentasSecundarias.btnGrabar.setEnabled(false);

        } else {
            this.fCrearCuentasSecundarias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearCuentasSecundarias, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            this.fCrearCuentasSecundarias.btnGrabar.setEnabled(true);
        }

    }

    private void listaDeCuentasPrincipales() {
        Object[] fila = new Object[4];
        DefaultTableModel modelo;
        fCrearCuentasSecundarias.cbxCuentasPrincipales.removeAllItems();
        ini.setListadeCuentasPrincipales();

        for (CCuentasPrincipalesLogistica ctaPpan : ini.getListadeCuentasPrincipales()) {
            fCrearCuentasSecundarias.cbxCuentasPrincipales.addItem(ctaPpan.getNombreCuentasPpal());

        }

        fCrearCuentasSecundarias.lblIdcuenta.setVisible(true);
        fCrearCuentasSecundarias.txtCuentaSecundaria.setText("");
        fCrearCuentasSecundarias.txtCuentaSecundaria.setEnabled(false);
        fCrearCuentasSecundarias.txtCodigoCuenta.setText("");
        fCrearCuentasSecundarias.txtCodigoCuenta.setEnabled(false);

        for (CCuentaSecundariaLogistica obj : this.ini.getListadeCuentasPrincipales().get(0).getListaDeCuentasSecundarias()) {
            fila = new Object[4];
            fila[0] = obj.getIdCuentaSecundaria();
            fila[1] = obj.getNombreCuentaSecundaria();
            fila[2] = obj.getCodigoCuentaSecundaria();
            if (obj.getActivo()< 1) {
                fila[3] = false;
            } else {
                fila[3] = true;
            }
            modelo = (DefaultTableModel) fCrearCuentasSecundarias.tblCuentasSecundarias.getModel();
            modelo.addRow(fila);
        }
 
        fCrearCuentasSecundarias.cuentaPPal=this.ini.getListadeCuentasPrincipales().get(0);
        fCrearCuentasSecundarias.iniciado = true;
    }

    private void llenarJtable()  {
       fCrearCuentasSecundarias.iniciado=false;

       fCrearCuentasSecundarias.cuentaPPal=this.ini.getListadeCuentasPrincipales().get(fCrearCuentasSecundarias.indice);
        fCrearCuentasSecundarias.txtCuentaSecundaria.setText("");
        fCrearCuentasSecundarias.txtCuentaSecundaria.setEnabled(false);
        fCrearCuentasSecundarias.txtCodigoCuenta.setText("");
        fCrearCuentasSecundarias.txtCodigoCuenta.setEnabled(false);
        fCrearCuentasSecundarias.actualizar=false;
        fCrearCuentasSecundarias.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearCuentasSecundarias.btnNuevo.setText("nuevo");
        
       fCrearCuentasSecundarias.limpiarTabla();
        
      // DefaultTableModel modelo = (DefaultTableModel) fCrearCuentasSecundarias.tblLineasVehiculos.getModel();
       
        //fCrearLineasXMarcaDeVehiculo.deleteAllRows(modelo);
        
//        
//        if (modelo.getRowCount() > 0) {
//            int a = modelo.getRowCount() - 1;
//            for (int i = a; i >= 0; i--) {
//                modelo.removeRow(i);
//            }
//        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearCuentasSecundarias.tblCuentasSecundarias.getModel();

        List<CCuentaSecundariaLogistica> listas= this.ini.getListadeCuentasPrincipales().get(fCrearCuentasSecundarias.indice).getListaDeCuentasSecundarias();
        
        for (CCuentaSecundariaLogistica obj : this.ini.getListadeCuentasPrincipales().get(fCrearCuentasSecundarias.indice).getListaDeCuentasSecundarias()) {
           try {
               int filaTabla2 = fCrearCuentasSecundarias.tblCuentasSecundarias.getRowCount();
               
               modelo2.addRow(new Object[filaTabla2]);
               
               fCrearCuentasSecundarias.tblCuentasSecundarias.setValueAt(obj.getIdCuentaSecundaria(), filaTabla2, 0);  // item
               fCrearCuentasSecundarias.tblCuentasSecundarias.setValueAt(obj.getNombreCuentaSecundaria(), filaTabla2, 1); // numero de factura
               fCrearCuentasSecundarias.tblCuentasSecundarias.setValueAt(obj.getCodigoCuentaSecundaria(), filaTabla2, 2); // numero de factura
               if (obj.getActivo()== 1) {
                   fCrearCuentasSecundarias.tblCuentasSecundarias.setValueAt(true, filaTabla2, 3); //
               } else {
                   fCrearCuentasSecundarias.tblCuentasSecundarias.setValueAt(false, filaTabla2, 3); //
               }
               
               idCuentaSec = obj.getIdCuentaSecundaria();
               
               Thread.sleep(1);
           } catch (InterruptedException ex) {
               Logger.getLogger(HiloFcrearCuentasSecundarias.class.getName()).log(Level.SEVERE, null, ex);
           }
        }

        fCrearCuentasSecundarias.lblIdcuenta.setVisible(true);

    }

    public boolean guardarRegistroCuentaSecundaria() {
        boolean valida = false;
        try {

            cuentaSec = new CCuentaSecundariaLogistica(ini);
       
            cuentaSec.setIdCuentaPrincipal(fCrearCuentasSecundarias.cuentaPPal.getIdCuentasPpal());
            
            cuentaSec.setNombreCuentaSecundaria(fCrearCuentasSecundarias.txtCuentaSecundaria.getText().trim());
            cuentaSec.setCodigoCuentaSecundaria(fCrearCuentasSecundarias.txtCodigoCuenta.getText().trim());
            
            cuentaSec.setNombreCuentaPrincipal(fCrearCuentasSecundarias.cuentaPPal.getNombreCuentasPpal());
            if (fCrearCuentasSecundarias.chkActivo.isSelected()) {
                cuentaSec.setActivo(1);
            } else {
                cuentaSec.setActivo(0);
            }
            valida = cuentaSec.grabarCuentaSecundaria();
            
            if (valida) {
                //new Thread(new HiloListadoDeMarcasVehiculos(ini)).start();
               ini.setListadeCuentasPrincipales();
                JOptionPane.showInternalMessageDialog(fCrearCuentasSecundarias, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearCuentasSecundarias.btnNuevo.setEnabled(true);
                fCrearCuentasSecundarias.jBtnNuevo.setEnabled(true);
                fCrearCuentasSecundarias.btnGrabar.setEnabled(false);
                fCrearCuentasSecundarias.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCuentasSecundarias.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }
    
private void seleccionarFila(){
        // TODO add your handling code here:

        int row = fCrearCuentasSecundarias.tblCuentasSecundarias.getSelectedRow();
        //txtLineaVehiculo.setText(String.valueOf(jTable1.getValueAt(row, 1)));
        
        fCrearCuentasSecundarias.lblIdcuenta.setVisible(true);
        for(CCuentaSecundariaLogistica ctaSec : ini.getListadeCuentasPrincipales().get(fCrearCuentasSecundarias.indice).getListaDeCuentasSecundarias()){
            
            if(ctaSec.getNombreCuentaSecundaria().equals(String.valueOf(fCrearCuentasSecundarias.tblCuentasSecundarias.getValueAt(row, 1)))){
                fCrearCuentasSecundarias.lblIdcuenta.setText("Id. Cuenta Secundaria :  " + ctaSec.getIdCuentaSecundaria());
                fCrearCuentasSecundarias.txtCuentaSecundaria.setText(ctaSec.getNombreCuentaSecundaria());
                 fCrearCuentasSecundarias.txtCodigoCuenta.setText(ctaSec.getCodigoCuentaSecundaria());
                if(ctaSec.getActivo()==1){
                    fCrearCuentasSecundarias.chkActivo.setText("Cuenta Secundaria Activo");
            fCrearCuentasSecundarias.chkActivo.setSelected(true); 
                }else{
                   fCrearCuentasSecundarias.chkActivo.setText("Cuenta Secundaria no Activo");
            fCrearCuentasSecundarias.chkActivo.setSelected(false); 
                }
            }
        }
        
        fCrearCuentasSecundarias.actualizar = true;
        fCrearCuentasSecundarias.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearCuentasSecundarias.btnNuevo.setText("Actualizar");
}
    

    private boolean actualizarRegistroCuentasSecundarias() {
        boolean valida = false;
        try {
            int row = fCrearCuentasSecundarias.tblCuentasSecundarias.getSelectedRow();
            fCrearCuentasSecundarias.lblIdcuenta.setText("Id. Cuenta Secundaria :  " + String.valueOf(fCrearCuentasSecundarias.tblCuentasSecundarias.getValueAt(row, 0)));
            cuentaSec = new CCuentaSecundariaLogistica(ini);
            cuentaSec.setIdCuentaPrincipal(fCrearCuentasSecundarias.cuentaPPal.getIdCuentasPpal());
            cuentaSec.setNombreCuentaPrincipal(fCrearCuentasSecundarias.cuentaPPal.getNombreCuentasPpal());
            cuentaSec.setIdCuentaSecundaria((Integer) fCrearCuentasSecundarias.tblCuentasSecundarias.getValueAt(row, 0));
            cuentaSec.setNombreCuentaSecundaria(fCrearCuentasSecundarias.txtCuentaSecundaria.getText().trim());
            cuentaSec.setCodigoCuentaSecundaria(fCrearCuentasSecundarias.txtCodigoCuenta.getText().trim());
            
           
            if (fCrearCuentasSecundarias.chkActivo.isSelected()) {
                cuentaSec.setActivo(1);
            } else {
                cuentaSec.setActivo(0);
            }

            valida = cuentaSec.actualizarCuentaSecundaria();

            if (valida) {
               //new Thread(new HiloListadoDeMarcasVehiculos(ini)).start();
                ini.setListadeCuentasPrincipales();
                JOptionPane.showInternalMessageDialog(fCrearCuentasSecundarias, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearCuentasSecundarias.lblIdcuenta.setVisible(true);
                fCrearCuentasSecundarias.lblIdcuenta.setText("Id. Cuenta Secundaria :  " + cuentaSec.getIdCuentaSecundaria());
                fCrearCuentasSecundarias.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearCuentasSecundarias.btnNuevo.setEnabled(true);
                fCrearCuentasSecundarias.jBtnNuevo.setEnabled(true);
                fCrearCuentasSecundarias.btnGrabar.setEnabled(false);
                fCrearCuentasSecundarias.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
             Logger.getLogger(FCrearCuentasSecundarias.class.getName()).log(Level.SEVERE, null, ex);
             
        }
        return valida;
    }

}
