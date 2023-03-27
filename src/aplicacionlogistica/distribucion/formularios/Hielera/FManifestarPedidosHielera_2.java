/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeEmpleados;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeVehiculos;
import aplicacionlogistica.distribucion.consultas.FBuscarListadoDeEmpleados;
import aplicacionlogistica.distribucion.formularios.Hielera.Threads.HiloFmanifestarPedidosHielera_2;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * Esta vista, permite que el usuario pueda asignar a un manifiesto de
 * Distribución el conductor, el auxiliar, un vehiculo dando una ruta y
 * clasificandolo dentro de un cnal de venta; el sistema se encrga de crear el
 * manifiestos y posteriormente se le asignan las factura que van a salir a
 * distrbucion.
 *
 * @author Luis Eduardo López
 */
public class FManifestarPedidosHielera_2 extends javax.swing.JInternalFrame {

    Inicio ini;
    //public List<CManifiestosDeDistribucion> listaDeManifiestos;
    public List<FDespachoHieleraConIntegracion> listaDeFormulariosManifiestos;
    public List<String> listaDeConductores;

    public int indice = 0;
    public FDespachoHieleraConIntegracion fDespachoHieleraConIntegracion = null;
    public boolean cargado = false;
    Timer timer;

    /**
     * Creates new fReportemovilizadoPorConductor
     * IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @throws java.lang.Exception
     */
    public FManifestarPedidosHielera_2(Inicio ini) throws Exception {
        this.ini = ini;

        try {
            initComponents();

            /* Se inicia un integrador con TNS que va a actualizar las factras y 
            se llaman la facturas pendientes por salira ruta */
            new Thread(new HiloFmanifestarPedidosHielera_2(ini, this, "pedidosPendientes")).start();

            new Thread(new HiloFmanifestarPedidosHielera_2(ini, this, "cargarmanifiestosSinDescargar")).start();

            int tiempoDeEperaBloqueo = 1000 * 60 * 5;
            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    // timerEvent(actionEvent);
                    new Thread(new HiloFmanifestarPedidosHielera_2(ini, FManifestarPedidosHielera_2.this, "pedidosPendientes")).start();
                }

            };
            timer = new Timer(tiempoDeEperaBloqueo, actionListener);
            timer.start();
            this.moveToFront();

        } catch (Exception ex) {
            Logger.getLogger(FManifestarPedidosHielera_2.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mnuBorrarFilas = new javax.swing.JPopupMenu();
        borraElementos = new javax.swing.JMenu();
        borrar1Fila = new javax.swing.JMenuItem();
        borrarTodo = new javax.swing.JMenuItem();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListManifiestos = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        listacturasPendientes = new javax.swing.JList<>();
        lblPendientes = new javax.swing.JLabel();
        lblAgendados = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listaFacturasAgendadas = new javax.swing.JList<>();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnMinuta = new javax.swing.JToggleButton();
        jBtnNuevo1 = new javax.swing.JButton();
        jBtnBorrarFIla = new javax.swing.JToggleButton();
        jBtnObservaciones = new javax.swing.JToggleButton();
        jBtnRefrescar = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();

        borraElementos.setText("Borrar Una Fila");

        borrar1Fila.setText("Borrar Factura");
        borrar1Fila.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                borrar1FilaMouseClicked(evt);
            }
        });
        borrar1Fila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrar1FilaActionPerformed(evt);
            }
        });
        borraElementos.add(borrar1Fila);

        borrarTodo.setText("Borrar todas las facturas");
        borrarTodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarTodoActionPerformed(evt);
            }
        });
        borraElementos.add(borrarTodo);

        mnuBorrarFilas.add(borraElementos);

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Formulario para manifestar Facturas en Ruta");
        setAutoscrolls(true);
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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Formulario Gestion de Manifiestos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1131, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jListManifiestos.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Manifiestos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N
        jListManifiestos.setFont(new java.awt.Font("Ubuntu", 0, 10)); // NOI18N
        jListManifiestos.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jListManifiestos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jListManifiestos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jListManifiestosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(jListManifiestos);

        jScrollPane2.setViewportView(listacturasPendientes);

        lblPendientes.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        lblPendientes.setForeground(new java.awt.Color(249, 24, 7));
        lblPendientes.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPendientes.setText("Facturas Pendientes");

        lblAgendados.setFont(new java.awt.Font("Ubuntu", 1, 18)); // NOI18N
        lblAgendados.setForeground(new java.awt.Color(0, 24, 255));
        lblAgendados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblAgendados.setText("Facturas Agendadas");

        jScrollPane3.setViewportView(listaFacturasAgendadas);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPendientes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAgendados, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPendientes, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAgendados, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 206, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jToolBar1.setRollover(true);

        jBtnNuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        jBtnNuevo.setToolTipText("agregar registro");
        jBtnNuevo.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnNuevo.setFocusable(false);
        jBtnNuevo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnNuevo.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnNuevo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnNuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNuevoActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnNuevo);

        jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        jBtnGrabar.setToolTipText("grabar registro");
        jBtnGrabar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnGrabar.setEnabled(false);
        jBtnGrabar.setFocusable(false);
        jBtnGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnGrabar.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnGrabarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnGrabar);

        jBtnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        jBtnImprimir.setToolTipText("imprimir mfto.");
        jBtnImprimir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnImprimir.setEnabled(false);
        jBtnImprimir.setFocusable(false);
        jBtnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnImprimir.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnImprimirActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnImprimir);

        jBtnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        jBtnCancel.setToolTipText("Cancelar operacion");
        jBtnCancel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnCancel.setFocusable(false);
        jBtnCancel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnCancel.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnCancel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnCancelActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnCancel);

        jBtnMinuta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Table.png"))); // NOI18N
        jBtnMinuta.setToolTipText("Consolidado mercancia");
        jBtnMinuta.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnMinuta.setEnabled(false);
        jBtnMinuta.setFocusable(false);
        jBtnMinuta.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnMinuta.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnMinuta.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnMinuta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnMinutaActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnMinuta);

        jBtnNuevo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/table_row_insert.png"))); // NOI18N
        jBtnNuevo1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnNuevo1.setEnabled(false);
        jBtnNuevo1.setFocusable(false);
        jBtnNuevo1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnNuevo1.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnNuevo1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnNuevo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnNuevo1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnNuevo1);

        jBtnBorrarFIla.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/table_row_delete.png"))); // NOI18N
        jBtnBorrarFIla.setToolTipText("Borrar Fila");
        jBtnBorrarFIla.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnBorrarFIla.setEnabled(false);
        jBtnBorrarFIla.setFocusable(false);
        jBtnBorrarFIla.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnBorrarFIla.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnBorrarFIla.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnBorrarFIla.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnBorrarFIlaActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnBorrarFIla);

        jBtnObservaciones.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Bubble.png"))); // NOI18N
        jBtnObservaciones.setToolTipText("Borrar Fila");
        jBtnObservaciones.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnObservaciones.setEnabled(false);
        jBtnObservaciones.setFocusable(false);
        jBtnObservaciones.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnObservaciones.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnObservaciones.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnObservaciones.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnObservacionesActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnObservaciones);

        jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png"))); // NOI18N
        jBtnRefrescar.setToolTipText("Refrescar Manifiestos");
        jBtnRefrescar.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnRefrescar.setFocusable(false);
        jBtnRefrescar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnRefrescar.setPreferredSize(new java.awt.Dimension(25, 25));
        jBtnRefrescar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jBtnRefrescar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnRefrescarActionPerformed(evt);
            }
        });
        jToolBar1.add(jBtnRefrescar);

        jBtnExit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jBtnExit.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jBtnExit.setFocusable(false);
        jBtnExit.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jBtnExit.setPreferredSize(new java.awt.Dimension(25, 25));
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing

    }//GEN-LAST:event_formInternalFrameClosing


    private void borrar1FilaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrar1FilaMouseClicked


    }//GEN-LAST:event_borrar1FilaMouseClicked

    private void borrarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarTodoActionPerformed


    }//GEN-LAST:event_borrarTodoActionPerformed

    private void borrar1FilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrar1FilaActionPerformed

    }//GEN-LAST:event_borrar1FilaActionPerformed


    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed

        if (fDespachoHieleraConIntegracion != null) {
            listaDeFormulariosManifiestos.add(indice, fDespachoHieleraConIntegracion);
            fDespachoHieleraConIntegracion.setVisible(false);
        }

        fDespachoHieleraConIntegracion = new FDespachoHieleraConIntegracion(this.ini, this);
        fDespachoHieleraConIntegracion.setSize(jPanel1.getSize());
        fDespachoHieleraConIntegracion.setLocation(0, -15);
        jPanel1.add(fDespachoHieleraConIntegracion);
        fDespachoHieleraConIntegracion.setVisible(true);
        fDespachoHieleraConIntegracion.nuevo();
        fDespachoHieleraConIntegracion.moveToFront();


    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnImprimirActionPerformed

    }//GEN-LAST:event_jBtnImprimirActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed

    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void jBtnMinutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnMinutaActionPerformed

    }//GEN-LAST:event_jBtnMinutaActionPerformed

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
        salir();
    }//GEN-LAST:event_jBtnExitActionPerformed

    private void jBtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabarActionPerformed

    }//GEN-LAST:event_jBtnGrabarActionPerformed

    private void jBtnNuevo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnNuevo1ActionPerformed

    private void jBtnBorrarFIlaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnBorrarFIlaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jBtnBorrarFIlaActionPerformed

    private void jBtnObservacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnObservacionesActionPerformed

    }//GEN-LAST:event_jBtnObservacionesActionPerformed

    private void jListManifiestosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListManifiestosMouseClicked
        //  if (seleccionarFormularioManifiesto()) return;
        indice = jListManifiestos.getSelectedIndex();
        new Thread(new HiloFmanifestarPedidosHielera_2(ini, this, "seleccionarFormularioManifiesto")).start();
        jBtnNuevo.setEnabled(true);
    }//GEN-LAST:event_jListManifiestosMouseClicked


    private void jBtnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnRefrescarActionPerformed

        new Thread(new HiloFmanifestarPedidosHielera_2(ini, this, "refrescarManifiestos")).start();
        new Thread(new HiloListadoDeEmpleados(ini)).start();
        new Thread(new HiloListadoDeVehiculos(ini)).start();
        jBtnNuevo.setEnabled(true);


    }//GEN-LAST:event_jBtnRefrescarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu borraElementos;
    private javax.swing.JMenuItem borrar1Fila;
    private javax.swing.JMenuItem borrarTodo;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JToggleButton jBtnBorrarFIla;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JToggleButton jBtnGrabar;
    public javax.swing.JToggleButton jBtnImprimir;
    public javax.swing.JToggleButton jBtnMinuta;
    public javax.swing.JButton jBtnNuevo;
    private javax.swing.JButton jBtnNuevo1;
    public javax.swing.JToggleButton jBtnObservaciones;
    public javax.swing.JToggleButton jBtnRefrescar;
    public javax.swing.JList<String> jListManifiestos;
    public transient javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblAgendados;
    private javax.swing.JLabel lblPendientes;
    public javax.swing.JList<String> listaFacturasAgendadas;
    public javax.swing.JList<String> listacturasPendientes;
    private javax.swing.JPopupMenu mnuBorrarFilas;
    // End of variables declaration//GEN-END:variables

    public void salir() {
//        // SE LIBERA EL MANIFIESTO ACTUAL
//        if (manifiestoActual != null) {
//
//            manifiestoActual.liberarManifiesto(true);
//            liberado = true;
//
//        }

        // SE CIERRA LA APLICACION
        this.dispose();

    }

    public void buscarEmpleado(FDespachoHieleraConIntegracion fDespachoHieleraConIntegracion, int rol) {

        FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(fDespachoHieleraConIntegracion, rol);
        this.getParent().add(form);
        form.toFront();
        form.setClosable(true);
        form.setVisible(true);
        form.setTitle("Formulario para buscar Empleados por apellidos");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        form.setLocation((screenSize.width - form.getSize().width) / 2, (screenSize.height - form.getSize().height) / 2);
        form.txtApellidosPersona.requestFocus();
        jBtnNuevo.setEnabled(false);
        form.show();
        //}

    }

    public void buscarEmpleado(FDespachoHielera fDespachoHielera, int rol) {
        FBuscarListadoDeEmpleados form = new FBuscarListadoDeEmpleados(fDespachoHielera, rol);
        this.getParent().add(form);
        form.toFront();
        form.setClosable(true);
        form.setVisible(true);
        form.setTitle("Formulario para buscar Empleados por apellidos");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        form.setLocation((screenSize.width - form.getSize().width) / 2, (screenSize.height - form.getSize().height) / 2);
        form.txtApellidosPersona.requestFocus();
        jBtnNuevo.setEnabled(false);
        form.show();
        //}

    }
     public void msgSinConexionBBDD(String numeroDeFactura) {
        JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " no existe en servidor local, \n"
                + "Factura no se ha grabado . \n "
                + "oprimir enter nuevamente. ", "Error, factura no existe", JOptionPane.WARNING_MESSAGE);
    }

    public void msgFacturaNoExiste(String numeroDeFactura) {
        JOptionPane.showInternalMessageDialog(this, "No hay conexion a la BBDD ", "Error, factura de conexion", JOptionPane.ERROR_MESSAGE);
    }

    public void msgFacturaNoExisteTNS(String numeroDeFactura) {
        JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " no existe en servidor origen", "Error, factura no existe", JOptionPane.WARNING_MESSAGE);
    }

    public void msgEntregaTotal(String numeroDeFactura) {
        JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

    }

    public void msgDevolucionTotal(String numeroDeFactura) {
        JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " fue devuelta de ruta", "Error, factura Devuelta", JOptionPane.WARNING_MESSAGE);

    }

    public void msgEntrgaParcial(String numeroDeFactura) {
        JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " Ya fue entregada", "Error, factura Entregada", JOptionPane.WARNING_MESSAGE);

    }

    public void msgFacturaAnulada(String numeroDeFactura) {
        JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + "  fue ANULADA", "Error, factura ANULADA", JOptionPane.ERROR_MESSAGE);

    }

    public void msgFacturaNoSePuedeBorrar(String numeroDeFactura) {
        JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura + " NO se puede borrrar del Manifiesto", "Error, Factura Despachada", JOptionPane.ERROR_MESSAGE);

    }

    public void msgListaVacia() {
        JOptionPane.showInternalMessageDialog(this, "No hay facturas disponible para borrar de la tabla", "Error", JOptionPane.WARNING_MESSAGE);

    }

    public void msgFacturaEnDistribucion(String numeroDeFactura) {
        JOptionPane.showInternalMessageDialog(this, "La Factura  # " + numeroDeFactura
                + " ya se encuentra en Distribución "
                + "", "Error", JOptionPane.WARNING_MESSAGE);

    }

    public void msgSinCanastas() {
        JOptionPane.showMessageDialog(this, "El manifiesto no tiene la cantidad de Canastas en ruta", "Manifiesto incompleto", JOptionPane.ERROR_MESSAGE);
    }

    public int msgGrabarCanastas() {
        int deseaGrabarRegistro = 0;

        deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea guardar el registro en la BBDD ?", "Guardar registro", JOptionPane.YES_NO_OPTION);

        return deseaGrabarRegistro;
    }

    public int msgDeseaBorrarFila() {
        int borrrarFila = 0;

        borrrarFila = JOptionPane.showConfirmDialog(this, "Borrar el regitro de la Tabla ?", "Borrar registro", JOptionPane.YES_NO_OPTION);

        return borrrarFila;
    }

    public void msgSinInternet() {
        JOptionPane.showMessageDialog(null, "No hay conexión a internet", "Error al guardar Datos", JOptionPane.WARNING_MESSAGE, null);

    }

    public void msgErrorAlguardar(String mensaje) {
        JOptionPane.showMessageDialog(null, "Error al guardar los datos, " + mensaje, "manifiesto ya fue Cerrado", JOptionPane.ERROR_MESSAGE, null);
    }

    public void msgRutasPendentes() {
        JOptionPane.showMessageDialog(this, "El conductor tiene pendientes de ruta,\n no se puede Imprimir este manifiesto", "Conductor no esta paz y salvo", JOptionPane.ERROR_MESSAGE, null);

    }

    public void msgRutaUsadaOtroUsuario() {
        JOptionPane.showMessageDialog(this, "Esa Ruta está siendo despachada por otro usuario ", "Error", 0);

    }

    public void msgCantidadDeCanastas() {
        JOptionPane.showMessageDialog(this, "El manifiesto no tiene la cantidad de Canastas en ruta", "Manifiesto incompleto", JOptionPane.ERROR_MESSAGE);

    }

    public void msgVehiculoNoExiste() {
        JOptionPane.showMessageDialog(this, "El vehiculo no existe en la BBDD", "Alerta", JOptionPane.INFORMATION_MESSAGE);

    }

    public void msgSinDespachador() {
        JOptionPane.showMessageDialog(this, "No ha seleccionado el despachador", "Alerta", JOptionPane.INFORMATION_MESSAGE);

    }

    public void msgSinSeleccionarManifiesto() {
        JOptionPane.showMessageDialog(this, "No hay manifiesto de ruta selecccionado ", "Error", 0);

    }

    public static void main() {

    }

    private void timerEvent(java.awt.event.ActionEvent evt) {
        if (timer.isRunning()) {
            timer.stop();
            System.out.println("se detiene el timer " + new Date());
            // System.exit(0);
//            timer = new Timer(60000, actionListener);
//            System.out.println("se llama al timer");
//            timer.start();
//            System.out.println("inicia el timer");
//            System.out.println("expired en funcion");
        }
    }

}
