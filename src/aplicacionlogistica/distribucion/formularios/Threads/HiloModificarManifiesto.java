/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloConsultarManifiesto;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.FModificarManifiesto;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
//import com.spire.ms.System.Collections.ArrayList;
import java.awt.HeadlessException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import mtto.vehiculos.CVehiculos;

/**
 *
 * @author Usuario
 */
public class HiloModificarManifiesto implements Runnable {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    FModificarManifiesto fModificarManifiesto = null;
    String caso;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloModificarManifiesto(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fModificarManifiesto
     * @param comando
     */
    public HiloModificarManifiesto(Inicio ini, FModificarManifiesto fModificarManifiesto, String comando) {
        this.ini = ini;
        this.fModificarManifiesto = fModificarManifiesto;
        this.caso = comando;
    }

    @Override
    public void run() {

        if (caso != null) {
            switch (caso) {
                case "grabarManifiesto":
                    grabar();
                    break;
                case "buscarManifiesto": 
                    try {
                    llenarDatosManifiestoParaModificar();

                } catch (Exception ex) {
                    Logger.getLogger(HiloModificarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
                }

                case "actualizarKilometraje":
                    actualizarKilometraje();

                    break;
                case "atualizarManifiestos":
                    actualizarManifiestos();
                    break;

            }
        }
    }

    public synchronized void llenarDatosManifiestoParaModificar() throws Exception {
        System.out.println("Trae los datos del manifiesto -> \n\n");
        fModificarManifiesto.manifiestoActual = null;
        try {

            if (this.fModificarManifiesto == null) {
                JOptionPane.showInternalMessageDialog(null, "formulario modificar manifiesto es igual  a null",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            this.fModificarManifiesto.lblCirculoDeProgreso1.setVisible(true);
            fModificarManifiesto.manifiestoActual = null;

            // fModificarManifiesto.manifiestoActual = new CManifiestosDeDistribucion(ini); //, Integer.parseInt(fModificarManifiesto.txtNumeroDeManifiesto.getText()));
            for (CManifiestosDeDistribucion manif : ini.getListaDeManifiestossinDescargar()) {
                if (fModificarManifiesto.txtNumeroDeManifiesto.getText().trim().equals(manif.getNumeroManifiesto())) {
                    fModificarManifiesto.manifiestoActual = manif;
                    break;
                }
            }

            if (fModificarManifiesto.manifiestoActual == null) {
                JOptionPane.showInternalMessageDialog(this.fModificarManifiesto, "Ese manifiesto No Existe en la BB DD",
                        "Error", JOptionPane.ERROR_MESSAGE);
                this.fModificarManifiesto.lblCirculoDeProgreso1.setVisible(false);
                return;
            }
            if (fModificarManifiesto.manifiestoActual.getEstadoManifiesto() >= 4) {
                JOptionPane.showInternalMessageDialog(this.fModificarManifiesto, "Ese manifiesto no se puede modificar en la BB DD",
                        "Error", JOptionPane.WARNING_MESSAGE);
                this.fModificarManifiesto.lblCirculoDeProgreso1.setVisible(false);

                return;
            }

            this.fModificarManifiesto.manifiestoActual.setListaFacturasPorManifiesto();

            //this.fModificarManifiesto.listaDeFacturasPorManifiesto = new ArrayList();
            llenarDatosManifiestoAModificar();

            this.fModificarManifiesto.lblCirculoDeProgreso1.setVisible(false);


            /*Se habilita el boton de imprimir para el reporte de las facturas por impresora*/
            if (this.fModificarManifiesto.manifiestoActual.getListaFacturasPorManifiesto() != null) {
                fModificarManifiesto.btnImprimir.setEnabled(true);
                fModificarManifiesto.btnGrabar.setEnabled(true);
            }

            this.fModificarManifiesto.lblCirculoDeProgreso1.setVisible(false);

            // this.fModificarManifiesto.txtKmDeSalida.requestFocus();
        } catch (Exception ex) {
            JOptionPane.showInternalMessageDialog(this.fModificarManifiesto, "Ese manifiesto no se puede modificar en la BB DD",
                    "Error", JOptionPane.WARNING_MESSAGE);
            Logger.getLogger(HiloConsultarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            this.fModificarManifiesto.lblCirculoDeProgreso1.setVisible(false);

        }

    }

    public boolean grabar() throws HeadlessException {
        if (this.fModificarManifiesto == null) {
            JOptionPane.showInternalMessageDialog(null, "formulario modificar manifiesto es igual  a null",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int deseaGrabarRegistro = 0;

        deseaGrabarRegistro = JOptionPane.showConfirmDialog(this.fModificarManifiesto, "Desea Actualizar el registro en la BBDD ?", "Guardar registro", JOptionPane.YES_NO_OPTION);

        if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {

            this.fModificarManifiesto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
            this.fModificarManifiesto.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif"))); // NOI18N

            try {
                /*Se crea un objeto ruta de distribucion*/
                for (CRutasDeDistribucion obj : ini.getListaDeRutasDeDistribucion()) {
                    if (obj.getNombreRutasDeDistribucion().equals(this.fModificarManifiesto.cbxRutaDeDistribucion.getSelectedItem().toString())) {
                        this.fModificarManifiesto.ruta = new CRutasDeDistribucion(ini);
                        this.fModificarManifiesto.ruta = obj;
                        break;
                    }
                }

                /*Se crea un objeto canal de distribucion*/
                for (CCanalesDeVenta obj : ini.getListaDeCanalesDeVenta()) {
                    if (obj.getNombreCanalDeVenta().equals(this.fModificarManifiesto.cbxCanales.getSelectedItem().toString())) {
                        this.fModificarManifiesto.canalDeVenta = new CCanalesDeVenta(ini);
                        this.fModificarManifiesto.canalDeVenta = obj;
                        break;
                    }
                }

                this.fModificarManifiesto.txtKmDeSalida.setEditable(false);
                this.fModificarManifiesto.txtPlaca.setEnabled(false);
                this.fModificarManifiesto.txtPlaca.setEditable(false);

                asignarValoresManifiesto();

                /*SE ASIGNAN LOS AUXILIARES */
                asignarListaDeauxiliares();
                if (this.fModificarManifiesto.manifiestoActual.actualizarManifiestoDeDistribucions()) {
                    this.fModificarManifiesto.btnGrabar.setEnabled(false);
                    this.fModificarManifiesto.jBtnGrabar.setEnabled(false);
                    this.fModificarManifiesto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
                    this.fModificarManifiesto.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));

                    this.fModificarManifiesto.txtNumeroDeManifiesto.setEnabled(false);
                    this.fModificarManifiesto.txtnombreDeConductor.setEnabled(false);
                    this.fModificarManifiesto.cbxCanales.setEnabled(false);
                    this.fModificarManifiesto.cbxRutaDeDistribucion.setEnabled(false);
                    this.fModificarManifiesto.dateManifiesto.setEnabled(false);

                    JOptionPane.showMessageDialog(null, "Manifiesto de distribucion modificado satisfactoriamente", "manifiesto modificado", JOptionPane.INFORMATION_MESSAGE);
                    new Thread(new HiloListadoDeManifiestosSinDescargar(ini)).start();;

                    return true;
                } else {
                    this.fModificarManifiesto.btnGrabar.setEnabled(false);
                    this.fModificarManifiesto.jBtnGrabar.setEnabled(false);
                    this.fModificarManifiesto.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/grabar.png")));
                    this.fModificarManifiesto.jBtnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png")));
                    JOptionPane.showMessageDialog(null, "Error al guardar los datos", "manifiesto ya fue guardado", JOptionPane.ERROR_MESSAGE, null);
                    return false;
                }  // FIN DEL IF-> ESTADO DEL MANIFIESTO
            } catch (Exception ex) {
                Logger.getLogger(FModificarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return false;

    }

    public void llenarDatosManifiestoAModificar() throws Exception {

        // SE CREA UN ARRAY DE LOS OBJETOS FACTURAS QUE ESTAN EN EL MANIFIESTO
        System.out.println("trae las facturas del manifiesto -> ");
        //manifiestoActual.setListaCFacturasCamdun(); // CfacturasCamdun
        System.out.println("trae la listade facturaspor manifiesto -> ");
        // manifiestoActual.setListaFacturasPorManifiesto();
        System.out.println("trae los datos de la vista facturas en manifiesto -> ");

        DefaultTableModel modelo2 = (DefaultTableModel) this.fModificarManifiesto.jTableFacturasPorManifiesto.getModel();

        double valor = 0.0;
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO
        for (CFacturasPorManifiesto obj : this.fModificarManifiesto.manifiestoActual.getListaFacturasPorManifiesto()) {

            int filaTabla2 = this.fModificarManifiesto.jTableFacturasPorManifiesto.getRowCount();
            modelo2.addRow(new Object[this.fModificarManifiesto.jTableFacturasPorManifiesto.getRowCount()]);

            this.fModificarManifiesto.jTableFacturasPorManifiesto.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
            this.fModificarManifiesto.jTableFacturasPorManifiesto.setValueAt(obj.getNumeroFactura(), filaTabla2, 1); // numero de la factura

            this.fModificarManifiesto.jTableFacturasPorManifiesto.setValueAt(obj.getNombreDeCliente(), filaTabla2, 2); // cliente

            this.fModificarManifiesto.jTableFacturasPorManifiesto.setValueAt(nf.format(obj.getValorARecaudarFactura()), filaTabla2, 3); // valor a recaudar  de la factura   

            // se ubica en la fila insertada
            this.fModificarManifiesto.jTableFacturasPorManifiesto.changeSelection(filaTabla2, 0, false, false);

            valor += obj.getValorARecaudarFactura();
        }
        
        String placa = this.fModificarManifiesto.manifiestoActual.getVehiculo();
        this.fModificarManifiesto.txtCantidadDePedidos.setText("" + this.fModificarManifiesto.manifiestoActual.getCantidadPedidos());
        this.fModificarManifiesto.txtPesoKgManifiesto.setText("" + (this.fModificarManifiesto.manifiestoActual.getPesoKgManifiesto()/1000));
       
                                double cad = this.fModificarManifiesto.manifiestoActual.getValorTotalManifiesto();
                             String cadena = String.format("%.0f", cad);
                             Double val =Double.parseDouble(cadena);
                                //cliente.setCelularCliente(cadena);
        this.fModificarManifiesto.txtValorTotalManifiesto.setText(nf.format(cad));

        this.fModificarManifiesto.txtPlaca.setText(placa);

        this.fModificarManifiesto.txtKmDeSalida.setText("" + this.fModificarManifiesto.manifiestoActual.getKmSalida());

        // SE BUSCA EL CONDUCTOR Y SE LLENA EL CAMPO
        this.fModificarManifiesto.conductor = new CEmpleados();
        this.fModificarManifiesto.conductor.setCedula(this.fModificarManifiesto.manifiestoActual.getConductor());
        this.fModificarManifiesto.txtnombreDeConductor.setText(this.fModificarManifiesto.manifiestoActual.getNombreConductor() + " "
                + this.fModificarManifiesto.manifiestoActual.getApellidosConductor());

        this.fModificarManifiesto.listaDeAuxiliares = this.fModificarManifiesto.manifiestoActual.getListaDeAuxiliares(""
                + this.fModificarManifiesto.manifiestoActual.getNumeroManifiesto());


        /* Se llenan os campos de texto de los auiliares*/
        llenarTxtAuxiliares();

        // SE BUSCA EL DESPACHADOR Y SE LLENA EL CAMPO
        if (this.fModificarManifiesto.manifiestoActual.getDespachador().equals("0")) {
            this.fModificarManifiesto.txtNombreDedespachador.setText("");

        } else {
            this.fModificarManifiesto.despachador = new CEmpleados();
            this.fModificarManifiesto.despachador.setCedula(this.fModificarManifiesto.manifiestoActual.getDespachador());

            this.fModificarManifiesto.txtNombreDedespachador.setText(this.fModificarManifiesto.manifiestoActual.getNombreDespachador()
                    + " " + this.fModificarManifiesto.manifiestoActual.getApellidosDespachador());

        }

        // SE BUSCA EL CANAL Y SE LLENA EL CAMPO
        this.fModificarManifiesto.cbxCanales.setSelectedItem(this.fModificarManifiesto.manifiestoActual.getNombreCanal());

        // SE BUSCA EL RUTA Y SE LLENA EL CAMPO
        this.fModificarManifiesto.cbxRutaDeDistribucion.setSelectedItem(this.fModificarManifiesto.manifiestoActual.getNombreDeRuta());

        SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
        String strFecha = this.fModificarManifiesto.manifiestoActual.getFechaDistribucion();
        Date fecha = null;
        fecha = formatoDelTexto.parse(strFecha);
        this.fModificarManifiesto.dateManifiesto.setDate(fecha);

        // dateManifiesto.setDate(Inicio.getFechaSql(manifiestoActual.getFechaDistribucion()));
        this.fModificarManifiesto.manifiestoActual.setValorTotalManifiesto(valor);

        this.fModificarManifiesto.btnImprimir.setEnabled(true);
        this.fModificarManifiesto.jBtnImprimir.setEnabled(true);

        System.out.println("empieza SE DESHABILITAN LOS COMPONENETES  en la vista de usuario -> \n\n");
        // SE DESHABILITAN LOS COMPONENETES

        this.fModificarManifiesto.txtnombreDeConductor.setEditable(true);
        this.fModificarManifiesto.txtnombreDeConductor.setEnabled(true);

        this.fModificarManifiesto.txtPlaca.setEditable(true);
        this.fModificarManifiesto.txtPlaca.setEnabled(true);

        this.fModificarManifiesto.txtKmDeSalida.setEditable(true);
        this.fModificarManifiesto.txtKmDeSalida.setEnabled(true);

        this.fModificarManifiesto.txtNombreDeAuxiliar1.setEditable(true);
        this.fModificarManifiesto.txtNombreDeAuxiliar1.setEnabled(true);

        this.fModificarManifiesto.txtNombreDeAuxiliar2.setEditable(true);
        this.fModificarManifiesto.txtNombreDeAuxiliar2.setEnabled(true);

        this.fModificarManifiesto.txtNombreDedespachador.setEditable(true);
        this.fModificarManifiesto.txtNombreDedespachador.setEnabled(true);

        this.fModificarManifiesto.cbxCanales.setEnabled(true);

        this.fModificarManifiesto.cbxRutaDeDistribucion.setEnabled(true);
        this.fModificarManifiesto.dateManifiesto.setEnabled(true);

        this.fModificarManifiesto.btnNuevo.setEnabled(false);
        this.fModificarManifiesto.jBtnNuevo.setEnabled(false);

        this.fModificarManifiesto.btnImprimir.setEnabled(true);
        this.fModificarManifiesto.jBtnImprimir.setEnabled(true);

        this.fModificarManifiesto.btnGrabar.setEnabled(true);
        this.fModificarManifiesto.jBtnGrabar.setEnabled(true);

        this.fModificarManifiesto.dateManifiesto.setEnabled(true);

        this.fModificarManifiesto.repaint();

        // this.fModificarManifiesto.btnImprimir.requestFocus();
    }

    private void llenarTxtAuxiliares() {
        /*se llenan los campos de texto de los nombres de los auxiliares*/
        int indice = 1;

        this.fModificarManifiesto.txtNombreDeAuxiliar1.setText("SALE A DISTRIBUCION SIN AUXILIAR1");
        this.fModificarManifiesto.txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR2");

        this.fModificarManifiesto.listaDeAuxiliares = this.fModificarManifiesto.manifiestoActual.getListaDeAuxiliares(""
                + this.fModificarManifiesto.manifiestoActual.getNumeroManifiesto());

        if (this.fModificarManifiesto.listaDeAuxiliares.size() > 0) {
            for (CEmpleados aux : this.fModificarManifiesto.listaDeAuxiliares) {
                switch (indice) {
                    case 1:
                        if (aux.getCedula().equals("0")) {
                            this.fModificarManifiesto.txtNombreDeAuxiliar1.setText("SALE A DISTRIBUCION SIN AUXILIAR1");
                        } else {
                            this.fModificarManifiesto.txtNombreDeAuxiliar1.setText(aux.getNombres() + " " + aux.getApellidos());
                        }
                        indice++;
                        break;
                    case 2:
                        if (aux.getCedula().equals("0")) {
                            this.fModificarManifiesto.txtNombreDeAuxiliar2.setText("SALE A DISTRIBUCION SIN AUXILIAR2");
                        } else {
                            this.fModificarManifiesto.txtNombreDeAuxiliar2.setText(aux.getNombres() + " " + aux.getApellidos());
                        }
                        indice++;
                        break;

                }
                /* fin switch */

            }
        }

    }

    private void traerFuncionario(String nombreFuncionario, String rol) {
        String nombre = "";
        for (CEmpleados funcionario : ini.getListaDeEmpleados()) {
            nombre = funcionario.getNombres() + " " + funcionario.getApellidos();

            if (nombreFuncionario.equals(nombre)) {
                switch (rol) {
                    case "conductor":
                        this.fModificarManifiesto.conductor = funcionario;
                        break;
                    case "auxiliar1":
                        this.fModificarManifiesto.auxiliar1 = funcionario;
                        break;
                    case "auxiliar2":
                        this.fModificarManifiesto.auxiliar2 = funcionario;
                        break;
                    case "despachador":
                        this.fModificarManifiesto.despachador = funcionario;
                        break;
                }
            }

        }
    }

    private void asignarValoresManifiesto() {
        try {
            /*  SE LLENAN LAS PROPIEDADES DE  EL MANIFIESTO */
            Date dt = new Date();
            dt = ini.getFechaSql(this.fModificarManifiesto.dateManifiesto);

            this.fModificarManifiesto.manifiestoActual.setFechaDistribucion("" + dt);

            this.fModificarManifiesto.manifiestoActual.setVehiculo(this.fModificarManifiesto.txtPlaca.getText().trim());

            traerFuncionario(this.fModificarManifiesto.txtnombreDeConductor.getText().trim(), "conductor");
            this.fModificarManifiesto.manifiestoActual.setConductor(this.fModificarManifiesto.conductor.getCedula());
            this.fModificarManifiesto.manifiestoActual.setNombreConductor(this.fModificarManifiesto.conductor.getNombres());
            this.fModificarManifiesto.manifiestoActual.setApellidosConductor(this.fModificarManifiesto.conductor.getApellidos());

            traerFuncionario(this.fModificarManifiesto.txtNombreDedespachador.getText().trim(), "despachador");
            this.fModificarManifiesto.manifiestoActual.setDespachador(this.fModificarManifiesto.despachador.getCedula());
            this.fModificarManifiesto.manifiestoActual.setNombreDespachador(this.fModificarManifiesto.despachador.getNombres());
            this.fModificarManifiesto.manifiestoActual.setApellidosDespachador(this.fModificarManifiesto.despachador.getApellidos());

            for (CCanalesDeVenta canal : ini.getListaDeCanalesDeVenta()) {
                if (canal.getNombreCanalDeVenta().equals(this.fModificarManifiesto.cbxCanales.getSelectedItem().toString())) {
                    this.fModificarManifiesto.manifiestoActual.setIdCanal(canal.getIdCanalDeVenta());
                }
            }

            for (CRutasDeDistribucion ruta : ini.getListaDeRutasDeDistribucion()) {
                if (ruta.getNombreDeRuta().equals(this.fModificarManifiesto.cbxRutaDeDistribucion.getSelectedItem().toString())) {
                    this.fModificarManifiesto.manifiestoActual.setIdCanal(ruta.getIdRutasDeDistribucion());
                }
            }

            actualizarKilometraje();

            this.fModificarManifiesto.manifiestoActual.setUsuarioManifiesto(Inicio.deCifrar(ini.getUser().getNombreUsuario()));

        } catch (Exception ex) {
            Logger.getLogger(HiloModificarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void actualizarKilometraje() {
        for (CVehiculos carro : ini.getListaDeVehiculos()) {
            if (carro.getPlaca().equals(this.fModificarManifiesto.txtPlaca.getText().trim())) {
                carro.setKilometrajeActual();
                this.fModificarManifiesto.manifiestoActual.setKmSalida(carro.getKilometrajeActual());
                this.fModificarManifiesto.txtKmDeSalida.setText("" + carro.getKilometrajeActual());
                break;
            }
        }
    }

    private void asignarListaDeauxiliares() {
        List<CEmpleados> lista = new ArrayList<>();
        try {
            if (!this.fModificarManifiesto.txtNombreDeAuxiliar1.getText().trim().equals("SALE A DISTRIBUCION SIN AUXILIAR1")) {
                if (this.fModificarManifiesto.txtNombreDeAuxiliar1.getText().trim().equals("")) {

                    traerFuncionario(this.fModificarManifiesto.txtNombreDeAuxiliar1.getText().trim(), "auxiliar1");

                    lista.add(this.fModificarManifiesto.auxiliar1);

                }
            }
            if (!this.fModificarManifiesto.txtNombreDeAuxiliar2.getText().trim().equals("SALE A DISTRIBUCION SIN AUXILIAR2")) {
                if (this.fModificarManifiesto.txtNombreDeAuxiliar2.getText().trim().equals("")) {
                    traerFuncionario(this.fModificarManifiesto.txtNombreDeAuxiliar1.getText().trim(), "auxiliar2");
                    lista.add(this.fModificarManifiesto.auxiliar1);

                }
            }

            if (lista.size() > 0) {
                this.fModificarManifiesto.manifiestoActual.setListaDeAuxiliares(lista);
            }

        } catch (Exception ex) {
            Logger.getLogger(HiloModificarManifiesto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void actualizarManifiestos() {
        this.fModificarManifiesto.btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeprogreso.gif")));
        ini.setListaDeManifiestossinDescargar(3, true, "");
        this.fModificarManifiesto.btnActualizar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Sync.png")));

    }
}
