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
public class HiloFcrearTiposContratosPersonas implements Runnable {

    Inicio ini = null;
    FCrearTiposContratosPersonas fCrearTiposContratosPersonas = null;
    String caso;
    CTiposDeContratosPersonas tipoContrato = null;
    int idTipoContratoPer = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearTiposContratosPersonas(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param FCrearTiposContratosPersonas
     * @param comando
     */
    public HiloFcrearTiposContratosPersonas(Inicio ini,  FCrearTiposContratosPersonas FCrearTiposContratosPersonas, String comando) {
        this.ini = ini;
        this.fCrearTiposContratosPersonas = FCrearTiposContratosPersonas;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeTiposDeContratos":
                        listaDeTiposDeContratos();
                        break;

                    case "guardar":
                        if (fCrearTiposContratosPersonas.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearTiposContratosPersonas, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearTiposContratosPersonas.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearTiposContratosPersonas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeTiposDeContratos() {
        ini.setListaDeTiposContratosPer();

        fCrearTiposContratosPersonas.txtnombreTipoContrato.setEnabled(false);
        
        for (CTiposDeContratosPersonas obj : ini.getListaDeTiposContratosPer()) {
            fila = new Object[3];
            fila[0] = obj.getIdTipoDeContrato();
            fila[1] = obj.getNombreTipoDeContrato();
            if (obj.getActivoTipoDeContrato()< 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            
            modelo = (DefaultTableModel) fCrearTiposContratosPersonas.tblTiposContratos.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearTiposContratosPersonas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroTipoContratoPer()) {
            fCrearTiposContratosPersonas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearTiposContratosPersonas.btnGrabar.setEnabled(false);
        } else {
            fCrearTiposContratosPersonas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearTiposContratosPersonas, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearTiposContratosPersonas.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearTiposContratosPersonas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroTipoContrato()) {
            fCrearTiposContratosPersonas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearTiposContratosPersonas.btnGrabar.setEnabled(false);

        } else {
            fCrearTiposContratosPersonas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearTiposContratosPersonas, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearTiposContratosPersonas.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearTiposContratosPersonas.iniciado = false;
        fCrearTiposContratosPersonas.tipoContrato = ini.getListaDeTiposContratosPer().get(fCrearTiposContratosPersonas.indice);

        fCrearTiposContratosPersonas.txtnombreTipoContrato.setText("");
        fCrearTiposContratosPersonas.txtnombreTipoContrato.setEnabled(false);
        fCrearTiposContratosPersonas.actualizar = false;
        fCrearTiposContratosPersonas.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearTiposContratosPersonas.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearTiposContratosPersonas.tblTiposContratos.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearTiposContratosPersonas.tblTiposContratos.getModel();

        for (CTiposDeContratosPersonas obj : ini.getListaDeTiposContratosPer()) {
            try {
                int filaTabla2 = fCrearTiposContratosPersonas.tblTiposContratos.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearTiposContratosPersonas.tblTiposContratos.setValueAt(obj.getIdTipoDeContrato(), filaTabla2, 0);  // item
                fCrearTiposContratosPersonas.tblTiposContratos.setValueAt(obj.getNombreTipoDeContrato(), filaTabla2, 1); // numero de factura
                if (obj.getActivoTipoDeContrato()== 1) {
                    fCrearTiposContratosPersonas.tblTiposContratos.setValueAt(true, filaTabla2, 2); //
                } else {
                    fCrearTiposContratosPersonas.tblTiposContratos.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearTiposContratosPersonas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroTipoContratoPer() {
        boolean valida = false;
        try {

            tipoContrato = new CTiposDeContratosPersonas(ini);
            tipoContrato.setNombreTipoDeContrato(fCrearTiposContratosPersonas.txtnombreTipoContrato.getText().trim());
            //canal.setIdCanalDeVenta(fCrearTiposContratosPersonas.causalDeDevolucion.getIdCanalDeVenta());
            if (fCrearTiposContratosPersonas.chkActivo.isSelected()) {
                tipoContrato.setActivoTipoDeContrato(1);
            } else {
                tipoContrato.setActivoTipoDeContrato(0);
            }
            valida = tipoContrato.grabarTiposDeContrato();
            if (valida) {

                ini.setListaDeTiposContratosPer();
                JOptionPane.showInternalMessageDialog(fCrearTiposContratosPersonas, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearTiposContratosPersonas.btnNuevo.setEnabled(true);
                fCrearTiposContratosPersonas.jBtnNuevo.setEnabled(true);
                fCrearTiposContratosPersonas.btnGrabar.setEnabled(false);
                fCrearTiposContratosPersonas.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearTiposContratosPersonas.tblTiposContratos.getSelectedRow();
        fCrearTiposContratosPersonas.lblIdTipoContrato.setText("Id. Tipo Contrato :  " + String.valueOf(fCrearTiposContratosPersonas.tblTiposContratos.getValueAt(row, 0)));
        
        fCrearTiposContratosPersonas.txtnombreTipoContrato.setText(String.valueOf(fCrearTiposContratosPersonas.tblTiposContratos.getValueAt(row, 1)));
        
        if ((Boolean) fCrearTiposContratosPersonas.tblTiposContratos.getModel().getValueAt(row, 2)) {
            fCrearTiposContratosPersonas.chkActivo.setText("Tipo Contrato  Activo");
            fCrearTiposContratosPersonas.chkActivo.setSelected(true);
        } else {
            fCrearTiposContratosPersonas.chkActivo.setText("Tipo Contrato  no Activo");
            fCrearTiposContratosPersonas.chkActivo.setSelected(false);
        }
        fCrearTiposContratosPersonas.actualizar = true;
        fCrearTiposContratosPersonas.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearTiposContratosPersonas.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroTipoContrato() {
        boolean valida = false;
        try {
            int row = fCrearTiposContratosPersonas.tblTiposContratos.getSelectedRow();
            fCrearTiposContratosPersonas.lblIdTipoContrato.setText("Id. Tipo Contrato :  " + String.valueOf(fCrearTiposContratosPersonas.tblTiposContratos.getValueAt(row, 0)));
            tipoContrato = new CTiposDeContratosPersonas(ini);
            tipoContrato.setIdTipoDeContrato((Integer) fCrearTiposContratosPersonas.tblTiposContratos.getValueAt(row, 0));
            tipoContrato.setNombreTipoDeContrato(fCrearTiposContratosPersonas.txtnombreTipoContrato.getText().trim());
            if (fCrearTiposContratosPersonas.chkActivo.isSelected()) {
                tipoContrato.setActivoTipoDeContrato(1);
            } else {
                tipoContrato.setActivoTipoDeContrato(0);
            }

            valida = tipoContrato.actualizarTipoContratoPer();

            if (valida) {
                ini.setListaDeTiposContratosPer();
                JOptionPane.showInternalMessageDialog(fCrearTiposContratosPersonas, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearTiposContratosPersonas.lblIdTipoContrato.setVisible(true);
                fCrearTiposContratosPersonas.lblIdTipoContrato.setText("Id. Tipo Contrato :  " + tipoContrato.getIdTipoDeContrato());
                fCrearTiposContratosPersonas.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearTiposContratosPersonas.btnNuevo.setEnabled(true);
                fCrearTiposContratosPersonas.jBtnNuevo.setEnabled(true);
                fCrearTiposContratosPersonas.btnGrabar.setEnabled(false);
                fCrearTiposContratosPersonas.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
