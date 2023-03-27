/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios;

import aplicacionlogistica.distribucion.Threads.HiloConsultarManifiesto;
import aplicacionlogistica.distribucion.consultas.*;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.PrincipalAuxiliarLogistica;
import aplicacionlogistica.distribucion.formularios.Threads.HiloFtrasladoDeFActuras;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import mtto.vehiculos.CCarros;
import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author VLI_488
 */
public class FTrasladoDeFacturas extends javax.swing.JInternalFrame {
    
    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    CEmpleados conductor;
    CUsuarios user;
    public CManifiestosDeDistribucion manifiestoOrigen = null;
    public CManifiestosDeDistribucion manifiestoDestino = null;
    CCarros carro = null;
    Inicio ini = null;
    
    NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    public DefaultTableModel modelo1, modelo2;
    
    Double valorTotalAConsignar = 0.0;
    boolean cargado = false;
    // boolean tieneManifiestosAnteriores = false;
    // boolean tieneMnifiestosAsigndos = false;
    boolean nuevo = false;
    boolean actualizar = false;
    boolean liberado = false;
    boolean grabar = false;
    boolean grabado = false;
    int kilometrosEntrada;
    int kilometrosRecorridos;
    
    int filaTabla2;
    int indiceLista = 0;
    int columna = 0;
    public int caso;
    
    String mensaje = null;
    
    public List<CFacturasPorManifiesto> listaDeFacturasParaTrasladar;
    public List<CFacturasPorManifiesto> listaDeFacturasEnManifiesto;

    public boolean isGrabado() {
        return grabado;
    }

    public void setGrabado(boolean grabado) {
        this.grabado = grabado;
    }
    
    
    
    public CEmpleados getConductor() {
        return conductor;
    }
    
    public void setConductor(CEmpleados conductor) {
        this.conductor = conductor;
    }
    
    public Inicio getIni() {
        return ini;
    }
    
    public void setIni(Inicio ini) {
        this.ini = ini;
    }

