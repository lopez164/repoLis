/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion;

import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import aplicacionlogistica.distribucion.formularios.FHabilitarFacturas;
import aplicacionlogistica.distribucion.formularios.FAnularFacturas;
import aplicacionlogistica.costumerService.PrincipalUsuarioCostumerService;
import aplicacionlogistica.distribucion.clientes.FGeoreferenciarClientes;
import aplicacionlogistica.configuracion.CambiarClave;
import aplicacionlogistica.configuracion.HiloBitacoraMovimientosEnElSistema;
import aplicacionlogistica.distribucion.consultas.FConsultarManifiestos;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FConsultaManifiestosDescargados;
import aplicacionlogistica.costumerService.FIngresarDescuentoClientes;
import aplicacionlogistica.costumerService.FIngresarRecogidasClientes;
import aplicacionlogistica.distribucion.consultas.FConsultaManifiestosSinDescargar;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasPorCliente;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.consultas.FHabilitarManifiesto;
import aplicacionlogistica.distribucion.formularios.FModificarManifiesto;
import aplicacionlogistica.distribucion.formularios.FTrasladoDeFacturas;
import aplicacionlogistica.distribucion.formularios.Hielera.DescargarFacturas_2;
import aplicacionlogistica.distribucion.formularios.Hielera.FControlarSalidaFacturasBarCode2;
import aplicacionlogistica.distribucion.formularios.Hielera.FManifestarPedidosHielera_2;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultaPedidosDescargadosPorConductorHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarProducto;
import aplicacionlogistica.distribucion.formularios.ReporteManifiestosSinDescargar.FReporteManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarCarros;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarEmpleados;
import aplicacionlogistica.distribucion.formularios.reportes.FDashBoardFacturas;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteAlertasSalidaDeProductos;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteDeRechazosTotales;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteFacturasPendientes;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteGeneralDeDescargueDeRutas;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteManifiestosConcilidos;
import aplicacionlogistica.distribucion.formularios.reportes.FReportePedidosEnDistribucion;
import aplicacionlogistica.distribucion.formularios.reportes.FReportePedidosMovilizadosPorPeriodo;
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
import aplicacionlogistica.picking.FReporteFacturasPendientesEnPicking;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author VLI_488
 */
public class PrincipalLogistica extends javax.swing.JFrame {

    public Inicio ini = null;

//    public CambiarClave fCambiarClave;
//    public DescargarFacturas fDescargarFActuras;
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
//    public FImportarArchivoExcel fImportarArchivoExcel;
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
//     public FManifestarPedidosEnRuta fManifestarPedidosEnRuta=null;
//     public FRegistroDePedidosFinLineaDePicking fRegistroDePedidosFinLineaDePicking=null;
//     public FGeoreferenciarClientes fGeoreferenciarClientes=null;
//     public FAdicionarPedidosNoReportados fAdicionarPedidosNoReportados=null;
    FManifestarPedidosHielera_2 fManifestarPedidosHielera = null;
    DescargarFacturas_2 descargarFacturas_2 = null;
    FControlarSalidaFacturasBarCode2 fControlarSalidaFacturasBarCode2 = null;
//     
    List<String> datos = new ArrayList<>();
    String usuario;
    String formulario = "PrincipalLogistica";

    ActionListener actionListener;
    public Timer timer;

    public PrincipalLogistica() throws Exception {
        String rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
        ini = new Inicio(new File(rutaDeArchivo + "ReadUs.ini"));
        CUsuarios user = new CUsuarios(ini, "79481521");
        ini.setUser(user);
        usuario = Inicio.deCifrar(ini.getUser().getNombreUsuario());

        initComponents();

        this.setSize((int) ini.getDimension().getWidth(), (int) ini.getDimension().getHeight() - 43);
        this.setLocation(0, 0);

        /*SE instsancian los formularios del menu */
        // new Thread(new HiloCargarFormulariosAuxiliarLogistica(this)).start();
    }

