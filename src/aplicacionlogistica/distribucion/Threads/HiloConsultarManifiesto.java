/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FConsultaManifiestosDescargados;
import aplicacionlogistica.costumerService.FDescargarDevoluciones;
import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import aplicacionlogistica.distribucion.formularios.FAdicionarPedidosNoReportados;
import aplicacionlogistica.distribucion.formularios.FTrasladoDeFacturas;
import aplicacionlogistica.distribucion.consultas.FConsultarManifiestos;
import aplicacionlogistica.distribucion.consultas.FHabilitarManifiesto;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRutaConIntegrador;
import aplicacionlogistica.distribucion.formularios.FModificarManifiesto;
import aplicacionlogistica.distribucion.formularios.poblaciones.FManifestarPedidosPoblaciones;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CRecogidasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CSoportesConsignaciones;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import mtto.vehiculos.CCarros;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloConsultarManifiesto implements Runnable {

    public static boolean band = false;
    Inicio ini;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    DescargarFacturas formDescargarFacturas = null;
    FDescargarDevoluciones fDescargarDevoluciones = null;
    FManifestarPedidosEnRuta fManifestarPedidosEnRuta = null;
    FManifestarPedidosPoblaciones fManifestarPedidosPoblaciones = null;
    FConsultarManifiestos fConsultarManifiestos = null;
    FTrasladoDeFacturas fTrasladoDeFacturas = null;
    FConsultarManifiestos form2 = null;
    FHabilitarManifiesto fHabilitarManifiesto = null;
    FAdicionarPedidosNoReportados fPedidosNoReportados = null;
    FConsultaManifiestosDescargados formConsultaManifiestoDescargado = null;
    FModificarManifiesto fModificarManifiesto = null;
    FManifestarPedidosEnRutaConIntegrador fManifestarPedidosEnRutaConIntegrador = null;

    //CManifiestosDeDistribucion manifiesto = null;
    int caso;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloConsultarManifiesto(Inicio ini, DescargarFacturas form) {

        this.formDescargarFacturas = form;
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fDescargarDevoluciones
     */
    public HiloConsultarManifiesto(Inicio ini, FDescargarDevoluciones fDescargarDevoluciones) {

        this.fDescargarDevoluciones = fDescargarDevoluciones;
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fPedidosNoReportados
     */
    public HiloConsultarManifiesto(Inicio ini, FAdicionarPedidosNoReportados fPedidosNoReportados) {

        this.fPedidosNoReportados = fPedidosNoReportados;
        this.ini = ini;

    }

    public HiloConsultarManifiesto(Inicio ini, FManifestarPedidosEnRuta formIngDist) {

        this.fManifestarPedidosEnRuta = formIngDist;
        this.ini = ini;

    }

    public HiloConsultarManifiesto(Inicio ini, FManifestarPedidosPoblaciones fManifestarPedidosPoblaciones) {

        this.fManifestarPedidosPoblaciones = fManifestarPedidosPoblaciones;
        this.ini = ini;

    }

    public HiloConsultarManifiesto(FConsultarManifiestos form) {

        this.fConsultarManifiestos = form;
        this.ini = this.fConsultarManifiestos.getIni();

    }

    public HiloConsultarManifiesto(FTrasladoDeFacturas form, int caso) {

        this.fTrasladoDeFacturas = form;
        this.ini = this.fTrasladoDeFacturas.getIni();
        this.caso = caso;
    }

    public HiloConsultarManifiesto(FHabilitarManifiesto form) {

        this.fHabilitarManifiesto = form;
        this.ini = this.fHabilitarManifiesto.getIni();

    }

    public HiloConsultarManifiesto(Inicio ini, FConsultaManifiestosDescargados formConsultaManifiestoDescargado) {

        this.formConsultaManifiestoDescargado = formConsultaManifiestoDescargado;
        this.ini = ini;

    }

    public HiloConsultarManifiesto(FModificarManifiesto fModificarManifiesto) {

        this.fModificarManifiesto = fModificarManifiesto;
        this.ini = fModificarManifiesto.ini;

        if (fModificarManifiesto != null) {
            try {
                llenarDatosManifiestoParaModificar();
            } catch (Exception ex) {
                Logger.getLogger(HiloConsultarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    public HiloConsultarManifiesto(Inicio ini, FManifestarPedidosEnRutaConIntegrador fManifestarPedidosEnRutaConIntegrador) {

        this.fManifestarPedidosEnRutaConIntegrador = fManifestarPedidosEnRutaConIntegrador;
        this.ini = ini;

    }

    @Override
    public void run() {

        if (formDescargarFacturas != null) {
            consultarManifiestoEnDescargueDeFacturas();
        }

        if (fDescargarDevoluciones != null) {
            consultarManifiestoEnDescargueDeVoluciones();
        }

        if (fManifestarPedidosEnRuta != null) {
            consultarManifiestoPedidosEnRuta();
        }

        if (fManifestarPedidosPoblaciones != null) {
            consultarManifiestoDeIngresoDeFacturasPoblaciones();
        }

        if (fConsultarManifiestos != null) {
            // manifiestoParaReimprimir();
        }

        if (fTrasladoDeFacturas != null) {

            switch (this.caso) {
                case 1:
                    consultarManifiestoOrigen();
                    fTrasladoDeFacturas.txtManifiestoOrigen.setEditable(false);
                    fTrasladoDeFacturas.txtManifiestoDestino.setEditable(true);
                    fTrasladoDeFacturas.txtNumeroFactura.requestFocus();
                    fTrasladoDeFacturas.txtManifiestoDestino.requestFocus();
                    fTrasladoDeFacturas.caso = 0;
                    break;
                case 2:
                    consultarManifiestoDestino();
                    fTrasladoDeFacturas.caso = 0;
                    fTrasladoDeFacturas.txtNumeroFactura.setEditable(true);
                    fTrasladoDeFacturas.txtNumeroFactura.setEnabled(true);
                    fTrasladoDeFacturas.txtManifiestoDestino.setEditable(false);
                    fTrasladoDeFacturas.cbxPrefijos.setEnabled(true);
                    fTrasladoDeFacturas.txtNumeroFactura.requestFocus();
                    break;

            }

        }
        if (fHabilitarManifiesto != null) {
            consultarManifiestoParaHabilitar();
        }

        if (fPedidosNoReportados != null) {
            try {

                fPedidosNoReportados.manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(fPedidosNoReportados.txtNumeroManifiesto.getText().trim()));

            } catch (Exception ex) {
                Logger.getLogger(HiloConsultarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            }

            consultarManifiestoIncompleto();
        }

        if (formConsultaManifiestoDescargado != null) {
            consultarManifiestoDescargado();
        }

    }

    private void consultarManifiestoIncompleto() {

        System.out.println("Trae los datos del manifiesto -> \n\n");
        try {

            this.fPedidosNoReportados.lblCirculoDeProgreso1.setVisible(true);
//            this.fPedidosNoReportados.lblCirculoDeProgreso2.setVisible(false);
            this.fPedidosNoReportados.listaDeFacturasPorManifiesto = new ArrayList();

            /* ENTRA AL CASO CORRESPONDIENTE DE ACUERDO AL ESTADO DEL MANIFIESTO */
            switch (this.fPedidosNoReportados.manifiestoActual.getEstadoManifiesto()) {

                case 0:
                    /* El caso cuando por primera vez se le crea a un vehículo
                     un manifiesto de distribución 
                     */
                    // fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto=new ArrayList();
                    //this.fPedidosNoReportados.crearNuevoManifiesto(this.fManifestarPedidosEnRuta.carro);
                    this.fPedidosNoReportados.listaDeFacturasPorManifiesto = new ArrayList();
                    break;

                case 1:// CASO CUANDO NO HAY MANIFIESTOS PENDIENTES
                    // fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto=new ArrayList();
                    this.fPedidosNoReportados.crearNuevoManifiesto(this.fManifestarPedidosEnRuta.carro);
                    //this.fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto = new ArrayList();
                    this.fPedidosNoReportados.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRuta.carro.getKilometrajeActual());
                    // this.fPedidosNoReportados.lblCirculoDeProgreso1.setVisible(false);
                    this.fPedidosNoReportados.txtnombreDeConductor.setEditable(false);
                    JOptionPane.showInternalMessageDialog(this.fPedidosNoReportados, "Ese manifiesto no se ha creado ", "Error", JOptionPane.WARNING_MESSAGE);

                    break;

                case 2:// TIENE MANIFIESTO ASIGANDO PERO NO SE HA CERRADO;

                    JOptionPane.showInternalMessageDialog(this.fPedidosNoReportados, "Ese manifiesto esta creado, pero no cerrado ", "No Cerrado", JOptionPane.WARNING_MESSAGE);
                    //this.fManifestarPedidosEnRuta.manifiestoActual.setRutArchivofacturasporManifiesto();
                    //this.fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto = new ArrayList();
                    // this.fPedidosNoReportados.modificarManifiesto();
                    // this.fPedidosNoReportados.lblCirculoDeProgreso1.setVisible(false);
                    //this.fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRuta.carro.getKilometraje());
                    break;

                case 3:// VEHICULO EN DISTRIBUCION,  NO SE PUEDE ASIGNAR OTRO MANIFIESTO

                    //this.fPedidosNoReportados.llenarDatosManifiestoCerrado();
                    break;

                case 4: // NUMERO DE MANIFIESTO YA DESCARGADO
                    JOptionPane.showInternalMessageDialog(this.fPedidosNoReportados, "Ese manifiesto esta descargado del sistema ", "No Cerrado", JOptionPane.WARNING_MESSAGE);

                    break;

                case 5: // MANIFIESTO ANULADO
                    JOptionPane.showInternalMessageDialog(this.fPedidosNoReportados, "Ese manifiesto anulado del sistema ", "Anulado", JOptionPane.WARNING_MESSAGE);

                    //this.fPedidosNoReportados.jTabbedPane1.setEnabledAt(this.fPedidosNoReportados.jTabbedPane1.indexOfComponent(this.fPedidosNoReportados.pnlAgregarLista), true);
                    // jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlAgregarLista));
                    //this.fPedidosNoReportados.crearNuevoManifiesto(this.fPedidosNoReportados.carro);
                    this.fPedidosNoReportados.txtKmDeSalida.setText("" + this.fPedidosNoReportados.carro.getKilometrajeActual());
                    // this.fPedidosNoReportados.lblCirculoDeProgreso1.setVisible(false);

                    break;

            }

            try {

                /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
                if (this.fPedidosNoReportados.listaDeFacturasPorManifiesto != null) {
                    fPedidosNoReportados.btnImprimir.setEnabled(true);
                }

            } catch (Exception ex) {
                Logger.getLogger(HiloConsultarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.fPedidosNoReportados.lblCirculoDeProgreso1.setVisible(false);
//            this.fPedidosNoReportados.lblCirculoDeProgreso2.setVisible(false);
            this.fPedidosNoReportados.txtKmDeSalida.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(HiloConsultarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void consultarManifiestoPedidosEnRuta() {

        System.out.println("Trae los datos del manifiesto -> \n\n");
        try {

            this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(true);
            fManifestarPedidosEnRuta.manifiestoActual = new CManifiestosDeDistribucion(ini);

            if (ini.getListaDeManifiestossinDescargar() != null) {
                for (CManifiestosDeDistribucion mfto : ini.getListaDeManifiestossinDescargar()) {
                    if (mfto.getVehiculo().equals(fManifestarPedidosEnRuta.txtPlaca.getText().trim())) {
                        fManifestarPedidosEnRuta.manifiestoActual = mfto;

                    }
                }
                Thread.sleep(10);
            }

            if (fManifestarPedidosEnRuta.manifiestoActual.getNumeroManifiesto() == null) {

                fManifestarPedidosEnRuta.manifiestoActual = new CManifiestosDeDistribucion(ini, fManifestarPedidosEnRuta.txtPlaca.getText().trim());
            }

            this.fManifestarPedidosEnRuta.lblCirculoDeProgreso2.setVisible(false);

            /*Se crea una lista provisionnal de acuerdo a reqerimiento*/
            List<CFacturasPorManifiesto> listaFacturasxmanifiesto = new ArrayList();

            this.fManifestarPedidosEnRuta.manifiestoActual.setListaFacturasPorManifiesto(listaFacturasxmanifiesto);

            if (fManifestarPedidosEnRuta.manifiestoActual.getEstadoManifiesto() == 0) {
                fManifestarPedidosEnRuta.manifiestoActual.setIsFree(1);

            }

            /* ENTRA AL CASO CORRESPONDIENTE DE ACUERDO AL ESTADO DEL MANIFIESTO */
            switch (this.fManifestarPedidosEnRuta.manifiestoActual.getEstadoManifiesto()) {

                case 0:
                    /* El caso cuando por primera vez se le crea a un vehículo
                     un manifiesto de distribución 
                     */
                    // fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto=new ArrayList();
                    //  this.fManifestarPedidosEnRuta.crearNuevoManifiesto(this.fManifestarPedidosEnRuta.carro);

                    listaFacturasxmanifiesto = new ArrayList();
                    this.fManifestarPedidosEnRuta.manifiestoActual.setListaFacturasPorManifiesto(listaFacturasxmanifiesto);
                    break;

                case 1:// CASO CUANDO NO HAY MANIFIESTOS PENDIENTES
                    // fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto=new ArrayList();
                    //  this.fManifestarPedidosEnRuta.crearNuevoManifiesto(this.fManifestarPedidosEnRuta.carro);
                    //this.fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto = new ArrayList();
                    this.fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRuta.carro.getKilometrajeActual());
                    this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
                    this.fManifestarPedidosEnRuta.txtnombreDeConductor.setEditable(false);

                    break;

                case 2:// TIENE MANIFIESTO ASIGANDO PERO NO SE HA CERRADO;
                    //this.fManifestarPedidosEnRuta.manifiestoActual.setRutArchivofacturasporManifiesto();
                    //this.fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto = new ArrayList();
                    this.fManifestarPedidosEnRuta.modificarManifiesto();

                    this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
                    //this.fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRuta.carro.getKilometraje());
                    break;

                case 3:// VEHICULO EN DISTRIBUCION,  NO SE PUEDE ASIGNAR OTRO MANIFIESTO

                    if (this.ini.getPropiedades().getProperty("permitirVariosManifiestos").equals("false")) {
                        JOptionPane.showInternalMessageDialog(this.fManifestarPedidosEnRuta, "Ese vehiculo ya tiene asignado un manifiesto de distribucion,\n"
                                + "debe liquidar y cerrarlo para crear uno nuevo", "Error", 0);
                        this.fManifestarPedidosEnRuta.cancelar();
                        break;
                    }

                    int reply = JOptionPane.showConfirmDialog(this.fManifestarPedidosEnRuta, "Desea Crear otro manifiesto de Distribucion?", "Vehiculo ya aparece en Ruta", YES_NO_OPTION, WARNING_MESSAGE);
                    this.fManifestarPedidosEnRuta.nuevo = false;

                    if (reply == JOptionPane.YES_OPTION) {

                        //   this.fManifestarPedidosEnRuta.crearNuevoManifiesto(this.fManifestarPedidosEnRuta.carro);
                        this.fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRuta.carro.getKilometrajeActual());
                        this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
                        //this.fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto = new ArrayList();
                    } else {

                        //this.fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto = new ArrayList();
                        // this.fManifestarPedidosEnRuta.llenarDatosManifiestoCerrado();
                        this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
                    }

                    /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
                    if (this.fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto() != null) {
                        fManifestarPedidosEnRuta.btnImprimir.setEnabled(true);
                    }

                    // JOptionPane.showInternalMessageDialog(this.fManifestarPedidosEnRuta, "ESE VEHICULO ESTA EN DISTRIBUCION EN ESTOS MOMENTOS,\n"
                    //       + "NO SE PUEDE CREAR UN NUEVO MANIFIESTO", "Error", 0);
                    break;

                case 4: // NUMERO DE MANIFIESTO YA DESCARGADO Y SE PUEDE CREAR OTRO
                    //this.fManifestarPedidosEnRuta.listaDeFacturasPorManifiesto = new ArrayList();

                    this.fManifestarPedidosEnRuta.jTabbedPane1.setEnabledAt(this.fManifestarPedidosEnRuta.jTabbedPane1.indexOfComponent(this.fManifestarPedidosEnRuta.pnlAgregarLista), true);
                    // jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlAgregarLista));
                    //     this.fManifestarPedidosEnRuta.crearNuevoManifiesto(this.fManifestarPedidosEnRuta.carro);
                    this.fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRuta.carro.getKilometrajeActual());
                    this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
                    this.fManifestarPedidosEnRuta.txtKmDeSalida.requestFocus();

                    /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
                    if (this.fManifestarPedidosEnRuta.manifiestoActual.getListaFacturasPorManifiesto() != null) {
                        fManifestarPedidosEnRuta.btnImprimir.setEnabled(true);
                    }
                    break;

                case 5: // MANIFIESTO ANULADO

                    this.fManifestarPedidosEnRuta.jTabbedPane1.setEnabledAt(this.fManifestarPedidosEnRuta.jTabbedPane1.indexOfComponent(this.fManifestarPedidosEnRuta.pnlAgregarLista), true);
                    // jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlAgregarLista));
                    //    this.fManifestarPedidosEnRuta.crearNuevoManifiesto(this.fManifestarPedidosEnRuta.carro);
                    this.fManifestarPedidosEnRuta.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRuta.carro.getKilometrajeActual());
                    this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
                    this.fManifestarPedidosEnRuta.txtKmDeSalida.requestFocus();
                    break;

            }

            this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
            this.fManifestarPedidosEnRuta.lblCirculoDeProgreso2.setVisible(false);
            this.fManifestarPedidosEnRuta.txtKmDeSalida.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(HiloConsultarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            this.fManifestarPedidosEnRuta.lblCirculoDeProgreso1.setVisible(false);
            this.fManifestarPedidosEnRuta.lblCirculoDeProgreso2.setVisible(false);
        }
    }

    private void consultarManifiestoDeIngresoDeFacturasPoblaciones() {

        System.out.println("Trae los datos del manifiesto -> \n\n");
        try {

            this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso1.setVisible(true);
            fManifestarPedidosPoblaciones.manifiestoActual = new CManifiestosDeDistribucion(ini);

            if (ini.getListaDeManifiestossinDescargar() != null) {
                for (CManifiestosDeDistribucion mfto : ini.getListaDeManifiestossinDescargar()) {
                    if (mfto.getVehiculo().equals(fManifestarPedidosPoblaciones.txtPlaca.getText().trim())) {
                        fManifestarPedidosPoblaciones.manifiestoActual = mfto;
                        break;
                    }
                }
                Thread.sleep(10);
            }

            if (fManifestarPedidosPoblaciones.manifiestoActual == null) {

                fManifestarPedidosPoblaciones.manifiestoActual = new CManifiestosDeDistribucion(ini, fManifestarPedidosPoblaciones.txtPlaca.getText());
            }

            this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso2.setVisible(false);

            /*Se crea una lista provisionnal de acuerdo a reqerimiento*/
            List<CFacturasPorManifiesto> lista = new ArrayList();

            this.fManifestarPedidosPoblaciones.manifiestoActual.setListaFacturasPorManifiesto(lista);

            if (fManifestarPedidosPoblaciones.manifiestoActual.getEstadoManifiesto() == 0) {
                fManifestarPedidosPoblaciones.manifiestoActual.setIsFree(1);

            }

            /* ENTRA AL CASO CORRESPONDIENTE DE ACUERDO AL ESTADO DEL MANIFIESTO */
            switch (this.fManifestarPedidosPoblaciones.manifiestoActual.getEstadoManifiesto()) {

                case 0:
                    /* El caso cuando por primera vez se le crea a un vehículo
                     un manifiesto de distribución 
                     */
                    // fManifestarPedidosPoblaciones.listaDeFacturasPorManifiesto=new ArrayList();
                    this.fManifestarPedidosPoblaciones.crearNuevoManifiesto(this.fManifestarPedidosPoblaciones.carro);

                    lista = new ArrayList();
                    this.fManifestarPedidosPoblaciones.manifiestoActual.setListaFacturasPorManifiesto(lista);
                    break;

                case 1:// CASO CUANDO NO HAY MANIFIESTOS PENDIENTES
                    // fManifestarPedidosPoblaciones.listaDeFacturasPorManifiesto=new ArrayList();
                    this.fManifestarPedidosPoblaciones.crearNuevoManifiesto(this.fManifestarPedidosPoblaciones.carro);
                    //this.fManifestarPedidosPoblaciones.listaDeFacturasPorManifiesto = new ArrayList();
                    this.fManifestarPedidosPoblaciones.txtKmDeSalida.setText("" + this.fManifestarPedidosPoblaciones.carro.getKilometrajeActual());
                    this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso1.setVisible(false);
                    this.fManifestarPedidosPoblaciones.txtnombreDeConductor.setEditable(false);

                    break;

                case 2:// TIENE MANIFIESTO ASIGANDO PERO NO SE HA CERRADO;
                    //this.fManifestarPedidosPoblaciones.manifiestoActual.setRutArchivofacturasporManifiesto();
                    //this.fManifestarPedidosPoblaciones.listaDeFacturasPorManifiesto = new ArrayList();
                    this.fManifestarPedidosPoblaciones.modificarManifiesto();

                    this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso1.setVisible(false);
                    //this.fManifestarPedidosPoblaciones.txtKmDeSalida.setText("" + this.fManifestarPedidosPoblaciones.carro.getKilometraje());
                    break;

                case 3:// VEHICULO EN DISTRIBUCION,  NO SE PUEDE ASIGNAR OTRO MANIFIESTO

                    int reply = JOptionPane.showConfirmDialog(this.fManifestarPedidosPoblaciones, "Desea Crear otro manifiesto de Distribucion?", "Vehiculo ya aparece en Ruta", YES_NO_OPTION, WARNING_MESSAGE);
                    this.fManifestarPedidosPoblaciones.nuevo = false;

                    if (reply == JOptionPane.YES_OPTION) {

                        this.fManifestarPedidosPoblaciones.crearNuevoManifiesto(this.fManifestarPedidosPoblaciones.carro);
                        this.fManifestarPedidosPoblaciones.txtKmDeSalida.setText("" + this.fManifestarPedidosPoblaciones.carro.getKilometrajeActual());
                        this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso1.setVisible(false);
                        //this.fManifestarPedidosPoblaciones.listaDeFacturasPorManifiesto = new ArrayList();
                    } else {

                        //this.fManifestarPedidosPoblaciones.listaDeFacturasPorManifiesto = new ArrayList();
                        this.fManifestarPedidosPoblaciones.llenarDatosManifiestoCerrado();
                        this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso1.setVisible(false);

                        /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
                        if (this.fManifestarPedidosPoblaciones.manifiestoActual.getListaFacturasPorManifiesto() != null) {
                            fManifestarPedidosPoblaciones.btnImprimir.setEnabled(true);

                        }
                    }

                    // JOptionPane.showInternalMessageDialog(this.fManifestarPedidosPoblaciones, "ESE VEHICULO ESTA EN DISTRIBUCION EN ESTOS MOMENTOS,\n"
                    //       + "NO SE PUEDE CREAR UN NUEVO MANIFIESTO", "Error", 0);
                    break;

                case 4: // NUMERO DE MANIFIESTO YA DESCARGADO Y SE PUEDE CREAR OTRO
                    //this.fManifestarPedidosPoblaciones.listaDeFacturasPorManifiesto = new ArrayList();

                    this.fManifestarPedidosPoblaciones.jTabbedPane1.setEnabledAt(this.fManifestarPedidosPoblaciones.jTabbedPane1.indexOfComponent(this.fManifestarPedidosPoblaciones.pnlAgregarFactura), true);
                    // jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlAgregarLista));
                    this.fManifestarPedidosPoblaciones.crearNuevoManifiesto(this.fManifestarPedidosPoblaciones.carro);
                    this.fManifestarPedidosPoblaciones.txtKmDeSalida.setText("" + this.fManifestarPedidosPoblaciones.carro.getKilometrajeActual());
                    this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso1.setVisible(false);
                    this.fManifestarPedidosPoblaciones.txtKmDeSalida.requestFocus();

                    /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
                    if (this.fManifestarPedidosPoblaciones.manifiestoActual.getListaFacturasPorManifiesto() != null) {
                        fManifestarPedidosPoblaciones.btnImprimir.setEnabled(true);
                    }
                    break;

                case 5: // MANIFIESTO ANULADO

                    this.fManifestarPedidosPoblaciones.jTabbedPane1.setEnabledAt(this.fManifestarPedidosPoblaciones.jTabbedPane1.indexOfComponent(this.fManifestarPedidosPoblaciones.pnlAgregarFactura), true);
                    // jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlAgregarLista));
                    this.fManifestarPedidosPoblaciones.crearNuevoManifiesto(this.fManifestarPedidosPoblaciones.carro);
                    this.fManifestarPedidosPoblaciones.txtKmDeSalida.setText("" + this.fManifestarPedidosPoblaciones.carro.getKilometrajeActual());
                    this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso1.setVisible(false);
                    this.fManifestarPedidosPoblaciones.txtKmDeSalida.requestFocus();
                    break;

            }

            this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso1.setVisible(false);
            this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso2.setVisible(false);
            this.fManifestarPedidosPoblaciones.txtKmDeSalida.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(HiloConsultarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso1.setVisible(false);
            this.fManifestarPedidosPoblaciones.lblCirculoDeProgreso2.setVisible(false);
        }
    }

    /**
     * Método que asigna al manifiesto actual un array con la lista de las
     * facturas que al descargar no se encuenetran en el manifiesto
     *
     *
     * @param file archivo temporal donde se encuentrab grabados los datos
     */
    private void consultarManifiestoEnDescargueDeFacturas() {

        CManifiestosDeDistribucion mfto = null;
        /**
         * Colocar hilo donde se muestra el gif del proceso
         */
        this.formDescargarFacturas.lblCirculoDeProgreso.setVisible(true);
        this.formDescargarFacturas.txtNumeroManifiesto.setEnabled(false);
        this.formDescargarFacturas.txtKilometrosEntrada.setEnabled(true);
        this.formDescargarFacturas.txtKilometrosEntrada.setEditable(true);

        try {

            /* Se crea el manifiesto de distribucion*/
            String numeroManifiesto = "" + Integer.parseInt(this.formDescargarFacturas.txtNumeroManifiesto.getText().trim());

            /* Busca el numero manifiesto en los pendiente;*/
            for (CManifiestosDeDistribucion manif : ini.getListaDeManifiestossinDescargar()) {

                if (manif.getNumeroManifiesto().equals(numeroManifiesto)) {
                    this.formDescargarFacturas.manifiestoActual = manif;
                    mfto = manif;
                    break;
                }
                Thread.sleep(10);
            }

            if (mfto == null) {
                this.formDescargarFacturas.manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(numeroManifiesto));
                this.ini.getListaDeManifiestossinDescargar().add(this.formDescargarFacturas.manifiestoActual);
            }

            // se valida que el numeroDeManifiesto exista
            // si  no existe el numeroDeManifiesto
            if (this.formDescargarFacturas.manifiestoActual.getNumeroManifiesto() == null) {
                //this.formDescargarFacturas.manifiestoActual = null;
                JOptionPane.showInternalMessageDialog(this.formDescargarFacturas, "ESE MANIFIESTO NO EXISTE EN LA BASE DE DATOS ", "Error", JOptionPane.ERROR_MESSAGE);

                // si el manifiesto existe   
            } else {

                /*Se llena el array de las facturas en distribucion */
                this.formDescargarFacturas.manifiestoActual.setListaFacturasPorManifiesto();

                File fichero = new File(this.formDescargarFacturas.manifiestoActual.getRutaArchivoDescargueFacturas());


                /* SWITCH PARA DETERMINAR EL MOVIMIENTO DEL MANIFIESTO */
                switch (this.formDescargarFacturas.manifiestoActual.getEstadoManifiesto()) {

                    case 1:

                        JOptionPane.showInternalMessageDialog(this.formDescargarFacturas, "ESE  MANIFIESTOS EN DISTRIBUCION ,\n"
                                + "HAY QUE CREARLO Y DARLE SALIDA A DISTRIBUCION", "Error", 0);
                        this.formDescargarFacturas.txtNumeroManifiesto.setEnabled(true);
                        break;

                    // MANIFIESTO CREADO PERO NO HA SALIDO A DISTRIBUCION
                    case 2:

                        JOptionPane.showInternalMessageDialog(this.formDescargarFacturas, "ESTE MANIFIESTO DE  DISTRIBUCION ,\n"
                                + "NO SE HA GRABADO EN EL SISTEMA", "Manifiesto sin facturas grabadas", 0);
                        this.formDescargarFacturas.txtNumeroManifiesto.setEnabled(true);
                        break;

                    // MANIFIESTO GRABADO Y ESTA EN DISTRIBUCION
                    case 3:

                        /* Se valida que el manifiesto no esté siendo usado por otro usuario en el sistema */
                        //if (!((this.formDescargarFacturas.manifiestoActual.getIsFree() == 1) || (this.formDescargarFacturas.manifiestoActual.getUsuario().equals(Inicio.deCifrar(ini.getUser().getNombreUsuario()))))) {
                        if (false) {
                            JOptionPane.showInternalMessageDialog(this.formDescargarFacturas, "Este manifiesto No se encuentra disponible,"
                                    + "está siendo utilizado  por otro usuario :  " + this.formDescargarFacturas.manifiestoActual.getUsuarioManifiesto(),
                                    "Error",
                                    JOptionPane.WARNING_MESSAGE);

                            this.formDescargarFacturas.manifiestoActual = null;

                            /* El manifiesto no está siendo usado por otro usuario en el sistema */
                        } else {
                            System.out.print("se dispara el hilo de asignar la lista de las facturas \n");
                            if (this.formDescargarFacturas.manifiestoActual.getListaFacturasPorManifiesto() == null
                                    || this.formDescargarFacturas.manifiestoActual.getListaFacturasPorManifiesto().size() == 0) {

                                /*Se llena el array de las facturas en distribucion */
                                //this.formDescargarFacturas.manifiestoActual.setListaFacturasPorManifiesto();
                                JOptionPane.showInternalMessageDialog(this.formDescargarFacturas, "Este manifiesto No tiene facturas asignadas",
                                        "Error", JOptionPane.WARNING_MESSAGE);
                                break;

                            }

                            if (this.formDescargarFacturas.manifiestoActual.getListaFacturasDescargadas() == null) {
                                /*Se llena el array de las facturas en distribucion */
                                List<CFacturasPorManifiesto> lista = new ArrayList();

                                this.formDescargarFacturas.manifiestoActual.setListaFacturasDescargadas(lista);
                            }

                            if (this.formDescargarFacturas.manifiestoActual.getListaFacturasPendientesPorDescargar() == null) {
                                /*Se llena el array de las facturas en distribucion */
                                List<CFacturasPorManifiesto> lista = new ArrayList();

                                this.formDescargarFacturas.manifiestoActual.setListaFacturasPendientesPorDescargar(lista);
                            }

                            if (this.formDescargarFacturas.manifiestoActual.getListaDeSoportesConsignaciones() == null) {
                                /*Se llena el array de las facturas en distribucion */
                                List<CSoportesConsignaciones> lista = new ArrayList();

                                this.formDescargarFacturas.manifiestoActual.setListaDeSoportesConsignaciones(lista);
                            }

                            if (this.formDescargarFacturas.manifiestoActual.getListaDeRecogidasPorManifiesto() == null) {
                                /*Se llena el array de las facturas en distribucion */
                                List<CRecogidasPorManifiesto> lista = new ArrayList();

                                this.formDescargarFacturas.manifiestoActual.setListaDeRecogidasPorManifiesto(lista);
                            }

                            System.out.print("se dispara el hilo de asignar la lista de las productos facturas \n");

                            /*Comienza a cargar los productos de las facturas del manifiesto*/
                            new Thread(new HiloCargarListaDeProductosFacturasPoManifiesto(this.ini,
                                    this.formDescargarFacturas.manifiestoActual,
                                    this.formDescargarFacturas.jProgressBar1,
                                    this.formDescargarFacturas.lblCirculoDeProgreso)).start();

                            /*Identifico el manifiesto lo estoy modificando en 
                             estos momentos */
                            this.formDescargarFacturas.esteManifiestoEsMio = true;

                            System.out.println("Aca consulta el manifiesto para bloquearlo \n");

                            /* SE BLOQUEA MANIFIESTO PARA QUE NO PUEDA SER UTLIZADO POR OTRO USUARIO */
                            this.formDescargarFacturas.manifiestoActual.liberarManifiesto(false);

                            /* SI EL FICHERO EXISTE, QUIERE DECIR QUE YA SE HABIA INCIADO ANTERIORMENTE
                             EL PROCESO DE DESCARGUE DEL MANIFIESTO, POR LO TANTO YA HAY CREADO UN INCIO DE
                             PROCESO 
                             */
                            if (fichero.exists()) {
                                System.out.print("valida la existencia del archivo \n");

                                int i = 0;

                                if (formDescargarFacturas.manifiestoActual.getListaFacturasPorManifiesto() != null) {
                                    while (this.formDescargarFacturas.jProgressBar1.getValue() < 100) {
                                        i++;
                                        Thread.sleep(10);
                                    }
                                }

                                this.formDescargarFacturas.manifiestoActual.setListaFacturasDescargadas(fichero);

                                boolean aparece = false;

                                if (this.formDescargarFacturas.manifiestoActual.getListaFacturasPendientesPorDescargar().size() == 0) {
                                    /*Llena la lista de las facturas pendientes por descargar */
                                    for (CFacturasPorManifiesto obj : this.formDescargarFacturas.manifiestoActual.getListaFacturasPorManifiesto()) {
                                        aparece = false;

                                        for (CFacturasPorManifiesto obj2 : this.formDescargarFacturas.manifiestoActual.getListaFacturasDescargadas()) {
                                            if (obj.getNumeroFactura().equals(obj2.getNumeroFactura())) {
                                                aparece = true;
                                            }
                                        }
                                        if (!aparece) {
                                            this.formDescargarFacturas.manifiestoActual.getListaFacturasPendientesPorDescargar().add(obj);
                                        }
                                        Thread.sleep(10);
                                    }
                                }

                                // SE RECUPERAN LOS DATOS DE LOS PRODUCTOS POR FACTURAS DESCARGADAS
                                fichero = new File(this.formDescargarFacturas.manifiestoActual.getRutaArchivoDescargueporductosPorFactura());

                                if (fichero.exists()) {
                                    this.formDescargarFacturas.manifiestoActual.setListaCProductosPorFacturaDescargados(fichero);

                                }
                                //this.formDescargarFacturas.listaDeCProductosPorFacturaDescargados = this.formDescargarFacturas.manifiestoActual.getListaCProductosPorFactura();
                                // SE RECUPERAN LOS DATOS DE LOS RECOGIDAS DESCARGADAS
                                fichero = new File(this.formDescargarFacturas.manifiestoActual.getRutArchivoRecogidasporManifiesto());

                                if (fichero.exists()) {
                                    this.formDescargarFacturas.manifiestoActual.setListaDeRecogidasPorManifiesto(fichero);
                                    //this.formDescargarFacturas.listaDeRecogidasPorManifiesto = this.formDescargarFacturas.manifiestoActual.getListaDeRecogidasPorManifiesto();
                                    this.formDescargarFacturas.txtKilometrosEntrada.setText("" + this.formDescargarFacturas.manifiestoActual.getKmEntrada());
                                    this.formDescargarFacturas.llenarjTableRecogidasPorVehiculo();
                                }

                                // SE RECUPERAN LOS DATOS DE LAS FACTURAS NO MANIFESTADAS
                                fichero = new File(this.formDescargarFacturas.manifiestoActual.getRutArchivoFacturasSinManifestar());

                                if (fichero.exists()) {
                                    this.formDescargarFacturas.manifiestoActual.setListaDefacturasSinManifestar(fichero);
                                    this.formDescargarFacturas.listaDeFacturasNoManifestadas = this.formDescargarFacturas.manifiestoActual.getListaDefacturasSinManifestar();
                                    this.formDescargarFacturas.llenarjListFacturasSinManifestar();

                                }
                                // SE RECUPERAN LOS DATOS DE LOS SOPORTES DE CONSIGNACIONES
                                fichero = new File(this.formDescargarFacturas.manifiestoActual.getRutaArchivoSoportesDeConsignaciones());

                                if (fichero.exists()) {
                                    this.formDescargarFacturas.manifiestoActual.setListaDeSoportesConsignaciones(fichero);
                                    this.formDescargarFacturas.llenarjTableListaDeConsignaciones();
                                }

                                this.formDescargarFacturas.asignarTipoDescargue();

                                this.formDescargarFacturas.llenarjTableFacturasPorVehiculo();


                                /* SINO EXISTE EL FICHERO SE ENCUENTRA EN DISTRIBUCIO 
                                OSEA ESTADO=3 Y HASTA AHORA SE INCIA EL PROCESO DE 
                                DESCARGUE DEL MANIFIESTO
                                 */
                            } else {

                                int i = 1;
                                if (this.formDescargarFacturas.manifiestoActual.getListaFacturasPendientesPorDescargar().size() == 0) {
                                    for (CFacturasPorManifiesto obj : this.formDescargarFacturas.manifiestoActual.getListaFacturasPorManifiesto()) {

                                        this.formDescargarFacturas.manifiestoActual.getListaFacturasPendientesPorDescargar().add(obj);
                                        Thread.sleep(1);

                                    }
                                }
                                this.formDescargarFacturas.btnGrabar100.setEnabled(true);
                            }

                            int i = 0;
                            while (this.formDescargarFacturas.jProgressBar1.getValue() < 100) {
                                i++;
                                Thread.sleep(10);
                            }

                            // SE RECUPERAN LOS DATOS DE LAS FACTURAS NO MANIFESTADAS
                            fichero = new File(this.formDescargarFacturas.manifiestoActual.getRutArchivoFacturasSinManifestar());

                            if (fichero.exists()) {
                                this.formDescargarFacturas.manifiestoActual.setListaDefacturasSinManifestar(fichero);
                                this.formDescargarFacturas.listaDeFacturasNoManifestadas = this.formDescargarFacturas.manifiestoActual.getListaDefacturasSinManifestar();
                                this.formDescargarFacturas.llenarjListFacturasSinManifestar();

                            }

                            this.formDescargarFacturas.llenarDatosVista();
                        }

                        break;

                    // MANIFIESTO DESCARGADO Y CUADRADO
                    case 4:

                        if (this.formDescargarFacturas.manifiestoActual.getListaFacturasDescargadas() == null
                                || this.formDescargarFacturas.manifiestoActual.getListaFacturasDescargadas().size() == 0) {

                            List<CFacturasPorManifiesto> lista = new ArrayList();
                            this.formDescargarFacturas.manifiestoActual.setListaFacturasPendientesPorDescargar(lista);

                            /*Se recuperan la lista de las Fscturas descargadas */
                            this.formDescargarFacturas.manifiestoActual.setListaFacturasDescargadas();


                            /*Se recuperan la lista de las Recogidas */
                            this.formDescargarFacturas.manifiestoActual.setListaDeRecogidasPorManifiesto();
                            this.formDescargarFacturas.manifiestoActual.setListaDeSoportesConsignaciones();
                            // this.formDescargarFacturas.listaDeSoportesDeConsignaciones = this.formDescargarFacturas.manifiestoActual.getListaDeSoportesConsignaciones();

                            this.formDescargarFacturas.asignarTipoDescargue();
                        }

                        this.formDescargarFacturas.llenarDatosVista();

                        /*Se llenan las jtables respectivas */
                        //this.formDescargarFacturas.llenarjTableFacturasPorVehiculo();
                        //this.formDescargarFacturas.llenarjTableFacturasDescargadas();
                        this.formDescargarFacturas.llenarjTableRecogidasPorManifiesto();

                        this.formDescargarFacturas.llenarjTableListaDeConsignaciones();

                        /*Se bloquea el manifiesto por estar ya descargado */
                        this.formDescargarFacturas.manifiestoActual.liberarManifiesto(false);

                        this.formDescargarFacturas.btnGrabar.setEnabled(false);
                        this.formDescargarFacturas.jBtnMinuta.setEnabled(true);

                        break;

                }

            }

            /*Se dispara un hilo que llena todos los manifiestos con las facturas*/
            if (!this.formDescargarFacturas.isFormularioCompleto) {
                new Thread(new HiloAsignarFacturas_ProductoAmanifiesto(this.ini)).start();

                new Thread(new HiloListadoDeManifiestosSinDescargar(this.ini)).start();

                this.formDescargarFacturas.isFormularioCompleto = true;
            }
            this.formDescargarFacturas.lblCirculoDeProgreso.setVisible(false);

        } catch (Exception ex) {

            this.formDescargarFacturas.lblCirculoDeProgreso.setVisible(false);
            JOptionPane.showInternalMessageDialog(this.formDescargarFacturas, "Error al consultar manifiesto", "Error", JOptionPane.ERROR_MESSAGE);

            //this.formDescargarFacturas.manifiestoActual.liberarManifiesto(true);
            System.out.println("Error en txtManifiestoKeyPressed " + ex);
            Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    /**
     * Método que asigna al manifiesto actual un array con la lista de las
     * facturas que al descargar no se encuenetran en el manifiesto
     *
     *
     * @param file archivo temporal donde se encuentrab grabados los datos
     */
    private void consultarManifiestoEnDescargueDeVoluciones() {

        /**
         * Colocar hilo donde se muestra el gif del proceso
         */
        this.fDescargarDevoluciones.lblCirculoDeProgreso.setVisible(true);
        this.fDescargarDevoluciones.txtManifiesto.setEnabled(false);
        //this.fDescargarDevoluciones.txtKilometrosEntrada.setEnabled(true);
        //this.fDescargarDevoluciones.txtKilometrosEntrada.setEditable(true);

        try {

            if (this.fDescargarDevoluciones.manifiestoActual == null) {
                /* Se crea el manifiesto de distribucion*/
                int manifiesto = Integer.parseInt(this.fDescargarDevoluciones.txtManifiesto.getText().trim());
                this.fDescargarDevoluciones.manifiestoActual = new CManifiestosDeDistribucion(ini, manifiesto);
            }

            // se valida que el numeroDeManifiesto exista
            // si  no existe el numeroDeManifiesto
            if (this.fDescargarDevoluciones.manifiestoActual.getNumeroManifiesto() == null) {
                this.fDescargarDevoluciones.manifiestoActual = null;
                JOptionPane.showInternalMessageDialog(this.fDescargarDevoluciones, "ESE MANIFIESTO NO EXISTE EN LA BASE DE DATOS ", "Error", JOptionPane.ERROR_MESSAGE);

                // si el manifiesto existe   
            } else {

                /*Se llena el array de las facturas en distribucion */
                this.fDescargarDevoluciones.listaFacturasPorManifiesto = this.fDescargarDevoluciones.manifiestoActual.getListaFacturasPorManifiesto();

                this.fDescargarDevoluciones.listaFacturasPendientesPorDescargar = new ArrayList();
                this.fDescargarDevoluciones.listaFacturasDescargadas = new ArrayList();
                this.fDescargarDevoluciones.listaDeRecogidasPorManifiesto = new ArrayList();
                this.fDescargarDevoluciones.listaDeCProductosPorFacturaDescargados = new ArrayList();
                this.fDescargarDevoluciones.listaDeFacturasNoManifestadas = new ArrayList();
                this.fDescargarDevoluciones.listaDeSoportesDeConsignaciones = new ArrayList();

                File fichero = new File(this.fDescargarDevoluciones.manifiestoActual.getRutaArchivoDescargueFacturas());

                /* SWITCH PARA DETERMINAR EL MOVIMIENTO DEL MANIFIESTO */
                switch (this.fDescargarDevoluciones.manifiestoActual.getEstadoManifiesto()) {

                    case 1:

                        JOptionPane.showInternalMessageDialog(this.fDescargarDevoluciones, "ESE  MANIFIESTOS EN DISTRIBUCION ,\n"
                                + "HAY QUE CREARLO Y DARLE SALIDA A DISTRIBUCION", "Error", 0);
                        this.fDescargarDevoluciones.txtManifiesto.setEnabled(true);
                        break;

                    // MANIFIESTO CREADO PERO NO HA SALIDO A DISTRIBUCION
                    case 2:

                        JOptionPane.showInternalMessageDialog(this.fDescargarDevoluciones, "ESTE MANIFIESTO DE  DISTRIBUCION ,\n"
                                + "NO SE HA GRABADO EN EL SISTEMA", "Error", 0);
                        this.fDescargarDevoluciones.txtManifiesto.setEnabled(true);
                        break;

                    // MANIFIESTO GRABADO Y ESTA EN DISTRIBUCION
                    case 3:

                        /* Se valida que el manifiesto no esté siendo usado por otro usuario en el sistema */
                        if (!((this.fDescargarDevoluciones.manifiestoActual.getIsFree() == 1) || (this.fDescargarDevoluciones.manifiestoActual.getUsuarioManifiesto().equals(Inicio.deCifrar(ini.getUser().getNombreUsuario()))))) {

                            JOptionPane.showInternalMessageDialog(this.fDescargarDevoluciones, "Este manifiesto No se encuentra disponible,"
                                    + "está siendo utilizado  por otro usuario :  " + this.fDescargarDevoluciones.manifiestoActual.getUsuarioManifiesto(),
                                    "Error",
                                    JOptionPane.WARNING_MESSAGE);

                            this.fDescargarDevoluciones.manifiestoActual = null;

                            /* El manifiesto no está siendo usado por otro usuario en el sistema */
                        } else {
                            /*Identifico el manifiesto lo estoy modificando en 
                             estos momentos */
                            this.fDescargarDevoluciones.esteManifiestoEsMio = true;

                            /* SE BLOQUEA MANIFIESTO PARA QUE NO PUEDA SER UTLIZADO POR OTRO USUARIO */
                            this.fDescargarDevoluciones.manifiestoActual.liberarManifiesto(false);

                            /* SI EL FICHERO EXISTE, QUIERE DECIR QUE YA SE HABIA INCIADO ANTERIORMENTE
                             EL PROCESO DE DESCARGUE DEL MANIFIESTO, POR LO TANTO YA HAY CREADO UN INCIO DE
                             PROCESO 
                             */
                            if (fichero.exists()) {

                                // SE RECUPERAN LOS DATOS DE LAS FACTURAS DESCARGADAS
                                this.fDescargarDevoluciones.manifiestoActual.setListaFacturasDescargadas(fichero);

                                /* Se llena la lista de las facturas pendientes por descargar*/
                                boolean aparece = false;
                                //for (Vst_FacturasDescargadas obj : this.fDescargarDevoluciones.listaFacturasPorManifiesto) {

                                aparece = false;

                                /*LLena la lista las facturas descargadas*/
//                                for (Vst_FacturasDescargadas obj2 : this.fDescargarDevoluciones.manifiestoActual.getListaFacturasDescargadas()) {
//
//                                    for (Vst_FacturasDescargadas obj : this.fDescargarDevoluciones.listaFacturasPorManifiesto) {
//                                        aparece = false;
//
//                                        if (obj.getNumeroFactura().equals(obj2.getNumeroFactura())) {
//                                            aparece = true;
//                                            obj.setAdherencia(obj2.getAdherencia());
//                                            obj.setValorRechazo(obj2.getValorRechazo());
//                                            obj.setValorDescuento(obj2.getValorDescuento());
//                                            obj.setValorRecaudado(obj2.getValorRecaudado());
//                                            obj.setIdTipoDeMovimiento(obj2.getIdTipoDeMovimiento());
//                                            obj.setCausalDeRechazo(obj.getCausalDeRechazo());
//                                            this.fDescargarDevoluciones.listaFacturasDescargadas.add(obj);
//                                            break;
//                                        }
//
//                                        Thread.sleep(10);
//                                    }
//
//                                    //}//
//                                }
                                int i = 0;

                                this.fDescargarDevoluciones.valorTotalAConsignar = 0.0;

                                /*Llena la lista de las facturas pendientes por descargar */
                                for (CFacturasPorManifiesto obj : this.fDescargarDevoluciones.listaFacturasPorManifiesto) {
                                    aparece = false;

                                    for (CFacturasPorManifiesto obj2 : this.fDescargarDevoluciones.listaFacturasDescargadas) {
                                        if (obj.getNumeroFactura().equals(obj2.getNumeroFactura())) {
                                            aparece = true;
                                        }
                                    }
                                    if (!aparece) {
                                        this.fDescargarDevoluciones.listaFacturasPendientesPorDescargar.add(obj);
                                    }


                                    /*Se calcula el valor total a consignar en la ruta*/
                                    this.fDescargarDevoluciones.valorTotalAConsignar += obj.getValorTotalFactura();
                                    Thread.sleep(10);
                                }

                                this.fDescargarDevoluciones.lblValorAConsignar.setText(nf.format(this.fDescargarDevoluciones.valorTotalAConsignar));

                                // SE RECUPERAN LOS DATOS DE LOS PRODUCTOS POR FACTURAS DESCARGADAS
                                fichero = new File(this.fDescargarDevoluciones.manifiestoActual.getRutaArchivoDescargueporductosPorFactura());
                                this.fDescargarDevoluciones.manifiestoActual.setListaCProductosPorFacturaDescargados(fichero);
                                this.fDescargarDevoluciones.listaDeCProductosPorFacturaDescargados = this.fDescargarDevoluciones.manifiestoActual.getListaCProductosPorFacturaDescargados();

                                // SE RECUPERAN LOS DATOS DE LOS RECOGIDAS DESCARGADAS
                                fichero = new File(this.fDescargarDevoluciones.manifiestoActual.getRutArchivoRecogidasporManifiesto());
                                this.fDescargarDevoluciones.manifiestoActual.setListaDeRecogidasPorManifiesto(fichero);
                                this.fDescargarDevoluciones.listaDeRecogidasPorManifiesto = this.fDescargarDevoluciones.manifiestoActual.getListaDeRecogidasPorManifiesto();
                                // this.fDescargarDevoluciones.txtKilometrosEntrada.setText("" + this.fDescargarDevoluciones.manifiestoActual.getKmEntrada());

                                // SE RECUPERAN LOS DATOS DE LAS FACTURAS NO MANIFESTADAS
                                fichero = new File(this.fDescargarDevoluciones.manifiestoActual.getRutArchivoFacturasSinManifestar());
                                this.fDescargarDevoluciones.manifiestoActual.setListaDefacturasSinManifestar(fichero);
                                this.fDescargarDevoluciones.listaDeFacturasNoManifestadas = this.fDescargarDevoluciones.manifiestoActual.getListaDefacturasSinManifestar();

                                // SE RECUPERAN LOS DATOS DE LOS SOPORTES DE CONSIGNACIONES
                                fichero = new File(this.fDescargarDevoluciones.manifiestoActual.getRutaArchivoSoportesDeConsignaciones());
                                this.fDescargarDevoluciones.manifiestoActual.setListaDeSoportesConsignaciones(fichero);
                                this.fDescargarDevoluciones.listaDeSoportesDeConsignaciones = this.fDescargarDevoluciones.manifiestoActual.getListaDeSoportesConsignaciones();

                                asignarMovimientos();

                                this.fDescargarDevoluciones.llenarjTableFacturasPorVehiculo();
                                // this.fDescargarDevoluciones.llenarjTableRecogidasPorVehiculo();
                                this.fDescargarDevoluciones.llenarjListFacturasSinManifestar();
                                //this.fDescargarDevoluciones.llenarjTableListaDeConsignaciones();


                                /* SINO EXISTE EL FICHERO SE ENCUENTRA EN DISTRIBUCIO 
                                OSEA ESTADO=3 Y HASTA AHORA SE INCIA EL PROCESO DE 
                                DESCARGUE DEL MANIFIESTO
                                 */
                            } else {
                                int i = 1;
                                this.fDescargarDevoluciones.valorTotalAConsignar = 0.0;

                                for (CFacturasPorManifiesto obj : this.fDescargarDevoluciones.listaFacturasPorManifiesto) {

                                    this.fDescargarDevoluciones.listaFacturasPendientesPorDescargar.add(obj);
                                    Thread.sleep(1);

                                    /*Se calcula el valor total a consignar en la ruta*/
                                    this.fDescargarDevoluciones.valorTotalAConsignar += obj.getValorTotalFactura();
                                    Thread.sleep(10);
                                }

                                this.fDescargarDevoluciones.lblValorAConsignar.setText(nf.format(this.fDescargarDevoluciones.valorTotalAConsignar));
                            }

                            this.fDescargarDevoluciones.llenarDatosVista();
                        }

                        break;

                    // MANIFIESTO DESCARGADO Y CUADRADO
                    case 4:

                        /*Se recuperan la lista de las Fscturas descargadas */
                        this.fDescargarDevoluciones.manifiestoActual.setListaFacturasDescargadas();

                        /*Se recuperan la lista de las Recogidas */
                        this.fDescargarDevoluciones.manifiestoActual.setListaDeRecogidasPorManifiesto();
                        this.fDescargarDevoluciones.manifiestoActual.setListaDeSoportesConsignaciones();
                        this.fDescargarDevoluciones.listaDeSoportesDeConsignaciones = this.fDescargarDevoluciones.manifiestoActual.getListaDeSoportesConsignaciones();

                        asignarMovimientos();

                        /*Se llenan las jtables respectivas */
                        this.fDescargarDevoluciones.llenarjTableFacturasPorVehiculo();
                        //this.fDescargarDevoluciones.llenarjTableFacturasDescargadas();
                        //this.fDescargarDevoluciones.llenarjTableRecogidasPorManifiesto();

                        //this.fDescargarDevoluciones.llenarjTableListaDeConsignaciones();

                        /*Se bloquea el manifiesto por estar ya descargado */
                        this.fDescargarDevoluciones.manifiestoActual.liberarManifiesto(false);

                        this.fDescargarDevoluciones.llenarDatosVista();

                        this.fDescargarDevoluciones.btnGrabar.setEnabled(false);

                        break;

                }

            }
            this.fDescargarDevoluciones.lblCirculoDeProgreso.setVisible(false);
            this.fDescargarDevoluciones.cbxMovimientoFactura.setEnabled(true);

        } catch (Exception ex) {
            this.formDescargarFacturas.manifiestoActual.liberarManifiesto(true);
            System.out.println("Error en txtManifiestoKeyPressed " + ex);
            Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    private void asignarMovimientos() throws InterruptedException {
        for (CFacturasPorManifiesto obj : this.fDescargarDevoluciones.manifiestoActual.getListaFacturasDescargadas()) {

            switch (obj.getIdTipoDeMovimiento()) {
                case 2:
                    if (obj.getValorRecaudado() == 0.0) {
                        obj.setTipoDeDEscargue("E. T. Cr");

                    } else {
                        if (obj.getValorDescuento() == 0.0) {
                            obj.setTipoDeDEscargue("E. T. Cn");
                        } else {

                            obj.setTipoDeDEscargue("E. T. %");
                        }

                    }
                    break;
                case 3:
                    obj.setTipoDeDEscargue("D. T.");
                    break;
                case 4:
                    if (obj.getValorRecaudado() == 0.0) {
                        obj.setTipoDeDEscargue("E. N. Cr");
                    } else {
                        obj.setTipoDeDEscargue("E. N. Cn");
                    }
                    break;
                case 5:
                    break;
                case 6:
                    obj.setTipoDeDEscargue("R. E.");
                    break;
                case 7:
                    obj.setTipoDeDEscargue("N. V.");
                    break;

            }
            this.fDescargarDevoluciones.listaFacturasDescargadas.add(obj);
            Thread.sleep(10);
        }
    }

    private boolean consultarManifiestoOrigen() {
        boolean manifiestoValido = false;
        try {

            // manifiestoOrigen = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtManifiesto.getText().trim()), "CURRENT_DATE");
            this.fTrasladoDeFacturas.manifiestoOrigen = new CManifiestosDeDistribucion(ini, Integer.parseInt(this.fTrasladoDeFacturas.txtManifiestoOrigen.getText().trim()));
            this.fTrasladoDeFacturas.lblCirculoDeProgresoOrigen.setVisible(true);
            // se valida que el manifiesto exista
            if (this.fTrasladoDeFacturas.manifiestoOrigen.getVehiculo() == null) { // si  no existe el manifiesto

                JOptionPane.showInternalMessageDialog(this.fTrasladoDeFacturas, "Ese manifiesto no Existe en la BB DD ", "Error", JOptionPane.WARNING_MESSAGE);

            } else { //  si existe el vehículo, existe el manifiesto

                this.fTrasladoDeFacturas.manifiestoOrigen.setListaFacturasPorManifiesto();
                this.fTrasladoDeFacturas.manifiestoOrigen.setListaDeAuxiliares();

                this.fTrasladoDeFacturas.txtManifiestoOriginal.setText(this.fTrasladoDeFacturas.txtManifiestoOrigen.getText());
                this.fTrasladoDeFacturas.txtManifiestoDestin.setEditable(false);
                CCarros car = new CCarros(ini, this.fTrasladoDeFacturas.manifiestoOrigen.getVehiculo());
                this.fTrasladoDeFacturas.txtPlacaOrigen.setText(this.fTrasladoDeFacturas.manifiestoOrigen.getVehiculo());
                this.fTrasladoDeFacturas.txtCedulaConductorOrigen.setText(this.fTrasladoDeFacturas.manifiestoOrigen.getConductor());

                for (CEmpleados obj : ini.getListaDeEmpleados()) {
                    if (obj.getCedula().equals(this.fTrasladoDeFacturas.manifiestoOrigen.getConductor())) {
                        this.fTrasladoDeFacturas.txtNombreConductorOrigen.setText(obj.getNombres() + " " + obj.getApellidos());
                        break;
                    }
                    Thread.sleep(10);
                }

                // INDIC EL ESTADO ACTUAL DEL MANIFIESTO
                switch (this.fTrasladoDeFacturas.manifiestoOrigen.getEstadoManifiesto()) {
                    case 0:
                        this.fTrasladoDeFacturas.lblCirculoDeProgresoOrigen.setVisible(false);

                        break;
                    case 1:
                        JOptionPane.showInternalMessageDialog(this.fTrasladoDeFacturas, "ESE  MANIFIESTOS EN DISTRIBUCION ,\n"
                                + "HAY QUE CREARLO Y DARLE SALIDA A DISTRIBUCION", "Error", JOptionPane.ERROR_MESSAGE);
                        this.fTrasladoDeFacturas.lblCirculoDeProgresoOrigen.setVisible(false);
                        break;

                    case 2: // MANIFIESTO CREADO, PERO NO HA SALIDO A DISTRIBUCION
                        JOptionPane.showInternalMessageDialog(this.fTrasladoDeFacturas, "ESTE MANIFIESTO DE  DISTRIBUCION ,\n"
                                + "NO SE HA GRABADO EN EL SISTEMA", "Error", JOptionPane.ERROR_MESSAGE);
                        this.fTrasladoDeFacturas.lblCirculoDeProgresoOrigen.setVisible(false);
                        break;
                    case 3: // MANIFIESTO EN DISTRIBUCION
                        llenarDatosManifiestoOrigen();
                        manifiestoValido = true;
                        break;
                    case 4:// MANIFIESTO YA DESCARGADO
                        //llenarDatosManifiestoCerrado();
                        JOptionPane.showInternalMessageDialog(this.fTrasladoDeFacturas, "ESTE MANIFIESTO  YA ESTA DESCARGADO ,\n"
                                + "NO SE PUEDE REALIZAR TRASLADOS DE FACTURAS", "Error", JOptionPane.ERROR_MESSAGE);
                        this.fTrasladoDeFacturas.lblCirculoDeProgresoOrigen.setVisible(false);

                        break;

                }

            }

        } catch (Exception ex) {
            System.out.println("Error en txtManifiestoKeyPressed ");
            Logger.getLogger(FTrasladoDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }

        return manifiestoValido;
    }

    /**
     * Método que asigna al manifiesto actual un array con la lista de las
     * facturas que al descargar no se encuenetran en el manifiesto
     *
     *
     * @param file archivo temporal donde se encuentrab grabados los datos
     */
    private void consultarManifiestoDescargado() {

        /**
         * Colocar hilo donde se muestra el gif del proceso
         */
        this.formConsultaManifiestoDescargado.lblCirculoDeProgreso.setVisible(true);
        this.formConsultaManifiestoDescargado.txtNumeroManifiesto.setEnabled(false);
        this.formConsultaManifiestoDescargado.txtKilometrosEntrada.setEnabled(false);
        this.formConsultaManifiestoDescargado.txtKilometrosEntrada.setEditable(false);

        try {

            if (this.formConsultaManifiestoDescargado.manifiestoActual == null) {
                /* Se crea el manifiesto de distribucion*/
                int manifiesto = Integer.parseInt(this.formConsultaManifiestoDescargado.txtNumeroManifiesto.getText().trim());
                this.formConsultaManifiestoDescargado.manifiestoActual = new CManifiestosDeDistribucion(ini, manifiesto);
            }
            if (this.formConsultaManifiestoDescargado.manifiestoActual.getEstadoManifiesto() == 2) {
                JOptionPane.showInternalMessageDialog(this.formConsultaManifiestoDescargado, "Este manifiesto no tiene facturas asignadas ", "Error, sin facturas", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // se valida que el numeroDeManifiesto exista
            // si  no existe el numeroDeManifiesto
            if (this.formConsultaManifiestoDescargado.manifiestoActual.getNumeroManifiesto() == null) {
                this.formConsultaManifiestoDescargado.manifiestoActual = null;
                JOptionPane.showInternalMessageDialog(this.formConsultaManifiestoDescargado, "ESE MANIFIESTO NO EXISTE EN LA BASE DE DATOS ", "Error", JOptionPane.ERROR_MESSAGE);

                // si el manifiesto existe   
            } else {

                /* SWITCH PARA DETERMINAR EL MOVIMIENTO DEL MANIFIESTO */
                switch (this.formConsultaManifiestoDescargado.manifiestoActual.getEstadoManifiesto()) {

                    case 1:

                        JOptionPane.showInternalMessageDialog(this.formConsultaManifiestoDescargado, "ESE  MANIFIESTOS EN DISTRIBUCION ,\n"
                                + "HAY QUE CREARLO Y DARLE SALIDA A DISTRIBUCION", "Error", 0);
                        this.formConsultaManifiestoDescargado.txtNumeroManifiesto.setEnabled(true);
                        break;

                    // MANIFIESTO CREADO PERO NO HA SALIDO A DISTRIBUCION
                    case 2:

                        JOptionPane.showInternalMessageDialog(this.formConsultaManifiestoDescargado, "ESTE MANIFIESTO DE  DISTRIBUCION ,\n"
                                + "NO SE HA GRABADO EN EL SISTEMA", "Error", 0);
                        this.formConsultaManifiestoDescargado.txtNumeroManifiesto.setEnabled(true);
                        break;

                    // MANIFIESTO GRABADO Y ESTA EN DISTRIBUCION
                    case 3:
                        JOptionPane.showInternalMessageDialog(this.formConsultaManifiestoDescargado, "Este manifiesto se encuentra en Distribucion", "En Distribucion", JOptionPane.WARNING_MESSAGE);

                        break;

                    // MANIFIESTO DESCARGADO Y CUADRADO
                    case 4:

                        /*Se recuperan la lista de las Fscturas descargadas */
                        this.formConsultaManifiestoDescargado.manifiestoActual.setListaFacturasDescargadas();

                        /*Se recuperan la lista de las Recogidas */
                        this.formConsultaManifiestoDescargado.manifiestoActual.setListaDeRecogidasPorManifiesto();
                        this.formConsultaManifiestoDescargado.manifiestoActual.setListaDeSoportesConsignaciones();
                        this.formConsultaManifiestoDescargado.listaDeSoportesDeConsignaciones = this.formConsultaManifiestoDescargado.manifiestoActual.getListaDeSoportesConsignaciones();

                        for (CFacturasPorManifiesto obj : this.formConsultaManifiestoDescargado.manifiestoActual.getListaFacturasDescargadas()) {

                            switch (obj.getIdTipoDeMovimiento()) {
                                case 2:
                                    if (obj.getValorRecaudado() == 0.0) {
                                        obj.setTipoDeDEscargue("E. T. Cr");

                                    } else {
                                        if (obj.getValorDescuento() == 0.0) {
                                            obj.setTipoDeDEscargue("E. T. Cn");
                                        } else {

                                            obj.setTipoDeDEscargue("E. T. %");
                                        }

                                    }
                                    break;
                                case 3:
                                    obj.setTipoDeDEscargue("D. T.");
                                    break;
                                case 4:
                                    if (obj.getValorRecaudado() == 0.0) {
                                        obj.setTipoDeDEscargue("E. N. Cr");
                                    } else {
                                        obj.setTipoDeDEscargue("E. N. Cn");
                                    }
                                    break;
                                case 5:
                                    break;
                                case 6:
                                    obj.setTipoDeDEscargue("R. E.");
                                    break;

                                case 7:
                                    obj.setTipoDeDEscargue("N. V.");
                                    break;
                            }
                            this.formConsultaManifiestoDescargado.listaFacturasDescargadas.add(obj);
                            Thread.sleep(10);
                        }

                        /*Se llenan las jtables respectivas */
                        this.formConsultaManifiestoDescargado.llenarjTableFacturasPorVehiculo();

                        /*Se bloquea el manifiesto por estar ya descargado */
                        this.formConsultaManifiestoDescargado.manifiestoActual.liberarManifiesto(false);

                        this.formConsultaManifiestoDescargado.llenarDatosVista();

                        this.formConsultaManifiestoDescargado.btnGrabar.setEnabled(false);

                        break;

                }

            }
            this.formConsultaManifiestoDescargado.lblCirculoDeProgreso.setVisible(false);

        } catch (Exception ex) {

            this.formConsultaManifiestoDescargado.manifiestoActual.liberarManifiesto(true);
            System.out.println("Error en txtManifiestoKeyPressed " + ex);
            Logger.getLogger(DescargarFacturas.class.getName()).log(Level.SEVERE, null, ex);

        }

    }

    public synchronized void llenarDatosManifiestoOrigen() throws Exception {

        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        this.fTrasladoDeFacturas.txtManifiestoOriginal.setEnabled(true);
        this.fTrasladoDeFacturas.txtManifiestoOriginal.setEditable(false);

        this.fTrasladoDeFacturas.txtCedulaConductorOrigen.setEnabled(true);
        this.fTrasladoDeFacturas.txtCedulaConductorOrigen.setEditable(false);
        this.fTrasladoDeFacturas.txtNombreAuxiliar1Origen.setEnabled(true);
        this.fTrasladoDeFacturas.txtNombreAuxiliar1Origen.setEditable(false);
        this.fTrasladoDeFacturas.txtNombreAuxiliar2Origen.setEnabled(true);
        this.fTrasladoDeFacturas.txtNombreAuxiliar2Origen.setEditable(false);
        this.fTrasladoDeFacturas.txtNombreAuxiliar3Origen.setEnabled(true);
        this.fTrasladoDeFacturas.txtNombreAuxiliar3Origen.setEditable(false);
        this.fTrasladoDeFacturas.txtNombreConductorOrigen.setEnabled(true);
        this.fTrasladoDeFacturas.txtNombreConductorOrigen.setEditable(false);
        this.fTrasladoDeFacturas.txtNombreDespachadorOrigen.setEnabled(true);
        this.fTrasladoDeFacturas.txtNombreDespachadorOrigen.setEditable(false);
        this.fTrasladoDeFacturas.txtPlacaOrigen.setEnabled(true);
        this.fTrasladoDeFacturas.txtPlacaOrigen.setEditable(false);
        //this.fTrasladoDeFacturas.btnNuevo.setEnabled(false);
        //this.fTrasladoDeFacturas.btnNuevo.setEnabled(true);
        ;

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
//        manifiestoOrigen.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        //  manifiestoOrigen.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");

        DefaultTableModel modelo = (DefaultTableModel) this.fTrasladoDeFacturas.tblFacturasPorManifiestoOrigen.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int x = a; x >= 0; x--) {
                modelo.removeRow(x);
            }
        }

        int cantidadFacturasEnManifiesto = 0;

        this.fTrasladoDeFacturas.modelo2 = (DefaultTableModel) this.fTrasladoDeFacturas.tblFacturasPorManifiestoOrigen.getModel();

        int promedio = this.fTrasladoDeFacturas.manifiestoOrigen.getListaFacturasPorManifiesto().size() / 100;
        int i = 0;
        int valorBarra = 0;

        double valor = 0.0;
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO
        for (CFacturasPorManifiesto obj : this.fTrasladoDeFacturas.manifiestoOrigen.getListaFacturasPorManifiesto()) {
            if (i > promedio) {
                i = 0;
                valorBarra++;
                this.fTrasladoDeFacturas.barraOrigen.setValue(valorBarra);

            }

            // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
            // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
            int filaTabla2 = this.fTrasladoDeFacturas.tblFacturasPorManifiestoOrigen.getRowCount();
            this.fTrasladoDeFacturas.modelo2.addRow(new Object[this.fTrasladoDeFacturas.tblFacturasPorManifiestoOrigen.getRowCount()]);

            this.fTrasladoDeFacturas.tblFacturasPorManifiestoOrigen.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
            this.fTrasladoDeFacturas.tblFacturasPorManifiestoOrigen.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de la factura

            this.fTrasladoDeFacturas.tblFacturasPorManifiestoOrigen.setValueAt(obj.getNombreDeCliente(), filaTabla2, 2); // cliente

            this.fTrasladoDeFacturas.tblFacturasPorManifiestoOrigen.setValueAt(nf.format(obj.getValorARecaudarFactura()), filaTabla2, 3); // valor a recaudar  de la factura   

            // se ubica en la fila insertada
            this.fTrasladoDeFacturas.tblFacturasPorManifiestoOrigen.changeSelection(filaTabla2, 0, false, false);

            cantidadFacturasEnManifiesto++;
            i++;
            Thread.sleep(10);
        }

        /* Se llenan os campos de texto de los auiliares*/
        this.fTrasladoDeFacturas.llenarTxtAuxiliaresOrigen();

        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
        if (this.fTrasladoDeFacturas.manifiestoOrigen.getDespachador().equals("0")) {
            this.fTrasladoDeFacturas.txtNombreDespachadorOrigen.setText("");

        } else {
            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (this.fTrasladoDeFacturas.manifiestoOrigen.getDespachador().equals(obj.getCedula())) {

                    this.fTrasladoDeFacturas.txtNombreDespachadorOrigen.setText(obj.getNombres() + " " + obj.getApellidos());
                }
                Thread.sleep(10);
            }
        }

        //btnExportar.setEnabled(true);
        // btnImprimir.setEnabled(true);
        this.fTrasladoDeFacturas.barraOrigen.setValue(100);
        this.fTrasladoDeFacturas.lblCirculoDeProgresoOrigen.setVisible(false);
        this.fTrasladoDeFacturas.btnImprimir.requestFocus();

    }

    public boolean consultarManifiestoDestino() {
        boolean manifiestoValido = false;
        try {

            // manifiestoOrigen = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtManifiesto.getText().trim()), "CURRENT_DATE");
            this.fTrasladoDeFacturas.manifiestoDestino = new CManifiestosDeDistribucion(ini, Integer.parseInt(this.fTrasladoDeFacturas.txtManifiestoDestino.getText().trim()));
            this.fTrasladoDeFacturas.lblCirculoDeProgresoDestino.setVisible(true);

            // se valida que el manifiesto exista
            if (this.fTrasladoDeFacturas.manifiestoDestino.getVehiculo() == null) { // si  no existe el manifiesto

                JOptionPane.showInternalMessageDialog(this.fTrasladoDeFacturas, "Ese manifiesto no Existe en la BB DD ", "Error", JOptionPane.WARNING_MESSAGE);

            } else { //  si existe el vehículo, existe el manifiesto

                /*Se recuperan las facuras que tenga asignado el manifiesto */
                this.fTrasladoDeFacturas.listaDeFacturasParaTrasladar = new ArrayList();
                this.fTrasladoDeFacturas.listaDeFacturasEnManifiesto = new ArrayList();

                this.fTrasladoDeFacturas.manifiestoDestino.setListaFacturasPorManifiesto();
                this.fTrasladoDeFacturas.listaDeFacturasEnManifiesto = this.fTrasladoDeFacturas.manifiestoDestino.getListaFacturasPorManifiesto();

                /*Se recuperan los auxiliares asignados */
                this.fTrasladoDeFacturas.manifiestoDestino.setListaDeAuxiliares();

                /*Se llenan los campos del fBuscarManifiestos*/
                this.fTrasladoDeFacturas.txtManifiestoDestin.setText(this.fTrasladoDeFacturas.txtManifiestoDestino.getText());
                this.fTrasladoDeFacturas.txtManifiestoDestin.setEditable(false);
                this.fTrasladoDeFacturas.txtManifiestoDestin.setEnabled(true);
                CCarros car = new CCarros(ini, this.fTrasladoDeFacturas.manifiestoDestino.getVehiculo());
                this.fTrasladoDeFacturas.txtPlacaDestino.setText(this.fTrasladoDeFacturas.manifiestoDestino.getVehiculo());
                this.fTrasladoDeFacturas.txtCedulaConductorDestino.setText(this.fTrasladoDeFacturas.manifiestoDestino.getConductor());

                for (CEmpleados obj : ini.getListaDeEmpleados()) {
                    if (obj.getCedula().equals(this.fTrasladoDeFacturas.manifiestoDestino.getConductor())) {
                        this.fTrasladoDeFacturas.txtNombreConductorDestino.setText(obj.getNombres() + " " + obj.getApellidos());
                        break;
                    }
                    Thread.sleep(10);
                }

                // INDIC EL ESTADO ACTUAL DEL MANIFIESTO
                switch (this.fTrasladoDeFacturas.manifiestoDestino.getEstadoManifiesto()) {
                    case 0:
                        this.fTrasladoDeFacturas.lblCirculoDeProgresoDestino.setVisible(false);

                        break;
                    case 1:
                        JOptionPane.showInternalMessageDialog(this.fTrasladoDeFacturas, "ESE  MANIFIESTOS EN DISTRIBUCION ,\n"
                                + "HAY QUE CREARLO Y DARLE SALIDA A DISTRIBUCION", "Error", JOptionPane.ERROR_MESSAGE);
                        this.fTrasladoDeFacturas.lblCirculoDeProgresoDestino.setVisible(false);
                        break;

                    case 2: // MANIFIESTO CREADO, PERO NO HA SALIDO A DISTRIBUCION
                        // JOptionPane.showInternalMessageDialog(this, "ESTE MANIFIESTO DE  DISTRIBUCION ,\n"
                        //         + "NO SE HA GRABADO EN EL SISTEMA", "Error",JOptionPane.WARNING_MESSAGE);
                        llenarDatosManifiestoDestino();
                        manifiestoValido = true;
                        //listaDeFacturasTrasladadas = new ArrayList();
                        break;
                    case 3: // MANIFIESTO EN DISTRIBUCION
                        llenarDatosManifiestoDestino();
                        manifiestoValido = true;
                        //listaDeFacturasTrasladadas = new ArrayList();
                        break;
                    case 4:// MANIFIESTO YA DESCARGADO
                        //llenarDatosManifiestoCerrado();
                        JOptionPane.showInternalMessageDialog(this.fTrasladoDeFacturas, "ESTE MANIFIESTO  YA ESTA DESCARGADO ,\n"
                                + "NO SE PUEDE REALIZAR TRASLADOS DE FACTURAS", "Error", JOptionPane.ERROR_MESSAGE);
                        this.fTrasladoDeFacturas.lblCirculoDeProgresoDestino.setVisible(false);

                        break;

                }

            }

        } catch (Exception ex) {
            System.out.println("Error en txtManifiestoKeyPressed ");
            Logger.getLogger(FTrasladoDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return manifiestoValido;
    }

    public synchronized void llenarDatosManifiestoDestino() throws Exception {

        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        if (this.fTrasladoDeFacturas.isGrabado()) {
            this.fTrasladoDeFacturas.txtManifiestoDestin.setEnabled(false);
            this.fTrasladoDeFacturas.txtManifiestoDestin.setEditable(false);

            this.fTrasladoDeFacturas.txtNumeroFactura.setEnabled(false);
            this.fTrasladoDeFacturas.txtNumeroFactura.setEditable(false);
        } else {
            this.fTrasladoDeFacturas.txtManifiestoDestin.setEnabled(true);
            this.fTrasladoDeFacturas.txtManifiestoDestin.setEditable(false);

            this.fTrasladoDeFacturas.txtNumeroFactura.setEnabled(true);
            this.fTrasladoDeFacturas.txtNumeroFactura.setEditable(true);
        }

        this.fTrasladoDeFacturas.txtCedulaConductorDestino.setEnabled(true);
        this.fTrasladoDeFacturas.txtCedulaConductorDestino.setEditable(false);
        this.fTrasladoDeFacturas.txtNombreAuxiliar1Destino.setEnabled(true);
        this.fTrasladoDeFacturas.txtNombreAuxiliar1Destino.setEditable(false);
        this.fTrasladoDeFacturas.txtNombreAuxiliar2Destino.setEnabled(true);
        this.fTrasladoDeFacturas.txtNombreAuxiliar2Destino.setEditable(false);
        this.fTrasladoDeFacturas.txtNombreAuxiliar3Destino.setEnabled(true);
        this.fTrasladoDeFacturas.txtNombreAuxiliar3Destino.setEditable(false);
        this.fTrasladoDeFacturas.txtNombreConductorDestino.setEnabled(true);
        this.fTrasladoDeFacturas.txtNombreConductorDestino.setEditable(false);
        this.fTrasladoDeFacturas.txtNombreDespachadorDestino.setEnabled(true);
        this.fTrasladoDeFacturas.txtNombreDespachadorDestino.setEditable(false);
        this.fTrasladoDeFacturas.txtPlacaDestino.setEnabled(true);
        this.fTrasladoDeFacturas.txtPlacaDestino.setEditable(false);
        this.fTrasladoDeFacturas.cbxPrefijos.setEnabled(true);

//        this.fTrasladoDeFacturas.btnNuevo.setEnabled(false);
        this.fTrasladoDeFacturas.btnAgregar.setEnabled(true);
        this.fTrasladoDeFacturas.btnImprimir.setEnabled(false);

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
//        manifiestoOrigen.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        //  manifiestoOrigen.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");

        DefaultTableModel modelo = (DefaultTableModel) this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int x = a; x >= 0; x--) {
                modelo.removeRow(x);
            }
        }

        this.fTrasladoDeFacturas.modelo2 = (DefaultTableModel) this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.getModel();

        int promedio = this.fTrasladoDeFacturas.manifiestoOrigen.getListaFacturasPorManifiesto().size() / 100;
        int i = 0;
        int valorBarra = 0;

        double valor = 0.0;
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO
        for (CFacturasPorManifiesto obj : this.fTrasladoDeFacturas.manifiestoDestino.getListaFacturasPorManifiesto()) {
            if (i > promedio) {
                i = 0;
                valorBarra++;
                this.fTrasladoDeFacturas.barraDestino.setValue(valorBarra);

            }

           

            // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
            // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
            int filaTabla2 = this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.getRowCount();
            this.fTrasladoDeFacturas.modelo2.addRow(new Object[this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.getRowCount()]);

            this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
            this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de la factura

            this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.setValueAt(obj.getNombreDeCliente(), filaTabla2, 2); // cliente

            this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.setValueAt(nf.format(obj.getValorARecaudarFactura()), filaTabla2, 3); // valor a recaudar  de la factura   

            // se ubica en la fila insertada
            this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.changeSelection(filaTabla2, 0, false, false);

            i++;
            Thread.sleep(10);
        }

        /* Se llenan os campos de texto de los auiliares*/
        this.fTrasladoDeFacturas.llenarTxtAuxiliaresDestino();

        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
        if (this.fTrasladoDeFacturas.manifiestoDestino.getDespachador().equals("0")) {
            this.fTrasladoDeFacturas.txtNombreDespachadorDestino.setText("");

        } else {
            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (this.fTrasladoDeFacturas.manifiestoDestino.getDespachador().equals(obj.getCedula())) {

                    this.fTrasladoDeFacturas.txtNombreDespachadorDestino.setText(obj.getNombres() + " " + obj.getApellidos());
                }
                Thread.sleep(10);
            }
        }

        this.fTrasladoDeFacturas.barraDestino.setValue(100);
        this.fTrasladoDeFacturas.lblCirculoDeProgresoDestino.setVisible(false);
        this.fTrasladoDeFacturas.btnImprimir.requestFocus();

    }

    private void consultarManifiestoParaHabilitar() {
        try {

            // manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtManifiesto.getText().trim()), "CURRENT_DATE");
            this.fHabilitarManifiesto.manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(this.fHabilitarManifiesto.txtManifiesto.getText().trim()));

            // se valida que el manifiesto exista
            if (this.fHabilitarManifiesto.manifiestoActual.getVehiculo() == null) { // si  no existe el manifiesto

                JOptionPane.showInternalMessageDialog(this.fHabilitarManifiesto, "Ese manifiesto no Existe en la BB DD ", "Error", JOptionPane.WARNING_MESSAGE);
                this.fHabilitarManifiesto.txtManifiesto.requestFocus();
                this.fHabilitarManifiesto.txtManifiesto.requestFocus();
            } else { //  si existe el vehículo, existe el manifiesto

                this.fHabilitarManifiesto.manifiestoActual.setListaFacturasPorManifiesto();
                this.fHabilitarManifiesto.manifiestoActual.setListaDeAuxiliares();

                this.fHabilitarManifiesto.txtManifiesto.setEditable(false);
                CCarros car = new CCarros(ini, this.fHabilitarManifiesto.manifiestoActual.getVehiculo());
                this.fHabilitarManifiesto.txtPlaca.setText(this.fHabilitarManifiesto.manifiestoActual.getVehiculo());
                this.fHabilitarManifiesto.txtCedulaConductor.setText(this.fHabilitarManifiesto.manifiestoActual.getConductor());

                for (CEmpleados obj : ini.getListaDeEmpleados()) {
                    if (obj.getCedula().equals(this.fHabilitarManifiesto.manifiestoActual.getConductor())) {
                        this.fHabilitarManifiesto.txtNombreConductor.setText(obj.getNombres() + " " + obj.getApellidos());
                        break;
                    }
                    Thread.sleep(10);
                }

                this.fHabilitarManifiesto.txtCodigoManifiesto.setText(this.fHabilitarManifiesto.manifiestoActual.codificarManifiesto());

                // INDIC EL ESTADO ACTUAL DEL MANIFIESTO
                switch (this.fHabilitarManifiesto.manifiestoActual.getEstadoManifiesto()) {
                    case 0:

                        break;
                    case 1:
                        JOptionPane.showInternalMessageDialog(this.fHabilitarManifiesto, "ESE  MANIFIESTOS EN DISTRIBUCION ,\n"
                                + "HAY QUE CREARLO Y DARLE SALIDA A DISTRIBUCION", "Error", 0);
                        break;

                    case 2: // MANIFIESTO CREADO, PERO NO HA SALIDO A DISTRIBUCION
                        llenarDatosManifiestoHabilitado();
                        break;
                    case 3: // MANIFIESTO EN DISTRIBUCION
                        llenarDatosManifiestoHabilitado();
                        break;
                    case 4:// MANIFIESTO YA DESCARGADO
                        JOptionPane.showInternalMessageDialog(this.fHabilitarManifiesto, "ESTE MANIFIESTO DE  DISTRIBUCION ,\n"
                                + "NO SE HA GRABADO EN EL SISTEMA", "Error", 0);

                        break;

                }

            }

        } catch (Exception ex) {
            System.out.println("Error en txtManifiestoKeyPressed ");
            Logger.getLogger(FHabilitarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llenarDatosManifiestoHabilitado() throws Exception {
        double valorTotalManifiesto = 0.0;

        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        this.fHabilitarManifiesto.txtCanalVentas.setEnabled(true);
        this.fHabilitarManifiesto.txtCanalVentas.setEditable(false);
        this.fHabilitarManifiesto.txtCedulaConductor.setEnabled(true);
        this.fHabilitarManifiesto.txtCedulaConductor.setEditable(false);
        this.fHabilitarManifiesto.txtCodigoManifiesto.setEnabled(true);
        this.fHabilitarManifiesto.txtCodigoManifiesto.setEditable(false);
        this.fHabilitarManifiesto.txtNombreAuxiliar1.setEnabled(true);
        this.fHabilitarManifiesto.txtNombreAuxiliar1.setEditable(false);
        this.fHabilitarManifiesto.txtNombreAuxiliar2.setEnabled(true);
        this.fHabilitarManifiesto.txtNombreAuxiliar2.setEditable(false);
        this.fHabilitarManifiesto.txtNombreAuxiliar3.setEnabled(true);
        this.fHabilitarManifiesto.txtNombreAuxiliar3.setEditable(false);
        this.fHabilitarManifiesto.txtNombreConductor.setEnabled(true);
        this.fHabilitarManifiesto.txtNombreConductor.setEditable(false);
        this.fHabilitarManifiesto.txtNombreDespachador.setEnabled(true);
        this.fHabilitarManifiesto.txtNombreDespachador.setEditable(false);
        this.fHabilitarManifiesto.txtPlaca.setEnabled(true);
        this.fHabilitarManifiesto.txtPlaca.setEditable(false);
        this.fHabilitarManifiesto.txtRuta.setEnabled(true);
        this.fHabilitarManifiesto.txtRuta.setEditable(false);

        this.fHabilitarManifiesto.btnNuevo.setEnabled(false);
        this.fHabilitarManifiesto.btnNuevo.setEnabled(true);
        this.fHabilitarManifiesto.btnImprimir.setEnabled(false);

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
//        manifiestoActual.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        //  manifiestoActual.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");

        int cantidadFacturasEnManifiesto = 0;

        /* Se llenan os campos de texto de los auiliares*/
        this.fHabilitarManifiesto.llenarTxtAuxiliares();

        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
        if (this.fHabilitarManifiesto.manifiestoActual.getDespachador().equals("0")) {
            this.fHabilitarManifiesto.txtNombreDespachador.setText("");

        } else {
            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (this.fHabilitarManifiesto.manifiestoActual.getDespachador().equals(obj.getCedula())) {

                    this.fHabilitarManifiesto.txtNombreDespachador.setText(obj.getNombres() + " " + obj.getApellidos());
                }
            }
            Thread.sleep(10);
        }

        // SE BUSCA EL CANAL Y SE LLENA EL CAMPO
        for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
            if (obj.getIdCanalDeVenta() == this.fHabilitarManifiesto.manifiestoActual.getIdCanal()) {
                this.fHabilitarManifiesto.txtCanalVentas.setText(obj.getNombreCanalDeVenta());
                break;

            }
            Thread.sleep(10);
        }

        // SE BUSCA EL RUTA Y SE LLENA EL CAMPO
        for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
            if (obj.getIdRutasDeDistribucion() == this.fHabilitarManifiesto.manifiestoActual.getIdRuta()) {
                this.fHabilitarManifiesto.txtRuta.setText(obj.getNombreRutasDeDistribucion());
            }
            Thread.sleep(10);
        }

        this.fHabilitarManifiesto.txtCodigoManifiesto.setText(this.fHabilitarManifiesto.manifiestoActual.codificarManifiesto());

        this.fHabilitarManifiesto.lblCirculoDeProgreso.setVisible(false);
        this.fHabilitarManifiesto.txtManifiesto.setEnabled(false);
        this.fHabilitarManifiesto.btnGrabar.setEnabled(true);
        this.fHabilitarManifiesto.btnImprimir.setEnabled(true);
        this.fHabilitarManifiesto.btnImprimir.requestFocus();

    }

    private void consultarManifiestoParaModificar() throws InterruptedException {

        // manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtManifiesto.getText().trim()), "CURRENT_DATE");
        this.fModificarManifiesto.manifiestoActual = null;
        this.fModificarManifiesto.lblCirculoDeProgreso1.setVisible(true);

        for (CManifiestosDeDistribucion manifiesto : ini.getListaDeManifiestossinDescargar()) {
            if (manifiesto.getNumeroManifiesto().equals(fModificarManifiesto.txtNumeroDeManifiesto.getText().trim())) {
                fModificarManifiesto.manifiestoActual = manifiesto;
            }
            Thread.sleep(10);
        }

        // se valida que el manifiesto exista
        if (this.fModificarManifiesto.manifiestoActual == null) { // si  no existe el manifiesto

            JOptionPane.showInternalMessageDialog(this.fModificarManifiesto, "Ese manifiesto no Existe en la BB DD",
                    "Error", JOptionPane.WARNING_MESSAGE);

            this.fModificarManifiesto.txtNumeroDeManifiesto.requestFocus();
            this.fModificarManifiesto.txtNumeroDeManifiesto.requestFocus();

        } else {
            try {
                //  si existe el vehículo, existe el manifiesto

                this.fModificarManifiesto.manifiestoActual.setListaFacturasPorManifiesto();
                this.fModificarManifiesto.manifiestoActual.setListaDeAuxiliares();
                //this.fModificarManifiesto.manifiestoActual.setListaDeDescuentos();

                this.fModificarManifiesto.txtNumeroDeManifiesto.setEditable(false);

                this.fModificarManifiesto.txtPlaca.setText(this.fModificarManifiesto.manifiestoActual.getVehiculo());
                this.fModificarManifiesto.txtnombreDeConductor.setText(this.fModificarManifiesto.manifiestoActual.getConductor());

                // this.fModificarManifiesto.llenarDatosManifiestoCerrado();
            } catch (Exception ex) {
                Logger.getLogger(HiloConsultarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public synchronized void llenarDatosManifiestoParaModificar() throws Exception {
        System.out.println("Trae los datos del manifiesto -> \n\n");
        try {

            this.fModificarManifiesto.lblCirculoDeProgreso1.setVisible(true);
            fModificarManifiesto.manifiestoActual = null;

            fModificarManifiesto.manifiestoActual = new CManifiestosDeDistribucion(ini, Integer.parseInt(fModificarManifiesto.txtNumeroDeManifiesto.getText()));

            if (fModificarManifiesto.manifiestoActual.getEstadoManifiesto() >= 4
                    || fModificarManifiesto.manifiestoActual.getNumeroManifiesto() == null) {
                JOptionPane.showInternalMessageDialog(this.fModificarManifiesto, "Ese manifiesto no se puede modificar en la BB DD",
                        "Error", JOptionPane.WARNING_MESSAGE);
                this.fModificarManifiesto.lblCirculoDeProgreso1.setVisible(false);

                return;
            }

            this.fModificarManifiesto.manifiestoActual.setListaFacturasPorManifiesto();

            //this.fModificarManifiesto.listaDeFacturasPorManifiesto = new ArrayList();
            this.fModificarManifiesto.llenarDatosManifiestoAModificar();
            this.fModificarManifiesto.lblCirculoDeProgreso1.setVisible(false);


            /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
            if (this.fModificarManifiesto.manifiestoActual.getListaFacturasPorManifiesto() != null) {
                fModificarManifiesto.btnImprimir.setEnabled(true);
                fModificarManifiesto.btnGrabar.setEnabled(true);
            }

            this.fModificarManifiesto.lblCirculoDeProgreso1.setVisible(false);

            this.fModificarManifiesto.txtKmDeSalida.requestFocus();

        } catch (Exception ex) {
            JOptionPane.showInternalMessageDialog(this.fModificarManifiesto, "Ese manifiesto no se puede modificar en la BB DD",
                    "Error", JOptionPane.WARNING_MESSAGE);
            Logger.getLogger(HiloConsultarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            this.fModificarManifiesto.lblCirculoDeProgreso1.setVisible(false);

        }

    }

    private void consultarManifiestoPedidosEnRutaConIntegrador() {

        System.out.println("Trae los datos del manifiesto -> \n\n");
        try {

            this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso1.setVisible(true);
            fManifestarPedidosEnRutaConIntegrador.manifiestoActual = new CManifiestosDeDistribucion(ini);

            if (ini.getListaDeManifiestossinDescargar() != null) {
                for (CManifiestosDeDistribucion mfto : ini.getListaDeManifiestossinDescargar()) {
                    if (mfto.getVehiculo().equals(fManifestarPedidosEnRutaConIntegrador.txtPlaca.getText().trim())) {
                        fManifestarPedidosEnRutaConIntegrador.manifiestoActual = mfto;

                    }
                }
                Thread.sleep(10);
            }

            if (fManifestarPedidosEnRutaConIntegrador.manifiestoActual.getNumeroManifiesto() == null) {

                fManifestarPedidosEnRutaConIntegrador.manifiestoActual = new CManifiestosDeDistribucion(ini, fManifestarPedidosEnRutaConIntegrador.txtPlaca.getText().trim());
            }

            fManifestarPedidosEnRutaConIntegrador.carro.setListaDeDocumentosPorVehiculoVencidos();

            if (fManifestarPedidosEnRutaConIntegrador.carro.getListaDeDocumentosPorVehiculoVencidos() != null) {
                fManifestarPedidosEnRutaConIntegrador.jBtnDocumentosVehiculos.setEnabled(true);
            }
            this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso2.setVisible(false);

            /*Se crea una lista provisionnal de acuerdo a reqerimiento*/
            List<CFacturasPorManifiesto> listaFacturasxmanifiesto = new ArrayList();

            this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setListaFacturasPorManifiesto(listaFacturasxmanifiesto);

            if (fManifestarPedidosEnRutaConIntegrador.manifiestoActual.getEstadoManifiesto() == 0) {
                fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setIsFree(1);

            }

            /* ENTRA AL CASO CORRESPONDIENTE DE ACUERDO AL ESTADO DEL MANIFIESTO */
            switch (this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.getEstadoManifiesto()) {

                case 0:
                    /* El caso cuando por primera vez se le crea a un vehículo
                     un manifiesto de distribución 
                     */
                    // fManifestarPedidosEnRutaConIntegrador.listaDeFacturasPorManifiesto=new ArrayList();
                    this.fManifestarPedidosEnRutaConIntegrador.crearNuevoManifiesto(this.fManifestarPedidosEnRutaConIntegrador.carro);

                    listaFacturasxmanifiesto = new ArrayList();
                    this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setListaFacturasPorManifiesto(listaFacturasxmanifiesto);
                    break;

                case 1:// CASO CUANDO NO HAY MANIFIESTOS PENDIENTES
                    // fManifestarPedidosEnRutaConIntegrador.listaDeFacturasPorManifiesto=new ArrayList();
                    this.fManifestarPedidosEnRutaConIntegrador.crearNuevoManifiesto(this.fManifestarPedidosEnRutaConIntegrador.carro);
                    //this.fManifestarPedidosEnRutaConIntegrador.listaDeFacturasPorManifiesto = new ArrayList();
                    this.fManifestarPedidosEnRutaConIntegrador.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRutaConIntegrador.carro.getKilometrajeActual());
                    this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso1.setVisible(false);
                    this.fManifestarPedidosEnRutaConIntegrador.txtnombreDeConductor.setEditable(false);

                    break;

                case 2:// TIENE MANIFIESTO ASIGANDO PERO NO SE HA CERRADO;
                    //this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.setRutArchivofacturasporManifiesto();
                    //this.fManifestarPedidosEnRutaConIntegrador.listaDeFacturasPorManifiesto = new ArrayList();
                    this.fManifestarPedidosEnRutaConIntegrador.modificarManifiesto();

                    this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso1.setVisible(false);
                    //this.fManifestarPedidosEnRutaConIntegrador.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRutaConIntegrador.carro.getKilometraje());
                    break;

                case 3:// VEHICULO EN DISTRIBUCION,  NO SE PUEDE ASIGNAR OTRO MANIFIESTO

                    if (this.ini.isPermitirVariosManifiestos() != true) {
                        JOptionPane.showInternalMessageDialog(this.fManifestarPedidosEnRutaConIntegrador, "Ese vehiculo ya tiene asignado un manifiesto de distribucion,\n"
                                + "debe liquidar y cerrarlo para crear uno nuevo", "Error", 0);
                        this.fManifestarPedidosEnRutaConIntegrador.cancelar();
                        break;
                    }

                    int reply = JOptionPane.showConfirmDialog(this.fManifestarPedidosEnRutaConIntegrador, "Desea Crear otro manifiesto de Distribucion?", "Vehiculo ya aparece en Ruta", YES_NO_OPTION, WARNING_MESSAGE);
                    this.fManifestarPedidosEnRutaConIntegrador.nuevo = false;

                    if (reply == JOptionPane.YES_OPTION) {

                        this.fManifestarPedidosEnRutaConIntegrador.crearNuevoManifiesto(this.fManifestarPedidosEnRutaConIntegrador.carro);
                        this.fManifestarPedidosEnRutaConIntegrador.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRutaConIntegrador.carro.getKilometrajeActual());
                        this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso1.setVisible(false);
                        //this.fManifestarPedidosEnRutaConIntegrador.listaDeFacturasPorManifiesto = new ArrayList();
                    } else {

                        //this.fManifestarPedidosEnRutaConIntegrador.listaDeFacturasPorManifiesto = new ArrayList();
                        this.fManifestarPedidosEnRutaConIntegrador.llenarDatosManifiestoCerrado();
                        this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso1.setVisible(false);
                    }

                    /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
                    if (this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.getListaFacturasPorManifiesto() != null) {
                        fManifestarPedidosEnRutaConIntegrador.btnImprimir.setEnabled(true);
                    }

                    // JOptionPane.showInternalMessageDialog(this.fManifestarPedidosEnRutaConIntegrador, "ESE VEHICULO ESTA EN DISTRIBUCION EN ESTOS MOMENTOS,\n"
                    //       + "NO SE PUEDE CREAR UN NUEVO MANIFIESTO", "Error", 0);
                    break;

                case 4: // NUMERO DE MANIFIESTO YA DESCARGADO Y SE PUEDE CREAR OTRO
                    //this.fManifestarPedidosEnRutaConIntegrador.listaDeFacturasPorManifiesto = new ArrayList();

                    this.fManifestarPedidosEnRutaConIntegrador.jTabbedPane1.setEnabledAt(this.fManifestarPedidosEnRutaConIntegrador.jTabbedPane1.indexOfComponent(this.fManifestarPedidosEnRutaConIntegrador.pnlAgregarLista), true);
                    // jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlAgregarLista));
                    this.fManifestarPedidosEnRutaConIntegrador.crearNuevoManifiesto(this.fManifestarPedidosEnRutaConIntegrador.carro);
                    this.fManifestarPedidosEnRutaConIntegrador.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRutaConIntegrador.carro.getKilometrajeActual());
                    this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso1.setVisible(false);
                    this.fManifestarPedidosEnRutaConIntegrador.txtKmDeSalida.requestFocus();

                    /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
                    if (this.fManifestarPedidosEnRutaConIntegrador.manifiestoActual.getListaFacturasPorManifiesto() != null) {
                        fManifestarPedidosEnRutaConIntegrador.btnImprimir.setEnabled(true);
                    }
                    break;

                case 5: // MANIFIESTO ANULADO

                    this.fManifestarPedidosEnRutaConIntegrador.jTabbedPane1.setEnabledAt(this.fManifestarPedidosEnRutaConIntegrador.jTabbedPane1.indexOfComponent(this.fManifestarPedidosEnRutaConIntegrador.pnlAgregarLista), true);
                    // jTabbedPane1.setSelectedIndex(jTabbedPane1.indexOfComponent(pnlAgregarLista));
                    this.fManifestarPedidosEnRutaConIntegrador.crearNuevoManifiesto(this.fManifestarPedidosEnRutaConIntegrador.carro);
                    this.fManifestarPedidosEnRutaConIntegrador.txtKmDeSalida.setText("" + this.fManifestarPedidosEnRutaConIntegrador.carro.getKilometrajeActual());
                    this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso1.setVisible(false);
                    this.fManifestarPedidosEnRutaConIntegrador.txtKmDeSalida.requestFocus();
                    break;

            }

            this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso1.setVisible(false);
            this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso2.setVisible(false);
            this.fManifestarPedidosEnRutaConIntegrador.txtKmDeSalida.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(HiloConsultarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso1.setVisible(false);
            this.fManifestarPedidosEnRutaConIntegrador.lblCirculoDeProgreso2.setVisible(false);
        }
    }

}
