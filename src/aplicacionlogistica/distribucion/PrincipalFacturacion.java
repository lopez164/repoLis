/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion;

import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.costumerService.PrincipalUsuarioCostumerService;
import aplicacionlogistica.distribucion.clientes.FGeoreferenciarClientes;
import aplicacionlogistica.configuracion.CambiarClave;
import aplicacionlogistica.configuracion.HiloBitacoraMovimientosEnElSistema;
import aplicacionlogistica.distribucion.consultas.FConsultarManifiestos;
import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcel;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FConsultaManifiestosDescargados;
import aplicacionlogistica.costumerService.Reportes.FReporteClientesConDescuento;
import aplicacionlogistica.costumerService.Reportes.FReporteDescuentos_Recogidas;
import aplicacionlogistica.distribucion.Threads.HiloCargarFormularioPrincipalFacturacion;
import aplicacionlogistica.distribucion.consultas.FConsultaManifiestosSinDescargar;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasPorCliente;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultaPedidosDescargadosPorConductorHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarProducto;
import aplicacionlogistica.distribucion.formularios.ReporteManifiestosSinDescargar.FReporteManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.reportes.FDashBoardFacturas;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteDeRechazosTotales;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteFacturasPendientes;
import aplicacionlogistica.distribucion.formularios.reportes.FReportePedidosMovilizadosPorPeriodo;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteManifiestosEnDistribucion.FReporteManifiestosEnDistribucion;
import aplicacionlogistica.distribucion.formularios.reportes.facturacion.FReporteFacturacionPorCanal;
import aplicacionlogistica.distribucion.integrador.FIntegrador;
import aplicacionlogistica.distribucion.integrador.FIntegradorExcelFile;
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
public class PrincipalFacturacion extends javax.swing.JFrame {

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
    String formulario = "PrincipalFacturacion";
     ActionListener actionListener;
     public Timer timer;

    public PrincipalFacturacion() throws Exception {
        String rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
        ini = new Inicio(new File(rutaDeArchivo + "ReadUs.ini"));
        CUsuarios user = new CUsuarios(ini, "79481521");
        ini.setUser(user);
        usuario = Inicio.deCifrar(ini.getUser().getNombreUsuario());

        initComponents();

        this.setSize((int) ini.getDimension().getWidth(), (int) ini.getDimension().getHeight() - 43);
        this.setLocation(0, 0);

        /*SE instsancian los formularios del menu */
        new Thread(new HiloCargarFormularioPrincipalFacturacion(this)).start();
    }

