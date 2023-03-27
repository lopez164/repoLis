/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera.Threads;

import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloGuardarManifiestoDescargado;
import aplicacionlogistica.distribucion.Threads.HiloGuardarManifiestoDescargado100;
import aplicacionlogistica.distribucion.Threads.HiloGuardarSenteciasSql;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.formularios.FDescargarEntregasTotalesConDescuento;
import aplicacionlogistica.distribucion.formularios.FDescargarRechazosParciales;
import aplicacionlogistica.distribucion.formularios.Hielera.FliquidarManifiestos;
import aplicacionlogistica.distribucion.formularios.Hielera.Imprimir.ReporteDescargueDeFacturasHielera;
import aplicacionlogistica.distribucion.objetos.CCausalesDeDevolucion;
import aplicacionlogistica.distribucion.objetos.CCuentasBancarias;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasDescargadas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import aplicacionlogistica.distribucion.objetos.CRecogidasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CSoportesConsignaciones;
import com.mxrck.autocompleter.TextAutoCompleter;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloFLiquidarManifiestos implements Runnable {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    FliquidarManifiestos fliquidarManifiestos = null;
    String caso;

    String senteciaSqlFacturasDescargadas = null;
    String senteciaSqlProductosPorFacturaDescargados = null;
    String senteciaSqlSoportesConsignaciones = null;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFLiquidarManifiestos(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fliquidarManifiestos
     * @param comando
     */
    public HiloFLiquidarManifiestos(Inicio ini, FliquidarManifiestos fliquidarManifiestos, String comando) {
        this.ini = ini;
        this.fliquidarManifiestos = fliquidarManifiestos;
        this.caso = comando;
    }

    @Override
    public void run() {
        try {
            if (caso != null) {
                switch (caso) {
                    case "descargarFactura":
                        descargarFactura();
                        break;
                    case "llenarjTableFacturasPorVehiculo":
                        asignarTipoDescargue();
                        llenarjTableFacturasDescargadasPorVehiculo();

                        break;
                    case "buscarManifiesto":

                        break;
                    case "salir":
                        salir();
                        break;
                    case "nuevo":
                        nuevo();
                        break;
                    case "cerrarManifiesto":
                        cerrarManifiesto();
                        break;
                    case "cancelar":
                        cancelar();
                        break;
                    case "agregarLafactura":
                        //consultarLafactura();
                        descargarFactura();
                        break;
                    case "imprimir":
                        imprimir();
                        break;
                    case "imprimirConManifiestosAbiertos":
                        imprimirConManifiestosAbiertos();
                        break;
                    case "gestionarRecogida":
                        gestionarRecogida();
                        break;
                    case "gestionarRechazoTotal":
                        descargarRechazosTotales();
                        break;
                    case "descargarRechazosParciales":
                        descargarRechazosParciales();
                        break;
                    case "borrarTodasLasFacturas":
                        borrarTodasLasFacturas();
                        break;
                    case "borrarUnaFactura":
                        borrarUnaFactura();
                        break;
                    case "getionarSoporteConsignacion":
                        getionarSoporteConsignacion();
                        break;
                    case "grabarCien100":
                        grabarCien100();
                        break;

                    default:
                        JOptionPane.showInternalMessageDialog(this.fliquidarManifiestos, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloFLiquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void descargarFactura() {
        try {
            /* Evento al oprimir la tecla enter*/

            fliquidarManifiestos.btnGrabar100.setEnabled(false);
            fliquidarManifiestos.jBtnGrabar100.setEnabled(false);
            fliquidarManifiestos.facturaActual = null;

            String numeroDeFactura;
            numeroDeFactura = fliquidarManifiestos.txtNumeroDeFactura.getText().trim().replace("POSC", "PO");

//            if (fliquidarManifiestos.rBtnContado.isSelected()) {
//                fliquidarManifiestos.isFacturaCredito = false;
//            } else {
//                fliquidarManifiestos.isFacturaCredito = true;
//            }
            fliquidarManifiestos.jTabbedPane1.setEnabled(true);
            fliquidarManifiestos.txtNumeroDeFactura.setSelectionStart(0);
            fliquidarManifiestos.txtNumeroDeFactura.setSelectionEnd(fliquidarManifiestos.txtNumeroDeFactura.getText().length());
            try {

                limpiarTblProductosPorFactura();

                // SE CREA EL OBJETO FACTURA
                for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasPorManifiesto()) {
                    if (obj.getNumeroFactura().equals(numeroDeFactura)) {

                        /*Encuentra la factura en el listado del manifiesto*/
                        fliquidarManifiestos.facturaActual = obj;
                        fliquidarManifiestos.facturaActual.setListaProductosPorFactura(false);
                        break;
                    }
                }

                // SE VALIDA QUE LA FACTURA EXISTA EN EL MANIFIESTO
                if (fliquidarManifiestos.facturaActual != null) {

                    if (!fliquidarManifiestos.facturaActual.getNumeroDescuento().equals("0")) {
                        fliquidarManifiestos.tipoMovimientoFactura = 2;
                        fliquidarManifiestos.chkConDescuento.setSelected(true);
                    }

                    /*consulta local de los de los productos de la factura para llenar
                        la tabla de los productos de la factura*/
                    if (fliquidarManifiestos.facturaActual.getListaProductosPorFactura().size() > 0) {

                        // SE LLENA LA TABLA DE PRODUCTOS CONTENIDOS EN LA FACTURA
                        llenarTablaProductosPorFactura();

                        // SE LLENA LAS RESPECTIVAS ETIQUETAS
                        fliquidarManifiestos.lblValorFactura.setText(nf.format(fliquidarManifiestos.facturaActual.getValorTotalFactura()));

                        // SE VALIDA QUE SE HAYA ELEGIDO UN MOVIMIENTO PARA LA FACTURA
                        //if (!"NINGUNO".equals(fliquidarManifiestos.cbxMovimientoFactura.getSelectedItem().toString())) {
                        if (fliquidarManifiestos.cbxMovimientoFactura.getSelectedIndex() > 0) {

                            switch (fliquidarManifiestos.tipoMovimientoFactura) {
                                case 1: // caso no utilizado, pues identifica un movimiento no valido
                                    /* NINGUNO */
                                    break;

                                case 2:// DESCARGUE DE TIPO DE MOVIMIENTO ENTREGA TOTAL

                                    opcioEntregaTotal();
                                    fliquidarManifiestos.btnGrabar100.setEnabled(true);
                                    fliquidarManifiestos.jBtnGrabar100.setEnabled(true);

                                    break;

                                // DESCARGUE DE RECHAZO TOTAL
                                case 3:
                                    fliquidarManifiestos.facturaActual.setListaProductosPorFactura(true);
                                    fliquidarManifiestos.facturaActual.setIdTipoDeMovimiento(3);
//                                    if (!estaDescargadaLaFactura()) {
//
//                                        fliquidarManifiestos.chkConDescuento.setEnabled(false);
//                                        fliquidarManifiestos.chkConDescuento.setSelected(false);
//
//                                    }

                                    break;

                                case 4: // entrega parcial
                                    opcionDevolucionParcial();

                                    break;

                                case 5: //  DESCARGUE DE RECOGIDAS
                                    fliquidarManifiestos.facturaActual.setIdTipoDeMovimiento(5);

                                    if (!estaDescargadaRecogida()) {

                                        fliquidarManifiestos.chkConDescuento.setEnabled(false);
                                        fliquidarManifiestos.jTabbedPane1.setEnabledAt(fliquidarManifiestos.jTabbedPane1.indexOfComponent(fliquidarManifiestos.pnlRecogidas), true);
                                        fliquidarManifiestos.jTabbedPane1.setSelectedIndex(fliquidarManifiestos.jTabbedPane1.indexOfComponent(fliquidarManifiestos.pnlRecogidas));

                                        fliquidarManifiestos.txtNumeroDeSoporteRecogida.setEditable(true);
                                        fliquidarManifiestos.txtFacturaAfectada.setEditable(true);
                                        fliquidarManifiestos.txtValorRecogida.setEditable(true);
                                        fliquidarManifiestos.txtValorDescontado.setEditable(true);

                                        fliquidarManifiestos.txtNumeroDeSoporteRecogida.setEnabled(true);
                                        fliquidarManifiestos.txtFacturaAfectada.setEnabled(true);
                                        fliquidarManifiestos.txtValorRecogida.setEnabled(true);
                                        fliquidarManifiestos.txtValorDescontado.setEnabled(true);

                                        fliquidarManifiestos.txtNumeroDeSoporteRecogida.requestFocus();
                                    } else {
                                        //fliquidarManifiestos.descargarFacturas_2.msgRecogidaYaHecha(fliquidarManifiestos.manifiesto.codificarManifiesto());
                                        //JOptionPane.showInternalMessageDialog(this, "La Recogida aparece descargada del Mfto. N° " + manifiesto.codificarManifiesto(), "Error", 0);
                                    }

                                    break;

                                //  RE ENVIOS
                                case 6:
                                    fliquidarManifiestos.facturaActual.setIdTipoDeMovimiento(6);
                                    // SE VALIDA SI LA FACTURA NO ESTA DESCARGADA Y SE DESCARGA LA ENTREGA TOTAL

                                    if (!estaDescargadaLaFactura()) {
                                        //       facturaActual.liberarFactura(false);

                                        descargarReEnvios();

                                        fliquidarManifiestos.txtNumeroDeFactura.requestFocus();

                                    }
                                    break;

                                // NO VISITADOS
                                case 7:
                                    fliquidarManifiestos.facturaActual.setIdTipoDeMovimiento(7);
                                    // SE VALIDA SI LA FACTURA NO ESTA DESCARGADA Y SE DESCARGA LA ENTREGA TOTAL

                                    if (!estaDescargadaLaFactura()) {
//                                            facturaTemporal.liberarFactura(false);

                                        descargarNoVisitados();
                                        fliquidarManifiestos.txtNumeroDeFactura.requestFocus();

                                    }
                                    break;

                            }

                            fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));

                            //   } /* Fin if valida si las (cantidadFacturasPendientesPorDescargar > 0) */
                        } else {
                            fliquidarManifiestos.descargarFacturas_2.msgSeleccionarMovimiento();
                            //JOptionPane.showInternalMessageDialog(this, "Debe escoger un movimiento para la factura digitada ", "Error", 0);
                            //txtNumeroDeFactura.requestFocus();
                        }
                        //txtNumeroDeFactura.requestFocus();
                    } else {
                        fliquidarManifiestos.descargarFacturas_2.msgFacturasSinProductos();

                        // JOptionPane.showInternalMessageDialog(this, "La factura no tiene productos asignados", "Error", JOptionPane.ERROR_MESSAGE);
                        // txtNumeroDeFactura.requestFocus();
                    }
                    /* Cuando la factura no existe en el manifiesto */
                } else {
                    if (fliquidarManifiestos.listaDeFacturasNoManifestadas == null) {
                        fliquidarManifiestos.listaDeFacturasNoManifestadas = new ArrayList<>();
                    }
                    /*Se valida que el dato no exista en el Jlist, ni en el archivo*/
                    if (!verificarQueFacturaExiste()) {
                        fliquidarManifiestos.listaDeFacturasNoManifestadas.add(numeroDeFactura);

                        /*Agrega el numero de factura al objeto JList*/
                        anexarFacturaActualALalista();

                        /*Agrega la factura al fichero*/
                        ArchivosDeTexto archivo = new ArchivosDeTexto();
                        //archivo.insertarLineaEnFichero(numeroDeFactura, fliquidarManifiestos.manifiesto.getRutArchivoFacturasSinManifestar());
                        //fliquidarManifiestos.descargarFacturas_2.msgFacturaNoExiste(fliquidarManifiestos.manifiesto.fliquidarManifiestos.codificarManifiesto());
                        //JOptionPane.showInternalMessageDialog(this, "La factura No existe en el manifiesto N° " + manifiesto.codificarManifiesto(), "Factura no existe en el sistema", JOptionPane.ERROR_MESSAGE);                //txtNumeroDeFactura.requestFocus();
                    }

                }
            } catch (Exception ex) {
                System.out.println("Error en txtNumeroDeFacturaKeyPressed ");
                Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
                //txtNumeroDeFactura.requestFocus();
            }

            fliquidarManifiestos.txtNumeroDeFactura.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llenarTablaProductosPorFactura() {
        DefaultTableModel modelo1 = (DefaultTableModel) fliquidarManifiestos.jTableProductosPorFactura.getModel();

        // se nexan los productos a la tabla de productos por factura
        for (CProductosPorFactura obj : fliquidarManifiestos.facturaActual.getListaProductosPorFactura()) {

            int fila = fliquidarManifiestos.jTableProductosPorFactura.getRowCount();

            modelo1.addRow(new Object[fliquidarManifiestos.jTableProductosPorFactura.getRowCount()]);
            fliquidarManifiestos.jTableProductosPorFactura.setValueAt("" + (fila + 1), fila, 0);
            fliquidarManifiestos.jTableProductosPorFactura.setValueAt(obj.getCodigoProducto(), fila, 1);
            fliquidarManifiestos.jTableProductosPorFactura.setValueAt(obj.getDescripcionProducto(), fila, 2);
            fliquidarManifiestos.jTableProductosPorFactura.setValueAt(obj.getCantidad(), fila, 3);
            fliquidarManifiestos.jTableProductosPorFactura.setValueAt(nf.format(obj.getValorUnitarioConIva()), fila, 4);
            fliquidarManifiestos.jTableProductosPorFactura.setValueAt(nf.format(obj.getValorProductoXCantidad()), fila, 5);

        }

    }

    private void limpiar() {

        fliquidarManifiestos.manifiesto = null;
        fliquidarManifiestos.carro = null;
        fliquidarManifiestos.facturaActual = null;
        fliquidarManifiestos.valorTotalRecaudado = 0.0;
        fliquidarManifiestos.valorTotalRecogidas = 0.0;
        fliquidarManifiestos.valorTotalAConsignar = 0.0;
        fliquidarManifiestos.valorRecogida = 0.0;;
        fliquidarManifiestos.valorDescontado = 0.0;
        fliquidarManifiestos.valorConsignado = 0.0;
        fliquidarManifiestos.isFormularioCompleto = false;
        fliquidarManifiestos.cargado = false;
        fliquidarManifiestos.nuevo = false;
        fliquidarManifiestos.actualizar = false;
        fliquidarManifiestos.liberado = false;
        fliquidarManifiestos.grabar = false;
        fliquidarManifiestos.estaOcupadoGrabando = false;
        fliquidarManifiestos.isFacturaCredito = false;
        fliquidarManifiestos.esteManifiestoEsMio = false;
        fliquidarManifiestos.kilometrosRecorridos = 0;
        fliquidarManifiestos.filaSeleccionada = 0;
        fliquidarManifiestos.indiceLista = 0;
        fliquidarManifiestos.columna = 0;
        fliquidarManifiestos.mensaje = null;
        fliquidarManifiestos.causalrechazo = 0;
        //fliquidarManifiestos.fliquidarManifiestos.rutaDeArchivoFacturasDescargadas = null;
        //fliquidarManifiestos.rutaDeArchivoProductosDescargados = null;
        //fliquidarManifiestos.rutaDeArchivoRecogidasDescargado = null;
        //fliquidarManifiestos.rutaDeArchivoFacturasParaVolverAZonificar = null;
        //fliquidarManifiestos.rutaDeArchivoSoporteConsignaciones = null;
        fliquidarManifiestos.tipoMovimientoFactura = 0;
        fliquidarManifiestos.listaDeFacturasNoManifestadas = null;
        senteciaSqlFacturasDescargadas = null;
        senteciaSqlProductosPorFacturaDescargados = null;
        senteciaSqlSoportesConsignaciones = null;
        fliquidarManifiestos.modelo1 = null;
        fliquidarManifiestos.modelo2 = null;
        fliquidarManifiestos.modelo3 = null;

        fliquidarManifiestos.txtNombreConductor.setEditable(false);
//        fliquidarManifiestos.txtKilometrosEntrada.setEnabled(false);
//        fliquidarManifiestos.txtKilometrosEntrada.setEditable(false);

        //fliquidarManifiestos.cbxMovimientoFactura.setEnabled(false);
        fliquidarManifiestos.cbxMovimientoFactura.setSelectedItem("NINGUNO");
        fliquidarManifiestos.cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
        fliquidarManifiestos.cbxEntidadesBancarias.setEnabled(false);
        fliquidarManifiestos.cbxNumeroDeCuenta.setEnabled(false);
        fliquidarManifiestos.chkConDescuento.setEnabled(false);

        fliquidarManifiestos.txtNombreConductor.setEnabled(false);
        fliquidarManifiestos.cbxPrefijo.setEnabled(false);
        fliquidarManifiestos.txtNumeroDeFactura.setEnabled(false);
        fliquidarManifiestos.txtNumeroDeFactura.setEnabled(false);
        //       fliquidarManifiestos.txtKilometrosEntrada.setEnabled(false);
        //       fliquidarManifiestos.txtKilometrosSalida.setEnabled(false);
        fliquidarManifiestos.txtNombreConductor.setEnabled(false);
        //      fliquidarManifiestos.txtPlaca.setEnabled(false);
        fliquidarManifiestos.txtNumeroSoporteConsignacion.setEnabled(false);
        fliquidarManifiestos.txtMedioDePago.setEnabled(false);
        fliquidarManifiestos.txtValorConsignacion.setEnabled(false);
        fliquidarManifiestos.dateOperacion.setEnabled(false);

        fliquidarManifiestos.txtNombreConductor.setText("");
        fliquidarManifiestos.txtNumeroDeFactura.setText("");
        fliquidarManifiestos.cbxPrefijo.setEnabled(false);
        fliquidarManifiestos.txtNumeroDeFactura.setEnabled(false);
        fliquidarManifiestos.txtNumeroDeFactura.setEditable(false);
        //    fliquidarManifiestos.txtKilometrosEntrada.setText("");
        //    fliquidarManifiestos.txtKilometrosSalida.setText("");
        //    fliquidarManifiestos.lblKilometrosRecorridos.setText("");
        fliquidarManifiestos.txtNombreConductor.setText("");
        //   fliquidarManifiestos.txtPlaca.setText("");
        fliquidarManifiestos.txtNumeroSoporteConsignacion.setText("");
        fliquidarManifiestos.txtMedioDePago.setText("");
        fliquidarManifiestos.txtValorConsignacion.setText("");

        fliquidarManifiestos.lblIdentificadorManifiesto.setText("Total a recaudar en Manifiesto # ");
        fliquidarManifiestos.valorTotalRecaudado = 0.0;
        fliquidarManifiestos.chkConDescuento.setEnabled(false);
        fliquidarManifiestos.chkConDescuento.setSelected(false);
        fliquidarManifiestos.rBtnContado.setEnabled(false);
        fliquidarManifiestos.rBtnCredito.setEnabled(false);

        fliquidarManifiestos.rBtnContado.setSelected(true);

        fliquidarManifiestos.cbxCausalDeRechazoFactura.setEnabled(false);

        /* elimina lo registros de las tablas de la vista */
        limpiarTablas();


        /* Elimina los datos de los arrays*/
        limpiarArrays();

        fliquidarManifiestos.valorTotalRecogidas = 0.0;
        fliquidarManifiestos.valorTotalAConsignar = 0.0;
        fliquidarManifiestos.valorTotalRecaudado = 0.0;
        fliquidarManifiestos.valorConsignado = 0.0;
        fliquidarManifiestos.mensaje = "";

        fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));
        fliquidarManifiestos.lblValorFactura.setText("$ 0.0");
        fliquidarManifiestos.lblValorConsignado.setText("$ 0.0");
        //numeroDeFacturasEnManifiesto = 0;

        //cantidadFacturasDescargadasManifiesto = 0;
        fliquidarManifiestos.lblFacturasPendientes.setText("0");
        //manifiestoActual = null;
        fliquidarManifiestos.facturaActual = null;

        fliquidarManifiestos.esteManifiestoEsMio = false;

        eliminarDatosDejListFacturasNoManifestadas();
        fliquidarManifiestos.dateOperacion.setEnabled(false);
        fliquidarManifiestos.jBtnConsignaciones.setEnabled(false);
        fliquidarManifiestos.btnGrabar.setEnabled(false);
        fliquidarManifiestos.jBtnGrabar.setEnabled(false);

        fliquidarManifiestos.btnNuevo.setEnabled(true);
        fliquidarManifiestos.jBtnNuevo.setEnabled(true);

        fliquidarManifiestos.btnImprimir.setEnabled(false);
        fliquidarManifiestos.jBtnImprimir.setEnabled(false);

        fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N

    }

    private void limpiarArrays() {

        if (fliquidarManifiestos.manifiesto != null) {
            /*Se vacían las listas respectivas */
            //listaFacturasPorManifiesto = new ArrayList();
            //listaFacturasDescargadas = new ArrayList();
            fliquidarManifiestos.manifiesto.setListaFacturasPendientesPorDescargar(null);
            fliquidarManifiestos.manifiesto.setListaFacturasDescargadas();
            //listaDeRecogidasPorManifiesto = new ArrayList();
            fliquidarManifiestos.listaDeFacturasNoManifestadas = new ArrayList();
            //listaDeSoportesDeConsignaciones = new ArrayList();
        }

    }

    public void limpiarTablas() {
        DefaultTableModel modelo = (DefaultTableModel) fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        modelo = (DefaultTableModel) fliquidarManifiestos.jTableProductosPorFactura.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        modelo = (DefaultTableModel) fliquidarManifiestos.jTableRecogidas.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        modelo = (DefaultTableModel) fliquidarManifiestos.jTableListaDeConsignaciones.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
    }

    public void limpiarTblProductosPorFactura() {

        DefaultTableModel modelo = (DefaultTableModel) fliquidarManifiestos.jTableProductosPorFactura.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    private void ordenartblFacturasPorVehiculo() {
        for (int i = 0; i < fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount(); i++) {
            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(i + 1, i, 0);
        }
    }

    public boolean estaDescargadaLaFactura() {
        boolean descargada = false;
        if (fliquidarManifiestos.manifiesto.getListaFacturasDescargadas() != null) {
            for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasDescargadas()) {
                if (fliquidarManifiestos.facturaActual.getNumeroFactura().equals(obj.getNumeroFactura())) {
                    descargada = true;
                    break;
                }
            }
        }

        return descargada;
    }

    public boolean estaDescargadaRecogida() {
        boolean descargada = false;
//        for (CRecogidasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaDeRecogidasPorManifiesto()) {
//            if (fliquidarManifiestos.facturaActual.getNumeroFactura().equals(obj.getNumeroFactura())) {
//                descargada = true;
//                break;
//            }
//        }

        return descargada;
    }

    public void anexarFacturaActualALalista() {

        final DefaultListModel model = new DefaultListModel();
        for (String obj : fliquidarManifiestos.listaDeFacturasNoManifestadas) {

            model.addElement(obj);
        }
        fliquidarManifiestos.jListFacturasNoManifestadas.setModel(model);

    }

    public boolean verificarQueFacturaExiste() {
        boolean siExiste = false;
        for (String valor : fliquidarManifiestos.listaDeFacturasNoManifestadas) {
            if (valor.equals(fliquidarManifiestos.txtNumeroDeFactura.getText().trim())) {
                siExiste = true;
                break;
            }
        }
        return siExiste;
    }

    public boolean validarSoporte(CSoportesConsignaciones soporte) {
        boolean siExiste = false;
//        if (fliquidarManifiestos.manifiesto.getListaDeSoportesConsignaciones() != null) {
//            for (CSoportesConsignaciones obj : fliquidarManifiestos.manifiesto.getListaDeSoportesConsignaciones()) {
//                if (obj.getNumeroSoporte().equals(fliquidarManifiestos.txtNumeroSoporteConsignacion.getText().trim())) {
//                    siExiste = true;
//                    return true;
//                }
//            }
//        }

        //       fliquidarManifiestos.mensaje = soporte.validarConsignacion();
        return siExiste;
    }

    public void eliminarDatosDejListFacturasNoManifestadas() {

        DefaultListModel model = new DefaultListModel();
        fliquidarManifiestos.jListFacturasNoManifestadas.setModel(model);
        //See more at: http://collectioncode.com/java/limpiar-jlist-en-java/#sthash.v1nd3O4o.dpuf

    }

    public void llenarDatosVista() {

        fliquidarManifiestos.txtNombreConductor.setEnabled(true);
        fliquidarManifiestos.cbxPrefijo.setEnabled(false);
        fliquidarManifiestos.txtNumeroDeFactura.setEnabled(false);
        fliquidarManifiestos.txtNumeroDeFactura.setEnabled(false);
//        fliquidarManifiestos.txtKilometrosEntrada.setEnabled(true);
        //       fliquidarManifiestos.txtKilometrosSalida.setEnabled(true);
        fliquidarManifiestos.txtNombreConductor.setEnabled(true);
        //       fliquidarManifiestos.txtPlaca.setEnabled(true);

        fliquidarManifiestos.txtNombreConductor.setEditable(false);
        fliquidarManifiestos.txtNumeroDeFactura.setEditable(false);
        //       fliquidarManifiestos.txtKilometrosEntrada.setEditable(false);
//        fliquidarManifiestos.txtKilometrosSalida.setEditable(false);
        fliquidarManifiestos.txtNombreConductor.setEditable(false);
//        fliquidarManifiestos.txtPlaca.setEditable(false);

        fliquidarManifiestos.valorTotalAConsignar = fliquidarManifiestos.valorTotalRecaudado - fliquidarManifiestos.valorTotalRecogidas;

        fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));

        // fliquidarManifiestos.txtKilometrosEntrada.setText("" + fliquidarManifiestos.manifiesto.getKmEntrada());
        // fliquidarManifiestos.txtKilometrosSalida.setText("" + fliquidarManifiestos.manifiesto.getKmSalida());
        // fliquidarManifiestos.kilometrosRecorridos = fliquidarManifiestos.manifiesto.getKmEntrada() - fliquidarManifiestos.manifiesto.getKmSalida();
//        fliquidarManifiestos.lblKilometrosRecorridos.setText("" + fliquidarManifiestos.kilometrosRecorridos);
        fliquidarManifiestos.txtNumeroDeFactura.setEditable(true);

        // fliquidarManifiestos.lblIdentificadorManifiesto.setText("Total a recaudar en Manifiesto # " + manifiesto.codificarManifiesto());
        //if (fliquidarManifiestos.manifiesto.getEstadoManifiesto() == 4) {
        if (false) {

            try {
                /*Se llenan las jtables respectivas */
                llenarjTableFacturasDescargadasPorVehiculo();

                //               fliquidarManifiestos.lblCirculoDeProgreso.setVisible(false);
                fliquidarManifiestos.lblFacturasPendientes.setText(" " + 0);
                /*Se habilitan las pestañas para visualizar las recogidas */
                fliquidarManifiestos.jTabbedPane1.setEnabled(true);
                fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));

                // txtManifiesto.setEnabled(false);
                // txtKilometrosEntrada.setEnabled(false);
                //txtNumeroDeFactura.setEnabled(false);
                fliquidarManifiestos.txtNombreConductor.setText(fliquidarManifiestos.manifiesto.getNombreConductor() + " " + fliquidarManifiestos.manifiesto.getApellidosConductor());
                //fliquidarManifiestos.txtPlaca.setText(manifiesto.getNombreDeRuta());
                fliquidarManifiestos.cbxMovimientoFactura.setEnabled(true);
                fliquidarManifiestos.cbxPrefijo.setEnabled(false);
                fliquidarManifiestos.txtNumeroDeFactura.setEnabled(false);
                fliquidarManifiestos.chkConDescuento.setEnabled(false);
                fliquidarManifiestos.chkConDescuento.setSelected(false);
                fliquidarManifiestos.chkConDescuento.setSelected(false);
                fliquidarManifiestos.rBtnContado.setEnabled(false);
                fliquidarManifiestos.rBtnCredito.setEnabled(false);
                fliquidarManifiestos.cbxCausalDeRechazoFactura.setEnabled(false);
                fliquidarManifiestos.btnDescargarRechazoTotal.setEnabled(false);
                fliquidarManifiestos.btnGrabar.setEnabled(false);
                fliquidarManifiestos.jBtnGrabar.setEnabled(false);
                fliquidarManifiestos.jBtnConsignaciones.setEnabled(false);
                fliquidarManifiestos.dateOperacion.setEnabled(false);
                fliquidarManifiestos.jBtnMinuta.setEnabled(true);
                fliquidarManifiestos.btnImprimir.setEnabled(true);

                fliquidarManifiestos.btnImprimir.requestFocus();

                fliquidarManifiestos.descargarFacturas_2.msgManifiestoDescargado();
                // JOptionPane.showInternalMessageDialog(this, "Manifiesto descargado y completo", "Manifiesto completo", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //if (fliquidarManifiestos.manifiesto.getEstadoManifiesto() == 3) {
        if (false) {
//            fliquidarManifiestos.lblCirculoDeProgreso.setVisible(false);
            fliquidarManifiestos.txtNombreConductor.setText(fliquidarManifiestos.manifiesto.getNombreConductor() + " " + fliquidarManifiestos.manifiesto.getApellidosConductor());
            //fliquidarManifiestos.txtPlaca.setText(fliquidarManifiestos.manifiesto.getNombreDeRuta());
            fliquidarManifiestos.jTabbedPane1.setEnabled(true);

            if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().isEmpty()) {
                fliquidarManifiestos.cbxMovimientoFactura.setEnabled(true);
                fliquidarManifiestos.cbxPrefijo.setEnabled(true);
//                fliquidarManifiestos.txtKilometrosEntrada.setEnabled(true);
                fliquidarManifiestos.txtNumeroDeFactura.setEnabled(true);
                fliquidarManifiestos.chkConDescuento.setEnabled(true);
                fliquidarManifiestos.rBtnContado.setEnabled(true);
                fliquidarManifiestos.rBtnCredito.setEnabled(true);
                fliquidarManifiestos.descargarFacturas_2.msgManifiestoDescargado();
                //JOptionPane.showInternalMessageDialog(this, "Manifiesto descargado y completo", "Manifiesto completo", JOptionPane.INFORMATION_MESSAGE);
                //fliquidarManifiestos.btnGrabar.setEnabled(true);
                // fliquidarManifiestos.jBtnGrabar.setEnabled(true);

                fliquidarManifiestos.lblFacturasPendientes.setText("" + fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size());

                fliquidarManifiestos.btnGrabar.requestFocus();
            } else {
//                fliquidarManifiestos.txtKilometrosEntrada.setEditable(true);
                fliquidarManifiestos.btnGrabar.setEnabled(false);
                fliquidarManifiestos.jBtnGrabar.setEnabled(false);

                fliquidarManifiestos.btnImprimir.setEnabled(false);
                fliquidarManifiestos.cbxMovimientoFactura.setEnabled(true);
                fliquidarManifiestos.chkConDescuento.setEnabled(false);
                fliquidarManifiestos.chkConDescuento.setSelected(false);
                fliquidarManifiestos.rBtnContado.setEnabled(false);
                fliquidarManifiestos.rBtnCredito.setEnabled(false);
                fliquidarManifiestos.chkConDescuento.setEnabled(false);
                fliquidarManifiestos.rBtnContado.setEnabled(true);
                fliquidarManifiestos.rBtnCredito.setEnabled(true);
                fliquidarManifiestos.lblFacturasPendientes.setText("" + fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size());

                //               fliquidarManifiestos.txtKilometrosEntrada.requestFocus();
                //               fliquidarManifiestos.txtKilometrosEntrada.requestFocus();
            }

        }

    }

//    public void eliminararchivos() {
//        /* Se prrocede a borrar el archivo temporal de las facturas descargadas*/
//        //String rutaDeArchivoFacturas = "tmp/tmp_" + this.fCambiarClave.manifiesto.codificarManifiesto() + ".txt";
//        ArchivosDeTexto archivo = new ArchivosDeTexto(fliquidarManifiestos.manifiesto.getRutaArchivoDescargueFacturas());
//        archivo.borrarArchivo();
//
//        /* Se prrocede a borrar el archivo temporal de los productos por facturas descargadas*/
//        //String rutaDeArchivoProductosDescargados = "tmp/tmp_" + this.fCambiarClave.manifiesto.codificarManifiesto() + "_productosDescargados.txt";
//        archivo = new ArchivosDeTexto(fliquidarManifiestos.manifiesto.getRutaArchivoDescargueporductosPorFactura());
//        archivo.borrarArchivo();
//
//        /* Se prrocede a borrar el archivo temporal de las Recogidas*/
//        //String rutaDeArchivoRecogidas = "tmp/tmp_" + this.fCambiarClave.manifiesto.codificarManifiesto() + "_productosDescargados.txt";
//        archivo = new ArchivosDeTexto(fliquidarManifiestos.manifiesto.getRutArchivoRecogidasporManifiesto());
//        archivo.borrarArchivo();
//
//        /* Se prrocede a borrar el archivo temporal de las facturas no manifestadas */
//        archivo = new ArchivosDeTexto(fliquidarManifiestos.manifiesto.getRutArchivoFacturasSinManifestar());
//        archivo.borrarArchivo();
//
//        /* Se prrocede a borrar el archivo temporal de las facturas no manifestadas */
//        archivo = new ArchivosDeTexto(fliquidarManifiestos.manifiesto.getRutaArchivoSoportesDeConsignaciones());
//        archivo.borrarArchivo();
//    }
    public void asignarTipoDescargue() throws InterruptedException {

        if (fliquidarManifiestos.manifiesto.getListaFacturasDescargadas() == null) {
            return;
        }

        for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasDescargadas()) {

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

                default:
                    JOptionPane.showInternalMessageDialog(fliquidarManifiestos, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

            }

            Thread.sleep(10);
        }
    }

    public void salir() {
//        if (manifiesto != null) {
//
//            if (esteManifiestoEsMio) {
//                new Thread(new HiloLiberarManifiesto(manifiesto, true)).start();//-->Error v 
//                //manifiestoActual.liberarManifiesto(true);
//                //manifiestoActual = null;
//                esteManifiestoEsMio = false;
//                limpiar();
//            }
//        }
//
//        btnImprimir.setEnabled(false);
//        cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
//        cbxMovimientoFactura.setSelectedItem("NINGUNO");
//        this.dispose();
        fliquidarManifiestos.setVisible(false);
    }

    private boolean borrarUnaFactura() throws HeadlessException {
        boolean borrado = false;
//        int borrarFila;
//
//        /*Si el manifiesto ya fue descargado aborta operacion*/
//        if (manifiesto.getEstadoManifiesto() == 4) {
//            return true;
//        }
//
//        // VLIDA SI HAY ELEMENTOS EN EL JTABLE, SI TIENE MAS DE UN ELEMENTO
//        if (jTableFacturasDescargadasPorVehiculo.getRowCount() > 0) {
//           fliquidarManifiestos. modelo2 = (DefaultTableModel) jTableFacturasDescargadasPorVehiculo.getModel();
//
//            /* se identifica la fila seleccionada de la tabla*/
//            filaSeleccionada = jTableFacturasDescargadasPorVehiculo.getSelectedRow();
//
//            /*Se identifica el numero de la factura contenida en la fila seleccionada*/
//            String numFactura =fliquidarManifiestos. modelo2.getValueAt(filaSeleccionada, 1).toString();
//
//            // borrarFila = JOptionPane.showOptionDialog(this, "Desea eliminar el Registro de la Tabla?", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No"}, "No");
//            //borrarFila = JOptionPane.showConfirmDialog(this, "Desea eliminar la factura # " + numFactura + " de la Tabla ?", "Eliminar Fcatura # " + numFactura, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//            borrarFila = descargarFacturas_2.msgBorrarFila(numFactura);
//            // HA SELECCIONADO BORRAR LA FILA
//            if (borrarFila == JOptionPane.YES_OPTION) {
//
//                try {
//
//                    /*Borra los datos del archivo almacenado en el disco */
//                    if (eliminarFacturaDelArchivo(numFactura)) {
//
//                        /*Actualiza los valores de la factura sin descargar*/
//                        for (CFacturasPorManifiesto fact : manifiesto.getListaFacturasPorManifiesto()) {
//
//                            if (fact.getNumeroFactura().equals(numFactura)) {
//
//                                /*Elimina el objeto al array de facturas descargadas*/
//                                fact.setCausalDeRechazo(1);
//                                fact.setIdMovimientoFactura(1);
//                                fact.setIdTipoDeMovimiento(1);
//                                fact.setAdherenciaDescargue(0);
//                                fact.setNumeroDescuento("0");
//                                fact.setNumeroRecogida("0");
//                                fact.setValorARecaudarFactura(fact.getValorTotalFactura());
//                                fact.setValorDescuento(0.0);
//                                fact.setValorRecaudado(0.0);
//                                fact.setValorRechazo(0.0);
//
//                                /*Actualiza los valores de los productos de la factura*/
//                                for (CProductosPorFactura pxf : fact.getListaProductosPorFactura()) {
//                                    pxf.setValorDescuentoItem(0.0);
//                                    pxf.setValorRechazoItem(0.0);
//                                    pxf.setCantidadRechazadaItem(0.0);
//                                    pxf.setCausalDeRechazo(1);
//                                    pxf.setEntregado(0);
//                                    pxf.setPorcentajeDescuento(0.0);
//                                    pxf.setValorTotalLiquidacionItem(0.0);
//
//                                }
//
//                                break;
//                            }
//                        }
//
//                        for (CFacturasPorManifiesto fact : manifiesto.getListaFacturasDescargadas()) {
//                            if (fact.getNumeroFactura().equals(numFactura)) {
//
//                                /*Elimina el objeto al array de facturas descargadas*/
//                                manifiesto.getListaFacturasDescargadas().remove(fact);
//
//                                /*Agrega el objeto al arrray de facturas pendientes por descargar*/
//                                manifiesto.getListaFacturasPendientesPorDescargar().add(fact);
//                                borrado = true;
//                                break;
//                            }
//                        }
//
//                        /*se reasigna las adeherencia de descargue */
//                        int i = 1;
//                        for (CFacturasPorManifiesto fact : manifiesto.getListaFacturasDescargadas()) {
//                            fact.setAdherenciaDescargue(i);
//                            i++;
//
//                        }
//
//                        llenarjTableFacturasDescargadasPorVehiculo();
//
//                        lblFacturasPendientes.setText("" + manifiesto.getListaFacturasPendientesPorDescargar().size());
//                        lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));
//
//                        File fichero = new File(manifiesto.getRutaArchivoDescargueporductosPorFactura());
//
//                        if (fichero.exists()) {
//
//                            /*Se actualiza la lista de los productos descargados*/
//                            manifiesto.setListaCProductosPorFacturaDescargados(fichero);
//                            // listaDeCProductosPorFacturaDescargados = manifiesto.getListaCProductosPorFacturaDescargados();
//
//                        }
//                        //lblFacturasPendientes.setText("" + listaFacturasPendientesPorDescargar.size());
//                    }
//
//                } catch (Exception ex) {
//                    Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//                btnGrabar.setEnabled(false);
//                jBtnGrabar.setEnabled(false);
//            }
//
//        } else {
//            borrado = false;
//            descargarFacturas_2.msgTablaVacia();
//            //JOptionPane.showInternalMessageDialog(this, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);
//
//        }
        return borrado;
    }

    private void grabarYCerrarManifiesto() throws InterruptedException {

        if (fliquidarManifiestos.manifiesto.cerrarManifiestoDeDistribucions()) {
            fliquidarManifiestos.btnGrabar.setEnabled(false);
            fliquidarManifiestos.jBtnGrabar.setEnabled(false);

            fliquidarManifiestos.btnGrabar100.setEnabled(false);
            fliquidarManifiestos.txtNumeroDeFactura.setEnabled(false);
            fliquidarManifiestos.cbxPrefijo.setEnabled(false);
            fliquidarManifiestos.cbxMovimientoFactura.setEnabled(false);
            fliquidarManifiestos.rBtnContado.setEnabled(false);
            fliquidarManifiestos.rBtnCredito.setEnabled(false);
            fliquidarManifiestos.btnImprimir.setEnabled(true);
            fliquidarManifiestos.jBtnImprimir.setEnabled(true);

            fliquidarManifiestos.conductorLiquidado = true;

            fliquidarManifiestos.descargarFacturas_2.msgDatoGuardado();
            //JOptionPane.showMessageDialog(this, "Datos Guardados correctamente", "Manifiesto Cerrado", JOptionPane.INFORMATION_MESSAGE);

        } else {

            fliquidarManifiestos.descargarFacturas_2.msgErrorAlGuardar("No se cerraron los manifiestos");
        }
        //new Thread(new HiloGuardarManifiestoDescargado(ini, fliquidarManifiestos, false)).start();

    }

    private boolean borrarTodasLasFacturas() throws HeadlessException {
//        int borrarFila;
//        if (manifiesto.getEstadoManifiesto() == 4) {
//            return true;
//        }
//        // VLIDA SI HAY ELEMENTOS EN EL JTABLE, SI TIENE MAS DE UN ELEMENTO
//        if (jTableFacturasDescargadasPorVehiculo.getRowCount() > 0) {
//           fliquidarManifiestos. modelo2 = (DefaultTableModel) jTableFacturasDescargadasPorVehiculo.getModel();
//
//            // borrarFila = JOptionPane.showOptionDialog(this, "Desea eliminar el Registro de la Tabla?", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No"}, "No");
//            borrarFila = descargarFacturas_2.msgBorrarTodasLasFilas();
//
//            // HA SELECCIONADO BORRAR LA FILA
//            if (borrarFila == JOptionPane.OK_OPTION) {
//
//                try {
//
//                    /* Elimina los datos de los arrays*/
//                    limpiarArrays();
//
//                    /*Actualiza los valores de las facturas sin descargar*/
//                    for (CFacturasPorManifiesto fact : manifiesto.getListaFacturasPorManifiesto()) {
//                        /*Elimina el objeto al array de facturas descargadas*/
//                        fact.setCausalDeRechazo(1);
//                        fact.setIdMovimientoFactura(1);
//                        fact.setIdTipoDeMovimiento(1);
//                        fact.setAdherenciaDescargue(0);
//                        fact.setNumeroDescuento("0");
//                        fact.setNumeroRecogida("0");
//                        fact.setValorARecaudarFactura(fact.getValorTotalFactura());
//                        fact.setValorDescuento(0.0);
//                        fact.setValorRecaudado(0.0);
//                        fact.setValorRechazo(0.0);
//
//                        /*Actualiza los valores de los productos de la factura*/
//                        for (CProductosPorFactura pxf : fact.getListaProductosPorFactura()) {
//                            pxf.setValorDescuentoItem(0.0);
//                            pxf.setValorRechazoItem(0.0);
//                            pxf.setCantidadRechazadaItem(0.0);
//                            pxf.setCausalDeRechazo(1);
//                            pxf.setEntregado(0);
//                            pxf.setPorcentajeDescuento(0.0);
//                            pxf.setValorTotalLiquidacionItem(0.0);
//
//                        }
//
//                        break;
//
//                    }
//
//                    /*Elimina los datos de las tablas */
//                    limpiarTablas();
//
//                    /*Se borran los archivos con los registros */
//                    eliminararchivos();
//                    File fichero;
//                    fichero = new File(manifiesto.getRutaArchivoDescargueFacturas());
//                    fichero = new File(manifiesto.getRutaArchivoDescargueporductosPorFactura());
//                    fichero = new File(manifiesto.getRutArchivoRecogidasporManifiesto());
//                    fichero = new File(manifiesto.getRutArchivoFacturasSinManifestar());
//
//                    lblValorRecaudoManifiesto.setText(nf.format(0.0));
//
//                    lblFacturasPendientes.setText("" + manifiesto.getListaFacturasPendientesPorDescargar().size());
//
//                    manifiesto.setListaCProductosPorFacturaDescargados(manifiesto.getRutaArchivoDescargueporductosPorFactura());
//
//                } catch (Exception ex) {
//                    Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
//                    btnGrabar.setEnabled(false);
//                    jBtnGrabar.setEnabled(false);
//                }
//
//                btnGrabar.setEnabled(false);
//                jBtnGrabar.setEnabled(false);
//            }
//
//        } else {
//            descargarFacturas_2.msgTablaVacia();
//            // JOptionPane.showInternalMessageDialog(this, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);
//        }
        return false;
    }

    public void llenarjListFacturasSinManifestar() throws Exception {

        eliminarDatosDejListFacturasNoManifestadas();

        final DefaultListModel model = new DefaultListModel();

        for (String obj : fliquidarManifiestos.listaDeFacturasNoManifestadas) {

            model.addElement(obj);
        }
        fliquidarManifiestos.jListFacturasNoManifestadas.setModel(model);

    }

    public void llenarjTableFacturasDescargadasPorVehiculo() throws Exception {

        fliquidarManifiestos.modelo2 = (DefaultTableModel) fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getModel();
        if (fliquidarManifiestos.modelo2.getRowCount() > 0) {
            int a = fliquidarManifiestos.modelo2.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                fliquidarManifiestos.modelo2.removeRow(i);
            }
        }

        fliquidarManifiestos.txtNombreConductor.setEditable(false);

        if (fliquidarManifiestos.manifiesto.getListaFacturasDescargadas() != null) {

            for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasDescargadas()) {

                int fila = fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount();
                fliquidarManifiestos.modelo2.addRow(new Object[fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount()]);
                fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt("" + (fila + 1), fila, 0);  // item
                fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(obj.getNumeroFactura(), fila, 1); // numero de factura
                fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(obj.getNombreDeCliente(), fila, 2); // nombre del nombreDelCliente
                fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(obj.getTipoDeDEscargue(), fila, 3); // nombre del nombreDelCliente
                fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(nf.format(obj.getValorRecaudado()), fila, 4);

                fliquidarManifiestos.valorTotalRecaudado += obj.getValorRecaudado();

                Thread.sleep(10);
            }
        }

        fliquidarManifiestos.cbxMovimientoFactura.setEnabled(true);

        if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().isEmpty()) {
            fliquidarManifiestos.btnImprimir.setEnabled(true);
            fliquidarManifiestos.jBtnImprimir.setEnabled(true);

            fliquidarManifiestos.btnGrabar100.setEnabled(false);
            fliquidarManifiestos.jBtnGrabar100.setEnabled(false);

        }

    }

    public void llenarjTableFacturasDescargadas() throws Exception {
        DefaultTableModel modelo = (DefaultTableModel) fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        fliquidarManifiestos.modelo2 = (DefaultTableModel) fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getModel();
        fliquidarManifiestos.txtNombreConductor.setEditable(false);

        for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasDescargadas()) {
            fliquidarManifiestos.filaSeleccionada = fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount();
            fliquidarManifiestos.modelo2.addRow(new Object[fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount()]);
            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt("" + (fliquidarManifiestos.filaSeleccionada + 1), fliquidarManifiestos.filaSeleccionada, 0);  // item
            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(obj.getNumeroFactura(), fliquidarManifiestos.filaSeleccionada, 1); // numero de factura
            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(obj.getNombreDeCliente(), fliquidarManifiestos.filaSeleccionada, 2); // nombre del nombreDelCliente
            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(nf.format(obj.getValorRecaudado()), fliquidarManifiestos.filaSeleccionada, 3);

            fliquidarManifiestos.valorTotalRecaudado += obj.getValorRecaudado();

        }

    }

    public void llenarjTableRecogidasPorVehiculo() throws Exception {
        DefaultTableModel modelo = (DefaultTableModel) fliquidarManifiestos.jTableRecogidas.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        fliquidarManifiestos.modelo2 = (DefaultTableModel) fliquidarManifiestos.jTableRecogidas.getModel();
        fliquidarManifiestos.txtNombreConductor.setEditable(false);
//
//        if (fliquidarManifiestos.manifiesto.getListaDeRecogidasPorManifiesto() != null) {
//
//            for (CRecogidasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaDeRecogidasPorManifiesto()) {
//
//                fliquidarManifiestos.filaSeleccionada = fliquidarManifiestos.jTableRecogidas.getRowCount();
//
//                fliquidarManifiestos.modelo2.addRow(new Object[fliquidarManifiestos.jTableRecogidas.getRowCount()]);
//                fliquidarManifiestos.jTableRecogidas.setValueAt("" + (fliquidarManifiestos.filaSeleccionada + 1), fliquidarManifiestos.filaSeleccionada, 0);  // item
//                fliquidarManifiestos.jTableRecogidas.setValueAt(obj.getNumeroFactura(), fliquidarManifiestos.filaSeleccionada, 1); // numero de factura
//                fliquidarManifiestos.jTableRecogidas.setValueAt(obj.getNumeroDeSoporte(), fliquidarManifiestos.filaSeleccionada, 2); // nombre del nombreDelCliente
//                fliquidarManifiestos.jTableRecogidas.setValueAt(obj.getFacturaAfectada(), fliquidarManifiestos.filaSeleccionada, 3); // nombre del nombreDelCliente
//                for (CFacturasPorManifiesto fac : fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar()) {
//                    if (obj.getNumeroFactura().equals(obj.getNumeroFactura())) {
//                        fliquidarManifiestos.jTableRecogidas.setValueAt(fac.getNombreDeCliente(), fliquidarManifiestos.filaSeleccionada, 4); // nombre del nombreDelCliente
//                        break;
//                    }
//                }
//                //jTableRecogidas.setValueAt(obj.get, filaSeleccionada, 4); // nombre del nombreDelCliente
//                fliquidarManifiestos.jTableRecogidas.setValueAt(nf.format(obj.getValorRecaudadoRecogida()), filaSeleccionada, 5);
//
//                fliquidarManifiestos.valorTotalRecogidas += obj.getValorRecaudadoRecogida();
//            }
//        }

    }

    public void llenarjTableRecogidasPorManifiesto() throws Exception {
        DefaultTableModel modelo = (DefaultTableModel) fliquidarManifiestos.jTableRecogidas.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        fliquidarManifiestos.modelo2 = (DefaultTableModel) fliquidarManifiestos.jTableRecogidas.getModel();
        fliquidarManifiestos.txtNombreConductor.setEditable(false);

//        if (fliquidarManifiestos.manifiesto.getListaDeRecogidasPorManifiesto() != null) {
//
//            for (CRecogidasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaDeRecogidasPorManifiesto()) {
//
//                Vst_Factura facturaActual = new Vst_Factura(ini, obj.getNumeroFactura(), false);
//
//                fliquidarManifiestos.filaSeleccionada = fliquidarManifiestos.jTableRecogidas.getRowCount();
//
//               fliquidarManifiestos.modelo2.addRow(new Object[fliquidarManifiestos.jTableRecogidas.getRowCount()]);
//                fliquidarManifiestos.jTableRecogidas.setValueAt("" + (fliquidarManifiestos.filaSeleccionada + 1), fliquidarManifiestos.filaSeleccionada, 0);  // item
//                fliquidarManifiestos.jTableRecogidas.setValueAt(obj.getNumeroFactura(), fliquidarManifiestos.filaSeleccionada, 1); // numero de factura
//                fliquidarManifiestos.jTableRecogidas.setValueAt(obj.getNumeroDeSoporte(), fliquidarManifiestos.filaSeleccionada, 2); // nombre del nombreDelCliente
//                fliquidarManifiestos.jTableRecogidas.setValueAt(obj.getFacturaAfectada(), fliquidarManifiestos.filaSeleccionada, 3); // nombre del nombreDelCliente
//                fliquidarManifiestos.jTableRecogidas.setValueAt(facturaActual.getNombreDeCliente(), fliquidarManifiestos.filaSeleccionada, 4); // nombre del nombreDelCliente
//                fliquidarManifiestos.jTableRecogidas.setValueAt(nf.format(obj.getValorRecaudadoRecogida()), fliquidarManifiestos.filaSeleccionada, 5);
//
//                fliquidarManifiestos.valorTotalRecogidas += obj.getValorRecaudadoRecogida();
//            }
//        }
    }

    public void llenarjTableListaDeConsignaciones() throws Exception {

        fliquidarManifiestos.modelo2 = (DefaultTableModel) fliquidarManifiestos.jTableListaDeConsignaciones.getModel();
        if (fliquidarManifiestos.modelo2.getRowCount() > 0) {
            int a = fliquidarManifiestos.modelo2.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                fliquidarManifiestos.modelo2.removeRow(i);
            }
        }

        fliquidarManifiestos.txtNombreConductor.setEditable(false);

