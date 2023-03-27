/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.distribucion.formularios.Hielera.FliquidarManifiestos;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import java.awt.HeadlessException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloGuardarManifiestoDescargado100 implements Runnable {

    public static boolean band = false;
    private final int tiempo = 5;
    Inicio ini;

    DescargarFacturas fDescargarFacturas = null;
    FliquidarManifiestos fliquidarManifiestos= null;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarManifiestoDescargado100(Inicio ini, DescargarFacturas form) {

        this.fDescargarFacturas = form;
        this.ini = ini;
   
    }

     /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarManifiestoDescargado100(Inicio ini, FliquidarManifiestos fliquidarManifiestos) {

        this.fliquidarManifiestos = fliquidarManifiestos;
        this.ini = ini;
   
    }
    
//    public HiloGuardarManifiestoDescargado100(Inicio ini, JInternalFrame form) {
//
//    }

    @Override
    public void run() {

        if(fDescargarFacturas != null){
        metodoViejo(); 
        }
        
         if(fliquidarManifiestos != null){
        metodoNuevo(); 
        }
    }

    public void metodoViejo() throws HeadlessException {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        int filaSeleccionada;
        boolean contado = false;
        if (this.fDescargarFacturas.rBtnContado.isSelected()) {
            contado = true;
        }
        this.fDescargarFacturas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fDescargarFacturas.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        this.fDescargarFacturas.btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fDescargarFacturas.jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        this.fDescargarFacturas.estaOcupadoGrabando = true;

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarManifiestoDescargado100.class.getName()).log(Level.SEVERE, null, ex);
        }

        //String fechax;
        //fechax = fecha.format(new Date());
        this.fDescargarFacturas.cbxMovimientoFactura.setEnabled(false);
        this.fDescargarFacturas.txtNumeroDeFactura.setEnabled(false);
        this.fDescargarFacturas.txtKilometrosEntrada.setEnabled(false);

        this.fDescargarFacturas.cbxCausalDeRechazoFactura.setEnabled(false);
        this.fDescargarFacturas.btnDescargarRechazoTotal.setEnabled(false);

        /* Se cambia el estado de manifiesto a manifiesto cerrado*/
        try {
            /* Se graba las facturas descargadas, prductos por factura descargados, manifiesto  se actualiza */
            if (this.fDescargarFacturas.manifiestoActual.grabarFacturasDescargadas100(contado)) {
                
                
                /*Se llena la jtable de las facturasDescargadas */
                
                fDescargarFacturas.modelo2 = (DefaultTableModel) fDescargarFacturas.jTableFacturasPorVehiculo.getModel();
                int i=0;
                for (CFacturasPorManifiesto fac : fDescargarFacturas.manifiestoActual.getListaFacturasDescargadas()) {
                    
                    
                    filaSeleccionada = fDescargarFacturas.jTableFacturasPorVehiculo.getRowCount();

                    fDescargarFacturas.modelo2.addRow(new Object[fDescargarFacturas.jTableFacturasPorVehiculo.getRowCount()]);

                    fDescargarFacturas.jTableFacturasPorVehiculo.setValueAt("" + i++, filaSeleccionada, 0);  // item
                    fDescargarFacturas.jTableFacturasPorVehiculo.setValueAt(fac.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
                    fDescargarFacturas.jTableFacturasPorVehiculo.setValueAt(fac.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
                    fDescargarFacturas.jTableFacturasPorVehiculo.setValueAt(fac.getTipoDeDEscargue(), filaSeleccionada, 3); // nombre del nombreDelCliente
                    fDescargarFacturas.jTableFacturasPorVehiculo.setValueAt(nf.format(fac.getValorRecaudado()), filaSeleccionada, 4);
                    
                    fDescargarFacturas.jTableFacturasPorVehiculo.changeSelection(filaSeleccionada, 0, false, false);
                    
                }
                
                this.fDescargarFacturas.lblFacturasPendientes.setText("0");
                
                /* al pasar a estado 4 es decir cerrardo, se libera el manifiesto */
                new Thread(new HiloLiberarManifiesto(this.fDescargarFacturas.manifiestoActual, true)).start();

                /*Mensajes de rigor*/
                System.out.println("Termina de guardar los datos de formulario descargar facturas  ");

                this.fDescargarFacturas.estaOcupadoGrabando = false;
                this.fDescargarFacturas.btnImprimir.setEnabled(true);
                this.fDescargarFacturas.btnGrabar.setEnabled(false);

                this.fDescargarFacturas.lblCirculoDeProgreso.setVisible(false);
                this.fDescargarFacturas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDescargarFacturas.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                this.fDescargarFacturas.btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/Misc-Upload-Database-icon.png"))); // NOI18N
                this.fDescargarFacturas.jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N

                JOptionPane.showMessageDialog(this.fDescargarFacturas, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                this.fDescargarFacturas.jBtnMinuta.setEnabled(true);
                this.fDescargarFacturas.manifiestoActual.setEstadoManifiesto(4);

                this.fDescargarFacturas.btnImprimir.requestFocus();

                /* si presenta algun error en grabar el manifiesto  */
            } else {

                this.fDescargarFacturas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDescargarFacturas.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                this.fDescargarFacturas.btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/Misc-Upload-Database-icon.png"))); // NOI18N
                this.fDescargarFacturas.jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N

                this.fDescargarFacturas.btnGrabar.setEnabled(true);
                JOptionPane.showMessageDialog(this.fDescargarFacturas, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this.fDescargarFacturas, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            this.fDescargarFacturas.btnGrabar.setEnabled(true);
            this.fDescargarFacturas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDescargarFacturas.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

            this.fDescargarFacturas.btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/Misc-Upload-Database-icon.png"))); // NOI18N
            this.fDescargarFacturas.jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N

        } catch (Exception ex) {
            Logger.getLogger(HiloGuardarManifiestoDescargado100.class.getName()).log(Level.SEVERE, null, ex);
            this.fDescargarFacturas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDescargarFacturas.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

            this.fDescargarFacturas.btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/Misc-Upload-Database-icon.png"))); // NOI18N
            this.fDescargarFacturas.jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N
        }

        this.fDescargarFacturas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        this.fDescargarFacturas.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

        this.fDescargarFacturas.btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/Misc-Upload-Database-icon.png"))); // NOI18N
        this.fDescargarFacturas.jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N
    }

 public void   metodoNuevo(){
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        int filaSeleccionada;
        boolean contado = false;
        if (this.fliquidarManifiestos.rBtnContado.isSelected()) {
            contado = true;
        }
        this.fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        this.fliquidarManifiestos.btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fliquidarManifiestos.jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        this.fliquidarManifiestos.estaOcupadoGrabando = true;

        //String fechax;
        //fechax = fecha.format(new Date());
        this.fliquidarManifiestos.cbxMovimientoFactura.setEnabled(false);
        this.fliquidarManifiestos.txtNumeroDeFactura.setEnabled(false);
//        this.fliquidarManifiestos.txtKilometrosEntrada.setEnabled(false);

        this.fliquidarManifiestos.cbxCausalDeRechazoFactura.setEnabled(false);
        this.fliquidarManifiestos.btnDescargarRechazoTotal.setEnabled(false);

      
        try {
            /* Se graba las facturas descargadas, prductos por factura descargados, manifiesto  se actualiza */
            if (this.fliquidarManifiestos.manifiesto.grabarFacturasDescargadas100(contado)) {
                
              
//                fliquidarManifiestos.conductorActual.setListaFacturasDescargadas();
                fliquidarManifiestos.manifiesto.setListaFacturasPendientesPorDescargar();

                /*Se llena la jtable de las facturasDescargadas */
                fliquidarManifiestos.modelo2 = (DefaultTableModel) fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getModel();

                if (fliquidarManifiestos.modelo2.getRowCount() > 0) {
                    int a = fliquidarManifiestos.modelo2.getRowCount() - 1;
                    for (int i = a; i >= 0; i--) {
                        fliquidarManifiestos.modelo2.removeRow(i);
                    }
                }
     
                int i=1;
                for (CFacturasPorManifiesto fac : fliquidarManifiestos.manifiesto.getListaFacturasDescargadas()) {
                    
                   
                    if (this.fliquidarManifiestos.rBtnContado.isSelected()) {

                        fac.setTipoDeDEscargue("E. T. Cn");

                        // SI LA FACTURA ES DE CREDITO
                    }
                    if (this.fliquidarManifiestos.rBtnCredito.isSelected()) {

                        fac.setTipoDeDEscargue("E. T. Cr");

                    }
                    
                    filaSeleccionada = fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount();

                    fliquidarManifiestos.modelo2.addRow(new Object[fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount()]);

                    fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt("" + i++, filaSeleccionada, 0);  // item
                    fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fac.getNumeroFactura(), filaSeleccionada, 1); // numero de factura
                    fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fac.getNombreDeCliente(), filaSeleccionada, 2); // nombre del nombreDelCliente
                    fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fac.getTipoDeDEscargue(), filaSeleccionada, 3); // nombre del nombreDelCliente
                    fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(nf.format(fac.getValorRecaudado()), filaSeleccionada, 4);
                    
                    fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.changeSelection(filaSeleccionada, 0, false, false);
                    
                }
                
                
                
                this.fliquidarManifiestos.lblFacturasPendientes.setText("" + fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size());
                this.fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));
                
               
               // new Thread(new HiloLiberarManifiesto(this.fliquidarManifiestos.conductorActual, true)).start();

                /*Mensajes de rigor*/
                System.out.println("Termina de guardar los datos de formulario descargar facturas  ");

                this.fliquidarManifiestos.estaOcupadoGrabando = false;
                this.fliquidarManifiestos.btnImprimir.setEnabled(true);
                this.fliquidarManifiestos.btnGrabar.setEnabled(false);

//                this.fliquidarManifiestos.lblCirculoDeProgreso.setVisible(false);
                this.fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                this.fliquidarManifiestos.btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/Misc-Upload-Database-icon.png"))); // NOI18N
                this.fliquidarManifiestos.jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N

                JOptionPane.showMessageDialog(this.fliquidarManifiestos.descargarFacturas_2, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                this.fliquidarManifiestos.jBtnMinuta.setEnabled(true);
              

                this.fliquidarManifiestos.btnImprimir.requestFocus();

                /* si presenta algun error en grabar el manifiesto  */
            } else {

                this.fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                this.fliquidarManifiestos.btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/Misc-Upload-Database-icon.png"))); // NOI18N
                this.fliquidarManifiestos.jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N

                this.fliquidarManifiestos.btnGrabar.setEnabled(true);
                JOptionPane.showMessageDialog(this.fliquidarManifiestos, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this.fliquidarManifiestos, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            this.fliquidarManifiestos.btnGrabar.setEnabled(true);
            this.fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

            this.fliquidarManifiestos.btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/Misc-Upload-Database-icon.png"))); // NOI18N
            this.fliquidarManifiestos.jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N

        } catch (Exception ex) {
            Logger.getLogger(HiloGuardarManifiestoDescargado100.class.getName()).log(Level.SEVERE, null, ex);
            this.fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

            this.fliquidarManifiestos.btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/Misc-Upload-Database-icon.png"))); // NOI18N
            this.fliquidarManifiestos.jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N
        }

        this.fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        this.fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

        this.fliquidarManifiestos.btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/Misc-Upload-Database-icon.png"))); // NOI18N
        this.fliquidarManifiestos.jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N
    
    }
 
    private double calcularValoraAConsignar() {
        double valor = 0.0;
        for (CFacturasPorManifiesto fac : fliquidarManifiestos.manifiesto.getListaFacturasDescargadas()) {
            valor += fac.getValorRecaudado();
        }
        return valor;
    }
}
