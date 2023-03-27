/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.FAdicionarPedidosNoReportados;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRutaConIntegrador;
import aplicacionlogistica.distribucion.formularios.Hielera.FDespachoHieleraConIntegracion;
import aplicacionlogistica.distribucion.formularios.Hielera.Threads.HiloFmanifestarPedidosHielera_2;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import java.awt.HeadlessException;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloGuardarFacturasPorManifiesto implements Runnable {

    public static boolean band = false;
    private final int tiempo = 5;
    Inicio ini;
    ArrayList<String> sentenciasSQL;

    FManifestarPedidosEnRuta fManifestarPedidosEnRuta = null;
    FAdicionarPedidosNoReportados fAdicionarPedidosNoReportados = null;
    FManifestarPedidosEnRutaConIntegrador fManifestarPedidosEnRutaConIntegrador = null;
    FDespachoHieleraConIntegracion fDespachoHieleraConIntegracion = null;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloGuardarFacturasPorManifiesto(Inicio ini, FManifestarPedidosEnRuta form) {

        this.fManifestarPedidosEnRuta = form;
        this.ini = ini;

    }

    public HiloGuardarFacturasPorManifiesto(Inicio ini, FAdicionarPedidosNoReportados fAdicionarPedidosNoReportados) {

        this.fAdicionarPedidosNoReportados = fAdicionarPedidosNoReportados;
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fManifestarPedidosEnRutaConIntegrador
     */
    public HiloGuardarFacturasPorManifiesto(Inicio ini, FManifestarPedidosEnRutaConIntegrador fManifestarPedidosEnRutaConIntegrador) {

        this.fManifestarPedidosEnRutaConIntegrador = fManifestarPedidosEnRutaConIntegrador;
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fDespachoHieleraConIntegracion
     */
    public HiloGuardarFacturasPorManifiesto(Inicio ini, FDespachoHieleraConIntegracion fDespachoHieleraConIntegracion) {

        this.fDespachoHieleraConIntegracion = fDespachoHieleraConIntegracion;
        this.ini = ini;

    }

    @Override
    public void run() {

        if (fManifestarPedidosEnRuta != null) {
            guardarPedidosEnManifiesto();
        }

        if (fAdicionarPedidosNoReportados != null) {
            guardarPedidosNoReportados();
        }

        if (fManifestarPedidosEnRutaConIntegrador != null) {
            guardarPedidosEnManifiesto();
        }

        if (fDespachoHieleraConIntegracion != null) {
            guardarPedidosEnManifiestoConIntegracion();
        }

    }

    private void guardarPedidosEnManifiestoConIntegracion() throws HeadlessException {
        sentenciasSQL = new ArrayList<>();
        String sentciaListaDeFacturas = "update facturas set isFree='0' where numeroFactura in(";

        this.fDespachoHieleraConIntegracion.btnImprimir.setEnabled(false);
        this.fDespachoHieleraConIntegracion.jBtnImprimir.setEnabled(false);

        this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        //this.fDespachoHieleraConIntegracion.repaint();

        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
        this.fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
        this.fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(false);

        this.fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(false);
        //this.fDespachoHieleraConIntegracion.pnlAgregarLista.setEnabled(false);

        this.fDespachoHieleraConIntegracion.estaOcupadoGrabando = true;
        double valorManifiesto = 0.0;
        double pesoKgManifiesto = 0.0;

        /*Se asigna al manifiesto el listado de los descuentos teniendo como fuente
        la cadena de las facturas por manifiesto del formulario origen*/
        //form.manifiestoActual.setListaDeDescuentos(fManifestarPedidosEnRuta.getCadenaDeFacturas());
        //form.manifiestoActual.setListaDeRecogidas(fManifestarPedidosEnRuta.getCadenaDeFacturas());
        try {

            /* SE CREA CADENA PARA GRABAR LOS REGISTROS EN LA BASE DE DATOS */
            File fichero = new File(fDespachoHieleraConIntegracion.manifiestoActual.getRutArchivofacturasporManifiesto());
//            if (fichero.exists()) {
//                fManifestarPedidosEnRuta.manifiestoActual.setListaFacturasPorManifiesto(fichero);
//
//            }

            /* Se crea un listado de las facturas que salen a distribucion en al cadena sql  y se actualiza
la tabla local fcaturas en el campo isFree=0 para que no se pueda sacar la
factura a distribucion */

 /*Controla cantidad de registros por ciclo */
            int y = 0;
            /*cuenta los ciclos para la barra de progreso*/
            int x = (int) fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size() / 90;

            /*controla la barra de progreso*/
            int z = 0;
            this.fDespachoHieleraConIntegracion.valorDespBarraProgreso = z;
            int i = 1;
            //fDespachoHieleraConIntegracion.brrProgreso.setValue(z);

            for (CFacturasPorManifiesto obj : fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto()) {
                if (y == x) {
                    this.fDespachoHieleraConIntegracion.valorDespBarraProgreso = z++;
                    y = 0;
                }
                obj.setAdherencia(i);
                sentciaListaDeFacturas += "'" + obj.getNumeroFactura() + "',";
                sentenciasSQL.add(obj.getSentenciaInsertSQL());

                /* Se calcula el valor a recudar en el manifiesto */
                valorManifiesto += obj.getValorARecaudarFactura();

                /*se calcula el peso de las facturas del manifiesto*/
                pesoKgManifiesto += obj.getPesoFactura();

                this.fDespachoHieleraConIntegracion.repaint();
                i++;
                y++;
            }
            fDespachoHieleraConIntegracion.manifiestoActual.setCantidadPedidos(fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size());
            fDespachoHieleraConIntegracion.manifiestoActual.setValorTotalManifiesto(valorManifiesto);

            //fManifestarPedidosEnRuta.manifiestoActual.setEstadoManifiesto(3);
            sentciaListaDeFacturas = sentciaListaDeFacturas.substring(0, sentciaListaDeFacturas.length() - 1);
            sentciaListaDeFacturas = sentciaListaDeFacturas + ");";

//this.fManifestarPedidosEnRuta.manifiestoActual.setValorTotalManifiesto(valorManifiesto);
/*Se deshabilita la factura localmente para informar que no se pueden
sacar a distribucion en otro manifiesto, yq que se encuentra ya en una
ruta*/
            sentenciasSQL.add(sentciaListaDeFacturas);

            /*Se graban los registros en la Base de datos
              SE GRABAN LOS REGISTROS DE FACTURAS POR MANIFIESTO  EN LA BBDD REMOTA */
            if (ini.insertarDatosRemotamente(sentenciasSQL, "HiloGuardarFacturasPorManifiesto")) {

                fDespachoHieleraConIntegracion.manifiestoActual.setEstadoManifiesto(3);

                fDespachoHieleraConIntegracion.manifiestoActual.actualizarManifiestoDeDistribucions();
                fDespachoHieleraConIntegracion.manifiestoActual.setPesoKgManifiesto(pesoKgManifiesto);

                fDespachoHieleraConIntegracion.manifiestoActual.setListaFacturasPorManifiesto();
                fDespachoHieleraConIntegracion.band = false;

                fDespachoHieleraConIntegracion.manifiestoActual.setListaDeDescuentos();
                fDespachoHieleraConIntegracion.manifiestoActual.setListaDeRecogidas();

                /*Se deshabilita la factura localmente para informar que no se pueden
                 sacar a distribucion en otro manifiesto, ya que se encuentra  en una ruta*/
                ini.insertarDatosLocalmente(sentciaListaDeFacturas);

                /* actualiza el campo de texto del formulario */
                fDespachoHieleraConIntegracion.txtPlaca.setText(fDespachoHieleraConIntegracion.manifiestoActual.getVehiculo());
                //fDespachoHieleraConIntegracion.txtPlaca.setEnabled(true);
                // fDespachoHieleraConIntegracion.txtPlaca.setEditable(true);

                for (int k = 0; k < fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getRowCount(); k++) {
                    fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(k + 1, k, 0);
                }

                /*Si el cliente esta habilitado para enviar los SMS a los clientes de las facturas*/
                if (ini.enviaSMS() == 1) {
                    // hila para enviar sms a los clientes.
                }

                /* SE HABILITA BOTON DE IMPRIMIR */
                this.fDespachoHieleraConIntegracion.btnImprimir.setEnabled(true);
                this.fDespachoHieleraConIntegracion.jBtnImprimir.setEnabled(true);
                this.fDespachoHieleraConIntegracion.jBtnMinuta.setEnabled(true);

                /* SE DESHABILITA EL BOTON DE GRABAR */
                this.fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
                this.fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
                this.fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(false);

                this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

                this.fDespachoHieleraConIntegracion.repaint();

                /*Se elimina el archivo temporal */
                fichero = new File(fDespachoHieleraConIntegracion.manifiestoActual.getRutArchivofacturasporManifiesto());
                fichero.delete();

                new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 3)).start();
                
                

                this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

                /*Se refresca la vista para manifestarle a otro conductor */
                 new Thread(new HiloFmanifestarPedidosHielera_2(ini, fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2, "refrescarManifiestos")).start();
                
                //JOptionPane.showMessageDialog(this.fDespachoHieleraConIntegracion, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                fDespachoHieleraConIntegracion.cbxPrefijos.setEnabled(true);
                fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(true);
                fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEditable(true);

                fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();
                fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();

            } else {
                this.fDespachoHieleraConIntegracion.btnGrabar.setEnabled(true);
                this.fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(true);
                this.fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(true);

                this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

                JOptionPane.showMessageDialog(this.fDespachoHieleraConIntegracion, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);

            }

        } catch (NumberFormatException | HeadlessException ex) {

            this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

            JOptionPane.showMessageDialog(this.fManifestarPedidosEnRuta, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);

            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex + " \nSe genero error en guardar los datos");


            /*Se libera el manifiesto para que se pueda usar  */
            this.fDespachoHieleraConIntegracion.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/
            ;
        } catch (SQLException ex) {
            this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

            JOptionPane.showMessageDialog(this.fDespachoHieleraConIntegracion, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this.fDespachoHieleraConIntegracion, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            /*Se libera el manifiesto para que se pueda usar  */
            this.fDespachoHieleraConIntegracion.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        } catch (Exception ex) {
            this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

            JOptionPane.showMessageDialog(this.fDespachoHieleraConIntegracion, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);

            /*Se libera el manifiesto para que se pueda usar  */
            this.fDespachoHieleraConIntegracion.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        }

        this.fDespachoHieleraConIntegracion.estaOcupadoGrabando = false;
        //this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/grabar.png"))); // NOI18N
    }

    private void guardarPedidosEnManifiestoConIntegracion2() throws HeadlessException {
        sentenciasSQL = new ArrayList<>();
        String sentciaListaDeFacturas = "update facturas set isFree='0' where numeroFactura ='" 
                + this.fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura() + "';"; 

        this.fDespachoHieleraConIntegracion.btnImprimir.setEnabled(false);
        this.fDespachoHieleraConIntegracion.jBtnImprimir.setEnabled(false);

        this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        //this.fDespachoHieleraConIntegracion.repaint();

        try {
            Thread.sleep(50);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
        this.fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
        this.fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(false);

        this.fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(false);
        //this.fDespachoHieleraConIntegracion.pnlAgregarLista.setEnabled(false);

        this.fDespachoHieleraConIntegracion.estaOcupadoGrabando = true;
        double valorManifiesto = 0.0;
        double pesoKgManifiesto = 0.0;

        /*Se asigna al manifiesto el listado de los descuentos teniendo como fuente
        la cadena de las facturas por manifiesto del formulario origen*/
        //form.manifiestoActual.setListaDeDescuentos(fManifestarPedidosEnRuta.getCadenaDeFacturas());
        //form.manifiestoActual.setListaDeRecogidas(fManifestarPedidosEnRuta.getCadenaDeFacturas());
        try {

            /* SE CREA CADENA PARA GRABAR LOS REGISTROS EN LA BASE DE DATOS */
 //           File fichero = new File(fDespachoHieleraConIntegracion.manifiestoActual.getRutArchivofacturasporManifiesto());
//            if (fichero.exists()) {
//                fManifestarPedidosEnRuta.manifiestoActual.setListaFacturasPorManifiesto(fichero);
//
//            }

            /* Se crea un listado de las facturas que salen a distribucion en al cadena sql  y se actualiza
la tabla local fcaturas en el campo isFree=0 para que no se pueda sacar la
factura a distribucion */

 /*Controla cantidad de registros por ciclo */
            int y = 0;
            /*cuenta los ciclos para la barra de progreso*/
            int x = (int) fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size() / 90;

            /*controla la barra de progreso*/
            int z = 0;
            this.fDespachoHieleraConIntegracion.valorDespBarraProgreso = z;
            int i = 1;
            //fDespachoHieleraConIntegracion.brrProgreso.setValue(z);

            for (CFacturasPorManifiesto obj : fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto()) {
                if (y == x) {
                    this.fDespachoHieleraConIntegracion.valorDespBarraProgreso = z++;
                    y = 0;
                }
                obj.setAdherencia(i);
              //  sentciaListaDeFacturas += "'" + obj.getNumeroFactura() + "',";
                sentenciasSQL.add(obj.getSentenciaInsertSQL());

                /* Se calcula el valor a recudar en el manifiesto */
                valorManifiesto += obj.getValorARecaudarFactura();

                /*se calcula el peso de las facturas del manifiesto*/
                pesoKgManifiesto += obj.getPesoFactura();

                this.fDespachoHieleraConIntegracion.repaint();
                i++;
                y++;
            }
            fDespachoHieleraConIntegracion.manifiestoActual.setCantidadPedidos(fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size());
            fDespachoHieleraConIntegracion.manifiestoActual.setValorTotalManifiesto(valorManifiesto);

            //fManifestarPedidosEnRuta.manifiestoActual.setEstadoManifiesto(3);
            //sentciaListaDeFacturas = sentciaListaDeFacturas.substring(0, sentciaListaDeFacturas.length() - 1);
            //sentciaListaDeFacturas = sentciaListaDeFacturas + ");";

//this.fManifestarPedidosEnRuta.manifiestoActual.setValorTotalManifiesto(valorManifiesto);
/*Se deshabilita la factura localmente para informar que no se pueden
sacar a distribucion en otro manifiesto, yq que se encuentra ya en una
ruta*/
            sentenciasSQL.add(sentciaListaDeFacturas);

            /*Se graban los registros en la Base de datos
              SE GRABAN LOS REGISTROS DE FACTURAS POR MANIFIESTO  EN LA BBDD REMOTA */
            if (ini.insertarDatosRemotamente(sentenciasSQL, "HiloGuardarFacturasPorManifiesto")) {

                fDespachoHieleraConIntegracion.manifiestoActual.setEstadoManifiesto(3);

                fDespachoHieleraConIntegracion.manifiestoActual.actualizarManifiestoDeDistribucions();
                fDespachoHieleraConIntegracion.manifiestoActual.setPesoKgManifiesto(pesoKgManifiesto);

                fDespachoHieleraConIntegracion.manifiestoActual.setListaFacturasPorManifiesto();
                fDespachoHieleraConIntegracion.band = false;

                fDespachoHieleraConIntegracion.manifiestoActual.setListaDeDescuentos();
                fDespachoHieleraConIntegracion.manifiestoActual.setListaDeRecogidas();

                /*Se deshabilita la factura localmente para informar que no se pueden
                 sacar a distribucion en otro manifiesto, ya que se encuentra  en una ruta*/
                ini.insertarDatosLocalmente(sentciaListaDeFacturas);

                /* actualiza el campo de texto del formulario */
                fDespachoHieleraConIntegracion.txtPlaca.setText(fDespachoHieleraConIntegracion.manifiestoActual.getVehiculo());
                //fDespachoHieleraConIntegracion.txtPlaca.setEnabled(true);
                // fDespachoHieleraConIntegracion.txtPlaca.setEditable(true);

                for (int k = 0; k < fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getRowCount(); k++) {
                    fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(k + 1, k, 0);
                }

                /*Si el cliente esta habilitado para enviar los SMS a los clientes de las facturas*/
                if (ini.enviaSMS() == 1) {
                    // hila para enviar sms a los clientes.
                }

                /* SE HABILITA BOTON DE IMPRIMIR */
                this.fDespachoHieleraConIntegracion.btnImprimir.setEnabled(true);
                this.fDespachoHieleraConIntegracion.jBtnImprimir.setEnabled(true);
                this.fDespachoHieleraConIntegracion.jBtnMinuta.setEnabled(true);

                /* SE DESHABILITA EL BOTON DE GRABAR */
                this.fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
                this.fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
                this.fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(false);

                this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

                this.fDespachoHieleraConIntegracion.repaint();

//                /*Se elimina el archivo temporal */
//                fichero = new File(fDespachoHieleraConIntegracion.manifiestoActual.getRutArchivofacturasporManifiesto());
//                fichero.delete();

                new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 3)).start();
                
                

                this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

                /*Se refresca la vista para manifestarle a otro conductor */
                 new Thread(new HiloFmanifestarPedidosHielera_2(ini, fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2, "refrescarManifiestos")).start();
                
                //JOptionPane.showMessageDialog(this.fDespachoHieleraConIntegracion, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                fDespachoHieleraConIntegracion.cbxPrefijos.setEnabled(true);
                fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(true);
                fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEditable(true);

                fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();
                fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();

            } else {
                this.fDespachoHieleraConIntegracion.btnGrabar.setEnabled(true);
                this.fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(true);
                this.fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(true);

                this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

                JOptionPane.showMessageDialog(this.fDespachoHieleraConIntegracion, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);

            }

        } catch (NumberFormatException | HeadlessException ex) {

            this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

            JOptionPane.showMessageDialog(this.fManifestarPedidosEnRuta, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);

            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex + " \nSe genero error en guardar los datos");


            /*Se libera el manifiesto para que se pueda usar  */
            this.fDespachoHieleraConIntegracion.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/
            ;
        } catch (SQLException ex) {
            this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

            JOptionPane.showMessageDialog(this.fDespachoHieleraConIntegracion, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this.fDespachoHieleraConIntegracion, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            /*Se libera el manifiesto para que se pueda usar  */
            this.fDespachoHieleraConIntegracion.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        } catch (Exception ex) {
            this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

            JOptionPane.showMessageDialog(this.fDespachoHieleraConIntegracion, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);

            /*Se libera el manifiesto para que se pueda usar  */
            this.fDespachoHieleraConIntegracion.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        }

        this.fDespachoHieleraConIntegracion.estaOcupadoGrabando = false;
        //this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/grabar.png"))); // NOI18N
    }

    
    private void guardarPedidosEnManifiesto() throws HeadlessException {
        sentenciasSQL = new ArrayList<>();
        String sentciaListaDeFacturas = "update facturas set isFree='0' where numeroFactura in(";

        this.fManifestarPedidosEnRuta.btnImprimir.setEnabled(false);
        this.fManifestarPedidosEnRuta.jBtnImprimir.setEnabled(false);

        this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        this.fManifestarPedidosEnRuta.repaint();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.fManifestarPedidosEnRuta.btnGrabar.setEnabled(false);
        this.fManifestarPedidosEnRuta.jBtnGrabar.setEnabled(false);
        this.fManifestarPedidosEnRuta.jBtnObservaciones.setEnabled(false);

        this.fManifestarPedidosEnRuta.txtNumeroDeFactura.setEnabled(false);
        this.fManifestarPedidosEnRuta.pnlAgregarLista.setEnabled(false);

        this.fManifestarPedidosEnRuta.estaOcupadoGrabando = true;
        double valorManifiesto = 0.0;
        double pesoKgManifiesto = 0.0;

        /*Se asigna al manifiesto el listado de los descuentos teniendo como fuente
        la cadena de las facturas por manifiesto del formulario origen*/
        //form.manifiestoActual.setListaDeDescuentos(fManifestarPedidosEnRuta.getCadenaDeFacturas());
        //form.manifiestoActual.setListaDeRecogidas(fManifestarPedidosEnRuta.getCadenaDeFacturas());
        try {

            /* SE CREA CADENA PARA GRABAR LOS REGISTROS EN LA BASE DE DATOS */
            File fichero = new File(fManifestarPedidosEnRuta.manifiestoActual.getRutArchivofacturasporManifiesto());
//            if (fichero.exists()) {
//                fManifestarPedidosEnRuta.manifiestoActual.setListaFacturasPorManifiesto(fichero);
//
//            }

            /* Se crea un listado de las facturas que salen a distribucion en al cadena sql  y se actualiza
la tabla local fcaturas en el campo isFree=0 para que no se pueda sacar la
factura a distribucion */

 /*Controla cantidad de registros por ciclo */
            int y = 0;
            /*cuenta los ciclos para la barra de progreso*/
            int x = (int) fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto().size() / 90;

            /*controla la barra de progreso*/
            int z = 0;
            this.fManifestarPedidosEnRuta.valorDespBarraProgreso = z;
            int i = 1;
            fManifestarPedidosEnRuta.brrProgreso.setValue(z);

            for (CFacturasPorManifiesto obj : fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto()) {
                if (y == x) {
                    this.fManifestarPedidosEnRuta.valorDespBarraProgreso = z++;
                    y = 0;
                }
                obj.setAdherencia(i);
                sentciaListaDeFacturas += "'" + obj.getNumeroFactura() + "',";
                sentenciasSQL.add(obj.getSentenciaInsertSQL());

                /* Se calcula el valor a recudar en el manifiesto */
                valorManifiesto += obj.getValorARecaudarFactura();

                /*se calcula el peso de las facturas del manifiesto*/
                pesoKgManifiesto += obj.getPesoFactura();

                this.fManifestarPedidosEnRuta.repaint();
                i++;
                y++;
            }
            fManifestarPedidosEnRuta.manifiestoActual.setCantidadPedidos(fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto().size());
            fManifestarPedidosEnRuta.manifiestoActual.setValorTotalManifiesto(valorManifiesto);

            //fManifestarPedidosEnRuta.manifiestoActual.setEstadoManifiesto(3);
            sentciaListaDeFacturas = sentciaListaDeFacturas.substring(0, sentciaListaDeFacturas.length() - 1);
            sentciaListaDeFacturas = sentciaListaDeFacturas + ");";

//this.fManifestarPedidosEnRuta.manifiestoActual.setValorTotalManifiesto(valorManifiesto);
/*Se deshabilita la factura localmente para informar que no se pueden
sacar a distribucion en otro manifiesto, yq que se encuentra ya en una
ruta*/
            sentenciasSQL.add(sentciaListaDeFacturas);

            /*SE ACTUALIZA EL ESTADO DEL MANIFIESTO CON DATOS NUEVOS */
//this.fManifestarPedidosEnRuta.manifiestoActual.setIsFree(1);
/*   Se libera el manifiesto*/
//this.fManifestarPedidosEnRuta.manifiestoActual.setValorTotalManifiesto(valorManifiesto);
/* Se actualiza el valor total del manifiesto*/
//this.fManifestarPedidosEnRuta.manifiestoActual.setEstadoManifiesto(3);
/* Se actualiza el estado al manifiesto para indicar que está en distribución*/
//            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            java.util.Date fechaEnviar = new Date();
//            String fechaActual = formato.format(fechaEnviar);
//this.fManifestarPedidosEnRuta.manifiestoActual.setHoraDeDespacho(fechaActual);
/* Se actualiza la fecha y hora de despacho a distribución*/
//this.fManifestarPedidosEnRuta.manifiestoActual.setHoraDeLiquidacion(fechaActual);

            /*Se graban los registros en la Base de datos*/
//sentenciasSQL.add(this.fManifestarPedidosEnRuta.manifiestoActual.getSentenciaInsertSQL());
//            this.fManifestarPedidosEnRuta.carro.setConductor(this.fManifestarPedidosEnRuta.conductor.getCedula());
//            this.fManifestarPedidosEnRuta.carro.setKilometraje(Integer.parseInt(this.fManifestarPedidosEnRuta.txtKmDeSalida.getText().trim()));
//            sentenciasSQL.add(this.fManifestarPedidosEnRuta.carro.getSentenciaInsertSQL());
            /* SE GRABAN LOS REGISTROS DE FACTURAS POR MANIFIESTO  EN LA BBDD REMOTA */
            if (ini.insertarDatosRemotamente(sentenciasSQL, "HiloGuardarFacturasPorManifiesto")) {

                fManifestarPedidosEnRuta.manifiestoActual.setEstadoManifiesto(3);

                fManifestarPedidosEnRuta.manifiestoActual.actualizarManifiestoDeDistribucions();
                fManifestarPedidosEnRuta.manifiestoActual.setPesoKgManifiesto(pesoKgManifiesto);

                fManifestarPedidosEnRuta.manifiestoActual.setListaFacturasPorManifiesto();
                fManifestarPedidosEnRuta.band = false;

                fManifestarPedidosEnRuta.manifiestoActual.setListaDeDescuentos();
                fManifestarPedidosEnRuta.manifiestoActual.setListaDeRecogidas();

                //form.manifiestoActual.actualizarManifiestoDeDistribucions();
                /*Se deshabilita la factura localmente para informar que no se pueden
                 sacar a distribucion en otro manifiesto, ya que se encuentra  en una ruta*/
                ini.insertarDatosLocalmente(sentciaListaDeFacturas);

                /*Si el cliente esta habilitado para enviar los SMS a los clientes de las facturas*/
                if (ini.enviaSMS() == 1) {
                    // hila para enviar sms a los clientes.
                }

                /* SE HABILITA BOTON DE IMPRIMIR */
                this.fManifestarPedidosEnRuta.btnImprimir.setEnabled(true);
                this.fManifestarPedidosEnRuta.jBtnImprimir.setEnabled(true);
                this.fManifestarPedidosEnRuta.jBtnMinuta.setEnabled(true);

                /* SE DESHABILITA EL BOTON DE GRABAR */
                this.fManifestarPedidosEnRuta.btnGrabar.setEnabled(false);
                this.fManifestarPedidosEnRuta.jBtnGrabar.setEnabled(false);
                this.fManifestarPedidosEnRuta.jBtnObservaciones.setEnabled(false);

                this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

                this.fManifestarPedidosEnRuta.repaint();

                /*Se elimina el archivo temporal */
                fichero = new File(fManifestarPedidosEnRuta.manifiestoActual.getRutArchivofacturasporManifiesto());
                fichero.delete();

                new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 3)).start();

                JOptionPane.showMessageDialog(this.fManifestarPedidosEnRuta, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);

            } else {
                this.fManifestarPedidosEnRuta.btnGrabar.setEnabled(true);
                this.fManifestarPedidosEnRuta.jBtnGrabar.setEnabled(true);
                this.fManifestarPedidosEnRuta.jBtnObservaciones.setEnabled(true);

                this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

                JOptionPane.showMessageDialog(this.fManifestarPedidosEnRuta, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);

            }

        } catch (NumberFormatException | HeadlessException ex) {
            this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

            JOptionPane.showMessageDialog(this.fManifestarPedidosEnRuta, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);

            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);


            /*Se libera el manifiesto para que se pueda usar  */
            this.fManifestarPedidosEnRuta.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/
            ;
        } catch (SQLException ex) {
            this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

            JOptionPane.showMessageDialog(this.fManifestarPedidosEnRuta, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this.fManifestarPedidosEnRuta, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            /*Se libera el manifiesto para que se pueda usar  */
            this.fManifestarPedidosEnRuta.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        } catch (Exception ex) {
            this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fManifestarPedidosEnRuta.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

            JOptionPane.showMessageDialog(this.fManifestarPedidosEnRuta, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);

            /*Se libera el manifiesto para que se pueda usar  */
            this.fManifestarPedidosEnRuta.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        }

        this.fManifestarPedidosEnRuta.estaOcupadoGrabando = false;
        //this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/grabar.png"))); // NOI18N
    }

    private void guardarPedidosNoReportados() throws HeadlessException {
        sentenciasSQL = new ArrayList<>();
        String sentciaListaDeFacturas = "update facturas set isFree='0' where numeroFactura in(";

        this.fAdicionarPedidosNoReportados.btnImprimir.setEnabled(false);
        this.fAdicionarPedidosNoReportados.jBtnImprimir.setEnabled(false);

        this.fAdicionarPedidosNoReportados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fAdicionarPedidosNoReportados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fAdicionarPedidosNoReportados.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        this.fAdicionarPedidosNoReportados.repaint();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.fAdicionarPedidosNoReportados.btnGrabar.setEnabled(false);
        this.fAdicionarPedidosNoReportados.jBtnGrabar.setEnabled(false);

        this.fAdicionarPedidosNoReportados.txtNumeroDeFactura.setEnabled(false);

// this.fAdicionarPedidosNoReportados.pnlAgregarLista.setEnabled(false);
        this.fAdicionarPedidosNoReportados.estaOcupadoGrabando = true;
        double valorManifiesto = 0.0;
        double pesoKgManifiesto = 0.0;

        /*Se asigna al manifiesto el listado de los descuentos teniendo como fuente
        la cadena de las facturas por manifiesto del formulario origen*/
        //form.manifiestoActual.setListaDeDescuentos(fManifestarPedidosEnRuta.getCadenaDeFacturas());
        //form.manifiestoActual.setListaDeRecogidas(fManifestarPedidosEnRuta.getCadenaDeFacturas());
        try {

            /* SE CREA CADENA PARA GRABAR LOS REGISTROS EN LA BASE DE DATOS */
            File fichero = new File(fAdicionarPedidosNoReportados.manifiestoActual.getRutArchivofacturasporManifiesto());
//            if (fichero.exists()) {
//                fManifestarPedidosEnRuta.manifiestoActual.setListaFacturasPorManifiesto(fichero);
//
//            }

            /* Se crea un listado de las facturas que salen a distribucion en al cadena sql  y se actualiza
la tabla local fcaturas en el campo isFree=0 para que no se pueda sacar la
factura a distribucion */

 /*Controla cantidad de registros ppor ciclo */
            int y = 0;
            /*cuenta los ciclos para la barra de progreso*/
            int x = (int) fAdicionarPedidosNoReportados.manifiestoActual.getListaFacturasPorManifiesto().size() / 90;

            /*controla la barra de progreso*/
            int z = 0;
            this.fAdicionarPedidosNoReportados.valorDespBarraProgreso = z;
            int i = 1;
            fAdicionarPedidosNoReportados.brrProgreso.setValue(z);

            for (CFacturasPorManifiesto obj : fAdicionarPedidosNoReportados.manifiestoActual.getListaFacturasPorManifiesto()) {
                if (y == x) {
                    this.fAdicionarPedidosNoReportados.valorDespBarraProgreso = z++;
                    y = 0;
                }
                obj.setAdherencia(i);
                sentciaListaDeFacturas += "'" + obj.getNumeroFactura() + "',";
                sentenciasSQL.add(obj.getSentenciaInsertSQL());

                /* Se calcula el valor a recudar en el manifiesto */
                valorManifiesto += obj.getValorARecaudarFactura();

                /*se calcula el peso de las facturas del manifiesto*/
                pesoKgManifiesto += obj.getPesoFactura();

                this.fAdicionarPedidosNoReportados.repaint();
                i++;
                y++;
            }

            fAdicionarPedidosNoReportados.manifiestoActual.setValorTotalManifiesto(valorManifiesto);
            fAdicionarPedidosNoReportados.manifiestoActual.setPesoKgManifiesto(pesoKgManifiesto);
            fAdicionarPedidosNoReportados.manifiestoActual.setEstadoManifiesto(3);

            sentciaListaDeFacturas = sentciaListaDeFacturas.substring(0, sentciaListaDeFacturas.length() - 1);
            sentciaListaDeFacturas = sentciaListaDeFacturas + ");";

//this.fManifestarPedidosEnRuta.manifiestoActual.setValorTotalManifiesto(valorManifiesto);
/*Se deshabilita la factura localmente para informar que no se pueden
sacar a distribucion en otro manifiesto, yq que se encuentra ya en una
ruta*/
            sentenciasSQL.add(sentciaListaDeFacturas);

            /*SE ACTUALIZA EL ESTADO DEL MANIFIESTO CON DATOS NUEVOS */
//this.fManifestarPedidosEnRuta.manifiestoActual.setIsFree(1);
/*   Se libera el manifiesto*/
//this.fManifestarPedidosEnRuta.manifiestoActual.setValorTotalManifiesto(valorManifiesto);
/* Se actualiza el valor total del manifiesto*/
//this.fManifestarPedidosEnRuta.manifiestoActual.setEstadoManifiesto(3);
/* Se actualiza el estado al manifiesto para indicar que está en distribución*/
            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date fechaEnviar = new Date();
            String fechaActual = formato.format(fechaEnviar);

//this.fManifestarPedidosEnRuta.manifiestoActual.setHoraDeDespacho(fechaActual);
/* Se actualiza la fecha y hora de despacho a distribución*/
//this.fManifestarPedidosEnRuta.manifiestoActual.setHoraDeLiquidacion(fechaActual);

            /*Se graban los registros en la Base de datos*/
//sentenciasSQL.add(this.fManifestarPedidosEnRuta.manifiestoActual.getSentenciaInsertSQL());
//            this.fManifestarPedidosEnRuta.carro.setConductor(this.fManifestarPedidosEnRuta.conductor.getCedula());
//            this.fManifestarPedidosEnRuta.carro.setKilometraje(Integer.parseInt(this.fManifestarPedidosEnRuta.txtKmDeSalida.getText().trim()));
//            sentenciasSQL.add(this.fManifestarPedidosEnRuta.carro.getSentenciaInsertSQL());
            /* SE GRABAN LOS REGISTROS DE FACTURAS POR MANIFIESTO  EN LA BBDD REMOTA */
            if (ini.insertarDatosRemotamente(sentenciasSQL, "HiloGuardarFacturasPorManifiesto")) {
                // fManifestarPedidosEnRuta.manifiestoActual.setEstadoManifiesto(3);
                fAdicionarPedidosNoReportados.manifiestoActual.setListaFacturasPorManifiesto();
                fAdicionarPedidosNoReportados.band = false;

                fAdicionarPedidosNoReportados.manifiestoActual.setListaDeDescuentos();
                fAdicionarPedidosNoReportados.manifiestoActual.setListaDeRecogidas();

                //form.manifiestoActual.actualizarManifiestoDeDistribucions();
                /*Se deshabilita la factura localmente para informar que no se pueden
    sacar a distribucion en otro manifiesto, ya que se encuentra  en una
    ruta*/
                ini.insertarDatosLocalmente(sentciaListaDeFacturas);

                /*Si el cliente esta habilitado para enviar los SMS a los clientes
    de las facturas*/
                if (ini.enviaSMS() == 1) {
                    // hila para enviar sms a los clientes.
                }

                /* SE HABILITA BOTON DE IMPRIMIR */
                this.fAdicionarPedidosNoReportados.btnImprimir.setEnabled(true);
                this.fAdicionarPedidosNoReportados.jBtnImprimir.setEnabled(true);

                /* SE DESHABILITA EL BOTON DE GRABAR */
                this.fAdicionarPedidosNoReportados.btnGrabar.setEnabled(false);
                this.fAdicionarPedidosNoReportados.jBtnGrabar.setEnabled(false);

                //this.fManifestarPedidosEnRuta.jBtnMinuta.setEnabled(true);
                this.fAdicionarPedidosNoReportados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fAdicionarPedidosNoReportados.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

                this.fAdicionarPedidosNoReportados.repaint();

                /*Se elimina el archivo temporal */
                fichero = new File(fAdicionarPedidosNoReportados.manifiestoActual.getRutArchivofacturasporManifiesto());
                fichero.delete();

                new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 3)).start();

                JOptionPane.showMessageDialog(this.fAdicionarPedidosNoReportados, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);

            } else {
                this.fAdicionarPedidosNoReportados.btnGrabar.setEnabled(true);
                this.fAdicionarPedidosNoReportados.jBtnGrabar.setEnabled(true);

                this.fAdicionarPedidosNoReportados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fAdicionarPedidosNoReportados.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                JOptionPane.showMessageDialog(this.fAdicionarPedidosNoReportados, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);

            }

        } catch (NumberFormatException | HeadlessException ex) {
            this.fAdicionarPedidosNoReportados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fAdicionarPedidosNoReportados.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

            JOptionPane.showMessageDialog(this.fAdicionarPedidosNoReportados, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);


            /*Se libera el manifiesto para que se pueda usar  */
            this.fAdicionarPedidosNoReportados.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/
            ;
        } catch (SQLException ex) {
            this.fAdicionarPedidosNoReportados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fAdicionarPedidosNoReportados.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

            JOptionPane.showMessageDialog(this.fAdicionarPedidosNoReportados, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this.fAdicionarPedidosNoReportados, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            /*Se libera el manifiesto para que se pueda usar  */
            this.fAdicionarPedidosNoReportados.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        } catch (Exception ex) {
            this.fAdicionarPedidosNoReportados.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fAdicionarPedidosNoReportados.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

            JOptionPane.showMessageDialog(this.fAdicionarPedidosNoReportados, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);

            /*Se libera el manifiesto para que se pueda usar  */
            this.fAdicionarPedidosNoReportados.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        }

        this.fAdicionarPedidosNoReportados.estaOcupadoGrabando = false;
        //this.fManifestarPedidosEnRuta.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/grabar.png"))); // NOI18N
    }

    private void guardarPedidosEnManifiestoConIntegrador() throws HeadlessException {
        sentenciasSQL = new ArrayList<>();
        String sentciaListaDeFacturas = "update facturas set isFree='0' where numeroFactura in(";

        this.fManifestarPedidosEnRutaConIntegrador.btnImprimir.setEnabled(false);
        this.fManifestarPedidosEnRutaConIntegrador.jBtnImprimir.setEnabled(false);

        this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        this.fManifestarPedidosEnRutaConIntegrador.repaint();

        try {
            Thread.sleep(300);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setEnabled(false);
        this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setEnabled(false);
        this.fManifestarPedidosEnRutaConIntegrador.jBtnObservaciones.setEnabled(false);

        this.fManifestarPedidosEnRutaConIntegrador.txtNumeroDeFactura.setEnabled(false);
        this.fManifestarPedidosEnRutaConIntegrador.pnlAgregarLista.setEnabled(false);

        this.fManifestarPedidosEnRutaConIntegrador.estaOcupadoGrabando = true;
        double valorManifiesto = 0.0;
        double pesoKgManifiesto = 0.0;

        /*Se asigna al manifiesto el listado de los descuentos teniendo como fuente
        la cadena de las facturas por manifiesto del formulario origen*/
        //form.manifiestoActual.setListaDeDescuentos(fManifestarPedidosEnRutaConIntegrador.getCadenaDeFacturas());
        //form.manifiestoActual.setListaDeRecogidas(fManifestarPedidosEnRutaConIntegrador.getCadenaDeFacturas());
        try {

            /* SE CREA CADENA PARA GRABAR LOS REGISTROS EN LA BASE DE DATOS */
            File fichero = new File(fManifestarPedidosEnRutaConIntegrador.manifiestoActual.getRutArchivofacturasporManifiesto());
//            if (fichero.exists()) {
//                fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setListaFacturasPorManifiesto(fichero);
//
//            }

            /* Se crea un listado de las facturas que salen a distribucion en al cadena sql  y se actualiza
la tabla local fcaturas en el campo isFree=0 para que no se pueda sacar la
factura a distribucion */

 /*Controla cantidad de registros por ciclo */
            int y = 0;
            /*cuenta los ciclos para la barra de progreso*/
            int x = (int) fManifestarPedidosEnRutaConIntegrador.manifiestoActual.getListaFacturasPorManifiesto().size() / 90;

            /*controla la barra de progreso*/
            int z = 0;
            this.fManifestarPedidosEnRutaConIntegrador.valorDespBarraProgreso = z;
            int i = 1;
            fManifestarPedidosEnRutaConIntegrador.brrProgreso.setValue(z);

            for (CFacturasPorManifiesto obj : fManifestarPedidosEnRutaConIntegrador.manifiestoActual.getListaFacturasPorManifiesto()) {
                if (y == x) {
                    this.fManifestarPedidosEnRutaConIntegrador.valorDespBarraProgreso = z++;
                    y = 0;
                }
                obj.setAdherencia(i);
                sentciaListaDeFacturas += "'" + obj.getNumeroFactura() + "',";
                sentenciasSQL.add(obj.getSentenciaInsertSQL());

                /* Se calcula el valor a recudar en el manifiesto */
                valorManifiesto += obj.getValorARecaudarFactura();

                /*se calcula el peso de las facturas del manifiesto*/
                pesoKgManifiesto += obj.getPesoFactura();

                this.fManifestarPedidosEnRutaConIntegrador.repaint();
                i++;
                y++;
            }
            fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setCantidadPedidos(fManifestarPedidosEnRutaConIntegrador.manifiestoActual.getListaFacturasPorManifiesto().size());
            fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setValorTotalManifiesto(valorManifiesto);

            //fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setEstadoManifiesto(3);
            sentciaListaDeFacturas = sentciaListaDeFacturas.substring(0, sentciaListaDeFacturas.length() - 1);
            sentciaListaDeFacturas = sentciaListaDeFacturas + ");";

//this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setValorTotalManifiesto(valorManifiesto);
/*Se deshabilita la factura localmente para informar que no se pueden
sacar a distribucion en otro manifiesto, yq que se encuentra ya en una
ruta*/
            sentenciasSQL.add(sentciaListaDeFacturas);

            /*SE ACTUALIZA EL ESTADO DEL MANIFIESTO CON DATOS NUEVOS */
//this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setIsFree(1);
/*   Se libera el manifiesto*/
//this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setValorTotalManifiesto(valorManifiesto);
/* Se actualiza el valor total del manifiesto*/
//this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setEstadoManifiesto(3);
/* Se actualiza el estado al manifiesto para indicar que está en distribución*/
//            SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            java.util.Date fechaEnviar = new Date();
//            String fechaActual = formato.format(fechaEnviar);
//this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setHoraDeDespacho(fechaActual);
/* Se actualiza la fecha y hora de despacho a distribución*/
//this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setHoraDeLiquidacion(fechaActual);

            /*Se graban los registros en la Base de datos*/
//sentenciasSQL.add(this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.getSentenciaInsertSQL());
//            this.fManifestarPedidosEnRutaConIntegrador.carro.setConductor(this.fManifestarPedidosEnRutaConIntegrador.conductor.getCedula());
//            this.fManifestarPedidosEnRutaConIntegrador.carro.setKilometraje(Integer.parseInt(this.fManifestarPedidosEnRutaConIntegrador.txtKmDeSalida.getText().trim()));
//            sentenciasSQL.add(this.fManifestarPedidosEnRutaConIntegrador.carro.getSentenciaInsertSQL());
            /* SE GRABAN LOS REGISTROS DE FACTURAS POR MANIFIESTO  EN LA BBDD REMOTA */
            if (ini.insertarDatosRemotamente(sentenciasSQL, "HiloGuardarFacturasPorManifiesto")) {

                fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setEstadoManifiesto(3);

                fManifestarPedidosEnRutaConIntegrador.manifiestoActual.actualizarManifiestoDeDistribucions();
                fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setPesoKgManifiesto(pesoKgManifiesto);

                fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setListaFacturasPorManifiesto();
                fManifestarPedidosEnRutaConIntegrador.band = false;

                fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setListaDeDescuentos();
                fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setListaDeRecogidas();

                //form.manifiestoActual.actualizarManifiestoDeDistribucions();
                /*Se deshabilita la factura localmente para informar que no se pueden
                 sacar a distribucion en otro manifiesto, ya que se encuentra  en una ruta*/
                ini.insertarDatosLocalmente(sentciaListaDeFacturas);

                /*Si el cliente esta habilitado para enviar los SMS a los clientes de las facturas*/
                if (ini.enviaSMS() == 1) {
                    // hila para enviar sms a los clientes.
                }

                /* SE HABILITA BOTON DE IMPRIMIR */
                this.fManifestarPedidosEnRutaConIntegrador.btnImprimir.setEnabled(true);
                this.fManifestarPedidosEnRutaConIntegrador.jBtnImprimir.setEnabled(true);
                this.fManifestarPedidosEnRutaConIntegrador.jBtnMinuta.setEnabled(true);

                /* SE DESHABILITA EL BOTON DE GRABAR */
                this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setEnabled(false);
                this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setEnabled(false);
                this.fManifestarPedidosEnRutaConIntegrador.jBtnObservaciones.setEnabled(false);

                this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

                this.fManifestarPedidosEnRutaConIntegrador.repaint();

                /*Se elimina el archivo temporal */
                fichero = new File(fManifestarPedidosEnRutaConIntegrador.manifiestoActual.getRutArchivofacturasporManifiesto());
                fichero.delete();

                new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 3)).start();

                JOptionPane.showMessageDialog(this.fManifestarPedidosEnRutaConIntegrador, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);

            } else {
                this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setEnabled(true);
                this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setEnabled(true);
                this.fManifestarPedidosEnRutaConIntegrador.jBtnObservaciones.setEnabled(true);

                this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

                JOptionPane.showMessageDialog(this.fManifestarPedidosEnRutaConIntegrador, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);

            }

        } catch (NumberFormatException | HeadlessException ex) {
            this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

            JOptionPane.showMessageDialog(this.fManifestarPedidosEnRutaConIntegrador, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);

            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);


            /*Se libera el manifiesto para que se pueda usar  */
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/
            ;
        } catch (SQLException ex) {
            this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

            JOptionPane.showMessageDialog(this.fManifestarPedidosEnRutaConIntegrador, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this.fManifestarPedidosEnRutaConIntegrador, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            /*Se libera el manifiesto para que se pueda usar  */
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        } catch (Exception ex) {
            this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fManifestarPedidosEnRutaConIntegrador.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

            JOptionPane.showMessageDialog(this.fManifestarPedidosEnRutaConIntegrador, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            Logger.getLogger(HiloGuardarFacturasPorManifiesto.class.getName()).log(Level.SEVERE, null, ex);

            /*Se libera el manifiesto para que se pueda usar  */
            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.liberarManifiesto(true);
            /*   Se libera el manifiesto*/

        }

        this.fManifestarPedidosEnRutaConIntegrador.estaOcupadoGrabando = false;
        //this.fManifestarPedidosEnRutaConIntegrador.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/imagenes/grabar.png"))); // NOI18N
    }
}
