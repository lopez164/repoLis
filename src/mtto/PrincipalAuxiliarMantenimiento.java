/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto;

import GPS.Monitoreo.PuntosMonitoreo;
import GPS.PuntosGps.PuntosGps;
import mtto.ingresoDeRegistros.FImportarConsumoCombustible;
import mtto.Servicios.FAgregarMantenimientoVehiculo;
import aplicacionlogistica.configuracion.CambiarClave;
import aplicacionlogistica.configuracion.Fondo;
import aplicacionlogistica.configuracion.HiloBitacoraMovimientosEnElSistema;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.PrincipalAdministradorDelSistema;
import aplicacionlogistica.distribucion.PrincipalSuperUsuario;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeAgencias;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeDepartamentos;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeVehiculos;
import aplicacionlogistica.distribucion.clientes.FGeoreferenciarClientes;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarCarros;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarDocumentosEmpleados;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarEmpleados;
import aplicacionlogistica.distribucion.formularios.reportes.rptDocumentosEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import mtto.ingresoDeRegistros.IngresarFacturasDeGastos;
import mtto.documentos.Threads.HiloListadoDeProveedores;
import mtto.documentos.Threads.HiloListadoDeSucursales;
import mtto.proveedores.IngresarProveedores;
import mtto.proveedores.IngresarSucursalDeProveedor;
import mtto.reportes.FReporteGastosFlota;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import mtto.documentos.FIngresarDocumentosVehiculos;
import mtto.documentos.Reportes.FReporteDocumentosVehiculos;
import mtto.documentos.Reportes.FReporteListadoCarrosPropios;
import mtto.documentos.Threads.HiloCargarFormulariosMantenimiento;
import mtto.documentos.Threads.HiloListadoDeCuentasSecundarias;
import mtto.documentos.Threads.HiloListadoDeTiposDeDocumentos;
import mtto.documentos.Threads.HiloListadoDeTiposDeMantenimiento;
import mtto.reportes.FReporteGastosCombustible;

/**
 *
 * @author VLI_488
 */
public class PrincipalAuxiliarMantenimiento extends javax.swing.JFrame {

    public Inicio ini = null;
    CUsuarios user;
    public CambiarClave fCambiarClave;
    public IngresarProveedores formProveedores;
    public IngresarSucursalDeProveedor formCrearSucursales;
    public IngresarCarros fIngresaarCArros;
    public IngresarFacturasDeGastos ingresarFacturasDeGastos;
    public FGeoreferenciarClientes fGeoreferenciarClientes;
    public FReporteGastosFlota fReporteGastosFlota;
    public FAgregarMantenimientoVehiculo fAgregarMantenimientoVehiculo;
    public FIngresarDocumentosVehiculos fIngresarDocumentosVehiculos;
    public PuntosMonitoreo puntosMonitoreo;

    List<String> datos = new ArrayList<>();
    public Timer timer;
    String usuario;
    String formulario = "PrincipalMantenimiento";
    ActionListener actionListener;

    PuntosGps puntosGps;

    // ImageIcon icon= new ImageIcon(this.getClass().getResource("aplicacionlogistica/configuracion/imagenes/fondos/mantenimientos.png"));
    public void setUser(CUsuarios user) {
        this.user = user;
    }

    public CUsuarios getUser() {
        return user;
    }

    public PrincipalAuxiliarMantenimiento() throws Exception {
        String rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
        ini = new Inicio(new File(rutaDeArchivo + "ReadUs.ini"));

        CUsuarios user = new CUsuarios(ini, "79481521");
        ini.setUser(user);

        initComponents();

        this.setSize((int) ini.getDimension().getWidth(), (int) ini.getDimension().getHeight() - 43);
        this.setLocation(0, 0);

        /*SE instancian los formularios del menu */
        new Thread(new HiloListadoDeTiposDeMantenimiento(this.ini)).start();
        new Thread(new HiloListadoDeProveedores(this.ini)).start();
        new Thread(new HiloListadoDeSucursales(this.ini)).start();

        new Thread(new HiloCargarFormulariosMantenimiento(this)).start();

    }

