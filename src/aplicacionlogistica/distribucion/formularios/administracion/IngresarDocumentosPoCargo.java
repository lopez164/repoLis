/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.administracion;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.CCargos;
import aplicacionlogistica.distribucion.objetos.CDocumentos;
import aplicacionlogistica.distribucion.objetos.CDocumentosPorCargo;
import aplicacionlogistica.distribucion.objetos.CDocumentosPorEmpleado;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VLI_488
 */
public class IngresarDocumentosPoCargo extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    File archivo;
    CEmpleados empleado;
    CCargos cargo;
    CDocumentos documento;
    CDocumentosPorCargo docsPorCargo;
    CDocumentosPorEmpleado docsPorEmpleado;
    ArrayList<CDocumentos> arrTodosDocumentosPendientes;
    ArrayList<CDocumentos> arrDocumentosFaltantes;
    ArrayList<CDocumentosPorEmpleado> arrDocumentosVencidos;
    ArrayList<CDocumentosPorEmpleado> arrDocumentosVigentes;
    ArrayList<CDocumentosPorEmpleado> arrDocumentosPorEmpleado;
    ArrayList<CDocumentos> arrDocumentosEmpleado;
    int indiceLista = 0;
    DefaultTableModel modelo1;
    int columna = 0;
    boolean actualizar = false;
    String usuario;
    String mensaje = null;
    Inicio ini;
   
    public void setEmpleado(CEmpleados empleado) {
        this.empleado = empleado;
    }

    public ArrayList<CDocumentos> getArrDocumentosEmpleado() {
        return arrDocumentosEmpleado;
    }

    public ArrayList<CDocumentos> getArrDocumentosFaltantes() {
        return arrDocumentosFaltantes;
    }

    public ArrayList<CDocumentosPorEmpleado> getArrDocumentosVencidos() {
        return arrDocumentosVencidos;
    }

    public ArrayList<CDocumentosPorEmpleado> getArrDocumentosVigentes() {
        return arrDocumentosVigentes;
    }

    public ArrayList<CDocumentosPorEmpleado> getArrDocumentosPorEmpleado() {
        return arrDocumentosPorEmpleado;
    }
    
     /**
     * Creates new form IngresarPersonas
     */
    public String getUsuario() {
        return usuario;
    }

    public IngresarDocumentosPoCargo(Inicio ini) {
        initComponents();
        this.ini=ini;
        this.usuario = ini.getUsuarioDelSistema();
        //foto = new File("/aplicacionlogistica/configuracion/imagenes/perfil.jpg");
        //  JScrollPane jscp = new JScrollPane(this);
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
       
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pumImagen = new javax.swing.JPopupMenu();
        agregarImagen = new javax.swing.JMenu();
        borrarFila = new javax.swing.JPopupMenu();
        borraUnaFila = new javax.swing.JMenu();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaDocsVencidos = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        lblRuta1 = new org.edisoncor.gui.label.LabelCustom();
        jLabel4 = new javax.swing.JLabel();
        cbxCargos = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstDocumentosPorCargo = new javax.swing.JList();

        agregarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Display 16x16.png"))); // NOI18N
        agregarImagen.setText("Agregar Imagen Empleado");
        agregarImagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregarImagenMouseClicked(evt);
            }
        });
        pumImagen.add(agregarImagen);

        borraUnaFila.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Remove.png"))); // NOI18N
        borraUnaFila.setText("Borra una Fila");
        borraUnaFila.setToolTipText("");
        borraUnaFila.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                borraUnaFilaMouseClicked(evt);
            }
        });
        borraUnaFila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borraUnaFilaActionPerformed(evt);
            }
        });
        borrarFila.add(borraUnaFila);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Formulario para asignar los Documentos a los Empleados");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jTabbedPane1.setEnabled(false);

        tablaDocsVencidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "NOMBRES DOCUMENTO", "FECHA EXP.", "FECHA VENC.", "ARCHIVO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaDocsVencidos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tablaDocsVencidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDocsVencidosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tablaDocsVencidos);
        if (tablaDocsVencidos.getColumnModel().getColumnCount() > 0) {
            tablaDocsVencidos.getColumnModel().getColumn(0).setResizable(false);
            tablaDocsVencidos.getColumnModel().getColumn(1).setResizable(false);
            tablaDocsVencidos.getColumnModel().getColumn(1).setPreferredWidth(250);
            tablaDocsVencidos.getColumnModel().getColumn(2).setResizable(false);
            tablaDocsVencidos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tablaDocsVencidos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tablaDocsVencidos.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        jTabbedPane1.addTab(" Vencidos", jScrollPane4);

        jPanel6.setAutoscrolls(true);
        jPanel6.setDoubleBuffered(false);
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblRuta1.setText(" ");
        lblRuta1.setFont(new java.awt.Font("Arial", 1, 12)); // NOI18N
        jPanel6.add(lblRuta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(636, 209, -1, -1));

        jTabbedPane1.addTab("Indicadores", jPanel6);

        jLabel4.setText("Cargo");

        cbxCargos.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxCargos, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxCargos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(45, 45, 45)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 555, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/exit.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setToolTipText("Salir de ingresar empleado...");
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
                .addContainerGap()
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 95, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lstDocumentosPorCargo.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstDocumentosPorCargo.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                lstDocumentosPorCargoValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(lstDocumentosPorCargo);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 477, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(47, 47, 47)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(107, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(200, Short.MAX_VALUE))
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 681, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 60, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        if (actualizar) {
           
            btnNuevo.setEnabled(false);
            habilitar(true);
           // txtCedula.setEditable(false);

        } else {
            limpiar();
//            FBuscarEmpleados form = new FBuscarEmpleados(this);
//            PrincipalTalentoHumano.escritorio.add(form);
//            form.toFront();
//            form.setClosable(true);
//            form.setVisible(true);
//            form.setTitle("Formulario para buscar Empleados por apellidos");
//            form.txtApellidosPersona.requestFocus();
            btnNuevo.setEnabled(false);
            
        }
        //txtCedula.requestFocus();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void agregarImagenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agregarImagenMouseClicked
        /*File fileFoto = null;
        if (actualizarFoto) {
            if (!txtCedula.getText().isEmpty()) {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("Elija la imagen de la fotografía del Empleado");
                FileNameExtensionFilter filter = new FileNameExtensionFilter(
                        "JPG & GIF Imagenes", "jpg", "gif", "png");
                chooser.setFileFilter(filter);
                int returnVal = chooser.showOpenDialog(chooser);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    fileFoto = chooser.getSelectedFile();
                    if (fileFoto != null) {
                        ImageIcon filfoto = new ImageIcon(fileFoto.getPath());
                        this.foto = fileFoto;
                        //panelFotografia.setIcon(filfoto);
                        tieneFoto = true;
                        /*javax.swing.GroupLayout panelImage1Layout = new javax.swing.GroupLayout(panelFotografia);
                        panelFotografia.setLayout(panelImage1Layout);
                        panelImage1Layout.setHorizontalGroup(
                                panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGap(0, 329, Short.MAX_VALUE));
                        panelImage1Layout.setVerticalGroup(
                                panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGap(0, 405, Short.MAX_VALUE)); 
                    }

                }
            } else {
                
            }
        }**/

    }//GEN-LAST:event_agregarImagenMouseClicked

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
//        if (!txtCedula.getText().isEmpty()) {
//           if(!arrDocumentosPorEmpleado.isEmpty()){
//               int x = JOptionPane.showConfirmDialog(this, "Desea guardar el registro ?", "Guardar registro", 0);
//            if (x == 0) {
//               
//                    boolean grabado = false;
//                    for (CDocumentosPorEmpleado obj : arrDocumentosPorEmpleado) {
//                        if (obj.grabarDocumentosPorEmpleado()) {
//                             grabado = true;
//                        } else {
//                            grabado = false;
//                        }
//                    }
//                    if (grabado) {
//
//                        JOptionPane.showMessageDialog(null, "Datos Guardados correctamente", "Guardado", 1, null);
//
//                    } else {
//                        JOptionPane.showMessageDialog(null, "Error al guardar los datos", "Error", 1, null);
//                    }
//            } 
//           }else{
//             JOptionPane.showInternalMessageDialog(this, "No se han agregado documentos al empleado " + empleado.getNombres() + " " +
//             empleado.getApellidos(), "Error al guardar", 2);  
//           }
//           
//        } else {
//            JOptionPane.showInternalMessageDialog(this, "No se pueden Guardar Datos con campos vacíos", "Error al guardar", 2);
//        }

    }//GEN-LAST:event_btnGrabarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiar();
        habilitar(false);
        btnNuevo.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void tablaDocsVencidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDocsVencidosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaDocsVencidosMouseClicked

    private void lstDocumentosPorCargoValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_lstDocumentosPorCargoValueChanged