//        for (CSoportesConsignaciones obj2 : fliquidarManifiestos.manifiesto.getListaDeSoportesConsignaciones()) {
//
//            fliquidarManifiestos.filaSeleccionada = fliquidarManifiestos.jTableListaDeConsignaciones.getRowCount();
//            fliquidarManifiestos.modelo2.addRow(new Object[fliquidarManifiestos.jTableListaDeConsignaciones.getRowCount()]);
//
//            //jTableListaDeConsignaciones.setValueAt("" + (filaSeleccionada + 1), filaSeleccionada, 0);  // item
//            fliquidarManifiestos.jTableListaDeConsignaciones.setValueAt(obj2.getNumeroSoporte(), fliquidarManifiestos.filaSeleccionada, 0); // numero de factura
//            fliquidarManifiestos.jTableListaDeConsignaciones.setValueAt(obj2.getNombreDelBanco(), fliquidarManifiestos.filaSeleccionada, 1); // nombre del nombreDelCliente
//            // jTableListaDeConsignaciones.setValueAt(obj2.getNumeroDeCuenta(), filaSeleccionada, 2);
//            fliquidarManifiestos.jTableListaDeConsignaciones.setValueAt(obj2.getMedioDePago(), fliquidarManifiestos.filaSeleccionada, 2);
//            fliquidarManifiestos.jTableListaDeConsignaciones.setValueAt(nf.format(obj2.getValor()), fliquidarManifiestos.filaSeleccionada, 3);
//
//            fliquidarManifiestos.valorConsignado += obj2.getValor();
//
//        }
    }

    public void opcionDevolucionParcial() {
        //DESCARGUE DE RECHAZO PARCIAL
        fliquidarManifiestos.facturaActual.setIdTipoDeMovimiento(4);

        fliquidarManifiestos.chkConDescuento.setEnabled(true);
        fliquidarManifiestos.jTabbedPane1.setEnabledAt(fliquidarManifiestos.jTabbedPane1.indexOfComponent(fliquidarManifiestos.pnlRechazosParciales), true);
        fliquidarManifiestos.jTabbedPane1.setSelectedIndex(fliquidarManifiestos.jTabbedPane1.indexOfComponent(fliquidarManifiestos.pnlRechazosParciales));

        if (!estaDescargadaLaFactura()) {
            FDescargarRechazosParciales fDescargueRechazosParciales = new FDescargarRechazosParciales(fliquidarManifiestos, 1);
            fDescargueRechazosParciales.setClosable(true);
            fDescargueRechazosParciales.setTitle("Formulario para Descargar Entregas parciales con y sin Descuentos");
            fDescargueRechazosParciales.setLocation((ini.getDimension().width - fDescargueRechazosParciales.getSize().width) / 2, (ini.getDimension().height - fDescargueRechazosParciales.getSize().height) / 2);
            fliquidarManifiestos.getDesktopPane().add(fDescargueRechazosParciales);
            fliquidarManifiestos.cbxPrefijo.setEnabled(false);
            fliquidarManifiestos.txtNumeroDeFactura.setEnabled(false);
            fDescargueRechazosParciales.setVisible(true);
            fDescargueRechazosParciales.toFront();
        }
    }

    public void opcioEntregaTotal() throws Exception {

        // SE VALIDA SI LA FACTURA NO ESTA DESCARGADA Y SE DESCARGA LA ENTREGA TOTAL
        if (!estaDescargadaLaFactura()) {

            /*Utilizando el objeto Cfacturas se bloqua la factura para que no pueda ser anexada a otro manifiesto*/
            //facturaTemporal.liberarFactura(false);
            /*Se activa cuando hay descuento en la facura*/
            if (fliquidarManifiestos.chkConDescuento.isSelected()) {

                FDescargarEntregasTotalesConDescuento fDescargueEntregaTotalConDescuento = new FDescargarEntregasTotalesConDescuento(ini, fliquidarManifiestos);
                fDescargueEntregaTotalConDescuento.toFront();
                fDescargueEntregaTotalConDescuento.setClosable(true);
                fDescargueEntregaTotalConDescuento.setVisible(true);
                fDescargueEntregaTotalConDescuento.setTitle("Formulario para Descargar entregas Totales con Descuentos factura #" + fliquidarManifiestos.facturaActual.getNumeroFactura());
                fDescargueEntregaTotalConDescuento.setLocation((ini.getDimension().width - fDescargueEntregaTotalConDescuento.getSize().width) / 2, (ini.getDimension().height - fDescargueEntregaTotalConDescuento.getSize().height) / 2);
                // fDescargueEntregaTotalConDescuento.fliquidarManifiestos = this;
                fDescargueEntregaTotalConDescuento.llenarFormulario2();
                fliquidarManifiestos.getDesktopPane().add(fDescargueEntregaTotalConDescuento);
                fliquidarManifiestos.cbxPrefijo.setEnabled(true);
                fliquidarManifiestos.txtNumeroDeFactura.setEnabled(true);
                fDescargueEntregaTotalConDescuento.toFront();

                fDescargueEntregaTotalConDescuento.txtPorcentajeDescuento.requestFocus();

                //fDescargueEntregaTotalConDescuento.show();
            } else {
                descargarEntregasTotales2();
            }
            //txtNumeroDeFactura.requestFocus();
        }
    }

    private void consultarLafactura() {

        if (!verificarQueFacturaExiste()) {
            return;
        }

        fliquidarManifiestos.facturaActual = null;
        try {
            // CFacturas fac = new CFacturas(ini, txtNumeroDeFactura.getText().trim());
            // SE CREA EL OBJETO FACTURA
            for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar()) {
                if (obj.getNumeroFactura().equals(fliquidarManifiestos.txtNumeroDeFactura.getText().trim())) {
                    fliquidarManifiestos.facturaActual = obj;

                    break;
                }
            }
            if (fliquidarManifiestos.facturaActual != null) {

                FConsultarFacturasRemoto formularios = new FConsultarFacturasRemoto(ini, new CFacturas(ini, fliquidarManifiestos.facturaActual.getNumeroFactura()));
                fliquidarManifiestos.getDesktopPane().add(formularios);
                formularios.toFront();
                formularios.setClosable(true);
                formularios.setVisible(true);
                formularios.setTitle("Formulario para consultar Facturas");
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                //form.setSize(PrincipalLogistica.escritorio.getSize());
                formularios.setLocation((screenSize.width - formularios.getSize().width) / 2, (screenSize.height - formularios.getSize().height) / 2);
                formularios.show();
            } else {
                fliquidarManifiestos.descargarFacturas_2.msgFacturaNoAparece();
                //JOptionPane.showInternalMessageDialog(this, "La factura no aparece en el manifiesto", "Factura no visible ", 2);
            }

        } catch (Exception ex) {
            Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método encargado de realizar el descargue de las entregas totales de una
     * ruta
     *
     * @param facturaDescargada corresponde al numero de la factura que salió a
     * distribución
     *
     */
    private void descargarEntregasTotales() throws Exception {

        if (!fliquidarManifiestos.isFacturaCredito) {

            fliquidarManifiestos.facturaActual.setValorRecaudado(fliquidarManifiestos.facturaActual.getValorTotalFactura());
            fliquidarManifiestos.facturaActual.setTipoDeDEscargue("E. T. Cn");

            // SI LA FACTURA ES DE CREDITO
        } else {

            fliquidarManifiestos.facturaActual.setValorRecaudado(0.0);
            fliquidarManifiestos.facturaActual.setTipoDeDEscargue("E. T. Cr");

        }

        fliquidarManifiestos.facturaActual.setAdherenciaDescargue(fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size() + 1);
        fliquidarManifiestos.facturaActual.setValorRechazo(0.0);
        fliquidarManifiestos.facturaActual.setValorDescuento(0.0);
        fliquidarManifiestos.facturaActual.setValorRecaudado(fliquidarManifiestos.facturaActual.getValorTotalFactura());
        fliquidarManifiestos.facturaActual.setIdTipoDeMovimiento(2);
        fliquidarManifiestos.facturaActual.setCausalDeRechazo(1);
        fliquidarManifiestos.facturaActual.setActivo(1);

        /*Se añade la factura a la lista de facturas a Descargar*/
        fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().add(fliquidarManifiestos.facturaActual);

//        fliquidarManifiestos.modelo2 = (DefaultTableModel) fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getModel();
//
//        // se anexa el registro a la tabla de facturas por numeroDeManifiesto
//        fliquidarManifiestos.filaSeleccionada = fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount();
//
//        fliquidarManifiestos.modelo2.addRow(new Object[fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount()]);
//        fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt("" + (fliquidarManifiestos.filaSeleccionada + 1), fliquidarManifiestos.filaSeleccionada, 0);  // item
//        fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNumeroFactura(), fliquidarManifiestos.filaSeleccionada, 1); // numero de factura
//        fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNombreDeCliente(), fliquidarManifiestos.filaSeleccionada, 2); // nombre del nombreDelCliente
//        fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getTipoDeDEscargue(), fliquidarManifiestos.filaSeleccionada, 3); // nombre del nombreDelCliente
//        fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(nf.format(fliquidarManifiestos.facturaActual.getValorRecaudado()), fliquidarManifiestos.filaSeleccionada, 4);
//
//        // se ubica en la fila insertada
//        fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.changeSelection(fliquidarManifiestos.filaSeleccionada, 0, false, false);
        agregarElementoAlaTablaFacturasDescargadas();
        //fliquidarManifiestos.rutaDeArchivoFacturasDescargadas = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + fliquidarManifiestos.manifiesto.codificarManifiesto() + "_FacturasDescargados.txt";
//        fliquidarManifiestos.rutaDeArchivoFacturasDescargadas = fliquidarManifiestos.manifiesto.getRutaArchivoDescargueFacturas();
        // ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + fliquidarManifiestos.manifiesto.codificarManifiesto() + ".txt";
        senteciaSqlFacturasDescargadas = fliquidarManifiestos.facturaActual.getCadenaConLosCamposParaDescargue();

        Inicio.GuardaConsultaEnFichero(senteciaSqlFacturasDescargadas, fliquidarManifiestos.rutaDeArchivoFacturasDescargadas);

        for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar()) {

            if (obj.getNumeroFactura().equals(fliquidarManifiestos.facturaActual.getNumeroFactura())) {

                /*Se elimina la factura de la lista pendientes por descargar*/
                fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().remove(obj);
                break;
            }
        }

        fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));

        fliquidarManifiestos.lblFacturasPendientes.setText("" + fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size());

        if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().isEmpty()) {
            //fliquidarManifiestos.btnGrabar.setEnabled(true);
            //fliquidarManifiestos.jBtnGrabar.setEnabled(true);
            fliquidarManifiestos.cbxCausalDeRechazoFactura.setEnabled(false);
            fliquidarManifiestos.btnDescargarRechazoTotal.setEnabled(false);
            fliquidarManifiestos.chkConDescuento.setSelected(false);
            fliquidarManifiestos.chkConDescuento.setEnabled(false);

            fliquidarManifiestos.btnGrabar.requestFocus();
        }

        // new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this)).start();//-->Error v 
        if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size() == fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size()) {
            if (fliquidarManifiestos.valorTotalAConsignar >= fliquidarManifiestos.valorConsignado) {
                //fliquidarManifiestos.btnGrabar.setEnabled(true);
                //fliquidarManifiestos.jBtnGrabar.setEnabled(true);
            }
        }

        fliquidarManifiestos.txtNumeroDeFactura.requestFocus();

    }

    /**
     * Método encargado de realizar el descargue de las entregas totales de una
     * ruta
     *
     * @param facturaDescargada corresponde al numero de la factura que salió a
     * distribución
     *
     */
    private void descargarEntregasTotales2() throws Exception {

        if (fliquidarManifiestos.rBtnContado.isSelected()) {

            fliquidarManifiestos.facturaActual.setValorRecaudado(fliquidarManifiestos.facturaActual.getValorTotalFactura());
            fliquidarManifiestos.facturaActual.setTipoDeDEscargue("E. T. Cn");

            // SI LA FACTURA ES DE CREDITO
        }
        if (fliquidarManifiestos.rBtnCredito.isSelected()) {

            fliquidarManifiestos.facturaActual.setValorRecaudado(fliquidarManifiestos.facturaActual.getFpContado());
            fliquidarManifiestos.facturaActual.setTipoDeDEscargue("E. T. Cr");

        }

        fliquidarManifiestos.facturaActual.setAdherenciaDescargue(fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size() + 1);
        fliquidarManifiestos.facturaActual.setValorRechazo(0.0);
        fliquidarManifiestos.facturaActual.setValorDescuento(0.0);
        fliquidarManifiestos.facturaActual.setIdTipoDeMovimiento(2);
        fliquidarManifiestos.facturaActual.setCausalDeRechazo(1);
        fliquidarManifiestos.facturaActual.setActivo(1);

        if (fliquidarManifiestos.facturaActual.grabarFacturasPoManifiestoDescargada()) {

//            /*Se añade la factura a la lista de facturas a Descargar*/
//            fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().add(fliquidarManifiestos.facturaActual);
//
//            fliquidarManifiestos.modelo2 = (DefaultTableModel) fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getModel();
//
//            // se anexa el registro a la tabla de facturas por numeroDeManifiesto
//            fliquidarManifiestos.filaSeleccionada = fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount();
//
//            fliquidarManifiestos.modelo2.addRow(new Object[fliquidarManifiestos.filaSeleccionada]);
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt("" + (fliquidarManifiestos.filaSeleccionada + 1), fliquidarManifiestos.filaSeleccionada, 0);  // item
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNumeroFactura(), fliquidarManifiestos.filaSeleccionada, 1); // numero de factura
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNombreDeCliente(), fliquidarManifiestos.filaSeleccionada, 2); // nombre del nombreDelCliente
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getTipoDeDEscargue(), fliquidarManifiestos.filaSeleccionada, 3); // nombre del nombreDelCliente
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(nf.format(fliquidarManifiestos.facturaActual.getValorRecaudado()), fliquidarManifiestos.filaSeleccionada, 4);
//
//            // se ubica en la fila insertada
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.changeSelection(fliquidarManifiestos.filaSeleccionada, 0, false, false);
            agregarElementoAlaTablaFacturasDescargadas();

            //rutaDeArchivoFacturasDescargadas = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + fliquidarManifiestos.manifiesto.codificarManifiesto() + "_FacturasDescargados.txt";
            //rutaDeArchivoFacturasDescargadas = fliquidarManifiestos.manifiesto.getRutaArchivoDescargueFacturas();
            // ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + fliquidarManifiestos.manifiesto.codificarManifiesto() + ".txt";
            // senteciaSqlFacturasDescargadas = fliquidarManifiestos.facturaActual.getCadenaConLosCamposParaDescargue();
            //Inicio.GuardaConsultaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoFacturasDescargadas);
            for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar()) {

                if (obj.getNumeroFactura().equals(fliquidarManifiestos.facturaActual.getNumeroFactura())) {

                    /*Se elimina la factura de la lista pendientes por descargar*/
                    fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().remove(obj);
                    break;
                }
            }

            fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));

            fliquidarManifiestos.lblFacturasPendientes.setText("" + fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size());

            if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().isEmpty()) {
                // fliquidarManifiestos.btnGrabar.setEnabled(true);
                //fliquidarManifiestos.jBtnGrabar.setEnabled(true);
                fliquidarManifiestos.cbxCausalDeRechazoFactura.setEnabled(false);
                fliquidarManifiestos.btnDescargarRechazoTotal.setEnabled(false);
                fliquidarManifiestos.chkConDescuento.setSelected(false);
                fliquidarManifiestos.chkConDescuento.setEnabled(false);

                fliquidarManifiestos.btnGrabar.requestFocus();
            }

            // new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this)).start();//-->Error v 
            if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size() == fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size()) {
                if (fliquidarManifiestos.valorTotalAConsignar >= fliquidarManifiestos.valorConsignado) {
                    //fliquidarManifiestos.btnGrabar.setEnabled(true);
                    // fliquidarManifiestos.jBtnGrabar.setEnabled(true);
                }
            }
        }

        fliquidarManifiestos.txtNumeroDeFactura.requestFocus();

    }

    /**
     * Método encargado de realizar el descargue de las entregas totales de una
     * ruta
     *
     *
     */
    public void descargarEntregasTotalesConDescuento() throws Exception {

        fliquidarManifiestos.facturaActual.setAdherenciaDescargue(fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size() + 1);

        if (fliquidarManifiestos.facturaActual.grabarFacturasPoManifiestoDescargada()) {

            /*Se añade la factura a la lista de facturas a Descargar*/
            fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().add(fliquidarManifiestos.facturaActual);

//            // SI LA FACTURA ES DE CONTADO
//            fliquidarManifiestos.modelo2 = (DefaultTableModel) fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getModel();
//
//            // se anexa el registro a la tabla de facturas por numeroDeManifiesto
//            fliquidarManifiestos.filaSeleccionada = fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount();
//
//            fliquidarManifiestos.modelo2.addRow(new Object[fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount()]);
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt("" + (fliquidarManifiestos.filaSeleccionada + 1), fliquidarManifiestos.filaSeleccionada, 0);  // item
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNumeroFactura(), fliquidarManifiestos.filaSeleccionada, 1); // numero de factura
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNombreDeCliente(), fliquidarManifiestos.filaSeleccionada, 2); // nombre del nombreDelCliente
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt("E. T. %", fliquidarManifiestos.filaSeleccionada, 3); // nombre del nombreDelCliente
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(nf.format(fliquidarManifiestos.facturaActual.getValorRecaudado()), fliquidarManifiestos.filaSeleccionada, 4);
//
//            // se ubica en la fila insertada
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.changeSelection(fliquidarManifiestos.filaSeleccionada, 0, false, false);
            agregarElementoAlaTablaFacturasDescargadas();

            //  fliquidarManifiestos.rutaDeArchivoFacturasDescargadas = fliquidarManifiestos.manifiesto.getRutaArchivoDescargueFacturas();
            senteciaSqlFacturasDescargadas = fliquidarManifiestos.facturaActual.getCadenaConLosCamposParaDescargue();

            //ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
            // ArchivosDeTexto archivo = new ArchivosDeTexto(fliquidarManifiestos.rutaDeArchivoFacturasDescargadas);
            // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
            //archivo.insertarConsultaEnFichero(senteciaSqlFacturasDescargadas, ruta);
            // archivo.insertarLineaEnFichero(senteciaSqlFacturasDescargadas, fliquidarManifiestos.rutaDeArchivoFacturasDescargadas);
            // SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
            for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar()) {
                //aqui
                if (obj.getNumeroFactura().equals(fliquidarManifiestos.facturaActual.getNumeroFactura())) {

                    /*Se elimina la factura de la lista pendientes por descargar*/
                    fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().remove(obj);
                    break;
                }
            }

            fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));

            // armarArchivoProductosPorFacturadescargados(facturaDescargada);
            //new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this, 2)).start(); //--> error
            fliquidarManifiestos.lblFacturasPendientes.setText("" + fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size());
            fliquidarManifiestos.rBtnContado.setSelected(true);
            //chkDescuento.setSelected(false);

            if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().isEmpty()) {
                //fliquidarManifiestos.btnGrabar.setEnabled(true);
                //fliquidarManifiestos.jBtnGrabar.setEnabled(true);
                fliquidarManifiestos.cbxCausalDeRechazoFactura.setEnabled(false);
                fliquidarManifiestos.btnDescargarRechazoTotal.setEnabled(false);
                fliquidarManifiestos.chkConDescuento.setSelected(false);
                fliquidarManifiestos.chkConDescuento.setEnabled(false);

                fliquidarManifiestos.btnGrabar.requestFocus();
            }

            //new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this)).start();//-->Error v 
            if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size() == fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size()) {
                if (fliquidarManifiestos.valorTotalAConsignar >= fliquidarManifiestos.valorConsignado) {
                    // fliquidarManifiestos.btnGrabar.setEnabled(true);
                    // fliquidarManifiestos.jBtnGrabar.setEnabled(true);
                }
            }
        }
        fliquidarManifiestos.txtNumeroDeFactura.requestFocus();

    }

    /**
     * Método encargado de realizar el descargue de los rechazos totales de una
     * ruta
     *
     * @param facturaDescargada corresponde al numero de la factura que salió a
     * distribución
     *
     */
    private void descargarRechazosTotales() throws Exception {

        fliquidarManifiestos.causalrechazo = 0;

        for (CCausalesDeDevolucion obj : ini.getListaDeCausalesDeDevolucion()) {
            if (obj.getNombreCausalesDeDevolucion().equals(fliquidarManifiestos.cbxCausalDeRechazoFactura.getSelectedItem())) {
                fliquidarManifiestos.causalrechazo = obj.getIdCausalesDeDevolucion();
                fliquidarManifiestos.facturaActual.setCausalDeRechazo(fliquidarManifiestos.causalrechazo);
                break;
            }
        }

        fliquidarManifiestos.facturaActual.setValorRechazo(fliquidarManifiestos.facturaActual.getValorTotalFactura());
        fliquidarManifiestos.facturaActual.setValorDescuento(0.0);
        fliquidarManifiestos.facturaActual.setValorRecaudado(0.0);
        fliquidarManifiestos.facturaActual.setIdTipoDeMovimiento(3);
        fliquidarManifiestos.facturaActual.setTipoDeDEscargue("D. T.");
        fliquidarManifiestos.facturaActual.setCausalDeRechazo(fliquidarManifiestos.causalrechazo);
        fliquidarManifiestos.facturaActual.setActivo(1);
        fliquidarManifiestos.facturaActual.setAdherenciaDescargue(fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size() + 1);

        if (fliquidarManifiestos.facturaActual.grabarFacturasPoManifiestoDescargada()) {

//                fliquidarManifiestos.modelo2 = (DefaultTableModel) fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getModel();
//
//                // se anexa el registro a la tabla de facturas por numeroDeManifiesto
//                fliquidarManifiestos.filaSeleccionada = fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount();
//
//                fliquidarManifiestos.modelo2.addRow(new Object[fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount()]);
//                fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt("" + (fliquidarManifiestos.filaSeleccionada + 1), fliquidarManifiestos.filaSeleccionada, 0);  // item
//                fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNumeroFactura(), fliquidarManifiestos.filaSeleccionada, 1); // numero de factura
//                fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNombreDeCliente(), fliquidarManifiestos.filaSeleccionada, 2); // nombre del nombreDelCliente
//                fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getTipoDeDEscargue(), fliquidarManifiestos.filaSeleccionada, 3); // nombre del nombreDelCliente
//                fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(nf.format(0.0), fliquidarManifiestos.filaSeleccionada, 4);
//
//                // se ubica en la fila insertada
//                fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.changeSelection(fliquidarManifiestos.filaSeleccionada, 0, false, false);
            /* la factura fue seleccionada de la tabla*/
            if (fliquidarManifiestos.filaSeleccionada > 0) {

                for (CFacturasPorManifiesto fxm : fliquidarManifiestos.manifiesto.getListaFacturasDescargadas()) {
                    if (fxm.getNumeroFactura().equals(fliquidarManifiestos.facturaActual.getNumeroFactura())) {
                        fxm.setListaProductosPorFactura(true);

                        fxm.setValorRechazo(fliquidarManifiestos.facturaActual.getValorRechazo());
                        fxm.setValorRecaudado(fliquidarManifiestos.facturaActual.getValorRecaudado());
                        fxm.setIdMovimientoFactura(fliquidarManifiestos.facturaActual.getIdMovimientoFactura());
                        fxm.setCausalDeRechazo(fliquidarManifiestos.facturaActual.getIdCausalDeRechazo());

                        for (int i = 0; i < fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount(); i++) {
                            if (fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getValueAt(i, 1).equals(fliquidarManifiestos.facturaActual.getNumeroFactura())) {
                                fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getTipoDeDEscargue(), i, 3); // nombre del nombreDelCliente
                                fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(nf.format(fliquidarManifiestos.facturaActual.getValorRecaudado()), i, 4);

                                /* Se calcula el valor recaudado */
                                fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));
                                fliquidarManifiestos.lblFacturasPendientes.setText("" + fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size());

                                if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().isEmpty()) {
                                    fliquidarManifiestos.btnGrabar100.setEnabled(false);
                                    fliquidarManifiestos.jBtnGrabar100.setEnabled(false);
                                    fliquidarManifiestos.btnImprimir.setEnabled(true);
                                    fliquidarManifiestos.jBtnImprimir.setEnabled(true);
                                } else {
                                    fliquidarManifiestos.btnGrabar100.setEnabled(false);
                                    fliquidarManifiestos.jBtnGrabar100.setEnabled(false);
                                }
                            }
                        }
                        fliquidarManifiestos.filaSeleccionada = -1;
                        return;
                    }
                }
            }

            /* la factura digitada en el campo de texto*/
            agregarElementoAlaTablaFacturasDescargadas();

            fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().add(fliquidarManifiestos.facturaActual);

            // fliquidarManifiestos.rutaDeArchivoFacturasDescargadas = fliquidarManifiestos.manifiesto.getRutaArchivoDescargueFacturas();
            senteciaSqlFacturasDescargadas = fliquidarManifiestos.facturaActual.getCadenaConLosCamposParaDescargue();

            //ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
            // ArchivosDeTexto archivo = new ArchivosDeTexto(fliquidarManifiestos.rutaDeArchivoFacturasDescargadas);
            // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
            //archivo.insertarConsultaEnFichero(senteciaSqlFacturasDescargadas, ruta);
            // archivo.insertarLineaEnFichero(senteciaSqlFacturasDescargadas, fliquidarManifiestos.rutaDeArchivoFacturasDescargadas);
            // SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
            for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar()) {
                if (obj.getNumeroFactura().equals(fliquidarManifiestos.facturaActual.getNumeroFactura())) {

                    /*Se elimina la factura de la lista pendientes por descargar*/
                    fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().remove(obj);
                    break;
                }
            }

            fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));
            fliquidarManifiestos.rBtnContado.setSelected(true);

            // armarArchivoProductosPorFacturadescargados(facturaDescargada);
            //           new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this)).start();
            fliquidarManifiestos.lblFacturasPendientes.setText("" + fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size());
            fliquidarManifiestos.rBtnContado.setSelected(true);
            fliquidarManifiestos.rBtnContado.setSelected(true);

            if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().isEmpty()) {
                // fliquidarManifiestos.btnGrabar.setEnabled(true);
                // fliquidarManifiestos.jBtnGrabar.setEnabled(true);
                fliquidarManifiestos.chkConDescuento.setSelected(false);
                fliquidarManifiestos.chkConDescuento.setEnabled(false);
                fliquidarManifiestos.cbxCausalDeRechazoFactura.setEnabled(false);
                fliquidarManifiestos.btnDescargarRechazoTotal.setEnabled(false);
                fliquidarManifiestos.btnGrabar.requestFocus();
            }

            if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size() == fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size()) {
                if (fliquidarManifiestos.valorTotalAConsignar >= fliquidarManifiestos.valorConsignado) {
                    // fliquidarManifiestos.btnGrabar.setEnabled(true);
                    // fliquidarManifiestos.jBtnGrabar.setEnabled(true);
                }
            }

            fliquidarManifiestos.txtNumeroDeFactura.requestFocus();
        }
        fliquidarManifiestos.btnGrabar100.setEnabled(true);
        fliquidarManifiestos.jBtnGrabar100.setEnabled(true);

    }

    /**
     * Método encargado de realizar el descargue de los rechazos parciales de
     * una ruta
     *
     *
     * @throws java.lang.Exception
     */
    public void descargarRechazosParciales() throws Exception {

//            fliquidarManifiestos.modelo2 = (DefaultTableModel) fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getModel();
//
//            // se anexa el registro a la tabla de facturas por numeroDeManifiesto
//            fliquidarManifiestos.filaSeleccionada = fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount();
//
//            fliquidarManifiestos.modelo2.addRow(new Object[fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount()]);
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt("" + (fliquidarManifiestos.filaSeleccionada + 1), fliquidarManifiestos.filaSeleccionada, 0);  // item
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNumeroFactura(), fliquidarManifiestos.filaSeleccionada, 1); // numero de factura
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNombreDeCliente(), fliquidarManifiestos.filaSeleccionada, 2); // nombre del nombreDelCliente
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getTipoDeDEscargue(), fliquidarManifiestos.filaSeleccionada, 3); // nombre del nombreDelCliente
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(nf.format(fliquidarManifiestos.facturaActual.getValorRecaudado()), fliquidarManifiestos.filaSeleccionada, 4);
//            // se ubica en la fila insertada
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.changeSelection(fliquidarManifiestos.filaSeleccionada, 0, false, false);
//
//            
        agregarElementoAlaTablaFacturasDescargadas();

    }

    /**
     * Método encargado de realizar el descargue de las recogidas realizadas en
     * una ruta
     *
     * @param fac corresponde al numero de la factura que salió a distribución
     */
    protected void agregarRecogida(CFacturasPorManifiesto fac) {

        CRecogidasPorManifiesto rxm = new CRecogidasPorManifiesto(ini);

        rxm.setIdRecogidasPorManifiesto(fac.getConsecutivo());
        rxm.setIdNumeroManifiesto(fac.getNumeroManifiesto());
        rxm.setNumeroFactura(fac.getNumeroFactura());
        rxm.setFacturaAfectada(fliquidarManifiestos.txtFacturaAfectada.getText().trim());
        rxm.setNumeroDeSoporte(fliquidarManifiestos.txtNumeroDeSoporteRecogida.getText().trim());
        rxm.setValorRecogida(fliquidarManifiestos.valorRecogida);
        rxm.setValorRecaudadoRecogida(fliquidarManifiestos.valorDescontado);

        //rutaDeArchivoRecogidasDescargado = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + fliquidarManifiestos.manifiesto.codificarManifiesto() + "_RecogidasDescargados.txt";
        //fliquidarManifiestos.rutaDeArchivoRecogidasDescargado = fliquidarManifiestos.manifiesto.getRutArchivoRecogidasporManifiesto();
        senteciaSqlFacturasDescargadas = rxm.getCadenaConLosCampos();

        //ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
        //ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoRecogidasDescargado);
        // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
        //archivo.insertarLineaEnFichero(senteciaSqlFacturasDescargadas, rutaDeArchivoRecogidasDescargado);
        fliquidarManifiestos.valorTotalRecogidas += fliquidarManifiestos.valorDescontado;

        fliquidarManifiestos.valorTotalAConsignar = fliquidarManifiestos.valorTotalRecaudado - fliquidarManifiestos.valorTotalRecogidas;

        fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));

        /*Se agrega la recogida a l lista repectiva*/
        //fliquidarManifiestos.manifiesto.getListaDeRecogidasPorManifiesto().add(rxm);
        //manifiestoActual.setListaDeRecogidasPorManifiestoDescargadas(listaDeRecogidasPorManifiesto);

        /* sellana el  jtable rcogidas*/
        fliquidarManifiestos.modelo3 = (DefaultTableModel) fliquidarManifiestos.jTableRecogidas.getModel();

        int fila = fliquidarManifiestos.jTableRecogidas.getRowCount();

        fliquidarManifiestos.modelo3.addRow(new Object[fliquidarManifiestos.jTableRecogidas.getRowCount()]);

        fliquidarManifiestos.jTableRecogidas.setValueAt("" + (fila + 1), fila, 0);  // item
        fliquidarManifiestos.jTableRecogidas.setValueAt(fliquidarManifiestos.facturaActual.getNumeroFactura(), fila, 1); // numero de factura
        fliquidarManifiestos.jTableRecogidas.setValueAt(fliquidarManifiestos.txtNumeroDeSoporteRecogida.getText().trim(), fila, 2); // numero de soporte
        fliquidarManifiestos.jTableRecogidas.setValueAt(fliquidarManifiestos.txtFacturaAfectada.getText().trim(), fila, 3); // numero de factura recogida
        fliquidarManifiestos.jTableRecogidas.setValueAt(fac.getNombreDeCliente(), fila, 4); // nombre del nombreDelCliente
        fliquidarManifiestos.jTableRecogidas.setValueAt(nf.format(fliquidarManifiestos.valorDescontado), fila, 5); // valor de la recogida

        // se ubica el cursor en la fila insertada
        fliquidarManifiestos.jTableRecogidas.changeSelection(fila, 0, false, false);

        /*se deshabilitan  se limpian los campos de texto respectivo*/
        fliquidarManifiestos.txtFacturaAfectada.setEditable(false);
        fliquidarManifiestos.txtNumeroDeSoporteRecogida.setEditable(false);
        fliquidarManifiestos.txtValorRecogida.setEditable(false);

        fliquidarManifiestos.txtFacturaAfectada.setEnabled(false);
        fliquidarManifiestos.txtNumeroDeSoporteRecogida.setEnabled(false);
        fliquidarManifiestos.txtValorRecogida.setEnabled(false);

        fliquidarManifiestos.txtFacturaAfectada.setText("");
        fliquidarManifiestos.txtNumeroDeSoporteRecogida.setText("");
        fliquidarManifiestos.txtValorRecogida.setText("");
        fliquidarManifiestos.txtValorDescontado.setText("");
        fliquidarManifiestos.rBtnContado.setSelected(true);
        fliquidarManifiestos.btnAgregarRecogida.setEnabled(false);

        fliquidarManifiestos.txtNumeroDeFactura.requestFocus();
    }

    /**
     * Método encargado de realizar el descargue de las facturas que vuelven a
     * salir a Distribución
     *
     *
     * @throws java.lang.Exception
     */
    public void descargarReEnvios() throws Exception {

        fliquidarManifiestos.facturaActual.setAdherenciaDescargue(fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size() + 1);
        fliquidarManifiestos.facturaActual.setValorDescuento(0.0);
        fliquidarManifiestos.facturaActual.setValorRecaudado(0.0);
        fliquidarManifiestos.facturaActual.setTipoDeDEscargue("R. E.");
        fliquidarManifiestos.facturaActual.setValorRechazo(0.0);
        fliquidarManifiestos.facturaActual.setValorDescuento(0.0);
        fliquidarManifiestos.facturaActual.setValorRecaudado(0.0);
        fliquidarManifiestos.facturaActual.setIdTipoDeMovimiento(6); // vuelve a zona
        fliquidarManifiestos.facturaActual.setCausalDeRechazo(18); // FUERA DE FECHA U HORARIO
        fliquidarManifiestos.facturaActual.setActivo(1);
        //txtNumeroDeFactura.setText(nf.format(0.0));

        if (fliquidarManifiestos.facturaActual.grabarFacturasPoManifiestoDescargada()) {

//            fliquidarManifiestos.modelo2 = (DefaultTableModel) fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getModel();
//
//            // se anexa el registro a la tabla de facturas por numeroDeManifiesto
//            fliquidarManifiestos.filaSeleccionada = fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount();
//
//            fliquidarManifiestos.modelo2.addRow(new Object[fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount()]);
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt("" + (fliquidarManifiestos.filaSeleccionada + 1), fliquidarManifiestos.filaSeleccionada, 0);  // item
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNumeroFactura(), fliquidarManifiestos.filaSeleccionada, 1); // numero de factura
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNombreDeCliente(), fliquidarManifiestos.filaSeleccionada, 2); // nombre del nombreDelCliente
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getTipoDeDEscargue(), fliquidarManifiestos.filaSeleccionada, 3); // nombre del nombreDelCliente
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(nf.format(0.0), fliquidarManifiestos.filaSeleccionada, 4);
//
//            // se ubica en la fila insertada
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.changeSelection(fliquidarManifiestos.filaSeleccionada, 0, false, false);
            agregarElementoAlaTablaFacturasDescargadas();

