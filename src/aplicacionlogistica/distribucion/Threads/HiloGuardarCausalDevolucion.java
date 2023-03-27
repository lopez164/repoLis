/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.administracion.logistica.FCrearCausalesDEDevolucion;
import aplicacionlogistica.distribucion.objetos.CCausalesDeDevolucion;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloGuardarCausalDevolucion implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    public FCrearCausalesDEDevolucion fCrearCausalesDevolucion;
    Inicio ini;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarCausalDevolucion(FCrearCausalesDEDevolucion form, Inicio ini) {
        this.ini = ini;
        this.fCrearCausalesDevolucion = form;

    }

    @Override
    public void run() {

        this.fCrearCausalesDevolucion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        fCrearCausalesDevolucion.causalDeDevolucion.setNombreCausalesDeDEvolucion(fCrearCausalesDevolucion.txtNombeCausalDeDevolucion.getText());

        if (fCrearCausalesDevolucion.chkActivo.isSelected()) {
            fCrearCausalesDevolucion.causalDeDevolucion.setActivoCausalesDeDEvolucion(1);

        } else {
            fCrearCausalesDevolucion.causalDeDevolucion.setActivoCausalesDeDEvolucion(0);

        }

        if (fCrearCausalesDevolucion.causalDeDevolucion.grabarCausalesDeRechazols(1)) {
            ini.getListaDeCausalesDeDevolucion().add(fCrearCausalesDevolucion.causalDeDevolucion);

            /*Limpia la tabla*/
            DefaultTableModel modelo = (DefaultTableModel) fCrearCausalesDevolucion.tblCausalesDeDEvolcuion.getModel();
            if (modelo.getRowCount() > 0) {
                int a = modelo.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    modelo.removeRow(i);
                }
            }

            /*Llena la tabla con los registros */
            for (CCausalesDeDevolucion obj : ini.getListaDeCausalesDeDevolucion()){

                Object[] fila = new Object[3];
                fila[0] = obj.getIdCausalesDeDevolucion();
                fila[1] = obj.getNombreCausalesDeDevolucion();
                if (obj.getActivoCausalesDeDevolucion()< 1) {
                    fila[2] = false;
                } else {
                    fila[2] = true;
                }

                modelo = (DefaultTableModel) fCrearCausalesDevolucion.tblCausalesDeDEvolcuion.getModel();
                modelo.addRow(fila);
            }

            JOptionPane.showInternalMessageDialog(fCrearCausalesDevolucion, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);
            this.fCrearCausalesDevolucion.btnGrabar.setEnabled(false);

        } else {
            JOptionPane.showInternalMessageDialog(fCrearCausalesDevolucion, "El registro no se ha guardado ", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            this.fCrearCausalesDevolucion.btnGrabar.setEnabled(true);

        }

        this.fCrearCausalesDevolucion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

    }

}
