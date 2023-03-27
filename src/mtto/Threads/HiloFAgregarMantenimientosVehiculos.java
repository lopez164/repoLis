/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
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
import mtto.Servicios.FAgregarMantenimientoVehiculo;
import mtto.documentos.objetos.GastosPorVehiculo;
import mtto.ingresoDeRegistros.objetos.GastosFlota;
import mtto.ingresoDeRegistros.objetos.MantenimientosPorPlaca;
import mtto.ingresoDeRegistros.objetos.TiposDeMantenimientos;
import mtto.proveedores.SucursalesPorproveedor;
import mtto.vehiculos.CCarros;
import mtto.vehiculos.CVehiculos;

/**
 *
 * @author Usuario
 */
public class HiloFAgregarMantenimientosVehiculos implements Runnable {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    FAgregarMantenimientoVehiculo fAgregarMantenimientoVehiculo = null;
    String caso;
    List<String> listaDeSentenciasPorFactura = null;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFAgregarMantenimientosVehiculos(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fAgregarMantenimientoVehiculo
     * @param comando
     */
    public HiloFAgregarMantenimientosVehiculos(Inicio ini, FAgregarMantenimientoVehiculo fAgregarMantenimientoVehiculo, String comando) {
        this.ini = ini;
        this.fAgregarMantenimientoVehiculo = fAgregarMantenimientoVehiculo;
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
                    case "cancelarOperacion":
                        cancelarOperacion();
                        break;
                    case "agregarFactura":
                         agregarFactura();
                        break;
                    case "eliminarFilaTblProductos":
                        eliminarFilaTblProductos();
                        break;
                    case "grabar":
                        grabar();
                        break;
                    case "asignarValoresgastosFlota":
                        asignarValoresgastosFlota();
                        break;
                    case "editarFactura":
                        editarFactura();
                        // descargarFactura();
                        break;
                    case "imprimir":
                        // imprimir();
                        break;
                    case "borrarUnaFactura":
                        borrarUnaFactura();
                        break;
                    case "getionarSoporteConsignacion":
                        //getionarSoporteConsignacion();
                        break;
                    case "eliminarFacturaDelaTabla":
                        eliminarFacturaDelaTabla(); 
                        break;

                    default:
                        JOptionPane.showInternalMessageDialog(fAgregarMantenimientoVehiculo, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

                }
            }
        } catch (Exception ex) {
            Logger.getLogger(HiloFAgregarMantenimientosVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void cargarFormulario() {

        fAgregarMantenimientoVehiculo.cargarFormulario();
       
    }
       
    private void editarFactura(){
        
         if (validarOrdenDeMantenimiento()) {
            fAgregarMantenimientoVehiculo.habilitarPanel();

            fAgregarMantenimientoVehiculo.txtPlaca.setEditable(false);
            fAgregarMantenimientoVehiculo.txtNombreConductorActual.setEditable(false);
            fAgregarMantenimientoVehiculo.txtKmActual.setEditable(false);
            fAgregarMantenimientoVehiculo.cbxTiposDeMantenimientos.setEnabled(false);
            fAgregarMantenimientoVehiculo.txtOrdenNumero.setEditable(false);
            fAgregarMantenimientoVehiculo.txtObservacionesMtto.setEditable(false);
            fAgregarMantenimientoVehiculo.dateFechaCambioAceiteMtto.setEnabled(false);

            if (fAgregarMantenimientoVehiculo.listaGastosFlota == null) {
                fAgregarMantenimientoVehiculo.listaGastosFlota = new ArrayList<>();
                asignarValoresMantenimiento();
            }

        } else {
            JOptionPane.showMessageDialog(this.fAgregarMantenimientoVehiculo, "Faltan datos para completar \n " + fAgregarMantenimientoVehiculo.mensaje, "Faltan Datos", JOptionPane.WARNING_MESSAGE);
        }
    }

  
     private void grabar() throws HeadlessException {
        int x = JOptionPane.showConfirmDialog(this. fAgregarMantenimientoVehiculo, "Desea guardar el registro ?", "Guardar registro", JOptionPane.OK_CANCEL_OPTION);

        if (x == JOptionPane.OK_OPTION) {
            asignarValoresMantenimiento();
             fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setListaFacturasmantenimientosPOrPlaca( fAgregarMantenimientoVehiculo.listaGastosFlota);

            if ( fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.guardarMantenimientoPorPlaca()) {
                 fAgregarMantenimientoVehiculo.btnGrabarMantenimiento.setEnabled(false);
                 fAgregarMantenimientoVehiculo.jBtnAgregarFActura.setEnabled(false);

                JOptionPane.showMessageDialog(this.fAgregarMantenimientoVehiculo, "Registro guardado con exito", "ok", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this.fAgregarMantenimientoVehiculo, "Error al guardar registro en el sisyema", "Error", JOptionPane.INFORMATION_MESSAGE);

            };

            // new Thread(new HiloGuardarFacturaLogistica(this)).start();
        }
    }

    private void borrarUnaFactura() throws HeadlessException {
     
    }

    private void ordenarTabla() {
     
    }

    private void llenarjTableFacturasPorManifiesto() throws Exception {
       
    }

    
    private void asignarValoresMantenimiento() throws NumberFormatException {
        try {
             fAgregarMantenimientoVehiculo.mantenimientosPorPlaca = new MantenimientosPorPlaca(ini);

             fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setActivo(1);
             fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setPlacaVehiculo( fAgregarMantenimientoVehiculo.carro.getPlaca());
            for (CAgencias ag : ini.getListaDeAgencias()) {
                if (ag.getIdAgencia() ==  fAgregarMantenimientoVehiculo.carro.getAgencia()) {
                     fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setAgencia(ag.getIdAgencia());
                     fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setNombreAgencia(ag.getNombreAgencia());
                     fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setRegional(ag.getIdRegional());
                     fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setNombreRegional(ag.getNombreRegional());
                     fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setZona(ag.getIdZona());
                     fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setNombreZona(ag.getNombreZona());
                    break;
                }

            }
            for (TiposDeMantenimientos tm : ini.getListaDeTiposDeMantenimientos()) {
                if (tm.getNombreMantenimiento().equals( fAgregarMantenimientoVehiculo.cbxTiposDeMantenimientos.getSelectedItem().toString())) {
                     fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setIdtipoMantenimiento(tm.getIdMantenimiento());
                     fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setNombreMantenimiento(tm.getNombreMantenimiento());

                }
            }
             fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setNombreConductor( fAgregarMantenimientoVehiculo.conductor.getNombres());
             fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setApellidosConductor( fAgregarMantenimientoVehiculo.conductor.getApellidos());
             fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setConductorVehiculo( fAgregarMantenimientoVehiculo.conductor.getCedula());

            Date dt = new Date();
            dt = ini.getFechaSql( fAgregarMantenimientoVehiculo.dateFechaCambioAceiteMtto);
             fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setFechaMantenimiento(dt);

             fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setFlag(1);
             fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setIdNumeroDeorden(Integer.parseInt( fAgregarMantenimientoVehiculo.txtOrdenNumero.getText()));
             fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setKilometrajeMantenimiento(Integer.parseInt( fAgregarMantenimientoVehiculo.txtKmActual.getText().trim()));
             fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setUsuario(Inicio.deCifrar(ini.getUser().getNombreUsuario()));
             fAgregarMantenimientoVehiculo.mantenimientosPorPlaca.setObservaciones( fAgregarMantenimientoVehiculo.txtObservacionesMtto.getText().trim());

        } catch (Exception ex) {
            Logger.getLogger(FAgregarMantenimientoVehiculo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     private boolean validarOrdenDeMantenimiento() {
        fAgregarMantenimientoVehiculo.mensaje = "";

        boolean validado = true;

        if (fAgregarMantenimientoVehiculo.txtPlaca.getText().equals("")) {
            fAgregarMantenimientoVehiculo.mensaje += "No se ha especificado la placa del vehiculo \n";
            validado = false;
        }
        if (!fAgregarMantenimientoVehiculo.validarConductor()) {
            fAgregarMantenimientoVehiculo.mensaje += "No se ha especificado el nombre del conductor \n"
                    + "o conductor no existe \n\n";
            validado = false;
        }
        if (fAgregarMantenimientoVehiculo.txtKmActual.getText().equals("")) {
            fAgregarMantenimientoVehiculo.mensaje += "No se ha especificado el Km Actual\n";
            validado = false;
        }
        if (fAgregarMantenimientoVehiculo.cbxTiposDeMantenimientos.getSelectedIndex() == 0) {
            fAgregarMantenimientoVehiculo.mensaje += "No se ha especificado el tipo de mantenimiento\n";
            validado = false;

        }
        if (fAgregarMantenimientoVehiculo.txtOrdenNumero.getText().equals("")) {
            fAgregarMantenimientoVehiculo.mensaje += "no se ha especificado el numero de la orden de trabajo\n";
            validado = false;

        }
        if (fAgregarMantenimientoVehiculo.txtObservacionesMtto.getText().equals("")) {
            fAgregarMantenimientoVehiculo.mensaje += "No se ha especificado observacion alguna\n";
            validado = false;
        }

        return validado;
    }

private void agregarFactura() {
        if (fAgregarMantenimientoVehiculo.listaDeProductos.size() < 1) {
            JOptionPane.showInternalMessageDialog(this.fAgregarMantenimientoVehiculo, "No puede agregar la factura # " + fAgregarMantenimientoVehiculo.txtNumeroFactura.getText() + ", no tiene prouctos asignados.", "Sin Productos", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int x = JOptionPane.showConfirmDialog(this.fAgregarMantenimientoVehiculo, "Desea Agregar el registro de la  factura  # " + fAgregarMantenimientoVehiculo.txtNumeroFactura.getText() + " ?", "Agregar registro", JOptionPane.YES_NO_OPTION);

        if (x == JOptionPane.YES_OPTION) {
            fAgregarMantenimientoVehiculo.jtpGastosDeMantenimiento.setEnabledAt(fAgregarMantenimientoVehiculo.jtpGastosDeMantenimiento.indexOfComponent(fAgregarMantenimientoVehiculo.pnlProveedor), true);
            fAgregarMantenimientoVehiculo.jtpGastosDeMantenimiento.setSelectedIndex(fAgregarMantenimientoVehiculo.jtpGastosDeMantenimiento.indexOfComponent(fAgregarMantenimientoVehiculo.pnlProveedor));

            llenarJtableFacturaMtto();

            fAgregarMantenimientoVehiculo.gastosFlota = null;
            fAgregarMantenimientoVehiculo.listaDeProductos = null;

            /*panel de Mantenimiento*/
            fAgregarMantenimientoVehiculo.txtPlaca.setEnabled(false);


            /*Panel Factura*/
            fAgregarMantenimientoVehiculo.txtNumeroFactura.setText("");
            fAgregarMantenimientoVehiculo.txtNumeroOrdenFactura.setText("");
            //txtPlacaFactura.setText("");
            //txtkilometrajeFactura.setText("");
            fAgregarMantenimientoVehiculo.btnAgregarProducto.setEnabled(false);
            fAgregarMantenimientoVehiculo.lblValorFactura.setText("$0.0");

            fAgregarMantenimientoVehiculo.txtNumeroFactura.setEnabled(false);
            fAgregarMantenimientoVehiculo.txtNumeroOrdenFactura.setEnabled(false);
           fAgregarMantenimientoVehiculo. txtPlacaFactura.setEnabled(false);
            fAgregarMantenimientoVehiculo.txtkilometrajeFactura.setEnabled(false);
            fAgregarMantenimientoVehiculo.dateFechaIngresoFactura.setEnabled(false);
            fAgregarMantenimientoVehiculo.btnAgregarProducto.setEnabled(false);

            /*Panel proveedor*/
            fAgregarMantenimientoVehiculo.txtNombreProveedor.setText("");
            fAgregarMantenimientoVehiculo.txtDireccionProveedor.setText("");
            fAgregarMantenimientoVehiculo.txtBarrioProveedor.setText("");
            fAgregarMantenimientoVehiculo.txtCelularProveedor.setText("");
            fAgregarMantenimientoVehiculo.txtContactoProveedor.setText("");
            fAgregarMantenimientoVehiculo.txtTelefonoProveedor.setText("");

            fAgregarMantenimientoVehiculo.txtNombreProveedor.setEnabled(false);

            fAgregarMantenimientoVehiculo.txtDireccionProveedor.setEnabled(false);
            fAgregarMantenimientoVehiculo.txtBarrioProveedor.setEnabled(false);
            fAgregarMantenimientoVehiculo.txtCelularProveedor.setEnabled(false);
            fAgregarMantenimientoVehiculo.txtContactoProveedor.setEnabled(false);
            fAgregarMantenimientoVehiculo.txtTelefonoProveedor.setEnabled(false);
        }
    }


        private void llenarJtableFacturaMtto() {

        try {
            /*Se asignan los vaores al objeto gastoFlota*/

 /*Busca y asigna el codigo de la Sucursal*/
            for (SucursalesPorproveedor prov : ini.getListaDeSucursales()) {
                if (prov.getNombreSucursal().equals(fAgregarMantenimientoVehiculo.txtNombreProveedor.getText().trim())) {
                    fAgregarMantenimientoVehiculo.gastosFlota.setSucursalProveedor("" + prov.getIdSucursal());
                    fAgregarMantenimientoVehiculo.gastosFlota.setIdProveedor(prov.getCedula());
                    fAgregarMantenimientoVehiculo.gastosFlota.setNombreSucursalProveedor(prov.getNombreSucursal());
                    fAgregarMantenimientoVehiculo.gastosFlota.setNombreProveedor(prov.getNombreProveedor());

                    break;
                }

            }

            fAgregarMantenimientoVehiculo.gastosFlota.setNumeroDeOrden(fAgregarMantenimientoVehiculo.txtNumeroOrdenFactura.getText().trim());
            fAgregarMantenimientoVehiculo.gastosFlota.setNumeroFactura(fAgregarMantenimientoVehiculo.txtNumeroFactura.getText().trim());
            fAgregarMantenimientoVehiculo.gastosFlota.setVehiculo(fAgregarMantenimientoVehiculo.txtPlaca.getText().trim());
            fAgregarMantenimientoVehiculo.gastosFlota.setConductor(fAgregarMantenimientoVehiculo.conductor.getCedula());
            fAgregarMantenimientoVehiculo.gastosFlota.setNombreConductor(fAgregarMantenimientoVehiculo.conductor.getNombres() + " " + fAgregarMantenimientoVehiculo.conductor.getApellidos());
            fAgregarMantenimientoVehiculo.gastosFlota.setKilometraje(fAgregarMantenimientoVehiculo.txtKmActual.getText().trim());

            double suma = 0.0;
            for (GastosPorVehiculo item : fAgregarMantenimientoVehiculo.listaDeProductos) {
                suma += Double.parseDouble(item.getValorTotal());
            }
            fAgregarMantenimientoVehiculo.gastosFlota.setValorfactura("" + suma);

            Date dt = new Date();
            dt = ini.getFechaSql(fAgregarMantenimientoVehiculo.dateFechaIngresoFactura);

            fAgregarMantenimientoVehiculo.gastosFlota.setFechaFactura("" + dt);

            for (CAgencias agencia : ini.getListaDeAgencias()) {
                if (agencia.getIdAgencia() == fAgregarMantenimientoVehiculo.carro.getAgencia()) {
                    fAgregarMantenimientoVehiculo.gastosFlota.setAgencia("" + agencia.getIdAgencia());
                    fAgregarMantenimientoVehiculo.gastosFlota.setRegional("" + agencia.getIdRegional());
                    fAgregarMantenimientoVehiculo.gastosFlota.setZona("" + agencia.getIdZona());

                    break;
                }

            }
            fAgregarMantenimientoVehiculo.gastosFlota.setFormatoFotografia(".jpg");
            fAgregarMantenimientoVehiculo.gastosFlota.setActivo("" + 1);
            fAgregarMantenimientoVehiculo.gastosFlota.setFlag("" + 1);
            fAgregarMantenimientoVehiculo.gastosFlota.setUsuario(Inicio.deCifrar(ini.getUser().getNombreUsuario()));

            limpiarTablaListaDeFacturas();

            /*se asignan los productos y servicios a la factura*/
            fAgregarMantenimientoVehiculo.gastosFlota.setListaGastosPorVehiculo(fAgregarMantenimientoVehiculo.listaDeProductos);

            fAgregarMantenimientoVehiculo.listaGastosFlota.add(fAgregarMantenimientoVehiculo.gastosFlota);

            fAgregarMantenimientoVehiculo.modeloFacturas = (DefaultTableModel) fAgregarMantenimientoVehiculo.tblListaDeFacturas.getModel();

            for (GastosFlota fac : fAgregarMantenimientoVehiculo.listaGastosFlota) {

                /* se anexa el registro a la Jtable de facturas por manifiesto */
                //modeloFacturas = (DefaultTableModel) tblListaDeFacturas.getModel();
                int filaTabla2 = fAgregarMantenimientoVehiculo.tblListaDeFacturas.getRowCount();

                fAgregarMantenimientoVehiculo.modeloFacturas.addRow(new Object[fAgregarMantenimientoVehiculo.tblListaDeFacturas.getRowCount()]);

                fAgregarMantenimientoVehiculo.tblListaDeFacturas.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);
                fAgregarMantenimientoVehiculo.tblListaDeFacturas.setValueAt(fac.getNumeroFactura(), filaTabla2, 1);
                fAgregarMantenimientoVehiculo.tblListaDeFacturas.setValueAt(fac.getNombreSucursalProveedor(), filaTabla2, 2);
                fAgregarMantenimientoVehiculo.tblListaDeFacturas.setValueAt(fAgregarMantenimientoVehiculo.gastosFlota.getNombreConductor(), filaTabla2, 3);
                fAgregarMantenimientoVehiculo.tblListaDeFacturas.setValueAt(nf.format(Double.parseDouble(fac.getValorfactura())), filaTabla2, 4);

            }

            fAgregarMantenimientoVehiculo.btnNuevoMantenimiento.setEnabled(false);
            fAgregarMantenimientoVehiculo.btnGrabarMantenimiento.setEnabled(true);
            fAgregarMantenimientoVehiculo.jbtnImprimir.setEnabled(false);

            fAgregarMantenimientoVehiculo.btnAceptarFactura.setEnabled(false);
            fAgregarMantenimientoVehiculo.jBtnAgregarFActura.setEnabled(true);
            fAgregarMantenimientoVehiculo.btnEliminarFila.setEnabled(false);
            fAgregarMantenimientoVehiculo.btnCancelarFactura.setEnabled(false);

            limpiarTablaDetalleFactura();

            fAgregarMantenimientoVehiculo.jBtnAgregarFActura.requestFocus();
            fAgregarMantenimientoVehiculo.jBtnAgregarFActura.requestFocus();
        } catch (Exception ex) {
            Logger.getLogger(FAgregarMantenimientoVehiculo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void limpiarTablaListaDeFacturas() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) fAgregarMantenimientoVehiculo.tblListaDeFacturas.getModel();
            if (modelo.getRowCount() > 0) {
                int a = modelo.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    modelo.removeRow(i);
                }
            }

        } catch (Exception ex) {
        }

    }


    private void limpiarTablaDetalleFactura() {
        try {
            DefaultTableModel modelo = (DefaultTableModel) fAgregarMantenimientoVehiculo.tblDetalleFactura.getModel();
            if (modelo.getRowCount() > 0) {
                int a = modelo.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    modelo.removeRow(i);
                }
            }

        } catch (Exception ex) {
        }

    }

   
    public void limpiarOperacion() {

        fAgregarMantenimientoVehiculo.tblDetalleFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.tblListaDeFacturas.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtBarrioProveedor.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtCelularProveedor.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtContactoProveedor.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtDireccionProveedor.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtIdConductorActualMtto.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtKmActual.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtKmCambioAnteriorMtto.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtKmFrecuenciaMtto.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtNombreConductorMantenimientoAnterior.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtNombreConductorActual.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtNumeroFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtNumeroOrdenFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtObservacionesMtto.setEnabled(false);

        fAgregarMantenimientoVehiculo.dateFechaCambioAceiteMtto.setEnabled(false);
        fAgregarMantenimientoVehiculo.dateFechaIngresoFactura.setEnabled(false);

        fAgregarMantenimientoVehiculo.btCancelarOperacion.setEnabled(false);
        fAgregarMantenimientoVehiculo.jbtnImprimir.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnAgregarProducto.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnEliminarFila.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnGrabarMantenimiento.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnAceptarFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnCancelarFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnNuevoMantenimiento.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnSalir.setEnabled(false);

        fAgregarMantenimientoVehiculo.txtPlaca.setText("");
        fAgregarMantenimientoVehiculo.txtTipoVehiculoMtto.setText("");
        fAgregarMantenimientoVehiculo.txtBarrioProveedor.setText("");
        fAgregarMantenimientoVehiculo.txtCelularProveedor.setText("");
        fAgregarMantenimientoVehiculo.txtContactoProveedor.setText("");
        fAgregarMantenimientoVehiculo.txtDireccionProveedor.setText("");
        fAgregarMantenimientoVehiculo.txtIdConductorActualMtto.setText("");
        fAgregarMantenimientoVehiculo.txtKmActual.setText("");
        fAgregarMantenimientoVehiculo.txtKmCambioAnteriorMtto.setText("");
        fAgregarMantenimientoVehiculo.txtKmFrecuenciaMtto.setText("");
        fAgregarMantenimientoVehiculo.txtNombreConductorMantenimientoAnterior.setText("");
        fAgregarMantenimientoVehiculo.txtNombreConductorActual.setText("");
        fAgregarMantenimientoVehiculo.txtOrdenNumero.setText("");

        fAgregarMantenimientoVehiculo.txtNumeroFactura.setText("");
        fAgregarMantenimientoVehiculo.txtNumeroOrdenFactura.setText("");
        fAgregarMantenimientoVehiculo.txtObservacionesMtto.setText("");
        fAgregarMantenimientoVehiculo.txtPlacaFactura.setText("");

        fAgregarMantenimientoVehiculo.btnAgregarProducto.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnGrabarMantenimiento.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnNuevoMantenimiento.setEnabled(true);
        fAgregarMantenimientoVehiculo.actualizar = false;

        fAgregarMantenimientoVehiculo.sucursalProveedor = null;
        fAgregarMantenimientoVehiculo.carro = null;
        fAgregarMantenimientoVehiculo.conductor = null;

        fAgregarMantenimientoVehiculo.listaDeProductos = null;
        fAgregarMantenimientoVehiculo.listaGastosFlota = null;

        fAgregarMantenimientoVehiculo.placaElegida = false;
        fAgregarMantenimientoVehiculo.conductorElegido = false;
        fAgregarMantenimientoVehiculo.proveedroElegido = false;

        fAgregarMantenimientoVehiculo.validado = false;
        fAgregarMantenimientoVehiculo.cargado = false;

        fAgregarMantenimientoVehiculo.caso = 0;

        fAgregarMantenimientoVehiculo.lblValorFactura.setText("$0.0");

        limpiarTablaDetalleFactura();
        limpiarTablaListaDeFacturas();

    }

     private void cancelarOperacion() {
        limpiarOperacion();
        fAgregarMantenimientoVehiculo.btnNuevoMantenimiento.setEnabled(true);
        fAgregarMantenimientoVehiculo.btnSalir.setEnabled(true);
    }
   
     
    public void limpiarFacturas() {

        fAgregarMantenimientoVehiculo.tblDetalleFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.tblListaDeFacturas.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtBarrioProveedor.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtCelularProveedor.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtContactoProveedor.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtDireccionProveedor.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtIdConductorActualMtto.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtKmActual.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtKmCambioAnteriorMtto.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtKmFrecuenciaMtto.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtNombreConductorMantenimientoAnterior.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtNombreConductorActual.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtNumeroFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtNumeroOrdenFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtObservacionesMtto.setEnabled(false);

        fAgregarMantenimientoVehiculo.dateFechaCambioAceiteMtto.setEnabled(false);
        fAgregarMantenimientoVehiculo.dateFechaIngresoFactura.setEnabled(false);

        fAgregarMantenimientoVehiculo.btCancelarOperacion.setEnabled(false);
        fAgregarMantenimientoVehiculo.jbtnImprimir.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnAgregarProducto.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnEliminarFila.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnGrabarMantenimiento.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnAceptarFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnCancelarFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnNuevoMantenimiento.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnSalir.setEnabled(false);

        fAgregarMantenimientoVehiculo.txtBarrioProveedor.setText("");
        fAgregarMantenimientoVehiculo.txtCelularProveedor.setText("");
        fAgregarMantenimientoVehiculo.txtContactoProveedor.setText("");
        fAgregarMantenimientoVehiculo.txtDireccionProveedor.setText("");
        fAgregarMantenimientoVehiculo.txtIdConductorActualMtto.setText("");
        fAgregarMantenimientoVehiculo.txtKmActual.setText("");
        fAgregarMantenimientoVehiculo.txtKmCambioAnteriorMtto.setText("");
        fAgregarMantenimientoVehiculo.txtKmFrecuenciaMtto.setText("");
        fAgregarMantenimientoVehiculo.txtNombreConductorMantenimientoAnterior.setText("");
        fAgregarMantenimientoVehiculo.txtNombreConductorActual.setText("");
        fAgregarMantenimientoVehiculo.txtNumeroFactura.setText("");
        fAgregarMantenimientoVehiculo.txtNumeroOrdenFactura.setText("");

        fAgregarMantenimientoVehiculo.btnAgregarProducto.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnGrabarMantenimiento.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnNuevoMantenimiento.setEnabled(true);
        fAgregarMantenimientoVehiculo.actualizar = false;

        fAgregarMantenimientoVehiculo.listaDeProductos = null;
        fAgregarMantenimientoVehiculo.listaGastosFlota = null;

        fAgregarMantenimientoVehiculo.lblValorFactura.setText("$0.0");

        limpiarTablaDetalleFactura();
        limpiarTablaListaDeFacturas();

    }

     private void cancelarFactura() {

        /*Panel Factura*/
        fAgregarMantenimientoVehiculo.txtNumeroFactura.setText("");
        fAgregarMantenimientoVehiculo.txtNumeroOrdenFactura.setText("");
        //txtPlacaFactura.setText("");
        //txtkilometrajeFactura.setText("");
        fAgregarMantenimientoVehiculo.btnAgregarProducto.setEnabled(false);
        fAgregarMantenimientoVehiculo.lblValorFactura.setText("$0.0");

        fAgregarMantenimientoVehiculo.txtNumeroFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtNumeroOrdenFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtPlacaFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtkilometrajeFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.dateFechaIngresoFactura.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnAgregarProducto.setEnabled(false);


        /*Panel proveedor*/
        fAgregarMantenimientoVehiculo.txtNombreProveedor.setText("");
        fAgregarMantenimientoVehiculo.txtDireccionProveedor.setText("");
        fAgregarMantenimientoVehiculo.txtBarrioProveedor.setText("");
        fAgregarMantenimientoVehiculo.txtCelularProveedor.setText("");
        fAgregarMantenimientoVehiculo.txtContactoProveedor.setText("");
        fAgregarMantenimientoVehiculo.txtTelefonoProveedor.setText("");

        fAgregarMantenimientoVehiculo.txtNombreProveedor.setEnabled(false);

        fAgregarMantenimientoVehiculo.txtDireccionProveedor.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtBarrioProveedor.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtCelularProveedor.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtContactoProveedor.setEnabled(false);
        fAgregarMantenimientoVehiculo.txtTelefonoProveedor.setEnabled(false);

        limpiarTablaDetalleFactura();
        fAgregarMantenimientoVehiculo.listaDeProductos = null;
        fAgregarMantenimientoVehiculo.gastosFlota = null;
        fAgregarMantenimientoVehiculo.jbtnImprimir.setEnabled(true);

    }


       private void eliminarFacturaDelaTabla() {

        int x = fAgregarMantenimientoVehiculo.tblListaDeFacturas.getSelectedRow();
        String numeroFactura = (String) fAgregarMantenimientoVehiculo.modeloFacturas.getValueAt(x, 1);
        String proveedor = (String) fAgregarMantenimientoVehiculo.modeloFacturas.getValueAt(x, 2);

        /*Se busca objeto factura en la lista para eliminarla*/
        for (GastosFlota fac : fAgregarMantenimientoVehiculo.listaGastosFlota) {
            if (numeroFactura.equals(fac.getNumeroFactura()) && proveedor.equals(fac.getNombreSucursalProveedor())) {

                /*La factura es encontrada en la lista y se elimina*/
                fAgregarMantenimientoVehiculo.listaGastosFlota.remove(fac);
            }

        }

        /*Se elimina la fila de la tabla que contiene la factura*/
        fAgregarMantenimientoVehiculo.modeloFacturas.removeRow(x);

        limpiarTablaListaDeFacturas();

        for (GastosFlota fac : fAgregarMantenimientoVehiculo.listaGastosFlota) {

            /* se anexa el registro a la Jtable de facturas por manifiesto */
            //modeloFacturas = (DefaultTableModel) tblListaDeFacturas.getModel();
            int filaTabla2 = fAgregarMantenimientoVehiculo.tblListaDeFacturas.getRowCount();

            fAgregarMantenimientoVehiculo.modeloFacturas.addRow(new Object[fAgregarMantenimientoVehiculo.tblListaDeFacturas.getRowCount()]);

            fAgregarMantenimientoVehiculo.tblListaDeFacturas.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);
            fAgregarMantenimientoVehiculo.tblListaDeFacturas.setValueAt(fac.getNumeroFactura(), filaTabla2, 1);
            fAgregarMantenimientoVehiculo.tblListaDeFacturas.setValueAt(fac.getNombreProveedor(), filaTabla2, 2);
            fAgregarMantenimientoVehiculo.tblListaDeFacturas.setValueAt(fac.getConductor(), filaTabla2, 3);
            fAgregarMantenimientoVehiculo.tblListaDeFacturas.setValueAt(nf.format(fac.getValorfactura()), filaTabla2, 4);

        }

       fAgregarMantenimientoVehiculo.btnEliminarFila.setEnabled(false);
        cancelarFactura();
    }

       
   private void eliminarFilaTblProductos() {

        int x = fAgregarMantenimientoVehiculo.tblDetalleFactura.getSelectedRow();

        String cuenta = (String) fAgregarMantenimientoVehiculo.modeloDetalleFactura.getValueAt(x, 1);
        String producto = (String) fAgregarMantenimientoVehiculo.modeloDetalleFactura.getValueAt(x, 2);

        /*Se busca objeto factura en la lista para eliminarla*/
        for (GastosPorVehiculo itm : fAgregarMantenimientoVehiculo.listaDeProductos) {

            if (cuenta.equals(itm.getNombreCuentaSecundaria()) && producto.equals(itm.getDescripcionProductoServicio())) {

                /*La factura es encontrada en la lista y se elimina*/
                fAgregarMantenimientoVehiculo.listaDeProductos.remove(itm);
                break;
            }

        }

        /*Se elimina la fila de la tabla que contiene la factura*/
        fAgregarMantenimientoVehiculo.modeloDetalleFactura.removeRow(x);

        limpiarTablaDetalleFactura();
        double valor = 0.0;

        for (GastosPorVehiculo itm : fAgregarMantenimientoVehiculo.listaDeProductos) {

            /* se anexa el registro a la Jtable de facturas por manifiesto */
            //modeloFacturas = (DefaultTableModel) tblListaDeFacturas.getModel();
            int filaTabla2 = fAgregarMantenimientoVehiculo.tblDetalleFactura.getRowCount();

            fAgregarMantenimientoVehiculo.modeloDetalleFactura.addRow(new Object[fAgregarMantenimientoVehiculo.tblDetalleFactura.getRowCount()]);

            fAgregarMantenimientoVehiculo.tblDetalleFactura.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);
            fAgregarMantenimientoVehiculo.tblDetalleFactura.setValueAt(itm.getNombreCuentaSecundaria(), filaTabla2, 1);
            fAgregarMantenimientoVehiculo.tblDetalleFactura.setValueAt(itm.getDescripcionProductoServicio(), filaTabla2, 2);
            fAgregarMantenimientoVehiculo.tblDetalleFactura.setValueAt(itm.getCantidad(), filaTabla2, 3);
            fAgregarMantenimientoVehiculo.tblDetalleFactura.setValueAt(nf.format(itm.getValorUnitario()), filaTabla2, 4);
            //form.tblDetalleFactura.setValueAt(nf.format(facturaActual.getValorTotalFactura()), filaTabla2, 3);
            fAgregarMantenimientoVehiculo.tblDetalleFactura.setValueAt(nf.format(itm.getValorTotal()), filaTabla2, 5);

            valor += Double.parseDouble(itm.getValorTotal());
        }
        fAgregarMantenimientoVehiculo.lblValorFactura.setText("" + nf.format(valor));

        fAgregarMantenimientoVehiculo.btnEliminarProducto.setEnabled(false);
        fAgregarMantenimientoVehiculo.btnAgregarProducto.setEnabled(true);
    }


    private void asignarValoresgastosFlota() {
        fAgregarMantenimientoVehiculo.gastosFlota = new GastosFlota(ini);
        fAgregarMantenimientoVehiculo.gastosFlota.setActivo("1");
        fAgregarMantenimientoVehiculo.gastosFlota.setAgencia("" + fAgregarMantenimientoVehiculo.carro.getAgencia());
        fAgregarMantenimientoVehiculo.gastosFlota.setConductor(fAgregarMantenimientoVehiculo.conductor.getCedula());

        Date dt = new Date();
        dt = ini.getFechaSql(fAgregarMantenimientoVehiculo.dateFechaIngresoFactura);

        fAgregarMantenimientoVehiculo.gastosFlota.setFechaFactura("" + dt);
        // -gastosFlota.setFechaIng(mensaje);
        fAgregarMantenimientoVehiculo.gastosFlota.setFlag("1");
        fAgregarMantenimientoVehiculo.gastosFlota.setFormatoFotografia(".jpg");
        fAgregarMantenimientoVehiculo.gastosFlota.setIdGastoFlota(fAgregarMantenimientoVehiculo.texto);
        fAgregarMantenimientoVehiculo.gastosFlota.setIdProveedor(fAgregarMantenimientoVehiculo.sucursalProveedor.getCedula());
        fAgregarMantenimientoVehiculo.gastosFlota.setKilometraje("" + fAgregarMantenimientoVehiculo.carro.getKilometrajeActual());
        fAgregarMantenimientoVehiculo.gastosFlota.setNombreConductor(fAgregarMantenimientoVehiculo.conductor.getNombres() + " " + fAgregarMantenimientoVehiculo.conductor.getApellidos());
        fAgregarMantenimientoVehiculo.gastosFlota.setNombreProveedor(fAgregarMantenimientoVehiculo.sucursalProveedor.getNombreProveedor());
        fAgregarMantenimientoVehiculo.gastosFlota.setNombreSucursalProveedor(fAgregarMantenimientoVehiculo.sucursalProveedor.getNombreSucursal());
        fAgregarMantenimientoVehiculo.gastosFlota.setNumeroDeOrden("" +fAgregarMantenimientoVehiculo. mantenimientosPorPlaca.getIdNumeroDeorden());
        fAgregarMantenimientoVehiculo.gastosFlota.setNumeroFactura(fAgregarMantenimientoVehiculo.txtNumeroFactura.getText());
    }

}
