/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.costumerService;

import aplicacionlogistica.distribucion.formularios.*;
import aplicacionlogistica.distribucion.formularios.Hielera.FliquidarManifiestos;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.CCausalesDeDevolucion;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Usuario
 */
public class FDescarggueDevoluciones extends javax.swing.JInternalFrame {

    Object[] fila = new Object[3];
    public DefaultTableModel modelo;
    //private PerformanceInfiniteProgressPanel panel;

    DescargarFacturas form = null;
    FliquidarManifiestos descargarFacturasHielera = null;
    public FDescargarDevoluciones dDescargarDevoluciones;
    public DefaultTableModel modelo1, modelo2;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    NumberFormat percentageFormat = NumberFormat.getPercentInstance(Locale.getDefault());
    //public CFacturasPorManifiesto form.facturaActual = null;
    public CManifiestosDeDistribucion manifiestoActual;

    // CFacturasPorManifiesto facturaDescargada;
    int filaSelleccionada, columnaselecionada;
    int valorBandera;

    //ArrayList<CFacturasPorManifiesto> arrFactPorMfto;
    // ArrayList<CFacturasDescargadas> arrFacturasDescargadas;
    Inicio ini;
    boolean nuevo = false;
    double valorFactura = 0.0;
    double nuevoValorFactura;
    double valorDescuentoFactura;
    double valorrechazoFactura;
    double valorAbono = 0.0;
    int causalRechazoFactura = 1;
    boolean validadorDeRechazo = false;
    public boolean conDescuento = false;
    int totalRechazos = 0;
    String nombreCliente;

    //CProductosPorFacturaDescargados pxfd;
    String numeroDeManifiesto;
    int cosecutivoFactura;
    String numeroDeFactura;
    String idProducto;

    double itemValorUnitario = 0.0;
    double itemValorUnitarioConIva = 0.0;
    double itemPorcentajeDescuento = 0.0;
    double itemCantidadRechazada = 0;
    double itemValorTotalItem;
    int itemCantidad;
    int itemCausalDeRechazo;
    double porcentajeDescuento = 0.0;
    
    int tipoDeMovimiento=0;

    public FDescarggueDevoluciones() {
        //this.ini = form.ini;
        initComponents();
    }

    public FDescarggueDevoluciones(CFacturasPorManifiesto facturaActual) {
        this.form.facturaActual = facturaActual;

        initComponents();

    }

    public FDescarggueDevoluciones(FDescargarDevoluciones dDescargarDevoluciones) {
        this.dDescargarDevoluciones = dDescargarDevoluciones;
        this.ini = dDescargarDevoluciones.ini;
        initComponents();

    }

