/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion.formularios;

import aplicacionlogistica.distribucion.Threads.HiloListadoDeAgencias;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeCanalesDeVenta;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeCargos;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeCausalesdeDevolucion;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeCentrosDeCosto;

import aplicacionlogistica.distribucion.Threads.HiloListadoDeContratosPersonas;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeDepartamentos;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeEmpleados;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeEntidadesBancarias;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeEstadosCiviles;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeLineasDeVehiculos;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeMarcasVehiculos;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeMovimientosManifiestosfacturas;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeNivelesDeAcceso;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeRegionales;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeRutasDeDistribucion;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeTiposDeAcceso;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeTiposDeCarrocerias;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeTiposDeCombustibles;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeTiposDeContratosVehiculos;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeTiposDeSangre;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeTiposDeServicio;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeTiposDeVehiculos;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeUsuarios;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeVehiculos;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeZonas;
import aplicacionlogistica.distribucion.PrincipalAuxiliarLogistica;
import aplicacionlogistica.distribucion.PrincipalLogistica;
import mtto.PrincipalMantenimiento;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.PrincipalUsuarioCostumerService;
import aplicacionlogistica.distribucion.PrincipalAdministradorDelSistema;
import aplicacionlogistica.distribucion.PrincipalAsistenteAdministrativo;
import aplicacionlogistica.distribucion.PrincipalUsuarioConsultas;
import aplicacionlogistica.distribucion.Threads.HiloListadoClientes;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeManifiestosSinDescargar;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeProductosCamdun;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeVendedores;
import aplicacionlogistica.distribucion.Threads.HiloListadoDestinosFacturas;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import mtto.documentos.Threads.HiloListadoDeTiposDeMantenimiento;
import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import java.awt.Dimension;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileReader;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * import java.awt.Toolkit; import java.awt.Toolkit;
 *
 * @author VLI_488
 */
public class IngresoAlSistema extends javax.swing.JFrame {

    public int totalFilasDeConsultas = 0;
    public int contadorDeRegistros = 0;
    public int contadorDeCiclos = 0;
    double numeroDeFilasPorCiclos;
    public static boolean band = false;

    int intentos = 0;
    int controlCbx = 0;
    public Inicio ini = null;
    boolean sistemaIniciado = false;

