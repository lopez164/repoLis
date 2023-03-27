/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion;

import aplicacionlogistica.configuracion.CambiarClave;
import aplicacionlogistica.configuracion.HiloBitacoraMovimientosEnElSistema;
import aplicacionlogistica.distribucion.consultas.FConsultarManifiestos;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FConsultaManifiestosDescargados;
import aplicacionlogistica.costumerService.Reportes.FReporteClientesConDescuento;
import aplicacionlogistica.costumerService.Reportes.FReporteDescuentos_Recogidas;
import aplicacionlogistica.distribucion.clientes.FGeoreferenciarClientes;
import aplicacionlogistica.distribucion.consultas.FConsultaManifiestosSinDescargar;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasPorCliente;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.formularios.FAnularFacturaSinMovimiento;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultaPedidosDescargadosPorConductorHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarPedidosConductorHielera;
import aplicacionlogistica.distribucion.formularios.ReporteManifiestosSinDescargar.FReporteManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.administracion.FBorrarFacturaManifiesto;
import aplicacionlogistica.distribucion.formularios.administracion.FDeshabilitarManifiestoAbierto;
import aplicacionlogistica.distribucion.formularios.reportes.FDashBoardFacturas;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteAlertasSalidaDeProductos;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteDeRechazosTotales;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteFacturasPendientes;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteManifiestosConcilidos;
import aplicacionlogistica.distribucion.formularios.reportes.FReportePedidosMovilizadosPorPeriodo;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteManifiestosEnDistribucion.FReporteManifiestosEnDistribucion;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteMovilizadoPorConductor.FReportemovilizadoPorConductor;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteovilizadoPorTodosLosConductores.FReportemovilizadoPorTodosLosConductores;
import aplicacionlogistica.distribucion.formularios.reportes.facturacion.FReporteFacturacionPorCanal;
import aplicacionlogistica.distribucion.formularios.reportes.facturacion.FReporteFacturacionTAT;
import aplicacionlogistica.picking.FReporteFacturasPendientesEnPicking;
import java.awt.Dimension;
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
public class PrincipalUsuarioAuditoria extends javax.swing.JFrame {

    public Inicio ini = null;
    public FReporteManifiestosConcilidos fManifiestosConciliados = null;
    public FReporteDeRechazosTotales fReporteRechazosTotales = null;
    public FReportePedidosMovilizadosPorPeriodo fReporteMovilizado = null;
    public FReporteManifiestosEnDistribucion fReporteManifiestosEnDistribucion = null;
    public FReporteFacturasPendientes fReporteFacturasPendientes = null;
    public FReporteFacturacionPorCanal fReporteFacturacionPorCanal = null;
    public FReporteFacturacionTAT fReporteFacturacionTAT = null;
    public FReporteManifiestosSinDescargar freporteManifiestosSinDescargar = null;
    public FBorrarFacturaManifiesto fDesvincularFactura = null;
    public FReporteFacturasPendientesEnPicking fReporteFacturasPendientesEnPicking = null;
    public FReporteDescuentos_Recogidas fReporteDescuentos_Recogidas = null;

    List<String> datos = new ArrayList<>();
    String usuario;
    String formulario = "PrincipalUsuarioAuditoria";

    public Timer timer;
    ActionListener actionListener;

    public FConsultaManifiestosDescargados fConsultaManifiestosDescargados = null;

    public PrincipalUsuarioAuditoria() {
        initComponents();

    }

