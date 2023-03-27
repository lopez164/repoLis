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
public class HiloFcrearTiposDeServicio implements Runnable {

    Inicio ini = null;
    FCrearTiposdeServicios fCrearTiposdeServicios = null;
    String caso;
    CTiposDeServicio tipoServicio = null;
    int idTipoervicio = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearTiposDeServicio(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param FCrearTiposdeServicios
     * @param comando
     */
    public HiloFcrearTiposDeServicio(Inicio ini,  FCrearTiposdeServicios FCrearTiposdeServicios, String comando) {
        this.ini = ini;
        this.fCrearTiposdeServicios = FCrearTiposdeServicios;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeTiposDeServicio":
                          listaDeTiposDeServicio();
                          break;

                    case "guardar":
                        if (fCrearTiposdeServicios.actualizar) {
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
                        JOptionPane.showInternalMessageDialog(fCrearTiposdeServicios, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearTiposdeServicios.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearTiposDeServicio.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeTiposDeServicio() {
        ini.setListaDeTiposDeServicios();

        fCrearTiposdeServicios.txtTipoServicio.setEnabled(false);
        
        for (CTiposDeServicio obj : ini.getListaDeTiposDeServicios()) {
            fila = new Object[3];
            fila[0] = obj.getIdTipoDeServicio();
            fila[1] = obj.getNombreTipoDeServicio();
            if (obj.getActivoTipoDeServicio()< 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }
            
            modelo = (DefaultTableModel) fCrearTiposdeServicios.tblTipoServicio.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {
        fCrearTiposdeServicios.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (guardarRegistroTipoServicio()) {
            fCrearTiposdeServicios.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearTiposdeServicios.btnGrabar.setEnabled(false);
        } else {
            fCrearTiposdeServicios.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearTiposdeServicios, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearTiposdeServicios.btnGrabar.setEnabled(true);
        }
    }

    private void actualizar() {
        fCrearTiposdeServicios.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

        if (actualizarRegistroTipoServicio()) {
            fCrearTiposdeServicios.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fCrearTiposdeServicios.btnGrabar.setEnabled(false);

        } else {
            fCrearTiposdeServicios.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fCrearTiposdeServicios, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            fCrearTiposdeServicios.btnGrabar.setEnabled(true);
        }

    }

    private void llenarJtable() {
        fCrearTiposdeServicios.iniciado = false;
        fCrearTiposdeServicios.tiposDeServicios = ini.getListaDeTiposDeServicios().get(fCrearTiposdeServicios.indice);

        fCrearTiposdeServicios.txtTipoServicio.setText("");
        fCrearTiposdeServicios.txtTipoServicio.setEnabled(false);
        fCrearTiposdeServicios.actualizar = false;
        fCrearTiposdeServicios.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearTiposdeServicios.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearTiposdeServicios.tblTipoServicio.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearTiposdeServicios.tblTipoServicio.getModel();

        for (CTiposDeServicio obj : ini.getListaDeTiposDeServicios()) {
            try {
                int filaTabla2 = fCrearTiposdeServicios.tblTipoServicio.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearTiposdeServicios.tblTipoServicio.setValueAt(obj.getIdTipoDeServicio(), filaTabla2, 0);  // item
                fCrearTiposdeServicios.tblTipoServicio.setValueAt(obj.getNombreTipoDeServicio(), filaTabla2, 1); // numero de factura
                if (obj.getActivoTipoDeServicio()== 1) {
                    fCrearTiposdeServicios.tblTipoServicio.setValueAt(true, filaTabla2, 2); //
                } else {
                    fCrearTiposdeServicios.tblTipoServicio.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearTiposDeServicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public boolean guardarRegistroTipoServicio() {
        boolean valida = false;
        try {

            tipoServicio = new CTiposDeServicio(ini);
            tipoServicio.setNombreTipoDeServicio(fCrearTiposdeServicios.txtTipoServicio.getText().trim());
            //canal.setIdCanalDeVenta(fCrearTiposdeServicios.causalDeDevolucion.getIdCanalDeVenta());
            if (fCrearTiposdeServicios.chkActivo.isSelected()) {
                tipoServicio.setActivoTipoDeServicio(1);
            } else {
                tipoServicio.setActivoTipoDeServicio(0);
            }
            valida = tipoServicio.grabarTipoDeServicios();
            if (valida) {

                ini.setListaDeTiposDeServicios();
                JOptionPane.showInternalMessageDialog(fCrearTiposdeServicios, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                fCrearTiposdeServicios.btnNuevo.setEnabled(true);
                fCrearTiposdeServicios.jBtnNuevo.setEnabled(true);
                fCrearTiposdeServicios.btnGrabar.setEnabled(false);
                fCrearTiposdeServicios.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valida;
    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearTiposdeServicios.tblTipoServicio.getSelectedRow();
        fCrearTiposdeServicios.lblIdTipoServicio.setText("Id. Tipo Servicio :  " + String.valueOf(fCrearTiposdeServicios.tblTipoServicio.getValueAt(row, 0)));
        
        fCrearTiposdeServicios.txtTipoServicio.setText(String.valueOf(fCrearTiposdeServicios.tblTipoServicio.getValueAt(row, 1)));
        
        if ((Boolean) fCrearTiposdeServicios.tblTipoServicio.getModel().getValueAt(row, 2)) {
            fCrearTiposdeServicios.chkActivo.setText("Tipo Servicio  Activo");
            fCrearTiposdeServicios.chkActivo.setSelected(true);
        } else {
            fCrearTiposdeServicios.chkActivo.setText("Tipo Servicio  no Activo");
            fCrearTiposdeServicios.chkActivo.setSelected(false);
        }
        fCrearTiposdeServicios.actualizar = true;
        fCrearTiposdeServicios.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearTiposdeServicios.btnNuevo.setText("Actualizar");

    }

    private boolean actualizarRegistroTipoServicio() {
        boolean valida = false;
        try {
            int row = fCrearTiposdeServicios.tblTipoServicio.getSelectedRow();
            fCrearTiposdeServicios.lblIdTipoServicio.setText("Id. Tipo Servicio :  " + String.valueOf(fCrearTiposdeServicios.tblTipoServicio.getValueAt(row, 0)));
            tipoServicio = new CTiposDeServicio(ini);
            tipoServicio.setIdTipoDeServicio((Integer) fCrearTiposdeServicios.tblTipoServicio.getValueAt(row, 0));
            tipoServicio.setNombreTipoDeServicio(fCrearTiposdeServicios.txtTipoServicio.getText().trim());
            if (fCrearTiposdeServicios.chkActivo.isSelected()) {
                tipoServicio.setActivoTipoDeServicio(1);
            } else {
                tipoServicio.setActivoTipoDeServicio(0);
            }

            valida = tipoServicio.actualizarTipoDeServicios();

            if (valida) {
                ini.setListaDeTiposDeServicios();
                JOptionPane.showInternalMessageDialog(fCrearTiposdeServicios, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);

                fCrearTiposdeServicios.lblIdTipoServicio.setVisible(true);
                fCrearTiposdeServicios.lblIdTipoServicio.setText("Id. Tipo Servicio :  " + tipoServicio.getIdTipoDeServicio());
                fCrearTiposdeServicios.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
                fCrearTiposdeServicios.btnNuevo.setEnabled(true);
                fCrearTiposdeServicios.jBtnNuevo.setEnabled(true);
                fCrearTiposdeServicios.btnGrabar.setEnabled(false);
                fCrearTiposdeServicios.jBtnGrabar.setEnabled(false);
                llenarJtable();
            }
        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);

        }
        return valida;
    }

}
