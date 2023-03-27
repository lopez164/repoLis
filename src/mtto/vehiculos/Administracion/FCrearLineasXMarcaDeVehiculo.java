/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.vehiculos.Administracion;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class FCrearLineasXMarcaDeVehiculo extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    Object[] fila = new Object[3];
    DefaultTableModel modelo;
    CUsuarios user;
    String usuario;
    public boolean actualizar = false;
    String mensaje;
    Inicio ini;
    CMarcasDeVehiculos marcaVehiculo;
    public int indice;
    boolean iniciado=false;
    
  //  List<CMarcasDeVehiculos>  listaDeMarcasDeVehiculos;
    

//private FResidentes residente = (FResidentes)this.getParent();//
    /**
     * Creates new form FBuscar2
     */
    public FCrearLineasXMarcaDeVehiculo() {
        initComponents();
        
       
    }

    public FCrearLineasXMarcaDeVehiculo(Inicio ini) {
        
        initComponents();
        this.ini = ini;
        this.user = ini.getUser();
        this.setResizable(false);
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

                    } else {
                        if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
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
                }

                //y listo, regresamos siempre falso para que las demas notificaciones continuen, si regresamos true
                // significa que el dispatcher consumio el evento
                return false;
            }
        });
        
          new Thread(new HiloFcrearLineasXMarcasDeVehiculo(ini, this, "listaDeMarcasDeVehiculos")).start();
         
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        label1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblLineasVehiculos = new javax.swing.JTable();
        chkActivo = new javax.swing.JCheckBox();
        label2 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        txtLineaVehiculo = new javax.swing.JTextField();
        label3 = new javax.swing.JLabel();
        cbxMarcaDeVehiculo = new javax.swing.JComboBox<>();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();

        setClosable(true);
        setTitle("Formulario para el ingreso de Marcas de Vehiculo");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N

        label1.setText("Nombre de la Marca del Vehiculo");

        tblLineasVehiculos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id Linea", "NombreLinea Vehiculo", "Activo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblLineasVehiculos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblLineasVehiculos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLineasVehiculosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblLineasVehiculos);
        if (tblLineasVehiculos.getColumnModel().getColumnCount() > 0) {
            tblLineasVehiculos.getColumnModel().getColumn(0).setResizable(false);
            tblLineasVehiculos.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblLineasVehiculos.getColumnModel().getColumn(1).setResizable(false);
            tblLineasVehiculos.getColumnModel().getColumn(1).setPreferredWidth(300);
            tblLineasVehiculos.getColumnModel().getColumn(2).setResizable(false);
            tblLineasVehiculos.getColumnModel().getColumn(2).setPreferredWidth(50);
        }

        chkActivo.setSelected(true);
        chkActivo.setText("Linea de vehiculo activA");
        chkActivo.setEnabled(false);
        chkActivo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkActivoStateChanged(evt);
            }
        });
        chkActivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkActivoActionPerformed(evt);
            }
        });

        label2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        label2.setText("Id. Linea vehiculo : ");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/exit.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setToolTipText("Salir de ingresar Registro");
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setPreferredSize(new java.awt.Dimension(97, 97));
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/cancel-64x64.png"))); // NOI18N
        btnCancelar.setText("Cancelar");
        btnCancelar.setToolTipText("Limpia la ventana para ingreasr registro...");
        btnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar.setPreferredSize(new java.awt.Dimension(97, 97));
        btnCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });

        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setToolTipText("Agregar ó Modificar un registro");
        btnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNuevo.setPreferredSize(new java.awt.Dimension(97, 97));
        btnNuevo.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoActionPerformed(evt);
            }
        });

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        btnGrabar.setText("Grabar");
        btnGrabar.setToolTipText("Guardar registro nuevo ó modificado");
        btnGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGrabar.setPreferredSize(new java.awt.Dimension(97, 97));
        btnGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(1, 1, 1))
        );

        txtLineaVehiculo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtLineaVehiculoFocusGained(evt);
            }
        });
        txtLineaVehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtLineaVehiculoKeyPressed(evt);
            }
        });

        label3.setText("Nombre de la Linea del vehiculo");

        cbxMarcaDeVehiculo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxMarcaDeVehiculo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxMarcaDeVehiculoItemStateChanged(evt);
            }
        });
        cbxMarcaDeVehiculo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbxMarcaDeVehiculoMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(label2)
                            .addComponent(label3)
                            .addComponent(label1)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(chkActivo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtLineaVehiculo, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(cbxMarcaDeVehiculo, javax.swing.GroupLayout.Alignment.LEADING, 0, 378, Short.MAX_VALUE)))
                        .addContainerGap(101, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(label2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(label1)
                .addGap(1, 1, 1)
                .addComponent(cbxMarcaDeVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(label3)
                .addGap(1, 1, 1)
                .addComponent(txtLineaVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(chkActivo)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        jToolBar1.setRollover(true);

        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jBtnNuevo.setToolTipText("agregar registro");
        jBtnNuevo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnNuevo.setFocusable(false);
        jBtnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnNuevo.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnNuevo);

        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        jBtnGrabar.setToolTipText("grabar registro");
        jBtnGrabar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnGrabar.setEnabled(false);
        jBtnGrabar.setFocusable(false);
        jBtnGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnGrabar.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGrabarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnGrabar);

        jBtnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jBtnCancel.setToolTipText("Cancelar operacion");
        jBtnCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnCancel.setFocusable(false);
        jBtnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnCancel.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnCancel);

        jBtnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jBtnExit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnExit.setFocusable(false);
        jBtnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnExit.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnExitActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnExit);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(1, 1, 1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblLineasVehiculosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLineasVehiculosMouseClicked
  
        new Thread(new HiloFcrearLineasXMarcasDeVehiculo(ini, this, "seleccionarFila")).start();

    }//GEN-LAST:event_tblLineasVehiculosMouseClicked

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        salir();
    }//GEN-LAST:event_btnSalirActionPerformed

    public void salir() {
        this.dispose();
        this.setVisible(false);
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    public void cancelar() {
        actualizar = false;
        txtLineaVehiculo.setText("");
        chkActivo.setEnabled(false);
        chkActivo.setSelected(false);
        label2.setVisible(false);
       btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
       jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));
        btnNuevo.setText("Nuevo");
        btnNuevo.setEnabled(true);
        jBtnNuevo.setEnabled(true);
        btnGrabar.setEnabled(false);
        jBtnGrabar.setEnabled(false);

        tblLineasVehiculos.setEnabled(true);
    }

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_btnNuevoActionPerformed

    public void nuevo() {
        if (actualizar) {
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            btnGrabar.setEnabled(true);
            jBtnGrabar.setEnabled(true);
            txtLineaVehiculo.setEnabled(true);
            chkActivo.setEnabled(true);
            btnNuevo.setEnabled(false);
            txtLineaVehiculo.requestFocus();

        } else {
            txtLineaVehiculo.setEnabled(true);
            chkActivo.setSelected(true);
            tblLineasVehiculos.setEnabled(false);
            btnNuevo.setEnabled(false);
            jBtnNuevo.setEnabled(false);
            btnGrabar.setEnabled(true);
            jBtnGrabar.setEnabled(true);
            chkActivo.setSelected(true);
            chkActivo.setEnabled(true);
            txtLineaVehiculo.requestFocus();
        }
        //txtCedula.requestFocus();
    }

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        grabar();
    }//GEN-LAST:event_btnGrabarActionPerformed

    public void grabar() throws HeadlessException {
        if (!txtLineaVehiculo.getText().isEmpty()) {
            int x = JOptionPane.showConfirmDialog(this, "Desea guardar el registro ?", "Guardar registro", 0);
            if (x == 0) {

                
                new Thread(new HiloFcrearLineasXMarcasDeVehiculo(ini, this, "guardar")).start();

            }
        }
    }

    private void chkActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkActivoActionPerformed
        if (chkActivo.isSelected()) {
            chkActivo.setText("Linea Vehiculo activo");
        } else {
            chkActivo.setText("Linea Vehiculo No Activo");
        }
    }//GEN-LAST:event_chkActivoActionPerformed

    private void chkActivoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkActivoStateChanged
        if (chkActivo.isSelected()) {
            chkActivo.setText("Linea Vehiculo activo");
        } else {
            chkActivo.setText("Linea Vehiculo No Activo");
        }
    }//GEN-LAST:event_chkActivoStateChanged

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabarActionPerformed
        grabar();
    }//GEN-LAST:event_jBtnGrabarActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        cancelar();
    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        salir();        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnExitActionPerformed

    private void txtLineaVehiculoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtLineaVehiculoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLineaVehiculoFocusGained

    private void txtLineaVehiculoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtLineaVehiculoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLineaVehiculoKeyPressed

    private void cbxMarcaDeVehiculoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxMarcaDeVehiculoItemStateChanged
     if(iniciado){
       indice=cbxMarcaDeVehiculo.getSelectedIndex();
      
      new Thread(new HiloFcrearLineasXMarcasDeVehiculo(ini, this, "llenarJtable")).start(); 
    
     }
      
    }//GEN-LAST:event_cbxMarcaDeVehiculoItemStateChanged

    private void cbxMarcaDeVehiculoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbxMarcaDeVehiculoMouseClicked
     
    }//GEN-LAST:event_cbxMarcaDeVehiculoMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JButton btnNuevo;
    public javax.swing.JButton btnSalir;
    public javax.swing.JComboBox<String> cbxMarcaDeVehiculo;
    public javax.swing.JCheckBox chkActivo;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JToggleButton jBtnGrabar;
    public javax.swing.JButton jBtnNuevo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel label1;
    public javax.swing.JLabel label2;
    private javax.swing.JLabel label3;
    public javax.swing.JTable tblLineasVehiculos;
    public javax.swing.JTextField txtLineaVehiculo;
    // End of variables declaration//GEN-END:variables
 public boolean validar() {
        boolean verificado = true;
        mensaje = "";
        if (txtLineaVehiculo.getText().isEmpty()) {
            mensaje += "No ha colocado el nombre de Marca de vehiculo " + "  \n";
            verificado = false;
        }

        return verificado;

    }

