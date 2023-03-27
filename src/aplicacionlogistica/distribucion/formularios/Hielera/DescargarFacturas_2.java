/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.Hielera.Threads.HiloDescargarFacturas_2;
import aplicacionlogistica.distribucion.formularios.Hielera.Threads.HiloFLiquidarManifiestos;
import aplicacionlogistica.distribucion.integrador.HiloIntegradorTNSLaHielera;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

/**
 * Esta vista, permite que el usuario pueda asignar a un manifiesto de
 * Distribución el conductor, el auxiliar, un vehiculo dando una ruta y
 * clasificandolo dentro de un cnal de venta; el sistema se encrga de crear el
 * manifiestos y posteriormente se le asignan las factura que van a salir a
 * distrbucion.
 *
 * @author Luis Eduardo López
 */
public class DescargarFacturas_2 extends javax.swing.JInternalFrame {

    Inicio ini;
    //public List<CManifiestosDeDistribucion> listaDeManifiestos;
    public List<FliquidarManifiestos> listaDeFormulariosManifiestos;
    public List<String> listaDeConductores;
    public String fechaDistribucion;

    public List<CEmpleados> listaDeConductoresConManfiestos;
    public List<CManifiestosDeDistribucion> listaDeManfiestosSinDescargar;

    int indiceJList = 0;
    public FliquidarManifiestos fliquidarManifiestos = null;

    /**
     * Creates new form IngresarManifiestoDeDistribucion
     *
     * @param ini
     * @throws java.lang.Exception
     */
    public DescargarFacturas_2(Inicio ini) throws Exception {
        this.ini = ini;
        initComponents();
        new Thread(new HiloDescargarFacturas_2(ini, this, "refrescarFormularios")).start();

        new Thread(new HiloIntegradorTNSLaHielera(this.ini)).start();

    }

