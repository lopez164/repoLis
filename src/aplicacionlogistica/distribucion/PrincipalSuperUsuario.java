/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion;

//import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import GPS.Monitoreo.PuntosMonitoreo;
import GPS.PuntosGps.PuntosGps;
import GPS.PuntosGps_rutas.PuntosGps_rutas;
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
import aplicacionlogistica.configuracion.formularios.FormularioConfiguracionDelSistema;
import aplicacionlogistica.costumerService.FConsultaManifiestosDescargados;
import aplicacionlogistica.costumerService.FGestionarIncidencias;
import aplicacionlogistica.costumerService.FIncidenciasSvC;
import aplicacionlogistica.costumerService.FIngresarDescuentoClientes;
import aplicacionlogistica.costumerService.FIngresarRecogidasClientes;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.FCrearCargos;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.FCrearCentrosDeCosto;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.FCrearEntidadesBancarias;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.FCrearTiposContratosPersonas;
import mtto.vehiculos.Administracion.FCrearEstadosVehiculos;
import mtto.vehiculos.Administracion.FCrearLineasXMarcaDeVehiculo;
import mtto.vehiculos.Administracion.FCrearMarcasDeLLantas;
import mtto.vehiculos.Administracion.FCrearMarcasVehiculos;
import mtto.vehiculos.Administracion.FCrearTiposCarrocerias;
import mtto.vehiculos.Administracion.FCrearTiposdeCombustible;
import mtto.vehiculos.Administracion.FCrearTiposdeContratosVehiculos;
import mtto.vehiculos.Administracion.FCrearTiposdeDocumentos;
import mtto.vehiculos.Administracion.FCrearTiposdeServicios;
import mtto.vehiculos.Administracion.FCrearTiposdeVehiculos;
import aplicacionlogistica.distribucion.administracion.logistica.FCrearCanalDeVentas;
import aplicacionlogistica.distribucion.administracion.logistica.FCrearCausalesDEDevolucion;
import aplicacionlogistica.distribucion.administracion.logistica.FCrearEstadosManifiestos;
import aplicacionlogistica.distribucion.consultas.FConsultaManifiestosSinDescargar;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasPorCliente;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.consultas.FHabilitarManifiesto;
import aplicacionlogistica.distribucion.formularios.FAdicionarPedidosNoReportados;
import aplicacionlogistica.distribucion.formularios.FAnularFacturaSinMovimiento;
import aplicacionlogistica.distribucion.formularios.FCrearFacturasDeVenta;
import aplicacionlogistica.distribucion.formularios.Hielera.FManifestarPedidosHielera_2;
import aplicacionlogistica.distribucion.formularios.FModificarManifiesto;
import aplicacionlogistica.distribucion.formularios.Hielera.DescargarFacturas_2;
import aplicacionlogistica.distribucion.formularios.ReporteManifiestosSinDescargar.FReporteManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.administracion.FBorrarFacturaManifiesto;
import aplicacionlogistica.distribucion.administracion.logistica.FCrearRutasDeDistribucion;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import aplicacionlogistica.distribucion.formularios.Hielera.FControlarSalidaFacturasBarCode2;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultaPedidosDescargadosPorConductorHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarPedidosConductorHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarProducto;
import aplicacionlogistica.distribucion.formularios.administracion.FDeshabilitarManifiestoAbierto;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarCarros;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarEmpleados;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarUsuarios;
import aplicacionlogistica.distribucion.formularios.poblaciones.FManifestarPedidosPoblaciones;
import aplicacionlogistica.distribucion.formularios.reportes.FDashBoardFacturas;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteAlertasSalidaDeProductos;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteDePorteria;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteDeRechazosTotales;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteFacturasGeneradas;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteFacturasPendientes;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteGeneralDeDescargueDeRutas;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteManifiestosConcilidos;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteManifiestosMovilizadosPorConductor;
import aplicacionlogistica.distribucion.formularios.reportes.FReportePedidosMovilizadosPorPeriodo;
import aplicacionlogistica.distribucion.formularios.reportes.FReportePedidosEnDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteManifiestosEnDistribucion.FReporteManifiestosEnDistribucion;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteMovilizadoPorConductor.FReportemovilizadoPorConductor;
import aplicacionlogistica.distribucion.formularios.reportes.facturacion.FReporteFacturacionPorCanal;
import aplicacionlogistica.distribucion.formularios.reportes.facturacion.FReporteFacturacionTAT;
import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcelPacheco;
import aplicacionlogistica.distribucion.integrador.FIntegrador;
import aplicacionlogistica.distribucion.integrador.FIntegradorExcelFile;
import aplicacionlogistica.distribucion.integrador.HiloIntegradorTNS;
import aplicacionlogistica.picking.FManifestarFacturasEnPicking;
import aplicacionlogistica.picking.FRegistroDePedidosFinLineaDePicking;
import java.awt.Image;
import java.awt.Point;
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
import mtto.Administracion.FCrearCuentasPrincipales;
import mtto.Administracion.FCrearCuentasSecundarias;
import mtto.Servicios.FAgregarMantenimientoVehiculo;
import mtto.ingresoDeRegistros.FImportarConsumoCombustible;
import mtto.documentos.FIngresarDocumentosVehiculos;
import mtto.documentos.Reportes.FReporteDocumentosVehiculos;
import mtto.documentos.Reportes.FReporteListadoCarrosPropios;
import mtto.personal.FReporteDelPersonal;
import mtto.proveedores.IngresarProveedores;
import mtto.proveedores.IngresarSucursalDeProveedor;

/**
 *
 * @author VLI_488
 */
public class PrincipalSuperUsuario extends javax.swing.JFrame {

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

    FReportePedidosEnDistribucion fReportePedidosEnDistribucion = null;

    public FManifestarFacturasEnPicking fFacturasEnPicking = null;
    public FManifestarPedidosEnRuta fManifestarPedidosEnRuta = null;
    public FRegistroDePedidosFinLineaDePicking fRegistroDePedidosFinLineaDePicking = null;
    public FGeoreferenciarClientes fGeoreferenciarClientes = null;
    public FAdicionarPedidosNoReportados fAdicionarPedidosNoReportados = null;

    FManifestarPedidosHielera_2 fManifestarPedidosHielera = null;
    DescargarFacturas_2 descargarFacturas_2 = null;

   // public PuntosGps puntosGps = null;
    public PuntosGps_rutas puntosGps = null;
    FReporteDePorteria fReporteDePorteria = null;
    FReporteFacturasGeneradas fReporteFacturasGeneradas = null;
    String rutaImagenDeFondo = null;

    List<String> datos = new ArrayList<>();
    String usuario;
    String formulario = "PrincipalSuperUsuario";

    ActionListener actionListener;
    public Timer timer;

