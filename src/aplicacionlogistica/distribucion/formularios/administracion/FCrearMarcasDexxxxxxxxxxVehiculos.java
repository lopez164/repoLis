/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.administracion;


import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloGuardarMarcaDeVehiculo;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import mtto.vehiculos.Administracion.CMarcasDeVehiculos;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
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
public class FCrearMarcasDexxxxxxxxxxVehiculos  extends javax.swing.JInternalFrame {
KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
Object[] fila = new Object[3];
DefaultTableModel modelo;
CUsuarios user;
String usuario;
public boolean actualizar=false;
Inicio  ini;
String mensaje;
public CMarcasDeVehiculos marcaDeVehiculos= null;
    

//private FResidentes residente = (FResidentes)this.getParent();//
    /**
     * Creates new form FBuscar2
     */
    public FCrearMarcasDexxxxxxxxxxVehiculos() {
        initComponents();
         txtMarcadeVehiculo.requestFocus();
    }
 public FCrearMarcasDexxxxxxxxxxVehiculos(Inicio ini) {
        initComponents();
        this.ini=ini;
        this.user=ini.getUser();
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation ((screenSize.width - this.getSize().width) / 2,(screenSize.height - this.getSize().height) / 2);
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
        txtMarcadeVehiculo.setEnabled(false);
     
         for(CMarcasDeVehiculos obj : ini.getListaDeMarcasDeVehiculos()){
                    fila = new Object[3];
                    fila[0] = obj.getIdMarcaDeVehiculos();
                    fila[1] = obj.getNombreMarcaDeVehiculos();
                    if(obj.getActivoMarcaDeVehiculos()<1){
                       fila[2] = false;
                    }else{
                       fila[2] = true; 
                    }
                    fila[2] = true;
                    modelo = (DefaultTableModel) jTable1.getModel();
                    modelo.addRow(fila);
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtMarcadeVehiculo = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        chkActivo = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnNuevo = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();

        setClosable(true);
        setTitle("Formulario para el ingreso de Marcas de Vehiculo");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N

        jLabel1.setText("Marca de Vehiculo");

        txtMarcadeVehiculo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtMarcadeVehiculoFocusGained(evt);
            }
        });
        txtMarcadeVehiculo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMarcadeVehiculoKeyPressed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id Marca", "Marca de Vehiculo", "Activo"
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
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(300);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(50);
        }

        chkActivo.setText("Activo");
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
        btnGrabar.setEnabled(false);
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
                .addComponent(btnNuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnGrabar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(chkActivo)
                        .addComponent(jLabel1)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
                        .addComponent(txtMarcadeVehiculo)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtMarcadeVehiculo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(chkActivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(7, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
    
        
        try {
        // TODO add your handling code here:
        marcaDeVehiculos= new CMarcasDeVehiculos(ini);
        int row=jTable1.getSelectedRow();
        txtMarcadeVehiculo.setText(String.valueOf(jTable1.getValueAt(row, 1)));
        if((Boolean) jTable1.getModel().getValueAt(row,2)){
            chkActivo.setText("Marca Activo");
            chkActivo.setSelected(true);
            marcaDeVehiculos.setActivoMarcaDeVehiculos(1);
        }else{
            chkActivo.setText("Marca no Activo");
            chkActivo.setSelected(false);
            marcaDeVehiculos.setActivoMarcaDeVehiculos(0);
        }
        actualizar=true;
       
        marcaDeVehiculos.setIdMarcaDeVehiculos(Integer.parseInt((jTable1.getValueAt(row, 0)).toString()));
        marcaDeVehiculos.setNombreMarcaDeVehiculos(txtMarcadeVehiculo.getText().trim());
        
        
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/actualizar_64x64.png"))); // NOI18N
        btnNuevo.setText("Actualizar");
    } catch (Exception ex) {
        Logger.getLogger(FCrearMarcasDexxxxxxxxxxVehiculos.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    }//GEN-LAST:event_jTable1MouseClicked

    private void txtMarcadeVehiculoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMarcadeVehiculoKeyPressed

//        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            ArrayList<String> list = new ArrayList();
//            txtNombreDocumento.setEnabled(false);
//            try {
//                CEmpleados  empl = new CEmpleados(usuario);
//                list= empl.buscarEmpleados(txtNombreDocumento.getText());
//                for(String obj : list){
//                    CEmpleados empleado= new CEmpleados(usuario, obj.toString());
//                    fila = new Object[3];
//                    fila[0] = empleado.getCedula();
//                    fila[1] = empleado.getNombres();
//                    fila[2] = empleado.getApellidos();
//                    modelo = (DefaultTableModel) jTable1.getModel();
//                    modelo.addRow(fila);
//                }
//
//            } catch (SQLException ex) {
//                Logger.getLogger(FCrearCargos.class.getName()).log(Level.SEVERE, null, ex);
//            } catch (Exception ex) {
//                Logger.getLogger(FCrearCargos.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }//GEN-LAST:event_txtMarcadeVehiculoKeyPressed

    private void txtMarcadeVehiculoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtMarcadeVehiculoFocusGained
        txtMarcadeVehiculo.setSelectionStart(0);
        txtMarcadeVehiculo.setSelectionEnd(txtMarcadeVehiculo.getText().length());
    }//GEN-LAST:event_txtMarcadeVehiculoFocusGained

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        salir();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void salir() {
        cancelar();
        this.dispose();
        this.setVisible(false);
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void cancelar() {
        actualizar=false;
        txtMarcadeVehiculo.setText("");
        chkActivo.setEnabled(false);
        chkActivo.setSelected(false);
        btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); // NOI18N
        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png"))); // NOI18N
        btnNuevo.setText("Nuevo");
        btnNuevo.setEnabled(true);
        jTable1.setEnabled(true);
        marcaDeVehiculos= null;
    }

    private void btnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoActionPerformed
        nuevo();
    }//GEN-LAST:event_btnNuevoActionPerformed

    private void nuevo() {
        if (actualizar) {
            btnNuevo.setEnabled(false);
            txtMarcadeVehiculo.setEnabled(true);
            chkActivo.setEnabled(true);
            btnNuevo.setEnabled(false);
            btnGrabar.setEnabled(true);
            
        } else {
            try {
                marcaDeVehiculos = new CMarcasDeVehiculos(ini);
                txtMarcadeVehiculo.setEnabled(true);
                txtMarcadeVehiculo.requestFocus();
                chkActivo.setSelected(true);
                jTable1.setEnabled(false);
                btnNuevo.setEnabled(false);
            } catch (Exception ex) {
                Logger.getLogger(FCrearMarcasDexxxxxxxxxxVehiculos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //txtCedula.requestFocus();
    }

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
      
        grabar();
        
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void grabar() throws HeadlessException {
        if (!txtMarcadeVehiculo.getText().isEmpty()) {
            int x = JOptionPane.showConfirmDialog(this, "Desea guardar el registro ?", "Guardar registro", 0);
            if (x == 0) {
                if (validar()) {

                 //   new Thread(new HiloGuardarMarcaDeVehiculo(this, ini)).start();

                    habilitar(false);

                } else {
                    JOptionPane.showInternalMessageDialog(this, "Se presentó un error al guardar el regisstro", "Error al guardar", 0);
                }

            }
        }
    }     

    private void chkActivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkActivoActionPerformed
        if(chkActivo.isSelected()){
            chkActivo.setText("Marca activo");
        }else{
             chkActivo.setText("Marca No Activo");
        }
    }//GEN-LAST:event_chkActivoActionPerformed

    private void chkActivoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkActivoStateChanged
        if(chkActivo.isSelected()){
            chkActivo.setText("Marca activo");
        }else{
             chkActivo.setText("Marca No Activo");
        }
    }//GEN-LAST:event_chkActivoStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancelar;
    public javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnNuevo;
    private javax.swing.JButton btnSalir;
    public javax.swing.JCheckBox chkActivo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane3;
    public javax.swing.JTable jTable1;
    public javax.swing.JTextField txtMarcadeVehiculo;
    // End of variables declaration//GEN-END:variables
 public boolean validar() {
   boolean verificado = true;
     try {
        
        mensaje = "";
        if (txtMarcadeVehiculo.getText().isEmpty()) {
            mensaje += "No ha colocado el nombre de la Marca " + "  \n";
            verificado = false;
        }
        
        
       
    } catch (Exception ex) {
        Logger.getLogger(FCrearMarcasDexxxxxxxxxxVehiculos.class.getName()).log(Level.SEVERE, null, ex);
    }
     return verificado;
}
public boolean guardarMarca(){
     boolean valida=false;
    try {
       
          CMarcasDeVehiculos marca = new CMarcasDeVehiculos(ini);  
          marca.setNombreMarcaDeVehiculos(txtMarcadeVehiculo.getText().trim());
          if(chkActivo.isSelected()){
           marca.setActivoMarcaDeVehiculos(1);
          }else{
               marca.setActivoMarcaDeVehiculos(0);
          }
          valida=marca.grabarMarcaDeVehiculoss();
          if(valida){
                    fila = new Object[3];
                    fila[0] = marca.getIdMarcaDeVehiculos();
                    fila[1] = marca.getNombreMarcaDeVehiculos();
                    if(marca.getActivoMarcaDeVehiculos()<1){
                       fila[2] = false;
                    }else{
                       fila[2] = true; 
                    }
                    fila[2] = true;
                    modelo = (DefaultTableModel) jTable1.getModel();
                    modelo.addRow(fila);
          }
         
          
    } catch (Exception ex) {
        Logger.getLogger(FCrearMarcasDexxxxxxxxxxVehiculos.class.getName()).log(Level.SEVERE, null, ex);
    }
    return valida;
}

public boolean actualizarMarca(){
     boolean valida=false;
    try {
         int row=jTable1.getSelectedRow();
         CMarcasDeVehiculos marca = new CMarcasDeVehiculos(ini); 
          marca.setIdMarcaDeVehiculos((Integer)jTable1.getValueAt(row, 0));
          marca.setNombreMarcaDeVehiculos(txtMarcadeVehiculo.getText().trim());
          if(chkActivo.isSelected()){
           marca.setActivoMarcaDeVehiculos(1);
          }else{
               marca.setActivoMarcaDeVehiculos(0);
          }
         
          valida=marca.actualizarMarcaDeVehiculoss();
          
           if(valida){
             jTable1.setValueAt(marca.getNombreMarcaDeVehiculos(), row, 1);
             if(marca.getActivoMarcaDeVehiculos()==1){
                jTable1.setValueAt(true, row, 2); 
             }else{
                jTable1.setValueAt(false, row, 2); 
             }
             
           }
    } catch (Exception ex) {
        Logger.getLogger(FCrearMarcasDexxxxxxxxxxVehiculos.class.getName()).log(Level.SEVERE, null, ex);
    }return valida;
}

private boolean habilitar(boolean valor){
     boolean valida=false;
    
    return valida; 
}
}
