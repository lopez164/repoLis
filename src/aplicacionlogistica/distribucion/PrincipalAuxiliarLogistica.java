/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion;

import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import aplicacionlogistica.distribucion.formularios.FHabilitarFacturas;
import aplicacionlogistica.distribucion.formularios.FAnularFacturas;
import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.costumerService.PrincipalUsuarioCostumerService;
import aplicacionlogistica.distribucion.clientes.FGeoreferenciarClientes;
import aplicacionlogistica.configuracion.CambiarClave;
import aplicacionlogistica.configuracion.HiloBitacoraMovimientosEnElSistema;
import aplicacionlogistica.distribucion.Threads.HiloCargarFormulariosAuxiliarLogistica;
import aplicacionlogistica.distribucion.consultas.FConsultarManifiestos;
import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcel;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FConsultaManifiestosDescargados;
import aplicacionlogistica.costumerService.Reportes.FReporteClientesConDescuento;
import aplicacionlogistica.costumerService.Reportes.FReporteDescuentos_Recogidas;
import aplicacionlogistica.distribucion.consultas.FConsultaManifiestosSinDescargar;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasPorCliente;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.consultas.FHabilitarManifiesto;
import aplicacionlogistica.distribucion.formularios.Hielera.DescargarFacturas_2;
import aplicacionlogistica.distribucion.formularios.Hielera.FManifestarPedidosHielera_2;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultaPedidosDescargadosPorConductorHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarPedidosConductorHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarProducto;
import aplicacionlogistica.distribucion.formularios.ReporteManifiestosSinDescargar.FReporteManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteDeRechazosTotales;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteFacturasPendientes;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteFacturasSinMovimientos;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteManifiestosConcilidos;
import aplicacionlogistica.distribucion.formularios.reportes.FReportePedidosMovilizadosPorPeriodo;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteManifiestosEnDistribucion.FReporteManifiestosEnDistribucion;
import aplicacionlogistica.distribucion.formularios.reportes.facturacion.FReporteFacturacionPorCanal;
import aplicacionlogistica.distribucion.integrador.FIntegrador;
import aplicacionlogistica.picking.FManifestarFacturasEnPicking;
import aplicacionlogistica.picking.FRegistroDePedidosFinLineaDePicking;
import aplicacionlogistica.picking.FReporteFacturasPendientesEnPicking;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author VLI_488
 */
public class PrincipalAuxiliarLogistica extends javax.swing.JFrame {

    public Inicio ini = null;

    public CambiarClave fCambiarClave;
    public DescargarFacturas fDescargarFActuras;
    //public FManifestarFacturasEnPicking fManifestarPedidos;

//    public IngresarEmpleados fIngresarEmpleados;
//    public IngresarCarros fIngresaarCArros;
//    public FHabilitarFacturas fHabilitarFacturas;
//    public FAnularFacturas fAnularFacturas;
//
//    public FConsultarFacturasRemoto fConsultarFacturasRemoto;
//    public FConsultarFacturasPorCliente fConsultarFacturasPorCliente;
//    public FConsultarManifiestos fConsultarManifiestos;
//
//    public FTrasladoDeFacturas fTrasladoDePedidos;
//    public FHabilitarManifiesto fHabilitarManifiesto;
//    public FImportarArchivoExcel fImportarExcel;
//
//    public FReporteManifiestosConcilidos fManifiestosConciliados=null;
//    public FReporteDeRechazosTotales fReporteRechazosTotales=null;
//    public FReportePedidosMovilizadosPorPeriodo fReporteMovilizado=null;
//    public FReporteManifiestosEnDistribucion fReporteManifiestosEnDistribucion=null;
//    public FReporteFacturasSinMovimientos fReporteSinMovimientos=null;
//    public FReporteFacturacionPorCanal fReporteFacturacionPorCanal=null;
//    public FReporteFacturacionTAT fReporteFacturacionTAT=null;
//    public FReporteManifiestosSinDescargar freporteManifiestosSinDescargar=null;
//    public FBorrarFacturaManifiesto fDesvincularFactura=null;
//    public FReporteFacturasPendientesEnPicking fReporteFacturasPendientesEnPicking=null;
//    public FReporteDescuentos_Recogidas fReporteDescuentos_Recogidas=null;
//    
//     public FManifestarFacturasEnPicking fFacturasEnPicking=null;
//  // public FManifestarPedidosEnRuta fManifestarPedidosEnRuta=null;
//     public FRegistroDePedidosFinLineaDePicking fRegistroDePedidosFinLineaDePicking=null;
//     public FGeoreferenciarClientes fGeoreferenciarClientes=null;
//     public FAdicionarPedidosNoReportados fAdicionarPedidosNoReportados=null;
//     
    List<String> datos = new ArrayList<>();
    String usuario;
    String formulario = "PrincipalAuxiliarLogistica";
    ActionListener actionListener;
    public Timer timer;

    public PrincipalAuxiliarLogistica() throws Exception {
        String rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
        ini = new Inicio(new File(rutaDeArchivo + "ReadUs.ini"));
        CUsuarios user = new CUsuarios(ini, "79481521");
        ini.setUser(user);
        usuario = Inicio.deCifrar(ini.getUser().getNombreUsuario());

        initComponents();

        this.setSize((int) ini.getDimension().getWidth(), (int) ini.getDimension().getHeight() - 43);
        this.setLocation(0, 0);

        /*SE instsancian los formularios del menu */
        new Thread(new HiloCargarFormulariosAuxiliarLogistica(this)).start();
    }

