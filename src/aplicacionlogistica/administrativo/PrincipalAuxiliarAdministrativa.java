/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.administrativo;

import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import aplicacionlogistica.distribucion.formularios.FHabilitarFacturas;
import aplicacionlogistica.distribucion.formularios.FAnularFacturas;
import aplicacionlogistica.distribucion.formularios.FTrasladoDeFacturas;
import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.costumerService.PrincipalUsuarioCostumerService;
import aplicacionlogistica.distribucion.clientes.FGeoreferenciarClientes;
import aplicacionlogistica.configuracion.CambiarClave;
import aplicacionlogistica.distribucion.Threads.HiloCargarFormulariosAuxiliarLogistica;
import aplicacionlogistica.distribucion.consultas.FConsultarManifiestos;
import aplicacionlogistica.distribucion.*;
import aplicacionlogistica.distribucion.importarExcel.FImportarArchivoExcel;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasPorCliente;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.consultas.FHabilitarManifiesto;
import aplicacionlogistica.distribucion.formularios.ReporteManifiestosSinDescargar.FReporteManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.administracion.FBorrarFacturaManifiesto;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarCarros;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarEmpleados;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteDeRechazosTotales;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteFacturasSinMovimientos;
import aplicacionlogistica.distribucion.formularios.reportes.FReporteManifiestosConcilidos;
import aplicacionlogistica.distribucion.formularios.reportes.FReportePedidosMovilizadosPorPeriodo;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteManifiestosEnDistribucion.FReporteManifiestosEnDistribucion;
import aplicacionlogistica.distribucion.formularios.reportes.facturacion.FReporteFacturacionPorCanal;
import aplicacionlogistica.distribucion.formularios.reportes.facturacion.FReporteFacturacionTAT;
import aplicacionlogistica.picking.FManifestarFacturasEnPicking;
import aplicacionlogistica.picking.FRegistroDePedidosFinLineaDePicking;
import aplicacionlogistica.picking.FReporteFacturasPendientesEnPicking;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author VLI_488
 */
public class PrincipalAuxiliarAdministrativa extends javax.swing.JFrame {

    public Inicio ini = null;
    public CambiarClave fCambiarClave;
    public DescargarFacturas fDescargarFActuras;
    //public FManifestarFacturasEnPicking fManifestarPedidos;

    public IngresarEmpleados fIngresarEmpleados;
    public IngresarCarros fIngresaarCArros;
    public FHabilitarFacturas fHabilitarFacturas;
    public FAnularFacturas fAnularFacturas;

    public FConsultarFacturasRemoto fConsultarFacturasRemoto;
    public FConsultarFacturasPorCliente fConsultarFacturasPorCliente;
    public FConsultarManifiestos fConsultarManifiestos;

    public FTrasladoDeFacturas fTrasladoDePedidos;
    public FHabilitarManifiesto fHabilitarManifiesto;
    public FImportarArchivoExcel fImportarExcel;

    public FReporteManifiestosConcilidos fManifiestosConciliados=null;
    public FReporteDeRechazosTotales fReporteRechazosTotales=null;
    public FReportePedidosMovilizadosPorPeriodo fReporteMovilizado=null;
    public FReporteManifiestosEnDistribucion fReporteManifiestosEnDistribucion=null;
    public FReporteFacturasSinMovimientos fReporteSinMovimientos=null;
    public FReporteFacturacionPorCanal fReporteFacturacionPorCanal=null;
    public FReporteFacturacionTAT fReporteFacturacionTAT=null;
    public FReporteManifiestosSinDescargar freporteManifiestosSinDescargar=null;
    public FBorrarFacturaManifiesto fDesvincularFactura=null;
    public FReporteFacturasPendientesEnPicking fReporteFacturasPendientesEnPicking=null;
    
    
     public FManifestarFacturasEnPicking fFacturasEnPicking=null;
     public FManifestarPedidosEnRuta fManifestarPedidosEnRuta=null;
     public FRegistroDePedidosFinLineaDePicking fRegistroDePedidosFinLineaDePicking=null;
     public FGeoreferenciarClientes fGeoreferenciarClientes=null;

    public PrincipalAuxiliarAdministrativa() throws Exception {
        String rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
        ini = new Inicio(new File(rutaDeArchivo + "ReadUs.ini"));
        CUsuarios user = new CUsuarios(ini, "79481521");
        ini.setUser(user);

        initComponents();

        this.setSize((int) ini.getDimension().getWidth(), (int) ini.getDimension().getHeight() - 43);
        this.setLocation(0, 0);

        /*SE instsancian los formularios del menu */
//        new Thread(new HiloCargarFormulariosAuxiliarLogistica(this)).start();
    }

