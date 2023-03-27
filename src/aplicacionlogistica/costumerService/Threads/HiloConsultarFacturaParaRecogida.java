/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FIngresarRecogidasClientes;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloConsultarFacturaParaRecogida implements Runnable {

    Inicio ini;
    FIngresarRecogidasClientes fIngresarRecogidasClientes;

    /**
     * Constructor de clase
     *
     * @param fIngresarDescuentoClientes
     */
    public HiloConsultarFacturaParaRecogida(FIngresarRecogidasClientes fIngresarRecogidasClientes) {

        this.fIngresarRecogidasClientes = fIngresarRecogidasClientes;
        this.ini = fIngresarRecogidasClientes.ini;

    }

    @Override
    public void run() {

        fIngresarRecogidasClientes.lblCirculoDeProgreso.setVisible(true);
        fIngresarRecogidasClientes.txtNumeroFactura.setEnabled(false);

        if (fIngresarRecogidasClientes.consultarFactura()) {
            fIngresarRecogidasClientes.llenarJTable();
            switch (fIngresarRecogidasClientes.factura.getIdEstadoFactura()) {
                case 1:
                      habilitarTxtBox(true);
                    break;
                case 2:
                    fIngresarRecogidasClientes.lblCirculoDeProgreso.setVisible(false);
                     habilitarTxtBox(false);
                    JOptionPane.showInternalMessageDialog(fIngresarRecogidasClientes, "La factura solicitada ya fue entregada", "Error", JOptionPane.ERROR_MESSAGE);
                    fIngresarRecogidasClientes.pnlDescuento.setEnabled(false);
                    break;
                case 3:
                    fIngresarRecogidasClientes.lblCirculoDeProgreso.setVisible(false);
                    fIngresarRecogidasClientes.pnlDescuento.setEnabled(false);
                     habilitarTxtBox(false);
                    JOptionPane.showInternalMessageDialog(fIngresarRecogidasClientes, "La factura solicitada ya fue Devuelta", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case 4:
                    fIngresarRecogidasClientes.lblCirculoDeProgreso.setVisible(false);
                    fIngresarRecogidasClientes.pnlDescuento.setEnabled(false);
                     habilitarTxtBox(false);
                    JOptionPane.showInternalMessageDialog(fIngresarRecogidasClientes, "La factura solicitada ya fue entregada", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case 5:
                    fIngresarRecogidasClientes.lblCirculoDeProgreso.setVisible(false);
                    fIngresarRecogidasClientes.pnlDescuento.setEnabled(true);
                    habilitarTxtBox(true);
                    break;
                case 6:
                    fIngresarRecogidasClientes.lblCirculoDeProgreso.setVisible(false);
                    fIngresarRecogidasClientes.pnlDescuento.setEnabled(true);
                    break;
                case 7:
                    fIngresarRecogidasClientes.lblCirculoDeProgreso.setVisible(false);
                    fIngresarRecogidasClientes.pnlDescuento.setEnabled(true);
                    break;

            }
            
               /* se verifica que la factura esté libre, sino arroja el siguiente mensaje... 
                     =0 indica que la factura esta siendo ocupada por alguien  */
                    if (fIngresarRecogidasClientes.factura.getIsFree() == 0) {
                        fIngresarRecogidasClientes.lblCirculoDeProgreso.setVisible(false);
                        fIngresarRecogidasClientes.pnlDescuento.setEnabled(false);
                         habilitarTxtBox(false);
                        JOptionPane.showInternalMessageDialog(fIngresarRecogidasClientes, "La Factura  # " + fIngresarRecogidasClientes.factura.getNumeroFactura()
                                + " ya se encuentra en Distribución "
                                + "", "Error", JOptionPane.WARNING_MESSAGE);

                        //return;
                    }

        } else {
            fIngresarRecogidasClientes.lblCirculoDeProgreso.setVisible(false);
            fIngresarRecogidasClientes.pnlDescuento.setEnabled(false);
            fIngresarRecogidasClientes.pnlFactura.setEnabled(false);
            JOptionPane.showInternalMessageDialog(fIngresarRecogidasClientes, "La factura solicitada no existe", "Error", JOptionPane.ERROR_MESSAGE);
            
        fIngresarRecogidasClientes.btnNuevo.setEnabled(true);
        fIngresarRecogidasClientes.btnNuevo2.setEnabled(true);

        }

        fIngresarRecogidasClientes.lblCirculoDeProgreso.setVisible(false);

    }
 private void habilitarTxtBox(boolean valor) {
        fIngresarRecogidasClientes.btnNuevo.setEnabled(!valor);
        fIngresarRecogidasClientes.btnNuevo2.setEnabled(!valor);
        fIngresarRecogidasClientes.txtNumeroFormato.setEnabled(valor);
        fIngresarRecogidasClientes.txtValorDescuento.setEnabled(valor);
        fIngresarRecogidasClientes.txtNumeroFormato.setEditable(valor);
        fIngresarRecogidasClientes.txtValorDescuento.setEditable(valor);
        
        fIngresarRecogidasClientes.txtNumeroFormato.requestFocus();

    }
}
