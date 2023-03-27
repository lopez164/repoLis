/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService;

import GPS.Monitoreo.PuntosMonitoreo;
import GPS.PuntosGps_rutas.PuntosGps_rutas;
import aplicacionlogistica.configuracion.CambiarClave;
import aplicacionlogistica.distribucion.consultas.FConsultarManifiestos;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.Reportes.FReporteClientesConDescuento;
import aplicacionlogistica.costumerService.Reportes.FReporteDescuentos_Recogidas;
import aplicacionlogistica.costumerService.Threads.HiloListadoDeTiposDeGestiones;
import aplicacionlogistica.costumerService.Threads.HiloListadoDeTiposDePeticones;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeCausalesdeDevolucion;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeEmpleados;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeMovimientosManifiestosfacturas;
import aplicacionlogistica.distribucion.clientes.FGeoreferenciarClientes;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasPorCliente;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.consultas.graficos.FgraficoPedidosMovilizados;
import aplicacionlogistica.distribucion.consultas.graficos.FgraficoPorDia;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteDeRechazosTotales;
import aplicacionlogistica.distribucion.formularios.reportes.FReportePedidosMovilizadosPorPeriodo;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteManifiestosEnDistribucion.FReporteManifiestosEnDistribucion;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteMovilizadoPorConductor.FReportemovilizadoPorConductor;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteovilizadoPorTodosLosConductores.FReportemovilizadoPorTodosLosConductores;
import aplicacionlogistica.distribucion.integrador.FIntegrador;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author VLI_488
 */
public class PrincipalUsuarioCostumerService extends javax.swing.JFrame {

    public Inicio ini = null;
    FGeoreferenciarClientes fGeoreferenciarClientes;
    FIngresarDescuentoClientes fIngresarDescuentoClientes;
    FIngresarRecogidasClientes fIngresarRecogidasClientes;
    FReporteClientesConDescuento fReporteClientesConDescuento;

    public FReporteDescuentos_Recogidas fReporteDescuentos_Recogidas = null;


    /*Metodo constructor para pruebas*/
    public PrincipalUsuarioCostumerService() throws Exception {
        String rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
        ini = new Inicio(new File(rutaDeArchivo + "ReadUs.ini"));

        CUsuarios user = new CUsuarios(ini, "79481522");
        ini.setUser(user);
        //if (ini.isClienteActivo()) {
        if (true) {

            initComponents();
            
            iniciarVariables();
         
        

            fGeoreferenciarClientes = new FGeoreferenciarClientes(ini);
            fIngresarDescuentoClientes = new FIngresarDescuentoClientes(ini);
            fIngresarRecogidasClientes = new FIngresarRecogidasClientes(ini);
            fReporteDescuentos_Recogidas = new FReporteDescuentos_Recogidas(ini);

            this.setSize((int) ini.getDimension().getWidth(), (int) ini.getDimension().getHeight() - 43);

            this.setLocation(0, 0);
            this.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos());

            /*MÉTODO PARA CAMBIAR LA CLAVE, CUANDO SE INGRSA POR PRIMERA VEZ AL 
            SISTEMA*/
            if (this.ini.getUser().getClaveUsuario().equals(Inicio.cifrar("123456"))) { // cuando el usuario es nuevo
                //this.escritorio.setSize(600, 400);
                CambiarClave form = new CambiarClave(ini);
                form.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
                this.escritorio.add(form);
                form.setLocation((ini.getDimension().width - form.getSize().width) / 2, (ini.getDimension().height - form.getSize().height) / 2);
                jMenuBar1.setVisible(false);
                form.setResizable(false);
                form.show();
                form.txtClaveAnterior.requestFocus();

            }
        } else {
            JOptionPane.showMessageDialog(this, "Estimado usuario la licencia ha caducado, consulte con el administrador", "Cliente no activo", 1);
            System.exit(0);
        }