//    public boolean guardarRegistroLineaDeVehiculo() {
//        boolean valida = false;
//        try {
//
//            CMarcasDeVehiculos marca = new CMarcasDeVehiculos(ini);
//            marca.setNombreMarcaDeVehiculos(txtLineaVehiculo.getText().trim());
//            if (chkActivo.isSelected()) {
//                marca.setActivoMarcaDeVehiculos(1);
//            } else {
//                marca.setActivoMarcaDeVehiculos(0);
//            }
//            valida = marca.grabarMarcaDeVehiculoss();
//            if (valida) {
//                 new Thread(new HiloListadoDeMarcasVehiculos(ini)).start();
//               JOptionPane.showInternalMessageDialog(this, "El registro  ha sido guardado perfectamente", "Registro guardado", 1);
//
//                btnNuevo.setEnabled(true);
//                jBtnNuevo.setEnabled(true);
//                btnGrabar.setEnabled(false);
//                jBtnGrabar.setEnabled(false);
//                llenarjTable();
//            }
//
//        } catch (Exception ex) {
//            Logger.getLogger(FCrearLineasXMarcaDeVehiculo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return valida;
//    }
//
//    public boolean actualizarRegistroLineaDeVehiculo() {
//        boolean valida = false;
//        try {
//            int row = tblLineasVehiculos.getSelectedRow();
//            label2.setText("Id. Marca de Vehiculo :  " + String.valueOf(tblLineasVehiculos.getValueAt(row, 0)));
//            CMarcasDeVehiculos marcaVehiculo = new CMarcasDeVehiculos(ini);
//            marcaVehiculo.setIdMarcaDeVehiculos((Integer) tblLineasVehiculos.getValueAt(row, 0));
//            marcaVehiculo.setNombreMarcaDeVehiculos(txtLineaVehiculo.getText().trim());
//            if (chkActivo.isSelected()) {
//                marcaVehiculo.setActivoMarcaDeVehiculos(1);
//            } else {
//                marcaVehiculo.setActivoMarcaDeVehiculos(0);
//            }
//
//            valida = marcaVehiculo.actualizarMarcaDeVehiculoss();
//
//            if (valida) {
//                new Thread(new HiloListadoDeMarcasVehiculos(ini)).start();
//                JOptionPane.showInternalMessageDialog(this, "El registro  ha sido Actualizado perfectamente", "Registro guardado", 1);
//               
//                label2.setVisible(true);
//                label2.setText("Id. Marca de Vehiculo :  " + marcaVehiculo.getIdMarcaDeVehiculos());
//                btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
//                btnNuevo.setEnabled(true);
//                jBtnNuevo.setEnabled(true);
//                btnGrabar.setEnabled(false);
//                jBtnGrabar.setEnabled(false);
//                llenarjTable();
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(FCrearLineasXMarcaDeVehiculo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return valida;
//    }

    private boolean habilitar(boolean valor) {
        boolean valida = false;

        return valida;
    }

//    public void llenarjTable() throws Exception {
//        DefaultTableModel modelo = (DefaultTableModel) tblLineasVehiculos.getModel();
//        if (modelo.getRowCount() > 0) {
//            int a = modelo.getRowCount() - 1;
//            for (int i = a; i >= 0; i--) {
//                modelo.removeRow(i);
//            }
//        }
//
//        DefaultTableModel modelo2 = (DefaultTableModel) tblLineasVehiculos.getModel();
//
//        for (CMarcasDeVehiculos obj : ini.getListaDeMarcasDeVehiculos()) {
//            int filaTabla2 = tblLineasVehiculos.getRowCount();
//            modelo2.addRow(new Object[tblLineasVehiculos.getRowCount()]);
//            tblLineasVehiculos.setValueAt(obj.getIdMarcaDeVehiculos(), filaTabla2, 0);  // item
//            tblLineasVehiculos.setValueAt(obj.getNombreMarcaDeVehiculos(), filaTabla2, 1); // numero de factura
//            if (obj.getActivoMarcaDeVehiculos() == 1) {
//                tblLineasVehiculos.setValueAt(true, filaTabla2, 2); //
//            } else {
//                tblLineasVehiculos.setValueAt(false, filaTabla2, 2); //
//            }
//
//        }
//
//    }
    
    public void limpiarTabla (){
        modelo = (DefaultTableModel) tblLineasVehiculos.getModel();
        
       if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                try {
                    System.out.println(tblLineasVehiculos.getValueAt(i, 1));
                    modelo.removeRow(i);
                    Thread.sleep(1);
                } catch (InterruptedException ex) {
                    Logger.getLogger(FCrearLineasXMarcaDeVehiculo.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        
    }
    
    public static void deleteAllRows(DefaultTableModel model) {
    for( int i = model.getRowCount() - 1; i >= 0; i-- ) {
        model.removeRow(i);
    }
}


}