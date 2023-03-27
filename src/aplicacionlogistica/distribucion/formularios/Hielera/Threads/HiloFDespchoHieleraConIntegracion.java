/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloCrearManifiesto;
import aplicacionlogistica.distribucion.Threads.HiloGuardarFacturasPorManifiesto;
import aplicacionlogistica.distribucion.Threads.HiloGuardarFacturasPorManifiesto_2;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.Hielera.FDespachoHieleraConIntegracion;
import static aplicacionlogistica.distribucion.formularios.Hielera.FDespachoHieleraConIntegracion.valorDespBarraProgreso;
import aplicacionlogistica.distribucion.formularios.Hielera.FManifestarPedidosHielera_2;
import aplicacionlogistica.distribucion.imprimir.RepporteRuteroConductoresConPesosyDEscuentos;
import aplicacionlogistica.distribucion.integrador.HiloIntegradorTNSLaHielera;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.HeadlessException;
import java.io.File;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mtto.vehiculos.CCarros;
import mtto.vehiculos.CVehiculos;

/**
 *
 * @author Usuario
 */
public class HiloFDespchoHieleraConIntegracion implements Runnable {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    FDespachoHieleraConIntegracion fDespachoHieleraConIntegracion = null;
    String caso;
    List<String> listaDeSentenciasPorFactura = null;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFDespchoHieleraConIntegracion(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fDespachoHielera
     * @param comando
     */
    public HiloFDespchoHieleraConIntegracion(Inicio ini, FDespachoHieleraConIntegracion fDespachoHielera, String comando) {
        this.ini = ini;
        this.fDespachoHieleraConIntegracion = fDespachoHielera;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "cargarFormulario":
                        cargarFormulario();
                        break;
                    case "agregarPedido":
                        agregarPedido();
                        break;
                    case "agregarPedido2":
                        agregarPedido2();
                        break;
                    case "buscarManifiesto":

                        break;
                    case "salir":
                        // salir();
                        break;
                    case "nuevo":
                        nuevo();
                        break;
                    case "grabar":
                        grabar();
                        break;
                    case "cancelar":
                        //cancelar();
                        break;
                    case "agregarLafactura":
                        //consultarLafactura();
                        // descargarFactura();
                        break;
                    case "imprimir":
                        // imprimir();
                        break;
                    case "cargarFormulario2":
                        cargarFormulario2();
                        break;
                    case "crearManifiesto":
                        crearManifiesto();
                        break;
                    case "asignarDespachador":
                        asignarDespachador();
                        break;
                    case "borrarUnaFactura":
                        borrarUnaFactura();
                        break;
                    case "getionarSoporteConsignacion":
                        //getionarSoporteConsignacion();
                        break;

                    default:
                        JOptionPane.showInternalMessageDialog(fDespachoHieleraConIntegracion, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloFDespchoHieleraConIntegracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarFormulario() {

        cancelar();

        asignarFechaManifiesto();

        if (fDespachoHieleraConIntegracion.manifiestoActual == null) {
            try {
                fDespachoHieleraConIntegracion.manifiestoActual = new CManifiestosDeDistribucion(ini);
                fDespachoHieleraConIntegracion.manifiestoActual.setEstadoManifiesto(2);
            } catch (Exception ex) {
                Logger.getLogger(HiloFDespchoHieleraConIntegracion.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.removeAllItems();
        fDespachoHieleraConIntegracion.cbxCanales.removeAllItems();
        fDespachoHieleraConIntegracion.cbxPrefijos.removeAllItems();

        // SE LLENAN LAS LISTAS DESPLEGABLES
        for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
            if (obj.getActivoCanal() == 1) {
                fDespachoHieleraConIntegracion.cbxCanales.addItem(obj.getNombreCanalDeVenta());
            }

        }
        for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
            if (obj.getActivoRutasDeDistribucion() == 1) {
                fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.addItem(obj.getNombreRutasDeDistribucion());
            }
        }

        fDespachoHieleraConIntegracion.autoTxtVehiculos = new TextAutoCompleter(fDespachoHieleraConIntegracion.txtPlaca);
        if ((ini.getListaDeVehiculos() == null) || (ini.getListaDeVehiculos().size() == 0)) {
            ini.setListaDeVehiculos(1);
        }

        for (CVehiculos car : this.ini.getListaDeVehiculos()) {
            if (car.getActivoVehiculo() == 1) {
                fDespachoHieleraConIntegracion.autoTxtVehiculos.addItem(car.getPlaca() + " ");
            }
        }

        fDespachoHieleraConIntegracion.autoTxtConductores = new TextAutoCompleter(fDespachoHieleraConIntegracion.txtnombreDeConductor);

        for (CEmpleados conductor : this.ini.getListaDeEmpleados()) {
            if (conductor.getEmpleadoActivo() == 1) {
                fDespachoHieleraConIntegracion.autoTxtConductores.addItem(conductor.getNombres() + " " + conductor.getApellidos());
            }
        }

        fDespachoHieleraConIntegracion.autoTxtAuxiliar1 = new TextAutoCompleter(fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1);

        for (CEmpleados auxiliar1 : this.ini.getListaDeEmpleados()) {
            if (auxiliar1.getEmpleadoActivo() == 1) {
                fDespachoHieleraConIntegracion.autoTxtAuxiliar1.addItem(auxiliar1.getNombres() + " " + auxiliar1.getApellidos());
            }
        }

        fDespachoHieleraConIntegracion.autoTxtAuxiliar2 = new TextAutoCompleter(fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2);

        for (CEmpleados auxiliar2 : this.ini.getListaDeEmpleados()) {
            if (auxiliar2.getEmpleadoActivo() == 1) {
                fDespachoHieleraConIntegracion.autoTxtAuxiliar2.addItem(auxiliar2.getNombres() + " " + auxiliar2.getApellidos());
            }
        }

        fDespachoHieleraConIntegracion.autoTxtAuxiliar3 = new TextAutoCompleter(fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3);

        for (CEmpleados auxiliar3 : this.ini.getListaDeEmpleados()) {
            if (auxiliar3.getEmpleadoActivo() == 1) {
                fDespachoHieleraConIntegracion.autoTxtAuxiliar3.addItem(auxiliar3.getNombres() + " " + auxiliar3.getApellidos());
            }
        }

        fDespachoHieleraConIntegracion.autoTxtDespachador = new TextAutoCompleter(fDespachoHieleraConIntegracion.txtNombreDedespachador);

        for (CEmpleados despachador : this.ini.getListaDeEmpleados()) {
            if (despachador.getEmpleadoActivo() == 1) {
                fDespachoHieleraConIntegracion.autoTxtDespachador.addItem(despachador.getNombres() + " " + despachador.getApellidos());
            }
        }

        String strMain = this.ini.getPrefijos();
        String[] arrSplit = strMain.split(",");
        for (int i = 0; i < arrSplit.length; i++) {
            fDespachoHieleraConIntegracion.cbxPrefijos.addItem(arrSplit[i].replace("'", ""));
        }

        fDespachoHieleraConIntegracion.lblCirculoDeProgreso1.setVisible(false);
        fDespachoHieleraConIntegracion.lblCirculoDeProgreso2.setVisible(false);

        fDespachoHieleraConIntegracion.cargado = true;

    }

    public void cargarFormulario2() {

        try {

            int dia = LocalDate.now().getDayOfMonth();

            asignarFechaManifiesto();

            fDespachoHieleraConIntegracion.carro = new CCarros(ini, fDespachoHieleraConIntegracion.manifiestoActual.getVehiculo());
            fDespachoHieleraConIntegracion.conductor = new CEmpleados(ini);

            /*Atualiza el listado de vehiculos de la BBDD*/
            //new Thread(new HiloListadoDePlacas(ini)).start();
            // lblBarraDeProgreso.setVisible(false);
            fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.removeAllItems();
            fDespachoHieleraConIntegracion.cbxCanales.removeAllItems();
            fDespachoHieleraConIntegracion.cbxPrefijos.removeAllItems();
            // SE LLENAN LAS LISTAS DESPLEGABLES
            if (ini.getListaDeCanalesDeVenta() == null) {
                ini.setListaDeCanalesDeVenta();
            }

            for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
                if (obj.getActivoCanal() == 1) {
                    fDespachoHieleraConIntegracion.cbxCanales.addItem(obj.getNombreCanalDeVenta());
                }

            }

            if (ini.getListaDeRutasDeDistribucion() == null) {
                ini.setListaDeRutasDeDistribucion();
            }
            for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
                if (obj.getActivoRutasDeDistribucion() == 1) {
                    fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.addItem(obj.getNombreRutasDeDistribucion());
                }
            }

            fDespachoHieleraConIntegracion.autoTxtVehiculos = new TextAutoCompleter(fDespachoHieleraConIntegracion.txtPlaca);

            if (ini.getListaDeVehiculos() == null) {
                ini.setListaDeVehiculos(1);
            }
            for (CVehiculos car : this.ini.getListaDeVehiculos()) {
                if (car.getActivoVehiculo() == 1) {
                    fDespachoHieleraConIntegracion.autoTxtVehiculos.addItem(car.getPlaca() + " ");
                }
            }

            fDespachoHieleraConIntegracion.autoTxtConductores = new TextAutoCompleter(fDespachoHieleraConIntegracion.txtnombreDeConductor);

            if (ini.getListaDeEmpleados() == null) {
                ini.setListaDeEmpleados();
            }
            for (CEmpleados conductor : this.ini.getListaDeEmpleados()) {
                if (conductor.getEmpleadoActivo() == 1) {
                    fDespachoHieleraConIntegracion.autoTxtConductores.addItem(conductor.getNombres() + " " + conductor.getApellidos());
                }
            }

            fDespachoHieleraConIntegracion.autoTxtAuxiliar1 = new TextAutoCompleter(fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1);

            for (CEmpleados auxiliar1 : this.ini.getListaDeEmpleados()) {
                if (auxiliar1.getEmpleadoActivo() == 1) {
                    fDespachoHieleraConIntegracion.autoTxtAuxiliar1.addItem(auxiliar1.getNombres() + " " + auxiliar1.getApellidos());
                }
            }

            fDespachoHieleraConIntegracion.autoTxtAuxiliar2 = new TextAutoCompleter(fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2);

            for (CEmpleados auxiliar2 : this.ini.getListaDeEmpleados()) {
                if (auxiliar2.getEmpleadoActivo() == 1) {
                    fDespachoHieleraConIntegracion.autoTxtAuxiliar2.addItem(auxiliar2.getNombres() + " " + auxiliar2.getApellidos());
                }
            }

            fDespachoHieleraConIntegracion.autoTxtAuxiliar3 = new TextAutoCompleter(fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3);

            for (CEmpleados auxiliar3 : this.ini.getListaDeEmpleados()) {
                if (auxiliar3.getEmpleadoActivo() == 1) {
                    fDespachoHieleraConIntegracion.autoTxtAuxiliar3.addItem(auxiliar3.getNombres() + " " + auxiliar3.getApellidos());
                }
            }

            fDespachoHieleraConIntegracion.autoTxtDespachador = new TextAutoCompleter(fDespachoHieleraConIntegracion.txtNombreDedespachador);

            for (CEmpleados despachador : this.ini.getListaDeEmpleados()) {
                if (despachador.getEmpleadoActivo() == 1) {
                    fDespachoHieleraConIntegracion.autoTxtDespachador.addItem(despachador.getNombres() + " " + despachador.getApellidos());
                }
            }

            String strMain = this.ini.getPrefijos();
            String[] arrSplit = strMain.split(",");
            for (int i = 0; i < arrSplit.length; i++) {
                fDespachoHieleraConIntegracion.cbxPrefijos.addItem(arrSplit[i].replace("'", ""));
            }

            fDespachoHieleraConIntegracion.lblCirculoDeProgreso1.setVisible(false);
            fDespachoHieleraConIntegracion.lblCirculoDeProgreso2.setVisible(false);

            llenarDatosManifiesto();
        } catch (Exception ex) {
            Logger.getLogger(HiloFDespchoHieleraConIntegracion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void asignarFechaManifiesto() {
        if (LocalTime.now().isAfter(LocalTime.of(0, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(5, 0, 0))) {

            Date dt = new Date();
            fDespachoHieleraConIntegracion.dateManifiesto.setDate(new Date(dt.getTime() - (1000 * 60 * 60 * 24)));
        } else {
            fDespachoHieleraConIntegracion.dateManifiesto.setDate(new Date());

        }
    }

    public void cancelar() {
        limpiar();

        fDespachoHieleraConIntegracion.nuevo = false;
        fDespachoHieleraConIntegracion.dateManifiesto.setEnabled(false);

        fDespachoHieleraConIntegracion.txtnombreDeConductor.setEditable(false);

        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setEditable(false);
        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setEditable(false);
        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setEditable(false);
        fDespachoHieleraConIntegracion.txtNombreDedespachador.setEditable(false);

        fDespachoHieleraConIntegracion.txtKmDeSalida.setEditable(false);
        fDespachoHieleraConIntegracion.txtPlaca.setEditable(false);
        fDespachoHieleraConIntegracion.txtKmDeSalida.setEnabled(false);
        fDespachoHieleraConIntegracion.txtKmDeSalida.setEditable(false);
        fDespachoHieleraConIntegracion.cbxCanales.setEnabled(false);
        fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.setEnabled(false);
        fDespachoHieleraConIntegracion.dateManifiesto.setEnabled(false);

        fDespachoHieleraConIntegracion.btnCrearManifiesto.setEnabled(false);
        fDespachoHieleraConIntegracion.jTabbedPane1.setEnabled(false);

        fDespachoHieleraConIntegracion.btnNuevo.setEnabled(true);
        fDespachoHieleraConIntegracion.jBtnNuevo.setEnabled(true);

        fDespachoHieleraConIntegracion.btnImprimir.setEnabled(false);
        fDespachoHieleraConIntegracion.jBtnImprimir.setEnabled(false);

        fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
        fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
        fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(false);

        fDespachoHieleraConIntegracion.jBtnBorrarFIla.setEnabled(false);

        fDespachoHieleraConIntegracion.jBtnMinuta.setEnabled(false);

        fDespachoHieleraConIntegracion.grabar = false;
        //  lblNumeroManifiesto.setText("Total a recaudar en Manifiesto # ");

        if (fDespachoHieleraConIntegracion.manifiestoActual != null) {
            fDespachoHieleraConIntegracion.manifiestoActual.liberarManifiesto(true);
            fDespachoHieleraConIntegracion.liberado = true;

        }
        /* Vaciar las variables  */
        fDespachoHieleraConIntegracion.conductor = null;
        fDespachoHieleraConIntegracion.auxiliar1 = null;
        fDespachoHieleraConIntegracion.auxiliar2 = null;
        fDespachoHieleraConIntegracion.auxiliar3 = null;
        fDespachoHieleraConIntegracion.despachador = null;
        fDespachoHieleraConIntegracion.carro = null;
        fDespachoHieleraConIntegracion.facturaActual = null;
        fDespachoHieleraConIntegracion.band = false;
        fDespachoHieleraConIntegracion.valorDespBarraProgreso = 0;
        fDespachoHieleraConIntegracion.archivoConListaDeFacturas = null;;
        fDespachoHieleraConIntegracion.valorTotalManifiesto = 0.0;
        fDespachoHieleraConIntegracion.cargado = false;
        fDespachoHieleraConIntegracion.liberado = false;
        fDespachoHieleraConIntegracion.grabar = false;
        fDespachoHieleraConIntegracion.nuevo = false;
        fDespachoHieleraConIntegracion.kilometraje = 0;
        fDespachoHieleraConIntegracion.formaDePago = "";
        fDespachoHieleraConIntegracion.mensaje = null;
        fDespachoHieleraConIntegracion.contadorDeFacturas = 0;
        fDespachoHieleraConIntegracion.sumadorPesosFacturas = 0.0;
        fDespachoHieleraConIntegracion.listaDeFacturasEnElArchivo = null;;
        fDespachoHieleraConIntegracion.listaDeAuxiliares = null;
        fDespachoHieleraConIntegracion.manifiestoActual = null;
        fDespachoHieleraConIntegracion.estaOcupadoGrabando = false;
        fDespachoHieleraConIntegracion.cadenaDeFacturas = null;
        fDespachoHieleraConIntegracion.adherenciaGenral = 0;
        fDespachoHieleraConIntegracion.cantDeSalidas = 0;

        fDespachoHieleraConIntegracion.cbxPrefijos.setEnabled(false);
        fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(false);
        fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEditable(false);

        fDespachoHieleraConIntegracion.txtNombreDeCliente.setEditable(false);
        fDespachoHieleraConIntegracion.txtBarroCliente.setEditable(false);
        fDespachoHieleraConIntegracion.txtDireccionCliente.setEditable(false);
        fDespachoHieleraConIntegracion.txtTelefonoCliente.setEditable(false);
        fDespachoHieleraConIntegracion.txtNombreVendedor.setEditable(false);
        fDespachoHieleraConIntegracion.txtNumeroDeManifiesto.setEditable(false);

        fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();
        fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();

        fDespachoHieleraConIntegracion.manifiestoActual = null;
        fDespachoHieleraConIntegracion.conductor = null;
        fDespachoHieleraConIntegracion.despachador = null;
        fDespachoHieleraConIntegracion.listaDeAuxiliares = null;

        // listaFacturasPorManifiesto = null;
        fDespachoHieleraConIntegracion.valorTotalManifiesto = 0.0;
        fDespachoHieleraConIntegracion.formaDePago = "";
        fDespachoHieleraConIntegracion.lblCirculoDeProgreso1.setVisible(false);

        fDespachoHieleraConIntegracion.btnImprimir.setEnabled(false);
        fDespachoHieleraConIntegracion.jBtnImprimir.setEnabled(false);

        /*Componentes de JTabbePane */
        fDespachoHieleraConIntegracion.btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png")));
        fDespachoHieleraConIntegracion.jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png")));

        fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
        fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

        fDespachoHieleraConIntegracion.lblCirculoDeProgreso1.setVisible(false);
        fDespachoHieleraConIntegracion.lblCirculoDeProgreso2.setVisible(false);

    }

    private void limpiar() {

        try {

            fDespachoHieleraConIntegracion.jBtnDocumentos.setEnabled(false);
            fDespachoHieleraConIntegracion.lblCirculoDeProgreso1.setVisible(false);
            fDespachoHieleraConIntegracion.lblCirculoDeProgreso1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif")));

            fDespachoHieleraConIntegracion.txtPlaca.setText("");

            fDespachoHieleraConIntegracion.txtnombreDeConductor.setText("");

            fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setText("");
            fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setText("");
            fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setText("");
            fDespachoHieleraConIntegracion.txtNombreDedespachador.setText("");

            fDespachoHieleraConIntegracion.txtKmDeSalida.setText("");
            fDespachoHieleraConIntegracion.txtNumeroDeManifiesto.setText("");
            fDespachoHieleraConIntegracion.cbxCanales.setSelectedIndex(0);
            fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.setSelectedIndex(0);

            DefaultTableModel model;
            model = (DefaultTableModel) fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getModel();
            if (model.getRowCount() > 0) {
                int a = model.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    model.removeRow(i);
                }
            }
            model = (DefaultTableModel) fDespachoHieleraConIntegracion.jTableProductosPorFactura.getModel();
            if (model.getRowCount() > 0) {
                int a = model.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    model.removeRow(i);
                }
            }

            //listaFacturasPorManifiesto = null;
            fDespachoHieleraConIntegracion.lblPesoManifiesto.setText("0.0 Kg");
            fDespachoHieleraConIntegracion.lblValorManifiesto.setText("$0.0");
            fDespachoHieleraConIntegracion.lblValorFactura.setText("$ 0.0");

            fDespachoHieleraConIntegracion.setTitle("");
            fDespachoHieleraConIntegracion.txtNumeroDeFactura.setText("");
            fDespachoHieleraConIntegracion.txtNumeroDeFactura.setText("");
            fDespachoHieleraConIntegracion.txtNombreDeCliente.setText("");
            fDespachoHieleraConIntegracion.txtBarroCliente.setText("");
            fDespachoHieleraConIntegracion.txtDireccionCliente.setText("");
            fDespachoHieleraConIntegracion.txtTelefonoCliente.setText("");
            fDespachoHieleraConIntegracion.txtNombreVendedor.setText("");
            fDespachoHieleraConIntegracion.lblCantidadFacturas.setText("0");

            fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
            fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));
            fDespachoHieleraConIntegracion.lblCirculoDeProgreso1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif")));

            fDespachoHieleraConIntegracion.lblCirculoDeProgreso1.setVisible(false);
            fDespachoHieleraConIntegracion.lblCirculoDeProgreso2.setVisible(false);

            fDespachoHieleraConIntegracion.conductor = null;
            fDespachoHieleraConIntegracion.auxiliar1 = null;
            fDespachoHieleraConIntegracion.auxiliar2 = null;
            fDespachoHieleraConIntegracion.auxiliar3 = null;
            fDespachoHieleraConIntegracion.despachador = null;
            fDespachoHieleraConIntegracion.carro = null;
            fDespachoHieleraConIntegracion.facturaActual = null;
            valorDespBarraProgreso = 0;
            fDespachoHieleraConIntegracion.valorTotalManifiesto = 0.0;
            fDespachoHieleraConIntegracion.cargado = false;
            fDespachoHieleraConIntegracion.liberado = false;
            fDespachoHieleraConIntegracion.grabar = false;
            fDespachoHieleraConIntegracion.nuevo = false;
            fDespachoHieleraConIntegracion.kilometraje = 0;
            fDespachoHieleraConIntegracion.mensaje = null;
            fDespachoHieleraConIntegracion.contadorDeFacturas = 0;
            fDespachoHieleraConIntegracion.sumadorPesosFacturas = 0.0;
            fDespachoHieleraConIntegracion.listaDeFacturasEnElArchivo = null;
            fDespachoHieleraConIntegracion.listaDeAuxiliares = null;
            fDespachoHieleraConIntegracion.manifiestoActual = null;
            fDespachoHieleraConIntegracion.estaOcupadoGrabando = false;
            fDespachoHieleraConIntegracion.cadenaDeFacturas = null;
            fDespachoHieleraConIntegracion.cantDeSalidas = 1;
            fDespachoHieleraConIntegracion.adherenciaGenral = 1;

        } catch (Exception ex) {

        }

    }

    private void agregarPedido() {
        fDespachoHieleraConIntegracion.jBtnBorrarFIla.setEnabled(false);

        try {
            String numeroFactura = fDespachoHieleraConIntegracion.txtNumeroDeFactura.getText().trim().replace("POSC", "PO");

            //objeto factura en la BBDD Local
            fDespachoHieleraConIntegracion.facturaActual = new CFacturas(ini, numeroFactura);

            // Si no e encuentra la factura, entonces la integra a LIS
            if (fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura() == null) {
                String prefijo = numeroFactura.substring(0, 2); //fDespachoHieleraConIntegracion.cbxPrefijos.getSelectedItem().toString();
                String numero = numeroFactura.substring(2, numeroFactura.length());


                /*
                char[] ch = numeroFactura.toCharArray();
               
                StringBuilder prefijo = new StringBuilder();
                StringBuilder numero = new StringBuilder();
                
                for (char c : ch) {
                    if (Character.isDigit(c)) {
                        numero.append(c);
                    } else {
                        prefijo.append(c);
                    }
                
                }
                **/
                //String cadena1=prefijo.toString();
                // String caadenas2=numero.toString();
                fDespachoHieleraConIntegracion.facturaActual = new CFacturas(ini, prefijo, numero);

                if (fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura() == null) {

                    fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaNoExisteTNS(prefijo.toString() + numero.toString());

                    return;

                }

                /*lanza un hilo para integrar la factura al sistema */
                new Thread(new HiloIntegradorTNSLaHielera(ini, prefijo.toString(), numero.toString())).start();

                fDespachoHieleraConIntegracion.facturaActual.setListaDetalleFactura(prefijo.toString(), numero.toString());

                if (!estaLaFacturaEnElManifiesto()) {
                    CFacturasPorManifiesto facXMfto = new CFacturasPorManifiesto(ini);

                    /* Se agregan las propiedades a l objeto vista facturas por manifiesto */
                    facXMfto.setAdherencia(fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size() + 1);
                    facXMfto.setNumeroManifiesto(fDespachoHieleraConIntegracion.manifiestoActual.getNumeroManifiesto());
                    facXMfto.setNumeroFactura(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                    facXMfto.setNombreDeCliente(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                    facXMfto.setDireccionDeCliente(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                    facXMfto.setValorTotalFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setPesoFactura(fDespachoHieleraConIntegracion.facturaActual.getPesofactura());
                    facXMfto.setConsecutivo(0);
                    facXMfto.setIdCanal(fDespachoHieleraConIntegracion.manifiestoActual.getIdCanal());
                    facXMfto.setNombreCanal(fDespachoHieleraConIntegracion.manifiestoActual.getNombreCanal());
                    facXMfto.setValorARecaudarFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setFechaDistribucion(fDespachoHieleraConIntegracion.manifiestoActual.getFechaDistribucion());
                    facXMfto.setVehiculo(fDespachoHieleraConIntegracion.manifiestoActual.getVehiculo());
                    facXMfto.setConductor(fDespachoHieleraConIntegracion.manifiestoActual.getConductor());
                    facXMfto.setNombreConductor(fDespachoHieleraConIntegracion.manifiestoActual.getNombreConductor());
                    facXMfto.setNombreDeCliente(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                    facXMfto.setDireccionDeCliente(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                    facXMfto.setValorFacturaSinIva(fDespachoHieleraConIntegracion.facturaActual.getValorFacturaSinIva());
                    facXMfto.setValorTotalFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setValorRechazo(0.0);
                    facXMfto.setValorDescuento(0.0);
                    facXMfto.setValorRecaudado(0.0);
                    facXMfto.setVendedor(fDespachoHieleraConIntegracion.facturaActual.getVendedor());
                    facXMfto.setVehiculoAsignado(fDespachoHieleraConIntegracion.txtPlaca.getText().trim());
                    facXMfto.setFpContado(fDespachoHieleraConIntegracion.facturaActual.getFpContado());

                    if (fDespachoHieleraConIntegracion.conductor.getListaDeFacturasPorConductorSinDespachar() == null) {
                        List<CFacturasPorManifiesto> lista = new ArrayList();
                        lista.add(facXMfto);
                        fDespachoHieleraConIntegracion.conductor.setListaDeFacturasPorConductorSinDespachar(lista);

                    } else {
                        fDespachoHieleraConIntegracion.conductor.getListaDeFacturasPorConductorSinDespachar().add(facXMfto);
                    }

                    fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().add(facXMfto);

                    limpiarDatodsDePanelProductosPorFactura();
                    fDespachoHieleraConIntegracion.txtNombreDeCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                    fDespachoHieleraConIntegracion.txtDireccionCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                    fDespachoHieleraConIntegracion.txtTelefonoCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getTelefonoCliente());
                    fDespachoHieleraConIntegracion.txtBarroCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getBarrio());
                    fDespachoHieleraConIntegracion.txtNombreVendedor.setText(fDespachoHieleraConIntegracion.facturaActual.getVendedor());
                    fDespachoHieleraConIntegracion.lblValorFactura.setText(nf.format(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura()));

                    DecimalFormat df = new DecimalFormat("#,###.0");
                    fDespachoHieleraConIntegracion.lblPesoDeLaFactura.setText(df.format((fDespachoHieleraConIntegracion.facturaActual.getPesofactura()) / 1000) + " Kg");

                    /* se llena la tabla de productos por Factura*/
                    llenarJtableProductosPorFactura();

                    /*Se coloca el registro en la tabla de facturas*/
                    colocarRegistroEnLaTabla();

                }

                fDespachoHieleraConIntegracion.valorTotalManifiesto = 0.0;
                fDespachoHieleraConIntegracion.sumadorPesosFacturas = 0.0;
                /* Calcula el valor del manifiesto y el peso de los productos */
                for (CFacturasPorManifiesto fac : fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto()) {

                    fDespachoHieleraConIntegracion.valorTotalManifiesto += fac.getValorTotalFactura();
                    fDespachoHieleraConIntegracion.sumadorPesosFacturas += fac.getPesoFactura();

                }
                DecimalFormat df = new DecimalFormat("#,###.0");
                fDespachoHieleraConIntegracion.lblPesoManifiesto.setText(df.format(fDespachoHieleraConIntegracion.sumadorPesosFacturas / 1000) + " Kg");
                fDespachoHieleraConIntegracion.lblValorManifiesto.setText(nf.format(fDespachoHieleraConIntegracion.valorTotalManifiesto));
                fDespachoHieleraConIntegracion.manifiestoActual.setValorTotalManifiesto(fDespachoHieleraConIntegracion.valorTotalManifiesto);
                fDespachoHieleraConIntegracion.lblCantidadFacturas.setText("" + fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size());

                // new Thread(new HiloIntegradorTNS(ini, prefijo, numeroFactura)).start();
            } else {
                agregarFactura();

            }

            if (fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto() != null) {
                fDespachoHieleraConIntegracion.lblCantidadFacturas.setText("" + fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size());
            }

            /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
            if (fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size() > 0) {
                fDespachoHieleraConIntegracion.btnImprimir.setEnabled(true);
                fDespachoHieleraConIntegracion.jBtnImprimir.setEnabled(true);

                fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
                fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);

            }
        } catch (Exception ex) {
            Logger.getLogger(FDespachoHieleraConIntegracion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Metodo para grabafr una factura al manifiesto
     *
     */
    private void agregarPedido2() {
        fDespachoHieleraConIntegracion.jBtnBorrarFIla.setEnabled(false);

        try {
            String numeroFactura = fDespachoHieleraConIntegracion.txtNumeroDeFactura.getText().trim().replace("POSC", "PO");

            //objeto factura en la BBDD Local
            fDespachoHieleraConIntegracion.facturaActual = new CFacturas(ini, numeroFactura);

            // Si no e encuentra la factura, entonces la integra a LIS
            if (fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura() == null) {
                String prefijo = numeroFactura.substring(0, 2); //fDespachoHieleraConIntegracion.cbxPrefijos.getSelectedItem().toString();
                String numero = numeroFactura.substring(2, numeroFactura.length());

                /*se valida la existencia de la factura en TNS */
                fDespachoHieleraConIntegracion.facturaActual = new CFacturas(ini, prefijo, numero);

                if (fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura() == null) {

                    fDespachoHieleraConIntegracion.facturaActual = null;
                    fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaNoExisteTNS(prefijo.toString() + numero.toString());

                    return;

                }

                /*lanza un hilo para integrar la factura al sistema */
                new Thread(new HiloIntegradorTNSLaHielera(ini, prefijo.toString(), numero.toString())).start();

                if (!estaLaFacturaEnElManifiesto()) {
                    CFacturasPorManifiesto facXMfto = new CFacturasPorManifiesto(ini);

                    /* Se agregan las propiedades a l objeto vista facturas por manifiesto */
                    facXMfto.setAdherencia(fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size() + 1);
                    facXMfto.setNumeroManifiesto(fDespachoHieleraConIntegracion.manifiestoActual.getNumeroManifiesto());
                    facXMfto.setNumeroFactura(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                    facXMfto.setNombreDeCliente(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                    facXMfto.setDireccionDeCliente(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                    facXMfto.setValorTotalFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setPesoFactura(fDespachoHieleraConIntegracion.facturaActual.getPesofactura());
                    facXMfto.setConsecutivo(0);
                    facXMfto.setIdCanal(fDespachoHieleraConIntegracion.manifiestoActual.getIdCanal());
                    facXMfto.setNombreCanal(fDespachoHieleraConIntegracion.manifiestoActual.getNombreCanal());
                    facXMfto.setValorARecaudarFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setFechaDistribucion(fDespachoHieleraConIntegracion.manifiestoActual.getFechaDistribucion());
                    facXMfto.setVehiculo(fDespachoHieleraConIntegracion.manifiestoActual.getVehiculo());
                    facXMfto.setConductor(fDespachoHieleraConIntegracion.manifiestoActual.getConductor());
                    facXMfto.setNombreConductor(fDespachoHieleraConIntegracion.manifiestoActual.getNombreConductor());
                    facXMfto.setNombreDeCliente(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                    facXMfto.setDireccionDeCliente(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                    facXMfto.setValorFacturaSinIva(fDespachoHieleraConIntegracion.facturaActual.getValorFacturaSinIva());
                    facXMfto.setValorTotalFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setValorRechazo(0.0);
                    facXMfto.setValorDescuento(0.0);
                    facXMfto.setValorRecaudado(0.0);
                    facXMfto.setDespachado(0);
                    facXMfto.setVendedor(fDespachoHieleraConIntegracion.facturaActual.getVendedor());
                    facXMfto.setVehiculoAsignado(fDespachoHieleraConIntegracion.txtPlaca.getText().trim());
                    facXMfto.setFpContado(fDespachoHieleraConIntegracion.facturaActual.getFpContado());

                    /*se valida que la factura esta grabada */
                   // Connection con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
                    Connection con = ini.getConnRemota();
                
                    /*Se deshabilita elcampo de texto de la Factura para que no 
                    ingrese una nueva factura */
                    fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(false);
                    int i = 1;

                    if (con != null) {
                        while (!CFacturas.existeFactura(con, facXMfto.getNumeroFactura())) {
                            System.out.print(i++ + " aca es \n");
                            Thread.sleep(500);

                        }
                    } else {
                        fDespachoHieleraConIntegracion.facturaActual = null;
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(true);
                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgSinConexionBBDD(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());

                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();
                        return;
                    }

                    // if (CFacturas.existeFactura(con, facXMfto.getNumeroFactura())) {
                    if (true) {
                       // con.close();
                        fDespachoHieleraConIntegracion.facturaActual.setListaDetalleFactura(prefijo.toString(), numero.toString());

                        /**
                         * acaa colocar codigo para validar si la factura tiene
                         * una novedad y si se acepta continuar de lo contrario
                         * salir(return)
                         */
                        /*Graba la fctura al manifiesto */
                        if (facXMfto.grabarFacturasPoManifiesto()) {

                            if (fDespachoHieleraConIntegracion.conductor.getListaDeFacturasPorConductorSinDespachar() == null) {
                                List<CFacturasPorManifiesto> lista = new ArrayList();
                                lista.add(facXMfto);
                                fDespachoHieleraConIntegracion.conductor.setListaDeFacturasPorConductorSinDespachar(lista);

                            } else {
                                fDespachoHieleraConIntegracion.conductor.getListaDeFacturasPorConductorSinDespachar().add(facXMfto);
                            }

                            fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().add(facXMfto);

                            limpiarDatodsDePanelProductosPorFactura();
                            fDespachoHieleraConIntegracion.txtNombreDeCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                            fDespachoHieleraConIntegracion.txtDireccionCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                            fDespachoHieleraConIntegracion.txtTelefonoCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getTelefonoCliente());
                            fDespachoHieleraConIntegracion.txtBarroCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getBarrio());
                            fDespachoHieleraConIntegracion.txtNombreVendedor.setText(fDespachoHieleraConIntegracion.facturaActual.getVendedor());
                            fDespachoHieleraConIntegracion.lblValorFactura.setText(nf.format(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura()));

                            DecimalFormat df = new DecimalFormat("#,###.0");
                            fDespachoHieleraConIntegracion.lblPesoDeLaFactura.setText(df.format((fDespachoHieleraConIntegracion.facturaActual.getPesofactura()) / 1000) + " Kg");

                            if (fDespachoHieleraConIntegracion.facturaActual.getListaDetalleFactura() != null) {
                                /* se llena la tabla de productos por Factura*/
                                llenarJtableProductosPorFactura();
                            }

                            /*Se coloca el registro en la tabla de facturas*/
                            colocarRegistroEnLaTabla();
                            fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(true);
                            fDespachoHieleraConIntegracion.txtDireccionCliente.requestFocus();
                            fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();

                        }

                    } else {
                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaNoExiste(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                        //con.close();
                        return;
                    }
                }

                fDespachoHieleraConIntegracion.valorTotalManifiesto = 0.0;
                fDespachoHieleraConIntegracion.sumadorPesosFacturas = 0.0;
                /* Calcula el valor del manifiesto y el peso de los productos */
                for (CFacturasPorManifiesto fac : fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto()) {

                    fDespachoHieleraConIntegracion.valorTotalManifiesto += fac.getValorTotalFactura();
                    fDespachoHieleraConIntegracion.sumadorPesosFacturas += fac.getPesoFactura();

                }
                DecimalFormat df = new DecimalFormat("#,###.0");
                fDespachoHieleraConIntegracion.lblPesoManifiesto.setText(df.format(fDespachoHieleraConIntegracion.sumadorPesosFacturas / 1000) + " Kg");
                fDespachoHieleraConIntegracion.lblValorManifiesto.setText(nf.format(fDespachoHieleraConIntegracion.valorTotalManifiesto));
                fDespachoHieleraConIntegracion.manifiestoActual.setValorTotalManifiesto(fDespachoHieleraConIntegracion.valorTotalManifiesto);
                fDespachoHieleraConIntegracion.lblCantidadFacturas.setText("" + fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size());

                fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(true);
                fDespachoHieleraConIntegracion.txtDireccionCliente.requestFocus();
                fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();

                // new Thread(new HiloIntegradorTNS(ini, prefijo, numeroFactura)).start();
            } else {
                agregarFactura2();

            }

            if (fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto() != null) {
                fDespachoHieleraConIntegracion.lblCantidadFacturas.setText("" + fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size());

                fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(true);
                fDespachoHieleraConIntegracion.txtDireccionCliente.requestFocus();
                fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();
            }

            /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
//            if (fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size() > 0) {
//                fDespachoHieleraConIntegracion.btnImprimir.setEnabled(true);
//                fDespachoHieleraConIntegracion.jBtnImprimir.setEnabled(true);
//
//                fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
//                fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
//
//            }
        } catch (Exception ex) {
            Logger.getLogger(FDespachoHieleraConIntegracion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método que asigna las factura leida por teclado ó por código de barras al
     * manifiesto actual y fue trasnmitida directamente desde la BBDD firebird
     *
     */
    public void agregarFactura() throws Exception {

        try {

            if (fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente().contains("A N U L A D O")) {
                fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaAnulada(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
//JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + "s e encuentra anulada", "Error, factura Anulada", JOptionPane.WARNING_MESSAGE);
                fDespachoHieleraConIntegracion.facturaActual = null;
                return;
            }

            /* SE VALIDA QUE LA FACTURA EXISTA  */
            if (fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura() != null) {

                /*Se valida el tipo de movimiento de la factura*/
                switch (fDespachoHieleraConIntegracion.facturaActual.getEstadoFactura()) {

                    case 2:
                        /*Entrega total*/
                        //JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);
                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgEntregaTotal(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                        return;

                    case 3:
                        /*Devolucion total*/
                        //JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " fue devuelta de ruta", "Error, factura Devuelta", JOptionPane.WARNING_MESSAGE);

                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgDevolucionTotal(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                        return;
                    case 4:/*Entrega con novedad*/
                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgEntregaTotal(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                        //JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

                        return;
                    case 5:/*Entrega total con recogida*/
                        // JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgEntregaTotal(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                        return;
                    case 6:
                        /*Re Envio*/
                        break;

                    case 8:
                        /*ANULADA*/
                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaAnulada(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                        //JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + "  fue ANULADA", "Error, factura ANULADA", JOptionPane.ERROR_MESSAGE);
                        return;

                }

                CFacturasPorManifiesto facXMfto = new CFacturasPorManifiesto(ini);

                if (fDespachoHieleraConIntegracion.facturaActual.getIsFree() == 0) {
//                    JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura
//                            + " ya se encuentra en Distribución "
//                            + "", "Error", JOptionPane.WARNING_MESSAGE);
                    fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaEnDistribucion(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());

                    /* =1  la factura está disponible para agregarla al manifiesto 
                           SE registran las facturas que presentan inconvenientes en el Jlist*/
                    //modelo3.addElement(numeroDeFactura);
                    //jListListaDeFacturasErradas.setModel(modelo3);
                    // return;
                }


                /* SE valida que la factura no esté en el manifiesto */
                if (!estaLaFacturaEnElManifiesto()) {
                    fDespachoHieleraConIntegracion.jBtnMinuta.setEnabled(false);

                    /* Se agregan las propiedades a l objeto vista facturas por manifiesto */
                    facXMfto.setAdherencia(fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size() + 1);
                    facXMfto.setNumeroManifiesto(fDespachoHieleraConIntegracion.manifiestoActual.getNumeroManifiesto());
                    facXMfto.setNumeroFactura(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                    facXMfto.setNombreDeCliente(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                    facXMfto.setDireccionDeCliente(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                    facXMfto.setValorTotalFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setPesoFactura(fDespachoHieleraConIntegracion.facturaActual.getPesofactura());
                    facXMfto.setConsecutivo(0);
                    facXMfto.setIdCanal(fDespachoHieleraConIntegracion.manifiestoActual.getIdCanal());
                    facXMfto.setNombreCanal(fDespachoHieleraConIntegracion.manifiestoActual.getNombreCanal());
                    facXMfto.setValorARecaudarFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setFechaDistribucion(fDespachoHieleraConIntegracion.manifiestoActual.getFechaDistribucion());
                    facXMfto.setVehiculo(fDespachoHieleraConIntegracion.manifiestoActual.getVehiculo());
                    facXMfto.setConductor(fDespachoHieleraConIntegracion.manifiestoActual.getConductor());
                    facXMfto.setNombreConductor(fDespachoHieleraConIntegracion.manifiestoActual.getNombreConductor());
                    facXMfto.setNombreDeCliente(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                    facXMfto.setDireccionDeCliente(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                    facXMfto.setValorFacturaSinIva(fDespachoHieleraConIntegracion.facturaActual.getValorFacturaSinIva());
                    facXMfto.setValorTotalFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setValorRechazo(0.0);
                    facXMfto.setValorDescuento(0.0);
                    facXMfto.setValorRecaudado(0.0);
                    facXMfto.setVendedor(fDespachoHieleraConIntegracion.facturaActual.getVendedor());
                    facXMfto.setVehiculoAsignado(fDespachoHieleraConIntegracion.txtPlaca.getText().trim());
                    facXMfto.setFpContado(fDespachoHieleraConIntegracion.facturaActual.getFpContado());

                    if (fDespachoHieleraConIntegracion.conductor.getListaDeFacturasPorConductorSinDespachar() == null) {
                        List<CFacturasPorManifiesto> lista = new ArrayList();
                        lista.add(facXMfto);
                        fDespachoHieleraConIntegracion.conductor.setListaDeFacturasPorConductorSinDespachar(lista);

                    } else {
                        fDespachoHieleraConIntegracion.conductor.getListaDeFacturasPorConductorSinDespachar().add(facXMfto);
                    }

                    fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().add(facXMfto);

                    /* sí hay datos en la tabla de los productos  de la factura se limpia la tabla */
                    limpiarDatodsDePanelProductosPorFactura();

                    fDespachoHieleraConIntegracion.txtNombreDeCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                    fDespachoHieleraConIntegracion.txtDireccionCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                    fDespachoHieleraConIntegracion.txtTelefonoCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getTelefonoCliente());
                    fDespachoHieleraConIntegracion.txtBarroCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getBarrio());
                    fDespachoHieleraConIntegracion.txtNombreVendedor.setText(fDespachoHieleraConIntegracion.facturaActual.getVendedor());
                    fDespachoHieleraConIntegracion.lblValorFactura.setText(nf.format(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura()));

                    DecimalFormat df = new DecimalFormat("#,###.0");
                    fDespachoHieleraConIntegracion.lblPesoDeLaFactura.setText(df.format((fDespachoHieleraConIntegracion.facturaActual.getPesofactura()) / 1000) + " Kg");

                    fDespachoHieleraConIntegracion.facturaActual.setListaDetalleFactura(false); // Vst_ProductosPorFactura

                    /* se llena la tabla de productos por Factura*/
                    llenarJtableProductosPorFactura();

                    fDespachoHieleraConIntegracion.sumadorPesosFacturas += fDespachoHieleraConIntegracion.facturaActual.getPesofactura();

                    /*Se coloca el registro en la tabla de facturas*/
                    colocarRegistroEnLaTabla();

                    /* se ubica el cursor en la fila insertada */
                    fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.changeSelection(fDespachoHieleraConIntegracion.filaTabla2, 0, false, false);

                    /* se imprime el dato en la respectiva etiqueta */
                    fDespachoHieleraConIntegracion.lblPesoManifiesto.setText(df.format(fDespachoHieleraConIntegracion.sumadorPesosFacturas / 1000) + " Kg");
                    fDespachoHieleraConIntegracion.lblValorManifiesto.setText(nf.format(fDespachoHieleraConIntegracion.manifiestoActual.getValorTotalManifiesto()));
                    fDespachoHieleraConIntegracion.btnGrabar.setEnabled(!fDespachoHieleraConIntegracion.grabar);
                    fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(!fDespachoHieleraConIntegracion.grabar);
                    fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(!fDespachoHieleraConIntegracion.grabar);

                    /* si el registro existe en la tabla TMPfacturasPorManifiesto, hace nada */
                    fDespachoHieleraConIntegracion.txtNombreDeCliente.requestFocus();

                }

                /* si no   existe la factura, arroja un mensaje de error. */
            } else {

//                JOptionPane.showInternalMessageDialog(this, "La factura # " + numeroDeFactura
//                        + "  no existe en el sistema "
//                        + "", "Error", JOptionPane.WARNING_MESSAGE);
                fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaNoExiste(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
            }

            fDespachoHieleraConIntegracion.txtNombreDeCliente.requestFocus();
        } catch (Exception ex) {
            Logger.getLogger(FDespachoHieleraConIntegracion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método que asigna las factura leida por teclado ó por código de barras al
     * manifiesto actual y fue trasnmitida directamente desde la BBDD firebird
     *
     */
    public void agregarFactura2() throws Exception {

        try {

            if (fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente().contains("A N U L A D O")) {
                fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaAnulada(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
//JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + "s e encuentra anulada", "Error, factura Anulada", JOptionPane.WARNING_MESSAGE);
                fDespachoHieleraConIntegracion.facturaActual = null;
                return;
            }

            /* SE VALIDA QUE LA FACTURA EXISTA  */
            if (fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura() != null) {

                /*Se valida el tipo de movimiento de la factura*/
                switch (fDespachoHieleraConIntegracion.facturaActual.getEstadoFactura()) {

                    case 2:
                        /*Entrega total*/
                        //JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);
                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgEntregaTotal(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                        return;

                    case 3:
                        /*Devolucion total*/
                        //JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " fue devuelta de ruta", "Error, factura Devuelta", JOptionPane.WARNING_MESSAGE);

                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgDevolucionTotal(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                        return;
                    case 4:/*Entrega con novedad*/
                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgEntregaTotal(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                        //JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

                        return;
                    case 5:/*Entrega total con recogida*/
                        // JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgEntregaTotal(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                        return;
                    case 6:
                        /*Re Envio*/
                        break;

                    case 8:
                        /*ANULADA*/
                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaAnulada(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                        //JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + "  fue ANULADA", "Error, factura ANULADA", JOptionPane.ERROR_MESSAGE);
                        return;

                }

                CFacturasPorManifiesto facXMfto = new CFacturasPorManifiesto(ini);

                if (fDespachoHieleraConIntegracion.facturaActual.getIsFree() == 0) {
//                    JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura
//                            + " ya se encuentra en Distribución "
//                            + "", "Error", JOptionPane.WARNING_MESSAGE);
                    fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaEnDistribucion(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());

                    /* =1  la factura está disponible para agregarla al manifiesto 
                           SE registran las facturas que presentan inconvenientes en el Jlist*/
                    //modelo3.addElement(numeroDeFactura);
                    //jListListaDeFacturasErradas.setModel(modelo3);
                    return;
                }


                /* SE valida que la factura no esté en el manifiesto */
                if (!estaLaFacturaEnElManifiesto()) {
                    fDespachoHieleraConIntegracion.jBtnMinuta.setEnabled(false);

                    /* Se agregan las propiedades a l objeto vista facturas por manifiesto */
                    facXMfto.setAdherencia(fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size() + 1);
                    facXMfto.setNumeroManifiesto(fDespachoHieleraConIntegracion.manifiestoActual.getNumeroManifiesto());
                    facXMfto.setNumeroFactura(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                    facXMfto.setNombreDeCliente(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                    facXMfto.setDireccionDeCliente(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                    facXMfto.setValorTotalFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setPesoFactura(fDespachoHieleraConIntegracion.facturaActual.getPesofactura());
                    facXMfto.setConsecutivo(0);
                    facXMfto.setIdCanal(fDespachoHieleraConIntegracion.manifiestoActual.getIdCanal());
                    facXMfto.setNombreCanal(fDespachoHieleraConIntegracion.manifiestoActual.getNombreCanal());
                    facXMfto.setValorARecaudarFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setFechaDistribucion(fDespachoHieleraConIntegracion.manifiestoActual.getFechaDistribucion());
                    facXMfto.setVehiculo(fDespachoHieleraConIntegracion.manifiestoActual.getVehiculo());
                    facXMfto.setConductor(fDespachoHieleraConIntegracion.manifiestoActual.getConductor());
                    facXMfto.setNombreConductor(fDespachoHieleraConIntegracion.manifiestoActual.getNombreConductor());
                    facXMfto.setNombreDeCliente(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                    facXMfto.setDireccionDeCliente(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                    facXMfto.setValorFacturaSinIva(fDespachoHieleraConIntegracion.facturaActual.getValorFacturaSinIva());
                    facXMfto.setValorTotalFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setValorRechazo(0.0);
                    facXMfto.setValorDescuento(0.0);
                    facXMfto.setValorRecaudado(0.0);
                    facXMfto.setDespachado(0);
                    facXMfto.setVendedor(fDespachoHieleraConIntegracion.facturaActual.getVendedor());
                    facXMfto.setVehiculoAsignado(fDespachoHieleraConIntegracion.txtPlaca.getText().trim());
                    facXMfto.setFpContado(fDespachoHieleraConIntegracion.facturaActual.getFpContado());

                    if (facXMfto.grabarFacturasPoManifiesto()) {

                        if (fDespachoHieleraConIntegracion.conductor.getListaDeFacturasPorConductorSinDespachar() == null) {
                            List<CFacturasPorManifiesto> lista = new ArrayList();
                            lista.add(facXMfto);
                            fDespachoHieleraConIntegracion.conductor.setListaDeFacturasPorConductorSinDespachar(lista);

                        } else {
                            fDespachoHieleraConIntegracion.conductor.getListaDeFacturasPorConductorSinDespachar().add(facXMfto);
                        }

                        fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().add(facXMfto);

                        /* sí hay datos en la tabla de los productos  de la factura se limpia la tabla */
                        limpiarDatodsDePanelProductosPorFactura();

                        fDespachoHieleraConIntegracion.txtNombreDeCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                        fDespachoHieleraConIntegracion.txtDireccionCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                        fDespachoHieleraConIntegracion.txtTelefonoCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getTelefonoCliente());
                        fDespachoHieleraConIntegracion.txtBarroCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getBarrio());
                        fDespachoHieleraConIntegracion.txtNombreVendedor.setText(fDespachoHieleraConIntegracion.facturaActual.getVendedor());
                        fDespachoHieleraConIntegracion.lblValorFactura.setText(nf.format(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura()));

                        DecimalFormat df = new DecimalFormat("#,###.0");
                        fDespachoHieleraConIntegracion.lblPesoDeLaFactura.setText(df.format((fDespachoHieleraConIntegracion.facturaActual.getPesofactura()) / 1000) + " Kg");

                        //   fDespachoHieleraConIntegracion.facturaActual.setListaDetalleFactura(false); // Vst_ProductosPorFactura
                        if (fDespachoHieleraConIntegracion.facturaActual.getListaDetalleFactura() != null) {
                            /* se llena la tabla de productos por Factura*/
                            llenarJtableProductosPorFactura();

                        }
                        fDespachoHieleraConIntegracion.sumadorPesosFacturas += fDespachoHieleraConIntegracion.facturaActual.getPesofactura();

                        /*Se coloca el registro en la tabla de facturas*/
                        colocarRegistroEnLaTabla();


                        /* se ubica el cursor en la fila insertada */
                        fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.changeSelection(fDespachoHieleraConIntegracion.filaTabla2, 0, false, false);

                        /* se imprime el dato en la respectiva etiqueta */
                        fDespachoHieleraConIntegracion.lblPesoManifiesto.setText(df.format(fDespachoHieleraConIntegracion.sumadorPesosFacturas / 1000) + " Kg");
                        fDespachoHieleraConIntegracion.lblValorManifiesto.setText(nf.format(fDespachoHieleraConIntegracion.manifiestoActual.getValorTotalManifiesto()));
                        fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
                        fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
                        fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(!fDespachoHieleraConIntegracion.grabar);

                        /* si el registro existe en la tabla TMPfacturasPorManifiesto, hace nada */
                        fDespachoHieleraConIntegracion.txtNombreDeCliente.requestFocus();

                    }
                }

                /* si no   existe la factura, arroja un mensaje de error. */
            } else {

//                JOptionPane.showInternalMessageDialog(this, "La factura # " + numeroDeFactura
//                        + "  no existe en el sistema "
//                        + "", "Error", JOptionPane.WARNING_MESSAGE);
                fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaNoExiste(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
            }

            fDespachoHieleraConIntegracion.txtNombreDeCliente.requestFocus();
        } catch (Exception ex) {
            Logger.getLogger(FDespachoHieleraConIntegracion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void limpiarDatodsDePanelProductosPorFactura() {

        DefaultTableModel model;
        model = (DefaultTableModel) fDespachoHieleraConIntegracion.jTableProductosPorFactura.getModel();
        if (model.getRowCount() > 0) {
            int a = model.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                model.removeRow(i);
            }
        }
        fDespachoHieleraConIntegracion.txtNombreDeCliente.setText("");
        fDespachoHieleraConIntegracion.txtDireccionCliente.setText("");
        fDespachoHieleraConIntegracion.txtTelefonoCliente.setText("");
        fDespachoHieleraConIntegracion.txtBarroCliente.setText("");
        fDespachoHieleraConIntegracion.txtNombreVendedor.setText("");
    }

    private void llenarJtableProductosPorFactura() {
        DefaultTableModel modelo1 = (DefaultTableModel) fDespachoHieleraConIntegracion.jTableProductosPorFactura.getModel();

        /* Se obtine el listado de los productos en el array y se recorre
         se anexan los productos a la Jtable de productos por factura */
        for (CProductosPorFactura obj : fDespachoHieleraConIntegracion.facturaActual.getListaDetalleFactura()) {

            int fila = fDespachoHieleraConIntegracion.jTableProductosPorFactura.getRowCount();

            modelo1.addRow(new Object[fDespachoHieleraConIntegracion.jTableProductosPorFactura.getRowCount()]);

            fDespachoHieleraConIntegracion.jTableProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
            fDespachoHieleraConIntegracion.jTableProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1);
            fDespachoHieleraConIntegracion.jTableProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2);
            fDespachoHieleraConIntegracion.jTableProductosPorFactura.setValueAt(obj.getCantidad(), fila, 3);
            fDespachoHieleraConIntegracion.jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), fila, 4);
            fDespachoHieleraConIntegracion.jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva() * obj.getCantidad()), fila, 5);
        }
    }

    private void colocarRegistroEnLaTabla() {

        /* se anexa el registro a la Jtable de facturas por manifiesto */
        fDespachoHieleraConIntegracion.filaTabla2 = fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getRowCount();

        fDespachoHieleraConIntegracion.modelo2.addRow(new Object[fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getRowCount()]);

        // fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt("" + (fDespachoHieleraConIntegracion.filaTabla2 + 1), fDespachoHieleraConIntegracion.filaTabla2, 0);
        fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura(), fDespachoHieleraConIntegracion.filaTabla2, 1);
        fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente(), fDespachoHieleraConIntegracion.filaTabla2, 2);
        fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(nf.format(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura()), fDespachoHieleraConIntegracion.filaTabla2, 3);

        fDespachoHieleraConIntegracion.valorTotalManifiesto += fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura();
        fDespachoHieleraConIntegracion.jBtnBorrarFIla.setEnabled(true);

    }

    /**
     * Método que permite valida si la factura e encuentra ingresada en el
     * manifiesto actual
     *
     * @return verdadero sí la factura ya está ingresada y retorna falso sí la
     * factura no se encuentra ingresada.
     */
    public boolean estaLaFacturaEnElManifiesto() {
        boolean existe = false;
        if (fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto() != null) {
            for (CFacturasPorManifiesto obj : fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto()) {
                if (fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura().equals(obj.getNumeroFactura())) {
                    existe = true;
                    break;
                }
            }
        }

        return existe;
    }

    public void nuevo() {
        try {
            //         cancelar();
            fDespachoHieleraConIntegracion.nuevo = true;
            limpiar();

            asignarFechaManifiesto();

            fDespachoHieleraConIntegracion.jPanel7.setEnabled(true);
            fDespachoHieleraConIntegracion.txtnombreDeConductor.setEnabled(true);
            fDespachoHieleraConIntegracion.txtnombreDeConductor.setEditable(true);
            fDespachoHieleraConIntegracion.cbxCanales.setEnabled(true);
            fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.setEnabled(true);
            fDespachoHieleraConIntegracion.dateManifiesto.setEnabled(false);
            fDespachoHieleraConIntegracion.jTabbedPane1.setEnabled(false);
            fDespachoHieleraConIntegracion.btnCrearManifiesto.setEnabled(true);
            fDespachoHieleraConIntegracion.btnNuevo.setEnabled(false);
            fDespachoHieleraConIntegracion.jBtnNuevo.setEnabled(false);
            fDespachoHieleraConIntegracion.txtPlaca.requestFocus();
            fDespachoHieleraConIntegracion.formaDePago = ""; // contado

            fDespachoHieleraConIntegracion.listaDeAuxiliares = new ArrayList();

            fDespachoHieleraConIntegracion.listaDeFacturasEnElArchivo = new ArrayList();
            fDespachoHieleraConIntegracion.manifiestoActual = new CManifiestosDeDistribucion(ini);
            fDespachoHieleraConIntegracion.manifiestoActual.setEstadoManifiesto(0);

            fDespachoHieleraConIntegracion.lblPesoManifiesto.setText("0.0 Kg");
            fDespachoHieleraConIntegracion.lblPesoDeLaFactura.setText("0.0 Kg");
            fDespachoHieleraConIntegracion.lblValorManifiesto.setText("$0.0");

            fDespachoHieleraConIntegracion.txtnombreDeConductor.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(FDespachoHieleraConIntegracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void imprimir() {
        /*valida sin pendientes del conductor */
        try {
            if (fDespachoHieleraConIntegracion.manifiestoActual.getEstadoManifiesto() == 2) {

                if (false) {
                    //if (manifiestoActual.getCantidadManifiestosPendientesConductor() > 0) {
                    fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgRutasPendentes();
                    //JOptionPane.showMessageDialog(this, "El conductor tiene pendientes de ruta,\n no se puede Imprimir este manifiesto", "Conductor no esta paz y salvo", JOptionPane.ERROR_MESSAGE, null);

                    return;
                }

                /*Manifiesto no grabado en la BBDD, trae los registros localmente */
                // manifiestoActual.setListaFacturasPorManifiesto(listaFacturasPorManifiesto);
            } else {
                /*Manifiesto grabado en la BBDD , trae registros desde allí*/
                fDespachoHieleraConIntegracion.manifiestoActual.setListaFacturasPorManifiesto();
            }


            /* Genera el manifiesto  Genera el rutero*/
            RepporteRuteroConductoresConPesosyDEscuentos demo = new RepporteRuteroConductoresConPesosyDEscuentos(ini, fDespachoHieleraConIntegracion.manifiestoActual);
            //RepporteRuteroLaHielera demo = new RepporteRuteroLaHielera(ini, manifiestoActual);

        } catch (Exception ex) {
            Logger.getLogger(FDespachoHieleraConIntegracion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método que asigna las factura leida por teclado ó por código de barras al
     * manifiesto actual
     *
     * @param numeroDeFactura leida por código de barras ó por teclado
     * @param desdeArchivo
     * @param adherencia
     */
    public void agregarFactura(String numeroDeFactura, boolean desdeArchivo, int adherencia) throws Exception {

        fDespachoHieleraConIntegracion.facturaActual = null;

        try {

            /* se crea el objeto factura, digitado en el campo de texto */
            fDespachoHieleraConIntegracion.facturaActual = new CFacturas(ini, numeroDeFactura);

            if (fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura() == null) {
                //JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " no existe en servidor local", "Error, factura no existe", JOptionPane.WARNING_MESSAGE);
                fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaNoExiste(numeroDeFactura);
                return;
            } else if (fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente().contains("A N U L A D O")) {
                fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaAnulada(numeroDeFactura);
                fDespachoHieleraConIntegracion.facturaActual = null;
                return;
            } else if (fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura() != null) {

                /*Se valida el tipo de movimiento de la factura*/
                switch (fDespachoHieleraConIntegracion.facturaActual.getEstadoFactura()) {

                    case 2:
                        /*Entrega total*/
                        //JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);
                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgEntregaTotal(numeroDeFactura);
                        return;

                    case 3:
                        /*Devolucion total*/
                        //JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " fue devuelta de ruta", "Error, factura Devuelta", JOptionPane.WARNING_MESSAGE);

                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgDevolucionTotal(numeroDeFactura);
                        return;
                    case 4:/*Entrega con novedad*/
                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgEntregaTotal(numeroDeFactura);
                        //JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

                        return;
                    case 5:/*Entrega total con recogida*/
                        // JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgEntregaTotal(numeroDeFactura);
                        return;
                    case 6:
                        /*Re Envio*/
                        break;

                    case 8:
                        /*ANULADA*/
                        fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaAnulada(numeroDeFactura);
                        //JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + "  fue ANULADA", "Error, factura ANULADA", JOptionPane.ERROR_MESSAGE);
                        return;

                }

                CFacturasPorManifiesto facXMfto = new CFacturasPorManifiesto(ini);

                if (fDespachoHieleraConIntegracion.facturaActual.getIsFree() == 0) {
//                    JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura
//                            + " ya se encuentra en Distribución "
//                            + "", "Error", JOptionPane.WARNING_MESSAGE);
                    fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaEnDistribucion(numeroDeFactura);

                    /* =1  la factura está disponible para agregarla al manifiesto 
                           SE registran las facturas que presentan inconvenientes en el Jlist*/
                    //modelo3.addElement(numeroDeFactura);
                    //jListListaDeFacturasErradas.setModel(modelo3);
                    // return;
                }


                /* SE valida que la factura no esté en el manifiesto */
                if (!estaLaFacturaEnElManifiesto()) {
                    fDespachoHieleraConIntegracion.jBtnMinuta.setEnabled(false);

                    /* Se agregan las propiedades a l objeto vista facturas por manifiesto */
                    facXMfto.setAdherencia(fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size() + 1);
                    facXMfto.setNumeroManifiesto(fDespachoHieleraConIntegracion.manifiestoActual.getNumeroManifiesto());
                    facXMfto.setNumeroFactura(fDespachoHieleraConIntegracion.facturaActual.getNumeroDeFactura());
                    facXMfto.setNombreDeCliente(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                    facXMfto.setDireccionDeCliente(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                    facXMfto.setValorTotalFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setPesoFactura(fDespachoHieleraConIntegracion.facturaActual.getPesofactura());
                    facXMfto.setConsecutivo(0);
                    facXMfto.setIdCanal(fDespachoHieleraConIntegracion.manifiestoActual.getIdCanal());
                    facXMfto.setNombreCanal(fDespachoHieleraConIntegracion.manifiestoActual.getNombreCanal());
                    facXMfto.setValorARecaudarFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setFechaDistribucion(fDespachoHieleraConIntegracion.manifiestoActual.getFechaDistribucion());
                    facXMfto.setVehiculo(fDespachoHieleraConIntegracion.manifiestoActual.getVehiculo());
                    facXMfto.setConductor(fDespachoHieleraConIntegracion.manifiestoActual.getConductor());
                    facXMfto.setNombreConductor(fDespachoHieleraConIntegracion.manifiestoActual.getNombreConductor());
                    facXMfto.setNombreDeCliente(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                    facXMfto.setDireccionDeCliente(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                    facXMfto.setValorFacturaSinIva(fDespachoHieleraConIntegracion.facturaActual.getValorFacturaSinIva());
                    facXMfto.setValorTotalFactura(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura());
                    facXMfto.setValorRechazo(0.0);
                    facXMfto.setValorDescuento(0.0);
                    facXMfto.setValorRecaudado(0.0);
                    facXMfto.setVendedor(fDespachoHieleraConIntegracion.facturaActual.getVendedor());


                    /* se actualiza el canal  y la forma de pago de distribución de la factura */
                    fDespachoHieleraConIntegracion.facturaActual.setFormaDePago(fDespachoHieleraConIntegracion.formaDePago);
                    fDespachoHieleraConIntegracion.sumadorPesosFacturas += facXMfto.getPesoFactura();
                    fDespachoHieleraConIntegracion.valorTotalManifiesto += facXMfto.getValorTotalFactura();
                    fDespachoHieleraConIntegracion.manifiestoActual.setPesoKgManifiesto(fDespachoHieleraConIntegracion.sumadorPesosFacturas);
                    fDespachoHieleraConIntegracion.manifiestoActual.setValorTotalManifiesto(fDespachoHieleraConIntegracion.valorTotalManifiesto);

                    /* se verifica que la factura esté libre, sino arroja el siguiente mensaje... 
                     =0 indica que la factura esta siendo ocupada por alguien  */
 /* se agrega el registro al array  de facturas por manifiesto */
                    fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().add(facXMfto);

                    /*Validamos de donde proviene el dato: bien sea del campo 
                            de texto ó de un archivo plano */
                    if (!desdeArchivo) {
                        /* sí hay datos en la tabla de los productos  de la factura se limpia la tabla */
                        limpiarDatodsDePanelProductosPorFactura();

                        fDespachoHieleraConIntegracion.txtNombreDeCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getNombreDeCliente());
                        fDespachoHieleraConIntegracion.txtDireccionCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getDireccionDeCliente());
                        fDespachoHieleraConIntegracion.txtTelefonoCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getTelefonoCliente());
                        fDespachoHieleraConIntegracion.txtBarroCliente.setText(fDespachoHieleraConIntegracion.facturaActual.getBarrio());
                        fDespachoHieleraConIntegracion.txtNombreVendedor.setText(fDespachoHieleraConIntegracion.facturaActual.getVendedor());
                        fDespachoHieleraConIntegracion.lblValorFactura.setText(nf.format(fDespachoHieleraConIntegracion.facturaActual.getValorTotalFactura()));

                        DecimalFormat df = new DecimalFormat("#,###.0");
                        fDespachoHieleraConIntegracion.lblPesoDeLaFactura.setText(df.format((fDespachoHieleraConIntegracion.facturaActual.getPesofactura()) / 1000) + " Kg");

                        fDespachoHieleraConIntegracion.facturaActual.setListaDetalleFactura(false); // Vst_ProductosPorFactura


                        /* se llena la tabla de productos por Factura*/
                        llenarJtableProductosPorFactura();

                    }
                    fDespachoHieleraConIntegracion.sumadorPesosFacturas += fDespachoHieleraConIntegracion.facturaActual.getPesofactura();

                    /*Se coloca el registro en la tabla de facturas*/
                    colocarRegistroEnLaTabla();

                    /* se ubica el cursor en la fila insertada */
                    fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.changeSelection(fDespachoHieleraConIntegracion.filaTabla2, 0, false, false);

                    /* se imprime el dato en la respectiva etiqueta */
                    DecimalFormat df = new DecimalFormat("#,###.0");
                    fDespachoHieleraConIntegracion.lblPesoManifiesto.setText(df.format(fDespachoHieleraConIntegracion.sumadorPesosFacturas / 1000) + " Kg");
                    fDespachoHieleraConIntegracion.lblValorManifiesto.setText(nf.format(fDespachoHieleraConIntegracion.manifiestoActual.getValorTotalManifiesto()));
                    fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
                    fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
                    fDespachoHieleraConIntegracion.jBtnBorrarFIla.setEnabled(!fDespachoHieleraConIntegracion.grabar);

                    /* si el registro existe en la tabla TMPfacturasPorManifiesto, hace nada */
                    fDespachoHieleraConIntegracion.txtNombreDeCliente.requestFocus();

                }

                /* si no   existe la factura, arroja un mensaje de error. */
            } else {

//                JOptionPane.showInternalMessageDialog(this, "La factura # " + numeroDeFactura
//                        + "  no existe en el sistema "
//                        + "", "Error", JOptionPane.WARNING_MESSAGE);
                fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaNoExiste(numeroDeFactura);
            }

            fDespachoHieleraConIntegracion.txtNombreDeCliente.requestFocus();
        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosHielera_2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void modificarManifiesto() throws Exception {

        /* Se verifica que haya un manifiesto instanciado */
        if (fDespachoHieleraConIntegracion.manifiestoActual != null) {

            /* Se valida que el manifiesto actual esté disponible , es decir que no esté otro usuario ingresando
             facturas al sistema con el mismo vehículo*/
            //if (manifiestoActual.getIsFree() == 1) {
            if ((fDespachoHieleraConIntegracion.manifiestoActual.getIsFree() == 1) || (fDespachoHieleraConIntegracion.manifiestoActual.getUsuarioManifiesto().equals(Inicio.deCifrar(ini.getUser().getNombreUsuario())))) {
                try {

                    DefaultTableModel modelo2 = (DefaultTableModel) fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getModel();
                    // manifiestoActual.listadoDeFacturas(manifiestoActual.getNumeroManifiesto());

                    //listaFacturasPorManifiesto = new ArrayList<>(); // CfacturasPorManifiesto

                    /*Se llama al fichero que contiene los numero de las facturas
                     a  que están registradas en el manifiesto*/
                    File fichero = new File(fDespachoHieleraConIntegracion.manifiestoActual.getRutArchivofacturasporManifiesto());

                    /*Se valida que exista el fichero */
                    if (fichero.exists()) {

                        fDespachoHieleraConIntegracion.manifiestoActual.setListaFacturasPorManifiesto(fichero);

                        //listaFacturasPorManifiesto = manifiestoActual.getListaFacturasPorManifiesto();
                        if (fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().isEmpty() || fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto() == null) {
                            fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
                            fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
                            fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(false);
                        } else {
                            fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
                            fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
                            fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(true);
                            fDespachoHieleraConIntegracion.btnImprimir.setEnabled(true);
                            fDespachoHieleraConIntegracion.jBtnImprimir.setEnabled(true);
                        }

                        llenarDatosManifiesto();

                        fDespachoHieleraConIntegracion.txtnombreDeConductor.setEditable(false);
                        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setEditable(false);
                        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setEditable(false);
                        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setEditable(false);

                        fDespachoHieleraConIntegracion.txtPlaca.setEnabled(true);
                        fDespachoHieleraConIntegracion.txtPlaca.setEditable(false);
                        fDespachoHieleraConIntegracion.cbxCanales.setEnabled(false);
                        fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.setEnabled(false);
                        fDespachoHieleraConIntegracion.dateManifiesto.setEnabled(false);
                        fDespachoHieleraConIntegracion.jTabbedPane1.setEnabled(true);
                        fDespachoHieleraConIntegracion.jTabbedPane1.setEnabledAt(fDespachoHieleraConIntegracion.jTabbedPane1.indexOfComponent(fDespachoHieleraConIntegracion.pnlAgregarFactura), true);
                        fDespachoHieleraConIntegracion.btnCrearManifiesto.setEnabled(false);

                        fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
                        fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
                        fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(true);

                        fDespachoHieleraConIntegracion.txtBarroCliente.setEditable(false);
                        fDespachoHieleraConIntegracion.txtDireccionCliente.setEditable(false);
                        fDespachoHieleraConIntegracion.txtNombreDeCliente.setEditable(false);
                        fDespachoHieleraConIntegracion.txtTelefonoCliente.setEditable(false);
                        fDespachoHieleraConIntegracion.txtNumeroDeManifiesto.setEditable(false);
                        fDespachoHieleraConIntegracion.txtNombreVendedor.setEditable(false);

                        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-mm-dd");
                        String strFecha = fDespachoHieleraConIntegracion.manifiestoActual.getFechaDistribucion();
                        Date fecha = null;
                        fecha = formatoDelTexto.parse(strFecha);
                        //dateManifiesto.setDate(manifiestoActual.getFechaDistribucion());

                        fDespachoHieleraConIntegracion.dateManifiesto.setDate(fecha);

                        fDespachoHieleraConIntegracion.txtKmDeSalida.setEditable(false);
                        fDespachoHieleraConIntegracion.cbxPrefijos.setEnabled(true);
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(true);
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEditable(true);
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();

                    } else {

                        //listaFacturasPorManifiesto = new ArrayList<>();
                        llenarDatosManifiesto();

                        //txtPlaca.setEditable(false);
                        fDespachoHieleraConIntegracion.txtKmDeSalida.setEditable(false);

                        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-mm-dd");
                        String strFecha = fDespachoHieleraConIntegracion.manifiestoActual.getFechaDistribucion();
                        Date fecha = null;
                        fecha = formatoDelTexto.parse(strFecha);

                        //dateManifiesto.setDate(manifiestoActual.getFechaDistribucion());
                        fDespachoHieleraConIntegracion.dateManifiesto.setDate(fecha);

                        fDespachoHieleraConIntegracion.txtnombreDeConductor.setEditable(false);
                        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setEditable(false);
                        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setEditable(false);
                        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setEditable(false);

                        fDespachoHieleraConIntegracion.jTabbedPane1.setEnabled(true);
                        fDespachoHieleraConIntegracion.jTabbedPane1.setEnabledAt(fDespachoHieleraConIntegracion.jTabbedPane1.indexOfComponent(fDespachoHieleraConIntegracion.pnlAgregarFactura), true);
                        fDespachoHieleraConIntegracion.cbxCanales.setEnabled(false);
                        fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.setEnabled(false);
                        fDespachoHieleraConIntegracion.dateManifiesto.setEnabled(false);
                        fDespachoHieleraConIntegracion.cbxPrefijos.setEnabled(true);
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(true);
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEditable(true);
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();
                        fDespachoHieleraConIntegracion.btnCrearManifiesto.setEnabled(false);

                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();

                    }

                } catch (Exception ex) {
                    Logger.getLogger(FManifestarPedidosHielera_2.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                limpiar();
                fDespachoHieleraConIntegracion.liberado = false;
                fDespachoHieleraConIntegracion.jTabbedPane1.setEnabled(false);
                fDespachoHieleraConIntegracion.manifiestoActual = null;
                fDespachoHieleraConIntegracion.btnCrearManifiesto.setEnabled(false);
                fDespachoHieleraConIntegracion.cbxCanales.setEnabled(false);
                fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.setEnabled(false);
                fDespachoHieleraConIntegracion.dateManifiesto.setEnabled(false);
                fDespachoHieleraConIntegracion.txtKmDeSalida.setEnabled(false);

                //JOptionPane.showMessageDialog(this, "Esa Ruta está siendo despachada por otro usuario ", "Error", 0);
                fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgRutaUsadaOtroUsuario();
                fDespachoHieleraConIntegracion.txtPlaca.requestFocus();

            }
        }
    }

    private void llenarDatosManifiesto() throws Exception {

        fDespachoHieleraConIntegracion.sumadorPesosFacturas = 0.0;

        /*  Se llenan los campos del conducor */
        for (CEmpleados obj : ini.getListaDeEmpleados()) {
            if (fDespachoHieleraConIntegracion.manifiestoActual.getConductor().equals(obj.getCedula())) {

                fDespachoHieleraConIntegracion.txtnombreDeConductor.setText(obj.getNombres() + " " + obj.getApellidos());
                fDespachoHieleraConIntegracion.conductor = obj;

            }
        }

        fDespachoHieleraConIntegracion.txtPlaca.setText(fDespachoHieleraConIntegracion.manifiestoActual.getVehiculo());

        fDespachoHieleraConIntegracion.setTitle(fDespachoHieleraConIntegracion.txtnombreDeConductor.getText() + "-" + fDespachoHieleraConIntegracion.manifiestoActual.getVehiculo());

        /*Se llenan los campos de texto de los auxiliares*/
        llenarTxtAuxiliares();

        /*  Se llenan los campos del despachador */
        if (fDespachoHieleraConIntegracion.manifiestoActual.getDespachador().equals("0")) {
            fDespachoHieleraConIntegracion.txtNombreDedespachador.setText("");

        } else {
            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (fDespachoHieleraConIntegracion.manifiestoActual.getDespachador().equals(obj.getCedula())) {

                    fDespachoHieleraConIntegracion.txtNombreDedespachador.setText(obj.getNombres() + " " + obj.getApellidos());
                    fDespachoHieleraConIntegracion.despachador = obj;
                }
            }
        }

        /*  Se llenan los campos del canal de distribución  */
        for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
            if (obj.getIdCanalDeVenta() == fDespachoHieleraConIntegracion.manifiestoActual.getIdCanal()) {
                fDespachoHieleraConIntegracion.cbxCanales.setSelectedItem(obj.getNombreCanalDeVenta());
                fDespachoHieleraConIntegracion.canalDeVenta = obj;
            }
        }
        /*  Se llenan los campos ruta de distribución  */
        for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
            if (obj.getIdRutasDeDistribucion() == fDespachoHieleraConIntegracion.manifiestoActual.getIdRuta()) {
                fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.setSelectedItem(obj.getNombreRutasDeDistribucion());
                fDespachoHieleraConIntegracion.ruta = obj;
            }
        }
        fDespachoHieleraConIntegracion.txtKmDeSalida.setText("" + fDespachoHieleraConIntegracion.carro.getKilometrajeActual());
        fDespachoHieleraConIntegracion.txtNumeroDeManifiesto.setText(fDespachoHieleraConIntegracion.manifiestoActual.codificarManifiesto());

        fDespachoHieleraConIntegracion.manifiestoActual.setIsFree(0);

        //System.out.println("libera manifiesto en formulario");
        fDespachoHieleraConIntegracion.manifiestoActual.liberarManifiesto(false);

        System.out.println("llama la lista de facturas por manifiesto");

//listaFacturasPorManifiesto = manifiestoActual.getListaFacturasPorManifiesto();
//        lblNumeroManifiesto.setText("Total a recaudar en Manifiesto # " + txtNumeroDeManifiesto.getText().trim());
        DecimalFormat df = new DecimalFormat("#,###.0");
        fDespachoHieleraConIntegracion.lblPesoManifiesto.setText(df.format(fDespachoHieleraConIntegracion.sumadorPesosFacturas / 1000) + " Kg");
        fDespachoHieleraConIntegracion.lblValorManifiesto.setText(nf.format(fDespachoHieleraConIntegracion.manifiestoActual.getValorTotalManifiesto()));

        llenarJtableFacturasXmanifiesto();

    }

    public void llenarJtableFacturasXmanifiesto() {
        DefaultTableModel modelo = (DefaultTableModel) fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        DecimalFormat df;
        fDespachoHieleraConIntegracion.valorTotalManifiesto = 0.0;
        fDespachoHieleraConIntegracion.modelo2 = (DefaultTableModel) fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getModel();
        fDespachoHieleraConIntegracion.adherenciaGenral = 1;
        //  SE LLENA EL JTABLE FACTURAS POR MANIFIESTO
        if (fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto() != null) {

            for (CFacturasPorManifiesto obj : fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto()) {

                fDespachoHieleraConIntegracion.adherenciaGenral = obj.getAdherencia() + 1;

                int fila = fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getRowCount();
                fDespachoHieleraConIntegracion.modelo2.addRow(new Object[fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getRowCount()]);

                fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt("" + obj.getAdherencia(), fila, 0); // item 
                fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), fila, 1); // numero de la factura
                fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), fila, 2); // cliente

                fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), fila, 3); // valor de la factura

                // se ubica en la fila insertada
                fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.changeSelection(fDespachoHieleraConIntegracion.filaTabla2, 0, false, false);

                fDespachoHieleraConIntegracion.sumadorPesosFacturas += obj.getPesoFactura();
                fDespachoHieleraConIntegracion.valorTotalManifiesto += obj.getValorTotalFactura();

            }

            df = new DecimalFormat("#,###.0");
            fDespachoHieleraConIntegracion.lblPesoManifiesto.setText(df.format(fDespachoHieleraConIntegracion.sumadorPesosFacturas / 1000) + " Kg");
            fDespachoHieleraConIntegracion.lblValorManifiesto.setText(nf.format(fDespachoHieleraConIntegracion.manifiestoActual.getValorTotalManifiesto()));
            fDespachoHieleraConIntegracion.lblCantidadFacturas.setText("" + fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size());
            fDespachoHieleraConIntegracion.manifiestoActual.setValorTotalManifiesto(fDespachoHieleraConIntegracion.valorTotalManifiesto);

        }
    }

    private void llenarTxtAuxiliares() {
        /*se llenan los campos de texto de los nombres de los auxiliares*/
        int indice = 1;

        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setText("SALE A DISTRIBUCION SIN AUXILIAR1");
        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR2");
        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setText("SALE A DISTRIBUCION SIN AUXILIAR3");
        fDespachoHieleraConIntegracion.listaDeAuxiliares = fDespachoHieleraConIntegracion.manifiestoActual.getListaDeAuxiliares("" + fDespachoHieleraConIntegracion.manifiestoActual.getNumeroManifiesto());

        if (fDespachoHieleraConIntegracion.listaDeAuxiliares.size() > 0) {
            for (CEmpleados aux : fDespachoHieleraConIntegracion.listaDeAuxiliares) {
                switch (indice) {
                    case 1:
                        if (aux.getCedula().equals("0")) {
                            fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setText("SALE A DISTRIBUCION SIN AUXILIAR1");
                        } else {
                            fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setText(aux.getNombres() + " " + aux.getApellidos());
                            fDespachoHieleraConIntegracion.auxiliar1 = aux;
                        }
                        indice++;
                        break;
                    case 2:
                        if (aux.getCedula().equals("0")) {
                            fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR2");
                        } else {
                            fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setText(aux.getNombres() + " " + aux.getApellidos());
                            fDespachoHieleraConIntegracion.auxiliar2 = aux;
                        }
                        indice++;
                        break;
                    case 3:
                        if (aux.getCedula().equals("0")) {
                            fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setText("SALE A DISTRIBUCION SIN AUXILIAR3");
                        } else {
                            fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setText(aux.getNombres() + " " + aux.getApellidos());
                            fDespachoHieleraConIntegracion.auxiliar3 = aux;
                        }
                        indice++;

                        break;

                }
                /* fin switch */

            }
        }

    }

    private void crearManifiesto() {
        try {

            fDespachoHieleraConIntegracion.canalDeVenta = null;
            fDespachoHieleraConIntegracion.ruta = null;

            /*Se crea un objeto ruta de distribucion*/
            for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
                if (obj.getNombreRutasDeDistribucion().equals(fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.getSelectedItem().toString())) {
                    fDespachoHieleraConIntegracion.ruta = new CRutasDeDistribucion(ini);
                    fDespachoHieleraConIntegracion.ruta = obj;
                    break;
                }
            }

            /*Se crea un objeto canal de distribucion*/
            for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
                if (obj.getNombreCanalDeVenta().equals(fDespachoHieleraConIntegracion.cbxCanales.getSelectedItem().toString())) {
                    fDespachoHieleraConIntegracion.canalDeVenta = new CCanalesDeVenta(ini);
                    fDespachoHieleraConIntegracion.canalDeVenta = obj;
                    break;
                }
            }

            fDespachoHieleraConIntegracion.sumadorPesosFacturas = 0.0;
            /* Se validan todos los datos del manifiesdo conductor, vehiculo, ruta, canal...*/
            if (validarManifiesto()) {

                new Thread(new HiloCrearManifiesto(ini, fDespachoHieleraConIntegracion, fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2)).start();

            } else {
                fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgErrorAlguardar(fDespachoHieleraConIntegracion.mensaje);
                // JOptionPane.showInternalMessageDialog(this, mensaje, "Error al guardar El Manifiesto de salida a Ruta", 0);
            }

        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosHielera_2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean validarManifiesto() {
        boolean verificado = true;
        try {

            fDespachoHieleraConIntegracion.mensaje = "";
//            String fi = String.valueOf(Inicio.getFechaSql());
//            String ff = String.valueOf(ini.getFechaActualServidor());

            if (fDespachoHieleraConIntegracion.cbxCanales.getSelectedIndex() == 0) {
                fDespachoHieleraConIntegracion.mensaje += "No ha seleccionado el canal de distribución " + "  \n";
                verificado = false;
            }

            if (fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.getSelectedIndex() == 0) {
                fDespachoHieleraConIntegracion.mensaje += "No ha seleccionado la Ruta de Distribución " + "  \n";
                verificado = false;
            }

//            if (!fi.equals(ff)) {
//                mensaje += "La fecha del Servidor y el Sistema no coinciden, verificar configuración del sistema " + "  \n";
//                verificado = false;
//            }
            if (fDespachoHieleraConIntegracion.carro == null) {
                fDespachoHieleraConIntegracion.mensaje += "No ha selecccionado el Vehiculo de la Ruta" + "  \n";
                verificado = false;
            }
            if (fDespachoHieleraConIntegracion.conductor == null) {
                fDespachoHieleraConIntegracion.mensaje += "No ha asigando el conductor de la ruta" + "  \n";
                verificado = false;

            }
//            if (auxiliar1 == null) {
//                mensaje += "No ha asigando el Auxiliar de la ruta " + "  \n";
//                verificado = false;
//            } else if (auxiliar1.getCedula().equals(conductor.getCedula())) {
//                mensaje += "La cédula del conductor y del auxiliar son la misma  " + "  \n";
//                verificado = false;
//            }
//            
            if (fDespachoHieleraConIntegracion.despachador == null) {
                fDespachoHieleraConIntegracion.mensaje += "No ha asigando un despachador de ruta.El Campo es obligatorio " + "  \n";
                verificado = false;
            }

            if (fDespachoHieleraConIntegracion.txtKmDeSalida.getText().isEmpty()) {
                fDespachoHieleraConIntegracion.mensaje += "No ha colocado el kilometraje de salidad  del vehículo" + "  \n";
                verificado = false;
            }

            if (fDespachoHieleraConIntegracion.canalDeVenta == null) {
                fDespachoHieleraConIntegracion.mensaje += "No ha selecccionado El canal de venta " + "  \n";
                verificado = false;
            }
            if (fDespachoHieleraConIntegracion.ruta == null) {
                fDespachoHieleraConIntegracion.mensaje += "No ha selecccionado la Ruta a distribuir " + "  \n";
                verificado = false;
            }
            if (fDespachoHieleraConIntegracion.manifiestoActual == null) {
                fDespachoHieleraConIntegracion.mensaje += "Vehiculo ya tiene asignado un manifiesto de Distribución sin cerrar" + "  \n";
                verificado = false;
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloFDespchoHieleraConIntegracion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return verificado;
    }

    public void validarManifiesto(Date fecha) throws Exception {
        if (fDespachoHieleraConIntegracion.conductor != null && fDespachoHieleraConIntegracion.carro != null) {
            CManifiestosDeDistribucion manifiestosDeDistribucion = new CManifiestosDeDistribucion(ini,
                    fDespachoHieleraConIntegracion.conductor.getCedula(), fDespachoHieleraConIntegracion.carro.getPlaca(), "" + fecha);

            if (manifiestosDeDistribucion.getNumeroManifiesto() == null) {
                // crearNuevoManifiesto(carro);

            } else {
                fDespachoHieleraConIntegracion.manifiestoActual = manifiestosDeDistribucion;

                // this.setTitle(carro.getPlaca());
                llenarDatosManifiesto();
                fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setEnabled(false);
                fDespachoHieleraConIntegracion.cbxCanales.setEnabled(false);
                fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.setEnabled(false);
                fDespachoHieleraConIntegracion.dateManifiesto.setEnabled(false);
                fDespachoHieleraConIntegracion.btnCrearManifiesto.setEnabled(false);

                if (fDespachoHieleraConIntegracion.manifiestoActual.getEstadoManifiesto() < 4) {
                    fDespachoHieleraConIntegracion.cbxPrefijos.setEnabled(true);
                    fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(true);
                    fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEditable(true);
                    fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();
                } else {
                    fDespachoHieleraConIntegracion.cbxPrefijos.setEnabled(false);
                    fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(false);
                    fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEditable(false);

                }
            }
        } else {
            fDespachoHieleraConIntegracion.txtPlaca.setEnabled(true);
            fDespachoHieleraConIntegracion.txtPlaca.setEditable(true);
            fDespachoHieleraConIntegracion.txtPlaca.requestFocus();
        }
    }

    public void validarManifiesto2(Date fecha) {
        if (fDespachoHieleraConIntegracion.conductor != null && fDespachoHieleraConIntegracion.carro != null) {
            try {
                CManifiestosDeDistribucion manifiestosDeDistribucion = new CManifiestosDeDistribucion(ini,
                        fDespachoHieleraConIntegracion.conductor.getCedula(),
                        fDespachoHieleraConIntegracion.carro.getPlaca(),
                        fDespachoHieleraConIntegracion.despachador.getCedula(), "" + fecha);

                if (manifiestosDeDistribucion.getNumeroManifiesto() == null) {
                    // crearNuevoManifiesto(carro);
                    // this.setTitle(txtPlaca.getText());
                    fDespachoHieleraConIntegracion.txtKmDeSalida.setText("120");
                    fDespachoHieleraConIntegracion.cbxCanales.setEnabled(true);
                    fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.setEnabled(true);
//                    txtNombreDeAuxiliar1.setEnabled(true);
//                    txtNombreDeAuxiliar1.setEditable(true);
//                    txtNombreDeAuxiliar1.requestFocus();
                } else {
                    fDespachoHieleraConIntegracion.manifiestoActual = manifiestosDeDistribucion;
                    //this.setTitle(carro.getPlaca());
                    llenarDatosManifiesto();
                    fDespachoHieleraConIntegracion.txtnombreDeConductor.setEnabled(false);
                    fDespachoHieleraConIntegracion.txtPlaca.setEnabled(false);
                    fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setEnabled(false);
                    fDespachoHieleraConIntegracion.cbxCanales.setEnabled(false);
                    fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.setEnabled(false);
                    fDespachoHieleraConIntegracion.dateManifiesto.setEnabled(false);
                    fDespachoHieleraConIntegracion.btnCrearManifiesto.setEnabled(false);
                    if (fDespachoHieleraConIntegracion.manifiestoActual.getEstadoManifiesto() < 4) {
                        fDespachoHieleraConIntegracion.cbxPrefijos.setEnabled(true);
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(true);
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEditable(true);
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.requestFocus();
                    } else {
                        fDespachoHieleraConIntegracion.cbxPrefijos.setEnabled(false);
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(false);
                        fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEditable(false);

                    }

                }

            } catch (Exception ex) {
                Logger.getLogger(FManifestarPedidosHielera_2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void grabar() throws HeadlessException {
        int deseaGrabarRegistro;

        /*valida sin pendientes del conductor 
        if (fDespachoHieleraConIntegracion.manifiestoActual.getObservaciones().length() <= 2) {
            // if (manifiestoActual.getCantidadManifiestosPendientesConductor() > 0) {
            //JOptionPane.showMessageDialog(this, "El manifiesto no tiene la cantidad de Canastas en ruta", "Manifiesto incompleto", JOptionPane.ERROR_MESSAGE);
            fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgSinCanastas();
            return true;
        }
         */
        if (fDespachoHieleraConIntegracion.manifiestoActual.getEstadoManifiesto() <= 3) {  // IINICIO DEL IF-> ESTADO DEL MANIFIESTO

            //deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea guardar el registro en la BBDD ?", "Guardar registro", JOptionPane.YES_NO_OPTION);
            deseaGrabarRegistro = fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgGrabarCanastas();

            if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {

                fDespachoHieleraConIntegracion.manifiestoActual.setListaDeFacturasSinDespachar();

                fDespachoHieleraConIntegracion.manifiestoActual.setCantidadPedidos(fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size());

                /* Se valida la conexión a internet para grabar los datos en la BBDD */
                if (true) { //if (ini.verificarConexion()) {


                    /* Guarda las facturas manifestadas*/
                    new Thread(new HiloGuardarFacturasPorManifiesto(ini, this.fDespachoHieleraConIntegracion)).start();
                    fDespachoHieleraConIntegracion.manifiestoActual.grabarFacturasEnManifiesto();

                } else {

                    //JOptionPane.showMessageDialog(null, "No hay conexión a internet", "Error al guardar Datos", JOptionPane.WARNING_MESSAGE, null);
                    fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgSinInternet();
                }

            }
        } else {
            fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
            fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
            fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(false);

            fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
            fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

            //JOptionPane.showMessageDialog(null, "Error al guardar los datos, manifiesto  ya está en distribución", "manifiesto ya fue guardado", JOptionPane.ERROR_MESSAGE, null);
            fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgErrorAlguardar("");
        }
    }

    public void grabarunPedido() throws HeadlessException {
        int deseaGrabarRegistro;

        /*valida sin pendientes del conductor 
        if (fDespachoHieleraConIntegracion.manifiestoActual.getObservaciones().length() <= 2) {
            // if (manifiestoActual.getCantidadManifiestosPendientesConductor() > 0) {
            //JOptionPane.showMessageDialog(this, "El manifiesto no tiene la cantidad de Canastas en ruta", "Manifiesto incompleto", JOptionPane.ERROR_MESSAGE);
            fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgSinCanastas();
            return true;
        }
         */
        if (fDespachoHieleraConIntegracion.manifiestoActual.getEstadoManifiesto() <= 3) {  // IINICIO DEL IF-> ESTADO DEL MANIFIESTO

            //deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea guardar el registro en la BBDD ?", "Guardar registro", JOptionPane.YES_NO_OPTION);
            deseaGrabarRegistro = fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgGrabarCanastas();

            // fDespachoHieleraConIntegracion.manifiestoActual.setListaDeFacturasSinDespachar();
            //  fDespachoHieleraConIntegracion.manifiestoActual.setCantidadPedidos(fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size());

            /* Se valida la conexión a internet para grabar los datos en la BBDD */
            if (true) { //if (ini.verificarConexion()) {


                /* Guarda las facturas manifestadas*/
                new Thread(new HiloGuardarFacturasPorManifiesto(ini, this.fDespachoHieleraConIntegracion)).start();
                fDespachoHieleraConIntegracion.manifiestoActual.grabarFacturasEnManifiesto();

            } else {

                //JOptionPane.showMessageDialog(null, "No hay conexión a internet", "Error al guardar Datos", JOptionPane.WARNING_MESSAGE, null);
                fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgSinInternet();
            }

        } else {
            fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
            fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
            fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(false);

            fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
            fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

            //JOptionPane.showMessageDialog(null, "Error al guardar los datos, manifiesto  ya está en distribución", "manifiesto ya fue guardado", JOptionPane.ERROR_MESSAGE, null);
            fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgErrorAlguardar("");
        }
    }

    private void asignarDespachador() {
        for (CEmpleados despachador : ini.getListaDeEmpleados()) {
            String cadena = despachador.getNombres() + " " + despachador.getApellidos();
            if (fDespachoHieleraConIntegracion.txtNombreDedespachador.getText().trim().equals(cadena)) {
                fDespachoHieleraConIntegracion.despachador = despachador;
                break;
            }

        }
        if (fDespachoHieleraConIntegracion.despachador == null) {
            fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgSinDespachador();
            // JOptionPane.showMessageDialog(this, "No ha seleccionado el despachador", "Alerta", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Date fecha = Inicio.getFechaSql(fDespachoHieleraConIntegracion.dateManifiesto);

            validarManifiesto2(fecha);

            fDespachoHieleraConIntegracion.txtNombreDedespachador.setEnabled(false);
            fDespachoHieleraConIntegracion.txtNombreDedespachador.setEditable(true);
            fDespachoHieleraConIntegracion.cbxCanales.requestFocus();
        }
    }

    public void llenarDatosManifiestoCerrado() throws Exception {

        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        fDespachoHieleraConIntegracion.txtnombreDeConductor.setEditable(false);

        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setEditable(false);
        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setEditable(false);
        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setEditable(false);
        fDespachoHieleraConIntegracion.txtNombreDedespachador.setEditable(false);
        fDespachoHieleraConIntegracion.txtKmDeSalida.setEditable(false);
        fDespachoHieleraConIntegracion.txtPlaca.setEditable(false);
        fDespachoHieleraConIntegracion.txtKmDeSalida.setEditable(false);
        fDespachoHieleraConIntegracion.cbxCanales.setEnabled(false);
        fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.setEnabled(false);
        fDespachoHieleraConIntegracion.dateManifiesto.setEnabled(false);
        fDespachoHieleraConIntegracion.btnCrearManifiesto.setEnabled(false);
        fDespachoHieleraConIntegracion.jTabbedPane1.setEnabled(false);
        fDespachoHieleraConIntegracion.btnNuevo.setEnabled(false);
        fDespachoHieleraConIntegracion.jBtnNuevo.setEnabled(false);

        fDespachoHieleraConIntegracion.btnImprimir.setEnabled(false);
        fDespachoHieleraConIntegracion.jBtnImprimir.setEnabled(false);

        fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
        fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
        fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(false);

        fDespachoHieleraConIntegracion.dateManifiesto.setEnabled(false);

        fDespachoHieleraConIntegracion.txtPlaca.setEnabled(false);
        fDespachoHieleraConIntegracion.txtnombreDeConductor.setEnabled(false);
        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar1.setEnabled(false);
        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar2.setEnabled(false);
        fDespachoHieleraConIntegracion.txtNombreDeAuxiliar3.setEnabled(false);
        fDespachoHieleraConIntegracion.txtNombreDedespachador.setEnabled(false);

        fDespachoHieleraConIntegracion.jTabbedPane1.setEnabledAt(fDespachoHieleraConIntegracion.jTabbedPane1.indexOfComponent(fDespachoHieleraConIntegracion.pnlAgregarFactura), false);

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
        //manifiestoActual.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        // manifiestoActual.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");

        fDespachoHieleraConIntegracion.manifiestoActual.setListaFacturasPorManifiesto();
        //listaDeCFacturasCamdunEnElManifiesto = manifiestoActual.getVstListaFacturasEnDistribucion(); // CfacturasCamdun
        //listaDeFacturasPorManifiesto = manifiestoActual.getListaFacturasPorManifiesto(); //CFacturasPorManifiesto

        // manifiestoActual.setListaDeAuxiliares();
        fDespachoHieleraConIntegracion.listaDeAuxiliares = fDespachoHieleraConIntegracion.manifiestoActual.getListaDeAuxiliares("" + fDespachoHieleraConIntegracion.manifiestoActual.getNumeroManifiesto());

        fDespachoHieleraConIntegracion.manifiestoActual.setListaDeDescuentos();
        fDespachoHieleraConIntegracion.manifiestoActual.setListaDeRecogidas();

        // SE BUSCA EL CONDUCTOR Y SE LLENA EL CAMPO
        fDespachoHieleraConIntegracion.txtnombreDeConductor.setText(fDespachoHieleraConIntegracion.manifiestoActual.getNombreConductor() + " " + fDespachoHieleraConIntegracion.manifiestoActual.getApellidosConductor());


        /* Se llenan os campos de texto de los auiliares*/
        llenarTxtAuxiliares();

        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
        if (fDespachoHieleraConIntegracion.manifiestoActual.getDespachador().equals("0")) {
            fDespachoHieleraConIntegracion.txtNombreDedespachador.setText("");

        } else {
            fDespachoHieleraConIntegracion.txtNombreDedespachador.setText(fDespachoHieleraConIntegracion.manifiestoActual.getNombreDespachador() + " " + fDespachoHieleraConIntegracion.manifiestoActual.getApellidosDespachador());

        }

        // SE BUSCA EL CANAL Y SE LLENA EL CAMPO
        fDespachoHieleraConIntegracion.cbxCanales.setSelectedItem(fDespachoHieleraConIntegracion.manifiestoActual.getNombreCanal());

        // SE BUSCA EL RUTA Y SE LLENA EL CAMPO
        fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.setSelectedItem(fDespachoHieleraConIntegracion.manifiestoActual.getNombreDeRuta());

        fDespachoHieleraConIntegracion.txtKmDeSalida.setText("" + fDespachoHieleraConIntegracion.manifiestoActual.getKmSalida());
        fDespachoHieleraConIntegracion.txtNumeroDeManifiesto.setText(fDespachoHieleraConIntegracion.manifiestoActual.codificarManifiesto());

        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-mm-dd");
        String strFecha = fDespachoHieleraConIntegracion.manifiestoActual.getFechaDistribucion();
        Date fecha = null;
        fecha = formatoDelTexto.parse(strFecha);
        //dateManifiesto.setDate(manifiestoActual.getFechaDistribucion());
        Date dt = new Date();
        fDespachoHieleraConIntegracion.dateManifiesto.setDate(fecha);

        int cantidadFacturasEnManifiesto = 0;
        fDespachoHieleraConIntegracion.sumadorPesosFacturas = 0.0;

        fDespachoHieleraConIntegracion.modelo2 = (DefaultTableModel) fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getModel();

        double valor = 0.0;
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO
        for (CFacturasPorManifiesto obj : fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto()) {

            // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
            // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
            fDespachoHieleraConIntegracion.filaTabla2 = fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getRowCount();
            fDespachoHieleraConIntegracion.modelo2.addRow(new Object[fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getRowCount()]);

            fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt("" + (fDespachoHieleraConIntegracion.filaTabla2 + 1), fDespachoHieleraConIntegracion.filaTabla2, 0); // item 
            fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), fDespachoHieleraConIntegracion.filaTabla2, 1); // numero de la factura

            fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), fDespachoHieleraConIntegracion.filaTabla2, 2); // cliente

            fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), fDespachoHieleraConIntegracion.filaTabla2, 3); // valor a recaudar  de la factura   
            fDespachoHieleraConIntegracion.valorTotalManifiesto += obj.getValorARecaudarFactura();

            // se ubica en la fila insertada
            fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.changeSelection(fDespachoHieleraConIntegracion.filaTabla2, 0, false, false);

            cantidadFacturasEnManifiesto++;
            valor += obj.getValorARecaudarFactura();
            fDespachoHieleraConIntegracion.sumadorPesosFacturas += obj.getPesoFactura();

            // }
            // }
            //this.repaint();
        }
        fDespachoHieleraConIntegracion.manifiestoActual.setValorTotalManifiesto(valor);

        //   lblNumeroManifiesto.setText("Total a recaudar en Manifiesto # " + txtNumeroDeManifiesto.getText().trim());
        DecimalFormat df = new DecimalFormat("#,###.0");
        fDespachoHieleraConIntegracion.lblPesoManifiesto.setText(df.format(fDespachoHieleraConIntegracion.sumadorPesosFacturas / 1000) + " Kg");
        fDespachoHieleraConIntegracion.lblValorManifiesto.setText(nf.format(fDespachoHieleraConIntegracion.manifiestoActual.getValorTotalManifiesto()));
        fDespachoHieleraConIntegracion.lblCantidadFacturas.setText("" + cantidadFacturasEnManifiesto);

        fDespachoHieleraConIntegracion.btnImprimir.setEnabled(true);
        fDespachoHieleraConIntegracion.jBtnImprimir.setEnabled(true);

        fDespachoHieleraConIntegracion.btnImprimir.requestFocus();

    }

    public synchronized void crearNuevoManifiesto(String cedulaConductor, String fecha) {
        try {

            fDespachoHieleraConIntegracion.canalDeVenta = null;
            fDespachoHieleraConIntegracion.ruta = null;


            /*Se crea un objeto ruta de distribucion*/
            for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
                if (obj.getNombreRutasDeDistribucion().equals(fDespachoHieleraConIntegracion.cbxRutaDeDistribucion.getSelectedItem().toString())) {
                    fDespachoHieleraConIntegracion.ruta = new CRutasDeDistribucion(ini);
                    fDespachoHieleraConIntegracion.ruta = obj;
                    break;
                }
            }

            /*Se crea un objeto canal de distribucion*/
            for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
                if (obj.getNombreCanalDeVenta().equals(fDespachoHieleraConIntegracion.cbxCanales.getSelectedItem().toString())) {
                    fDespachoHieleraConIntegracion.canalDeVenta = new CCanalesDeVenta(ini);
                    fDespachoHieleraConIntegracion.canalDeVenta = obj;
                    break;
                }
            }

            fDespachoHieleraConIntegracion.manifiestoActual = new CManifiestosDeDistribucion(ini);
            fDespachoHieleraConIntegracion.manifiestoActual.setFechaDistribucion("" + Inicio.getFechaSql(fDespachoHieleraConIntegracion.dateManifiesto));
            fDespachoHieleraConIntegracion.manifiestoActual.setVehiculo(fDespachoHieleraConIntegracion.txtPlaca.getText());
            fDespachoHieleraConIntegracion.manifiestoActual.setConductor(fDespachoHieleraConIntegracion.conductor.getCedula());
            fDespachoHieleraConIntegracion.manifiestoActual.setDespachador(fDespachoHieleraConIntegracion.despachador.getCedula());
            fDespachoHieleraConIntegracion.manifiestoActual.setIdCanal(fDespachoHieleraConIntegracion.canalDeVenta.getIdCanalDeVenta());
            fDespachoHieleraConIntegracion.manifiestoActual.setIdRuta(fDespachoHieleraConIntegracion.ruta.getIdRutasDeDistribucion());
            fDespachoHieleraConIntegracion.manifiestoActual.setEstadoManifiesto(2);
            fDespachoHieleraConIntegracion.manifiestoActual.setKmSalida(Integer.parseInt(fDespachoHieleraConIntegracion.txtKmDeSalida.getText()));
            fDespachoHieleraConIntegracion.manifiestoActual.setKmEntrada(Integer.parseInt(fDespachoHieleraConIntegracion.txtKmDeSalida.getText()));
            fDespachoHieleraConIntegracion.manifiestoActual.setKmRecorrido(0);
            fDespachoHieleraConIntegracion.manifiestoActual.setZona(ini.getUser().getZona());
            fDespachoHieleraConIntegracion.manifiestoActual.setRegional(ini.getUser().getRegional());
            fDespachoHieleraConIntegracion.manifiestoActual.setAgencia(ini.getUser().getAgencia());
            fDespachoHieleraConIntegracion.manifiestoActual.setIsFree(1);
            fDespachoHieleraConIntegracion.manifiestoActual.setValorTotalManifiesto(0.0);
            fDespachoHieleraConIntegracion.manifiestoActual.setValorRecaudado(0.0);
            fDespachoHieleraConIntegracion.manifiestoActual.setHoraDeDespacho("CURRENT_TIMESTAMP()");
            fDespachoHieleraConIntegracion.manifiestoActual.setHoraDeLiquidacion("CURRENT_TIMESTAMP()");
            fDespachoHieleraConIntegracion.manifiestoActual.setPesoKgManifiesto(0.0);
            fDespachoHieleraConIntegracion.manifiestoActual.setCantidadPedidos(0);
            fDespachoHieleraConIntegracion.manifiestoActual.setActivo(1);
            fDespachoHieleraConIntegracion.manifiestoActual.setFechaReal("CURRENT_TIMESTAMP()");
            fDespachoHieleraConIntegracion.manifiestoActual.setUsuarioManifiesto(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
            //manifiestoActual.setF(1);
            fDespachoHieleraConIntegracion.manifiestoActual.setObservaciones("SIN NOVEDAD");

            fDespachoHieleraConIntegracion.sumadorPesosFacturas = 0.0;
            /* Se validan todos los datos del manifiesdo conductor, vehiculo, ruta, canal...*/
            if (validarManifiesto()) {

//                new Thread(new HiloCrearManifiesto(ini, this, this.fManifestarPedidosHielera_2)).start();
            } else {
                fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgErrorAlguardar("");
                //JOptionPane.showInternalMessageDialog(this, mensaje, "Error al guardar El Manifiesto de salida a Ruta", 0);
            }

        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosHielera_2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void borrarUnaFactura() throws HeadlessException {
        int deseaEliminarLaFila = 0;
        String numeroDeFactura;
        boolean encontrado = false;

        fDespachoHieleraConIntegracion.conductor.setListaDeFacturasPorConductorSinDespachar();

        /* se identifica la fila seleccionada */
        int fila = fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getSelectedRow();

        if (fDespachoHieleraConIntegracion.conductor.getListaDeFacturasPorConductorSinDespachar() == null) {
            fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgListaVacia();
            return;
        }
        /* Se valida que  la Jtable tenga filas con da3os*/
        if (fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getRowCount() > 0) {

            try {
                numeroDeFactura = fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getValueAt(fila, 1).toString();

                CFacturasPorManifiesto facxman = new CFacturasPorManifiesto(ini, numeroDeFactura, fDespachoHieleraConIntegracion.manifiestoActual.getNumeroManifiesto());

                for (CFacturasPorManifiesto factura : fDespachoHieleraConIntegracion.conductor.getListaDeFacturasPorConductorSinDespachar()) {
                    if (numeroDeFactura.equals(factura.getNumeroFactura())) {

                        encontrado = true;
                        if (factura.getDespachado() != 0) {

                            return;
                        }
                    }
                }
                if (encontrado) {
                    deseaEliminarLaFila = fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgDeseaBorrarFila();

                    if (deseaEliminarLaFila == JOptionPane.YES_OPTION) {

                        try {

                            /* Elimina registro del de la BBDD */
                            facxman.eliminarFacturaDelManifiesto();

                            /*Elimina los registros de la lista del manifiesto y
                            y de la lista del conductor*/
                            eliminarRegistroDelasListas(numeroDeFactura);

                            /* LLenar el Jtable de las facturas por manifiesto */
                            llenarjTableFacturasPorManifiesto();

                            /* ordena la tabla con la nueva adherencia */
                            ordenarTabla();

                            fDespachoHieleraConIntegracion.jBtnBorrarFIla.setEnabled(false);
                            fDespachoHieleraConIntegracion.jBtnMinuta.setEnabled(false);

                        } catch (Exception ex) {
                            Logger.getLogger(HiloFDespchoHieleraConIntegracion.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } else {
                    fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.msgFacturaNoSePuedeBorrar(numeroDeFactura);
                }

                /*  SI DESEA BORRAR LA FILA */
            } catch (Exception ex) {
                Logger.getLogger(HiloFDespchoHieleraConIntegracion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showInternalMessageDialog(fDespachoHieleraConIntegracion, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);
        }
//        } else {
//            JOptionPane.showMessageDialog(null, "No se puede eliminar fila, maniesto en distribución ", "No se puede Borrar fila ", 1, null);
//        }
    }

    private void ordenarTabla() {
        for (int i = 0; i < fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getRowCount(); i++) {
            fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(i + 1, i, 0);
        }

    }

    private void llenarjTableFacturasPorManifiesto() throws Exception {
        DefaultTableModel modelo = (DefaultTableModel) fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        fDespachoHieleraConIntegracion.manifiestoActual.setListaFacturasPorManifiesto();

        modelo = (DefaultTableModel) fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getModel();

        for (CFacturasPorManifiesto obj : fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto()) {

            fDespachoHieleraConIntegracion.filaTabla2 = fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getRowCount();
            fDespachoHieleraConIntegracion.modelo2.addRow(new Object[fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.getRowCount()]);
            if (obj.getDespachado() > 0) {
                fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt("" + (fDespachoHieleraConIntegracion.filaTabla2 + 1), fDespachoHieleraConIntegracion.filaTabla2, 0);  // item

            } else {
                fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt("", fDespachoHieleraConIntegracion.filaTabla2, 0);  // item

            }
            fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), fDespachoHieleraConIntegracion.filaTabla2, 1); // numero de factura
            fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), fDespachoHieleraConIntegracion.filaTabla2, 2); // nombre del nombreDelCliente
            fDespachoHieleraConIntegracion.jTableFacturasPorManifiesto.setValueAt(nf.format(obj.getValorTotalFactura()), fDespachoHieleraConIntegracion.filaTabla2, 3);

        }

    }

    /**
     * Método quepermite organizar el archivo temporal donde se guardan los
     * datos de las facturas del manifiesto y calcula el valor total a recaudar
     * por el manifiesto y calcula la cantidad de facturas reportadas.
     *
     *
     * @return nada
     */
    private void eliminarRegistroDelasListas(String numeroDeFactura) throws Exception {
        fDespachoHieleraConIntegracion.valorTotalManifiesto = 0.0;
        String rutaDearchivo = fDespachoHieleraConIntegracion.manifiestoActual.getRutArchivofacturasporManifiesto(); //"tmp/tmp_" + manifiestoActual.codificarManifiesto() + "_FacturasDescargados.txt";
        // ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDearchivo);

        /* Aca se borra la linea que contiene el numero de la factura*/
        // archivo.borrarFactura(numeroDeFactura);
        int i = 1;

        /* Elimina reegistro del Array que contiene las facturas por manifiesto */
        for (CFacturasPorManifiesto obj : fDespachoHieleraConIntegracion.conductor.getListaDeFacturasPorConductorSinDespachar()) {
            if (obj.getNumeroFactura().equals(numeroDeFactura)) {
                fDespachoHieleraConIntegracion.conductor.getListaDeFacturasPorConductorSinDespachar().remove(obj);

                CFacturas fac = new CFacturas(ini, obj.getNumeroFactura());
                /*  libera la factura para ser usada por otro usuario */
                fac.liberarFactura(true);
                break;
            }

        }

        /* Elimina reegistro del Array que contiene las facturas por manifiesto */
        for (CFacturasPorManifiesto obj : fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto()) {
            if (obj.getNumeroFactura().equals(numeroDeFactura)) {
                fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().remove(obj);

                CFacturas fac = new CFacturas(ini, obj.getNumeroFactura());
                /*  libera la factura para ser usada por otro usuario */
                fac.liberarFactura(true);
                break;
            }
            fDespachoHieleraConIntegracion.valorTotalManifiesto += obj.getValorARecaudarFactura();

        }

        DecimalFormat df = new DecimalFormat("#,###.0");
        fDespachoHieleraConIntegracion.lblPesoManifiesto.setText(df.format(fDespachoHieleraConIntegracion.manifiestoActual.getPesoKgManifiesto() / 1000) + " Kg");
        fDespachoHieleraConIntegracion.lblValorManifiesto.setText(nf.format(fDespachoHieleraConIntegracion.manifiestoActual.getValorTotalManifiesto()));
        fDespachoHieleraConIntegracion.lblCantidadFacturas.setText("" + fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto().size());

    }

    public void grabarSalidasDomicilios() throws HeadlessException {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

        this.fDespachoHieleraConIntegracion.btnImprimir.setEnabled(false);
        this.fDespachoHieleraConIntegracion.jBtnImprimir.setEnabled(false);

        this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        this.fDespachoHieleraConIntegracion.repaint();

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto_2.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
        this.fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
        this.fDespachoHieleraConIntegracion.jBtnObservaciones.setEnabled(false);

        //this.fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(false);
        //this.fDespachoHieleraConIntegracion.pnlAgregarLista.setEnabled(false);
        this.fDespachoHieleraConIntegracion.estaOcupadoGrabando = true;
        double valorManifiesto = 0.0;
        double pesoKgManifiesto = 0.0;

        /* Se cambia el estado de manifiesto a manifiesto cerrado*/
        try {
            /* Se graba las facturas descargadas, prductos por factura descargados, manifiesto  se actualiza */
            if (this.fDespachoHieleraConIntegracion.manifiestoActual.grabarFacturasPorManifiesto()) {
                this.fDespachoHieleraConIntegracion.fManifestarPedidosHielera_2.jBtnNuevo.setEnabled(true);

                new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 3, true)).start();

                this.fDespachoHieleraConIntegracion.llenarJtableFacturasXmanifiesto();

                this.fDespachoHieleraConIntegracion.cantDeSalidas++;

                //fDespachoHielera.manifiestoActual.setEstadoManifiesto(3);
                // fDespachoHieleraConIntegracion.manifiestoActual.setIsFree(1);
                fDespachoHieleraConIntegracion.manifiestoActual.setCantDeSalidas(this.fDespachoHieleraConIntegracion.cantDeSalidas);
                // fDespachoHieleraConIntegracion.manifiestoActual.actualizarManifiestoDeDistribucions();

                // fDespachoHieleraConIntegracion.manifiestoActual.setHoraDeDespacho("" + new Date());
                // fDespachoHieleraConIntegracion.manifiestoActual.setPesoKgManifiesto(fDespachoHieleraConIntegracion.sumadorPesosFacturas);
                this.fDespachoHieleraConIntegracion.estaOcupadoGrabando = false;
                this.fDespachoHieleraConIntegracion.btnImprimir.setEnabled(true);
                this.fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
                this.fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);

                this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                //JOptionPane.showMessageDialog(this.fDespachoHieleraConIntegracion, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                this.fDespachoHieleraConIntegracion.jBtnMinuta.setEnabled(true);

                this.fDespachoHieleraConIntegracion.btnImprimir.requestFocus();

                /* si presenta algun error en grabar el manifiesto  */
            } else {

                this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                this.fDespachoHieleraConIntegracion.btnGrabar.setEnabled(false);
                this.fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);

                JOptionPane.showMessageDialog(this.fDespachoHieleraConIntegracion, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this.fDespachoHieleraConIntegracion, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            this.fDespachoHieleraConIntegracion.jBtnGrabar.setEnabled(false);
            this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

        } catch (Exception ex) {
            Logger.getLogger(HiloGuardarFacturasPorManifiesto_2.class.getName()).log(Level.SEVERE, null, ex);
            this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

        }

        this.fDespachoHieleraConIntegracion.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        this.fDespachoHieleraConIntegracion.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
    }

}