//        indiceLista = 0;
//        int contador = 0; // contador para verificar que el elemento no exista en la tabla(=0) sí existe (>=1)
//        for (int i = 0; i < tablaIngresoDocumentos.getRowCount(); i++) {
//            String str = tablaIngresoDocumentos.getValueAt(i, 1).toString();
//            if (lstDocumentosPorCargo.getSelectedValue().toString().equals(tablaIngresoDocumentos.getValueAt(i, 1).toString())) {
//                contador++;
//                JOptionPane.showMessageDialog(null, "Elemento ya ingresado en la tabla", "Valor duplicado", 1);
//            }
//        }
//        txtNombreDocumento.setEditable(false);
//        if (contador == 0) {
//            limpiarDocumentoAgregado();
//            int i = 0;
//            for (CDocumentos obj : arrTodosDocumentosPendientes) {
//                String str = lstDocumentosPorCargo.getSelectedValue().toString();
//                //String str2=obj.getNombreTipoDocumento();
//                if (obj.getNombreTipoDocumento().equals(str)) {
//                    txtNombreDocumento.setText(obj.getNombreTipoDocumento());
//                    documento = obj;
//                    txtEntidadEmisora.setEditable(true);
//                    txtNumeroDocumento.setEditable(true);
//                    txtEntidadEmisora.setText("");
//                    lblRuta.setText("");
//                    txtNumeroDocumento.setText("");
//                    btnAgregarArchivo.setEnabled(true);
//                    dateExpedicion.setEnabled(true);
//                    if (documento.getTieneVencimiento() == 1) {
//                        dateVencimiento.setEnabled(true);
//                        //dateVencimiento.setVisible(true);
//                       // lblVencimimento.setVisible(true);
//                    } else {
//                        dateVencimiento.setEnabled(false);
//                       // dateVencimiento.setVisible(false);
//                       // lblVencimimento.setVisible(false);
//                    }
//                    indiceLista = i;
//                }
//
//                i++;
//            }
//         habilitar(true);  
//         txtEntidadEmisora.requestFocus();
//         
//        }
//        contador = 0;
        
    }//GEN-LAST:event_lstDocumentosPorCargoValueChanged

    private void borraUnaFilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borraUnaFilaActionPerformed
     
    }//GEN-LAST:event_borraUnaFilaActionPerformed

    private void borraUnaFilaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borraUnaFilaMouseClicked
