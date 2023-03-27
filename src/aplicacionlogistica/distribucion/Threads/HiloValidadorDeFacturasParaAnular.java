/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.FAnularFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CFacturasParaAnular;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloValidadorDeFacturasParaAnular implements Runnable {

    FAnularFacturas formAnular = null;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini;

    ArrayList<CFacturasParaAnular> listaDeRechasosParaAnular = null;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloValidadorDeFacturasParaAnular(Inicio ini) {
        this.ini = ini;
    }

    public HiloValidadorDeFacturasParaAnular(Inicio ini, FAnularFacturas formAnular) {
        this.ini = ini;
        this.formAnular = formAnular;
    }

    @Override
    public void run() {
        this.formAnular.lblCirculoDeProgreso1.setVisible(true);
        this.formAnular.txtNumeroDeFactura.setEnabled(false);
        
        try {
            
            CFacturasPorManifiesto facxMan = null ;
                   // = new CFacturasPorManifiesto(ini, this.formAnular.txtNumeroDeFactura.getText());

            /*FACTURA NO APARECE SALIENDO A DISTRIBUCION */
            if (facxMan.getNumeroFactura() == null) {
                this.formAnular.btnAgregar.setEnabled(true);
                this.formAnular.btnGrabar.setEnabled(true);
                
                System.out.println("ya valido el numero de la factura .. " + this.formAnular.txtNumeroDeFactura.getText());

                // anexarFacturaParaAnular();
            } else {
                this.formAnular.btnAgregar.setEnabled(false);
                this.formAnular.txtJustificacion.setEnabled(false);
                this.formAnular.txtJustificacion.setEditable(false);
                JOptionPane.showInternalMessageDialog(this.formAnular, "La factura No cumple requisitos para ser anulada ", "Alerta ...!!", 2);
                this.formAnular.txtNumeroDeFactura.setEnabled(true);
                this.formAnular.txtNumeroDeFactura.setEditable(true);
                this.formAnular.txtNumeroDeFactura.requestFocus();
                this.formAnular.txtNumeroDeFactura.requestFocus();
            }
            this.formAnular.lblCirculoDeProgreso1.setVisible(false);
            
        } catch (Exception ex) {
            this.formAnular.lblCirculoDeProgreso1.setVisible(false);
            Logger.getLogger(HiloValidadorDeFacturasParaAnular.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showInternalMessageDialog(this.formAnular, "Problema de conexion BBDD", "Error", 2);

        }

    }


}