    public void cargarFcaturasSinDescargar() throws InterruptedException {

        if (jListManifiestos.getModel().getSize() > 1) {
            DefaultListModel listModel = (DefaultListModel) jListManifiestos.getModel();
            listModel.removeAllElements();
        }
        listaDeFormulariosManifiestos = new ArrayList<>();
        listaDeConductores = new ArrayList();

        if (fliquidarManifiestos != null) {
            fliquidarManifiestos.setVisible(false);
        }

        for (CManifiestosDeDistribucion manifiesto : ini.getListaDeManifiestossinDescargar()) {
            // this.listaDeManfiestosSinDescargar.add(manifiesto.getNombreConductor() + " " + manifiesto.getApellidosConductor()); //+ "-" + manifiesto.getVehiculo());

            fliquidarManifiestos = new FliquidarManifiestos(this.ini, this, manifiesto);

            jPanel1.add(fliquidarManifiestos);
            fliquidarManifiestos.setVisible(false);
            //fDescargarFacturasHielera.nuevo();
            fliquidarManifiestos.moveToFront();
            fliquidarManifiestos.txtNumeroDeFactura.setEnabled(true);
            fliquidarManifiestos.txtNumeroDeFactura.setEditable(true);

        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mnuBorrarFilas = new javax.swing.JPopupMenu();
        borraElementos = new javax.swing.JMenu();
        borrar1Fila = new javax.swing.JMenuItem();
        borrarTodo = new javax.swing.JMenuItem();
        jScrollPane4 = new javax.swing.JScrollPane();
        tablaDocsVencidos = new javax.swing.JTable();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListManifiestos = new javax.swing.JList<>();
        jPanel1 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnMinuta = new javax.swing.JToggleButton();
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

        tablaDocsVencidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "NOMBRES DOCUMENTO", "FECHA EXP.", "FECHA VENC.", "ARCHIVO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablaDocsVencidos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tablaDocsVencidos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaDocsVencidosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tablaDocsVencidos);
        if (tablaDocsVencidos.getColumnModel().getColumnCount() > 0) {
            tablaDocsVencidos.getColumnModel().getColumn(0).setResizable(false);
            tablaDocsVencidos.getColumnModel().getColumn(1).setResizable(false);
            tablaDocsVencidos.getColumnModel().getColumn(1).setPreferredWidth(250);
            tablaDocsVencidos.getColumnModel().getColumn(2).setResizable(false);
            tablaDocsVencidos.getColumnModel().getColumn(2).setPreferredWidth(100);
            tablaDocsVencidos.getColumnModel().getColumn(3).setPreferredWidth(100);
            tablaDocsVencidos.getColumnModel().getColumn(4).setPreferredWidth(100);
        }

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Formulario para Descargar manifiestos de Ruta");
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

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 693, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Formulario Gestion descargue de  Manifiestos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 10))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1136, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 787, Short.MAX_VALUE)
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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void tablaDocsVencidosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaDocsVencidosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tablaDocsVencidosMouseClicked


    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing

    }//GEN-LAST:event_formInternalFrameClosing


    private void borrar1FilaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_borrar1FilaMouseClicked


    }//GEN-LAST:event_borrar1FilaMouseClicked

    private void borrarTodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarTodoActionPerformed


    }//GEN-LAST:event_borrarTodoActionPerformed

    private void borrar1FilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrar1FilaActionPerformed

    }//GEN-LAST:event_borrar1FilaActionPerformed


    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed

        try {
            if (fliquidarManifiestos != null) {
                listaDeFormulariosManifiestos.add(indiceJList, fliquidarManifiestos);
                fliquidarManifiestos.setVisible(false);
            }

//            fliquidarManifiestos = new FliquidarManifiestos(this.ini, this, new CManifiestosDeDistribucion(ini));
//            jPanel1.add(fliquidarManifiestos);
//            fliquidarManifiestos.setVisible(true);
//           // fliquidarManifiestos.nuevo();
//           // new Thread(new HiloFLiquidarManifiestos(ini, fliquidarManifiestos, "nuevo")).start();
//            fliquidarManifiestos.moveToFront();
        } catch (Exception ex) {
            Logger.getLogger(DescargarFacturas_2.class.getName()).log(Level.SEVERE, null, ex);
        }


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

    private void jBtnObservacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnObservacionesActionPerformed

    }//GEN-LAST:event_jBtnObservacionesActionPerformed

    private void jListManifiestosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jListManifiestosMouseClicked
        String cadena = null;
        int indiceSeleccionado;
        if (jListManifiestos.getModel().getSize() < 1) {
            return;
        }

//         if (fliquidarManifiestos != null) {
//            listaDeFormulariosManifiestos.add(indiceJList, fliquidarManifiestos);
//           
//        }
        cadena = jListManifiestos.getSelectedValue();
        indiceSeleccionado = jListManifiestos.getSelectedIndex();
        String strMain = this.ini.getPrefijos();

        String[] arrSplit = cadena.split("-");
        String nombreConductor = arrSplit[0];
        //String placa = arrSplit[1];

        int i = 0;

        if (fliquidarManifiestos != null) {
            fliquidarManifiestos.setVisible(false);
        }

        fliquidarManifiestos = listaDeFormulariosManifiestos.get(indiceSeleccionado);
        //fliquidarManifiestos.fechaDistribucion = this.fechaDistribucion;

        jPanel1.add(fliquidarManifiestos);
        fliquidarManifiestos.setVisible(true);
        fliquidarManifiestos.filaSeleccionada=-1;
       
        fliquidarManifiestos.moveToFront();
        if (fliquidarManifiestos.manifiesto.getListaFacturasPendientesPorDescargar().isEmpty()) {
            fliquidarManifiestos.cbxMovimientoFactura.setEnabled(true);
            fliquidarManifiestos.cbxPrefijo.setEnabled(false);
            fliquidarManifiestos.txtNumeroDeFactura.setEnabled(false);
        }
        indiceJList = indiceSeleccionado;