    public IngresoAlSistema(boolean yaHayUna) {
        try {
            String rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");
            ini = new Inicio(new File(rutaDeArchivo + "ReadUs.ini"));
            ini.setRutaDeEntrada(1);

            Properties propiedades = new Properties();
            propiedades.load(new FileReader(rutaDeArchivo + "propiedadesLogistica.properties"));

            ini.setPropiedades(propiedades);

            if (!ini.verificarConexion()) {
                JOptionPane.showMessageDialog(null, "Error en la conexion a Internet ", " Alerta", JOptionPane.ERROR_MESSAGE);
                this.dispose();
                System.exit(0);
                return;
            }
            // new Thread(new HiloListadoDeUsuarios(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start(); //ok 

            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            initComponents();

            /*Si ya hay otra aplicacion corriedo, lo saca del sistema*/
            if (!yaHayUna) {

                JOptionPane.showMessageDialog(this, "No se pudo iniciar la aplicacion porque hay otra instancia en ejecución", "Error...", JOptionPane.WARNING_MESSAGE, new ImageIcon("src/aplicacionlogistica/imagenes/turbo_64x64.png"));
                txtClave.setEnabled(false);
                txtUsuario.setEnabled(false);
                txtClave.setEnabled(false);
                txtUsuario.setEnabled(false);
                System.exit(0);
            }

            /* SE VERIFICA SI HAY CONEXION A  BBDD */
            if (!CConexiones.hayConexion(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota())) {
                cbxRol.setSelectedIndex(0);
                cbxRol.setEnabled(true);
                JOptionPane.showMessageDialog(this, "Lo sentimos, no hay conexión  a Servidor BBDD " + ini.getServerRemota(), "Sin conexion ", 0);

            } else {
                txtClave.setEnabled(false);
                txtUsuario.setEnabled(false);
                btnAceptar.setEnabled(false);
                try {

                   iniciarVariablesDeLogistica();
                } catch (InterruptedException ex) {
                  Logger.getLogger(IngresoAlSistema.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            this.setTitle("Ingreso al sistema de Información de Logistica SIGDL");
            this.setResizable(false);
            this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);
            System.out.println("tiempo carga formulario " + new Date());

//            txtClave.setEnabled(true);
//            txtUsuario.setEnabled(true);
//            this.setVisible(true);
//            txtUsuario.requestFocus();
//            txtUsuario.requestFocus();
        } catch (Exception ex) {
            Logger.getLogger(IngresoAlSistema.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel4 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtUsuario = new javax.swing.JTextField();
        txtClave = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        cbxRol = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        panelFotografia = new org.edisoncor.gui.panel.PanelImage();
        panelFotografia2 = new org.edisoncor.gui.panel.PanelImage();
        btnAceptar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        barraInferior = new javax.swing.JProgressBar();
        barraSuperior = new javax.swing.JProgressBar();
        panelFotografia1 = new org.edisoncor.gui.panel.PanelImage();

        jPanel4.setBackground(new java.awt.Color(51, 255, 255));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 204, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 189, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());
        setIconImages(getIconImages());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        txtUsuario.setToolTipText("Digitar usuario para ingresar al sistema");
        txtUsuario.setEnabled(false);
        txtUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsuarioFocusGained(evt);
            }
        });
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });
        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyPressed(evt);
            }
        });

        txtClave.setToolTipText("Digitar la Clave para ingresar al sistema");
        txtClave.setEnabled(false);
        txtClave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtClaveFocusGained(evt);
            }
        });
        txtClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClaveActionPerformed(evt);
            }
        });
        txtClave.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtClaveKeyPressed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/kuzer.png"))); // NOI18N
        jLabel1.setText("Usuario");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/password.png"))); // NOI18N
        jLabel2.setText("Clave");

        cbxRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Logistica", "Mantenimientos" }));
        cbxRol.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbxRolMouseClicked(evt);
            }
        });
        cbxRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxRolActionPerformed(evt);
            }
        });

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Rol");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtUsuario)
                    .addComponent(txtClave)
                    .addComponent(cbxRol, 0, 179, Short.MAX_VALUE))
                .addGap(63, 63, 63))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelFotografia.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panelFotografia.setToolTipText("");
        panelFotografia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/logo_ogistica.jpeg"))); // NOI18N

        javax.swing.GroupLayout panelFotografiaLayout = new javax.swing.GroupLayout(panelFotografia);
        panelFotografia.setLayout(panelFotografiaLayout);
        panelFotografiaLayout.setHorizontalGroup(
            panelFotografiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 532, Short.MAX_VALUE)
        );
        panelFotografiaLayout.setVerticalGroup(
            panelFotografiaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        panelFotografia2.setBorder(null);
        panelFotografia2.setToolTipText("");
        panelFotografia2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/logo_ogistica_2.jpeg"))); // NOI18N

        btnAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/clean.png"))); // NOI18N
        btnAceptar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAceptarMouseClicked(evt);
            }
        });
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });
        btnAceptar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAceptarKeyPressed(evt);
            }
        });

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/exit.png"))); // NOI18N
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        barraInferior.setValue(5);

        barraSuperior.setStringPainted(true);

        javax.swing.GroupLayout panelFotografia2Layout = new javax.swing.GroupLayout(panelFotografia2);
        panelFotografia2.setLayout(panelFotografia2Layout);
        panelFotografia2Layout.setHorizontalGroup(
            panelFotografia2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFotografia2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
            .addComponent(barraSuperior, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 536, Short.MAX_VALUE)
            .addComponent(barraInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        panelFotografia2Layout.setVerticalGroup(
            panelFotografia2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFotografia2Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(panelFotografia2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );

        panelFotografia1.setBackground(new java.awt.Color(255, 255, 255));
        panelFotografia1.setBorder(null);
        panelFotografia1.setToolTipText("");
        panelFotografia1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/logoDistrilog.png"))); // NOI18N

        javax.swing.GroupLayout panelFotografia1Layout = new javax.swing.GroupLayout(panelFotografia1);
        panelFotografia1.setLayout(panelFotografia1Layout);
        panelFotografia1Layout.setHorizontalGroup(
            panelFotografia1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        panelFotografia1Layout.setVerticalGroup(
            panelFotografia1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(panelFotografia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(panelFotografia2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelFotografia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(106, 106, 106)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelFotografia1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addComponent(panelFotografia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(panelFotografia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 291, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnAceptarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAceptarKeyPressed

    }//GEN-LAST:event_btnAceptarKeyPressed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed

        if (controlCbx == 0) {
            JOptionPane.showMessageDialog(this, "Debe selecccionar una opcion ", "Seleccione una opcio", 0);
            return;
        }

        if (validarLogueo()) {
            try {
                try {
                    if (!sistemaIniciado) {
                        sistemaIniciado = true;
                        switch (cbxRol.getSelectedIndex()) {
                            case 0:
                                FormularioConfiguracionDelSistema formulario = new FormularioConfiguracionDelSistema();
                                formulario.toFront();
                                formulario.setVisible(true);
                                formulario.setResizable(false);
                                formulario.setTitle("Formulario para configuración del Sistema");
                                formulario.setLocation((ini.getDimension().width - formulario.getSize().width) / 2, (ini.getDimension().height - formulario.getSize().height) / 2);
                                formulario.show();
                                this.setVisible(false);
                                dispose();
                                break;
                            case 1:
                                if (ini.isEstaClienteActivo()) {
                                    iniciarVariablesDeLogistica();
                                    cargarFormularioPpal();
                                } else {
                                    JOptionPane.showMessageDialog(this, "No se pudo establcer conexion con el servidor, cliente inactivo ", "Cliente no activo", 0);
                                }
                                break;
                            case 2:

                                iniciarVariablesDeMantenimientos();
                                cargarFormularioPpal();
                                break;

                        }

//                        if (cbxRol.getSelectedItem().equals("Administrador")) {
//                            FormularioConfiguracionDelSistema formulario = new FormularioConfiguracionDelSistema();
//                            formulario.toFront();
//                            formulario.setVisible(true);
//                            formulario.setResizable(false);
//                            formulario.setTitle("Formulario para configuración del Sistema");
//                            formulario.setLocation((ini.getDimension().width - formulario.getSize().width) / 2, (ini.getDimension().height - formulario.getSize().height) / 2);
//                            formulario.show();
//                            this.setVisible(false);
//                            dispose();
//
//                        } else if (ini.isEstaClienteActivo()) {
//                            cargarFormularioPpal();
//                        } else {
//                            JOptionPane.showMessageDialog(this, "No se pudo establcer conexion con el servidor, cliente inactivo ", "Cliente no activo", 0);
//                        }
                    }

                } catch (Throwable ex) {
                    Logger.getLogger(IngresoAlSistema.class.getName()).log(Level.SEVERE, null, ex);
                }

            } // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here:
            catch (Exception ex) {
                Logger.getLogger(IngresoAlSistema.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnAceptarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAceptarMouseClicked

    }//GEN-LAST:event_btnAceptarMouseClicked

    private void txtClaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClaveKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnAceptar.requestFocus();

        }
    }//GEN-LAST:event_txtClaveKeyPressed

    private void txtClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClaveActionPerformed

    private void txtClaveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClaveFocusGained
        txtClave.setSelectionStart(0);
        txtClave.setSelectionEnd(txtClave.getText().length());
    }//GEN-LAST:event_txtClaveFocusGained

    private void txtUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtClave.requestFocus();
        }
    }//GEN-LAST:event_txtUsuarioKeyPressed

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void txtUsuarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioFocusGained
        txtUsuario.setSelectionStart(0);
        txtUsuario.setSelectionEnd(txtUsuario.getText().length());
    }//GEN-LAST:event_txtUsuarioFocusGained

    private void cbxRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxRolActionPerformed
        switch (cbxRol.getSelectedIndex()) {
            case 0:
                controlCbx++;
                break;
            case 1:
                controlCbx++;
                new Thread(new HiloListadoDeUsuarios(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start(); //ok 

                break;
            case 2:
                new Thread(new HiloListadoDeUsuarios(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros, cbxRol.getSelectedIndex())).start(); //ok 

                controlCbx++;
                break;

        }
        if (controlCbx > 0) {
            txtClave.setEnabled(true);
            txtUsuario.setEnabled(true);
            btnAceptar.setEnabled(true);
            txtUsuario.requestFocus();
        }
    }//GEN-LAST:event_cbxRolActionPerformed

    private void cbxRolMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbxRolMouseClicked

// TODO add your handling code here:
    }//GEN-LAST:event_cbxRolMouseClicked

    private boolean validarLogueo() {
        intentos += 1;
        boolean verificado = false;
        String msg = null;
        if (intentos <= 3) {
            char passArray[] = txtClave.getPassword();
            for (int i = 0; i < passArray.length; i++) {
                char c = passArray[i];

            }

            String pass = new String(passArray);

            // VALIDA SI LOS CAMPOS DE TEXTO ESTAN VACIOS
            if (txtUsuario.getText().isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No pueden haber campos vacíos", "LLenar los dos campos", 1);
                txtUsuario.setEnabled(true);
                txtUsuario.setEditable(true);
                txtClave.setEnabled(true);
                txtClave.setEditable(true);
                txtUsuario.requestFocus();

            } else {

                switch (cbxRol.getSelectedIndex()) {
                    case 0:
                        if (this.txtUsuario.getText().equals("lopez164") && pass.equals("jslslpzmjc")) {
                            verificado = true;

                        } else {
                            JOptionPane.showMessageDialog(this, "Usuario no autorizado para configurar el sistema", "Usuario no autorizado", 1);
                            verificado = false;

                        }
                        break;
                    case 1:
                        //String psw=Inicio.cifrar(pass); wRYIvKPLN8RvZSaYCUs9Ag

                        for (CUsuarios usu : ini.getListaDeUsuarios()) {
                            try {
                                String u = Inicio.cifrar(this.txtUsuario.getText()); // 6HC__mU-RKpLkO8i2PYAXw
                                String p = (Inicio.cifrar(pass));

                                if (usu.getNombreUsuario().equals(Inicio.cifrar(this.txtUsuario.getText())) && usu.getClaveUsuario().equals(Inicio.cifrar(pass))) {
                                    verificado = true;

                                    ini.setUser(usu);

                                    if (usu.getActivoUsuario() == 0) {
                                        JOptionPane.showMessageDialog(this, "El usuario actual " + this.txtUsuario.getText() + ", No se encuentra "
                                                + "activo.\n Consultar con el administrador ..!", "Usuario No Activo", 0);
                                        verificado = false;
                                        return false;
                                    }
                                    break;
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(IngresoAlSistema.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                        break;
                    case 2:
                        for (CUsuarios usu : ini.getListaDeUsuarios()) {
                            try {
                                String u = Inicio.cifrar(this.txtUsuario.getText()); // 6HC__mU-RKpLkO8i2PYAXw
                                String p = (Inicio.cifrar(pass));

                                if (usu.getNombreUsuario().equals(Inicio.cifrar(this.txtUsuario.getText())) && usu.getClaveUsuario().equals(Inicio.cifrar(pass))) {
                                    verificado = true;

                                    ini.setUser(usu);

                                    if (usu.getActivoUsuario() == 0) {
                                        JOptionPane.showMessageDialog(this, "El usuario actual " + this.txtUsuario.getText() + ", No se encuentra "
                                                + "activo.\n Consultar con el administrador ..!", "Usuario No Activo", 0);
                                        verificado = false;
                                        return false;
                                    }
                                    break;
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(IngresoAlSistema.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                        break;

                }
                /**
                 * **************************************************************************************************************
                 */
//                try {
//                    if (cbxRol.getSelectedItem().equals("Administrador")) {
//                        if (this.txtUsuario.getText().equals("lopez164") && pass.equals("jslslpzmjc")) {
//                            verificado = true;
//
//                        } else {
//                            JOptionPane.showMessageDialog(this, "Usuario no autorizado para configurar el sistema", "Usuario no autorizado", 1);
//                            verificado = false;
//
//                        }
//                    } else {
//                        //String psw=Inicio.cifrar(pass); wRYIvKPLN8RvZSaYCUs9Ag
//
//                        for (CUsuarios usu : ini.getListaDeUsuarios()) {
//                            String u = Inicio.cifrar(this.txtUsuario.getText()); // 6HC__mU-RKpLkO8i2PYAXw
//                            String p = (Inicio.cifrar(pass));
//
//                            if (usu.getNombreUsuario().equals(Inicio.cifrar(this.txtUsuario.getText())) && usu.getClaveUsuario().equals(Inicio.cifrar(pass))) {
//                                verificado = true;
//
//                                ini.setUser(usu);
//
//                                if (usu.getActivoUsuario() == 0) {
//                                    JOptionPane.showMessageDialog(this, "El usuario actual " + this.txtUsuario.getText() + ", No se encuentra "
//                                            + "activo.\n Consultar con el administrador ..!", "Usuario No Activo", 0);
//                                    verificado = false;
//                                    return false;
//                                }
//                                break;
//                            }
//
//                        }
//                    }

                if (!verificado) {

                    switch (intentos) {
                        case 1:
                            msg = "Lleva el primer intento...";
                            txtUsuario.setEnabled(true);
                            txtUsuario.setEditable(true);
                            txtClave.setEnabled(true);
                            txtClave.setEditable(true);
                            txtUsuario.requestFocus();
                            break;
                        case 2:
                            msg = "Lleva el segundo intento...";
                            break;
                        case 3:
                            msg = "Lleva el tercer intento...";
                            break;
                    }

                    JOptionPane.showMessageDialog(this, msg + ", " + msg, "Error Al ingresar", 1);
                    txtUsuario.requestFocus();
                }

//                } catch (Exception ex) {
//                    Logger.getLogger(IngresoAlSistema.class.getName()).log(Level.SEVERE, null, ex);
//                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "Ha sobrepasado el número de intentos ", "Intento Fallido", 0);
            this.dispose();
            System.exit(0);
        }

        return verificado;

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {

            NimRODLookAndFeel nf = new NimRODLookAndFeel();
            javax.swing.UIManager.setLookAndFeel(nf);


            /* Create and display the form */
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    // new IngresoAlSistema().setVisible(true);

                }
            });
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(IngresoAlSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"));

        return retValue;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barraInferior;
    public javax.swing.JProgressBar barraSuperior;
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cbxRol;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private org.edisoncor.gui.panel.PanelImage panelFotografia;
    private org.edisoncor.gui.panel.PanelImage panelFotografia1;
    private org.edisoncor.gui.panel.PanelImage panelFotografia2;
    public javax.swing.JPasswordField txtClave;
    public javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables

    private void cargarFormularioPpal() throws Exception, Throwable {
        try {

            switch (ini.getUser().getTipoAcceso()) {
                case 0:

                    break;
                case 1:
                    PrincipalAdministradorDelSistema fprincipalAdministradorDelSistema = new PrincipalAdministradorDelSistema(this.ini);
                    fprincipalAdministradorDelSistema.setExtendedState(MAXIMIZED_BOTH);
                    fprincipalAdministradorDelSistema.setVisible(true);
                    fprincipalAdministradorDelSistema.setSize(ini.getDimension());
                    fprincipalAdministradorDelSistema.setLocation(0, 0);
                    fprincipalAdministradorDelSistema.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos());
                    dispose();

                    break;
                case 2:
                    JOptionPane.showMessageDialog(this, "No tiene acceso a la aplicacion=2 ", "Intento Fallido", 0);
                    break;
                case 3:
                    PrincipalAsistenteAdministrativo fPrincipalAsistenteAdministrativo = new PrincipalAsistenteAdministrativo(this.ini);
                    fPrincipalAsistenteAdministrativo.setExtendedState(MAXIMIZED_BOTH);
                    fPrincipalAsistenteAdministrativo.setVisible(true);
                    fPrincipalAsistenteAdministrativo.setSize(ini.getDimension());
                    fPrincipalAsistenteAdministrativo.setLocation(0, 0);
                    fPrincipalAsistenteAdministrativo.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos());
                    dispose();
                    break;
                case 4:
                    JOptionPane.showMessageDialog(this, "No tiene acceso a la aplicacion=4 ", "Intento Fallido", 0);
                    break;
                case 5: // USUARIO CON PRIVILEGIOS EN LOGISTICA
                    JOptionPane.showMessageDialog(this, "No tiene acceso a la aplicacion=5 ", "Intento Fallido", 0);
                    break;
                case 6:
                    PrincipalMantenimiento formMantenimientos = new PrincipalMantenimiento(this.ini);
                    formMantenimientos.setExtendedState(MAXIMIZED_BOTH);
                    formMantenimientos.setVisible(true);
                    formMantenimientos.setSize(ini.getDimension());
                    formMantenimientos.setLocation(0, 0);
                    formMantenimientos.setTitle("Sistema de Control de Flota    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos());
                    dispose();
                    break;
                case 7:
                    PrincipalLogistica formpalLogistica = new PrincipalLogistica(this.ini);
                    formpalLogistica.setExtendedState(MAXIMIZED_BOTH);
                    formpalLogistica.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + ini.getUser().getNombres() + " " + ini.getUser().getApellidos());
                    formpalLogistica.setVisible(true);
                    new Thread(new HiloListadoDestinosFacturas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();

                    //this.setVisible(false);
                    dispose();
                    break;

                case 8:

                    PrincipalAuxiliarLogistica principalAuxiliarLogistica = new PrincipalAuxiliarLogistica(this.ini);
                    principalAuxiliarLogistica.setExtendedState(MAXIMIZED_BOTH);
                    principalAuxiliarLogistica.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + ini.getUser().getNombres() + " " + ini.getUser().getApellidos());
                    principalAuxiliarLogistica.setVisible(true);
                    new Thread(new HiloListadoDestinosFacturas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();

                    //this.setVisible(false);
                    dispose();
                    break;

                case 9:
                    PrincipalUsuarioConsultas fPrincipalUsuarioConsultas = new PrincipalUsuarioConsultas(this.ini);
                    fPrincipalUsuarioConsultas.setExtendedState(MAXIMIZED_BOTH);
                    fPrincipalUsuarioConsultas.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + ini.getUser().getNombres() + " " + ini.getUser().getApellidos());
                    fPrincipalUsuarioConsultas.setVisible(true);
                    new Thread(new HiloListadoDestinosFacturas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();

                    //this.setVisible(false);
                    dispose();
                    break;

                case 10:
                    PrincipalUsuarioCostumerService principalUsuarioCostumerService = new PrincipalUsuarioCostumerService(this.ini);
                    principalUsuarioCostumerService.setExtendedState(MAXIMIZED_BOTH);
                    principalUsuarioCostumerService.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + ini.getUser().getNombres() + " " + ini.getUser().getApellidos());
                    principalUsuarioCostumerService.setVisible(true);
                    new Thread(new HiloListadoDestinosFacturas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();

                    //this.setVisible(false);
                    dispose();
                    break;
            }

        } catch (Exception ex) {
            Logger.getLogger(IngresoAlSistema.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void iniciarVariablesDeLogistica() throws InterruptedException {
        boolean isOk = true;

        /*Verifica la conexion a la Base de Datos*/
        if (CConexiones.hayConexion(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota())) {
            new Thread(new HiloListadoDeManifiestosSinDescargar(this.ini, 3)).start();
            new Thread(new HiloListadoDeEmpleados(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeZonas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start(); //ok
            new Thread(new HiloListadoDeRegionales(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();//ok
            new Thread(new HiloListadoDeAgencias(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();//ok
            new Thread(new HiloListadoDeDepartamentos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeEstadosCiviles(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeCargos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeTiposDeSangre(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeTiposDeAcceso(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeNivelesDeAcceso(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeCentrosDeCosto(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();// ok
            new Thread(new HiloListadoDeContratosPersonas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeEntidadesBancarias(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();

            Thread.sleep(300);

            new Thread(new HiloListadoDeCausalesdeDevolucion(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeCanalesDeVenta(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeRutasDeDistribucion(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeMovimientosManifiestosfacturas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            // new Thread(new HiloListadoDestinosFacturas(this.ini, this.barraInferior,this.barraSuperior,this.totalFilasDeConsultas,this.contadorDeRegistros)).start();

            /*Atualiza el listado de clientes de la BBDD*/
            new Thread(new HiloListadoClientes(ini)).start();
            new Thread(new HiloListadoDeProductosCamdun(ini)).start();
            new Thread(new HiloListadoDeVendedores(ini)).start();

            new Thread(new HiloListadoDeMarcasVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeLineasDeVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeTiposDeVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeTiposDeCarrocerias(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeTiposDeServicio(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeTiposDeCombustibles(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeTiposDeContratosVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            // new Thread(new HiloListaDeNumeroDeManifiestosPendientes(this.ini, this.barraInferior,this.barraSuperior,this.totalFilasDeConsultas,this.contadorDeRegistros)).start();

            Thread.sleep(300);

            /*Hilos Mantenimientos */
            new Thread(new HiloListadoDeTiposDeMantenimiento(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            /*new Thread(new HiloListadoDeVehiculosMantenimientos(this.ini)).start(); */

        } else {
            JOptionPane.showMessageDialog(this, "No se pudo iniciar las variables porque No hay Conexion a la BBDD", "Error...", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // Thread.sleep(300);
        //new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 5, 3)).start();
    }

    private void iniciarVariablesDeMantenimientos() throws InterruptedException {
        boolean isOk = true;

        /*Verifica la conexion a la Base de Datos*/
        if (CConexiones.hayConexion(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota())) {
            //new Thread(new HiloListadoDeManifiestosSinDescargar(this.ini, 3)).start();
            new Thread(new HiloListadoDeEmpleados(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeZonas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start(); //ok
            new Thread(new HiloListadoDeRegionales(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();//ok
            new Thread(new HiloListadoDeAgencias(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();//ok
           new Thread(new HiloListadoDeDepartamentos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
//            new Thread(new HiloListadoDeEstadosCiviles(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
//            new Thread(new HiloListadoDeCargos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
//            new Thread(new HiloListadoDeTiposDeSangre(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
//            new Thread(new HiloListadoDeTiposDeAcceso(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
//            new Thread(new HiloListadoDeNivelesDeAcceso(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
//            new Thread(new HiloListadoDeCentrosDeCosto(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();// ok
//            new Thread(new HiloListadoDeContratosPersonas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
//            new Thread(new HiloListadoDeEntidadesBancarias(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();

            Thread.sleep(300);

//            new Thread(new HiloListadoDeCausalesdeDevolucion(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
//            new Thread(new HiloListadoDeCanalesDeVenta(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
//            new Thread(new HiloListadoDeRutasDeDistribucion(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
//            new Thread(new HiloListadoDeVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
//            new Thread(new HiloListadoDeMovimientosManifiestosfacturas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
//            // new Thread(new HiloListadoDestinosFacturas(this.ini, this.barraInferior,this.barraSuperior,this.totalFilasDeConsultas,this.contadorDeRegistros)).start();

            /*Atualiza el listado de clientes de la BBDD*/
//            new Thread(new HiloListadoClientes(ini)).start();
//            new Thread(new HiloListadoDeProductosCamdun(ini)).start();
//            new Thread(new HiloListadoDeVendedores(ini)).start();
            new Thread(new HiloListadoDeVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros,2)).start();
            new Thread(new HiloListadoDeMarcasVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeLineasDeVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeTiposDeVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeTiposDeCarrocerias(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeTiposDeServicio(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeTiposDeCombustibles(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            new Thread(new HiloListadoDeTiposDeContratosVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            // new Thread(new HiloListaDeNumeroDeManifiestosPendientes(this.ini, this.barraInferior,this.barraSuperior,this.totalFilasDeConsultas,this.contadorDeRegistros)).start();

            Thread.sleep(300);

            /*Hilos Mantenimientos */
            new Thread(new HiloListadoDeTiposDeMantenimiento(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            // new Thread(new HiloListadoDeVehiculosMantenimientos(this.ini)).start(); 

        } else {
            JOptionPane.showMessageDialog(this, "No se pudo iniciar las variables porque No hay Conexion a la BBDD", "Error...", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // Thread.sleep(300);
        //new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 5, 3)).start();
    }

}
