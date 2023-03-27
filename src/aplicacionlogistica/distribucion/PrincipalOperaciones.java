/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion;

import aplicacionlogistica.configuracion.CambiarClave;
import aplicacionlogistica.configuracion.HiloBitacoraMovimientosEnElSistema;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FConsultaManifiestosDescargados;
import aplicacionlogistica.distribucion.consultas.FConsultaManifiestosSinDescargar;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasPorCliente;
import aplicacionlogistica.distribucion.consultas.FConsultarFacturasRemoto;
import aplicacionlogistica.distribucion.consultas.FConsultarManifiestos;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultaPedidosDescargadosPorConductorHielera;
import aplicacionlogistica.distribucion.formularios.Hielera.consultas.FConsultarPedidosConductorHielera;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarDocumentosEmpleados;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarUsuarios;
import aplicacionlogistica.distribucion.formularios.reportes.rptDocumentosEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author VLI_488
 */
public class PrincipalOperaciones extends javax.swing.JFrame {

    Inicio ini = null;
    CUsuarios user;

    List<String> datos = new ArrayList<>();
    String usuario;
    String formulario = "PrincipalOperaciones";

    public void setUser(CUsuarios user) {
        this.user = user;
    }

    public CUsuarios getUser() {
        return user;
    }

    public PrincipalOperaciones() {
        initComponents();

    }

    public PrincipalOperaciones(Inicio ini) throws Exception {

        initComponents();

        try {
            ini.cargarImagenEscritorio(escritorio);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Revisar archivo de configuracion", "Imagen de Fonfo no encontrada", JOptionPane.ERROR_MESSAGE);
        }

        this.ini = ini;
        usuario = Inicio.deCifrar(ini.getUser().getNombreUsuario());
        this.user = ini.getUser();
        this.setSize(ini.getDimension());
        this.setLocation(0, 0);
        this.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + user.getNombres() + " " + user.getApellidos());
        if (user.getClaveUsuario().equals(Inicio.cifrar("123456"))) { // cuando el usuario es nuevo
            this.escritorio.setSize(600, 400);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            CambiarClave form = new CambiarClave(ini);
            PrincipalOperaciones.escritorio.add(form);
            form.setSize(escritorio.getSize());
            form.setLocation((screenSize.width - escritorio.getSize().width) / 2, (screenSize.height - escritorio.getSize().height) / 2);
            jMenuBar1.setVisible(false);
            form.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Inicio primera vez, cambio de Clave");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        } else {
            this.escritorio.setSize(600, 400);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            CambiarClave form = new CambiarClave(ini);
            PrincipalOperaciones.escritorio.add(form);
            form.setSize(escritorio.getSize());
            form.setLocation((screenSize.width - escritorio.getSize().width) / 2, (screenSize.height - escritorio.getSize().height) / 2);
            jMenuBar1.setVisible(true);
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
        jToolBar1 = new javax.swing.JToolBar();
        jMenuBar1 = new javax.swing.JMenuBar();
        menuArchivo = new javax.swing.JMenu();
        menuCambiarClave = new javax.swing.JMenuItem();
        jMenuAdministracion = new javax.swing.JMenu();
        menuIngresarEmpleados = new javax.swing.JMenuItem();
        menuIngresarUsuarios = new javax.swing.JMenuItem();
        jMenuLogistica = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu12 = new javax.swing.JMenu();
        menuAsignarDocumentos = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenu6 = new javax.swing.JMenu();
        jMenu8 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        jMenu10 = new javax.swing.JMenu();
        jMenu11 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        jMenu5 = new javax.swing.JMenu();
        mnuConsultas = new javax.swing.JMenu();
        mnuConsultar = new javax.swing.JMenu();
        mItmConsultarCliente = new javax.swing.JMenuItem();
        mItmConsultarFactura = new javax.swing.JMenuItem();
        mnuManifiestos = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenu36 = new javax.swing.JMenu();
        jMenuItem79 = new javax.swing.JMenuItem();
        jMenuItem80 = new javax.swing.JMenuItem();
        jMenuReportes = new javax.swing.JMenu();
        menuRptDocumentosEmpleados = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());
        setIconImages(getIconImages());

