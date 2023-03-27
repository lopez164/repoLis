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
import aplicacionlogistica.costumerService.Reportes.FReporteDescuentos_Recogidas;
import aplicacionlogistica.distribucion.clientes.FGeoreferenciarClientes;
import aplicacionlogistica.distribucion.consultas.FConsultaManifiestosSinDescargar;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasPorCliente;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultaPedidosDescargadosPorConductorHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarPedidosConductorHielera;
import aplicacionlogistica.distribucion.formularios.ReporteManifiestosSinDescargar.FReporteManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarEmpleados;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteDeRechazosTotales;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteFacturasSinMovimientos;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteGeneralDeDescargueDeRutas;
import aplicacionlogistica.distribucion.formularios.reportes.FReportePedidosEnDistribucion;
import aplicacionlogistica.distribucion.formularios.reportes.FReportePedidosMovilizadosPorPeriodo;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteManifiestosEnDistribucion.FReporteManifiestosEnDistribucion;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteMovilizadoPorConductor.FReportemovilizadoPorConductor;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteovilizadoPorTodosLosConductores.FReportemovilizadoPorTodosLosConductores;
import aplicacionlogistica.distribucion.formularios.reportes.facturacion.FReporteFacturacionPorCanal;
import aplicacionlogistica.distribucion.formularios.reportes.facturacion.FReporteFacturacionTAT;
import mtto.personal.FReporteDelPersonal;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author VLI_488
 */
public class PrincipalAsistenteAdministrativo extends javax.swing.JFrame {

    public Inicio ini = null;

    public FConsultaManifiestosDescargados fConsultaManifiestosDescargados = null;

    List<String> datos = new ArrayList<>();
    String usuario;
    String formulario = "PrincipalAsistenteAdministrativo";

    public PrincipalAsistenteAdministrativo() {
        initComponents();

    }

