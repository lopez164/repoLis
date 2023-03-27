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
public class HiloFcrearTiposDeCarroceria implements Runnable {

    Inicio ini = null;
    FCrearTiposCarrocerias fCrearTiposCarrocerias = null;
    String caso;
    CTiposDeCarrocerias tipoCarroceria = null;
    int idTipoCarroceria = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearTiposDeCarroceria(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fCrearTiposCarrocerias
     * @param comando
     */
    public HiloFcrearTiposDeCarroceria(Inicio ini,  FCrearTiposCarrocerias fCrearTiposCarrocerias, String comando) {
        this.ini = ini;
        this.fCrearTiposCarrocerias = fCrearTiposCarrocerias;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeTiposDeCarroceria":
                        listaDeTiposDeCarroceria();
                        break;

                    case "guardar":
                        if (fCrearTiposCarrocerias.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearTiposCarrocerias, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearTiposCarrocerias.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearTiposDeCarroceria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeTiposDeCarroceria() {
        ini.setListaDeTiposDeCarrocerias();

        fCrearTiposCarrocerias.txtTiposCarrocerias.setEnabled(false);
        
        for (CTiposDeCarrocerias obj : ini.getListaDeTiposDeCarrocerias()) {
            fila = new Object[3];
            fila[0] = obj.getIdCarroceria();
            fila[1] = obj.getNombreCarroceria();
            if (obj.getActivoCarroceria()< 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            
            modelo = (DefaultTableModel) fCrearTiposCarrocerias.tblTipoDeCarroceria.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearTiposCarrocerias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroTipoCarroceria()) {
            fCrearTiposCarrocerias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearTiposCarrocerias.btnGrabar.setEnabled(false);
        } else {
            fCrearTiposCarrocerias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearTiposCarrocerias, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearTiposCarrocerias.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearTiposCarrocerias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroTipoCarroceria()) {
            fCrearTiposCarrocerias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearTiposCarrocerias.btnGrabar.setEnabled(false);

        } else {
            fCrearTiposCarrocerias.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearTiposCarrocerias, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearTiposCarrocerias.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearTiposCarrocerias.iniciado = false;
        fCrearTiposCarrocerias.tiposCarrocerias = ini.getListaDeTiposDeCarrocerias().get(fCrearTiposCarrocerias.indice);

        fCrearTiposCarrocerias.txtTiposCarrocerias.setText("");
        fCrearTiposCarrocerias.txtTiposCarrocerias.setEnabled(false);
        fCrearTiposCarrocerias.actualizar = false;
        fCrearTiposCarrocerias.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearTiposCarrocerias.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearTiposCarrocerias.tblTipoDeCarroceria.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearTiposCarrocerias.tblTipoDeCarroceria.getModel();

        for (CTiposDeCarrocerias obj : ini.getListaDeTiposDeCarrocerias()) {
            try {
                int filaTabla2 = fCrearTiposCarrocerias.tblTipoDeCarroceria.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearTiposCarrocerias.tblTipoDeCarroceria.setValueAt(obj.getIdCarroceria(), filaTabla2, 0);  // item
                fCrearTiposCarrocerias.tblTipoDeCarroceria.setValueAt(obj.getNombreCarroceria(), filaTabla2, 1); // numero de factura
                if (obj.getActivoCarroceria()== 1) {
                    fCrearTiposCarrocerias.tblTipoDeCarroceria.setValueAt(true, filaTabla2, 2); //
                } else {
                    fCrearTiposCarrocerias.tblTipoDeCarroceria.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearTiposDeCarroceria.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroTipoCarroceria() {
        boolean valida = false;
        try {

            tipoCarroceria = new CTiposDeCarrocerias(ini);
            tipoCarroceria.setNombreCarroceria(fCrearTiposCarrocerias.txtTiposCarrocerias.getText().trim());
            //canal.setIdCanalDeVenta(fCrearTiposCarrocerias.causalDeDevolucion.getIdCanalDeVenta());
            if (fCrearTiposCarrocerias.chkActivo.isSelected()) {
                tipoCarroceria.setActivoCarroceria(1);
            } else {
                tipoCarroceria.setActivoCarroceria(0);
            }
            valida = tipoCarroceria.grabarCarrocerias();
            if (valida) {

                ini.setListaDeTiposDeCarrocerias();
                JOptionPane.showInternalMessageDialog(fCrearTiposCarrocerias, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearTiposCarrocerias.btnNuevo.setEnabled(true);
                fCrearTiposCarrocerias.jBtnNuevo.setEnabled(true);
                fCrearTiposCarrocerias.btnGrabar.setEnabled(false);
                fCrearTiposCarrocerias.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearTiposCarrocerias.tblTipoDeCarroceria.getSelectedRow();
        fCrearTiposCarrocerias.lblidTipoCarroceria.setText("Id. Tipo Carroceria :  " + String.valueOf(fCrearTiposCarrocerias.tblTipoDeCarroceria.getValueAt(row, 0)));
        
        fCrearTiposCarrocerias.txtTiposCarrocerias.setText(String.valueOf(fCrearTiposCarrocerias.tblTipoDeCarroceria.getValueAt(row, 1)));
        
        if ((Boolean) fCrearTiposCarrocerias.tblTipoDeCarroceria.getModel().getValueAt(row, 2)) {
            fCrearTiposCarrocerias.chkActivo.setText("Tipo Carroceria  Activo");
            fCrearTiposCarrocerias.chkActivo.setSelected(true);
        } else {
            fCrearTiposCarrocerias.chkActivo.setText("Tipo Carroceria  no Activo");
            fCrearTiposCarrocerias.chkActivo.setSelected(false);
        }
        fCrearTiposCarrocerias.actualizar = true;
        fCrearTiposCarrocerias.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearTiposCarrocerias.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroTipoCarroceria() {
        boolean valida = false;
        try {
            int row = fCrearTiposCarrocerias.tblTipoDeCarroceria.getSelectedRow();
            fCrearTiposCarrocerias.lblidTipoCarroceria.setText("Id. Tipo Carroceria :  " + String.valueOf(fCrearTiposCarrocerias.tblTipoDeCarroceria.getValueAt(row, 0)));
            tipoCarroceria = new CTiposDeCarrocerias(ini);
            tipoCarroceria.setIdCarroceria((Integer) fCrearTiposCarrocerias.tblTipoDeCarroceria.getValueAt(row, 0));
            tipoCarroceria.setNombreCarroceria(fCrearTiposCarrocerias.txtTiposCarrocerias.getText().trim());
            if (fCrearTiposCarrocerias.chkActivo.isSelected()) {
                tipoCarroceria.setActivoCarroceria(1);
            } else {
                tipoCarroceria.setActivoCarroceria(0);
            }

            valida = tipoCarroceria.actualizarCarrocerias(row);

            if (valida) {
                ini.setListaDeTiposDeCarrocerias();
                JOptionPane.showInternalMessageDialog(fCrearTiposCarrocerias, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearTiposCarrocerias.lblidTipoCarroceria.setVisible(true);
                fCrearTiposCarrocerias.lblidTipoCarroceria.setText("Id. Tipo Carroceria :  " + tipoCarroceria.getIdCarroceria());
                fCrearTiposCarrocerias.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearTiposCarrocerias.btnNuevo.setEnabled(true);
                fCrearTiposCarrocerias.jBtnNuevo.setEnabled(true);
                fCrearTiposCarrocerias.btnGrabar.setEnabled(false);
                fCrearTiposCarrocerias.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