    public PrincipalUsuarioAuditoria(Inicio ini) throws Exception, Throwable {

        //initComponents();
        this.ini = ini;
        usuario = Inicio.deCifrar(ini.getUser().getNombreUsuario());
        if (ini.isEstaClienteActivo()) {

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
                FDashBoardFacturas fDashBoardFacturas = new FDashBoardFacturas(ini);
                this.escritorio.add(fDashBoardFacturas);
                fDashBoardFacturas.setVisible(true);
                fDashBoardFacturas.show();
                ini.cargarImagenEscritorio(escritorio);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Revisar archivo de configuracion", "Imagen de Fonfo no encontrada", JOptionPane.ERROR_MESSAGE);
            }

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            /*MÉTODO PARA CAMBIAR LA CLAVE, CUANDO SE INGRSA POR PRIMERA VEZ AL 
            SISTEMA*/
            if (this.ini.getUser().getClaveUsuario().equals(Inicio.cifrar("123456"))) { // cuando el usuario es nuevo
                //PrincipalUsuarioAuditoria.escritorio.setSize(600, 400);
                CambiarClave form = new CambiarClave(ini);
                form.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
                PrincipalUsuarioAuditoria.escritorio.add(form);
                form.setLocation((screenSize.width - form.getSize().width) / 2, (screenSize.height - form.getSize().height) / 2);
                jMenuBar1.setVisible(false);
                form.setResizable(false);
                form.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Inicio primera vez, cambio de Clave");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

                form.txtClaveAnterior.requestFocus();

            }
        } else {
            JOptionPane.showMessageDialog(this, "Estimado usuario la licencia ha caducado, consulte con el administrador", "Cliente no activo", 1);
            System.exit(0);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        escritorio = new javax.swing.JDesktopPane();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuArchivo = new javax.swing.JMenu();
        menuCambiarClave = new javax.swing.JMenuItem();
        jMenuItem6 = new javax.swing.JMenuItem();
        mnuVehiculos1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        mnuConsultarFacturasCliente = new javax.swing.JMenuItem();
        mnuConsultarFactura = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenu15 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu36 = new javax.swing.JMenu();
        jMenuItem79 = new javax.swing.JMenuItem();
        jMenuItem80 = new javax.swing.JMenuItem();
        mnuReportes1 = new javax.swing.JMenu();
        mnuRechazos = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        mItmRptPorPeriodo = new javax.swing.JMenuItem();
        mItmRptFacturasSinMovimieto = new javax.swing.JMenuItem();
        mItmRptManifiestosEnDistribucion = new javax.swing.JMenuItem();
        mnuRptFacturacion = new javax.swing.JMenu();
        mnu_rptFacturacionPorCanal = new javax.swing.JMenuItem();
        mnu_rptFacturacionTAT = new javax.swing.JMenuItem();
        mItmRptMamifiestosSinDescargar = new javax.swing.JMenuItem();
        mItmRptDescuentosyRecogidas = new javax.swing.JMenuItem();
        jMenuItem30 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem81 = new javax.swing.JMenuItem();
        jMenuItem103 = new javax.swing.JMenuItem();
        mnuClientes = new javax.swing.JMenu();
        jMenu22 = new javax.swing.JMenu();
        jMenu33 = new javax.swing.JMenu();
        imActualizarCliente = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());
        setIconImages(getIconImages());

