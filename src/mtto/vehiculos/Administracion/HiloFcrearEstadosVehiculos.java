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
public class HiloFcrearEstadosVehiculos implements Runnable {

    Inicio ini = null;
    FCrearEstadosVehiculos fCrearEstadosVehiculos = null;
    String caso;
    CTiposEstadosVehiculos estadoVeh = null;
    int idTipoEstadoVeh = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearEstadosVehiculos(Inicio ini) {
        ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fCrearEstadosManifiestos
     * @param comando
     */
    public HiloFcrearEstadosVehiculos(Inicio ini,  FCrearEstadosVehiculos fCrearEstadosVehiculos, String comando) {
        this.ini = ini;
        this.fCrearEstadosVehiculos = fCrearEstadosVehiculos;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeEstadosVehiculos":
                        listaDeEstadosVeh();
                        break;

                    case "guardar":
                        if (fCrearEstadosVehiculos.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearEstadosVehiculos, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearEstadosVehiculos.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearEstadosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeEstadosVeh() {
        ini.setListaEstadosVehiculos();

        fCrearEstadosVehiculos.txtEstadoVehiculo.setEnabled(false);
        
        for (CTiposEstadosVehiculos obj : ini.getListaEstadosVehiculos()) {
            fila = new Object[3];
            fila[0] = obj.getIdtipoDeEstadosVehiculo();
            fila[1] = obj.getNombreEstadoVehiculo();
            if (obj.getActivoEstadoVehiculo()< 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            
            modelo = (DefaultTableModel) fCrearEstadosVehiculos.tblEstadosVehiculos.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearEstadosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroEstadoMan()) {
            fCrearEstadosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearEstadosVehiculos.btnGrabar.setEnabled(false);
        } else {
            fCrearEstadosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearEstadosVehiculos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearEstadosVehiculos.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearEstadosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroEstadoMan()) {
            fCrearEstadosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearEstadosVehiculos.btnGrabar.setEnabled(false);

        } else {
            fCrearEstadosVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearEstadosVehiculos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearEstadosVehiculos.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearEstadosVehiculos.iniciado = false;
        fCrearEstadosVehiculos.estadosVehiculos = ini.getListaEstadosVehiculos().get(fCrearEstadosVehiculos.indice);

        fCrearEstadosVehiculos.txtEstadoVehiculo.setText("");
        fCrearEstadosVehiculos.txtEstadoVehiculo.setEnabled(false);
        fCrearEstadosVehiculos.actualizar = false;
        fCrearEstadosVehiculos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearEstadosVehiculos.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearEstadosVehiculos.tblEstadosVehiculos.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearEstadosVehiculos.tblEstadosVehiculos.getModel();

        for (CTiposEstadosVehiculos obj : ini.getListaEstadosVehiculos()) {
            try {
                int filaTabla2 = fCrearEstadosVehiculos.tblEstadosVehiculos.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearEstadosVehiculos.tblEstadosVehiculos.setValueAt(obj.getIdtipoDeEstadosVehiculo(), filaTabla2, 0);  // item
                fCrearEstadosVehiculos.tblEstadosVehiculos.setValueAt(obj.getNombreEstadoVehiculo(), filaTabla2, 1); // numero de factura
                if (obj.getActivoEstadoVehiculo()== 1) {
                    fCrearEstadosVehiculos.tblEstadosVehiculos.setValueAt(true, filaTabla2, 2); //
                } else {
                    fCrearEstadosVehiculos.tblEstadosVehiculos.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearEstadosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroEstadoMan() {
        boolean valida = false;
        try {

            estadoVeh = new CTiposEstadosVehiculos(ini);
            estadoVeh.setNombreEstadoVehiculo(fCrearEstadosVehiculos.txtEstadoVehiculo.getText().trim());
            //canal.setIdCanalDeVenta(fCrearEstadosVehiculos.causalDeDevolucion.getIdCanalDeVenta());
            if (fCrearEstadosVehiculos.chkActivo.isSelected()) {
                estadoVeh.setActivoEstadoVehiculo(1);
            } else {
                estadoVeh.setActivoEstadoVehiculo(0);
            }
            valida = estadoVeh.grabarEstadoVehiculos();
            if (valida) {

                ini.setListaEstadosVehiculos();
                JOptionPane.showInternalMessageDialog(fCrearEstadosVehiculos, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearEstadosVehiculos.btnNuevo.setEnabled(true);
                fCrearEstadosVehiculos.jBtnNuevo.setEnabled(true);
                fCrearEstadosVehiculos.btnGrabar.setEnabled(false);
                fCrearEstadosVehiculos.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearEstadosVehiculos.tblEstadosVehiculos.getSelectedRow();
        fCrearEstadosVehiculos.lblEstadosVehiculos.setText("Id. Estado Vehiculo :  " + String.valueOf(fCrearEstadosVehiculos.tblEstadosVehiculos.getValueAt(row, 0)));
        
        fCrearEstadosVehiculos.txtEstadoVehiculo.setText(String.valueOf(fCrearEstadosVehiculos.tblEstadosVehiculos.getValueAt(row, 1)));
        
        if ((Boolean) fCrearEstadosVehiculos.tblEstadosVehiculos.getModel().getValueAt(row, 2)) {
            fCrearEstadosVehiculos.chkActivo.setText("Estadon Man.  Activo");
            fCrearEstadosVehiculos.chkActivo.setSelected(true);
        } else {
            fCrearEstadosVehiculos.chkActivo.setText("Estadon Man.  no Activo");
            fCrearEstadosVehiculos.chkActivo.setSelected(false);
        }
        fCrearEstadosVehiculos.actualizar = true;
        fCrearEstadosVehiculos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearEstadosVehiculos.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroEstadoMan() {
        boolean valida = false;
        try {
            int row = fCrearEstadosVehiculos.tblEstadosVehiculos.getSelectedRow();
            fCrearEstadosVehiculos.lblEstadosVehiculos.setText("Id. Estado Vehiculo :  " + String.valueOf(fCrearEstadosVehiculos.tblEstadosVehiculos.getValueAt(row, 0)));
            estadoVeh = new CTiposEstadosVehiculos(ini);
            estadoVeh.setIdtipoDeEstadosVehiculo((Integer) fCrearEstadosVehiculos.tblEstadosVehiculos.getValueAt(row, 0));
            estadoVeh.setNombreEstadoVehiculo(fCrearEstadosVehiculos.txtEstadoVehiculo.getText().trim());
            if (fCrearEstadosVehiculos.chkActivo.isSelected()) {
                estadoVeh.setActivoEstadoVehiculo(1);
            } else {
                estadoVeh.setActivoEstadoVehiculo(0);
            }

            valida = estadoVeh.actualizarEstadoVehiculos();

            if (valida) {
                ini.setListaEstadosVehiculos();
                JOptionPane.showInternalMessageDialog(fCrearEstadosVehiculos, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearEstadosVehiculos.lblEstadosVehiculos.setVisible(true);
                fCrearEstadosVehiculos.lblEstadosVehiculos.setText("Id. Estado Vehiculo :  " + estadoVeh.getIdtipoDeEstadosVehiculo());
                fCrearEstadosVehiculos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearEstadosVehiculos.btnNuevo.setEnabled(true);
                fCrearEstadosVehiculos.jBtnNuevo.setEnabled(true);
                fCrearEstadosVehiculos.btnGrabar.setEnabled(false);
                fCrearEstadosVehiculos.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
