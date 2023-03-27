/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios;

import aplicacionlogistica.distribucion.formularios.Hielera.FliquidarManifiestos;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FDescargarDevoluciones;
import aplicacionlogistica.distribucion.formularios.Hielera.Threads.HiloFLiquidarManifiestos;
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
public class FDescargarRechazosParciales extends javax.swing.JInternalFrame {

    Object[] fila = new Object[3];
    public DefaultTableModel modelo;
    //private PerformanceInfiniteProgressPanel panel;

    DescargarFacturas fDescargarFacturas = null;
    FliquidarManifiestos fLiquidarManifiestos = null;
    
    public FDescargarDevoluciones dDescargarDevoluciones;
    public DefaultTableModel modelo1, modelo2;
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    NumberFormat percentageFormat = NumberFormat.getPercentInstance(Locale.getDefault());
    //public CFacturasPorManifiesto fDescargarFacturas.facturaActual = null;
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

    public FDescargarRechazosParciales() {
        //this.ini = fDescargarFacturas.ini;
        initComponents();
    }

    public FDescargarRechazosParciales(CFacturasPorManifiesto facturaActual) {
        this.fDescargarFacturas.facturaActual = facturaActual;

        initComponents();

    }

    public FDescargarRechazosParciales(FDescargarDevoluciones dDescargarDevoluciones) {
        this.dDescargarDevoluciones = dDescargarDevoluciones;
        this.ini = dDescargarDevoluciones.ini;
        initComponents();

    }

