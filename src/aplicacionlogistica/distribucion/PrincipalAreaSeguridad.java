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
import aplicacionlogistica.costumerService.FConsultaManifiestosDescargados;
import aplicacionlogistica.distribucion.consultas.FConsultaManifiestosSinDescargar;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasPorCliente;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.consultas.FHabilitarManifiesto;
import aplicacionlogistica.distribucion.formularios.FAdicionarPedidosNoReportados;
import aplicacionlogistica.distribucion.formularios.FAnularFacturaSinMovimiento;
import aplicacionlogistica.distribucion.formularios.Hielera.FControlarSalidaFacturas;
import aplicacionlogistica.distribucion.formularios.Hielera.DescargarFacturas_2;
import aplicacionlogistica.distribucion.formularios.Hielera.FControlarSalidaFacturasBarCode2;
import aplicacionlogistica.distribucion.formularios.Hielera.FDespachoHieleraConIntegracion;
import aplicacionlogistica.distribucion.formularios.Hielera.FManifestarPedidosHielera_2;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultaPedidosDescargadosPorConductorHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarPedidosConductorHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarProducto;
import aplicacionlogistica.distribucion.formularios.ReporteManifiestosSinDescargar.FReporteManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.administracion.FBorrarFacturaManifiesto;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarCarros;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarEmpleados;
import aplicacionlogistica.distribucion.formularios.reportes.FDashBoardFacturas;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteDeRechazosTotales;
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
import aplicacionlogistica.distribucion.integrador.FIntegrador;
import aplicacionlogistica.distribucion.integrador.FIntegradorExcelFile;
import aplicacionlogistica.picking.FManifestarFacturasEnPicking;
import aplicacionlogistica.picking.FRegistroDePedidosFinLineaDePicking;
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

/**
 *
 * @author VLI_488
 */
public class PrincipalAreaSeguridad extends javax.swing.JFrame {

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
    FControlarSalidaFacturasBarCode2 fControlarSalidaFacturasBarCode2 = null;
    DescargarFacturas_2 descargarFacturas_2 = null;

    String rutaImagenDeFondo = null;

    List<String> datos = new ArrayList<>();
    String usuario;
    String formulario = "PrincipalAreaSeguridad";
    ActionListener actionListener;
    public Timer timer;

    public PrincipalAreaSeguridad() throws Exception {
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

    public PrincipalAreaSeguridad(Inicio ini) throws Exception, Throwable {

        //initComponents();
        this.ini = ini;
        String ruta;

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
             FDashBoardFacturas   fDashBoardFacturas = new FDashBoardFacturas(ini);
            this.escritorio.add(fDashBoardFacturas);
            fDashBoardFacturas.setVisible(true);
            fDashBoardFacturas.show();

                ini.cargarImagenEscritorio(escritorio);
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
                    fCambiarClave.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));

