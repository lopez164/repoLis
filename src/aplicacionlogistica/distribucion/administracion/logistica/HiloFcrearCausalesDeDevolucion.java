/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.administracion.logistica;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CCausalesDeDevolucion;
import aplicacionlogistica.distribucion.objetos.CCausalesDeDevolucion;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloFcrearCausalesDeDevolucion implements Runnable {

    Inicio ini = null;
    FCrearCausalesDEDevolucion fCrearCausalesDEDevolucion = null;
    String caso;
    CCausalesDeDevolucion causal = null;
    int idCausalDeDevolucion = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearCausalesDeDevolucion(Inicio ini) {
        ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fCrearCausalesDEDevolucion
     * @param comando
     */
    public HiloFcrearCausalesDeDevolucion(Inicio ini, FCrearCausalesDEDevolucion fCrearCausalesDEDevolucion, String comando) {
        this.ini = ini;
        this.fCrearCausalesDEDevolucion = fCrearCausalesDEDevolucion;
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
                        if (fCrearCausalesDEDevolucion.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearCausalesDEDevolucion, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearCausalesDEDevolucion.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearCausalesDeDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeCausales() {
        ini.setListaDeCausalesDeDevolucion();

        fCrearCausalesDEDevolucion.txtNombeCausalDeDevolucion.setEnabled(false);
        
        for (CCausalesDeDevolucion obj : ini.getListaDeCausalesDeDevolucion()) {
            fila = new Object[3];
            fila[0] = obj.getIdCausalesDeDevolucion();
            fila[1] = obj.getNombreCausalesDeDevolucion();
            if (obj.getActivoCausalesDeDevolucion()< 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            
            modelo = (DefaultTableModel) fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearCausalesDEDevolucion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroCausalDevol()) {
            fCrearCausalesDEDevolucion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearCausalesDEDevolucion.btnGrabar.setEnabled(false);
        } else {
            fCrearCausalesDEDevolucion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearCausalesDEDevolucion, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearCausalesDEDevolucion.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearCausalesDEDevolucion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroCausalDevol()) {
            fCrearCausalesDEDevolucion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearCausalesDEDevolucion.btnGrabar.setEnabled(false);

        } else {
            fCrearCausalesDEDevolucion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearCausalesDEDevolucion, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearCausalesDEDevolucion.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearCausalesDEDevolucion.iniciado = false;
        fCrearCausalesDEDevolucion.causalDeDevolucion = ini.getListaDeCausalesDeDevolucion().get(fCrearCausalesDEDevolucion.indice);

        fCrearCausalesDEDevolucion.txtNombeCausalDeDevolucion.setText("");
        fCrearCausalesDEDevolucion.txtNombeCausalDeDevolucion.setEnabled(false);
        fCrearCausalesDEDevolucion.actualizar = false;
        fCrearCausalesDEDevolucion.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearCausalesDEDevolucion.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.getModel();

        for (CCausalesDeDevolucion obj : ini.getListaDeCausalesDeDevolucion()) {
            try {
                int filaTabla2 = fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.setValueAt(obj.getIdCausalesDeDevolucion(), filaTabla2, 0);  // item
                fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.setValueAt(obj.getNombreCausalesDeDevolucion(), filaTabla2, 1); // numero de factura
                if (obj.getActivoCausalesDeDevolucion()== 1) {
                    fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.setValueAt(true, filaTabla2, 2); //
                } else {
                    fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearCausalesDeDevolucion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroCausalDevol() {
        boolean valida = false;
        try {

            causal = new CCausalesDeDevolucion(ini);
            causal.setNombreCausalesDeDEvolucion(fCrearCausalesDEDevolucion.txtNombeCausalDeDevolucion.getText().trim());
            //canal.setIdCanalDeVenta(fCrearCausalesDEDevolucion.causalDeDevolucion.getIdCanalDeVenta());
            if (fCrearCausalesDEDevolucion.chkActivo.isSelected()) {
                causal.setActivoCausalesDeDEvolucion(1);
            } else {
                causal.setActivoCausalesDeDEvolucion(0);
            }
            valida = causal.grabarCausalDeDevolcuion();
            if (valida) {

                ini.setListaDeCausalesDeDevolucion();
                JOptionPane.showInternalMessageDialog(fCrearCausalesDEDevolucion, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearCausalesDEDevolucion.btnNuevo.setEnabled(true);
                fCrearCausalesDEDevolucion.jBtnNuevo.setEnabled(true);
                fCrearCausalesDEDevolucion.btnGrabar.setEnabled(false);
                fCrearCausalesDEDevolucion.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.getSelectedRow();
        fCrearCausalesDEDevolucion.lblCausalDeDevolucion.setText("Id. Causal  de Devol. : " + String.valueOf(fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.getValueAt(row, 0)));
        
        fCrearCausalesDEDevolucion.txtNombeCausalDeDevolucion.setText(String.valueOf(fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.getValueAt(row, 1)));
        
        if ((Boolean) fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.getModel().getValueAt(row, 2)) {
            fCrearCausalesDEDevolucion.chkActivo.setText("Causal Activo");
            fCrearCausalesDEDevolucion.chkActivo.setSelected(true);
        } else {
            fCrearCausalesDEDevolucion.chkActivo.setText("Causal no Activo");
            fCrearCausalesDEDevolucion.chkActivo.setSelected(false);
        }
        fCrearCausalesDEDevolucion.actualizar = true;
        fCrearCausalesDEDevolucion.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearCausalesDEDevolucion.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroCausalDevol() {
        boolean valida = false;
        try {
            int row = fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.getSelectedRow();
            fCrearCausalesDEDevolucion.lblCausalDeDevolucion.setText("Id. Causal  de Devol. : " + String.valueOf(fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.getValueAt(row, 0)));
            causal = new CCausalesDeDevolucion(ini);
            causal.setIdCausalesDeDevolucion((Integer) fCrearCausalesDEDevolucion.tblCausalesDeDEvolcuion.getValueAt(row, 0));
            causal.setNombreCausalesDeDEvolucion(fCrearCausalesDEDevolucion.txtNombeCausalDeDevolucion.getText().trim());
            if (fCrearCausalesDEDevolucion.chkActivo.isSelected()) {
                causal.setActivoCausalesDeDEvolucion(1);
            } else {
                causal.setActivoCausalesDeDEvolucion(0);
            }

            valida = causal.actualizarCausalDeDevolucion();

            if (valida) {

                ini.setListaDeCausalesDeDevolucion();
                JOptionPane.showInternalMessageDialog(fCrearCausalesDEDevolucion, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearCausalesDEDevolucion.lblCausalDeDevolucion.setVisible(true);
                fCrearCausalesDEDevolucion.lblCausalDeDevolucion.setText("Id. Causal  de Devol. : " + causal.getIdCausalesDeDevolucion());
                fCrearCausalesDEDevolucion.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearCausalesDEDevolucion.btnNuevo.setEnabled(true);
                fCrearCausalesDEDevolucion.jBtnNuevo.setEnabled(true);
                fCrearCausalesDEDevolucion.btnGrabar.setEnabled(false);
                fCrearCausalesDEDevolucion.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