    public PrincipalAuxiliarAdministrativa(Inicio ini) throws Exception, Throwable {

        //initComponents();
        this.ini = ini;

        if (ini.isEstaClienteActivo()) {
            //if (ya) {
            initComponents();

            fCambiarClave = new CambiarClave(ini);

            /*SE instsancian los formularios del menu */
//            new Thread(new HiloCargarFormulariosAuxiliarLogistica(this)).start();

            this.setSize(this.ini.getDimension());
            this.setLocation(0, 0);
            this.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos());

            /*MÉTODO PARA CAMBIAR LA CLAVE, CUANDO SE INGRSA POR PRIMERA VEZ AL 
            SISTEMA*/
            if (this.ini.getUser().getClaveUsuario().equals(Inicio.cifrar("123456"))) { // cuando el usuario es nuevo
                //this.escritorio.setSize(600, 400);

                if (fCambiarClave != null) {
                    PrincipalAuxiliarAdministrativa.escritorio.add(fCambiarClave);
                    fCambiarClave.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));

                    fCambiarClave.setLocation((ini.getDimension().width - fCambiarClave.getSize().width) / 2, (ini.getDimension().height - fCambiarClave.getSize().height) / 2);
                    jMenuBar1.setVisible(false);
                    fCambiarClave.setResizable(false);
                    fCambiarClave.show();
                    fCambiarClave.setVisible(true);
                    fCambiarClave.txtClaveAnterior.requestFocus();
                }

            } else {

                /*SE instsancian los formularios del menu */
                //new Thread(new HiloCargarFormulariosAuxiliarLogistica(this)).start();
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
        mnuTalentoHumano = new javax.swing.JMenu();
        jMnuEmpleados = new javax.swing.JMenu();
        jSmnuCrearEmpleados = new javax.swing.JMenuItem();
        jMenu10 = new javax.swing.JMenu();
        jMenuItem21 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        mnuCarros = new javax.swing.JMenu();
        jMenuItem15 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenuItem20 = new javax.swing.JMenuItem();
        mnuVehiculos = new javax.swing.JMenu();
        jMenu14 = new javax.swing.JMenu();
        mnuDescargarFacturas = new javax.swing.JMenuItem();
        mnuConsultarManifiestos = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        mnuHabilitarManifiesto = new javax.swing.JMenuItem();
        mnuHabilitarFacturas = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem19 = new javax.swing.JMenuItem();
        mnuClientes = new javax.swing.JMenu();
        jMenu22 = new javax.swing.JMenu();
        jMenu33 = new javax.swing.JMenu();
        imActualizarClientes = new javax.swing.JMenuItem();
        mnuReportes = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        mnu_rptFacturacionPorCanal = new javax.swing.JMenuItem();
        mnu_rptFacturacionTAT = new javax.swing.JMenuItem();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem22 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenuItem18 = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();

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

        menuCambiarClave.setText("Cambio de Clave");
        menuCambiarClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCambiarClaveActionPerformed(evt);
            }
        });
        mnuArchivo.add(menuCambiarClave);

        jMenuItem6.setText("Salir");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        mnuArchivo.add(jMenuItem6);

        jMenuBar1.add(mnuArchivo);

        mnuTalentoHumano.setText("Talento Humano");

        jMnuEmpleados.setText("Empleados");

        jSmnuCrearEmpleados.setText("Crer Empleados");
        jSmnuCrearEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jSmnuCrearEmpleadosActionPerformed(evt);
            }
        });
        jMnuEmpleados.add(jSmnuCrearEmpleados);

        mnuTalentoHumano.add(jMnuEmpleados);

        jMenu10.setText("Documentos");

        jMenuItem21.setText("Documentos Conductores");
        jMenu10.add(jMenuItem21);

        mnuTalentoHumano.add(jMenu10);

        jMenuBar1.add(mnuTalentoHumano);

        jMenu7.setText("Flota");

        jMenu4.setText("Tracto Camiones");

        jMenuItem11.setText("Cabezotes");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem11);

        jMenuItem12.setText("Trailers");
        jMenu4.add(jMenuItem12);

        jMenu7.add(jMenu4);

        mnuCarros.setText("Carros");
        mnuCarros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuCarrosActionPerformed(evt);
            }
        });

        jMenuItem15.setText("Agregar Carros");
        jMenuItem15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem15ActionPerformed(evt);
            }
        });
        mnuCarros.add(jMenuItem15);

        jMenuItem16.setText("Documentos");
        mnuCarros.add(jMenuItem16);

        jMenu7.add(mnuCarros);

        jMenu6.setText("proveedores");

        jMenuItem13.setText("ng. Proveedores");
        jMenu6.add(jMenuItem13);

        jMenu7.add(jMenu6);

        jMenu8.setText("Gastos Flota");

        jMenuItem20.setText("Agreg. Facturas");
        jMenu8.add(jMenuItem20);

        jMenu7.add(jMenu8);

        jMenuBar1.add(jMenu7);

        mnuVehiculos.setText("Logistica");
        mnuVehiculos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVehiculosActionPerformed(evt);
            }
        });

        jMenu14.setText("distribucion");

        mnuDescargarFacturas.setText("Descargar Facturas");
        mnuDescargarFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuDescargarFacturasActionPerformed(evt);
            }
        });
        jMenu14.add(mnuDescargarFacturas);

        mnuConsultarManifiestos.setText("Consultar Manifiestos");
        mnuConsultarManifiestos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConsultarManifiestosActionPerformed(evt);
            }
        });
        jMenu14.add(mnuConsultarManifiestos);

        jMenuItem4.setText("Anular Facturas");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem4);

        jMenuItem5.setText("Consultar Factura");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem5);

        jMenuItem10.setText("Consultar Manifiesto");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem10);

        jMenuItem1.setText("Conciliar rutas");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem1);

        mnuVehiculos.add(jMenu14);

        mnuHabilitarManifiesto.setText("Habilitar Manifiesto");
        mnuHabilitarManifiesto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuHabilitarManifiestoActionPerformed(evt);
            }
        });
        mnuVehiculos.add(mnuHabilitarManifiesto);

        mnuHabilitarFacturas.setText("Habilitar Facturas");
        mnuHabilitarFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuHabilitarFacturasActionPerformed(evt);
            }
        });
        mnuVehiculos.add(mnuHabilitarFacturas);

        jMenu5.setText("Modificar Manifiestos");

        jMenuItem17.setText("Vincular Facturas");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem17);

        jMenuItem19.setText("Desvincular Factura");
        jMenuItem19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem19ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem19);

        mnuVehiculos.add(jMenu5);

        jMenuBar1.add(mnuVehiculos);

        mnuClientes.setText("Clientes");

        jMenu22.setText("Rutero");
        mnuClientes.add(jMenu22);

        jMenu33.setText("Rutas");
        mnuClientes.add(jMenu33);

        imActualizarClientes.setText("actualizarClientes");
        imActualizarClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                imActualizarClientesActionPerformed(evt);
            }
        });
        mnuClientes.add(imActualizarClientes);

        jMenuBar1.add(mnuClientes);

        mnuReportes.setText("Reportes");

        jMenu2.setText("Clientes");

        jMenuItem7.setText("Facturas Por Cliente");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem7);

        mnuReportes.add(jMenu2);

        jMenu3.setText("Reportes Facturacion");

        mnu_rptFacturacionPorCanal.setText("Reporte Canales");
        mnu_rptFacturacionPorCanal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_rptFacturacionPorCanalActionPerformed(evt);
            }
        });
        jMenu3.add(mnu_rptFacturacionPorCanal);

        mnu_rptFacturacionTAT.setText("Reporte TAT");
        mnu_rptFacturacionTAT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnu_rptFacturacionTATActionPerformed(evt);
            }
        });
        jMenu3.add(mnu_rptFacturacionTAT);

        mnuReportes.add(jMenu3);

        jMenu11.setText("Reportes Flota");

        jMenuItem22.setText("Gastos ");
        jMenu11.add(jMenuItem22);

        mnuReportes.add(jMenu11);

        jMenu9.setText("Logistica");

        jMenuItem8.setText("Reporte por periodo");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem8);

        jMenuItem9.setText("Facturas sin Movimiento");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem9);

        jMenuItem14.setText("Reporte Manifiestos En Distribucion");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem14);

        jMenuItem18.setText("Manifiestos sin Descargar");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu9.add(jMenuItem18);

        jMenu1.setText("Reportes Rechazos");

        jMenuItem2.setText("Totales");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Parciales");
        jMenu1.add(jMenuItem3);

        jMenu9.add(jMenu1);

        mnuReportes.add(jMenu9);

        jMenuBar1.add(mnuReportes);

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

            if (fCambiarClave != null) {
                this.escritorio.add(fCambiarClave);
                fCambiarClave.txtUsuario.setText(Inicio.deCifrar(this.ini.getUser().getNombreUsuario()));
                fCambiarClave.setLocation((ini.getDimension().width - fCambiarClave.getSize().width) / 2, (ini.getDimension().height - fCambiarClave.getSize().height) / 2);
                fCambiarClave.show();
                fCambiarClave.setVisible(true);
                fCambiarClave.txtClaveAnterior.requestFocus();
            }

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAuxiliarAdministrativa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuCambiarClaveActionPerformed

    private void jSmnuCrearEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jSmnuCrearEmpleadosActionPerformed

        if (fIngresarEmpleados != null) {
            this.escritorio.add(fIngresarEmpleados);
            fIngresarEmpleados.setLocation(((ini.getDimension().width - fIngresarEmpleados.getSize().width) / 2), ((ini.getDimension().height - fIngresarEmpleados.getSize().height) / 2) - 30);
            fIngresarEmpleados.setResizable(false);
            fIngresarEmpleados.setVisible(true);
            fIngresarEmpleados.show();
        }
    }//GEN-LAST:event_jSmnuCrearEmpleadosActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void mnuVehiculosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVehiculosActionPerformed

    }//GEN-LAST:event_mnuVehiculosActionPerformed

    private void mnuCarrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuCarrosActionPerformed

    }//GEN-LAST:event_mnuCarrosActionPerformed

    private void jMenuItem15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem15ActionPerformed

        if (fIngresaarCArros != null) {
            this.escritorio.add(fIngresaarCArros);
            fIngresaarCArros.setLocation(((ini.getDimension().width - fIngresaarCArros.getSize().width) / 2), ((ini.getDimension().height - fIngresaarCArros.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            fIngresaarCArros.setVisible(true);
            fIngresaarCArros.show();
        }
    }//GEN-LAST:event_jMenuItem15ActionPerformed

    private void mnuDescargarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuDescargarFacturasActionPerformed
        try {
            //DescargarFacturas fDescargarFActuras = new DescargarFacturas(this);

            if (fDescargarFActuras != null) {
                this.escritorio.add(fDescargarFActuras);
                fDescargarFActuras.CargarVista();
                fDescargarFActuras.setSize(ini.getDimension());
                // fDescargarFActuras.setLocation(((ini.getDimension().width - fDescargarFActuras.getSize().width) / 2), ((ini.getDimension().height - fDescargarFActuras.getSize().height) / 2));
                fDescargarFActuras.setLocation(0, 0);
                fDescargarFActuras.setVisible(true);
                fDescargarFActuras.show();
            }

        } catch (Exception ex) {
            Logger.getLogger(PrincipalAuxiliarAdministrativa.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_mnuDescargarFacturasActionPerformed

    private void mnuConsultarManifiestosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConsultarManifiestosActionPerformed
        try {
            if (fConsultarManifiestos != null) {
                this.escritorio.add(fConsultarManifiestos);
                fConsultarManifiestos.setLocation(((ini.getDimension().width - fConsultarManifiestos.getSize().width) / 2), ((ini.getDimension().height - fConsultarManifiestos.getSize().height) / 2));
                fConsultarManifiestos.setVisible(true);
                fConsultarManifiestos.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAuxiliarAdministrativa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuConsultarManifiestosActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {

            if (fReporteRechazosTotales != null) {
                this.escritorio.add(fReporteRechazosTotales);
                fReporteRechazosTotales.setLocation(((ini.getDimension().width - fReporteRechazosTotales.getSize().width) / 2), ((ini.getDimension().height - fReporteRechazosTotales.getSize().height) / 2));
                fReporteRechazosTotales.setVisible(true);
                fReporteRechazosTotales.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAuxiliarAdministrativa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
        try {

            if (fAnularFacturas != null) {
                this.escritorio.add(fAnularFacturas);
                // fDescargarFActuras.setSize(escritorio.getSize());
                fAnularFacturas.setLocation(((ini.getDimension().width - fAnularFacturas.getSize().width) / 2), ((ini.getDimension().height - fAnularFacturas.getSize().height) / 2));
                fAnularFacturas.setVisible(true);
                fAnularFacturas.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAuxiliarAdministrativa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem4ActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed

//
//            if (fConsultarFacturasRemoto != null) {
//                this.escritorio.add(fConsultarFacturasRemoto);
////            fDescargarFActuras.setSize(escritorio.getSize());
////            fDescargarFActuras.setMaximumSize(ini.getDimension());
//                fConsultarFacturasRemoto.setLocation(((ini.getDimension().width - fConsultarFacturasRemoto.getSize().width) / 2), ((ini.getDimension().height - fConsultarFacturasRemoto.getSize().height) / 2));
//                fConsultarFacturasRemoto.setVisible(true);
//                fConsultarFacturasRemoto.show();
//            }
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

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fConsultarFacturasPorCliente != null) {
                this.escritorio.add(fConsultarFacturasPorCliente);
                fConsultarFacturasPorCliente.setLocation(((ini.getDimension().width - fConsultarFacturasPorCliente.getSize().width) / 2), ((ini.getDimension().height - fConsultarFacturasPorCliente.getSize().height) / 2) - 30);
                fConsultarFacturasPorCliente.setVisible(true);
                fConsultarFacturasPorCliente.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAuxiliarAdministrativa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fReporteMovilizado != null) {
                this.escritorio.add(fReporteMovilizado);
                fReporteMovilizado.setLocation(((ini.getDimension().width - fReporteMovilizado.getSize().width) / 2), ((ini.getDimension().height - fReporteMovilizado.getSize().height) / 2) - 30);
                fReporteMovilizado.setVisible(true);
                fReporteMovilizado.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAuxiliarAdministrativa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed

        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fReporteSinMovimientos != null) {
                this.escritorio.add(fReporteSinMovimientos);
                fReporteSinMovimientos.setLocation(((ini.getDimension().width - fReporteSinMovimientos.getSize().width) / 2), ((ini.getDimension().height - fReporteSinMovimientos.getSize().height) / 2) - 30);
                fReporteSinMovimientos.setVisible(true);
                fReporteSinMovimientos.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAuxiliarAdministrativa.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem9ActionPerformed

    private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem10ActionPerformed
        // TODO add your handling code here:
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fConsultarManifiestos != null) {
                this.escritorio.add(fConsultarManifiestos);
                fConsultarManifiestos.setLocation(((ini.getDimension().width - fConsultarManifiestos.getSize().width) / 2), ((ini.getDimension().height - fConsultarManifiestos.getSize().height) / 2) - 30);
                fConsultarManifiestos.setVisible(true);
                fConsultarManifiestos.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem10ActionPerformed

    private void mnuHabilitarManifiestoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuHabilitarManifiestoActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fHabilitarManifiesto != null) {
                this.escritorio.add(fHabilitarManifiesto);
                fHabilitarManifiesto.setLocation(((ini.getDimension().width - fHabilitarManifiesto.getSize().width) / 2), ((ini.getDimension().height - fHabilitarManifiesto.getSize().height) / 2) - 30);
                fHabilitarManifiesto.setVisible(true);
                fHabilitarManifiesto.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuHabilitarManifiestoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fManifiestosConciliados != null) {
                this.escritorio.add(fManifiestosConciliados);
                fManifiestosConciliados.setLocation(((ini.getDimension().width - fManifiestosConciliados.getSize().width) / 2), ((ini.getDimension().height - fManifiestosConciliados.getSize().height) / 2) - 30);
                //fManifiestosConciliados.setVisible(true);
                fManifiestosConciliados.refrescarTblManifiestosSinConciliar();
                //fManifiestosConciliados.show();
            }

        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }     // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void mnuHabilitarFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuHabilitarFacturasActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fHabilitarFacturas != null) {
                this.escritorio.add(fHabilitarFacturas);
                fHabilitarFacturas.setLocation(((ini.getDimension().width - fHabilitarFacturas.getSize().width) / 2), ((ini.getDimension().height - fHabilitarFacturas.getSize().height) / 2) - 30);
                //fHabilitarFacturas.setVisible(true);
                //fHabilitarFacturas.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuHabilitarFacturasActionPerformed

    private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem14ActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fReporteManifiestosEnDistribucion != null) {
                this.escritorio.add(fReporteManifiestosEnDistribucion);
                fReporteManifiestosEnDistribucion.setLocation(((ini.getDimension().width - fReporteManifiestosEnDistribucion.getSize().width) / 2), ((ini.getDimension().height - fReporteManifiestosEnDistribucion.getSize().height) / 2) - 30);
                fReporteManifiestosEnDistribucion.setVisible(true);
                fReporteManifiestosEnDistribucion.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem14ActionPerformed

    private void mnu_rptFacturacionPorCanalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionPorCanalActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fReporteFacturacionPorCanal != null) {
                this.escritorio.add(fReporteFacturacionPorCanal);
                fReporteFacturacionPorCanal.setLocation(((ini.getDimension().width - fReporteFacturacionPorCanal.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturacionPorCanal.getSize().height) / 2) - 30);
                fReporteFacturacionPorCanal.setVisible(true);
                fReporteFacturacionPorCanal.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnu_rptFacturacionPorCanalActionPerformed

    private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem17ActionPerformed

    private void mnu_rptFacturacionTATActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnu_rptFacturacionTATActionPerformed
        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fReporteFacturacionTAT != null) {
                this.escritorio.add(fReporteFacturacionTAT);
                fReporteFacturacionTAT.setLocation(((ini.getDimension().width - fReporteFacturacionTAT.getSize().width) / 2), ((ini.getDimension().height - fReporteFacturacionTAT.getSize().height) / 2) - 30);
                fReporteFacturacionTAT.setVisible(true);
                fReporteFacturacionTAT.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnu_rptFacturacionTATActionPerformed

    private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem18ActionPerformed

        try {
            // TODO add your handling code here:FConsultarFacturasPorCliente
            if (freporteManifiestosSinDescargar != null) {
                this.escritorio.add(freporteManifiestosSinDescargar);
                freporteManifiestosSinDescargar.setLocation(((ini.getDimension().width - freporteManifiestosSinDescargar.getSize().width) / 2), ((ini.getDimension().height - freporteManifiestosSinDescargar.getSize().height) / 2) - 30);
                freporteManifiestosSinDescargar.setVisible(true);
                freporteManifiestosSinDescargar.cargarInformacion();
                freporteManifiestosSinDescargar.show();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalUsuarioCostumerService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem18ActionPerformed

    private void jMenuItem19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem19ActionPerformed
 // TODO add your handling code here:FConsultarFacturasPorCliente
            if (fDesvincularFactura != null) {
                this.escritorio.add(fDesvincularFactura);
                fDesvincularFactura.setLocation(((ini.getDimension().width - fDesvincularFactura.getSize().width) / 2), ((ini.getDimension().height - fDesvincularFactura.getSize().height) / 2) - 30);
                fDesvincularFactura.setVisible(true);
                //fDesvincularFactura.cargarInformacion();
                fDesvincularFactura.show();
            }

   
    }//GEN-LAST:event_jMenuItem19ActionPerformed

    private void imActualizarClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_imActualizarClientesActionPerformed
      
        if (fGeoreferenciarClientes != null) {
            this.escritorio.add(fGeoreferenciarClientes);
            fGeoreferenciarClientes.setLocation(((ini.getDimension().width - fGeoreferenciarClientes.getSize().width) / 2), ((ini.getDimension().height - fGeoreferenciarClientes.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            fGeoreferenciarClientes.setVisible(true);
            fGeoreferenciarClientes.show();
        }
    }//GEN-LAST:event_imActualizarClientesActionPerformed

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
            java.util.logging.Logger.getLogger(PrincipalAuxiliarAdministrativa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                    new PrincipalAuxiliarAdministrativa().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(PrincipalAuxiliarAdministrativa.class.getName()).log(Level.SEVERE, null, ex);
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
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"));
        return retValue;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JDesktopPane escritorio;
    private javax.swing.JMenuItem imActualizarClientes;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu22;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu33;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    public static javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
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
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenu jMnuEmpleados;
    private javax.swing.JMenuItem jSmnuCrearEmpleados;
    private javax.swing.JMenuItem menuCambiarClave;
    private javax.swing.JMenu mnuArchivo;
    private javax.swing.JMenu mnuCarros;
    private javax.swing.JMenu mnuClientes;
    private javax.swing.JMenuItem mnuConsultarManifiestos;
    private javax.swing.JMenuItem mnuDescargarFacturas;
    private javax.swing.JMenuItem mnuHabilitarFacturas;
    private javax.swing.JMenuItem mnuHabilitarManifiesto;
    private javax.swing.JMenu mnuReportes;
    private javax.swing.JMenu mnuTalentoHumano;
    private javax.swing.JMenu mnuVehiculos;
    private javax.swing.JMenuItem mnu_rptFacturacionPorCanal;
    private javax.swing.JMenuItem mnu_rptFacturacionTAT;
    // End of variables declaration//GEN-END:variables
}