    public PrincipalFacturacion(Inicio ini) throws Exception, Throwable {

        //initComponents();
        this.ini = ini;
        usuario = Inicio.deCifrar(ini.getUser().getNombreUsuario());
        if (ini.isEstaClienteActivo()) {
            initComponents();
            FDashBoardFacturas   fDashBoardFacturas = new FDashBoardFacturas(ini);
            this.escritorio.add(fDashBoardFacturas);
            fDashBoardFacturas.setVisible(true);
            fDashBoardFacturas.show();
            
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

            Properties propiedades = ini.getPropiedades();

            try {
                ini.cargarImagenEscritorio(escritorio);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Revisar archivo de configuracion", "Imagen de Fonfo no encontrada", JOptionPane.ERROR_MESSAGE);
            }

            fCambiarClave = new CambiarClave(ini);

            /*SE instsancian los formularios del menu */
            new Thread(new HiloCargarFormularioPrincipalFacturacion(this)).start();

            this.setSize(this.ini.getDimension());
            this.setLocation(0, 0);
            this.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos());

            /*MÉTODO PARA CAMBIAR LA CLAVE, CUANDO SE INGRSA POR PRIMERA VEZ AL 
            SISTEMA*/
            if (this.ini.getUser().getClaveUsuario().equals(Inicio.cifrar("123456"))) { // cuando el usuario es nuevo
                //this.escritorio.setSize(600, 400);
                CambiarClave fCambiarClave = new CambiarClave(ini);
                if (fCambiarClave != null) {
                    PrincipalFacturacion.escritorio.add(fCambiarClave);
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

                    fCambiarClave.setVisible(true);
                    fCambiarClave.txtClaveAnterior.requestFocus();
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
        jMenu9 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem40 = new javax.swing.JMenuItem();
        jMenuItem75 = new javax.swing.JMenuItem();
        jMenuItem69 = new javax.swing.JMenuItem();
        mnuDistribucionLaHielera = new javax.swing.JMenu();
        jMenuItem70 = new javax.swing.JMenuItem();
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

        jMenuItem69.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/logo_hielerablue.png"))); // NOI18N
        jMenuItem69.setText("Integrador Hielera");
        jMenuItem69.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem69ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem69);

        mnuLogistica.add(jMenu9);

        mnuDistribucionLaHielera.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        mnuDistribucionLaHielera.setText("distribucion Hielera");

        jMenuItem70.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/logo_hielerablue.png"))); // NOI18N
        jMenuItem70.setText("Integrador Hielera");
        jMenuItem70.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem70ActionPerformed(evt);
            }
        });
        mnuDistribucionLaHielera.add(jMenuItem70);

        mnuLogistica.add(mnuDistribucionLaHielera);

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
                PrincipalFacturacion.escritorio.add(fCambiarClave);
                fCambiarClave.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
                fCambiarClave.setLocation((ini.getDimension().width - fCambiarClave.getSize().width) / 2, (ini.getDimension().height - fCambiarClave.getSize().height) / 2);
                fCambiarClave.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario CambiarClave");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

                fCambiarClave.setVisible(true);
                fCambiarClave.txtClaveAnterior.requestFocus();
            }

        } catch (Exception ex) {
            Logger.getLogger(PrincipalFacturacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmCambiarClaveActionPerformed

    private void mnuLogisticaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLogisticaActionPerformed

    }//GEN-LAST:event_mnuLogisticaActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {

            FReporteDeRechazosTotales fReporteRechazosTotales = new FReporteDeRechazosTotales(ini);
            if (fReporteRechazosTotales != null) {
                PrincipalFacturacion.escritorio.add(fReporteRechazosTotales);
                fReporteRechazosTotales.setLocation(((ini.getDimension().width - fReporteRechazosTotales.getSize().width) / 2), ((ini.getDimension().height - fReporteRechazosTotales.getSize().height) / 2));
                fReporteRechazosTotales.setVisible(true);
                fReporteRechazosTotales.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteDeRechazosTotales");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalFacturacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void mItmConsultarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmConsultarFacturaActionPerformed

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
            Logger.getLogger(PrincipalLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_mItmConsultarFacturaActionPerformed

    private void mItmSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_mItmSalirActionPerformed

    private void mItmConsultarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmConsultarClienteActionPerformed
        try {
            FConsultarFacturasPorCliente fConsultarFacturasPorCliente = new FConsultarFacturasPorCliente(ini);
            if (fConsultarFacturasPorCliente != null) {
                PrincipalFacturacion.escritorio.add(fConsultarFacturasPorCliente);
                fConsultarFacturasPorCliente.setLocation(((ini.getDimension().width - fConsultarFacturasPorCliente.getSize().width) / 2), ((ini.getDimension().height - fConsultarFacturasPorCliente.getSize().height) / 2) - 30);
                fConsultarFacturasPorCliente.setVisible(true);
                fConsultarFacturasPorCliente.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FConsultarFacturasPorCliente");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalFacturacion.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmConsultarClienteActionPerformed

    private void mItmRptPorPeriodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmRptPorPeriodoActionPerformed
        try {
            FReportePedidosMovilizadosPorPeriodo fReporteMovilizado = new FReportePedidosMovilizadosPorPeriodo(ini);
            if (fReporteMovilizado != null) {
                PrincipalFacturacion.escritorio.add(fReporteMovilizado);
                fReporteMovilizado.setLocation(((ini.getDimension().width - fReporteMovilizado.getSize().width) / 2), ((ini.getDimension().height - fReporteMovilizado.getSize().height) / 2) - 30);
                fReporteMovilizado.setVisible(true);
                fReporteMovilizado.show();

                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReportePedidosMovilizadosPorPeriodo");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalFacturacion.class.getName()).log(Level.SEVERE, null, ex);
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
          }//GEN-LAST:event_mItmRptFacturasSinMovimietoActionPerformed

    private void mItmRptManifiestosEnDistribucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmRptManifiestosEnDistribucionActionPerformed
        try {
            FReporteManifiestosEnDistribucion fReporteManifiestosEnDistribucion = new FReporteManifiestosEnDistribucion(ini);
            if (fReporteManifiestosEnDistribucion != null) {
                PrincipalFacturacion.escritorio.add(fReporteManifiestosEnDistribucion);
                fReporteManifiestosEnDistribucion.setLocation(((ini.getDimension().width - fReporteManifiestosEnDistribucion.getSize().width) / 2), ((ini.getDimension().height - fReporteManifiestosEnDistribucion.getSize().height) / 2) - 30);
                fReporteManifiestosEnDistribucion.setVisible(true);
                fReporteManifiestosEnDistribucion.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteManifiestosEnDistribucion");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmRptManifiestosEnDistribucionActionPerformed

    private void mnu_rptFacturacionPorCanalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionPorCanalActionPerformed
        try {
            FReporteFacturacionPorCanal fReporteFacturacionPorCanal = new FReporteFacturacionPorCanal(ini);
            if (fReporteFacturacionPorCanal != null) {
                PrincipalFacturacion.escritorio.add(fReporteFacturacionPorCanal);
                fReporteFacturacionPorCanal.setLocation(((ini.getDimension().width - fReporteFacturacionPorCanal.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturacionPorCanal.getSize().height) / 2) - 30);
                fReporteFacturacionPorCanal.setVisible(true);
                fReporteFacturacionPorCanal.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteFacturacionPorCanal");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
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
                PrincipalFacturacion.escritorio.add(freporteManifiestosSinDescargar);
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
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_mItmRptMamifiestosSinDescargarActionPerformed

    private void mItmActualizarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmActualizarClientesActionPerformed

        FGeoreferenciarClientes fGeoreferenciarClientes = new FGeoreferenciarClientes(ini);
        if (fGeoreferenciarClientes != null) {
            PrincipalFacturacion.escritorio.add(fGeoreferenciarClientes);
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
        }
    }//GEN-LAST:event_mItmActualizarClientesActionPerformed

    private void mItmRptDescuentosyRecogidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmRptDescuentosyRecogidasActionPerformed

        try {
            FReporteDescuentos_Recogidas fReporteDescuentos_Recogidas = new FReporteDescuentos_Recogidas(ini);
            if (fReporteDescuentos_Recogidas != null) {
                PrincipalFacturacion.escritorio.add(fReporteDescuentos_Recogidas);
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
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmRptDescuentosyRecogidasActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        try {
            FConsultarManifiestos form1 = new FConsultarManifiestos(ini);

            PrincipalFacturacion.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2));
            form1.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FConsultarManifiestos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioConsultas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed

        try {
            FConsultaManifiestosDescargados fConsultaManifiestosDescargados = new FConsultaManifiestosDescargados(this.ini);

            PrincipalFacturacion.escritorio.add(fConsultaManifiestosDescargados);
            fConsultaManifiestosDescargados.setLocation(((ini.getDimension().width - fConsultaManifiestosDescargados.getSize().width) / 2), ((ini.getDimension().height - fConsultaManifiestosDescargados.getSize().height) / 2) - 30);
            fConsultaManifiestosDescargados.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FConsultaManifiestosDescargados");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioConsultas.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed

        try {
            FConsultaManifiestosSinDescargar fConsultaManifiestosSinDescargar = new FConsultaManifiestosSinDescargar(this.ini);

            PrincipalFacturacion.escritorio.add(fConsultaManifiestosSinDescargar);
            fConsultaManifiestosSinDescargar.setLocation(((ini.getDimension().width - fConsultaManifiestosSinDescargar.getSize().width) / 2), ((ini.getDimension().height - fConsultaManifiestosSinDescargar.getSize().height) / 2) - 30);
            fConsultaManifiestosSinDescargar.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FConsultaManifiestosSinDescargar");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioConsultas.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed

        try {
            FReporteClientesConDescuento fReporteClientesConDescuento = new FReporteClientesConDescuento(ini);
            if (fReporteClientesConDescuento != null) {
                PrincipalFacturacion.escritorio.add(fReporteClientesConDescuento);
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
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem69ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem69ActionPerformed
        try {
            FIntegrador fIntegrador = new FIntegrador(ini);

            PrincipalFacturacion.escritorio.add(fIntegrador);
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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem69ActionPerformed

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioConsultas.class.getName()).log(Level.SEVERE, null, ex);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem80ActionPerformed

    private void jMenuItem70ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem70ActionPerformed
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
    }//GEN-LAST:event_jMenuItem70ActionPerformed

    private void jMenuItem16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem16MouseClicked

    }//GEN-LAST:event_jMenuItem16MouseClicked

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        try {
            FImportarArchivoExcel fImportarExcel = new FImportarArchivoExcel(ini);

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
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem16ActionPerformed

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem40ActionPerformed

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TO
    }//GEN-LAST:event_jMenuItem75ActionPerformed

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

            PrincipalFacturacion.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2));
            form1.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FConsultarManifiestos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
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
            java.util.logging.Logger.getLogger(PrincipalFacturacion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the fCambiarClave */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new PrincipalFacturacion().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(PrincipalFacturacion.class.getName()).log(Level.SEVERE, null, ex);
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JMenuBar barraDeMenus;
    public static javax.swing.JDesktopPane escritorio;
    private aplicacionlogistica.distribucion.formularios.administracion.FCrearMarcasDexxxxxxxxxxVehiculos fCrearMarcasDeVehiculos1;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu36;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem40;
    private javax.swing.JMenuItem jMenuItem69;
    private javax.swing.JMenuItem jMenuItem70;
    private javax.swing.JMenuItem jMenuItem75;
    private javax.swing.JMenuItem jMenuItem80;
    private javax.swing.JMenuItem mItmActualizarClientes;
    private javax.swing.JMenuItem mItmCambiarClave;
    private javax.swing.JMenuItem mItmConsultarCliente;
    private javax.swing.JMenuItem mItmConsultarFactura;
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
    private javax.swing.JMenu mnuLogistica;
    private javax.swing.JMenu mnuManifiestos;
    private javax.swing.JMenu mnuRechazos;
    private javax.swing.JMenu mnuReportes;
    private javax.swing.JMenu mnuRptFacturacion;
    private javax.swing.JMenuItem mnu_rptFacturacionPorCanal;
    private javax.swing.JMenuItem mnu_rptFacturacionTAT;
    // End of variables declaration//GEN-END:variables
}
