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
public class HiloFcrearMarcasDeLLantas implements Runnable {

    Inicio ini = null;
    FCrearMarcasDeLLantas fCrearMarcasDeLLantas = null;
    String caso;
    CMarcasDeLLantas marcaDeLLantas = null;
    int idMarcaDeLlanta = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearMarcasDeLLantas(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param FCrearMarcasDeLLantas
     * @param comando
     */
    public HiloFcrearMarcasDeLLantas(Inicio ini,  FCrearMarcasDeLLantas FCrearMarcasDeLLantas, String comando) {
        this.ini = ini;
        this.fCrearMarcasDeLLantas = FCrearMarcasDeLLantas;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeMarcasDeLlantas":
                        listaDeMarcasDeLLantas();
                        break;

                    case "guardar":
                        if (fCrearMarcasDeLLantas.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearMarcasDeLLantas, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearMarcasDeLLantas.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearMarcasDeLLantas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeMarcasDeLLantas() {
        ini.setListaDeMarcasdeLLantas();

        fCrearMarcasDeLLantas.txtMarcaDeLlantas.setEnabled(false);
        
        for (CMarcasDeLLantas obj : ini.getListaDeMarcasdeLLantas()) {
            fila = new Object[3];
            fila[0] = obj.getIdMarcaDeLLantas();
            fila[1] = obj.getNombreMarcaDeLLantas();
            if (obj.getActivoMarcaDeLLantas()< 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            
            modelo = (DefaultTableModel) fCrearMarcasDeLLantas.tblMarcaDeLLantas.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearMarcasDeLLantas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroMarcaDeLlanta()) {
            fCrearMarcasDeLLantas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearMarcasDeLLantas.btnGrabar.setEnabled(false);
        } else {
            fCrearMarcasDeLLantas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearMarcasDeLLantas, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearMarcasDeLLantas.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearMarcasDeLLantas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroMarcaDeLLanta()) {
            fCrearMarcasDeLLantas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearMarcasDeLLantas.btnGrabar.setEnabled(false);

        } else {
            fCrearMarcasDeLLantas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearMarcasDeLLantas, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearMarcasDeLLantas.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearMarcasDeLLantas.iniciado = false;
        fCrearMarcasDeLLantas.marcaDeLLanta = ini.getListaDeMarcasdeLLantas().get(fCrearMarcasDeLLantas.indice);

        fCrearMarcasDeLLantas.txtMarcaDeLlantas.setText("");
        fCrearMarcasDeLLantas.txtMarcaDeLlantas.setEnabled(false);
        fCrearMarcasDeLLantas.actualizar = false;
        fCrearMarcasDeLLantas.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearMarcasDeLLantas.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearMarcasDeLLantas.tblMarcaDeLLantas.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearMarcasDeLLantas.tblMarcaDeLLantas.getModel();

        for (CMarcasDeLLantas obj : ini.getListaDeMarcasdeLLantas()) {
            try {
                int filaTabla2 = fCrearMarcasDeLLantas.tblMarcaDeLLantas.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearMarcasDeLLantas.tblMarcaDeLLantas.setValueAt(obj.getIdMarcaDeLLantas(), filaTabla2, 0);  // item
                fCrearMarcasDeLLantas.tblMarcaDeLLantas.setValueAt(obj.getNombreMarcaDeLLantas(), filaTabla2, 1); // numero de factura
                if (obj.getActivoMarcaDeLLantas()== 1) {
                    fCrearMarcasDeLLantas.tblMarcaDeLLantas.setValueAt(true, filaTabla2, 2); //
                } else {
                    fCrearMarcasDeLLantas.tblMarcaDeLLantas.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearMarcasDeLLantas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroMarcaDeLlanta() {
        boolean valida = false;
        try {

            marcaDeLLantas = new CMarcasDeLLantas(ini);
            marcaDeLLantas.setNombreMarcaDeLLantas(fCrearMarcasDeLLantas.txtMarcaDeLlantas.getText().trim());
            //canal.setIdCanalDeVenta(fCrearMarcasDeLLantas.causalDeDevolucion.getIdCanalDeVenta());
            if (fCrearMarcasDeLLantas.chkActivo.isSelected()) {
                marcaDeLLantas.setActivoMarcaDeLLantas(1);
            } else {
                marcaDeLLantas.setActivoMarcaDeLLantas(0);
            }
            valida = marcaDeLLantas.grabarMarcaDeLLantass();
            if (valida) {

                ini.setListaDeMarcasdeLLantas();
                JOptionPane.showInternalMessageDialog(fCrearMarcasDeLLantas, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearMarcasDeLLantas.btnNuevo.setEnabled(true);
                fCrearMarcasDeLLantas.jBtnNuevo.setEnabled(true);
                fCrearMarcasDeLLantas.btnGrabar.setEnabled(false);
                fCrearMarcasDeLLantas.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearMarcasDeLLantas.tblMarcaDeLLantas.getSelectedRow();
        fCrearMarcasDeLLantas.lblIdMarca.setText("Id. Marca de Llanta :  " + String.valueOf(fCrearMarcasDeLLantas.tblMarcaDeLLantas.getValueAt(row, 0)));
        
        fCrearMarcasDeLLantas.txtMarcaDeLlantas.setText(String.valueOf(fCrearMarcasDeLLantas.tblMarcaDeLLantas.getValueAt(row, 1)));
        
        if ((Boolean) fCrearMarcasDeLLantas.tblMarcaDeLLantas.getModel().getValueAt(row, 2)) {
            fCrearMarcasDeLLantas.chkActivo.setText("Marca de Llanta  Activo");
            fCrearMarcasDeLLantas.chkActivo.setSelected(true);
        } else {
            fCrearMarcasDeLLantas.chkActivo.setText("Marca de Llanta  no Activo");
            fCrearMarcasDeLLantas.chkActivo.setSelected(false);
        }
        fCrearMarcasDeLLantas.actualizar = true;
        fCrearMarcasDeLLantas.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearMarcasDeLLantas.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroMarcaDeLLanta() {
        boolean valida = false;
        try {
            int row = fCrearMarcasDeLLantas.tblMarcaDeLLantas.getSelectedRow();
            fCrearMarcasDeLLantas.lblIdMarca.setText("Id. Marca de Llanta :  " + String.valueOf(fCrearMarcasDeLLantas.tblMarcaDeLLantas.getValueAt(row, 0)));
            marcaDeLLantas = new CMarcasDeLLantas(ini);
            marcaDeLLantas.setIdMarcaDeLLantas((Integer) fCrearMarcasDeLLantas.tblMarcaDeLLantas.getValueAt(row, 0));
            marcaDeLLantas.setNombreMarcaDeLLantas(fCrearMarcasDeLLantas.txtMarcaDeLlantas.getText().trim());
            if (fCrearMarcasDeLLantas.chkActivo.isSelected()) {
                marcaDeLLantas.setActivoMarcaDeLLantas(1);
            } else {
                marcaDeLLantas.setActivoMarcaDeLLantas(0);
            }

            valida = marcaDeLLantas.actualizarMarcaDeLLantass();

            if (valida) {
                ini.setListaDeMarcasdeLLantas();
                JOptionPane.showInternalMessageDialog(fCrearMarcasDeLLantas, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearMarcasDeLLantas.lblIdMarca.setVisible(true);
                fCrearMarcasDeLLantas.lblIdMarca.setText("Id. Marca de Llanta :  " + marcaDeLLantas.getIdMarcaDeLLantas());
                fCrearMarcasDeLLantas.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearMarcasDeLLantas.btnNuevo.setEnabled(true);
                fCrearMarcasDeLLantas.jBtnNuevo.setEnabled(true);
                fCrearMarcasDeLLantas.btnGrabar.setEnabled(false);
                fCrearMarcasDeLLantas.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