                    fCambiarClave.setLocation((ini.getDimension().width - fCambiarClave.getSize().width) / 2, (ini.getDimension().height - fCambiarClave.getSize().height) / 2);
                    jMenuBar1.setVisible(false);
                    fCambiarClave.setResizable(false);
                    fCambiarClave.show();
                    fCambiarClave.setVisible(true);
                    fCambiarClave.txtClaveAnterior.requestFocus();
                }

            } else {

            }

            if (ini.getPropiedades().getProperty("idOperador").equals("1")) {
                mnuDistribucionLogistica.setVisible(true);
                mnuDistribucionLaHielera.setVisible(false);
            }
            if (ini.getPropiedades().getProperty("idOperador").equals("2")) {
                mnuDistribucionLogistica.setVisible(false);
                mnuDistribucionLaHielera.setVisible(true);
            }
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
        jMenuItem1 = new javax.swing.JMenuItem();
        mnuVehiculos = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem40 = new javax.swing.JMenuItem();
        jMenuItem75 = new javax.swing.JMenuItem();
        mnuDistribucionLogistica = new javax.swing.JMenu();
        mnuDescargarFacturas = new javax.swing.JMenuItem();
        mnuHabilitarManifiesto = new javax.swing.JMenuItem();
        mnuHabilitarFacturas = new javax.swing.JMenuItem();
        mnuDistribucionLaHielera = new javax.swing.JMenu();
        jMenuItem68 = new javax.swing.JMenuItem();
        mnuManifestarPedidos7 = new javax.swing.JMenuItem();
        jMenuItem74 = new javax.swing.JMenuItem();
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
            .addGap(0, 287, Short.MAX_VALUE)
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

        jMenuItem1.setText("manifestar");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        mnuArchivo.add(jMenuItem1);

        jMenuBar1.add(mnuArchivo);

        mnuVehiculos.setText("Logistica");
        mnuVehiculos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVehiculosActionPerformed(evt);
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

        mnuVehiculos.add(jMenu9);

        mnuDistribucionLogistica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        mnuDistribucionLogistica.setText("distribucion");

        mnuDescargarFacturas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        mnuDescargarFacturas.setText("Descargar Facturas");
        mnuDescargarFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDescargarFacturasActionPerformed(evt);
            }
        });
        mnuDistribucionLogistica.add(mnuDescargarFacturas);

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

        jMenuItem74.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Go back.png"))); // NOI18N
        jMenuItem74.setText("Descargue Hielera");
        jMenuItem74.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem74ActionPerformed(evt);
            }
        });
        mnuDistribucionLaHielera.add(jMenuItem74);

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

        mnuReportes.add(jMenu28);

        jMenuBar1.add(mnuReportes);

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
            this.escritorio.add(fCambiarClave);
            fCambiarClave.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
            fCambiarClave.setLocation((ini.getDimension().width - fCambiarClave.getSize().width) / 2, (ini.getDimension().height - fCambiarClave.getSize().height) / 2);
            fCambiarClave.show();
            datos = new ArrayList<>();
            fCambiarClave.setVisible(true);
            fCambiarClave.txtClaveAnterior.requestFocus();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario CambiarClave");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuCambiarClaveActionPerformed

    private void mnuVehiculosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVehiculosActionPerformed

    }//GEN-LAST:event_mnuVehiculosActionPerformed

    private void jMenuItem16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem16MouseClicked

    }//GEN-LAST:event_jMenuItem16MouseClicked

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        FImportarArchivoExcel fImportarExcel = new FImportarArchivoExcel(ini);

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

    }//GEN-LAST:event_jMenuItem16ActionPerformed

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
            datos.add(2, "Click Formulario DescargarFacturas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
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
            datos.add(2, "Click Formulario FReporteManifiestosMovilizadosPorConductor");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
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
            datos.add(2, "Click Formulario FReporteDeRechazosTotales");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
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
            datos.add(2, "Click Formulario FConsultarFacturasPorCliente");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
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
            datos.add(2, "Click Formulario FReportePedidosMovilizadosPorPeriodo");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
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
            datos.add(2, "Click Formulario FConsultarManifiestos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
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
            datos.add(2, "Click Formulario FHabilitarManifiesto");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuHabilitarManifiestoActionPerformed

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
            datos.add(2, "Click Formulario FHabilitarFacturas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
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
            datos.add(2, "Click Formulario FReporteManifiestosEnDistribucion");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
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
            datos.add(2, "Click Formulario FReporteFacturacionPorCanal");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
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
            datos.add(2, "Click Formulario FReporteFacturacionTAT");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
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
            datos.add(2, "Click Formulario FReporteManifiestosSinDescargar");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem18ActionPerformed

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

    }//GEN-LAST:event_imActualizarClientesActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed

    }//GEN-LAST:event_jMenuItem17ActionPerformed

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
            datos.add(2, "Click Formulario FReportePedidosEnDistribucion");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed
//
//        try {
//            FReporteClientesConDescuento fReporteClientesConDescuento = new FReporteClientesConDescuento(ini);
//            if (fReporteClientesConDescuento != null) {
//                this.escritorio.add(fReporteClientesConDescuento);
//                fReporteClientesConDescuento.setLocation(((ini.getDimension().width - fReporteClientesConDescuento.getSize().width) / 2), ((ini.getDimension().height - fReporteClientesConDescuento.getSize().height) / 2) - 30);
//                fReporteClientesConDescuento.setVisible(true);
//                fReporteClientesConDescuento.cargarInformacion();
//                fReporteClientesConDescuento.show();  datos = new ArrayList<>();

//datos.add(0, usuario);
//                datos.add(1,formulario);
//                datos.add(2, "Click Formulario FReporteClientesConDescuento");
//                datos.add(3, "CURRENT_TIMESTAMP");
//                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem32ActionPerformed
        JOptionPane.showMessageDialog(this, "Servidor Local " + ini.getServerLocal() + "\n BBDD " + ini.getBdLocal(), ini.getServerLocal(), JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem32ActionPerformed

    private void jMenuItem33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem33ActionPerformed
        JOptionPane.showMessageDialog(this, "Servidor Remoto " + ini.getServerRemota() + "\n BBDD " + ini.getBdRemota(), ini.getServerRemota(), JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem33ActionPerformed

    private void jMenuItem34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem34ActionPerformed
        JOptionPane.showMessageDialog(this, "Nombre de Cliente " + ini.getPropiedades().getProperty("nombreCliente"), "Nombre de Cliente ", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_jMenuItem34ActionPerformed

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
            datos.add(2, "Click Formulario FIntegrador");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem40ActionPerformed

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem42ActionPerformed

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem68ActionPerformed

    private void mnuManifestarPedidos7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuManifestarPedidos7MouseClicked

    private void mnuManifestarPedidos7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuManifestarPedidos7ActionPerformed
        try {
//            if (fControlarSalidaFacturasBarCode2 == null) {
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

//            } else {
//                this.escritorio.add(fControlarSalidaFacturasBarCode2);
//                fControlarSalidaFacturasBarCode2.setVisible(true);
//                fControlarSalidaFacturasBarCode2.show();
//
//            }

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:    // TODO add your handling code here:
    }//GEN-LAST:event_mnuManifestarPedidos7ActionPerformed

    private void jMenuItem74ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem74ActionPerformed
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
            } else {
                this.escritorio.add(descargarFacturas_2);
                descargarFacturas_2.setVisible(true);
                descargarFacturas_2.show();

            }

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jMenuItem74ActionPerformed

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
        }// TO// TO
    }//GEN-LAST:event_jMenuItem75ActionPerformed

    private void jMenuItem79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem79ActionPerformed
        try {
            FConsultarPedidosConductorHielera fConsultarPedidosConductorHielera = new FConsultarPedidosConductorHielera(ini);
            this.escritorio.add(fConsultarPedidosConductorHielera);
            fConsultarPedidosConductorHielera.setLocation(((ini.getDimension().width - fConsultarPedidosConductorHielera.getSize().width) / 2), ((ini.getDimension().height - fConsultarPedidosConductorHielera.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            fConsultarPedidosConductorHielera.setVisible(true);
            fConsultarPedidosConductorHielera.show();
            datos = new ArrayList<>();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FConsultarPedidosConductorHielera");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

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
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  fConsultaPedidosDescargadosPorConductorHielera");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioConsultas.class.getName()).log(Level.SEVERE, null, ex);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem80ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            FDespachoHieleraConIntegracion fManifestarPedidosHielera_2 = new FDespachoHieleraConIntegracion(this.ini);

            this.escritorio.add(fManifestarPedidosHielera_2);
            fManifestarPedidosHielera_2.setLocation(((ini.getDimension().width - fManifestarPedidosHielera_2.getSize().width) / 2), ((ini.getDimension().height - fManifestarPedidosHielera_2.getSize().height) / 2) - 30);
            fManifestarPedidosHielera_2.show();
            datos = new ArrayList<>();
            datos = new ArrayList<>();

            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  fManifestarPedidosHielera_2");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioConsultas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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
            java.util.logging.Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                    new PrincipalAreaSeguridad().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(PrincipalAreaSeguridad.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JDesktopPane escritorio;
    private javax.swing.JMenuItem imActualizarClientes;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu28;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu36;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu9;
    public static javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem23;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem32;
    private javax.swing.JMenuItem jMenuItem33;
    private javax.swing.JMenuItem jMenuItem34;
    private javax.swing.JMenuItem jMenuItem40;
    private javax.swing.JMenuItem jMenuItem42;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem68;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem74;
    private javax.swing.JMenuItem jMenuItem75;
    private javax.swing.JMenuItem jMenuItem79;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem80;
    private javax.swing.JMenuItem jMenuItem81;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem menuCambiarClave;
    private javax.swing.JMenu mnuArchivo;
    private javax.swing.JMenu mnuClientes;
    private javax.swing.JMenuItem mnuConsultarManifiestos;
    private javax.swing.JMenuItem mnuDescargarFacturas;
    private javax.swing.JMenu mnuDistribucionLaHielera;
    private javax.swing.JMenu mnuDistribucionLogistica;
    private javax.swing.JMenuItem mnuHabilitarFacturas;
    private javax.swing.JMenuItem mnuHabilitarManifiesto;
    private javax.swing.JMenuItem mnuManifestarPedidos7;
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
//        new Thread(new HiloListadoDeEmpleados(this.ini)).start();
//        
//              
//        new Thread(new HiloListadoDeDepartamentos(this.ini)).start();
//        new Thread(new HiloListadoDeAgencias(this.ini)).start();
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
