/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.vehiculos.Administracion;

import aplicacionlogistica.distribucion.administracion.logistica.*;
import aplicacionlogistica.configuracion.Inicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloFcrearTiposDeDocumentos implements Runnable {

    Inicio ini = null;
    FCrearTiposdeDocumentos fCrearTiposdeDocumentos = null;
    String caso;
    CTiposDeDocumentos tipoDocumento = null;
    int idTipoDocumento = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearTiposDeDocumentos(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param FCrearTiposdeDocumentos
     * @param comando
     */
    public HiloFcrearTiposDeDocumentos(Inicio ini,  FCrearTiposdeDocumentos FCrearTiposdeDocumentos, String comando) {
        this.ini = ini;
        this.fCrearTiposdeDocumentos = FCrearTiposdeDocumentos;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeTiposDeDocumentos":
                        listaDeTiposDeDocumentos();
                        break;

                    case "guardar":
                        if (fCrearTiposdeDocumentos.actualizar) {
                            actualizar();
                        } else {
                            guardar();
                        }

                        break;
                    case "llenarJtable":
                        llenarJtable();
                        break;
                    case "seleccionarFila":
                        seleccionarFila();
                        break;

                    default:
                        JOptionPane.showInternalMessageDialog(fCrearTiposdeDocumentos, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearTiposdeDocumentos.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearTiposDeDocumentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeTiposDeDocumentos() {
        ini.setListaDeTiposDeDocumentos();

        fCrearTiposdeDocumentos.txtTipoDocumento.setEnabled(false);
        
        for (CTiposDeDocumentos obj : ini.getListaDeTiposDeDocumentos()) {
            fila = new Object[5];
            fila[0] = obj.getIdtiposDocumentos();
            fila[1] = obj.getNombreTipoDocumento();
            fila[2] = obj.getFormato();
             if (obj.getTieneVencimiento()< 1) {
                fila[3] = false;
            } else {
                fila[3] = true;
            }
            
            if (obj.getActivo()< 1) {
                fila[4] = false;
            } else {
                fila[4] = true;
            }
            
             //tiposDeDocumentos.setIdtiposDocumentos(rst.getInt("idtiposDocumentos"));
             //tiposDeDocumentos.setNombreTipoDocumento(rst.getString("nombreTipoDocumento"));
             //tiposDeDocumentos.setFormato(rst.getString("formato"));
             //tiposDeDocumentos.setTieneVencimiento(rst.getInt("tieneVencimiento"));
                    //tiposDeDocumentos.setFechaIng(rst.getDate("fechaIng"));
                    //tiposDeDocumentos.setUsuario(rst.getString("usuario"));
                    //tiposDeDocumentos.setActivo(rst.getInt("activo"));
                    //tiposDeDocumentos.setFlag(rst.getInt("flag"));
            
            
            
            
            
            
            
            
            
            
            
            modelo = (DefaultTableModel) fCrearTiposdeDocumentos.tblTipoDocumento.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearTiposdeDocumentos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroTipoDocumento()) {
            fCrearTiposdeDocumentos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearTiposdeDocumentos.btnGrabar.setEnabled(false);
        } else {
            fCrearTiposdeDocumentos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearTiposdeDocumentos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearTiposdeDocumentos.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearTiposdeDocumentos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroTipoDocumento()) {
            fCrearTiposdeDocumentos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearTiposdeDocumentos.btnGrabar.setEnabled(false);

        } else {
            fCrearTiposdeDocumentos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearTiposdeDocumentos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearTiposdeDocumentos.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearTiposdeDocumentos.iniciado = false;
        fCrearTiposdeDocumentos.tiposDeDocumentos = ini.getListaDeTiposDeDocumentos().get(fCrearTiposdeDocumentos.indice);

        fCrearTiposdeDocumentos.txtTipoDocumento.setText("");
        fCrearTiposdeDocumentos.txtTipoDocumento.setEnabled(false);
        fCrearTiposdeDocumentos.actualizar = false;
        fCrearTiposdeDocumentos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearTiposdeDocumentos.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearTiposdeDocumentos.tblTipoDocumento.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearTiposdeDocumentos.tblTipoDocumento.getModel();

        for (CTiposDeDocumentos obj : ini.getListaDeTiposDeDocumentos()) {
            try {
                int filaTabla2 = fCrearTiposdeDocumentos.tblTipoDocumento.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearTiposdeDocumentos.tblTipoDocumento.setValueAt(obj.getIdtiposDocumentos(), filaTabla2, 0);  // item
                fCrearTiposdeDocumentos.tblTipoDocumento.setValueAt(obj.getNombreTipoDocumento(), filaTabla2, 1); // numero de factura
                fCrearTiposdeDocumentos.tblTipoDocumento.setValueAt(obj.getFormato(), filaTabla2, 2); // numero de factura
                
                if (obj.getTieneVencimiento()== 1) {
                    fCrearTiposdeDocumentos.tblTipoDocumento.setValueAt(true, filaTabla2, 3); //
                } else {
                    fCrearTiposdeDocumentos.tblTipoDocumento.setValueAt(false, filaTabla2, 3); //
                }
                if (obj.getActivo()== 1) {
                    fCrearTiposdeDocumentos.tblTipoDocumento.setValueAt(true, filaTabla2, 4); //
                } else {
                    fCrearTiposdeDocumentos.tblTipoDocumento.setValueAt(false, filaTabla2, 4); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearTiposDeDocumentos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroTipoDocumento() {
        boolean valida = false;
        try {

            tipoDocumento = new CTiposDeDocumentos(ini);
            
            tipoDocumento.setNombreTipoDocumento(fCrearTiposdeDocumentos.txtTipoDocumento.getText().trim());
            //canal.setIdCanalDeVenta(fCrearTiposdeDocumentos.causalDeDevolucion.getIdCanalDeVenta());
            tipoDocumento.setFormato(fCrearTiposdeDocumentos.txtFormato.getText().trim());
            
            if(fCrearTiposdeDocumentos.cbxvencimiento.getSelectedItem().equals("SI")){
                tipoDocumento.setTieneVencimiento(1);
            }
             if(fCrearTiposdeDocumentos.cbxvencimiento.getSelectedItem().equals("NO")){
                tipoDocumento.setTieneVencimiento(0);
            }
            if (fCrearTiposdeDocumentos.chkActivo.isSelected()) {
                tipoDocumento.setActivo(1);
            } else {
                tipoDocumento.setActivo(0);
            }
            
            valida = tipoDocumento.grabarTipoDeDocumento();
            
            if (valida) {

                ini.setListaDeTiposDeDocumentos();
                JOptionPane.showInternalMessageDialog(fCrearTiposdeDocumentos, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearTiposdeDocumentos.btnNuevo.setEnabled(true);
                fCrearTiposdeDocumentos.jBtnNuevo.setEnabled(true);
                fCrearTiposdeDocumentos.btnGrabar.setEnabled(false);
                fCrearTiposdeDocumentos.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearTiposdeDocumentos.tblTipoDocumento.getSelectedRow();
        fCrearTiposdeDocumentos.lblIdTipoDocumento.setText("Id. Tipo Documento :  " + String.valueOf(fCrearTiposdeDocumentos.tblTipoDocumento.getValueAt(row, 0)));
         fCrearTiposdeDocumentos.lblIdTipoDocumento.setVisible(true);
        
        fCrearTiposdeDocumentos.txtTipoDocumento.setText(String.valueOf(fCrearTiposdeDocumentos.tblTipoDocumento.getValueAt(row, 1)));
        fCrearTiposdeDocumentos.txtFormato.setText(String.valueOf(fCrearTiposdeDocumentos.tblTipoDocumento.getValueAt(row, 2)));
       
        if ((Boolean) fCrearTiposdeDocumentos.tblTipoDocumento.getModel().getValueAt(row, 3)) {
            fCrearTiposdeDocumentos.cbxvencimiento.setSelectedIndex(0);  
            fCrearTiposdeDocumentos.cbxvencimiento.setEnabled(false);
        } else {
             fCrearTiposdeDocumentos.cbxvencimiento.setSelectedIndex(1);  
             fCrearTiposdeDocumentos.cbxvencimiento.setEnabled(false);
        }
        
        if ((Boolean) fCrearTiposdeDocumentos.tblTipoDocumento.getModel().getValueAt(row, 4)) {
            fCrearTiposdeDocumentos.chkActivo.setText("Tipo Documento  Activo");
            fCrearTiposdeDocumentos.chkActivo.setSelected(true);
        } else {
            fCrearTiposdeDocumentos.chkActivo.setText("Tipo Documento  no Activo");
            fCrearTiposdeDocumentos.chkActivo.setSelected(false);
        }
        
        fCrearTiposdeDocumentos.actualizar = true;
        fCrearTiposdeDocumentos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearTiposdeDocumentos.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroTipoDocumento() {
        boolean valida = false;
        try {
            int row = fCrearTiposdeDocumentos.tblTipoDocumento.getSelectedRow();
            fCrearTiposdeDocumentos.lblIdTipoDocumento.setText("Id. Tipo Documento :  " + String.valueOf(fCrearTiposdeDocumentos.tblTipoDocumento.getValueAt(row, 0)));
            tipoDocumento = new CTiposDeDocumentos(ini);          
            tipoDocumento.setIdtiposDocumentos((Integer) fCrearTiposdeDocumentos.tblTipoDocumento.getValueAt(row, 0));
            
            tipoDocumento.setNombreTipoDocumento(fCrearTiposdeDocumentos.txtTipoDocumento.getText().trim());
            tipoDocumento.setFormato(fCrearTiposdeDocumentos.txtFormato.getText().trim());
            if(fCrearTiposdeDocumentos.cbxvencimiento.getSelectedItem().equals("SI")){
                tipoDocumento.setTieneVencimiento(1);
            }
             if(fCrearTiposdeDocumentos.cbxvencimiento.getSelectedItem().equals("NO")){
                tipoDocumento.setTieneVencimiento(0);
            }
            if (fCrearTiposdeDocumentos.chkActivo.isSelected()) {
                tipoDocumento.setActivo(1);
            } else {
                tipoDocumento.setActivo(0);
            }

            valida = tipoDocumento.actualizarTipoDeDocumento();

            if (valida) {
                ini.setListaDeTiposDeDocumentos();
                JOptionPane.showInternalMessageDialog(fCrearTiposdeDocumentos, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearTiposdeDocumentos.lblIdTipoDocumento.setVisible(true);
                fCrearTiposdeDocumentos.lblIdTipoDocumento.setText("Id. Tipo Documento :  " + tipoDocumento.getIdtiposDocumentos());
                fCrearTiposdeDocumentos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearTiposdeDocumentos.btnNuevo.setEnabled(true);
                fCrearTiposdeDocumentos.jBtnNuevo.setEnabled(true);
                fCrearTiposdeDocumentos.btnGrabar.setEnabled(false);
                fCrearTiposdeDocumentos.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
