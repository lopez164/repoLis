/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion;

import aplicacionlogistica.administrativo.PrincipalAdministrador;
import aplicacionlogistica.distribucion.PrincipalLogistica;
import aplicacionlogistica.administrativo.PrincipalTalentoHumanoAdministrador;
import aplicacionlogistica.administrativo.PrincipalTalentoHumanoAuxiliar;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author VLI_488
 */
public class CambiarClave extends javax.swing.JInternalFrame {
Inicio ini;
boolean iguales=false;
String mensaje;
String claveFinal;
CUsuarios user;
PrincipalAdministrador ppalAdministrador=null;
PrincipalTalentoHumanoAdministrador ppalTalHumanoAdministrador=null;
PrincipalTalentoHumanoAuxiliar ppalTalHumanoAux=null;
PrincipalLogistica ppalLogisticaAdministrador=null;
PrincipalLogistica ppalLogistica=null;
    /**
     * Creates new form CambiarClave
     */
    public CambiarClave() {
       initComponents();
        this.setTitle("Ingreso al sistema de Información de Logistica");
        this.setResizable(true);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation ((screenSize.width - this.getSize().width) / 2,(screenSize.height - this.getSize().height) / 2);
        txtUsuario.requestFocus();
    }

    public CambiarClave(Inicio ini) throws Exception {
        initComponents();
        this.ini=ini;
        this.user= ini.getUser();
        this.setTitle("Formulario para el cambio de clave de usuario");
        txtUsuario.setText(ini.getUsuarioDelSistema());
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation ((screenSize.width - this.getSize().width) / 2,(screenSize.height - this.getSize().height) / 2);
        txtClaveAnterior.requestFocus();
    }
    
     public CambiarClave(PrincipalTalentoHumanoAdministrador ppal) {
        ppalTalHumanoAdministrador=ppal;
        this.user=ppal.getUser();
        initComponents();
        this.setTitle("Formulario para el cambio de clave de usuario");
    try {
        txtUsuario.setText(Inicio.deCifrar(user.getNombreUsuario()));
    } catch (Exception ex) {
        Logger.getLogger(CambiarClave.class.getName()).log(Level.SEVERE, null, ex);
    }
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation ((screenSize.width - this.getSize().width) / 2,(screenSize.height - this.getSize().height) / 2);
        txtClaveAnterior.requestFocus();
    }
    
   public CambiarClave(PrincipalTalentoHumanoAuxiliar ppal) {
       ppalTalHumanoAux=ppal;
        this.user=ppal.getUser();
        initComponents();
        this.setResizable(false);
        this.setTitle("Formulario para el cambio de clave de usuario");
    try {
        txtUsuario.setText(Inicio.deCifrar(user.getNombreUsuario()));
    } catch (Exception ex) {
        Logger.getLogger(CambiarClave.class.getName()).log(Level.SEVERE, null, ex);
    }
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation ((screenSize.width - this.getSize().width) / 2,(screenSize.height - this.getSize().height) / 2);
        txtClaveAnterior.requestFocus();
    }
     