    public PrincipalAuxiliarLogistica(Inicio ini) throws Exception, Throwable {
        //initComponents();
        this.ini = ini;
        usuario = Inicio.deCifrar(ini.getUser().getNombreUsuario());
        if (ini.isEstaClienteActivo()) {
            //if (ya) {
            initComponents();
             int  tiempoDeEperaBloqueo = 60000 * (Integer.parseInt(ini.getPropiedades().getProperty("tiempoDeEperaBloqueo")));
            actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    timerEvent(actionEvent);
                    //System.out.println("expired");

                }

            };
            timer = new Timer(tiempoDeEperaBloqueo, actionListener);
            timer.start();

            Properties propiedades = ini.getPropiedades();

            try {
                ini.cargarImagenEscritorio(escritorio);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Revisar archivo de configuracion", "Imagen de Fonfo no encontrada", JOptionPane.ERROR_MESSAGE);
            }

            fCambiarClave = new CambiarClave(ini);

            /*SE instsancian los formularios del menu */
            new Thread(new HiloCargarFormulariosAuxiliarLogistica(this)).start();

            this.setSize(this.ini.getDimension());
            this.setLocation(0, 0);
            this.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos());

            /*MÉTODO PARA CAMBIAR LA CLAVE, CUANDO SE INGRSA POR PRIMERA VEZ AL 
            SISTEMA*/
            if (this.ini.getUser().getClaveUsuario().equals(Inicio.cifrar("123456"))) { // cuando el usuario es nuevo
                //this.escritorio.setSize(600, 400);
                CambiarClave fCambiarClave = new CambiarClave(ini);
                if (fCambiarClave != null) {
                    PrincipalAuxiliarLogistica.escritorio.add(fCambiarClave);
                    fCambiarClave.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));

                    fCambiarClave.setLocation((ini.getDimension().width - fCambiarClave.getSize().width) / 2, (ini.getDimension().height - fCambiarClave.getSize().height) / 2);
                    barraDeMenus.setVisible(false);
                    fCambiarClave.setResizable(false);
                    fCambiarClave.show();

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

                    fCambiarClave.setVisible(true);
                    fCambiarClave.txtClaveAnterior.requestFocus();
                }

            } else {
                if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                    mnuDistribucionLogistica.setVisible(true);
                    mnuDistribucionLaHielera.setVisible(false);
                }
                if (ini.getPropiedades().getProperty("idOperador").equals("2")) {
                    mnuDistribucionLogistica.setVisible(false);
                    mnuDistribucionLaHielera.setVisible(true);
                }
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

        fCrearMarcasDeVehiculos1 = new aplicacionlogistica.distribucion.formularios.administracion.FCrearMarcasDexxxxxxxxxxVehiculos();
        escritorio = new javax.swing.JDesktopPane();
        barraDeMenus = new javax.swing.JMenuBar();
        mnuArchivo = new javax.swing.JMenu();
        mItmCambiarClave = new javax.swing.JMenuItem();
        mItmSalir = new javax.swing.JMenuItem();
        mnuLogistica = new javax.swing.JMenu();
        mnuImportarExcel = new javax.swing.JMenu();
        mItmExportarExcel = new javax.swing.JMenuItem();
        mnuPicking = new javax.swing.JMenu();
        mItmMovilizrFacturas = new javax.swing.JMenuItem();
        mItmFacturasFinaalPicking = new javax.swing.JMenuItem();
        mItmReportePendientes = new javax.swing.JMenuItem();
        mnuDistribucionLogistica = new javax.swing.JMenu();
        mItmManifestarPedidos = new javax.swing.JMenuItem();
        mItmDescargarFacturas = new javax.swing.JMenuItem();
        mItmAnularFacturas = new javax.swing.JMenuItem();
        mItmConciliarFacturas = new javax.swing.JMenuItem();
        mnuDistribucionLaHielera = new javax.swing.JMenu();
        jMenuItem69 = new javax.swing.JMenuItem();
        mnuManifestarPedidos6 = new javax.swing.JMenuItem();
        jMenuItem74 = new javax.swing.JMenuItem();
        jMenuItem72 = new javax.swing.JMenuItem();
        jMenuItem73 = new javax.swing.JMenuItem();
        mnuHabilitarManifiesto = new javax.swing.JMenuItem();
        mnuHabilitarFacturas = new javax.swing.JMenuItem();
        mnuClientes = new javax.swing.JMenu();
        mItmActualizarClientes = new javax.swing.JMenuItem();
        mnuConsultas = new javax.swing.JMenu();
        mnuConsultar = new javax.swing.JMenu();
        mItmConsultarCliente = new javax.swing.JMenuItem();
        mItmConsultarFactura = new javax.swing.JMenuItem();
        mnuManifiestos = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenu36 = new javax.swing.JMenu();
        jMenuItem80 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        mnuReportes = new javax.swing.JMenu();
        mnuRechazos = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        mItmRptPorPeriodo = new javax.swing.JMenuItem();
        mItmRptFacturasSinMovimieto = new javax.swing.JMenuItem();
        mItmRptManifiestosEnDistribucion = new javax.swing.JMenuItem();
        mnuRptFacturacion = new javax.swing.JMenu();
        mnu_rptFacturacionPorCanal = new javax.swing.JMenuItem();
        mnu_rptFacturacionTAT = new javax.swing.JMenuItem();
        mItmRptMamifiestosSinDescargar = new javax.swing.JMenuItem();
        mItmRptDescuentosyRecogidas = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenu13 = new javax.swing.JMenu();
        jMenuItem32 = new javax.swing.JMenuItem();
        jMenuItem33 = new javax.swing.JMenuItem();
        jMenuItem34 = new javax.swing.JMenuItem();

        fCrearMarcasDeVehiculos1.setVisible(true);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());
        setIconImages(getIconImages());

        escritorio.setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);

        barraDeMenus.setEnabled(false);

        mnuArchivo.setText("Archivo");
        mnuArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuArchivoActionPerformed(evt);
            }
        });

        mItmCambiarClave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Key.png"))); // NOI18N
        mItmCambiarClave.setText("Cambio de Clave");
        mItmCambiarClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmCambiarClaveActionPerformed(evt);
            }
        });
        mnuArchivo.add(mItmCambiarClave);

        mItmSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        mItmSalir.setText("Salir");
        mItmSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmSalirActionPerformed(evt);
            }
        });
        mnuArchivo.add(mItmSalir);

        barraDeMenus.add(mnuArchivo);

        mnuLogistica.setText("Logistica");
        mnuLogistica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLogisticaActionPerformed(evt);
            }
        });

        mnuImportarExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        mnuImportarExcel.setText("Importar Excel");

        mItmExportarExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        mItmExportarExcel.setText("Importar Excel facturas");
        mItmExportarExcel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mItmExportarExcelMouseClicked(evt);
            }
        });
        mItmExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmExportarExcelActionPerformed(evt);
            }
        });
        mnuImportarExcel.add(mItmExportarExcel);

        mnuLogistica.add(mnuImportarExcel);

        mnuPicking.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Stopwatch.png"))); // NOI18N
        mnuPicking.setText("Proceso Picking");

        mItmMovilizrFacturas.setText("Movilizar Facturas");
        mItmMovilizrFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmMovilizrFacturasActionPerformed(evt);
            }
        });
        mnuPicking.add(mItmMovilizrFacturas);

        mItmFacturasFinaalPicking.setText("Facturas Final de Picking");
        mItmFacturasFinaalPicking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmFacturasFinaalPickingActionPerformed(evt);
            }
        });
        mnuPicking.add(mItmFacturasFinaalPicking);

        mItmReportePendientes.setText("Reporte de Pendientes");
        mItmReportePendientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmReportePendientesActionPerformed(evt);
            }
        });
        mnuPicking.add(mItmReportePendientes);

        mnuLogistica.add(mnuPicking);

        mnuDistribucionLogistica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        mnuDistribucionLogistica.setText("distribucion");

        mItmManifestarPedidos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        mItmManifestarPedidos.setText("Ingreso de Facturas");
        mItmManifestarPedidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mItmManifestarPedidosMouseClicked(evt);
            }
        });
        mItmManifestarPedidos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmManifestarPedidosActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(mItmManifestarPedidos);

        mItmDescargarFacturas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        mItmDescargarFacturas.setText("Descargar Facturas");
        mItmDescargarFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmDescargarFacturasActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(mItmDescargarFacturas);

        mItmAnularFacturas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Erase.png"))); // NOI18N
        mItmAnularFacturas.setText("Anular Facturas");
        mItmAnularFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmAnularFacturasActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(mItmAnularFacturas);

        mItmConciliarFacturas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Good mark.png"))); // NOI18N
        mItmConciliarFacturas.setText("Conciliar rutas");
        mItmConciliarFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmConciliarFacturasActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(mItmConciliarFacturas);

        mnuLogistica.add(mnuDistribucionLogistica);

        mnuDistribucionLaHielera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        mnuDistribucionLaHielera.setText("distribucion Hielera");

        jMenuItem69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/logo_hielerablue.png"))); // NOI18N
        jMenuItem69.setText("Integrador Hielera");
        jMenuItem69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem69ActionPerformed(evt);
            }
        });
        mnuDistribucionLaHielera.add(jMenuItem69);

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

        jMenuItem74.setText("Descargue Hielera");
        jMenuItem74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem74ActionPerformed(evt);
            }
        });
        mnuDistribucionLaHielera.add(jMenuItem74);

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

        mnuLogistica.add(mnuDistribucionLaHielera);

        mnuHabilitarManifiesto.setText("Habilitar Manifiesto");
        mnuHabilitarManifiesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuHabilitarManifiestoActionPerformed(evt);
            }
        });
        mnuLogistica.add(mnuHabilitarManifiesto);

        mnuHabilitarFacturas.setText("Habilitar Facturas");
        mnuHabilitarFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuHabilitarFacturasActionPerformed(evt);
            }
        });
        mnuLogistica.add(mnuHabilitarFacturas);

        barraDeMenus.add(mnuLogistica);

        mnuClientes.setText("Clientes");

        mItmActualizarClientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/map.png"))); // NOI18N
        mItmActualizarClientes.setText("actualizarClientes");
        mItmActualizarClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmActualizarClientesActionPerformed(evt);
            }
        });
        mnuClientes.add(mItmActualizarClientes);

        barraDeMenus.add(mnuClientes);

        mnuConsultas.setText("Consultas");

        mnuConsultar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        mnuConsultar.setText("Consultar");

        mItmConsultarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        mItmConsultarCliente.setText("Facturas Por Cliente");
        mItmConsultarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmConsultarClienteActionPerformed(evt);
            }
        });
        mnuConsultar.add(mItmConsultarCliente);

        mnuConsultas.add(mnuConsultar);

        mItmConsultarFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        mItmConsultarFactura.setText("Consultar Factura");
        mItmConsultarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmConsultarFacturaActionPerformed(evt);
            }
        });
        mnuConsultas.add(mItmConsultarFactura);

        mnuManifiestos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        mnuManifiestos.setText("Manifiestos");

        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem11.setText("De Distribucion");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        mnuManifiestos.add(jMenuItem11);

        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem12.setText("Descargados");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        mnuManifiestos.add(jMenuItem12);

        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem13.setText("Sin Descargar");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        mnuManifiestos.add(jMenuItem13);

        mnuConsultas.add(mnuManifiestos);

        jMenu36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        jMenu36.setText("Manifiestos Hielera");

        jMenuItem80.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem80.setText("Facturas Descargadas");
        jMenuItem80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem80ActionPerformed(evt);
            }
        });
        jMenu36.add(jMenuItem80);

        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem15.setText("De Distribucion");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        jMenu36.add(jMenuItem15);

        mnuConsultas.add(jMenu36);

        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Search.png"))); // NOI18N
        jMenuItem14.setText("Producto");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        mnuConsultas.add(jMenuItem14);

        barraDeMenus.add(mnuConsultas);

        mnuReportes.setText("Reportes");

        mnuRechazos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mnuRechazos.setText("Reporte Rechazos");

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem2.setText("Rechazos Totales");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        mnuRechazos.add(jMenuItem2);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem3.setText("RechazosParciales");
        mnuRechazos.add(jMenuItem3);

        mnuReportes.add(mnuRechazos);

        mItmRptPorPeriodo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mItmRptPorPeriodo.setText("Reporte por periodo");
        mItmRptPorPeriodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmRptPorPeriodoActionPerformed(evt);
            }
        });
        mnuReportes.add(mItmRptPorPeriodo);

        mItmRptFacturasSinMovimieto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mItmRptFacturasSinMovimieto.setText("Facturas sin Movimiento");
        mItmRptFacturasSinMovimieto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmRptFacturasSinMovimietoActionPerformed(evt);
            }
        });
        mnuReportes.add(mItmRptFacturasSinMovimieto);

        mItmRptManifiestosEnDistribucion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mItmRptManifiestosEnDistribucion.setText("Reporte Manifiestos En Distribucion");
        mItmRptManifiestosEnDistribucion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmRptManifiestosEnDistribucionActionPerformed(evt);
            }
        });
        mnuReportes.add(mItmRptManifiestosEnDistribucion);

        mnuRptFacturacion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mnuRptFacturacion.setText("Reportes Facturacion");

        mnu_rptFacturacionPorCanal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mnu_rptFacturacionPorCanal.setText("Reporte Canales");
        mnu_rptFacturacionPorCanal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_rptFacturacionPorCanalActionPerformed(evt);
            }
        });
        mnuRptFacturacion.add(mnu_rptFacturacionPorCanal);

        mnu_rptFacturacionTAT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mnu_rptFacturacionTAT.setText("Reporte TAT");
        mnu_rptFacturacionTAT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_rptFacturacionTATActionPerformed(evt);
            }
        });
        mnuRptFacturacion.add(mnu_rptFacturacionTAT);

        mnuReportes.add(mnuRptFacturacion);

        mItmRptMamifiestosSinDescargar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mItmRptMamifiestosSinDescargar.setText("Manifiestos sin Descargar");
        mItmRptMamifiestosSinDescargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmRptMamifiestosSinDescargarActionPerformed(evt);
            }
        });
        mnuReportes.add(mItmRptMamifiestosSinDescargar);

        mItmRptDescuentosyRecogidas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mItmRptDescuentosyRecogidas.setText("Desctos & Recogidas");
        mItmRptDescuentosyRecogidas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmRptDescuentosyRecogidasActionPerformed(evt);
            }
        });
        mnuReportes.add(mItmRptDescuentosyRecogidas);

        jMenuItem30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Accounting.png"))); // NOI18N
        jMenuItem30.setText("Descuentos Autorizados");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        mnuReportes.add(jMenuItem30);

        barraDeMenus.add(mnuReportes);

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

        barraDeMenus.add(jMenu13);

        setJMenuBar(barraDeMenus);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 514, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuArchivoActionPerformed
    }//GEN-LAST:event_mnuArchivoActionPerformed

    private void mItmCambiarClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmCambiarClaveActionPerformed
        try {
            CambiarClave fCambiarClave = new CambiarClave(ini);

            if (fCambiarClave != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fCambiarClave);
                fCambiarClave.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
                fCambiarClave.setLocation((ini.getDimension().width - fCambiarClave.getSize().width) / 2, (ini.getDimension().height - fCambiarClave.getSize().height) / 2);
                fCambiarClave.show();
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

                fCambiarClave.setVisible(true);
                fCambiarClave.txtClaveAnterior.requestFocus();
            }

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAuxiliarLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmCambiarClaveActionPerformed

    private void mnuLogisticaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLogisticaActionPerformed

    }//GEN-LAST:event_mnuLogisticaActionPerformed

    private void mItmExportarExcelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mItmExportarExcelMouseClicked

    }//GEN-LAST:event_mItmExportarExcelMouseClicked

    private void mItmExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmExportarExcelActionPerformed

        FImportarArchivoExcel fImportarExcel = new FImportarArchivoExcel(ini);
        if (fImportarExcel != null) {
            PrincipalAuxiliarLogistica.escritorio.add(fImportarExcel);
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

        }
    }//GEN-LAST:event_mItmExportarExcelActionPerformed

    private void mItmManifestarPedidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mItmManifestarPedidosMouseClicked

    }//GEN-LAST:event_mItmManifestarPedidosMouseClicked

    private void mItmManifestarPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmManifestarPedidosActionPerformed
        try {
//            FManifestarFacturasEnPicking fManifestarPedidos = new FManifestarFacturasEnPicking(ini);

            FManifestarPedidosEnRuta fManifestarPedidosEnRuta = new FManifestarPedidosEnRuta(ini);

            PrincipalAuxiliarLogistica.escritorio.add(fManifestarPedidosEnRuta);
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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAuxiliarLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_mItmManifestarPedidosActionPerformed

    private void mItmDescargarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmDescargarFacturasActionPerformed
        try {
            DescargarFacturas fDescargarFActuras = new DescargarFacturas(ini);

            if (fDescargarFActuras != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fDescargarFActuras);
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
            Logger.getLogger(PrincipalAuxiliarLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_mItmDescargarFacturasActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {

            FReporteDeRechazosTotales fReporteRechazosTotales = new FReporteDeRechazosTotales(ini);
            if (fReporteRechazosTotales != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fReporteRechazosTotales);
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
            Logger.getLogger(PrincipalAuxiliarLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void mItmAnularFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmAnularFacturasActionPerformed
        try {

            FAnularFacturas fAnularFacturas = new FAnularFacturas(ini);
            if (fAnularFacturas != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fAnularFacturas);
                // fDescargarFActuras.setSize(escritorio.getSize());
                fAnularFacturas.setLocation(((ini.getDimension().width - fAnularFacturas.getSize().width) / 2), ((ini.getDimension().height - fAnularFacturas.getSize().height) / 2));
                fAnularFacturas.setVisible(true);
                fAnularFacturas.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FAnularFacturas");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAuxiliarLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmAnularFacturasActionPerformed

    private void mItmConsultarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmConsultarFacturaActionPerformed

//
//            if (fConsultarFacturasRemoto != null) {
//                PrincipalAuxiliarLogistica.escritorio.add(fConsultarFacturasRemoto);
////            fDescargarFActuras.setSize(escritorio.getSize());
////            fDescargarFActuras.setMaximumSize(ini.getDimension());
//                fConsultarFacturasRemoto.setLocation(((ini.getDimension().width - fConsultarFacturasRemoto.getSize().width) / 2), ((ini.getDimension().height - fConsultarFacturasRemoto.getSize().height) / 2));
//                fConsultarFacturasRemoto.setVisible(true);
//                fConsultarFacturasRemoto.show();
//datos = new ArrayList<>();
//        datos.add(0, usuario);
//        datos.add(1, formulario);
//        datos.add(2, "Click Formulario FAnularFacturas");
//        datos.add(3, "CURRENT_TIMESTAMP");
// new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
//            }
        try {
            FConsultarFacturasRemoto form1 = new FConsultarFacturasRemoto(this.ini);

            PrincipalAuxiliarLogistica.escritorio.add(form1);
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
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_mItmConsultarFacturaActionPerformed

    private void mItmSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_mItmSalirActionPerformed

    private void mItmConsultarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmConsultarClienteActionPerformed
        try {
            FConsultarFacturasPorCliente fConsultarFacturasPorCliente = new FConsultarFacturasPorCliente(ini);
            if (fConsultarFacturasPorCliente != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fConsultarFacturasPorCliente);
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
            Logger.getLogger(PrincipalAuxiliarLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmConsultarClienteActionPerformed

    private void mItmRptPorPeriodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmRptPorPeriodoActionPerformed
        try {
            FReportePedidosMovilizadosPorPeriodo fReporteMovilizado = new FReportePedidosMovilizadosPorPeriodo(ini);
            if (fReporteMovilizado != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fReporteMovilizado);
                fReporteMovilizado.setLocation(((ini.getDimension().width - fReporteMovilizado.getSize().width) / 2), ((ini.getDimension().height - fReporteMovilizado.getSize().height) / 2) - 30);
                fReporteMovilizado.setVisible(true);
                fReporteMovilizado.show();

                datos = new ArrayList<>();
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
            Logger.getLogger(PrincipalAuxiliarLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmRptPorPeriodoActionPerformed

    private void mItmRptFacturasSinMovimietoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmRptFacturasSinMovimietoActionPerformed

        try {
          FReporteFacturasPendientes fReporteFacturasPendientes = new FReporteFacturasPendientes(ini);
            // TODO add your handling code here:FConsultarFacturasPorCliente

            this.escritorio.add(fReporteFacturasPendientes);
            fReporteFacturasPendientes.setLocation(((ini.getDimension().width - fReporteFacturasPendientes.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturasPendientes.getSize().height) / 2) - 30);
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
        
        
//        try {
//            FReporteFacturasSinMovimientos fReporteSinMovimientos = new FReporteFacturasSinMovimientos(ini);
//            if (fReporteSinMovimientos != null) {
//                PrincipalAuxiliarLogistica.escritorio.add(fReporteSinMovimientos);
//                fReporteSinMovimientos.setLocation(((ini.getDimension().width - fReporteSinMovimientos.getSize().width) / 2), ((ini.getDimension().height - fReporteSinMovimientos.getSize().height) / 2) - 30);
//                fReporteSinMovimientos.setVisible(true);
//                fReporteSinMovimientos.show();
//
//                datos = new ArrayList<>();
//                datos.add(0, usuario);
//                datos.add(1, formulario);
//                datos.add(2, "Click Formulario FReporteFacturasSinMovimientos");
//                datos.add(3, "CURRENT_TIMESTAMP");
//                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
//
//                if (timer.isRunning()) {
//                    timer.stop();
//                }
//                timer.start();
//
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(PrincipalAuxiliarLogistica.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_mItmRptFacturasSinMovimietoActionPerformed

    private void mnuHabilitarManifiestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuHabilitarManifiestoActionPerformed
        try {
            FHabilitarManifiesto fHabilitarManifiesto = new FHabilitarManifiesto(ini);
            if (fHabilitarManifiesto != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fHabilitarManifiesto);
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
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuHabilitarManifiestoActionPerformed

    private void mItmConciliarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmConciliarFacturasActionPerformed
        try {
            FReporteManifiestosConcilidos fManifiestosConciliados = new FReporteManifiestosConcilidos(ini);
            if (fManifiestosConciliados != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fManifiestosConciliados);
                fManifiestosConciliados.setLocation(((ini.getDimension().width - fManifiestosConciliados.getSize().width) / 2), ((ini.getDimension().height - fManifiestosConciliados.getSize().height) / 2) - 30);
                //fManifiestosConciliados.setVisible(true);
                fManifiestosConciliados.refrescarTblManifiestosSinConciliar();
                fManifiestosConciliados.show();
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
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }     // TODO add your handling code here:
    }//GEN-LAST:event_mItmConciliarFacturasActionPerformed

    private void mnuHabilitarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuHabilitarFacturasActionPerformed
        try {
            FHabilitarFacturas fHabilitarFacturas = new FHabilitarFacturas(ini);
            if (fHabilitarFacturas != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fHabilitarFacturas);
                fHabilitarFacturas.setLocation(((ini.getDimension().width - fHabilitarFacturas.getSize().width) / 2), ((ini.getDimension().height - fHabilitarFacturas.getSize().height) / 2) - 30);
                //fHabilitarFacturas.setVisible(true);
                fHabilitarFacturas.show();

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
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuHabilitarFacturasActionPerformed

    private void mItmRptManifiestosEnDistribucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmRptManifiestosEnDistribucionActionPerformed
        try {
            FReporteManifiestosEnDistribucion fReporteManifiestosEnDistribucion = new FReporteManifiestosEnDistribucion(ini);
            if (fReporteManifiestosEnDistribucion != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fReporteManifiestosEnDistribucion);
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
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmRptManifiestosEnDistribucionActionPerformed

    private void mnu_rptFacturacionPorCanalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionPorCanalActionPerformed
        try {
            FReporteFacturacionPorCanal fReporteFacturacionPorCanal = new FReporteFacturacionPorCanal(ini);
            if (fReporteFacturacionPorCanal != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fReporteFacturacionPorCanal);
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
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnu_rptFacturacionPorCanalActionPerformed

    private void mnu_rptFacturacionTATActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionTATActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
//            if (fReporteFacturacionTAT != null) {
//                PrincipalAuxiliarLogistica.escritorio.add(fReporteFacturacionTAT);
//                fReporteFacturacionTAT.setLocation(((ini.getDimension().width - fReporteFacturacionTAT.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturacionTAT.getSize().height) / 2) - 30);
//                fReporteFacturacionTAT.setVisible(true);
//                fReporteFacturacionTAT.show();
//datos = new ArrayList<>();
//                datos.add(0, usuario);
//                datos.add(1, formulario);
//                datos.add(2, "Click Formulario FConsultarFacturasPorCliente");
//                datos.add(3, "CURRENT_TIMESTAMP");
//                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
//            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnu_rptFacturacionTATActionPerformed

    private void mItmRptMamifiestosSinDescargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmRptMamifiestosSinDescargarActionPerformed

        try {
            FReporteManifiestosSinDescargar freporteManifiestosSinDescargar = new FReporteManifiestosSinDescargar(ini);
            if (freporteManifiestosSinDescargar != null) {
                PrincipalAuxiliarLogistica.escritorio.add(freporteManifiestosSinDescargar);
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
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_mItmRptMamifiestosSinDescargarActionPerformed

    private void mItmMovilizrFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmMovilizrFacturasActionPerformed

        try {
            FManifestarFacturasEnPicking fFacturasEnPicking = new FManifestarFacturasEnPicking(ini);
            if (fFacturasEnPicking != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fFacturasEnPicking);
                fFacturasEnPicking.setLocation(((ini.getDimension().width - fFacturasEnPicking.getSize().width) / 2), ((ini.getDimension().height - fFacturasEnPicking.getSize().height) / 2) - 30);
                fFacturasEnPicking.setVisible(true);
                //fDesvincularFactura.cargarInformacion();
                fFacturasEnPicking.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FManifestarFacturasEnPicking");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAuxiliarLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmMovilizrFacturasActionPerformed

    private void mItmFacturasFinaalPickingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmFacturasFinaalPickingActionPerformed
        try {
            // TODO add your handling code here: fRegistroDePedidosFinLineaDePicking
            FRegistroDePedidosFinLineaDePicking fRegistroDePedidosFinLineaDePicking = new FRegistroDePedidosFinLineaDePicking(ini);
            if (fRegistroDePedidosFinLineaDePicking != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fRegistroDePedidosFinLineaDePicking);
                fRegistroDePedidosFinLineaDePicking.setLocation(((ini.getDimension().width - fRegistroDePedidosFinLineaDePicking.getSize().width) / 2), ((ini.getDimension().height - fRegistroDePedidosFinLineaDePicking.getSize().height) / 2) - 30);
                fRegistroDePedidosFinLineaDePicking.setVisible(true);
                //fDesvincularFactura.cargarInformacion();
                fRegistroDePedidosFinLineaDePicking.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FRegistroDePedidosFinLineaDePicking");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAuxiliarLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmFacturasFinaalPickingActionPerformed

    private void mItmReportePendientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmReportePendientesActionPerformed

        try {
            FReporteFacturasPendientesEnPicking fReporteFacturasPendientesEnPicking = new FReporteFacturasPendientesEnPicking(ini);
            if (fReporteFacturasPendientesEnPicking != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fReporteFacturasPendientesEnPicking);
                fReporteFacturasPendientesEnPicking.setLocation(((ini.getDimension().width - fReporteFacturasPendientesEnPicking.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturasPendientesEnPicking.getSize().height) / 2) - 30);
                fReporteFacturasPendientesEnPicking.setVisible(true);
                fReporteFacturasPendientesEnPicking.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteFacturasPendientesEnPicking");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmReportePendientesActionPerformed

    private void mItmActualizarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmActualizarClientesActionPerformed

        FGeoreferenciarClientes fGeoreferenciarClientes = new FGeoreferenciarClientes(ini);
        if (fGeoreferenciarClientes != null) {
            PrincipalAuxiliarLogistica.escritorio.add(fGeoreferenciarClientes);
            fGeoreferenciarClientes.setLocation(((ini.getDimension().width - fGeoreferenciarClientes.getSize().width) / 2), ((ini.getDimension().height - fGeoreferenciarClientes.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            fGeoreferenciarClientes.setVisible(true);
            fGeoreferenciarClientes.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FGeoreferenciarClientes");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        }
    }//GEN-LAST:event_mItmActualizarClientesActionPerformed

    private void mItmRptDescuentosyRecogidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmRptDescuentosyRecogidasActionPerformed

        try {
            FReporteDescuentos_Recogidas fReporteDescuentos_Recogidas = new FReporteDescuentos_Recogidas(ini);
            if (fReporteDescuentos_Recogidas != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fReporteDescuentos_Recogidas);
                fReporteDescuentos_Recogidas.setLocation(((ini.getDimension().width - fReporteDescuentos_Recogidas.getSize().width) / 2), ((ini.getDimension().height - fReporteDescuentos_Recogidas.getSize().height) / 2) - 30);
                fReporteDescuentos_Recogidas.setVisible(true);
                //fReporteDescuentos_Recogidas.cargarInformacion();
                fReporteDescuentos_Recogidas.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteDescuentos_Recogidas");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

                if (timer.isRunning()) {
                    timer.stop();
                }
                timer.start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmRptDescuentosyRecogidasActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        try {
            FConsultarManifiestos form1 = new FConsultarManifiestos(ini);

            PrincipalAuxiliarLogistica.escritorio.add(form1);
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
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed

        try {
            FConsultaManifiestosDescargados fConsultaManifiestosDescargados = new FConsultaManifiestosDescargados(this.ini);

            PrincipalAuxiliarLogistica.escritorio.add(fConsultaManifiestosDescargados);
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
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed

        try {
            FConsultaManifiestosSinDescargar fConsultaManifiestosSinDescargar = new FConsultaManifiestosSinDescargar(this.ini);

            PrincipalAuxiliarLogistica.escritorio.add(fConsultaManifiestosSinDescargar);
            fConsultaManifiestosSinDescargar.setLocation(((ini.getDimension().width - fConsultaManifiestosSinDescargar.getSize().width) / 2), ((ini.getDimension().height - fConsultaManifiestosSinDescargar.getSize().height) / 2) - 30);
            fConsultaManifiestosSinDescargar.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FConsultaManifiestosSinDescargar");
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

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed

        try {
            FReporteClientesConDescuento fReporteClientesConDescuento = new FReporteClientesConDescuento(ini);
            if (fReporteClientesConDescuento != null) {
                PrincipalAuxiliarLogistica.escritorio.add(fReporteClientesConDescuento);
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
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem69ActionPerformed
        try {
            FIntegrador fIntegrador = new FIntegrador(ini);

            PrincipalAuxiliarLogistica.escritorio.add(fIntegrador);
            fIntegrador.setLocation(((ini.getDimension().width - fIntegrador.getSize().width) / 2), ((ini.getDimension().height - fIntegrador.getSize().height) / 2) - 30);
            //fIntegrador.setSize(ini.getDimension());
            fIntegrador.setVisible(true);
            fIntegrador.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FIntegrador");
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

    private void mnuManifestarPedidos6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos6MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuManifestarPedidos6MouseClicked

    private void mnuManifestarPedidos6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos6ActionPerformed
        try {
            FManifestarPedidosHielera_2 fManifestarPedidosHielera = new FManifestarPedidosHielera_2(ini);
            PrincipalAuxiliarLogistica.escritorio.add(fManifestarPedidosHielera);
            fManifestarPedidosHielera.setSize(ini.getDimension());
            fManifestarPedidosHielera.setLocation(((ini.getDimension().width - fManifestarPedidosHielera.getSize().width) / 2), ((ini.getDimension().height - fManifestarPedidosHielera.getSize().height) / 2));
            fManifestarPedidosHielera.setVisible(true);
            fManifestarPedidosHielera.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FManifestarPedidosHielera_2");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuManifestarPedidos6ActionPerformed

    private void jMenuItem74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem74ActionPerformed
        try {
            DescargarFacturas_2 descargarFacturas_2 = new DescargarFacturas_2(this.ini);
            PrincipalAuxiliarLogistica.escritorio.add(descargarFacturas_2);
            //descargarFacturas_2.CargarVista();
            descargarFacturas_2.setSize(ini.getDimension());
            // fDescargarFActuras.setLocation(((ini.getDimension().width - fDescargarFActuras.getSize().width) / 2), ((ini.getDimension().height - fDescargarFActuras.getSize().height) / 2));
            descargarFacturas_2.setLocation(0, 0);
            descargarFacturas_2.setVisible(true);
            descargarFacturas_2.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario DescargarFacturas_2");
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

    private void jMenuItem72ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem72ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem72ActionPerformed

    private void jMenuItem73ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem73ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem73ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        JOptionPane.showMessageDialog(this, "Servidor Local " + ini.getServerLocal() + "\n BBDD " + ini.getBdLocal(), ini.getServerLocal(), JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        JOptionPane.showMessageDialog(this, "Servidor Remoto " + ini.getServerRemota() + "\n BBDD " + ini.getBdRemota(), ini.getServerRemota(), JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        JOptionPane.showMessageDialog(this, "Nombre de Cliente " + ini.getPropiedades().getProperty("nombreCliente"), "Nombre de Cliente ", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem34ActionPerformed

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

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed

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
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
  try {
            FConsultarManifiestos form1 = new FConsultarManifiestos(ini);

            PrincipalAuxiliarLogistica.escritorio.add(form1);
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
    }//GEN-LAST:event_jMenuItem15ActionPerformed

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
            java.util.logging.Logger.getLogger(PrincipalAuxiliarLogistica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                    new PrincipalAuxiliarLogistica().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(PrincipalAuxiliarLogistica.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
//      private void llenarVariables() {
//        new Thread(new HiloListadoDeCausalesdeRechazo(ini, 5)).start();
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
        Image retValue = null;
        try {
            String ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "imagenes/iconoApp/" + ini.getPropiedades().getProperty("iconoApp");
            retValue = Toolkit.getDefaultToolkit().getImage(ruta);
            //getImage(ClassLoader.getSystemResource("aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"));
        } catch (Exception ex) {

        }
        return retValue;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JMenuBar barraDeMenus;
    public static javax.swing.JDesktopPane escritorio;
    private aplicacionlogistica.distribucion.formularios.administracion.FCrearMarcasDexxxxxxxxxxVehiculos fCrearMarcasDeVehiculos1;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu36;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem69;
    private javax.swing.JMenuItem jMenuItem72;
    private javax.swing.JMenuItem jMenuItem73;
    private javax.swing.JMenuItem jMenuItem74;
    private javax.swing.JMenuItem jMenuItem80;
    private javax.swing.JMenuItem mItmActualizarClientes;
    private javax.swing.JMenuItem mItmAnularFacturas;
    private javax.swing.JMenuItem mItmCambiarClave;
    private javax.swing.JMenuItem mItmConciliarFacturas;
    private javax.swing.JMenuItem mItmConsultarCliente;
    private javax.swing.JMenuItem mItmConsultarFactura;
    private javax.swing.JMenuItem mItmDescargarFacturas;
    private javax.swing.JMenuItem mItmExportarExcel;
    private javax.swing.JMenuItem mItmFacturasFinaalPicking;
    private javax.swing.JMenuItem mItmManifestarPedidos;
    private javax.swing.JMenuItem mItmMovilizrFacturas;
    private javax.swing.JMenuItem mItmReportePendientes;
    private javax.swing.JMenuItem mItmRptDescuentosyRecogidas;
    private javax.swing.JMenuItem mItmRptFacturasSinMovimieto;
    private javax.swing.JMenuItem mItmRptMamifiestosSinDescargar;
    private javax.swing.JMenuItem mItmRptManifiestosEnDistribucion;
    private javax.swing.JMenuItem mItmRptPorPeriodo;
    private javax.swing.JMenuItem mItmSalir;
    private javax.swing.JMenu mnuArchivo;
    private javax.swing.JMenu mnuClientes;
    private javax.swing.JMenu mnuConsultar;
    private javax.swing.JMenu mnuConsultas;
    private javax.swing.JMenu mnuDistribucionLaHielera;
    private javax.swing.JMenu mnuDistribucionLogistica;
    private javax.swing.JMenuItem mnuHabilitarFacturas;
    private javax.swing.JMenuItem mnuHabilitarManifiesto;
    private javax.swing.JMenu mnuImportarExcel;
    private javax.swing.JMenu mnuLogistica;
    private javax.swing.JMenuItem mnuManifestarPedidos6;
    private javax.swing.JMenu mnuManifiestos;
    private javax.swing.JMenu mnuPicking;
    private javax.swing.JMenu mnuRechazos;
    private javax.swing.JMenu mnuReportes;
    private javax.swing.JMenu mnuRptFacturacion;
    private javax.swing.JMenuItem mnu_rptFacturacionPorCanal;
    private javax.swing.JMenuItem mnu_rptFacturacionTAT;
    // End of variables declaration//GEN-END:variables

    private void timerEvent(java.awt.event.ActionEvent evt) {
        if (timer.isRunning()) {
            timer.stop();
            System.out.println("se detiene el timer " + new Date());
          //  System.exit(0);
//            timer = new Timer(60000, actionListener);
//            System.out.println("se llama al timer");
//            timer.start();
//            System.out.println("inicia el timer");
//            System.out.println("expired en funcion");
        }
    }

}
