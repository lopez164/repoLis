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
public class HiloFcrearEntidadesBancarias implements Runnable {

    Inicio ini = null;
    FCrearEntidadesBancarias fCrearEntidadesBancarias = null;
    String caso;
    CEntidadesBancarias banco = null;
    int idEntidadBancaria = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearEntidadesBancarias(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fCrearEntidadesBancarias
     * @param comando
     */
    public HiloFcrearEntidadesBancarias(Inicio ini,  FCrearEntidadesBancarias fCrearEntidadesBancarias, String comando) {
        this.ini = ini;
        this.fCrearEntidadesBancarias = fCrearEntidadesBancarias;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeBancos":
                        listaDeBancos();
                        break;

                    case "guardar":
                        if (fCrearEntidadesBancarias.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearEntidadesBancarias, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearEntidadesBancarias.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearEntidadesBancarias.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeBancos() {
        ini.setListaDeEntidadesBancarias();

        fCrearEntidadesBancarias.txtnombreBanco.setEnabled(false);
        
        for (CEntidadesBancarias obj : ini.getListaDeEntidadesBancarias()) {
            fila = new Object[3];
            fila[0] = obj.getIdEntidadBancaria();
            fila[1] = obj.getNombreEntidadBancaria();
            if (obj.getActivoEntidadBancaria()< 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            
            modelo = (DefaultTableModel) fCrearEntidadesBancarias.tblBancos.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearEntidadesBancarias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroEntidadBancaria()) {
            fCrearEntidadesBancarias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearEntidadesBancarias.btnGrabar.setEnabled(false);
        } else {
            fCrearEntidadesBancarias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearEntidadesBancarias, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearEntidadesBancarias.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearEntidadesBancarias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroEntidadBancaria()) {
            fCrearEntidadesBancarias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearEntidadesBancarias.btnGrabar.setEnabled(false);

        } else {
            fCrearEntidadesBancarias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearEntidadesBancarias, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearEntidadesBancarias.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearEntidadesBancarias.iniciado = false;
        fCrearEntidadesBancarias.banco = ini.getListaDeEntidadesBancarias().get(fCrearEntidadesBancarias.indice);

        fCrearEntidadesBancarias.txtnombreBanco.setText("");
        fCrearEntidadesBancarias.txtnombreBanco.setEnabled(false);
        fCrearEntidadesBancarias.actualizar = false;
        fCrearEntidadesBancarias.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearEntidadesBancarias.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearEntidadesBancarias.tblBancos.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearEntidadesBancarias.tblBancos.getModel();

        for (CEntidadesBancarias obj : ini.getListaDeEntidadesBancarias()) {
            try {
                int filaTabla2 = fCrearEntidadesBancarias.tblBancos.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearEntidadesBancarias.tblBancos.setValueAt(obj.getIdEntidadBancaria(), filaTabla2, 0);  // item
                fCrearEntidadesBancarias.tblBancos.setValueAt(obj.getNombreEntidadBancaria(), filaTabla2, 1); // numero de factura
                if (obj.getActivoEntidadBancaria()== 1) {
                    fCrearEntidadesBancarias.tblBancos.setValueAt(true, filaTabla2, 2); //
                } else {
                    fCrearEntidadesBancarias.tblBancos.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearEntidadesBancarias.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroEntidadBancaria() {
        boolean valida = false;
        try {

            banco = new CEntidadesBancarias(ini);
            banco.setNombreEntidadBancaria(fCrearEntidadesBancarias.txtnombreBanco.getText().trim());
            //canal.setIdCanalDeVenta(fCrearEntidadesBancarias.causalDeDevolucion.getIdCanalDeVenta());
            if (fCrearEntidadesBancarias.chkActivo.isSelected()) {
                banco.setActivoEntidadBancaria(1);
            } else {
                banco.setActivoEntidadBancaria(0);
            }
            valida = banco.grabarEntidadesBancarias();
            if (valida) {

                ini.setListaDeEntidadesBancarias();
                JOptionPane.showInternalMessageDialog(fCrearEntidadesBancarias, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearEntidadesBancarias.btnNuevo.setEnabled(true);
                fCrearEntidadesBancarias.jBtnNuevo.setEnabled(true);
                fCrearEntidadesBancarias.btnGrabar.setEnabled(false);
                fCrearEntidadesBancarias.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearEntidadesBancarias.tblBancos.getSelectedRow();
        fCrearEntidadesBancarias.lblIdBanco.setText("Id. Entidad Bancaria :  " + String.valueOf(fCrearEntidadesBancarias.tblBancos.getValueAt(row, 0)));
        
        fCrearEntidadesBancarias.txtnombreBanco.setText(String.valueOf(fCrearEntidadesBancarias.tblBancos.getValueAt(row, 1)));
        
        if ((Boolean) fCrearEntidadesBancarias.tblBancos.getModel().getValueAt(row, 2)) {
            fCrearEntidadesBancarias.chkActivo.setText("Entidad Bancaria  Activo");
            fCrearEntidadesBancarias.chkActivo.setSelected(true);
        } else {
            fCrearEntidadesBancarias.chkActivo.setText("Entidad Bancaria  no Activo");
            fCrearEntidadesBancarias.chkActivo.setSelected(false);
        }
        fCrearEntidadesBancarias.actualizar = true;
        fCrearEntidadesBancarias.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearEntidadesBancarias.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroEntidadBancaria() {
        boolean valida = false;
        try {
            int row = fCrearEntidadesBancarias.tblBancos.getSelectedRow();
            fCrearEntidadesBancarias.lblIdBanco.setText("Id. Entidad Bancaria :  " + String.valueOf(fCrearEntidadesBancarias.tblBancos.getValueAt(row, 0)));
            banco = new CEntidadesBancarias(ini);
            banco.setIdEntidadBancaria((Integer) fCrearEntidadesBancarias.tblBancos.getValueAt(row, 0));
            banco.setNombreEntidadBancaria(fCrearEntidadesBancarias.txtnombreBanco.getText().trim());
            if (fCrearEntidadesBancarias.chkActivo.isSelected()) {
                banco.setActivoEntidadBancaria(1);
            } else {
                banco.setActivoEntidadBancaria(0);
            }

            valida = banco.actualizarEntidadesBancarias();

            if (valida) {
                ini.setListaDeEntidadesBancarias();
                JOptionPane.showInternalMessageDialog(fCrearEntidadesBancarias, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearEntidadesBancarias.lblIdBanco.setVisible(true);
                fCrearEntidadesBancarias.lblIdBanco.setText("Id. Entidad Bancaria :  " + banco.getIdEntidadBancaria());
                fCrearEntidadesBancarias.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearEntidadesBancarias.btnNuevo.setEnabled(true);
                fCrearEntidadesBancarias.jBtnNuevo.setEnabled(true);
                fCrearEntidadesBancarias.btnGrabar.setEnabled(false);
                fCrearEntidadesBancarias.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