    public void llenarFormulario() throws Exception {
        if (form != null) {
            this.ini = form.ini;

            try {

                if (form.isFacturaCredito) {
                    txtPorcentajeDescuento.setEditable(true);
                    txtPorcentajeDescuento.setEnabled(true);
                    txtPorcentajeDescuento.requestFocus();
                }

                //form.facturaActual.setConsecutivo(form.facturaActual.getConsecutivo());
                //form.facturaActual.setNumeroManifiesto(form.conductorActual.getNumeroManifiesto());
                form.facturaActual.setAdherenciaDescargue(form.manifiestoActual.getListaFacturasDescargadas().size() + 1);
                // form.facturaActual.setNumeroFactura(form.facturaActual.getNumeroFactura());
                form.facturaActual.setValorARecaudarFactura(form.facturaActual.getValorTotalFactura());
                form.facturaActual.setValorRechazo(0.0);
                form.facturaActual.setValorDescuento(0.0);
                form.facturaActual.setValorRecaudado(0.0);
                form.facturaActual.setIdTipoDeMovimiento(4);
                form.facturaActual.setCausalDeRechazo(1);

                llenarTablaProductosPorFactura();
                ocultarColumnaCero();

            } catch (Exception ex) {
                Logger.getLogger(FDescarggueDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (dDescargarDevoluciones != null) {
            this.ini = dDescargarDevoluciones.ini;

//            lblFactura.setText("Factura # " + form.facturaActual.getNumeroFactura());

            try {

                if (dDescargarDevoluciones.isFacturaCredito) {
                    txtPorcentajeDescuento.setEditable(true);
                    txtPorcentajeDescuento.setEnabled(true);
                    txtPorcentajeDescuento.requestFocus();
                }

                // form.facturaActual.setConsecutivo(dDescargarDevoluciones.form.facturaActual.getConsecutivo());
                //form.facturaActual.setNumeroManifiesto(dDescargarDevoluciones.conductorActual.getNumeroManifiesto());
                form.facturaActual.setAdherenciaDescargue(dDescargarDevoluciones.listaFacturasDescargadas.size() + 1);
                // form.facturaActual.setNumeroFactura(form.facturaActual.getNumeroFactura());
                form.facturaActual.setValorARecaudarFactura(form.facturaActual.getValorTotalFactura());
                form.facturaActual.setValorRechazo(0.0);
                form.facturaActual.setValorDescuento(0.0);
                form.facturaActual.setValorRecaudado(0.0);
                form.facturaActual.setIdTipoDeMovimiento(1);
                form.facturaActual.setCausalDeRechazo(1);

                llenarTablaProductosPorFactura();
                ocultarColumnaCero();

            } catch (Exception ex) {
                Logger.getLogger(FDescarggueDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public FDescarggueDevoluciones(Inicio ini, JInternalFrame form) {
        

    }

    /**
     * Método constructor cuando se está descargando las facturas del
     * manifiesto, en el cualse va a descargar una factura con entregas con
     * rechazos parciales ó descuento en algunos ó todos los items
     *
     *
     * @param form es el fBuscarManifiestos que invoca a éste fBuscarManifiestos para descargar
 loa rechazos parciales del sistema.
     * @param a parametro de nada, para distraer constructor
     */
    public FDescarggueDevoluciones(DescargarFacturas form, int a) {
        initComponents();
        this.form = form;
        this.ini = form.ini;

        manifiestoActual = form.manifiestoActual;
        numeroDeManifiesto = form.manifiestoActual.getNumeroManifiesto();
//        lblFactura.setText("Factura # " + form.facturaActual.getNumeroFactura());

        try {

            llenarFormulario();

            lblValorFactura.setText("Total Factura : " + nf.format(valorFactura));
        } catch (Exception ex) {
            Logger.getLogger(FDescarggueDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
     /**
     * Método constructor cuando se está descargando las facturas del
     * manifiesto, en el cualse va a descargar una factura con entregas con
     * rechazos parciales ó descuento en algunos ó todos los items
     *
     *
     * @param form es el fBuscarManifiestos que invoca a éste fBuscarManifiestos para descargar
 loa rechazos parciales del sistema.
     * @param a parametro de nada, para distraer constructor
     */
    public FDescarggueDevoluciones(FliquidarManifiestos form, int a) {
        initComponents();
        this.descargarFacturasHielera = form;
        this.ini = form.ini;

//        manifiestoActual = form.conductorActual;
//        numeroDeManifiesto = form.conductorActual.getNumeroManifiesto();
//        lblFactura.setText("Factura # " + form.facturaActual.getNumeroFactura());

        try {

            llenarFormulario();

            lblValorFactura.setText("Total Factura : " + nf.format(valorFactura));
        } catch (Exception ex) {
            Logger.getLogger(FDescarggueDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método constructor cuando se está descargando las facturas del
     * manifiesto, en el cualse va a descargar una factura con entregas con
     * rechazos parciales ó descuento en algunos ó todos los items
     *
     *
     *
     * @param form es el fBuscarManifiestos que invoca a éste fBuscarManifiestos para descargar
 loa rechazos parciales del sistema.
     */
    public FDescarggueDevoluciones(DescargarFacturas form) {

    }

    private void ocultarColumnaCero() {

        tblProductosPorFactura.getColumn(tblProductosPorFactura.getColumnName(0)).setWidth(0);
        tblProductosPorFactura.getColumn(tblProductosPorFactura.getColumnName(0)).setMinWidth(0);
        tblProductosPorFactura.getColumn(tblProductosPorFactura.getColumnName(0)).setMaxWidth(0);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblProductosPorFactura = new javax.swing.JTable();
        lblValorFactura = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        rBtnDevolucionTotal = new javax.swing.JRadioButton();
        rBtnDevolucionParcial = new javax.swing.JRadioButton();
        rBtnReenvio = new javax.swing.JRadioButton();
        rBtnNoVisitados = new javax.swing.JRadioButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        txtNumeroDeFactura1 = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        txtNumeroDeFactura2 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        txtNumeroDeFactura3 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtPorcentajeDescuento = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        cbxCausalesDevolucion = new javax.swing.JComboBox<>();
        txtNumeroDeFactura = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jToolBar1 = new javax.swing.JToolBar();
        jBtnNuevo = new javax.swing.JButton();
        jBtnGrabar = new javax.swing.JToggleButton();
        jBtnImprimir = new javax.swing.JToggleButton();
        jBtnCancel = new javax.swing.JToggleButton();
        jBtnMinuta = new javax.swing.JToggleButton();
        jBtnObservaciones = new javax.swing.JToggleButton();
        jBtnRefrescar = new javax.swing.JToggleButton();
        jBtnExit = new javax.swing.JToggleButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txt_columnaId = new javax.swing.JTextField();
        txt_columnaCodigoProducto = new javax.swing.JTextField();
        txt_columnaNombreProducto = new javax.swing.JTextField();
        txt_columnaCantidadProducto = new javax.swing.JTextField();
        txt_columnaValorUnitarioSinIva = new javax.swing.JTextField();
        txt_columnaPorcentajeDescuento = new javax.swing.JTextField();
        txt_columnaCantidadRechazos = new javax.swing.JTextField();
        txt_columnaValorTotalItem = new javax.swing.JTextField();
        btnDescargar = new javax.swing.JButton();

        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosing(evt);
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameClosed(evt);
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

        tblProductosPorFactura.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "id", "#", "Ref", "Producto", "Cant.", "Vu sin Iva", " % Dto", "# Rech.", "Valor Total", "Causal de Rechazo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
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
        tblProductosPorFactura.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblProductosPorFactura.getTableHeader().setReorderingAllowed(false);
        tblProductosPorFactura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tblProductosPorFacturaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblProductosPorFacturaFocusLost(evt);
            }
        });
        tblProductosPorFactura.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductosPorFacturaMouseClicked(evt);
            }
        });
        tblProductosPorFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblProductosPorFacturaKeyPressed(evt);
            }
        });
        jScrollPane7.setViewportView(tblProductosPorFactura);
        if (tblProductosPorFactura.getColumnModel().getColumnCount() > 0) {
            tblProductosPorFactura.getColumnModel().getColumn(0).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(0).setPreferredWidth(20);
            tblProductosPorFactura.getColumnModel().getColumn(1).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(1).setPreferredWidth(60);
            tblProductosPorFactura.getColumnModel().getColumn(2).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(2).setPreferredWidth(120);
            tblProductosPorFactura.getColumnModel().getColumn(3).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(3).setPreferredWidth(250);
            tblProductosPorFactura.getColumnModel().getColumn(4).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(4).setPreferredWidth(60);
            tblProductosPorFactura.getColumnModel().getColumn(5).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(5).setPreferredWidth(100);
            tblProductosPorFactura.getColumnModel().getColumn(6).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(6).setPreferredWidth(60);
            tblProductosPorFactura.getColumnModel().getColumn(7).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(7).setPreferredWidth(60);
            tblProductosPorFactura.getColumnModel().getColumn(8).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(8).setPreferredWidth(100);
            tblProductosPorFactura.getColumnModel().getColumn(9).setResizable(false);
            tblProductosPorFactura.getColumnModel().getColumn(9).setPreferredWidth(250);
        }

        lblValorFactura.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        lblValorFactura.setText("Total Factura : ");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tipo de Movimiento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 9))); // NOI18N

        buttonGroup1.add(rBtnDevolucionTotal);
        rBtnDevolucionTotal.setText("Devolucion Total");
        rBtnDevolucionTotal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rBtnDevolucionTotalActionPerformed(evt);
            }
        });

        buttonGroup1.add(rBtnDevolucionParcial);
        rBtnDevolucionParcial.setSelected(true);
        rBtnDevolucionParcial.setText("Deoluvion Parcial");
        rBtnDevolucionParcial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rBtnDevolucionParcialActionPerformed(evt);
            }
        });

        buttonGroup1.add(rBtnReenvio);
        rBtnReenvio.setText("Reenvio");

        buttonGroup1.add(rBtnNoVisitados);
        rBtnNoVisitados.setText("No visitados");
        rBtnNoVisitados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rBtnNoVisitadosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(rBtnDevolucionTotal)
                .addGap(18, 18, 18)
                .addComponent(rBtnDevolucionParcial)
                .addGap(18, 18, 18)
                .addComponent(rBtnReenvio)
                .addGap(18, 18, 18)
                .addComponent(rBtnNoVisitados)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rBtnDevolucionTotal)
                    .addComponent(rBtnDevolucionParcial)
                    .addComponent(rBtnReenvio)
                    .addComponent(rBtnNoVisitados)))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel43.setText("Conductor");

        txtNumeroDeFactura1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroDeFactura1FocusGained(evt);
            }
        });
        txtNumeroDeFactura1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroDeFactura1ActionPerformed(evt);
            }
        });
        txtNumeroDeFactura1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroDeFactura1KeyPressed(evt);
            }
        });

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Placa");

        txtNumeroDeFactura2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroDeFactura2FocusGained(evt);
            }
        });
        txtNumeroDeFactura2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroDeFactura2ActionPerformed(evt);
            }
        });
        txtNumeroDeFactura2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroDeFactura2KeyPressed(evt);
            }
        });

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel45.setText("Manifiesto");

        txtNumeroDeFactura3.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroDeFactura3FocusGained(evt);
            }
        });
        txtNumeroDeFactura3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroDeFactura3ActionPerformed(evt);
            }
        });
        txtNumeroDeFactura3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroDeFactura3KeyPressed(evt);
            }
        });

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("% Descuento");

        txtPorcentajeDescuento.setText("0.0");
        txtPorcentajeDescuento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPorcentajeDescuentoFocusGained(evt);
            }
        });
        txtPorcentajeDescuento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPorcentajeDescuentoActionPerformed(evt);
            }
        });
        txtPorcentajeDescuento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPorcentajeDescuentoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNumeroDeFactura2, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumeroDeFactura3, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPorcentajeDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNumeroDeFactura1, javax.swing.GroupLayout.PREFERRED_SIZE, 332, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNumeroDeFactura1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNumeroDeFactura2, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtNumeroDeFactura3, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel45, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(txtPorcentajeDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 19, Short.MAX_VALUE))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Causal Devolucion");

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel46.setText("Factura #");

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("Forma de Pago");

        cbxCausalesDevolucion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxCausalesDevolucion.setEnabled(false);

        txtNumeroDeFactura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroDeFacturaFocusGained(evt);
            }
        });
        txtNumeroDeFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroDeFacturaActionPerformed(evt);
            }
        });
        txtNumeroDeFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroDeFacturaKeyPressed(evt);
            }
        });

        jRadioButton1.setText("Credito");

        jRadioButton2.setText("Contado");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel46, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jRadioButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jRadioButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxCausalesDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, 310, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxCausalesDevolucion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNumeroDeFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1)
                    .addComponent(jRadioButton2)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
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

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        jLabel1.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        jLabel1.setLabelFor(txt_columnaId);
        jLabel1.setText("#");

        jLabel3.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        jLabel3.setLabelFor(txt_columnaCodigoProducto);
        jLabel3.setText("Ref.");

        jLabel4.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        jLabel4.setLabelFor(txt_columnaNombreProducto);
        jLabel4.setText("Descripción Producto");

        jLabel5.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        jLabel5.setLabelFor(txt_columnaCantidadProducto);
        jLabel5.setText("Cant.");

        jLabel6.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        jLabel6.setText("V. Unit sin Iva");

        jLabel7.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        jLabel7.setLabelFor(txt_columnaPorcentajeDescuento);
        jLabel7.setText("% d.");

        jLabel8.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        jLabel8.setLabelFor(txt_columnaCantidadRechazos);
        jLabel8.setText("Cant. Rech.");

        jLabel9.setFont(new java.awt.Font("Ubuntu", 1, 12)); // NOI18N
        jLabel9.setLabelFor(txt_columnaValorTotalItem);
        jLabel9.setText("Valor Item");

        txt_columnaId.setEditable(false);
        txt_columnaId.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txt_columnaId.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_columnaIdActionPerformed(evt);
            }
        });

        txt_columnaCodigoProducto.setEditable(false);
        txt_columnaCodigoProducto.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N

        txt_columnaNombreProducto.setEditable(false);
        txt_columnaNombreProducto.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txt_columnaNombreProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_columnaNombreProductoActionPerformed(evt);
            }
        });

        txt_columnaCantidadProducto.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txt_columnaCantidadProducto.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_columnaCantidadProductoFocusGained(evt);
            }
        });
        txt_columnaCantidadProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_columnaCantidadProductoActionPerformed(evt);
            }
        });
        txt_columnaCantidadProducto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_columnaCantidadProductoKeyPressed(evt);
            }
        });

        txt_columnaValorUnitarioSinIva.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txt_columnaValorUnitarioSinIva.setEnabled(false);
        txt_columnaValorUnitarioSinIva.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_columnaValorUnitarioSinIvaFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_columnaValorUnitarioSinIvaFocusLost(evt);
            }
        });
        txt_columnaValorUnitarioSinIva.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_columnaValorUnitarioSinIvaKeyPressed(evt);
            }
        });

        txt_columnaPorcentajeDescuento.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txt_columnaPorcentajeDescuento.setEnabled(false);
        txt_columnaPorcentajeDescuento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_columnaPorcentajeDescuentoFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_columnaPorcentajeDescuentoFocusLost(evt);
            }
        });
        txt_columnaPorcentajeDescuento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_columnaPorcentajeDescuentoKeyPressed(evt);
            }
        });

        txt_columnaCantidadRechazos.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txt_columnaCantidadRechazos.setEnabled(false);
        txt_columnaCantidadRechazos.setName("numerico"); // NOI18N
        txt_columnaCantidadRechazos.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_columnaCantidadRechazosFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_columnaCantidadRechazosFocusLost(evt);
            }
        });
        txt_columnaCantidadRechazos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_columnaCantidadRechazosActionPerformed(evt);
            }
        });
        txt_columnaCantidadRechazos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_columnaCantidadRechazosKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_columnaCantidadRechazosKeyReleased(evt);
            }
        });

        txt_columnaValorTotalItem.setEditable(false);
        txt_columnaValorTotalItem.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        txt_columnaValorTotalItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_columnaValorTotalItemActionPerformed(evt);
            }
        });

        btnDescargar.setText("Descargar");
        btnDescargar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDescargarActionPerformed(evt);
            }
        });
        btnDescargar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnDescargarKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDescargar)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_columnaId, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_columnaCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_columnaNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(4, 4, 4)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_columnaCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(6, 6, 6)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_columnaValorUnitarioSinIva)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_columnaPorcentajeDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_columnaCantidadRechazos, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_columnaValorTotalItem)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))))
                .addGap(65, 65, 65))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGap(4, 4, 4)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_columnaCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_columnaId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_columnaNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_columnaCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_columnaValorUnitarioSinIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_columnaPorcentajeDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_columnaCantidadRechazos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_columnaValorTotalItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnDescargar)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblValorFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane7)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(290, 290, 290))
                            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblValorFactura, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDescargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarActionPerformed

        String mensaje = "";
        double tmpValorRechazo = 0.0;

        try {
            /*Se hace una nueva liquidacion de los valores de la factura a descargar*/

            int i = 0;
            String nombrecausalrechazo;
            for (CProductosPorFactura pxf : form.facturaActual.getListaProductosPorFactura()) {
                if (pxf.getCantidadRechazadaItem() > 0) {

                    if (pxf.getNombreCausalDeRechazo().equals("NINGUNO")) {

                        nombrecausalrechazo = "" + tblProductosPorFactura.getValueAt(i, 9);

                        if (nombrecausalrechazo.equals("null") || nombrecausalrechazo.length() == 0) {

                            mensaje += "El Producto " + (String) pxf.getDescripcionProducto() + " no tiene causal de devolucion,\n";

                            JOptionPane.showInternalMessageDialog(this, mensaje, "Error, verificar las devoluciones", JOptionPane.WARNING_MESSAGE);
                            return;
                        } else {
                            for (CCausalesDeDevolucion cr : ini.getListaDeCausalesDeDevolucion()) {
                                if (cr.getNombreCausalesDeDevolucion().equals(nombrecausalrechazo)) {
                                    pxf.setCausalDeRechazo(cr.getIdCausalesDeDevolucion());
                                    pxf.setNombreCausalDeRechazo(nombrecausalrechazo);
                                    pxf.setEntregado(0);
                                    break;
                                }
                            }

                        }

                    }
                }
                i++;
            }

            i = 0;
            valorrechazoFactura = 0.0;
            valorDescuentoFactura = 0.0;
            valorFactura = 0.0;
            
            for (CProductosPorFactura pxf : form.facturaActual.getListaProductosPorFactura()) {
                /* Se define la causal de rechazo de la factura*/
                if (tmpValorRechazo <= pxf.getValorRechazoItem()) {
                    tmpValorRechazo = pxf.getValorRechazoItem();
                    causalRechazoFactura = pxf.getCausalDeRechazo();
                }

                i++;
                valorrechazoFactura += pxf.getValorRechazoItem();
                valorDescuentoFactura += pxf.getValorDescuentoItem();
                valorFactura += pxf.getValorTotalLiquidacionItem();

            }

            double valor;
            if (this.form.rBtnContado.isSelected()) {

                this.form.facturaActual.setValorRechazo(valorrechazoFactura);
                this.form.facturaActual.setValorDescuento(valorDescuentoFactura);
                this.form.facturaActual.setValorRecaudado(valorFactura);
                this.form.facturaActual.setCausalDeRechazo(causalRechazoFactura);
                this.form.facturaActual.setTipoDeDEscargue("E. N. Cn");
                this.form.facturaActual.setIdTipoDeMovimiento(4);
                this.form.facturaActual.setActivo(1);

            } else {
                this.form.facturaActual.setValorRechazo(valorrechazoFactura);
                this.form.facturaActual.setValorDescuento(valorDescuentoFactura);
                this.form.facturaActual.setValorRecaudado(0.0);
                this.form.facturaActual.setCausalDeRechazo(causalRechazoFactura);
                this.form.facturaActual.setTipoDeDEscargue("E. N. Cr");
                this.form.facturaActual.setIdTipoDeMovimiento(4);
                this.form.facturaActual.setActivo(1);

                if (this.form.facturaActual.getValorRechazo() > form.facturaActual.getValorTotalFactura()) {
                    JOptionPane.showInternalMessageDialog(this, "Hay diferencia entre el valor de la factura y la devolucion parcial\n "
                            + "", "Error, verificar las devoluciones", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }

//            this.form.descargarRechazosParciales();

            this.dispose();
            this.setVisible(false);

        } catch (Exception ex) {
            Logger.getLogger(FDescarggueDevoluciones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnDescargarActionPerformed


    private void tblProductosPorFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaMouseClicked
        //btnDescargar.setEnabled(false);

        /* Se identifica la fila seleccionada*/
        filaSelleccionada = tblProductosPorFactura.getSelectedRow();

        /* Se identifica el codigo del producto */
        idProducto = tblProductosPorFactura.getValueAt(filaSelleccionada, 2).toString();

        for (CProductosPorFactura pxf : form.facturaActual.getListaProductosPorFactura()) {
            boolean ex = false;
            if (pxf.getCodigoProducto().equals(tblProductosPorFactura.getValueAt(filaSelleccionada, 2))) {

                txt_columnaCantidadProducto.setEnabled(true);
                txt_columnaValorUnitarioSinIva.setEnabled(true);
                txt_columnaPorcentajeDescuento.setEnabled(true);
                txt_columnaCantidadRechazos.setEnabled(true);

                txt_columnaId.setText(tblProductosPorFactura.getValueAt(filaSelleccionada, 1).toString());
                txt_columnaCodigoProducto.setText(pxf.getCodigoProducto());
                txt_columnaNombreProducto.setText(pxf.getDescripcionProducto());

                txt_columnaCantidadProducto.setText("" + pxf.getCantidad());
                txt_columnaValorUnitarioSinIva.setText("" + nf.format(pxf.getValorUnitarioSinIva()));
                txt_columnaPorcentajeDescuento.setText("" + pxf.getPorcentajeDescuento() * 100);
                // txt_columnaValorTotalItem.setText("" + nf.format(form.facturaActual.getListaProductosPorFactura().get(filaSelleccionada).getValorUnitarioTotalConIva()));
                txt_columnaCantidadRechazos.setText("" + pxf.getCantidadRechazadaItem());

               
                ex = true;
            }

            if (ex) {
                break;
            }

        }


    }//GEN-LAST:event_tblProductosPorFacturaMouseClicked

    private void tblProductosPorFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaKeyPressed

    }//GEN-LAST:event_tblProductosPorFacturaKeyPressed

    private void tblProductosPorFacturaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaFocusLost

    private void tblProductosPorFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_tblProductosPorFacturaFocusGained

    private void txt_columnaCantidadProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_columnaCantidadProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_columnaCantidadProductoActionPerformed

    private void txt_columnaValorTotalItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_columnaValorTotalItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_columnaValorTotalItemActionPerformed

    private void txt_columnaNombreProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_columnaNombreProductoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_columnaNombreProductoActionPerformed

    private void txt_columnaCantidadRechazosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_columnaCantidadRechazosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_columnaCantidadRechazosActionPerformed

    private void txt_columnaValorUnitarioSinIvaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_columnaValorUnitarioSinIvaKeyPressed
        /*Evento que se dispara al oprimir la tecla Enter*/
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            double valorUnitarioItemSinIva = Double.parseDouble(txt_columnaValorUnitarioSinIva.getText().trim().replace("$", ""));

            for (CProductosPorFactura pxf : form.facturaActual.getListaProductosPorFactura()) {

                if (pxf.getCodigoProducto().equals(txt_columnaCodigoProducto.getText().trim())) {
                    /*Se actualiza el valor unitario del producto*/
                    pxf.setValorUnitarioSinIva(valorUnitarioItemSinIva);

                    /*Se calcula el valor del descuento*/
                    double valorDescuento = ((pxf.getPorcentajeDescuento()) * valorUnitarioItemSinIva);

                    valorDescuento = valorDescuento * pxf.getCantidadEntregadaItem();
                    pxf.setValorDescuentoItem(valorDescuento);

                    /*Se calcula el valor a recaudar del producto*/
                    valorDescuento = (pxf.getValorUnitarioConIva() * pxf.getCantidadEntregadaItem()) - valorDescuento;
                    pxf.setValorTotalLiquidacionItem(valorDescuento);

                    tblProductosPorFactura.setValueAt(nf.format(valorUnitarioItemSinIva), filaSelleccionada, 5);
                    tblProductosPorFactura.setValueAt("" + nf.format(valorDescuento), filaSelleccionada, 8);

                    txt_columnaValorTotalItem.setText(nf.format(pxf.getValorTotalLiquidacionItem()));


                }
            }

            double nuevoValorFactura = 0;

            for (CProductosPorFactura pxf : form.facturaActual.getListaProductosPorFactura()) {
                nuevoValorFactura += pxf.getValorTotalLiquidacionItem();
            }

            // new Thread(new HiloActualizarCantidadDeProducto(ini, form.listaDeCProductosPorFacturaDescargados.get(filaSelleccionada), nuevoValorFactura)).start();
            lblValorFactura.setText("Total Factura : " + nf.format(nuevoValorFactura));

            txt_columnaPorcentajeDescuento.requestFocus();

        }
    }//GEN-LAST:event_txt_columnaValorUnitarioSinIvaKeyPressed

    private void txt_columnaPorcentajeDescuentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_columnaPorcentajeDescuentoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

        }

    }//GEN-LAST:event_txt_columnaPorcentajeDescuentoKeyPressed

    private void pierdeFocoDescuento() {
        /* Evento que se dispara cuando oprimimos la tecla Enter*/

        try {
            int causalDeRechazo = 1;

            for (CProductosPorFactura pxf : form.facturaActual.getListaProductosPorFactura()) {
                if (idProducto == pxf.getCodigoProducto()) {

                    //calcularValorItemFactura(productoDescargado, causalDeRechazo);
                    btnDescargar.setEnabled(true);
                    txt_columnaCantidadRechazos.requestFocus();

                    break;
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(FDescarggueDevoluciones.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void txt_columnaCantidadRechazosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_columnaCantidadRechazosKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            for (CProductosPorFactura pxf : form.facturaActual.getListaProductosPorFactura()) {
                if (pxf.getCodigoProducto().equals(txt_columnaCodigoProducto.getText().trim())) {

                    pxf.setCantidadRechazadaItem(Double.parseDouble(txt_columnaCantidadRechazos.getText().trim()));

                    if (pxf.getCantidadRechazadaItem() > pxf.getCantidad()) {
                         JOptionPane.showInternalMessageDialog(this, "La cantidad devuelta no puede ser mayor a la cantidad de "
                                + "de producto de la factua", "Error en devoluciones", JOptionPane.WARNING_MESSAGE);
                        pxf.setCantidadRechazadaItem(0.0);
                        return;

                    }
                    break;

                }

            }
            liquidarItem();
        }

    }//GEN-LAST:event_txt_columnaCantidadRechazosKeyPressed

    /**
     * Método liquida y actualiza los datos respectivos de la fila seleccionada
     * de la tabla productos de la factura
     *
     */
    private void perderElFocoRechazos() throws NumberFormatException {

    }

    private void txt_columnaValorUnitarioSinIvaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_columnaValorUnitarioSinIvaFocusGained
        txt_columnaValorUnitarioSinIva.setSelectionStart(0);
        txt_columnaValorUnitarioSinIva.setSelectionEnd(txt_columnaValorUnitarioSinIva.getText().length());
    }//GEN-LAST:event_txt_columnaValorUnitarioSinIvaFocusGained

    private void txt_columnaPorcentajeDescuentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_columnaPorcentajeDescuentoFocusGained
        txt_columnaPorcentajeDescuento.setSelectionStart(0);
        txt_columnaPorcentajeDescuento.setSelectionEnd(txt_columnaPorcentajeDescuento.getText().length());
    }//GEN-LAST:event_txt_columnaPorcentajeDescuentoFocusGained

    private void txt_columnaCantidadRechazosFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_columnaCantidadRechazosFocusGained
        txt_columnaCantidadRechazos.setSelectionStart(0);
        txt_columnaCantidadRechazos.setSelectionEnd(txt_columnaValorUnitarioSinIva.getText().length());
        //valorBandera = Integer.parseInt(txt_columnaCantidadRechazos.getText().trim());
        //     valorBandera = (int) listaTemporalDeProductosDescargados.get(filaSelleccionada).getCantidadRechazada();
    }//GEN-LAST:event_txt_columnaCantidadRechazosFocusGained

    private void txt_columnaCantidadRechazosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_columnaCantidadRechazosKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_columnaCantidadRechazosKeyReleased

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened

    }//GEN-LAST:event_formInternalFrameOpened

    private void btnDescargarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnDescargarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDescargarKeyPressed

    private void txtPorcentajeDescuentoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPorcentajeDescuentoFocusGained
        txtPorcentajeDescuento.setSelectionStart(0);
        txtPorcentajeDescuento.setSelectionEnd(txtPorcentajeDescuento.getText().length());
    }//GEN-LAST:event_txtPorcentajeDescuentoFocusGained

    private void txtPorcentajeDescuentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPorcentajeDescuentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPorcentajeDescuentoActionPerformed

    private void txtPorcentajeDescuentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPorcentajeDescuentoKeyPressed

        /* Evento al oprimir la tecla enter*/
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            //calcularCredito();a
            porcentajeDescuento = (Double.parseDouble(txtPorcentajeDescuento.getText().trim())) / 100;
            llenarTabla();
        }
    }//GEN-LAST:event_txtPorcentajeDescuentoKeyPressed

    private void llenarTabla() {
        int i = 0;
        double nuevoValorFactura = 0.0;
        for (CProductosPorFactura pxf : form.facturaActual.getListaProductosPorFactura()) {
            pxf.setPorcentajeDescuento(porcentajeDescuento);

            double valorDescuento = (pxf.getValorUnitarioSinIva() * pxf.getCantidadEntregadaItem()) * (pxf.getPorcentajeDescuento());
            pxf.setValorDescuentoItem(valorDescuento);

            double cantidadEntregada = pxf.getCantidad() - pxf.getCantidadRechazadaItem();
            pxf.setCantidadEntregadaItem(cantidadEntregada);

            double valorRechaso = pxf.getCantidadRechazadaItem() * pxf.getValorUnitarioConIva();
            pxf.setValorRechazoItem(valorRechaso);

            double valorRecaudadoItem = pxf.getValorProductoXCantidad() - pxf.getValorRechazoItem() - pxf.getValorDescuentoItem();
            pxf.setValorTotalLiquidacionItem(valorRecaudadoItem);

            // pxf.setNombreCausalDeRechazo(tblProductosPorFactura.getValueAt(filaSelleccionada, 9).toString());
            for (CCausalesDeDevolucion rch : ini.getListaDeCausalesDeDevolucion()) {
                if (rch.getNombreCausalesDeDevolucion().equals(pxf.getNombreCausalDeRechazo())) {
                    pxf.setCausalDeRechazo(rch.getIdCausalesDeDevolucion());
                }
            }

            pxf.setActivo(1);
            /* Se actualiza el Jtable */

            tblProductosPorFactura.setValueAt(pxf.getConsecutivoProductoPorFactura(), i, 0);
            tblProductosPorFactura.setValueAt("" + (i + 1), i, 1);
            tblProductosPorFactura.setValueAt(pxf.getCodigoProducto(), i, 2);
            tblProductosPorFactura.setValueAt(pxf.getDescripcionProducto(), i, 3);
            tblProductosPorFactura.setValueAt(pxf.getCantidad(), i, 4);
            tblProductosPorFactura.setValueAt("" + nf.format(pxf.getValorUnitarioSinIva()), i, 5);
            tblProductosPorFactura.setValueAt((pxf.getPorcentajeDescuento() * 100) + " %", i, 6);
            tblProductosPorFactura.setValueAt(pxf.getCantidadRechazadaItem(), i, 7);
            tblProductosPorFactura.setValueAt(("" + nf.format(pxf.getValorTotalLiquidacionItem())), i, 8);
            i++;

            nuevoValorFactura += pxf.getValorTotalLiquidacionItem();
        }


        lblValorFactura.setText("Total Factura : " + nf.format(nuevoValorFactura));

    }

    private void liquidarItem() {

        double NuevoValorFactura = 0.0;
        int i = 0;
        for (CProductosPorFactura pxf : form.facturaActual.getListaProductosPorFactura()) {

            if (pxf.getCodigoProducto().equals(txt_columnaCodigoProducto.getText().trim())) {

                pxf.setPorcentajeDescuento(porcentajeDescuento);
                 
                double cantidadEntregada = pxf.getCantidad() - pxf.getCantidadRechazadaItem();
                pxf.setCantidadEntregadaItem(cantidadEntregada);

                double valorDescuento = (pxf.getValorUnitarioSinIva() * pxf.getCantidadEntregadaItem()) * ((pxf.getPorcentajeDescuento()));
                pxf.setValorDescuentoItem(valorDescuento);        

                double valorRechaso = pxf.getCantidadRechazadaItem() * pxf.getValorUnitarioConIva();
                pxf.setValorRechazoItem(valorRechaso);

                double valorRecaudadoItem = pxf.getValorProductoXCantidad() - pxf.getValorRechazoItem() - pxf.getValorDescuentoItem();
                pxf.setValorTotalLiquidacionItem(valorRecaudadoItem);

                // pxf.setNombreCausalDeRechazo(tblProductosPorFactura.getValueAt(filaSelleccionada, 9).toString());
                for (CCausalesDeDevolucion rch : ini.getListaDeCausalesDeDevolucion()) {
                    if (rch.getNombreCausalesDeDevolucion().equals(pxf.getNombreCausalDeRechazo())) {
                        pxf.setCausalDeRechazo(rch.getIdCausalesDeDevolucion());
                    }
                }

                pxf.setActivo(1);
                /* Se actualiza el Jtable*/

                tblProductosPorFactura.setValueAt(pxf.getConsecutivoProductoPorFactura(), filaSelleccionada, 0);
                tblProductosPorFactura.setValueAt("" + (filaSelleccionada + 1), filaSelleccionada, 1);
                tblProductosPorFactura.setValueAt(pxf.getCodigoProducto(), filaSelleccionada, 2);
                tblProductosPorFactura.setValueAt(pxf.getDescripcionProducto(), filaSelleccionada, 3);
                tblProductosPorFactura.setValueAt(pxf.getCantidad(), filaSelleccionada, 4);
                tblProductosPorFactura.setValueAt("" + nf.format(pxf.getValorUnitarioSinIva()), filaSelleccionada, 5);
                tblProductosPorFactura.setValueAt(pxf.getPorcentajeDescuento() + " %", filaSelleccionada, 6);
                tblProductosPorFactura.setValueAt(pxf.getCantidadRechazadaItem(), filaSelleccionada, 7);
                tblProductosPorFactura.setValueAt(("" + nf.format(pxf.getValorTotalLiquidacionItem())), filaSelleccionada, 8);

                txt_columnaValorTotalItem.setText(nf.format(pxf.getValorTotalLiquidacionItem()));

                i++;
                NuevoValorFactura += pxf.getValorTotalLiquidacionItem();
            } else {
                NuevoValorFactura += pxf.getValorTotalLiquidacionItem();
            }

        }
        lblValorFactura.setText("Total Factura : " + nf.format(NuevoValorFactura));
    }

    private void txt_columnaCantidadRechazosFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_columnaCantidadRechazosFocusLost

        perderElFocoRechazos();

    }//GEN-LAST:event_txt_columnaCantidadRechazosFocusLost

    private void txt_columnaValorUnitarioSinIvaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_columnaValorUnitarioSinIvaFocusLost
        txt_columnaPorcentajeDescuento.requestFocus();
    }//GEN-LAST:event_txt_columnaValorUnitarioSinIvaFocusLost

    private void txt_columnaPorcentajeDescuentoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_columnaPorcentajeDescuentoFocusLost
        pierdeFocoDescuento();
    }//GEN-LAST:event_txt_columnaPorcentajeDescuentoFocusLost

    private void txt_columnaCantidadProductoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_columnaCantidadProductoKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            for (CProductosPorFactura pxf : form.facturaActual.getListaProductosPorFactura()) {
                if (pxf.getCodigoProducto().equals(txt_columnaCodigoProducto.getText().trim())) {

                    pxf.setCantidadRechazadaItem(Double.parseDouble(txt_columnaCantidadRechazos.getText().trim()));

                    double cantidadEntregada = pxf.getCantidad() - pxf.getCantidadRechazadaItem();
                    pxf.setCantidadEntregadaItem(cantidadEntregada);

                    double valorRechaso = pxf.getCantidadRechazadaItem() * pxf.getValorUnitarioConIva();
                    pxf.setValorRechazoItem(valorRechaso);

                    double valorDescuento = (pxf.getValorUnitarioSinIva() * pxf.getCantidadEntregadaItem()) * (pxf.getPorcentajeDescuento());
                    pxf.setValorDescuentoItem(valorDescuento);

                    double valorRecaudadoItem = pxf.getValorProductoXCantidad() - pxf.getValorRechazoItem() - pxf.getValorDescuentoItem();
                    pxf.setValorTotalLiquidacionItem(valorRecaudadoItem);

                    pxf.setNombreCausalDeRechazo(tblProductosPorFactura.getValueAt(filaSelleccionada, 9).toString());

                    for (CCausalesDeDevolucion rch : ini.getListaDeCausalesDeDevolucion()) {
                        if (rch.getNombreCausalesDeDevolucion().equals(pxf.getNombreCausalDeRechazo())) {
                            pxf.setCausalDeRechazo(rch.getIdCausalesDeDevolucion());
                        }
                    }

                    pxf.setActivo(1);


                    /* Se actualiza el Jtable*/
                    tblProductosPorFactura.setValueAt(pxf.getCantidadEntregadaItem(), filaSelleccionada, 4);
                    tblProductosPorFactura.setValueAt("" + nf.format(pxf.getValorTotalLiquidacionItem()), filaSelleccionada, 8);

                    txt_columnaValorTotalItem.setText(nf.format(pxf.getValorTotalLiquidacionItem()));

                   

                }
            }

            for (CProductosPorFactura obj : form.facturaActual.getListaProductosPorFactura()) {
                nuevoValorFactura += obj.getValorTotalLiquidacionItem();
            }

            lblValorFactura.setText("Total Factura : " + nf.format(nuevoValorFactura));
        }

    }//GEN-LAST:event_txt_columnaCantidadProductoKeyPressed


    private void txt_columnaCantidadProductoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_columnaCantidadProductoFocusGained
        txt_columnaCantidadProducto.setSelectionStart(0);
        txt_columnaCantidadProducto.setSelectionEnd(txt_columnaValorUnitarioSinIva.getText().length());
    }//GEN-LAST:event_txt_columnaCantidadProductoFocusGained

    private void formInternalFrameClosed(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosed
        limpiar();
        if (form != null) {
            form.txtNumeroDeFactura.setEnabled(true);
        } else {
            dDescargarDevoluciones.txtNumeroDeFactura.setEnabled(true);
        }

    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        limpiar();
    }//GEN-LAST:event_formInternalFrameClosing

    private void txt_columnaIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_columnaIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_columnaIdActionPerformed

    private void rBtnDevolucionTotalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rBtnDevolucionTotalActionPerformed
      tipoDeMovimiento=3;
      cbxCausalesDevolucion.setEnabled(true);
    }//GEN-LAST:event_rBtnDevolucionTotalActionPerformed

    private void rBtnNoVisitadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rBtnNoVisitadosActionPerformed
      tipoDeMovimiento=7;
    }//GEN-LAST:event_rBtnNoVisitadosActionPerformed

    private void txtNumeroDeFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFacturaFocusGained

    private void txtNumeroDeFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFacturaActionPerformed

    private void txtNumeroDeFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeFacturaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFacturaKeyPressed

    private void txtNumeroDeFactura1FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeFactura1FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFactura1FocusGained

    private void txtNumeroDeFactura1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeFactura1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFactura1ActionPerformed

    private void txtNumeroDeFactura1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeFactura1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFactura1KeyPressed

    private void txtNumeroDeFactura2FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeFactura2FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFactura2FocusGained

    private void txtNumeroDeFactura2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeFactura2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFactura2ActionPerformed

    private void txtNumeroDeFactura2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeFactura2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFactura2KeyPressed

    private void txtNumeroDeFactura3FocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroDeFactura3FocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFactura3FocusGained

    private void txtNumeroDeFactura3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroDeFactura3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFactura3ActionPerformed

    private void txtNumeroDeFactura3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroDeFactura3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroDeFactura3KeyPressed

    private void rBtnDevolucionParcialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rBtnDevolucionParcialActionPerformed
      tipoDeMovimiento=4;
      cbxCausalesDevolucion.setEnabled(true);
    }//GEN-LAST:event_rBtnDevolucionParcialActionPerformed

    private void jBtnNuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnNuevoActionPerformed

