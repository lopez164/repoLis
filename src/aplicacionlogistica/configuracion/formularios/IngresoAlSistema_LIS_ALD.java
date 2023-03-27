/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion.formularios;

import aplicacionlogistica.Threads.JcProgressBar;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeAgencias;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeCanalesDeVenta;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeCargos;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeCausalesdeDevolucion;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeCentrosDeCosto;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeContratosPersonas;
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
import aplicacionlogistica.configuracion.HiloBitacoraMovimientosEnElSistema;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import aplicacionlogistica.costumerService.PrincipalUsuarioCostumerService;
import aplicacionlogistica.distribucion.PrincipalSuperUsuario;
import aplicacionlogistica.distribucion.PrincipalAdministradorDelSistema;
import aplicacionlogistica.distribucion.PrincipalAreaSeguridad;
import aplicacionlogistica.distribucion.PrincipalAsistenteAdministrativo;
import aplicacionlogistica.distribucion.PrincipalFacturacion;
import aplicacionlogistica.distribucion.PrincipalUsuarioAuditoria;
import aplicacionlogistica.distribucion.PrincipalUsuarioConsultas;
import aplicacionlogistica.distribucion.Threads.HiloControlRalenti;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeCuentasBancarias;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeManifiestosSinDescargar;
import aplicacionlogistica.distribucion.Threads.HiloListadoDestinosFacturas;
import aplicacionlogistica.distribucion.integrador.HiloIntegradorTNSLaHielera;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import com.nilo.plaf.nimrod.NimRODLookAndFeel;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UnsupportedLookAndFeelException;
import mtto.documentos.Threads.HiloListadoDeSucursales;
import mtto.documentos.Threads.HiloListadoDeTiposDeDocumentos;
import mtto.documentos.Threads.HiloListadoDeTiposDeMantenimiento;


public class IngresoAlSistema_LIS_ALD extends javax.swing.JFrame {

    List<String> datos = new ArrayList();
    public int totalFilasDeConsultas = 0;
    public int contadorDeRegistros = 0;
    public int contadorDeCiclos = 0;
    double numeroDeFilasPorCiclos;
    public static boolean band = false;

    int intentos = 1;
    public Inicio ini = null;
    boolean sistemaIniciado = false;
    Properties propiedades;