    /**
     * Creates new fCambiarClave IngresarManifiestoDeDistribucion public
     * List<Vst_FacturasPorManifiesto> getVistaFacturasEnDistribucion() { return
     * vistaFacturasPorManifiesto; }
     *
     * @param ini
     */
    public FTrasladoDeFacturas(Inicio ini) throws Exception {
        CManifiestosDeDistribucion manif = new CManifiestosDeDistribucion(ini);
        
        try {
            initComponents();
            this.ini = ini;
            this.user = ini.getUser();
            limpiar();
            barraDestino.setSize(barraOrigen.getSize());
            
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
        } catch (Exception ex) {
            System.out.println("Error en dispatchKeyEvent ");
            Logger.getLogger(FTrasladoDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String strMain = this.ini.getPrefijos();
        String[] arrSplit = strMain.split(",");
        for (int i = 0; i < arrSplit.length; i++) {
            cbxPrefijos.addItem(arrSplit[i].replace("'", ""));
        }
        cargado = true;
        
    }

    /**
     * This method is called from within the constructor to initialize the
     * fCambiarClave. WARNING: Do NOT modify this code. The content of this
     * method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        borrarFila = new javax.swing.JPopupMenu();
        borraFila = new javax.swing.JMenuItem();
        borrarTodasLasFilas = new javax.swing.JMenuItem();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel5 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        txtManifiestoDestin = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        txtPlacaDestino = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        txtNombreConductorDestino = new javax.swing.JTextField();
        txtCedulaConductorDestino = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        txtNombreAuxiliar1Destino = new javax.swing.JTextField();
        jLabel48 = new javax.swing.JLabel();
        txtNombreAuxiliar2Destino = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        txtNombreAuxiliar3Destino = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        txtNombreDespachadorDestino = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblFacturasPorManifiestoDestino = new javax.swing.JTable();
        barraDestino = new javax.swing.JProgressBar();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblFacturasPorManifiestoOrigen = new javax.swing.JTable();
        barraOrigen = new javax.swing.JProgressBar();
        jPanel9 = new javax.swing.JPanel();
        txtManifiestoOriginal = new javax.swing.JTextField();
        jLabel39 = new javax.swing.JLabel();
        txtPlacaOrigen = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        txtNombreConductorOrigen = new javax.swing.JTextField();
        txtCedulaConductorOrigen = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        txtNombreAuxiliar1Origen = new javax.swing.JTextField();
        jLabel57 = new javax.swing.JLabel();
        txtNombreAuxiliar2Origen = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        txtNombreAuxiliar3Origen = new javax.swing.JTextField();
        txtNombreDespachadorOrigen = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jToolBar1 = new javax.swing.JToolBar();
        btnAgregar = new javax.swing.JButton();
        btnMostrarDocumento = new javax.swing.JToggleButton();
        btnGrabar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JToggleButton();
        btnCancelar = new javax.swing.JToggleButton();
        jButton4 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtManifiestoDestino = new javax.swing.JTextField();
        lblCirculoDeProgresoDestino = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        cbxPrefijos = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtNumeroFactura = new javax.swing.JTextField();
        lblCirculoDeProgresoOrigen = new javax.swing.JLabel();
        txtManifiestoOrigen = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        borraFila.setText("BorrA 1 Fila");
        borraFila.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borraFilaActionPerformed(evt);
            }
        });
        borrarFila.add(borraFila);

        borrarTodasLasFilas.setText("Borra Todas las Filas");
        borrarTodasLasFilas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarTodasLasFilasActionPerformed(evt);
            }
        });
        borrarFila.add(borrarTodasLasFilas);

        setClosable(true);
        setIconifiable(true);
        setTitle("Formulario para Traslado de Facturas  a otro manifiesto");
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

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Manifiesto Destino"));

        txtManifiestoDestin.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtManifiestoDestin.setEnabled(false);
        txtManifiestoDestin.setName("numerico"); // NOI18N
        txtManifiestoDestin.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtManifiestoDestinFocusGained(evt);
            }
        });
        txtManifiestoDestin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtManifiestoDestinActionPerformed(evt);
            }
        });
        txtManifiestoDestin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtManifiestoDestinKeyPressed(evt);
            }
        });

        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel38.setText("Placa");

        txtPlacaDestino.setEditable(false);
        txtPlacaDestino.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtPlacaDestino.setEnabled(false);
        txtPlacaDestino.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPlacaDestinoFocusGained(evt);
            }
        });
        txtPlacaDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlacaDestinoActionPerformed(evt);
            }
        });
        txtPlacaDestino.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPlacaDestinoKeyPressed(evt);
            }
        });

        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("Manifiesto");

        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("Conductor");

        txtNombreConductorDestino.setEditable(false);
        txtNombreConductorDestino.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtNombreConductorDestino.setEnabled(false);
        txtNombreConductorDestino.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreConductorDestinoFocusGained(evt);
            }
        });
        txtNombreConductorDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreConductorDestinoActionPerformed(evt);
            }
        });
        txtNombreConductorDestino.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreConductorDestinoKeyPressed(evt);
            }
        });

        txtCedulaConductorDestino.setEditable(false);
        txtCedulaConductorDestino.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtCedulaConductorDestino.setEnabled(false);
        txtCedulaConductorDestino.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCedulaConductorDestinoFocusGained(evt);
            }
        });
        txtCedulaConductorDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaConductorDestinoActionPerformed(evt);
            }
        });
        txtCedulaConductorDestino.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCedulaConductorDestinoKeyPressed(evt);
            }
        });

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel44.setText("Nombre Cond.");

        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel47.setText("Nombre Aux 1.");

        txtNombreAuxiliar1Destino.setEditable(false);
        txtNombreAuxiliar1Destino.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtNombreAuxiliar1Destino.setEnabled(false);
        txtNombreAuxiliar1Destino.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreAuxiliar1DestinoFocusGained(evt);
            }
        });
        txtNombreAuxiliar1Destino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreAuxiliar1DestinoActionPerformed(evt);
            }
        });
        txtNombreAuxiliar1Destino.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreAuxiliar1DestinoKeyPressed(evt);
            }
        });

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel48.setText("Nombre Aux 2.");

        txtNombreAuxiliar2Destino.setEditable(false);
        txtNombreAuxiliar2Destino.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtNombreAuxiliar2Destino.setEnabled(false);
        txtNombreAuxiliar2Destino.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreAuxiliar2DestinoFocusGained(evt);
            }
        });
        txtNombreAuxiliar2Destino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreAuxiliar2DestinoActionPerformed(evt);
            }
        });
        txtNombreAuxiliar2Destino.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreAuxiliar2DestinoKeyPressed(evt);
            }
        });

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel49.setText("Nombre Aux 3.");

        txtNombreAuxiliar3Destino.setEditable(false);
        txtNombreAuxiliar3Destino.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtNombreAuxiliar3Destino.setEnabled(false);
        txtNombreAuxiliar3Destino.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreAuxiliar3DestinoFocusGained(evt);
            }
        });
        txtNombreAuxiliar3Destino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreAuxiliar3DestinoActionPerformed(evt);
            }
        });
        txtNombreAuxiliar3Destino.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreAuxiliar3DestinoKeyPressed(evt);
            }
        });

        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel50.setText("Despachador");

        txtNombreDespachadorDestino.setEditable(false);
        txtNombreDespachadorDestino.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtNombreDespachadorDestino.setEnabled(false);
        txtNombreDespachadorDestino.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDespachadorDestinoFocusGained(evt);
            }
        });
        txtNombreDespachadorDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreDespachadorDestinoActionPerformed(evt);
            }
        });
        txtNombreDespachadorDestino.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDespachadorDestinoKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel40, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtManifiestoDestin, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPlacaDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCedulaConductorDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreConductorDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreAuxiliar1Destino, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreAuxiliar2Destino, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreAuxiliar3Destino, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreDespachadorDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(113, 113, 113))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel40, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtManifiestoDestin, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPlacaDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel41, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCedulaConductorDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreConductorDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreAuxiliar1Destino, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreAuxiliar2Destino, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreAuxiliar3Destino, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreDespachadorDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel50, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );

        tblFacturasPorManifiestoDestino.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Factura", "Cliente.", "valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFacturasPorManifiestoDestino.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblFacturasPorManifiestoDestino.setComponentPopupMenu(borrarFila);
        tblFacturasPorManifiestoDestino.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFacturasPorManifiestoDestinoMouseClicked(evt);
            }
        });
        jScrollPane5.setViewportView(tblFacturasPorManifiestoDestino);
        tblFacturasPorManifiestoDestino.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tblFacturasPorManifiestoDestino.getColumnModel().getColumnCount() > 0) {
            tblFacturasPorManifiestoDestino.getColumnModel().getColumn(0).setResizable(false);
            tblFacturasPorManifiestoDestino.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblFacturasPorManifiestoDestino.getColumnModel().getColumn(1).setResizable(false);
            tblFacturasPorManifiestoDestino.getColumnModel().getColumn(1).setPreferredWidth(80);
            tblFacturasPorManifiestoDestino.getColumnModel().getColumn(2).setResizable(false);
            tblFacturasPorManifiestoDestino.getColumnModel().getColumn(2).setPreferredWidth(250);
            tblFacturasPorManifiestoDestino.getColumnModel().getColumn(3).setPreferredWidth(120);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(barraDestino, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addGap(6, 6, 6))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraDestino, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38))
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblFacturasPorManifiestoOrigen.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Factura", "Cliente.", "valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFacturasPorManifiestoOrigen.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblFacturasPorManifiestoOrigen.setColumnSelectionAllowed(true);
        tblFacturasPorManifiestoOrigen.setComponentPopupMenu(borrarFila);
        tblFacturasPorManifiestoOrigen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFacturasPorManifiestoOrigenMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblFacturasPorManifiestoOrigen);
        tblFacturasPorManifiestoOrigen.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        if (tblFacturasPorManifiestoOrigen.getColumnModel().getColumnCount() > 0) {
            tblFacturasPorManifiestoOrigen.getColumnModel().getColumn(0).setResizable(false);
            tblFacturasPorManifiestoOrigen.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblFacturasPorManifiestoOrigen.getColumnModel().getColumn(1).setResizable(false);
            tblFacturasPorManifiestoOrigen.getColumnModel().getColumn(1).setPreferredWidth(80);
            tblFacturasPorManifiestoOrigen.getColumnModel().getColumn(2).setResizable(false);
            tblFacturasPorManifiestoOrigen.getColumnModel().getColumn(2).setPreferredWidth(250);
            tblFacturasPorManifiestoOrigen.getColumnModel().getColumn(3).setPreferredWidth(120);
        }

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Manifiesto Origen"));

        txtManifiestoOriginal.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtManifiestoOriginal.setEnabled(false);
        txtManifiestoOriginal.setName("numerico"); // NOI18N
        txtManifiestoOriginal.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtManifiestoOriginalFocusGained(evt);
            }
        });
        txtManifiestoOriginal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtManifiestoOriginalActionPerformed(evt);
            }
        });
        txtManifiestoOriginal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtManifiestoOriginalKeyPressed(evt);
            }
        });

        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel39.setText("Placa");

        txtPlacaOrigen.setEditable(false);
        txtPlacaOrigen.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtPlacaOrigen.setEnabled(false);
        txtPlacaOrigen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtPlacaOrigenFocusGained(evt);
            }
        });
        txtPlacaOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlacaOrigenActionPerformed(evt);
            }
        });
        txtPlacaOrigen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPlacaOrigenKeyPressed(evt);
            }
        });

        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel42.setText("Manifiesto");

        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel51.setText("Conductor");

        txtNombreConductorOrigen.setEditable(false);
        txtNombreConductorOrigen.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtNombreConductorOrigen.setEnabled(false);
        txtNombreConductorOrigen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreConductorOrigenFocusGained(evt);
            }
        });
        txtNombreConductorOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreConductorOrigenActionPerformed(evt);
            }
        });
        txtNombreConductorOrigen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreConductorOrigenKeyPressed(evt);
            }
        });

        txtCedulaConductorOrigen.setEditable(false);
        txtCedulaConductorOrigen.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtCedulaConductorOrigen.setEnabled(false);
        txtCedulaConductorOrigen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtCedulaConductorOrigenFocusGained(evt);
            }
        });
        txtCedulaConductorOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCedulaConductorOrigenActionPerformed(evt);
            }
        });
        txtCedulaConductorOrigen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCedulaConductorOrigenKeyPressed(evt);
            }
        });

        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel53.setText("Nombre Cond.");

        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel56.setText("Nombre Aux 1.");

        txtNombreAuxiliar1Origen.setEditable(false);
        txtNombreAuxiliar1Origen.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtNombreAuxiliar1Origen.setEnabled(false);
        txtNombreAuxiliar1Origen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreAuxiliar1OrigenFocusGained(evt);
            }
        });
        txtNombreAuxiliar1Origen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreAuxiliar1OrigenActionPerformed(evt);
            }
        });
        txtNombreAuxiliar1Origen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreAuxiliar1OrigenKeyPressed(evt);
            }
        });

        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel57.setText("Nombre Aux 2.");

        txtNombreAuxiliar2Origen.setEditable(false);
        txtNombreAuxiliar2Origen.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtNombreAuxiliar2Origen.setEnabled(false);
        txtNombreAuxiliar2Origen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreAuxiliar2OrigenFocusGained(evt);
            }
        });
        txtNombreAuxiliar2Origen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreAuxiliar2OrigenActionPerformed(evt);
            }
        });
        txtNombreAuxiliar2Origen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreAuxiliar2OrigenKeyPressed(evt);
            }
        });

        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel58.setText("Nombre Aux 3.");

        txtNombreAuxiliar3Origen.setEditable(false);
        txtNombreAuxiliar3Origen.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtNombreAuxiliar3Origen.setEnabled(false);
        txtNombreAuxiliar3Origen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreAuxiliar3OrigenFocusGained(evt);
            }
        });
        txtNombreAuxiliar3Origen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreAuxiliar3OrigenActionPerformed(evt);
            }
        });
        txtNombreAuxiliar3Origen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreAuxiliar3OrigenKeyPressed(evt);
            }
        });

        txtNombreDespachadorOrigen.setEditable(false);
        txtNombreDespachadorOrigen.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtNombreDespachadorOrigen.setEnabled(false);
        txtNombreDespachadorOrigen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNombreDespachadorOrigenFocusGained(evt);
            }
        });
        txtNombreDespachadorOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNombreDespachadorOrigenActionPerformed(evt);
            }
        });
        txtNombreDespachadorOrigen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNombreDespachadorOrigenKeyPressed(evt);
            }
        });

        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel52.setText("Despachador");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel42, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel51, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel56, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtManifiestoOriginal, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPlacaOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCedulaConductorOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreConductorOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreAuxiliar1Origen, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreAuxiliar2Origen, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreAuxiliar3Origen, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNombreDespachadorOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(59, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtManifiestoOriginal, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPlacaOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel51, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCedulaConductorOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreConductorOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreAuxiliar1Origen, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreAuxiliar2Origen, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreAuxiliar3Origen, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNombreDespachadorOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel52, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(barraOrigen, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 242, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(barraOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addGap(1, 1, 1))
        );

        jScrollPane1.setViewportView(jPanel5);

        jToolBar1.setRollover(true);

        btnAgregar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Create.png"))); // NOI18N
        btnAgregar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnAgregar.setFocusable(false);
        btnAgregar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnAgregar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnAgregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnAgregar);

        btnMostrarDocumento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Report.png"))); // NOI18N
        btnMostrarDocumento.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnMostrarDocumento.setEnabled(false);
        btnMostrarDocumento.setFocusable(false);
        btnMostrarDocumento.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnMostrarDocumento.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnMostrarDocumento);

        btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
        btnGrabar.setToolTipText("Grabar");
        btnGrabar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnGrabar.setEnabled(false);
        btnGrabar.setFocusable(false);
        btnGrabar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnGrabar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });
        jToolBar1.add(btnGrabar);

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Print.png"))); // NOI18N
        btnImprimir.setToolTipText("Imprimir");
        btnImprimir.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btnImprimir.setEnabled(false);
        btnImprimir.setFocusable(false);
        btnImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar1.add(btnImprimir);

        btnCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Cancel.png"))); // NOI18N
        btnCancelar.setToolTipText("Imprimir");
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

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Exit.png"))); // NOI18N
        jButton4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setPreferredSize(new java.awt.Dimension(22, 22));
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jPanel2.setBorder(null);

        jLabel2.setText("       Manifiesto Destino   ");

        txtManifiestoDestino.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtManifiestoDestino.setName("numerico"); // NOI18N
        txtManifiestoDestino.setPreferredSize(new java.awt.Dimension(120, 25));
        txtManifiestoDestino.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtManifiestoDestinoFocusGained(evt);
            }
        });
        txtManifiestoDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtManifiestoDestinoActionPerformed(evt);
            }
        });
        txtManifiestoDestino.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtManifiestoDestinoKeyPressed(evt);
            }
        });

        lblCirculoDeProgresoDestino.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        jLabel29.setText("Prefijo");

        cbxPrefijos.setPreferredSize(new java.awt.Dimension(50, 24));
        cbxPrefijos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxPrefijosActionPerformed(evt);
            }
        });

        jLabel3.setText("Facturas a Trasadar   ");

        txtNumeroFactura.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtNumeroFactura.setName(""); // NOI18N
        txtNumeroFactura.setPreferredSize(new java.awt.Dimension(170, 25));
        txtNumeroFactura.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtNumeroFacturaFocusGained(evt);
            }
        });
        txtNumeroFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroFacturaActionPerformed(evt);
            }
        });
        txtNumeroFactura.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNumeroFacturaKeyPressed(evt);
            }
        });

        lblCirculoDeProgresoOrigen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

        txtManifiestoOrigen.setFont(new java.awt.Font("DejaVu Sans", 0, 11)); // NOI18N
        txtManifiestoOrigen.setName("numerico"); // NOI18N
        txtManifiestoOrigen.setPreferredSize(new java.awt.Dimension(120, 25));
        txtManifiestoOrigen.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtManifiestoOrigenFocusGained(evt);
            }
        });
        txtManifiestoOrigen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtManifiestoOrigenActionPerformed(evt);
            }
        });
        txtManifiestoOrigen.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtManifiestoOrigenKeyPressed(evt);
            }
        });

        jLabel1.setText("     Manifiesto Origen   ");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(4, 4, 4)
                .addComponent(txtManifiestoOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblCirculoDeProgresoOrigen)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addGap(3, 3, 3)
                .addComponent(txtManifiestoDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCirculoDeProgresoDestino)
                .addGap(18, 18, 18)
                .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cbxPrefijos, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addGap(1, 1, 1)
                .addComponent(txtNumeroFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel29)
                        .addComponent(cbxPrefijos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(txtNumeroFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(lblCirculoDeProgresoOrigen)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtManifiestoDestino, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtManifiestoOrigen, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)))
                    .addComponent(lblCirculoDeProgresoDestino)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 1059, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblFacturasPorManifiestoOrigenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFacturasPorManifiestoOrigenMouseClicked
        // TODO add your handling code here:

        int row = tblFacturasPorManifiestoOrigen.getSelectedRow();
        // this.cedula=(String.valueOf(jTable1.getValueAt(row, 0)));
    }//GEN-LAST:event_tblFacturasPorManifiestoOrigenMouseClicked
    

    private void formInternalFrameClosing(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
//        if (manifiestoModificadoPorMi != null) {
//            manifiestoModificadoPorMi.setIsFree(1);
//        }

    }//GEN-LAST:event_formInternalFrameClosing

    private void txtManifiestoDestinFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtManifiestoDestinFocusGained
        txtManifiestoDestin.setSelectionStart(0);
        txtManifiestoDestin.setSelectionEnd(txtManifiestoDestin.getText().length());
    }//GEN-LAST:event_txtManifiestoDestinFocusGained

    private void txtManifiestoDestinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtManifiestoDestinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtManifiestoDestinActionPerformed

    private void txtManifiestoDestinKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtManifiestoDestinKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_F2) {
            FBuscarManifiestos form = new FBuscarManifiestos(ini, this);
            PrincipalAuxiliarLogistica.escritorio.add(form);
            form.toFront();
            form.setClosable(true);
            form.setVisible(true);
            form.setTitle("Formulario para Buscar manifiestos de la última semana");
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            //form.setSize(PrincipalLogistica.escritorio.getSize());
            form.setLocation((ini.getDimension().width - form.getSize().width) / 2, (ini.getDimension().height - form.getSize().height) / 2);
            form.show();
            
        }
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            consultarManifiestoOrigen();
        }

    }//GEN-LAST:event_txtManifiestoDestinKeyPressed
    
    public boolean consultarManifiestoOrigen() {
        boolean manifiestoValido = false;
        try {

            // manifiestoOrigen = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtManifiesto.getText().trim()), "CURRENT_DATE");
            manifiestoOrigen = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtManifiestoOrigen.getText().trim()));
            lblCirculoDeProgresoOrigen.setVisible(true);
            // se valida que el manifiesto exista
            if (manifiestoOrigen.getVehiculo() == null) { // si  no existe el manifiesto

                JOptionPane.showInternalMessageDialog(this, "Ese manifiesto no Existe en la BB DD ", "Error", JOptionPane.WARNING_MESSAGE);
                
            } else { //  si existe el vehículo, existe el manifiesto

                manifiestoOrigen.setListaFacturasPorManifiesto();
                manifiestoOrigen.setListaDeAuxiliares();
                
                txtManifiestoOriginal.setText(txtManifiestoOrigen.getText());
                txtManifiestoDestin.setEditable(false);
                CCarros car = new CCarros(ini, manifiestoOrigen.getVehiculo());
                txtPlacaOrigen.setText(manifiestoOrigen.getVehiculo());
                txtCedulaConductorOrigen.setText(manifiestoOrigen.getConductor());
                
                for (CEmpleados obj : ini.getListaDeEmpleados()) {
                    if (obj.getCedula().equals(manifiestoOrigen.getConductor())) {
                        txtNombreConductorOrigen.setText(obj.getNombres() + " " + obj.getApellidos());
                        break;
                    }
                }

                // INDIC EL ESTADO ACTUAL DEL MANIFIESTO
                switch (manifiestoOrigen.getEstadoManifiesto()) {
                    case 0:
                        
                        break;
                    case 1:
                        JOptionPane.showInternalMessageDialog(this, "ESE  MANIFIESTOS EN DISTRIBUCION ,\n"
                                + "HAY QUE CREARLO Y DARLE SALIDA A DISTRIBUCION", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    
                    case 2: // MANIFIESTO CREADO, PERO NO HA SALIDO A DISTRIBUCION
                        JOptionPane.showInternalMessageDialog(this, "ESTE MANIFIESTO DE  DISTRIBUCION ,\n"
                                + "NO SE HA GRABADO EN EL SISTEMA", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    case 3: // MANIFIESTO EN DISTRIBUCION
                        llenarDatosManifiestoOrigen();
                        manifiestoValido = true;
                        break;
                    case 4:// MANIFIESTO YA DESCARGADO
                        //llenarDatosManifiestoCerrado();
                        JOptionPane.showInternalMessageDialog(this, "ESTE MANIFIESTO  YA ESTA DESCARGADO ,\n"
                                + "NO SE PUEDE REALIZAR TRASLADOS DE FACTURAS", "Error", JOptionPane.ERROR_MESSAGE);
                        
                        break;
                    
                }
                
            }
            
        } catch (Exception ex) {
            System.out.println("Error en txtManifiestoKeyPressed ");
            Logger.getLogger(FTrasladoDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return manifiestoValido;
    }
    
    public boolean consultarManifiestoDestino() {
        boolean manifiestoValido = false;
        try {

            // manifiestoOrigen = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtManifiesto.getText().trim()), "CURRENT_DATE");
            manifiestoDestino = new CManifiestosDeDistribucion(ini, Integer.parseInt(txtManifiestoDestino.getText().trim()));

            // se valida que el manifiesto exista
            if (manifiestoDestino.getVehiculo() == null) { // si  no existe el manifiesto

                JOptionPane.showInternalMessageDialog(this, "Ese manifiesto no Existe en la BB DD ", "Error", JOptionPane.WARNING_MESSAGE);
                
            } else { //  si existe el vehículo, existe el manifiesto

                /*Se recuperan las facuras que tenga asignado el manifiesto */
                listaDeFacturasParaTrasladar = new ArrayList();
                manifiestoDestino.setListaFacturasPorManifiesto();
                listaDeFacturasParaTrasladar = manifiestoDestino.getListaFacturasPorManifiesto();

                /*Se recuperan los auxiliares asignados */
                manifiestoDestino.setListaDeAuxiliares();

                /*Se llenan los campos del formulario*/
                txtManifiestoDestin.setText(txtManifiestoDestino.getText());
                txtManifiestoDestin.setEditable(false);
                txtManifiestoDestin.setEnabled(true);
                CCarros car = new CCarros(ini, manifiestoDestino.getVehiculo());
                txtPlacaDestino.setText(manifiestoDestino.getVehiculo());
                txtCedulaConductorDestino.setText(manifiestoDestino.getConductor());
                
                for (CEmpleados obj : ini.getListaDeEmpleados()) {
                    if (obj.getCedula().equals(manifiestoDestino.getConductor())) {
                        txtNombreConductorDestino.setText(obj.getNombres() + " " + obj.getApellidos());
                        break;
                    }
                }

                // INDIC EL ESTADO ACTUAL DEL MANIFIESTO
                switch (manifiestoDestino.getEstadoManifiesto()) {
                    case 0:
                        
                        break;
                    case 1:
                        JOptionPane.showInternalMessageDialog(this, "ESE  MANIFIESTOS EN DISTRIBUCION ,\n"
                                + "HAY QUE CREARLO Y DARLE SALIDA A DISTRIBUCION", "Error", JOptionPane.ERROR_MESSAGE);
                        break;
                    
                    case 2: // MANIFIESTO CREADO, PERO NO HA SALIDO A DISTRIBUCION
                        // JOptionPane.showInternalMessageDialog(this, "ESTE MANIFIESTO DE  DISTRIBUCION ,\n"
                        //         + "NO SE HA GRABADO EN EL SISTEMA", "Error",JOptionPane.WARNING_MESSAGE);
                        llenarDatosManifiestoDestino();
                        manifiestoValido = true;
                        //listaDeFacturasTrasladadas = new ArrayList();
                        break;
                    case 3: // MANIFIESTO EN DISTRIBUCION
                        llenarDatosManifiestoDestino();
                        manifiestoValido = true;
                        //listaDeFacturasTrasladadas = new ArrayList();
                        break;
                    case 4:// MANIFIESTO YA DESCARGADO
                        //llenarDatosManifiestoCerrado();
                        JOptionPane.showInternalMessageDialog(this, "ESTE MANIFIESTO  YA ESTA DESCARGADO ,\n"
                                + "NO SE PUEDE REALIZAR TRASLADOS DE FACTURAS", "Error", JOptionPane.ERROR_MESSAGE);
                        
                        break;
                    
                }
                
            }
            
        } catch (Exception ex) {
            System.out.println("Error en txtManifiestoKeyPressed ");
            Logger.getLogger(FTrasladoDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return manifiestoValido;
    }
    

    private void txtPlacaDestinoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlacaDestinoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaDestinoFocusGained

    private void txtPlacaDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaDestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaDestinoActionPerformed

    private void txtPlacaDestinoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaDestinoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaDestinoKeyPressed
    

    private void txtNombreConductorDestinoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreConductorDestinoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorDestinoFocusGained

    private void txtNombreConductorDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreConductorDestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorDestinoActionPerformed

    private void txtNombreConductorDestinoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreConductorDestinoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorDestinoKeyPressed

    private void txtCedulaConductorDestinoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaConductorDestinoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorDestinoFocusGained

    private void txtCedulaConductorDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaConductorDestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorDestinoActionPerformed

    private void txtCedulaConductorDestinoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaConductorDestinoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorDestinoKeyPressed

    private void txtNombreAuxiliar1DestinoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar1DestinoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar1DestinoFocusGained

    private void txtNombreAuxiliar1DestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar1DestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar1DestinoActionPerformed

    private void txtNombreAuxiliar1DestinoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar1DestinoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar1DestinoKeyPressed

    private void txtNombreAuxiliar2DestinoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar2DestinoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar2DestinoFocusGained

    private void txtNombreAuxiliar2DestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar2DestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar2DestinoActionPerformed

    private void txtNombreAuxiliar2DestinoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar2DestinoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar2DestinoKeyPressed

    private void txtNombreAuxiliar3DestinoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar3DestinoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar3DestinoFocusGained

    private void txtNombreAuxiliar3DestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar3DestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar3DestinoActionPerformed

    private void txtNombreAuxiliar3DestinoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar3DestinoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar3DestinoKeyPressed

    private void txtNombreDespachadorDestinoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDespachadorDestinoFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDespachadorDestinoFocusGained

    private void txtNombreDespachadorDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDespachadorDestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDespachadorDestinoActionPerformed

    private void txtNombreDespachadorDestinoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDespachadorDestinoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDespachadorDestinoKeyPressed

    private void txtManifiestoOrigenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtManifiestoOrigenFocusGained
        txtManifiestoOrigen.setSelectionStart(0);
        txtManifiestoOrigen.setSelectionEnd(txtManifiestoOrigen.getText().length());
    }//GEN-LAST:event_txtManifiestoOrigenFocusGained

