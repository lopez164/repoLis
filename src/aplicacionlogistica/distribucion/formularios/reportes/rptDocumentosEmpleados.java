/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.CCargos;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.CEntidadesBancarias;
import aplicacionlogistica.distribucion.objetos.CEstadosCiviles;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.CTiposDeContratosPersonas;
import aplicacionlogistica.distribucion.objetos.CTiposDeSangre;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import aplicacionlogistica.distribucion.administracion.TalentoHUmano.CCentrosDeCosto;
import aplicacionlogistica.configuracion.organizacion.CCiudades;
import aplicacionlogistica.configuracion.organizacion.CDepartamentos;
import aplicacionlogistica.configuracion.organizacion.CRegionales;
import aplicacionlogistica.configuracion.organizacion.CZonas;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JComponent;

/**
 *
 * @author VLI_488
 */
public class rptDocumentosEmpleados extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    ArrayList<CDepartamentos> arrDepartamentos = null;
    ArrayList<CCiudades> arrCiudades = null;
    ArrayList<CCargos> arrCargos = null;
    ArrayList<CTiposDeSangre> arrTiposDeSangre = null;
    ArrayList<CEstadosCiviles> arrEstadosCiviles = null;
    ArrayList<CZonas> arrZonas = null;
    ArrayList<CRegionales> arrRegionales = null;
    ArrayList<CAgencias> arrAgenciass = null;
    ArrayList<CCentrosDeCosto> arrCentrosDeCosto = null;
    ArrayList<CTiposDeContratosPersonas> arrTiposContratos = null;
    ArrayList<CEntidadesBancarias> arrEntidadesBancarias = null;
    String usuario;
    boolean actualizar;
    boolean tieneFoto = false;
    boolean actualizarFoto = false;
    String mensaje = null;
    File foto = null;
    Inicio ini;

    /**
     * Creates new form IngresarPersonas
     */
    public String getUsuario() {
        return usuario;
    }

    public rptDocumentosEmpleados(Inicio ini) {
        initComponents();
       this.ini=ini;
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
        jPanel1 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        CbxZona = new javax.swing.JComboBox();
        cbxRegional = new javax.swing.JComboBox();
        cbxAgencia = new javax.swing.JComboBox();
        dateInicial = new com.toedter.calendar.JDateChooser();
        dateFinal = new com.toedter.calendar.JDateChooser();
        cbxCargos = new javax.swing.JComboBox();
        jComboBox5 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();

        agregarImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/logoDistrilog.png"))); // NOI18N
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1342, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 736, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab1", jPanel3);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1342, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 736, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab2", jPanel4);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "item", "empleado", "Zona", "Regional", "Agencia", "Cargo", "Documento", "Inicio", "Fin", "Estado"
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
        jScrollPane1.setViewportView(jTable1);
        jTable1.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(50);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(70);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(70);
            jTable1.getColumnModel().getColumn(6).setResizable(false);
            jTable1.getColumnModel().getColumn(6).setPreferredWidth(100);
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
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1322, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 725, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("tab3", jPanel5);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1342, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 736, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab4", jPanel6);

        CbxZona.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "zonas" }));

        cbxRegional.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "regionales" }));

        cbxAgencia.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "agencias" }));

        cbxCargos.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Cargos" }));

        jComboBox5.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel1.setText("Desdes");

        jLabel2.setText("Hasta");

        jTextField1.setText("jTextField1");

        jLabel3.setText("Desdes");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbxCargos, 0, 151, Short.MAX_VALUE)
                            .addComponent(CbxZona, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cbxRegional, 0, 144, Short.MAX_VALUE)
                            .addComponent(jComboBox5, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbxAgencia, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(72, 72, 72)
                                .addComponent(jLabel1))
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(dateInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(dateFinal, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jTextField1))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(CbxZona)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxAgencia)
                            .addComponent(jLabel1))
                        .addComponent(cbxRegional)
                        .addComponent(dateFinal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(dateInicial, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxCargos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox CbxZona;
    private javax.swing.JMenu agregarImagen;
    private javax.swing.JComboBox cbxAgencia;
    private javax.swing.JComboBox cbxCargos;
    private javax.swing.JComboBox cbxRegional;
    private com.toedter.calendar.JDateChooser dateFinal;
    private com.toedter.calendar.JDateChooser dateInicial;
    private javax.swing.JComboBox jComboBox5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JPopupMenu pumImagen;
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
            CEntidadesBancarias banco = empleado.getEntidadBancaria();
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
            empleado.setEstadoCivil(new CEstadosCiviles(usuario, cbxEstadoCivil.getSelectedItem().toString()));
            empleado.setTipoSangre(new CTiposDeSangre(usuario, cbxTipoDeSangre.getSelectedItem().toString()));
            empleado.setCargo(new CCargos(usuario, cbxCargos.getSelectedItem().toString()));
            cbxZonas.getSelectedItem().toString();
            cbxRegionales.getSelectedItem().toString();
            empleado.setAgencia(new CAgencias(usuario, cbxAgencias.getSelectedItem().toString()));
            empleado.setCentroDeCosto(new CCentrosDeCosto(usuario, cbxCentrosDeCosto.getSelectedItem().toString()));
            empleado.setTipoDeContrato(new CTiposDeContratosPersonas(usuario, cbxTiposDeContrato.getSelectedItem().toString()));
            empleado.setEntidadBancaria(new CEntidadesBancarias(usuario, cbxEntidadesBancarias.getSelectedItem().toString()));

            Date dt = new Date();
            dt = empleado.getFechaSql(dateCumpleanios);
            empleado.setCumpleanios(dt);
            dt = new Date();
            dt = empleado.getFechaSql(dateFechaIngreso);
            empleado.setFechaIngresoEmpresa(dt);
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
