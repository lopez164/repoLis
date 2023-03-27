/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.Hielera.FDespachoHielera;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRutaConIntegrador;
import aplicacionlogistica.distribucion.formularios.Hielera.FManifestarPedidosHielera_2;
import java.awt.HeadlessException;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarFacturasPorManifiesto_2 implements Runnable {

    public static boolean band = false;
    private final int tiempo = 5;
    Inicio ini;

    FManifestarPedidosEnRuta fManifestarPedidosEnRuta=null;
    FDespachoHielera fDespachoHielera=null;
    FManifestarPedidosHielera_2 fManifestarPedidosHielera_2 = null;
    FManifestarPedidosEnRutaConIntegrador fManifestarPedidosEnRutaConIntegrador = null;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarFacturasPorManifiesto_2(Inicio ini, FManifestarPedidosEnRuta form) {

        this.fManifestarPedidosEnRuta = form;
        this.ini = ini;
   
    }

    
    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarFacturasPorManifiesto_2(Inicio ini, FManifestarPedidosEnRutaConIntegrador fManifestarPedidosEnRutaConIntegrador) {

        this.fManifestarPedidosEnRutaConIntegrador = fManifestarPedidosEnRutaConIntegrador;
        this.ini = ini;
   
    }
    
    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarFacturasPorManifiesto_2(Inicio ini, FDespachoHielera fDespachoHielera,FManifestarPedidosHielera_2 fManifestarPedidosHielera_2) {

        this.fDespachoHielera = fDespachoHielera;
        this.ini = ini;
        this.fManifestarPedidosHielera_2 = fManifestarPedidosHielera_2;
   
    }
    
  

    @Override
    public void run() {
if(fManifestarPedidosEnRuta != null){
    salidasRutas(); 
}
       
if(fDespachoHielera != null){
    salidasDomicilios(); 
}

if(fManifestarPedidosEnRutaConIntegrador != null){
    salidasRutas(); 
}
    }

    public void salidasRutas() throws HeadlessException {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
               
        this.fManifestarPedidosEnRuta.btnImprimir.setEnabled(false);
        this.fManifestarPedidosEnRuta.jBtnImprimir.setEnabled(false);

        this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        this.fManifestarPedidosEnRuta.repaint();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto_2.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.fManifestarPedidosEnRuta.btnGrabar.setEnabled(false);
        this.fManifestarPedidosEnRuta.jBtnGrabar.setEnabled(false);
        this.fManifestarPedidosEnRuta.jBtnObservaciones.setEnabled(false);
        
        this.fManifestarPedidosEnRuta.txtNumeroDeFactura.setEnabled(false);
        this.fManifestarPedidosEnRuta.pnlAgregarLista.setEnabled(false);

        this.fManifestarPedidosEnRuta.estaOcupadoGrabando = true;
        double valorManifiesto = 0.0;
        double pesoKgManifiesto = 0.0;
        
        
        /* Se cambia el estado de manifiesto a manifiesto cerrado*/
        try {
            /* Se graba las facturas descargadas, prductos por factura descargados, manifiesto  se actualiza */
            if (this.fManifestarPedidosEnRuta.manifiestoActual.grabarFacturasPorManifiesto(true)) {
                
                
                fManifestarPedidosEnRuta.manifiestoActual.setEstadoManifiesto(3);
                fManifestarPedidosEnRuta.manifiestoActual.setIsFree(1);
                fManifestarPedidosEnRuta.manifiestoActual.actualizarManifiestoDeDistribucions();
                
                fManifestarPedidosEnRuta.manifiestoActual.setHoraDeDespacho("" + new Date());
                fManifestarPedidosEnRuta.manifiestoActual.setPesoKgManifiesto(fManifestarPedidosEnRuta.sumadorPesosFacturas);
                
                this.fManifestarPedidosEnRuta.estaOcupadoGrabando = false;
                this.fManifestarPedidosEnRuta.btnImprimir.setEnabled(true);
                this.fManifestarPedidosEnRuta.btnGrabar.setEnabled(false);
                
                
                this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
                
                
                JOptionPane.showMessageDialog(this.fManifestarPedidosEnRuta, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                this.fManifestarPedidosEnRuta.jBtnMinuta.setEnabled(true);
                
                
                this.fManifestarPedidosEnRuta.btnImprimir.requestFocus();

                /* si presenta algun error en grabar el manifiesto  */
            } else {

                this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
                
                
                this.fManifestarPedidosEnRuta.btnGrabar.setEnabled(true);
                JOptionPane.showMessageDialog(this.fManifestarPedidosEnRuta, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this.fManifestarPedidosEnRuta, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            this.fManifestarPedidosEnRuta.btnGrabar.setEnabled(true);
            this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
            
            
        } catch (Exception ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto_2.class.getName()).log(Level.SEVERE, null, ex);
            this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
            
        }
        
        this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
    }
    
    public void salidasDomicilios() throws HeadlessException {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
               
        this.fDespachoHielera.btnImprimir.setEnabled(false);
        this.fDespachoHielera.jBtnImprimir.setEnabled(false);

        this.fDespachoHielera.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fDespachoHielera.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        this.fDespachoHielera.repaint();

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto_2.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.fDespachoHielera.btnGrabar.setEnabled(false);
        this.fDespachoHielera.jBtnGrabar.setEnabled(false);
        this.fDespachoHielera.jBtnObservaciones.setEnabled(false);
        
        //this.fDespachoHielera.txtNumeroDeFactura.setEnabled(false);
        //this.fDespachoHielera.pnlAgregarLista.setEnabled(false);

        this.fDespachoHielera.estaOcupadoGrabando = true;
        double valorManifiesto = 0.0;
        double pesoKgManifiesto = 0.0;
        
        
        /* Se cambia el estado de manifiesto a manifiesto cerrado*/
        try {
            /* Se graba las facturas descargadas, prductos por factura descargados, manifiesto  se actualiza */
            if (this.fDespachoHielera.manifiestoActual.grabarFacturasPorManifiesto()) {
                
               new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 3, true)).start();
          
                this.fDespachoHielera.llenarJtableFacturasXmanifiesto();
                
                this.fDespachoHielera.cantDeSalidas++;
                                             
                //fDespachoHielera.manifiestoActual.setEstadoManifiesto(3);
               // fDespachoHielera.manifiestoActual.setIsFree(1);
                 fDespachoHielera.manifiestoActual.setCantDeSalidas( this.fDespachoHielera.cantDeSalidas);
               // fDespachoHielera.manifiestoActual.actualizarManifiestoDeDistribucions();
                
               // fDespachoHielera.manifiestoActual.setHoraDeDespacho("" + new Date());
               // fDespachoHielera.manifiestoActual.setPesoKgManifiesto(fDespachoHielera.sumadorPesosFacturas);
                
                this.fDespachoHielera.estaOcupadoGrabando = false;
                this.fDespachoHielera.btnImprimir.setEnabled(true);
                this.fDespachoHielera.btnGrabar.setEnabled(false);
                
                
                this.fDespachoHielera.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDespachoHielera.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
                                
                //JOptionPane.showMessageDialog(this.fDespachoHielera, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                this.fDespachoHielera.jBtnMinuta.setEnabled(true);
                
                this.fDespachoHielera.btnImprimir.requestFocus();

                /* si presenta algun error en grabar el manifiesto  */
            } else {

                this.fDespachoHielera.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDespachoHielera.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
                
                
                this.fDespachoHielera.btnGrabar.setEnabled(true);
                JOptionPane.showMessageDialog(this.fDespachoHielera, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this.fDespachoHielera, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            this.fDespachoHielera.btnGrabar.setEnabled(true);
            this.fDespachoHielera.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDespachoHielera.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
            
            
        } catch (Exception ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto_2.class.getName()).log(Level.SEVERE, null, ex);
            this.fDespachoHielera.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDespachoHielera.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
            
        }
        
        this.fDespachoHielera.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        this.fDespachoHielera.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
    }
    
     public void salidasRutasConIntegrador() throws HeadlessException {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
               
        this.fManifestarPedidosEnRutaConIntegrador.btnImprimir.setEnabled(false);
        this.fManifestarPedidosEnRutaConIntegrador.jBtnImprimir.setEnabled(false);

        this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        this.fManifestarPedidosEnRutaConIntegrador.repaint();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto_2.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setEnabled(false);
        this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setEnabled(false);
        this.fManifestarPedidosEnRutaConIntegrador.jBtnObservaciones.setEnabled(false);
        
        this.fManifestarPedidosEnRutaConIntegrador.txtNumeroDeFactura.setEnabled(false);
        this.fManifestarPedidosEnRutaConIntegrador.pnlAgregarLista.setEnabled(false);

        this.fManifestarPedidosEnRutaConIntegrador.estaOcupadoGrabando = true;
        double valorManifiesto = 0.0;
        double pesoKgManifiesto = 0.0;
        
        
        /* Se cambia el estado de manifiesto a manifiesto cerrado*/
        try {
            /* Se graba las facturas descargadas, prductos por factura descargados, manifiesto  se actualiza */
            if (this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.grabarFacturasPorManifiesto(true)) {
                
                
                fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setEstadoManifiesto(3);
                fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setIsFree(1);
                fManifestarPedidosEnRutaConIntegrador.manifiestoActual.actualizarManifiestoDeDistribucions();
                
                fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setHoraDeDespacho("" + new Date());
                fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setPesoKgManifiesto(fManifestarPedidosEnRutaConIntegrador.sumadorPesosFacturas);
                
                this.fManifestarPedidosEnRutaConIntegrador.estaOcupadoGrabando = false;
                this.fManifestarPedidosEnRutaConIntegrador.btnImprimir.setEnabled(true);
                this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setEnabled(false);
                
                
                this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
                
                
                JOptionPane.showMessageDialog(this.fManifestarPedidosEnRutaConIntegrador, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                this.fManifestarPedidosEnRutaConIntegrador.jBtnMinuta.setEnabled(true);
                
                
                this.fManifestarPedidosEnRutaConIntegrador.btnImprimir.requestFocus();

                /* si presenta algun error en grabar el manifiesto  */
            } else {

                this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
                
                
                this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setEnabled(true);
                JOptionPane.showMessageDialog(this.fManifestarPedidosEnRutaConIntegrador, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this.fManifestarPedidosEnRutaConIntegrador, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setEnabled(true);
            this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
            
            
        } catch (Exception ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto_2.class.getName()).log(Level.SEVERE, null, ex);
            this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
            
        }
        
        this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
    }
    

}