        escritorio.setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);
        escritorio.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jToolBar1.setRollover(true);
        escritorio.add(jToolBar1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 420, 30));

        jMenuBar1.setEnabled(false);

        menuArchivo.setText("Archivo");
        menuArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuArchivoActionPerformed(evt);
            }
        });

        menuCambiarClave.setText("Cambio de Clave");
        menuCambiarClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCambiarClaveActionPerformed(evt);
            }
        });
        menuArchivo.add(menuCambiarClave);

        jMenuBar1.add(menuArchivo);

        jMenuAdministracion.setText("Administracion");

        menuIngresarEmpleados.setText("Empleados");
        menuIngresarEmpleados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuIngresarEmpleadosMouseClicked(evt);
            }
        });
        menuIngresarEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuIngresarEmpleadosActionPerformed(evt);
            }
        });
        jMenuAdministracion.add(menuIngresarEmpleados);

        menuIngresarUsuarios.setText("Usuarios de Sistema");
        menuIngresarUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuIngresarUsuariosActionPerformed(evt);
            }
        });
        jMenuAdministracion.add(menuIngresarUsuarios);

        jMenuBar1.add(jMenuAdministracion);

        jMenuLogistica.setText("Logistica");

        jMenu2.setText("Empleados");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenu12.setText("Conductores");
        jMenu2.add(jMenu12);

        menuAsignarDocumentos.setText("Asignar Documentos");
        menuAsignarDocumentos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                menuAsignarDocumentosMouseClicked(evt);
            }
        });
        menuAsignarDocumentos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAsignarDocumentosActionPerformed(evt);
            }
        });
        jMenu2.add(menuAsignarDocumentos);

        jMenuLogistica.add(jMenu2);

        jMenu3.setText("Vehículos");

        jMenu7.setText("Ingresar Vehiculos");
        jMenu3.add(jMenu7);

        jMenu6.setText("Documentos");
        jMenu3.add(jMenu6);

        jMenu8.setText("Ordenes");
        jMenu3.add(jMenu8);

        jMenu9.setText("Mantenimientos");
        jMenu3.add(jMenu9);

        jMenu10.setText("LLantas");
        jMenu3.add(jMenu10);

        jMenu11.setText("Combustibles");
        jMenu3.add(jMenu11);

        jMenuLogistica.add(jMenu3);

        jMenu4.setText("importar Registros");
        jMenuLogistica.add(jMenu4);

        jMenu5.setText("Exportar Registros");
        jMenuLogistica.add(jMenu5);

        jMenuBar1.add(jMenuLogistica);

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

        mnuConsultas.add(jMenu36);

        jMenuBar1.add(mnuConsultas);

        jMenuReportes.setText("Reportes");

        menuRptDocumentosEmpleados.setText("Reporte Documentos Empleados");
        menuRptDocumentosEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRptDocumentosEmpleadosActionPerformed(evt);
            }
        });
        jMenuReportes.add(menuRptDocumentosEmpleados);

        jMenuBar1.add(jMenuReportes);

        jMenu1.setText("jMenu1");
        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 496, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuIngresarEmpleadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuIngresarEmpleadosMouseClicked


    }//GEN-LAST:event_menuIngresarEmpleadosMouseClicked

    private void menuIngresarEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuIngresarEmpleadosActionPerformed
        // ini= new Inicio(usuario);
////       IngresarEmpleados form= new IngresarEmpleados(this);
////       PrincipalOperaciones.escritorio.add(form);
//       form.setSize(escritorio.getSize());
//        form.setListaDeAgencias(arrAgenciass);
//        form.setListaDeCargos(arrCargos);
//        form.setListaDeCentrosDeCosto(arrCentrosDeCosto);
//        form.setListaDeCiudades(arrCiudades);
//        form.setListaDeDepartamentos(arrDepartamentos);
//        form.setListaDeEntidadesBancarias(arrEntidadesBancarias);
//        form.setListaDeEstadosCiviles(arrEstadosCiviles);
//        form.setListaDeRegionales(arrRegionales);
//        form.setListaDeTiposContratosPer(arrTiposContratos);
//        form.setListaDeTiposDeSangre(arrTiposDeSangre);
//        form.setListaDeZonas(arrZonas);

