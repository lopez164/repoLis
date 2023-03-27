/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.consultas;

import aplicacionlogistica.distribucion.Threads.PerformanceInfiniteProgressPanel;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class FConsultaManifiestosSinDescargar extends javax.swing.JInternalFrame {

    public void setIni(Inicio ini) {
        this.ini = ini;
    }

    Object[] fila = new Object[6];
    DefaultTableModel modelo;

    public DescargarFacturas fDescargarFacturas = null;
    FConsultarManifiestos fConsultarManifiesto = null;

    private PerformanceInfiniteProgressPanel panel;
    String usuario;
    int manifiesto;
    Inicio ini = null;

    /**
     * Creates new form FBuscar2
     */
    public FConsultaManifiestosSinDescargar() {
        initComponents();

    }

    public FConsultaManifiestosSinDescargar(Inicio ini) {
        initComponents();
        this.ini = ini;
        this.ini.LLenarListaDeManifiestosSinDescargar();
        llenarTablaConsultaManifiestos();

    }

    public FConsultaManifiestosSinDescargar(Inicio ini, JInternalFrame dFac) {

    }

    public FConsultaManifiestosSinDescargar(Inicio ini, DescargarFacturas dFac) {
        initComponents();
        this.ini = dFac.ini;
        this.ini.LLenarListaDeManifiestosSinDescargar();
        panel = new PerformanceInfiniteProgressPanel(true);
        this.add(panel);
        panel.setVisible(false);

        this.setResizable(false);
        //   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        ///this.setLocation((screenSize.width - this.getSize().width) / 2, (screenSize.height - this.getSize().height) / 2);
        llenarTabla();

    }

    public FConsultaManifiestosSinDescargar(Inicio ini, FConsultarManifiestos consMan) {
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
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario Reporte de Manifiestos pendientes por descargar");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mfto. #", "Ruta", "Canal", "Placa", "Conductor", "Fecha Dist."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
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
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTable1.getColumnModel().getColumn(3).setResizable(false);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(100);
            jTable1.getColumnModel().getColumn(4).setResizable(false);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(300);
            jTable1.getColumnModel().getColumn(5).setResizable(false);
            jTable1.getColumnModel().getColumn(5).setPreferredWidth(100);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 976, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 448, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTable1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable1MouseClicked
        // TODO add your handling code here:

        int row = jTable1.getSelectedRow();
        manifiesto = Integer.parseInt(jTable1.getValueAt(row, 0).toString());
    }//GEN-LAST:event_jTable1MouseClicked

    public void llenarTabla() {
        if(fDescargarFacturas !=null){
           this.ini = fDescargarFacturas.ini; 
        }
        

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
            for (CManifiestosDeDistribucion obj : ini.getListaDeManifiestossinDescargar()) {

                fila = new Object[4];

                fila[0] = obj.getNumeroManifiesto();
                fila[1] = obj.getVehiculo();
                for (CEmpleados emp : ini.getListaDeEmpleados()) {
                    if (emp.getCedula().equals(obj.getConductor())) {
                        fila[2] = emp.getNombres() + " " + emp.getApellidos();
                        break;
                    }
                }

                fila[3] = obj.getFechaDistribucion();
                modelo.addRow(fila);

            }

        } catch (Exception ex) {
            Logger.getLogger(FBuscarEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void llenarTablaConsultaManifiestos() {

        try {
            
            modelo = (DefaultTableModel) jTable1.getModel();
            for (CManifiestosDeDistribucion obj : ini.getListaDeManifiestossinDescargar()) {
                //CManifiestosDeDistribucion manif = new CManifiestosDeDistribucion(ini,Integer.parseInt(obj.toString()));

                fila = new Object[6];

                fila[0] = obj.getNumeroManifiesto();
                fila[1] = obj.getNombreDeRuta();
                fila[2] = obj.getNombreCanal();
                fila[3] = obj.getVehiculo();
                fila[4] = obj.getNombreConductor() + " " + obj.getApellidosConductor();
                fila[5] = obj.getFechaDistribucion();
              
                modelo.addRow(fila);

            }

        } catch (Exception ex) {
            Logger.getLogger(FBuscarEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
