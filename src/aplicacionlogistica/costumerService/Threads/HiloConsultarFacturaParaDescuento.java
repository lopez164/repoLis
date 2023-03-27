/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FIngresarDescuentoClientes;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloConsultarFacturaParaDescuento implements Runnable {

    Inicio ini;
    FIngresarDescuentoClientes fIngresarDescuentoClientes;

    /**
     * Constructor de clase
     *
     * @param fIngresarDescuentoClientes
     */
    public HiloConsultarFacturaParaDescuento(FIngresarDescuentoClientes fIngresarDescuentoClientes) {

        this.fIngresarDescuentoClientes = fIngresarDescuentoClientes;
        this.ini = fIngresarDescuentoClientes.ini;

    }

    @Override
    public void run() {

        fIngresarDescuentoClientes.lblCirculoDeProgreso.setVisible(true);
        fIngresarDescuentoClientes.txtNumeroFactura.setEnabled(false);

        if (fIngresarDescuentoClientes.consultarFactura()) {
            fIngresarDescuentoClientes.llenarJTable();
            switch (fIngresarDescuentoClientes.factura.getEstadoFactura()) {
                case 1:
                      habilitarTxtBox(true);
                     break;
                case 2:
                    fIngresarDescuentoClientes.lblCirculoDeProgreso.setVisible(false);
                     habilitarTxtBox(false);
                    JOptionPane.showInternalMessageDialog(fIngresarDescuentoClientes, "La factura solicitada ya fue entregada", "Error", JOptionPane.ERROR_MESSAGE);
                    fIngresarDescuentoClientes.pnlDescuento.setEnabled(false);
                    break;
                case 3:
                    fIngresarDescuentoClientes.lblCirculoDeProgreso.setVisible(false);
                    fIngresarDescuentoClientes.pnlDescuento.setEnabled(false);
                     habilitarTxtBox(false);
                    JOptionPane.showInternalMessageDialog(fIngresarDescuentoClientes, "La factura solicitada ya fue Devuelta", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case 4:
                    fIngresarDescuentoClientes.lblCirculoDeProgreso.setVisible(false);
                    fIngresarDescuentoClientes.pnlDescuento.setEnabled(false);
                     habilitarTxtBox(false);
                    JOptionPane.showInternalMessageDialog(fIngresarDescuentoClientes, "La factura solicitada ya fue entregada", "Error", JOptionPane.ERROR_MESSAGE);
                    break;
                case 5:
                    fIngresarDescuentoClientes.lblCirculoDeProgreso.setVisible(false);
                    fIngresarDescuentoClientes.pnlDescuento.setEnabled(true);
                    habilitarTxtBox(true);
                    break;
                case 6:
                    fIngresarDescuentoClientes.lblCirculoDeProgreso.setVisible(false);
                    fIngresarDescuentoClientes.pnlDescuento.setEnabled(true);
                    break;
                case 7:
                    fIngresarDescuentoClientes.lblCirculoDeProgreso.setVisible(false);
                    fIngresarDescuentoClientes.pnlDescuento.setEnabled(true);
                    break;

            }
            
             /* se verifica que la factura esté libre, sino arroja el siguiente mensaje... 
                     =0 indica que la factura esta siendo ocupada por alguien  */
                    if (fIngresarDescuentoClientes.factura.getIsFree() == 0) {
                        fIngresarDescuentoClientes.lblCirculoDeProgreso.setVisible(false);
                        fIngresarDescuentoClientes.pnlDescuento.setEnabled(false);
                         habilitarTxtBox(false);
                        JOptionPane.showInternalMessageDialog(fIngresarDescuentoClientes, "La Factura  # " + fIngresarDescuentoClientes.factura.getNumeroDeFactura()
                                + " ya se encuentra en Distribución "
                                + "", "Error", JOptionPane.WARNING_MESSAGE);
                        //return;
                    }

        } else {
            fIngresarDescuentoClientes.lblCirculoDeProgreso.setVisible(false);
            fIngresarDescuentoClientes.pnlDescuento.setEnabled(false);
            fIngresarDescuentoClientes.pnlFactura.setEnabled(false);
            JOptionPane.showInternalMessageDialog(fIngresarDescuentoClientes, "La factura solicitada no existe", "Error", JOptionPane.ERROR_MESSAGE);
            
        fIngresarDescuentoClientes.btnNuevo.setEnabled(true);
        fIngresarDescuentoClientes.btnNuevo2.setEnabled(true);

        }

        fIngresarDescuentoClientes.lblCirculoDeProgreso.setVisible(false);

    }

    private void habilitarTxtBox(boolean valor) {
        fIngresarDescuentoClientes.btnNuevo.setEnabled(!valor);
        fIngresarDescuentoClientes.btnNuevo2.setEnabled(!valor);
        fIngresarDescuentoClientes.txtNumeroFormato.setEnabled(valor);
        fIngresarDescuentoClientes.txtValorDescuento.setEnabled(valor);
        fIngresarDescuentoClientes.txtNumeroFormato.setEditable(valor);
        fIngresarDescuentoClientes.txtValorDescuento.setEditable(valor);
        
        fIngresarDescuentoClientes.txtNumeroFormato.requestFocus();

    }
}