    public PrincipalAuxiliarMantenimiento(Inicio ini) throws Exception {

        //initComponents();
        this.ini = ini;

        if (this.ini.isEstaClienteActivo()) {
            //if (ya) {
            initComponents();
            PrincipalAuxiliarMantenimiento.escritorio.setSize(this.ini.getDimension());
            cargarImagen(escritorio);

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

            /*MÉTODO PARA CAMBIAR LA CLAVE, CUANDO SE INGRSA POR PRIMERA VEZ AL 
            SISTEMA*/
            if (this.ini.getUser().getClaveUsuario().equals(Inicio.cifrar("123456"))) { // cuando el usuario es nuevo

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

                /*SE instancian los formularios del menu */
                // new Thread(new HiloCargarFormulariosMantenimiento(this)).start();
                new Thread(new HiloListadoDeTiposDeMantenimiento(this.ini)).start();
                new Thread(new HiloListadoDeProveedores(this.ini)).start();
                new Thread(new HiloListadoDeSucursales(this.ini)).start();
                new Thread(new HiloListadoDeTiposDeDocumentos(this.ini)).start();
                new Thread(new HiloListadoDeVehiculos(this.ini)).start();
                new Thread(new HiloListadoDeCuentasSecundarias(this.ini)).start();
                new Thread(new HiloListadoDeDepartamentos(this.ini)).start();
                new Thread(new HiloListadoDeAgencias(this.ini)).start();

                new Thread(new HiloCargarFormulariosMantenimiento(this)).start();

                // new Thread(new HiloListadoDeSucursales(this.ini)).start();
                if (!ini.isGPSservice()) {
                    jMenuGps.setVisible(false);
                }
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
        menuArchivo = new javax.swing.JMenu();
        menuCambiarClave = new javax.swing.JMenuItem();
        mnuSalir = new javax.swing.JMenuItem();
        jMenuAdministracion = new javax.swing.JMenu();
        jMenu14 = new javax.swing.JMenu();
        mnuProveedores = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu15 = new javax.swing.JMenu();
        mnuVehiculos = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu16 = new javax.swing.JMenu();
        menuIngresarEmpleados = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuLogistica = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenu12 = new javax.swing.JMenu();
        menuAsignarDocumentos = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenu7 = new javax.swing.JMenu();
        jMenu9 = new javax.swing.JMenu();
        miMantenimientos = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenu10 = new javax.swing.JMenu();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenu13 = new javax.swing.JMenu();
        miAgregarFactura = new javax.swing.JMenuItem();
        miActualizaCliente = new javax.swing.JMenuItem();
        miMantenimientos1 = new javax.swing.JMenuItem();
        jMenuReportes = new javax.swing.JMenu();
        menuRptDocumentosEmpleados = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem100 = new javax.swing.JMenuItem();
        jMenuGps = new javax.swing.JMenu();
        jMenuItem104 = new javax.swing.JMenuItem();
        jMenuItem105 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());
        setIconImages(getIconImages());

        escritorio.setDragMode(javax.swing.JDesktopPane.OUTLINE_DRAG_MODE);

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

        mnuSalir.setText("Salir");
        mnuSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSalirActionPerformed(evt);
            }
        });
        menuArchivo.add(mnuSalir);

        jMenuBar1.add(menuArchivo);

        jMenuAdministracion.setText("Administracion");

        jMenu14.setText("Proveedores");

        mnuProveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        mnuProveedores.setText("Agregar proveedores");
        mnuProveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProveedoresActionPerformed(evt);
            }
        });
        jMenu14.add(mnuProveedores);

        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem2.setText("Agregar Sucursales");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu14.add(jMenuItem2);

        jMenuAdministracion.add(jMenu14);

        jMenu15.setText("vehiculos");

        mnuVehiculos.setText("Agregar vehiculos");
        mnuVehiculos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVehiculosActionPerformed(evt);
            }
        });
        jMenu15.add(mnuVehiculos);

        jMenuItem3.setText("Crear Documentos");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem3);

        jMenuAdministracion.add(jMenu15);

        jMenu16.setText("Conductores");

        menuIngresarEmpleados.setText("Conductores");
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
        jMenu16.add(menuIngresarEmpleados);

        jMenuItem4.setText("Asignar documentos");
        jMenu16.add(jMenuItem4);

        jMenuAdministracion.add(jMenu16);

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
        jMenu7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu7ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenu7);

        jMenu9.setText("Mantenimientos");

        miMantenimientos.setText("Mantenimiento ");
        miMantenimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMantenimientosActionPerformed(evt);
            }
        });
        jMenu9.add(miMantenimientos);

        jMenu3.add(jMenu9);

        jMenu6.setText("Documentos");

        jMenuItem1.setText("Ingresar Documentos");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu6.add(jMenuItem1);

        jMenu3.add(jMenu6);

        jMenu8.setText("Ordenes");
        jMenu3.add(jMenu8);

        jMenu10.setText("LLantas");
        jMenu3.add(jMenu10);

        jMenu11.setText("Combustibles");
        jMenu3.add(jMenu11);

        jMenuItem5.setText("ingresar Facturas");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuLogistica.add(jMenu3);

        jMenu4.setText("importar Registros");

        jMenuItem9.setText("Importar Combustibles");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuLogistica.add(jMenu4);

        jMenu5.setText("Exportar Registros");
        jMenuLogistica.add(jMenu5);

        jMenu13.setText("Gastos Operativos");

        miAgregarFactura.setText("Ingresar Facturas");
        miAgregarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miAgregarFacturaActionPerformed(evt);
            }
        });
        jMenu13.add(miAgregarFactura);

        jMenuLogistica.add(jMenu13);

        miActualizaCliente.setText("Asignar Servicios");
        miActualizaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miActualizaClienteActionPerformed(evt);
            }
        });
        jMenuLogistica.add(miActualizaCliente);

        miMantenimientos1.setText("Mantenimiento ");
        miMantenimientos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMantenimientos1ActionPerformed(evt);
            }
        });
        jMenuLogistica.add(miMantenimientos1);

        jMenuBar1.add(jMenuLogistica);

        jMenuReportes.setText("Reportes");

        menuRptDocumentosEmpleados.setText("Reporte Documentos Empleados");
        menuRptDocumentosEmpleados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRptDocumentosEmpleadosActionPerformed(evt);
            }
        });
        jMenuReportes.add(menuRptDocumentosEmpleados);

        jMenu1.setText("Flota");

        jMenuItem6.setText("Gastos");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem6);

        jMenuItem8.setText("Combustible");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem8);

        jMenuItem7.setText("Documentos Flota");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem7);

        jMenuItem100.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/moving_truck.png"))); // NOI18N
        jMenuItem100.setText("lista Carros");
        jMenuItem100.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem100ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem100);

        jMenuReportes.add(jMenu1);

        jMenuBar1.add(jMenuReportes);

        jMenuGps.setText("Gps    ");

        jMenuItem104.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/map_edit.png"))); // NOI18N
        jMenuItem104.setText("Mapa de Rutas");
        jMenuItem104.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem104ActionPerformed(evt);
            }
        });
        jMenuGps.add(jMenuItem104);

        jMenuItem105.setIcon(new javax.swing.ImageIcon(getClass().getResource("/GPS/icons/route_25x25.png"))); // NOI18N
        jMenuItem105.setText("Monitoreo");
        jMenuItem105.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem105ActionPerformed(evt);
            }
        });
        jMenuGps.add(jMenuItem105);

        jMenuBar1.add(jMenuGps);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuIngresarEmpleadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuIngresarEmpleadosMouseClicked


    }//GEN-LAST:event_menuIngresarEmpleadosMouseClicked

    private void menuIngresarEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuIngresarEmpleadosActionPerformed
        try {
            IngresarEmpleados fIngresarEmpleados = new IngresarEmpleados(ini);
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
        PrincipalAuxiliarMantenimiento.escritorio.add(form1);
        form1.setSize(escritorio.getSize());
        form1.show();

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

    private void menuArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuArchivoActionPerformed

    }//GEN-LAST:event_menuArchivoActionPerformed

    private void menuCambiarClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuCambiarClaveActionPerformed
        try {
            PrincipalAuxiliarMantenimiento.escritorio.setSize(600, 400);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            CambiarClave form = new CambiarClave(ini);
            PrincipalAuxiliarMantenimiento.escritorio.add(form);
            form.setSize(escritorio.getSize());
            form.setLocation((screenSize.width - escritorio.getSize().width) / 2, (screenSize.height - escritorio.getSize().height) / 2);
            form.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAuxiliarMantenimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuCambiarClaveActionPerformed

    private void menuRptDocumentosEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRptDocumentosEmpleadosActionPerformed
        rptDocumentosEmpleados form;
        form = new rptDocumentosEmpleados(this.ini);
        PrincipalAuxiliarMantenimiento.escritorio.add(form);
        form.setSize(escritorio.getSize());
        form.show();
    }//GEN-LAST:event_menuRptDocumentosEmpleadosActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void mnuProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProveedoresActionPerformed

        try {
            IngresarProveedores ingresarProveedores = new IngresarProveedores(this.ini);
            this.escritorio.add(ingresarProveedores);
            ingresarProveedores.setLocation(((ini.getDimension().width - ingresarProveedores.getSize().width) / 2), ((ini.getDimension().height - ingresarProveedores.getSize().height) / 2) - 30);
            ingresarProveedores.setVisible(true);
            //fReporteDescuentos_Recogidas.cargarInformacion();
            ingresarProveedores.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_mnuProveedoresActionPerformed

    private void mnuSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSalirActionPerformed
        System.exit(0);
    }//GEN-LAST:event_mnuSalirActionPerformed

    private void mnuVehiculosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuVehiculosActionPerformed
        try {
            IngresarCarros fIngresaarCArros = new IngresarCarros(ini);
            this.escritorio.add(fIngresaarCArros);
            fIngresaarCArros.setLocation(((ini.getDimension().width - fIngresaarCArros.getSize().width) / 2), ((ini.getDimension().height - fIngresaarCArros.getSize().height) / 2) - 30);
            fIngresaarCArros.setVisible(true);
            //fReporteDescuentos_Recogidas.cargarInformacion();
            fIngresaarCArros.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_mnuVehiculosActionPerformed

    private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed

        if (ingresarFacturasDeGastos != null) {
            PrincipalAuxiliarMantenimiento.escritorio.add(ingresarFacturasDeGastos);
            ingresarFacturasDeGastos.setLocation(((ini.getDimension().width - ingresarFacturasDeGastos.getSize().width) / 2), ((ini.getDimension().height - ingresarFacturasDeGastos.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            ingresarFacturasDeGastos.setVisible(true);
            ingresarFacturasDeGastos.show();
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void miAgregarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miAgregarFacturaActionPerformed

        if (ingresarFacturasDeGastos != null) {
            PrincipalAuxiliarMantenimiento.escritorio.add(ingresarFacturasDeGastos);
            ingresarFacturasDeGastos.setLocation(((ini.getDimension().width - ingresarFacturasDeGastos.getSize().width) / 2), ((ini.getDimension().height - ingresarFacturasDeGastos.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            new Thread(new HiloListadoDeSucursales(this.ini)).start();
            ingresarFacturasDeGastos.setVisible(true);
            ingresarFacturasDeGastos.show();
        }

    }//GEN-LAST:event_miAgregarFacturaActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed

        if (fReporteGastosFlota != null) {
            PrincipalAuxiliarMantenimiento.escritorio.add(fReporteGastosFlota);
            fReporteGastosFlota.setLocation(((ini.getDimension().width - fReporteGastosFlota.getSize().width) / 2), ((ini.getDimension().height - fReporteGastosFlota.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            fReporteGastosFlota.setVisible(true);
            fReporteGastosFlota.show();
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void miMantenimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMantenimientosActionPerformed
        FAgregarMantenimientoVehiculo fAgregarMantenimientoVehiculo = new FAgregarMantenimientoVehiculo(ini);
        PrincipalAuxiliarMantenimiento.escritorio.add(fAgregarMantenimientoVehiculo);
        fAgregarMantenimientoVehiculo.setLocation(((ini.getDimension().width - fAgregarMantenimientoVehiculo.getSize().width) / 2), ((ini.getDimension().height - fAgregarMantenimientoVehiculo.getSize().height) / 2) - 35);
        fAgregarMantenimientoVehiculo.setVisible(true);
        fAgregarMantenimientoVehiculo.show();

    }//GEN-LAST:event_miMantenimientosActionPerformed

    private void miActualizaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miActualizaClienteActionPerformed

    }//GEN-LAST:event_miActualizaClienteActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        fIngresarDocumentosVehiculos = new FIngresarDocumentosVehiculos(ini);
        PrincipalAuxiliarMantenimiento.escritorio.add(fIngresarDocumentosVehiculos);
        fIngresarDocumentosVehiculos.setLocation(((ini.getDimension().width - fIngresarDocumentosVehiculos.getSize().width) / 2), ((ini.getDimension().height - fIngresarDocumentosVehiculos.getSize().height) / 2) - 35);
        fIngresarDocumentosVehiculos.setVisible(true);
        fIngresarDocumentosVehiculos.show();

    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed

        FReporteDocumentosVehiculos fReporteDocumentosVehiculos = new FReporteDocumentosVehiculos(ini);
        PrincipalAuxiliarMantenimiento.escritorio.add(fReporteDocumentosVehiculos);
        fReporteDocumentosVehiculos.setLocation(((ini.getDimension().width - fReporteDocumentosVehiculos.getSize().width) / 2), ((ini.getDimension().height - fReporteDocumentosVehiculos.getSize().height) / 2) - 35);
        //form1.setMaximizable(false);
        fReporteDocumentosVehiculos.setVisible(true);
        fReporteDocumentosVehiculos.show();

    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jMenu7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu7ActionPerformed

    }//GEN-LAST:event_jMenu7ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        try {
            IngresarSucursalDeProveedor ingresarSucursalDeProveedor = new IngresarSucursalDeProveedor(this.ini);
            this.escritorio.add(ingresarSucursalDeProveedor);
            ingresarSucursalDeProveedor.setLocation(((ini.getDimension().width - ingresarSucursalDeProveedor.getSize().width) / 2), ((ini.getDimension().height - ingresarSucursalDeProveedor.getSize().height) / 2) - 30);
            ingresarSucursalDeProveedor.setVisible(true);
            //fReporteDescuentos_Recogidas.cargarInformacion();
            ingresarSucursalDeProveedor.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalAdministradorDelSistema.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
        FReporteGastosCombustible fConsumoCombustible = new FReporteGastosCombustible(ini);
        PrincipalAuxiliarMantenimiento.escritorio.add(fConsumoCombustible);
        fConsumoCombustible.setLocation(((ini.getDimension().width - fConsumoCombustible.getSize().width) / 2), ((ini.getDimension().height - fConsumoCombustible.getSize().height) / 2) - 35);
        //form1.setMaximizable(false);
        fConsumoCombustible.setVisible(true);
        fConsumoCombustible.show();

    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        FImportarConsumoCombustible fImportarConsumoCombustible = new FImportarConsumoCombustible(ini);
        PrincipalAuxiliarMantenimiento.escritorio.add(fImportarConsumoCombustible);
        fImportarConsumoCombustible.setLocation(((ini.getDimension().width - fImportarConsumoCombustible.getSize().width) / 2), ((ini.getDimension().height - fImportarConsumoCombustible.getSize().height) / 2) - 35);
        //form1.setMaximizable(false);
        fImportarConsumoCombustible.setVisible(true);
        fImportarConsumoCombustible.show();


    }//GEN-LAST:event_jMenuItem9ActionPerformed

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

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
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
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void miMantenimientos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMantenimientos1ActionPerformed

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
    }//GEN-LAST:event_miMantenimientos1ActionPerformed

    private void jMenuItem104ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem104ActionPerformed
        //        MapViewOptions options = new MapViewOptions();
        //        options.importPlaces();
        //        options.setApiKey("AIzaSyAYy9jlVtsCMtCV783sA45quCVjySZyNR8");
        //

        puntosGps = new PuntosGps(ini);
        this.escritorio.add(puntosGps);
        // puntosGps.add(sample);
        puntosGps.setSize(escritorio.getSize());
        puntosGps.setLocation((ini.getDimension().width - puntosGps.getSize().width) / 2, (ini.getDimension().height - puntosGps.getSize().height) / 2);
        puntosGps.setLocation(new Point(0, 0));
        puntosGps.show();
        puntosGps.setVisible(true);
    }//GEN-LAST:event_jMenuItem104ActionPerformed

    private void jMenuItem105ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem105ActionPerformed

        // PuntosMonitoreo monitoreo = new PuntosMonitoreo(ini);
        // this.escritorio.add(monitoreo);
        // puntosGps.add(sample);
        if (puntosMonitoreo != null) {
            this.escritorio.add(puntosMonitoreo);
            puntosMonitoreo.setSize(escritorio.getSize());
            puntosMonitoreo.setLocation((ini.getDimension().width - puntosMonitoreo.getSize().width) / 2, (ini.getDimension().height - puntosMonitoreo.getSize().height) / 2);
            puntosMonitoreo.setLocation(new Point(0, 0));
            puntosMonitoreo.show();
            puntosMonitoreo.setVisible(true);
        }

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
            java.util.logging.Logger.getLogger(PrincipalAuxiliarMantenimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    new PrincipalAuxiliarMantenimiento().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(PrincipalAuxiliarMantenimiento.class.getName()).log(Level.SEVERE, null, ex);
                }

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
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu10;
    private javax.swing.JMenu jMenu11;
    private javax.swing.JMenu jMenu12;
    private javax.swing.JMenu jMenu13;
    private javax.swing.JMenu jMenu14;
    private javax.swing.JMenu jMenu15;
    private javax.swing.JMenu jMenu16;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenu jMenuAdministracion;
    public static javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuGps;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem100;
    private javax.swing.JMenuItem jMenuItem104;
    private javax.swing.JMenuItem jMenuItem105;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenu jMenuLogistica;
    private javax.swing.JMenu jMenuReportes;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenuItem menuAsignarDocumentos;
    private javax.swing.JMenuItem menuCambiarClave;
    private javax.swing.JMenuItem menuIngresarEmpleados;
    private javax.swing.JMenuItem menuRptDocumentosEmpleados;
    private javax.swing.JMenuItem miActualizaCliente;
    private javax.swing.JMenuItem miAgregarFactura;
    private javax.swing.JMenuItem miMantenimientos;
    private javax.swing.JMenuItem miMantenimientos1;
    private javax.swing.JMenuItem mnuProveedores;
    private javax.swing.JMenuItem mnuSalir;
    private javax.swing.JMenuItem mnuVehiculos;
    // End of variables declaration//GEN-END:variables

    /*Funcion que permite colocar imagen al fondo del escritorio */
    public final void cargarImagen(javax.swing.JDesktopPane jDeskp) {
        try {

            String ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "imagenes/imagenesDeFondo/descarga.jpg";
            File f = new File(ruta);
            InputStream foto1 = new FileInputStream(f);

            BufferedImage image = ImageIO.read(foto1);
            jDeskp.setBorder(new Fondo(image));

        } catch (Exception e) {
            System.out.println("Imagen no disponible");
        }
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