    public void llenarFormulario() throws Exception {
        if (fDescargarFacturas != null) {
            this.ini = fDescargarFacturas.ini;

            lblFactura.setText("Factura # " + fDescargarFacturas.facturaActual.getNumeroFactura());

            try {

                if (fDescargarFacturas.isFacturaCredito) {
                    txtPorcentajeDescuento.setEditable(true);
                    txtPorcentajeDescuento.setEnabled(true);
                    txtPorcentajeDescuento.requestFocus();
                }

                //form.facturaActual.setConsecutivo(fDescargarFacturas.facturaActual.getConsecutivo());
                //form.facturaActual.setNumeroManifiesto(fDescargarFacturas.conductorActual.getNumeroManifiesto());
                fDescargarFacturas.facturaActual.setAdherenciaDescargue(fDescargarFacturas.manifiestoActual.getListaFacturasDescargadas().size() + 1);
                // fDescargarFacturas.facturaActual.setNumeroFactura(fDescargarFacturas.facturaActual.getNumeroFactura());
                fDescargarFacturas.facturaActual.setValorARecaudarFactura(fDescargarFacturas.facturaActual.getValorTotalFactura());
                fDescargarFacturas.facturaActual.setValorRechazo(0.0);
                fDescargarFacturas.facturaActual.setValorDescuento(0.0);
                fDescargarFacturas.facturaActual.setValorRecaudado(0.0);
                fDescargarFacturas.facturaActual.setIdTipoDeMovimiento(4);
                fDescargarFacturas.facturaActual.setCausalDeRechazo(1);

                llenarTablaProductosPorFactura();
                ocultarColumnaCero();

            } catch (Exception ex) {
                Logger.getLogger(FDescargarRechazosParciales.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (dDescargarDevoluciones != null) {
            this.ini = dDescargarDevoluciones.ini;

            lblFactura.setText("Factura # " + fDescargarFacturas.facturaActual.getNumeroFactura());

            try {

                if (dDescargarDevoluciones.isFacturaCredito) {
                    txtPorcentajeDescuento.setEditable(true);
                    txtPorcentajeDescuento.setEnabled(true);
                    txtPorcentajeDescuento.requestFocus();
                }

                // fDescargarFacturas.facturaActual.setConsecutivo(dDescargarDevoluciones.fDescargarFacturas.facturaActual.getConsecutivo());
                //form.facturaActual.setNumeroManifiesto(dDescargarDevoluciones.conductorActual.getNumeroManifiesto());
                fDescargarFacturas.facturaActual.setAdherenciaDescargue(dDescargarDevoluciones.listaFacturasDescargadas.size() + 1);
                // fDescargarFacturas.facturaActual.setNumeroFactura(fDescargarFacturas.facturaActual.getNumeroFactura());
                fDescargarFacturas.facturaActual.setValorARecaudarFactura(fDescargarFacturas.facturaActual.getValorTotalFactura());
                fDescargarFacturas.facturaActual.setValorRechazo(0.0);
                fDescargarFacturas.facturaActual.setValorDescuento(0.0);
                fDescargarFacturas.facturaActual.setValorRecaudado(0.0);
                fDescargarFacturas.facturaActual.setIdTipoDeMovimiento(1);
                fDescargarFacturas.facturaActual.setCausalDeRechazo(1);

                llenarTablaProductosPorFactura();
                ocultarColumnaCero();

            } catch (Exception ex) {
                Logger.getLogger(FDescargarRechazosParciales.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
     public void llenarFormulario2() throws Exception {
        if (fLiquidarManifiestos != null) {
            this.ini = fLiquidarManifiestos.ini;

            lblFactura.setText("Factura # " + fLiquidarManifiestos.facturaActual.getNumeroFactura());

            try {

                if (fLiquidarManifiestos.isFacturaCredito) {
                    txtPorcentajeDescuento.setEditable(true);
                    txtPorcentajeDescuento.setEnabled(true);
                    txtPorcentajeDescuento.requestFocus();
                }

                //form.facturaActual.setConsecutivo(fDescargarFacturas.facturaActual.getConsecutivo());
                //form.facturaActual.setNumeroManifiesto(fDescargarFacturas.conductorActual.getNumeroManifiesto());
                fLiquidarManifiestos.facturaActual.setAdherenciaDescargue(fLiquidarManifiestos.manifiesto.getListaFacturasDescargadas().size() + 1);
                // fDescargarFacturas.facturaActual.setNumeroFactura(fDescargarFacturas.facturaActual.getNumeroFactura());
                fLiquidarManifiestos.facturaActual.setValorARecaudarFactura(fLiquidarManifiestos.facturaActual.getValorTotalFactura());
                fLiquidarManifiestos.facturaActual.setValorRechazo(0.0);
                fLiquidarManifiestos.facturaActual.setValorDescuento(0.0);
                fLiquidarManifiestos.facturaActual.setValorRecaudado(0.0);
                fLiquidarManifiestos.facturaActual.setIdTipoDeMovimiento(4);
                fLiquidarManifiestos.facturaActual.setCausalDeRechazo(1);
                fLiquidarManifiestos.facturaActual.setListaProductosPorFactura(true);

                llenarTablaProductosPorFactura2();
                ocultarColumnaCero();

            } catch (Exception ex) {
                Logger.getLogger(FDescargarRechazosParciales.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

       
    }

    public FDescargarRechazosParciales(Inicio ini, JInternalFrame form) {
        

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
    public FDescargarRechazosParciales(DescargarFacturas form, int a) {
        initComponents();
        this.fDescargarFacturas = form;
        this.ini = form.ini;

        manifiestoActual = form.manifiestoActual;
        numeroDeManifiesto = form.manifiestoActual.getNumeroManifiesto();
        lblFactura.setText("Factura # " + form.facturaActual.getNumeroFactura());

        try {

            llenarFormulario();

            lblValorFactura.setText("Total Factura : " + nf.format(valorFactura));
        } catch (Exception ex) {
            Logger.getLogger(FDescargarRechazosParciales.class.getName()).log(Level.SEVERE, null, ex);
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
    public FDescargarRechazosParciales(FliquidarManifiestos form, int a) {
        initComponents();
        this.fLiquidarManifiestos = form;
        this.ini = form.ini;

       // manifiestoActual = form.conductorActual;
        //numeroDeManifiesto = form.conductorActual.getNumeroManifiesto();
        lblFactura.setText("Factura # " + form.facturaActual.getNumeroFactura());

        try {

            llenarFormulario2();

            lblValorFactura.setText("Total Factura : " + nf.format(valorFactura));
        } catch (Exception ex) {
            Logger.getLogger(FDescargarRechazosParciales.class.getName()).log(Level.SEVERE, null, ex);
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
    public FDescargarRechazosParciales(DescargarFacturas form) {

         initComponents();
        this.fDescargarFacturas = form;
        this.ini = form.ini;

        manifiestoActual = form.manifiestoActual;
        numeroDeManifiesto = form.manifiestoActual.getNumeroManifiesto();
        lblFactura.setText("Factura # " + form.facturaActual.getNumeroFactura());

        try {

            llenarFormulario();

            lblValorFactura.setText("Total Factura : " + nf.format(valorFactura));
        } catch (Exception ex) {
            Logger.getLogger(FDescargarRechazosParciales.class.getName()).log(Level.SEVERE, null, ex);
        }
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

        jPanel1 = new javax.swing.JPanel();
        btnDescargar = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        tblProductosPorFactura = new javax.swing.JTable();
        lblValorFactura = new javax.swing.JLabel();
        txt_columnaId = new javax.swing.JTextField();
        txt_columnaCodigoProducto = new javax.swing.JTextField();
        txt_columnaNombreProducto = new javax.swing.JTextField();
        txt_columnaValorUnitarioSinIva = new javax.swing.JTextField();
        txt_columnaCantidadProducto = new javax.swing.JTextField();
        txt_columnaPorcentajeDescuento = new javax.swing.JTextField();
        txt_columnaCantidadRechazos = new javax.swing.JTextField();
        txt_columnaValorTotalItem = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        lblValorUnitarioConIva = new javax.swing.JLabel();
        lblValorUnitarioSinIva = new javax.swing.JLabel();
        lblporcentajeDescuento = new javax.swing.JLabel();
        lblcantidadEntregada = new javax.swing.JLabel();
        lblcantidadRechazada = new javax.swing.JLabel();
        lblvalorDescuento = new javax.swing.JLabel();
        lblvalorRechazo = new javax.swing.JLabel();
        lblvalorItem = new javax.swing.JLabel();
        lbl01 = new javax.swing.JLabel();
        lbl2 = new javax.swing.JLabel();
        lbl3 = new javax.swing.JLabel();
        lbl4 = new javax.swing.JLabel();
        lbl5 = new javax.swing.JLabel();
        lbl6 = new javax.swing.JLabel();
        lbl7 = new javax.swing.JLabel();
        lbl8 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        lblFactura = new javax.swing.JLabel();
        txtPorcentajeDescuento = new javax.swing.JTextField();

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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        lblValorUnitarioConIva.setText("valorUnitarioConIva :");

        lblValorUnitarioSinIva.setText("valorUnitarioSinIva :");

        lblporcentajeDescuento.setText("PorcentajeDescuento: ");

        lblcantidadEntregada.setText("cantidadEntregada:");

        lblcantidadRechazada.setText("cantidadRechazada:");

        lblvalorDescuento.setText("Valor descuento");

        lblvalorRechazo.setText("Valor rechazo");

        lblvalorItem.setText("valor total item");

        lbl01.setText("$");

        lbl2.setText("$");

        lbl3.setText("$");

        lbl4.setText("$");

        lbl5.setText("$");

        lbl6.setText("$");

        lbl7.setText("$");

        lbl8.setText("$");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblValorUnitarioConIva)
                    .addComponent(lblValorUnitarioSinIva)
                    .addComponent(lblporcentajeDescuento)
                    .addComponent(lblcantidadEntregada)
                    .addComponent(lblcantidadRechazada)
                    .addComponent(lblvalorDescuento)
                    .addComponent(lblvalorRechazo)
                    .addComponent(lblvalorItem))
                .addGap(25, 25, 25)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl01, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblValorUnitarioConIva)
                    .addComponent(lbl01, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblValorUnitarioSinIva)
                    .addComponent(lbl2, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblporcentajeDescuento)
                    .addComponent(lbl3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblcantidadEntregada)
                    .addComponent(lbl4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblcantidadRechazada)
                    .addComponent(lbl5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblvalorDescuento)
                    .addComponent(lbl6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblvalorRechazo)
                    .addComponent(lbl7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblvalorItem)
                    .addComponent(lbl8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setLabelFor(txt_columnaId);
        jLabel1.setText("#");

        jLabel3.setLabelFor(txt_columnaCodigoProducto);
        jLabel3.setText("Ref.");

        jLabel4.setLabelFor(txt_columnaNombreProducto);
        jLabel4.setText("Descripción Producto");

        jLabel5.setLabelFor(txt_columnaCantidadProducto);
        jLabel5.setText("Cant.");

        jLabel6.setLabelFor(lblValorUnitarioSinIva);
        jLabel6.setText("V. Unit sin Iva");

        jLabel7.setLabelFor(txt_columnaPorcentajeDescuento);
        jLabel7.setText("% d.");

        jLabel8.setLabelFor(txt_columnaCantidadRechazos);
        jLabel8.setText("Cant. Rech.");

        jLabel9.setLabelFor(txt_columnaValorTotalItem);
        jLabel9.setText("Valor Item");

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("% Descuento");

        lblFactura.setFont(new java.awt.Font("DejaVu Sans", 1, 18)); // NOI18N
        lblFactura.setText(".");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1090, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtPorcentajeDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblFactura, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_columnaId, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_columnaCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_columnaNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_columnaCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(215, 215, 215)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_columnaValorUnitarioSinIva, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txt_columnaPorcentajeDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(txt_columnaCantidadRechazos, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txt_columnaValorTotalItem, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addComponent(btnDescargar))
                            .addComponent(lblValorFactura))))
                .addGap(13, 13, 13))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41)
                    .addComponent(lblFactura)
                    .addComponent(txtPorcentajeDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGap(2, 2, 2)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_columnaCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_columnaId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_columnaNombreProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_columnaCantidadProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_columnaValorUnitarioSinIva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_columnaPorcentajeDescuento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_columnaCantidadRechazos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_columnaValorTotalItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDescargar))
                .addGap(9, 9, 9)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblValorFactura))
                .addContainerGap(18, Short.MAX_VALUE))
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
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDescargarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDescargarActionPerformed

        if (fDescargarFacturas != null) {
            decargar1();
            return;
        }
        
         if (fLiquidarManifiestos != null) {
            decargar2();
            return;
        }
    }//GEN-LAST:event_btnDescargarActionPerformed

    public boolean decargar1() {
        String mensaje = "";
        double tmpValorRechazo = 0.0;
        try {
            /*Se hace una nueva liquidacion de los valores de la factura a descargar*/
            
            int i = 0;
            String nombrecausalrechazo;
            for (CProductosPorFactura pxf : fDescargarFacturas.facturaActual.getListaProductosPorFactura()) {
                if (pxf.getCantidadRechazadaItem() > 0) {
                    if (pxf.getNombreCausalDeRechazo().equals("NINGUNO")) {
                        nombrecausalrechazo = "" + tblProductosPorFactura.getValueAt(i, 9);
                        if (nombrecausalrechazo.equals("null") || nombrecausalrechazo.length() == 0) {
                            mensaje += "El Producto " + (String) pxf.getDescripcionProducto() + " no tiene causal de devolucion,\n";
                            JOptionPane.showInternalMessageDialog(this, mensaje, "Error, verificar las devoluciones", JOptionPane.WARNING_MESSAGE);
                            return true;
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
            for (CProductosPorFactura pxf : fDescargarFacturas.facturaActual.getListaProductosPorFactura()) {
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
            if (this.fDescargarFacturas.rBtnContado.isSelected()) {
                this.fDescargarFacturas.facturaActual.setValorRechazo(valorrechazoFactura);
                this.fDescargarFacturas.facturaActual.setValorDescuento(valorDescuentoFactura);
                this.fDescargarFacturas.facturaActual.setValorRecaudado(valorFactura);
                this.fDescargarFacturas.facturaActual.setCausalDeRechazo(causalRechazoFactura);
                this.fDescargarFacturas.facturaActual.setTipoDeDEscargue("E. N. Cn");
                this.fDescargarFacturas.facturaActual.setIdTipoDeMovimiento(4);
                this.fDescargarFacturas.facturaActual.setActivo(1);
            } else {
                this.fDescargarFacturas.facturaActual.setValorRechazo(valorrechazoFactura);
                this.fDescargarFacturas.facturaActual.setValorDescuento(valorDescuentoFactura);
                this.fDescargarFacturas.facturaActual.setValorRecaudado(0.0);
                this.fDescargarFacturas.facturaActual.setCausalDeRechazo(causalRechazoFactura);
                this.fDescargarFacturas.facturaActual.setTipoDeDEscargue("E. N. Cr");
                this.fDescargarFacturas.facturaActual.setIdTipoDeMovimiento(4);
                this.fDescargarFacturas.facturaActual.setActivo(1);
                if (this.fDescargarFacturas.facturaActual.getValorRechazo() > fDescargarFacturas.facturaActual.getValorTotalFactura()) {
                    JOptionPane.showInternalMessageDialog(this, "Hay diferencia entre el valor de la factura y la devolucion parcial\n "
                            + "", "Error, verificar las devoluciones", JOptionPane.WARNING_MESSAGE);
                    return true;
                }
            }
            this.fDescargarFacturas.descargarRechazosParciales();
            this.dispose();
            this.setVisible(false);
        }catch (Exception ex) {
            Logger.getLogger(FDescargarRechazosParciales.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
      
    public boolean decargar2() {
        String mensaje = "";
        double tmpValorRechazo = 0.0;
        try {
            /*Se hace una nueva liquidacion de los valores de la factura a descargar*/
            
            int i = 0;
            String nombrecausalrechazo;
            for (CProductosPorFactura pxf : fLiquidarManifiestos.facturaActual.getListaProductosPorFactura()) {
                if (pxf.getCantidadRechazadaItem() > 0) {
                    if (pxf.getNombreCausalDeRechazo().equals("NINGUNO")) {
                        nombrecausalrechazo = "" + tblProductosPorFactura.getValueAt(i, 9);
                        if (nombrecausalrechazo.equals("null") || nombrecausalrechazo.length() == 0) {
                            mensaje += "El Producto " + (String) pxf.getDescripcionProducto() + " no tiene causal de devolucion,\n";
                            JOptionPane.showInternalMessageDialog(this, mensaje, "Error, verificar las devoluciones", JOptionPane.WARNING_MESSAGE);
                            return true;
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
            for (CProductosPorFactura pxf : fLiquidarManifiestos.facturaActual.getListaProductosPorFactura()) {
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
            if (this.fLiquidarManifiestos.rBtnContado.isSelected()) {
                this.fLiquidarManifiestos.facturaActual.setAdherenciaDescargue(this.fLiquidarManifiestos.manifiesto.getListaFacturasDescargadas().size() + 1);
                this.fLiquidarManifiestos.facturaActual.setValorRechazo(valorrechazoFactura);
                this.fLiquidarManifiestos.facturaActual.setValorDescuento(valorDescuentoFactura);
                this.fLiquidarManifiestos.facturaActual.setValorRecaudado(valorFactura);
                this.fLiquidarManifiestos.facturaActual.setCausalDeRechazo(causalRechazoFactura);
                this.fLiquidarManifiestos.facturaActual.setTipoDeDEscargue("E. N. Cn");
                this.fLiquidarManifiestos.facturaActual.setIdTipoDeMovimiento(4);
                this.fLiquidarManifiestos.facturaActual.setActivo(1);
            } else {
                this.fLiquidarManifiestos.facturaActual.setAdherenciaDescargue(this.fLiquidarManifiestos.manifiesto.getListaFacturasDescargadas().size() + 1);
                this.fLiquidarManifiestos.facturaActual.setValorRechazo(valorrechazoFactura);
                this.fLiquidarManifiestos.facturaActual.setValorDescuento(valorDescuentoFactura);
                this.fLiquidarManifiestos.facturaActual.setValorRecaudado(0.0);
                this.fLiquidarManifiestos.facturaActual.setCausalDeRechazo(causalRechazoFactura);
                this.fLiquidarManifiestos.facturaActual.setTipoDeDEscargue("E. N. Cr");
                this.fLiquidarManifiestos.facturaActual.setIdTipoDeMovimiento(4);
                this.fLiquidarManifiestos.facturaActual.setActivo(1);
                if (this.fLiquidarManifiestos.facturaActual.getValorRechazo() > fLiquidarManifiestos.facturaActual.getValorTotalFactura()) {
                    JOptionPane.showInternalMessageDialog(this, "Hay diferencia entre el valor de la factura y la devolucion parcial\n "
                            + "", "Error, verificar las devoluciones", JOptionPane.WARNING_MESSAGE);
                    return true;
                }
            }
           
            if (this.fLiquidarManifiestos.facturaActual.grabarFacturasPoManifiestoDescargada()) {
               
                //this.fLiquidarManifiestos.descargarRechazosParciales();
                new Thread(new HiloFLiquidarManifiestos(ini, fLiquidarManifiestos, "descargarRechazosParciales")).start();
               
                this.dispose();
                this.setVisible(false);
            }else{
                JOptionPane.showInternalMessageDialog(this, "Error al guardar la informacio\n "
                            + "", "Error al guardar", JOptionPane.ERROR_MESSAGE);
            };
 
            
           
        }catch (Exception ex) {
            Logger.getLogger(FDescargarRechazosParciales.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }


    private void tblProductosPorFacturaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductosPorFacturaMouseClicked
        if (fDescargarFacturas != null) {
            seleccionarFila1();
        }
        if (fLiquidarManifiestos != null) {
            seleccionarFila2();
        } 


    }//GEN-LAST:event_tblProductosPorFacturaMouseClicked

    public void seleccionarFila1() {
        //btnDescargar.setEnabled(false);
        
        /* Se identifica la fila seleccionada*/
        filaSelleccionada = tblProductosPorFactura.getSelectedRow();
        
        /* Se identifica el codigo del producto */
        idProducto = tblProductosPorFactura.getValueAt(filaSelleccionada, 2).toString();
        
        for (CProductosPorFactura pxf : fDescargarFacturas.facturaActual.getListaProductosPorFactura()) {
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
                // txt_columnaValorTotalItem.setText("" + nf.format(fDescargarFacturas.facturaActual.getListaProductosPorFactura().get(filaSelleccionada).getValorUnitarioTotalConIva()));
                txt_columnaCantidadRechazos.setText("" + pxf.getCantidadRechazadaItem());
                
                lbl01.setText(nf.format(pxf.getValorUnitarioConIva()));
                lbl2.setText(nf.format(pxf.getValorUnitarioSinIva()));
                lbl3.setText("" + pxf.getPorcentajeDescuento() * 100 + " %");
                lbl4.setText("" + pxf.getCantidadEntregadaItem());
                lbl5.setText("" + pxf.getCantidadRechazadaItem());
                lbl6.setText("" + nf.format(pxf.getValorDescuentoItem()));
                lbl7.setText("" + nf.format(pxf.getValorRechazoItem()));
                lbl8.setText("" + nf.format(pxf.getValorTotalLiquidacionItem()));
                ex = true;
            }
            
            if (ex) {
                break;
            }
            
        }
    }
    
     public void seleccionarFila2() {
        //btnDescargar.setEnabled(false);
        
        /* Se identifica la fila seleccionada*/
        filaSelleccionada = tblProductosPorFactura.getSelectedRow();
        
        /* Se identifica el codigo del producto */
        idProducto = tblProductosPorFactura.getValueAt(filaSelleccionada, 2).toString();
        
        for (CProductosPorFactura pxf : fLiquidarManifiestos.facturaActual.getListaProductosPorFactura()) {
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
                // txt_columnaValorTotalItem.setText("" + nf.format(fDescargarFacturas.facturaActual.getListaProductosPorFactura().get(filaSelleccionada).getValorUnitarioTotalConIva()));
                txt_columnaCantidadRechazos.setText("" + pxf.getCantidadRechazadaItem());
                
                lbl01.setText(nf.format(pxf.getValorUnitarioConIva()));
                lbl2.setText(nf.format(pxf.getValorUnitarioSinIva()));
                lbl3.setText("" + pxf.getPorcentajeDescuento() * 100 + " %");
                lbl4.setText("" + pxf.getCantidadEntregadaItem());
                lbl5.setText("" + pxf.getCantidadRechazadaItem());
                lbl6.setText("" + nf.format(pxf.getValorDescuentoItem()));
                lbl7.setText("" + nf.format(pxf.getValorRechazoItem()));
                lbl8.setText("" + nf.format(pxf.getValorTotalLiquidacionItem()));
                ex = true;
            }
            
            if (ex) {
                break;
            }
            
        }
    }

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

            for (CProductosPorFactura pxf : fDescargarFacturas.facturaActual.getListaProductosPorFactura()) {

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

                    lblValorUnitarioConIva.setText("Valor Unitario con iva " + nf.format(pxf.getValorUnitarioConIva()));
                    lblValorUnitarioSinIva.setText("Valor Unitario sin iva " + nf.format(pxf.getValorUnitarioSinIva()));
                    lblporcentajeDescuento.setText("PorcentajeDescuento: " + (pxf.getPorcentajeDescuento() * 100) + " %");
                    lblcantidadEntregada.setText("Cantidad entregada " + pxf.getCantidadEntregadaItem());
                    lblcantidadRechazada.setText("Cantidad Devuelta " + pxf.getCantidadRechazadaItem());
                    lblvalorDescuento.setText("ValorDescuento " + nf.format(pxf.getValorDescuentoItem()));
                    lblvalorRechazo.setText("ValorDevolucion " + nf.format(pxf.getValorRechazoItem()));
                    lblvalorItem.setText("Valor total item " + nf.format(pxf.getValorTotalLiquidacionItem()));

                }
            }

            double nuevoValorFactura = 0;

            for (CProductosPorFactura pxf : fDescargarFacturas.facturaActual.getListaProductosPorFactura()) {
                nuevoValorFactura += pxf.getValorTotalLiquidacionItem();
            }

            // new Thread(new HiloActualizarCantidadDeProducto(ini, fDescargarFacturas.listaDeCProductosPorFacturaDescargados.get(filaSelleccionada), nuevoValorFactura)).start();
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

            for (CProductosPorFactura pxf : fDescargarFacturas.facturaActual.getListaProductosPorFactura()) {
                if (idProducto == pxf.getCodigoProducto()) {

                    //calcularValorItemFactura(productoDescargado, causalDeRechazo);
                    btnDescargar.setEnabled(true);
                    txt_columnaCantidadRechazos.requestFocus();

                    break;
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(FDescargarRechazosParciales.class
                    .getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void txt_columnaCantidadRechazosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_columnaCantidadRechazosKeyPressed

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {

            if(fDescargarFacturas != null){
                liquidaritem1();
            }
            
            if(fLiquidarManifiestos != null){
                liquidaritem2();
            }
          
        }

    }//GEN-LAST:event_txt_columnaCantidadRechazosKeyPressed

    public boolean liquidaritem1() throws NumberFormatException {
        for (CProductosPorFactura pxf : fDescargarFacturas.facturaActual.getListaProductosPorFactura()) {
            if (pxf.getCodigoProducto().equals(txt_columnaCodigoProducto.getText().trim())) {
                pxf.setCantidadRechazadaItem(Double.parseDouble(txt_columnaCantidadRechazos.getText().trim()));
                if (pxf.getCantidadRechazadaItem() > pxf.getCantidad()) {
                    JOptionPane.showInternalMessageDialog(this, "La cantidad devuelta no puede ser mayor a la cantidad de "
                            + "de producto de la factua", "Error en devoluciones", JOptionPane.WARNING_MESSAGE);
                    pxf.setCantidadRechazadaItem(0.0);
                    return true;
                }
                break;
            }
        }
        calculauFila1();
        return false;
    }

    public boolean liquidaritem2() throws NumberFormatException {
        for (CProductosPorFactura pxf : fLiquidarManifiestos.facturaActual.getListaProductosPorFactura()) {
            if (pxf.getCodigoProducto().equals(txt_columnaCodigoProducto.getText().trim())) {
                pxf.setCantidadRechazadaItem(Double.parseDouble(txt_columnaCantidadRechazos.getText().trim()));
                if (pxf.getCantidadRechazadaItem() > pxf.getCantidad()) {
                    JOptionPane.showInternalMessageDialog(this, "La cantidad devuelta no puede ser mayor a la cantidad de "
                            + "de producto de la factura", "Error en devoluciones", JOptionPane.WARNING_MESSAGE);
                    pxf.setCantidadRechazadaItem(0.0);
                    return true;
                }
                break;
            }
        }
        calculauFila2();
        return false;
    }
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
        for (CProductosPorFactura pxf : fDescargarFacturas.facturaActual.getListaProductosPorFactura()) {
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

        lbl01.setText("$ 0.0");
        lbl2.setText("$0.0");
        lbl3.setText((porcentajeDescuento * 100) + " %");
        lbl4.setText("0.0");
        lbl5.setText("0.0");
        lbl6.setText("$0.0");
        lbl7.setText("$0.0");
        lbl8.setText("$0.0");

        lblValorFactura.setText("Total Factura : " + nf.format(nuevoValorFactura));

    }

    private void calculauFila1() {

        double NuevoValorFactura = 0.0;
        int i = 0;
        for (CProductosPorFactura pxf : fDescargarFacturas.facturaActual.getListaProductosPorFactura()) {

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

                lbl01.setText(nf.format(pxf.getValorUnitarioConIva()));
                lbl2.setText(nf.format(pxf.getValorUnitarioSinIva()));
                lbl3.setText("" + pxf.getPorcentajeDescuento() * 100 + " %");
                lbl4.setText("" + pxf.getCantidadEntregadaItem());
                lbl5.setText("" + pxf.getCantidadRechazadaItem());
                lbl6.setText("" + nf.format(pxf.getValorDescuentoItem()));
                lbl7.setText("" + nf.format(pxf.getValorRechazoItem()));
                lbl8.setText("" + nf.format(pxf.getValorTotalLiquidacionItem()));
                i++;
                NuevoValorFactura += pxf.getValorTotalLiquidacionItem();
            } else {
                NuevoValorFactura += pxf.getValorTotalLiquidacionItem();
            }

        }
        lblValorFactura.setText("Total Factura : " + nf.format(NuevoValorFactura));
    }

    private void calculauFila2() {

        double NuevoValorFactura = 0.0;
        int i = 0;
        for (CProductosPorFactura pxf : fLiquidarManifiestos.facturaActual.getListaProductosPorFactura()) {

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

                lbl01.setText(nf.format(pxf.getValorUnitarioConIva()));
                lbl2.setText(nf.format(pxf.getValorUnitarioSinIva()));
                lbl3.setText("" + pxf.getPorcentajeDescuento() * 100 + " %");
                lbl4.setText("" + pxf.getCantidadEntregadaItem());
                lbl5.setText("" + pxf.getCantidadRechazadaItem());
                lbl6.setText("" + nf.format(pxf.getValorDescuentoItem()));
                lbl7.setText("" + nf.format(pxf.getValorRechazoItem()));
                lbl8.setText("" + nf.format(pxf.getValorTotalLiquidacionItem()));
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

            for (CProductosPorFactura pxf : fDescargarFacturas.facturaActual.getListaProductosPorFactura()) {
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

                    lblValorUnitarioConIva.setText("Valor Unitario con iva " + nf.format(pxf.getValorUnitarioConIva()));
                    lblValorUnitarioSinIva.setText("Valor Unitario sin iva " + nf.format(pxf.getValorUnitarioSinIva()));
                    lblporcentajeDescuento.setText("PorcentajeDescuento: " + (pxf.getPorcentajeDescuento() * 100) + " %");
                    lblcantidadEntregada.setText("Cantidad entregada " + pxf.getCantidadEntregadaItem());
                    lblcantidadRechazada.setText("Cantidad Devuelta " + pxf.getCantidadRechazadaItem());
                    lblvalorDescuento.setText("ValorDescuento " + nf.format(pxf.getValorDescuentoItem()));
                    lblvalorRechazo.setText("ValorDevolucion " + nf.format(pxf.getValorRechazoItem()));
                    lblvalorItem.setText("Valor total item " + nf.format(pxf.getValorTotalLiquidacionItem()));

                }
            }

            for (CProductosPorFactura obj : fDescargarFacturas.facturaActual.getListaProductosPorFactura()) {
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
        if (fDescargarFacturas != null) {
            fDescargarFacturas.txtNumeroDeFactura.setEnabled(true);
            return;
        } 
        if (dDescargarDevoluciones != null)  {
            dDescargarDevoluciones.txtNumeroDeFactura.setEnabled(true);
             return;
        }

         if (fLiquidarManifiestos != null)  {
            fLiquidarManifiestos.txtNumeroDeFactura.setEnabled(true);
             return;
        }
    }//GEN-LAST:event_formInternalFrameClosed

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
        limpiar();
    }//GEN-LAST:event_formInternalFrameClosing

    private void txt_columnaIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_columnaIdActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_columnaIdActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDescargar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JLabel lbl01;
    private javax.swing.JLabel lbl2;
    private javax.swing.JLabel lbl3;
    private javax.swing.JLabel lbl4;
    private javax.swing.JLabel lbl5;
    private javax.swing.JLabel lbl6;
    private javax.swing.JLabel lbl7;
    private javax.swing.JLabel lbl8;
    private javax.swing.JLabel lblFactura;
    private javax.swing.JLabel lblValorFactura;
    private javax.swing.JLabel lblValorUnitarioConIva;
    private javax.swing.JLabel lblValorUnitarioSinIva;
    private javax.swing.JLabel lblcantidadEntregada;
    private javax.swing.JLabel lblcantidadRechazada;
    private javax.swing.JLabel lblporcentajeDescuento;
    private javax.swing.JLabel lblvalorDescuento;
    private javax.swing.JLabel lblvalorItem;
    private javax.swing.JLabel lblvalorRechazo;
    public javax.swing.JTable tblProductosPorFactura;
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
        for (CProductosPorFactura pxf : this.fDescargarFacturas.facturaActual.getListaProductosPorFactura()) {

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

            nuevoValorFactura = fDescargarFacturas.facturaActual.getValorTotalFactura();

        }

        lblValorFactura.setText("Total Factura : " + nf.format(nuevoValorFactura));

    }
    
     private void llenarTablaProductosPorFactura2() {
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
        for (CProductosPorFactura pxf : this.fLiquidarManifiestos.facturaActual.getListaProductosPorFactura()) {

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

            nuevoValorFactura = fLiquidarManifiestos.facturaActual.getValorTotalFactura();

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
