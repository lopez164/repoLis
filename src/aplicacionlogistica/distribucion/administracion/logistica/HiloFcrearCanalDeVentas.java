/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.administracion.logistica;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloFcrearCanalDeVentas implements Runnable {

    Inicio ini = null;
    FCrearCanalDeVentas fCrearCanalDeVentas = null;
    String caso;
    CCanalesDeVenta canal = null;
    int idCanalDeVenta = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearCanalDeVentas(Inicio ini) {
        ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fCrearCanalDeVentas
     * @param comando
     */
    public HiloFcrearCanalDeVentas(Inicio ini, FCrearCanalDeVentas fCrearCanalDeVentas, String comando) {
        this.ini = ini;
        this.fCrearCanalDeVentas = fCrearCanalDeVentas;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeCanales":
                        listaDeCanales();
                        break;

                    case "guardar":
                        if (fCrearCanalDeVentas.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearCanalDeVentas, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearCanalDeVentas.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearCanalDeVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeCanales() {
        ini.setListaDeCanalesDeVenta();

        fCrearCanalDeVentas.txtNombeCanalDeVentas.setEnabled(false);

        for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
            fila = new Object[3];
            fila[0] = obj.getIdCanalDeVenta();
            fila[1] = obj.getNombreCanalDeVenta();
            if (obj.getActivoCanal() < 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            
            modelo = (DefaultTableModel) fCrearCanalDeVentas.tblCanales.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearCanalDeVentas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroCanal()) {
            fCrearCanalDeVentas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearCanalDeVentas.btnGrabar.setEnabled(false);
        } else {
            fCrearCanalDeVentas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearCanalDeVentas, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearCanalDeVentas.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearCanalDeVentas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroCanal()) {
            fCrearCanalDeVentas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearCanalDeVentas.btnGrabar.setEnabled(false);

        } else {
            fCrearCanalDeVentas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearCanalDeVentas, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearCanalDeVentas.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearCanalDeVentas.iniciado = false;
        fCrearCanalDeVentas.canalDeVentas = ini.getListaDeCanalesDeVenta().get(fCrearCanalDeVentas.indice);

        fCrearCanalDeVentas.txtNombeCanalDeVentas.setText("");
        fCrearCanalDeVentas.txtNombeCanalDeVentas.setEnabled(false);
        fCrearCanalDeVentas.actualizar = false;
        fCrearCanalDeVentas.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearCanalDeVentas.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearCanalDeVentas.tblCanales.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearCanalDeVentas.tblCanales.getModel();

        for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
            try {
                int filaTabla2 = fCrearCanalDeVentas.tblCanales.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearCanalDeVentas.tblCanales.setValueAt(obj.getIdCanalDeVenta(), filaTabla2, 0);  // item
                fCrearCanalDeVentas.tblCanales.setValueAt(obj.getNombreCanalDeVenta(), filaTabla2, 1); // numero de factura
                if (obj.getActivoCanal() == 1) {
                    fCrearCanalDeVentas.tblCanales.setValueAt(true, filaTabla2, 2); //
                } else {
                    fCrearCanalDeVentas.tblCanales.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearCanalDeVentas.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroCanal() {
        boolean valida = false;
        try {

            canal = new CCanalesDeVenta(ini);
            canal.setNombreCanalDeVenta(fCrearCanalDeVentas.txtNombeCanalDeVentas.getText().trim());
            //canal.setIdCanalDeVenta(fCrearCanalDeVentas.canalDeVentas.getIdCanalDeVenta());
            if (fCrearCanalDeVentas.chkActivo.isSelected()) {
                canal.setActivoCanal(1);
            } else {
                canal.setActivoCanal(0);
            }
            valida = canal.grabarCanals();
            if (valida) {

                ini.setListaDeCanalesDeVenta();
                JOptionPane.showInternalMessageDialog(fCrearCanalDeVentas, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearCanalDeVentas.btnNuevo.setEnabled(true);
                fCrearCanalDeVentas.jBtnNuevo.setEnabled(true);
                fCrearCanalDeVentas.btnGrabar.setEnabled(false);
                fCrearCanalDeVentas.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCanalDeVentas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearCanalDeVentas.tblCanales.getSelectedRow();
        fCrearCanalDeVentas.lblCanalDeVentas.setText("Id. Canal de Venta : " + String.valueOf(fCrearCanalDeVentas.tblCanales.getValueAt(row, 0)));
        
        fCrearCanalDeVentas.txtNombeCanalDeVentas.setText(String.valueOf(fCrearCanalDeVentas.tblCanales.getValueAt(row, 1)));
        
        if ((Boolean) fCrearCanalDeVentas.tblCanales.getModel().getValueAt(row, 2)) {
            fCrearCanalDeVentas.chkActivo.setText("Canal Activo");
            fCrearCanalDeVentas.chkActivo.setSelected(true);
        } else {
            fCrearCanalDeVentas.chkActivo.setText("Canal no Activo");
            fCrearCanalDeVentas.chkActivo.setSelected(false);
        }
        fCrearCanalDeVentas.actualizar = true;
        fCrearCanalDeVentas.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearCanalDeVentas.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroCanal() {
        boolean valida = false;
        try {
            int row = fCrearCanalDeVentas.tblCanales.getSelectedRow();
            fCrearCanalDeVentas.lblCanalDeVentas.setText("Id. Canal de Venta : " + String.valueOf(fCrearCanalDeVentas.tblCanales.getValueAt(row, 0)));
            canal = new CCanalesDeVenta(ini);
            canal.setIdCanalDeVenta((Integer) fCrearCanalDeVentas.tblCanales.getValueAt(row, 0));
            canal.setNombreCanalDeVenta(fCrearCanalDeVentas.txtNombeCanalDeVentas.getText().trim());
            if (fCrearCanalDeVentas.chkActivo.isSelected()) {
                canal.setActivoCanal(1);
            } else {
                canal.setActivoCanal(0);
            }

            valida = canal.actualizarCanals();

            if (valida) {

                ini.setListaDeCanalesDeVenta();
                JOptionPane.showInternalMessageDialog(fCrearCanalDeVentas, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearCanalDeVentas.lblCanalDeVentas.setVisible(true);
                fCrearCanalDeVentas.lblCanalDeVentas.setText("Id. Canal de Venta : " + canal.getIdCanalDeVenta());
                fCrearCanalDeVentas.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearCanalDeVentas.btnNuevo.setEnabled(true);
                fCrearCanalDeVentas.jBtnNuevo.setEnabled(true);
                fCrearCanalDeVentas.btnGrabar.setEnabled(false);
                fCrearCanalDeVentas.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCanalDeVentas.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