    public PrincipalLogistica(Inicio ini) throws Exception, Throwable {

        //initComponents();
        this.ini = ini;
        usuario = Inicio.deCifrar(ini.getUser().getNombreUsuario());

        if (ini.isEstaClienteActivo()) {
            //if (ya) {
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

            try {
                ini.cargarImagenEscritorio(escritorio);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Revisar archivo de configuracion", "Imagen de Fonfo no encontrada", JOptionPane.ERROR_MESSAGE);
            }

            /*SE instsancian los formularios del menu */
            //  new Thread(new HiloCargarFormulariosAuxiliarLogistica(this)).start();
            /*MÉTODO PARA CAMBIAR LA CLAVE, CUANDO SE INGRSA POR PRIMERA VEZ AL 
            SISTEMA*/
            if (this.ini.getUser().getClaveUsuario().equals(Inicio.cifrar("123456"))) { // cuando el usuario es nuevo
                //this.escritorio.setSize(600, 400);

                CambiarClave fCambiarClave = new CambiarClave(ini);
                if (fCambiarClave != null) {
                    this.escritorio.add(fCambiarClave);
                    fCambiarClave.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));

                    fCambiarClave.setLocation((ini.getDimension().width - fCambiarClave.getSize().width) / 2, (ini.getDimension().height - fCambiarClave.getSize().height) / 2);
                    jMenuBar1.setVisible(false);
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
                    //jMenu7.setVisible(false);
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

        escritorio = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuArchivo = new javax.swing.JMenu();
        menuCambiarClave = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem36 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        mnuVehiculos = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem40 = new javax.swing.JMenuItem();
        jMenuItem68 = new javax.swing.JMenuItem();
        jMenuItem75 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenuItem22 = new javax.swing.JMenuItem();
        mnuDistribucionLogistica = new javax.swing.JMenu();
        mnuManifestarPedidos = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem41 = new javax.swing.JMenuItem();
        jMenuItem31 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        mnuDistribucionLaHielera = new javax.swing.JMenu();
        jMenuItem69 = new javax.swing.JMenuItem();
        mnuManifestarPedidos6 = new javax.swing.JMenuItem();
        mnuManifestarPedidos7 = new javax.swing.JMenuItem();
        mnuDescargueHielera = new javax.swing.JMenuItem();
        jMenuItem72 = new javax.swing.JMenuItem();
        jMenuItem73 = new javax.swing.JMenuItem();
        mnuHabilitarManifiesto = new javax.swing.JMenuItem();
        mnuHabilitarFacturas = new javax.swing.JMenuItem();
        mnuClientes = new javax.swing.JMenu();
        imActualizarClientes = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu15 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem23 = new javax.swing.JMenuItem();
        jMenu36 = new javax.swing.JMenu();
        jMenuItem80 = new javax.swing.JMenuItem();
        jMenuItem35 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenu28 = new javax.swing.JMenu();
        jMenu10 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem24 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem26 = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        mnu_rptFacturacionPorCanal1 = new javax.swing.JMenuItem();
        mnu_rptFacturacionTAT1 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem42 = new javax.swing.JMenuItem();
        jMenuItem81 = new javax.swing.JMenuItem();
        jMenuItem103 = new javax.swing.JMenuItem();
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
            .addGap(0, 493, Short.MAX_VALUE)
        );
        escritorioLayout.setVerticalGroup(
            escritorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 279, Short.MAX_VALUE)
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

        jMenu7.setText("Administracion");

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/group.png"))); // NOI18N
        jMenu4.setText("Personal");

        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/People.png"))); // NOI18N
        jMenuItem11.setText("Personal");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem11);

