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
public class HiloFcrearTiposdeContratosVehiculos implements Runnable {

    Inicio ini = null;
    FCrearTiposdeContratosVehiculos fCrearTiposdeContratosVehiculos = null;
    String caso;
    CTiposDeContratosVehiculos tipoContrato = null;
    int idTipoContrato = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearTiposdeContratosVehiculos(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param FCrearTiposdeContratosVehiculos
     * @param comando
     */
    public HiloFcrearTiposdeContratosVehiculos(Inicio ini,  FCrearTiposdeContratosVehiculos FCrearTiposdeContratosVehiculos, String comando) {
        this.ini = ini;
        this.fCrearTiposdeContratosVehiculos = FCrearTiposdeContratosVehiculos;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeTiposDeContratosVeh":
                        listaDeTiposDeContratosVeh();
                        break;

                    case "guardar":
                        if (fCrearTiposdeContratosVehiculos.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearTiposdeContratosVehiculos, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearTiposdeContratosVehiculos.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearTiposdeContratosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeTiposDeContratosVeh() {
        ini.setListaDeTiposDeContratosVehiculos();

        fCrearTiposdeContratosVehiculos.txtTipoContratoVeh.setEnabled(false);
        
        for (CTiposDeContratosVehiculos obj : ini.getListaDeTiposDeContratosVehiculos()) {
            fila = new Object[3];
            fila[0] = obj.getIdTipoDeContrato();
            fila[1] = obj.getNombreTipoDeContrato();
            if (obj.getActivoTipoDeContrato()< 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            
            modelo = (DefaultTableModel) fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearTiposdeContratosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroTipoContrato()) {
            fCrearTiposdeContratosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearTiposdeContratosVehiculos.btnGrabar.setEnabled(false);
        } else {
            fCrearTiposdeContratosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearTiposdeContratosVehiculos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearTiposdeContratosVehiculos.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearTiposdeContratosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroTipoContrato()) {
            fCrearTiposdeContratosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearTiposdeContratosVehiculos.btnGrabar.setEnabled(false);

        } else {
            fCrearTiposdeContratosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearTiposdeContratosVehiculos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearTiposdeContratosVehiculos.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearTiposdeContratosVehiculos.iniciado = false;
        fCrearTiposdeContratosVehiculos.tiposDeContratos = ini.getListaDeTiposDeContratosVehiculos().get(fCrearTiposdeContratosVehiculos.indice);

        fCrearTiposdeContratosVehiculos.txtTipoContratoVeh.setText("");
        fCrearTiposdeContratosVehiculos.txtTipoContratoVeh.setEnabled(false);
        fCrearTiposdeContratosVehiculos.actualizar = false;
        fCrearTiposdeContratosVehiculos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearTiposdeContratosVehiculos.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.getModel();

        for (CTiposDeContratosVehiculos obj : ini.getListaDeTiposDeContratosVehiculos()) {
            try {
                int filaTabla2 = fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.setValueAt(obj.getIdTipoDeContrato(), filaTabla2, 0);  // item
                fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.setValueAt(obj.getNombreTipoDeContrato(), filaTabla2, 1); // numero de factura
                if (obj.getActivoTipoDeContrato()== 1) {
                    fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.setValueAt(true, filaTabla2, 2); //
                } else {
                    fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearTiposdeContratosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroTipoContrato() {
        boolean valida = false;
        try {

            tipoContrato = new CTiposDeContratosVehiculos(ini);
            tipoContrato.setNombreTipoDeContrato(fCrearTiposdeContratosVehiculos.txtTipoContratoVeh.getText().trim());
            //canal.setIdCanalDeVenta(fCrearTiposdeContratosVehiculos.causalDeDevolucion.getIdCanalDeVenta());
            if (fCrearTiposdeContratosVehiculos.chkActivo.isSelected()) {
                tipoContrato.setActivoTipoDeContrato(1);
            } else {
                tipoContrato.setActivoTipoDeContrato(0);
            }
            valida = tipoContrato.grabarTipoDeContratos();
            if (valida) {

                ini.setListaDeTiposDeContratosVehiculos();
                JOptionPane.showInternalMessageDialog(fCrearTiposdeContratosVehiculos, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearTiposdeContratosVehiculos.btnNuevo.setEnabled(true);
                fCrearTiposdeContratosVehiculos.jBtnNuevo.setEnabled(true);
                fCrearTiposdeContratosVehiculos.btnGrabar.setEnabled(false);
                fCrearTiposdeContratosVehiculos.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.getSelectedRow();
        fCrearTiposdeContratosVehiculos.lblIdTipoContrato.setText("Id. Tipo Contrato :  " + String.valueOf(fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.getValueAt(row, 0)));
        
        fCrearTiposdeContratosVehiculos.txtTipoContratoVeh.setText(String.valueOf(fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.getValueAt(row, 1)));
        
        if ((Boolean) fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.getModel().getValueAt(row, 2)) {
            fCrearTiposdeContratosVehiculos.chkActivo.setText("Tipo Contrato  Activo");
            fCrearTiposdeContratosVehiculos.chkActivo.setSelected(true);
        } else {
            fCrearTiposdeContratosVehiculos.chkActivo.setText("Tipo Contrato  no Activo");
            fCrearTiposdeContratosVehiculos.chkActivo.setSelected(false);
        }
        fCrearTiposdeContratosVehiculos.actualizar = true;
        fCrearTiposdeContratosVehiculos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearTiposdeContratosVehiculos.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroTipoContrato() {
        boolean valida = false;
        try {
            int row = fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.getSelectedRow();
            fCrearTiposdeContratosVehiculos.lblIdTipoContrato.setText("Id. Tipo Contrato :  " + String.valueOf(fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.getValueAt(row, 0)));
            tipoContrato = new CTiposDeContratosVehiculos(ini);
            tipoContrato.setIdTipoDeContrato((Integer) fCrearTiposdeContratosVehiculos.tblTiposContratoVeh.getValueAt(row, 0));
            tipoContrato.setNombreTipoDeContrato(fCrearTiposdeContratosVehiculos.txtTipoContratoVeh.getText().trim());
            if (fCrearTiposdeContratosVehiculos.chkActivo.isSelected()) {
                tipoContrato.setActivoTipoDeContrato(1);
            } else {
                tipoContrato.setActivoTipoDeContrato(0);
            }

            valida = tipoContrato.actualizarTipoDeContratos();

            if (valida) {
                ini.setListaDeTiposDeContratosVehiculos();
                JOptionPane.showInternalMessageDialog(fCrearTiposdeContratosVehiculos, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearTiposdeContratosVehiculos.lblIdTipoContrato.setVisible(true);
                fCrearTiposdeContratosVehiculos.lblIdTipoContrato.setText("Id. Tipo Contrato :  " + tipoContrato.getIdTipoDeContrato());
                fCrearTiposdeContratosVehiculos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearTiposdeContratosVehiculos.btnNuevo.setEnabled(true);
                fCrearTiposdeContratosVehiculos.jBtnNuevo.setEnabled(true);
                fCrearTiposdeContratosVehiculos.btnGrabar.setEnabled(false);
                fCrearTiposdeContratosVehiculos.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
