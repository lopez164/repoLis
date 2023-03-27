/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.ReporteManifiestosSinDescargar;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.reportes.HiloReportePedidosMovilizadosPorPeriodo;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
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
    public int xxx;

    public int cantidadGraficos = 0;
    public ArrayList<CManifiestosDeDistribucion> listaDeRegistros;
    public Date fechaIncial;
    //public Date fechaFinal;
    public CEmpleados conductor;
    public String[] header;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

    

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
        lblExportandoExcel.setVisible(false);
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

           // jFechaInicial.setDate(new Date(min));
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
        lblCirculoDeProgreso = new javax.swing.JLabel();
        btnEjecutar = new javax.swing.JButton();
        btnExportarExcel = new javax.swing.JButton();
        barra = new javax.swing.JProgressBar();
        lblExportandoExcel = new javax.swing.JLabel();
        pnlGrafico1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaRegistros = new javax.swing.JTable();
        jToolBar1 = new javax.swing.JToolBar();
        brnNuevo = new javax.swing.JButton();
        btnCancelar = new javax.swing.JToggleButton();
        btnCancelar1 = new javax.swing.JToggleButton();
        btnSalir = new javax.swing.JToggleButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Consulta Manifiestos Pendientes por Descargar");
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

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta Manifiestos Pendientes por descargar"));

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        lblCirculoDeProgreso.setEnabled(false);

        btnEjecutar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Yes.png"))); // NOI18N
        btnEjecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEjecutarActionPerformed(evt);
            }
        });

        btnExportarExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        btnExportarExcel.setEnabled(false);
        btnExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarExcelActionPerformed(evt);
            }
        });

        lblExportandoExcel.setText("Exportando a excel ");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEjecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExportarExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblExportandoExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))
                    .addComponent(barra, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnEjecutar)
                            .addComponent(btnExportarExcel)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblExportandoExcel)))
                .addGap(2, 2, 2)
                .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, 6, Short.MAX_VALUE))
        );

        pnlGrafico1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tablaRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "FechaDist.", "Manifiesto", "Ruta", "Canal", "Vehiculo", "TipoVehiculo", "Conductor", "# Pedidos", "Valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
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
        tablaRegistros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaRegistrosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaRegistros);
        if (tablaRegistros.getColumnModel().getColumnCount() > 0) {
            tablaRegistros.getColumnModel().getColumn(0).setMinWidth(40);
            tablaRegistros.getColumnModel().getColumn(0).setPreferredWidth(40);
            tablaRegistros.getColumnModel().getColumn(0).setMaxWidth(40);
            tablaRegistros.getColumnModel().getColumn(1).setMinWidth(120);
            tablaRegistros.getColumnModel().getColumn(1).setPreferredWidth(120);
            tablaRegistros.getColumnModel().getColumn(1).setMaxWidth(120);
            tablaRegistros.getColumnModel().getColumn(2).setMinWidth(80);
            tablaRegistros.getColumnModel().getColumn(2).setPreferredWidth(80);
            tablaRegistros.getColumnModel().getColumn(2).setMaxWidth(80);
            tablaRegistros.getColumnModel().getColumn(3).setMinWidth(200);
            tablaRegistros.getColumnModel().getColumn(3).setPreferredWidth(200);
            tablaRegistros.getColumnModel().getColumn(3).setMaxWidth(200);
            tablaRegistros.getColumnModel().getColumn(4).setMinWidth(100);
            tablaRegistros.getColumnModel().getColumn(4).setPreferredWidth(100);
            tablaRegistros.getColumnModel().getColumn(4).setMaxWidth(100);
            tablaRegistros.getColumnModel().getColumn(5).setMinWidth(80);
            tablaRegistros.getColumnModel().getColumn(5).setPreferredWidth(80);
            tablaRegistros.getColumnModel().getColumn(5).setMaxWidth(80);
            tablaRegistros.getColumnModel().getColumn(6).setMinWidth(100);
            tablaRegistros.getColumnModel().getColumn(6).setPreferredWidth(100);
            tablaRegistros.getColumnModel().getColumn(6).setMaxWidth(100);
            tablaRegistros.getColumnModel().getColumn(7).setMinWidth(200);
            tablaRegistros.getColumnModel().getColumn(7).setPreferredWidth(200);
            tablaRegistros.getColumnModel().getColumn(7).setMaxWidth(200);
            tablaRegistros.getColumnModel().getColumn(8).setMinWidth(80);
            tablaRegistros.getColumnModel().getColumn(8).setPreferredWidth(80);
            tablaRegistros.getColumnModel().getColumn(8).setMaxWidth(80);
            tablaRegistros.getColumnModel().getColumn(9).setMinWidth(180);
            tablaRegistros.getColumnModel().getColumn(9).setPreferredWidth(180);
            tablaRegistros.getColumnModel().getColumn(9).setMaxWidth(180);
        }

        javax.swing.GroupLayout pnlGrafico1Layout = new javax.swing.GroupLayout(pnlGrafico1);
        pnlGrafico1.setLayout(pnlGrafico1Layout);
        pnlGrafico1Layout.setHorizontalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlGrafico1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1123, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlGrafico1Layout.setVerticalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGrafico1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE))
        );

        jToolBar1.setRollover(true);

        brnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        brnNuevo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        brnNuevo.setFocusable(false);
        brnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        brnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        brnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                brnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(brnNuevo);

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        btnCancelar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancelar.setFocusable(false);
        btnCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCancelar);

        btnCancelar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        btnCancelar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnCancelar1.setFocusable(false);
        btnCancelar1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnCancelar1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnCancelar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelar1ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnCancelar1);

        btnSalir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        btnSalir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnSalir.setFocusable(false);
        btnSalir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSalir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });
        jToolBar1.add(btnSalir);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlGrafico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(pnlGrafico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing


    }//GEN-LAST:event_formInternalFrameClosing

    private void btnEjecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEjecutarActionPerformed
       
        cargarInformacion();


    }//GEN-LAST:event_btnEjecutarActionPerformed

    public void cargarInformacion() {
        btnEjecutar.setEnabled(false);
        btnExportarExcel.setEnabled(false);
        
        xxx = 1;
        //   fechaIncial = ini.getFechaSql(this.jFechaInicial);
        
        new Thread(new HiloReporteManifiestosSinDescargar(this)).start();
    }

    private void btnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcelActionPerformed
        btnExportarExcel.setEnabled(false);

        xxx = 2;
        new Thread(new HiloReporteManifiestosSinDescargar(this)).start();

    }//GEN-LAST:event_btnExportarExcelActionPerformed

    private void tablaRegistrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaRegistrosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaRegistrosMouseClicked

    private void brnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brnNuevoActionPerformed
        nuevo();        // TODO add your handling code here:
    }//GEN-LAST:event_brnNuevoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
         cancelar();      // TODO add your handling code here:
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
         salir();// TODO add your handling code here:
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnCancelar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelar1ActionPerformed
      btnExportarExcel.setEnabled(false);

        xxx = 2;
        new Thread(new HiloReporteManifiestosSinDescargar(this)).start();
    }//GEN-LAST:event_btnCancelar1ActionPerformed

    public void llenarTablabitacora() throws Exception {

        
        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + listaDeRegistros.size());

        DefaultTableModel modelo = (DefaultTableModel) tablaRegistros.getModel();

        for (CManifiestosDeDistribucion obj : listaDeRegistros) {
            filaTabla2 = tablaRegistros.getRowCount();

            modelo.addRow(new Object[tablaRegistros.getRowCount()]);
            int a = 0;

            tablaRegistros.setValueAt(filaTabla2 + 1, filaTabla2, a++);  // item
            tablaRegistros.setValueAt(obj.getFechaDistribucion(), filaTabla2, a++);  // item
            tablaRegistros.setValueAt(obj.getNumeroManifiesto(), filaTabla2, a++); // numero de manifiesto
            tablaRegistros.setValueAt(obj.getNombreDeRuta(), filaTabla2, a++); // placa del vehiculo
            tablaRegistros.setValueAt(obj.getNombreCanal(), filaTabla2, a++); // placa del vehiculo
            tablaRegistros.setValueAt(obj.getVehiculo(), filaTabla2, a++); // fecha de distribucion
            tablaRegistros.setValueAt(obj.getTipoVehiculo(), filaTabla2, a++); // fecha de distribucion
            tablaRegistros.setValueAt(obj.getNombreConductor() + " " + obj.getApellidosConductor(), filaTabla2, a++); // nombre del conductor
            tablaRegistros.setValueAt(obj.getCantidadPedidos(), filaTabla2, a++); // nombre del conductor
            tablaRegistros.setValueAt(nf.format(obj.getValorTotalManifiesto()), filaTabla2, a++); // nombre del conductor

        }
        btnExportarExcel.setEnabled(true);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barra;
    private javax.swing.JButton brnNuevo;
    private javax.swing.JToggleButton btnCancelar;
    private javax.swing.JToggleButton btnCancelar1;
    public javax.swing.JButton btnEjecutar;
    public javax.swing.JButton btnExportarExcel;
    private javax.swing.JToggleButton btnSalir;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JLabel lblExportandoExcel;
    public javax.swing.JPanel pnlGrafico1;
    private javax.swing.JTable tablaRegistros;
    // End of variables declaration//GEN-END:variables

    public void limpiarTablaManifiestos() {

        DefaultTableModel modelo = (DefaultTableModel) tablaRegistros.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    private void nuevo() {
        limpiar();
        btnEjecutar.setEnabled(true);
    }

    private void cancelar() {
        limpiar();
    }

    private void salir() {
        limpiar();
        this.setVisible(false);

    }

    private void limpiar() {
        limpiarTablaManifiestos();
        barra.setValue(0);
        btnEjecutar.setEnabled(false);
        btnExportarExcel.setEnabled(false);
        lblCirculoDeProgreso.setVisible(false);
        lblExportandoExcel.setVisible(false);

    }
}
