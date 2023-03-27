/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.vehiculos.Administracion;

import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mtto.documentos.objetos.DocumentosPorTipoDeVehiculo;

/**
 *
 * @author Usuario
 */
public class HiloFcreaDocXTipoDeVehiculo implements Runnable {

    Inicio ini = null;
    FCrearDocXTipoDeVehiculo fCrearDocXTipoDeVehiculo = null;
    String caso;
    CLineasPorMarca linea = null;
    int idMarcaVehiculo=0;
    List<DocumentosPorTipoDeVehiculo> listaDeDocumentos;
            

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcreaDocXTipoDeVehiculo(Inicio ini) {
        this.ini = ini;
        

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fCrearDocXTipoDeVehiculo
     * @param comando
     */
    public HiloFcreaDocXTipoDeVehiculo(Inicio ini, FCrearDocXTipoDeVehiculo fCrearDocXTipoDeVehiculo, String comando) {
        this.ini = ini;
        this.fCrearDocXTipoDeVehiculo = fCrearDocXTipoDeVehiculo;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "cargarFormulario":
                        cargarFormulario();
                        break;

                    case "guardar":
                        if (fCrearDocXTipoDeVehiculo.actualizar) {
                            actualizar();
                        } else {
                            guardar();
                        }

                        break;
                    case "llenarJtable":
                       
                        llenarJtable();
                        break;
                    case "seleccionarFila":
                        // fCrearDocXTipoDeVehiculo.marcaVehiculo = ini.listaDeMarcasDeVehiculos.get(fCrearDocXTipoDeVehiculo.indice);
                        seleccionarFila();
                        break;

                }
            }
          /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */  
          fCrearDocXTipoDeVehiculo.iniciado=true;
          
        } catch (Exception ex) {
            Logger.getLogger(HiloFcreaDocXTipoDeVehiculo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void guardar() {
        this.fCrearDocXTipoDeVehiculo.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroLineaDeVehiculo()) {
            this.fCrearDocXTipoDeVehiculo.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fCrearDocXTipoDeVehiculo.btnGrabar.setEnabled(false);
        } else {
            this.fCrearDocXTipoDeVehiculo.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearDocXTipoDeVehiculo, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            this.fCrearDocXTipoDeVehiculo.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        this.fCrearDocXTipoDeVehiculo.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroLineaDeVehiculo()) {
            this.fCrearDocXTipoDeVehiculo.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fCrearDocXTipoDeVehiculo.btnGrabar.setEnabled(false);

        } else {
            this.fCrearDocXTipoDeVehiculo.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearDocXTipoDeVehiculo, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            this.fCrearDocXTipoDeVehiculo.btnGrabar.setEnabled(true);
        }

    }

    private void cargarFormulario() {
        Object[] fila = new Object[3];
        DefaultTableModel modelo;
        fCrearDocXTipoDeVehiculo.cbxTipoDeVehiculo.removeAllItems();
     
        ini.setListaDeTiposDeVehiculos();
        ini.setListaDeTiposDeDocumentos();

        for (CTiposDeVehiculos tipoVehiculo : ini.getListaDeTiposDeVehiculos()) {
            fCrearDocXTipoDeVehiculo.cbxTipoDeVehiculo.addItem(tipoVehiculo.getNombreTipoDeVehiculo());

        }


        for (CTiposDeDocumentos obj : this.ini.getListaDeTiposDeDocumentos()) {
            fila = new Object[3];
            fila[0] = obj.getIdtiposDocumentos();
            fila[1] = obj.getNombreTipoDocumento();
            if (obj.getActivo() == 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            modelo = (DefaultTableModel) fCrearDocXTipoDeVehiculo.tblLineasVehiculos.getModel();
            modelo.addRow(fila);
        }

       // fCrearDocXTipoDeVehiculo.tipoVehiculo=this.ini.getListaDeMarcasDeVehiculos().get(0);
        fCrearDocXTipoDeVehiculo.iniciado = true;
    }

    private void llenarJtable()  {
       fCrearDocXTipoDeVehiculo.iniciado=false;
     //  fCrearDocXTipoDeVehiculo.tipoVehiculo=this.ini.getListaDeMarcasDeVehiculos().get(fCrearDocXTipoDeVehiculo.indice);

        fCrearDocXTipoDeVehiculo.actualizar=false;
        fCrearDocXTipoDeVehiculo.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearDocXTipoDeVehiculo.btnNuevo.setText("nuevo");
        
       fCrearDocXTipoDeVehiculo.limpiarTabla();
        
       getListaDocXtipoVehiculo();
        
       DefaultTableModel modelo = (DefaultTableModel) fCrearDocXTipoDeVehiculo.tblLineasVehiculos.getModel();
       
        //fCrearDocXTipoDeVehiculo.deleteAllRows(modelo);
        
        
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        
        DefaultTableModel modelo2 = (DefaultTableModel) fCrearDocXTipoDeVehiculo.tblLineasVehiculos.getModel();

        List<CLineasPorMarca> listas= this.ini.getListaDeMarcasDeVehiculos().get(fCrearDocXTipoDeVehiculo.indice).getListaDeLineasDelVehiculo();
        
        for (DocumentosPorTipoDeVehiculo obj : listaDeDocumentos) {
           try {
               int filaTabla2 = fCrearDocXTipoDeVehiculo.tblLineasVehiculos.getRowCount();
               
               modelo2.addRow(new Object[filaTabla2]);
               
               fCrearDocXTipoDeVehiculo.tblLineasVehiculos.setValueAt(obj.getIdTipoDocumento(), filaTabla2, 0);  // item
               fCrearDocXTipoDeVehiculo.tblLineasVehiculos.setValueAt(obj.getNombreTipoDocumento(), filaTabla2, 1); // numero de factura
               if (obj.getActivo()== 1) {
                   fCrearDocXTipoDeVehiculo.tblLineasVehiculos.setValueAt(true, filaTabla2, 2); //
               } else {
                   fCrearDocXTipoDeVehiculo.tblLineasVehiculos.setValueAt(false, filaTabla2, 2); //
               }
               
               //idMarcaVehiculo = obj.getIdMarcaDeVehiculo();
               
               Thread.sleep(1);
           } catch (InterruptedException ex) {
               Logger.getLogger(HiloFcreaDocXTipoDeVehiculo.class.getName()).log(Level.SEVERE, null, ex);
           }
        }


    }

    public boolean guardarRegistroLineaDeVehiculo() {
        boolean valida = false;
        try {

            linea = new CLineasPorMarca(ini);
//            linea.setIdMarcaDeVehiculo(fCrearDocXTipoDeVehiculo.tipoVehiculo.getIdMarcaDeVehiculo());
            
       //     linea.setNombreMarcaDeVehiculos(fCrearDocXTipoDeVehiculo.tipoVehiculo.getNombreMarcaDeVehiculos());
            if (fCrearDocXTipoDeVehiculo.chkActivo.isSelected()) {
                linea.setActivoLinea(1);
            } else {
                linea.setActivoLinea(0);
            }
            valida = linea.grabarLineassVehiculos();
            if (valida) {
                //new Thread(new HiloListadoDeMarcasVehiculos(ini)).start();
               ini.setListaDeMarcasDeVehiculos();
                JOptionPane.showInternalMessageDialog(fCrearDocXTipoDeVehiculo, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearDocXTipoDeVehiculo.btnNuevo.setEnabled(true);
                fCrearDocXTipoDeVehiculo.jBtnNuevo.setEnabled(true);
                fCrearDocXTipoDeVehiculo.btnGrabar.setEnabled(false);
                fCrearDocXTipoDeVehiculo.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearLineasXMarcaDeVehiculo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }
    
private void seleccionarFila(){
        // TODO add your handling code here:

        int row = fCrearDocXTipoDeVehiculo.tblLineasVehiculos.getSelectedRow();
        //txtLineaVehiculo.setText(String.valueOf(jTable1.getValueAt(row, 1)));
        
        for(CLineasPorMarca linea : ini.getListaDeMarcasDeVehiculos().get(fCrearDocXTipoDeVehiculo.indice).getListaDeLineasDelVehiculo()){
            
            if(linea.getNombreLineaVehiculo().equals(String.valueOf(fCrearDocXTipoDeVehiculo.tblLineasVehiculos.getValueAt(row, 1)))){
                if(linea.getActivoLinea()==1){
                    fCrearDocXTipoDeVehiculo.chkActivo.setText("LInea de vehiculo Activo");
            fCrearDocXTipoDeVehiculo.chkActivo.setSelected(true); 
                }else{
                   fCrearDocXTipoDeVehiculo.chkActivo.setText("LInea de vehiculo no Activo");
            fCrearDocXTipoDeVehiculo.chkActivo.setSelected(false); 
                }
            }
        }
        
        fCrearDocXTipoDeVehiculo.actualizar = true;
        fCrearDocXTipoDeVehiculo.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearDocXTipoDeVehiculo.btnNuevo.setText("Actualizar");
}
    

    private boolean actualizarRegistroLineaDeVehiculo() {
        boolean valida = false;
        try {
            int row = fCrearDocXTipoDeVehiculo.tblLineasVehiculos.getSelectedRow();
            linea = new CLineasPorMarca(ini);
//            linea.setIdMarcaDeVehiculo(fCrearDocXTipoDeVehiculo.tipoVehiculo.getIdMarcaDeVehiculo());
 //            linea.setNombreMarcaDeVehiculos(fCrearDocXTipoDeVehiculo.tipoVehiculo.getNombreMarcaDeVehiculos());
            linea.setIdlineasVehiculos((Integer) fCrearDocXTipoDeVehiculo.tblLineasVehiculos.getValueAt(row, 0));
            if (fCrearDocXTipoDeVehiculo.chkActivo.isSelected()) {
                linea.setActivoLinea(1);
            } else {
                linea.setActivoLinea(0);
            }

            valida = linea.actualizarLineassVehiculos();

            if (valida) {
               //new Thread(new HiloListadoDeMarcasVehiculos(ini)).start();
                ini.setListaDeMarcasDeVehiculos();
                JOptionPane.showInternalMessageDialog(fCrearDocXTipoDeVehiculo, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearDocXTipoDeVehiculo.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearDocXTipoDeVehiculo.btnNuevo.setEnabled(true);
                fCrearDocXTipoDeVehiculo.jBtnNuevo.setEnabled(true);
                fCrearDocXTipoDeVehiculo.btnGrabar.setEnabled(false);
                fCrearDocXTipoDeVehiculo.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
             Logger.getLogger(FCrearLineasXMarcaDeVehiculo.class.getName()).log(Level.SEVERE, null, ex);
             
        }
        return valida;
    }
    
    private void getListaDocXtipoVehiculo(){
       try {
            ResultSet rst = null;
            Statement st;
            Connection con;

          
            con = ini.getConnRemota();
         
            String sql = "select dt.idDocumentosPorTipoVehiculo, dt.idTipoDocumento, td.nombreDeDocumento "
                    + "dt.idTipoVehiculo,tv.nombreTipoVehiculo dt.isLogistica, dt.activo, dt.fechaIng, dt.usuario "
                    + "from documentosportipodevehiculo  dt "
                    + "join tiposdedocumentos td on td.idtipoDeDocumento = dt.idTipoDocumento "
                    + "join tiposdevehiculos tv on tv.idTipoVehiculo = dt.idTipoVehiculo "
                    + "where "
                    + " dt.idTipoVehiculo =' " + fCrearDocXTipoDeVehiculo.tipoDeVehiculo.getIdTipoDeVehiculo() + "';";

            CTiposDeDocumentos tiposDeDocumentos = new CTiposDeDocumentos(this.ini);
            
            listaDeDocumentos = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    DocumentosPorTipoDeVehiculo doc = new DocumentosPorTipoDeVehiculo(this.ini);

                    doc.setIdTipoDocumento(rst.getInt("idtiposDocumentos"));
                    doc.setNombreTipoDocumento(rst.getString("nombreTipoDocumento"));
                    doc.setIdTipoVehiculo(rst.getInt("idTipoVehiculo"));
                    doc.setNombreTipoVehiculo(rst.getString("nombreTipoVehiculo"));
                    doc.setActivo(rst.getInt("activo"));
                    
//                    doc.setFormato(rst.getString("formato"));
//                    doc.setTieneVencimiento(rst.getInt("tieneVencimiento"));
//                    doc.setFechaIng(rst.getDate("fechaIng"));
//                    doc.setUsuario(rst.getString("usuario"));
//                    doc.setActivo(rst.getInt("activo"));
//                    doc.setFlag(rst.getInt("flag"));

                    listaDeDocumentos.add(doc);
                    Thread.sleep(1);

                }
                rst.close();
                st.close();
                //con.close();

            }
        } catch (InterruptedException e) {
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
        } catch (Exception ex) {
            Logger.getLogger(HiloFcreaDocXTipoDeVehiculo.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }

}
