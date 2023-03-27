    /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.distribucion.formularios.Hielera.FliquidarManifiestos;
import aplicacionlogistica.distribucion.objetos.CFacturasDescargadas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.CRecogidasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CSoportesConsignaciones;
import java.awt.HeadlessException;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarManifiestoDescargado implements Runnable {

    public static boolean band = false;
    private final int tiempo = 5;
    Inicio ini;
    boolean nuevo= false;

    DescargarFacturas fDescargarFacturas = null;
    FliquidarManifiestos fLiquidarManifiesto = null;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarManifiestoDescargado(Inicio ini, DescargarFacturas form) {

        this.fDescargarFacturas = form;
        this.ini = ini;

    }
    
    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarManifiestoDescargado(Inicio ini, DescargarFacturas form, boolean nuevo) {

        this.fDescargarFacturas = form;
        this.ini = ini;
        this.nuevo=nuevo;

    } 
    
     
    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarManifiestoDescargado(Inicio ini, FliquidarManifiestos fliquidarManifiestos, boolean nuevo) {

        this.fLiquidarManifiesto = fliquidarManifiestos;
        this.ini = ini;
        this.nuevo=nuevo;

    }

    public HiloGuardarManifiestoDescargado(Inicio ini, JInternalFrame form) {

    }

    @Override
    public void run() {

        if(fDescargarFacturas != null){
         metodoViejo(); 
        }
        
        if(fLiquidarManifiesto != null){
         metodoNuevo();
        }
        
    
    }

    private void metodoViejo() throws HeadlessException {
        this.fDescargarFacturas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fDescargarFacturas.jBtnGrabar.setIcon( new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        
        this.fDescargarFacturas.estaOcupadoGrabando = true;

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarManifiestoDescargado.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<String> sentenciasSQL_1; // Sentencias para guardar el descargue del manifiesto de la ruta.
        List<String> sentenciasSQL_2; // Sentecias que guarda los productos por factura descargadas
        List<String> sentenciasSQL_local;

        //this.fDescargarFacturas.conductorActual.setVistaFacturasEnDistribucion(this.fDescargarFacturas.listaFacturasDescargadas);
       // SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sentenciasSQL_1 = new ArrayList<>();
        sentenciasSQL_2 = new ArrayList<>();
        sentenciasSQL_local = new ArrayList<>();
        //String fechax;
        //fechax = fecha.format(new Date());

        this.fDescargarFacturas.cbxMovimientoFactura.setEnabled(false);
        this.fDescargarFacturas.txtNumeroDeFactura.setEnabled(false);
        this.fDescargarFacturas.txtKilometrosEntrada.setEnabled(false);

        this.fDescargarFacturas.cbxCausalDeRechazoFactura.setEnabled(false);
        this.fDescargarFacturas.btnDescargarRechazoTotal.setEnabled(false);
        
        
        /* Se cambia el estado de manifiesto a manifiesto cerrado*/
        
        /* ********************************** SE ARMAR EL ARCHIVO SQL PARA GRABAR LOS DATOS DEL MANIFIESTO******************************** */
        File fichero = new File(this.fDescargarFacturas.manifiestoActual.getRutaArchivoDescargueFacturas());

        /* Se arma el array de sentencias SQL para grabar las facturas  descargados */
        if (fichero.exists()) {
            this.fDescargarFacturas.manifiestoActual.setListaFacturasDescargadas(fichero);
            
            
            for (CFacturasPorManifiesto obj : this.fDescargarFacturas.manifiestoActual.getListaFacturasDescargadas()) {
                try {
                                   
                    sentenciasSQL_1.add(obj.getSentenciaInsertFcaturasDescargadasSQL());
     
                } catch (Exception ex) {
                    Logger.getLogger(HiloGuardarManifiestoDescargado.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }

        /* Se arma el array de sentencias SQL para grabar los productos  descargados */
        fichero = new File(this.fDescargarFacturas.manifiestoActual.getRutaArchivoDescargueporductosPorFactura());
        
        if (fichero.exists()) {
            this.fDescargarFacturas.manifiestoActual.setListaCProductosPorFacturaDescargados(fichero);

            for (CProductosPorFactura obj : this.fDescargarFacturas.manifiestoActual.getListaCProductosPorFacturaDescargados()) {
                //sentenciasSQL_1.add(obj.getSentenciaInsertSQL());

                /*Array de sentecias sql con los productos descargados*/
                sentenciasSQL_2.add(obj.getSentenciaInsertSQLDescargar());
            }

        }

        /* Se arma el array de sentencias SQL para grabar las Recogidas  descargados */
        fichero = new File(this.fDescargarFacturas.manifiestoActual.getRutArchivoRecogidasporManifiesto());
        if (fichero.exists()) {

            for (CRecogidasPorManifiesto obj : this.fDescargarFacturas.manifiestoActual.getListaDeRecogidasPorManifiesto()) {
                sentenciasSQL_1.add(obj.getSentenciaInsertSQL());
            }
        }

        /* Se arma el array de sentencias SQL para grabar los soportes de las consignaciones */
        fichero = new File(this.fDescargarFacturas.manifiestoActual.getRutaArchivoSoportesDeConsignaciones());
        if (fichero.exists()) {

            for (CSoportesConsignaciones obj : this.fDescargarFacturas.manifiestoActual.getListaDeSoportesConsignaciones()) {
                sentenciasSQL_1.add(obj.getSentenciaInsertSQL());
            }
        }
        
        
        try {
            /* Se graba las facturas descargadas, prductos por factura descargados, manifiesto  se actualiza */
            if (ini.insertarDatosRemotamente(sentenciasSQL_1,"HiloGuardarFacturasPorManifiesto")) {
                
                this.fDescargarFacturas.manifiestoActual.setListaDeAuxiliares();
                 this.fDescargarFacturas.manifiestoActual.setIsFree(1);
                    
                /*Se arman las sentencias sql para actualizar el  movimiento en el servidor  local a las facturas */
                for (CFacturasPorManifiesto obj : this.fDescargarFacturas.manifiestoActual.getListaFacturasDescargadas()) {
                    CFacturasDescargadas fd = new CFacturasDescargadas(ini);

                    sentenciasSQL_local.add(fd.actualizarEstadoFactura(obj.getIdTipoDeMovimiento(), obj.getNumeroFactura()));

                }
                /*Actualiza le estado de la facturas en el servidor local*/
                new Thread(new HiloGuardarSenteciasSql(ini, sentenciasSQL_local, true)).start();
                
                
                /* al pasar a estado 4 es decir cerrardo, se libera el manifiesto */
                this.fDescargarFacturas.manifiestoActual.actualizarManifiestoDeDistribucions();
                this.fDescargarFacturas.manifiestoActual.cerrarManifiestoDeDistribucions();
                
                
                /*Se borran los archivos con los registros */
                this.fDescargarFacturas.eliminararchivos();
                
                
                /*Mensajes de rigor*/
                System.out.println("Termina de guardar los datos de formulario descargar facturas  ");

                this.fDescargarFacturas.estaOcupadoGrabando = false;
                this.fDescargarFacturas.btnImprimir.setEnabled(true);
                this.fDescargarFacturas.btnGrabar.setEnabled(false);

                /*Guarda los productos descargados*/
                new Thread(new HiloGuardarSenteciasSql(ini, sentenciasSQL_2)).start();

                this.fDescargarFacturas.manifiestoActual.setListaFacturasDescargadas();
                 
                this.fDescargarFacturas.lblCirculoDeProgreso.setVisible(false);
                this.fDescargarFacturas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDescargarFacturas.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
                
                
                JOptionPane.showMessageDialog(this.fDescargarFacturas, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                this.fDescargarFacturas.jBtnMinuta.setEnabled(true);
                this.fDescargarFacturas.manifiestoActual.setEstadoManifiesto(4);

                this.fDescargarFacturas.btnImprimir.requestFocus();

                /* si presenta algun error en grabar el manifiesto  */
            } else {
                
                this.fDescargarFacturas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDescargarFacturas.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
                
                this.fDescargarFacturas.btnGrabar.setEnabled(true);
                JOptionPane.showMessageDialog(this.fDescargarFacturas, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(this.fDescargarFacturas, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            this.fDescargarFacturas.btnGrabar.setEnabled(true);
            this.fDescargarFacturas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDescargarFacturas.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

        } catch (Exception ex) {
            Logger.getLogger(HiloGuardarManifiestoDescargado.class.getName()).log(Level.SEVERE, null, ex);
            this.fDescargarFacturas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDescargarFacturas.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        }
       
        this.fDescargarFacturas.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        this.fDescargarFacturas.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
    }

    private void metodoNuevo(){
        this.fLiquidarManifiesto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fLiquidarManifiesto.jBtnGrabar.setIcon( new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        
        this.fLiquidarManifiesto.estaOcupadoGrabando = true;

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarManifiestoDescargado.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<String> sentenciasSQL_1; // Sentencias para guardar el descargue del manifiesto de la ruta.
        List<String> sentenciasSQL_2; // Sentecias que guarda los productos por factura descargadas
        List<String> sentenciasSQL_local;

        //this.fLiquidarManifiesto.conductorActual.setVistaFacturasEnDistribucion(this.fLiquidarManifiesto.listaFacturasDescargadas);
       // SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sentenciasSQL_1 = new ArrayList<>();
        sentenciasSQL_2 = new ArrayList<>();
        sentenciasSQL_local = new ArrayList<>();
        //String fechax;
        //fechax = fecha.format(new Date());

        this.fLiquidarManifiesto.cbxMovimientoFactura.setEnabled(false);
        this.fLiquidarManifiesto.txtNumeroDeFactura.setEnabled(false);
//        this.fLiquidarManifiesto.txtKilometrosEntrada.setEnabled(false);

        this.fLiquidarManifiesto.cbxCausalDeRechazoFactura.setEnabled(false);
        this.fLiquidarManifiesto.btnDescargarRechazoTotal.setEnabled(false);
        
        
        /* Se cambia el estado de manifiesto a manifiesto cerrado*/
        
        /* ********************************** SE ARMAR EL ARCHIVO SQL PARA GRABAR LOS DATOS DEL MANIFIESTO******************************** */
        File fichero ; //= new File(this.fLiquidarManifiesto.conductorActual.getRutaArchivoDescargueFacturas());

        /* Se arma el array de sentencias SQL para grabar las facturas  descargados */
//        if (fichero.exists()) {
//            this.fLiquidarManifiesto.conductorActual.setListaFacturasDescargadas(fichero);
//            
//            
//            for (CFacturasPorManifiesto obj : this.fLiquidarManifiesto.conductorActual.getListaFacturasDescargadas()) {
//                try {
//                                   
//                    sentenciasSQL_1.add(obj.getSentenciaInsertFcaturasDescargadasSQL());
//     
//                } catch (Exception ex) {
//                    Logger.getLogger(HiloGuardarManifiestoDescargado.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            }
//        }

        /* Se arma el array de sentencias SQL para grabar los productos  descargados */
//        fichero = new File(this.fLiquidarManifiesto.conductorActual.getRutaArchivoDescargueporductosPorFactura());
//        
//        if (fichero.exists()) {
//            this.fLiquidarManifiesto.conductorActual.setListaCProductosPorFacturaDescargados(fichero);
//
//            for (CProductosPorFactura obj : this.fLiquidarManifiesto.conductorActual.getListaCProductosPorFacturaDescargados()) {
//                //sentenciasSQL_1.add(obj.getSentenciaInsertSQL());
//
//                /*Array de sentecias sql con los productos descargados*/
//                sentenciasSQL_2.add(obj.getSentenciaInsertSQLDescargar());
//            }
//
//        }

        /* Se arma el array de sentencias SQL para grabar las Recogidas  descargados */
//        fichero = new File(this.fLiquidarManifiesto.conductorActual.getRutArchivoRecogidasporManifiesto());
//        if (fichero.exists()) {
//
//            for (CRecogidasPorManifiesto obj : this.fLiquidarManifiesto.conductorActual.getListaDeRecogidasPorManifiesto()) {
//                sentenciasSQL_1.add(obj.getSentenciaInsertSQL());
//            }
//        }

        /* Se arma el array de sentencias SQL para grabar los soportes de las consignaciones */
//        fichero = new File(this.fLiquidarManifiesto.conductorActual.getRutaArchivoSoportesDeConsignaciones());
//        if (fichero.exists()) {
//
//            for (CSoportesConsignaciones obj : this.fLiquidarManifiesto.conductorActual.getListaDeSoportesConsignaciones()) {
//                sentenciasSQL_1.add(obj.getSentenciaInsertSQL());
//            }
//        }
        
        
        try {
            /* Se graba las facturas descargadas, prductos por factura descargados, manifiesto  se actualiza */
            if (ini.insertarDatosRemotamente(sentenciasSQL_1,"HiloGuardarFacturasPorManifiesto")) {
                
               // this.fLiquidarManifiesto.conductorActual.setListaDeAuxiliares();
                this.fLiquidarManifiesto.manifiesto.setIsFree(1);
                    
                /*Se arman las sentencias sql para actualizar el  movimiento en el servidor  local a las facturas */
                for (CFacturasPorManifiesto obj : this.fLiquidarManifiesto.manifiesto.getListaFacturasDescargadas()) {
                    CFacturasDescargadas fd = new CFacturasDescargadas(ini);

                    sentenciasSQL_local.add(fd.actualizarEstadoFactura(obj.getIdTipoDeMovimiento(), obj.getNumeroFactura()));

                }
                /*Actualiza le estado de la facturas en el servidor local*/
                new Thread(new HiloGuardarSenteciasSql(ini, sentenciasSQL_local, true)).start();
                
                
                /* al pasar a estado 4 es decir cerrardo, se libera el manifiesto */
//                this.fLiquidarManifiesto.conductorActual.actualizarManifiestoDeDistribucions();
                this.fLiquidarManifiesto.manifiesto.cerrarManifiestoDeDistribucions();
                
                
                /*Se borran los archivos con los registros */
                //this.fLiquidarManifiesto.eliminararchivos();
                
                
                /*Mensajes de rigor*/
                System.out.println("Termina de guardar los datos de formulario descargar facturas  ");

                this.fLiquidarManifiesto.estaOcupadoGrabando = false;
                this.fLiquidarManifiesto.btnImprimir.setEnabled(true);
                this.fLiquidarManifiesto.btnGrabar.setEnabled(false);

                /*Guarda los productos descargados*/
                new Thread(new HiloGuardarSenteciasSql(ini, sentenciasSQL_2)).start();

//                this.fLiquidarManifiesto.conductorActual.setListaFacturasDescargadas();
                 
//                this.fLiquidarManifiesto.lblCirculoDeProgreso.setVisible(false);
                this.fLiquidarManifiesto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fLiquidarManifiesto.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
                
                
                JOptionPane.showMessageDialog(this.fLiquidarManifiesto, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                this.fLiquidarManifiesto.jBtnMinuta.setEnabled(true);
//                this.fLiquidarManifiesto.conductorActual.setEstadoManifiesto(4);

                this.fLiquidarManifiesto.btnImprimir.requestFocus();

                /* si presenta algun error en grabar el manifiesto  */
            } else {
                
                this.fLiquidarManifiesto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fLiquidarManifiesto.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
                
                this.fLiquidarManifiesto.btnGrabar.setEnabled(true);
                JOptionPane.showMessageDialog(this.fLiquidarManifiesto, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(this.fLiquidarManifiesto, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            this.fLiquidarManifiesto.btnGrabar.setEnabled(true);
            this.fLiquidarManifiesto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fLiquidarManifiesto.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

        } catch (Exception ex) {
            Logger.getLogger(HiloGuardarManifiestoDescargado.class.getName()).log(Level.SEVERE, null, ex);
            this.fLiquidarManifiesto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fLiquidarManifiesto.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        }
       
        this.fLiquidarManifiesto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        this.fLiquidarManifiesto.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

    }
    
}
