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
import mtto.Administracion.FCrearCuentasPrincipales;
import mtto.Administracion.FCrearCuentasSecundarias;
import mtto.documentos.FIngresarDocumentosVehiculos;
import mtto.documentos.Reportes.FReporteDocumentosVehiculos;
import mtto.documentos.Reportes.FReporteListadoCarrosPropios;
import mtto.documentos.Threads.HiloCargarFormulariosMantenimiento;
import mtto.documentos.Threads.HiloListadoDeCuentasSecundarias;
import mtto.documentos.Threads.HiloListadoDeTiposDeDocumentos;
import mtto.documentos.Threads.HiloListadoDeTiposDeMantenimiento;
import mtto.documentos.Threads.HiloListadoDeCarrosPropios;
import mtto.reportes.FReporteGastosCombustible;
import mtto.vehiculos.Administracion.FCrearDocXTipoDeVehiculo;
import mtto.vehiculos.Administracion.FCrearEstadosVehiculos;
import mtto.vehiculos.Administracion.FCrearLineasXMarcaDeVehiculo;
import mtto.vehiculos.Administracion.FCrearMarcasDeLLantas;
import mtto.vehiculos.Administracion.FCrearMarcasVehiculos;
import mtto.vehiculos.Administracion.FCrearTiposCarrocerias;
import mtto.vehiculos.Administracion.FCrearTiposdeCombustible;
import mtto.vehiculos.Administracion.FCrearTiposdeContratosVehiculos;
import mtto.vehiculos.Administracion.FCrearTiposdeDocumentos;
import mtto.vehiculos.Administracion.FCrearTiposdeServicios;
import mtto.vehiculos.Administracion.FCrearTiposdeVehiculos;

/**
 *
 * @author VLI_488
 */
public class PrincipalMantenimiento extends javax.swing.JFrame {

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

    public PrincipalMantenimiento() throws Exception {
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

    public PrincipalMantenimiento(Inicio ini) throws Exception {

        //initComponents();
        this.ini = ini;

        if (this.ini.isEstaClienteActivo()) {
            //if (ya) {
            initComponents();
            PrincipalMantenimiento.escritorio.setSize(this.ini.getDimension());
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
                //new Thread(new HiloListadoDeTiposDeMantenimiento(this.ini)).start();
                new Thread(new HiloListadoDeProveedores(this.ini)).start();
                new Thread(new HiloListadoDeSucursales(this.ini)).start();
                new Thread(new HiloListadoDeTiposDeDocumentos(this.ini)).start();
                //new Thread(new HiloListadoDeVehiculos(this.ini)).start();
                new Thread(new HiloListadoDeCuentasSecundarias(this.ini)).start();
               // new Thread(new HiloListadoDeDepartamentos(this.ini)).start();
                //new Thread(new HiloListadoDeAgencias(this.ini)).start();

                new Thread(new HiloCargarFormulariosMantenimiento(this)).start();

                // new Thread(new HiloListadoDeSucursales(this.ini)).start();
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
        jMenu39 = new javax.swing.JMenu();
        jMenuItem97 = new javax.swing.JMenuItem();
        jMenuItem98 = new javax.swing.JMenuItem();
        jMenu15 = new javax.swing.JMenu();
        mnuVehiculos = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu9 = new javax.swing.JMenu();
        miMantenimientos = new javax.swing.JMenuItem();
        jMenu8 = new javax.swing.JMenu();
        jMenu10 = new javax.swing.JMenu();
        jMenu11 = new javax.swing.JMenu();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu20 = new javax.swing.JMenu();
        jMenu18 = new javax.swing.JMenu();
        jMenu38 = new javax.swing.JMenu();
        jMenuItem95 = new javax.swing.JMenuItem();
        jMenuItem96 = new javax.swing.JMenuItem();
        jMenuItem27 = new javax.swing.JMenuItem();
        jMenuItem28 = new javax.swing.JMenuItem();
        jMenuItem84 = new javax.swing.JMenuItem();
        jMenuItem85 = new javax.swing.JMenuItem();
        jMenuItem86 = new javax.swing.JMenuItem();
        jMenuItem87 = new javax.swing.JMenuItem();
        jMenuItem88 = new javax.swing.JMenuItem();
        jMenuItem89 = new javax.swing.JMenuItem();
        jMenuItem90 = new javax.swing.JMenuItem();
        jMenuItem91 = new javax.swing.JMenuItem();
        jMenuItem92 = new javax.swing.JMenuItem();
        jMenuItem94 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenu12 = new javax.swing.JMenu();
        jMenuItem11 = new javax.swing.JMenuItem();
        menuAsignarDocumentos = new javax.swing.JMenuItem();
        jMenuLogistica = new javax.swing.JMenu();
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
        jMenu17 = new javax.swing.JMenu();
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

        menuCambiarClave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Key.png"))); // NOI18N
        menuCambiarClave.setText("Cambio de Clave");
        menuCambiarClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuCambiarClaveActionPerformed(evt);
            }
        });
        menuArchivo.add(menuCambiarClave);

        mnuSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        mnuSalir.setText("Salir");
        mnuSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSalirActionPerformed(evt);
            }
        });
        menuArchivo.add(mnuSalir);

        jMenuBar1.add(menuArchivo);

        jMenuAdministracion.setText("Administracion");

        jMenu14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Briefcase.png"))); // NOI18N
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

        jMenu39.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenu39.setText("Cuentas");

        jMenuItem97.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem97.setText("Cta. Ppal");
        jMenuItem97.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem97ActionPerformed(evt);
            }
        });
        jMenu39.add(jMenuItem97);

        jMenuItem98.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem98.setText("Cta. Secundaria");
        jMenuItem98.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem98ActionPerformed(evt);
            }
        });
        jMenu39.add(jMenuItem98);

        jMenu14.add(jMenu39);

        jMenuAdministracion.add(jMenu14);

        jMenu15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/lorry.png"))); // NOI18N
        jMenu15.setText("vehiculos");

        mnuVehiculos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/lorry_add.png"))); // NOI18N
        mnuVehiculos.setText("Agregar vehiculos");
        mnuVehiculos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuVehiculosActionPerformed(evt);
            }
        });
        jMenu15.add(mnuVehiculos);

        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/page_add.png"))); // NOI18N
        jMenuItem3.setText("Asignar Documentos");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem3);

        jMenu9.setText("Mantenimientos");

        miMantenimientos.setText("Mantenimiento ");
        miMantenimientos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miMantenimientosActionPerformed(evt);
            }
        });
        jMenu9.add(miMantenimientos);

        jMenu15.add(jMenu9);

        jMenu8.setText("Ordenes");
        jMenu15.add(jMenu8);

        jMenu10.setText("LLantas");
        jMenu15.add(jMenu10);

        jMenu11.setText("Combustibles");
        jMenu15.add(jMenu11);

        jMenuItem5.setText("ingresar Facturas");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu15.add(jMenuItem5);

        jMenu20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Equipment.png"))); // NOI18N
        jMenu20.setText("Administracion");

        jMenu18.setText("Financiero");

        jMenu38.setText("Proveedores");

        jMenuItem95.setText("Proveedores");
        jMenuItem95.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem95ActionPerformed(evt);
            }
        });
        jMenu38.add(jMenuItem95);

        jMenuItem96.setText("Sucursales");
        jMenuItem96.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem96ActionPerformed(evt);
            }
        });
        jMenu38.add(jMenuItem96);

        jMenu18.add(jMenu38);

        jMenu20.add(jMenu18);

        jMenuItem27.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/lorry.png"))); // NOI18N
        jMenuItem27.setText("Lineas X Marca");
        jMenuItem27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem27ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem27);

        jMenuItem28.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/lorry.png"))); // NOI18N
        jMenuItem28.setText("Tipos Documentos");
        jMenuItem28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem28ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem28);

        jMenuItem84.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem84.setText("Marcas Vehiculos");
        jMenuItem84.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem84ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem84);

        jMenuItem85.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem85.setText("Tipos Carrocerias");
        jMenuItem85.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem85ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem85);

        jMenuItem86.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem86.setText("Tipos Combustible");
        jMenuItem86.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem86ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem86);

        jMenuItem87.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem87.setText("Tipos Contrato");
        jMenuItem87.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem87ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem87);

        jMenuItem88.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem88.setText("Tipos Servicio");
        jMenuItem88.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem88ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem88);

        jMenuItem89.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem89.setText("Tipos Vehiculos");
        jMenuItem89.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem89ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem89);

        jMenuItem90.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem90.setText("Marcas LLantas");
        jMenuItem90.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem90ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem90);

        jMenuItem91.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem91.setText("Estados Vehiculo");
        jMenuItem91.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem91ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem91);

        jMenuItem92.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem92.setText("Tipos Documentos");
        jMenuItem92.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem92ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem92);

        jMenuItem94.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem94.setText("LineasVehiculos");
        jMenuItem94.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem94ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem94);

        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jMenuItem1.setText("Doc xTipo de Vehiculo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu20.add(jMenuItem1);

        jMenu15.add(jMenu20);

        jMenuAdministracion.add(jMenu15);

        jMenu2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Boss.png"))); // NOI18N
        jMenu2.setText("Empleados");
        jMenu2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu2ActionPerformed(evt);
            }
        });

        jMenu12.setText("Conductores");
        jMenu12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu12ActionPerformed(evt);
            }
        });

        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/People.png"))); // NOI18N
        jMenuItem11.setText("Personal");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu12.add(jMenuItem11);

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

        jMenuAdministracion.add(jMenu2);

        jMenuBar1.add(jMenuAdministracion);

        jMenuLogistica.setText("Logistica");

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Upload.png"))); // NOI18N
        jMenu4.setText("importar Registros");

        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        jMenuItem9.setText("Importar Combustibles");
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jMenuItem9);

        jMenuLogistica.add(jMenu4);

        jMenu5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/How-to.png"))); // NOI18N
        jMenu5.setText("Exportar Registros");
        jMenuLogistica.add(jMenu5);

        jMenu13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Equipment.png"))); // NOI18N
        jMenu13.setText("Gastos Operativos");

        miAgregarFactura.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/page_add.png"))); // NOI18N
        miAgregarFactura.setText("Ingresar Facturas");
        miAgregarFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miAgregarFacturaActionPerformed(evt);
            }
        });
        jMenu13.add(miAgregarFactura);

        miActualizaCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Blue pin.png"))); // NOI18N
        miActualizaCliente.setText("Asignar Servicios");
        miActualizaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miActualizaClienteActionPerformed(evt);
            }
        });
        jMenu13.add(miActualizaCliente);

        jMenuLogistica.add(jMenu13);

        miMantenimientos1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Equipment.png"))); // NOI18N
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
            .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 307, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
        PrincipalMantenimiento.escritorio.add(form1);
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
            PrincipalMantenimiento.escritorio.setSize(600, 400);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            CambiarClave form = new CambiarClave(ini);
            PrincipalMantenimiento.escritorio.add(form);
            form.setSize(escritorio.getSize());
            form.setLocation((screenSize.width - escritorio.getSize().width) / 2, (screenSize.height - escritorio.getSize().height) / 2);
            form.show();
        } catch (Exception ex) {
            Logger.getLogger(PrincipalMantenimiento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_menuCambiarClaveActionPerformed

    private void menuRptDocumentosEmpleadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRptDocumentosEmpleadosActionPerformed
        rptDocumentosEmpleados form;
        form = new rptDocumentosEmpleados(this.ini);
        PrincipalMantenimiento.escritorio.add(form);
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
            PrincipalMantenimiento.escritorio.add(ingresarFacturasDeGastos);
            ingresarFacturasDeGastos.setLocation(((ini.getDimension().width - ingresarFacturasDeGastos.getSize().width) / 2), ((ini.getDimension().height - ingresarFacturasDeGastos.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            ingresarFacturasDeGastos.setVisible(true);
            ingresarFacturasDeGastos.show();
        }
    }//GEN-LAST:event_jMenuItem5ActionPerformed

    private void miAgregarFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miAgregarFacturaActionPerformed

        if (ingresarFacturasDeGastos != null) {
            PrincipalMantenimiento.escritorio.add(ingresarFacturasDeGastos);
            ingresarFacturasDeGastos.setLocation(((ini.getDimension().width - ingresarFacturasDeGastos.getSize().width) / 2), ((ini.getDimension().height - ingresarFacturasDeGastos.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            new Thread(new HiloListadoDeSucursales(this.ini)).start();
            ingresarFacturasDeGastos.setVisible(true);
            ingresarFacturasDeGastos.show();
        }

    }//GEN-LAST:event_miAgregarFacturaActionPerformed

    private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed

        if (fReporteGastosFlota != null) {
            PrincipalMantenimiento.escritorio.add(fReporteGastosFlota);
            fReporteGastosFlota.setLocation(((ini.getDimension().width - fReporteGastosFlota.getSize().width) / 2), ((ini.getDimension().height - fReporteGastosFlota.getSize().height) / 2) - 35);
            //form1.setMaximizable(false);
            fReporteGastosFlota.setVisible(true);
            fReporteGastosFlota.show();
        }
    }//GEN-LAST:event_jMenuItem6ActionPerformed

    private void miMantenimientosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miMantenimientosActionPerformed
        FAgregarMantenimientoVehiculo fAgregarMantenimientoVehiculo = new FAgregarMantenimientoVehiculo(ini);
        PrincipalMantenimiento.escritorio.add(fAgregarMantenimientoVehiculo);
        fAgregarMantenimientoVehiculo.setLocation(((ini.getDimension().width - fAgregarMantenimientoVehiculo.getSize().width) / 2), ((ini.getDimension().height - fAgregarMantenimientoVehiculo.getSize().height) / 2) - 35);
        fAgregarMantenimientoVehiculo.setVisible(true);
        fAgregarMantenimientoVehiculo.show();

    }//GEN-LAST:event_miMantenimientosActionPerformed

    private void miActualizaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_miActualizaClienteActionPerformed

    }//GEN-LAST:event_miActualizaClienteActionPerformed

    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed

        FReporteDocumentosVehiculos fReporteDocumentosVehiculos = new FReporteDocumentosVehiculos(ini);
        PrincipalMantenimiento.escritorio.add(fReporteDocumentosVehiculos);
        fReporteDocumentosVehiculos.setLocation(((ini.getDimension().width - fReporteDocumentosVehiculos.getSize().width) / 2), ((ini.getDimension().height - fReporteDocumentosVehiculos.getSize().height) / 2) - 35);
        //form1.setMaximizable(false);
        fReporteDocumentosVehiculos.setVisible(true);
        fReporteDocumentosVehiculos.show();

    }//GEN-LAST:event_jMenuItem7ActionPerformed

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
        PrincipalMantenimiento.escritorio.add(fConsumoCombustible);
        fConsumoCombustible.setLocation(((ini.getDimension().width - fConsumoCombustible.getSize().width) / 2), ((ini.getDimension().height - fConsumoCombustible.getSize().height) / 2) - 35);
        //form1.setMaximizable(false);
        fConsumoCombustible.setVisible(true);
        fConsumoCombustible.show();

    }//GEN-LAST:event_jMenuItem8ActionPerformed

    private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
        FImportarConsumoCombustible fImportarConsumoCombustible = new FImportarConsumoCombustible(ini);
        PrincipalMantenimiento.escritorio.add(fImportarConsumoCombustible);
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

         puntosMonitoreo = new PuntosMonitoreo(ini);
       // this.escritorio.add(monitoreo);
        // puntosGps.add(sample);
        if(puntosMonitoreo != null){
        this.escritorio.add(puntosMonitoreo);
        puntosMonitoreo.setSize(escritorio.getSize());
        puntosMonitoreo.setLocation((ini.getDimension().width - puntosMonitoreo.getSize().width) / 2, (ini.getDimension().height - puntosMonitoreo.getSize().height) / 2);
        puntosMonitoreo.setLocation(new Point(0, 0));
        puntosMonitoreo.show();
        puntosMonitoreo.setVisible(true);
        }
         
    }//GEN-LAST:event_jMenuItem105ActionPerformed

    private void jMenuItem95ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem95ActionPerformed

        try {
            IngresarProveedores fIngresarProveedores = new IngresarProveedores(ini);

            this.escritorio.add(fIngresarProveedores);
            fIngresarProveedores.setLocation(((ini.getDimension().width - fIngresarProveedores.getSize().width) / 2), ((ini.getDimension().height - fIngresarProveedores.getSize().height) / 2) - 30);
            fIngresarProveedores.setResizable(false);
            fIngresarProveedores.setMaximizable(false);
            fIngresarProveedores.setVisible(true);
            fIngresarProveedores.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  IngresarProveedores");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem95ActionPerformed

    private void jMenuItem96ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem96ActionPerformed

        try {
            IngresarSucursalDeProveedor fIngresarSucursalDeProveedor = new IngresarSucursalDeProveedor(ini);

            this.escritorio.add(fIngresarSucursalDeProveedor);
            fIngresarSucursalDeProveedor.setLocation(((ini.getDimension().width - fIngresarSucursalDeProveedor.getSize().width) / 2), ((ini.getDimension().height - fIngresarSucursalDeProveedor.getSize().height) / 2) - 30);
            fIngresarSucursalDeProveedor.setResizable(false);
            fIngresarSucursalDeProveedor.setMaximizable(false);
            fIngresarSucursalDeProveedor.setVisible(true);
            fIngresarSucursalDeProveedor.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  fIngresarSucursalDeProveedor");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem96ActionPerformed

    private void jMenuItem97ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem97ActionPerformed

        try {
            FCrearCuentasPrincipales fCrearCuentasPrincipales = new FCrearCuentasPrincipales(ini);

            this.escritorio.add(fCrearCuentasPrincipales);
            fCrearCuentasPrincipales.setLocation(((ini.getDimension().width - fCrearCuentasPrincipales.getSize().width) / 2), ((ini.getDimension().height - fCrearCuentasPrincipales.getSize().height) / 2) - 30);
            fCrearCuentasPrincipales.setResizable(false);
            fCrearCuentasPrincipales.setMaximizable(false);
            fCrearCuentasPrincipales.setVisible(true);
            fCrearCuentasPrincipales.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  fCrearCuentasPrincipales");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem97ActionPerformed

    private void jMenuItem98ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem98ActionPerformed

        try {
            FCrearCuentasSecundarias fCrearCuentasSecundarias = new FCrearCuentasSecundarias(ini);

            this.escritorio.add(fCrearCuentasSecundarias);
            fCrearCuentasSecundarias.setLocation(((ini.getDimension().width - fCrearCuentasSecundarias.getSize().width) / 2), ((ini.getDimension().height - fCrearCuentasSecundarias.getSize().height) / 2) - 30);
            fCrearCuentasSecundarias.setResizable(false);
            fCrearCuentasSecundarias.setMaximizable(false);
            fCrearCuentasSecundarias.setVisible(true);
            fCrearCuentasSecundarias.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  fCrearCuentasPrincipales");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }   // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem98ActionPerformed

    private void jMenuItem27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem27ActionPerformed

        try {
            FCrearLineasXMarcaDeVehiculo fCrearLineasXMarcaDeVehiculo = new FCrearLineasXMarcaDeVehiculo(ini);

            this.escritorio.add(fCrearLineasXMarcaDeVehiculo);
            fCrearLineasXMarcaDeVehiculo.setLocation(((ini.getDimension().width - fCrearLineasXMarcaDeVehiculo.getSize().width) / 2), ((ini.getDimension().height - fCrearLineasXMarcaDeVehiculo.getSize().height) / 2) - 30);
            fCrearLineasXMarcaDeVehiculo.setVisible(true);
            fCrearLineasXMarcaDeVehiculo.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearLineasXMarcaDeVehiculo");
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
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem28ActionPerformed

    private void jMenuItem84ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem84ActionPerformed
        try {
            FCrearMarcasVehiculos fCrearMarcasVehiculos = new FCrearMarcasVehiculos(ini);

            this.escritorio.add(fCrearMarcasVehiculos);
            fCrearMarcasVehiculos.setLocation(((ini.getDimension().width - fCrearMarcasVehiculos.getSize().width) / 2), ((ini.getDimension().height - fCrearMarcasVehiculos.getSize().height) / 2) - 30);
            fCrearMarcasVehiculos.setResizable(false);
            fCrearMarcasVehiculos.setMaximizable(false);
            fCrearMarcasVehiculos.setVisible(true);
            fCrearMarcasVehiculos.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearMarcasVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem84ActionPerformed

    private void jMenuItem85ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem85ActionPerformed
        try {
            FCrearTiposCarrocerias fCrearTiposCarrocerias = new FCrearTiposCarrocerias(ini);

            this.escritorio.add(fCrearTiposCarrocerias);
            fCrearTiposCarrocerias.setLocation(((ini.getDimension().width - fCrearTiposCarrocerias.getSize().width) / 2), ((ini.getDimension().height - fCrearTiposCarrocerias.getSize().height) / 2) - 30);
            fCrearTiposCarrocerias.setResizable(false);
            fCrearTiposCarrocerias.setMaximizable(false);
            fCrearTiposCarrocerias.setVisible(true);
            fCrearTiposCarrocerias.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearTiposCarrocerias");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jMenuItem85ActionPerformed

    private void jMenuItem86ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem86ActionPerformed
        try {
            FCrearTiposdeCombustible fCrearTiposdeCombustible = new FCrearTiposdeCombustible(ini);

            this.escritorio.add(fCrearTiposdeCombustible);
            fCrearTiposdeCombustible.setLocation(((ini.getDimension().width - fCrearTiposdeCombustible.getSize().width) / 2), ((ini.getDimension().height - fCrearTiposdeCombustible.getSize().height) / 2) - 30);
            fCrearTiposdeCombustible.setResizable(false);
            fCrearTiposdeCombustible.setMaximizable(false);
            fCrearTiposdeCombustible.setVisible(true);
            fCrearTiposdeCombustible.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearTiposdeCombustible");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem86ActionPerformed

    private void jMenuItem87ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem87ActionPerformed
        try {
            FCrearTiposdeContratosVehiculos fCrearTiposdeContratosVehiculos = new FCrearTiposdeContratosVehiculos(ini);

            this.escritorio.add(fCrearTiposdeContratosVehiculos);
            fCrearTiposdeContratosVehiculos.setLocation(((ini.getDimension().width - fCrearTiposdeContratosVehiculos.getSize().width) / 2), ((ini.getDimension().height - fCrearTiposdeContratosVehiculos.getSize().height) / 2) - 30);
            fCrearTiposdeContratosVehiculos.setResizable(false);
            fCrearTiposdeContratosVehiculos.setMaximizable(false);
            fCrearTiposdeContratosVehiculos.setVisible(true);
            fCrearTiposdeContratosVehiculos.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearTiposdeContratosVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem87ActionPerformed

    private void jMenuItem88ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem88ActionPerformed
        try {
            FCrearTiposdeServicios fCrearTiposdeServicios = new FCrearTiposdeServicios(ini);

            this.escritorio.add(fCrearTiposdeServicios);
            fCrearTiposdeServicios.setLocation(((ini.getDimension().width - fCrearTiposdeServicios.getSize().width) / 2), ((ini.getDimension().height - fCrearTiposdeServicios.getSize().height) / 2) - 30);
            fCrearTiposdeServicios.setResizable(false);
            fCrearTiposdeServicios.setMaximizable(false);
            fCrearTiposdeServicios.setVisible(true);
            fCrearTiposdeServicios.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearTiposdeServicios");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem88ActionPerformed

    private void jMenuItem89ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem89ActionPerformed

        try {
            FCrearTiposdeVehiculos fCrearTiposdeVehiculos = new FCrearTiposdeVehiculos(ini);

            this.escritorio.add(fCrearTiposdeVehiculos);
            fCrearTiposdeVehiculos.setLocation(((ini.getDimension().width - fCrearTiposdeVehiculos.getSize().width) / 2), ((ini.getDimension().height - fCrearTiposdeVehiculos.getSize().height) / 2) - 30);
            fCrearTiposdeVehiculos.setResizable(false);
            fCrearTiposdeVehiculos.setMaximizable(false);
            fCrearTiposdeVehiculos.setVisible(true);
            fCrearTiposdeVehiculos.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearTiposdeVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem89ActionPerformed

    private void jMenuItem90ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem90ActionPerformed
        FCrearMarcasDeLLantas fCrearMarcasLLantas = new FCrearMarcasDeLLantas(ini);
        try {

            this.escritorio.add(fCrearMarcasLLantas);
            fCrearMarcasLLantas.setLocation(((ini.getDimension().width - fCrearMarcasLLantas.getSize().width) / 2), ((ini.getDimension().height - fCrearMarcasLLantas.getSize().height) / 2) - 30);
            fCrearMarcasLLantas.setResizable(false);
            fCrearMarcasLLantas.setMaximizable(false);
            fCrearMarcasLLantas.setVisible(true);
            fCrearMarcasLLantas.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearMarcasLLantas");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem90ActionPerformed

    private void jMenuItem91ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem91ActionPerformed

        try {
            FCrearEstadosVehiculos fCrearEstadosVehiculos = new FCrearEstadosVehiculos(ini);

            this.escritorio.add(fCrearEstadosVehiculos);
            fCrearEstadosVehiculos.setLocation(((ini.getDimension().width - fCrearEstadosVehiculos.getSize().width) / 2), ((ini.getDimension().height - fCrearEstadosVehiculos.getSize().height) / 2) - 30);
            fCrearEstadosVehiculos.setResizable(false);
            fCrearEstadosVehiculos.setMaximizable(false);
            fCrearEstadosVehiculos.setVisible(true);
            fCrearEstadosVehiculos.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearEstadosVehiculos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem91ActionPerformed

    private void jMenuItem92ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem92ActionPerformed
        FCrearTiposdeDocumentos fCrearTiposdeDocumentos = new FCrearTiposdeDocumentos(ini);
        try {

            this.escritorio.add(fCrearTiposdeDocumentos);
            fCrearTiposdeDocumentos.setLocation(((ini.getDimension().width - fCrearTiposdeDocumentos.getSize().width) / 2), ((ini.getDimension().height - fCrearTiposdeDocumentos.getSize().height) / 2) - 30);
            fCrearTiposdeDocumentos.setResizable(false);
            fCrearTiposdeDocumentos.setMaximizable(false);
            fCrearTiposdeDocumentos.setVisible(true);
            fCrearTiposdeDocumentos.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearTiposdeDocumentos");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem92ActionPerformed

    private void jMenuItem94ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem94ActionPerformed
        try {
            FCrearLineasXMarcaDeVehiculo fCrearLineasXMarcaDeVehiculo = new FCrearLineasXMarcaDeVehiculo(ini);

            this.escritorio.add(fCrearLineasXMarcaDeVehiculo);
            fCrearLineasXMarcaDeVehiculo.setLocation(((ini.getDimension().width - fCrearLineasXMarcaDeVehiculo.getSize().width) / 2), ((ini.getDimension().height - fCrearLineasXMarcaDeVehiculo.getSize().height) / 2) - 30);
            fCrearLineasXMarcaDeVehiculo.setResizable(false);
            fCrearLineasXMarcaDeVehiculo.setMaximizable(false);
            fCrearLineasXMarcaDeVehiculo.setVisible(true);
            fCrearLineasXMarcaDeVehiculo.show();
            datos = new ArrayList<>();
            datos.add(0, usuario);
            datos.add(1, formulario);
            datos.add(2, "Click en form  FCrearLineasXMarcaDeVehiculo");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
            if (timer.isRunning()) {
                timer.stop();
            }
            timer.start();

            // TODO add your handling code here:
        } catch (Exception ex) {
            Logger.getLogger(PrincipalSuperUsuario.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jMenuItem94ActionPerformed

    private void jMenu12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenu12ActionPerformed

    private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem11ActionPerformed

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
    }//GEN-LAST:event_jMenuItem11ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
       
        try {
            FCrearDocXTipoDeVehiculo fCrearDocXTipoDeVehiculo = new FCrearDocXTipoDeVehiculo(ini);

            this.escritorio.add(fCrearDocXTipoDeVehiculo);
            fCrearDocXTipoDeVehiculo.setLocation(((ini.getDimension().width - fCrearDocXTipoDeVehiculo.getSize().width) / 2), ((ini.getDimension().height - fCrearDocXTipoDeVehiculo.getSize().height) / 2) - 30);
            fCrearDocXTipoDeVehiculo.setVisible(true);
            fCrearDocXTipoDeVehiculo.show();
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
        }
    }//GEN-LAST:event_jMenuItem1ActionPerformed

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
            java.util.logging.Logger.getLogger(PrincipalMantenimiento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    new PrincipalMantenimiento().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(PrincipalMantenimiento.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JMenu jMenu17;
    private javax.swing.JMenu jMenu18;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu20;
    private javax.swing.JMenu jMenu38;
    private javax.swing.JMenu jMenu39;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu8;
    private javax.swing.JMenu jMenu9;
    private javax.swing.JMenu jMenuAdministracion;
    public static javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem100;
    private javax.swing.JMenuItem jMenuItem104;
    private javax.swing.JMenuItem jMenuItem105;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem27;
    private javax.swing.JMenuItem jMenuItem28;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem84;
    private javax.swing.JMenuItem jMenuItem85;
    private javax.swing.JMenuItem jMenuItem86;
    private javax.swing.JMenuItem jMenuItem87;
    private javax.swing.JMenuItem jMenuItem88;
    private javax.swing.JMenuItem jMenuItem89;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItem90;
    private javax.swing.JMenuItem jMenuItem91;
    private javax.swing.JMenuItem jMenuItem92;
    private javax.swing.JMenuItem jMenuItem94;
    private javax.swing.JMenuItem jMenuItem95;
    private javax.swing.JMenuItem jMenuItem96;
    private javax.swing.JMenuItem jMenuItem97;
    private javax.swing.JMenuItem jMenuItem98;
    private javax.swing.JMenu jMenuLogistica;
    private javax.swing.JMenu jMenuReportes;
    private javax.swing.JMenu menuArchivo;
    private javax.swing.JMenuItem menuAsignarDocumentos;
    private javax.swing.JMenuItem menuCambiarClave;
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