//            fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));
//
//            fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().add(fliquidarManifiestos.facturaActual);
//
//            //fliquidarManifiestos.rutaDeArchivoFacturasDescargadas = fliquidarManifiestos.manifiesto.getRutaArchivoDescargueFacturas();
//            //senteciaSqlFacturasDescargadas = fliquidarManifiestos.facturaActual.getCadenaConLosCamposParaDescargue();
//            // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
//            //Inicio.GuardaConsultaEnFichero(senteciaSqlFacturasDescargadas, fliquidarManifiestos.rutaDeArchivoFacturasDescargadas);
//            // SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
//            for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar()) {
//                if (obj.getNumeroFactura().equals(fliquidarManifiestos.facturaActual.getNumeroFactura())) {
//
//                    /*Se elimina la factura de la lista pendientes por descargar*/
//                    fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().remove(obj);
//                    break;
//                }
//            }
//
//          
//
//            // armarArchivoProductosPorFacturadescargados(facturaDescargada);
////        new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this)).start();
//            fliquidarManifiestos.lblFacturasPendientes.setText("" + fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size());
//
//            if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().isEmpty()) {
//                fliquidarManifiestos.btnGrabar.setEnabled(true);
//                fliquidarManifiestos.jBtnGrabar.setEnabled(true);
//
//                fliquidarManifiestos.chkConDescuento.setSelected(false);
//                fliquidarManifiestos.chkConDescuento.setEnabled(false);
//                fliquidarManifiestos.cbxCausalDeRechazoFactura.setEnabled(false);
//                fliquidarManifiestos.btnDescargarRechazoTotal.setEnabled(false);
//                fliquidarManifiestos.btnGrabar.requestFocus();
//            }
//
//            if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size() == fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size()) {
//                if (fliquidarManifiestos.valorTotalAConsignar >= fliquidarManifiestos.valorConsignado) {
//                    fliquidarManifiestos.btnGrabar.setEnabled(true);
//                    fliquidarManifiestos.jBtnGrabar.setEnabled(true);
//                }
//            }
//        }
//        fliquidarManifiestos.txtNumeroDeFactura.requestFocus();
        }
    }

    /**
     * Método encargado de realizar el descargue de las facturas que vuelven a
     * salir a Distribución
     *
     *
     * @throws java.lang.Exception
     */
    public void descargarNoVisitados() throws Exception {

        fliquidarManifiestos.facturaActual.setTipoDeDEscargue("N. V.");
        fliquidarManifiestos.facturaActual.setValorDescuento(0.0);
        fliquidarManifiestos.facturaActual.setValorRecaudado(0.0);
        fliquidarManifiestos.facturaActual.setAdherenciaDescargue(fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size() + 1);
        fliquidarManifiestos.facturaActual.setValorRechazo(0.0);
        fliquidarManifiestos.facturaActual.setValorDescuento(0.0);
        fliquidarManifiestos.facturaActual.setValorRecaudado(0.0);
        fliquidarManifiestos.facturaActual.setIdTipoDeMovimiento(7); // vuelve a zona
        fliquidarManifiestos.facturaActual.setCausalDeRechazo(18); // FUERA DE FECHA U HORARIO
        fliquidarManifiestos.facturaActual.setActivo(1);
        //txtNumeroDeFactura.setText(nf.format(0.0));

        if (fliquidarManifiestos.facturaActual.grabarFacturasPoManifiestoDescargada()) {

//            fliquidarManifiestos.modelo2 = (DefaultTableModel) fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getModel();
//
//            // se anexa el registro a la tabla de facturas por numeroDeManifiesto
//            fliquidarManifiestos.filaSeleccionada = fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount();
//
//            fliquidarManifiestos.modelo2.addRow(new Object[fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount()]);
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt("" + (fliquidarManifiestos.filaSeleccionada + 1), fliquidarManifiestos.filaSeleccionada, 0);  // item
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNumeroFactura(), fliquidarManifiestos.filaSeleccionada, 1); // numero de factura
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNombreDeCliente(), fliquidarManifiestos.filaSeleccionada, 2); // nombre del nombreDelCliente
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getTipoDeDEscargue(), fliquidarManifiestos.filaSeleccionada, 3); // nombre del nombreDelCliente
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(nf.format(0.0), fliquidarManifiestos.filaSeleccionada, 4);
//
//            // se ubica en la fila insertada
//            fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.changeSelection(fliquidarManifiestos.filaSeleccionada, 0, false, false);
            agregarElementoAlaTablaFacturasDescargadas();