     public CambiarClave(PrincipalAdministrador ppal) {
    try {
        ppalAdministrador=ppal;
        this.user=ppal.getUser();
        initComponents();
        this.setTitle("Formulario para el cambio de clave de usuario");
         txtUsuario.setText(Inicio.deCifrar(user.getNombreUsuario()));
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation ((screenSize.width - this.getSize().width) / 2,(screenSize.height - this.getSize().height) / 2);
        txtClaveAnterior.requestFocus();
    } catch (Exception ex) {
        Logger.getLogger(CambiarClave.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    
      public CambiarClave(PrincipalLogistica ppal, Inicio ini) {
    try {
        ppalLogistica=ppal;
        this.ini=ini;
        this.user=ini.getUser();
        initComponents();
        this.setTitle("Formulario para el cambio de clave de usuario");
         txtUsuario.setText(Inicio.deCifrar(user.getNombreUsuario()));
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation ((screenSize.width - this.getSize().width) / 2,(screenSize.height - this.getSize().height) / 2);
        txtClaveAnterior.requestFocus();
    } catch (Exception ex) {
        Logger.getLogger(CambiarClave.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel2 = new javax.swing.JPanel();
        txtUsuario = new javax.swing.JTextField();
        txtClave = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtClave1 = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtClaveAnterior = new javax.swing.JPasswordField();
        panelFotografia2 = new org.edisoncor.gui.panel.PanelImage();
        btnAceptar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario para cambio de clave del Usuario");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(76, 33));

        txtUsuario.setEditable(false);
        txtUsuario.setToolTipText("");
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });
        txtUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtUsuarioFocusGained(evt);
            }
        });
        txtUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUsuarioKeyPressed(evt);
            }
        });

        txtClave.setEditable(false);
        txtClave.setToolTipText("Digitar la Clave para ingresar al sistema");
        txtClave.setEnabled(false);
        txtClave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClaveActionPerformed(evt);
            }
        });
        txtClave.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtClaveFocusGained(evt);
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
        jLabel2.setText("Confirmar Clave");

        txtClave1.setEditable(false);
        txtClave1.setToolTipText("Digitar la Clave para ingresar al sistema");
        txtClave1.setEnabled(false);
        txtClave1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtClave1FocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtClave1FocusLost(evt);
            }
        });
        txtClave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClave1ActionPerformed(evt);
            }
        });
        txtClave1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtClave1KeyPressed(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/password.png"))); // NOI18N
        jLabel3.setText("Digitar Clave");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/password.png"))); // NOI18N
        jLabel4.setText("Clave anterior");

        txtClaveAnterior.setToolTipText("Digitar la Clave para ingresar al sistema");
        txtClaveAnterior.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtClaveAnteriorFocusGained(evt);
            }
        });
        txtClaveAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtClaveAnteriorActionPerformed(evt);
            }
        });
        txtClaveAnterior.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtClaveAnteriorKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                    .addComponent(txtClave)
                    .addComponent(txtClave1)
                    .addComponent(txtClaveAnterior))
                .addGap(97, 97, 97))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtClaveAnterior, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(txtClave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(txtClave1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2))))
        );

        panelFotografia2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        panelFotografia2.setToolTipText("");
        panelFotografia2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/perfil.jpg"))); // NOI18N

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

        javax.swing.GroupLayout panelFotografia2Layout = new javax.swing.GroupLayout(panelFotografia2);
        panelFotografia2.setLayout(panelFotografia2Layout);
        panelFotografia2Layout.setHorizontalGroup(
            panelFotografia2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFotografia2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
        );
        panelFotografia2Layout.setVerticalGroup(
            panelFotografia2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFotografia2Layout.createSequentialGroup()
                .addGroup(panelFotografia2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelFotografia2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(panelFotografia2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioActionPerformed

    private void txtUsuarioFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioFocusGained
        txtUsuario.setSelectionStart(0);
        txtUsuario.setSelectionEnd(txtUsuario.getText().length());
    }//GEN-LAST:event_txtUsuarioFocusGained

    private void txtUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUsuarioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtClaveAnterior.requestFocus();
        }
    }//GEN-LAST:event_txtUsuarioKeyPressed

    private void txtClaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClaveActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClaveActionPerformed

    private void txtClaveFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClaveFocusGained
        txtClave.setSelectionStart(0);
        txtClave.setSelectionEnd(txtClave.getText().length());
    }//GEN-LAST:event_txtClaveFocusGained

    private void txtClaveKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClaveKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            txtClave1.setEnabled(true);
            txtClave1.setEditable(true);
            txtClave1.requestFocus();
        }
    }//GEN-LAST:event_txtClaveKeyPressed

    private void txtClave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClave1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClave1ActionPerformed

    private void txtClave1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClave1FocusGained
       txtClave1.setSelectionStart(0);
        txtClave1.setSelectionEnd(txtClave1.getText().length());
    }//GEN-LAST:event_txtClave1FocusGained

    private void txtClave1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClave1KeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            compararClaves();
        }
    }//GEN-LAST:event_txtClave1KeyPressed

    private void txtClaveAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtClaveAnteriorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtClaveAnteriorActionPerformed

    private void txtClaveAnteriorFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClaveAnteriorFocusGained
        txtClaveAnterior.setSelectionStart(0);
        txtClaveAnterior.setSelectionEnd(txtClaveAnterior.getText().length());
    }//GEN-LAST:event_txtClaveAnteriorFocusGained

    private void txtClaveAnteriorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtClaveAnteriorKeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
           try {
               char passArray[] = txtClaveAnterior.getPassword();
               for (int i = 0; i < passArray.length; i++) {
                   char c = passArray[i];
                   // if (!Character.isLetterOrDigit(c)) return false;
               }

               String passAnterior = new String(passArray);
               // CUsuarios usua= ini.getUser();
               if(this.user.getClaveUsuario().equals(Inicio.cifrar(passAnterior))){
                   txtClaveAnterior.setEditable(false);
                   txtClave.setEnabled(true);
                   txtClave.setEditable(true);
                   txtClave.requestFocus();
               }else{
                   JOptionPane.showMessageDialog(null, "Clave ó usuario errado", "ERROR", 1);
                    txtClaveAnterior.requestFocus();
               }
               
           } catch (Exception ex) {
               Logger.getLogger(CambiarClave.class.getName()).log(Level.SEVERE, null, ex);
           }
        }
          
        
    }//GEN-LAST:event_txtClaveAnteriorKeyPressed

    private void btnAceptarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAceptarMouseClicked
        if( ingresar()){

        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnAceptarMouseClicked

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
          
        if(iguales){
            int x = JOptionPane.showConfirmDialog(this, "Desea Actualizar la clave de usuario ?", "Actualizar registro", 0);
            if (x == 0) {
                try {
                    
                    if (validar()) {
                        if (user.cambiarClave(Inicio.deCifrar(user.getNombreUsuario()), claveFinal)) {
                            //new Thread(new HiloCambiarClave(5,user,claveFinal)).start();
                            JOptionPane.showInternalMessageDialog(this, "El clave del usuario ha sido guardado perfectamente", "Registro guardado", 1);
                            txtClave.setEnabled(false);
                            txtClave.setEditable(false);
                            txtClave1.setEnabled(false);
                            txtClave1.setEditable(false);
                            txtClaveAnterior.setEditable(false);
                            btnAceptar.setEnabled(false);
                            switch(user.getTipoAcceso()){
                                case 0:
                                    break;
                                            
                                case 1:
                                    break;
                                case 2:
                                     PrincipalAdministrador.jMenuBar1.setVisible(true);
                                    break;
                                case 3:
                                     PrincipalTalentoHumanoAdministrador.jMenuBar1.setVisible(true);
                                    break;
                                case 4:
                                     PrincipalTalentoHumanoAuxiliar.jMenuBar1.setVisible(true);
                                     
                                    break;
                                 case 5:
                                       PrincipalLogistica.jMenuBar1.setVisible(true);
                                     break;
                                default:
                                    break;
                            }
                           
                           
                            
                            //PrincipalTalentoHumanoAdministrador.jMenuBar1.setVisible(true);
                            this.dispose();
                        } else {
                            JOptionPane.showInternalMessageDialog(this, "Se presentó un error al actualizar la clave de usuario", "Error al actualizar", 0);
                        }

                    } else {
                        JOptionPane.showInternalMessageDialog(this, mensaje, "Error al guardar", 0);
                    }
                } catch (Exception ex) {
                    Logger.getLogger(CambiarClave.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                
            }
          
       }

    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnAceptarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAceptarKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            ingresar();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnAceptarKeyPressed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
       this.setVisible(false);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void txtClave1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtClave1FocusLost
       compararClaves();
    }//GEN-LAST:event_txtClave1FocusLost

    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel2;
    private org.edisoncor.gui.panel.PanelImage panelFotografia2;
    private javax.swing.JPasswordField txtClave;
    private javax.swing.JPasswordField txtClave1;
    public javax.swing.JPasswordField txtClaveAnterior;
    public javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables

    private boolean ingresar()  {
       
        boolean verificado=false;
         String msg = null;
        
            char passArray[] = txtClave.getPassword();
            for (int i = 0; i < passArray.length; i++) {
                char c = passArray[i];
                // if (!Character.isLetterOrDigit(c)) return false;
            }

            String pass = new String(passArray);
            if (txtUsuario.getText().isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No pueden haber campos vacíos", "LLenar los dos campos", 1);
            } else {
              try {
                    //ini = new Inicio(this.txtUsuario.getText(), pass);
                    if (true) {
                        verificado=true;

                    } else {
                       
                    }
                } catch (Exception ex) {
                    Logger.getLogger(CambiarClave.class.getName()).log(Level.SEVERE, null, ex);
                }


            }
       

    return verificado;
        
    }
    
     public boolean validar() {
        boolean verificado = true;
        mensaje = "";
        if (txtUsuario.getText().isEmpty()) {
            mensaje += "Campo del usuario está vacío" + "  \n";
            verificado = false;
        }
        if (txtClaveAnterior.getText().isEmpty()) {
            mensaje += "Campo de la clave anterior está vacío" + "  \n";
            verificado = false;
        }
        if (txtClave.getText().isEmpty()) {
            mensaje += "Campo de la clave 1 está vacío " + "  \n";
            verificado = false;
        }
        if (txtClave1.getText().isEmpty()) {
            mensaje += "Campo de la clave 2 está vacío " + "  \n";
            verificado = false;
        }

        if (!iguales) {
            mensaje += "las claves ingresadas son diferentes " + "  \n";
            verificado = false;
        }
        
        if(claveFinal.equals("123456")){
            mensaje += "Debe cambiar la clave, no puede ser igual a la clave inicial " + "  \n";
            verificado = false;
        }
        return verificado;
    }

    private void compararClaves() throws HeadlessException {
        char passArray1[] = txtClave.getPassword();
            for (int i = 0; i < passArray1.length; i++) {
                char c = passArray1[i];
                // if (!Character.isLetterOrDigit(c)) return false;
            }

            String pass1 = new String(passArray1);
            
            char passArray2[] = txtClave1.getPassword();
            for (int i = 0; i < passArray2.length; i++) {
                char c = passArray2[i];
                // if (!Character.isLetterOrDigit(c)) return false;
            }

            String pass2 = new String(passArray2);
         
         if(pass1.equals(pass2)){
             //JOptionPane.showMessageDialog(null, "claves iguales..", "Erroe en las claves digitadas", 0);
             iguales=true;
             claveFinal=pass2;
             btnAceptar.setEnabled(true);
             btnAceptar.requestFocus();
         }else{
             iguales=false;
             
            JOptionPane.showMessageDialog(null, "La clave1 y la clave2 son diferentes, no coinciden..", "Error en las claves digitadas", 1); 
         }
    }

   
}