//        for (FliquidarManifiestos fd : listaDeFormulariosManifiestos) {
//            
//            indiceJList=i;
//            
//            String cond = fd.conductorActual.getNombres() + " " + fd.conductorActual.getApellidos();
////            String veh = fd.carro.getPlaca();
//            if ((fd.conductorActual.getNombres() + " " + fd.conductorActual.getApellidos()).equals(nombreConductor)) {
//
//              
//                if (fliquidarManifiestos != null) {
//                    fliquidarManifiestos.setVisible(false);
//                }
//
//                fliquidarManifiestos = fd;
//                //  FpnlSalidaRutaLaHielera fpnlSalidaRutaLaHielera = new FpnlSalidaRutaLaHielera();
//                jPanel1.add(fliquidarManifiestos);
//                fliquidarManifiestos.setVisible(true);
//                fliquidarManifiestos.moveToFront();
//                
////                if(fliquidarManifiestos.conductorActual.getKmRecorrido()==0){
////                    fliquidarManifiestos.txtKilometrosEntrada.setEnabled(true);
////                    fliquidarManifiestos.txtKilometrosEntrada.requestFocus();
////                }else{
////                fliquidarManifiestos.cbxMovimientoFactura.setEnabled(true);
////                 fliquidarManifiestos.cbxMovimientoFactura.setEnabled(true);
////
////                break;
////            }
//
//            i++;
//        }
//        }
    }//GEN-LAST:event_jListManifiestosMouseClicked

    private void jBtnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnRefrescarActionPerformed

        //refrescarFormularios();
        new Thread(new HiloDescargarFacturas_2(ini, this, "refrescarFormularios")).start();

    }//GEN-LAST:event_jBtnRefrescarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu borraElementos;
    private javax.swing.JMenuItem borrar1Fila;
    private javax.swing.JMenuItem borrarTodo;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JToggleButton jBtnGrabar;
    public javax.swing.JToggleButton jBtnImprimir;
    public javax.swing.JToggleButton jBtnMinuta;
    public javax.swing.JButton jBtnNuevo;
    public javax.swing.JToggleButton jBtnObservaciones;
    public javax.swing.JToggleButton jBtnRefrescar;
    public javax.swing.JList<String> jListManifiestos;
    public transient javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPopupMenu mnuBorrarFilas;
    private javax.swing.JTable tablaDocsVencidos;
    // End of variables declaration//GEN-END:variables

    public void salir() {
//        // SE LIBERA EL MANIFIESTO ACTUAL
//        if (conductorActual != null) {
//
//            conductorActual.liberarManifiesto(true);
//            liberado = true;
//
//        }

        // SE CIERRA el formulario
        this.dispose();

    }

    public void grabarcien() {
        int deseaGrabar = JOptionPane.showConfirmDialog(fliquidarManifiestos.descargarFacturas_2, "Desea guardar todos los registros de una sola vez ?", "Guardar registro", JOptionPane.YES_NO_OPTION);

        /* Se valida el deseo de cerrarManifiesto los datos en la BBDD  */
        if (deseaGrabar == JOptionPane.YES_OPTION) {

            //grabarCien100();
            new Thread(new HiloFLiquidarManifiestos(ini, fliquidarManifiestos, "grabarCien100")).start();
        }

    }

    public void msgFacturasPendientes() {
        JOptionPane.showMessageDialog(this, "No se han descargado todas las facturas", "Error", JOptionPane.WARNING_MESSAGE);

    }

    public int msgDeseaCerrarManifiesto() {
        int deseaGrabar = JOptionPane.showConfirmDialog(this, "Desea Cerrar el manifiesto? \n"
                + " Una vez cerrado no se pueden hacer cambios.", "Cerrar Maifiesto", JOptionPane.YES_NO_OPTION);
        return deseaGrabar;
    }

    public void msgErrorAlGuardar(String error) {
        JOptionPane.showMessageDialog(this, "Error al guardar los datos " + error, "Error", JOptionPane.ERROR_MESSAGE, null);
    }

    public void msgSinInternet() {
        JOptionPane.showMessageDialog(this, "Error al guardar los datos, no hay Internet  ", "Error al grabar ", JOptionPane.ERROR_MESSAGE, null);

    }

    public void msgNoSePuedeGrabar() {
        JOptionPane.showMessageDialog(this, "Este manifiesto no se puede guardar . Verificar con el administrador", "Error al grabar ", JOptionPane.ERROR_MESSAGE, null);

    }

    public void msgRecogidaYaHecha(String codigoManifiesto) {
        JOptionPane.showInternalMessageDialog(this, "La Recogida aparece descargada del Mfto. N° " + codigoManifiesto, "Error", 0);

    }

    public void msgSeleccionarMovimiento() {
        JOptionPane.showInternalMessageDialog(this, "Debe escoger un movimiento para la factura digitada ", "Error", 0);

    }

    public void msgFacturasSinProductos() {
        JOptionPane.showInternalMessageDialog(this, "La factura no tiene productos asignados", "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void msgFacturaNoExiste(String manifiesto) {
        JOptionPane.showInternalMessageDialog(this, "La factura No existe en el manifiesto N° " + manifiesto, "Factura no existe en el sistema", JOptionPane.ERROR_MESSAGE);                //txtNumeroDeFactura.requestFocus();

    }

    public void msgFacturaNoAparece() {
        JOptionPane.showInternalMessageDialog(this, "La factura no aparece en el manifiesto", "Factura no visible ", 2);

    }

    public void msgPedidoNoSePuedeBorrar(String numFactura) {
        JOptionPane.showInternalMessageDialog(this, "El pedido # " + numFactura + " no se puede borrar", "Alerta al borrar factura", JOptionPane.WARNING_MESSAGE);

    }

    public void msgSistemaOcupado() {
        JOptionPane.showInternalMessageDialog(this, "El sistema está ocupado grabando los datos,\n no se puede cerrar el formulario", "Error al cerrar", JOptionPane.ERROR_MESSAGE);

    }

    public void msgKilometrajeErrado() {
        JOptionPane.showInternalMessageDialog(this, "EL KILOMETRAJE DE ENTRADA ESTA MAL TOMADO ,\n"
                + "EL RECORRIDO  NO PUEDE SER NEGATIVO", "Error", 0);
    }

    public int msgGuardarKilometraje() {
        int actualizarKilometraje = JOptionPane.showConfirmDialog(this, "El kilometraje está correcto ?", "Guardar registro", 0);
        return actualizarKilometraje;
    }

    public void msgVehiculoNoExiste() {
        JOptionPane.showInternalMessageDialog(this, "EL vehiculo no existe", "Error", 0);
    }

    public void msgDatoGuardado() {
        JOptionPane.showMessageDialog(this, "Datos Guardados correctamente", "Manifiesto Cerrado", JOptionPane.INFORMATION_MESSAGE);
    }

    public int msgGuardarRecogida() {
        int x = JOptionPane.showConfirmDialog(this, "Desea ingresar la Recogida ?", "Guardar registro", JOptionPane.YES_NO_OPTION);
        return x;

    }

    public void msgCausalDevolucion() {
        JOptionPane.showInternalMessageDialog(this, "No ha seleccionado una causal de Devolucion", "Alerta ", JOptionPane.WARNING_MESSAGE);

    }

    public int msgBorrarFila(String numFactura) {
        int borrarFila = JOptionPane.showConfirmDialog(this, "Desea eliminar la factura # " + numFactura + " de la Tabla ?", "Eliminar Fcatura # " + numFactura, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return borrarFila;
    }

    public void msgTablaVacia() {
        JOptionPane.showInternalMessageDialog(this, "La tabla de datos está vacía, no se pueden borrar filas", "Error al borrar", 0);

    }

    public int msgBorrarTodasLasFilas() {
        int borrarFila = JOptionPane.showConfirmDialog(this, "Desea eliminar los  Registro de la Tabla ?", "Eliminar Registros", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return borrarFila;
    }

    public void msgManifiestoNoSeleccionado() {
        JOptionPane.showMessageDialog(this, "No hay manifiesto de ruta selecccionado ", "Error", 0);
    }

    public void msgManifiestoDescargado() {
        JOptionPane.showInternalMessageDialog(this, "Manifiesto descargado y completo", "Manifiesto completo", JOptionPane.INFORMATION_MESSAGE);

    }

}
