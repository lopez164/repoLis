/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.consultas;

import aplicacionlogistica.distribucion.Threads.HiloConsultarManifiesto;
import aplicacionlogistica.distribucion.Threads.PerformanceInfiniteProgressPanel;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.FManifestarPedidosEnRuta;
import aplicacionlogistica.distribucion.formularios.FModificarManifiesto;
import aplicacionlogistica.distribucion.formularios.Hielera.FDespachoHielera;
import aplicacionlogistica.distribucion.formularios.poblaciones.FManifestarPedidosPoblaciones;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import mtto.vehiculos.CCarros;
import mtto.vehiculos.CVehiculos;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class FConsultarVehiculos extends javax.swing.JInternalFrame {

    Object[] fila = new Object[2];
    DefaultTableModel modelo;

    private PerformanceInfiniteProgressPanel panel;

    CCarros carro;
    Inicio ini;

    // IngresarManifiestoDeDistribucion form;
    // FManifestarPedidosEnRuta fManifestarPedidosEnRuta;
    FManifestarPedidosEnRuta fManifestarPedidosEnRuta = null;
    FManifestarPedidosPoblaciones fManifestarPedidosPoblaciones = null;
     FModificarManifiesto fModificarManifiesto =  null;

    /**
     * Creates new form FBuscar2
     */
    public FConsultarVehiculos() {
        initComponents();

    }

    public FConsultarVehiculos(Inicio ini) {
        initComponents();
        this.ini = ini;
        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);

    }

    public FConsultarVehiculos(Inicio ini, JInternalFrame dFac) {
        initComponents();

        this.ini = ini;
        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);

        llenarTabla();
    }
    
     public FConsultarVehiculos(Inicio ini, FModificarManifiesto fModificarManifiesto) {
        initComponents();
        this.fModificarManifiesto = fModificarManifiesto;
        this.ini = ini;
        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);

        llenarTabla();
    }

    public FConsultarVehiculos(Inicio ini, FManifestarPedidosEnRuta form) {
        this.ini = ini;
        this.fManifestarPedidosEnRuta = form;
        initComponents();
        carro = new CCarros(ini);
        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);

        llenarTabla();
    }

    public FConsultarVehiculos(Inicio ini, FManifestarPedidosPoblaciones fManifestarPedidosPoblaciones) {
        this.ini = ini;
        this.fManifestarPedidosPoblaciones = fManifestarPedidosPoblaciones;
        initComponents();
        carro = new CCarros(ini);
        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);

        llenarTabla();
    }
    
      public FConsultarVehiculos(Inicio ini, FDespachoHielera guiDespachoHielera) {
        this.ini = ini;
        //this.fManifestarPedidosEnRuta = form;
        initComponents();
        carro = new CCarros(ini);
        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);
        this.setResizable(false);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);

        llenarTabla();
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
        jTextField1.setText("Para consultar los vehiculos debe escoger  dando click sobre él...");
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
                "ITEM", "PLACA", "CONDUCTOR"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
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
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(70);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(400);
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
                .addContainerGap(90, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(38, 38, 38))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        try {
            int row = jTable1.getSelectedRow();
            //String placa manifiesto=jTable1.getValueAt(row, 1).toString();

//            for(CCarros  veh : ini.getListaDeVehiculos()){
//                if(veh.getPlaca().equals(jTable1.getValueAt(row, 1).toString())){
//                    carro=veh;
//                }
//            }
            carro = ini.getListaDeVehiculos().get(row);

        } catch (Exception ex) {
            Logger.getLogger(FConsultarVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jTable1MouseClicked

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed

        if (fManifestarPedidosEnRuta != null) {
            fManifestarPedidosEnRuta.txtPlaca.setText("" + carro.getPlaca());
            btnAceptar01();
        }

        if (fManifestarPedidosPoblaciones != null) {
            fManifestarPedidosPoblaciones.txtPlaca.setText("" + carro.getPlaca());
            btnAceptar02();
        }
        
         if (fModificarManifiesto != null) {
            fModificarManifiesto.txtPlaca.setText("" + carro.getPlaca());
           
        }
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_btnAceptarActionPerformed

    public void btnAceptar01() {
        try {
            /* INICIAMOS EL NUEVO MANIFIESTO, EN EL CUAL SE CONSULTA LOS MANIFIESTOS
            QUE TENGA LA PLACA INGRESADA EN EL CAMPO DE TEXTO, DE LOS
            CUALES SE TOMA EL ÚLTIMO */
            this.fManifestarPedidosEnRuta.manifiestoActual = new CManifiestosDeDistribucion(ini, carro.getPlaca());

            this.fManifestarPedidosEnRuta.carro = carro;

            /* Si el carro no tiene manifiestos, es decir no ha trabajado en la operación, entonces se crea el
            manifiesto de distribución en blanco y se le asigna un estado =0
            si ya tiene manifiestos creados , la consulta trae el último que sacó a distribución  y verifica su estado */
            if (this.fManifestarPedidosEnRuta.manifiestoActual.getNumeroManifiesto() == null) {
                this.fManifestarPedidosEnRuta.manifiestoActual.setEstadoManifiesto(0);
                this.fManifestarPedidosEnRuta.manifiestoActual.setIsFree(1);
            }

            new Thread(new HiloConsultarManifiesto(ini, fManifestarPedidosEnRuta)).start();

        } catch (Exception ex) {
            Logger.getLogger(FConsultarVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void btnAceptar02() {
        try {
            /* INICIAMOS EL NUEVO MANIFIESTO, EN EL CUAL SE CONSULTA LOS MANIFIESTOS
            QUE TENGA LA PLACA INGRESADA EN EL CAMPO DE TEXTO, DE LOS
            CUALES SE TOMA EL ÚLTIMO */
            this.fManifestarPedidosPoblaciones.manifiestoActual = new CManifiestosDeDistribucion(ini, carro.getPlaca());

            this.fManifestarPedidosPoblaciones.carro = carro;

            /* Si el carro no tiene manifiestos, es decir no ha trabajado en la operación, entonces se crea el
            manifiesto de distribución en blanco y se le asigna un estado =0
            si ya tiene manifiestos creados , la consulta trae el último que sacó a distribución  y verifica su estado */
            if (this.fManifestarPedidosPoblaciones.manifiestoActual.getNumeroManifiesto() == null) {
                this.fManifestarPedidosPoblaciones.manifiestoActual.setEstadoManifiesto(0);
                this.fManifestarPedidosPoblaciones.manifiestoActual.setIsFree(1);
            }

            new Thread(new HiloConsultarManifiesto(ini, fManifestarPedidosPoblaciones)).start();

        } catch (Exception ex) {
            Logger.getLogger(FConsultarVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llenarTabla() {

        try {
            int i = 1;
            for (CVehiculos obj : ini.getListaDeVehiculos()) {

                fila = new Object[3];
                fila[0] = i;
                fila[1] = obj.getPlaca();
                for (CEmpleados emp : ini.getListaDeEmpleados()) {
                    if (emp.getCedula().equals(obj.getConductor())) {
                        fila[2] = emp.getNombres() + " " + emp.getApellidos();
                        break;
                    }
                }
                modelo = (DefaultTableModel) jTable1.getModel();
                modelo.addRow(fila);
            }

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
