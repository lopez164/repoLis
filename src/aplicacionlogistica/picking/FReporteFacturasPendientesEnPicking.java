/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.picking;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.distribucion.formularios.reportes.ReporteMovilizadoPorConductor.HiloFReporteMovilzadoPorConductor;
import aplicacionlogistica.distribucion.formularios.reportes.facturacion.FReporteFacturacionPorCanalHilo;
import aplicacionlogistica.distribucion.formularios.reportes.HiloReportePedidosMovilizadosPorPeriodo;
import com.opencsv.CSVWriter;
import java.awt.Desktop;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VLI_488
 */
public class FReporteFacturasPendientesEnPicking extends javax.swing.JInternalFrame {

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    HiloReportePedidosMovilizadosPorPeriodo datos;
    public Inicio ini;
    int filaTabla2;

    public int cantidadGraficos = 0;
    public List<List<String>> listaDeFacturas = null;
    public Date fechaIncial;

    String sql;
    String rutaArchivo;

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
    public FReporteFacturasPendientesEnPicking(Inicio ini) {
        initComponents();
        rutaArchivo = ini.getRutaDeApp() + "reportes/reporteFacturasPendientesPorAlistamiento.csv";
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
            Logger.getLogger(FReporteFacturasPendientesEnPicking.class.getName()).log(Level.SEVERE, null, ex);
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

        jToolBar1 = new javax.swing.JToolBar();
        brnNuevo = new javax.swing.JButton();
        btnExportarExcel2 = new javax.swing.JToggleButton();
        btnRefrescar = new javax.swing.JToggleButton();
        btnImprimir = new javax.swing.JToggleButton();
        btnCancelar = new javax.swing.JToggleButton();
        btnSalir = new javax.swing.JToggleButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel40 = new javax.swing.JLabel();
        lblCirculoDeProgreso = new javax.swing.JLabel();
        jFechaInicial = new com.toedter.calendar.JDateChooser();
        btnAceptar = new javax.swing.JButton();
        btnExportarExcel1 = new javax.swing.JButton();
        barra = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        lblContador = new javax.swing.JLabel();
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

        btnExportarExcel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        btnExportarExcel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnExportarExcel2.setFocusable(false);
        btnExportarExcel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnExportarExcel2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnExportarExcel2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarExcel2ActionPerformed(evt);
            }
        });
        jToolBar1.add(btnExportarExcel2);

        btnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png"))); // NOI18N
        btnRefrescar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnRefrescar.setFocusable(false);
        btnRefrescar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnRefrescar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefrescarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnRefrescar);

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        btnImprimir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnImprimir.setFocusable(false);
        btnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });
        jToolBar1.add(btnImprimir);

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

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Consulta de facturas pendientes por proceso de Picking(alistamiento)"));

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setLabelFor(jFechaInicial);
        jLabel40.setText("Fecha");

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

        btnExportarExcel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Excel-icon.png"))); // NOI18N
        btnExportarExcel1.setEnabled(false);
        btnExportarExcel1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportarExcel1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Exportando a excel ");

        lblContador.setFont(new java.awt.Font("DejaVu Sans", 0, 36)); // NOI18N
        lblContador.setText("0");
        lblContador.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(barra, javax.swing.GroupLayout.DEFAULT_SIZE, 1099, Short.MAX_VALUE)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAceptar, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnExportarExcel1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblContador, javax.swing.GroupLayout.PREFERRED_SIZE, 252, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCirculoDeProgreso, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAceptar)
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel1)
                        .addComponent(btnExportarExcel1))
                    .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jFechaInicial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblContador))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barra, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlGrafico1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tablaRegistros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "Fecha", "numeroFactura", "Manifiesto", "Destino", "nombre Cliente", "Direccion", "Valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaRegistros.getTableHeader().setReorderingAllowed(false);
        tablaRegistros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaRegistrosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaRegistros);
        if (tablaRegistros.getColumnModel().getColumnCount() > 0) {
            tablaRegistros.getColumnModel().getColumn(0).setPreferredWidth(30);
            tablaRegistros.getColumnModel().getColumn(1).setPreferredWidth(30);
            tablaRegistros.getColumnModel().getColumn(3).setPreferredWidth(30);
            tablaRegistros.getColumnModel().getColumn(4).setPreferredWidth(150);
            tablaRegistros.getColumnModel().getColumn(5).setPreferredWidth(150);
            tablaRegistros.getColumnModel().getColumn(6).setPreferredWidth(150);
            tablaRegistros.getColumnModel().getColumn(7).setPreferredWidth(80);
        }

        javax.swing.GroupLayout pnlGrafico1Layout = new javax.swing.GroupLayout(pnlGrafico1);
        pnlGrafico1.setLayout(pnlGrafico1Layout);
        pnlGrafico1Layout.setHorizontalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        pnlGrafico1Layout.setVerticalGroup(
            pnlGrafico1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlGrafico1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 731, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(407, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(pnlGrafico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(5, 5, 5))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlGrafico1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing


    }//GEN-LAST:event_formInternalFrameClosing

    private void btnAceptarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAceptarActionPerformed
        aceptar();


    }//GEN-LAST:event_btnAceptarActionPerformed

    private void aceptar() {
        btnAceptar.setEnabled(false);
        btnExportarExcel1.setEnabled(false);

        fechaIncial = ini.getFechaSql(this.jFechaInicial);

        new Thread(new HiloReporteFacturasPendientesenPicking(this, 0)).start();
    }

    private void btnExportarExcel1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcel1ActionPerformed
        exportarExcel();
    }//GEN-LAST:event_btnExportarExcel1ActionPerformed

    private void tablaRegistrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaRegistrosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaRegistrosMouseClicked

    private void brnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_brnNuevoActionPerformed
        // nuevo();        // TODO add your handling code here:
    }//GEN-LAST:event_brnNuevoActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        // cancelar();      // TODO add your handling code here:
        cancelar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        salir();// TODO add your handling code here:
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnExportarExcel2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportarExcel2ActionPerformed
        exportarExcel();
    }//GEN-LAST:event_btnExportarExcel2ActionPerformed

    private void btnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefrescarActionPerformed
        refrescar();
    }//GEN-LAST:event_btnRefrescarActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnImprimirActionPerformed

    public void llenarTablabitacora() {

        System.out.println(new Date() + " acá se calcula la cantidad de rtegistros de la lista =" + listaDeFacturas.size());

        DefaultTableModel modelo = (DefaultTableModel) tablaRegistros.getModel();

        int a = 0;

        for (List<String> obj : this.listaDeFacturas) {
            filaTabla2 = tablaRegistros.getRowCount();
            modelo.addRow(new Object[tablaRegistros.getRowCount()]);

            tablaRegistros.setValueAt(filaTabla2 + 1, filaTabla2, a++);  // item
            tablaRegistros.setValueAt("" + obj.get(0), filaTabla2, a++); // placa del vehiculo
            tablaRegistros.setValueAt("" + obj.get(1), filaTabla2, a++); // fecha de distribucion
            tablaRegistros.setValueAt("" + obj.get(2), filaTabla2, a++); // fecha de distribucion
            tablaRegistros.setValueAt("" + obj.get(3), filaTabla2, a++); // fecha de distribucion
            tablaRegistros.setValueAt("" + obj.get(4), filaTabla2, a++); // fecha de distribucion
            tablaRegistros.setValueAt("" + obj.get(5), filaTabla2, a++); // fecha de distribucion
            tablaRegistros.setValueAt(nf.format(Double.parseDouble(obj.get(6))), filaTabla2, a++); // fecha de distribucion

            a = 0;

        }
        lblContador.setText("" + this.listaDeFacturas.size());

        //tablaRegistros.setValueAt(obj.getCantidadDePedidos(), filaTabla2, a++); // nombre del conductor
        //tablaRegistros.setValueAt(obj.getValorManifiesto(), filaTabla2, a++); // nombre del conductor
    }

    private void refrescar() {
        limpiarTabla();
        aceptar();
    }
     private void salir() {
        cancelar();
        this.dispose();
    }
    
      private void cancelar() {
        limpiarTabla();
        btnAceptar.setEnabled(true);
        lblContador.setText("0");
       
    }

    private void exportarExcel() {
        btnExportarExcel1.setEnabled(false);

        new Thread(new HiloReporteFacturasPendientesenPicking(this, 1)).start();
    }

    private void limpiarTabla() {
        DefaultTableModel modelo = (DefaultTableModel) tablaRegistros.getModel();
        int filas = tablaRegistros.getRowCount();
        try {
            for (int i = 0; i <= filas; i++) {
                modelo.removeRow(0);

            }
        } catch (Exception ex) {

        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barra;
    private javax.swing.JButton brnNuevo;
    private javax.swing.JButton btnAceptar;
    private javax.swing.JToggleButton btnCancelar;
    public javax.swing.JButton btnExportarExcel1;
    private javax.swing.JToggleButton btnExportarExcel2;
    private javax.swing.JToggleButton btnImprimir;
    private javax.swing.JToggleButton btnRefrescar;
    private javax.swing.JToggleButton btnSalir;
    public com.toedter.calendar.JDateChooser jFechaInicial;
    public javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgreso;
    public javax.swing.JLabel lblContador;
    public javax.swing.JPanel pnlGrafico1;
    private javax.swing.JTable tablaRegistros;
    // End of variables declaration//GEN-END:variables

}