    public PrincipalAsistenteAdministrativo(Inicio ini) throws Exception, Throwable {

        //initComponents();
        this.ini = ini;
        usuario = Inicio.deCifrar(ini.getUser().getNombreUsuario());
        if (ini.isEstaClienteActivo()) {

            initComponents();

            Properties propiedades = ini.getPropiedades();

            try {
                ini.cargarImagenEscritorio(escritorio);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Revisar archivo de configuracion", "Imagen de Fonfo no encontrada", JOptionPane.ERROR_MESSAGE);
            }

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

            this.setSize(screenSize);
            this.setLocation(0, 0);
            this.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos());

            /*MÉTODO PARA CAMBIAR LA CLAVE, CUANDO SE INGRSA POR PRIMERA VEZ AL 
            SISTEMA*/
            if (this.ini.getUser().getClaveUsuario().equals(Inicio.cifrar("123456"))) { // cuando el usuario es nuevo
                //this.escritorio.setSize(600, 400);
                CambiarClave form = new CambiarClave(ini);
                form.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
                this.escritorio.add(form);
                form.setLocation((screenSize.width - form.getSize().width) / 2, (screenSize.height - form.getSize().height) / 2);
                jMenuBar1.setVisible(false);
                form.setResizable(false);
                form.show();
                form.txtClaveAnterior.requestFocus();
                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario CambiarClave");
                datos.add(3, "CURRENT_TIMESTAMP");
                 new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

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
        jMenu4 = new javax.swing.JMenu();
        mnuPersonal = new javax.swing.JMenu();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem29 = new javax.swing.JMenuItem();
        jMenu21 = new javax.swing.JMenu();
        jMenu25 = new javax.swing.JMenu();
        jMenuItem36 = new javax.swing.JMenuItem();
        mnuClientes = new javax.swing.JMenu();
        jMenu22 = new javax.swing.JMenu();
        jMenu33 = new javax.swing.JMenu();
        imActualizarCliente = new javax.swing.JMenuItem();
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
        jMenu5 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        mnu_rptFacturacionPorCanal = new javax.swing.JMenuItem();
        mnu_rptFacturacionTAT = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem25 = new javax.swing.JMenuItem();
        jMenuItem42 = new javax.swing.JMenuItem();
        jMenuItem39 = new javax.swing.JMenuItem();

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

        jMenu4.setText("Personal");

        mnuPersonal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/group.png"))); // NOI18N
        mnuPersonal.setText("Personal");

        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/People.png"))); // NOI18N
        jMenuItem12.setText("Personal");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        mnuPersonal.add(jMenuItem12);

        jMenuItem29.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem29.setText("Reporte de personal");
        jMenuItem29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem29ActionPerformed(evt);
            }
        });
        mnuPersonal.add(jMenuItem29);

        jMenu4.add(mnuPersonal);

        jMenu21.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/lorry.png"))); // NOI18N
        jMenu21.setText("Vehículos");

        jMenu25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/page_green.png"))); // NOI18N
        jMenu25.setText("Documentos");

        jMenuItem36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem36.setText("Ingresar Documentos");
        jMenuItem36.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem36ActionPerformed(evt);
            }
        });
        jMenu25.add(jMenuItem36);

        jMenu21.add(jMenu25);

        jMenu4.add(jMenu21);

        jMenuBar1.add(jMenu4);

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

        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenu5.setText("Reporte Rechazos");

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem5.setText("Rechazos Totales");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem5);

        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem8.setText("RechazosParciales");
        jMenu5.add(jMenuItem8);

        mnuReportes1.add(jMenu5);

        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem14.setText("Reporte por periodo");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        mnuReportes1.add(jMenuItem14);

        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem15.setText("Facturas sin Movimiento");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        mnuReportes1.add(jMenuItem15);

        jMenuItem16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem16.setText("Reporte Manifiestos En Distribucion");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        mnuReportes1.add(jMenuItem16);

        jMenu6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenu6.setText("Reportes Facturacion");

        mnu_rptFacturacionPorCanal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mnu_rptFacturacionPorCanal.setText("Reporte Canales");
        mnu_rptFacturacionPorCanal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_rptFacturacionPorCanalActionPerformed(evt);
            }
        });
        jMenu6.add(mnu_rptFacturacionPorCanal);

        mnu_rptFacturacionTAT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mnu_rptFacturacionTAT.setText("Reporte TAT");
        mnu_rptFacturacionTAT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_rptFacturacionTATActionPerformed(evt);
            }
        });
        jMenu6.add(mnu_rptFacturacionTAT);

        mnuReportes1.add(jMenu6);

        jMenuItem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem18.setText("Manifiestos sin Descargar");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        mnuReportes1.add(jMenuItem18);

        jMenuItem19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem19.setText("Desctos & Recogidas");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        mnuReportes1.add(jMenuItem19);

        jMenuItem25.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem25.setText("De Distribucion");
        jMenuItem25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem25ActionPerformed(evt);
            }
        });
        mnuReportes1.add(jMenuItem25);

        jMenuItem42.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem42.setText("Reporte general Descargue Rutas");
        jMenuItem42.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem42ActionPerformed(evt);
            }
        });
        mnuReportes1.add(jMenuItem42);

        jMenuItem39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem39.setText("Documentos Flota");
        jMenuItem39.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem39ActionPerformed(evt);
            }
        });
        mnuReportes1.add(jMenuItem39);

        jMenuBar1.add(mnuReportes1);

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
            // this.escritorio.setSize(600, 400);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            CambiarClave form = new CambiarClave(ini);
            this.escritorio.add(form);
            form.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
            form.setLocation((screenSize.width - form.getSize().width) / 2, (screenSize.height - form.getSize().height) / 2);
            form.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario CambiarClave");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            form.txtClaveAnterior.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuCambiarClaveActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
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
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            FReportemovilizadoPorConductor form1 = new FReportemovilizadoPorConductor(this.ini);

            this.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2) - 30);
            form1.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FReportemovilizadoPorConductor");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            FReportemovilizadoPorTodosLosConductores form1 = new FReportemovilizadoPorTodosLosConductores(this.ini);

            this.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2) - 30);
            form1.show();

            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FReportemovilizadoPorTodosLosConductores");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void imActualizarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imActualizarClienteActionPerformed
        FGeoreferenciarClientes fGeoreferenciarClientes = new FGeoreferenciarClientes(ini);

        if (fGeoreferenciarClientes != null) {
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
        }
    }//GEN-LAST:event_imActualizarClienteActionPerformed

    private void mnuVehiculos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVehiculos1ActionPerformed

    }//GEN-LAST:event_mnuVehiculos1ActionPerformed

    private void mnuConsultarFacturasClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConsultarFacturasClienteActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            FConsultarFacturasPorCliente form1 = new FConsultarFacturasPorCliente(this.ini);

            this.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2) - 30);
            form1.show();

            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FConsultarFacturasPorCliente");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuConsultarFacturasClienteActionPerformed

    private void mnuConsultarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConsultarFacturaActionPerformed
        try {
            FConsultarFacturasRemoto fConsultarFacturasRemoto = new FConsultarFacturasRemoto(this.ini);

            this.escritorio.add(fConsultarFacturasRemoto);
            fConsultarFacturasRemoto.setLocation(((ini.getDimension().width - fConsultarFacturasRemoto.getSize().width) / 2), ((ini.getDimension().height - fConsultarFacturasRemoto.getSize().height) / 2) - 30);
            fConsultarFacturasRemoto.show();

            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click Formulario FConsultarFacturasRemoto");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuConsultarFacturaActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed

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
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed

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
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed

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
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

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
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem29ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
        try {
            FReporteDeRechazosTotales fReporteRechazosTotales = new FReporteDeRechazosTotales(ini);
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
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed

        try {
            FReportePedidosMovilizadosPorPeriodo fReporteMovilizado = new FReportePedidosMovilizadosPorPeriodo(ini);
            if (fReporteMovilizado != null) {
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
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed

        try {
            FReporteFacturasSinMovimientos fReporteFacturasSinMovimientos = new FReporteFacturasSinMovimientos(ini);
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fReporteFacturasSinMovimientos != null) {
                this.escritorio.add(fReporteFacturasSinMovimientos);
                fReporteFacturasSinMovimientos.setLocation(((ini.getDimension().width - fReporteFacturasSinMovimientos.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturasSinMovimientos.getSize().height) / 2) - 30);
                fReporteFacturasSinMovimientos.setVisible(true);
                fReporteFacturasSinMovimientos.show();

                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteFacturasSinMovimientos");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        try {
            FReporteManifiestosEnDistribucion fReporteManifiestosEnDistribucion = new FReporteManifiestosEnDistribucion(ini);
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

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void mnu_rptFacturacionPorCanalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionPorCanalActionPerformed
        try {
            FReporteFacturacionPorCanal fReporteFacturacionPorCanal = new FReporteFacturacionPorCanal(ini);
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
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnu_rptFacturacionPorCanalActionPerformed

    private void mnu_rptFacturacionTATActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionTATActionPerformed
        try {
            FReporteFacturacionTAT fReporteFacturacionTAT = new FReporteFacturacionTAT(ini);

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

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnu_rptFacturacionTATActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed

        try {
            FReporteManifiestosSinDescargar freporteManifiestosSinDescargar = new FReporteManifiestosSinDescargar(ini);
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

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed

        try {
            FReporteDescuentos_Recogidas fReporteDescuentos_Recogidas = new FReporteDescuentos_Recogidas(ini);
            if (fReporteDescuentos_Recogidas != null) {
                this.escritorio.add(fReporteDescuentos_Recogidas);
                fReporteDescuentos_Recogidas.setLocation(((ini.getDimension().width - fReporteDescuentos_Recogidas.getSize().width) / 2), ((ini.getDimension().height - fReporteDescuentos_Recogidas.getSize().height) / 2) - 30);
                fReporteDescuentos_Recogidas.setVisible(true);
                //fReporteDescuentos_Recogidas.cargarInformacion();
                fReporteDescuentos_Recogidas.show();

                datos = new ArrayList<>();
                datos.add(0, usuario);
                datos.add(1, formulario);
                datos.add(2, "Click Formulario FReporteDescuentos_Recogidas");
                datos.add(3, "CURRENT_TIMESTAMP");

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem25ActionPerformed
        FReportePedidosEnDistribucion fReportePedidosEnDistribucion = new FReportePedidosEnDistribucion(ini);
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

            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem25ActionPerformed

    private void jMenuItem36ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem36ActionPerformed

    }//GEN-LAST:event_jMenuItem36ActionPerformed

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
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem42ActionPerformed

    private void jMenuItem39ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem39ActionPerformed

    }//GEN-LAST:event_jMenuItem39ActionPerformed

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioConsultas.class.getName()).log(Level.SEVERE, null, ex);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem80ActionPerformed

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
            java.util.logging.Logger.getLogger(PrincipalAsistenteAdministrativo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new PrincipalAsistenteAdministrativo().setVisible(true);
            }
        });
    }

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"));
        return retValue;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JDesktopPane escritorio;
    private javax.swing.JMenuItem imActualizarCliente;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu21;
    private javax.swing.JMenu jMenu22;
    private javax.swing.JMenu jMenu25;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu33;
    private javax.swing.JMenu jMenu36;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    public static javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem19;
    private javax.swing.JMenuItem jMenuItem25;
    private javax.swing.JMenuItem jMenuItem29;
    private javax.swing.JMenuItem jMenuItem36;
    private javax.swing.JMenuItem jMenuItem39;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem42;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem79;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem80;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem menuCambiarClave;
    private javax.swing.JMenu mnuArchivo;
    private javax.swing.JMenu mnuClientes;
    private javax.swing.JMenuItem mnuConsultarFactura;
    private javax.swing.JMenuItem mnuConsultarFacturasCliente;
    private javax.swing.JMenu mnuPersonal;
    private javax.swing.JMenu mnuReportes1;
    private javax.swing.JMenu mnuVehiculos1;
    private javax.swing.JMenuItem mnu_rptFacturacionPorCanal;
    private javax.swing.JMenuItem mnu_rptFacturacionTAT;
    // End of variables declaration//GEN-END:variables
}
