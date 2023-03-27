/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.vehiculos.Administracion;

import aplicacionlogistica.distribucion.administracion.TalentoHUmano.*;
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
public class HiloFcrearTiposDeVehiculos implements Runnable {

    Inicio ini = null;
    FCrearTiposdeVehiculos fCrearTiposdeVehiculos = null;
    String caso;
    CTiposDeVehiculos tipoVehiculo = null;
    int idTipoVehiculo = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearTiposDeVehiculos(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param FCrearTiposdeVehiculos
     * @param comando
     */
    public HiloFcrearTiposDeVehiculos(Inicio ini,  FCrearTiposdeVehiculos FCrearTiposdeVehiculos, String comando) {
        this.ini = ini;
        this.fCrearTiposdeVehiculos = FCrearTiposdeVehiculos;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeTiposDeVehiculo":
                        listaDeTiposDeVehiculo();
                        break;

                    case "guardar":
                        if (fCrearTiposdeVehiculos.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearTiposdeVehiculos, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearTiposdeVehiculos.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearTiposDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeTiposDeVehiculo() {
        ini.setListaDeTiposDeVehiculos();

        fCrearTiposdeVehiculos.txtTipoVehiculo.setEnabled(false);
        
        for (CTiposDeVehiculos obj : ini.getListaDeTiposDeVehiculos()) {
            fila = new Object[3];
            fila[0] = obj.getIdTipoDeVehiculo();
            fila[1] = obj.getNombreTipoDeVehiculo();
            if (obj.getActivoTipoDeVehiculo()< 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            
            modelo = (DefaultTableModel) fCrearTiposdeVehiculos.tblTipoVehiculo.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearTiposdeVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroTipoVehiculo()) {
            fCrearTiposdeVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearTiposdeVehiculos.btnGrabar.setEnabled(false);
        } else {
            fCrearTiposdeVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearTiposdeVehiculos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearTiposdeVehiculos.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearTiposdeVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroTipoVehiculo()) {
            fCrearTiposdeVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearTiposdeVehiculos.btnGrabar.setEnabled(false);

        } else {
            fCrearTiposdeVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearTiposdeVehiculos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearTiposdeVehiculos.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearTiposdeVehiculos.iniciado = false;
        fCrearTiposdeVehiculos.tiposDevehiculos = ini.getListaDeTiposDeVehiculos().get(fCrearTiposdeVehiculos.indice);

        fCrearTiposdeVehiculos.txtTipoVehiculo.setText("");
        fCrearTiposdeVehiculos.txtTipoVehiculo.setEnabled(false);
        fCrearTiposdeVehiculos.actualizar = false;
        fCrearTiposdeVehiculos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearTiposdeVehiculos.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearTiposdeVehiculos.tblTipoVehiculo.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearTiposdeVehiculos.tblTipoVehiculo.getModel();

        for (CTiposDeVehiculos obj : ini.getListaDeTiposDeVehiculos()) {
            try {
                int filaTabla2 = fCrearTiposdeVehiculos.tblTipoVehiculo.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearTiposdeVehiculos.tblTipoVehiculo.setValueAt(obj.getIdTipoDeVehiculo(), filaTabla2, 0);  // item
                fCrearTiposdeVehiculos.tblTipoVehiculo.setValueAt(obj.getNombreTipoDeVehiculo(), filaTabla2, 1); // numero de factura
                if (obj.getActivoTipoDeVehiculo()== 1) {
                    fCrearTiposdeVehiculos.tblTipoVehiculo.setValueAt(true, filaTabla2, 2); //
                } else {
                    fCrearTiposdeVehiculos.tblTipoVehiculo.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearTiposDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroTipoVehiculo() {
        boolean valida = false;
        try {

            tipoVehiculo = new CTiposDeVehiculos(ini);
            tipoVehiculo.setNombreTipoDeVehiculo(fCrearTiposdeVehiculos.txtTipoVehiculo.getText().trim());
            //canal.setIdCanalDeVenta(fCrearTiposdeVehiculos.causalDeDevolucion.getIdCanalDeVenta());
            if (fCrearTiposdeVehiculos.chkActivo.isSelected()) {
                tipoVehiculo.setActivoTipoDeVehiculo(1);
            } else {
                tipoVehiculo.setActivoTipoDeVehiculo(0);
            }
            valida = tipoVehiculo.grabarTipoDeVehiculos();
            if (valida) {

                ini.setListaDeTiposDeVehiculos();
                JOptionPane.showInternalMessageDialog(fCrearTiposdeVehiculos, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearTiposdeVehiculos.btnNuevo.setEnabled(true);
                fCrearTiposdeVehiculos.jBtnNuevo.setEnabled(true);
                fCrearTiposdeVehiculos.btnGrabar.setEnabled(false);
                fCrearTiposdeVehiculos.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearTiposdeVehiculos.tblTipoVehiculo.getSelectedRow();
        fCrearTiposdeVehiculos.lblIdTipoVehiculo.setText("Id. Tipo vehiculo :  " + String.valueOf(fCrearTiposdeVehiculos.tblTipoVehiculo.getValueAt(row, 0)));
        
        fCrearTiposdeVehiculos.txtTipoVehiculo.setText(String.valueOf(fCrearTiposdeVehiculos.tblTipoVehiculo.getValueAt(row, 1)));
        
        if ((Boolean) fCrearTiposdeVehiculos.tblTipoVehiculo.getModel().getValueAt(row, 2)) {
            fCrearTiposdeVehiculos.chkActivo.setText("Tipo vehiculo  Activo");
            fCrearTiposdeVehiculos.chkActivo.setSelected(true);
        } else {
            fCrearTiposdeVehiculos.chkActivo.setText("Tipo vehiculo  no Activo");
            fCrearTiposdeVehiculos.chkActivo.setSelected(false);
        }
        fCrearTiposdeVehiculos.actualizar = true;
        fCrearTiposdeVehiculos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearTiposdeVehiculos.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroTipoVehiculo() {
        boolean valida = false;
        try {
            int row = fCrearTiposdeVehiculos.tblTipoVehiculo.getSelectedRow();
            fCrearTiposdeVehiculos.lblIdTipoVehiculo.setText("Id. Tipo vehiculo :  " + String.valueOf(fCrearTiposdeVehiculos.tblTipoVehiculo.getValueAt(row, 0)));
            tipoVehiculo = new CTiposDeVehiculos(ini);
            tipoVehiculo.setIdTipoDeVehiculo((Integer) fCrearTiposdeVehiculos.tblTipoVehiculo.getValueAt(row, 0));
            tipoVehiculo.setNombreTipoDeVehiculo(fCrearTiposdeVehiculos.txtTipoVehiculo.getText().trim());
            if (fCrearTiposdeVehiculos.chkActivo.isSelected()) {
                tipoVehiculo.setActivoTipoDeVehiculo(1);
            } else {
                tipoVehiculo.setActivoTipoDeVehiculo(0);
            }

            valida = tipoVehiculo.actualizarTipoDeVehiculos();

            if (valida) {
                ini.setListaDeTiposDeVehiculos();
                JOptionPane.showInternalMessageDialog(fCrearTiposdeVehiculos, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearTiposdeVehiculos.lblIdTipoVehiculo.setVisible(true);
                fCrearTiposdeVehiculos.lblIdTipoVehiculo.setText("Id. Tipo vehiculo :  " + tipoVehiculo.getIdTipoDeVehiculo());
                fCrearTiposdeVehiculos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearTiposdeVehiculos.btnNuevo.setEnabled(true);
                fCrearTiposdeVehiculos.jBtnNuevo.setEnabled(true);
                fCrearTiposdeVehiculos.btnGrabar.setEnabled(false);
                fCrearTiposdeVehiculos.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
