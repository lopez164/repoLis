/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.ReportesManifiestosPendientes;

import aplicacionlogistica.distribucion.formularios.reportes.ReporteFacturasEnDistribucion.*;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteMovilizadoPorConductor.*;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.reportes.HiloReportePedidosMovilizadosPorPeriodo;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.vistas.Vst_FacturasEnDistribucion;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VLI_488
 */
public class FReporteManifiestosSinDescargar extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    HiloReportePedidosMovilizadosPorPeriodo datos;
    public Inicio ini;
    int filaTabla2;

    public int cantidadGraficos = 0;
    public ArrayList<Vst_FacturasEnDistribucion> listaDeRegistros;
    public Date fechaIncial;
    //public Date fechaFinal;
    public CEmpleados conductor;
    public String[] header;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

    public CEmpleados getConductor() {
        return conductor;
    }

    public void setConductor(CEmpleados conductor) {
        this.conductor = conductor;
    }

    /**
     * Creates new form IngresarPersonas
     */
    /**
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     */
    public FReporteManifiestosSinDescargar(Inicio ini) {
        initComponents();

        lblCirculoDeProgreso.setVisible(false);
        jLabel1.setVisible(false);
        this.ini = ini;

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
        //panelFotografia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/perfil.jpg")));
        try {
            lblCirculoDeProgreso.setVisible(false);
            // HiloCrearChartBarras1 FgraficoPedidosMovilizados = new HiloCrearChartBarras1(this.ini,pnlGrafico1) ;
            Calendar calendario = Calendar.getInstance();
            int diaMinimo = calendario.getActualMinimum(Calendar.DAY_OF_MONTH);
            int diaMaximo = calendario.getActualMaximum(Calendar.DAY_OF_MONTH);
            int anio = calendario.get(Calendar.YEAR);
            int mes = calendario.get(Calendar.MONTH);
            GregorianCalendar gc = new GregorianCalendar(anio, mes, diaMinimo);
            long min = gc.getTimeInMillis();

            jFechaInicial.setDate(new Date(min));
            gc = new GregorianCalendar(anio, mes, diaMaximo);
            min = gc.getTimeInMillis();

        } catch (Exception ex) {
            System.out.println("Error en dispatchKeyEvent ");
            Logger.getLogger(FReporteManifiestosSinDescargar.class.getName()).log(Level.SEVERE, null, ex);
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

        jPanel8 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jFechaInicial = new com.toedter.calendar.JDateChooser();
        jButton1 = new javax.swing.JButton();
        btnExportarExcel = new javax.swing.JButton();
        barra = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        pnlGrafico1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaRegistros = new javax.swing.JTable();

        setClosable(true);
        setIconifiable(true);
        setTitle("Consulta de Pedidos movilizados por conductores");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/turbo_64x64.png"))); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
        });

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta pedidos movilizados por rango"));

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setLabelFor(jFechaInicial);
        jLabel40.setText("Fecha");

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        lblCirculoDeProgreso.setEnabled(false);

        jFechaInicial.setToolTipText("Seleccionar la fecha ");
        jFechaInicial.setDateFormatString("yyyy/MM/dd");
        jFechaInicial.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Yes.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnExportarExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        btnExportarExcel.setEnabled(false);
        btnExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarExcelActionPerformed(evt);
            }
        });

        jLabel1.setText("Exportando a excel ");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExportarExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(506, 506, 506))
                    .addComponent(barra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1)
                            .addComponent(btnExportarExcel)
                            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(9, 9, 9))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 12, Short.MAX_VALUE))
        );

        pnlGrafico1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tablaRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "FechaDistribucion", "Manifiesto", "CANAL", "Vehiculo", "Conductor", "# Pedidos", "Valor"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaRegistros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaRegistrosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaRegistros);
        if (tablaRegistros.getColumnModel().getColumnCount() > 0) {
            tablaRegistros.getColumnModel().getColumn(0).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(0).setPreferredWidth(50);
            tablaRegistros.getColumnModel().getColumn(1).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(1).setPreferredWidth(50);
            tablaRegistros.getColumnModel().getColumn(2).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(2).setPreferredWidth(50);
            tablaRegistros.getColumnModel().getColumn(3).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(3).setPreferredWidth(100);
            tablaRegistros.getColumnModel().getColumn(4).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(4).setPreferredWidth(50);
            tablaRegistros.getColumnModel().getColumn(5).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(5).setPreferredWidth(200);
            tablaRegistros.getColumnModel().getColumn(6).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(6).setPreferredWidth(50);
            tablaRegistros.getColumnModel().getColumn(7).setResizable(false);
            tablaRegistros.getColumnModel().getColumn(7).setPreferredWidth(50);
        }

        javax.swing.GroupLayout pnlGrafico1Layout = new javax.swing.GroupLayout(pnlGrafico1);
        pnlGrafico1.setLayout(pnlGrafico1Layout);
        pnlGrafico1Layout.setHorizontalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGrafico1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1))
        );
        pnlGrafico1Layout.setVerticalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGrafico1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlGrafico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlGrafico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing


    }//GEN-LAST:event_formInternalFrameClosing

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jButton1.setEnabled(false);
        btnExportarExcel.setEnabled(false);

        fechaIncial = ini.getFechaSql(this.jFechaInicial);

        new Thread(new HiloReporteManifiestosSinDescargar(this)).start();


    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcelActionPerformed
        btnExportarExcel.setEnabled(false);

        // new Thread(new FReporteMovilzadoPorConductorHilo(this, false)).start();

    }//GEN-LAST:event_btnExportarExcelActionPerformed

    private void tablaRegistrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaRegistrosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaRegistrosMouseClicked

    public void llenarTablabitacora() throws Exception {

        int sumaEntregasTotales = 0;
        int sumaEntregasParciales = 0;
        int sumaDevolucinesTotales = 0;
        int sumaReenvios = 0;
        int sumaProgramados = 0;
        int sumaEntregas = 0;
        Double sumaSubtotales = 0.0;

        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + listaDeRegistros.size());

        DefaultTableModel modelo = (DefaultTableModel) tablaRegistros.getModel();

        for (Vst_FacturasEnDistribucion obj : listaDeRegistros) {
            filaTabla2 = tablaRegistros.getRowCount();

            modelo.addRow(new Object[tablaRegistros.getRowCount()]);
            int a = 0;

            tablaRegistros.setValueAt(filaTabla2 + 1, filaTabla2, a);  // item
            tablaRegistros.setValueAt(obj.getFechaDistribucion(), filaTabla2, a++);  // item
            tablaRegistros.setValueAt(obj.getNumeroManifiesto(), filaTabla2, a++); // numero de manifiesto
            tablaRegistros.setValueAt(obj.getNombreCanal(), filaTabla2, a++); // placa del vehiculo
            tablaRegistros.setValueAt(obj.getVehiculo(), filaTabla2, a++); // fecha de distribucion

            tablaRegistros.setValueAt(obj.getNombreConductor(), filaTabla2, a++); // nombre del conductor

            //tablaRegistros.setValueAt(obj.getCantidadDePedidos(), filaTabla2, a++); // nombre del conductor
            //tablaRegistros.setValueAt(obj.getValorManifiesto(), filaTabla2, a++); // nombre del conductor

        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barra;
    public javax.swing.JButton btnExportarExcel;
    private javax.swing.JButton jButton1;
    public com.toedter.calendar.JDateChooser jFechaInicial;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JPanel pnlGrafico1;
    private javax.swing.JTable tablaRegistros;
    // End of variables declaration//GEN-END:variables

}
