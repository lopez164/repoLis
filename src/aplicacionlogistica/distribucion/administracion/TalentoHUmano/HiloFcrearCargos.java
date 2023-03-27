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
public class HiloFcrearCargos implements Runnable {

    Inicio ini = null;
    FCrearCargos FCrearCargos = null;
    String caso;
    CCargos cargo = null;
    int idCargo = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearCargos(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param FCrearCargos
     * @param comando
     */
    public HiloFcrearCargos(Inicio ini,  FCrearCargos FCrearCargos, String comando) {
        this.ini = ini;
        this.FCrearCargos = FCrearCargos;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeCargos":
                        listaDeCargos();
                        break;

                    case "guardar":
                        if (FCrearCargos.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(FCrearCargos, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // FCrearCargos.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearCargos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeCargos() {
        ini.setListaDeCargos();

        FCrearCargos.txtnombreCargo.setEnabled(false);
        
        for (CCargos obj : ini.getListaDeCargos()) {
            fila = new Object[3];
            fila[0] = obj.getIdCargo();
            fila[1] = obj.getNombreCargo();
            if (obj.getActivoCargo()< 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            
            modelo = (DefaultTableModel) FCrearCargos.tblCargos.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        FCrearCargos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroCargo()) {
            FCrearCargos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            FCrearCargos.btnGrabar.setEnabled(false);
        } else {
            FCrearCargos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(FCrearCargos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            FCrearCargos.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        FCrearCargos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroCargo()) {
            FCrearCargos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            FCrearCargos.btnGrabar.setEnabled(false);

        } else {
            FCrearCargos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(FCrearCargos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            FCrearCargos.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        FCrearCargos.iniciado = false;
        FCrearCargos.cargo = ini.getListaDeCargos().get(FCrearCargos.indice);

        FCrearCargos.txtnombreCargo.setText("");
        FCrearCargos.txtnombreCargo.setEnabled(false);
        FCrearCargos.actualizar = false;
        FCrearCargos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        FCrearCargos.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) FCrearCargos.tblCargos.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) FCrearCargos.tblCargos.getModel();

        for (CCargos obj : ini.getListaDeCargos()) {
            try {
                int filaTabla2 = FCrearCargos.tblCargos.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                FCrearCargos.tblCargos.setValueAt(obj.getIdCargo(), filaTabla2, 0);  // item
                FCrearCargos.tblCargos.setValueAt(obj.getNombreCargo(), filaTabla2, 1); // numero de factura
                if (obj.getActivoCargo()== 1) {
                    FCrearCargos.tblCargos.setValueAt(true, filaTabla2, 2); //
                } else {
                    FCrearCargos.tblCargos.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearCargos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroCargo() {
        boolean valida = false;
        try {

            cargo = new CCargos(ini);
            cargo.setNombreCargo(FCrearCargos.txtnombreCargo.getText().trim());
            //canal.setIdCanalDeVenta(FCrearCargos.causalDeDevolucion.getIdCanalDeVenta());
            if (FCrearCargos.chkActivo.isSelected()) {
                cargo.setActivoCargo(1);
            } else {
                cargo.setActivoCargo(0);
            }
            valida = cargo.grabarCargos();
            if (valida) {

                ini.setListaDeCargos();
                JOptionPane.showInternalMessageDialog(FCrearCargos, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                FCrearCargos.btnNuevo.setEnabled(true);
                FCrearCargos.jBtnNuevo.setEnabled(true);
                FCrearCargos.btnGrabar.setEnabled(false);
                FCrearCargos.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = FCrearCargos.tblCargos.getSelectedRow();
        FCrearCargos.lblIdCargos.setText("Id. Cargo :  " + String.valueOf(FCrearCargos.tblCargos.getValueAt(row, 0)));
        
        FCrearCargos.txtnombreCargo.setText(String.valueOf(FCrearCargos.tblCargos.getValueAt(row, 1)));
        
        if ((Boolean) FCrearCargos.tblCargos.getModel().getValueAt(row, 2)) {
            FCrearCargos.chkActivo.setText("Cargo  Activo");
            FCrearCargos.chkActivo.setSelected(true);
        } else {
            FCrearCargos.chkActivo.setText("Cargo  no Activo");
            FCrearCargos.chkActivo.setSelected(false);
        }
        FCrearCargos.actualizar = true;
        FCrearCargos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        FCrearCargos.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroCargo() {
        boolean valida = false;
        try {
            int row = FCrearCargos.tblCargos.getSelectedRow();
            FCrearCargos.lblIdCargos.setText("Id. Cargo :  " + String.valueOf(FCrearCargos.tblCargos.getValueAt(row, 0)));
            cargo = new CCargos(ini);
            cargo.setIdCargo((Integer) FCrearCargos.tblCargos.getValueAt(row, 0));
            cargo.setNombreCargo(FCrearCargos.txtnombreCargo.getText().trim());
            if (FCrearCargos.chkActivo.isSelected()) {
                cargo.setActivoCargo(1);
            } else {
                cargo.setActivoCargo(0);
            }

            valida = cargo.actualizarCargos();

            if (valida) {
                ini.setListaDeCargos();
                JOptionPane.showInternalMessageDialog(FCrearCargos, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                FCrearCargos.lblIdCargos.setVisible(true);
                FCrearCargos.lblIdCargos.setText("Id. Cargo :  " + cargo.getIdCargo());
                FCrearCargos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                FCrearCargos.btnNuevo.setEnabled(true);
                FCrearCargos.jBtnNuevo.setEnabled(true);
                FCrearCargos.btnGrabar.setEnabled(false);
                FCrearCargos.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