        jMenuItem36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png"))); // NOI18N
        jMenuItem36.setText("Sincronizar Personal");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem36);

        jMenu7.add(jMenu4);

        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/lorry.png"))); // NOI18N
        jMenuItem12.setText("Vehiculos");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu7.add(jMenuItem12);

        jMenuBar1.add(jMenu7);

        mnuVehiculos.setText("Logistica");
        mnuVehiculos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVehiculosActionPerformed(evt);
            }
        });

        jMenu9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Misc-Upload-Database-icon.png"))); // NOI18N
        jMenu9.setText("Importar Datos");

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

        jMenuItem68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/logo_hielerablue.png"))); // NOI18N
        jMenuItem68.setText("Integrador Hielera");
        jMenuItem68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem68ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem68);

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

        jMenuItem41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Update.png"))); // NOI18N
        jMenuItem41.setText("Modificar Manifiesto");
        jMenuItem41.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem41ActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(jMenuItem41);

        jMenuItem31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Move.png"))); // NOI18N
        jMenuItem31.setText("Traslado de Facturas");
        jMenuItem31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem31ActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(jMenuItem31);

        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem14.setText("Descuentos");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(jMenuItem14);

        jMenuItem17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Red pin.png"))); // NOI18N
        jMenuItem17.setText("Recogidas");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(jMenuItem17);

        mnuVehiculos.add(mnuDistribucionLogistica);

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

        jMenuItem80.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem80.setText("Facturas Descargadas");
        jMenuItem80.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem80ActionPerformed(evt);
            }
        });
        jMenu36.add(jMenuItem80);

        jMenuItem35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem35.setText("De Distribucion");
        jMenuItem35.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem35ActionPerformed(evt);
            }
        });
        jMenu36.add(jMenuItem35);

        jMenu5.add(jMenu36);

        jMenuItem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Search.png"))); // NOI18N
        jMenuItem18.setText("Producto");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem18);

        jMenuBar1.add(jMenu5);

        jMenu8.setText("Reportes");

        jMenu28.setText("Distribucion");

        jMenu10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenu10.setText("Reporte Rechazos");

        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem10.setText("Rechazos Totales");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu10.add(jMenuItem10);

        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem15.setText("RechazosParciales");
        jMenu10.add(jMenuItem15);

        jMenu28.add(jMenu10);

        jMenuItem24.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem24.setText("Reporte por periodo");
        jMenuItem24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem24ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem24);

        jMenuItem25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem25.setText("Facturas Pendientes");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem25);

        jMenuItem26.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem26.setText("Reporte Manifiestos En Distribucion");
        jMenuItem26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem26ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem26);

        jMenu11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenu11.setText("Reportes Facturacion");

        mnu_rptFacturacionPorCanal1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mnu_rptFacturacionPorCanal1.setText("Reporte Canales");
        mnu_rptFacturacionPorCanal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_rptFacturacionPorCanal1ActionPerformed(evt);
            }
        });
        jMenu11.add(mnu_rptFacturacionPorCanal1);

        mnu_rptFacturacionTAT1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mnu_rptFacturacionTAT1.setText("Reporte TAT");
        mnu_rptFacturacionTAT1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_rptFacturacionTAT1ActionPerformed(evt);
            }
        });
        jMenu11.add(mnu_rptFacturacionTAT1);

        jMenu28.add(jMenu11);

        jMenuItem27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem27.setText("Manifiestos sin Descargar");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem27);

        jMenuItem28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem28.setText("Desctos & Recogidas");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem28);

        jMenuItem29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem29.setText("De Distribucion");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem29);

        jMenuItem30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Accounting.png"))); // NOI18N
        jMenuItem30.setText("Descuentos Autorizados");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem30);

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

        jMenuItem103.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem103.setText("Salida de Producto");
        jMenuItem103.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem103ActionPerformed(evt);
            }
        });
        jMenu28.add(jMenuItem103);

        jMenu8.add(jMenu28);

        jMenuBar1.add(jMenu8);

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
            .addComponent(escritorio, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuArchivoActionPerformed
    }//GEN-LAST:event_mnuArchivoActionPerformed

    private void menuCambiarClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCambiarClaveActionPerformed
        try {
            // this.escritorio.setSize(600, 400);
            CambiarClave fCambiarClave = new CambiarClave(ini);
            if (fCambiarClave != null) {
                this.escritorio.add(fCambiarClave);
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
            Logger.getLogger(PrincipalLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuCambiarClaveActionPerformed

    private void mnuVehiculosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVehiculosActionPerformed

    }//GEN-LAST:event_mnuVehiculosActionPerformed

    private void jMenuItem16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem16MouseClicked

    }//GEN-LAST:event_jMenuItem16MouseClicked

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed

       // FImportarArchivoExcel fImportarArchivoExcel = new FImportarArchivoExcel(ini);
         FImportarArchivoExcelPacheco fImportarArchivoExcel = new FImportarArchivoExcelPacheco(ini);
        //fImportarArchivoExcel = new FImportarArchivoExcel(ini);
        this.escritorio.add(fImportarArchivoExcel);
        fImportarArchivoExcel.setLocation(((ini.getDimension().width - fImportarArchivoExcel.getSize().width) / 2), ((ini.getDimension().height - fImportarArchivoExcel.getSize().height) / 2));
        fImportarArchivoExcel.setVisible(true);
        fImportarArchivoExcel.show();
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


    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void mnuManifestarPedidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuManifestarPedidosMouseClicked

    }//GEN-LAST:event_mnuManifestarPedidosMouseClicked

    private void mnuManifestarPedidosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuManifestarPedidosActionPerformed
        try {
            FManifestarPedidosEnRuta fManifestarPedidosEnRuta = new FManifestarPedidosEnRuta(ini);

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
            Logger.getLogger(PrincipalLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_mnuManifestarPedidosActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        try {

            FAnularFacturas fAnularFacturas = new FAnularFacturas(ini);
            if (fAnularFacturas != null) {
                this.escritorio.add(fAnularFacturas);
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
            Logger.getLogger(PrincipalLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void mnuHabilitarManifiestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuHabilitarManifiestoActionPerformed
        try {
            FHabilitarManifiesto fHabilitarManifiesto = new FHabilitarManifiesto(ini);
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
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuHabilitarManifiestoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            FReporteManifiestosConcilidos fManifiestosConciliados = new FReporteManifiestosConcilidos(ini);
            if (fManifiestosConciliados != null) {
                this.escritorio.add(fManifiestosConciliados);
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
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void mnuHabilitarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuHabilitarFacturasActionPerformed
        try {
            FHabilitarFacturas fHabilitarFacturas = new FHabilitarFacturas(ini);
            if (fHabilitarFacturas != null) {
                this.escritorio.add(fHabilitarFacturas);
                fHabilitarFacturas.setLocation(((ini.getDimension().width - fHabilitarFacturas.getSize().width) / 2), ((ini.getDimension().height - fHabilitarFacturas.getSize().height) / 2) - 30);
                fHabilitarFacturas.setVisible(true);
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

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed
        try {
            FManifestarFacturasEnPicking fFacturasEnPicking = new FManifestarFacturasEnPicking(ini);
            if (fFacturasEnPicking != null) {
                this.escritorio.add(fFacturasEnPicking);
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
            Logger.getLogger(PrincipalLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem21ActionPerformed
        try {
            // TODO add your handling code here: fRegistroDePedidosFinLineaDePicking
            FRegistroDePedidosFinLineaDePicking fRegistroDePedidosFinLineaDePicking = new FRegistroDePedidosFinLineaDePicking(ini);

            if (fRegistroDePedidosFinLineaDePicking != null) {
                this.escritorio.add(fRegistroDePedidosFinLineaDePicking);
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
            Logger.getLogger(PrincipalLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem21ActionPerformed

    private void jMenuItem22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem22ActionPerformed

        try {
            FReporteFacturasPendientesEnPicking fReporteFacturasPendientesEnPicking = new FReporteFacturasPendientesEnPicking(ini);
            if (fReporteFacturasPendientesEnPicking != null) {
                this.escritorio.add(fReporteFacturasPendientesEnPicking);
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
    }//GEN-LAST:event_jMenuItem22ActionPerformed

    private void imActualizarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imActualizarClientesActionPerformed

        FGeoreferenciarClientes fGeoreferenciarClientes = new FGeoreferenciarClientes(ini);
        this.escritorio.add(fGeoreferenciarClientes);
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


    }//GEN-LAST:event_imActualizarClientesActionPerformed

    private void jMenuItem40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem40ActionPerformed
        try {
            FIntegrador fIntegrador = new FIntegrador(ini);
            if (fIntegrador != null) {
                this.escritorio.add(fIntegrador);
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

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem40ActionPerformed

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
    }//GEN-LAST:event_jMenuItem23ActionPerformed

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
            Logger.getLogger(PrincipalLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed

        try {
            FConsultarFacturasPorCliente fConsultarFacturasPorCliente = new FConsultarFacturasPorCliente(ini);

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
            Logger.getLogger(PrincipalLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed

        try {
            IngresarCarros fIngresaarCArros = new IngresarCarros(ini);
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
            Logger.getLogger(PrincipalLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed

        try {
            IngresarEmpleados fIngresarEmpleados = new IngresarEmpleados(ini);
            this.escritorio.add(fIngresarEmpleados);
            fIngresarEmpleados.setLocation(((ini.getDimension().width - fIngresarEmpleados.getSize().width) / 2), ((ini.getDimension().height - fIngresarEmpleados.getSize().height) / 2) - 30);
            fIngresarEmpleados.setVisible(true);
            //fReporteDescuentos_Recogidas.cargarInformacion();
            fIngresarEmpleados.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario IngresarEmpleados");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

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
            datos.add(2, "Click Formulario FModificarManifiesto");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem41ActionPerformed

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
            datos.add(2, "Click Formulario FIntegrador");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalLogistica.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem68ActionPerformed

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

            if (fManifestarPedidosHielera == null) {
                fManifestarPedidosHielera = new FManifestarPedidosHielera_2(ini);
                this.escritorio.add(fManifestarPedidosHielera);
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
            if (fControlarSalidaFacturasBarCode2 == null) {
                fControlarSalidaFacturasBarCode2 = new FControlarSalidaFacturasBarCode2(ini);

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
            } else {
                this.escritorio.add(fControlarSalidaFacturasBarCode2);
                fControlarSalidaFacturasBarCode2.setVisible(true);
                fControlarSalidaFacturasBarCode2.show();
            }

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
                datos.add(2, "Click Formulario DescargarFacturas_2");
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
    }//GEN-LAST:event_jMenuItem73ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        try {
            FReporteDeRechazosTotales fReporteRechazosTotales = new FReporteDeRechazosTotales(ini);

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem24ActionPerformed

        try {
            FReportePedidosMovilizadosPorPeriodo fReporteMovilizado = new FReportePedidosMovilizadosPorPeriodo(ini);

            this.escritorio.add(fReporteMovilizado);
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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem24ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
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
          }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem26ActionPerformed
        try {
            FReporteManifiestosEnDistribucion fReporteManifiestosEnDistribucion = new FReporteManifiestosEnDistribucion(ini);

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem26ActionPerformed

    private void mnu_rptFacturacionPorCanal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionPorCanal1ActionPerformed
        try {
            FReporteFacturacionPorCanal fReporteFacturacionPorCanal = new FReporteFacturacionPorCanal(ini);

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnu_rptFacturacionPorCanal1ActionPerformed

    private void mnu_rptFacturacionTAT1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionTAT1ActionPerformed
        try {
            FReporteFacturacionTAT fReporteFacturacionTAT = new FReporteFacturacionTAT(ini);

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnu_rptFacturacionTAT1ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed

        try {
            FReporteManifiestosSinDescargar freporteManifiestosSinDescargar = new FReporteManifiestosSinDescargar(ini);

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem27ActionPerformed

    private void jMenuItem28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem28ActionPerformed

    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem29ActionPerformed
        FReportePedidosEnDistribucion fReportePedidosEnDistribucion = new FReportePedidosEnDistribucion(ini);
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem29ActionPerformed

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
            datos.add(2, "Click Formulario FReporteGeneralDeDescargueDeRutas");
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
            datos.add(2, "Click Formulario FIntegradorExcelFile");
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

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        JOptionPane.showMessageDialog(this, "Servidor Local " + ini.getServerLocal() + "\n BBDD " + ini.getBdLocal(), ini.getServerLocal(), JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        JOptionPane.showMessageDialog(this, "Servidor Remoto " + ini.getServerRemota() + "\n BBDD " + ini.getBdRemota(), ini.getServerRemota(), JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        JOptionPane.showMessageDialog(this, "Nombre de Cliente " + ini.getPropiedades().getProperty("nombreCliente"), "Nombre de Cliente ", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem34ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        FIngresarDescuentoClientes fIngresarDescuentoClientes = new FIngresarDescuentoClientes(ini);
        this.escritorio.add(fIngresarDescuentoClientes);
        fIngresarDescuentoClientes.setLocation(((ini.getDimension().width - fIngresarDescuentoClientes.getSize().width) / 2), ((ini.getDimension().height - fIngresarDescuentoClientes.getSize().height) / 2) - 35);
        //form1.setMaximizable(false);
        fIngresarDescuentoClientes.setVisible(true);
        fIngresarDescuentoClientes.show();
        datos = new ArrayList<>();
        datos.add(0, usuario);
        datos.add(1, formulario);
        datos.add(2, "Click Formulario FIngresarDescuentoClientes");
        datos.add(3, "CURRENT_TIMESTAMP");
        new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        if (timer.isRunning()) {
            timer.stop();
        }
        timer.start();

    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        FIngresarRecogidasClientes fIngresarRecogidasClientes = new FIngresarRecogidasClientes(ini);
        this.escritorio.add(fIngresarRecogidasClientes);
        fIngresarRecogidasClientes.setLocation(((ini.getDimension().width - fIngresarRecogidasClientes.getSize().width) / 2), ((ini.getDimension().height - fIngresarRecogidasClientes.getSize().height) / 2) - 35);
        //form1.setMaximizable(false);
        fIngresarRecogidasClientes.setVisible(true);
        fIngresarRecogidasClientes.show();
        datos = new ArrayList<>();
        datos.add(0, usuario);
        datos.add(1, formulario);
        datos.add(2, "Click Formulario FIngresarRecogidasClientes");
        datos.add(3, "CURRENT_TIMESTAMP");
        new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        if (timer.isRunning()) {
            timer.stop();
        }
        timer.start();

    }//GEN-LAST:event_jMenuItem17ActionPerformed

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

    private void jMenuItem31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem31ActionPerformed
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
    }//GEN-LAST:event_jMenuItem31ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed

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
    }//GEN-LAST:event_jMenuItem18ActionPerformed

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

    private void jMenuItem35ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem35ActionPerformed
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
    }//GEN-LAST:event_jMenuItem35ActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed
        /* Se inicia un integrador con TNS que va a actualizar todos los
        empleados*/

        new Thread(new HiloIntegradorTNS(ini, jMenuItem36)).start();
    }//GEN-LAST:event_jMenuItem36ActionPerformed

    private void jMenuItem103ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem103ActionPerformed
        try {
            FReporteAlertasSalidaDeProductos  fReporteAlertasSalidaDeProductos = new FReporteAlertasSalidaDeProductos(ini);
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
            java.util.logging.Logger.getLogger(PrincipalLogistica.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
                    new PrincipalLogistica().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(PrincipalLogistica.class.getName()).log(Level.SEVERE, null, ex);
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
    public javax.swing.JDesktopPane escritorio;
    private javax.swing.JMenuItem imActualizarClientes;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu28;
    private javax.swing.JMenu jMenu36;
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
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem31;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem35;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem40;
    private javax.swing.JMenuItem jMenuItem41;
    private javax.swing.JMenuItem jMenuItem42;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem68;
    private javax.swing.JMenuItem jMenuItem69;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem72;
    private javax.swing.JMenuItem jMenuItem73;
    private javax.swing.JMenuItem jMenuItem75;
    private javax.swing.JMenuItem jMenuItem80;
    private javax.swing.JMenuItem jMenuItem81;
    private javax.swing.JMenuItem menuCambiarClave;
    private javax.swing.JMenu mnuArchivo;
    private javax.swing.JMenu mnuClientes;
    private javax.swing.JMenuItem mnuDescargueHielera;
    private javax.swing.JMenu mnuDistribucionLaHielera;
    private javax.swing.JMenu mnuDistribucionLogistica;
    private javax.swing.JMenuItem mnuHabilitarFacturas;
    private javax.swing.JMenuItem mnuHabilitarManifiesto;
    private javax.swing.JMenuItem mnuManifestarPedidos;
    private javax.swing.JMenuItem mnuManifestarPedidos6;
    private javax.swing.JMenuItem mnuManifestarPedidos7;
    private javax.swing.JMenu mnuVehiculos;
    private javax.swing.JMenuItem mnu_rptFacturacionPorCanal1;
    private javax.swing.JMenuItem mnu_rptFacturacionTAT1;
    // End of variables declaration//GEN-END:variables

    private void timerEvent(java.awt.event.ActionEvent evt) {
        if (timer.isRunning()) {
            timer.stop();
            System.out.println("se detiene el timer " + new Date());
            //System.exit(0);
//            timer = new Timer(60000, actionListener);
//            System.out.println("se llama al timer");
//            timer.start();
//            System.out.println("inicia el timer");
//            System.out.println("expired en funcion");
        }
    }
}