//        int x;
//        if (tablaIngresoDocumentos.getRowCount() > 0) {
//            int fila = tablaIngresoDocumentos.getSelectedRow();
//            x = JOptionPane.showOptionDialog(null, "Desea eliminar el Registro de la Tabla?", "Eliminar Registro", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No", "Cancelar"}, "No");
//            if (x == 0) {
//                modelo1.removeRow(fila);
//                String doc;
//                arrDocumentosPorEmpleado.remove(fila);
//                for (int i = 0; i < tablaIngresoDocumentos.getRowCount(); i++) {
//                    tablaIngresoDocumentos.setValueAt(i + 1, i, 0);
//                }
//            }
//        }else{
//           JOptionPane.showInternalMessageDialog(this, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);
//        }
    }//GEN-LAST:event_borraUnaFilaMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu agregarImagen;
    private javax.swing.JMenu borraUnaFila;
    private javax.swing.JPopupMenu borrarFila;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox cbxCargos;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    public javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    public javax.swing.JTabbedPane jTabbedPane1;
    private org.edisoncor.gui.label.LabelCustom lblRuta1;
    private javax.swing.JList lstDocumentosPorCargo;
    private javax.swing.JPopupMenu pumImagen;
    private javax.swing.JTable tablaDocsVencidos;
    // End of variables declaration//GEN-END:variables

   

    public void llenarCamposDeTexto(CEmpleados empleado) {
        try {
//            txtNombres.setText(empleado.getNombres());
//            txtApellidos.setText(empleado.getApellidos());
        } catch (Exception ex) {
        }    }

    private void habilitar(boolean valor) {
        try {
            jTabbedPane1.setEnabled(valor);
            
//            txtEntidadEmisora.setEditable(valor);
//            txtNumeroDocumento.setEditable(valor);
//            txtLugarDeExpedicion.setEditable(valor);
//            dateExpedicion.setEnabled(valor);
//            //dateVencimiento.setEnabled(valor);
//            btnAgregarArchivo.setEnabled(valor);
//            btnAgregarRegistro.setEnabled(valor);
//            tablaIngresoDocumentos.setEnabled(valor);
//            tablaDocsVencidos.setEnabled(valor);
//            tablaDocsFaltantes.setEnabled(valor);
//            tablaDocsVigentes.setEnabled(valor);
        } catch (Exception ex) {
        }
    }

    public boolean validar() {
        boolean verificado = true;
//        mensaje = "";
//        if (txtNombres.getText().isEmpty()) {
//            mensaje += "No ha colocado el nombre del empleado" + "  \n";
//            verificado = false;
//        }
//        if (txtApellidos.getText().isEmpty()) {
//            mensaje += "No ha colocado los apellidos del empleado" + "  \n";
//            verificado = false;
//        }
        
        
        return verificado;
    }
    private boolean validarTabla() {
        boolean validado = true;
        mensaje = "";
        if (archivo == null) {
            mensaje += "No ha escogido un archivo de soporte del documento actual.  \n";
            validado = false;
        }
//        if (txtNumeroDocumento.getText().isEmpty()) {
//            mensaje += "No ha introducido el número del documento. \n ";
//            validado = false;
//        }
//        if (txtEntidadEmisora.getText().isEmpty()) {
//            mensaje += "No ha introducido la entidad que genera el documento. \n";
//            validado = false;
//        }
//         if (txtLugarDeExpedicion.getText().isEmpty()) {
//            mensaje += "No ha introducido donde fué expedido el documento. \n";
//            validado = false;
//        }

        return validado;
    }

   /* public boolean guardarRegistrosDocumentos() {
        boolean guardado = false;
        try {

            CEmpleados empleado = new CEmpleados(usuario);
            empleado.setCedula(txtCedula.getText().trim());
            empleado.setNombres(txtNombres.getText().trim());
            empleado.setApellidos(txtApellidos.getText().trim());
           
            

            guardado = empleado.grabarEmpleados();


        } catch (Exception ex) {
            Logger.getLogger(IngresarDocumentosEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return guardado;
    } **/

    private void limpiar() {

        try {
//            txtCedula.setText("");
//            txtNombres.setText("");
//            txtApellidos.setText("");
//            txtCargo.setText("");
            DefaultListModel model = new DefaultListModel(); 
            lstDocumentosPorCargo.setModel(model); 
//            DefaultTableModel modelo = (DefaultTableModel) tablaDocsFaltantes.getModel();
//             if (modelo.getRowCount() > 0) {
//            int a = modelo.getRowCount() - 1;
//            for (int i = a; i >= 0; i--) {
//                modelo.removeRow(i);
//            }
//            }    
//             
//             modelo = (DefaultTableModel) tablaDocsVencidos.getModel();
//             if (modelo.getRowCount() > 0) {
//            int a = modelo.getRowCount() - 1;
//            for (int i = a; i >= 0; i--) {
//                modelo.removeRow(i);
//            }
//            }    
//            
//            modelo = (DefaultTableModel) tablaDocsVigentes.getModel();
//             if (modelo.getRowCount() > 0) {
//            int a = modelo.getRowCount() - 1;
//            for (int i = a; i >= 0; i--) {
//                modelo.removeRow(i);
//            }
   //         }    
            btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); //
            btnNuevo.setText("Nuevo");
            btnNuevo.setEnabled(true);
          jPanel6.removeAll();
        } catch (Exception ex) {
        }

    }

    public void llenarDatos() {
        Date fechaActual= new Date();
        arrDocumentosVigentes= new ArrayList<>();
        arrDocumentosVencidos= new ArrayList<>();
        arrDocumentosFaltantes=new ArrayList<>();
        //this.cargo = empleado.getCargo(); // se define el cargo que ocupa actualmente el empleado
//        docsPorCargo = new CDocumentosPorCargo(ini,empleado.getCargo());
        docsPorEmpleado = new CDocumentosPorEmpleado(ini);              // se define los documentos que actualmente tiene el empleado
//        arrDocumentosFaltantes = docsPorCargo.getDocumentos(empleado.getCargo(), empleado, false); // trae todos los documentos que  le faltan al empleado 
        ArrayList<CDocumentos> docsVigentes= new ArrayList();
        docsVigentes=docsPorEmpleado.getDocumentos(empleado);
        for(CDocumentos obj: docsVigentes){
            /* primero verifica si el documento tiene vencimiento
             * si tiene vencimienton pregunta sí está vencido
             *   si está vencido incorpora el registro a un array de documentos vencidos
             *   sino esta vencido lo incorpora a un array de documentos vigentes
             * sino tiene vencimiento lo anexa a un array de documentos vigentes
             */
            
            if(obj.getTieneVencimiento()==1){ // verifica si el documento  tiene vencimiento el documento
               CDocumentosPorEmpleado dxe = new CDocumentosPorEmpleado(empleado,obj);
               if(dxe.getFechaVencimiento().before(fechaActual) ){ // verifica si está venmcido
                  arrDocumentosVencidos.add(dxe);   // lo incorpora al array de documentos vencidos
               }else{                         // no tiene vencimiento el documento
                   arrDocumentosVigentes.add(dxe);//lo incorpora al array de documentos vigentes
               }           
            }else{  // este es el caso en que el documentos no tiene vencimiento
                CDocumentosPorEmpleado dxe = new CDocumentosPorEmpleado(empleado,obj);
                arrDocumentosVigentes.add(dxe); //lo incorpora al array de documentos vigentes
            }
           
        }
        Iterator i = arrDocumentosFaltantes.iterator();
       // DefaultListModel modelLista = new DefaultListModel();
        //modelLista.removeAllElements();
          arrTodosDocumentosPendientes= new ArrayList();
        for (CDocumentos obj : arrDocumentosFaltantes) {
          arrTodosDocumentosPendientes.add(obj);
        }
        for(CDocumentosPorEmpleado obj : arrDocumentosVencidos){
            arrTodosDocumentosPendientes.add(obj.getDocumento());
        }
        Iterator it= arrTodosDocumentosPendientes.iterator();
        DefaultListModel modelLista = new DefaultListModel();
        modelLista.removeAllElements();
        
        
        
        for (CDocumentos obj : arrTodosDocumentosPendientes) {
            if (it.hasNext()) {
                modelLista.addElement(obj.getNombreTipoDocumento());
                it.next();
            }
        }
        
        
        
        lstDocumentosPorCargo.setModel(modelLista);
        llenarTablaDocumentosVigentes();
        llenarTablaDocumentosFaltantes();
        llenarTablaDocumentosVencidos();
//        cargo = empleado.getCargo();
//        txtCargo.setText(cargo.getNombreCargo());
 //       modelo1 = (DefaultTableModel) tablaIngresoDocumentos.getModel();
        arrDocumentosPorEmpleado = new ArrayList<>();
        arrDocumentosEmpleado = new ArrayList<>();
    //    ChartPieDocumentosPorEmpleado chrP1 = new ChartPieDocumentosPorEmpleado("Gráfico Gestión de Documentos del empleado " 
     //           + empleado.getNombres() + " " + empleado.getApellidos() , this);
       
    }
    private void limpiarDocumentoAgregado(){
//        txtNombreDocumento.setText("");
//        txtEntidadEmisora.setText("");
//        txtNumeroDocumento.setText("");
//        txtLugarDeExpedicion.setText("");
//        txtRutaDelArchivo.setText("");
//        dateExpedicion.setDate(new Date());
//        dateVencimiento.setDate(new Date());
    }

    private void llenarTablaDocumentosVigentes() {
        DefaultTableModel modelo = new DefaultTableModel();
        int fil = 0;
//        modelo = (DefaultTableModel) tablaDocsVigentes.getModel();
//        for (CDocumentosPorEmpleado obj : arrDocumentosVigentes) {
//            modelo.addRow(new Object[fil]);
//            tablaDocsVigentes.setValueAt(fil + 1, fil, 0);
//            tablaDocsVigentes.setValueAt(obj.getDocumento().getNombreTipoDocumento().toString(), fil, 1);
//            tablaDocsVigentes.setValueAt(obj.getFechaExpedicion(), fil, 2);
//            if (obj.getDocumento().getTieneVencimiento() == 1) {
//                tablaDocsVigentes.setValueAt(obj.getFechaVencimiento(), fil, 3);
//            } else {
//                tablaDocsVigentes.setValueAt(obj.getFechaVencimiento(), fil, 3);
//            }
//            tablaDocsVigentes.setValueAt(true, fil, 4);
//          fil++;
//
//        }
    }
    private void llenarTablaDocumentosVencidos() {
        DefaultTableModel modelo = new DefaultTableModel();
        int fil = 0;
        modelo = (DefaultTableModel) tablaDocsVencidos.getModel();
        for (CDocumentosPorEmpleado obj : arrDocumentosVencidos) {
            modelo.addRow(new Object[fil]);
            tablaDocsVencidos.setValueAt(fil + 1, fil, 0);
            tablaDocsVencidos.setValueAt(obj.getDocumento().getNombreTipoDocumento().toString(), fil, 1);
            tablaDocsVencidos.setValueAt(obj.getFechaExpedicion(), fil, 2);
            if (obj.getDocumento().getTieneVencimiento() == 1) {
                tablaDocsVencidos.setValueAt(obj.getFechaVencimiento(), fil, 3);
            } else {
                tablaDocsVencidos.setValueAt(obj.getFechaVencimiento(), fil, 3);
            }
            tablaDocsVencidos.setValueAt(true, fil, 4);
            
          fil++;

        }
    }
     private void llenarTablaDocumentosFaltantes() {
        DefaultTableModel modelo = new DefaultTableModel();
        int fil = 0;
//        modelo = (DefaultTableModel) tablaDocsFaltantes.getModel();
//        for (CDocumentos obj : arrDocumentosFaltantes) {
//            modelo.addRow(new Object[fil]);
//            tablaDocsFaltantes.setValueAt(fil + 1, fil, 0);
//            tablaDocsFaltantes.setValueAt(obj.getNombreTipoDocumento(), fil, 1);
//            tablaDocsFaltantes.setValueAt(" ", fil, 2);
//            tablaDocsFaltantes.setValueAt(" ", fil, 3);
//           fil++;
//
//        }
    }
     
 
}
