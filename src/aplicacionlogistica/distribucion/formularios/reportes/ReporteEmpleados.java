/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.administrativo.PrincipalAdministrador;
import aplicacionlogistica.administrativo.PrincipalTalentoHumanoAdministrador;
import aplicacionlogistica.administrativo.PrincipalTalentoHumanoAuxiliar;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.CCargos;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import aplicacionlogistica.configuracion.organizacion.CRegionales;
import aplicacionlogistica.configuracion.organizacion.CZonas;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VLI_488
 */
public class ReporteEmpleados extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    
    ArrayList<CEmpleados> arrEmpleados = null;
    String usuario;
    boolean actualizar;
    boolean tieneFoto = false;
    boolean actualizarFoto = false;
    String mensaje = null;
    File foto = null;
    Inicio ini;
    CUsuarios user;
    /**
     * Creates new form IngresarPersonas
     */
    public String getUsuario() {
        return usuario;
    }

    public CUsuarios getUser() {
        return user;
    }

    
    
    public ArrayList<CEmpleados> getArrEmpleados() {
        return arrEmpleados;
    }

    public void setArrEmpleados(ArrayList<CEmpleados> arrEmpleados) {
        this.arrEmpleados = arrEmpleados;
    }

    
    
    public ReporteEmpleados(Inicio ini) {
        initComponents();
        this.ini=ini;
        this.user = ini.getUser();
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
        int zona;
           int regional;
           int agencia;
        switch(user.getNivelAcceso()){
           
            case 0:
                break;
            case 1:
                CbxZona.setEnabled(false);
                cbxRegional.setEnabled(false);
                cbxAgencia.setEnabled(false);
                dateInicial.setEnabled(false);
                dateFinal.setEnabled(false);
                cbxCargos.setEnabled(false);
                jComboBox5.setEnabled(false);
                txtApellidos.setEnabled(false);
                break;
            case 2:
                CbxZona.setEnabled(!false);
                cbxRegional.setEnabled(!false);
                cbxAgencia.setEnabled(!false);
                dateInicial.setEnabled(!false);
                dateFinal.setEnabled(!false);
                cbxCargos.setEnabled(!false);
                jComboBox5.setEnabled(!false);
                txtApellidos.setEnabled(!false);
                break;
            case 3:
                CbxZona.setEnabled(false);
                CbxZona.removeAllItems();
                zona=user.getZona();
                for( CZonas obj :ini.getListaDeZonas() ){
                    if(obj.getIdZona()==1){
                        CbxZona.addItem(obj.getNombreZona());
                    }
                }
                
                cbxRegional.setEnabled(!false);
                cbxAgencia.setEnabled(!false);
                dateInicial.setEnabled(!false);
                dateFinal.setEnabled(!false);
                cbxCargos.setEnabled(!false);
                jComboBox5.setEnabled(!false);
                txtApellidos.setEnabled(!false);
                break;
            case 4:
                CbxZona.setEnabled(false);
                CbxZona.removeAllItems();
                zona=user.getZona();
                for( CZonas obj :ini.getListaDeZonas() ){
                    if(obj.getIdZona()==zona){
                        CbxZona.addItem(obj.getNombreZona());
                    }
                }
                cbxRegional.removeAllItems();
                regional=user.getRegional();
                for( CRegionales obj :ini.getListaDeRegionales() ){
                    if(obj.getIdRegional()==regional){
                        cbxRegional.addItem(obj.getNombreRegional());
                    }
                }
                cbxRegional.setEnabled(false);
                cbxAgencia.setEnabled(!false);
                dateInicial.setEnabled(!false);
                dateFinal.setEnabled(!false);
                cbxCargos.setEnabled(!false);
                jComboBox5.setEnabled(!false);
                txtApellidos.setEnabled(!false);
                break;
            case 5:
                 CbxZona.setEnabled(false);
                CbxZona.removeAllItems();
                zona=user.getZona();
                for( CZonas obj :ini.getListaDeZonas() ){
                    if(obj.getIdZona()==zona){
                        CbxZona.addItem(obj.getNombreZona());
                    }
                }
                cbxRegional.removeAllItems();
                regional=user.getRegional();
                for( CRegionales obj :ini.getListaDeRegionales() ){
                    if(obj.getIdRegional()==regional){
                        cbxRegional.addItem(obj.getNombreRegional());
                    }
                }
                cbxRegional.setEnabled(false);
                cbxAgencia.removeAllItems();
                agencia=user.getAgencia();
                 for( CAgencias obj :ini.getListaDeAgencias() ){
                    if(obj.getIdAgencia()==agencia){
                        cbxAgencia.addItem(obj.getNombreAgencia());
                    }
                }
                cbxAgencia.setEnabled(false);
                dateInicial.setEnabled(!false);
                dateFinal.setEnabled(!false);
                cbxCargos.setEnabled(!false);
                jComboBox5.setEnabled(!false);
                txtApellidos.setEnabled(!false);
                break;
            default :
                break;
                
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

        pumImagen = new javax.swing.JPopupMenu();
        agregarImagen = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        CbxZona = new javax.swing.JComboBox();
        cbxRegional = new javax.swing.JComboBox();
        cbxAgencia = new javax.swing.JComboBox();
        dateInicial = new com.toedter.calendar.JDateChooser();
        dateFinal = new com.toedter.calendar.JDateChooser();
        cbxCargos = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtApellidos = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        agregarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Display 16x16.png"))); // NOI18N
        agregarImagen.setText("Agregar Imagen Empleado");
        agregarImagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                agregarImagenMouseClicked(evt);
            }
        });
        pumImagen.add(agregarImagen);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Reporte de Documentos de los empleados de la Compañía");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "item", "Documento", "empleado", "Zona", "Regional", "Agencia", "Cargo", "Inicio", "Fin", "Estado"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable1.setColumnSelectionAllowed(true);
        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable1MouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(70);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(70);
            jTable1.getColumnModel().getColumn(7).setResizable(false);
            jTable1.getColumnModel().getColumn(7).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(8).setResizable(false);
            jTable1.getColumnModel().getColumn(8).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(9).setResizable(false);
            jTable1.getColumnModel().getColumn(9).setPreferredWidth(20);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1223, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 693, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab3", jPanel5);

        CbxZona.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "zonas" }));

        cbxRegional.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "regionales" }));

        cbxAgencia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "agencias" }));

        dateInicial.setDateFormatString("yyyy/MM/dd");

        dateFinal.setDateFormatString("yyyy/MM/dd");

        cbxCargos.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cargos" }));
        cbxCargos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cbxCargosFocusGained(evt);
            }
        });

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Desdes");

        jLabel2.setText("Hasta");

        jLabel3.setText("Apellidos");

        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/buscarPersonas32x32.png"))); // NOI18N
        jToolBar1.add(jButton1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/excel32x32.png"))); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/adobepdf32x32.png"))); // NOI18N
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton3);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/printer32x32.png"))); // NOI18N
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jButton5);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/buscarPersonas32x32.png"))); // NOI18N
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel4.setText("Buscar");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(CbxZona, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxCargos, 0, 230, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxRegional, 0, 230, Short.MAX_VALUE))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbxAgencia, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, 370, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dateFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)))
                .addGap(151, 151, 151))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1268, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CbxZona, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxRegional, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbxAgencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jLabel1)
                        .addComponent(dateFinal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(dateInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(cbxCargos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addComponent(jTabbedPane1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1275, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void agregarImagenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_agregarImagenMouseClicked
    /*    File fileFoto = null;
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
                        panelFotografia.setIcon(filfoto);
                        tieneFoto = true;
                        javax.swing.GroupLayout panelImage1Layout = new javax.swing.GroupLayout(panelFotografia);
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
        }
**/
    }//GEN-LAST:event_agregarImagenMouseClicked

    private void cbxCargosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbxCargosFocusGained
      cbxCargos.removeAllItems();
      cbxCargos.addItem("Elija el cargo");
        for(CCargos obj : ini.getListaDeCargos()){
        cbxCargos.addItem(obj.getNombreCargo());
        
    }
    }//GEN-LAST:event_cbxCargosFocusGained

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
         if(arrEmpleados.size()>0 && arrEmpleados != null){
              int fil = 0;
              DefaultTableModel    modelo = (DefaultTableModel) jTable1.getModel();
         for (CEmpleados obj : arrEmpleados) {
             modelo.addRow(new Object[fil]);
             jTable1.setValueAt(fil + 1, fil, 0);
             jTable1.setValueAt(obj.getCedula(), fil, 1);
             jTable1.setValueAt(obj.getNombres() + " " + obj.getApellidos()  , fil, 2);
//             jTable1.setValueAt(obj.getAgencia().getNombreZona(), fil, 3);
//             jTable1.setValueAt(obj.getAgencia().getNombreRegional(), fil, 4);
//             jTable1.setValueAt(obj.getAgencia().getNombreAgencia(), fil, 5);
//             jTable1.setValueAt(obj.getCargo().getNombreCargo(), fil, 6);
             jTable1.setValueAt(obj.getFechaIngresoEmpresa().toString(), fil, 7);
             if (obj.getEmpleadoActivo() == 1) {
                 jTable1.setValueAt("Activo", fil, 8);
             } else {
                  jTable1.setValueAt("No Activo", fil, 8);
             }
            
             fil++;
         }
     }
       
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
      rptFichaEmpleado form2;
        if(jTable1.getRowCount()>0){
          
         try {
            int row = jTable1.getSelectedRow();
            CEmpleados empleado=null;
            for(CEmpleados obj : arrEmpleados){
                if(obj.getCedula().equals(jTable1.getValueAt(row, 1).toString())){
                   empleado=obj; 
                }
            }
            switch(user.getTipoAcceso()){
                case 1:
                    break;
                case 2:
                     form2 = new rptFichaEmpleado(empleado);
                     PrincipalAdministrador.escritorio.add(form2);
                     form2.setSize(PrincipalAdministrador.escritorio.getSize());
                     form2.show();   
                    break;
                case 3:
                     form2 = new rptFichaEmpleado(empleado);
                     PrincipalTalentoHumanoAdministrador.escritorio.add(form2);
                     form2.setSize(PrincipalTalentoHumanoAdministrador.escritorio.getSize());
                     form2.show();  
                    break;
                case 4:
                     form2 = new rptFichaEmpleado(empleado);
                     PrincipalTalentoHumanoAuxiliar.escritorio.add(form2);
                     form2.setSize(PrincipalTalentoHumanoAuxiliar.escritorio.getSize());
                     form2.show();  
                    break;
                case 5:
                    break;
                case 6:
                    break;
                default:
            }
          
            

                  // TODO add your handling code here:
         }catch (Exception ex) {
            //Logger.getLogger(FAgregarDocumentos1.class.getName()).log(Level.SEVERE, null, ex);
        } 
      }
         
    }//GEN-LAST:event_jTable1MouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox CbxZona;
    private javax.swing.JMenu agregarImagen;
    private javax.swing.JComboBox cbxAgencia;
    private javax.swing.JComboBox cbxCargos;
    private javax.swing.JComboBox cbxRegional;
    private com.toedter.calendar.JDateChooser dateFinal;
    private com.toedter.calendar.JDateChooser dateInicial;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPopupMenu pumImagen;
    private javax.swing.JTextField txtApellidos;
    // End of variables declaration//GEN-END:variables

    
    public void llenarCamposDeTexto(CEmpleados empleado) {
     /*   try {
            txtNombres.setText(empleado.getNombres());
            txtApellidos.setText(empleado.getApellidos());
            txtDireccion.setText(empleado.getDireccion());
            txtBarrio.setText(empleado.getBarrio());
            CCiudades ciudad = empleado.getCiudad();
            CDepartamentos dpto = new CDepartamentos(usuario, ciudad.getIdDepartamento());
            cbxDepartamentos.removeAllItems();
            cbxDepartamentos.addItem(dpto.getNombreDepartamento());
            cbxCiudades.removeAllItems();
            cbxCiudades.addItem(ciudad.getNombreCiudad());
            txtTelefono.setText(empleado.getTelefonoFijo());
            txtCelular.setText(empleado.getTelefonoCelular());
            txtCelularCorporativo.setText(empleado.getCelularCorporativo());
            txtEscolaridad.setText(empleado.getEscolaridad());
            cbxGenero.removeAllItems();
            cbxGenero.addItem(empleado.getGenero());
            dateCumpleanios.setDate(empleado.getCumpleanios());
            txtLugarDeNacimiento.setText(empleado.getLugarNacimiento());
            cbxEstadoCivil.removeAllItems();
            CEstadosCiviles estCivil = empleado.getEstadoCivil();
            cbxEstadoCivil.addItem(estCivil.getNombreEstadoCivil());
            txtEmail.setText(empleado.getEmail());
            CTiposDeSangre tipoDeSangre = empleado.getTipoSangre();
            cbxTipoDeSangre.removeAllItems();
            cbxTipoDeSangre.addItem(tipoDeSangre.getNombreTipoDeSAngre());
            CCargos cargo = empleado.getCargo();
            cbxCargos.removeAllItems();
            cbxCargos.addItem(cargo.getNombreCargo());
            dateFechaIngreso.setDate(empleado.getFechaIngresoEmpresa());
            CAgencias agencia = empleado.getAgencia();
            CRegionales regional = new CRegionales(usuario, agencia.getIdRegional());
            CZonas zona = new CZonas(usuario, regional.getIdZona());
            cbxZonas.removeAllItems();
            cbxZonas.addItem(zona.getNombreZona());
            cbxRegionales.removeAllItems();
            cbxRegionales.addItem(regional.getNombreRegional());
            cbxAgencias.removeAllItems();
            cbxAgencias.addItem(agencia.getNombreAgencia());

            CCentrosDeCosto centroCosto = empleado.getCentroDeCosto();
            cbxCentrosDeCosto.removeAllItems();
            cbxCentrosDeCosto.addItem(centroCosto.getNombreCentroDeCosto());

            //panelFotografia.
            CTiposDeContratosPersonas contrato = empleado.getTipoDeContrato();
            cbxTiposDeContrato.removeAllItems();
            cbxTiposDeContrato.addItem(contrato.getNombreTipoDeContrato());
            txtNumeroDeCuenta.setText(empleado.getNumeroCuenta());
            CEntidadesBancarias banco = empleado.getIdBanco();
            cbxEntidadesBancarias.removeAllItems();
            cbxEntidadesBancarias.addItem(banco.getNombreEntidadBancaria());
            this.foto = empleado.getFotografia();

            ImageIcon filfoto = empleado.getImage();
            if (filfoto != null) {
                tieneFoto = true;
            }
            panelFotografia.setIcon(filfoto);
            panelFotografia.updateUI();
            javax.swing.GroupLayout panelImage1Layout = new javax.swing.GroupLayout(panelFotografia);
            panelFotografia.setLayout(panelImage1Layout);
            panelImage1Layout.setHorizontalGroup(
                    panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 329, Short.MAX_VALUE));
            panelImage1Layout.setVerticalGroup(
                    panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGap(0, 405, Short.MAX_VALUE));

        } catch (Exception ex) {
        }



**/
    }

    private void habilitar(boolean valor) {
/*
        try {
            txtCedula.setEditable(valor);
            txtNombres.setEditable(valor);
            txtApellidos.setEditable(valor);
            txtDireccion.setEditable(valor);
            txtBarrio.setEditable(valor);
            txtTelefono.setEditable(valor);
            txtCelular.setEditable(valor);
            txtCelularCorporativo.setEditable(valor);
            txtEscolaridad.setEditable(valor);
            txtLugarDeNacimiento.setEditable(valor);
            txtNumeroDeCuenta.setEditable(valor);
            txtEmail.setEditable(valor);

            cbxGenero.setEnabled(valor);
            cbxDepartamentos.setEnabled(valor);
            cbxCiudades.setEnabled(valor);
            cbxEstadoCivil.setEnabled(valor);
            cbxTipoDeSangre.setEnabled(valor);
            cbxCargos.setEnabled(valor);
            cbxZonas.setEnabled(valor);
            cbxRegionales.setEnabled(valor);
            cbxAgencias.setEnabled(valor);
            cbxCentrosDeCosto.setEnabled(valor);
            cbxTiposDeContrato.setEnabled(valor);
            cbxEntidadesBancarias.setEnabled(valor);

            dateCumpleanios.setEnabled(valor);
            dateFechaIngreso.setEnabled(valor);

        } catch (Exception ex) {
        }



**/
    }

    public boolean validar() {
        boolean verificado = true;
     /*   mensaje = "";
        if (txtNombres.getText().isEmpty()) {
            mensaje += "No ha colocado el nombre del empleado" + "  \n";
            verificado = false;
        }
        if (txtApellidos.getText().isEmpty()) {
            mensaje += "No ha colocado los apellidos del empleado" + "  \n";
            verificado = false;
        }
        if (txtDireccion.getText().isEmpty()) {
            mensaje += "No ha colocado la dirección del empleado" + "  \n";
            verificado = false;
        }
        if (txtBarrio.getText().isEmpty()) {
            mensaje += "No ha colocado el barrio donde vive el empleado" + "  \n";
            verificado = false;
        }

        if (cbxDepartamentos.getSelectedIndex() == -1) {
            mensaje += "No ha selecccionado el departamento donde vive el empleado" + "  \n";
            verificado = false;
        }

        if (cbxCiudades.getSelectedIndex() == -1) {
            mensaje += "No ha seleccionado la ciudad donde vive el empleado" + "  \n";
            verificado = false;
        }
        if (txtTelefono.getText().isEmpty()) {
            mensaje += "No ha ingresado el telefono del empleado" + "  \n";
            verificado = false;
        }
        if (txtCelular.getText().isEmpty()) {
            mensaje += "No ha ingresado el celular del empleado" + "  \n";
            verificado = false;
        }
        if (txtCelularCorporativo.getText().isEmpty()) {
            mensaje += "No ha ingresado el celular corporativo del empleado" + "  \n";
            verificado = false;
        }
        if (txtEscolaridad.getText().isEmpty()) {
            mensaje += "No ha ingresado el nivel educativo del empleado" + "  \n";
            verificado = false;
        }

        if (txtLugarDeNacimiento.getText().isEmpty()) {
            mensaje += "No ha ingresado el Lugar de Nacimiento del empleado" + "  \n";
            verificado = false;
        }

        if (cbxEstadoCivil.getSelectedIndex() == -1) {
            mensaje += "No ha seleccionado el estado civil del empleado" + "  \n";
            verificado = false;
        }
        if (txtEmail.getText().isEmpty()) {
            mensaje += "No ha ingresado el Email del empleado" + "  \n";
            verificado = false;
        }
        if (!Inicio.validateEmail(txtEmail.getText())) {
            mensaje += "El formato del email no es válido" + "  \n";
            verificado = false;

        }

        if (cbxTipoDeSangre.getSelectedIndex() == -1) {
            mensaje += "No ha selecccionado el tipo de sangre del empleado" + "  \n";
            verificado = false;
        }

        if (cbxCargos.getSelectedIndex() == -1) {
            mensaje += "No ha seleccionado el cargo del empleado" + "  \n";
            verificado = false;
        }
        if (cbxZonas.getSelectedIndex() == -1) {
            mensaje += "No ha seleccionado la zona de ubicación del empleado" + "  \n";
            verificado = false;
        }

        if (cbxRegionales.getSelectedIndex() == -1) {
            mensaje += "No ha seleccionado la regional donde está ubicado el empleado" + "  \n";
            verificado = false;
        }
        if (cbxAgencias.getSelectedIndex() == -1) {
            mensaje += "No ha selecccionado la agencia donde está ubicado el empleado" + "  \n";
            verificado = false;
        }

        if (cbxCentrosDeCosto.getSelectedIndex() == -1) {
            mensaje += "No ha seleccionado el centro de costo al cual pertenece el empleado" + "  \n";
            verificado = false;
        }

        if (cbxTiposDeContrato.getSelectedIndex() == -1) {
            mensaje += "No ha seleecionado el tipo de contrato del empleado" + "  \n";
            verificado = false;
        }
        if (txtNumeroDeCuenta.getText().isEmpty()) {
            mensaje += "No ha ingresado el número de cuenta del empleado" + "  \n";
            verificado = false;
        }

        if (cbxEntidadesBancarias.getSelectedIndex() == -1) {
            mensaje += "No ha seleccionado el Banco donde tiene la cuenta el empleado" + "  \n";
            verificado = false;
        }


        if (!tieneFoto) {
            mensaje += "No ha seleccionado la fotografía del empleado" + "  \n";
            verificado = false;
        }**/
        return verificado; 
    }

    public boolean guardarRegistroEmpleado() {
        boolean guardado = false;
      /* 
        try {

            CEmpleados empleado = new CEmpleados(usuario);
            empleado.setCedula(txtCedula.getText().trim());
            empleado.setNombres(txtNombres.getText().trim());
            empleado.setApellidos(txtApellidos.getText().trim());
            empleado.setDireccion(txtDireccion.getText().trim());
            empleado.setBarrio(txtBarrio.getText().trim());
            empleado.setTelefonoFijo(txtTelefono.getText().trim());
            empleado.setTelefonoCelular(txtCelular.getText().trim());
            empleado.setCelularCorporativo(txtCelularCorporativo.getText().trim());
            empleado.setEscolaridad(txtEscolaridad.getText().trim());
            empleado.setLugarNacimiento(txtLugarDeNacimiento.getText().trim());
            empleado.setNumeroCuenta(txtNumeroDeCuenta.getText().trim());
            empleado.setEmail(txtEmail.getText().trim());

            empleado.setGenero(cbxGenero.getSelectedItem().toString());
            cbxDepartamentos.getSelectedItem().toString();
            empleado.setCiudad(new CCiudades(usuario, cbxCiudades.getSelectedItem().toString()));
            empleado.setIdEstadoCivil(new CEstadosCiviles(usuario, cbxEstadoCivil.getSelectedItem().toString()));
            empleado.setIdTipoSangre(new CTiposDeSangre(usuario, cbxTipoDeSangre.getSelectedItem().toString()));
            empleado.setIdCargo(new CCargos(usuario, cbxCargos.getSelectedItem().toString()));
            cbxZonas.getSelectedItem().toString();
            cbxRegionales.getSelectedItem().toString();
            empleado.setIdAgencia(new CAgencias(usuario, cbxAgencias.getSelectedItem().toString()));
            empleado.setIdCentroDeCosto(new CCentrosDeCosto(usuario, cbxCentrosDeCosto.getSelectedItem().toString()));
            empleado.setIdTipoDeContrato(new CTiposDeContratosPersonas(usuario, cbxTiposDeContrato.getSelectedItem().toString()));
            empleado.setIdBanco(new CEntidadesBancarias(usuario, cbxEntidadesBancarias.getSelectedItem().toString()));

            Date dt = new Date();
            dt = empleado.getFechaSql(dateCumpleanios);
            empleado.setCumpleanios(dt);
            dt = new Date();
            dt = empleado.getFechaSql(dateFechaIngreso);
            empleado.setFechaDeIngreso(dt);
            empleado.setFotografia(foto);
            

            guardado = empleado.grabarEmpleados();


        } catch (Exception ex) {
            Logger.getLogger(IngresarEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }**/
        return guardado; 
    }

    private void limpiar() {
/*
        try {
            txtCedula.setText("");
            txtNombres.setText("");
            txtApellidos.setText("");
            txtDireccion.setText("");
            txtBarrio.setText("");
            txtTelefono.setText("");
            txtCelular.setText("");
            txtCelularCorporativo.setText("");
            txtEscolaridad.setText("");
            txtLugarDeNacimiento.setText("");
            txtNumeroDeCuenta.setText("");
            txtEmail.setText("");


            cbxGenero.removeAllItems();
            cbxDepartamentos.removeAllItems();
            cbxCiudades.removeAllItems();
            cbxEstadoCivil.removeAllItems();
            cbxTipoDeSangre.removeAllItems();
            cbxCargos.removeAllItems();
            cbxZonas.removeAllItems();
            cbxRegionales.removeAllItems();
            cbxAgencias.removeAllItems();
            cbxCentrosDeCosto.removeAllItems();
            cbxTiposDeContrato.removeAllItems();
            cbxEntidadesBancarias.removeAllItems();

            dateCumpleanios.setDate(new Date());
            dateFechaIngreso.setDate(new Date());

            panelFotografia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/perfil.jpg")));
            panelFotografia.updateUI();
            btnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/agregar.png"))); //
            btnNuevo.setText("Nuevo");
            btnNuevo.setEnabled(true);
            arrDepartamentos = null;
            arrCiudades = null;
            arrCargos = null;
            arrTiposDeSangre = null;
            arrEstadosCiviles = null;
            arrZonas = null;
            arrRegionales = null;
            arrAgenciass = null;
            arrCentrosDeCosto = null;
            arrTiposContratos = null;
            arrEntidadesBancarias = null;
            actualizarFoto = false;
            actualizar = false;
            tieneFoto=false;
        } catch (Exception ex) {
        }
**/
    }
}