//            fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));
//
//            fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().add(fliquidarManifiestos.facturaActual);
//
//            // fliquidarManifiestos.rutaDeArchivoFacturasDescargadas = fliquidarManifiestos.manifiesto.getRutaArchivoDescargueFacturas();
//            //  senteciaSqlFacturasDescargadas = fliquidarManifiestos.facturaActual.getCadenaConLosCamposParaDescargue();
//            // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
//            // Inicio.GuardaConsultaEnFichero(senteciaSqlFacturasDescargadas, fliquidarManifiestos.rutaDeArchivoFacturasDescargadas);
//            // SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
//            for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar()) {
//                if (obj.getNumeroFactura().equals(fliquidarManifiestos.facturaActual.getNumeroFactura())) {
//
//                    /*Se elimina la factura de la lista pendientes por descargar*/
//                    fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().remove(obj);
//                    break;
//                }
//            }
//
//            fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));
//
//            // armarArchivoProductosPorFacturadescargados(facturaDescargada);
////        new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this)).start();
//            fliquidarManifiestos.lblFacturasPendientes.setText("" + fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size());
//
//            if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().isEmpty()) {
//                fliquidarManifiestos.btnGrabar.setEnabled(true);
//                fliquidarManifiestos.jBtnGrabar.setEnabled(true);
//                fliquidarManifiestos.chkConDescuento.setSelected(false);
//                fliquidarManifiestos.chkConDescuento.setEnabled(false);
//                fliquidarManifiestos.cbxCausalDeRechazoFactura.setEnabled(false);
//                fliquidarManifiestos.btnDescargarRechazoTotal.setEnabled(false);
//                fliquidarManifiestos.btnGrabar.requestFocus();
//            }
//
//            if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size() == fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size()) {
//                if (fliquidarManifiestos.valorTotalAConsignar >= fliquidarManifiestos.valorConsignado) {
//                    fliquidarManifiestos.btnGrabar.setEnabled(true);
//                    fliquidarManifiestos.jBtnGrabar.setEnabled(true);
//                }
//            }
//        }
//        fliquidarManifiestos.txtNumeroDeFactura.requestFocus();
        }
    }

    /**
     * Método encargado de realizar el descargue de las recogidas realizadas en
     * una ruta
     *
     * @param soporte
     */
    protected void agregarConsignacion(CSoportesConsignaciones soporte) {

        //rutaDeArchivoRecogidasDescargado = "" + (new File(".").getAbsolutePath()).replace(".", "") + "tmp/tmp_" + fliquidarManifiestos.manifiesto.codificarManifiesto() + "_RecogidasDescargados.txt";
//        fliquidarManifiestos.rutaDeArchivoSoporteConsignaciones = fliquidarManifiestos.manifiesto.getRutaArchivoSoportesDeConsignaciones();
        senteciaSqlSoportesConsignaciones = soporte.getCadenaConLosCampos();

        //ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
        //ArchivosDeTexto archivo = new ArchivosDeTexto(rutaDeArchivoSoporteConsignaciones);
        // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
        //archivo.insertarLineaEnFichero(senteciaSqlSoportesConsignaciones, rutaDeArchivoSoporteConsignaciones);
        senteciaSqlSoportesConsignaciones = null;

        fliquidarManifiestos.valorConsignado += soporte.getValor();

        fliquidarManifiestos.lblValorConsignado.setText(nf.format(fliquidarManifiestos.valorConsignado));

        /*Se agrega la consignacion a la lista repectiva*/
//        fliquidarManifiestos.manifiesto.getListaDeSoportesConsignaciones().add(soporte);
        //manifiestoActual.setListaDeSoportesConsignaciones(listaDeSoportesDeConsignaciones);

        /* sellana el  jtable rcogidas*/
        DefaultTableModel modelo = (DefaultTableModel) fliquidarManifiestos.jTableListaDeConsignaciones.getModel();

        int fila = fliquidarManifiestos.jTableListaDeConsignaciones.getRowCount();

        modelo.addRow(new Object[fila]);

        fliquidarManifiestos.jTableListaDeConsignaciones.setValueAt(soporte.getNumeroSoporte(), fila, 0);  // item
        fliquidarManifiestos.jTableListaDeConsignaciones.setValueAt(soporte.getNombreDelBanco(), fila, 1); // numero de factura
        fliquidarManifiestos.jTableListaDeConsignaciones.setValueAt(soporte.getMedioDePago(), fila, 2); // numero de soporte
        fliquidarManifiestos.jTableListaDeConsignaciones.setValueAt(nf.format(soporte.getValor()), fila, 3); // valor de la recogida

        // se ubica el cursor en la fila insertada
        fliquidarManifiestos.jTableListaDeConsignaciones.changeSelection(fila, 0, false, false);

        fliquidarManifiestos.txtNumeroSoporteConsignacion.setText("");
        fliquidarManifiestos.txtMedioDePago.setText("");
        fliquidarManifiestos.txtValorConsignacion.setText("");

        if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size() == fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size()) {
            if (fliquidarManifiestos.valorTotalAConsignar >= fliquidarManifiestos.valorConsignado) {
                //fliquidarManifiestos.btnGrabar.setEnabled(true);
                //fliquidarManifiestos.jBtnGrabar.setEnabled(true);
            }
        }

        fliquidarManifiestos.txtNumeroSoporteConsignacion.requestFocus();
    }

    public void nuevo() {
        cancelar();

        fliquidarManifiestos.nuevo = true;
        fliquidarManifiestos.pnlRechazosTotales.setEnabled(true);

        fliquidarManifiestos.txtNombreConductor.setEnabled(true);
        fliquidarManifiestos.txtNombreConductor.setEditable(true);
        fliquidarManifiestos.jTabbedPane1.setEnabled(false);
        fliquidarManifiestos.btnNuevo.setEnabled(false);
        fliquidarManifiestos.jBtnNuevo.setEnabled(false);
        fliquidarManifiestos.txtNombreConductor.requestFocus();
        fliquidarManifiestos.cbxCausalDeRechazoFactura.setSelectedItem("NINGUNO");
        fliquidarManifiestos.cbxMovimientoFactura.setSelectedItem("NINGUNO");

        fliquidarManifiestos.dateOperacion.setEnabled(true);
        fliquidarManifiestos.jBtnConsignaciones.setEnabled(true);
        fliquidarManifiestos.btnGrabar.setEnabled(false);
        fliquidarManifiestos.jBtnGrabar.setEnabled(false);

        fliquidarManifiestos.btnNuevo.setEnabled(false);
        fliquidarManifiestos.jBtnNuevo.setEnabled(false);

        fliquidarManifiestos.btnImprimir.setEnabled(false);
        fliquidarManifiestos.jBtnImprimir.setEnabled(false);

        fliquidarManifiestos.jBtnMinuta.setEnabled(false);

        fliquidarManifiestos.autoTxtNumeroManifiesto = new TextAutoCompleter(fliquidarManifiestos.txtNombreConductor);
        for (CManifiestosDeDistribucion manifiesto : ini.getListaDeManifiestossinDescargar()) {

            if (manifiesto.getEstadoManifiesto() != 4) {
                fliquidarManifiestos.autoTxtNumeroManifiesto.addItem(manifiesto.getNumeroManifiesto() + "     ");
            }
        }
    }

    private void cancelar() {
        if (fliquidarManifiestos.manifiesto != null) {

            if (fliquidarManifiestos.esteManifiestoEsMio) {
                // new Thread(new HiloLiberarManifiesto(manifiestoActual, true)).start();//-->Error v 
                //manifiestoActual.liberarManifiesto(true);
                //manifiestoActual = null;
                fliquidarManifiestos.esteManifiestoEsMio = false;
                fliquidarManifiestos.jBtnMinuta.setEnabled(false);
                fliquidarManifiestos.jProgressBar1.setValue(0);

//                fliquidarManifiestos.lblCirculoDeProgreso.setVisible(false);
                fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
            }

        }
        fliquidarManifiestos.autoTxtNumeroManifiesto = new TextAutoCompleter(fliquidarManifiestos.txtNombreConductor);
        for (CManifiestosDeDistribucion manifiesto : this.ini.getListaDeManifiestossinDescargar()) {

            if (manifiesto.getEstadoManifiesto() != 4) {
                fliquidarManifiestos.autoTxtNumeroManifiesto.addItem(manifiesto.getNumeroManifiesto() + "     ");
            }
        }
        limpiar();
    }

    public boolean cerrarManifiesto() throws HeadlessException {
        /*Se valida que lista de Facturas Descargadas este completa*/
        if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size() > 0) {
            // JOptionPane.showMessageDialog(this, "No se han descargado todas las facturas", "Error", JOptionPane.WARNING_MESSAGE);
            fliquidarManifiestos.descargarFacturas_2.msgFacturasPendientes();
            return true;
        }
        if (fliquidarManifiestos.valorConsignado < fliquidarManifiestos.valorTotalAConsignar) {
//          JOptionPane.showMessageDialog(this, "No se puede guardar manifiesto,\n "
//                  + "el valor consignado es menor al valor de la ruta ", "Error en la consignacion", JOptionPane.ERROR_MESSAGE, null);
//          return;
        }
        /* Se valida que el manifiesto se encuentre en estado = 3 osea en distribución*/