    public IngresoAlSistema_LIS_ALD(Properties propiedades, boolean yaHayUna, int aliado) {
        try {

            
            String ruta = "" + (new File(".").getAbsolutePath()).replace(".", "");
            ini = new Inicio(new File(ruta + "ReadUs.ini"));
            this.propiedades = propiedades;
            
            if(!ini.validarInternet()){
                 JOptionPane.showMessageDialog(this, "Sin Internet");
                 System.exit(0);
            }

            //new CEmpleados.sincronizarEmpleados();
            ini.setPropiedades(this.propiedades);
            ini.setRutaDeEntrada(Integer.parseInt(propiedades.getProperty("idOperador")));
            
            /*Datos del cliente*/
            ini.setNitCliente(propiedades.getProperty("nitCliente"));
            ini.setIdAliado(propiedades.getProperty("idAliado"));
            ini.setNombreDelCliente(propiedades.getProperty("nombreCliente"));
            ini.setDireccionCliente(propiedades.getProperty("direccionCliente"));
            ini.setCiudadCliente(propiedades.getProperty("ciudadCliente"));  
           
            ini.setContactoCliente(propiedades.getProperty("contactoCliente"));
            ini.setEmailCliente(propiedades.getProperty("emailCliente"));
            ini.setTelefonoCliente(propiedades.getProperty("telefonoCliente"));
            ini.setCelularCliente(propiedades.getProperty("celularCliente"));
           
            ini.setPrefijos(propiedades.getProperty("prefijos"));
            ini.setuRLFuente(propiedades.getProperty("uRLFuente"));
            ini.setServidorFuente(propiedades.getProperty("servidorFuente"));
            ini.setDbFuente(propiedades.getProperty("dbFuente"));
            ini.setUserFuente(propiedades.getProperty("userFuente"));
            ini.setPsdFuente(propiedades.getProperty("psdFuente"));
            ini.setGeoPositionCliente(propiedades.getProperty("geoPositionCliente"));

            if (propiedades.getProperty("imprimirMinuta").equals("true")) {
                ini.setImprimirMinuta(true);
            } else {
                ini.setImprimirMinuta(false);
            }

            if (propiedades.getProperty("permitirVariosManifiestos").equals("true")) {
                ini.setPermitirVariosManifiestos(true);
            } else {
                ini.setPermitirVariosManifiestos(false);
            }

            ini.setCodigoAgencia(propiedades.getProperty("codigoAgencia"));
            
            CAgencias ag = new CAgencias(ini, Integer.parseInt(ini.getCodigoAgencia()));
            
            ini.setIdAgencia(ag.getIdAgencia());
            ini.setIdRegional(ag.getIdRegional());
            ini.setIdZona(ag.getIdZona());
            

            /* Se valida que haya acceso a TNS*/
            String val = propiedades.getProperty("uRLFuente");
            if (!val.equals("falso")) {
                /* Se inicia un integrador con TNS que va a actualizar todos los empleados*/
                new Thread(new HiloIntegradorTNSLaHielera(ini, null)).start();
            }
           
            initComponents();
            band = true;
            
             /* Se cargan los usuarios para inicir el sistema*/
            new Thread(new HiloListadoDeUsuarios(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros, 1)).start(); //ok

            

            panelLogo.setIcon(new ImageIcon(ruta + "imagenes/logos/" + propiedades.getProperty("panelLogo"))); // NOI18N
            panelFotografia1.setIcon(new ImageIcon(ruta + "imagenes/logosInicio/" + propiedades.getProperty("panel1"))); // NOI18N
            panelFotografia2.setIcon(new ImageIcon(ruta + "imagenes/logosInicio/" + propiedades.getProperty("panel2"))); // NOI18N

            /*Se verifiac si ya hay otra aplicacion corriedo, lo saca del sistema*/
            if (!yaHayUna) {

                JOptionPane.showMessageDialog(this, "No se pudo iniciar la aplicacion porque \n hay otra instancia en ejecución", "Error...", JOptionPane.WARNING_MESSAGE);
                txtClave.setEnabled(false);
                txtUsuario.setEnabled(false);
                txtClave.setEnabled(false);
                txtUsuario.setEnabled(false);
                System.exit(0);
            }

            if (ini.getConnGPS() == null) {
                ini.setConnGPS();
            }

            System.out.println("tiempo carga formulario " + new Date());
            txtClave.setEnabled(true);
            txtUsuario.setEnabled(true);
            barraSuperior.setValue(0);
            barraInferior.setValue(0);
            
            txtUsuario.requestFocus();
            txtUsuario.requestFocus();
            
        } catch (Exception ex) {
            Logger.getLogger(IngresoAlSistema_LIS_ALD.class.getName()).log(Level.SEVERE, null, ex);
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
        panelFotografia1 = new org.edisoncor.gui.panel.PanelImage();
        panelFotografia2 = new org.edisoncor.gui.panel.PanelImage();
        btnAceptar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        barraInferior = new javax.swing.JProgressBar();
        barraSuperior = new javax.swing.JProgressBar();
        panelLogo = new org.edisoncor.gui.panel.PanelImage();

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

        cbxRol.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Administrador", "Usuario" }));
        cbxRol.setSelectedIndex(1);

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel3.setText("Rol");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtUsuario)
                    .addComponent(txtClave)
                    .addComponent(cbxRol, 0, 252, Short.MAX_VALUE))
                .addGap(28, 28, 28))
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

        panelFotografia1.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panelFotografia1.setToolTipText("");

        javax.swing.GroupLayout panelFotografia1Layout = new javax.swing.GroupLayout(panelFotografia1);
        panelFotografia1.setLayout(panelFotografia1Layout);
        panelFotografia1Layout.setHorizontalGroup(
            panelFotografia1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 497, Short.MAX_VALUE)
        );
        panelFotografia1Layout.setVerticalGroup(
            panelFotografia1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 81, Short.MAX_VALUE)
        );

        panelFotografia2.setBorder(null);
        panelFotografia2.setToolTipText("");

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

        barraInferior.setStringPainted(true);

        barraSuperior.setStringPainted(true);

        javax.swing.GroupLayout panelFotografia2Layout = new javax.swing.GroupLayout(panelFotografia2);
        panelFotografia2.setLayout(panelFotografia2Layout);
        panelFotografia2Layout.setHorizontalGroup(
            panelFotografia2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFotografia2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
            .addComponent(barraSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(barraSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraInferior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );

        panelLogo.setBackground(new java.awt.Color(255, 255, 255));
        panelLogo.setBorder(null);
        panelLogo.setToolTipText("");
        panelLogo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/logos/logoAldPlus.png"))); // NOI18N

        javax.swing.GroupLayout panelLogoLayout = new javax.swing.GroupLayout(panelLogo);
        panelLogo.setLayout(panelLogoLayout);
        panelLogoLayout.setHorizontalGroup(
            panelLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 119, Short.MAX_VALUE)
        );
        panelLogoLayout.setVerticalGroup(
            panelLogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(panelLogo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(panelFotografia2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(panelFotografia1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(88, 88, 88)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelLogo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1)
                .addComponent(panelFotografia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(panelFotografia1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 322, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnAceptarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAceptarKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            try {
                ingresar();
            } catch (Exception ex) {
                Logger.getLogger(IngresoAlSistema_LIS_ALD.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_btnAceptarKeyPressed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed

        try {
            ingresar();
        } catch (Exception ex) {
            Logger.getLogger(IngresoAlSistema_LIS_ALD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAceptarActionPerformed


    private void btnAceptarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAceptarMouseClicked
        try {
            ingresar();
        } catch (Exception ex) {
            Logger.getLogger(IngresoAlSistema_LIS_ALD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnAceptarMouseClicked

    private void txtClaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClaveKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            btnAceptar.setEnabled(true);
            btnAceptar.requestFocus();

        } else {
            btnAceptar.setEnabled(true);
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

    private boolean validarLogueo() {
        //intentos += 1;
        boolean verificado = false;
        String msg = null;
        if (intentos <= 5) {
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

                try {
                    if (cbxRol.getSelectedItem().equals("Administrador")) {
                        if (this.txtUsuario.getText().equals("lopez164") && pass.equals("jslslpzmjc")) {
                            verificado = true;

                        } else {
                            btnAceptar.setEnabled(true);
                            txtUsuario.setEnabled(true);
                            txtClave.setEnabled(true);

                            JOptionPane.showMessageDialog(this, "Usuario no autorizado para configurar el sistema", "Usuario no autorizado", 1);
                            verificado = false;

                        }
                    } else {
                        //String psw=Inicio.cifrar(pass); wRYIvKPLN8RvZSaYCUs9Ag

                        if (ini.getListaDeUsuarios() == null) {
                            btnAceptar.setEnabled(true);
                            txtUsuario.setEnabled(true);
                            txtClave.setEnabled(true);

                            JOptionPane.showMessageDialog(this, "NO se pudo cargar las variables del sistema", "Error de conexion", 1);
                            return false;
                        }

                        for (CUsuarios usu : ini.getListaDeUsuarios()) {
                            String u = Inicio.cifrar(this.txtUsuario.getText()); // 6HC__mU-RKpLkO8i2PYAXw
                            String p = (Inicio.cifrar(pass));

                            if (usu.getNombreUsuario().equals(Inicio.cifrar(this.txtUsuario.getText())) && usu.getClaveUsuario().equals(Inicio.cifrar(pass))) {
                                verificado = true;

                                ini.setUser(usu);

                                if (usu.getActivoUsuario() == 0) {
                                    btnAceptar.setEnabled(true);
                                    txtUsuario.setEnabled(true);
                                    txtClave.setEnabled(true);

                                    JOptionPane.showMessageDialog(this, "El usuario actual " + this.txtUsuario.getText() + ", No se encuentra "
                                            + "activo.\n Consultar con el administrador ..!", "Usuario No Activo", 0);
                                    verificado = false;
                                    return false;
                                }
                                break;
                            }

                        }
                    }

                    if (!verificado) {

                        switch (intentos) {
                            case 1:
                                msg = "Lleva el primer intento...";
                                txtUsuario.setEnabled(true);
                                txtUsuario.setEditable(true);
                                txtClave.setEnabled(true);
                                txtClave.setEditable(true);
                                btnAceptar.setEnabled(false);

                                datos.add(0, txtUsuario.getText().trim());
                                datos.add(1, "IngresoAlSistema");
                                datos.add(2, "primer intento fallido");
                                datos.add(3, "CURRENT_TIMESTAMP");
                                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

                                JOptionPane.showMessageDialog(this, msg, "Error Al ingresar", 1);
                                txtUsuario.requestFocus();

                                break;
                            case 2:
                                msg = "Lleva el segundo intento...";
                                txtUsuario.setEnabled(true);
                                txtUsuario.setEditable(true);
                                txtClave.setEnabled(true);
                                txtClave.setEditable(true);
                                btnAceptar.setEnabled(false);

                                datos.add(0, txtUsuario.getText().trim());
                                datos.add(1, "IngresoAlSistema");
                                datos.add(2, "segundo intento fallido");
                                datos.add(3, "CURRENT_TIMESTAMP");
                                new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

                                JOptionPane.showMessageDialog(this, msg, "Error Al ingresar", 1);
                                txtUsuario.requestFocus();
                                break;
                            case 3:
                                JOptionPane.showMessageDialog(this, "Ha sobrepasado el número de intentos ", "Intento Fallido", 0);
                                this.dispose();
                                System.exit(0);
                        }
                        intentos++;
                        txtUsuario.requestFocus();
                    }
                    txtUsuario.requestFocus();

                } catch (Exception ex) {
                    Logger.getLogger(IngresoAlSistema_LIS_ALD.class.getName()).log(Level.SEVERE, null, ex);
                }

                txtUsuario.requestFocus();
            }

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
            Logger.getLogger(IngresoAlSistema_LIS_ALD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Image getIconImage() {

        String ruta = "" + (new File(".").getAbsolutePath()).replace(".", "") + "imagenes/iconoApp/" + ini.getPropiedades().getProperty("iconoApp");
        Image retValue = Toolkit.getDefaultToolkit().getImage(ruta);
        //getImage(ClassLoader.getSystemResource("aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"));
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
    private org.edisoncor.gui.panel.PanelImage panelFotografia1;
    private org.edisoncor.gui.panel.PanelImage panelFotografia2;
    public org.edisoncor.gui.panel.PanelImage panelLogo;
    public javax.swing.JPasswordField txtClave;
    public javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables

    private void ingresar() throws Exception {
//        btnAceptar.setEnabled(false);
//        txtUsuario.setEnabled(false);
//        txtClave.setEnabled(false);

        if (validarLogueo()) {

            datos.add(0, txtUsuario.getText().trim());
            datos.add(1, "IngresoAlSistema");
            datos.add(2, "Ingresa al sistema");
            datos.add(3, "CURRENT_TIMESTAMP");
            new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();

            try {
                try {

                    if (!sistemaIniciado) {
                        sistemaIniciado = true;
                        if (cbxRol.getSelectedItem().equals("Administrador")) {
                            FormularioConfiguracionDelSistema formulario = new FormularioConfiguracionDelSistema();
                            formulario.toFront();
                            formulario.setVisible(true);
                            formulario.setResizable(false);
                            formulario.setTitle("Formulario para configuración del Sistema");
                            formulario.setLocation((ini.getDimension().width - formulario.getSize().width) / 2, (ini.getDimension().height - formulario.getSize().height) / 2);
                            formulario.show();

                            dispose();

                        // } else if (ini.isEstaClienteActivo()) {
                        } else if (true) {
                            btnAceptar.setEnabled(false);
                           
                            //iniciarVariables();
                           // cargarFormularioPpal();
                            new Thread(new JcProgressBar(this)).start();
                            new Thread(new HiloIngresoAlSistema_Lis(this.ini,this)).start();
                                   
                        } else {
                            JOptionPane.showMessageDialog(this, "No se pudo establcer conexion con el servidor, cliente inactivo ", "Cliente no activo", 0);
                        }

                    }

                } catch (Throwable ex) {
                    Logger.getLogger(IngresoAlSistema_LIS_ALD.class.getName()).log(Level.SEVERE, null, ex);
                }

            } // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here:
            catch (Exception ex) {
                Logger.getLogger(IngresoAlSistema_LIS_ALD.class.getName()).log(Level.SEVERE, null, ex);
            } // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here: // TODO add your handling code here:

        }
    }

    public boolean validarRedServicoConexion() throws HeadlessException {
        boolean verificado = false;

        /* SE VERIFICA SI HAY CONEXION A  INTERNET */
        if (!ini.verificarConexion()) {
            JOptionPane.showMessageDialog(null, "Error en la conexion a Internet ", " Alerta", JOptionPane.ERROR_MESSAGE);
            return verificado;
        }

        if (!ini.isClienteActivo()) {
            JOptionPane.showMessageDialog(null, "Cliente no se encuentra activo", " Alerta", JOptionPane.ERROR_MESSAGE);
            return verificado;
        }
        /* SE VERIFICA SI HAY CONEXION A  BBDD */
        if (!CConexiones.hayConexion(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota())) {
            cbxRol.setSelectedIndex(0);
            cbxRol.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Lo sentimos, no hay conexión  a Servidor BBDD " + ini.getServerRemota(), "Sin conexion ", 0);
            return verificado;
        }
        verificado = true;
        return verificado;
    }

    public void cargarFormularioPpal() throws Exception, Throwable {
        try {

            switch (ini.getUser().getTipoAcceso()) {
                case 0:
                    PrincipalSuperUsuario fPrincipalSuperUsuario = new PrincipalSuperUsuario(this.ini);
                    fPrincipalSuperUsuario.setExtendedState(MAXIMIZED_BOTH);
                    fPrincipalSuperUsuario.setVisible(true);

                    fPrincipalSuperUsuario.setSize(ini.getDimension());
                    fPrincipalSuperUsuario.setLocation(0, 0);
                    fPrincipalSuperUsuario.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos() + "    Perfil : Administrador del Sistema");
                    dispose();

                    break;
                case 1:
                    if (!validarRedServicoConexion()) {
                        return;
                    }
                    PrincipalAdministradorDelSistema fFrincipalAdministradorDelSistema = new PrincipalAdministradorDelSistema(this.ini);
                    fFrincipalAdministradorDelSistema.setExtendedState(MAXIMIZED_BOTH);
                    fFrincipalAdministradorDelSistema.setVisible(true);
                    fFrincipalAdministradorDelSistema.setSize(ini.getDimension());
                    fFrincipalAdministradorDelSistema.setLocation(0, 0);
                    fFrincipalAdministradorDelSistema.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos() + "    Perfil : Administrador del Sistema");
                    dispose();

                    break;
                case 2:
                    if (!validarRedServicoConexion()) {
                        return;
                    }
                    PrincipalAreaSeguridad fPrincipalAreaSeguridad = new PrincipalAreaSeguridad(this.ini);
                    fPrincipalAreaSeguridad.setExtendedState(MAXIMIZED_BOTH);
                    fPrincipalAreaSeguridad.setVisible(true);
                    fPrincipalAreaSeguridad.setSize(ini.getDimension());
                    fPrincipalAreaSeguridad.setLocation(0, 0);
                    fPrincipalAreaSeguridad.setTitle("Sistema de Información Flota de Transporte    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos() + "    Perfil : Area de Seguridad");
                    dispose();

                    break;

                case 3:
                    if (!validarRedServicoConexion()) {
                        return;
                    }
                    PrincipalAsistenteAdministrativo fPrincipalAsistenteAdministrativo = new PrincipalAsistenteAdministrativo(this.ini);
                    fPrincipalAsistenteAdministrativo.setExtendedState(MAXIMIZED_BOTH);
                    fPrincipalAsistenteAdministrativo.setVisible(true);
                    fPrincipalAsistenteAdministrativo.setSize(ini.getDimension());
                    fPrincipalAsistenteAdministrativo.setLocation(0, 0);
                    fPrincipalAsistenteAdministrativo.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos() + "    Perfil : Asistente Administrativo");
                    dispose();
                    break;
                case 4:
                    if (!validarRedServicoConexion()) {
                        return;
                    }
                    PrincipalUsuarioAuditoria fPrincipalUsuarioAuditoria = new PrincipalUsuarioAuditoria(this.ini);
                    fPrincipalUsuarioAuditoria.setExtendedState(MAXIMIZED_BOTH);
                    fPrincipalUsuarioAuditoria.setVisible(true);
                    fPrincipalUsuarioAuditoria.setSize(ini.getDimension());
                    fPrincipalUsuarioAuditoria.setLocation(0, 0);
                    fPrincipalUsuarioAuditoria.setTitle("Sistema de Información Logistica(Auditoria)    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos() + "    Perfil : Auditoria ");
                    dispose();
                    break;

                case 5: // USUARIO facturacion
                    if (!validarRedServicoConexion()) {
                        return;
                    }
                    PrincipalFacturacion fPrincipalFacturacion = new PrincipalFacturacion(this.ini);
                    fPrincipalFacturacion.setExtendedState(MAXIMIZED_BOTH);
                    fPrincipalFacturacion.setVisible(true);
                    fPrincipalFacturacion.setSize(ini.getDimension());
                    fPrincipalFacturacion.setLocation(0, 0);
                    fPrincipalFacturacion.setTitle("Sistema de Información Flota de Transporte    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos() + "    Perfil : Facturacion ");
                    dispose();
                    break;

                case 6:
                    if (!validarRedServicoConexion()) {
                        return;
                    }
                    PrincipalMantenimiento formMantenimientos = new PrincipalMantenimiento(this.ini);
                    formMantenimientos.setExtendedState(MAXIMIZED_BOTH);
                    formMantenimientos.setVisible(true);
                    formMantenimientos.setSize(ini.getDimension());
                    formMantenimientos.setLocation(0, 0);
                    formMantenimientos.setTitle("Sistema de Información Flota de Transporte    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos() + "    Perfil : Mantenimientos ");
                    band = false;
                    barraInferior.setValue(100);
                    barraSuperior.setValue(100);
                    dispose();
                    break;

                case 7:
                    if (!validarRedServicoConexion()) {
                        return;
                    }
                    PrincipalLogistica principalLogistica = new PrincipalLogistica(this.ini);
                    principalLogistica.setExtendedState(MAXIMIZED_BOTH);
                    principalLogistica.setVisible(true);
                    principalLogistica.setSize(ini.getDimension());
                    principalLogistica.setLocation(0, 0);
                    principalLogistica.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + this.ini.getUser().getNombres() + " " + this.ini.getUser().getApellidos() + "    Perfil : Logistico");
                    dispose();
                    //this.setVisible(false)
                    break;

                case 8:
                    if (!validarRedServicoConexion()) {
                        return;
                    }
                    PrincipalAuxiliarLogistica principalAuxiliarLogistica = new PrincipalAuxiliarLogistica(this.ini);
                    principalAuxiliarLogistica.setExtendedState(MAXIMIZED_BOTH);
                    principalAuxiliarLogistica.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + ini.getUser().getNombres() + " " + ini.getUser().getApellidos() + "    Perfil : Auxiliar de Logistica");
                    principalAuxiliarLogistica.setVisible(true);
                    principalAuxiliarLogistica.setSize(ini.getDimension());
                    principalAuxiliarLogistica.setLocation(0, 0);

                    new Thread(new HiloListadoDestinosFacturas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();

                    //this.setVisible(false);
                    dispose();
                    break;

                case 9:  // usuario paraj Consultas
                    if (!validarRedServicoConexion()) {
                        return;
                    }
                    PrincipalUsuarioConsultas principalUsuarioConsultas = new PrincipalUsuarioConsultas(this.ini);
                    principalUsuarioConsultas.setExtendedState(MAXIMIZED_BOTH);
                    principalUsuarioConsultas.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + ini.getUser().getNombres() + " " + ini.getUser().getApellidos() + "    Perfil : Usuario de Consultas");
                    principalUsuarioConsultas.setVisible(true);
                    principalUsuarioConsultas.setSize(ini.getDimension());
                    principalUsuarioConsultas.setLocation(0, 0);

                    new Thread(new HiloListadoDestinosFacturas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();

                    //this.setVisible(false);
                    dispose();
                    break;

                case 10: // Costumer Service
                    if (!validarRedServicoConexion()) {
                        return;
                    }
                    PrincipalUsuarioCostumerService principalUsuarioCostumerService = new PrincipalUsuarioCostumerService(this.ini);
                    principalUsuarioCostumerService.setExtendedState(MAXIMIZED_BOTH);
                    principalUsuarioCostumerService.setTitle("Sistema de Información Logistica    -- Usuario de Sistema: " + ini.getUser().getNombres() + " " + ini.getUser().getApellidos() + "    Perfil : Servicio al cliente");
                    principalUsuarioCostumerService.setVisible(true);
                    principalUsuarioCostumerService.setSize(ini.getDimension());
                    principalUsuarioCostumerService.setLocation(0, 0);

                    new Thread(new HiloListadoDestinosFacturas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                    //this.setVisible(false);
                    dispose();
                    break;
            }

        } catch (Exception ex) {
            Logger.getLogger(IngresoAlSistema_LIS_ALD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void iniciarVariables() throws InterruptedException {
        boolean isOk = true;


        /*Hilo que controla el tiempo de espera de l conexion en 120 segundos**/
        new Thread(new HiloControlRalenti(ini)).start();

        /*Verifica la conexion a la Base de Datos */
        if (CConexiones.hayConexion(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota())) {
            ini.setRalenti(130);

            try {
                //new Thread(new HiloListadoDeUsuarios(this.ini, this.barraInferior,this.barraSuperior,this.totalFilasDeConsultas,this.contadorDeRegistros)).start(); //ok 
                new Thread(new HiloListadoDeEmpleados(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeZonas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start(); //ok
                new Thread(new HiloListadoDeRegionales(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();//ok
                new Thread(new HiloListadoDeAgencias(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();//ok
                // new Thread(new HiloListadoDeDepartamentos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                //new Thread(new HiloListadoDeCiudades(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeEstadosCiviles(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeCargos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeTiposDeSangre(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeTiposDeAcceso(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeNivelesDeAcceso(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeCentrosDeCosto(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();// ok
                new Thread(new HiloListadoDeContratosPersonas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeEntidadesBancarias(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeCuentasBancarias(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
               
               // Thread.sleep(300);

                new Thread(new HiloListadoDeCausalesdeDevolucion(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeCanalesDeVenta(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeRutasDeDistribucion(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                //new Thread(new HiloListadoDeVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeMovimientosManifiestosfacturas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                // new Thread(new HiloListadoDestinosFacturas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();

                /*Atualiza el listado de clientes de la BBDD*/
                // new Thread(new HiloListadoClientes(ini)).start();
                // new Thread(new HiloListadoDeProductosCamdun(ini)).start();
                //new Thread(new HiloListadoDeVendedores(ini)).start();
                 new Thread(new HiloListadoDeVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeMarcasVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeLineasDeVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeTiposDeVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeTiposDeCarrocerias(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeTiposDeServicio(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeTiposDeCombustibles(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeTiposDeContratosVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeTiposDeMantenimiento(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeTiposDeDocumentos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                new Thread(new HiloListadoDeSucursales(ini)).start();

                new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 3)).start();
                Thread.sleep(3600);

                /*Hilos Mantenimientos */
                //new Thread(new HiloListadoDeTiposDeMantenimiento(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
                //new Thread(new HiloListadoDeVehiculosMantenimientos(this.ini)).start();
            } catch (InterruptedException ex) {
                JOptionPane.showMessageDialog(this, "Hubo un error cargando las variables", "Error...", JOptionPane.ERROR_MESSAGE);
                System.exit(0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo iniciar las variables porque No hay Conexion a la BBDD", "Error...", JOptionPane.ERROR_MESSAGE);
            // System.exit(0);
        }

        // Thread.sleep(300);
    }

    public void errorCargandoVariables() {
        JOptionPane.showMessageDialog(this, "Hubo un error cargando las varablrs", "Error...", JOptionPane.ERROR_MESSAGE);
        System.exit(0);
    }

    private void controlDeRegistrosDelSistema() throws Exception {

        datos.add(0, Inicio.deCifrar(ini.getUser().getNombreUsuario()));
        datos.add(1, "IngresoAlSistema");
        datos.add(2, "Ingresa al sistema");
        datos.add(3, "CURRENT_TIMESTAMP");
        new Thread(new HiloBitacoraMovimientosEnElSistema(ini, datos)).start();
    }

}