//       form.show();
    }//GEN-LAST:event_menuIngresarEmpleadosActionPerformed

    private void menuAsignarDocumentosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuAsignarDocumentosMouseClicked
        switch (user.getTipoAcceso()) {
            case 1: // no tiene acceso

                break;
            case 2: // acceso total
                switch (user.getNivelAcceso()) {
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;

                }
                break;
            case 3: // acceso a nivel Administrativo
                switch (user.getNivelAcceso()) {
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;

                }
            case 4: // Acceso a nivel de seguridad
                switch (user.getNivelAcceso()) {
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;

                }
            case 5: // Acceso a nivel de Operativo
                switch (user.getNivelAcceso()) {
                    case 1:

                        break;
                    case 2:

                        break;
                    case 3:

                        break;
                    case 4:

                        break;

                }
            default:
        }
    }//GEN-LAST:event_menuAsignarDocumentosMouseClicked

    private void menuAsignarDocumentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAsignarDocumentosActionPerformed
        IngresarDocumentosEmpleados form1 = new IngresarDocumentosEmpleados(ini);
        PrincipalOperaciones.escritorio.add(form1);
        form1.setSize(escritorio.getSize());
        form1.show();
        datos = new ArrayList<>();
        datos.add(0, usuario);
        datos.add(1, formulario);
        datos.add(2, "Click en form  IngresarDocumentosEmpleados");
        datos.add(3, "CURRENT_TIMESTAMP");
        new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

        switch (ini.getTipoDeacceso()) {
            case 0:
                break;
            case 1:

                break;
            case 2:
//              IngresarDocumentosEmpleados form1= new IngresarDocumentosEmpleados(this.ini);
//              Principal.escritorio.add(form1);
//              form1.setSize(escritorio.getSize());
//              form1.show();
//              break;
            case 3:
                break;
            case 4:
                break;
            default:
                break;
        }

    }//GEN-LAST:event_menuAsignarDocumentosActionPerformed

    private void menuIngresarUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuIngresarUsuariosActionPerformed
        switch (ini.getTipoDeacceso()) {
            case 0:
                break;
            case 1:
                IngresarUsuarios form;
                try {
//              form = new IngresarUsuarios(this);
//              PrincipalOperaciones.escritorio.add(form);
//              form.setSize(escritorio.getSize());
//              form.show();       
                } catch (Exception ex) {
                    Logger.getLogger(PrincipalOperaciones.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            default:
                break;
        }

    }//GEN-LAST:event_menuIngresarUsuariosActionPerformed

    private void menuArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuArchivoActionPerformed

    }//GEN-LAST:event_menuArchivoActionPerformed

    private void menuCambiarClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCambiarClaveActionPerformed
        try {
            this.escritorio.setSize(600, 400);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            CambiarClave form = new CambiarClave(ini);
            PrincipalOperaciones.escritorio.add(form);
            form.setSize(escritorio.getSize());
            form.setLocation((screenSize.width - escritorio.getSize().width) / 2, (screenSize.height - escritorio.getSize().height) / 2);
            form.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  CambiarClave");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalOperaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuCambiarClaveActionPerformed

    private void menuRptDocumentosEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRptDocumentosEmpleadosActionPerformed
        rptDocumentosEmpleados form;
        form = new rptDocumentosEmpleados(this.ini);
        PrincipalOperaciones.escritorio.add(form);
        form.setSize(escritorio.getSize());
        form.show();
        datos = new ArrayList<>();
        datos.add(0, usuario);
        datos.add(1, formulario);
        datos.add(2, "Click en form  rptDocumentosEmpleados");
        datos.add(3, "CURRENT_TIMESTAMP");
        new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
    }//GEN-LAST:event_menuRptDocumentosEmpleadosActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu2ActionPerformed

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
                datos.add(2, "Click en form  FConsultarFacturasPorCliente");
                datos.add(3, "CURRENT_TIMESTAMP");
                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            }
        } catch (Exception ex) {
            Logger.getLogger(PrincipalOperaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mItmConsultarClienteActionPerformed

    private void mItmConsultarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mItmConsultarFacturaActionPerformed

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
            datos.add(2, "Click en form  FConsultarFacturasRemoto");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalOperaciones.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_mItmConsultarFacturaActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed
        try {
            FConsultarManifiestos form1 = new FConsultarManifiestos(ini);

            PrincipalAuxiliarLogistica.escritorio.add(form1);
            form1.setLocation(((ini.getDimension().width - form1.getSize().width) / 2), ((ini.getDimension().height - form1.getSize().height) / 2));
            form1.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FConsultarManifiestos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalOperaciones.class.getName()).log(Level.SEVERE, null, ex);
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
            datos.add(2, "Click en form  FConsultaManifiestosDescargados");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalOperaciones.class.getName()).log(Level.SEVERE, null, ex);
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
            datos.add(2, "Click en form  FConsultaManifiestosSinDescargar");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalOperaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem13ActionPerformed

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

        } catch (Exception ex) {
            Logger.getLogger(PrincipalOperaciones.class.getName()).log(Level.SEVERE, null, ex);
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
            datos.add(2, "Click en form  FConsultaPedidosDescargadosPorConductorHielera");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalOperaciones.class.getName()).log(Level.SEVERE, null, ex);
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
            java.util.logging.Logger.getLogger(PrincipalOperaciones.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                new PrincipalOperaciones().setVisible(true);

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
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu36;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenu jMenuAdministracion;
    public static javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem79;
    private javax.swing.JMenuItem jMenuItem80;
    private javax.swing.JMenu jMenuLogistica;
    private javax.swing.JMenu jMenuReportes;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JMenuItem mItmConsultarCliente;
    private javax.swing.JMenuItem mItmConsultarFactura;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenuItem menuAsignarDocumentos;
    private javax.swing.JMenuItem menuCambiarClave;
    private javax.swing.JMenuItem menuIngresarEmpleados;
    private javax.swing.JMenuItem menuIngresarUsuarios;
    private javax.swing.JMenuItem menuRptDocumentosEmpleados;
    private javax.swing.JMenu mnuConsultar;
    private javax.swing.JMenu mnuConsultas;
    private javax.swing.JMenu mnuManifiestos;
    // End of variables declaration//GEN-END:variables
}
