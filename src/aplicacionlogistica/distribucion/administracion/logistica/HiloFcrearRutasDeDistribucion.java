/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.administracion.logistica;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloFcrearRutasDeDistribucion implements Runnable {

    Inicio ini = null;
   FCrearRutasDeDistribucion fCrearRutasDeDistribucion = null;
    String caso;
    CRutasDeDistribucion ruta = null;
    int idCanalDeVenta = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearRutasDeDistribucion(Inicio ini) {
        ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fCrearRutasDeDistribucion
     * @param comando
     */
    public HiloFcrearRutasDeDistribucion(Inicio ini, FCrearRutasDeDistribucion fCrearRutasDeDistribucion, String comando) {
        this.ini = ini;
        this.fCrearRutasDeDistribucion = fCrearRutasDeDistribucion;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeRutas":
                        listaDeRutas();
                        break;

                    case "guardar":
                        if (fCrearRutasDeDistribucion.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearRutasDeDistribucion, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearRutasDeDistribucion.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearRutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeRutas() {
        ini.setListaDeRutasDeDistribucion();

        fCrearRutasDeDistribucion.txtNombreRuta.setEnabled(false);

        for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
            fila = new Object[4];
            fila[0] = obj.getIdRutasDeDistribucion();
            fila[1] = obj.getNombreRutasDeDistribucion();
            if(obj.getTipoRuta().equals("F")){
                fila[2]="FORANEA";
            }
            if(obj.getTipoRuta().equals("U")){
                fila[2]="URBANA";
            }
            
            if (obj.getActivoRutasDeDistribucion()< 1) {
                fila[3] = false;
            } else {
                fila[3] = true;
            }
            modelo = (DefaultTableModel) fCrearRutasDeDistribucion.tblRutasDeDistribucion.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearRutasDeDistribucion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroRuta()) {
            fCrearRutasDeDistribucion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearRutasDeDistribucion.btnGrabar.setEnabled(false);
        } else {
            fCrearRutasDeDistribucion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearRutasDeDistribucion, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearRutasDeDistribucion.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearRutasDeDistribucion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroCanal()) {
            fCrearRutasDeDistribucion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearRutasDeDistribucion.btnGrabar.setEnabled(false);

        } else {
            fCrearRutasDeDistribucion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearRutasDeDistribucion, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearRutasDeDistribucion.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearRutasDeDistribucion.iniciado = false;
        fCrearRutasDeDistribucion.ruta = ini.getListaDeRutasDeDistribucion().get(fCrearRutasDeDistribucion.indice);

        fCrearRutasDeDistribucion.txtNombreRuta.setText("");
        fCrearRutasDeDistribucion.txtNombreRuta.setEnabled(false);
        fCrearRutasDeDistribucion.actualizar = false;
        fCrearRutasDeDistribucion.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearRutasDeDistribucion.btnNuevo.setText("nuevo");

        //fCrearRutasDeDistribucion.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearRutasDeDistribucion.tblRutasDeDistribucion.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearRutasDeDistribucion.tblRutasDeDistribucion.getModel();

        for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
            try {
                int filaTabla2 = fCrearRutasDeDistribucion.tblRutasDeDistribucion.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearRutasDeDistribucion.tblRutasDeDistribucion.setValueAt(obj.getIdRutasDeDistribucion(), filaTabla2, 0);  // item
                fCrearRutasDeDistribucion.tblRutasDeDistribucion.setValueAt(obj.getNombreRutasDeDistribucion(), filaTabla2, 1); // numero de factura
               if(obj.getTipoRuta().equals("F")){
                   fCrearRutasDeDistribucion.tblRutasDeDistribucion.setValueAt("FORANEA", filaTabla2, 2);
               }
               if(obj.getTipoRuta().equals("U")){
                   fCrearRutasDeDistribucion.tblRutasDeDistribucion.setValueAt("URBANO", filaTabla2, 2);
               }
                if (obj.getActivoRutasDeDistribucion()== 1) {
                    fCrearRutasDeDistribucion.tblRutasDeDistribucion.setValueAt(true, filaTabla2, 3); //
                } else {
                    fCrearRutasDeDistribucion.tblRutasDeDistribucion.setValueAt(false, filaTabla2, 3); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearRutasDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroRuta() {
        boolean valida = false;
        try {

            ruta = new CRutasDeDistribucion(ini);
            ruta.setNombreRutasDeDistribucion(fCrearRutasDeDistribucion.txtNombreRuta.getText().trim());
            ruta.setTipoRuta(fCrearRutasDeDistribucion.cbxTipoRuta.getSelectedItem().toString().substring(0, 1));
            if (fCrearRutasDeDistribucion.chkActivo.isSelected()) {
                ruta.setActivoRutasDeDistribucion(1);
            } else {
                ruta.setActivoRutasDeDistribucion(0);
            }
            
            valida = ruta.grabarRutasDeDistribucions();
            if (valida) {

                ini.setListaDeRutasDeDistribucion();
                JOptionPane.showInternalMessageDialog(fCrearRutasDeDistribucion, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearRutasDeDistribucion.btnNuevo.setEnabled(true);
                fCrearRutasDeDistribucion.jBtnNuevo.setEnabled(true);
                fCrearRutasDeDistribucion.btnGrabar.setEnabled(false);
                fCrearRutasDeDistribucion.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCanalDeVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearRutasDeDistribucion.tblRutasDeDistribucion.getSelectedRow();
        fCrearRutasDeDistribucion.lblRutaDeDistribucion.setText("Id. Ruta de Dist. : " + String.valueOf(fCrearRutasDeDistribucion.tblRutasDeDistribucion.getValueAt(row, 0)));
        
        fCrearRutasDeDistribucion.txtNombreRuta.setText(String.valueOf(fCrearRutasDeDistribucion.tblRutasDeDistribucion.getValueAt(row, 1)));
        
        if ((Boolean) fCrearRutasDeDistribucion.tblRutasDeDistribucion.getModel().getValueAt(row, 3)) {
            fCrearRutasDeDistribucion.chkActivo.setText("Canal Activo");
            fCrearRutasDeDistribucion.chkActivo.setSelected(true);
        } else {
            fCrearRutasDeDistribucion.chkActivo.setText("Canal no Activo");
            fCrearRutasDeDistribucion.chkActivo.setSelected(false);
        }
        fCrearRutasDeDistribucion.actualizar = true;
        fCrearRutasDeDistribucion.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearRutasDeDistribucion.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroCanal() {
        boolean valida = false;
        try {
            int row = fCrearRutasDeDistribucion.tblRutasDeDistribucion.getSelectedRow();
            fCrearRutasDeDistribucion.lblRutaDeDistribucion.setText("Id. Ruta de Dist. : " + String.valueOf(fCrearRutasDeDistribucion.tblRutasDeDistribucion.getValueAt(row, 0)));
           
            ruta = new CRutasDeDistribucion(ini);
            ruta.setIdRutasDeDistribucion((Integer) fCrearRutasDeDistribucion.tblRutasDeDistribucion.getValueAt(row, 0));
            ruta.setNombreRutasDeDistribucion(fCrearRutasDeDistribucion.txtNombreRuta.getText().trim());
            ruta.setTipoRuta(fCrearRutasDeDistribucion.cbxTipoRuta.getSelectedItem().toString().substring(0, 1));
            if (fCrearRutasDeDistribucion.chkActivo.isSelected()) {
                ruta.setActivoRutasDeDistribucion(1);
            } else {
                ruta.setActivoRutasDeDistribucion(0);
            }

            valida = ruta.actualizarRutasDeDistribucions();

            if (valida) {

                ini.setListaDeRutasDeDistribucion();
                JOptionPane.showInternalMessageDialog(fCrearRutasDeDistribucion, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearRutasDeDistribucion.lblRutaDeDistribucion.setVisible(true);
                fCrearRutasDeDistribucion.lblRutaDeDistribucion.setText("Id. Ruta de Dist. : " + ruta.getIdRutasDeDistribucion());
                fCrearRutasDeDistribucion.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearRutasDeDistribucion.btnNuevo.setEnabled(true);
                fCrearRutasDeDistribucion.jBtnNuevo.setEnabled(true);
                fCrearRutasDeDistribucion.btnGrabar.setEnabled(false);
                fCrearRutasDeDistribucion.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCanalDeVentas.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