        this.setSize((int) ini.getDimension().getWidth(), (int) ini.getDimension().getHeight() - 43);
        this.setLocation(0, 0);

    }

   

    public PrincipalUsuarioCostumerService(Inicio ini) throws Exception, Throwable {

        this.ini = ini;
        new Thread(new HiloListadoDeTiposDePeticones(this.ini)).start();
        new Thread(new HiloListadoDeTiposDeGestiones(this.ini)).start();

        if (ini.isClienteActivo()) {

            initComponents();

            iniciarVariables();
            
            try {
                ini.cargarImagenEscritorio(escritorio);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Revisar archivo de configuracion", "Imagen de Fonfo no encontrada", JOptionPane.ERROR_MESSAGE);
            }

            fGeoreferenciarClientes = new FGeoreferenciarClientes(ini);
            fIngresarDescuentoClientes = new FIngresarDescuentoClientes(ini);
            fIngresarRecogidasClientes = new FIngresarRecogidasClientes(ini);
            fReporteDescuentos_Recogidas = new FReporteDescuentos_Recogidas(ini);

            this.setSize((int) ini.getDimension().getWidth(), (int) ini.getDimension().getHeight() - 43);

            this.setLocation(0, 0);
            this.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos());

            /*MÉTODO PARA CAMBIAR LA CLAVE, CUANDO SE INGRSA POR PRIMERA VEZ AL SISTEMA*/
            if (this.ini.getUser().getClaveUsuario().equals(Inicio.cifrar("123456"))) { // cuando el usuario es nuevo
                //this.escritorio.setSize(600, 400);
                CambiarClave form = new CambiarClave(ini);
                form.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
                this.escritorio.add(form);
                form.setLocation((ini.getDimension().width - form.getSize().width) / 2, (ini.getDimension().height - form.getSize().height) / 2);
                jMenuBar1.setVisible(false);
                form.setResizable(false);
                form.show();
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
        mnuGraficos = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        mnuDocumentos = new javax.swing.JMenu();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem15 = new javax.swing.JMenuItem();
        imActualizarCliente = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenuItem40 = new javax.swing.JMenuItem();
        jMenuItem68 = new javax.swing.JMenuItem();
        mnuVehiculos = new javax.swing.JMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        jMenu14 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem19 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        mnuReportes = new javax.swing.JMenu();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        mnuConsultarFacturasCliente = new javax.swing.JMenuItem();
        mnuConsultarFactura = new javax.swing.JMenuItem();
        mnuReporteMovilizado = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu17 = new javax.swing.JMenu();
        jMenuItem104 = new javax.swing.JMenuItem();
        jMenuItem105 = new javax.swing.JMenuItem();

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

        mnuGraficos.setText("Graficos");

        jMenuItem4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/3d bar chart.png"))); // NOI18N
        jMenuItem4.setText("Movilizado por periodo");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        mnuGraficos.add(jMenuItem4);

        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/3d bar chart.png"))); // NOI18N
        jMenuItem9.setText("Movilizado Diario");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        mnuGraficos.add(jMenuItem9);

        jMenuBar1.add(mnuGraficos);

        mnuDocumentos.setText("Documentos");

        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem14.setText("Descuentos");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        mnuDocumentos.add(jMenuItem14);

        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Red pin.png"))); // NOI18N
        jMenuItem15.setText("Recogidas");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        mnuDocumentos.add(jMenuItem15);

        imActualizarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/map.png"))); // NOI18N
        imActualizarCliente.setText("actualizar Cliente");
        imActualizarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imActualizarClienteActionPerformed(evt);
            }
        });
        mnuDocumentos.add(imActualizarCliente);

        jMenuItem16.setText("jMenuItem16");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        mnuDocumentos.add(jMenuItem16);

        jMenuItem40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Sync.png"))); // NOI18N
        jMenuItem40.setText("Integrador");
        jMenuItem40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem40ActionPerformed(evt);
            }
        });
        mnuDocumentos.add(jMenuItem40);

        jMenuItem68.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/logo_hielerablue.png"))); // NOI18N
        jMenuItem68.setText("Integrador Hielera");
        jMenuItem68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem68ActionPerformed(evt);
            }
        });
        mnuDocumentos.add(jMenuItem68);

        jMenuBar1.add(mnuDocumentos);

        mnuVehiculos.setText("Consultas");
        mnuVehiculos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVehiculosActionPerformed(evt);
            }
        });

        jMenuItem20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem20.setText("Facturas Por Cliente");
        jMenuItem20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem20ActionPerformed(evt);
            }
        });
        mnuVehiculos.add(jMenuItem20);

        jMenu14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Delivery.png"))); // NOI18N
        jMenu14.setText("distribucion");

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenuItem1.setText("Consultar Manifiestos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem1);

        jMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/find.png"))); // NOI18N
        jMenuItem5.setText("Consultar Factura");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem5);

        mnuVehiculos.add(jMenu14);

        jMenu3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Text preview.png"))); // NOI18N
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

        mnuVehiculos.add(jMenu3);

        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Text.png"))); // NOI18N
        jMenuItem7.setText("Manifiestos");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        mnuVehiculos.add(jMenuItem7);

        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Load.png"))); // NOI18N
        jMenuItem8.setText("Descargue Devoluciones");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        mnuVehiculos.add(jMenuItem8);

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/People.png"))); // NOI18N
        jMenu4.setText("Incidencias CS");

        jMenuItem19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/New.png"))); // NOI18N
        jMenuItem19.setText("Crear Incidencia");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem19);

        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Address book.png"))); // NOI18N
        jMenuItem12.setText("Incidencias");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem12);

        mnuVehiculos.add(jMenu4);

        jMenuBar1.add(mnuVehiculos);

        mnuReportes.setText("Reportes");

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenu1.setText("Consultas Rechazos");
        jMenu1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu1ActionPerformed(evt);
            }
        });

        jMenuItem2.setText("Rechazos Totales");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("RechazosParciales");
        jMenu1.add(jMenuItem3);

        mnuReportes.add(jMenu1);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Find.png"))); // NOI18N
        jMenu2.setText("Consultar");

        mnuConsultarFacturasCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Text preview.png"))); // NOI18N
        mnuConsultarFacturasCliente.setText("Facturas Por Cliente");
        mnuConsultarFacturasCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConsultarFacturasClienteActionPerformed(evt);
            }
        });
        jMenu2.add(mnuConsultarFacturasCliente);

        mnuConsultarFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/find.png"))); // NOI18N
        mnuConsultarFactura.setText("Consultar Factura");
        jMenu2.add(mnuConsultarFactura);

        mnuReportes.add(jMenu2);

        mnuReporteMovilizado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        mnuReporteMovilizado.setText("Reporte por periodo");
        mnuReporteMovilizado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuReporteMovilizadoActionPerformed(evt);
            }
        });
        mnuReportes.add(mnuReporteMovilizado);

        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem13.setText("Reporte Manifiestos En Distribucion");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        mnuReportes.add(jMenuItem13);

        jMenuItem17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jMenuItem17.setText("Desctos & Recogidas");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        mnuReportes.add(jMenuItem17);

        jMenuItem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Accounting.png"))); // NOI18N
        jMenuItem18.setText("Descuentos Autorizados");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        mnuReportes.add(jMenuItem18);

        jMenuBar1.add(mnuReportes);

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
            .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuArchivoActionPerformed
    }//GEN-LAST:event_mnuArchivoActionPerformed

    private void menuCambiarClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCambiarClaveActionPerformed
        try {
            // this.escritorio.setSize(600, 400);

            CambiarClave form = new CambiarClave(ini);
            this.escritorio.add(form);
            form.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
            form.setLocation((ini.getDimension().width - form.getSize().width) / 2, (ini.getDimension().height - form.getSize().height) / 2);
            form.show();
            form.txtClaveAnterior.requestFocus();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuCambiarClaveActionPerformed

    private void mnuVehiculosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVehiculosActionPerformed

    }//GEN-LAST:event_mnuVehiculosActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            FConsultarManifiestos form1 = new FConsultarManifiestos(ini);

            PrincipalUsuarioCostumerService.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2));
            form1.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            FReporteDeRechazosTotales form1 = new FReporteDeRechazosTotales(ini);

            PrincipalUsuarioCostumerService.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2));
            form1.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void mnuConsultarFacturasClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConsultarFacturasClienteActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            FConsultarFacturasPorCliente form1 = new FConsultarFacturasPorCliente(this.ini);

            this.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2) - 30);
            form1.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuConsultarFacturasClienteActionPerformed

    private void jMenu1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu1ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed

        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            FgraficoPedidosMovilizados form1 = new FgraficoPedidosMovilizados(this.ini);

            this.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2) - 30);
            form1.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void mnuReporteMovilizadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuReporteMovilizadoActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            FReportePedidosMovilizadosPorPeriodo form1 = new FReportePedidosMovilizadosPorPeriodo(this.ini);

            this.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2) - 30);
            form1.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuReporteMovilizadoActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            FgraficoPorDia form1 = new FgraficoPorDia(this.ini);

            this.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2) - 30);
            form1.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            FReportemovilizadoPorConductor form1 = new FReportemovilizadoPorConductor(this.ini);

            this.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2) - 30);
            form1.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
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
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem12ActionPerformed
        // TODO add your handling code here:
        try {

            FIncidenciasSvC fIncidenciasSvC = new FIncidenciasSvC(this);

            this.escritorio.add(fIncidenciasSvC);
            //JOptionPane.showMessageDialog(this,"width=" + screenSize.width +" heigh= "+ screenSize.height , "Cliente no activo", 1);
            fIncidenciasSvC.setSize(ini.getDimension().width, ini.getDimension().height);

            fIncidenciasSvC.setLocation(0, 0);

            //form1.setLocation(((screenSize.width - form1.getSize().width) / 2), ((screenSize.height - form1.getSize().height) / 2) - 30);
            fIncidenciasSvC.show();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem12ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            FConsultarManifiestos form1 = new FConsultarManifiestos(this.ini);

            this.escritorio.add(form1);
            form1.setSize(ini.getDimension().width, ini.getDimension().height - 70);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2) - 30);
            form1.setLocation(0, 0);
            form1.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            FDescargarDevoluciones form1 = new FDescargarDevoluciones(this.ini);
            this.escritorio.add(form1);
            form1.setSize(ini.getDimension().width, ini.getDimension().height - 70);
            form1.setLocation(0, 0);
            //form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2) - 30);
            form1.show();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem13ActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            FReporteManifiestosEnDistribucion form1 = new FReporteManifiestosEnDistribucion(this.ini);

            this.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2) - 30);
            form1.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

    private void imActualizarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imActualizarClienteActionPerformed
        if (fGeoreferenciarClientes != null) {
            this.escritorio.add(fGeoreferenciarClientes);
            fGeoreferenciarClientes.setLocation(((ini.getDimension().width - fGeoreferenciarClientes.getSize().width) / 2), ((ini.getDimension().height - fGeoreferenciarClientes.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            fGeoreferenciarClientes.setVisible(true);
            fGeoreferenciarClientes.show();
        }
    }//GEN-LAST:event_imActualizarClienteActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        if (fIngresarDescuentoClientes != null) {
            this.escritorio.add(fIngresarDescuentoClientes);
            fIngresarDescuentoClientes.setLocation(((ini.getDimension().width - fIngresarDescuentoClientes.getSize().width) / 2), ((ini.getDimension().height - fIngresarDescuentoClientes.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            fIngresarDescuentoClientes.setVisible(true);
            fIngresarDescuentoClientes.show();
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed
        if (fIngresarRecogidasClientes != null) {
            this.escritorio.add(fIngresarRecogidasClientes);
            fIngresarRecogidasClientes.setLocation(((ini.getDimension().width - fIngresarRecogidasClientes.getSize().width) / 2), ((ini.getDimension().height - fIngresarRecogidasClientes.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            fIngresarRecogidasClientes.setVisible(true);
            fIngresarRecogidasClientes.show();
        }

    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed

        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fReporteDescuentos_Recogidas != null) {
                this.escritorio.add(fReporteDescuentos_Recogidas);
                fReporteDescuentos_Recogidas.setLocation(((ini.getDimension().width - fReporteDescuentos_Recogidas.getSize().width) / 2), ((ini.getDimension().height - fReporteDescuentos_Recogidas.getSize().height) / 2) - 30);
                fReporteDescuentos_Recogidas.setVisible(true);
                //fReporteDescuentos_Recogidas.cargarInformacion();
                fReporteDescuentos_Recogidas.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem16ActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed

        try {
            fReporteClientesConDescuento = new FReporteClientesConDescuento(ini);
            if (fReporteClientesConDescuento != null) {
                this.escritorio.add(fReporteClientesConDescuento);
                fReporteClientesConDescuento.setLocation(((ini.getDimension().width - fReporteClientesConDescuento.getSize().width) / 2), ((ini.getDimension().height - fReporteClientesConDescuento.getSize().height) / 2) - 30);
                fReporteClientesConDescuento.setVisible(true);
                fReporteClientesConDescuento.cargarInformacion();
                fReporteClientesConDescuento.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
       

          // TODO add your handling code here:
        try {

            FGestionarIncidencias fSolucionarIncidencias = new FGestionarIncidencias(ini);

            this.escritorio.add(fSolucionarIncidencias);
            //JOptionPane.showMessageDialog(this,"width=" + screenSize.width +" heigh= "+ screenSize.height , "Cliente no activo", 1);
            fSolucionarIncidencias.setSize(ini.getDimension().width, ini.getDimension().height);

            fSolucionarIncidencias.setLocation(0, 0);

            //form1.setLocation(((screenSize.width - form1.getSize().width) / 2), ((screenSize.height - form1.getSize().height) / 2) - 30);
            fSolucionarIncidencias.show();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void jMenuItem40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem40ActionPerformed
        try {
            FIntegrador fIntegrador = new FIntegrador(ini);
            if (fIntegrador != null) {
                this.escritorio.add(fIntegrador);
                fIntegrador.setLocation(((ini.getDimension().width - fIntegrador.getSize().width) / 2), ((ini.getDimension().height - fIntegrador.getSize().height) / 2) - 30);
                //fIntegrador.setSize(ini.getDimension());
                fIntegrador.setVisible(true);
                fIntegrador.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem40ActionPerformed

    private void jMenuItem68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem68ActionPerformed
        try {
            FIntegrador fIntegrador = new FIntegrador(ini);

            this.escritorio.add(fIntegrador);
            fIntegrador.setLocation(((ini.getDimension().width - fIntegrador.getSize().width) / 2), ((ini.getDimension().height - fIntegrador.getSize().height) / 2) - 30);
            //fIntegrador.setSize(ini.getDimension());
            fIntegrador.setVisible(true);
            fIntegrador.show();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }// TODO add your handling code here:// TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem68ActionPerformed

    private void jMenuItem20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem20ActionPerformed

        try {
           FConsultarFacturasPorCliente fConsultarFacturasPorCliente = new FConsultarFacturasPorCliente(ini);

            if (fConsultarFacturasPorCliente != null) {
                this.escritorio.add(fConsultarFacturasPorCliente);
                fConsultarFacturasPorCliente.setLocation(((ini.getDimension().width - fConsultarFacturasPorCliente.getSize().width) / 2), ((ini.getDimension().height - fConsultarFacturasPorCliente.getSize().height) / 2) - 30);
                fConsultarFacturasPorCliente.setVisible(true);
                fConsultarFacturasPorCliente.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem20ActionPerformed

    private void jMenuItem104ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem104ActionPerformed
        //        MapViewOptions options = new MapViewOptions();
        //        options.importPlaces();
        //        options.setApiKey("AIzaSyAYy9jlVtsCMtCV783sA45quCVjySZyNR8");
        //

       PuntosGps_rutas puntosGps = new PuntosGps_rutas(ini);
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
            java.util.logging.Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
       

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new PrincipalUsuarioCostumerService().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
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
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu17;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    public static javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
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
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem40;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem68;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem menuCambiarClave;
    private javax.swing.JMenu mnuArchivo;
    private javax.swing.JMenuItem mnuConsultarFactura;
    private javax.swing.JMenuItem mnuConsultarFacturasCliente;
    private javax.swing.JMenu mnuDocumentos;
    private javax.swing.JMenu mnuGraficos;
    private javax.swing.JMenuItem mnuReporteMovilizado;
    private javax.swing.JMenu mnuReportes;
    private javax.swing.JMenu mnuVehiculos;
    // End of variables declaration//GEN-END:variables

 public void iniciarVariables() {
        new Thread(new HiloListadoDeTiposDePeticones(this.ini)).start();
        new Thread(new HiloListadoDeTiposDeGestiones(this.ini)).start();
        new Thread(new HiloListadoDeMovimientosManifiestosfacturas(this.ini)).start();
        new Thread(new HiloListadoDeCausalesdeDevolucion(this.ini)).start();
        new Thread(new HiloListadoDeEmpleados(this.ini)).start();
    }
}
