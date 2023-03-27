/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.administracion.logistica.FCrearCanalDeVentas;
import aplicacionlogistica.distribucion.formularios.administracion.FCrearMarcasDexxxxxxxxxxVehiculos;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloGuardarCanalesDeVentas implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    public FCrearCanalDeVentas fCrearCanalDeVentas;
    Inicio ini;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarCanalesDeVentas(FCrearCanalDeVentas form, Inicio ini) {
        this.ini = ini;
        this.fCrearCanalDeVentas = form;

    }

    @Override
    public void run() {

        this.fCrearCanalDeVentas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        fCrearCanalDeVentas.canalDeVentas.setNombreCanalDeVenta(fCrearCanalDeVentas.txtNombeCanalDeVentas.getText());

        if (fCrearCanalDeVentas.chkActivo.isSelected()) {
            fCrearCanalDeVentas.canalDeVentas.setActivoCanal(1);

        } else {
            fCrearCanalDeVentas.canalDeVentas.setActivoCanal(0);

        }

        if (fCrearCanalDeVentas.canalDeVentas.grabarCanals(1)) {
            ini.getListaDeCanalesDeVenta().add(fCrearCanalDeVentas.canalDeVentas);

            /*Limpia la tabla*/
            DefaultTableModel modelo = (DefaultTableModel) fCrearCanalDeVentas.tblCanales.getModel();
            if (modelo.getRowCount() > 0) {
                int a = modelo.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    modelo.removeRow(i);
                }
            }

            /*Llena la tabla con los registros */
            for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()){

                Object[] fila = new Object[3];
                fila[0] = obj.getIdCanalDeVenta();
                fila[1] = obj.getNombreCanalDeVenta();
                if (obj.getActivoCanal()< 1) {
                    fila[2] = false;
                } else {
                    fila[2] = true;
                }

                modelo = (DefaultTableModel) fCrearCanalDeVentas.tblCanales.getModel();
                modelo.addRow(fila);
            }

            JOptionPane.showInternalMessageDialog(fCrearCanalDeVentas, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);
            this.fCrearCanalDeVentas.btnGrabar.setEnabled(false);

        } else {
            JOptionPane.showInternalMessageDialog(fCrearCanalDeVentas, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            this.fCrearCanalDeVentas.btnGrabar.setEnabled(true);

        }

        this.fCrearCanalDeVentas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

    }

}
