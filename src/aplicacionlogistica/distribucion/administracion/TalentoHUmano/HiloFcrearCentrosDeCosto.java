/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.administracion.TalentoHUmano;

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
public class HiloFcrearCentrosDeCosto implements Runnable {

    Inicio ini = null;
    FCrearCentrosDeCosto fCrearCentrosDeCosto = null;
    String caso;
    CCentrosDeCosto centroDeCosto= null;
    int idCentroDeCosto = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearCentrosDeCosto(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param FCrearCentrosDeCosto
     * @param comando
     */
    public HiloFcrearCentrosDeCosto(Inicio ini,  FCrearCentrosDeCosto FCrearCentrosDeCosto, String comando) {
        this.ini = ini;
        this.fCrearCentrosDeCosto = FCrearCentrosDeCosto;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeCentrosDeCosto":
                        listaDeCentrosDeCosto();
                        break;

                    case "guardar":
                        if (fCrearCentrosDeCosto.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearCentrosDeCosto, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la car
            tabla correspondiente. */
            // fCrearCentrosDeCosto.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearCentrosDeCosto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeCentrosDeCosto() {
        ini.setListaDeCentrosDeCosto();

        fCrearCentrosDeCosto.txtnombreCentroDeCosto.setEnabled(false);
        
        for (CCentrosDeCosto obj : ini.getListaDeCentrosDeCosto()) {
            fila = new Object[3];
            fila[0] = obj.getIdCentroDeCosto();
            fila[1] = obj.getNombreCentroDeCosto();
            if (obj.getactivoCentroDeCosto()< 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            
            modelo = (DefaultTableModel) fCrearCentrosDeCosto.tblCentrosDeCosto.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearCentrosDeCosto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroCentroDeCosto()) {
            fCrearCentrosDeCosto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearCentrosDeCosto.btnGrabar.setEnabled(false);
        } else {
            fCrearCentrosDeCosto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearCentrosDeCosto, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearCentrosDeCosto.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearCentrosDeCosto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroCentroDeCosto()) {
            fCrearCentrosDeCosto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearCentrosDeCosto.btnGrabar.setEnabled(false);

        } else {
            fCrearCentrosDeCosto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearCentrosDeCosto, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearCentrosDeCosto.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearCentrosDeCosto.iniciado = false;
        fCrearCentrosDeCosto.centroDeCosto= ini.getListaDeCentrosDeCosto().get(fCrearCentrosDeCosto.indice);

        fCrearCentrosDeCosto.txtnombreCentroDeCosto.setText("");
        fCrearCentrosDeCosto.txtnombreCentroDeCosto.setEnabled(false);
        fCrearCentrosDeCosto.actualizar = false;
        fCrearCentrosDeCosto.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearCentrosDeCosto.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearCentrosDeCosto.tblCentrosDeCosto.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearCentrosDeCosto.tblCentrosDeCosto.getModel();

        for (CCentrosDeCosto obj : ini.getListaDeCentrosDeCosto()) {
            try {
                int filaTabla2 = fCrearCentrosDeCosto.tblCentrosDeCosto.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearCentrosDeCosto.tblCentrosDeCosto.setValueAt(obj.getIdCentroDeCosto(), filaTabla2, 0);  // item
                fCrearCentrosDeCosto.tblCentrosDeCosto.setValueAt(obj.getNombreCentroDeCosto(), filaTabla2, 1); // numero de factura
                if (obj.getactivoCentroDeCosto()== 1) {
                    fCrearCentrosDeCosto.tblCentrosDeCosto.setValueAt(true, filaTabla2, 2); //
                } else {
                    fCrearCentrosDeCosto.tblCentrosDeCosto.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearCentrosDeCosto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroCentroDeCosto() {
        boolean valida = false;
        try {

            centroDeCosto= new CCentrosDeCosto(ini);
            centroDeCosto.setNombreCentroDeCosto(fCrearCentrosDeCosto.txtnombreCentroDeCosto.getText().trim());
            //canal.setIdCanalDeVenta(fCrearCentrosDeCosto.causalDeDevolucion.getIdCanalDeVenta());
            if (fCrearCentrosDeCosto.chkActivo.isSelected()) {
                centroDeCosto.setactivoCentroDeCosto(1);
            } else {
                centroDeCosto.setactivoCentroDeCosto(0);
            }
            valida = centroDeCosto.grabarCentrosDeCosto();
            if (valida) {

                ini.setListaDeCentrosDeCosto();
                JOptionPane.showInternalMessageDialog(fCrearCentrosDeCosto, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearCentrosDeCosto.btnNuevo.setEnabled(true);
                fCrearCentrosDeCosto.jBtnNuevo.setEnabled(true);
                fCrearCentrosDeCosto.btnGrabar.setEnabled(false);
                fCrearCentrosDeCosto.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearCentrosDeCosto.tblCentrosDeCosto.getSelectedRow();
        fCrearCentrosDeCosto.lblIdCentroDeCosto.setText("Id. Centro de Costo :  " + String.valueOf(fCrearCentrosDeCosto.tblCentrosDeCosto.getValueAt(row, 0)));
        
        fCrearCentrosDeCosto.txtnombreCentroDeCosto.setText(String.valueOf(fCrearCentrosDeCosto.tblCentrosDeCosto.getValueAt(row, 1)));
        
        if ((Boolean) fCrearCentrosDeCosto.tblCentrosDeCosto.getModel().getValueAt(row, 2)) {
            fCrearCentrosDeCosto.chkActivo.setText("Centro de Costo  Activo");
            fCrearCentrosDeCosto.chkActivo.setSelected(true);
        } else {
            fCrearCentrosDeCosto.chkActivo.setText("Centro de Costo  no Activo");
            fCrearCentrosDeCosto.chkActivo.setSelected(false);
        }
        fCrearCentrosDeCosto.actualizar = true;
        fCrearCentrosDeCosto.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearCentrosDeCosto.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroCentroDeCosto() {
        boolean valida = false;
        try {
            int row = fCrearCentrosDeCosto.tblCentrosDeCosto.getSelectedRow();
            fCrearCentrosDeCosto.lblIdCentroDeCosto.setText("Id. Centro de Costo :  " + String.valueOf(fCrearCentrosDeCosto.tblCentrosDeCosto.getValueAt(row, 0)));
            centroDeCosto= new CCentrosDeCosto(ini);
            centroDeCosto.setIdCentroDeCosto((Integer) fCrearCentrosDeCosto.tblCentrosDeCosto.getValueAt(row, 0));
            centroDeCosto.setNombreCentroDeCosto(fCrearCentrosDeCosto.txtnombreCentroDeCosto.getText().trim());
            if (fCrearCentrosDeCosto.chkActivo.isSelected()) {
                centroDeCosto.setactivoCentroDeCosto(1);
            } else {
                centroDeCosto.setactivoCentroDeCosto(0);
            }

            valida = centroDeCosto.actualizarCentrosDeCosto();

            if (valida) {
                ini.setListaDeCentrosDeCosto();
                JOptionPane.showInternalMessageDialog(fCrearCentrosDeCosto, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearCentrosDeCosto.lblIdCentroDeCosto.setVisible(true);
                fCrearCentrosDeCosto.lblIdCentroDeCosto.setText("Id. Centro de Costo :  " + centroDeCosto.getIdCentroDeCosto());
                fCrearCentrosDeCosto.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearCentrosDeCosto.btnNuevo.setEnabled(true);
                fCrearCentrosDeCosto.jBtnNuevo.setEnabled(true);
                fCrearCentrosDeCosto.btnGrabar.setEnabled(false);
                fCrearCentrosDeCosto.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
