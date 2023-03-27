/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.reportes;

import aplicacionlogistica.distribucion.Threads.HiloListadoDeManifiestosSinConciliar;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.formularios.reportes.threads.HiloRefrescarReporteManifiestosSinConciliar;
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
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VLI_488
 */
public class FReporteManifiestosConcilidos extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    HiloReportePedidosMovilizadosPorPeriodo datos;
    public Inicio ini;
    int filaTabla2;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());

    public int cantidadGraficos = 0;
    public ArrayList<CManifiestosDeDistribucion> listaDeManifiestosSinConciliar;
    public Date fechaIncial;

    /**
     * Creates new form IngresarPersonas
     */
    /**
     * Crea el Formulario IngresarPersonas
     *
     * @param ini
     */
    public FReporteManifiestosConcilidos(Inicio ini) throws Exception {
        // new Thread(new HiloListadoDeManifiestosSinConciliar(ini)).start();
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

        cuadrarFechaJDateChooser();

    }

    private void cuadrarFechaJDateChooser() {
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
            //jFechaFinal.setDate(new Date(min));

        } catch (Exception ex) {
            System.out.println("Error en dispatchKeyEvent ");
            Logger.getLogger(FReporteManifiestosConcilidos.class.getName()).log(Level.SEVERE, null, ex);
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
        btnAceptar = new javax.swing.JButton();
        btnExportarExcel = new javax.swing.JButton();
        barra = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        pnlGrafico1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblManifiestosSinConciliar = new javax.swing.JTable()
        {

            public boolean isCellEditable(int rowIndex, int colIndex) {

                return false; //Disallow the editing of any cell

            }
        }
        ;
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnrefresh = new javax.swing.JToggleButton();
        jBtnExcel = new javax.swing.JToggleButton();
        jBtnReport = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario de consulta movilizado por periodo");
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
        jLabel40.setText("Fecha Incial");

        lblCirculoDeProgreso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N
        lblCirculoDeProgreso.setEnabled(false);

        jFechaInicial.setToolTipText("Seleccionar la fecha ");
        jFechaInicial.setDateFormatString("yyyy/MM/dd");
        jFechaInicial.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N

        btnAceptar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Yes.png"))); // NOI18N
        btnAceptar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAceptarActionPerformed(evt);
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
                .addGap(6, 6, 6)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExportarExcel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(160, 160, 160))
                    .addComponent(barra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jFechaInicial, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnAceptar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnExportarExcel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1))
                .addGap(5, 5, 5)
                .addComponent(barra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlGrafico1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblManifiestosSinConciliar.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "vehiculo", "conductor", "ruta", "fecha Dist.", "N° Manifiesto", "Vr. Recaudado", "Vr. Consignacion", "estado", "Add"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblManifiestosSinConciliar);
        if (tblManifiestosSinConciliar.getColumnModel().getColumnCount() > 0) {
            tblManifiestosSinConciliar.getColumnModel().getColumn(0).setMinWidth(35);
            tblManifiestosSinConciliar.getColumnModel().getColumn(0).setPreferredWidth(35);
            tblManifiestosSinConciliar.getColumnModel().getColumn(0).setMaxWidth(35);
            tblManifiestosSinConciliar.getColumnModel().getColumn(1).setMinWidth(80);
            tblManifiestosSinConciliar.getColumnModel().getColumn(1).setPreferredWidth(80);
            tblManifiestosSinConciliar.getColumnModel().getColumn(1).setMaxWidth(80);
            tblManifiestosSinConciliar.getColumnModel().getColumn(2).setMinWidth(180);
            tblManifiestosSinConciliar.getColumnModel().getColumn(3).setPreferredWidth(125);
            tblManifiestosSinConciliar.getColumnModel().getColumn(4).setPreferredWidth(65);
            tblManifiestosSinConciliar.getColumnModel().getColumn(5).setPreferredWidth(50);
            tblManifiestosSinConciliar.getColumnModel().getColumn(6).setMinWidth(100);
            tblManifiestosSinConciliar.getColumnModel().getColumn(7).setMinWidth(100);
            tblManifiestosSinConciliar.getColumnModel().getColumn(9).setPreferredWidth(20);
        }

        javax.swing.GroupLayout pnlGrafico1Layout = new javax.swing.GroupLayout(pnlGrafico1);
        pnlGrafico1.setLayout(pnlGrafico1Layout);
        pnlGrafico1Layout.setHorizontalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlGrafico1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1230, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        pnlGrafico1Layout.setVerticalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGrafico1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE))
        );

        jToolBar1.setRollover(true);

        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jBtnNuevo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnNuevo.setFocusable(false);
        jBtnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnNuevo);

        jBtnrefresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png"))); // NOI18N
        jBtnrefresh.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnrefresh.setFocusable(false);
        jBtnrefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnrefresh.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnrefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnrefreshActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnrefresh);

        jBtnExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        jBtnExcel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnExcel.setFocusable(false);
        jBtnExcel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnExcel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnExcelActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnExcel);

        jBtnReport.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        jBtnReport.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnReport.setFocusable(false);
        jBtnReport.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnReport.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(jBtnReport);

        jBtnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jBtnCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnCancel.setFocusable(false);
        jBtnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnCancel);

        jBtnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jBtnExit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnExit.setFocusable(false);
        jBtnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnExit.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnExitActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnExit);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlGrafico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(pnlGrafico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing


    }//GEN-LAST:event_formInternalFrameClosing

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        btnAceptar.setEnabled(false);
        btnExportarExcel.setEnabled(false);

        fechaIncial = ini.getFechaSql(this.jFechaInicial);
        //fechaFinal = ini.getFechaSql(this.jFechaFinal);
        //String ss = "" + this.jFechaFinal.getDate();
        // HiloCrearChartBarras1 FgraficoPedidosMovilizados = new HiloCrearChartBarras1(this.ini,this,fechaIncial,fechaFinal) ;

        //new Thread(new HiloReportePedidosMovilizadosPorPeriodo(this)).start();

    }//GEN-LAST:event_btnAceptarActionPerformed

    private void btnExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcelActionPerformed
        btnExportarExcel.setEnabled(false);
        //new Thread(new HiloReportePedidosMovilizadosPorPeriodo(this, false)).start();


    }//GEN-LAST:event_btnExportarExcelActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed
        limpiar();
    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void limpiar() {

        cuadrarFechaJDateChooser();
        limpiarTabla();
        barra.setValue(0);
        btnAceptar.setEnabled(true);
    }

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        limpiar();
        this.dispose();
        this.setVisible(false);

    }//GEN-LAST:event_jBtnExitActionPerformed

    private void jBtnExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExcelActionPerformed
        btnExportarExcel.setEnabled(false);
        // new Thread(new HiloReportePedidosMovilizadosPorPeriodo(this, false)).start();
    }//GEN-LAST:event_jBtnExcelActionPerformed

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed
        limpiar();
    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnrefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnrefreshActionPerformed
        refrescarTblManifiestosSinConciliar();
    }//GEN-LAST:event_jBtnrefreshActionPerformed

    public void llenarTablabitacora() throws Exception {
        limpiarTabla();
        
       
        DefaultTableModel modelo = (DefaultTableModel) tblManifiestosSinConciliar.getModel();
      
       //tblManifiestosSinConciliar = new JTable(modelo);
        
        for (CManifiestosDeDistribucion obj : ini.getListaDeManifiestossinConciliar()) {
            filaTabla2 = tblManifiestosSinConciliar.getRowCount();

            modelo.addRow(new Object[tblManifiestosSinConciliar.getRowCount()]);

            tblManifiestosSinConciliar.setValueAt(filaTabla2 + 1, filaTabla2, 0);  // item
            tblManifiestosSinConciliar.setValueAt(obj.getVehiculo(), filaTabla2, 1);  // item
            tblManifiestosSinConciliar.setValueAt(obj.getNombreConductor(), filaTabla2, 2); // numero de manifiesto
            tblManifiestosSinConciliar.setValueAt(obj.getNombreDeRuta(), filaTabla2, 3); // fecha de distribucion
            tblManifiestosSinConciliar.setValueAt(obj.getFechaDistribucion(), filaTabla2, 4); // placa del vehiculo
            tblManifiestosSinConciliar.setValueAt(obj.getNumeroManifiesto(), filaTabla2, 5); // nombre del conductor
            tblManifiestosSinConciliar.setValueAt(nf.format(obj.getValorRecaudado()), filaTabla2, 6); // nombre del conductor
            tblManifiestosSinConciliar.setValueAt(nf.format(obj.getValorConsignado()), filaTabla2, 7); // nombre del conductor
            if (obj.getEstadoManifiesto() == 3) {
                tblManifiestosSinConciliar.setValueAt("En distribucion", filaTabla2, 8); // nombre del conductor
              tblManifiestosSinConciliar.setValueAt(false, filaTabla2, 9); // nombre del conductor
              

            } else {
               tblManifiestosSinConciliar.setValueAt("Descargado", filaTabla2, 8); // nombre del conductor
               tblManifiestosSinConciliar.setValueAt(true, filaTabla2, 9); // nombre del conductor
            }

           

        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barra;
    private javax.swing.JButton btnAceptar;
    public javax.swing.JButton btnExportarExcel;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExcel;
    private javax.swing.JToggleButton jBtnExit;
    private javax.swing.JButton jBtnNuevo;
    private javax.swing.JToggleButton jBtnReport;
    private javax.swing.JToggleButton jBtnrefresh;
    public com.toedter.calendar.JDateChooser jFechaInicial;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JPanel pnlGrafico1;
    private javax.swing.JTable tblManifiestosSinConciliar;
    // End of variables declaration//GEN-END:variables

    public void limpiarTabla() {

        DefaultTableModel modelo = (DefaultTableModel) tblManifiestosSinConciliar.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

    public boolean refrescarTblManifiestosSinConciliar() {
        boolean ok = false;
        barra.setValue(0);
        limpiarTabla();
        new Thread(new HiloRefrescarReporteManifiestosSinConciliar(this)).start();
        return ok;

    }

}
