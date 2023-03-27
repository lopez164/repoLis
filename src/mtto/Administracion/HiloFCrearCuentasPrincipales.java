/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.Administracion;

import aplicacionlogistica.distribucion.administracion.TalentoHUmano.*;
import aplicacionlogistica.distribucion.administracion.logistica.*;
import aplicacionlogistica.configuracion.Inicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mtto.ingresoDeRegistros.objetos.CCuentasPrincipalesLogistica;

/**
 *
 * @author Usuario
 */
public class HiloFCrearCuentasPrincipales implements Runnable {

    Inicio ini = null;
    FCrearCuentasPrincipales fCrearCuentasPrincipales = null;
    String caso;
    CCuentasPrincipalesLogistica cuentaPPal = null;
    int idCuentaPpal = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFCrearCuentasPrincipales(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fCrearCuentasPrincipales
     * @param comando
     */
    public HiloFCrearCuentasPrincipales(Inicio ini,  FCrearCuentasPrincipales fCrearCuentasPrincipales, String comando) {
        this.ini = ini;
        this.fCrearCuentasPrincipales = fCrearCuentasPrincipales;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeCuenTasPPales":
                        listaDeCuenTasPPales();
                        break;

                    case "guardar":
                        if (fCrearCuentasPrincipales.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearCuentasPrincipales, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // FCrearCuentasPrincipales.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFCrearCuentasPrincipales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeCuenTasPPales() {
        ini.setListadeCuentasPrincipales();

        fCrearCuentasPrincipales.txtnombreCuentasPPales.setEnabled(false);
        
        for (CCuentasPrincipalesLogistica obj : ini.getListadeCuentasPrincipales()) {
            fila = new Object[4];
            fila[0] = obj.getIdCuentasPpal();
            fila[1] = obj.getNombreCuentasPpal();
            fila[2] = obj.getCodigoCuenta();
            if (obj.getActivoCuentasPpal()< 1) {
                fila[3] = false;
            } else {
                fila[3] = true;
            }
            
            modelo = (DefaultTableModel) fCrearCuentasPrincipales.tblCuentasPPales.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearCuentasPrincipales.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroCuentaPPal()) {
            fCrearCuentasPrincipales.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearCuentasPrincipales.btnGrabar.setEnabled(false);
        } else {
            fCrearCuentasPrincipales.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearCuentasPrincipales, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearCuentasPrincipales.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearCuentasPrincipales.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegisCuentaPpal()) {
            fCrearCuentasPrincipales.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearCuentasPrincipales.btnGrabar.setEnabled(false);

        } else {
            fCrearCuentasPrincipales.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearCuentasPrincipales, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearCuentasPrincipales.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearCuentasPrincipales.iniciado = false;
        fCrearCuentasPrincipales.cuentaPPal = ini.getListadeCuentasPrincipales().get(fCrearCuentasPrincipales.indice);

        fCrearCuentasPrincipales.txtnombreCuentasPPales.setText("");
        fCrearCuentasPrincipales.txtnombreCuentasPPales.setEnabled(false);
        fCrearCuentasPrincipales.txtCoigoCuenta.setText("");
        fCrearCuentasPrincipales.txtCoigoCuenta.setEnabled(false);
        
        fCrearCuentasPrincipales.actualizar = false;
        fCrearCuentasPrincipales.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearCuentasPrincipales.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearCuentasPrincipales.tblCuentasPPales.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearCuentasPrincipales.tblCuentasPPales.getModel();

        for (CCuentasPrincipalesLogistica obj : ini.getListadeCuentasPrincipales()) {
            try {
                int filaTabla2 = fCrearCuentasPrincipales.tblCuentasPPales.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearCuentasPrincipales.tblCuentasPPales.setValueAt(obj.getIdCuentasPpal(), filaTabla2, 0);  // item
                fCrearCuentasPrincipales.tblCuentasPPales.setValueAt(obj.getNombreCuentasPpal(), filaTabla2, 1); // numero de factura
                fCrearCuentasPrincipales.tblCuentasPPales.setValueAt(obj.getCodigoCuenta(), filaTabla2, 2); // numero de factura
                if (obj.getActivoCuentasPpal()== 1) {
                    fCrearCuentasPrincipales.tblCuentasPPales.setValueAt(true, filaTabla2, 3); //
                } else {
                    fCrearCuentasPrincipales.tblCuentasPPales.setValueAt(false, filaTabla2, 3); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFCrearCuentasPrincipales.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroCuentaPPal() {
        boolean valida = false;
        try {

            cuentaPPal = new CCuentasPrincipalesLogistica(ini);
            cuentaPPal.setNombreCuentaPpal(fCrearCuentasPrincipales.txtnombreCuentasPPales.getText().trim());
            cuentaPPal.setCodigoCuenta(fCrearCuentasPrincipales.txtCoigoCuenta.getText().trim());
            if (fCrearCuentasPrincipales.chkActivo.isSelected()) {
                cuentaPPal.setActivoCuentasPpal(1);
            } else {
                cuentaPPal.setActivoCuentasPpal(0);
            }
            valida = cuentaPPal.grabarCuentasPpals();
            if (valida) {

                ini.setListadeCuentasPrincipales();
                JOptionPane.showInternalMessageDialog(fCrearCuentasPrincipales, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearCuentasPrincipales.btnNuevo.setEnabled(true);
                fCrearCuentasPrincipales.jBtnNuevo.setEnabled(true);
                fCrearCuentasPrincipales.btnGrabar.setEnabled(false);
                fCrearCuentasPrincipales.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearCuentasPrincipales.tblCuentasPPales.getSelectedRow();
        fCrearCuentasPrincipales.lblIdCuentasPPales.setText("Id. Cuenta Ppal :  " + String.valueOf(fCrearCuentasPrincipales.tblCuentasPPales.getValueAt(row, 0)));
        
        fCrearCuentasPrincipales.txtnombreCuentasPPales.setText(String.valueOf(fCrearCuentasPrincipales.tblCuentasPPales.getValueAt(row, 1)));
         fCrearCuentasPrincipales.txtCoigoCuenta.setText(String.valueOf(fCrearCuentasPrincipales.tblCuentasPPales.getValueAt(row, 2)));
        
        if ((Boolean) fCrearCuentasPrincipales.tblCuentasPPales.getModel().getValueAt(row, 3)) {
            fCrearCuentasPrincipales.chkActivo.setText("Cuenta Ppal  Activo");
            fCrearCuentasPrincipales.chkActivo.setSelected(true);
        } else {
            fCrearCuentasPrincipales.chkActivo.setText("Cuenta Ppal  no Activo");
            fCrearCuentasPrincipales.chkActivo.setSelected(false);
        }
        fCrearCuentasPrincipales.actualizar = true;
        fCrearCuentasPrincipales.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearCuentasPrincipales.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegisCuentaPpal() {
        boolean valida = false;
        try {
            int row = fCrearCuentasPrincipales.tblCuentasPPales.getSelectedRow();
            fCrearCuentasPrincipales.lblIdCuentasPPales.setText("Id. Cuenta Ppal :  " + String.valueOf(fCrearCuentasPrincipales.tblCuentasPPales.getValueAt(row, 0)));
            cuentaPPal = new CCuentasPrincipalesLogistica(ini);
            cuentaPPal.setIdCuentasPpal((Integer) fCrearCuentasPrincipales.tblCuentasPPales.getValueAt(row, 0));
            cuentaPPal.setNombreCuentaPpal(fCrearCuentasPrincipales.txtnombreCuentasPPales.getText().trim());
            cuentaPPal.setCodigoCuenta(fCrearCuentasPrincipales.txtCoigoCuenta.getText().trim());
            if (fCrearCuentasPrincipales.chkActivo.isSelected()) {
                cuentaPPal.setActivoCuentasPpal(1);
            } else {
                cuentaPPal.setActivoCuentasPpal(0);
            }

            valida = cuentaPPal.actualizarCuentasPpals();

            if (valida) {
                ini.setListadeCuentasPrincipales();
                JOptionPane.showInternalMessageDialog(fCrearCuentasPrincipales, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearCuentasPrincipales.lblIdCuentasPPales.setVisible(true);
                fCrearCuentasPrincipales.lblIdCuentasPPales.setText("Id. Cuenta Ppal. :  " + cuentaPPal.getIdCuentasPpal());
                fCrearCuentasPrincipales.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearCuentasPrincipales.btnNuevo.setEnabled(true);
                fCrearCuentasPrincipales.jBtnNuevo.setEnabled(true);
                fCrearCuentasPrincipales.btnGrabar.setEnabled(false);
                fCrearCuentasPrincipales.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
