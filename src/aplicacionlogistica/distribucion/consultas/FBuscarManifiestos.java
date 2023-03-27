/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.consultas;

import aplicacionlogistica.distribucion.Threads.HiloConsultarManifiesto;
import aplicacionlogistica.distribucion.Threads.PerformanceInfiniteProgressPanel;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.Vst_empleados;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class FBuscarManifiestos extends javax.swing.JInternalFrame {

    public void setIni(Inicio ini) {
        this.ini = ini;
    }

    Object[] fila = new Object[3];
    DefaultTableModel modelo;

    public DescargarFacturas fDescargarFacturas = null;
    FConsultarManifiestos fConsultarManifiesto = null;

    private PerformanceInfiniteProgressPanel panel;
    String usuario;
    int numeroManifiesto;
    Inicio ini = null;

    /**
     * Creates new form FBuscar2
     */
    public FBuscarManifiestos() {
        initComponents();

    }

    public FBuscarManifiestos(Inicio ini) {
        initComponents();
        this.ini = ini;

    }

    public FBuscarManifiestos(Inicio ini, JInternalFrame dFac) {

    }

    public FBuscarManifiestos(Inicio ini, DescargarFacturas dFac) {
        initComponents();
        fDescargarFacturas = dFac;
        this.ini = ini;
        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);

        this.setResizable(false);
        //   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ///this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);
        llenarTabla();

    }

    public FBuscarManifiestos(Inicio ini, FConsultarManifiestos consMan) {
        initComponents();
        this.ini = ini;
        this.fConsultarManifiesto = consMan;
        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.ini.LLenarListaDeManifiestosSinDescargar();
        this.setResizable(false);

        llenarTablaConsultaManifiestos();

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
        jTextField1 = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        btnAceptar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        jTextField1.setEditable(false);
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Para consultar manifiesto debe escoger  dando click sobre él...");
        jTextField1.setEnabled(false);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MFTO. #", "PLACA", "CONDUCTOR", "FECHA"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
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
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(300);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(100);
        }

        btnAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/clean.png"))); // NOI18N
        btnAceptar.setText("Aceptar");
        btnAceptar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAceptar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
            }
        });

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/exit.png"))); // NOI18N
        btnSalir.setText("Salir");
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(90, 90, 90))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 196, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

        int row = jTable1.getSelectedRow();
        numeroManifiesto = Integer.parseInt(jTable1.getValueAt(row, 0).toString());
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed

        if (fDescargarFacturas != null) {
            fDescargarFacturas.txtNumeroManifiesto.setText("" + numeroManifiesto);
            for (CManifiestosDeDistribucion manifiesto : ini.getListaDeManifiestossinDescargar()) {
                if (manifiesto.getNumeroManifiesto().equals("" + numeroManifiesto)) {
                    fDescargarFacturas.manifiestoActual = manifiesto;
                    fDescargarFacturas.txtNumeroManifiesto.setText(manifiesto.getNumeroManifiesto());
                    new Thread(new HiloConsultarManifiesto(ini, fDescargarFacturas)).start();
                }
            }

        }
        if (fConsultarManifiesto != null) {
            fConsultarManifiesto.txtManifiesto.setText("" + numeroManifiesto);
            fConsultarManifiesto.consultarManifiesto();
        }
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_btnAceptarActionPerformed

    public void llenarTabla() {

        DefaultTableModel modelo = (DefaultTableModel) jTable1.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        try {
            if (ini.getListaDeManifiestossinDescargar() == null) {
                ini.LLenarListaDeManifiestosSinDescargar();
            }
            modelo = (DefaultTableModel) jTable1.getModel();
            for (CManifiestosDeDistribucion manifiestos : ini.getListaDeManifiestossinDescargar()) {

                if (manifiestos.getEstadoManifiesto() <= 3) {
                    if (manifiestos.getEstadoManifiesto() < 4) {

                        fila = new Object[4];

                        fila[0] = manifiestos.getNumeroManifiesto();
                        fila[1] = manifiestos.getVehiculo();
                        fila[2] = manifiestos.getNombreConductor() + " " + manifiestos.getApellidosConductor();
                        fila[3] = manifiestos.getFechaDistribucion();
                        modelo.addRow(fila);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(FBuscarEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llenarTablaConsultaManifiestos() {

        List<String> list = new ArrayList();

        try {
            CManifiestosDeDistribucion mani = new CManifiestosDeDistribucion(ini);

            list = mani.listaDeManifiestosSinDescargar();
            modelo = (DefaultTableModel) jTable1.getModel();
            for (CManifiestosDeDistribucion obj : ini.getListaDeManifiestossinDescargar()) {
                //CManifiestosDeDistribucion manif = new CManifiestosDeDistribucion(ini,Integer.parseInt(obj.toString()));

                fila = new Object[4];

                fila[0] = obj.getNumeroManifiesto();
                fila[1] = obj.getVehiculo();
                fila[2] = obj.getNombreConductor() + " " + obj.getApellidosConductor();
                fila[3] = obj.getFechaDistribucion();
                modelo.addRow(fila);

            }

        } catch (SQLException ex) {
            Logger.getLogger(FBuscarEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(FBuscarEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAceptar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
