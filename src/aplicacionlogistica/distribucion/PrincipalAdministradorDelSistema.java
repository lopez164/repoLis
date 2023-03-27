/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion;

import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import aplicacionlogistica.distribucion.formularios.FHabilitarFacturas;
import aplicacionlogistica.distribucion.formularios.FAnularFacturas;
import aplicacionlogistica.distribucion.formularios.FTrasladoDeFacturas;
import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.distribucion.clientes.FGeoreferenciarClientes;
import aplicacionlogistica.configuracion.CambiarClave;
import aplicacionlogistica.configuracion.HiloBitacoraMovimientosEnElSistema;
import aplicacionlogistica.distribucion.consultas.FConsultarManifiestos;
import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcel;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.formularios.FModificarClaveOlvidada;
import aplicacionlogistica.costumerService.FConsultaManifiestosDescargados;
import aplicacionlogistica.costumerService.Reportes.FReporteClientesConDescuento;
import aplicacionlogistica.costumerService.Reportes.FReporteDescuentos_Recogidas;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.FCrearCargos;
//import aplicacionlogistica.distribucion.administracion.Vehiculos.FCrearMarcasLLantas;
import mtto.vehiculos.Administracion.FCrearMarcasVehiculos;
import mtto.vehiculos.Administracion.FCrearTiposCarrocerias;
import mtto.vehiculos.Administracion.FCrearTiposdeCombustible;
import mtto.vehiculos.Administracion.FCrearTiposdeContratosVehiculos;
import mtto.vehiculos.Administracion.FCrearTiposdeDocumentos;
import mtto.vehiculos.Administracion.FCrearTiposdeServicios;
import mtto.vehiculos.Administracion.FCrearTiposdeVehiculos;
import aplicacionlogistica.distribucion.consultas.FConsultaManifiestosSinDescargar;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasPorCliente;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.consultas.FHabilitarManifiesto;
import aplicacionlogistica.distribucion.formularios.FAdicionarPedidosNoReportados;
import aplicacionlogistica.distribucion.formularios.FAnularFacturaSinMovimiento;
import aplicacionlogistica.distribucion.formularios.FCrearFacturasDeVenta;
import aplicacionlogistica.distribucion.formularios.Hielera.DescargarFacturas_2;
import aplicacionlogistica.distribucion.formularios.Hielera.FManifestarPedidosHielera_2;
import aplicacionlogistica.distribucion.formularios.ReporteManifiestosSinDescargar.FReporteManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.administracion.FBorrarFacturaManifiesto;
import aplicacionlogistica.distribucion.administracion.logistica.FCrearRutasDeDistribucion;
import aplicacionlogistica.distribucion.formularios.FModificarManifiesto;
import aplicacionlogistica.distribucion.formularios.Hielera.FControlarSalidaFacturasBarCode2;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultaPedidosDescargadosPorConductorHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarPedidosConductorHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarProducto;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarCarros;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarEmpleados;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarUsuarios;
import aplicacionlogistica.distribucion.formularios.poblaciones.FManifestarPedidosPoblaciones;
import aplicacionlogistica.distribucion.formularios.reportes.FDashBoardFacturas;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteAlertasSalidaDeProductos;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteDePorteria;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteDeRechazosTotales;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteFacturasPendientes;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteManifiestosConcilidos;
import aplicacionlogistica.distribucion.formularios.reportes.FReportePedidosMovilizadosPorPeriodo;
import aplicacionlogistica.distribucion.formularios.reportes.FReportePedidosEnDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteManifiestosEnDistribucion.FReporteManifiestosEnDistribucion;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteMovilizadoPorConductor.FReportemovilizadoPorConductor;
import aplicacionlogistica.distribucion.formularios.reportes.facturacion.FReporteFacturacionPorCanal;
import aplicacionlogistica.distribucion.formularios.reportes.facturacion.FReporteFacturacionTAT;
import aplicacionlogistica.distribucion.integrador.FIntegrador;
import aplicacionlogistica.distribucion.integrador.HiloIntegradorTNS;
import mtto.personal.FReporteDelPersonal;
import aplicacionlogistica.picking.FManifestarFacturasEnPicking;
import aplicacionlogistica.picking.FRegistroDePedidosFinLineaDePicking;
import aplicacionlogistica.picking.FReporteFacturasPendientesEnPicking;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import mtto.Servicios.FAgregarMantenimientoVehiculo;
import mtto.documentos.FIngresarDocumentosVehiculos;
import mtto.documentos.Reportes.FReporteDocumentosVehiculos;
import mtto.ingresoDeRegistros.IngresarFacturasDeGastos;
import mtto.personal.FReporteDeVehiculos;
import mtto.reportes.FReporteGastosFlota;
import mtto.vehiculos.Administracion.FCrearMarcasDeLLantas;

/**
 *
 * @author VLI_488
 */
public class PrincipalAdministradorDelSistema extends javax.swing.JFrame {

    public Inicio ini = null;
    public CambiarClave fCambiarClave;
    public DescargarFacturas fDescargarFActuras;
    //public FManifestarFacturasEnPicking fManifestarPedidos;

    public IngresarEmpleados fIngresarEmpleados;
    public IngresarCarros fIngresaarCArros;
    public FHabilitarFacturas fHabilitarFacturas;
    public FAnularFacturas fAnularFacturas;
    FAnularFacturaSinMovimiento fAnularFacturaSinMovimiento = null;

    public FConsultarFacturasRemoto fConsultarFacturasRemoto;
    public FConsultarFacturasPorCliente fConsultarFacturasPorCliente;
    public FConsultarManifiestos fConsultarManifiestos;

    public FTrasladoDeFacturas fTrasladoDePedidos;
    public FHabilitarManifiesto fHabilitarManifiesto;
    public FImportarArchivoExcel fImportarExcel;

    public FReporteManifiestosConcilidos fReporteManifiestosConcilidos = null;
    public FReporteDeRechazosTotales fReporteRechazosTotales = null;
    public FReportePedidosMovilizadosPorPeriodo fReporteMovilizado = null;
    public FReporteManifiestosEnDistribucion fReporteManifiestosEnDistribucion = null;
    //public FReporteFacturasSinMovimientos fReporteFacturasPendientes = null;
    public FReporteFacturasPendientes fReporteFacturasPendientes = null;
    public FReporteFacturacionPorCanal fReporteFacturacionPorCanal = null;
    public FReporteFacturacionTAT fReporteFacturacionTAT = null;
    public FReporteManifiestosSinDescargar freporteManifiestosSinDescargar = null;
    public FBorrarFacturaManifiesto fDesvincularFactura = null;
    public FReporteFacturasPendientesEnPicking fReporteFacturasPendientesEnPicking = null;
    public FReporteDescuentos_Recogidas fReporteDescuentos_Recogidas = null;
    FReportePedidosEnDistribucion fReportePedidosEnDistribucion = null;

    public FManifestarFacturasEnPicking fFacturasEnPicking = null;
    public FManifestarPedidosEnRuta fManifestarPedidosEnRuta = null;
    public FRegistroDePedidosFinLineaDePicking fRegistroDePedidosFinLineaDePicking = null;
    public FGeoreferenciarClientes fGeoreferenciarClientes = null;
    public FAdicionarPedidosNoReportados fAdicionarPedidosNoReportados = null;

    public FReporteGastosFlota fReporteGastosFlota;
    public FAgregarMantenimientoVehiculo fAgregarMantenimientoVehiculo;
    public FIngresarDocumentosVehiculos fIngresarDocumentosVehiculos;
    public IngresarFacturasDeGastos ingresarFacturasDeGastos;
    public FReporteDePorteria fReporteDePorteria;

    String rutaImagenDeFondo = null;

    List<String> datos = new ArrayList<>();
    String usuario;
    String formulario = "PrincipalAdministradorDelSistema";
    ActionListener actionListener;
    public Timer timer;

    public PrincipalAdministradorDelSistema() throws Exception {
        String rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
        ini = new Inicio(new File(rutaDeArchivo + "ReadUs.ini"));
        usuario = Inicio.deCifrar(ini.getUser().getNombreUsuario());
        CUsuarios user = new CUsuarios(ini, "79481521");
        ini.setUser(user);

        initComponents();

        this.setSize((int) ini.getDimension().getWidth(), (int) ini.getDimension().getHeight() - 43);
        this.setLocation(0, 0);

        /*SE instsancian los formularios del menu */
        // new Thread(new HiloCargarFormulariosAuxiliarLogistica(this)).start();
    }

