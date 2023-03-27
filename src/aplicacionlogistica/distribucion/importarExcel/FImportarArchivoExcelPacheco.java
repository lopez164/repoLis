/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.importarExcel;

import aplicacionlogistica.distribucion.Threads.HiloImportarFacturasDesdeArchivo;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Usuario
 */
public class FImportarArchivoExcelPacheco extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();

    public boolean cancelar = false;
    Object[] fila = new Object[3];
    CUsuarios user;
    String usuario;
    public File fileClientes;
    public File fileFacturas;
    public File file;

    public String mensaje = "";
    public Inicio ini;
    int numHojas;
    int numrows;
    int numcolumns;

    XSSFWorkbook workbookClientes;
    XSSFWorkbook workbookFacturas;

    Iterator rows;
    public List<String> sqlInsercionRemota = null;
    public List<String> sqlInsercionRemotaManifiestos = null;

//private FResidentes residente = (FResidentes)this.getParent();//
    /**
     * Creates new form FBuscar2
     */
    public FImportarArchivoExcelPacheco() {
        initComponents();

    }

    public FImportarArchivoExcelPacheco(Inicio ini) {
        initComponents();
        this.ini = ini;
        this.user = ini.getUser();
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

//ImageIcon imageIcon = new ImageIcon("/aplicacionlogistica/configuracion/imagenes/barraDeProgreso.gif");
//lblBarraDeProgreso.setIcon(imageIcon);
        lblBarraDeProgreso.setVisible(false);

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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtErrores = new javax.swing.JTextArea();
        lblBarraDeProgreso = new javax.swing.JLabel();
        panelImage1 = new org.edisoncor.gui.panel.PanelImage();
        jPanel2 = new javax.swing.JPanel();
        btnSalir = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnFuente = new javax.swing.JButton();
        btnImportar = new javax.swing.JButton();
        barraInferior = new javax.swing.JProgressBar();
        barraSuperior = new javax.swing.JProgressBar();
        txtFileClientes = new javax.swing.JTextField();
        lblLocal = new javax.swing.JLabel();
        lblRemoto = new javax.swing.JLabel();
        rbtTrasmLocal = new javax.swing.JCheckBox();
        rbtTrasmRemota = new javax.swing.JCheckBox();
        txtFileFacturas = new javax.swing.JTextField();
        jBtnClientes = new javax.swing.JButton();
        jBtnFacturas = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario para importar datos desde Excel");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N
        setMaximumSize(new java.awt.Dimension(929, 564));

        txtErrores.setColumns(20);
        txtErrores.setRows(5);
        jScrollPane1.setViewportView(txtErrores);

        lblBarraDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/barraDeProgreso.gif"))); // NOI18N

        panelImage1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/database-25000.png"))); // NOI18N

        javax.swing.GroupLayout panelImage1Layout = new javax.swing.GroupLayout(panelImage1);
        panelImage1.setLayout(panelImage1Layout);
        panelImage1Layout.setHorizontalGroup(
            panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 137, Short.MAX_VALUE)
        );
        panelImage1Layout.setVerticalGroup(
            panelImage1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 154, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblBarraDeProgreso, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(panelImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(73, 73, 73))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(panelImage1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblBarraDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
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

        btnFuente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/database-64.png"))); // NOI18N
        btnFuente.setText("Fuente");
        btnFuente.setToolTipText("Agregar ó Modificar un registro");
        btnFuente.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnFuente.setPreferredSize(new java.awt.Dimension(97, 97));
        btnFuente.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        btnFuente.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnFuente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFuenteActionPerformed(evt);
            }
        });

        btnImportar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/exportarExcel.jpg"))); // NOI18N
        btnImportar.setText("Importar");
        btnImportar.setToolTipText("Guardar registro nuevo ó modificado");
        btnImportar.setEnabled(false);
        btnImportar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImportar.setPreferredSize(new java.awt.Dimension(97, 97));
        btnImportar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImportar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(btnFuente, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnImportar, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelar, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(3, 3, 3)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 95, Short.MAX_VALUE)
                    .addComponent(btnCancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnImportar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(btnFuente, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );

        barraInferior.setStringPainted(true);
        barraInferior.setVerifyInputWhenFocusTarget(false);

        barraSuperior.setStringPainted(true);
        barraSuperior.setVerifyInputWhenFocusTarget(false);

        txtFileClientes.setEditable(false);
        txtFileClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFileClientesActionPerformed(evt);
            }
        });

        lblLocal.setText("Servidor Local");

        lblRemoto.setText("Servidor Remoto");

        rbtTrasmLocal.setText("Trasmision Local");
        rbtTrasmLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtTrasmLocalActionPerformed(evt);
            }
        });

        rbtTrasmRemota.setSelected(true);
        rbtTrasmRemota.setText("Trasmision Remota");
        rbtTrasmRemota.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtTrasmRemotaActionPerformed(evt);
            }
        });

        txtFileFacturas.setEditable(false);
        txtFileFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFileFacturasActionPerformed(evt);
            }
        });

        jBtnClientes.setText("Clientes");
        jBtnClientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnClientesActionPerformed(evt);
            }
        });

        jBtnFacturas.setText("Facturas");
        jBtnFacturas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnFacturasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtFileFacturas, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jBtnFacturas))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblRemoto)
                        .addComponent(lblLocal)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(rbtTrasmLocal)
                                .addComponent(rbtTrasmRemota))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(barraInferior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(barraSuperior, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(txtFileClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 582, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jBtnClientes))))
                .addGap(5, 5, 5))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFileClientes, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnClientes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFileFacturas, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jBtnFacturas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblLocal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraSuperior, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblRemoto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraInferior, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(rbtTrasmLocal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbtTrasmRemota)
                        .addGap(48, 48, 48))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        cancelar = false;
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed

        cancelar = true;
        btnFuente.setEnabled(true);
        btnImportar.setEnabled(false);
        this.fileClientes = null;
        this.fileFacturas = null;
        mensaje = "";
        this.barraInferior.setValue(0);
        this.barraSuperior.setValue(0);
        txtFileClientes.setText("");
        txtErrores.setText("");

    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnFuenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFuenteActionPerformed

        cancelar = false;
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Elija Archivo a importar");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Escoja el archivo en formato office 2007:  .xls", "xlsx");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(jPanel1);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                this.file = fc.getSelectedFile();
                txtFileClientes.setText(file.getAbsolutePath());
                btnImportar.setEnabled(true);
                btnFuente.setEnabled(false);
            } catch (Exception ex) {
                Logger.getLogger(FImportarArchivoExcelPacheco.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en fuente de archivos " + ex);
            }
        } else {
            // log.append("acción cancelada por el usuario." + newline);
        }
                btnImportar.setEnabled(true);

    }//GEN-LAST:event_btnFuenteActionPerformed

    private void btnImportarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportarActionPerformed

        // if (validar()) {
        if (true) {

            try {
                this.btnFuente.setEnabled(false);
                this.btnImportar.setEnabled(false);
                System.out.println("empieza el proceso de importacion del archivo de excel  " + new Date());
                txtErrores.setText("Inicio de trasmision de la informacion " + new Date());

                // new Thread(new JcArchivoExcelProductosPorFacturaCamdun_4(5, this, this.file, this.ini)).start();
                new Thread(new HiloImportarFacturasDesdeArchivo(this)).start();
                //new Thread(new HiloImportarDesdeExcel(this)).start();
            } catch (Exception ex) {
                Logger.getLogger(FImportarArchivoExcelPacheco.class.getName()).log(Level.SEVERE, null, ex);

            }
        } else {
            JOptionPane.showMessageDialog(null, "No se puede hacer la actualización  porque :" + mensaje + " \n", "Error !!!", 1, null);

        }

    }//GEN-LAST:event_btnImportarActionPerformed

    private void txtFileClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFileClientesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFileClientesActionPerformed

    private void rbtTrasmRemotaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtTrasmRemotaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtTrasmRemotaActionPerformed

    private void rbtTrasmLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtTrasmLocalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_rbtTrasmLocalActionPerformed

    private void txtFileFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFileFacturasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFileFacturasActionPerformed

    private void jBtnFacturasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnFacturasActionPerformed

        cancelar = false;
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Elija Archivo a importar");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Escoja el archivo en formato office 2007:  .xslx", "xlsx");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(jPanel1);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                this.fileFacturas = fc.getSelectedFile();
                txtFileFacturas.setText(fileFacturas.getAbsolutePath());
                btnImportar.setEnabled(true);
                btnFuente.setEnabled(false);
            } catch (Exception ex) {
                Logger.getLogger(FImportarArchivoExcelPacheco.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en fuente de archivos " + ex);
            }
        } else {
            // log.append("acción cancelada por el usuario." + newline);
        }
    }//GEN-LAST:event_jBtnFacturasActionPerformed

    private void jBtnClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnClientesActionPerformed

        cancelar = false;
        final JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Elija Archivo a importar");
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Escoja el archivo en formato office 2007:  .xslx", "xlsx");
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(jPanel1);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                this.fileClientes = fc.getSelectedFile();
                txtFileClientes.setText(fileClientes.getAbsolutePath());
                btnImportar.setEnabled(true);
                btnFuente.setEnabled(false);
            } catch (Exception ex) {
                Logger.getLogger(FImportarArchivoExcelPacheco.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en fuente de archivos " + ex);
            }
        } else {
            // log.append("acción cancelada por el usuario." + newline);
        }
    }//GEN-LAST:event_jBtnClientesActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barraInferior;
    public javax.swing.JProgressBar barraSuperior;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnFuente;
    private javax.swing.JButton btnImportar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton jBtnClientes;
    private javax.swing.JButton jBtnFacturas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lblBarraDeProgreso;
    public javax.swing.JLabel lblLocal;
    public javax.swing.JLabel lblRemoto;
    private org.edisoncor.gui.panel.PanelImage panelImage1;
    public javax.swing.JCheckBox rbtTrasmLocal;
    public javax.swing.JCheckBox rbtTrasmRemota;
    public javax.swing.JTextArea txtErrores;
    private javax.swing.JTextField txtFileClientes;
    private javax.swing.JTextField txtFileFacturas;
    // End of variables declaration//GEN-END:variables
 public boolean validar() {
        boolean verificado = true;
        int val;
        try {

            FileInputStream fisCliente;
            FileInputStream fisfacturas;

            fisCliente = new FileInputStream(this.fileClientes);
            fisfacturas = new FileInputStream(this.fileFacturas);

            workbookClientes = new XSSFWorkbook(fisCliente);
            workbookFacturas = new XSSFWorkbook(fisfacturas);

            numHojas = workbookFacturas.getNumberOfSheets();
            XSSFSheet sheet = workbookFacturas.getSheetAt(0);
            numrows = sheet.getLastRowNum();
            rows = sheet.rowIterator();
            if (fileClientes == null || fileFacturas == null) {
                verificado = false;
                mensaje += "Error, el archivo no existe " + "  \n";
            }
            if (numrows <= 0) {
                verificado = false;
                mensaje += "Error, el archivo está vacío  " + "  \n";
            }
            //if (rows.hasNext()) {
            if (false) {

                XSSFRow row = ((XSSFRow) rows.next());
                try {
                    String valor = row.getCell(0).toString();

                    if (!(valor.length() > 0)) { // id cliente
                        verificado = false;
                        mensaje += "Error en el campo id cliente " + "  \n";
                    }
                    int tipo = row.getCell(1).getCellType();
                    if (tipo == 0) {
                        val = (int) row.getCell(1).getNumericCellValue();
                        valor = String.valueOf(val);
                    } else {
                        valor = row.getCell(1).getStringCellValue();
                    }

                    if (!(valor != null || valor.isEmpty() || valor.equals(""))) { // nit del cliente
                        verificado = false;
                        mensaje += "Error en el campo nit del cliente " + "  \n";
                    }
                    valor = row.getCell(2).getStringCellValue();
                    if (!(valor != null || valor.isEmpty() || valor.equals(""))) {  // nombre del establecimiento
                        verificado = false;
                        mensaje += "Error en el campo nombre del establecimiento " + "  \n";
                    }
                    valor = row.getCell(3).getStringCellValue();
                    if (!(valor != null || valor.isEmpty() || valor.equals(""))) { // nombre del cliente
                        verificado = false;
                        mensaje += "Error en el campo nombre del cliente" + "  \n";
                    }
                    valor = row.getCell(4).getStringCellValue();
                    if (!(valor != null || valor.isEmpty() || valor.equals(""))) { // direccion del cliente
                        verificado = false;
                        mensaje += "Error en el campo direccion del cliente" + "  \n";
                    }
                    valor = row.getCell(5).getStringCellValue();
                    if (!(valor != null || valor.isEmpty() || valor.equals(""))) { // barrio del cliente
                        verificado = false;
                        mensaje += "Error en el campo barrio del cliente" + "  \n";
                    }
//                    if (!(row.getCell(6).getCellType() == 1 || row.getCell(6).getStringCellValue().isEmpty() )) { // ciudad
//                        verificado = false;
//                        mensaje += "Error en el campo ciudad" + "  \n";
//                    }
                    valor = row.getCell(7).getStringCellValue();
                    if (!(valor != null || valor.isEmpty() || valor.equals(""))) { //  tipo de negocio
                        verificado = false;
                        mensaje += "Error en el campo tipo de negocio" + "  \n";
                    }
                    if (!(row.getCell(8).getCellType() == 0)) { // numero de la factura
                        verificado = false;
                        mensaje += "Error en el campo tipo de negocio" + "  \n";
                    }

                    if (!(row.getCell(10).getCellType() == 0)) { // codigo del producto
                        verificado = false;
                        //mensaje += "Error en el campo codigo del producto" + "  \n";
                    }
                    valor = row.getCell(11).getStringCellValue();
                    if (!(valor != null || valor.isEmpty() || valor.equals(""))) { // descripcion del producto
                        verificado = false;
                        mensaje += "Error en el campo descripcion del producto" + "  \n";
                    }
                    valor = row.getCell(12).getStringCellValue();
                    if (!(valor != null || valor.isEmpty() || valor.equals(""))) { // proveedor del producto
                        verificado = false;
                        mensaje += "Error en el campo descripcion del proveedor del producto" + "  \n";
                    }

                    if (!(row.getCell(13).getCellType() == 0)) { //  vaalor factura sin iva
                        verificado = false;
                        mensaje += "Error en el campo proveedor del producto" + "  \n";
                    }
                    if (!(row.getCell(14).getCellType() == 0)) { // valor iva la factura
                        verificado = false;
                        mensaje += "Error en el campo valor iva la factura" + "  \n";
                    }
                    if (!(row.getCell(15).getCellType() == 0)) { // vslor total de la factura(errado el dato)
                        verificado = false;
                        mensaje += "Error en el campo vslor total de la factura" + "  \n";
                    }
                    if (!(row.getCell(16).getCellType() == 0)) { //  canttidad de producto
                        verificado = false;
                        mensaje += "Error en el campo canttidad de producto" + "  \n";
                    }
                    if (!(row.getCell(17).getCellType() == 0)) { // valor unitario
                        verificado = false;
                        mensaje += "Error en el campo valor unitario" + "  \n";
                    }
                    if (!(row.getCell(18).getCellType() == 0)) { // valor total producto
                        verificado = false;
                        mensaje += "Error en el campo valor total producto" + "  \n";
                    }
                    valor = row.getCell(19).getStringCellValue();
                    if (!(valor != null || valor.isEmpty() || valor.equals(""))) { // vendedor
                        verificado = false;
                        mensaje += "Error en el campo vendedor" + "  \n";
                    }
                    if (!verificado) {
                        mensaje += "Error, el archivo no cumple los requisitos " + "  \n";
                    }
                } catch (Exception ex) {
                    verificado = false;
                    mensaje += "Error, el archivo no corresponde al formato adecuado (1)" + ex + "  \n";
                }

            } else {
                verificado = false;
                mensaje += "Error, el archivo está vacío  " + "  \n";
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FImportarArchivoExcelPacheco.class.getName()).log(Level.SEVERE, null, ex);
            verificado = false;
            mensaje += "Error, el archivo no existe " + "  \n";
        } catch (IOException ex) {
            Logger.getLogger(FImportarArchivoExcelPacheco.class.getName()).log(Level.SEVERE, null, ex);
            verificado = false;
            mensaje += "Error, el archivo no corresponde al formato (2) " + "  \n";
        }
        return verificado;
    }

}