//        if (fliquidarManifiestos.manifiesto.getEstadoManifiesto() == 3) {

        int deseaGrabar = fliquidarManifiestos.descargarFacturas_2.msgDeseaCerrarManifiesto();

        /* Se valida el deseo de cerrarManifiesto los datos en la BBDD  */
        if (deseaGrabar == JOptionPane.OK_OPTION) {

            /* Se veirifica sí hay conexion a Internet */
            if (ini.verificarConexion()) {

                try {

                    fliquidarManifiestos.cbxMovimientoFactura.setEnabled(true);
                    fliquidarManifiestos.chkConDescuento.setEnabled(false);
                    fliquidarManifiestos.chkConDescuento.setSelected(false);
                    fliquidarManifiestos.rBtnContado.setEnabled(false);
                    fliquidarManifiestos.rBtnCredito.setEnabled(false);
                    fliquidarManifiestos.txtNumeroDeFactura.setEditable(false);
                    fliquidarManifiestos.btnGrabar.setEnabled(false);
                    fliquidarManifiestos.jBtnGrabar.setEnabled(false);
                    grabarYCerrarManifiesto();

                } catch (Exception e) {
                    fliquidarManifiestos.descargarFacturas_2.msgErrorAlGuardar(e.getMessage());
                    //JOptionPane.showMessageDialog(this, "Error al guardar los datos " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE, null);
                }
            } else {
                fliquidarManifiestos.descargarFacturas_2.msgSinInternet();
                //JOptionPane.showMessageDialog(this, "Error al guardar los datos, no hay Internet  ", "Error al cerrarManifiesto ", JOptionPane.ERROR_MESSAGE, null);
            }

        }

