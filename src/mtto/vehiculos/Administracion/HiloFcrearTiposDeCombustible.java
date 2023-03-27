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
public class HiloFcrearTiposDeCombustible implements Runnable {

    
    Inicio ini = null;
    FCrearTiposdeCombustible fCrearTiposdeCombustible = null;
    String caso;
    CTiposDeCombustibles tiposCombustible = null;
    int idTipoCombustible = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearTiposDeCombustible(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param FCrearTiposdeCombustible
     * @param comando
     */
    public HiloFcrearTiposDeCombustible(Inicio ini,  FCrearTiposdeCombustible FCrearTiposdeCombustible, String comando) {
        this.ini = ini;
        this.fCrearTiposdeCombustible = FCrearTiposdeCombustible;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeTiposDeCombustible":
                        listaDeTiposDeCombustible();
                        break;

                    case "guardar":
                        if (fCrearTiposdeCombustible.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearTiposdeCombustible, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearTiposdeCombustible.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearTiposDeCombustible.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeTiposDeCombustible() {
        ini.setListaDeTiposDeCombustibles();

        fCrearTiposdeCombustible.txtTiposCobustible.setEnabled(false);
        
        for (CTiposDeCombustibles obj : ini.getListaDeTiposDeCombustibles()) {
            fila = new Object[3];
            fila[0] = obj.getIdCombustible();
            fila[1] = obj.getNombreCombustible();
            if (obj.getActivoCombustible()< 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            
            modelo = (DefaultTableModel) fCrearTiposdeCombustible.tblTiposCombustible.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearTiposdeCombustible.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroTipoCombustible()) {
            fCrearTiposdeCombustible.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearTiposdeCombustible.btnGrabar.setEnabled(false);
        } else {
            fCrearTiposdeCombustible.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearTiposdeCombustible, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearTiposdeCombustible.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearTiposdeCombustible.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroTipoCombustible()) {
            fCrearTiposdeCombustible.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearTiposdeCombustible.btnGrabar.setEnabled(false);

        } else {
            fCrearTiposdeCombustible.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearTiposdeCombustible, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearTiposdeCombustible.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearTiposdeCombustible.iniciado = false;
        fCrearTiposdeCombustible.tiposDeCombustible = ini.getListaDeTiposDeCombustibles().get(fCrearTiposdeCombustible.indice);

        fCrearTiposdeCombustible.txtTiposCobustible.setText("");
        fCrearTiposdeCombustible.txtTiposCobustible.setEnabled(false);
        fCrearTiposdeCombustible.actualizar = false;
        fCrearTiposdeCombustible.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearTiposdeCombustible.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearTiposdeCombustible.tblTiposCombustible.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearTiposdeCombustible.tblTiposCombustible.getModel();

        for (CTiposDeCombustibles obj : ini.getListaDeTiposDeCombustibles()) {
            try {
                int filaTabla2 = fCrearTiposdeCombustible.tblTiposCombustible.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearTiposdeCombustible.tblTiposCombustible.setValueAt(obj.getIdCombustible(), filaTabla2, 0);  // item
                fCrearTiposdeCombustible.tblTiposCombustible.setValueAt(obj.getNombreCombustible(), filaTabla2, 1); // numero de factura
                if (obj.getActivoCombustible()== 1) {
                    fCrearTiposdeCombustible.tblTiposCombustible.setValueAt(true, filaTabla2, 2); //
                } else {
                    fCrearTiposdeCombustible.tblTiposCombustible.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearTiposDeCombustible.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroTipoCombustible() {
        boolean valida = false;
        try {

            tiposCombustible = new CTiposDeCombustibles(ini);
            tiposCombustible.setNombreCombustible(fCrearTiposdeCombustible.txtTiposCobustible.getText().trim());
            //canal.setIdCanalDeVenta(fCrearTiposdeCombustible.causalDeDevolucion.getIdCanalDeVenta());
            if (fCrearTiposdeCombustible.chkActivo.isSelected()) {
                tiposCombustible.setActivoCombustible(1);
            } else {
                tiposCombustible.setActivoCombustible(0);
            }
            valida = tiposCombustible.grabarCombustibles();
            if (valida) {

                ini.setListaDeTiposDeCombustibles();
                JOptionPane.showInternalMessageDialog(fCrearTiposdeCombustible, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearTiposdeCombustible.btnNuevo.setEnabled(true);
                fCrearTiposdeCombustible.jBtnNuevo.setEnabled(true);
                fCrearTiposdeCombustible.btnGrabar.setEnabled(false);
                fCrearTiposdeCombustible.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearTiposdeCombustible.tblTiposCombustible.getSelectedRow();
        fCrearTiposdeCombustible.lblIdTipoCombustible.setText("Id. Tipo Combustible :  " + String.valueOf(fCrearTiposdeCombustible.tblTiposCombustible.getValueAt(row, 0)));
        
        fCrearTiposdeCombustible.txtTiposCobustible.setText(String.valueOf(fCrearTiposdeCombustible.tblTiposCombustible.getValueAt(row, 1)));
        
        if ((Boolean) fCrearTiposdeCombustible.tblTiposCombustible.getModel().getValueAt(row, 2)) {
            fCrearTiposdeCombustible.chkActivo.setText("Tipo Combustible  Activo");
            fCrearTiposdeCombustible.chkActivo.setSelected(true);
        } else {
            fCrearTiposdeCombustible.chkActivo.setText("Tipo Combustible  no Activo");
            fCrearTiposdeCombustible.chkActivo.setSelected(false);
        }
        fCrearTiposdeCombustible.actualizar = true;
        fCrearTiposdeCombustible.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearTiposdeCombustible.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroTipoCombustible() {
        boolean valida = false;
        try {
            int row = fCrearTiposdeCombustible.tblTiposCombustible.getSelectedRow();
            fCrearTiposdeCombustible.lblIdTipoCombustible.setText("Id. Tipo Combustible :  " + String.valueOf(fCrearTiposdeCombustible.tblTiposCombustible.getValueAt(row, 0)));
            tiposCombustible = new CTiposDeCombustibles(ini);
            tiposCombustible.setIdCombustible((Integer) fCrearTiposdeCombustible.tblTiposCombustible.getValueAt(row, 0));
            tiposCombustible.setNombreCombustible(fCrearTiposdeCombustible.txtTiposCobustible.getText().trim());
            if (fCrearTiposdeCombustible.chkActivo.isSelected()) {
                tiposCombustible.setActivoCombustible(1);
            } else {
                tiposCombustible.setActivoCombustible(0);
            }

            valida = tiposCombustible.actualizarCombustibles();

            if (valida) {
                ini.setListaDeTiposDeCombustibles();
                JOptionPane.showInternalMessageDialog(fCrearTiposdeCombustible, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearTiposdeCombustible.lblIdTipoCombustible.setVisible(true);
                fCrearTiposdeCombustible.lblIdTipoCombustible.setText("Id. Tipo Combustible :  " + tiposCombustible.getIdCombustible());
                fCrearTiposdeCombustible.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearTiposdeCombustible.btnNuevo.setEnabled(true);
                fCrearTiposdeCombustible.jBtnNuevo.setEnabled(true);
                fCrearTiposdeCombustible.btnGrabar.setEnabled(false);
                fCrearTiposdeCombustible.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