    private void txtManifiestoOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtManifiestoOrigenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtManifiestoOrigenActionPerformed

    private void txtManifiestoOrigenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtManifiestoOrigenKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
//            if (consultarManifiestoOrigen()) {
//                txtManifiestoOrigen.setEditable(false);
//                txtManifiestoDestino.setEditable(true);
//                txtNumeroFactura.requestFocus();
//                txtManifiestoDestino.requestFocus();
//
//            };
           // this.caso = 1;
           // new Thread(new HiloConsultarManifiesto(this, caso)).start();
            new Thread(new HiloFtrasladoDeFActuras(ini, this, "ConsultarManifiestoOrigen")).start();
        }
    }//GEN-LAST:event_txtManifiestoOrigenKeyPressed

    private void txtManifiestoDestinoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtManifiestoDestinoFocusGained
        txtManifiestoDestino.setSelectionStart(0);
        txtManifiestoDestino.setSelectionEnd(txtManifiestoDestino.getText().length());
    }//GEN-LAST:event_txtManifiestoDestinoFocusGained

    private void txtManifiestoDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtManifiestoDestinoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtManifiestoDestinoActionPerformed

    private void txtManifiestoDestinoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtManifiestoDestinoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            
          

            /*Valida los numeros de los manifiestos no sean iguales*/
            if (txtManifiestoOrigen.getText().trim().equals(txtManifiestoDestino.getText().trim())) {
                JOptionPane.showInternalMessageDialog(this, "Los dos numero de Manifiestos son iguales", "!Alerta!", JOptionPane.WARNING_MESSAGE);
                caso = 0;
                return;
            }
            
            if (manifiestoOrigen.getEstadoManifiesto() == 4) {
                JOptionPane.showInternalMessageDialog(this, "El manifiesto origen ya fue descargado", "!Alerta!", JOptionPane.WARNING_MESSAGE);
                caso = 0;
                return;
            }