//        try {
//            if (fliquidarManifiestos != null) {
//                listaDeFormulariosManifiestos.add(indice, fliquidarManifiestos);
//                fliquidarManifiestos.setVisible(false);
//            }
//
//            fliquidarManifiestos = new FliquidarManifiestos(this.ini, this, new CManifiestosDeDistribucion(ini));
//            jPanel1.add(fliquidarManifiestos);
//            fliquidarManifiestos.setVisible(true);
//            fliquidarManifiestos.nuevo();
//            fliquidarManifiestos.moveToFront();
//        } catch (Exception ex) {
//            Logger.getLogger(DescargarFacturas_2.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }//GEN-LAST:event_jBtnNuevoActionPerformed

    private void jBtnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnGrabarActionPerformed

    }//GEN-LAST:event_jBtnGrabarActionPerformed

    private void jBtnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnImprimirActionPerformed

    }//GEN-LAST:event_jBtnImprimirActionPerformed

    private void jBtnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnCancelActionPerformed

    }//GEN-LAST:event_jBtnCancelActionPerformed

    private void jBtnMinutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnMinutaActionPerformed

    }//GEN-LAST:event_jBtnMinutaActionPerformed

    private void jBtnObservacionesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnObservacionesActionPerformed

    }//GEN-LAST:event_jBtnObservacionesActionPerformed

    private void jBtnRefrescarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnRefrescarActionPerformed

   //     new Thread(new HiloRefrescarListadoDeManifiestosSinDescargar(ini, this)).start();

    }//GEN-LAST:event_jBtnRefrescarActionPerformed

    private void jBtnExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnExitActionPerformed
      //  salir();
    }//GEN-LAST:event_jBtnExitActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDescargar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxCausalesDevolucion;
    private javax.swing.JToggleButton jBtnCancel;
    private javax.swing.JToggleButton jBtnExit;
    public javax.swing.JToggleButton jBtnGrabar;
    public javax.swing.JToggleButton jBtnImprimir;
    public javax.swing.JToggleButton jBtnMinuta;
    public javax.swing.JButton jBtnNuevo;
    public javax.swing.JToggleButton jBtnObservaciones;
    public javax.swing.JToggleButton jBtnRefrescar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblValorFactura;
    private javax.swing.JRadioButton rBtnDevolucionParcial;
    private javax.swing.JRadioButton rBtnDevolucionTotal;
    private javax.swing.JRadioButton rBtnNoVisitados;
    private javax.swing.JRadioButton rBtnReenvio;
    public javax.swing.JTable tblProductosPorFactura;
    private javax.swing.JTextField txtNumeroDeFactura;
    private javax.swing.JTextField txtNumeroDeFactura1;
    private javax.swing.JTextField txtNumeroDeFactura2;
    private javax.swing.JTextField txtNumeroDeFactura3;
    private javax.swing.JTextField txtPorcentajeDescuento;
    private javax.swing.JTextField txt_columnaCantidadProducto;
    private javax.swing.JTextField txt_columnaCantidadRechazos;
    private javax.swing.JTextField txt_columnaCodigoProducto;
    private javax.swing.JTextField txt_columnaId;
    private javax.swing.JTextField txt_columnaNombreProducto;
    private javax.swing.JTextField txt_columnaPorcentajeDescuento;
    private javax.swing.JTextField txt_columnaValorTotalItem;
    private javax.swing.JTextField txt_columnaValorUnitarioSinIva;
    // End of variables declaration//GEN-END:variables

    private void llenarTablaProductosPorFactura() {
        modelo = (DefaultTableModel) tblProductosPorFactura.getModel();

        /*Limpia la tabla de los datos*/
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

        nuevoValorFactura = 0;

        // se nexan los productos a la tabla de productos por factura
        for (CProductosPorFactura pxf : this.form.facturaActual.getListaProductosPorFactura()) {

            int fil = tblProductosPorFactura.getRowCount();

            modelo.addRow(new Object[fil]);

            tblProductosPorFactura.setValueAt(pxf.getConsecutivoProductoPorFactura(), fil, 0);
            tblProductosPorFactura.setValueAt("" + (fil + 1), fil, 1);
            tblProductosPorFactura.setValueAt(pxf.getCodigoProducto(), fil, 2);
            tblProductosPorFactura.setValueAt(pxf.getDescripcionProducto(), fil, 3);
            tblProductosPorFactura.setValueAt(pxf.getCantidad(), fil, 4);
            tblProductosPorFactura.setValueAt("" + nf.format(pxf.getValorUnitarioSinIva()), fil, 5);
            tblProductosPorFactura.setValueAt(0.0 + " %", fil, 6);
            tblProductosPorFactura.setValueAt("" + 0.0, fil, 7);
            tblProductosPorFactura.setValueAt(("" + nf.format(pxf.getValorProductoXCantidad())), fil, 8);

            JComboBox combo = CreameCombo();
            TableColumn col = tblProductosPorFactura.getColumnModel().getColumn(9);
            //col.setMinWidth(300);
            //col.setMaxWidth(300);
            col.setResizable(false);
            col = tblProductosPorFactura.getColumnModel().getColumn(9);
            col.setCellEditor(new DefaultCellEditor(combo));//AGREGO EL COMBO AL CELLEDITOR

            pxf.setPorcentajeDescuento(0.0);
            pxf.setCantidadEntregadaItem(pxf.getCantidad());
            pxf.setCantidadRechazadaItem(0.0);
            pxf.setCausalDeRechazo(1);
            pxf.setValorDescuentoItem(0.0);
            pxf.setEntregado(1);
            pxf.setNombreCausalDeRechazo("NINGUNO");
            pxf.setPorcentajeDescuento(0.0);
            pxf.setValorTotalLiquidacionItem(pxf.getCantidadEntregadaItem() * pxf.getValorUnitarioConIva());
            pxf.setValorRechazoItem(0.0);

            nuevoValorFactura = form.facturaActual.getValorTotalFactura();

        }

        lblValorFactura.setText("Total Factura : " + nf.format(nuevoValorFactura));

    }

    private JComboBox CreameCombo() {
        //JComboBox combo = new JComboBox(new String[]{"Hector", "Maria Julia", "Daniel", "Salomon", "Pancrasia", "Prudencia", "Martin", "Daniela", "Mario"}) {
        String[] lista = new String[ini.getListaDeCausalesDeDevolucion().size()];

        int i = 0;
        for (CCausalesDeDevolucion obj : ini.getListaDeCausalesDeDevolucion()) {
            lista[i] = (obj.getNombreCausalesDeDevolucion());
            i++;
        }
        JComboBox combo = new JComboBox(lista) {
            public void updateUI() {
                super.updateUI();
                setBorder(BorderFactory.createEmptyBorder());
                setUI(new BasicComboBoxUI() {
                    @Override
                    protected JButton createArrowButton() {
                        JButton button = super.createArrowButton();
                        button.setContentAreaFilled(false);
                        button.setBorder(BorderFactory.createEmptyBorder());
                        return button;
                    }
                });
//                
//              
            }
        };
        return combo;
    }

    public void limpiar() {
        Inicio ini = null;
        nuevo = false;
        valorFactura = 0.0;
        nuevoValorFactura = 0.0;
        valorDescuentoFactura = 0.0;
        valorrechazoFactura = 0.0;
        valorAbono = 0.0;
        causalRechazoFactura = 1;
        validadorDeRechazo = false;
        conDescuento = false;
        totalRechazos = 0;
        itemValorUnitario = 0.0;
        itemValorUnitarioConIva = 0.0;
        itemPorcentajeDescuento = 0.0;
        itemCantidadRechazada = 0;
        itemValorTotalItem = 0.0;
        itemCantidad = 0;
        itemCausalDeRechazo = 0;

        txt_columnaCantidadProducto.setText("");
        txt_columnaCantidadRechazos.setText("");
        txt_columnaCodigoProducto.setText("");
        txt_columnaId.setText("");
        txt_columnaNombreProducto.setText("");
        txt_columnaPorcentajeDescuento.setText("");
        txt_columnaValorTotalItem.setText("");
        txt_columnaValorUnitarioSinIva.setText("");

        modelo = (DefaultTableModel) tblProductosPorFactura.getModel();

        /*Limpia la tabla de los datos*/
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }

    }

}
