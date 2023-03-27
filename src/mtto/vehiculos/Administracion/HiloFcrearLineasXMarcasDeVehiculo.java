/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.vehiculos.Administracion;

import aplicacionlogistica.configuracion.Inicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloFcrearLineasXMarcasDeVehiculo implements Runnable {

    Inicio ini = null;
    FCrearLineasXMarcaDeVehiculo fCrearLineasXMarcaDeVehiculo = null;
    String caso;
    CLineasPorMarca linea = null;
    int idMarcaVehiculo=0;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearLineasXMarcasDeVehiculo(Inicio ini) {
        this.ini = ini;
        

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fCrearLineasXMarcaDeVehiculo
     * @param comando
     */
    public HiloFcrearLineasXMarcasDeVehiculo(Inicio ini, FCrearLineasXMarcaDeVehiculo fCrearLineasXMarcaDeVehiculo, String comando) {
        this.ini = ini;
        this.fCrearLineasXMarcaDeVehiculo = fCrearLineasXMarcaDeVehiculo;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeMarcasDeVehiculos":
                        listaDeMarcasDeVehiculos();
                        break;

                    case "guardar":
                        if (fCrearLineasXMarcaDeVehiculo.actualizar) {
                            actualizar();
                        } else {
                            guardar();
                        }

                        break;
                    case "llenarJtable":
                       
                        llenarJtable();
                        break;
                    case "seleccionarFila":
                        // fCrearLineasXMarcaDeVehiculo.marcaVehiculo = ini.listaDeMarcasDeVehiculos.get(fCrearLineasXMarcaDeVehiculo.indice);
                        seleccionarFila();
                        break;

                }
            }
          /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */  
          fCrearLineasXMarcaDeVehiculo.iniciado=true;
          
        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearLineasXMarcasDeVehiculo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void guardar() {
        this.fCrearLineasXMarcaDeVehiculo.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroLineaDeVehiculo()) {
            this.fCrearLineasXMarcaDeVehiculo.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fCrearLineasXMarcaDeVehiculo.btnGrabar.setEnabled(false);
        } else {
            this.fCrearLineasXMarcaDeVehiculo.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearLineasXMarcaDeVehiculo, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            this.fCrearLineasXMarcaDeVehiculo.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        this.fCrearLineasXMarcaDeVehiculo.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroLineaDeVehiculo()) {
            this.fCrearLineasXMarcaDeVehiculo.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fCrearLineasXMarcaDeVehiculo.btnGrabar.setEnabled(false);

        } else {
            this.fCrearLineasXMarcaDeVehiculo.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearLineasXMarcaDeVehiculo, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            this.fCrearLineasXMarcaDeVehiculo.btnGrabar.setEnabled(true);
        }

    }

    private void listaDeMarcasDeVehiculos() {
        Object[] fila = new Object[3];
        DefaultTableModel modelo;
        fCrearLineasXMarcaDeVehiculo.cbxMarcaDeVehiculo.removeAllItems();
        ini.setListaDeMarcasDeVehiculos();

        for (CMarcasDeVehiculos marca : ini.getListaDeMarcasDeVehiculos()) {
            fCrearLineasXMarcaDeVehiculo.cbxMarcaDeVehiculo.addItem(marca.getNombreMarcaDeVehiculos());

        }

        fCrearLineasXMarcaDeVehiculo.label2.setVisible(false);
        fCrearLineasXMarcaDeVehiculo.txtLineaVehiculo.setText("");
        fCrearLineasXMarcaDeVehiculo.txtLineaVehiculo.setEnabled(false);

        for (CLineasPorMarca obj : this.ini.getListaDeMarcasDeVehiculos().get(0).getListaDeLineasDelVehiculo()) {
            fila = new Object[3];
            fila[0] = obj.getIdlineasVehiculos();
            fila[1] = obj.getNombreLineaVehiculo();
            if (obj.getActivoLinea() < 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            modelo = (DefaultTableModel) fCrearLineasXMarcaDeVehiculo.tblLineasVehiculos.getModel();
            modelo.addRow(fila);
        }

        fCrearLineasXMarcaDeVehiculo.marcaVehiculo=this.ini.getListaDeMarcasDeVehiculos().get(0);
        fCrearLineasXMarcaDeVehiculo.iniciado = true;
    }

    private void llenarJtable()  {
       fCrearLineasXMarcaDeVehiculo.iniciado=false;
       fCrearLineasXMarcaDeVehiculo.marcaVehiculo=this.ini.getListaDeMarcasDeVehiculos().get(fCrearLineasXMarcaDeVehiculo.indice);

        fCrearLineasXMarcaDeVehiculo.txtLineaVehiculo.setText("");
        fCrearLineasXMarcaDeVehiculo.txtLineaVehiculo.setEnabled(false);
        fCrearLineasXMarcaDeVehiculo.actualizar=false;
        fCrearLineasXMarcaDeVehiculo.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearLineasXMarcaDeVehiculo.btnNuevo.setText("nuevo");
        
       fCrearLineasXMarcaDeVehiculo.limpiarTabla();
        
      // DefaultTableModel modelo = (DefaultTableModel) fCrearLineasXMarcaDeVehiculo.tblLineasVehiculos.getModel();
       
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

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearLineasXMarcaDeVehiculo.tblLineasVehiculos.getModel();

        List<CLineasPorMarca> listas= this.ini.getListaDeMarcasDeVehiculos().get(fCrearLineasXMarcaDeVehiculo.indice).getListaDeLineasDelVehiculo();
        
        for (CLineasPorMarca obj : this.ini.getListaDeMarcasDeVehiculos().get(fCrearLineasXMarcaDeVehiculo.indice).getListaDeLineasDelVehiculo()) {
           try {
               int filaTabla2 = fCrearLineasXMarcaDeVehiculo.tblLineasVehiculos.getRowCount();
               
               modelo2.addRow(new Object[filaTabla2]);
               
               fCrearLineasXMarcaDeVehiculo.tblLineasVehiculos.setValueAt(obj.getIdlineasVehiculos(), filaTabla2, 0);  // item
               fCrearLineasXMarcaDeVehiculo.tblLineasVehiculos.setValueAt(obj.getNombreLineaVehiculo(), filaTabla2, 1); // numero de factura
               if (obj.getActivoLinea() == 1) {
                   fCrearLineasXMarcaDeVehiculo.tblLineasVehiculos.setValueAt(true, filaTabla2, 2); //
               } else {
                   fCrearLineasXMarcaDeVehiculo.tblLineasVehiculos.setValueAt(false, filaTabla2, 2); //
               }
               
               idMarcaVehiculo = obj.getIdMarcaDeVehiculo();
               
               Thread.sleep(1);
           } catch (InterruptedException ex) {
               Logger.getLogger(HiloFcrearLineasXMarcasDeVehiculo.class.getName()).log(Level.SEVERE, null, ex);
           }
        }

        fCrearLineasXMarcaDeVehiculo.label2.setVisible(false);

    }

    public boolean guardarRegistroLineaDeVehiculo() {
        boolean valida = false;
        try {

            linea = new CLineasPorMarca(ini);
            linea.setIdMarcaDeVehiculo(fCrearLineasXMarcaDeVehiculo.marcaVehiculo.getIdMarcaDeVehiculo());
            
            linea.setNombreLineaVehiculo(fCrearLineasXMarcaDeVehiculo.txtLineaVehiculo.getText().trim());
            linea.setNombreMarcaDeVehiculos(fCrearLineasXMarcaDeVehiculo.marcaVehiculo.getNombreMarcaDeVehiculos());
            if (fCrearLineasXMarcaDeVehiculo.chkActivo.isSelected()) {
                linea.setActivoLinea(1);
            } else {
                linea.setActivoLinea(0);
            }
            valida = linea.grabarLineassVehiculos();
            if (valida) {
                //new Thread(new HiloListadoDeMarcasVehiculos(ini)).start();
               ini.setListaDeMarcasDeVehiculos();
                JOptionPane.showInternalMessageDialog(fCrearLineasXMarcaDeVehiculo, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearLineasXMarcaDeVehiculo.btnNuevo.setEnabled(true);
                fCrearLineasXMarcaDeVehiculo.jBtnNuevo.setEnabled(true);
                fCrearLineasXMarcaDeVehiculo.btnGrabar.setEnabled(false);
                fCrearLineasXMarcaDeVehiculo.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearLineasXMarcaDeVehiculo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }
    
private void seleccionarFila(){
        // TODO add your handling code here:

        int row = fCrearLineasXMarcaDeVehiculo.tblLineasVehiculos.getSelectedRow();
        //txtLineaVehiculo.setText(String.valueOf(jTable1.getValueAt(row, 1)));
        
        fCrearLineasXMarcaDeVehiculo.label2.setVisible(true);
        for(CLineasPorMarca linea : ini.getListaDeMarcasDeVehiculos().get(fCrearLineasXMarcaDeVehiculo.indice).getListaDeLineasDelVehiculo()){
            
            if(linea.getNombreLineaVehiculo().equals(String.valueOf(fCrearLineasXMarcaDeVehiculo.tblLineasVehiculos.getValueAt(row, 1)))){
                fCrearLineasXMarcaDeVehiculo.label2.setText("Id. Linea de Vehiculo :  " + linea.getIdlineasVehiculos());
                fCrearLineasXMarcaDeVehiculo.txtLineaVehiculo.setText(linea.getNombreLineaVehiculo());
                if(linea.getActivoLinea()==1){
                    fCrearLineasXMarcaDeVehiculo.chkActivo.setText("LInea de vehiculo Activo");
            fCrearLineasXMarcaDeVehiculo.chkActivo.setSelected(true); 
                }else{
                   fCrearLineasXMarcaDeVehiculo.chkActivo.setText("LInea de vehiculo no Activo");
            fCrearLineasXMarcaDeVehiculo.chkActivo.setSelected(false); 
                }
            }
        }
        
        fCrearLineasXMarcaDeVehiculo.actualizar = true;
        fCrearLineasXMarcaDeVehiculo.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearLineasXMarcaDeVehiculo.btnNuevo.setText("Actualizar");
}
    

    private boolean actualizarRegistroLineaDeVehiculo() {
        boolean valida = false;
        try {
            int row = fCrearLineasXMarcaDeVehiculo.tblLineasVehiculos.getSelectedRow();
            fCrearLineasXMarcaDeVehiculo.label2.setText("Id. Linea vehiculo :  " + String.valueOf(fCrearLineasXMarcaDeVehiculo.tblLineasVehiculos.getValueAt(row, 0)));
            linea = new CLineasPorMarca(ini);
            linea.setIdMarcaDeVehiculo(fCrearLineasXMarcaDeVehiculo.marcaVehiculo.getIdMarcaDeVehiculo());
             linea.setNombreMarcaDeVehiculos(fCrearLineasXMarcaDeVehiculo.marcaVehiculo.getNombreMarcaDeVehiculos());
            linea.setIdlineasVehiculos((Integer) fCrearLineasXMarcaDeVehiculo.tblLineasVehiculos.getValueAt(row, 0));
            linea.setNombreLineaVehiculo(fCrearLineasXMarcaDeVehiculo.txtLineaVehiculo.getText().trim());
            if (fCrearLineasXMarcaDeVehiculo.chkActivo.isSelected()) {
                linea.setActivoLinea(1);
            } else {
                linea.setActivoLinea(0);
            }

            valida = linea.actualizarLineassVehiculos();

            if (valida) {
               //new Thread(new HiloListadoDeMarcasVehiculos(ini)).start();
                ini.setListaDeMarcasDeVehiculos();
                JOptionPane.showInternalMessageDialog(fCrearLineasXMarcaDeVehiculo, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearLineasXMarcaDeVehiculo.label2.setVisible(true);
                fCrearLineasXMarcaDeVehiculo.label2.setText("Id. Linea de Vehiculo :  " + linea.getIdlineasVehiculos());
                fCrearLineasXMarcaDeVehiculo.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearLineasXMarcaDeVehiculo.btnNuevo.setEnabled(true);
                fCrearLineasXMarcaDeVehiculo.jBtnNuevo.setEnabled(true);
                fCrearLineasXMarcaDeVehiculo.btnGrabar.setEnabled(false);
                fCrearLineasXMarcaDeVehiculo.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
             Logger.getLogger(FCrearLineasXMarcaDeVehiculo.class.getName()).log(Level.SEVERE, null, ex);
             
        }
        return valida;
    }

}
