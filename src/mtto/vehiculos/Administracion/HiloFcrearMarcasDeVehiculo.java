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
public class HiloFcrearMarcasDeVehiculo implements Runnable {

    Inicio ini = null;
    FCrearMarcasVehiculos fCrearMarcasVehiculos = null;
    String caso;
    //CMarcasDeVehiculos marcaVehiculo = null;
    int idMarcaVehiculo = 0;
    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFcrearMarcasDeVehiculo(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param FCrearMarcasVehiculos
     * @param comando
     */
    public HiloFcrearMarcasDeVehiculo(Inicio ini, FCrearMarcasVehiculos FCrearMarcasVehiculos, String comando) {
        this.ini = ini;
        this.fCrearMarcasVehiculos = FCrearMarcasVehiculos;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "listaDeMarcasVehiculos":
                        listaDeMarcasVehiculos();
                        break;

                    case "guardar":
                        guardar();
                        break;
                    case "llenarJtable":
                        llenarJtable();
                        break;
                    case "seleccionarFila":
                        seleccionarFila();
                        break;

                    default:
                        JOptionPane.showInternalMessageDialog(fCrearMarcasVehiculos, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
            /*esta variable controla los eventos del combobox que contiene las
            marcas de los vehiculos, no se puede move o modificar de lo contrario
            se va a duplicar la informacion de la lineas de los vehiculos en la 
            tabla correspondiente. */
            // fCrearMarcasVehiculos.iniciado=true;

        } catch (Exception ex) {
            Logger.getLogger(HiloFcrearMarcasDeVehiculo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void listaDeMarcasVehiculos() {
        ini.setListaDeMarcasDeVehiculos();

        fCrearMarcasVehiculos.txtMarcaVehiculo.setEnabled(false);

        for (CMarcasDeVehiculos obj : ini.getListaDeMarcasDeVehiculos()) {
            fila = new Object[3];
            fila[0] = obj.getIdMarcaDeVehiculo();
            fila[1] = obj.getNombreMarcaDeVehiculos();
            if (obj.getActivoMarcaDeVehiculos() < 1) {
                fila[2] = false;
            } else {
                fila[2] = true;
            }

            modelo = (DefaultTableModel) fCrearMarcasVehiculos.tblMarcasVehiculos.getModel();
            modelo.addRow(fila);
        }
    }

    private void guardar() {

        //boolean valida = false;
        try {
            fCrearMarcasVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N

            if (fCrearMarcasVehiculos.marcaVehiculo == null) {
                fCrearMarcasVehiculos.marcaVehiculo = new CMarcasDeVehiculos(ini);
            }

            fCrearMarcasVehiculos.marcaVehiculo.setNombreMarcaDeVehiculos(fCrearMarcasVehiculos.txtMarcaVehiculo.getText().trim());
            if (fCrearMarcasVehiculos.chkActivo.isSelected()) {
                fCrearMarcasVehiculos.marcaVehiculo.setActivoMarcaDeVehiculos(1);
            } else {
                fCrearMarcasVehiculos.marcaVehiculo.setActivoMarcaDeVehiculos(0);
            }

            if (fCrearMarcasVehiculos.actualizar) {

                 if (fCrearMarcasVehiculos.marcaVehiculo.actualizarMarcaDeVehiculoss()) {

                    fCrearMarcasVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                    fCrearMarcasVehiculos.btnGrabar.setEnabled(false);
                    ini.setListaDeMarcasDeVehiculos();
                    JOptionPane.showInternalMessageDialog(fCrearMarcasVehiculos, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                    fCrearMarcasVehiculos.btnNuevo.setEnabled(true);
                    fCrearMarcasVehiculos.jBtnNuevo.setEnabled(true);
                    fCrearMarcasVehiculos.btnGrabar.setEnabled(false);
                    fCrearMarcasVehiculos.jBtnGrabar.setEnabled(false);
                    llenarJtable();
                    
                } else {
                    fCrearMarcasVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                    JOptionPane.showInternalMessageDialog(fCrearMarcasVehiculos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
                    fCrearMarcasVehiculos.btnGrabar.setEnabled(true);
                }
                
            } else {
                if (fCrearMarcasVehiculos.marcaVehiculo.grabarMarcaDeVehiculoss()) {

                    fCrearMarcasVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                    fCrearMarcasVehiculos.btnGrabar.setEnabled(false);
                    ini.setListaDeMarcasDeVehiculos();
                    JOptionPane.showInternalMessageDialog(fCrearMarcasVehiculos, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);

                    fCrearMarcasVehiculos.btnNuevo.setEnabled(true);
                    fCrearMarcasVehiculos.jBtnNuevo.setEnabled(true);
                    fCrearMarcasVehiculos.btnGrabar.setEnabled(false);
                    fCrearMarcasVehiculos.jBtnGrabar.setEnabled(false);
                    llenarJtable();
                } else {
                    fCrearMarcasVehiculos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                    JOptionPane.showInternalMessageDialog(fCrearMarcasVehiculos, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
                    fCrearMarcasVehiculos.btnGrabar.setEnabled(true);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(FCrearCausalesDEDevolucion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    private void llenarJtable() {
        fCrearMarcasVehiculos.iniciado = false;
        fCrearMarcasVehiculos.marcaVehiculo = ini.getListaDeMarcasDeVehiculos().get(fCrearMarcasVehiculos.indice);

        fCrearMarcasVehiculos.txtMarcaVehiculo.setText("");
        fCrearMarcasVehiculos.txtMarcaVehiculo.setEnabled(false);
         fCrearMarcasVehiculos.chkActivo.setEnabled(false);
        fCrearMarcasVehiculos.actualizar = false;
        fCrearMarcasVehiculos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        fCrearMarcasVehiculos.btnNuevo.setText("nuevo");

        //fCrearCanalDeVentas.limpiarTabla();
        DefaultTableModel modelo = (DefaultTableModel) fCrearMarcasVehiculos.tblMarcasVehiculos.getModel();

        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        // Object[] fila = new Object[3];
        //  DefaultTableModel modelo;

        DefaultTableModel modelo2 = (DefaultTableModel) fCrearMarcasVehiculos.tblMarcasVehiculos.getModel();

        for (CMarcasDeVehiculos obj : ini.getListaDeMarcasDeVehiculos()) {
            try {
                int filaTabla2 = fCrearMarcasVehiculos.tblMarcasVehiculos.getRowCount();

                modelo2.addRow(new Object[filaTabla2]);

                fCrearMarcasVehiculos.tblMarcasVehiculos.setValueAt(obj.getIdMarcaDeVehiculo(), filaTabla2, 0);  // item
                fCrearMarcasVehiculos.tblMarcasVehiculos.setValueAt(obj.getNombreMarcaDeVehiculos(), filaTabla2, 1); // numero de factura
                if (obj.getActivoMarcaDeVehiculos() == 1) {
                    fCrearMarcasVehiculos.tblMarcasVehiculos.setValueAt(true, filaTabla2, 2); //
                } else {
                    fCrearMarcasVehiculos.tblMarcasVehiculos.setValueAt(false, filaTabla2, 2); //
                }

                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(HiloFcrearMarcasDeVehiculo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void seleccionarFila() {
        // TODO add your handling code here:

        int row = fCrearMarcasVehiculos.tblMarcasVehiculos.getSelectedRow();
        int id = Integer.parseInt(String.valueOf(fCrearMarcasVehiculos.tblMarcasVehiculos.getValueAt(row, 0)));
        fCrearMarcasVehiculos.lblIdMarcaVeh.setText("Id. Marca Veh. :  " + id);

        for (CMarcasDeVehiculos marca : ini.getListaDeMarcasDeVehiculos()) {
            if (id == marca.getIdMarcaDeVehiculo()) {
                fCrearMarcasVehiculos.marcaVehiculo = marca;
                break;
            }
        }

        fCrearMarcasVehiculos.txtMarcaVehiculo.setText(fCrearMarcasVehiculos.marcaVehiculo.getNombreMarcaDeVehiculos());
        if (fCrearMarcasVehiculos.marcaVehiculo.getActivoMarcaDeVehiculos() == 1) {
            fCrearMarcasVehiculos.chkActivo.setText("Marca Veh.  Activo");
            fCrearMarcasVehiculos.chkActivo.setSelected(true);
        }

        if (fCrearMarcasVehiculos.marcaVehiculo.getActivoMarcaDeVehiculos() == 0) {
            fCrearMarcasVehiculos.chkActivo.setText("Marca Veh.  no Activo");
            fCrearMarcasVehiculos.chkActivo.setSelected(false);
        }

        fCrearMarcasVehiculos.actualizar = true;
        fCrearMarcasVehiculos.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        fCrearMarcasVehiculos.btnNuevo.setText("Actualizar");

    }

   

}