        escritorio.setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);

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

        mnuVehiculos1.setText("Consultas");
        mnuVehiculos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVehiculos1ActionPerformed(evt);
            }
        });

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenu2.setText("Consultar");

        mnuConsultarFacturasCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mnuConsultarFacturasCliente.setText("Facturas Por Cliente");
        mnuConsultarFacturasCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConsultarFacturasClienteActionPerformed(evt);
            }
        });
        jMenu2.add(mnuConsultarFacturasCliente);

        mnuConsultarFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/find.png"))); // NOI18N
        mnuConsultarFactura.setText("Consultar Factura");
        mnuConsultarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConsultarFacturaActionPerformed(evt);
            }
        });
        jMenu2.add(mnuConsultarFactura);

        mnuVehiculos1.add(jMenu2);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenu3.setText("Informes Conductores");

        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/user.png"))); // NOI18N
        jMenuItem10.setText("Por Conductor");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem10);

        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/group.png"))); // NOI18N
        jMenuItem11.setText("Todos conductores");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem11);

        mnuVehiculos1.add(jMenu3);

        jMenu15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        jMenu15.setText("Manifiestos");

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem4.setText("De Distribucion");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem4);

        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem9.setText("Descargados");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem9);

        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem7.setText("Sin Descargar");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem7);

        mnuVehiculos1.add(jMenu15);

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

        mnuVehiculos1.add(jMenu36);

        jMenuBar1.add(mnuVehiculos1);

        mnuReportes1.setText("Reportes");

        mnuRechazos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mnuRechazos.setText("Reporte Rechazos");

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem5.setText("Rechazos Totales");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        mnuRechazos.add(jMenuItem5);

        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem8.setText("RechazosParciales");
        mnuRechazos.add(jMenuItem8);

        mnuReportes1.add(mnuRechazos);

        mItmRptPorPeriodo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mItmRptPorPeriodo.setText("Reporte por periodo");
        mItmRptPorPeriodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmRptPorPeriodoActionPerformed(evt);
            }
        });
        mnuReportes1.add(mItmRptPorPeriodo);

        mItmRptFacturasSinMovimieto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mItmRptFacturasSinMovimieto.setText("Facturas Pendientes");
        mItmRptFacturasSinMovimieto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmRptFacturasSinMovimietoActionPerformed(evt);
            }
        });
        mnuReportes1.add(mItmRptFacturasSinMovimieto);

        mItmRptManifiestosEnDistribucion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mItmRptManifiestosEnDistribucion.setText("Reporte Manifiestos En Distribucion");
        mItmRptManifiestosEnDistribucion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmRptManifiestosEnDistribucionActionPerformed(evt);
            }
        });
        mnuReportes1.add(mItmRptManifiestosEnDistribucion);

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

        mnuReportes1.add(mnuRptFacturacion);

        mItmRptMamifiestosSinDescargar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mItmRptMamifiestosSinDescargar.setText("Manifiestos sin Descargar");
        mItmRptMamifiestosSinDescargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmRptMamifiestosSinDescargarActionPerformed(evt);
            }
        });
        mnuReportes1.add(mItmRptMamifiestosSinDescargar);

        mItmRptDescuentosyRecogidas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mItmRptDescuentosyRecogidas.setText("Desctos & Recogidas");
        mItmRptDescuentosyRecogidas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mItmRptDescuentosyRecogidasActionPerformed(evt);
            }
        });
        mnuReportes1.add(mItmRptDescuentosyRecogidas);

        jMenuItem30.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Accounting.png"))); // NOI18N
        jMenuItem30.setText("Descuentos Autorizados");
        jMenuItem30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem30ActionPerformed(evt);
            }
        });
        mnuReportes1.add(jMenuItem30);

        jMenuItem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem18.setText("Desctos & Recogidas");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        mnuReportes1.add(jMenuItem18);

        jMenuItem81.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem81.setText("Mov X Conductor por periodo");
        jMenuItem81.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem81ActionPerformed(evt);
            }
        });
        mnuReportes1.add(jMenuItem81);

        jMenuItem103.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem103.setText("Salida de Producto");
        jMenuItem103.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem103ActionPerformed(evt);
            }
        });
        mnuReportes1.add(jMenuItem103);

        jMenuBar1.add(mnuReportes1);

        mnuClientes.setText("Clientes");

        jMenu22.setText("Rutero");
        mnuClientes.add(jMenu22);

        jMenu33.setText("Rutas");
        mnuClientes.add(jMenu33);

        imActualizarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Update.png"))); // NOI18N
        imActualizarCliente.setText("actualizar Cliente");
        imActualizarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imActualizarClienteActionPerformed(evt);
            }
        });
        mnuClientes.add(imActualizarCliente);

        jMenuBar1.add(mnuClientes);

        jMenu1.setText("Tareas");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delete.png"))); // NOI18N
        jMenuItem1.setText("Anular Facturas");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delete.png"))); // NOI18N
        jMenuItem2.setText("Deshabilitar Manifiesto");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 283, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuArchivoActionPerformed
    }//GEN-LAST:event_mnuArchivoActionPerformed

    private void menuCambiarClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCambiarClaveActionPerformed
        try {
            // PrincipalUsuarioAuditoria.escritorio.setSize(600, 400);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            CambiarClave form = new CambiarClave(ini);
            PrincipalUsuarioAuditoria.escritorio.add(form);
            form.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
            form.setLocation((screenSize.width - form.getSize().width) / 2, (screenSize.height - form.getSize().height) / 2);
            form.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  CambiarClave");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            form.txtClaveAnterior.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuCambiarClaveActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        try {
            FConsultarManifiestos form1 = new FConsultarManifiestos(ini);

            PrincipalUsuarioAuditoria.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2));
            form1.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FConsultarManifiestos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            FReportemovilizadoPorConductor form1 = new FReportemovilizadoPorConductor(this.ini);

            PrincipalUsuarioAuditoria.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2) - 30);
            form1.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReportemovilizadoPorConductor");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            FReportemovilizadoPorTodosLosConductores form1 = new FReportemovilizadoPorTodosLosConductores(this.ini);

            PrincipalUsuarioAuditoria.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2) - 30);
            form1.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FReportemovilizadoPorTodosLosConductores");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void imActualizarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imActualizarClienteActionPerformed
        FGeoreferenciarClientes fGeoreferenciarClientes = new FGeoreferenciarClientes(ini);

        if (fGeoreferenciarClientes != null) {
            PrincipalUsuarioAuditoria.escritorio.add(fGeoreferenciarClientes);
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
        }
    }//GEN-LAST:event_imActualizarClienteActionPerformed

    private void mnuVehiculos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVehiculos1ActionPerformed

    }//GEN-LAST:event_mnuVehiculos1ActionPerformed

    private void mnuConsultarFacturasClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConsultarFacturasClienteActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            FConsultarFacturasPorCliente form1 = new FConsultarFacturasPorCliente(this.ini);

            PrincipalUsuarioAuditoria.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2) - 30);
            form1.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FConsultarFacturasPorCliente");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuConsultarFacturasClienteActionPerformed

    private void mnuConsultarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConsultarFacturaActionPerformed
        try {
            FConsultarFacturasRemoto fConsultarFacturasRemoto = new FConsultarFacturasRemoto(this.ini);

            PrincipalUsuarioAuditoria.escritorio.add(fConsultarFacturasRemoto);
            fConsultarFacturasRemoto.setLocation(((ini.getDimension().width - fConsultarFacturasRemoto.getSize().width) / 2), ((ini.getDimension().height - fConsultarFacturasRemoto.getSize().height) / 2) - 30);
            fConsultarFacturasRemoto.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FConsultarFacturasRemoto");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuConsultarFacturaActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed

        try {
            fConsultaManifiestosDescargados = new FConsultaManifiestosDescargados(this.ini);

            PrincipalUsuarioAuditoria.escritorio.add(fConsultaManifiestosDescargados);
            fConsultaManifiestosDescargados.setLocation(((ini.getDimension().width - fConsultaManifiestosDescargados.getSize().width) / 2), ((ini.getDimension().height - fConsultaManifiestosDescargados.getSize().height) / 2) - 30);
            fConsultaManifiestosDescargados.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FConsultaManifiestosDescargados");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed

        try {
            FConsultaManifiestosSinDescargar fConsultaManifiestosSinDescargar = new FConsultaManifiestosSinDescargar(this.ini);

            PrincipalUsuarioAuditoria.escritorio.add(fConsultaManifiestosSinDescargar);
            fConsultaManifiestosSinDescargar.setLocation(((ini.getDimension().width - fConsultaManifiestosSinDescargar.getSize().width) / 2), ((ini.getDimension().height - fConsultaManifiestosSinDescargar.getSize().height) / 2) - 30);
            fConsultaManifiestosSinDescargar.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FConsultaManifiestosSinDescargar");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {

            FAnularFacturaSinMovimiento fAnularFacturaSinMovimiento = new FAnularFacturaSinMovimiento(ini);
            PrincipalUsuarioAuditoria.escritorio.add(fAnularFacturaSinMovimiento);
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
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        try {
            fReporteRechazosTotales = new FReporteDeRechazosTotales(ini);
            if (fReporteRechazosTotales != null) {
                PrincipalUsuarioAuditoria.escritorio.add(fReporteRechazosTotales);
                fReporteRechazosTotales.setLocation(((ini.getDimension().width - fReporteRechazosTotales.getSize().width) / 2), ((ini.getDimension().height - fReporteRechazosTotales.getSize().height) / 2));
                fReporteRechazosTotales.setVisible(true);
                fReporteRechazosTotales.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click en form  FReporteDeRechazosTotales");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void mItmRptPorPeriodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmRptPorPeriodoActionPerformed
        try {
            fReporteMovilizado = new FReportePedidosMovilizadosPorPeriodo(ini);
            if (fReporteMovilizado != null) {
                PrincipalUsuarioAuditoria.escritorio.add(fReporteMovilizado);
                fReporteMovilizado.setLocation(((ini.getDimension().width - fReporteMovilizado.getSize().width) / 2), ((ini.getDimension().height - fReporteMovilizado.getSize().height) / 2) - 30);
                fReporteMovilizado.setVisible(true);
                fReporteMovilizado.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click en form  FReportePedidosMovilizadosPorPeriodo");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmRptPorPeriodoActionPerformed

    private void mItmRptFacturasSinMovimietoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmRptFacturasSinMovimietoActionPerformed

        try {
            fReporteFacturasPendientes = new FReporteFacturasPendientes(ini);
            if (fReporteFacturasPendientes != null) {
                PrincipalUsuarioAuditoria.escritorio.add(fReporteFacturasPendientes);
                fReporteFacturasPendientes.setLocation(((ini.getDimension().width - fReporteFacturasPendientes.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturasPendientes.getSize().height) / 2) - 30);
                fReporteFacturasPendientes.setVisible(true);
                fReporteFacturasPendientes.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click en form  FReporteFacturasPendientes");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmRptFacturasSinMovimietoActionPerformed

    private void mItmRptManifiestosEnDistribucionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmRptManifiestosEnDistribucionActionPerformed
        try {
            fReporteManifiestosEnDistribucion = new FReporteManifiestosEnDistribucion(ini);
            if (fReporteManifiestosEnDistribucion != null) {
                PrincipalUsuarioAuditoria.escritorio.add(fReporteManifiestosEnDistribucion);
                fReporteManifiestosEnDistribucion.setLocation(((ini.getDimension().width - fReporteManifiestosEnDistribucion.getSize().width) / 2), ((ini.getDimension().height - fReporteManifiestosEnDistribucion.getSize().height) / 2) - 30);
                fReporteManifiestosEnDistribucion.setVisible(true);
                fReporteManifiestosEnDistribucion.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click en form  FReporteManifiestosEnDistribucion");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmRptManifiestosEnDistribucionActionPerformed

    private void mnu_rptFacturacionPorCanalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionPorCanalActionPerformed
        try {
            fReporteFacturacionPorCanal = new FReporteFacturacionPorCanal(ini);
            if (fReporteFacturacionPorCanal != null) {
                PrincipalUsuarioAuditoria.escritorio.add(fReporteFacturacionPorCanal);
                fReporteFacturacionPorCanal.setLocation(((ini.getDimension().width - fReporteFacturacionPorCanal.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturacionPorCanal.getSize().height) / 2) - 30);
                fReporteFacturacionPorCanal.setVisible(true);
                fReporteFacturacionPorCanal.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click en form  FReporteFacturacionPorCanal");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnu_rptFacturacionPorCanalActionPerformed

    private void mnu_rptFacturacionTATActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionTATActionPerformed
        try {
            fReporteFacturacionTAT = new FReporteFacturacionTAT(ini);
            if (fReporteFacturacionTAT != null) {
                PrincipalUsuarioAuditoria.escritorio.add(fReporteFacturacionTAT);
                fReporteFacturacionTAT.setLocation(((ini.getDimension().width - fReporteFacturacionTAT.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturacionTAT.getSize().height) / 2) - 30);
                fReporteFacturacionTAT.setVisible(true);
                fReporteFacturacionTAT.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click en form  FReporteFacturacionTAT");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnu_rptFacturacionTATActionPerformed

    private void mItmRptMamifiestosSinDescargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmRptMamifiestosSinDescargarActionPerformed

        try {
            freporteManifiestosSinDescargar = new FReporteManifiestosSinDescargar(ini);
            if (freporteManifiestosSinDescargar != null) {
                PrincipalUsuarioAuditoria.escritorio.add(freporteManifiestosSinDescargar);
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
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmRptMamifiestosSinDescargarActionPerformed

    private void mItmRptDescuentosyRecogidasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmRptDescuentosyRecogidasActionPerformed

        try {
            fReporteDescuentos_Recogidas = new FReporteDescuentos_Recogidas(ini);
            if (fReporteDescuentos_Recogidas != null) {
                PrincipalUsuarioAuditoria.escritorio.add(fReporteDescuentos_Recogidas);
                fReporteDescuentos_Recogidas.setLocation(((ini.getDimension().width - fReporteDescuentos_Recogidas.getSize().width) / 2), ((ini.getDimension().height - fReporteDescuentos_Recogidas.getSize().height) / 2) - 30);
                fReporteDescuentos_Recogidas.setVisible(true);
                //fReporteDescuentos_Recogidas.cargarInformacion();
                fReporteDescuentos_Recogidas.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click en form  FReporteDescuentos_Recogidas");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmRptDescuentosyRecogidasActionPerformed

    private void jMenuItem30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem30ActionPerformed

        try {
            FReporteClientesConDescuento fReporteClientesConDescuento = new FReporteClientesConDescuento(ini);
            if (fReporteClientesConDescuento != null) {
                PrincipalUsuarioAuditoria.escritorio.add(fReporteClientesConDescuento);
                fReporteClientesConDescuento.setLocation(((ini.getDimension().width - fReporteClientesConDescuento.getSize().width) / 2), ((ini.getDimension().height - fReporteClientesConDescuento.getSize().height) / 2) - 30);
                fReporteClientesConDescuento.setVisible(true);
                fReporteClientesConDescuento.cargarInformacion();
                fReporteClientesConDescuento.show();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click en form  FReporteClientesConDescuento");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem30ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            FDeshabilitarManifiestoAbierto fdeshabilitarManifiestoAbierto = new FDeshabilitarManifiestoAbierto(ini);
            PrincipalUsuarioAuditoria.escritorio.add(fdeshabilitarManifiestoAbierto);
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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem79ActionPerformed
        try {
            FConsultarPedidosConductorHielera fConsultarPedidosConductorHielera = new FConsultarPedidosConductorHielera(ini);
            PrincipalUsuarioAuditoria.escritorio.add(fConsultarPedidosConductorHielera);
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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem79ActionPerformed

    private void jMenuItem80ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem80ActionPerformed

        try {
            FConsultaPedidosDescargadosPorConductorHielera fConsultaPedidosDescargadosPorConductorHielera = new FConsultaPedidosDescargadosPorConductorHielera(this.ini);

            PrincipalUsuarioAuditoria.escritorio.add(fConsultaPedidosDescargadosPorConductorHielera);
            fConsultaPedidosDescargadosPorConductorHielera.setLocation(((ini.getDimension().width - fConsultaPedidosDescargadosPorConductorHielera.getSize().width) / 2), ((ini.getDimension().height - fConsultaPedidosDescargadosPorConductorHielera.getSize().height) / 2) - 30);
            fConsultaPedidosDescargadosPorConductorHielera.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FConsultaPedidosDescargadosPorConductorHielera");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(Level.SEVERE, null, ex);
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
            java.util.logging.Logger.getLogger(PrincipalUsuarioAuditoria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PrincipalUsuarioAuditoria().setVisible(true);
            }
        });
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
    public static javax.swing.JDesktopPane escritorio;
    private javax.swing.JMenuItem imActualizarCliente;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu22;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu33;
    private javax.swing.JMenu jMenu36;
    public static javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem103;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem30;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem79;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem80;
    private javax.swing.JMenuItem jMenuItem81;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem mItmRptDescuentosyRecogidas;
    private javax.swing.JMenuItem mItmRptFacturasSinMovimieto;
    private javax.swing.JMenuItem mItmRptMamifiestosSinDescargar;
    private javax.swing.JMenuItem mItmRptManifiestosEnDistribucion;
    private javax.swing.JMenuItem mItmRptPorPeriodo;
    private javax.swing.JMenuItem menuCambiarClave;
    private javax.swing.JMenu mnuArchivo;
    private javax.swing.JMenu mnuClientes;
    private javax.swing.JMenuItem mnuConsultarFactura;
    private javax.swing.JMenuItem mnuConsultarFacturasCliente;
    private javax.swing.JMenu mnuRechazos;
    private javax.swing.JMenu mnuReportes1;
    private javax.swing.JMenu mnuRptFacturacion;
    private javax.swing.JMenu mnuVehiculos1;
    private javax.swing.JMenuItem mnu_rptFacturacionPorCanal;
    private javax.swing.JMenuItem mnu_rptFacturacionTAT;
    // End of variables declaration//GEN-END:variables
}