//        } else {
//            descargarFacturas_2.msgNoSePuedeGrabar();
//            //JOptionPane.showMessageDialog(this, "Este manifiesto no se puede guardar . Verificar con el administrador", "Error al cerrarManifiesto ", JOptionPane.ERROR_MESSAGE, null);
//        }
        return false;
    }

    /**
     * Método que elimina la factura y los productos de una facturas
     * descargadas, que se encuentra almacenada en un archivo
     *
     * @param numFactura es el número de la factura que se va a modificar .
     */
    private boolean eliminarFacturaDelArchivo(String numFactura) {
        boolean borrado = false;
        boolean encontrado = false;
        try {
//            String rutaDeArchivoFacturaDescargada = fliquidarManifiestos.manifiesto.getRutaArchivoDescargueFacturas();
//            File archivo = new File(rutaDeArchivoFacturaDescargada);
//
//            if (archivo.exists()) {
//                FileReader fr = new FileReader(archivo);
//                BufferedReader br = new BufferedReader(fr);
//                String linea;
//
//                while ((linea = br.readLine()) != null) {
//
//                    String strMain = linea;
//                    String[] arrSplit = strMain.split(",");
//
//                    if (arrSplit[1].equals(numFactura)) {
//                        encontrado = true;
//                    }
//
//                }
//            }

            if (!encontrado) {
                fliquidarManifiestos.descargarFacturas_2.msgPedidoNoSePuedeBorrar(numFactura);
                // JOptionPane.showInternalMessageDialog(this, "El pedido # " + numFactura + " no se puede borrar", "Alerta al borrar factura", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            // IDENTIFICAMOS EL OBJETO FACTURA DESCARGADA  PARA REALIZAR LOS RESPECTIVOS CAMBIOS
            // for (CFacturasDescargadas obj : arrayTemporalFacDes) {listaFacturasDescargadas
            for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasDescargadas()) {
                if (obj.getNumeroFactura().equals(numFactura)) {

                    /*Se elimina la linea del archivo donde esta la factura*/
                    CFacturasDescargadas facturaDescargada = new CFacturasDescargadas(ini);

                    facturaDescargada.setConsecutivo(obj.getConsecutivo());
                    facturaDescargada.setNumeroManifiesto(obj.getNumeroManifiesto());
                    facturaDescargada.setAdherenciaDescargue(obj.getAdherencia());
                    facturaDescargada.setNumeroFactura(obj.getNumeroFactura());
                    facturaDescargada.setValorRechazo(obj.getValorRechazo());
                    facturaDescargada.setValorDescuento(obj.getValorDescuento());
                    facturaDescargada.setValorRecaudado(obj.getValorRecaudado());
                    facturaDescargada.setIdTipoDeMovimiento(obj.getIdTipoDeMovimiento());
                    facturaDescargada.setCausalDeRechazo(obj.getCausalDeRechazo());
                    facturaDescargada.setActivo(1);

//                    if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
//                        borrado = facturaDescargada.borrarFacturaDescargada(fliquidarManifiestos.filaSeleccionada, manifiesto);
//                    } else {
//
//                        borrado = facturaDescargada.borrarFacturaDescargada(numFactura, manifiesto);
//                    }
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return borrado;
    }

    /**
     * Método que elimina la factura y los productos de una facturas
     * descargadas, que se encuentra almacenada en un archivo
     *
     * @param numFactura es el número de la factura que se va a modificar .
     */
    private boolean eliminarFacturaDelArchivo2(String numFactura) {
        boolean borrado = false;

        try {
            // IDENTIFICAMOS EL OBJETO FACTURA DESCARGADA  PARA REALIZAR LOS RESPECTIVOS CAMBIOS
            // for (CFacturasDescargadas obj : arrayTemporalFacDes) {listaFacturasDescargadas
            for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasDescargadas()) {
                if (obj.getNumeroFactura().equals(numFactura)) {

                    /*Se elimina la linea del archivo donde esta la factura*/
                    CFacturasDescargadas facturaDescargada = new CFacturasDescargadas(ini);

                    facturaDescargada.setConsecutivo(obj.getConsecutivo());
                    facturaDescargada.setNumeroManifiesto(obj.getNumeroManifiesto());
                    facturaDescargada.setAdherenciaDescargue(obj.getAdherencia());
                    facturaDescargada.setNumeroFactura(obj.getNumeroFactura());
                    facturaDescargada.setValorRechazo(obj.getValorRechazo());
                    facturaDescargada.setValorDescuento(obj.getValorDescuento());
                    facturaDescargada.setValorRecaudado(obj.getValorRecaudado());
                    facturaDescargada.setIdTipoDeMovimiento(obj.getIdTipoDeMovimiento());
                    facturaDescargada.setCausalDeRechazo(obj.getCausalDeRechazo());
                    facturaDescargada.setActivo(1);

//                    borrado = facturaDescargada.borrarFacturaDescargada(fliquidarManifiestos.filaSeleccionada, this.manifiesto);
                    // arrayTemporalFacDes.remove(obj);
                    //borrado = true;
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(FliquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return borrado;
    }

    public void imprimir() {
        try {
            //manifiestoActual.setVistaFacturasDescargadas(fliquidarManifiestos.manifiesto.getListaFacturasDescargadas());
            //manifiestoActual.setListaDeSoportesConsignaciones(listaDeSoportesDeConsignaciones);

            ReporteDescargueDeFacturasHielera demo = new ReporteDescargueDeFacturasHielera(ini, fliquidarManifiestos.manifiesto);

        } catch (Exception ex) {
            Logger.getLogger(HiloFLiquidarManifiestos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void imprimirConManifiestosAbiertos() {
        //manifiestoActual.setVistaFacturasDescargadas(fliquidarManifiestos.manifiesto.getListaFacturasDescargadas());
        //manifiestoActual.setListaDeSoportesConsignaciones(listaDeSoportesDeConsignaciones);

        ReporteDescargueDeFacturasHielera demo = new ReporteDescargueDeFacturasHielera(ini, fliquidarManifiestos.manifiesto, true);
    }

    public void getionarSoporteConsignacion() throws NumberFormatException {
        CSoportesConsignaciones soporte = new CSoportesConsignaciones(ini);
        soporte.setFechaOperacion(ini.getFechaSql(fliquidarManifiestos.dateOperacion));

        for (CCuentasBancarias obj : ini.getListaDeCuentasBancarias()) {
            if (obj.getNumeroDeCuenta().equals(fliquidarManifiestos.cbxNumeroDeCuenta.getSelectedItem().toString())) {
                soporte.setIdBanco(obj.getIdBanco());
                soporte.setNombreDelBanco(obj.getNombreDeBanco());
                soporte.setIdCuentaBancaria(obj.getIdcuentasBancarias());
                break;
            }
        }

        soporte.setMedioDePago(fliquidarManifiestos.txtMedioDePago.getText().trim());
        soporte.setNumeroDeCuenta(fliquidarManifiestos.cbxNumeroDeCuenta.getSelectedItem().toString());
        soporte.setNumeroManifiesto(Integer.parseInt(fliquidarManifiestos.txtNombreConductor.getText().trim()));
        soporte.setNumeroSoporte(fliquidarManifiestos.txtNumeroSoporteConsignacion.getText().trim());
        soporte.setValor(Double.parseDouble(fliquidarManifiestos.txtValorConsignacion.getText().trim()));

        if (!validarSoporte(soporte)) {

            agregarConsignacion(soporte);

        } else {
            if (fliquidarManifiestos.mensaje.equals("")) {

                JOptionPane.showInternalMessageDialog(fliquidarManifiestos, "Soporte ya ingresado en este manifiesto ", "Soporte ya registrado", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showInternalMessageDialog(fliquidarManifiestos, fliquidarManifiestos.mensaje, "Soporte ya ingresado en la BBDD", JOptionPane.ERROR_MESSAGE);
                fliquidarManifiestos.mensaje = "";
            }
        }
        fliquidarManifiestos.txtNumeroSoporteConsignacion.setText("");
        fliquidarManifiestos.txtMedioDePago.setText("");
        fliquidarManifiestos.txtValorConsignacion.setText("");

        fliquidarManifiestos.txtNumeroSoporteConsignacion.requestFocus();
    }

    private double calcularValoraAConsignar() {

        Double valor = 0.0;
        for (CFacturasPorManifiesto fd : fliquidarManifiestos.manifiesto.getListaFacturasDescargadas()) {
            valor += fd.getValorRecaudado();
        }
        return valor;
    }

    private boolean metodoNuevo() {
        String fecha = "";
        boolean grabado = false;
        fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
        fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        this.fliquidarManifiestos.estaOcupadoGrabando = true;

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloGuardarManifiestoDescargado.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<String> sentenciasSQL_Facturas; // Sentencias para guardar el descargue del manifiesto de la ruta.
        List<String> sentenciasSQL_2; // Sentecias que guarda los productos por factura descargadas
        List<String> sentenciasSQL_local;

        //this.fLiquidarManifiesto.manifiesto.setVistaFacturasEnDistribucion(this.fLiquidarManifiesto.listaFacturasDescargadas);
        // SimpleDateFormat fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sentenciasSQL_Facturas = new ArrayList<>();
        sentenciasSQL_2 = new ArrayList<>();
        sentenciasSQL_local = new ArrayList<>();
        //String fechax;
        //fechax = fecha.format(new Date());

        this.fliquidarManifiestos.cbxMovimientoFactura.setEnabled(true);
        this.fliquidarManifiestos.txtNumeroDeFactura.setEnabled(false);
//        this.fliquidarManifiestos.txtKilometrosEntrada.setEnabled(false);

        this.fliquidarManifiestos.cbxCausalDeRechazoFactura.setEnabled(false);
        this.fliquidarManifiestos.btnDescargarRechazoTotal.setEnabled(false);

        for (CFacturasPorManifiesto fac : fliquidarManifiestos.manifiesto.getListaFacturasDescargadas()) {
            sentenciasSQL_Facturas.add(fac.getSentenciaInsertSQL());
        }

        try {
            /* Se graba las facturas descargadas, prductos por factura descargados, manifiesto  se actualiza */
            if (ini.insertarDatosRemotamente(sentenciasSQL_Facturas, "HiloGuardarFacturasPorManifiesto")) {

                // this.fLiquidarManifiesto.manifiesto.setListaDeAuxiliares();
                fliquidarManifiestos.manifiesto.setIsFree(1);

                /*Se arman las sentencias sql para actualizar el  movimiento en el servidor  local a las facturas */
                for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasDescargadas()) {
                    fecha = obj.getFechaDistribucion();
                    CFacturasDescargadas fd = new CFacturasDescargadas(ini);

                    sentenciasSQL_local.add(fd.actualizarEstadoFactura(obj.getIdTipoDeMovimiento(), obj.getNumeroFactura()));

                }
                /*Actualiza le estado de la facturas en el servidor local*/
                new Thread(new HiloGuardarSenteciasSql(ini, sentenciasSQL_local, true)).start();

                /* al pasar a estado 4 es decir cerrardo, se libera el manifiesto */
//                fliquidarManifiestos.manifiesto.actualizarManifiestoDeDistribucions();
                fliquidarManifiestos.manifiesto.cerrarManifiestoDeDistribucions();

                /*Se borran los archivos con los registros */
                //this.fLiquidarManifiesto.eliminararchivos();
                /*Mensajes de rigor*/
                System.out.println("Termina de guardar los datos de formulario descargar facturas  ");

                fliquidarManifiestos.estaOcupadoGrabando = false;
                fliquidarManifiestos.btnImprimir.setEnabled(true);
                fliquidarManifiestos.btnGrabar.setEnabled(false);
                fliquidarManifiestos.jBtnGrabar.setEnabled(false);

                /*Guarda los productos descargados*/
                new Thread(new HiloGuardarSenteciasSql(ini, sentenciasSQL_2)).start();

                fliquidarManifiestos.manifiesto.setListaFacturasDescargadas();
                fliquidarManifiestos.manifiesto.setListaFacturasPendientesPorDescargar();

//                fliquidarManifiestos.lblCirculoDeProgreso.setVisible(false);
                fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                JOptionPane.showMessageDialog(fliquidarManifiestos, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);
                fliquidarManifiestos.jBtnMinuta.setEnabled(true);

//                fliquidarManifiestos.manifiesto.setEstadoManifiesto(4);
                fliquidarManifiestos.btnImprimir.requestFocus();

                /* si presenta algun error en cerrarManifiesto el manifiesto  */
            } else {

                fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                // fliquidarManifiestos.btnGrabar.setEnabled(true);
                // fliquidarManifiestos.jBtnGrabar.setEnabled(true);
                JOptionPane.showMessageDialog(fliquidarManifiestos, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (SQLException | HeadlessException ex) {
            JOptionPane.showMessageDialog(fliquidarManifiestos, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            // fliquidarManifiestos.btnGrabar.setEnabled(true); 
            // fliquidarManifiestos.jBtnGrabar.setEnabled(true);

            fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

        } catch (Exception ex) {
            Logger.getLogger(HiloGuardarManifiestoDescargado.class.getName()).log(Level.SEVERE, null, ex);
            fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
            fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        }

        fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

        return grabado;
    }

    private void gestionarRecogida() {
        for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasDescargadas()) {
            if (obj.getNumeroFactura().equals(fliquidarManifiestos.facturaActual.getNumeroFactura())) {
//                        facturaDescargada.setConsecutivo(obj.getConsecutivo());
//                        facturaDescargada.setNumeroManifiesto(obj.getNumeroManifiesto());
//                        facturaDescargada.setNumeroFactura(obj.getNumeroFactura());
                agregarRecogida(obj);

            }
        }
    }

    public void grabarCien100() {
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
        this.fliquidarManifiestos.cbxMovimientoFactura.setEnabled(true);
        this.fliquidarManifiestos.txtNumeroDeFactura.setEnabled(false);
//        this.fliquidarManifiestos.txtKilometrosEntrada.setEnabled(false);

        this.fliquidarManifiestos.cbxCausalDeRechazoFactura.setEnabled(false);
        this.fliquidarManifiestos.btnDescargarRechazoTotal.setEnabled(false);

        try {
            /* Se graba las facturas descargadas, prductos por factura descargados, manifiesto  se actualiza */
            if (this.fliquidarManifiestos.manifiesto.grabarFacturasDescargadas100(contado)) {

                fliquidarManifiestos.manifiesto.setListaDeFacturasSinDespachar();
                /* Actualiza las facturas que no fueron despachadas */

                if (fliquidarManifiestos.manifiesto.getListaDeFacturasSinDespachar().size() > 0) {
                    String listaFacturas = "'";
                    for (CFacturasPorManifiesto facxman : fliquidarManifiestos.manifiesto.getListaDeFacturasSinDespachar()) {
                        listaFacturas += facxman.getNumeroFactura() + "','";
                    }

                    listaFacturas = listaFacturas.substring(0, listaFacturas.length() - 2);

                    String sql = "update facturaspormanifiesto set "
                            + "despachado = '2', "
                            + "fechaDespachado = CURRENT_TIMESTAMP,"
                            + "usuariodespachador='AUTOMATICO' "
                            + "where "
                            + "numeroManifiesto='" + fliquidarManifiestos.manifiesto.getNumeroManifiesto() + "' "
                            + "AND numeroFactura in(" + listaFacturas + ");";

                    /*Actualiza el estado de Despachado = 2 de las facturas que no hayan
                    sido verificados en porteria */
                    ini.insertarDatosRemotamente(sql);

                }

                fliquidarManifiestos.manifiesto.setListaFacturasDescargadas();
                fliquidarManifiestos.manifiesto.setListaFacturasPendientesPorDescargar();

                asignarTipoDescargue();

                llenarjTableFacturasDescargadasPorVehiculo();

                this.fliquidarManifiestos.lblFacturasPendientes.setText("" + fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size());
                this.fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));

                // new Thread(new HiloLiberarManifiesto(this.fliquidarManifiestos.manifiesto, true)).start();

                /*Mensajes de rigor*/
                System.out.println("Termina de guardar los datos de formulario descargar facturas  ");

                this.fliquidarManifiestos.estaOcupadoGrabando = false;
                this.fliquidarManifiestos.btnImprimir.setEnabled(true);
                this.fliquidarManifiestos.btnGrabar.setEnabled(false);
                this.fliquidarManifiestos.jBtnGrabar.setEnabled(false);

//                this.fliquidarManifiestos.lblCirculoDeProgreso.setVisible(false);
                this.fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                this.fliquidarManifiestos.btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/Misc-Upload-Database-icon.png"))); // NOI18N
                this.fliquidarManifiestos.jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N

                fliquidarManifiestos.btnGrabar100.setEnabled(false);
                fliquidarManifiestos.jBtnGrabar100.setEnabled(false);

                this.fliquidarManifiestos.jBtnMinuta.setEnabled(true);

                JOptionPane.showMessageDialog(this.fliquidarManifiestos.descargarFacturas_2, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);

                this.fliquidarManifiestos.btnImprimir.requestFocus();

                /* si presenta algun error en cerrarManifiesto el manifiesto  */
            } else {

                this.fliquidarManifiestos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
                this.fliquidarManifiestos.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

                this.fliquidarManifiestos.btnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/Misc-Upload-Database-icon.png"))); // NOI18N
                this.fliquidarManifiestos.jBtnGrabar100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N

                // this.fliquidarManifiestos.btnGrabar.setEnabled(true);
                // this.fliquidarManifiestos.jBtnGrabar.setEnabled(true);
                JOptionPane.showMessageDialog(this.fliquidarManifiestos, "Error al guardar los datos", "Error", JOptionPane.ERROR_MESSAGE, null);
            }
        } catch (HeadlessException ex) {
            JOptionPane.showMessageDialog(this.fliquidarManifiestos, "Error al guardar los datos " + ex, "Error", JOptionPane.ERROR_MESSAGE, null);
            // this.fliquidarManifiestos.btnGrabar.setEnabled(true);
            //this.fliquidarManifiestos.jBtnGrabar.setEnabled(true);

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

    private void agregarElementoAlaTablaFacturasDescargadas() {

        /*Se añade la factura a la lista de facturas a Descargar*/
        fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().add(fliquidarManifiestos.facturaActual);

        fliquidarManifiestos.modelo2 = (DefaultTableModel) fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getModel();

        // se anexa el registro a la tabla de facturas por numeroDeManifiesto
        fliquidarManifiestos.filaSeleccionada = fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount();

        fliquidarManifiestos.modelo2.addRow(new Object[fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.getRowCount()]);
        fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt("" + (fliquidarManifiestos.filaSeleccionada + 1), fliquidarManifiestos.filaSeleccionada, 0);  // item
        fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNumeroFactura(), fliquidarManifiestos.filaSeleccionada, 1); // numero de factura
        fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getNombreDeCliente(), fliquidarManifiestos.filaSeleccionada, 2); // nombre del nombreDelCliente
        fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(fliquidarManifiestos.facturaActual.getTipoDeDEscargue(), fliquidarManifiestos.filaSeleccionada, 3); // nombre del nombreDelCliente
        fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.setValueAt(nf.format(fliquidarManifiestos.facturaActual.getValorRecaudado()), fliquidarManifiestos.filaSeleccionada, 4);

        // se ubica en la fila insertada
        fliquidarManifiestos.tblFacturasDescargadasPorVehiculo.changeSelection(fliquidarManifiestos.filaSeleccionada, 0, false, false);

        //fliquidarManifiestos.rutaDeArchivoFacturasDescargadas = fliquidarManifiestos.manifiesto.getRutaArchivoDescargueFacturas();
        //senteciaSqlFacturasDescargadas = fliquidarManifiestos.facturaActual.getCadenaConLosCamposParaDescargue();
        // ArchivosDeTexto archivo = new ArchivosDeTexto(ruta);
        //ArchivosDeTexto archivo = new ArchivosDeTexto(fliquidarManifiestos.rutaDeArchivoFacturasDescargadas);
        // SE GUARDA TEMPORALMENTE LA FACTURA DESCARGADA
        //archivo.insertarLineaEnFichero(senteciaSqlFacturasDescargadas, fliquidarManifiestos.rutaDeArchivoFacturasDescargadas);
// SE ACTUALIZAN LOS ARREGLOS Y EL OBJETO MANIFIESTO
        for (CFacturasPorManifiesto obj : fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar()) {
            boolean ex = false;
            if (obj.getNumeroFactura().equals(fliquidarManifiestos.facturaActual.getNumeroFactura())) {

                /*Se elimina la factura de la lista pendientes por descargar*/
                fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().remove(obj);
                ex = true;
                break;

            }

        }


        /* Se calcula el valor recaudado */
        fliquidarManifiestos.lblValorRecaudoManifiesto.setText(nf.format(calcularValoraAConsignar()));

        fliquidarManifiestos.cbxPrefijo.setEnabled(true);
        fliquidarManifiestos.txtNumeroDeFactura.setEnabled(true);

        fliquidarManifiestos.lblFacturasPendientes.setText("" + fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size());
        //fliquidarManifiestos.rBtnContado.setSelected(true);

        // armarArchivoProductosPorFacturadescargados(facturaDescargada);
//            new Thread(new hiloArmararchivoProductosPorFacturaDescargados(ini, this)).start();
        if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().isEmpty()) {
            // fliquidarManifiestos.btnGrabar.setEnabled(true);
            //fliquidarManifiestos.jBtnGrabar.setEnabled(true);
            fliquidarManifiestos.chkConDescuento.setSelected(false);
            fliquidarManifiestos.chkConDescuento.setEnabled(false);
            fliquidarManifiestos.cbxCausalDeRechazoFactura.setEnabled(false);
            fliquidarManifiestos.btnDescargarRechazoTotal.setEnabled(false);
            fliquidarManifiestos.btnGrabar100.setEnabled(false);
            fliquidarManifiestos.jBtnGrabar100.setEnabled(false);

        }

        if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().size() == fliquidarManifiestos.manifiesto.getListaFacturasDescargadas().size()) {
            if (fliquidarManifiestos.valorTotalAConsignar >= fliquidarManifiestos.valorConsignado) {
                // fliquidarManifiestos.btnGrabar.setEnabled(true);
                // fliquidarManifiestos.jBtnGrabar.setEnabled(true);
            }
        }

        if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().isEmpty()) {
            fliquidarManifiestos.btnGrabar100.setEnabled(false);
            fliquidarManifiestos.jBtnGrabar100.setEnabled(false);
            fliquidarManifiestos.btnImprimir.setEnabled(true);
            fliquidarManifiestos.jBtnImprimir.setEnabled(true);
        } else {
            fliquidarManifiestos.btnGrabar100.setEnabled(false);
            fliquidarManifiestos.jBtnGrabar100.setEnabled(false);
        }

        fliquidarManifiestos.txtNumeroDeFactura.requestFocus();

    }

}
