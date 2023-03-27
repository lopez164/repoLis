/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.administracion.logistica;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CCausalesDeDevolucion;
import aplicacionlogistica.distribucion.objetos.CEstadosManifiestos;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloFcrearEstadosManifiestos implements Runnable {

    Inicio ini = null;
    FCrearEstadosManifiestos fCrearEstadosManifiestos = null;
    String caso;
    CEstadosManifiestos estadoMan = null;
    int idCausalDeDevolucion = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearEstadosManifiestos(Inicio ini) {
        ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fCrearEstadosManifiestos
     * @param comando
     */
    public HiloFcrearEstadosManifiestos(Inicio ini, FCrearEstadosManifiestos fCrearEstadosManifiestos, String comando) {
        this.ini = ini;
        this.fCrearEstadosManifiestos = fCrearEstadosManifiestos;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeCausales":
                        listaDeCausales();
                        break;

                    case "guardar":
                        if (fCrearEstadosManifiestos.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearEstadosManifiestos, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearEstadosManifiestos.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearEstadosManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeCausales() {
        ini.setListaDeEstadosManifiestos();

        fCrearEstadosManifiestos.txtNombeCausalDeDevolucion.setEnabled(false);
        
        for (CEstadosManifiestos obj : ini.getListaDeEstadosManifiestos()) {
            fila = new Object[3];
            fila[0] = obj.getIdEstadoManifiesto();
            fila[1] = obj.getNombreDeEstadoManifiesto();
            if (obj.getActivoEstadoManifiesto()< 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            
            modelo = (DefaultTableModel) fCrearEstadosManifiestos.tblEstadosVehiculos.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearEstadosManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroEstadoMan()) {
            fCrearEstadosManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearEstadosManifiestos.btnGrabar.setEnabled(false);
        } else {
            fCrearEstadosManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearEstadosManifiestos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearEstadosManifiestos.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearEstadosManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroEstadoMan()) {
            fCrearEstadosManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearEstadosManifiestos.btnGrabar.setEnabled(false);

        } else {
            fCrearEstadosManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearEstadosManifiestos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearEstadosManifiestos.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearEstadosManifiestos.iniciado = false;
        fCrearEstadosManifiestos.estadoManifiesto = ini.getListaDeEstadosManifiestos().get(fCrearEstadosManifiestos.indice);

        fCrearEstadosManifiestos.txtNombeCausalDeDevolucion.setText("");
        fCrearEstadosManifiestos.txtNombeCausalDeDevolucion.setEnabled(false);
        fCrearEstadosManifiestos.actualizar = false;
        fCrearEstadosManifiestos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearEstadosManifiestos.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearEstadosManifiestos.tblEstadosVehiculos.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearEstadosManifiestos.tblEstadosVehiculos.getModel();

        for (CEstadosManifiestos obj : ini.getListaDeEstadosManifiestos()) {
            try {
                int filaTabla2 = fCrearEstadosManifiestos.tblEstadosVehiculos.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearEstadosManifiestos.tblEstadosVehiculos.setValueAt(obj.getIdEstadoManifiesto(), filaTabla2, 0);  // item
                fCrearEstadosManifiestos.tblEstadosVehiculos.setValueAt(obj.getNombreDeEstadoManifiesto(), filaTabla2, 1); // numero de factura
                if (obj.getActivoEstadoManifiesto()== 1) {
                    fCrearEstadosManifiestos.tblEstadosVehiculos.setValueAt(true, filaTabla2, 2); //
                } else {
                    fCrearEstadosManifiestos.tblEstadosVehiculos.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearEstadosManifiestos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroEstadoMan() {
        boolean valida = false;
        try {

            estadoMan = new CEstadosManifiestos(ini);
            estadoMan.setNombreDeEstadoManifiesto(fCrearEstadosManifiestos.txtNombeCausalDeDevolucion.getText().trim());
            //canal.setIdCanalDeVenta(fCrearEstadosManifiestos.causalDeDevolucion.getIdCanalDeVenta());
            if (fCrearEstadosManifiestos.chkActivo.isSelected()) {
                estadoMan.setActivoEstadoManifiesto(1);
            } else {
                estadoMan.setActivoEstadoManifiesto(0);
            }
            valida = estadoMan.grabarEstadosManifiestos();
            if (valida) {

                ini.setListaDeEstadosManifiestos();
                JOptionPane.showInternalMessageDialog(fCrearEstadosManifiestos, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearEstadosManifiestos.btnNuevo.setEnabled(true);
                fCrearEstadosManifiestos.jBtnNuevo.setEnabled(true);
                fCrearEstadosManifiestos.btnGrabar.setEnabled(false);
                fCrearEstadosManifiestos.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearEstadosManifiestos.tblEstadosVehiculos.getSelectedRow();
        fCrearEstadosManifiestos.lblEstadoManifieto.setText("Id. Estado de Man  : " + String.valueOf(fCrearEstadosManifiestos.tblEstadosVehiculos.getValueAt(row, 0)));
        
        fCrearEstadosManifiestos.txtNombeCausalDeDevolucion.setText(String.valueOf(fCrearEstadosManifiestos.tblEstadosVehiculos.getValueAt(row, 1)));
        
        if ((Boolean) fCrearEstadosManifiestos.tblEstadosVehiculos.getModel().getValueAt(row, 2)) {
            fCrearEstadosManifiestos.chkActivo.setText("Estadon Man.  Activo");
            fCrearEstadosManifiestos.chkActivo.setSelected(true);
        } else {
            fCrearEstadosManifiestos.chkActivo.setText("Estadon Man.  no Activo");
            fCrearEstadosManifiestos.chkActivo.setSelected(false);
        }
        fCrearEstadosManifiestos.actualizar = true;
        fCrearEstadosManifiestos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearEstadosManifiestos.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroEstadoMan() {
        boolean valida = false;
        try {
            int row = fCrearEstadosManifiestos.tblEstadosVehiculos.getSelectedRow();
            fCrearEstadosManifiestos.lblEstadoManifieto.setText("Id. Estado de Man  : " + String.valueOf(fCrearEstadosManifiestos.tblEstadosVehiculos.getValueAt(row, 0)));
            estadoMan = new CEstadosManifiestos(ini);
            estadoMan.setIdEstadoManifiesto((Integer) fCrearEstadosManifiestos.tblEstadosVehiculos.getValueAt(row, 0));
            estadoMan.setNombreDeEstadoManifiesto(fCrearEstadosManifiestos.txtNombeCausalDeDevolucion.getText().trim());
            if (fCrearEstadosManifiestos.chkActivo.isSelected()) {
                estadoMan.setActivoEstadoManifiesto(1);
            } else {
                estadoMan.setActivoEstadoManifiesto(0);
            }

            valida = estadoMan.actualizarEstadosManifiestos();

            if (valida) {
                ini.setListaDeEstadosManifiestos();
                JOptionPane.showInternalMessageDialog(fCrearEstadosManifiestos, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearEstadosManifiestos.lblEstadoManifieto.setVisible(true);
                fCrearEstadosManifiestos.lblEstadoManifieto.setText("Id. Estado de Man  : " + estadoMan.getIdCausalesDeDevolucion(caso));
                fCrearEstadosManifiestos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearEstadosManifiestos.btnNuevo.setEnabled(true);
                fCrearEstadosManifiestos.jBtnNuevo.setEnabled(true);
                fCrearEstadosManifiestos.btnGrabar.setEnabled(false);
                fCrearEstadosManifiestos.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