    public PrincipalSuperUsuario() throws Exception {
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

    public PrincipalSuperUsuario(Inicio ini) throws Exception, Throwable {

        //initComponents();
        this.ini = ini;
        String ruta;
        usuario = Inicio.deCifrar(ini.getUser().getNombreUsuario());
        formulario = "PrincipalSuperUsuario";

        //if (ini.isEstaClienteActivo()) {
        if (true) {

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

            try {
                ini.cargarImagenEscritorio(escritorio);
                /** FDashBoardFacturas fDashBoardFacturas = new FDashBoardFacturas(ini);
                this.escritorio.add(fDashBoardFacturas);
                fDashBoardFacturas.setVisible(true);
                fDashBoardFacturas.show(); */
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Revisar archivo de configuracion", "Imagen de Fondo no encontrada", JOptionPane.ERROR_MESSAGE);
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
                    fCambiarClave.txtUsuario.setText(usuario);

                    fCambiarClave.setLocation((ini.getDimension().width - fCambiarClave.getSize().width) / 2, (ini.getDimension().height - fCambiarClave.getSize().height) / 2);
                    jMenuBar1.setVisible(false);
                    fCambiarClave.setResizable(false);
                    fCambiarClave.show();
                    datos = new ArrayList<>();
                    datos.add(0, usuario);
                    datos.add(1, formulario);
                    datos.add(2, "Inicio primera vez, cambio de Clave");
                    datos.add(3, "CURRENT_TIMESTAMP");
                    new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

                    if (timer.isRunning()) {
                        timer.stop();
                    }
                    timer.start();
                    fCambiarClave.setVisible(true);
                    fCambiarClave.txtClaveAnterior.requestFocus();

                }

            } else {

            }

//            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
//                mnuDistribucionLogistica.setVisible(true);
//                mnuDistribucionLaHielera.setVisible(false);
//            }  
//            if (ini.getPropiedades().getProperty("idOperador").equals("2")) {
//                mnuDistribucionLogistica.setVisible(false);
//                mnuDistribucionLaHielera.setVisible(true);
//            }
            iniciarVariables();

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
        jMenu10 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu20 = new javax.swing.JMenu();
        jMenu18 = new javax.swing.JMenu();
        jMenu38 = new javax.swing.JMenu();
        jMenuItem95 = new javax.swing.JMenuItem();
        jMenuItem96 = new javax.swing.JMenuItem();
        jMenu39 = new javax.swing.JMenu();
        jMenuItem97 = new javax.swing.JMenuItem();
        jMenuItem98 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem84 = new javax.swing.JMenuItem();
        jMenuItem85 = new javax.swing.JMenuItem();
        jMenuItem86 = new javax.swing.JMenuItem();
        jMenuItem87 = new javax.swing.JMenuItem();
        jMenuItem88 = new javax.swing.JMenuItem();
        jMenuItem89 = new javax.swing.JMenuItem();
        jMenuItem90 = new javax.swing.JMenuItem();
        jMenuItem91 = new javax.swing.JMenuItem();
        jMenuItem92 = new javax.swing.JMenuItem();
        jMenuItem94 = new javax.swing.JMenuItem();
        jMenu26 = new javax.swing.JMenu();
        jMenu25 = new javax.swing.JMenu();
        jMenu27 = new javax.swing.JMenu();
        jMenuItem74 = new javax.swing.JMenuItem();
        jMenuItem37 = new javax.swing.JMenuItem();
        jMenuItem99 = new javax.swing.JMenuItem();
        jMenuItem36 = new javax.swing.JMenuItem();
        miMantenimientos = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        mnuPersonal = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        mnuCrearUsuarios = new javax.swing.JMenuItem();
        mnuCambioClave = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        mItmEmpleados = new javax.swing.JMenuItem();
        jMenuItem76 = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        mnuItmDeshabilitarMfto = new javax.swing.JMenuItem();
        jMenuItem82 = new javax.swing.JMenuItem();
        jMenuItem69 = new javax.swing.JMenuItem();
        jMenuItem83 = new javax.swing.JMenuItem();
        mnuVehiculos = new javax.swing.JMenu();
        jMenu29 = new javax.swing.JMenu();
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
        jMenu32 = new javax.swing.JMenu();
        jMenuItem45 = new javax.swing.JMenuItem();
        jMenuItem48 = new javax.swing.JMenuItem();
        jMenuItem67 = new javax.swing.JMenuItem();
        jMenuItem62 = new javax.swing.JMenuItem();
        jMenuItem66 = new javax.swing.JMenuItem();
        jMenuItem47 = new javax.swing.JMenuItem();
        jMenuItem56 = new javax.swing.JMenuItem();
        jMenuItem61 = new javax.swing.JMenuItem();
        jMenuItem65 = new javax.swing.JMenuItem();
        jMenuItem46 = new javax.swing.JMenuItem();
        jMenu33 = new javax.swing.JMenu();
        jMenuItem57 = new javax.swing.JMenuItem();
        jMenuItem58 = new javax.swing.JMenuItem();
        jMenuItem59 = new javax.swing.JMenuItem();
        jMenuItem60 = new javax.swing.JMenuItem();
        jMenu37 = new javax.swing.JMenu();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenuItem93 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem40 = new javax.swing.JMenuItem();
        jMenuItem75 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        mnuDistribucionLogistica = new javax.swing.JMenu();
        mnuManifestarPedidos = new javax.swing.JMenuItem();
        mnuDescargarFacturas = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        mnuManifestarPedidos1 = new javax.swing.JMenuItem();
        jMenuItem41 = new javax.swing.JMenuItem();
        jMenuItem101 = new javax.swing.JMenuItem();
        mnuHabilitarManifiesto = new javax.swing.JMenuItem();
        mnuHabilitarFacturas = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem31 = new javax.swing.JMenuItem();
        mnuDistribucionLaHielera = new javax.swing.JMenu();
        jMenuItem68 = new javax.swing.JMenuItem();
        mnuManifestarPedidos6 = new javax.swing.JMenuItem();
        mnuManifestarPedidos7 = new javax.swing.JMenuItem();
        mnuDescargueHielera = new javax.swing.JMenuItem();
        jMenuItem72 = new javax.swing.JMenuItem();
        jMenuItem73 = new javax.swing.JMenuItem();
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
        jMenu36 = new javax.swing.JMenu();
        jMenuItem79 = new javax.swing.JMenuItem();
        jMenuItem80 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        mnuReportes = new javax.swing.JMenu();
        jMenu16 = new javax.swing.JMenu();
        jMenuItem38 = new javax.swing.JMenuItem();
        jMenuItem39 = new javax.swing.JMenuItem();
        jMenuItem100 = new javax.swing.JMenuItem();
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
        jMenuItem42 = new javax.swing.JMenuItem();
        jMenuItem81 = new javax.swing.JMenuItem();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenuItem103 = new javax.swing.JMenuItem();
        jMenu12 = new javax.swing.JMenu();
        jMenuItem102 = new javax.swing.JMenuItem();
        jMenu13 = new javax.swing.JMenu();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenuItem34 = new javax.swing.JMenuItem();
        jMenu34 = new javax.swing.JMenu();
        jMenu35 = new javax.swing.JMenu();
        jMenuItem70 = new javax.swing.JMenuItem();
        jMenuItem71 = new javax.swing.JMenuItem();
        jMenuItem77 = new javax.swing.JMenuItem();
        jMenuItem78 = new javax.swing.JMenuItem();
        jMenu17 = new javax.swing.JMenu();
        jMenuItem104 = new javax.swing.JMenuItem();
        jMenuItem105 = new javax.swing.JMenuItem();

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
            .addGap(0, 287, Short.MAX_VALUE)
        );

        jMenuBar1.setEnabled(false);
        jMenuBar1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuBar1MouseClicked(evt);
            }
        });

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

        jMenu10.setText("Flota");

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/lorry_go.png"))); // NOI18N
        jMenu4.setText("Transporte");
        jMenu10.add(jMenu4);

        jMenu20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Equipment.png"))); // NOI18N
        jMenu20.setText("Administracion");

        jMenu18.setText("Financiero");

        jMenu38.setText("Proveedores");

        jMenuItem95.setText("Proveedores");
        jMenuItem95.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem95ActionPerformed(evt);
            }
        });
        jMenu38.add(jMenuItem95);

        jMenuItem96.setText("Sucursales");
        jMenuItem96.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem96ActionPerformed(evt);
            }
        });
        jMenu38.add(jMenuItem96);

        jMenu18.add(jMenu38);

        jMenu39.setText("Cuentas");

        jMenuItem97.setText("Cta. Ppal");
        jMenuItem97.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem97ActionPerformed(evt);
            }
        });
        jMenu39.add(jMenuItem97);

        jMenuItem98.setText("Cta. Secundaria");
        jMenuItem98.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem98ActionPerformed(evt);
            }
        });
        jMenu39.add(jMenuItem98);

        jMenu18.add(jMenu39);

        jMenu20.add(jMenu18);

        jMenuItem27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/lorry.png"))); // NOI18N
        jMenuItem27.setText("Lineas X Marca");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem27);

        jMenuItem28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/lorry.png"))); // NOI18N
        jMenuItem28.setText("Tipos Documentos");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem28);

        jMenuItem84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem84.setText("Marcas Vehiculos");
        jMenuItem84.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem84ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem84);

        jMenuItem85.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem85.setText("Tipos Carrocerias");
        jMenuItem85.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem85ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem85);

        jMenuItem86.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem86.setText("Tipos Combustible");
        jMenuItem86.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem86ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem86);

        jMenuItem87.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem87.setText("Tipos Contrato");
        jMenuItem87.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem87ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem87);

        jMenuItem88.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem88.setText("Tipos Servicio");
        jMenuItem88.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem88ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem88);

        jMenuItem89.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem89.setText("Tipos Vehiculos");
        jMenuItem89.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem89ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem89);

        jMenuItem90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem90.setText("Marcas LLantas");
        jMenuItem90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem90ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem90);

        jMenuItem91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem91.setText("Estados Vehiculo");
        jMenuItem91.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem91ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem91);

        jMenuItem92.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem92.setText("Tipos Documentos");
        jMenuItem92.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem92ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem92);

        jMenuItem94.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem94.setText("LineasVehiculos");
        jMenuItem94.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem94ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem94);

        jMenu10.add(jMenu20);

        jMenu26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/tire_icon.png"))); // NOI18N
        jMenu26.setText("LLantas");
        jMenu10.add(jMenu26);

        jMenu25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/order-icon.png"))); // NOI18N
        jMenu25.setText("Ordenes");
        jMenu10.add(jMenu25);

        jMenu27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Gas-pump-icon.png"))); // NOI18N
        jMenu27.setText("Combustibles");

        jMenuItem74.setText("cargar Combustibles Excel");
        jMenuItem74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem74ActionPerformed(evt);
            }
        });
        jMenu27.add(jMenuItem74);

        jMenu10.add(jMenu27);

        jMenuItem37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/page_add.png"))); // NOI18N
        jMenuItem37.setText("ingresar Facturas");
        jMenuItem37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem37ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem37);

        jMenuItem99.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem99.setText("Carros");
        jMenuItem99.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem99ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem99);

        jMenuItem36.setText("Ingresar Documentos");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem36);

        miMantenimientos.setText("Mantenimiento ");
        miMantenimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMantenimientosActionPerformed(evt);
            }
        });
        jMenu10.add(miMantenimientos);

        jMenuBar1.add(jMenu10);

        jMenu7.setText("Administracion");

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

        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png"))); // NOI18N
        jMenuItem15.setText("Sincronizar Personal");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        mnuPersonal.add(jMenuItem15);

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

        jMenu7.add(mnuPersonal);

        mItmEmpleados.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/map_add.png"))); // NOI18N
        mItmEmpleados.setText("Rutas");
        mItmEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmEmpleadosActionPerformed(evt);
            }
        });
        jMenu7.add(mItmEmpleados);

        jMenuItem76.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Application.png"))); // NOI18N
        jMenuItem76.setText("Parametrizar");
        jMenuItem76.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem76ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem76);

        jMenuBar1.add(jMenu7);

        jMenu14.setText("Auditoria");
        jMenu14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu14ActionPerformed(evt);
            }
        });

        mnuItmDeshabilitarMfto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/No-entry.png"))); // NOI18N
        mnuItmDeshabilitarMfto.setText("Deshabilitar Manifiesto");
        mnuItmDeshabilitarMfto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItmDeshabilitarMftoActionPerformed(evt);
            }
        });
        jMenu14.add(mnuItmDeshabilitarMfto);

        jMenuItem82.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Erase.png"))); // NOI18N
        jMenuItem82.setText("Eiminar factura sin  Mov.");
        jMenuItem82.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem82ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem82);

        jMenuItem69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Erase.png"))); // NOI18N
        jMenuItem69.setText("Eliminar Facturaxmafto");
        jMenuItem69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem69ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem69);

        jMenuItem83.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/No-entry.png"))); // NOI18N
        jMenuItem83.setText("Deshabilitar Mafto Sin Mov.");
        jMenuItem83.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem83ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem83);

        jMenuBar1.add(jMenu14);

        mnuVehiculos.setText("Logistica");
        mnuVehiculos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVehiculosActionPerformed(evt);
            }
        });

        jMenu29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Boss.png"))); // NOI18N
        jMenu29.setText("Parametrizacion");

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

        jMenu29.add(jMenu30);

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

        jMenu29.add(jMenu31);

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

        jMenuItem61.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem61.setText("Estados Vehiculo");
        jMenuItem61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem61ActionPerformed(evt);
            }
        });
        jMenu32.add(jMenuItem61);

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

        jMenu29.add(jMenu32);

        jMenu33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/List.png"))); // NOI18N
        jMenu33.setText("Facturacion");

        jMenuItem57.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem57.setText("Clientes");
        jMenu33.add(jMenuItem57);

        jMenuItem58.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem58.setText("Productos");
        jMenu33.add(jMenuItem58);

        jMenuItem59.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem59.setText("Facturas");
        jMenu33.add(jMenuItem59);

        jMenuItem60.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem60.setText("Detalle Factura");
        jMenu33.add(jMenuItem60);

        jMenu29.add(jMenu33);

        jMenu37.setText("Documentos");

        jMenuItem26.setText("Tipod Documentos");
        jMenu37.add(jMenuItem26);

        jMenuItem93.setText("Doc X LineaVeh");
        jMenu37.add(jMenuItem93);

        jMenu29.add(jMenu37);

        mnuVehiculos.add(jMenu29);

        jMenu9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N
        jMenu9.setText("Importar  Datos");

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

        jMenuItem40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Sync.png"))); // NOI18N
        jMenuItem40.setText("Integrador");
        jMenuItem40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem40ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem40);

        jMenuItem75.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        jMenuItem75.setText("TNS-SQL");
        jMenuItem75.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem75MouseClicked(evt);
            }
        });
        jMenuItem75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem75ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem75);

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

        mnuDistribucionLogistica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        mnuDistribucionLogistica.setText("distribucion");

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
        mnuDistribucionLogistica.add(mnuManifestarPedidos);

        mnuDescargarFacturas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        mnuDescargarFacturas.setText("Descargar Facturas");
        mnuDescargarFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDescargarFacturasActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(mnuDescargarFacturas);

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Erase.png"))); // NOI18N
        jMenuItem4.setText("Anular Facturas");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(jMenuItem4);

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Good mark.png"))); // NOI18N
        jMenuItem1.setText("Conciliar rutas");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(jMenuItem1);

        jMenuItem24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Move.png"))); // NOI18N
        jMenuItem24.setText("Traslado de Facturas");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(jMenuItem24);

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
        mnuDistribucionLogistica.add(mnuManifestarPedidos1);

        jMenuItem41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Update.png"))); // NOI18N
        jMenuItem41.setText("Modificar Manifiesto");
        jMenuItem41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem41ActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(jMenuItem41);

        jMenuItem101.setText("Agregar Factura a Manifiesto");
        jMenuItem101.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem101ActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(jMenuItem101);

        mnuVehiculos.add(mnuDistribucionLogistica);

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

        mnuDistribucionLaHielera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        mnuDistribucionLaHielera.setText("distribucion Hielera");

        jMenuItem68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/logo_hielerablue.png"))); // NOI18N
        jMenuItem68.setText("Integrador Hielera");
        jMenuItem68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem68ActionPerformed(evt);
            }
        });
        mnuDistribucionLaHielera.add(jMenuItem68);

        mnuManifestarPedidos6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Deliveries.png"))); // NOI18N
        mnuManifestarPedidos6.setText("Manifestar Domicilios");
        mnuManifestarPedidos6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuManifestarPedidos6MouseClicked(evt);
            }
        });
        mnuManifestarPedidos6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuManifestarPedidos6ActionPerformed(evt);
            }
        });
        mnuDistribucionLaHielera.add(mnuManifestarPedidos6);

        mnuManifestarPedidos7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Go forward.png"))); // NOI18N
        mnuManifestarPedidos7.setText("Salidas a Ruta");
        mnuManifestarPedidos7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuManifestarPedidos7MouseClicked(evt);
            }
        });
        mnuManifestarPedidos7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuManifestarPedidos7ActionPerformed(evt);
            }
        });
        mnuDistribucionLaHielera.add(mnuManifestarPedidos7);

        mnuDescargueHielera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Go back.png"))); // NOI18N
        mnuDescargueHielera.setText("Descargue Hielera");
        mnuDescargueHielera.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDescargueHieleraActionPerformed(evt);
            }
        });
        mnuDistribucionLaHielera.add(mnuDescargueHielera);

        jMenuItem72.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Move.png"))); // NOI18N
        jMenuItem72.setText("Traslado de Facturas");
        jMenuItem72.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem72ActionPerformed(evt);
            }
        });
        mnuDistribucionLaHielera.add(jMenuItem72);

        jMenuItem73.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Update.png"))); // NOI18N
        jMenuItem73.setText("Modificar Manifiesto");
        jMenuItem73.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem73ActionPerformed(evt);
            }
        });
        mnuDistribucionLaHielera.add(jMenuItem73);

        mnuVehiculos.add(mnuDistribucionLaHielera);

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
        mnuConsultarManifiestos.setText("Manifiestos x Cond");
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

        jMenu5.add(jMenu15);

        jMenu36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        jMenu36.setText("Manifiestos Hielera");

        jMenuItem79.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem79.setText("Movilizado Conductor");
        jMenuItem79.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem79ActionPerformed(evt);
            }
        });
        jMenu36.add(jMenuItem79);

        jMenuItem80.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem80.setText("Facturas Descargadas");
        jMenuItem80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem80ActionPerformed(evt);
            }
        });
        jMenu36.add(jMenuItem80);

        jMenu5.add(jMenu36);

        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Search.png"))); // NOI18N
        jMenuItem12.setText("Producto");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem12);

        jMenuBar1.add(jMenu5);

        mnuReportes.setText("Reportes");

        jMenu16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/lorry_go.png"))); // NOI18N
        jMenu16.setText("Flota");

        jMenuItem38.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Accounting.png"))); // NOI18N
        jMenuItem38.setText("Gastos");
        jMenuItem38.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem38ActionPerformed(evt);
            }
        });
        jMenu16.add(jMenuItem38);

        jMenuItem39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/List.png"))); // NOI18N
        jMenuItem39.setText("Documentos Flota");
        jMenuItem39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem39ActionPerformed(evt);
            }
        });
        jMenu16.add(jMenuItem39);

        jMenuItem100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/moving_truck.png"))); // NOI18N
        jMenuItem100.setText("lista Carros");
        jMenuItem100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem100ActionPerformed(evt);
            }
        });
        jMenu16.add(jMenuItem100);

        mnuReportes.add(jMenu16);

        jMenu28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Company.png"))); // NOI18N
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

        jMenuItem42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem42.setText("Reporte general Descargue Rutas");
        jMenuItem42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem42ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem42);

        jMenuItem81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem81.setText("Mov X Conductor por periodo");
        jMenuItem81.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem81ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem81);

        jMenuItem35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem35.setText("Reporte de Porteria");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem35);

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

        jMenuItem102.setText("Facturas Generadas");
        jMenuItem102.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem102ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem102);

        jMenuBar1.add(jMenu12);

        jMenu13.setText("Acerca de");

        jMenuItem32.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Database.png"))); // NOI18N
        jMenuItem32.setText("Server Local");
        jMenuItem32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem32ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem32);

        jMenuItem33.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Database.png"))); // NOI18N
        jMenuItem33.setText("Servidor Remoto");
        jMenuItem33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem33ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem33);

        jMenuItem34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Briefcase.png"))); // NOI18N
        jMenuItem34.setText("Cliente");
        jMenuItem34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem34ActionPerformed(evt);
            }
        });
        jMenu13.add(jMenuItem34);

        jMenuBar1.add(jMenu13);

        jMenu34.setText("Costumer Service");

        jMenu35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/People.png"))); // NOI18N
        jMenu35.setText("Incidencias CS");

        jMenuItem70.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/New.png"))); // NOI18N
        jMenuItem70.setText("Crear Incidencia");
        jMenuItem70.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem70ActionPerformed(evt);
            }
        });
        jMenu35.add(jMenuItem70);

        jMenuItem71.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Address book.png"))); // NOI18N
        jMenuItem71.setText("Incidencias");
        jMenuItem71.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem71ActionPerformed(evt);
            }
        });
        jMenu35.add(jMenuItem71);

        jMenu34.add(jMenu35);

        jMenuItem77.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem77.setText("Descuentos");
        jMenuItem77.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem77ActionPerformed(evt);
            }
        });
        jMenu34.add(jMenuItem77);

        jMenuItem78.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Red pin.png"))); // NOI18N
        jMenuItem78.setText("Recogidas");
        jMenuItem78.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem78ActionPerformed(evt);
            }
        });
        jMenu34.add(jMenuItem78);

        jMenuBar1.add(jMenu34);

        jMenu17.setText("Gps    ");

        jMenuItem104.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/map_edit.png"))); // NOI18N
        jMenuItem104.setText("Mapa de Rutas");
        jMenuItem104.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem104ActionPerformed(evt);
            }
        });
        jMenu17.add(jMenuItem104);

        jMenuItem105.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/route_25x25.png"))); // NOI18N
        jMenuItem105.setText("Monitoreo");
        jMenuItem105.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem105ActionPerformed(evt);
            }
        });
        jMenu17.add(jMenuItem105);

        jMenuBar1.add(jMenu17);

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
            this.escritorio.add(fCambiarClave);
            fCambiarClave.txtUsuario.setText(usuario);
            fCambiarClave.setLocation((ini.getDimension().width - fCambiarClave.getSize().width) / 2, (ini.getDimension().height - fCambiarClave.getSize().height) / 2);
            fCambiarClave.show();
            fCambiarClave.setVisible(true);
            fCambiarClave.txtClaveAnterior.requestFocus();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  CambiarClave");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuCambiarClaveActionPerformed

    private void mnuVehiculosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVehiculosActionPerformed

    }//GEN-LAST:event_mnuVehiculosActionPerformed

    private void jMenuItem16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem16MouseClicked

    }//GEN-LAST:event_jMenuItem16MouseClicked

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        try {
            FImportarArchivoExcelPacheco fImportarExcel = new FImportarArchivoExcelPacheco(ini);

            this.escritorio.add(fImportarExcel);
            fImportarExcel.setLocation(((ini.getDimension().width - fImportarExcel.getSize().width) / 2), ((ini.getDimension().height - fImportarExcel.getSize().height) / 2));
            fImportarExcel.setVisible(true);
            fImportarExcel.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FImportarArchivoExcel");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void mnuManifestarPedidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuManifestarPedidosMouseClicked

    }//GEN-LAST:event_mnuManifestarPedidosMouseClicked

    private void mnuManifestarPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuManifestarPedidosActionPerformed
        try {
            FManifestarPedidosEnRuta fManifestarPedidosEnRuta = new FManifestarPedidosEnRuta(ini);
            this.escritorio.add(fManifestarPedidosEnRuta);
            fManifestarPedidosEnRuta.setSize(ini.getDimension());
            fManifestarPedidosEnRuta.setLocation(((ini.getDimension().width - fManifestarPedidosEnRuta.getSize().width) / 2), ((ini.getDimension().height - fManifestarPedidosEnRuta.getSize().height) / 2));
            fManifestarPedidosEnRuta.setVisible(true);
            fManifestarPedidosEnRuta.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FManifestarPedidosEnRuta");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

//             FManifestarPedidosEnRutaConIntegrador fManifestarPedidosEnRuta = new FManifestarPedidosEnRutaConIntegrador(ini);
//            this.escritorio.add(fManifestarPedidosEnRuta);
//            fManifestarPedidosEnRuta.setSize(ini.getDimension());
//            fManifestarPedidosEnRuta.setLocation(((ini.getDimension().width - fManifestarPedidosEnRuta.getSize().width) / 2), ((ini.getDimension().height - fManifestarPedidosEnRuta.getSize().height) / 2));
//            fManifestarPedidosEnRuta.setVisible(true);
//            fManifestarPedidosEnRuta.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_mnuManifestarPedidosActionPerformed

    private void mnuDescargarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDescargarFacturasActionPerformed
        try {
            fDescargarFActuras = new DescargarFacturas(this.ini);
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
            datos.add(2, "Click en form  DescargarFacturas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_mnuDescargarFacturasActionPerformed

    private void mnuConsultarManifiestosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConsultarManifiestosActionPerformed
        try {
            FReporteManifiestosMovilizadosPorConductor fReporteManifiestosMovilizadosPorConductor = new FReporteManifiestosMovilizadosPorConductor(ini);

            this.escritorio.add(fReporteManifiestosMovilizadosPorConductor);
            fReporteManifiestosMovilizadosPorConductor.setLocation(((ini.getDimension().width - fReporteManifiestosMovilizadosPorConductor.getSize().width) / 2), ((ini.getDimension().height - fReporteManifiestosMovilizadosPorConductor.getSize().height) / 2));
            fReporteManifiestosMovilizadosPorConductor.setVisible(true);
            fReporteManifiestosMovilizadosPorConductor.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReporteManifiestosMovilizadosPorConductor");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuConsultarManifiestosActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            fReporteRechazosTotales = new FReporteDeRechazosTotales(ini);

            this.escritorio.add(fReporteRechazosTotales);
            fReporteRechazosTotales.setLocation(((ini.getDimension().width - fReporteRechazosTotales.getSize().width) / 2), ((ini.getDimension().height - fReporteRechazosTotales.getSize().height) / 2));
            fReporteRechazosTotales.setVisible(true);
            fReporteRechazosTotales.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReporteDeRechazosTotales");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
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
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FAnularFacturaSinMovimiento");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
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


    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed

        try {
            fConsultarFacturasPorCliente = new FConsultarFacturasPorCliente(ini);

            this.escritorio.add(fConsultarFacturasPorCliente);
            fConsultarFacturasPorCliente.setLocation(((ini.getDimension().width - fConsultarFacturasPorCliente.getSize().width) / 2), ((ini.getDimension().height - fConsultarFacturasPorCliente.getSize().height) / 2) - 30);
            fConsultarFacturasPorCliente.setVisible(true);
            fConsultarFacturasPorCliente.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FConsultarFacturasPorCliente");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed

        try {
            fReporteMovilizado = new FReportePedidosMovilizadosPorPeriodo(ini);

            this.escritorio.add(fReporteMovilizado);
            fReporteMovilizado.setLocation(((ini.getDimension().width - fReporteMovilizado.getSize().width) / 2), ((ini.getDimension().height - fReporteMovilizado.getSize().height) / 2) - 30);
            fReporteMovilizado.setVisible(true);
            fReporteMovilizado.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReportePedidosMovilizadosPorPeriodo");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed

        try {

            fReporteFacturasPendientes = new FReporteFacturasPendientes(ini);
            this.escritorio.add(fReporteFacturasPendientes);
            fReporteFacturasPendientes.setVisible(true);
            fReporteFacturasPendientes.show();
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
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        try {
            fConsultarManifiestos = new FConsultarManifiestos(ini);

            this.escritorio.add(fConsultarManifiestos);
            fConsultarManifiestos.setLocation(((ini.getDimension().width - fConsultarManifiestos.getSize().width) / 2), ((ini.getDimension().height - fConsultarManifiestos.getSize().height) / 2) - 30);
            fConsultarManifiestos.setVisible(true);
            fConsultarManifiestos.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FConsultarManifiestos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void mnuHabilitarManifiestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuHabilitarManifiestoActionPerformed
        try {
            fHabilitarManifiesto = new FHabilitarManifiesto(ini);

            this.escritorio.add(fHabilitarManifiesto);
            fHabilitarManifiesto.setLocation(((ini.getDimension().width - fHabilitarManifiesto.getSize().width) / 2), ((ini.getDimension().height - fHabilitarManifiesto.getSize().height) / 2) - 30);
            fHabilitarManifiesto.setVisible(true);
            fHabilitarManifiesto.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FHabilitarManifiesto");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuHabilitarManifiestoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            fReporteManifiestosConcilidos = new FReporteManifiestosConcilidos(ini);

            this.escritorio.add(fReporteManifiestosConcilidos);
            fReporteManifiestosConcilidos.setLocation(((ini.getDimension().width - fReporteManifiestosConcilidos.getSize().width) / 2), ((ini.getDimension().height - fReporteManifiestosConcilidos.getSize().height) / 2) - 30);
            //fManifiestosConciliados.setVisible(true);
            fReporteManifiestosConcilidos.refrescarTblManifiestosSinConciliar();
            //fManifiestosConciliados.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FHabilitarManifiesto");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }     // TODO add your handling code here:     // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void mnuHabilitarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuHabilitarFacturasActionPerformed
        try {
            fHabilitarFacturas = new FHabilitarFacturas(ini);

            this.escritorio.add(fHabilitarFacturas);
            fHabilitarFacturas.setLocation(((ini.getDimension().width - fHabilitarFacturas.getSize().width) / 2), ((ini.getDimension().height - fHabilitarFacturas.getSize().height) / 2) - 30);
            fHabilitarFacturas.setVisible(true);
            fHabilitarFacturas.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FHabilitarFacturas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuHabilitarFacturasActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        try {
            fReporteManifiestosEnDistribucion = new FReporteManifiestosEnDistribucion(ini);

            this.escritorio.add(fReporteManifiestosEnDistribucion);
            fReporteManifiestosEnDistribucion.setLocation(((ini.getDimension().width - fReporteManifiestosEnDistribucion.getSize().width) / 2), ((ini.getDimension().height - fReporteManifiestosEnDistribucion.getSize().height) / 2) - 30);
            fReporteManifiestosEnDistribucion.setVisible(true);
            fReporteManifiestosEnDistribucion.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReporteManifiestosEnDistribucion");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void mnu_rptFacturacionPorCanalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionPorCanalActionPerformed
        try {
            fReporteFacturacionPorCanal = new FReporteFacturacionPorCanal(ini);

            this.escritorio.add(fReporteFacturacionPorCanal);
            fReporteFacturacionPorCanal.setLocation(((ini.getDimension().width - fReporteFacturacionPorCanal.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturacionPorCanal.getSize().height) / 2) - 30);
            fReporteFacturacionPorCanal.setVisible(true);
            fReporteFacturacionPorCanal.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReporteFacturacionPorCanal");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnu_rptFacturacionPorCanalActionPerformed

    private void mnu_rptFacturacionTATActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionTATActionPerformed
        try {
            fReporteFacturacionTAT = new FReporteFacturacionTAT(ini);

            this.escritorio.add(fReporteFacturacionTAT);
            fReporteFacturacionTAT.setLocation(((ini.getDimension().width - fReporteFacturacionTAT.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturacionTAT.getSize().height) / 2) - 30);
            fReporteFacturacionTAT.setVisible(true);
            fReporteFacturacionTAT.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReporteFacturacionTAT");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnu_rptFacturacionTATActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed

        try {
            freporteManifiestosSinDescargar = new FReporteManifiestosSinDescargar(ini);

            this.escritorio.add(freporteManifiestosSinDescargar);
            freporteManifiestosSinDescargar.setLocation(((ini.getDimension().width - freporteManifiestosSinDescargar.getSize().width) / 2), ((ini.getDimension().height - freporteManifiestosSinDescargar.getSize().height) / 2) - 30);
            freporteManifiestosSinDescargar.setVisible(true);
            freporteManifiestosSinDescargar.cargarInformacion();
            freporteManifiestosSinDescargar.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReporteManifiestosSinDescargar");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        try {
            FManifestarFacturasEnPicking fFacturasEnPicking = new FManifestarFacturasEnPicking(ini);

            this.escritorio.add(fFacturasEnPicking);
            fFacturasEnPicking.setLocation(((ini.getDimension().width - fFacturasEnPicking.getSize().width) / 2), ((ini.getDimension().height - fFacturasEnPicking.getSize().height) / 2) - 30);
            fFacturasEnPicking.setVisible(true);
            //fDesvincularFactura.cargarInformacion();
            fFacturasEnPicking.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FManifestarFacturasEnPicking");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        try {
            FRegistroDePedidosFinLineaDePicking fRegistroDePedidosFinLineaDePicking = new FRegistroDePedidosFinLineaDePicking(ini);

            this.escritorio.add(fRegistroDePedidosFinLineaDePicking);
            fRegistroDePedidosFinLineaDePicking.setLocation(((ini.getDimension().width - fRegistroDePedidosFinLineaDePicking.getSize().width) / 2), ((ini.getDimension().height - fRegistroDePedidosFinLineaDePicking.getSize().height) / 2) - 30);
            fRegistroDePedidosFinLineaDePicking.setVisible(true);
            //fDesvincularFactura.cargarInformacion();
            fRegistroDePedidosFinLineaDePicking.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FRegistroDePedidosFinLineaDePicking");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed
//
//        try {
//            // TODO add your handling code here:FConsultarFacturasPorCliente
//            if (fReporteFacturasPendientesEnPicking != null) {
//                this.escritorio.add(fReporteFacturasPendientesEnPicking);
//                fReporteFacturasPendientesEnPicking.setLocation(((ini.getDimension().width - fReporteFacturasPendientesEnPicking.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturasPendientesEnPicking.getSize().height) / 2) - 30);
//                fReporteFacturasPendientesEnPicking.setVisible(true);
//                fReporteFacturasPendientesEnPicking.show();
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void imActualizarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imActualizarClientesActionPerformed
        try {
            FGeoreferenciarClientes fGeoreferenciarClientes = new FGeoreferenciarClientes(ini);

            this.escritorio.add(fGeoreferenciarClientes);
            fGeoreferenciarClientes.setLocation(((ini.getDimension().width - fGeoreferenciarClientes.getSize().width) / 2), ((ini.getDimension().height - fGeoreferenciarClientes.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            fGeoreferenciarClientes.setVisible(true);
            fGeoreferenciarClientes.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FGeoreferenciarClientes");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_imActualizarClientesActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed

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
            datos.add(2, "Click en form  FModificarClaveOlvidada");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuCambioClaveActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed

        try {
            fIngresarEmpleados = new IngresarEmpleados(ini);
            System.out.print("se abre gui con timer " + new Date() + "\n");
            this.escritorio.add(fIngresarEmpleados);
            fIngresarEmpleados.setLocation(((ini.getDimension().width - fIngresarEmpleados.getSize().width) / 2), ((ini.getDimension().height - fIngresarEmpleados.getSize().height) / 2) - 30);
            fIngresarEmpleados.setVisible(true);
            //fReporteDescuentos_Recogidas.cargarInformacion();
            fIngresarEmpleados.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  IngresarEmpleados");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
                System.out.print("Se detiene el timer en abrir gui" + new Date() + "\n");
            }
            timer.restart();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
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
            datos.add(2, "Click en form  FConsultarManifiestos");
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
            datos.add(2, "Click en form  FConsultaManifiestosDescargados");
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
            datos.add(2, "Click en form  FConsultaManifiestosSinDescargar");
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

            this.escritorio.add(fReportePedidosEnDistribucion);
            fReportePedidosEnDistribucion.setLocation(((ini.getDimension().width - fReportePedidosEnDistribucion.getSize().width) / 2), ((ini.getDimension().height - fReportePedidosEnDistribucion.getSize().height) / 2) - 30);
            fReportePedidosEnDistribucion.setVisible(true);
            fReportePedidosEnDistribucion.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReportePedidosEnDistribucion");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void mnuCrearUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCrearUsuariosActionPerformed

        try {
            IngresarUsuarios ingresarUsuarios = new IngresarUsuarios(ini);

            this.escritorio.add(ingresarUsuarios);
            ingresarUsuarios.setLocation(((ini.getDimension().width - ingresarUsuarios.getSize().width) / 2), ((ini.getDimension().height - ingresarUsuarios.getSize().height) / 2) - 30);
            ingresarUsuarios.setVisible(true);
            ingresarUsuarios.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  IngresarUsuarios");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuCrearUsuariosActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed
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
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed
       
               
                try {
            FCrearLineasXMarcaDeVehiculo fCrearLineasXMarcaDeVehiculo = new FCrearLineasXMarcaDeVehiculo(ini);

            this.escritorio.add(fCrearLineasXMarcaDeVehiculo);
            fCrearLineasXMarcaDeVehiculo.setLocation(((ini.getDimension().width - fCrearLineasXMarcaDeVehiculo.getSize().width) / 2), ((ini.getDimension().height - fCrearLineasXMarcaDeVehiculo.getSize().height) / 2) - 30);
            fCrearLineasXMarcaDeVehiculo.setVisible(true);
            fCrearLineasXMarcaDeVehiculo.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearLineasXMarcaDeVehiculo");
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

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed

        try {
            FReporteDelPersonal fReporteDelPersonal = new FReporteDelPersonal(ini);

            this.escritorio.add(fReporteDelPersonal);
            fReporteDelPersonal.setLocation(((ini.getDimension().width - fReporteDelPersonal.getSize().width) / 2), ((ini.getDimension().height - fReporteDelPersonal.getSize().height) / 2) - 30);
            fReporteDelPersonal.setVisible(true);
            fReporteDelPersonal.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReporteDelPersonal");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void mItmEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmEmpleadosActionPerformed
        try {
            FCrearRutasDeDistribucion fCrearRutas = new FCrearRutasDeDistribucion(ini);

            this.escritorio.add(fCrearRutas);
            fCrearRutas.setLocation(((ini.getDimension().width - fCrearRutas.getSize().width) / 2), ((ini.getDimension().height - fCrearRutas.getSize().height) / 2) - 30);
            fCrearRutas.setVisible(true);
            fCrearRutas.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearRutas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_mItmEmpleadosActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
//
//        try {
//            FReporteClientesConDescuento fReporteClientesConDescuento = new FReporteClientesConDescuento(ini);
//            if (fReporteClientesConDescuento != null) {
//                this.escritorio.add(fReporteClientesConDescuento);
//                fReporteClientesConDescuento.setLocation(((ini.getDimension().width - fReporteClientesConDescuento.getSize().width) / 2), ((ini.getDimension().height - fReporteClientesConDescuento.getSize().height) / 2) - 30);
//                fReporteClientesConDescuento.setVisible(true);
//                fReporteClientesConDescuento.cargarInformacion();
//                fReporteClientesConDescuento.show();
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void mnuManifestarPedidos1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos1MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuManifestarPedidos1MouseClicked

    private void mnuManifestarPedidos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos1ActionPerformed

        try {
            FManifestarPedidosPoblaciones fManifestarPedidosPoblaciones = new FManifestarPedidosPoblaciones(ini);

            this.escritorio.add(fManifestarPedidosPoblaciones);
            fManifestarPedidosPoblaciones.setLocation(((ini.getDimension().width - fManifestarPedidosPoblaciones.getSize().width) / 2), ((ini.getDimension().height - fManifestarPedidosPoblaciones.getSize().height) / 2) - 30);
            fManifestarPedidosPoblaciones.setSize(ini.getDimension());
            fManifestarPedidosPoblaciones.setVisible(true);
            fManifestarPedidosPoblaciones.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FManifestarPedidosPoblaciones");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_mnuManifestarPedidos1ActionPerformed

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
        try {
            FCrearFacturasDeVenta fCrearFacturasDeVenta = new FCrearFacturasDeVenta(ini);

            this.escritorio.add(fCrearFacturasDeVenta);
            //fCrearFacturasDeVenta.setLocation(((ini.getDimension().width - fCrearFacturasDeVenta.getSize().width) / 2), ((ini.getDimension().height - fCrearFacturasDeVenta.getSize().height) / 2) - 30);
            fCrearFacturasDeVenta.setSize(ini.getDimension());
            fCrearFacturasDeVenta.setVisible(true);
            fCrearFacturasDeVenta.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FManifestarPedidosPoblaciones");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:
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

    private void miMantenimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMantenimientosActionPerformed

        try {
            FAgregarMantenimientoVehiculo fAgregarMantenimientoVehiculo = new FAgregarMantenimientoVehiculo(ini);

            this.escritorio.add(fAgregarMantenimientoVehiculo);
            fAgregarMantenimientoVehiculo.setLocation(((ini.getDimension().width - fAgregarMantenimientoVehiculo.getSize().width) / 2), ((ini.getDimension().height - fAgregarMantenimientoVehiculo.getSize().height) / 2) - 30);
            //fIntegrador.setSize(ini.getDimension());
            fAgregarMantenimientoVehiculo.setVisible(true);
            fAgregarMantenimientoVehiculo.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FAgregarMantenimientoVehiculo");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TOD// TOD

    }//GEN-LAST:event_miMantenimientosActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed

        try {
            FIngresarDocumentosVehiculos fIngresarDocumentosVehiculos = new FIngresarDocumentosVehiculos(ini);

            this.escritorio.add(fIngresarDocumentosVehiculos);
            fIngresarDocumentosVehiculos.setLocation(((ini.getDimension().width - fIngresarDocumentosVehiculos.getSize().width) / 2), ((ini.getDimension().height - fIngresarDocumentosVehiculos.getSize().height) / 2) - 30);
            //fIntegrador.setSize(ini.getDimension());
            fIngresarDocumentosVehiculos.setVisible(true);
            fIngresarDocumentosVehiculos.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FIngresarDocumentosVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TOD// TOD

    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItem37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem37ActionPerformed

    }//GEN-LAST:event_jMenuItem37ActionPerformed

    private void jMenuItem38ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem38ActionPerformed


    }//GEN-LAST:event_jMenuItem38ActionPerformed

    private void jMenuItem39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem39ActionPerformed
        try {
            FReporteDocumentosVehiculos fReporteDocumentosVehiculos = new FReporteDocumentosVehiculos(ini);

            this.escritorio.add(fReporteDocumentosVehiculos);
            fReporteDocumentosVehiculos.setLocation(((ini.getDimension().width - fReporteDocumentosVehiculos.getSize().width) / 2), ((ini.getDimension().height - fReporteDocumentosVehiculos.getSize().height) / 2) - 30);
            //fIntegrador.setSize(ini.getDimension());
            fReporteDocumentosVehiculos.setVisible(true);
            fReporteDocumentosVehiculos.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReporteDocumentosVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:// TODO add your handling code here:// TODO add your handling code here:

    }//GEN-LAST:event_jMenuItem39ActionPerformed

    private void jMenuItem40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem40ActionPerformed
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
    }//GEN-LAST:event_jMenuItem40ActionPerformed

    private void jMenuItem41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem41ActionPerformed
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
    }//GEN-LAST:event_jMenuItem41ActionPerformed

    private void jMenuItem42ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem42ActionPerformed

        try {
            FReporteGeneralDeDescargueDeRutas fReporteGeneralDeDescargueDeRutas = new FReporteGeneralDeDescargueDeRutas(ini);

            this.escritorio.add(fReporteGeneralDeDescargueDeRutas);
            fReporteGeneralDeDescargueDeRutas.setLocation(((ini.getDimension().width - fReporteGeneralDeDescargueDeRutas.getSize().width) / 2), ((ini.getDimension().height - fReporteGeneralDeDescargueDeRutas.getSize().height) / 2) - 30);
            //fIntegrador.setSize(ini.getDimension());
            fReporteGeneralDeDescargueDeRutas.setVisible(true);
            fReporteGeneralDeDescargueDeRutas.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReporteGeneralDeDescargueDeRutas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem42ActionPerformed

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
            datos.add(2, "Click en form  FCrearCargos");
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
            datos.add(2, "Click en form  FCrearMarcasVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem45ActionPerformed

    private void jMenuItem46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem46ActionPerformed
        try {
            FCrearLineasXMarcaDeVehiculo fCrearLineasXMarcaDeVehiculo = new FCrearLineasXMarcaDeVehiculo(ini);

            this.escritorio.add(fCrearLineasXMarcaDeVehiculo);
            fCrearLineasXMarcaDeVehiculo.setLocation(((ini.getDimension().width - fCrearLineasXMarcaDeVehiculo.getSize().width) / 2), ((ini.getDimension().height - fCrearLineasXMarcaDeVehiculo.getSize().height) / 2) - 30);
            fCrearLineasXMarcaDeVehiculo.setResizable(false);
            fCrearLineasXMarcaDeVehiculo.setMaximizable(false);
            fCrearLineasXMarcaDeVehiculo.setVisible(true);
            fCrearLineasXMarcaDeVehiculo.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearLineasXMarcaDeVehiculo");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem46ActionPerformed

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
            datos.add(2, "Click en form  FCrearTiposdeVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem47ActionPerformed

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
            datos.add(2, "Click en form  FCrearTiposCarrocerias");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jMenuItem48ActionPerformed

    private void jMenuItem49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem49ActionPerformed
        try {
            FCrearCentrosDeCosto fCrearCentrosDeCosto = new FCrearCentrosDeCosto(ini);

            this.escritorio.add(fCrearCentrosDeCosto);
            fCrearCentrosDeCosto.setLocation(((ini.getDimension().width - fCrearCentrosDeCosto.getSize().width) / 2), ((ini.getDimension().height - fCrearCentrosDeCosto.getSize().height) / 2) - 30);
            fCrearCentrosDeCosto.setResizable(false);
            fCrearCentrosDeCosto.setMaximizable(false);
            fCrearCentrosDeCosto.setVisible(true);
            fCrearCentrosDeCosto.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearCentrosDeCosto");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem49ActionPerformed

    private void jMenuItem50ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem50ActionPerformed
        try {
            FCrearEntidadesBancarias fCrearEntidadesBancarias = new FCrearEntidadesBancarias(ini);

            this.escritorio.add(fCrearEntidadesBancarias);
            fCrearEntidadesBancarias.setLocation(((ini.getDimension().width - fCrearEntidadesBancarias.getSize().width) / 2), ((ini.getDimension().height - fCrearEntidadesBancarias.getSize().height) / 2) - 30);
            fCrearEntidadesBancarias.setResizable(false);
            fCrearEntidadesBancarias.setMaximizable(false);
            fCrearEntidadesBancarias.setVisible(true);
            fCrearEntidadesBancarias.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearEntidadesBancarias");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem50ActionPerformed

    private void jMenuItem44ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem44ActionPerformed
        try {
            FCrearRutasDeDistribucion fCrearRutasDeDistribucion = new FCrearRutasDeDistribucion(ini);

            this.escritorio.add(fCrearRutasDeDistribucion);
            fCrearRutasDeDistribucion.setLocation(((ini.getDimension().width - fCrearRutasDeDistribucion.getSize().width) / 2), ((ini.getDimension().height - fCrearRutasDeDistribucion.getSize().height) / 2) - 30);
            fCrearRutasDeDistribucion.setResizable(false);
            fCrearRutasDeDistribucion.setMaximizable(false);
            fCrearRutasDeDistribucion.setVisible(true);
            fCrearRutasDeDistribucion.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearTiposCarrocerias");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem44ActionPerformed

    private void jMenuItem51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem51ActionPerformed
        try {
            FCrearCanalDeVentas fCrearCanalDeVentas = new FCrearCanalDeVentas(ini);
            this.escritorio.add(fCrearCanalDeVentas);
            fCrearCanalDeVentas.setLocation(((ini.getDimension().width - fCrearCanalDeVentas.getSize().width) / 2), ((ini.getDimension().height - fCrearCanalDeVentas.getSize().height) / 2) - 30);
            fCrearCanalDeVentas.setResizable(false);
            fCrearCanalDeVentas.setMaximizable(false);
            fCrearCanalDeVentas.setVisible(true);
            fCrearCanalDeVentas.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearCanalDeVentas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jMenuItem51ActionPerformed

    private void jMenuItem52ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem52ActionPerformed

        try {
            FCrearCausalesDEDevolucion fCrearCausalesDEDevolucion = new FCrearCausalesDEDevolucion(ini);
            this.escritorio.add(fCrearCausalesDEDevolucion);
            fCrearCausalesDEDevolucion.setLocation(((ini.getDimension().width - fCrearCausalesDEDevolucion.getSize().width) / 2), ((ini.getDimension().height - fCrearCausalesDEDevolucion.getSize().height) / 2) - 30);
            fCrearCausalesDEDevolucion.setResizable(false);
            fCrearCausalesDEDevolucion.setMaximizable(false);
            fCrearCausalesDEDevolucion.setVisible(true);
            fCrearCausalesDEDevolucion.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearCausalesDEDevolucion");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem52ActionPerformed

    private void jMenuItem53ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem53ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem53ActionPerformed

    private void jMenuItem54ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem54ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem54ActionPerformed

    private void jMenuItem55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem55ActionPerformed
        // TODO add your handling code here:  
        FCrearEstadosManifiestos fCrearEstadosManifiestos = new FCrearEstadosManifiestos(ini);
        try {

            this.escritorio.add(fCrearEstadosManifiestos);
            fCrearEstadosManifiestos.setLocation(((ini.getDimension().width - fCrearEstadosManifiestos.getSize().width) / 2), ((ini.getDimension().height - fCrearEstadosManifiestos.getSize().height) / 2) - 30);
            fCrearEstadosManifiestos.setResizable(false);
            fCrearEstadosManifiestos.setMaximizable(false);
            fCrearEstadosManifiestos.setVisible(true);
            fCrearEstadosManifiestos.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearEstadosManifiestos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem55ActionPerformed

    private void jMenuItem56ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem56ActionPerformed
        FCrearMarcasDeLLantas fCrearMarcasLLantas = new FCrearMarcasDeLLantas(ini);
        try {

            this.escritorio.add(fCrearMarcasLLantas);
            fCrearMarcasLLantas.setLocation(((ini.getDimension().width - fCrearMarcasLLantas.getSize().width) / 2), ((ini.getDimension().height - fCrearMarcasLLantas.getSize().height) / 2) - 30);
            fCrearMarcasLLantas.setResizable(false);
            fCrearMarcasLLantas.setMaximizable(false);
            fCrearMarcasLLantas.setVisible(true);
            fCrearMarcasLLantas.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearMarcasLLantas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }


        }//GEN-LAST:event_jMenuItem56ActionPerformed

    private void jMenuItem61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem61ActionPerformed

        try {
            FCrearEstadosVehiculos fCrearEstadosVehiculos = new FCrearEstadosVehiculos(ini);

            this.escritorio.add(fCrearEstadosVehiculos);
            fCrearEstadosVehiculos.setLocation(((ini.getDimension().width - fCrearEstadosVehiculos.getSize().width) / 2), ((ini.getDimension().height - fCrearEstadosVehiculos.getSize().height) / 2) - 30);
            fCrearEstadosVehiculos.setResizable(false);
            fCrearEstadosVehiculos.setMaximizable(false);
            fCrearEstadosVehiculos.setVisible(true);
            fCrearEstadosVehiculos.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearEstadosVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem61ActionPerformed

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
            datos.add(2, "Click en form  FCrearTiposdeContratosVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem62ActionPerformed

    private void jMenuItem63ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem63ActionPerformed
        try {
            FCrearTiposContratosPersonas fCrearTiposContratosPersonas = new FCrearTiposContratosPersonas(ini);

            this.escritorio.add(fCrearTiposContratosPersonas);
            fCrearTiposContratosPersonas.setLocation(((ini.getDimension().width - fCrearTiposContratosPersonas.getSize().width) / 2), ((ini.getDimension().height - fCrearTiposContratosPersonas.getSize().height) / 2) - 30);
            fCrearTiposContratosPersonas.setResizable(false);
            fCrearTiposContratosPersonas.setMaximizable(false);
            fCrearTiposContratosPersonas.setVisible(true);
            fCrearTiposContratosPersonas.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  fCrearTiposContratosPersonas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem63ActionPerformed

    private void jMenuItem64ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem64ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem64ActionPerformed

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
            datos.add(2, "Click en form  FCrearTiposdeDocumentos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem65ActionPerformed

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
            datos.add(2, "Click en form  FCrearTiposdeServicios");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem66ActionPerformed

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
            datos.add(2, "Click en form  FCrearTiposdeCombustible");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jMenuItem67ActionPerformed

    private void jMenuItem68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem68ActionPerformed
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
    }//GEN-LAST:event_jMenuItem68ActionPerformed

    private void jMenuItem72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem72ActionPerformed
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
    }//GEN-LAST:event_jMenuItem72ActionPerformed

    private void jMenuItem73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem73ActionPerformed
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
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem73ActionPerformed

    private void mnuManifestarPedidos6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuManifestarPedidos6MouseClicked

    private void mnuManifestarPedidos6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos6ActionPerformed
        try {
            if (fManifestarPedidosHielera == null) {
                fManifestarPedidosHielera = new FManifestarPedidosHielera_2(ini);
                this.escritorio.add(fManifestarPedidosHielera);
                fManifestarPedidosHielera.setSize((int) ini.getDimension().getWidth(), (int) ini.getDimension().getHeight() - 45);
                // fManifestarPedidosHielera.setLocation(((ini.getDimension().width - fManifestarPedidosHielera.getSize().width) / 2), ((ini.getDimension().height - fManifestarPedidosHielera.getSize().height) / 2));
                fManifestarPedidosHielera.setLocation(0, 0);
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
            } else {
                this.escritorio.add(fManifestarPedidosHielera);
                fManifestarPedidosHielera.setVisible(true);
                fManifestarPedidosHielera.show();
            }

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuManifestarPedidos6ActionPerformed

    private void mnuManifestarPedidos7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuManifestarPedidos7MouseClicked

    private void mnuManifestarPedidos7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos7ActionPerformed
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
        }// TODO add your handling code here:// TODO add your handling code here:    // TODO add your handling code here:
    }//GEN-LAST:event_mnuManifestarPedidos7ActionPerformed

    private void mnuDescargueHieleraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDescargueHieleraActionPerformed
        try {
            if (descargarFacturas_2 == null) {
                descargarFacturas_2 = new DescargarFacturas_2(this.ini);
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
            } else {
                this.escritorio.add(descargarFacturas_2);
                descargarFacturas_2.setVisible(true);
                descargarFacturas_2.show();
            }

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_mnuDescargueHieleraActionPerformed

    private void mnuItmDeshabilitarMftoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItmDeshabilitarMftoActionPerformed
        try {
            FDeshabilitarManifiestoAbierto fdeshabilitarManifiestoAbierto = new FDeshabilitarManifiestoAbierto(ini);
            this.escritorio.add(fdeshabilitarManifiestoAbierto);
            // fDescargarFActuras.setSize(escritorio.getSize());
            fdeshabilitarManifiestoAbierto.setLocation(((ini.getDimension().width - fdeshabilitarManifiestoAbierto.getSize().width) / 2), ((ini.getDimension().height - fdeshabilitarManifiestoAbierto.getSize().height) / 2));
            fdeshabilitarManifiestoAbierto.setVisible(true);
            fdeshabilitarManifiestoAbierto.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FDeshabilitar");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();
            System.out.println("se inicio el timer en el muenu" + new Date());

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuItmDeshabilitarMftoActionPerformed

    private void jMenuItem70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem70ActionPerformed

        // TODO add your handling code here:
        try {

            FGestionarIncidencias fGestionarIncidencias = new FGestionarIncidencias(ini);

            this.escritorio.add(fGestionarIncidencias);
            //JOptionPane.showMessageDialog(this,"width=" + screenSize.width +" heigh= "+ screenSize.height , "Cliente no activo", 1);
            fGestionarIncidencias.setSize(ini.getDimension().width, ini.getDimension().height);

            fGestionarIncidencias.setLocation(0, 0);

            //form1.setLocation(((screenSize.width - form1.getSize().width) / 2), ((screenSize.height - form1.getSize().height) / 2) - 30);
            fGestionarIncidencias.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FGestionarIncidencias");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem70ActionPerformed

    private void jMenuItem71ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem71ActionPerformed
        // TODO add your handling code here:
        try {

            FIncidenciasSvC fIncidenciasSvC = new FIncidenciasSvC(this);

            this.escritorio.add(fIncidenciasSvC);
            //JOptionPane.showMessageDialog(this,"width=" + screenSize.width +" heigh= "+ screenSize.height , "Cliente no activo", 1);
            fIncidenciasSvC.setSize(ini.getDimension().width, ini.getDimension().height);

            fIncidenciasSvC.setLocation(0, 0);

            //form1.setLocation(((screenSize.width - form1.getSize().width) / 2), ((screenSize.height - form1.getSize().height) / 2) - 30);
            fIncidenciasSvC.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FIncidenciasSvC");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem71ActionPerformed

    private void jMenuItem75MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem75MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem75MouseClicked

    private void jMenuItem75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem75ActionPerformed
        try {
            FIntegradorExcelFile FIntegradorExcelFile = new FIntegradorExcelFile(ini);

            this.escritorio.add(FIntegradorExcelFile);
            FIntegradorExcelFile.setLocation(((ini.getDimension().width - FIntegradorExcelFile.getSize().width) / 2), ((ini.getDimension().height - FIntegradorExcelFile.getSize().height) / 2) - 30);
            //fIntegrador.setSize(ini.getDimension());
            FIntegradorExcelFile.setVisible(true);
            FIntegradorExcelFile.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FIntegradorExcelFile");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TO
    }//GEN-LAST:event_jMenuItem75ActionPerformed

    private void jMenuItem76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem76ActionPerformed
        try {
            FormularioConfiguracionDelSistema formularioConfiguracionDelSistema = new FormularioConfiguracionDelSistema();
            formularioConfiguracionDelSistema.toFront();
            formularioConfiguracionDelSistema.setResizable(false);
            formularioConfiguracionDelSistema.setTitle("Formulario para configuración del Sistema");
            formularioConfiguracionDelSistema.setLocation((ini.getDimension().width - formularioConfiguracionDelSistema.getSize().width) / 2, (ini.getDimension().height - formularioConfiguracionDelSistema.getSize().height) / 2);
            formularioConfiguracionDelSistema.setVisible(true);
            formularioConfiguracionDelSistema.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FormularioConfiguracionDelSistema");
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
            FIngresarDescuentoClientes fIngresarDescuentoClientes = new FIngresarDescuentoClientes(ini);
            this.escritorio.add(fIngresarDescuentoClientes);
            fIngresarDescuentoClientes.setLocation(((ini.getDimension().width - fIngresarDescuentoClientes.getSize().width) / 2), ((ini.getDimension().height - fIngresarDescuentoClientes.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            fIngresarDescuentoClientes.setVisible(true);
            fIngresarDescuentoClientes.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FIngresarDescuentoClientes");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jMenuItem77ActionPerformed

    private void jMenuItem78ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem78ActionPerformed
        try {
            FIngresarRecogidasClientes fIngresarRecogidasClientes = new FIngresarRecogidasClientes(ini);
            this.escritorio.add(fIngresarRecogidasClientes);
            fIngresarRecogidasClientes.setLocation(((ini.getDimension().width - fIngresarRecogidasClientes.getSize().width) / 2), ((ini.getDimension().height - fIngresarRecogidasClientes.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            fIngresarRecogidasClientes.setVisible(true);
            fIngresarRecogidasClientes.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FIngresarRecogidasClientes");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem78ActionPerformed

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
            datos.add(2, "Click en form  FConsultarPedidosConductorHielera");
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
            datos.add(2, "Click en form  fConsultaPedidosDescargadosPorConductorHielera");
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

    private void jMenuItem69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem69ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem69ActionPerformed

    private void jMenuItem82ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem82ActionPerformed
        try {

            FAnularFacturaSinMovimiento fAnularFacturaSinMovimiento = new FAnularFacturaSinMovimiento(ini);
            this.escritorio.add(fAnularFacturaSinMovimiento);
            // fDescargarFActuras.setSize(escritorio.getSize());
            fAnularFacturaSinMovimiento.setLocation(((ini.getDimension().width - fAnularFacturaSinMovimiento.getSize().width) / 2), ((ini.getDimension().height - fAnularFacturaSinMovimiento.getSize().height) / 2));
            fAnularFacturaSinMovimiento.setVisible(true);
            fAnularFacturaSinMovimiento.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FAnularFacturaSinMovimiento");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem82ActionPerformed

    private void jMenuItem83ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem83ActionPerformed

        try {
            FDeshabilitarManifiestoAbierto fdeshabilitarManifiestoAbierto = new FDeshabilitarManifiestoAbierto(ini);
            this.escritorio.add(fdeshabilitarManifiestoAbierto);
            // fDescargarFActuras.setSize(escritorio.getSize());
            fdeshabilitarManifiestoAbierto.setLocation(((ini.getDimension().width - fdeshabilitarManifiestoAbierto.getSize().width) / 2), ((ini.getDimension().height - fdeshabilitarManifiestoAbierto.getSize().height) / 2));
            fdeshabilitarManifiestoAbierto.setVisible(true);
            fdeshabilitarManifiestoAbierto.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FDeshabilitarManifiestoAbierto");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem83ActionPerformed

    private void jMenuItem84ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem84ActionPerformed
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
            datos.add(2, "Click en form  FCrearMarcasVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem84ActionPerformed

    private void jMenuItem85ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem85ActionPerformed
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
            datos.add(2, "Click en form  FCrearTiposCarrocerias");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jMenuItem85ActionPerformed

    private void jMenuItem86ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem86ActionPerformed
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
            datos.add(2, "Click en form  FCrearTiposdeCombustible");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem86ActionPerformed

    private void jMenuItem87ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem87ActionPerformed
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
            datos.add(2, "Click en form  FCrearTiposdeContratosVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem87ActionPerformed

    private void jMenuItem88ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem88ActionPerformed
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
            datos.add(2, "Click en form  FCrearTiposdeServicios");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem88ActionPerformed

    private void jMenuItem89ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem89ActionPerformed

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
            datos.add(2, "Click en form  FCrearTiposdeVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem89ActionPerformed

    private void jMenuItem90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem90ActionPerformed
        FCrearMarcasDeLLantas fCrearMarcasLLantas = new FCrearMarcasDeLLantas(ini);
        try {

            this.escritorio.add(fCrearMarcasLLantas);
            fCrearMarcasLLantas.setLocation(((ini.getDimension().width - fCrearMarcasLLantas.getSize().width) / 2), ((ini.getDimension().height - fCrearMarcasLLantas.getSize().height) / 2) - 30);
            fCrearMarcasLLantas.setResizable(false);
            fCrearMarcasLLantas.setMaximizable(false);
            fCrearMarcasLLantas.setVisible(true);
            fCrearMarcasLLantas.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearMarcasLLantas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem90ActionPerformed

    private void jMenuItem91ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem91ActionPerformed

        try {
            FCrearEstadosVehiculos fCrearEstadosVehiculos = new FCrearEstadosVehiculos(ini);

            this.escritorio.add(fCrearEstadosVehiculos);
            fCrearEstadosVehiculos.setLocation(((ini.getDimension().width - fCrearEstadosVehiculos.getSize().width) / 2), ((ini.getDimension().height - fCrearEstadosVehiculos.getSize().height) / 2) - 30);
            fCrearEstadosVehiculos.setResizable(false);
            fCrearEstadosVehiculos.setMaximizable(false);
            fCrearEstadosVehiculos.setVisible(true);
            fCrearEstadosVehiculos.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearEstadosVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem91ActionPerformed

    private void jMenuItem92ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem92ActionPerformed
        FCrearTiposdeDocumentos fCrearTiposdeDocumentos = new FCrearTiposdeDocumentos(ini);
        try {

            this.escritorio.add(fCrearTiposdeDocumentos);
            fCrearTiposdeDocumentos.setLocation(((ini.getDimension().width - fCrearTiposdeDocumentos.getSize().width) / 2), ((ini.getDimension().height - fCrearTiposdeDocumentos.getSize().height) / 2) - 30);
            fCrearTiposdeDocumentos.setResizable(false);
            fCrearTiposdeDocumentos.setMaximizable(false);
            fCrearTiposdeDocumentos.setVisible(true);
            fCrearTiposdeDocumentos.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearTiposdeDocumentos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem92ActionPerformed

    private void jMenuItem94ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem94ActionPerformed
        try {
            FCrearLineasXMarcaDeVehiculo fCrearLineasXMarcaDeVehiculo = new FCrearLineasXMarcaDeVehiculo(ini);

            this.escritorio.add(fCrearLineasXMarcaDeVehiculo);
            fCrearLineasXMarcaDeVehiculo.setLocation(((ini.getDimension().width - fCrearLineasXMarcaDeVehiculo.getSize().width) / 2), ((ini.getDimension().height - fCrearLineasXMarcaDeVehiculo.getSize().height) / 2) - 30);
            fCrearLineasXMarcaDeVehiculo.setResizable(false);
            fCrearLineasXMarcaDeVehiculo.setMaximizable(false);
            fCrearLineasXMarcaDeVehiculo.setVisible(true);
            fCrearLineasXMarcaDeVehiculo.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearLineasXMarcaDeVehiculo");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem94ActionPerformed

    private void jMenuItem95ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem95ActionPerformed

        try {
            IngresarProveedores fIngresarProveedores = new IngresarProveedores(ini);

            this.escritorio.add(fIngresarProveedores);
            fIngresarProveedores.setLocation(((ini.getDimension().width - fIngresarProveedores.getSize().width) / 2), ((ini.getDimension().height - fIngresarProveedores.getSize().height) / 2) - 30);
            fIngresarProveedores.setResizable(false);
            fIngresarProveedores.setMaximizable(false);
            fIngresarProveedores.setVisible(true);
            fIngresarProveedores.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  IngresarProveedores");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem95ActionPerformed

    private void jMenuItem96ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem96ActionPerformed

        try {
            IngresarSucursalDeProveedor fIngresarSucursalDeProveedor = new IngresarSucursalDeProveedor(ini);

            this.escritorio.add(fIngresarSucursalDeProveedor);
            fIngresarSucursalDeProveedor.setLocation(((ini.getDimension().width - fIngresarSucursalDeProveedor.getSize().width) / 2), ((ini.getDimension().height - fIngresarSucursalDeProveedor.getSize().height) / 2) - 30);
            fIngresarSucursalDeProveedor.setResizable(false);
            fIngresarSucursalDeProveedor.setMaximizable(false);
            fIngresarSucursalDeProveedor.setVisible(true);
            fIngresarSucursalDeProveedor.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  fIngresarSucursalDeProveedor");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem96ActionPerformed

    private void jMenuItem97ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem97ActionPerformed

        try {
            FCrearCuentasPrincipales fCrearCuentasPrincipales = new FCrearCuentasPrincipales(ini);

            this.escritorio.add(fCrearCuentasPrincipales);
            fCrearCuentasPrincipales.setLocation(((ini.getDimension().width - fCrearCuentasPrincipales.getSize().width) / 2), ((ini.getDimension().height - fCrearCuentasPrincipales.getSize().height) / 2) - 30);
            fCrearCuentasPrincipales.setResizable(false);
            fCrearCuentasPrincipales.setMaximizable(false);
            fCrearCuentasPrincipales.setVisible(true);
            fCrearCuentasPrincipales.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  fCrearCuentasPrincipales");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem97ActionPerformed

    private void jMenuItem98ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem98ActionPerformed

        try {
            FCrearCuentasSecundarias fCrearCuentasSecundarias = new FCrearCuentasSecundarias(ini);

            this.escritorio.add(fCrearCuentasSecundarias);
            fCrearCuentasSecundarias.setLocation(((ini.getDimension().width - fCrearCuentasSecundarias.getSize().width) / 2), ((ini.getDimension().height - fCrearCuentasSecundarias.getSize().height) / 2) - 30);
            fCrearCuentasSecundarias.setResizable(false);
            fCrearCuentasSecundarias.setMaximizable(false);
            fCrearCuentasSecundarias.setVisible(true);
            fCrearCuentasSecundarias.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  fCrearCuentasPrincipales");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }   // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem98ActionPerformed

    private void jMenuItem99ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem99ActionPerformed
        try {
            IngresarCarros fIngresarCarros = new IngresarCarros(ini);

            this.escritorio.add(fIngresarCarros);
            fIngresarCarros.setLocation(((ini.getDimension().width - fIngresarCarros.getSize().width) / 2), ((ini.getDimension().height - fIngresarCarros.getSize().height) / 2) - 30);
            fIngresarCarros.setResizable(false);
            fIngresarCarros.setMaximizable(false);
            fIngresarCarros.setVisible(true);
            fIngresarCarros.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  IngresarCarros");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }   // TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem99ActionPerformed

    private void jMenuItem100ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem100ActionPerformed
        try {
            FReporteListadoCarrosPropios fReporteListadoCarrosPropios = new FReporteListadoCarrosPropios(this.ini);
            this.escritorio.add(fReporteListadoCarrosPropios);
            fReporteListadoCarrosPropios.setLocation(((ini.getDimension().width - fReporteListadoCarrosPropios.getSize().width) / 2), ((ini.getDimension().height - fReporteListadoCarrosPropios.getSize().height) / 2) - 30);

            fReporteListadoCarrosPropios.setVisible(true);
            fReporteListadoCarrosPropios.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReporteListadoCarrosPropios");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem100ActionPerformed

    private void jMenuItem101ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem101ActionPerformed
        try {
            FAdicionarPedidosNoReportados fAdicionarPedidosNoReportados = new FAdicionarPedidosNoReportados(this.ini);
            this.escritorio.add(fAdicionarPedidosNoReportados);
            fAdicionarPedidosNoReportados.setLocation(((ini.getDimension().width - fAdicionarPedidosNoReportados.getSize().width) / 2), ((ini.getDimension().height - fAdicionarPedidosNoReportados.getSize().height) / 2) - 30);

            fAdicionarPedidosNoReportados.setVisible(true);
            fAdicionarPedidosNoReportados.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReporteListadoCarrosPropios");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem101ActionPerformed

    private void jMenuBar1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuBar1MouseClicked

    }//GEN-LAST:event_jMenuBar1MouseClicked

    private void jMenu14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu14ActionPerformed

    }//GEN-LAST:event_jMenu14ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed

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

    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        /* Se inicia un integrador con TNS que va a actualizar todos los 
            empleados*/

        new Thread(new HiloIntegradorTNS(ini, jMenuItem15)).start();
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
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
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem102ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem102ActionPerformed
        try {
            fReporteFacturasGeneradas = new FReporteFacturasGeneradas(ini);
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
        }       // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem102ActionPerformed

    private void jMenuItem104ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem104ActionPerformed
//        MapViewOptions options = new MapViewOptions();
//        options.importPlaces();
//        options.setApiKey("AIzaSyAYy9jlVtsCMtCV783sA45quCVjySZyNR8");
//       

        puntosGps = new PuntosGps_rutas(ini);
        this.escritorio.add(puntosGps);
        // puntosGps.add(sample);
        puntosGps.setSize(escritorio.getSize());
        puntosGps.setLocation((ini.getDimension().width - puntosGps.getSize().width) / 2, (ini.getDimension().height - puntosGps.getSize().height) / 2);
        puntosGps.setLocation(new Point(0, 0));
        puntosGps.show();
        puntosGps.setVisible(true);
    }//GEN-LAST:event_jMenuItem104ActionPerformed

    private void jMenuItem105ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem105ActionPerformed

        PuntosMonitoreo monitoreo = new PuntosMonitoreo(ini);
        this.escritorio.add(monitoreo);
        // puntosGps.add(sample);
        monitoreo.setSize(escritorio.getSize());
        monitoreo.setLocation((ini.getDimension().width - monitoreo.getSize().width) / 2, (ini.getDimension().height - monitoreo.getSize().height) / 2);
        monitoreo.setLocation(new Point(0, 0));
        monitoreo.show();
        monitoreo.setVisible(true);
    }//GEN-LAST:event_jMenuItem105ActionPerformed

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

    private void jMenuItem74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem74ActionPerformed
       try {
            FImportarConsumoCombustible fConsumoCombustible = new FImportarConsumoCombustible(ini);
            // TODO add your handling code here:FConsultarFacturasPorCliente

            this.escritorio.add(fConsumoCombustible);
            fConsumoCombustible.setLocation(((ini.getDimension().width - fConsumoCombustible.getSize().width) / 2), ((ini.getDimension().height - fConsumoCombustible.getSize().height) / 2) - 30);
            fConsumoCombustible.setVisible(true);
            fConsumoCombustible.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FConsumoCombustible");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem74ActionPerformed

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
            java.util.logging.Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>


        /* Create and display the fCambiarClave */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new PrincipalSuperUsuario().setVisible(true);

                } catch (Exception ex) {
                    Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
//      private void llenarVariables() {
//        new Thread(new HiloListadoDeCausalesdeDevolucion(ini, 5)).start();
//        new Thread(new HiloListadoDeMovimientosManifiestosfacturas(ini, 5)).start();
//        new Thread(new HiloListadoDeDepartamentos(ini, 5)).start();
//        new Thread(new HiloListadoDeCiudades(ini, 5)).start();
//        new Thread(new HiloListadoDeEstadosCiviles(ini, 5)).start();
//        new Thread(new HiloListadoDeTiposDeSangre(ini, 5)).start();
//        new Thread(new HiloListadoDeAgencias(ini, 5)).start();
//        new Thread(new HiloListadoDeRegionales(ini, 5)).start();
//        new Thread(new HiloListadoDeZonas(ini, 5)).start();
//        new Thread(new HiloListadoDeCentrosDeCosto(ini, 5)).start();
//        new Thread(new HiloListadoDeContratosPersonas(ini, 5)).start();
//        new Thread(new HiloListadoDeCargos(ini, 5)).start();
//        new Thread(new HiloListadoDeEntidadesBancarias(ini, 5)).start();
//        //new Thread(new HiloManifiestosDeDistribucion(ini,5, this)).start();
//
//    }

    @Override
    public Image getIconImage() {
        String ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "imagenes/iconoApp/" + ini.getPropiedades().getProperty("iconoApp");
        Image retValue = Toolkit.getDefaultToolkit().getImage(ruta);
        //getImage(ClassLoader.getSystemResource("app/configuracion/imagenes/turbo_64x64.png"));
        return retValue;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JDesktopPane escritorio;
    private javax.swing.JMenuItem imActualizarClientes;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu16;
    private javax.swing.JMenu jMenu17;
    private javax.swing.JMenu jMenu18;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu20;
    private javax.swing.JMenu jMenu25;
    private javax.swing.JMenu jMenu26;
    private javax.swing.JMenu jMenu27;
    private javax.swing.JMenu jMenu28;
    private javax.swing.JMenu jMenu29;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu30;
    private javax.swing.JMenu jMenu31;
    private javax.swing.JMenu jMenu32;
    private javax.swing.JMenu jMenu33;
    private javax.swing.JMenu jMenu34;
    private javax.swing.JMenu jMenu35;
    private javax.swing.JMenu jMenu36;
    private javax.swing.JMenu jMenu37;
    private javax.swing.JMenu jMenu38;
    private javax.swing.JMenu jMenu39;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    public static javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem100;
    private javax.swing.JMenuItem jMenuItem101;
    private javax.swing.JMenuItem jMenuItem102;
    private javax.swing.JMenuItem jMenuItem103;
    private javax.swing.JMenuItem jMenuItem104;
    private javax.swing.JMenuItem jMenuItem105;
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
    private javax.swing.JMenuItem jMenuItem41;
    private javax.swing.JMenuItem jMenuItem42;
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
    private javax.swing.JMenuItem jMenuItem57;
    private javax.swing.JMenuItem jMenuItem58;
    private javax.swing.JMenuItem jMenuItem59;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem60;
    private javax.swing.JMenuItem jMenuItem61;
    private javax.swing.JMenuItem jMenuItem62;
    private javax.swing.JMenuItem jMenuItem63;
    private javax.swing.JMenuItem jMenuItem64;
    private javax.swing.JMenuItem jMenuItem65;
    private javax.swing.JMenuItem jMenuItem66;
    private javax.swing.JMenuItem jMenuItem67;
    private javax.swing.JMenuItem jMenuItem68;
    private javax.swing.JMenuItem jMenuItem69;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem70;
    private javax.swing.JMenuItem jMenuItem71;
    private javax.swing.JMenuItem jMenuItem72;
    private javax.swing.JMenuItem jMenuItem73;
    private javax.swing.JMenuItem jMenuItem74;
    private javax.swing.JMenuItem jMenuItem75;
    private javax.swing.JMenuItem jMenuItem76;
    private javax.swing.JMenuItem jMenuItem77;
    private javax.swing.JMenuItem jMenuItem78;
    private javax.swing.JMenuItem jMenuItem79;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem80;
    private javax.swing.JMenuItem jMenuItem81;
    private javax.swing.JMenuItem jMenuItem82;
    private javax.swing.JMenuItem jMenuItem83;
    private javax.swing.JMenuItem jMenuItem84;
    private javax.swing.JMenuItem jMenuItem85;
    private javax.swing.JMenuItem jMenuItem86;
    private javax.swing.JMenuItem jMenuItem87;
    private javax.swing.JMenuItem jMenuItem88;
    private javax.swing.JMenuItem jMenuItem89;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItem90;
    private javax.swing.JMenuItem jMenuItem91;
    private javax.swing.JMenuItem jMenuItem92;
    private javax.swing.JMenuItem jMenuItem93;
    private javax.swing.JMenuItem jMenuItem94;
    private javax.swing.JMenuItem jMenuItem95;
    private javax.swing.JMenuItem jMenuItem96;
    private javax.swing.JMenuItem jMenuItem97;
    private javax.swing.JMenuItem jMenuItem98;
    private javax.swing.JMenuItem jMenuItem99;
    private javax.swing.JMenuItem mItmEmpleados;
    private javax.swing.JMenuItem menuCambiarClave;
    private javax.swing.JMenuItem miMantenimientos;
    private javax.swing.JMenu mnuArchivo;
    private javax.swing.JMenuItem mnuCambioClave;
    private javax.swing.JMenu mnuClientes;
    private javax.swing.JMenuItem mnuConsultarManifiestos;
    private javax.swing.JMenuItem mnuCrearUsuarios;
    private javax.swing.JMenuItem mnuDescargarFacturas;
    private javax.swing.JMenuItem mnuDescargueHielera;
    private javax.swing.JMenu mnuDistribucionLaHielera;
    private javax.swing.JMenu mnuDistribucionLogistica;
    private javax.swing.JMenuItem mnuHabilitarFacturas;
    private javax.swing.JMenuItem mnuHabilitarManifiesto;
    private javax.swing.JMenuItem mnuItmDeshabilitarMfto;
    private javax.swing.JMenuItem mnuManifestarPedidos;
    private javax.swing.JMenuItem mnuManifestarPedidos1;
    private javax.swing.JMenuItem mnuManifestarPedidos6;
    private javax.swing.JMenuItem mnuManifestarPedidos7;
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

    private void iniciarVariables() {
//        new Thread(new HiloListadoDestinosFacturas(this.ini)).start();
//        new Thread(new HiloListadoDeTiposDeDocumentos(this.ini)).start();
//
//        /* Costumer Service */
//        new Thread(new HiloListadoDeTiposDePeticones(this.ini)).start();
//        new Thread(new HiloListadoDeTiposDeGestiones(this.ini)).start();
//        new Thread(new HiloListadoDeMovimientosManifiestosfacturas(this.ini)).start();
//        new Thread(new HiloListadoDeCausalesdeDevolucion(this.ini)).start();
//       // new Thread(new HiloListadoDeEmpleados(this.ini)).start();
//
//        //new Thread(new HiloListadoDeDepartamentos(this.ini)).start();
//       // new Thread(new HiloListadoDeAgencias(this.ini)).start();
//
//        /* Vehiculos */
//        new Thread(new HiloListadoDeTiposDeDocumentos(this.ini)).start();

    }

    private void timerEvent(java.awt.event.ActionEvent evt) {
        if (timer.isRunning()) {
            timer.stop();
            System.out.println("se detiene el timer " + new Date());
            // System.exit(0);
//            timer = new Timer(60000, actionListener);
//            System.out.println("se llama al timer");
//            timer.start();
//            System.out.println("inicia el timer");
//            System.out.println("expired en funcion");
        }
    }

}