    public PrincipalAdministradorDelSistema(Inicio ini) throws Exception, Throwable {

        //initComponents();
        this.ini = ini;
        String ruta;
        usuario = Inicio.deCifrar(ini.getUser().getNombreUsuario());
        if (ini.isClienteActivo()) {
            //if (ya) {
            initComponents();
            int tiempoDeEperaBloqueo = 60000 * (Integer.parseInt(ini.getPropiedades().getProperty("tiempoDeEperaBloqueo")));

            actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    timerEvent(actionEvent);
                    //System.out.println("expired");

                }

            };
            timer = new Timer(tiempoDeEperaBloqueo, actionListener);
            timer.start();

            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                jMenu14.setVisible(true);
                mnuDescargueHielera.setVisible(false);
            }
            if (ini.getPropiedades().getProperty("idOperador").equals("2")) {
                jMenu14.setVisible(false);
                mnuDescargueHielera.setVisible(true);
                mnuFlota.setVisible(true);
            }

            try {
                FDashBoardFacturas fDashBoardFacturas = new FDashBoardFacturas(ini);
                this.escritorio.add(fDashBoardFacturas);
                fDashBoardFacturas.setVisible(true);
                fDashBoardFacturas.show();
                ini.cargarImagenEscritorio(escritorio);
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Revisar archivo de configuracion", "Imagen de Fonfo no encontrada", JOptionPane.ERROR_MESSAGE);
            }

            //this.escritorio=this.ini.getEscritorio();
            fCambiarClave = new CambiarClave(ini);

            /*SE instsancian los formularios del menu */
            //  new Thread(new HiloCargarFormulariosAuxiliarLogistica(this)).start();
            /*MÉTODO PARA CAMBIAR LA CLAVE, CUANDO SE INGRSA POR PRIMERA VEZ AL 
            SISTEMA*/
            if (this.ini.getUser().getClaveUsuario().equals(Inicio.cifrar("123456"))) { // cuando el usuario es nuevo
                //this.escritorio.setSize(600, 400);

                if (fCambiarClave != null) {
                    this.escritorio.add(fCambiarClave);
                    fCambiarClave.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));

                    fCambiarClave.setLocation((ini.getDimension().width - fCambiarClave.getSize().width) / 2, (ini.getDimension().height - fCambiarClave.getSize().height) / 2);
                    jMenuBar1.setVisible(false);
                    fCambiarClave.setResizable(false);
                    fCambiarClave.show();
                    datos = new ArrayList<>();
                    fCambiarClave.setVisible(true);
                    fCambiarClave.txtClaveAnterior.requestFocus();
                    datos = new ArrayList<>();

                    datos.add(0, usuario);
                    datos.add(1, formulario);
                    datos.add(2, "Click Formulario CambiarClave");
                    datos.add(3, "CURRENT_TIMESTAMP");
                    new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                    if (timer.isRunning()) {
                        timer.stop();
                    }
                    timer.start();

                }

            } else {

//                /*SE instancian los formularios del menu */
//                new Thread(new HiloListadoDeTiposDeMantenimiento(this.ini)).start();
//                new Thread(new HiloListadoDeProveedores(this.ini)).start();
//                new Thread(new HiloListadoDeSucursales(this.ini)).start();
//                new Thread(new HiloListadoDeTiposDeDocumentos(this.ini)).start();
//                new Thread(new HiloListadoDeCarrosPropios(this.ini)).start();
//                new Thread(new HiloListadoDeCuentasSecundarias(this.ini)).start();
//                new Thread(new HiloListadoDeDepartamentos(this.ini)).start();
//                new Thread(new HiloListadoDeAgencias(this.ini)).start();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Estimado usuario la licencia ha caducado, consulte con el administrador", "Cliente no activo", 1);
            System.exit(0);
        }
    }

    /**
     * This method is called from within the constructor to initialize the
     * fCambiarClave. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        escritorio = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuArchivo = new javax.swing.JMenu();
        menuCambiarClave = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        mnuFlota = new javax.swing.JMenu();
        jMenu17 = new javax.swing.JMenu();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenu18 = new javax.swing.JMenu();
        jMenu19 = new javax.swing.JMenu();
        jMenu20 = new javax.swing.JMenu();
        jMenu21 = new javax.swing.JMenu();
        jMenu22 = new javax.swing.JMenu();
        jMenu23 = new javax.swing.JMenu();
        miMantenimientos = new javax.swing.JMenuItem();
        jMenu24 = new javax.swing.JMenu();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenu25 = new javax.swing.JMenu();
        jMenu26 = new javax.swing.JMenu();
        jMenu27 = new javax.swing.JMenu();
        jMenuItem37 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenu32 = new javax.swing.JMenu();
        jMenuItem45 = new javax.swing.JMenuItem();
        jMenuItem48 = new javax.swing.JMenuItem();
        jMenuItem67 = new javax.swing.JMenuItem();
        jMenuItem62 = new javax.swing.JMenuItem();
        jMenuItem66 = new javax.swing.JMenuItem();
        jMenuItem47 = new javax.swing.JMenuItem();
        jMenuItem56 = new javax.swing.JMenuItem();
        jMenuItem65 = new javax.swing.JMenuItem();
        jMenuItem46 = new javax.swing.JMenuItem();
        jMenuItem61 = new javax.swing.JMenuItem();
        mnuPersonal = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        mnuCrearUsuarios = new javax.swing.JMenuItem();
        mnuCambioClave = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenu30 = new javax.swing.JMenu();
        jMenuItem43 = new javax.swing.JMenuItem();
        jMenuItem49 = new javax.swing.JMenuItem();
        jMenuItem50 = new javax.swing.JMenuItem();
        jMenuItem54 = new javax.swing.JMenuItem();
        jMenuItem63 = new javax.swing.JMenuItem();
        jMenuItem64 = new javax.swing.JMenuItem();
        jMenu31 = new javax.swing.JMenu();
        jMenuItem44 = new javax.swing.JMenuItem();
        jMenuItem51 = new javax.swing.JMenuItem();
        jMenuItem52 = new javax.swing.JMenuItem();
        jMenuItem53 = new javax.swing.JMenuItem();
        jMenuItem55 = new javax.swing.JMenuItem();
        mnuVehiculos = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        mnuManifestarPedidos = new javax.swing.JMenuItem();
        mnuDescargarFacturas = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        mnuManifestarPedidos1 = new javax.swing.JMenuItem();
        mnuDescargueHielera = new javax.swing.JMenu();
        jMenuItem69 = new javax.swing.JMenuItem();
        mnuManifestarPedidos8 = new javax.swing.JMenuItem();
        mnuManifestarPedidos9 = new javax.swing.JMenuItem();
        jMenuItem75 = new javax.swing.JMenuItem();
        jMenuItem76 = new javax.swing.JMenuItem();
        jMenuItem77 = new javax.swing.JMenuItem();
        mnuHabilitarManifiesto = new javax.swing.JMenuItem();
        mnuHabilitarFacturas = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        mnuClientes = new javax.swing.JMenu();
        imActualizarClientes = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        mnuConsultarManifiestos = new javax.swing.JMenuItem();
        jMenu15 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenuItem79 = new javax.swing.JMenuItem();
        jMenuItem80 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        mnuReportes = new javax.swing.JMenu();
        jMenu16 = new javax.swing.JMenu();
        jMenuItem38 = new javax.swing.JMenuItem();
        jMenuItem39 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenu28 = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mnu_rptFacturacionPorCanal = new javax.swing.JMenuItem();
        mnu_rptFacturacionTAT = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem81 = new javax.swing.JMenuItem();
        jMenuItem40 = new javax.swing.JMenuItem();
        jMenuItem103 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        jMenu13 = new javax.swing.JMenu();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenuItem34 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());
        setIconImages(getIconImages());

        javax.swing.GroupLayout escritorioLayout = new javax.swing.GroupLayout(escritorio);
        escritorio.setLayout(escritorioLayout);
        escritorioLayout.setHorizontalGroup(
            escritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 554, Short.MAX_VALUE)
        );
        escritorioLayout.setVerticalGroup(
            escritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 285, Short.MAX_VALUE)
        );

        jMenuBar1.setEnabled(false);

        mnuArchivo.setText("Archivo");
        mnuArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuArchivoActionPerformed(evt);
            }
        });

        menuCambiarClave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Key.png"))); // NOI18N
        menuCambiarClave.setText("Cambio de Clave");
        menuCambiarClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCambiarClaveActionPerformed(evt);
            }
        });
        mnuArchivo.add(menuCambiarClave);

        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jMenuItem6.setText("Salir");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        mnuArchivo.add(jMenuItem6);

        jMenuBar1.add(mnuArchivo);

        mnuFlota.setText("Flota");

        jMenu17.setText("Documentos");

        jMenuItem35.setText("Gestionar Doctos");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenu17.add(jMenuItem35);

        mnuFlota.add(jMenu17);

        jMenu18.setText("Gastos");
        mnuFlota.add(jMenu18);

        jMenu19.setText("Mantenimientos");
        mnuFlota.add(jMenu19);

        jMenu20.setText("Administracion");
        mnuFlota.add(jMenu20);

        jMenu21.setText("Vehículos");

        jMenu22.setText("Ingresar Vehiculos");
        jMenu22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu22ActionPerformed(evt);
            }
        });
        jMenu21.add(jMenu22);

        jMenu23.setText("Mantenimientos");

        miMantenimientos.setText("Mantenimiento ");
        miMantenimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMantenimientosActionPerformed(evt);
            }
        });
        jMenu23.add(miMantenimientos);

        jMenu21.add(jMenu23);

        jMenu24.setText("Documentos");

        jMenuItem36.setText("Ingresar Documentos");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenu24.add(jMenuItem36);

        jMenu21.add(jMenu24);

        jMenu25.setText("Ordenes");
        jMenu21.add(jMenu25);

        jMenu26.setText("LLantas");
        jMenu21.add(jMenu26);

        jMenu27.setText("Combustibles");
        jMenu21.add(jMenu27);

        jMenuItem37.setText("ingresar Facturas");
        jMenuItem37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem37ActionPerformed(evt);
            }
        });
        jMenu21.add(jMenuItem37);

        mnuFlota.add(jMenu21);

        jMenuBar1.add(mnuFlota);

        jMenu7.setText("Administracion");

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/lorry_go.png"))); // NOI18N
        jMenu4.setText("Transporte");

        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem12.setText("Vehiculos");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem12);

        jMenu32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/lorry_go.png"))); // NOI18N
        jMenu32.setText("Vehiculos");

        jMenuItem45.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem45.setText("Marcas Vehiculos");
        jMenuItem45.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem45ActionPerformed(evt);
            }
        });
        jMenu32.add(jMenuItem45);

        jMenuItem48.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem48.setText("Tipos Carrocerias");
        jMenuItem48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem48ActionPerformed(evt);
            }
        });
        jMenu32.add(jMenuItem48);

        jMenuItem67.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem67.setText("Tipos Combustible");
        jMenuItem67.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem67ActionPerformed(evt);
            }
        });
        jMenu32.add(jMenuItem67);

        jMenuItem62.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem62.setText("Tipos Contrato");
        jMenuItem62.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem62ActionPerformed(evt);
            }
        });
        jMenu32.add(jMenuItem62);

        jMenuItem66.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem66.setText("Tipos Servicio");
        jMenuItem66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem66ActionPerformed(evt);
            }
        });
        jMenu32.add(jMenuItem66);

        jMenuItem47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem47.setText("Tipos Vehiculos");
        jMenuItem47.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem47ActionPerformed(evt);
            }
        });
        jMenu32.add(jMenuItem47);

        jMenuItem56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem56.setText("Marcas LLantas");
        jMenuItem56.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem56ActionPerformed(evt);
            }
        });
        jMenu32.add(jMenuItem56);

        jMenuItem65.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem65.setText("Tipos Documentos");
        jMenuItem65.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem65ActionPerformed(evt);
            }
        });
        jMenu32.add(jMenuItem65);

        jMenuItem46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem46.setText("LineasVehiculos");
        jMenuItem46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem46ActionPerformed(evt);
            }
        });
        jMenu32.add(jMenuItem46);

        jMenuItem61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem61.setText("Estados Vehiculo");
        jMenuItem61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem61ActionPerformed(evt);
            }
        });
        jMenu32.add(jMenuItem61);

        jMenu4.add(jMenu32);

        jMenu7.add(jMenu4);

        mnuPersonal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/group.png"))); // NOI18N
        mnuPersonal.setText("Personal");

        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/People.png"))); // NOI18N
        jMenuItem11.setText("Personal");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        mnuPersonal.add(jMenuItem11);

        jMenuItem28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png"))); // NOI18N
        jMenuItem28.setText("Sincronizar Personal");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        mnuPersonal.add(jMenuItem28);

        jMenu8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/group_add.png"))); // NOI18N
        jMenu8.setText("Usuarios");

        mnuCrearUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/user_add.png"))); // NOI18N
        mnuCrearUsuarios.setText("Usuarios");
        mnuCrearUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCrearUsuariosActionPerformed(evt);
            }
        });
        jMenu8.add(mnuCrearUsuarios);

        mnuCambioClave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/key_go.png"))); // NOI18N
        mnuCambioClave.setText("Clave");
        mnuCambioClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCambioClaveActionPerformed(evt);
            }
        });
        jMenu8.add(mnuCambioClave);

        mnuPersonal.add(jMenu8);

        jMenuItem29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem29.setText("Reporte de personal");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        mnuPersonal.add(jMenuItem29);

        jMenu30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/group_go.png"))); // NOI18N
        jMenu30.setText("Talento Humano");

        jMenuItem43.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem43.setText("Cargos");
        jMenuItem43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem43ActionPerformed(evt);
            }
        });
        jMenu30.add(jMenuItem43);

        jMenuItem49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem49.setText("Centros de Costo");
        jMenuItem49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem49ActionPerformed(evt);
            }
        });
        jMenu30.add(jMenuItem49);

        jMenuItem50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem50.setText("Bancos");
        jMenuItem50.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem50ActionPerformed(evt);
            }
        });
        jMenu30.add(jMenuItem50);

        jMenuItem54.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem54.setText("Cuentas Bancarias");
        jMenuItem54.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem54ActionPerformed(evt);
            }
        });
        jMenu30.add(jMenuItem54);

        jMenuItem63.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem63.setText("Tipos Contrato");
        jMenuItem63.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem63ActionPerformed(evt);
            }
        });
        jMenu30.add(jMenuItem63);

        jMenuItem64.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem64.setText("Tipos Documentos");
        jMenuItem64.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem64ActionPerformed(evt);
            }
        });
        jMenu30.add(jMenuItem64);

        mnuPersonal.add(jMenu30);

        jMenu7.add(mnuPersonal);

        jMenu31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        jMenu31.setText("Distribucion");

        jMenuItem44.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem44.setText("Rutas");
        jMenuItem44.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem44ActionPerformed(evt);
            }
        });
        jMenu31.add(jMenuItem44);

        jMenuItem51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem51.setText("Canal de Venta");
        jMenuItem51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem51ActionPerformed(evt);
            }
        });
        jMenu31.add(jMenuItem51);

        jMenuItem52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem52.setText("Causales Devolucion");
        jMenuItem52.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem52ActionPerformed(evt);
            }
        });
        jMenu31.add(jMenuItem52);

        jMenuItem53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem53.setText("Clasificacion Rutas");
        jMenuItem53.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem53ActionPerformed(evt);
            }
        });
        jMenu31.add(jMenuItem53);

        jMenuItem55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem55.setText("Estados Manifiesto");
        jMenuItem55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem55ActionPerformed(evt);
            }
        });
        jMenu31.add(jMenuItem55);

        jMenu7.add(jMenu31);

        jMenuBar1.add(jMenu7);

        mnuVehiculos.setText("Logistica");
        mnuVehiculos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVehiculosActionPerformed(evt);
            }
        });

        jMenu9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        jMenu9.setText("Importar Excel");

        jMenuItem16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        jMenuItem16.setText("Importar Excel facturas");
        jMenuItem16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem16MouseClicked(evt);
            }
        });
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem16);

        mnuVehiculos.add(jMenu9);

        jMenu6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Stopwatch.png"))); // NOI18N
        jMenu6.setText("Proceso Picking");

        jMenuItem20.setText("Movilizar Facturas");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem20);

        jMenuItem21.setText("Facturas Final de Picking");
        jMenuItem21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem21ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem21);

        jMenuItem22.setText("Reporte de Pendientes");
        jMenuItem22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem22ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem22);

        mnuVehiculos.add(jMenu6);

        jMenu14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        jMenu14.setText("distribucion");

        mnuManifestarPedidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        mnuManifestarPedidos.setText("Ingreso de Facturas");
        mnuManifestarPedidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuManifestarPedidosMouseClicked(evt);
            }
        });
        mnuManifestarPedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuManifestarPedidosActionPerformed(evt);
            }
        });
        jMenu14.add(mnuManifestarPedidos);

        mnuDescargarFacturas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        mnuDescargarFacturas.setText("Descargar Facturas");
        mnuDescargarFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDescargarFacturasActionPerformed(evt);
            }
        });
        jMenu14.add(mnuDescargarFacturas);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Erase.png"))); // NOI18N
        jMenuItem4.setText("Anular Facturas");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem4);

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Good mark.png"))); // NOI18N
        jMenuItem1.setText("Conciliar rutas");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem1);

        jMenuItem24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Move.png"))); // NOI18N
        jMenuItem24.setText("Traslado de Facturas");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem24);

        mnuManifestarPedidos1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        mnuManifestarPedidos1.setText("Manifestar pedidos Poblaciones");
        mnuManifestarPedidos1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuManifestarPedidos1MouseClicked(evt);
            }
        });
        mnuManifestarPedidos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuManifestarPedidos1ActionPerformed(evt);
            }
        });
        jMenu14.add(mnuManifestarPedidos1);

        mnuVehiculos.add(jMenu14);

        mnuDescargueHielera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        mnuDescargueHielera.setText("distribucion Hielera");

        jMenuItem69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/logo_hielerablue.png"))); // NOI18N
        jMenuItem69.setText("Integrador Hielera");
        jMenuItem69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem69ActionPerformed(evt);
            }
        });
        mnuDescargueHielera.add(jMenuItem69);

        mnuManifestarPedidos8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Deliveries.png"))); // NOI18N
        mnuManifestarPedidos8.setText("Manifestar Domicilios");
        mnuManifestarPedidos8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuManifestarPedidos8MouseClicked(evt);
            }
        });
        mnuManifestarPedidos8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuManifestarPedidos8ActionPerformed(evt);
            }
        });
        mnuDescargueHielera.add(mnuManifestarPedidos8);

        mnuManifestarPedidos9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Go forward.png"))); // NOI18N
        mnuManifestarPedidos9.setText("Salidas a Ruta");
        mnuManifestarPedidos9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuManifestarPedidos9MouseClicked(evt);
            }
        });
        mnuManifestarPedidos9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuManifestarPedidos9ActionPerformed(evt);
            }
        });
        mnuDescargueHielera.add(mnuManifestarPedidos9);

        jMenuItem75.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Go back.png"))); // NOI18N
        jMenuItem75.setText("Descargue Hielera");
        jMenuItem75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem75ActionPerformed(evt);
            }
        });
        mnuDescargueHielera.add(jMenuItem75);

        jMenuItem76.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Move.png"))); // NOI18N
        jMenuItem76.setText("Traslado de Facturas");
        jMenuItem76.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem76ActionPerformed(evt);
            }
        });
        mnuDescargueHielera.add(jMenuItem76);

        jMenuItem77.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Update.png"))); // NOI18N
        jMenuItem77.setText("Modificar Manifiesto");
        jMenuItem77.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem77ActionPerformed(evt);
            }
        });
        mnuDescargueHielera.add(jMenuItem77);

        mnuVehiculos.add(mnuDescargueHielera);

        mnuHabilitarManifiesto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/flag_green.png"))); // NOI18N
        mnuHabilitarManifiesto.setText("Habilitar Manifiesto");
        mnuHabilitarManifiesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuHabilitarManifiestoActionPerformed(evt);
            }
        });
        mnuVehiculos.add(mnuHabilitarManifiesto);

        mnuHabilitarFacturas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/flag_blue.png"))); // NOI18N
        mnuHabilitarFacturas.setText("Habilitar Facturas");
        mnuHabilitarFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuHabilitarFacturasActionPerformed(evt);
            }
        });
        mnuVehiculos.add(mnuHabilitarFacturas);

        jMenu11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/lorry_go.png"))); // NOI18N
        jMenu11.setText("Transporte");
        mnuVehiculos.add(jMenu11);

        jMenuItem31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/page_add.png"))); // NOI18N
        jMenuItem31.setText("Facturar");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });
        mnuVehiculos.add(jMenuItem31);

        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/building_add.png"))); // NOI18N
        jMenuItem15.setText("Canal de Ventas");
        mnuVehiculos.add(jMenuItem15);

        jMenuBar1.add(mnuVehiculos);

        mnuClientes.setText("Clientes");

        imActualizarClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/map.png"))); // NOI18N
        imActualizarClientes.setText("actualizarClientes");
        imActualizarClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imActualizarClientesActionPerformed(evt);
            }
        });
        mnuClientes.add(imActualizarClientes);

        jMenuBar1.add(mnuClientes);

        jMenu5.setText("Consultas");

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenu2.setText("Consultar");

        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem7.setText("Facturas Por Cliente");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        jMenu5.add(jMenu2);

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/find.png"))); // NOI18N
        jMenuItem5.setText("Consultar Factura");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem5);

        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem10.setText("Consultar Manifiesto");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem10);

        mnuConsultarManifiestos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        mnuConsultarManifiestos.setText("Consultar Manifiestos");
        mnuConsultarManifiestos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConsultarManifiestosActionPerformed(evt);
            }
        });
        jMenu5.add(mnuConsultarManifiestos);

        jMenu15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        jMenu15.setText("Manifiestos");

        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem13.setText("De Distribucion");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem13);

        jMenuItem19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem19.setText("Descargados");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem19);

        jMenuItem23.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem23.setText("Sin Descargar");
        jMenuItem23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem23ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem23);

        jMenuItem79.setText("Movilizado Conductor");
        jMenuItem79.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem79ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem79);

        jMenuItem80.setText("Facturas Descargadas");
        jMenuItem80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem80ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem80);

        jMenu5.add(jMenu15);

        jMenuItem27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Search.png"))); // NOI18N
        jMenuItem27.setText("Producto");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem27);

        jMenuBar1.add(jMenu5);

        mnuReportes.setText("Reportes");

        jMenu16.setText("Flota");

        jMenuItem38.setText("Gastos");
        jMenuItem38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem38ActionPerformed(evt);
            }
        });
        jMenu16.add(jMenuItem38);

        jMenuItem39.setText("Documentos Flota");
        jMenuItem39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem39ActionPerformed(evt);
            }
        });
        jMenu16.add(jMenuItem39);

        jMenuItem26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem26.setText("Lista Vehiculos");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu16.add(jMenuItem26);

        mnuReportes.add(jMenu16);

        jMenu28.setText("Distribucion");

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenu1.setText("Reporte Rechazos");

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem2.setText("Rechazos Totales");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem3.setText("RechazosParciales");
        jMenu1.add(jMenuItem3);

        jMenu28.add(jMenu1);

        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem8.setText("Reporte por periodo");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem8);

        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem9.setText("Facturas Pendientes");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem9);

        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem14.setText("Reporte Manifiestos En Distribucion");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem14);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenu3.setText("Reportes Facturacion");

        mnu_rptFacturacionPorCanal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mnu_rptFacturacionPorCanal.setText("Reporte Canales");
        mnu_rptFacturacionPorCanal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_rptFacturacionPorCanalActionPerformed(evt);
            }
        });
        jMenu3.add(mnu_rptFacturacionPorCanal);

        mnu_rptFacturacionTAT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mnu_rptFacturacionTAT.setText("Reporte TAT");
        mnu_rptFacturacionTAT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_rptFacturacionTATActionPerformed(evt);
            }
        });
        jMenu3.add(mnu_rptFacturacionTAT);

        jMenu28.add(jMenu3);

        jMenuItem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem18.setText("Manifiestos sin Descargar");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem18);

        jMenuItem17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem17.setText("Desctos & Recogidas");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem17);

        jMenuItem25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem25.setText("De Distribucion");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem25);

        jMenuItem30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Accounting.png"))); // NOI18N
        jMenuItem30.setText("Descuentos Autorizados");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem30);

        jMenuItem81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem81.setText("Mov X Conductor por periodo");
        jMenuItem81.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem81ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem81);

        jMenuItem40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem40.setText("Reporte de Porteria");
        jMenuItem40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem40ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem40);

        jMenuItem103.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem103.setText("Salida de Producto");
        jMenuItem103.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem103ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem103);

        mnuReportes.add(jMenu28);

        jMenuBar1.add(mnuReportes);

        jMenu12.setText("Informes");
        jMenuBar1.add(jMenu12);

        jMenu13.setText("Acerca de");

        jMenuItem32.setText("Server Local");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem32);

        jMenuItem33.setText("Servidor Remoto");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem33);

        jMenuItem34.setText("Cliente");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem34);

        jMenuBar1.add(jMenu13);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorio)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorio)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuArchivoActionPerformed
    }//GEN-LAST:event_mnuArchivoActionPerformed

    private void menuCambiarClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCambiarClaveActionPerformed
        try {
            // this.escritorio.setSize(600, 400);
            fCambiarClave = new CambiarClave(ini);
            if (fCambiarClave != null) {
                this.escritorio.add(fCambiarClave);
                fCambiarClave.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
                fCambiarClave.setLocation((ini.getDimension().width - fCambiarClave.getSize().width) / 2, (ini.getDimension().height - fCambiarClave.getSize().height) / 2);
                fCambiarClave.show();
                fCambiarClave.setVisible(true);
                fCambiarClave.txtClaveAnterior.requestFocus();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario CambiarClave");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuCambiarClaveActionPerformed

    private void mnuVehiculosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVehiculosActionPerformed

    }//GEN-LAST:event_mnuVehiculosActionPerformed

    private void jMenuItem16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem16MouseClicked

    }//GEN-LAST:event_jMenuItem16MouseClicked

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        FImportarArchivoExcel fImportarExcel = new FImportarArchivoExcel(ini);

        if (fImportarExcel != null) {
            try {
                this.escritorio.add(fImportarExcel);
                fImportarExcel.setLocation(((ini.getDimension().width - fImportarExcel.getSize().width) / 2), ((ini.getDimension().height - fImportarExcel.getSize().height) / 2));
                fImportarExcel.setVisible(true);
                fImportarExcel.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FImportarArchivoExcel");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            } catch (Exception ex) {
                Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void mnuManifestarPedidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuManifestarPedidosMouseClicked

    }//GEN-LAST:event_mnuManifestarPedidosMouseClicked

    private void mnuManifestarPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuManifestarPedidosActionPerformed
        try {
            fManifestarPedidosEnRuta = new FManifestarPedidosEnRuta(ini);

            if (fManifestarPedidosEnRuta != null) {
                this.escritorio.add(fManifestarPedidosEnRuta);
                fManifestarPedidosEnRuta.setSize(ini.getDimension());
                fManifestarPedidosEnRuta.setLocation(((ini.getDimension().width - fManifestarPedidosEnRuta.getSize().width) / 2), ((ini.getDimension().height - fManifestarPedidosEnRuta.getSize().height) / 2));
                fManifestarPedidosEnRuta.setVisible(true);
                fManifestarPedidosEnRuta.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FManifestarPedidosEnRuta");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_mnuManifestarPedidosActionPerformed

    private void mnuDescargarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDescargarFacturasActionPerformed
        try {
            fDescargarFActuras = new DescargarFacturas(this.ini);

            if (fDescargarFActuras != null) {
                this.escritorio.add(fDescargarFActuras);
                fDescargarFActuras.CargarVista();
                fDescargarFActuras.setSize(ini.getDimension());
                // fDescargarFActuras.setLocation(((ini.getDimension().width - fDescargarFActuras.getSize().width) / 2), ((ini.getDimension().height - fDescargarFActuras.getSize().height) / 2));
                fDescargarFActuras.setLocation(0, 0);
                fDescargarFActuras.setVisible(true);
                fDescargarFActuras.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario DescargarFacturas");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_mnuDescargarFacturasActionPerformed

    private void mnuConsultarManifiestosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConsultarManifiestosActionPerformed
        try {
            fConsultarManifiestos = new FConsultarManifiestos(ini);
            if (fConsultarManifiestos != null) {
                this.escritorio.add(fConsultarManifiestos);
                fConsultarManifiestos.setLocation(((ini.getDimension().width - fConsultarManifiestos.getSize().width) / 2), ((ini.getDimension().height - fConsultarManifiestos.getSize().height) / 2));
                fConsultarManifiestos.setVisible(true);
                fConsultarManifiestos.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FConsultarManifiestos");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuConsultarManifiestosActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            fReporteRechazosTotales = new FReporteDeRechazosTotales(ini);
            if (fReporteRechazosTotales != null) {
                this.escritorio.add(fReporteRechazosTotales);
                fReporteRechazosTotales.setLocation(((ini.getDimension().width - fReporteRechazosTotales.getSize().width) / 2), ((ini.getDimension().height - fReporteRechazosTotales.getSize().height) / 2));
                fReporteRechazosTotales.setVisible(true);
                fReporteRechazosTotales.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteDeRechazosTotales");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        try {

            fAnularFacturaSinMovimiento = new FAnularFacturaSinMovimiento(ini);
            this.escritorio.add(fAnularFacturaSinMovimiento);
            // fDescargarFActuras.setSize(escritorio.getSize());
            fAnularFacturaSinMovimiento.setLocation(((ini.getDimension().width - fAnularFacturaSinMovimiento.getSize().width) / 2), ((ini.getDimension().height - fAnularFacturaSinMovimiento.getSize().height) / 2));
            fAnularFacturaSinMovimiento.setVisible(true);
            fAnularFacturaSinMovimiento.show();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FAnularFacturaSinMovimiento");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed

        try {
            FConsultarFacturasRemoto form1 = new FConsultarFacturasRemoto(this.ini);

            this.escritorio.add(form1);
//             form1.setSize(screenSize.width,screenSize.height-43);
//            form1.setMaximumSize(screenSize);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2));
            form1.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FConsultarFacturasRemoto");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed

        try {
            fConsultarFacturasPorCliente = new FConsultarFacturasPorCliente(ini);

            if (fConsultarFacturasPorCliente != null) {
                this.escritorio.add(fConsultarFacturasPorCliente);
                fConsultarFacturasPorCliente.setLocation(((ini.getDimension().width - fConsultarFacturasPorCliente.getSize().width) / 2), ((ini.getDimension().height - fConsultarFacturasPorCliente.getSize().height) / 2) - 30);
                fConsultarFacturasPorCliente.setVisible(true);
                fConsultarFacturasPorCliente.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FConsultarFacturasPorCliente");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed

        try {
            fReporteMovilizado = new FReportePedidosMovilizadosPorPeriodo(ini);
            if (fReporteMovilizado != null) {
                this.escritorio.add(fReporteMovilizado);
                fReporteMovilizado.setLocation(((ini.getDimension().width - fReporteMovilizado.getSize().width) / 2), ((ini.getDimension().height - fReporteMovilizado.getSize().height) / 2) - 30);
                fReporteMovilizado.setVisible(true);
                fReporteMovilizado.show();

                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReportePedidosMovilizadosPorPeriodo");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed

        try {
            fReporteFacturasPendientes = new FReporteFacturasPendientes(ini);
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fReporteFacturasPendientes != null) {
                this.escritorio.add(fReporteFacturasPendientes);
                fReporteFacturasPendientes.setLocation(((ini.getDimension().width - fReporteFacturasPendientes.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturasPendientes.getSize().height) / 2) - 30);
                fReporteFacturasPendientes.setVisible(true);
                fReporteFacturasPendientes.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteFacturasPendientes");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        try {
            fConsultarManifiestos = new FConsultarManifiestos(ini);

            if (fConsultarManifiestos != null) {
                this.escritorio.add(fConsultarManifiestos);
                fConsultarManifiestos.setLocation(((ini.getDimension().width - fConsultarManifiestos.getSize().width) / 2), ((ini.getDimension().height - fConsultarManifiestos.getSize().height) / 2) - 30);
                fConsultarManifiestos.setVisible(true);
                fConsultarManifiestos.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FConsultarManifiestos");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void mnuHabilitarManifiestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuHabilitarManifiestoActionPerformed
        try {
            fHabilitarManifiesto = new FHabilitarManifiesto(ini);
            if (fHabilitarManifiesto != null) {
                this.escritorio.add(fHabilitarManifiesto);
                fHabilitarManifiesto.setLocation(((ini.getDimension().width - fHabilitarManifiesto.getSize().width) / 2), ((ini.getDimension().height - fHabilitarManifiesto.getSize().height) / 2) - 30);
                fHabilitarManifiesto.setVisible(true);
                fHabilitarManifiesto.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FHabilitarManifiesto");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuHabilitarManifiestoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            fReporteManifiestosConcilidos = new FReporteManifiestosConcilidos(ini);
            if (fReporteManifiestosConcilidos != null) {
                this.escritorio.add(fReporteManifiestosConcilidos);
                fReporteManifiestosConcilidos.setLocation(((ini.getDimension().width - fReporteManifiestosConcilidos.getSize().width) / 2), ((ini.getDimension().height - fReporteManifiestosConcilidos.getSize().height) / 2) - 30);
                //fManifiestosConciliados.setVisible(true);
                fReporteManifiestosConcilidos.refrescarTblManifiestosSinConciliar();
                //fManifiestosConciliados.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteManifiestosConcilidos");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }     // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void mnuHabilitarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuHabilitarFacturasActionPerformed
        try {
            fHabilitarFacturas = new FHabilitarFacturas(ini);
            if (fHabilitarFacturas != null) {
                this.escritorio.add(fHabilitarFacturas);
                fHabilitarFacturas.setLocation(((ini.getDimension().width - fHabilitarFacturas.getSize().width) / 2), ((ini.getDimension().height - fHabilitarFacturas.getSize().height) / 2) - 30);
                //fHabilitarFacturas.setVisible(true);
                //fHabilitarFacturas.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FHabilitarFacturas");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuHabilitarFacturasActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        try {
            fReporteManifiestosEnDistribucion = new FReporteManifiestosEnDistribucion(ini);
            if (fReporteManifiestosEnDistribucion != null) {
                this.escritorio.add(fReporteManifiestosEnDistribucion);
                fReporteManifiestosEnDistribucion.setLocation(((ini.getDimension().width - fReporteManifiestosEnDistribucion.getSize().width) / 2), ((ini.getDimension().height - fReporteManifiestosEnDistribucion.getSize().height) / 2) - 30);
                fReporteManifiestosEnDistribucion.setVisible(true);
                fReporteManifiestosEnDistribucion.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteManifiestosEnDistribucion");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void mnu_rptFacturacionPorCanalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionPorCanalActionPerformed
        try {
            fReporteFacturacionPorCanal = new FReporteFacturacionPorCanal(ini);
            if (fReporteFacturacionPorCanal != null) {
                this.escritorio.add(fReporteFacturacionPorCanal);
                fReporteFacturacionPorCanal.setLocation(((ini.getDimension().width - fReporteFacturacionPorCanal.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturacionPorCanal.getSize().height) / 2) - 30);
                fReporteFacturacionPorCanal.setVisible(true);
                fReporteFacturacionPorCanal.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteFacturacionPorCanal");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnu_rptFacturacionPorCanalActionPerformed

    private void mnu_rptFacturacionTATActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionTATActionPerformed
        try {
            fReporteFacturacionTAT = new FReporteFacturacionTAT(ini);

            if (fReporteFacturacionTAT != null) {
                this.escritorio.add(fReporteFacturacionTAT);
                fReporteFacturacionTAT.setLocation(((ini.getDimension().width - fReporteFacturacionTAT.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturacionTAT.getSize().height) / 2) - 30);
                fReporteFacturacionTAT.setVisible(true);
                fReporteFacturacionTAT.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteFacturacionTAT");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnu_rptFacturacionTATActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed

        try {
            freporteManifiestosSinDescargar = new FReporteManifiestosSinDescargar(ini);
            if (freporteManifiestosSinDescargar != null) {
                this.escritorio.add(freporteManifiestosSinDescargar);
                freporteManifiestosSinDescargar.setLocation(((ini.getDimension().width - freporteManifiestosSinDescargar.getSize().width) / 2), ((ini.getDimension().height - freporteManifiestosSinDescargar.getSize().height) / 2) - 30);
                freporteManifiestosSinDescargar.setVisible(true);
                freporteManifiestosSinDescargar.cargarInformacion();
                freporteManifiestosSinDescargar.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteManifiestosSinDescargar");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        if (fFacturasEnPicking != null) {
            try {
                this.escritorio.add(fFacturasEnPicking);
                fFacturasEnPicking.setLocation(((ini.getDimension().width - fFacturasEnPicking.getSize().width) / 2), ((ini.getDimension().height - fFacturasEnPicking.getSize().height) / 2) - 30);
                fFacturasEnPicking.setVisible(true);
                //fDesvincularFactura.cargarInformacion();
                fFacturasEnPicking.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario fFacturasEnPicking");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            } catch (Exception ex) {
                Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        // TODO add your handling code here: fRegistroDePedidosFinLineaDePicking

        if (fRegistroDePedidosFinLineaDePicking != null) {
            try {
                this.escritorio.add(fRegistroDePedidosFinLineaDePicking);
                fRegistroDePedidosFinLineaDePicking.setLocation(((ini.getDimension().width - fRegistroDePedidosFinLineaDePicking.getSize().width) / 2), ((ini.getDimension().height - fRegistroDePedidosFinLineaDePicking.getSize().height) / 2) - 30);
                fRegistroDePedidosFinLineaDePicking.setVisible(true);
                //fDesvincularFactura.cargarInformacion();
                fRegistroDePedidosFinLineaDePicking.show();

                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario fRegistroDePedidosFinLineaDePicking");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            } catch (Exception ex) {
                Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed

        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fReporteFacturasPendientesEnPicking != null) {
                this.escritorio.add(fReporteFacturasPendientesEnPicking);
                fReporteFacturasPendientesEnPicking.setLocation(((ini.getDimension().width - fReporteFacturasPendientesEnPicking.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturasPendientesEnPicking.getSize().height) / 2) - 30);
                fReporteFacturasPendientesEnPicking.setVisible(true);
                fReporteFacturasPendientesEnPicking.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario fReporteFacturasPendientesEnPicking");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void imActualizarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imActualizarClientesActionPerformed
        fGeoreferenciarClientes = new FGeoreferenciarClientes(ini);

        if (fGeoreferenciarClientes != null) {
            try {
                this.escritorio.add(fGeoreferenciarClientes);
                fGeoreferenciarClientes.setLocation(((ini.getDimension().width - fGeoreferenciarClientes.getSize().width) / 2), ((ini.getDimension().height - fGeoreferenciarClientes.getSize().height) / 2) - 35);
                //form1.setMaximizable(false);
                fGeoreferenciarClientes.setVisible(true);
                fGeoreferenciarClientes.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario fGeoreferenciarClientes");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            } catch (Exception ex) {
                Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_imActualizarClientesActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed

        try {
            fReporteDescuentos_Recogidas = new FReporteDescuentos_Recogidas(ini);
            if (fReporteDescuentos_Recogidas != null) {
                this.escritorio.add(fReporteDescuentos_Recogidas);
                fReporteDescuentos_Recogidas.setLocation(((ini.getDimension().width - fReporteDescuentos_Recogidas.getSize().width) / 2), ((ini.getDimension().height - fReporteDescuentos_Recogidas.getSize().height) / 2) - 30);
                fReporteDescuentos_Recogidas.setVisible(true);
                //fReporteDescuentos_Recogidas.cargarInformacion();
                fReporteDescuentos_Recogidas.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario fReporteDescuentos_Recogidas");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void mnuCambioClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCambioClaveActionPerformed
        try {
            FModificarClaveOlvidada modificarClave = new FModificarClaveOlvidada(ini);
            this.escritorio.add(modificarClave);
            modificarClave.setLocation(((ini.getDimension().width - modificarClave.getSize().width) / 2), ((ini.getDimension().height - modificarClave.getSize().height) / 2) - 30);
            modificarClave.setVisible(true);
            //fReporteDescuentos_Recogidas.cargarInformacion();
            modificarClave.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FModificarClaveOlvidada");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuCambioClaveActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed

        try {
            fIngresarEmpleados = new IngresarEmpleados(ini);
            this.escritorio.add(fIngresarEmpleados);
            fIngresarEmpleados.setLocation(((ini.getDimension().width - fIngresarEmpleados.getSize().width) / 2), ((ini.getDimension().height - fIngresarEmpleados.getSize().height) / 2) - 30);
            fIngresarEmpleados.setVisible(true);
            //fReporteDescuentos_Recogidas.cargarInformacion();
            fIngresarEmpleados.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario fIngresarEmpleados");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        try {
            FConsultarManifiestos form1 = new FConsultarManifiestos(ini);

            this.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2));
            form1.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FConsultarManifiestos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioConsultas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed

        try {
            FConsultaManifiestosDescargados fConsultaManifiestosDescargados = new FConsultaManifiestosDescargados(this.ini);

            this.escritorio.add(fConsultaManifiestosDescargados);
            fConsultaManifiestosDescargados.setLocation(((ini.getDimension().width - fConsultaManifiestosDescargados.getSize().width) / 2), ((ini.getDimension().height - fConsultaManifiestosDescargados.getSize().height) / 2) - 30);
            fConsultaManifiestosDescargados.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FConsultaManifiestosDescargados");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioConsultas.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem23ActionPerformed

        try {
            FConsultaManifiestosSinDescargar fConsultaManifiestosSinDescargar = new FConsultaManifiestosSinDescargar(this.ini);

            this.escritorio.add(fConsultaManifiestosSinDescargar);
            fConsultaManifiestosSinDescargar.setLocation(((ini.getDimension().width - fConsultaManifiestosSinDescargar.getSize().width) / 2), ((ini.getDimension().height - fConsultaManifiestosSinDescargar.getSize().height) / 2) - 30);
            fConsultaManifiestosSinDescargar.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FConsultaManifiestosDescargados");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioConsultas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem23ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        fReportePedidosEnDistribucion = new FReportePedidosEnDistribucion(ini);
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fReportePedidosEnDistribucion != null) {
                this.escritorio.add(fReportePedidosEnDistribucion);
                fReportePedidosEnDistribucion.setLocation(((ini.getDimension().width - fReportePedidosEnDistribucion.getSize().width) / 2), ((ini.getDimension().height - fReportePedidosEnDistribucion.getSize().height) / 2) - 30);
                fReportePedidosEnDistribucion.setVisible(true);
                fReportePedidosEnDistribucion.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReportePedidosEnDistribucion");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void mnuCrearUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCrearUsuariosActionPerformed

        try {
            IngresarUsuarios ingresarUsuarios = new IngresarUsuarios(ini);
            if (ingresarUsuarios != null) {
                this.escritorio.add(ingresarUsuarios);
                ingresarUsuarios.setLocation(((ini.getDimension().width - ingresarUsuarios.getSize().width) / 2), ((ini.getDimension().height - ingresarUsuarios.getSize().height) / 2) - 30);
                ingresarUsuarios.setVisible(true);
                ingresarUsuarios.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario IngresarUsuarios");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuCrearUsuariosActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
        try {
            FTrasladoDeFacturas fTrasladoDeFacturas = new FTrasladoDeFacturas(ini);
            if (fTrasladoDeFacturas != null) {
                this.escritorio.add(fTrasladoDeFacturas);
                fTrasladoDeFacturas.setLocation(((ini.getDimension().width - fTrasladoDeFacturas.getSize().width) / 2), ((ini.getDimension().height - fTrasladoDeFacturas.getSize().height) / 2) - 30);
                fTrasladoDeFacturas.setVisible(true);
                fTrasladoDeFacturas.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FTrasladoDeFacturas");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed

        try {
            FReporteDelPersonal fReporteDelPersonal = new FReporteDelPersonal(ini);
            if (fReporteDelPersonal != null) {
                this.escritorio.add(fReporteDelPersonal);
                fReporteDelPersonal.setLocation(((ini.getDimension().width - fReporteDelPersonal.getSize().width) / 2), ((ini.getDimension().height - fReporteDelPersonal.getSize().height) / 2) - 30);
                fReporteDelPersonal.setVisible(true);
                fReporteDelPersonal.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteDelPersonal");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed

        try {
            FReporteClientesConDescuento fReporteClientesConDescuento = new FReporteClientesConDescuento(ini);
            if (fReporteClientesConDescuento != null) {
                this.escritorio.add(fReporteClientesConDescuento);
                fReporteClientesConDescuento.setLocation(((ini.getDimension().width - fReporteClientesConDescuento.getSize().width) / 2), ((ini.getDimension().height - fReporteClientesConDescuento.getSize().height) / 2) - 30);
                fReporteClientesConDescuento.setVisible(true);
                fReporteClientesConDescuento.cargarInformacion();
                fReporteClientesConDescuento.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteClientesConDescuento");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void mnuManifestarPedidos1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuManifestarPedidos1MouseClicked

    private void mnuManifestarPedidos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos1ActionPerformed

        try {
            FManifestarPedidosPoblaciones fManifestarPedidosPoblaciones = new FManifestarPedidosPoblaciones(ini);
            if (fManifestarPedidosPoblaciones != null) {
                this.escritorio.add(fManifestarPedidosPoblaciones);
                fManifestarPedidosPoblaciones.setLocation(((ini.getDimension().width - fManifestarPedidosPoblaciones.getSize().width) / 2), ((ini.getDimension().height - fManifestarPedidosPoblaciones.getSize().height) / 2) - 30);
                fManifestarPedidosPoblaciones.setSize(ini.getDimension());
                fManifestarPedidosPoblaciones.setVisible(true);
                fManifestarPedidosPoblaciones.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FManifestarPedidosPoblaciones");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:
    }//GEN-LAST:event_mnuManifestarPedidos1ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
        try {
            FCrearFacturasDeVenta fCrearFacturasDeVenta = new FCrearFacturasDeVenta(ini);
            if (fCrearFacturasDeVenta != null) {
                this.escritorio.add(fCrearFacturasDeVenta);
                //fCrearFacturasDeVenta.setLocation(((ini.getDimension().width - fCrearFacturasDeVenta.getSize().width) / 2), ((ini.getDimension().height - fCrearFacturasDeVenta.getSize().height) / 2) - 30);
                fCrearFacturasDeVenta.setSize(ini.getDimension());
                fCrearFacturasDeVenta.setVisible(true);
                fCrearFacturasDeVenta.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FCrearFacturasDeVenta");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        JOptionPane.showMessageDialog(this, "Servidor Local " + ini.getServerLocal() + "\n BBDD " + ini.getBdLocal(), ini.getServerLocal(), JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        JOptionPane.showMessageDialog(this, "Servidor Remoto " + ini.getServerRemota() + "\n BBDD " + ini.getBdRemota(), ini.getServerRemota(), JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        JOptionPane.showMessageDialog(this, "Nombre de Cliente " + ini.getPropiedades().getProperty("nombreCliente"), "Nombre de Cliente ", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenu22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu22ActionPerformed

    }//GEN-LAST:event_jMenu22ActionPerformed

    private void miMantenimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMantenimientosActionPerformed
        try {
            FAgregarMantenimientoVehiculo fAgregarMantenimientoVehiculo = new FAgregarMantenimientoVehiculo(ini);
            this.escritorio.add(fAgregarMantenimientoVehiculo);
            fAgregarMantenimientoVehiculo.setLocation(((ini.getDimension().width - fAgregarMantenimientoVehiculo.getSize().width) / 2), ((ini.getDimension().height - fAgregarMantenimientoVehiculo.getSize().height) / 2) - 35);
            fAgregarMantenimientoVehiculo.setVisible(true);
            fAgregarMantenimientoVehiculo.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FAgregarMantenimientoVehiculo");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_miMantenimientosActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        try {
            fIngresarDocumentosVehiculos = new FIngresarDocumentosVehiculos(ini);
            this.escritorio.add(fIngresarDocumentosVehiculos);
            fIngresarDocumentosVehiculos.setLocation(((ini.getDimension().width - fIngresarDocumentosVehiculos.getSize().width) / 2), ((ini.getDimension().height - fIngresarDocumentosVehiculos.getSize().height) / 2) - 35);
            fIngresarDocumentosVehiculos.setVisible(true);
            fIngresarDocumentosVehiculos.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FIngresarDocumentosVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItem37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem37ActionPerformed

        if (ingresarFacturasDeGastos != null) {
            try {
                this.escritorio.add(ingresarFacturasDeGastos);
                ingresarFacturasDeGastos.setLocation(((ini.getDimension().width - ingresarFacturasDeGastos.getSize().width) / 2), ((ini.getDimension().height - ingresarFacturasDeGastos.getSize().height) / 2) - 35);
                //form1.setMaximizable(false);
                ingresarFacturasDeGastos.setVisible(true);
                ingresarFacturasDeGastos.show();
                datos = new ArrayList<>();

                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FingresarFacturasDeGastos");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            } catch (Exception ex) {
                Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jMenuItem37ActionPerformed

    private void jMenuItem38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem38ActionPerformed

        if (fReporteGastosFlota != null) {
            try {
                this.escritorio.add(fReporteGastosFlota);
                fReporteGastosFlota.setLocation(((ini.getDimension().width - fReporteGastosFlota.getSize().width) / 2), ((ini.getDimension().height - fReporteGastosFlota.getSize().height) / 2) - 35);
                //form1.setMaximizable(false);
                fReporteGastosFlota.setVisible(true);
                fReporteGastosFlota.show();
                datos = new ArrayList<>();

                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteGastosFlota");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            } catch (Exception ex) {
                Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }//GEN-LAST:event_jMenuItem38ActionPerformed

    private void jMenuItem39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem39ActionPerformed

        try {
            FReporteDocumentosVehiculos fReporteDocumentosVehiculos = new FReporteDocumentosVehiculos(ini);
            this.escritorio.add(fReporteDocumentosVehiculos);
            fReporteDocumentosVehiculos.setLocation(((ini.getDimension().width - fReporteDocumentosVehiculos.getSize().width) / 2), ((ini.getDimension().height - fReporteDocumentosVehiculos.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            fReporteDocumentosVehiculos.setVisible(true);
            fReporteDocumentosVehiculos.show();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FReporteDocumentosVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem39ActionPerformed

    private void jMenuItem43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem43ActionPerformed

        try {
            FCrearCargos fCrearCargos = new FCrearCargos(ini);

            this.escritorio.add(fCrearCargos);
            fCrearCargos.setLocation(((ini.getDimension().width - fCrearCargos.getSize().width) / 2), ((ini.getDimension().height - fCrearCargos.getSize().height) / 2) - 30);
            fCrearCargos.setResizable(false);
            fCrearCargos.setMaximizable(false);
            fCrearCargos.setVisible(true);
            fCrearCargos.show();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FCrearCargos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:       // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem43ActionPerformed

    private void jMenuItem49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem49ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem49ActionPerformed

    private void jMenuItem50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem50ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem50ActionPerformed

    private void jMenuItem54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem54ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem54ActionPerformed

    private void jMenuItem63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem63ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem63ActionPerformed

    private void jMenuItem64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem64ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem64ActionPerformed

    private void jMenuItem44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem44ActionPerformed
        try {
            FCrearRutasDeDistribucion fCrearRutas = new FCrearRutasDeDistribucion(ini);

            this.escritorio.add(fCrearRutas);
            fCrearRutas.setLocation(((ini.getDimension().width - fCrearRutas.getSize().width) / 2), ((ini.getDimension().height - fCrearRutas.getSize().height) / 2) - 30);
            fCrearRutas.setVisible(true);
            fCrearRutas.show();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FCrearRutas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }       // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem44ActionPerformed

    private void jMenuItem51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem51ActionPerformed
        try {
            FCrearRutasDeDistribucion fCrearRutas = new FCrearRutasDeDistribucion(ini);

            this.escritorio.add(fCrearRutas);
            fCrearRutas.setLocation(((ini.getDimension().width - fCrearRutas.getSize().width) / 2), ((ini.getDimension().height - fCrearRutas.getSize().height) / 2) - 30);
            fCrearRutas.setVisible(true);
            fCrearRutas.show();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FCrearRutas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }          // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem51ActionPerformed

    private void jMenuItem52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem52ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem52ActionPerformed

    private void jMenuItem53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem53ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem53ActionPerformed

    private void jMenuItem55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem55ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem55ActionPerformed

    private void jMenuItem45ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem45ActionPerformed

        try {
            FCrearMarcasVehiculos fCrearMarcasVehiculos = new FCrearMarcasVehiculos(ini);

            this.escritorio.add(fCrearMarcasVehiculos);
            fCrearMarcasVehiculos.setLocation(((ini.getDimension().width - fCrearMarcasVehiculos.getSize().width) / 2), ((ini.getDimension().height - fCrearMarcasVehiculos.getSize().height) / 2) - 30);
            fCrearMarcasVehiculos.setResizable(false);
            fCrearMarcasVehiculos.setMaximizable(false);
            fCrearMarcasVehiculos.setVisible(true);
            fCrearMarcasVehiculos.show();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FCrearMarcasVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem45ActionPerformed

    private void jMenuItem48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem48ActionPerformed
        try {
            FCrearTiposCarrocerias fCrearTiposCarrocerias = new FCrearTiposCarrocerias(ini);

            this.escritorio.add(fCrearTiposCarrocerias);
            fCrearTiposCarrocerias.setLocation(((ini.getDimension().width - fCrearTiposCarrocerias.getSize().width) / 2), ((ini.getDimension().height - fCrearTiposCarrocerias.getSize().height) / 2) - 30);
            fCrearTiposCarrocerias.setResizable(false);
            fCrearTiposCarrocerias.setMaximizable(false);
            fCrearTiposCarrocerias.setVisible(true);
            fCrearTiposCarrocerias.show();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FCrearTiposCarrocerias");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem48ActionPerformed

    private void jMenuItem67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem67ActionPerformed

        try {
            FCrearTiposdeCombustible fCrearTiposdeCombustible = new FCrearTiposdeCombustible(ini);

            this.escritorio.add(fCrearTiposdeCombustible);
            fCrearTiposdeCombustible.setLocation(((ini.getDimension().width - fCrearTiposdeCombustible.getSize().width) / 2), ((ini.getDimension().height - fCrearTiposdeCombustible.getSize().height) / 2) - 30);
            fCrearTiposdeCombustible.setResizable(false);
            fCrearTiposdeCombustible.setMaximizable(false);
            fCrearTiposdeCombustible.setVisible(true);
            fCrearTiposdeCombustible.show();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FCrearTiposdeCombustible");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem67ActionPerformed

    private void jMenuItem62ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem62ActionPerformed
        try {
            FCrearTiposdeContratosVehiculos fCrearTiposdeContratosVehiculos = new FCrearTiposdeContratosVehiculos(ini);

            this.escritorio.add(fCrearTiposdeContratosVehiculos);
            fCrearTiposdeContratosVehiculos.setLocation(((ini.getDimension().width - fCrearTiposdeContratosVehiculos.getSize().width) / 2), ((ini.getDimension().height - fCrearTiposdeContratosVehiculos.getSize().height) / 2) - 30);
            fCrearTiposdeContratosVehiculos.setResizable(false);
            fCrearTiposdeContratosVehiculos.setMaximizable(false);
            fCrearTiposdeContratosVehiculos.setVisible(true);
            fCrearTiposdeContratosVehiculos.show();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FCrearTiposdeContratosVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem62ActionPerformed

    private void jMenuItem66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem66ActionPerformed
        try {
            FCrearTiposdeServicios fCrearTiposdeServicios = new FCrearTiposdeServicios(ini);

            this.escritorio.add(fCrearTiposdeServicios);
            fCrearTiposdeServicios.setLocation(((ini.getDimension().width - fCrearTiposdeServicios.getSize().width) / 2), ((ini.getDimension().height - fCrearTiposdeServicios.getSize().height) / 2) - 30);
            fCrearTiposdeServicios.setResizable(false);
            fCrearTiposdeServicios.setMaximizable(false);
            fCrearTiposdeServicios.setVisible(true);
            fCrearTiposdeServicios.show();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FCrearTiposdeServicios");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem66ActionPerformed

    private void jMenuItem47ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem47ActionPerformed

        try {
            FCrearTiposdeVehiculos fCrearTiposdeVehiculos = new FCrearTiposdeVehiculos(ini);

            this.escritorio.add(fCrearTiposdeVehiculos);
            fCrearTiposdeVehiculos.setLocation(((ini.getDimension().width - fCrearTiposdeVehiculos.getSize().width) / 2), ((ini.getDimension().height - fCrearTiposdeVehiculos.getSize().height) / 2) - 30);
            fCrearTiposdeVehiculos.setResizable(false);
            fCrearTiposdeVehiculos.setMaximizable(false);
            fCrearTiposdeVehiculos.setVisible(true);
            fCrearTiposdeVehiculos.show();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FCrearTiposdeVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem47ActionPerformed

    private void jMenuItem56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem56ActionPerformed
        try {
            FCrearMarcasDeLLantas fCrearMarcasLLantas = new FCrearMarcasDeLLantas(ini);

            this.escritorio.add(fCrearMarcasLLantas);
            fCrearMarcasLLantas.setLocation(((ini.getDimension().width - fCrearMarcasLLantas.getSize().width) / 2), ((ini.getDimension().height - fCrearMarcasLLantas.getSize().height) / 2) - 30);
            fCrearMarcasLLantas.setResizable(false);
            fCrearMarcasLLantas.setMaximizable(false);
            fCrearMarcasLLantas.setVisible(true);

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FCrearMarcasLLantas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem56ActionPerformed

    private void jMenuItem65ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem65ActionPerformed
        try {
            FCrearTiposdeDocumentos fCrearTiposdeDocumentos = new FCrearTiposdeDocumentos(ini);

            this.escritorio.add(fCrearTiposdeDocumentos);
            fCrearTiposdeDocumentos.setLocation(((ini.getDimension().width - fCrearTiposdeDocumentos.getSize().width) / 2), ((ini.getDimension().height - fCrearTiposdeDocumentos.getSize().height) / 2) - 30);
            fCrearTiposdeDocumentos.setResizable(false);
            fCrearTiposdeDocumentos.setMaximizable(false);
            fCrearTiposdeDocumentos.setVisible(true);
            fCrearTiposdeDocumentos.show();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FCrearTiposdeDocumentos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem65ActionPerformed

    private void jMenuItem46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem46ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem46ActionPerformed

    private void jMenuItem61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem61ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem61ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed

        try {
            fIngresaarCArros = new IngresarCarros(ini);
            this.escritorio.add(fIngresaarCArros);
            fIngresaarCArros.setLocation(((ini.getDimension().width - fIngresaarCArros.getSize().width) / 2), ((ini.getDimension().height - fIngresaarCArros.getSize().height) / 2) - 30);
            fIngresaarCArros.setVisible(true);
            //fReporteDescuentos_Recogidas.cargarInformacion();
            fIngresaarCArros.show();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario IngresarCarros");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        try {
            FReporteDeVehiculos fReporteDeVehiculos = new FReporteDeVehiculos(ini);
            if (fReporteDeVehiculos != null) {
                this.escritorio.add(fReporteDeVehiculos);
                fReporteDeVehiculos.setLocation(((ini.getDimension().width - fReporteDeVehiculos.getSize().width) / 2), ((ini.getDimension().height - fReporteDeVehiculos.getSize().height) / 2) - 30);
                fReporteDeVehiculos.setVisible(true);
                fReporteDeVehiculos.show();
                datos = new ArrayList<>();

                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteDeVehiculos");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void jMenuItem79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem79ActionPerformed
        try {
            FConsultarPedidosConductorHielera fConsultarPedidosConductorHielera = new FConsultarPedidosConductorHielera(ini);
            this.escritorio.add(fConsultarPedidosConductorHielera);
            fConsultarPedidosConductorHielera.setLocation(((ini.getDimension().width - fConsultarPedidosConductorHielera.getSize().width) / 2), ((ini.getDimension().height - fConsultarPedidosConductorHielera.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            fConsultarPedidosConductorHielera.setVisible(true);
            fConsultarPedidosConductorHielera.show();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FConsultarPedidosConductorHielera");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem79ActionPerformed

    private void jMenuItem80ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem80ActionPerformed

        try {
            FConsultaPedidosDescargadosPorConductorHielera fConsultaPedidosDescargadosPorConductorHielera = new FConsultaPedidosDescargadosPorConductorHielera(this.ini);

            this.escritorio.add(fConsultaPedidosDescargadosPorConductorHielera);
            fConsultaPedidosDescargadosPorConductorHielera.setLocation(((ini.getDimension().width - fConsultaPedidosDescargadosPorConductorHielera.getSize().width) / 2), ((ini.getDimension().height - fConsultaPedidosDescargadosPorConductorHielera.getSize().height) / 2) - 30);
            fConsultaPedidosDescargadosPorConductorHielera.show();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FConsultaPedidosDescargadosPorConductorHielera");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioConsultas.class.getName()).log(Level.SEVERE, null, ex);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem80ActionPerformed

    private void jMenuItem69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem69ActionPerformed
        try {
            FIntegrador fIntegrador = new FIntegrador(ini);

            this.escritorio.add(fIntegrador);
            fIntegrador.setLocation(((ini.getDimension().width - fIntegrador.getSize().width) / 2), ((ini.getDimension().height - fIntegrador.getSize().height) / 2) - 30);
            //fIntegrador.setSize(ini.getDimension());
            fIntegrador.setVisible(true);
            fIntegrador.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FIntegrador");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem69ActionPerformed

    private void mnuManifestarPedidos8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos8MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuManifestarPedidos8MouseClicked

    private void mnuManifestarPedidos8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos8ActionPerformed
        try {
            FManifestarPedidosHielera_2 fManifestarPedidosHielera = new FManifestarPedidosHielera_2(ini);
            this.escritorio.add(fManifestarPedidosHielera);
            fManifestarPedidosHielera.setSize(ini.getDimension());
            fManifestarPedidosHielera.setLocation(((ini.getDimension().width - fManifestarPedidosHielera.getSize().width) / 2), ((ini.getDimension().height - fManifestarPedidosHielera.getSize().height) / 2));
            fManifestarPedidosHielera.setVisible(true);
            fManifestarPedidosHielera.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FManifestarPedidosHielera_2");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuManifestarPedidos8ActionPerformed

    private void mnuManifestarPedidos9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos9MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuManifestarPedidos9MouseClicked

    private void mnuManifestarPedidos9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos9ActionPerformed
        try {
            FControlarSalidaFacturasBarCode2 fControlarSalidaFacturasBarCode2 = new FControlarSalidaFacturasBarCode2(ini);

            this.escritorio.add(fControlarSalidaFacturasBarCode2);
            fControlarSalidaFacturasBarCode2.setLocation(((ini.getDimension().width - fControlarSalidaFacturasBarCode2.getSize().width) / 2), ((ini.getDimension().height - fControlarSalidaFacturasBarCode2.getSize().height) / 2) - 30);
            //fIntegrador.setSize(ini.getDimension());
            fControlarSalidaFacturasBarCode2.setVisible(true);
            fControlarSalidaFacturasBarCode2.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FControlarSalidaFacturas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// 
    }//GEN-LAST:event_mnuManifestarPedidos9ActionPerformed

    private void jMenuItem75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem75ActionPerformed
        try {
            DescargarFacturas_2 descargarFacturas_2 = new DescargarFacturas_2(this.ini);
            this.escritorio.add(descargarFacturas_2);
            //descargarFacturas_2.CargarVista();
            descargarFacturas_2.setSize(ini.getDimension());
            // fDescargarFActuras.setLocation(((ini.getDimension().width - fDescargarFActuras.getSize().width) / 2), ((ini.getDimension().height - fDescargarFActuras.getSize().height) / 2));
            descargarFacturas_2.setLocation(0, 0);
            descargarFacturas_2.setVisible(true);
            descargarFacturas_2.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  DescargarFacturas_2");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem75ActionPerformed

    private void jMenuItem76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem76ActionPerformed
        try {
            FTrasladoDeFacturas fTrasladoDeFacturas = new FTrasladoDeFacturas(ini);

            this.escritorio.add(fTrasladoDeFacturas);
            fTrasladoDeFacturas.setLocation(((ini.getDimension().width - fTrasladoDeFacturas.getSize().width) / 2), ((ini.getDimension().height - fTrasladoDeFacturas.getSize().height) / 2) - 30);
            fTrasladoDeFacturas.setVisible(true);
            fTrasladoDeFacturas.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FTrasladoDeFacturas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem76ActionPerformed

    private void jMenuItem77ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem77ActionPerformed
        try {
            FModificarManifiesto fModificarManifiesto = new FModificarManifiesto(ini);
            this.escritorio.add(fModificarManifiesto);
            fModificarManifiesto.setLocation(((ini.getDimension().width - fModificarManifiesto.getSize().width) / 2), ((ini.getDimension().height - fModificarManifiesto.getSize().height) / 2) - 30);
            fModificarManifiesto.setMaximizable(false);
            fModificarManifiesto.setVisible(true);
            fModificarManifiesto.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FModificarManifiesto");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:

    }//GEN-LAST:event_jMenuItem77ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed

        try {
            FConsultarProducto fConsultarProducto = new FConsultarProducto(this.ini);

            this.escritorio.add(fConsultarProducto);
            //             form1.setSize(screenSize.width,screenSize.height-43);
            //            form1.setMaximumSize(screenSize);
            fConsultarProducto.setLocation(((ini.getDimension().width - fConsultarProducto.getSize().width) / 2), ((ini.getDimension().height - fConsultarProducto.getSize().height) / 2));
            fConsultarProducto.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FConsultarFacturasRemoto");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem81ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem81ActionPerformed
        try {
            FReportemovilizadoPorConductor fReportemovilizadoPorConductor = new FReportemovilizadoPorConductor(this.ini);

            this.escritorio.add(fReportemovilizadoPorConductor);
            fReportemovilizadoPorConductor.setLocation(((ini.getDimension().width - fReportemovilizadoPorConductor.getSize().width) / 2), ((ini.getDimension().height - fReportemovilizadoPorConductor.getSize().height) / 2) - 30);
            fReportemovilizadoPorConductor.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReportemovilizadoPorConductor");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioConsultas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem81ActionPerformed

    private void jMenuItem40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem40ActionPerformed
        try {
            fReporteDePorteria = new FReporteDePorteria(ini);
            // TODO add your handling code here:FConsultarFacturasPorCliente

            this.escritorio.add(fReporteDePorteria);
            fReporteDePorteria.setLocation(((ini.getDimension().width - fReporteDePorteria.getSize().width) / 2), ((ini.getDimension().height - fReporteDePorteria.getSize().height) / 2) - 30);
            fReporteDePorteria.setVisible(true);
            fReporteDePorteria.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReporteDePorteria");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem40ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        /* Se inicia un integrador con TNS que va a actualizar todos los
        empleados*/

        new Thread(new HiloIntegradorTNS(ini, jMenuItem28)).start();
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem103ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem103ActionPerformed
        try {
            FReporteAlertasSalidaDeProductos fReporteAlertasSalidaDeProductos = new FReporteAlertasSalidaDeProductos(ini);
            // TODO add your handling code here:FConsultarFacturasPorCliente

            this.escritorio.add(fReporteAlertasSalidaDeProductos);
            fReporteAlertasSalidaDeProductos.setLocation(((ini.getDimension().width - fReporteAlertasSalidaDeProductos.getSize().width) / 2), ((ini.getDimension().height - fReporteAlertasSalidaDeProductos.getSize().height) / 2) - 30);
            fReporteAlertasSalidaDeProductos.setVisible(true);
            fReporteAlertasSalidaDeProductos.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReporteFacturasPendientes");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem103ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the fCambiarClave */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new PrincipalAdministradorDelSistema().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    @Override
    public Image getIconImage() {
        String ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "imagenes/iconoApp/" + ini.getPropiedades().getProperty("iconoApp");
        Image retValue = Toolkit.getDefaultToolkit().getImage(ruta);
        //getImage(ClassLoader.getSystemResource("aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"));
        return retValue;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JDesktopPane escritorio;
    private javax.swing.JMenuItem imActualizarClientes;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu16;
    private javax.swing.JMenu jMenu17;
    private javax.swing.JMenu jMenu18;
    private javax.swing.JMenu jMenu19;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu20;
    private javax.swing.JMenu jMenu21;
    private javax.swing.JMenu jMenu22;
    private javax.swing.JMenu jMenu23;
    private javax.swing.JMenu jMenu24;
    private javax.swing.JMenu jMenu25;
    private javax.swing.JMenu jMenu26;
    private javax.swing.JMenu jMenu27;
    private javax.swing.JMenu jMenu28;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu30;
    private javax.swing.JMenu jMenu31;
    private javax.swing.JMenu jMenu32;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    public static javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem103;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem20;
    private javax.swing.JMenuItem jMenuItem21;
    private javax.swing.JMenuItem jMenuItem22;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem24;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem26;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem37;
    private javax.swing.JMenuItem jMenuItem38;
    private javax.swing.JMenuItem jMenuItem39;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem40;
    private javax.swing.JMenuItem jMenuItem43;
    private javax.swing.JMenuItem jMenuItem44;
    private javax.swing.JMenuItem jMenuItem45;
    private javax.swing.JMenuItem jMenuItem46;
    private javax.swing.JMenuItem jMenuItem47;
    private javax.swing.JMenuItem jMenuItem48;
    private javax.swing.JMenuItem jMenuItem49;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem50;
    private javax.swing.JMenuItem jMenuItem51;
    private javax.swing.JMenuItem jMenuItem52;
    private javax.swing.JMenuItem jMenuItem53;
    private javax.swing.JMenuItem jMenuItem54;
    private javax.swing.JMenuItem jMenuItem55;
    private javax.swing.JMenuItem jMenuItem56;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem61;
    private javax.swing.JMenuItem jMenuItem62;
    private javax.swing.JMenuItem jMenuItem63;
    private javax.swing.JMenuItem jMenuItem64;
    private javax.swing.JMenuItem jMenuItem65;
    private javax.swing.JMenuItem jMenuItem66;
    private javax.swing.JMenuItem jMenuItem67;
    private javax.swing.JMenuItem jMenuItem69;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem75;
    private javax.swing.JMenuItem jMenuItem76;
    private javax.swing.JMenuItem jMenuItem77;
    private javax.swing.JMenuItem jMenuItem79;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem80;
    private javax.swing.JMenuItem jMenuItem81;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem menuCambiarClave;
    private javax.swing.JMenuItem miMantenimientos;
    private javax.swing.JMenu mnuArchivo;
    private javax.swing.JMenuItem mnuCambioClave;
    private javax.swing.JMenu mnuClientes;
    private javax.swing.JMenuItem mnuConsultarManifiestos;
    private javax.swing.JMenuItem mnuCrearUsuarios;
    private javax.swing.JMenuItem mnuDescargarFacturas;
    private javax.swing.JMenu mnuDescargueHielera;
    private javax.swing.JMenu mnuFlota;
    private javax.swing.JMenuItem mnuHabilitarFacturas;
    private javax.swing.JMenuItem mnuHabilitarManifiesto;
    private javax.swing.JMenuItem mnuManifestarPedidos;
    private javax.swing.JMenuItem mnuManifestarPedidos1;
    private javax.swing.JMenuItem mnuManifestarPedidos8;
    private javax.swing.JMenuItem mnuManifestarPedidos9;
    private javax.swing.JMenu mnuPersonal;
    private javax.swing.JMenu mnuReportes;
    private javax.swing.JMenu mnuVehiculos;
    private javax.swing.JMenuItem mnu_rptFacturacionPorCanal;
    private javax.swing.JMenuItem mnu_rptFacturacionTAT;
    // End of variables declaration//GEN-END:variables

    /*Funcion que permite colocar imagen al fondo del escritorio */
    public final void cargarImagen(javax.swing.JDesktopPane jDeskp, InputStream fileImagen) {
        try {
            BufferedImage image = ImageIO.read(fileImagen);
//              jDeskp.setBorder(new Fondo(image)); 

        } catch (Exception e) {
            System.out.println("Imagen no disponible");
        }
    }

    private void timerEvent(java.awt.event.ActionEvent evt) {
        if (timer.isRunning()) {
            timer.stop();
            System.out.println("se detiene el timer " + new Date());
            //    System.exit(0);
//            timer = new Timer(60000, actionListener);
//            System.out.println("se llama al timer");
//            timer.start();
//            System.out.println("inicia el timer");
//            System.out.println("expired en funcion");
        }
    }

}