//            if (consultarManifiestoDestino()) {
//                txtNumeroFactura.setEditable(true);
//                txtManifiestoDestino.setEditable(false);
//                txtNumeroFactura.requestFocus();
//            }
  //this.caso = 2;
           // new Thread(new HiloConsultarManifiesto(this, caso)).start();
           
             new Thread(new HiloFtrasladoDeFActuras(ini, this, "ConsultarManifiestoDestino")).start();
            
        }

    }//GEN-LAST:event_txtManifiestoDestinoKeyPressed

    private void txtManifiestoOriginalFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtManifiestoOriginalFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtManifiestoOriginalFocusGained

    private void txtManifiestoOriginalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtManifiestoOriginalActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtManifiestoOriginalActionPerformed

    private void txtManifiestoOriginalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtManifiestoOriginalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtManifiestoOriginalKeyPressed

    private void txtPlacaOrigenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtPlacaOrigenFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaOrigenFocusGained

    private void txtPlacaOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlacaOrigenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaOrigenActionPerformed

    private void txtPlacaOrigenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPlacaOrigenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPlacaOrigenKeyPressed

    private void txtNombreConductorOrigenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreConductorOrigenFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorOrigenFocusGained

    private void txtNombreConductorOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreConductorOrigenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorOrigenActionPerformed

    private void txtNombreConductorOrigenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreConductorOrigenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreConductorOrigenKeyPressed

    private void txtCedulaConductorOrigenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtCedulaConductorOrigenFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorOrigenFocusGained

    private void txtCedulaConductorOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCedulaConductorOrigenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorOrigenActionPerformed

    private void txtCedulaConductorOrigenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCedulaConductorOrigenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCedulaConductorOrigenKeyPressed

    private void txtNombreAuxiliar1OrigenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar1OrigenFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar1OrigenFocusGained

    private void txtNombreAuxiliar1OrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar1OrigenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar1OrigenActionPerformed

    private void txtNombreAuxiliar1OrigenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar1OrigenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar1OrigenKeyPressed

    private void txtNombreAuxiliar2OrigenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar2OrigenFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar2OrigenFocusGained

    private void txtNombreAuxiliar2OrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar2OrigenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar2OrigenActionPerformed

    private void txtNombreAuxiliar2OrigenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar2OrigenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar2OrigenKeyPressed

    private void txtNombreAuxiliar3OrigenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar3OrigenFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar3OrigenFocusGained

    private void txtNombreAuxiliar3OrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar3OrigenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar3OrigenActionPerformed

    private void txtNombreAuxiliar3OrigenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreAuxiliar3OrigenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreAuxiliar3OrigenKeyPressed

    private void txtNombreDespachadorOrigenFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNombreDespachadorOrigenFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDespachadorOrigenFocusGained

    private void txtNombreDespachadorOrigenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNombreDespachadorOrigenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDespachadorOrigenActionPerformed

    private void txtNombreDespachadorOrigenKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNombreDespachadorOrigenKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNombreDespachadorOrigenKeyPressed

    private void tblFacturasPorManifiestoDestinoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFacturasPorManifiestoDestinoMouseClicked

    }//GEN-LAST:event_tblFacturasPorManifiestoDestinoMouseClicked

    private void txtNumeroFacturaFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtNumeroFacturaFocusGained
        txtNumeroFactura.setSelectionStart(0);
        txtNumeroFactura.setSelectionEnd(txtNumeroFactura.getText().length());
    }//GEN-LAST:event_txtNumeroFacturaFocusGained

    private void txtNumeroFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroFacturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroFacturaActionPerformed

    private void txtNumeroFacturaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNumeroFacturaKeyPressed
        
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            
//            try {
//                boolean encontrado = false;
//                boolean facturaEncontrada = false;
//                CFacturasPorManifiesto factura = null;
//                String numeroFactura = cbxPrefijos.getSelectedItem().toString() + txtNumeroFactura.getText().trim();
//                
//                CFacturasPorManifiesto facxMan = new CFacturasPorManifiesto(ini, numeroFactura, txtManifiestoOrigen.getText().trim());
//                if (facxMan.getNumeroFactura() == null) {
//                    JOptionPane.showInternalMessageDialog(this, "Ese numero de factura, no existe en el manifiesto de origen", "Error", JOptionPane.WARNING_MESSAGE);
//                    return;
//                }
//                
//                if (facxMan.getDespachado() > 2) {
//                    JOptionPane.showInternalMessageDialog(this, "Ese numero de factura, no se le puede hacer traslado de manifiesto",
//                            "Factura en reparto", JOptionPane.ERROR_MESSAGE);
//                    
//                    return;
//                }
//                
//                if (facxMan.getEstadoFactura() < 1) {
//                    JOptionPane.showInternalMessageDialog(this, "Ese numero de factura, no se le puede hacer \n traslado de manifiesto, ya tiene movimiento ",
//                            "Factura con Movimiento", JOptionPane.ERROR_MESSAGE);
//                    
//                    return;
//                }
//                
//                factura = null;
//                /*Se valida si el numero de la factura ya existe en el manifiesto destino*/
//                for (CFacturasPorManifiesto obj : manifiestoDestino.getListaFacturasPorManifiesto()) {
//                    if (obj.getNumeroFactura().equals(numeroFactura)) {
//                        encontrado = true;
//                        factura = obj;
//                        break;
//                    }
//                    /*Si la factura existe se sale de la funcion para que la ignore*/
//                    if (encontrado) {
//                        return;
//                    }
//                }
//                
//                factura = null;
//                /*Se valida si el numero de la factura existe en el manifiesto origen*/
//                for (CFacturasPorManifiesto obj : manifiestoOrigen.getListaFacturasPorManifiesto()) {
//                    if (obj.getNumeroFactura().equals(numeroFactura)) {
//                        encontrado = true;
//                        factura = obj;
//                        break;
//                    }
//                }
//                /*Si la factura no existe, entonce lanza una alerta para informar que no
//                existe en el manifiesto origen*/
//                if (factura == null) {
//                    JOptionPane.showInternalMessageDialog(this, "Ese numero de factura, no existe en el manifiesto de origen", "Error", JOptionPane.WARNING_MESSAGE);
//
//                    /*Si la encuentra se procede a  validar  si se encuentra el numero de la
//                    factura para trasladar*/
//                } else {
//                    for (CFacturasPorManifiesto obj : listaDeFacturasParaTrasladar) {
//                        if (obj.getNumeroFactura().equals(factura.getNumeroFactura())) {
//                            facturaEncontrada = true;
//                            break;
//                        }
//                        
//                    }
//                    /*Si el numero de factura  no esta en la lista se procede a agregarla*/
//                    if (!facturaEncontrada) {
//                        listaDeFacturasParaTrasladar.add(factura);
//                        agregarFacturaALaTablaDestino(factura);
//                        btnGrabar1.setEnabled(true);
//                    }
//                    
//                }
//                cbxPrefijos.requestFocus();
//                txtNumeroFactura.requestFocus();
//            } catch (Exception ex) {
//                Logger.getLogger(FTrasladoDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
//            }

        new Thread(new HiloFtrasladoDeFActuras(ini, this, "llamarRegistro")).start();
       
        }
        

    }//GEN-LAST:event_txtNumeroFacturaKeyPressed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void borraFilaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borraFilaActionPerformed
        borrarUnaFactura();
    }//GEN-LAST:event_borraFilaActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        limpiar();
        this.dispose();
        this.setVisible(false);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void borrarTodasLasFilasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarTodasLasFilasActionPerformed
        borrarTodasLasFacturas();
    }//GEN-LAST:event_borrarTodasLasFilasActionPerformed

    private void btnAgregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarActionPerformed
        limpiar();
        txtManifiestoOrigen.setEnabled(true);
        txtManifiestoOrigen.setEditable(true);
        txtManifiestoDestino.requestFocus();
        txtManifiestoDestino.setEditable(false);
        txtManifiestoOrigen.requestFocus();
    }//GEN-LAST:event_btnAgregarActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        if (listaDeFacturasParaTrasladar.isEmpty()) {
            JOptionPane.showInternalMessageDialog(this, "No hay datos para trasladar al Manifiesto # " + manifiestoDestino.getNumeroManifiesto(), "! Alerta !", JOptionPane.WARNING_MESSAGE);
        } else {
            
            new Thread(new HiloFtrasladoDeFActuras(ini, this, "grabarTrasladoDeFacturas")).start();
          
           
        }
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void cbxPrefijosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxPrefijosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxPrefijosActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JProgressBar barraDestino;
    public javax.swing.JProgressBar barraOrigen;
    private javax.swing.JMenuItem borraFila;
    private javax.swing.JPopupMenu borrarFila;
    private javax.swing.JMenuItem borrarTodasLasFilas;
    public javax.swing.JButton btnAgregar;
    public javax.swing.JToggleButton btnCancelar;
    public javax.swing.JButton btnGrabar;
    public javax.swing.JToggleButton btnImprimir;
    public javax.swing.JToggleButton btnMostrarDocumento;
    public javax.swing.JComboBox<String> cbxPrefijos;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JToolBar jToolBar1;
    public javax.swing.JLabel lblCirculoDeProgresoDestino;
    public javax.swing.JLabel lblCirculoDeProgresoOrigen;
    public javax.swing.JTable tblFacturasPorManifiestoDestino;
    public javax.swing.JTable tblFacturasPorManifiestoOrigen;
    public javax.swing.JTextField txtCedulaConductorDestino;
    public javax.swing.JTextField txtCedulaConductorOrigen;
    public javax.swing.JTextField txtManifiestoDestin;
    public javax.swing.JTextField txtManifiestoDestino;
    public javax.swing.JTextField txtManifiestoOrigen;
    public javax.swing.JTextField txtManifiestoOriginal;
    public javax.swing.JTextField txtNombreAuxiliar1Destino;
    public javax.swing.JTextField txtNombreAuxiliar1Origen;
    public javax.swing.JTextField txtNombreAuxiliar2Destino;
    public javax.swing.JTextField txtNombreAuxiliar2Origen;
    public javax.swing.JTextField txtNombreAuxiliar3Destino;
    public javax.swing.JTextField txtNombreAuxiliar3Origen;
    public javax.swing.JTextField txtNombreConductorDestino;
    public javax.swing.JTextField txtNombreConductorOrigen;
    public javax.swing.JTextField txtNombreDespachadorDestino;
    public javax.swing.JTextField txtNombreDespachadorOrigen;
    public javax.swing.JTextField txtNumeroFactura;
    public javax.swing.JTextField txtPlacaDestino;
    public javax.swing.JTextField txtPlacaOrigen;
    // End of variables declaration//GEN-END:variables

    private void limpiar() {
        try {
            lblCirculoDeProgresoDestino.setVisible(false);
            lblCirculoDeProgresoOrigen.setVisible(false);
            barraDestino.setValue(0);
            barraOrigen.setValue(0);
            
            btnGrabar.setEnabled(false);
            btnMostrarDocumento.setEnabled(false);
            btnImprimir.setEnabled(false);
            
            txtManifiestoOrigen.setEditable(false);
            txtManifiestoDestino.setEditable(false);
            txtNumeroFactura.setEditable(false);
            
            valorTotalAConsignar = 0.0;
            barraOrigen.setValue(0);
            txtManifiestoOriginal.setText("");
            txtManifiestoDestin.setText("");
            
            txtManifiestoOrigen.setText("");
            txtManifiestoDestino.setText("");
            txtNumeroFactura.setText("");
            
            txtCedulaConductorOrigen.setText("");
            txtCedulaConductorOrigen.setText("");
            txtNombreAuxiliar1Origen.setText("");
            txtNombreAuxiliar1Origen.setText("");
            txtNombreAuxiliar2Origen.setText("");
            txtNombreAuxiliar2Origen.setText("");
            txtNombreAuxiliar3Origen.setText("");
            txtNombreAuxiliar3Origen.setText("");
            txtNombreConductorOrigen.setText("");
            txtNombreConductorOrigen.setText("");
            txtNombreDespachadorOrigen.setText("");
            txtNombreDespachadorOrigen.setText("");
            txtPlacaOrigen.setText("");
            txtPlacaOrigen.setText("");
            
            txtCedulaConductorDestino.setText("");
            txtCedulaConductorDestino.setText("");
            txtNombreAuxiliar1Destino.setText("");
            txtNombreAuxiliar1Destino.setText("");
            txtNombreAuxiliar2Destino.setText("");
            txtNombreAuxiliar2Destino.setText("");
            txtNombreAuxiliar3Destino.setText("");
            txtNombreAuxiliar3Destino.setText("");
            txtNombreConductorDestino.setText("");
            txtNombreConductorDestino.setText("");
            txtNombreDespachadorDestino.setText("");
            txtNombreDespachadorDestino.setText("");
            txtPlacaDestino.setText("");
            txtPlacaDestino.setText("");

            /* se limpia la tabla de facturas origen*/
            DefaultTableModel modelo = (DefaultTableModel) tblFacturasPorManifiestoOrigen.getModel();
            if (modelo.getRowCount() > 0) {
                int a = modelo.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    modelo.removeRow(i);
                }
            }

            /* se limpia la tabla de facturas destino*/
            DefaultTableModel modelo1 = (DefaultTableModel) tblFacturasPorManifiestoDestino.getModel();
            if (modelo1.getRowCount() > 0) {
                int a = modelo1.getRowCount() - 1;
                for (int i = a; i >= 0; i--) {
                    modelo1.removeRow(i);
                }
            }
            
            listaDeFacturasParaTrasladar = null;
            manifiestoOrigen = null;
            manifiestoDestino = null;
            
        } catch (Exception ex) {
            
        }
        
    }
    
    private boolean validarManifiesto() {
        boolean verificado = true;
        try {
            
            mensaje = "";
            String fi = String.valueOf(Inicio.getFechaSql());
            String ff = String.valueOf(ini.getFechaActualServidor());
            if (!fi.equals(ff)) {
                mensaje += "La fecha del Servidor y el Sistema no coinciden, verificar configuración del sistema " + "  \n";
                verificado = false;
            }
            if (txtManifiestoDestin.getText().isEmpty()) {
                mensaje += "No ha selecccionado el Vehiculo de la Ruta" + "  \n";
                verificado = false;
            }
            
        } catch (SQLException ex) {
            System.out.println("Error en validarManifiesto ");
            Logger.getLogger(FTrasladoDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return verificado;
    }
    
    private void ordenarTabla() {
        for (int i = 0; i < tblFacturasPorManifiestoOrigen.getRowCount(); i++) {
            tblFacturasPorManifiestoOrigen.setValueAt(i + 1, i, 0);
        }
    }
    
    public synchronized void llenarDatosManifiestoOrigen() throws Exception {
        
        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        txtManifiestoOriginal.setEnabled(true);
        txtManifiestoOriginal.setEditable(false);
        
        txtCedulaConductorOrigen.setEnabled(true);
        txtCedulaConductorOrigen.setEditable(false);
        txtNombreAuxiliar1Origen.setEnabled(true);
        txtNombreAuxiliar1Origen.setEditable(false);
        txtNombreAuxiliar2Origen.setEnabled(true);
        txtNombreAuxiliar2Origen.setEditable(false);
        txtNombreAuxiliar3Origen.setEnabled(true);
        txtNombreAuxiliar3Origen.setEditable(false);
        txtNombreConductorOrigen.setEnabled(true);
        txtNombreConductorOrigen.setEditable(false);
        txtNombreDespachadorOrigen.setEnabled(true);
        txtNombreDespachadorOrigen.setEditable(false);
        txtPlacaOrigen.setEnabled(true);
        txtPlacaOrigen.setEditable(false);
        
        btnAgregar.setEnabled(false);
        btnAgregar.setEnabled(true);
        ;

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
//        manifiestoOrigen.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        //  manifiestoOrigen.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");
        
        int cantidadFacturasEnManifiesto = 0;
        
        modelo2 = (DefaultTableModel) tblFacturasPorManifiestoOrigen.getModel();
        
        int promedio = manifiestoOrigen.getListaFacturasPorManifiesto().size() / 100;
        int i = 0;
        int valorBarra = 0;
        
        double valor = 0.0;
        
        DefaultTableModel modelo = (DefaultTableModel) tblFacturasPorManifiestoOrigen.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int x = a; x >= 0; x--) {
                modelo.removeRow(x);
            }
        }
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO
        for (CFacturasPorManifiesto obj : manifiestoOrigen.getListaFacturasPorManifiesto()) {
            if (i > promedio) {
                i = 0;
                valorBarra++;
                this.barraOrigen.setValue(valorBarra);
                
            }

            // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
            // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
            filaTabla2 = tblFacturasPorManifiestoOrigen.getRowCount();
            modelo2.addRow(new Object[tblFacturasPorManifiestoOrigen.getRowCount()]);
            
            tblFacturasPorManifiestoOrigen.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
            tblFacturasPorManifiestoOrigen.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de la factura

            tblFacturasPorManifiestoOrigen.setValueAt(obj.getNombreDeCliente(), filaTabla2, 2); // cliente

            tblFacturasPorManifiestoOrigen.setValueAt(nf.format(obj.getValorARecaudarFactura()), filaTabla2, 3); // valor a recaudar  de la factura   

            // se ubica en la fila insertada
            tblFacturasPorManifiestoOrigen.changeSelection(filaTabla2, 0, false, false);
            
            cantidadFacturasEnManifiesto++;
            i++;
            
        }

        /* Se llenan os campos de texto de los auiliares*/
        llenarTxtAuxiliaresOrigen();

        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
        if (manifiestoOrigen.getDespachador().equals("0")) {
            txtNombreDespachadorOrigen.setText("");
            
        } else {
            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (manifiestoOrigen.getDespachador().equals(obj.getCedula())) {
                    
                    txtNombreDespachadorOrigen.setText(obj.getNombres() + " " + obj.getApellidos());
                }
            }
        }

        //btnExportar.setEnabled(true);
        // btnImprimir.setEnabled(true);
        this.barraOrigen.setValue(100);
        lblCirculoDeProgresoOrigen.setVisible(false);
        btnImprimir.requestFocus();
        
    }
    
    public synchronized void llenarDatosManifiestoDestino() throws Exception {
        
        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        txtManifiestoDestin.setEnabled(true);
        txtManifiestoDestin.setEditable(false);
        txtNumeroFactura.setEnabled(true);
        txtNumeroFactura.setEditable(true);
        
        txtCedulaConductorDestino.setEnabled(true);
        txtCedulaConductorDestino.setEditable(false);
        txtNombreAuxiliar1Destino.setEnabled(true);
        txtNombreAuxiliar1Destino.setEditable(false);
        txtNombreAuxiliar2Destino.setEnabled(true);
        txtNombreAuxiliar2Destino.setEditable(false);
        txtNombreAuxiliar3Destino.setEnabled(true);
        txtNombreAuxiliar3Destino.setEditable(false);
        txtNombreConductorDestino.setEnabled(true);
        txtNombreConductorDestino.setEditable(false);
        txtNombreDespachadorDestino.setEnabled(true);
        txtNombreDespachadorDestino.setEditable(false);
        txtPlacaDestino.setEnabled(true);
        txtPlacaDestino.setEditable(false);
        
        btnAgregar.setEnabled(false);
        btnAgregar.setEnabled(true);
        btnImprimir.setEnabled(false);

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
//        manifiestoOrigen.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        //  manifiestoOrigen.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");
        
        DefaultTableModel modelo = (DefaultTableModel) tblFacturasPorManifiestoDestino.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int x = a; x >= 0; x--) {
                modelo.removeRow(x);
            }
        }
        
        modelo2 = (DefaultTableModel) tblFacturasPorManifiestoDestino.getModel();
        
        double valor = 0.0;
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO
        for (CFacturasPorManifiesto obj : manifiestoDestino.getListaFacturasPorManifiesto()) {

            // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
            // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
            filaTabla2 = tblFacturasPorManifiestoDestino.getRowCount();
            modelo2.addRow(new Object[tblFacturasPorManifiestoDestino.getRowCount()]);
            
            tblFacturasPorManifiestoDestino.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
            tblFacturasPorManifiestoDestino.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de la factura

            tblFacturasPorManifiestoDestino.setValueAt(obj.getNombreDeCliente(), filaTabla2, 2); // cliente

            tblFacturasPorManifiestoDestino.setValueAt(nf.format(obj.getValorARecaudarFactura()), filaTabla2, 3); // valor a recaudar  de la factura   

            // se ubica en la fila insertada
            tblFacturasPorManifiestoDestino.changeSelection(filaTabla2, 0, false, false);

            // }
            // }
            //this.repaint();
        }

        /* Se llenan os campos de texto de los auiliares*/
        llenarTxtAuxiliaresDestino();

        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
        if (manifiestoDestino.getDespachador().equals("0")) {
            txtNombreDespachadorDestino.setText("");
            
        } else {
            for (CEmpleados obj : ini.getListaDeEmpleados()) {
                if (manifiestoDestino.getDespachador().equals(obj.getCedula())) {
                    
                    txtNombreDespachadorDestino.setText(obj.getNombres() + " " + obj.getApellidos());
                }
            }
        }

        //btnExportar.setEnabled(true);
        //btnImprimir.setEnabled(true);
        btnImprimir.requestFocus();
        
    }
    
    public void llenarTxtAuxiliaresOrigen() {
        /*se llenan los campos de texto de los nombres de los auxiliares*/
        int indice = 1;

//        txtNombreDeAuxiliar1.setText("SALE A DISTRIBUCION SIN AUXILIAR1");
//        txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR2");
//        txtNombreDeAuxiliar3.setText("SALE A DISTRIBUCION SIN AUXILIAR3");
        for (CEmpleados aux : manifiestoOrigen.getListaDeAuxiliares()) {
            switch (indice) {
                case 1:
                    if (aux.getCedula().equals("0")) {
                        txtNombreAuxiliar1Origen.setText("");
                    } else {
                        txtNombreAuxiliar1Origen.setText(aux.getNombres() + " " + aux.getApellidos());
                    }
                    indice++;
                    break;
                case 2:
                    if (aux.getCedula().equals("0")) {
                        txtNombreAuxiliar2Origen.setText("");
                    } else {
                        txtNombreAuxiliar2Origen.setText(aux.getNombres() + " " + aux.getApellidos());
                    }
                    indice++;
                    break;
                case 3:
                    if (aux.getCedula().equals("0")) {
                        txtNombreAuxiliar3Origen.setText("");
                    } else {
                        txtNombreAuxiliar3Origen.setText(aux.getNombres() + " " + aux.getApellidos());
                    }
                    indice++;
                    
                    break;
                
            }
            /* fin switch */
            
        }
    }
    
    public void llenarTxtAuxiliaresDestino() {
        /*se llenan los campos de texto de los nombres de los auxiliares*/
        int indice = 1;

//        txtNombreDeAuxiliar1.setText("SALE A DISTRIBUCION SIN AUXILIAR1");
//        txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR2");
//        txtNombreDeAuxiliar3.setText("SALE A DISTRIBUCION SIN AUXILIAR3");
        for (CEmpleados aux : manifiestoDestino.getListaDeAuxiliares()) {
            switch (indice) {
                case 1:
                    if (aux.getCedula().equals("0")) {
                        txtNombreAuxiliar1Destino.setText("");
                    } else {
                        txtNombreAuxiliar1Destino.setText(aux.getNombres() + " " + aux.getApellidos());
                    }
                    indice++;
                    break;
                case 2:
                    if (aux.getCedula().equals("0")) {
                        txtNombreAuxiliar2Destino.setText("");
                    } else {
                        txtNombreAuxiliar2Destino.setText(aux.getNombres() + " " + aux.getApellidos());
                    }
                    indice++;
                    break;
                case 3:
                    if (aux.getCedula().equals("0")) {
                        txtNombreAuxiliar3Destino.setText("");
                    } else {
                        txtNombreAuxiliar3Destino.setText(aux.getNombres() + " " + aux.getApellidos());
                    }
                    indice++;
                    
                    break;
                
            }
            /* fin switch */
            
        }
    }
    
    private void agregarFacturaALaTablaDestino(CFacturasPorManifiesto factura) {
        modelo2 = (DefaultTableModel) tblFacturasPorManifiestoDestino.getModel();
        
        double valor = 0.0;
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO

        // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
        // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
        filaTabla2 = tblFacturasPorManifiestoDestino.getRowCount();
        modelo2.addRow(new Object[tblFacturasPorManifiestoDestino.getRowCount()]);
        
        tblFacturasPorManifiestoDestino.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
        tblFacturasPorManifiestoDestino.setValueAt(factura.getNumeroFactura(), filaTabla2, 1); // numero de la factura

        tblFacturasPorManifiestoDestino.setValueAt(factura.getNombreDeCliente(), filaTabla2, 2); // cliente

        tblFacturasPorManifiestoDestino.setValueAt(nf.format(factura.getValorARecaudarFactura()), filaTabla2, 3); // valor a recaudar  de la factura   

        // se ubica en la fila insertada
        tblFacturasPorManifiestoDestino.changeSelection(filaTabla2, 0, false, false);
        
    }
    
    private boolean grabarTrasladoDeFacturas() {
        boolean guardado= false;
        int deseaGrabarRegistro;
        
        deseaGrabarRegistro = JOptionPane.showConfirmDialog(this, "Desea guardar el registro?", "Guardar registro", JOptionPane.YES_NO_OPTION);
        
        if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {
            // manifiestoDestino.setListaFacturasPorManifiesto(listaDeFacturasParaTrasladar);

            if (manifiestoDestino.grabarTrasladoDeFacturas(manifiestoOrigen.getNumeroManifiesto(), listaDeFacturasParaTrasladar)) {
                
                new Thread(new HiloConsultarManifiesto(this, 1)).start(); // Manifiesto origen (1)
                
                new Thread(new HiloConsultarManifiesto(this, 2)).start(); // Manifiesto Destino (2)               
                
                btnMostrarDocumento.setEnabled(true);
                btnImprimir.setEnabled(true);
                
                txtNumeroFactura.setEditable(false);
                guardado=true;
                
                JOptionPane.showInternalMessageDialog(this, "Datos guardados perfectamente en la BBDD ", "Ok", JOptionPane.INFORMATION_MESSAGE);
                
            } else {
                guardado= false;
                JOptionPane.showInternalMessageDialog(this, "Error al guardar los Datos en la  BBDD ", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        }
        return guardado;
    }
    
    private boolean borrarUnaFactura() {
        boolean borrado = false;
        int borrarFila;
        modelo2 = (DefaultTableModel) tblFacturasPorManifiestoDestino.getModel();

        /* se identifica la fila seleccionada de la tabla*/
        int fila = tblFacturasPorManifiestoDestino.getSelectedRow();

        /*Se identifica el numero de la factura contenida en la fila seleccionada*/
        String numFactura = modelo2.getValueAt(fila, 1).toString();
        
        if (manifiestoDestino.getEstadoManifiesto() == 4) {
            return true;
        }
        // VLIDA SI HAY ELEMENTOS EN EL JTABLE, SI TIENE MAS DE UN ELEMENTO
        if (tblFacturasPorManifiestoDestino.getRowCount() > 0) {

            // borrarFila = JOptionPane.showOptionDialog(this, "Desea eliminar el Registro de la Tabla?", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No"}, "No");
            borrarFila = JOptionPane.showConfirmDialog(this, "Desea eliminar la factura # " + numFactura + " de la Tabla ?", "Eliminar Fcatura # " + numFactura, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            // HA SELECCIONADO BORRAR LA FILA
            if (borrarFila == JOptionPane.YES_OPTION) {
                
                try {
                    /*Borra la factura del array factuas por manifiesto*/
                    for (CFacturasPorManifiesto obj : listaDeFacturasParaTrasladar) {
                        if (obj.getNumeroFactura().equals(numFactura)) {
                            listaDeFacturasParaTrasladar.remove(obj);
                            break;
                        }
                    }

                    /*Elimina la factura de la tabla*/
                    modelo2.removeRow(fila);

                    /*Organiza el Jtable de facturas por manifiesto*/
                    llenarjTableFacturasPormanifiesto();
                    
                } catch (Exception ex) {
                    Logger.getLogger(FTrasladoDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
        }
        return borrado;
    }
    
    private boolean borrarUnaFactura(String numeroFactura) {
        boolean borrado = false;
        int borrarFila;
        
        modelo2 = (DefaultTableModel) tblFacturasPorManifiestoDestino.getModel();

        /* se identifica la fila seleccionada de la tabla*/
        int fila = tblFacturasPorManifiestoDestino.getSelectedRow();
        
        for (fila = 0; fila < modelo2.getRowCount(); fila++) {
            /*Se identifica el numero de la factura contenida en la fila seleccionada*/
            if (numeroFactura.equals(modelo2.getValueAt(fila, 1).toString())) {
                
                try {
                    /*Borra la factura del array factuas por manifiesto*/
                    for (CFacturasPorManifiesto obj : manifiestoOrigen.getListaFacturasPorManifiesto()) {
                        if (obj.getNumeroFactura().equals(numeroFactura)) {
                            manifiestoOrigen.getListaFacturasPorManifiesto().remove(obj);
                            break;
                        }
                    }
                    modelo2.removeRow(fila);

                    /*Organiza el Jtable de facturas por manifiesto*/
                    llenarjTableFacturasPormanifiestoOrigen();
                    
                } catch (Exception ex) {
                    Logger.getLogger(FTrasladoDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }

        /*Se identifica el numero de la factura contenida en la fila seleccionada*/
        String numFactura = modelo2.getValueAt(fila, 1).toString();
        
        if (manifiestoDestino.getEstadoManifiesto() == 4) {
            return true;
        }
        // VLIDA SI HAY ELEMENTOS EN EL JTABLE, SI TIENE MAS DE UN ELEMENTO
        if (tblFacturasPorManifiestoDestino.getRowCount() > 0) {

            // borrarFila = JOptionPane.showOptionDialog(this, "Desea eliminar el Registro de la Tabla?", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No"}, "No");
            borrarFila = JOptionPane.showConfirmDialog(this, "Desea eliminar la factura # " + numFactura + " de la Tabla ?", "Eliminar Fcatura # " + numFactura, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            // HA SELECCIONADO BORRAR LA FILA
            if (borrarFila == JOptionPane.YES_OPTION) {
                
                try {
                    /*Borra la factura del array factuas por manifiesto*/
                    for (CFacturasPorManifiesto obj : listaDeFacturasParaTrasladar) {
                        if (obj.getNumeroFactura().equals(numFactura)) {
                            listaDeFacturasParaTrasladar.remove(obj);
                            break;
                        }
                    }

                    /*Elimina la factura de la tabla*/
                    modelo2.removeRow(fila);

                    /*Organiza el Jtable de facturas por manifiesto*/
                    llenarjTableFacturasPormanifiesto();
                    
                } catch (Exception ex) {
                    Logger.getLogger(FTrasladoDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
        }
        return borrado;
    }
    
    private boolean borrarFilasFacturaTrasladada(List<CFacturasPorManifiesto> lista) {
        boolean borrado = false;
        int borrarFila;
        
        modelo2 = (DefaultTableModel) tblFacturasPorManifiestoDestino.getModel();
        
        for (CFacturasPorManifiesto fxm : lista) {
            for (int fila = 0; fila < modelo2.getRowCount(); fila++) {
                /*Se identifica el numero de la factura contenida en la fila seleccionada*/
                if (fxm.getNumeroFactura().equals(modelo2.getValueAt(fila, 1).toString())) {
                    
                    try {
                        /*Borra la factura del array factuas por manifiesto*/
                        for (CFacturasPorManifiesto obj : manifiestoOrigen.getListaFacturasPorManifiesto()) {
                            if (obj.getNumeroFactura().equals(fxm.getNumeroFactura())) {
                                manifiestoOrigen.getListaFacturasPorManifiesto().remove(obj);
                                break;
                            }
                        }
                        modelo2.removeRow(fila);
                        borrado = true;

                        /*Organiza el Jtable de facturas por manifiesto*/
                        //llenarjTableFacturasPormanifiestoOrigen();
                    } catch (Exception ex) {
                        Logger.getLogger(FTrasladoDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
                        borrado = false;
                    }
                    
                }
            }
        }
        
        return borrado;
    }
    
    private boolean borrarTodasLasFacturas() {
        boolean borrado = false;
        int borraTodaslasFacturas;
        modelo2 = (DefaultTableModel) tblFacturasPorManifiestoDestino.getModel();

        /* se identifica la fila seleccionada de la tabla*/
        int fila = tblFacturasPorManifiestoDestino.getSelectedRow();

        /*Se identifica el numero de la factura contenida en la fila seleccionada*/
        String numFactura = modelo2.getValueAt(fila, 1).toString();
        
        if (manifiestoDestino.getEstadoManifiesto() == 4) {
            return true;
        }
        // VLIDA SI HAY ELEMENTOS EN EL JTABLE, SI TIENE MAS DE UN ELEMENTO
        if (tblFacturasPorManifiestoDestino.getRowCount() > 0) {

            // borrarFila = JOptionPane.showOptionDialog(this, "Desea eliminar el Registro de la Tabla?", "Eliminar Registro", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Si", "No"}, "No");
            borraTodaslasFacturas = JOptionPane.showConfirmDialog(this, "Desea eliminar todas las facturas de la Tabla ?", "Eliminar Facturas ", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            // HA SELECCIONADO BORRAR LA FILA
            if (borraTodaslasFacturas == JOptionPane.YES_OPTION) {
                
                try {
                    /*Borra la factura del array facturas por manifiesto*/
                    listaDeFacturasParaTrasladar = new ArrayList();

                    /*Elimina la factura de la tabla*/
                    modelo2 = null;

                    /*Organiza el Jtable de facturas por manifiesto*/
                    llenarjTableFacturasPormanifiesto();
                    
                } catch (Exception ex) {
                    Logger.getLogger(FTrasladoDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
            
        }
        return borrado;
    }
    
    public void llenarjTableFacturasPormanifiestoOrigen() throws Exception {
        DefaultTableModel modelo = (DefaultTableModel) tblFacturasPorManifiestoOrigen.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        
        modelo2 = null;
        
        modelo2 = (DefaultTableModel) tblFacturasPorManifiestoOrigen.getModel();
        
        for (CFacturasPorManifiesto obj : manifiestoOrigen.getListaFacturasPorManifiesto()) {
            filaTabla2 = tblFacturasPorManifiestoOrigen.getRowCount();
            modelo2.addRow(new Object[tblFacturasPorManifiestoOrigen.getRowCount()]);
            
            tblFacturasPorManifiestoOrigen.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
            tblFacturasPorManifiestoOrigen.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de factura
            tblFacturasPorManifiestoOrigen.setValueAt(obj.getNombreDeCliente(), filaTabla2, 2); // nombre del nombreDelCliente
            tblFacturasPorManifiestoOrigen.setValueAt(nf.format(obj.getValorTotalFactura()), filaTabla2, 3);
        }

//     
    }
    
    public void llenarjTableFacturasPormanifiesto() throws Exception {
        DefaultTableModel modelo = (DefaultTableModel) tblFacturasPorManifiestoDestino.getModel();
        if (modelo.getRowCount() > 0) {
            int a = modelo.getRowCount() - 1;
            for (int i = a; i >= 0; i--) {
                modelo.removeRow(i);
            }
        }
        
        if (!listaDeFacturasParaTrasladar.isEmpty()) {
            
            modelo2 = null;
            
            modelo2 = (DefaultTableModel) tblFacturasPorManifiestoDestino.getModel();
            
            for (CFacturasPorManifiesto obj : listaDeFacturasParaTrasladar) {
                filaTabla2 = tblFacturasPorManifiestoDestino.getRowCount();
                modelo2.addRow(new Object[tblFacturasPorManifiestoDestino.getRowCount()]);
                
                tblFacturasPorManifiestoDestino.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0);  // item
                tblFacturasPorManifiestoDestino.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de factura
                tblFacturasPorManifiestoDestino.setValueAt(obj.getNombreDeCliente(), filaTabla2, 2); // nombre del nombreDelCliente
                tblFacturasPorManifiestoDestino.setValueAt(nf.format(obj.getValorTotalFactura()), filaTabla2, 3);
            }
        }
//     

    }
}
