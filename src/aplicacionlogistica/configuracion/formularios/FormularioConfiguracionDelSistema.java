/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion.formularios;

import aplicacionlogistica.configuracion.Inicio;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;

/**
 *
 * @author lopez164
 */
public class FormularioConfiguracionDelSistema extends javax.swing.JFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    public boolean actualizar;
    public boolean actualizarFoto = false;
    public boolean estaOcupadoGrabando = false;
    //IngresoAlSistema form;
    Inicio ini;

    /**
     * Creates new form NewJFrame
     */
    public FormularioConfiguracionDelSistema() {

        initComponents();
        cargarFormulario();
    }

    private void cargarFormulario() {
        try {
            //                this.setDefaultLookAndFeelDecorated(true);
//                UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");

            manager.addKeyEventDispatcher(new KeyEventDispatcher() {
                @Override
                public boolean dispatchKeyEvent(KeyEvent e) {
                    //como dije, solo las notificaciones del tipo "typed" son las que actualizan los componentes
                    if (e.getID() == KeyEvent.KEY_TYPED) {
                        // if(e.getSource() instanceof JComponent
                        //if(e.getKeyChar()>='0' && e.getKeyChar()<='9' ){
                        if (e.getSource() instanceof JComponent
                                // si el textfield esta marcado en el nombre y si el nombre es igual a "ignore_upper_case"
                                // entonces el campo puede tomar las minusculas
                                && ((JComponent) e.getSource()).getName() != null
                                && ((JComponent) e.getSource()).getName().startsWith("numerico")) {

                            if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9') {
                                return false;
                            } else {
                                return true;
                            }

                        } else if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
                            if (e.getSource() instanceof JComponent
                                    // si el textfield esta marcado en el nombre y si el nombre es igual a "ignore_upper_case"
                                    // entonces el campo puede tomar las minusculas
                                    && ((JComponent) e.getSource()).getName() != null
                                    && ((JComponent) e.getSource()).getName().startsWith("minuscula")) {
                                return false;
                            } else {
                                //como vamos a convertir todo a mayúsculas, entonces solo checamos si los caracteres son
                                //minusculas
                                e.setKeyChar((char) (((int) e.getKeyChar()) - 32));
                            }
                        }
                    }

                    //y listo, regresamos siempre falso para que las demas notificaciones continuen, si regresamos true
                    // significa que el dispatcher consumio el evento
                    return false;
                }
            });
            String rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");

            ini = new Inicio(new File(rutaDeArchivo + "ReadUs.ini"));

            txtIdAliado.setText(ini.getIdAliado());
            txtNit.setText(ini.getNitCliente());
            txtNombreCliente.setText(ini.getNombreDelCliente());
            txtDireccion.setText((ini.getDireccionCliente()));
            txtBarrio.setText((ini.getBarrioCliente()));
            txtCiudad.setText((ini.getCiudadCliente()));
            txtContacto.setText((ini.getContactoCliente()));
            txtEmail.setText((ini.getEmailCliente()));
            txtTelefonoFijo.setText((ini.getTelefonoCliente()));
            txtCelular.setText((ini.getCelularCliente()));

            txtBDlocal.setText((ini.getBdLocal()));
            txtURLlocal.setText((ini.getUrlLocal()));
            txtHostLocal.setText((ini.getServerLocal()));
            txtPuertoLocal.setText((ini.getPuerto()));
            txtUsuarioLocal.setText((ini.getUsuarioBDLocal()));
            txtClaveLocal.setText((ini.getClaveBDLocal()));

            txtBDremota.setText((ini.getBdRemota()));
            txtURLremota.setText((ini.getUrlRemota()));
            txtHostRemoto.setText((ini.getServerRemota()));
            txtPuertoRemoto.setText((ini.getPuertoRemota()));
            txtUsuarioRemoto.setText((ini.getUsuarioBDRemota()));
            txtClaveRemoto.setText((ini.getClaveBDRemota()));

            txtBDGPS.setText((ini.getBdGPS() ));//ini.getBdGPS()));
            txtURLGPS.setText((ini.getUrlGPS()));//ini.getUrlGPS()));
            txtHostGPS.setText((ini.getServerGPS()));//ini.getServerGPS()));
            txtPuertoGPS.setText((ini.getPuertoGPS()));//ini.getPuertoGPS()));
            txtUsuarioGPS.setText((ini.getUsuarioBDGPS() ));//ini.getUsuarioBDGPS()));
            txtClaveGPS.setText((ini.getClaveBDGPS()));//ini.getClaveBDGPS()));

            /*Variables de SMS*/
            if (ini.enviaSMS == 0) {
                chkSMS.setSelected(false);
            } else {
                chkSMS.setSelected(true);
            }
            txtUsuarioSMS.setText(ini.getUSuarioSMS());
            txtClaveSMS.setText(ini.getClaveSMS());
            txtOrigenSMS.setText(ini.getOrigenSMS());
            txtUrlLinkSMS.setText(ini.getUrlLinkSMS());
            txtMenssaje.setText(ini.getMensajeSMS());
            txtIndicativoSMS.setText(ini.getIndicativoSMS());

        } catch (Exception ex) {
            Logger.getLogger(FormularioConfiguracionDelSistema.class.getName()).log(Level.SEVERE, null, ex);
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

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        txtIdAliado = new javax.swing.JTextField();
        txtNit = new javax.swing.JTextField();
        txtNombreCliente = new javax.swing.JTextField();
        txtDireccion = new javax.swing.JTextField();
        txtBarrio = new javax.swing.JTextField();
        txtCiudad = new javax.swing.JTextField();
        txtContacto = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtTelefonoFijo = new javax.swing.JTextField();
        txtCelular = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        txtBDlocal = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        txtURLlocal = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtHostLocal = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        txtPuertoLocal = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtUsuarioLocal = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        txtClaveLocal = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        txtBDremota = new javax.swing.JTextField();
        txtURLremota = new javax.swing.JTextField();
        txtHostRemoto = new javax.swing.JTextField();
        txtPuertoRemoto = new javax.swing.JTextField();
        txtUsuarioRemoto = new javax.swing.JTextField();
        txtClaveRemoto = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        txtBDGPS = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        txtURLGPS = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        txtHostGPS = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        txtPuertoGPS = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        txtUsuarioGPS = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        txtClaveGPS = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtUsuarioSMS = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txtClaveSMS = new javax.swing.JTextField();
        txtOrigenSMS = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        txtUrlLinkSMS = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMenssaje = new javax.swing.JTextArea();
        jLabel28 = new javax.swing.JLabel();
        txtIndicativoSMS = new javax.swing.JTextField();
        chkSMS = new javax.swing.JCheckBox();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setIconImage(getIconImage());
        setIconImages(getIconImages());
        setResizable(false);

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Cliente"));

        txtIdAliado.setEditable(false);
        txtIdAliado.setToolTipText("Ingresar documento de identificación");
        txtIdAliado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtIdAliadoFocusGained(evt);
            }
        });
        txtIdAliado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdAliadoActionPerformed(evt);
            }
        });
        txtIdAliado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdAliadoKeyPressed(evt);
            }
        });

        txtNit.setToolTipText("Ingresar Nombres completos");
        txtNit.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNitFocusGained(evt);
            }
        });
        txtNit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNitActionPerformed(evt);
            }
        });
        txtNit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNitKeyPressed(evt);
            }
        });

        txtNombreCliente.setToolTipText("Ingresar apellidos completos");
        txtNombreCliente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreClienteFocusGained(evt);
            }
        });
        txtNombreCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreClienteKeyPressed(evt);
            }
        });

        txtDireccion.setToolTipText("Ingresar dirección");
        txtDireccion.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtDireccionFocusGained(evt);
            }
        });
        txtDireccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDireccionActionPerformed(evt);
            }
        });
        txtDireccion.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDireccionKeyPressed(evt);
            }
        });

        txtBarrio.setToolTipText("ingresar el #  de teléfono fijo");
        txtBarrio.setName("numerico"); // NOI18N
        txtBarrio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBarrioFocusGained(evt);
            }
        });
        txtBarrio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBarrioKeyPressed(evt);
            }
        });

        txtCiudad.setToolTipText("Ingreasr el # celular particular");
        txtCiudad.setName("numerico"); // NOI18N
        txtCiudad.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCiudadFocusGained(evt);
            }
        });
        txtCiudad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCiudadActionPerformed(evt);
            }
        });
        txtCiudad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCiudadKeyPressed(evt);
            }
        });

        txtContacto.setToolTipText("Ingreasr el # celular particular");
        txtContacto.setName("numerico"); // NOI18N
        txtContacto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtContactoFocusGained(evt);
            }
        });
        txtContacto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtContactoActionPerformed(evt);
            }
        });
        txtContacto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtContactoKeyPressed(evt);
            }
        });

        txtEmail.setToolTipText("Ingresar el barrio");
        txtEmail.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtEmailFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtEmailFocusLost(evt);
            }
        });
        txtEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtEmailActionPerformed(evt);
            }
        });
        txtEmail.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtEmailKeyPressed(evt);
            }
        });

        txtTelefonoFijo.setToolTipText("Ingresar el grado de escolaridad");
        txtTelefonoFijo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtTelefonoFijoFocusGained(evt);
            }
        });
        txtTelefonoFijo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTelefonoFijoActionPerformed(evt);
            }
        });
        txtTelefonoFijo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTelefonoFijoKeyPressed(evt);
            }
        });

        txtCelular.setToolTipText("Ingresar el grado de escolaridad");
        txtCelular.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCelularFocusGained(evt);
            }
        });
        txtCelular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCelularActionPerformed(evt);
            }
        });
        txtCelular.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCelularKeyPressed(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("idAliado");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel2.setText("Nit Cliente");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel3.setText("Nombre Cliente");

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel5.setText("Dirección del Cliente");

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel6.setText("Telefono Fijo");

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("Telefono Celullar");

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel27.setText("Email Cliente");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("Ciudad");

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel8.setText("Barrio");

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel22.setText("Contacto");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtBarrio, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtDireccion, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNombreCliente, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 326, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtCelular, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtTelefonoFijo, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(txtIdAliado, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtNit, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtContacto)))
                .addGap(5, 5, 5))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel1))
                    .addComponent(txtIdAliado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNit, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(6, 6, 6)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel5))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(txtDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel8))
                    .addComponent(txtBarrio, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel9))
                    .addComponent(txtCiudad, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel22))
                    .addComponent(txtContacto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel27)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel6))
                    .addComponent(txtTelefonoFijo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel7))
                    .addComponent(txtCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(68, 68, 68))
        );

        jTabbedPane1.addTab("Datos del Cliente", jPanel6);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Servidor Local-Remoto"));

        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel10.setText("BBDD Local");

        txtBDlocal.setToolTipText("Ingreasar # teléfono celular corporativo");
        txtBDlocal.setName("minuscula"); // NOI18N
        txtBDlocal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBDlocalFocusGained(evt);
            }
        });
        txtBDlocal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBDlocalKeyPressed(evt);
            }
        });

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel11.setText("URL Local");

        txtURLlocal.setToolTipText("Ingresar el grado de escolaridad");
        txtURLlocal.setName("minuscula"); // NOI18N
        txtURLlocal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtURLlocalFocusGained(evt);
            }
        });
        txtURLlocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtURLlocalActionPerformed(evt);
            }
        });
        txtURLlocal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtURLlocalKeyPressed(evt);
            }
        });

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("Host Local");

        txtHostLocal.setToolTipText("Ingresar el grado de escolaridad");
        txtHostLocal.setName("minuscula"); // NOI18N
        txtHostLocal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtHostLocalFocusGained(evt);
            }
        });
        txtHostLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHostLocalActionPerformed(evt);
            }
        });
        txtHostLocal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHostLocalKeyPressed(evt);
            }
        });

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel13.setText("puerto");

        txtPuertoLocal.setToolTipText("Ingresar el grado de escolaridad");
        txtPuertoLocal.setName("numerico"); // NOI18N
        txtPuertoLocal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPuertoLocalFocusGained(evt);
            }
        });
        txtPuertoLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPuertoLocalActionPerformed(evt);
            }
        });
        txtPuertoLocal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPuertoLocalKeyPressed(evt);
            }
        });

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel14.setText("Usuario Local");

        txtUsuarioLocal.setToolTipText("Ingresar el lugar de nacimiento");
        txtUsuarioLocal.setName("minuscula"); // NOI18N
        txtUsuarioLocal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsuarioLocalFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUsuarioLocalFocusLost(evt);
            }
        });
        txtUsuarioLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioLocalActionPerformed(evt);
            }
        });
        txtUsuarioLocal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsuarioLocalKeyPressed(evt);
            }
        });

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel15.setText("PW BBDD Local");

        txtClaveLocal.setToolTipText("Ingresar el grado de escolaridad");
        txtClaveLocal.setName("minuscula"); // NOI18N
        txtClaveLocal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtClaveLocalFocusGained(evt);
            }
        });
        txtClaveLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClaveLocalActionPerformed(evt);
            }
        });
        txtClaveLocal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtClaveLocalKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtClaveLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUsuarioLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHostLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtURLlocal, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBDlocal, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPuertoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtBDlocal, txtClaveLocal, txtHostLocal, txtPuertoLocal, txtURLlocal, txtUsuarioLocal});

        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel10))
                    .addComponent(txtBDlocal, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel11))
                    .addComponent(txtURLlocal, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel12))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(txtHostLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(txtPuertoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel14))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUsuarioLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtClaveLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel17.setText("BBDD Remote");

        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel16.setText("Url Remote");

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel18.setText("Host Remoto");

        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel19.setText("Puerto");

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel20.setText("Usuario Remoto");

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel21.setText("PW BBDD Remote");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel21, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel21)
                .addContainerGap(47, Short.MAX_VALUE))
        );

        txtBDremota.setToolTipText("Ingresar el grado de escolaridad");
        txtBDremota.setName("minuscula"); // NOI18N
        txtBDremota.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBDremotaFocusGained(evt);
            }
        });
        txtBDremota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBDremotaActionPerformed(evt);
            }
        });
        txtBDremota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBDremotaKeyPressed(evt);
            }
        });

        txtURLremota.setToolTipText("Ingresar el email personal ó corporativo");
        txtURLremota.setName("minuscula"); // NOI18N
        txtURLremota.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtURLremotaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtURLremotaFocusLost(evt);
            }
        });
        txtURLremota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtURLremotaActionPerformed(evt);
            }
        });
        txtURLremota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtURLremotaKeyPressed(evt);
            }
        });

        txtHostRemoto.setToolTipText("Ingresar el email personal ó corporativo");
        txtHostRemoto.setName("minuscula"); // NOI18N
        txtHostRemoto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtHostRemotoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtHostRemotoFocusLost(evt);
            }
        });
        txtHostRemoto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHostRemotoKeyPressed(evt);
            }
        });

        txtPuertoRemoto.setToolTipText("Ingresar el grado de escolaridad");
        txtPuertoRemoto.setName("numerico"); // NOI18N
        txtPuertoRemoto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPuertoRemotoFocusGained(evt);
            }
        });
        txtPuertoRemoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPuertoRemotoActionPerformed(evt);
            }
        });
        txtPuertoRemoto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPuertoRemotoKeyPressed(evt);
            }
        });

        txtUsuarioRemoto.setToolTipText("Ingresar el grado de escolaridad");
        txtUsuarioRemoto.setName("minuscula"); // NOI18N
        txtUsuarioRemoto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsuarioRemotoFocusGained(evt);
            }
        });
        txtUsuarioRemoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioRemotoActionPerformed(evt);
            }
        });
        txtUsuarioRemoto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsuarioRemotoKeyPressed(evt);
            }
        });

        txtClaveRemoto.setToolTipText("Ingresar el grado de escolaridad");
        txtClaveRemoto.setName("minuscula"); // NOI18N
        txtClaveRemoto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtClaveRemotoFocusGained(evt);
            }
        });
        txtClaveRemoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClaveRemotoActionPerformed(evt);
            }
        });
        txtClaveRemoto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtClaveRemotoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtClaveRemoto, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUsuarioRemoto, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPuertoRemoto, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtHostRemoto, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtURLremota, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBDremota, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtBDremota, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtURLremota, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtHostRemoto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPuertoRemoto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtUsuarioRemoto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtClaveRemoto, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 45, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 54, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Bases de Datos", jPanel1);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Servidor  Mantenimientos"));

        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel29.setText("BBDD GPS");

        txtBDGPS.setText("tracker");
        txtBDGPS.setToolTipText("Ingreasar # teléfono celular corporativo");
        txtBDGPS.setName("minuscula"); // NOI18N
        txtBDGPS.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtBDGPSFocusGained(evt);
            }
        });
        txtBDGPS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBDGPSActionPerformed(evt);
            }
        });
        txtBDGPS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtBDGPSKeyPressed(evt);
            }
        });

        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel30.setText("URL GPS");

        txtURLGPS.setText("jdbc:mysql");
        txtURLGPS.setToolTipText("Ingresar el grado de escolaridad");
        txtURLGPS.setName("minuscula"); // NOI18N
        txtURLGPS.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtURLGPSFocusGained(evt);
            }
        });
        txtURLGPS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtURLGPSActionPerformed(evt);
            }
        });
        txtURLGPS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtURLGPSKeyPressed(evt);
            }
        });

        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel31.setText("Host GPS");

        txtHostGPS.setText("129.151.107.181");
        txtHostGPS.setToolTipText("Ingresar el grado de escolaridad");
        txtHostGPS.setName("minuscula"); // NOI18N
        txtHostGPS.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtHostGPSFocusGained(evt);
            }
        });
        txtHostGPS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtHostGPSActionPerformed(evt);
            }
        });
        txtHostGPS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtHostGPSKeyPressed(evt);
            }
        });

        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel32.setText("puerto GPS");

        txtPuertoGPS.setText("3306");
        txtPuertoGPS.setToolTipText("Ingresar el grado de escolaridad");
        txtPuertoGPS.setName("numerico"); // NOI18N
        txtPuertoGPS.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPuertoGPSFocusGained(evt);
            }
        });
        txtPuertoGPS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPuertoGPSActionPerformed(evt);
            }
        });
        txtPuertoGPS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPuertoGPSKeyPressed(evt);
            }
        });

        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel33.setText("Usuario GPS");

        txtUsuarioGPS.setText("luislopez");
        txtUsuarioGPS.setToolTipText("Ingresar el lugar de nacimiento");
        txtUsuarioGPS.setName("minuscula"); // NOI18N
        txtUsuarioGPS.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsuarioGPSFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUsuarioGPSFocusLost(evt);
            }
        });
        txtUsuarioGPS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioGPSActionPerformed(evt);
            }
        });
        txtUsuarioGPS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsuarioGPSKeyPressed(evt);
            }
        });

        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel34.setText("PW BBDD GPS");

        txtClaveGPS.setText("%jslslpzmjC12%");
        txtClaveGPS.setToolTipText("Ingresar el grado de escolaridad");
        txtClaveGPS.setName("minuscula"); // NOI18N
        txtClaveGPS.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtClaveGPSFocusGained(evt);
            }
        });
        txtClaveGPS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClaveGPSActionPerformed(evt);
            }
        });
        txtClaveGPS.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtClaveGPSKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                        .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
                    .addComponent(jLabel34))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtClaveGPS, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                    .addComponent(txtUsuarioGPS)
                    .addComponent(txtHostGPS)
                    .addComponent(txtURLGPS)
                    .addComponent(txtPuertoGPS)
                    .addComponent(txtBDGPS)))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel29))
                    .addComponent(txtBDGPS, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jLabel30))
                    .addComponent(txtURLGPS, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel31))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(txtHostGPS, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel32)
                    .addComponent(txtPuertoGPS, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jLabel33))
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtUsuarioGPS, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtClaveGPS, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel34)))
        );

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(239, 239, 239))
        );

        jTabbedPane1.addTab("BBDD GPS", jPanel8);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos SMS"));

        jPanel7.setBorder(javax.swing.BorderFactory.createCompoundBorder());

        jLabel4.setText("usuario");

        txtUsuarioSMS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioSMSActionPerformed(evt);
            }
        });

        jLabel23.setText("Clave");

        txtOrigenSMS.setText("Distrilog B2B");

        jLabel24.setText("origen");

        jLabel25.setText("UrlLink");

        txtUrlLinkSMS.setText("https://gateway.plusmms.net/send.php");
        txtUrlLinkSMS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUrlLinkSMSActionPerformed(evt);
            }
        });

        jLabel26.setText("Mensaje al cliente");

        txtMenssaje.setColumns(20);
        txtMenssaje.setLineWrap(true);
        txtMenssaje.setRows(5);
        txtMenssaje.setWrapStyleWord(true);
        txtMenssaje.setAutoscrolls(false);
        jScrollPane1.setViewportView(txtMenssaje);

        jLabel28.setText("indicativo");

        txtIndicativoSMS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIndicativoSMSActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 418, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 385, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel23))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtClaveSMS, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE)
                                        .addComponent(txtUsuarioSMS)))
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jLabel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtOrigenSMS, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel7Layout.createSequentialGroup()
                                    .addComponent(jLabel25, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtUrlLinkSMS, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                                    .addComponent(jLabel28)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtIndicativoSMS, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(31, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtUsuarioSMS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(txtClaveSMS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(txtOrigenSMS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(txtUrlLinkSMS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(txtIndicativoSMS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel26)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(63, Short.MAX_VALUE))
        );

        chkSMS.setText("Usa SMS?");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(chkSMS)))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chkSMS)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("SMS", jPanel2);

        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 554, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 488, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIdAliadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdAliadoFocusGained
        txtIdAliado.setSelectionStart(0);
        txtIdAliado.setSelectionEnd(txtIdAliado.getText().length());
    }//GEN-LAST:event_txtIdAliadoFocusGained

    private void txtIdAliadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdAliadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdAliadoActionPerformed

    private void txtIdAliadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdAliadoKeyPressed

    }//GEN-LAST:event_txtIdAliadoKeyPressed

    private void txtNitFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNitFocusGained
        txtNit.setSelectionStart(0);
        txtNit.setSelectionEnd(txtNit.getText().length());
    }//GEN-LAST:event_txtNitFocusGained

    private void txtNitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNitActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNitActionPerformed

    private void txtNitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNitKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtNombreCliente.requestFocus();
        }
    }//GEN-LAST:event_txtNitKeyPressed

    private void txtNombreClienteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreClienteFocusGained
        txtNombreCliente.setSelectionStart(0);
        txtNombreCliente.setSelectionEnd(txtNombreCliente.getText().length());
    }//GEN-LAST:event_txtNombreClienteFocusGained

    private void txtNombreClienteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreClienteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtDireccion.requestFocus();
        }
    }//GEN-LAST:event_txtNombreClienteKeyPressed

    private void txtDireccionFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtDireccionFocusGained
        txtDireccion.setSelectionStart(0);
        txtDireccion.setSelectionEnd(txtDireccion.getText().length());
    }//GEN-LAST:event_txtDireccionFocusGained

    private void txtDireccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDireccionActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDireccionActionPerformed

    private void txtDireccionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDireccionKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtEmail.requestFocus();
        }
    }//GEN-LAST:event_txtDireccionKeyPressed

    private void txtEmailFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusGained
        txtEmail.setSelectionStart(0);
        txtEmail.setSelectionEnd(txtEmail.getText().length());
    }//GEN-LAST:event_txtEmailFocusGained

    private void txtEmailFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtEmailFocusLost

    }//GEN-LAST:event_txtEmailFocusLost

    private void txtEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmailActionPerformed

    private void txtEmailKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtEmailKeyPressed

    }//GEN-LAST:event_txtEmailKeyPressed

    private void txtCiudadFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCiudadFocusGained
        txtCiudad.setSelectionStart(0);
        txtCiudad.setSelectionEnd(txtCiudad.getText().length());
    }//GEN-LAST:event_txtCiudadFocusGained

    private void txtCiudadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCiudadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCiudadActionPerformed

    private void txtCiudadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCiudadKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtBDlocal.requestFocus();
        }
    }//GEN-LAST:event_txtCiudadKeyPressed

    private void txtBarrioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBarrioFocusGained
        txtBarrio.setSelectionStart(0);
        txtBarrio.setSelectionEnd(txtBarrio.getText().length());
    }//GEN-LAST:event_txtBarrioFocusGained

    private void txtBarrioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBarrioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtCiudad.requestFocus();
        }
    }//GEN-LAST:event_txtBarrioKeyPressed

    private void txtTelefonoFijoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtTelefonoFijoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoFijoFocusGained

    private void txtTelefonoFijoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTelefonoFijoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoFijoActionPerformed

    private void txtTelefonoFijoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTelefonoFijoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTelefonoFijoKeyPressed

    private void txtCelularFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCelularFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCelularFocusGained

    private void txtCelularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCelularActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCelularActionPerformed

    private void txtCelularKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCelularKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCelularKeyPressed

    private void txtBDlocalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBDlocalFocusGained
        txtBDlocal.setSelectionStart(0);
        txtBDlocal.setSelectionEnd(txtBDlocal.getText().length());
    }//GEN-LAST:event_txtBDlocalFocusGained

    private void txtBDlocalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBDlocalKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtClaveLocal.requestFocus();
        }
    }//GEN-LAST:event_txtBDlocalKeyPressed

    private void txtURLlocalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtURLlocalFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtURLlocalFocusGained

    private void txtURLlocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtURLlocalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtURLlocalActionPerformed

    private void txtURLlocalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtURLlocalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtURLlocalKeyPressed

    private void txtHostLocalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHostLocalFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHostLocalFocusGained

    private void txtHostLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHostLocalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHostLocalActionPerformed

    private void txtHostLocalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHostLocalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHostLocalKeyPressed

    private void txtPuertoLocalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPuertoLocalFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPuertoLocalFocusGained

    private void txtPuertoLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPuertoLocalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPuertoLocalActionPerformed

    private void txtPuertoLocalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPuertoLocalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPuertoLocalKeyPressed

    private void txtUsuarioLocalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioLocalFocusGained
        txtUsuarioLocal.setSelectionStart(0);
        txtUsuarioLocal.setSelectionEnd(txtUsuarioLocal.getText().length());
    }//GEN-LAST:event_txtUsuarioLocalFocusGained

    private void txtUsuarioLocalFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioLocalFocusLost

    }//GEN-LAST:event_txtUsuarioLocalFocusLost

    private void txtUsuarioLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioLocalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioLocalActionPerformed

    private void txtUsuarioLocalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioLocalKeyPressed

    }//GEN-LAST:event_txtUsuarioLocalKeyPressed

    private void txtClaveLocalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClaveLocalFocusGained
        txtClaveLocal.setSelectionStart(0);
        txtClaveLocal.setSelectionEnd(txtClaveLocal.getText().length());
    }//GEN-LAST:event_txtClaveLocalFocusGained

    private void txtClaveLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClaveLocalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClaveLocalActionPerformed

    private void txtClaveLocalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClaveLocalKeyPressed

    }//GEN-LAST:event_txtClaveLocalKeyPressed

    private void txtBDremotaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBDremotaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBDremotaFocusGained

    private void txtBDremotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBDremotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBDremotaActionPerformed

    private void txtBDremotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBDremotaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBDremotaKeyPressed

    private void txtURLremotaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtURLremotaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtURLremotaFocusGained

    private void txtURLremotaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtURLremotaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtURLremotaFocusLost

    private void txtURLremotaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtURLremotaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtURLremotaKeyPressed

    private void txtHostRemotoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHostRemotoFocusGained
        txtHostRemoto.setSelectionStart(0);
        txtHostRemoto.setSelectionEnd(txtHostRemoto.getText().length());
    }//GEN-LAST:event_txtHostRemotoFocusGained

    private void txtHostRemotoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHostRemotoFocusLost

    }//GEN-LAST:event_txtHostRemotoFocusLost

    private void txtHostRemotoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHostRemotoKeyPressed

    }//GEN-LAST:event_txtHostRemotoKeyPressed

    private void txtPuertoRemotoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPuertoRemotoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPuertoRemotoFocusGained

    private void txtPuertoRemotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPuertoRemotoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPuertoRemotoActionPerformed

    private void txtPuertoRemotoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPuertoRemotoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPuertoRemotoKeyPressed

    private void txtUsuarioRemotoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioRemotoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioRemotoFocusGained

    private void txtUsuarioRemotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioRemotoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioRemotoActionPerformed

    private void txtUsuarioRemotoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioRemotoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioRemotoKeyPressed

    private void txtClaveRemotoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClaveRemotoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClaveRemotoFocusGained

    private void txtClaveRemotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClaveRemotoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClaveRemotoActionPerformed

    private void txtClaveRemotoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClaveRemotoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClaveRemotoKeyPressed

    private void txtContactoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtContactoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContactoFocusGained

    private void txtContactoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtContactoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContactoActionPerformed

    private void txtContactoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContactoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtContactoKeyPressed

    private void txtURLremotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtURLremotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtURLremotaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        int deseaGrabar = JOptionPane.showConfirmDialog(this, "Desea guardar el registro ?", "Guardar registro", JOptionPane.YES_NO_OPTION);

        /* Se valida el deseo de grabar los datos en la BBDD  */
        if (deseaGrabar == 0) {
            String rutaDeArchivo = "" + (new File(".").getAbsolutePath()).replace(".", "");

            /*Toma los datos de la interface grafica y los asigna a las variables
            del archivo de configuracion del sistema*/
            asignarValores();

            /*Toma los valores de las variables y los encripta para guardarlos en 
            el archivo de configuracion*/
            FileWriter fichero = null;
            PrintWriter pw = null;
            try {
                fichero = new FileWriter(rutaDeArchivo + "ReadUs.ini");
                pw = new PrintWriter(fichero);

                pw.println(Inicio.cifrar(ini.getIdAliado()));
                pw.println(Inicio.cifrar(ini.getNitCliente()));
                pw.println(Inicio.cifrar(ini.getNombreDelCliente()));
                pw.println(Inicio.cifrar(ini.getDireccionCliente()));
                pw.println(Inicio.cifrar(ini.getBarrioCliente()));
                pw.println(Inicio.cifrar(ini.getCiudadCliente()));
                pw.println(Inicio.cifrar(ini.getContactoCliente()));
                pw.println(Inicio.cifrar(ini.getEmailCliente()));
                pw.println(Inicio.cifrar(ini.getTelefonoCliente()));
                pw.println(Inicio.cifrar(ini.getCelularCliente()));

                pw.println(Inicio.cifrar(ini.getBdLocal()));
                pw.println(Inicio.cifrar(ini.getUrlLocal()));
                pw.println(Inicio.cifrar(ini.getServerLocal()));
                pw.println(Inicio.cifrar(ini.getPuerto()));
                pw.println(Inicio.cifrar(ini.getUsuarioBDLocal()));
                pw.println(Inicio.cifrar(ini.getClaveBDLocal()));

                pw.println(Inicio.cifrar(ini.getBdRemota()));
                pw.println(Inicio.cifrar(ini.getUrlRemota()));
                pw.println(Inicio.cifrar(ini.getServerRemota()));
                pw.println(Inicio.cifrar(ini.getPuertoRemota()));
                pw.println(Inicio.cifrar(ini.getUsuarioBDRemota()));
                pw.println(Inicio.cifrar(ini.getClaveBDRemota()));

                // variables  parael modulo de GPS
                pw.println(Inicio.cifrar(ini.getBdGPS()));
                pw.println(Inicio.cifrar(ini.getUrlGPS()));
                pw.println(Inicio.cifrar(ini.getServerGPS()));
                pw.println(Inicio.cifrar(ini.getPuertoGPS()));
                pw.println(Inicio.cifrar(ini.getUsuarioBDGPS()));
                pw.println(Inicio.cifrar(ini.getClaveBDGPS()));

                // variables  para el envio de SMS
                pw.println(Inicio.cifrar("" + ini.enviaSMS()));
                pw.println(Inicio.cifrar(ini.getUSuarioSMS()));
                pw.println(Inicio.cifrar(ini.getClaveSMS()));
                pw.println(Inicio.cifrar(ini.getOrigenSMS()));
                pw.println(Inicio.cifrar(ini.getUrlLinkSMS()));
                pw.println(Inicio.cifrar(ini.getMensajeSMS()));
                pw.println(Inicio.cifrar(ini.getIndicativoSMS()));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    // Nuevamente aprovechamos el finally para 
                    // asegurarnos que se cierra el fichero.
                    if (null != fichero) {
                        fichero.close();
                        JOptionPane.showMessageDialog(this, "Datos Guardados correctamente", "Guardado", JOptionPane.INFORMATION_MESSAGE, null);

                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        this.dispose();
        System.exit(0);


    }//GEN-LAST:event_jButton2ActionPerformed

    private void txtUsuarioSMSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioSMSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioSMSActionPerformed

    private void txtUrlLinkSMSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUrlLinkSMSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUrlLinkSMSActionPerformed

    private void txtIndicativoSMSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIndicativoSMSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIndicativoSMSActionPerformed

    private void txtBDGPSFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtBDGPSFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBDGPSFocusGained

    private void txtBDGPSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBDGPSKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBDGPSKeyPressed

    private void txtURLGPSFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtURLGPSFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtURLGPSFocusGained

    private void txtURLGPSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtURLGPSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtURLGPSActionPerformed

    private void txtURLGPSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtURLGPSKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtURLGPSKeyPressed

    private void txtHostGPSFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtHostGPSFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHostGPSFocusGained

    private void txtHostGPSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtHostGPSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHostGPSActionPerformed

    private void txtHostGPSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtHostGPSKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtHostGPSKeyPressed

    private void txtPuertoGPSFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPuertoGPSFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPuertoGPSFocusGained

    private void txtPuertoGPSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPuertoGPSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPuertoGPSActionPerformed

    private void txtPuertoGPSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPuertoGPSKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPuertoGPSKeyPressed

    private void txtUsuarioGPSFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioGPSFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioGPSFocusGained

    private void txtUsuarioGPSFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioGPSFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioGPSFocusLost

    private void txtUsuarioGPSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioGPSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioGPSActionPerformed

    private void txtUsuarioGPSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioGPSKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioGPSKeyPressed

    private void txtClaveGPSFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClaveGPSFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClaveGPSFocusGained

    private void txtClaveGPSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClaveGPSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClaveGPSActionPerformed

    private void txtClaveGPSKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClaveGPSKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClaveGPSKeyPressed

    private void txtBDGPSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBDGPSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBDGPSActionPerformed

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
            java.util.logging.Logger.getLogger(FormularioConfiguracionDelSistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }


        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // new FormularioConfiguracionDelSistema().setVisible(true);
            }
        });
    }

    @Override
    public Image getIconImage() {
        Image retValue = Toolkit.getDefaultToolkit().
                getImage(ClassLoader.getSystemResource("aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"));

        return retValue;
    }

    private void asignarValores() {
        ini.setIdAliado(txtIdAliado.getText());
        ini.setNitCliente(txtNit.getText());
        ini.setNombreDelCliente(txtNombreCliente.getText());
        ini.setDireccionCliente(txtDireccion.getText());
        ini.setBarrioCliente(txtBarrio.getText());
        ini.setCiudadCliente(txtCiudad.getText());
        ini.setContactoCliente(txtContacto.getText());
        ini.setEmailCliente(txtEmail.getText());
        ini.setTelefonoCliente(txtTelefonoFijo.getText());
        ini.setCelularCliente(txtCelular.getText());

        ini.setBdLocal(txtBDlocal.getText());
        ini.setUrlLocal(txtURLlocal.getText());
        ini.setServerLocal(txtHostLocal.getText());
        ini.setPuertoLocal(txtPuertoLocal.getText());
        ini.setUsuarioBDLocal(txtUsuarioLocal.getText());
        ini.setClaveBDLocal(txtClaveLocal.getText());

        ini.setBdRemota(txtBDremota.getText());
        ini.setUrlRemota(txtURLremota.getText());
        ini.setServerRemota(txtHostRemoto.getText());
        ini.setPuertoRemota(txtPuertoRemoto.getText());
        ini.setUsuarioBDRemota(txtUsuarioRemoto.getText());
        ini.setClaveBDRemota(txtClaveRemoto.getText());

        ini.setBdGPS(txtBDGPS.getText());
        ini.setUrlGPS(txtURLGPS.getText());
        ini.setServerGPS(txtHostGPS.getText());
        ini.setPuertoGPS(txtPuertoGPS.getText());
        ini.setUsuarioBDGPS(txtUsuarioGPS.getText());
        ini.setClaveBDGPS(txtClaveGPS.getText());

        // Variables para el envio de SMS
        if (chkSMS.isSelected()) {
            ini.setEnviaSMS(1);
        } else {
            ini.setEnviaSMS(0);
        }
        ini.setUSuarioSMS(txtUsuarioSMS.getText());
        ini.setClaveSMS(txtClaveSMS.getText());
        ini.setOrigenSMS(txtOrigenSMS.getText());
        ini.setUrlLinkSMS(txtUrlLinkSMS.getText());
        ini.setMensajeSMS(txtMenssaje.getText());
        ini.setIndicativoSMS(txtIndicativoSMS.getText());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox chkSMS;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JTextField txtBDGPS;
    private javax.swing.JTextField txtBDlocal;
    private javax.swing.JTextField txtBDremota;
    private javax.swing.JTextField txtBarrio;
    private javax.swing.JTextField txtCelular;
    private javax.swing.JTextField txtCiudad;
    private javax.swing.JTextField txtClaveGPS;
    private javax.swing.JTextField txtClaveLocal;
    private javax.swing.JTextField txtClaveRemoto;
    private javax.swing.JTextField txtClaveSMS;
    private javax.swing.JTextField txtContacto;
    private javax.swing.JTextField txtDireccion;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtHostGPS;
    private javax.swing.JTextField txtHostLocal;
    private javax.swing.JTextField txtHostRemoto;
    private javax.swing.JTextField txtIdAliado;
    private javax.swing.JTextField txtIndicativoSMS;
    private javax.swing.JTextArea txtMenssaje;
    private javax.swing.JTextField txtNit;
    private javax.swing.JTextField txtNombreCliente;
    private javax.swing.JTextField txtOrigenSMS;
    private javax.swing.JTextField txtPuertoGPS;
    private javax.swing.JTextField txtPuertoLocal;
    private javax.swing.JTextField txtPuertoRemoto;
    private javax.swing.JTextField txtTelefonoFijo;
    private javax.swing.JTextField txtURLGPS;
    private javax.swing.JTextField txtURLlocal;
    private javax.swing.JTextField txtURLremota;
    private javax.swing.JTextField txtUrlLinkSMS;
    private javax.swing.JTextField txtUsuarioGPS;
    private javax.swing.JTextField txtUsuarioLocal;
    private javax.swing.JTextField txtUsuarioRemoto;
    private javax.swing.JTextField txtUsuarioSMS;
    // End of variables declaration//GEN-END:variables
}
