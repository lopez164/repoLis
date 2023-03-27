/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloConsultarManifiesto;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.FModificarManifiesto;
import aplicacionlogistica.distribucion.formularios.FTrasladoDeFacturas;
import aplicacionlogistica.distribucion.objetos.CCanalesDeVenta;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CRutasDeDistribucion;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import java.awt.HeadlessException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloFtrasladoDeFActuras implements Runnable {

    Inicio ini = null;
    FTrasladoDeFacturas fTrasladoDeFacturas;
    String caso;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFtrasladoDeFActuras(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fModificarManifiesto
     * @param comando
     */
    public HiloFtrasladoDeFActuras(Inicio ini, FTrasladoDeFacturas fTrasladoDeFacturas, String comando) {
        this.ini = ini;
        this.fTrasladoDeFacturas = fTrasladoDeFacturas;
        this.caso = comando;
    }

    @Override
    public void run() {

        if (caso != null) {
            switch (caso) {
                case "ConsultarManifiestoOrigen":
                    int caso = 1;
                    new Thread(new HiloConsultarManifiesto(this.fTrasladoDeFacturas, caso)).start();
                    break;

                case "ConsultarManifiestoDestino":
                    caso = 2;
                    new Thread(new HiloConsultarManifiesto(this.fTrasladoDeFacturas, caso)).start();
                    break;

                case "llamarRegistro":
                    llamarRegistro();
                    break;

                case "grabarTrasladoDeFacturas":
                    grabarTrasladoDeFacturas();

                    break;

            }
        }
    }

    private void llamarRegistro() {

        try {
            boolean encontrado = false;
            boolean facturaEncontrada = false;
            CFacturasPorManifiesto factura = null;

            String numeroFactura = this.fTrasladoDeFacturas.cbxPrefijos.getSelectedItem().toString()
                    + this.fTrasladoDeFacturas.txtNumeroFactura.getText().trim();

            CFacturasPorManifiesto facxMan = new CFacturasPorManifiesto(ini, numeroFactura,
                    this.fTrasladoDeFacturas.txtManifiestoOrigen.getText().trim());

            if (facxMan.getNumeroFactura() == null) {
                JOptionPane.showInternalMessageDialog(this.fTrasladoDeFacturas, "Ese numero de factura, no existe en el manifiesto de origen", "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (facxMan.getDespachado() > 2) {
                JOptionPane.showInternalMessageDialog(this.fTrasladoDeFacturas, "Ese numero de factura, no se le puede hacer traslado de manifiesto",
                        "Factura en reparto", JOptionPane.ERROR_MESSAGE);

                return;
            }

            if (facxMan.getEstadoFactura() < 1) {
                JOptionPane.showInternalMessageDialog(this.fTrasladoDeFacturas, "Ese numero de factura, no se le puede hacer \n traslado de manifiesto, ya tiene movimiento ",
                        "Factura con Movimiento", JOptionPane.ERROR_MESSAGE);

                return;
            }

            factura = null;
            /*Se valida si el numero de la factura ya existe en el manifiesto destino*/
            for (CFacturasPorManifiesto obj : this.fTrasladoDeFacturas.manifiestoDestino.getListaFacturasPorManifiesto()) {
                if (obj.getNumeroFactura().equals(numeroFactura)) {
                    encontrado = true;
                    factura = obj;
                    break;
                }
                /*Si la factura existe se sale de la funcion para que la ignore*/
                if (encontrado) {
                    return;
                }
            }

            factura = null;
            /*Se valida si el numero de la factura existe en el manifiesto origen*/
            for (CFacturasPorManifiesto obj : this.fTrasladoDeFacturas.manifiestoOrigen.getListaFacturasPorManifiesto()) {
                if (obj.getNumeroFactura().equals(numeroFactura)) {
                    encontrado = true;
                    factura = obj;
                    break;
                }
            }
            /*Si la factura no existe, entonce lanza una alerta para informar que no
                existe en el manifiesto origen*/
            if (factura == null) {
                JOptionPane.showInternalMessageDialog(this.fTrasladoDeFacturas, "Ese numero de factura, no existe en el manifiesto de origen", "Error", JOptionPane.WARNING_MESSAGE);

                /*Si la encuentra se procede a  validar  si se encuentra el numero de la
                    factura para trasladar*/
            } else {
                for (CFacturasPorManifiesto obj : this.fTrasladoDeFacturas.listaDeFacturasParaTrasladar) {
                    if (obj.getNumeroFactura().equals(factura.getNumeroFactura())) {
                        facturaEncontrada = true;
                        break;
                    }

                }
                /*Si el numero de factura  no esta en la lista se procede a agregarla*/
                if (!facturaEncontrada) {
                    this.fTrasladoDeFacturas.listaDeFacturasParaTrasladar.add(facxMan);
                    agregarFacturaALaTablaDestino(facxMan);
                    this.fTrasladoDeFacturas.btnGrabar.setEnabled(true);
                }

            }
            this.fTrasladoDeFacturas.cbxPrefijos.requestFocus();
            this.fTrasladoDeFacturas.txtNumeroFactura.requestFocus();
        } catch (Exception ex) {
            Logger.getLogger(FTrasladoDeFacturas.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void agregarFacturaALaTablaDestino(CFacturasPorManifiesto factura) {
        DefaultTableModel modelo2 = (DefaultTableModel) this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.getModel();
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
        double valor = 0.0;
        // SE LLENA LA TABLA DE FACTURAS POR MANIFIESTO

        // for (CFacturasPorManifiesto obj2 : listaDeFacturasPorManifiesto) {
        // if (obj2.getNumeroFactura().equals(obj.getNumeroFactura())) {
        int filaTabla2 = this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.getRowCount();
        modelo2.addRow(new Object[this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.getRowCount()]);

        this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.setValueAt("" + (filaTabla2 + 1), filaTabla2, 0); // item 
        this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.setValueAt(factura.getNumeroFactura(), filaTabla2, 1); // numero de la factura

        this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.setValueAt(factura.getNombreDeCliente(), filaTabla2, 2); // cliente

        this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.setValueAt(nf.format(factura.getValorARecaudarFactura()), filaTabla2, 3); // valor a recaudar  de la factura   

        // se ubica en la fila insertada
        this.fTrasladoDeFacturas.tblFacturasPorManifiestoDestino.changeSelection(filaTabla2, 0, false, false);

    }

    private void grabarTrasladoDeFacturas() {
        boolean guardado = false;
        int deseaGrabarRegistro;

        deseaGrabarRegistro = JOptionPane.showConfirmDialog(this.fTrasladoDeFacturas, "Desea guardar el registro?", "Guardar registro", JOptionPane.YES_NO_OPTION);

        if (deseaGrabarRegistro == JOptionPane.YES_OPTION) {
            // manifiestoDestino.setListaFacturasPorManifiesto(listaDeFacturasParaTrasladar);

            if (this.fTrasladoDeFacturas.manifiestoDestino.grabarTrasladoDeFacturas(this.fTrasladoDeFacturas.manifiestoOrigen.getNumeroManifiesto(),
                    this.fTrasladoDeFacturas.listaDeFacturasParaTrasladar)) {
                guardado = true;
                this.fTrasladoDeFacturas.setGrabado(true);

                new Thread(new HiloConsultarManifiesto(this.fTrasladoDeFacturas, 1)).start(); // Manifiesto origen (1)

                new Thread(new HiloConsultarManifiesto(this.fTrasladoDeFacturas, 2)).start(); // Manifiesto Destino (2)               

                this.fTrasladoDeFacturas.btnMostrarDocumento.setEnabled(true);
                this.fTrasladoDeFacturas.btnImprimir.setEnabled(true);

                this.fTrasladoDeFacturas.txtNumeroFactura.setEditable(false);

                this.fTrasladoDeFacturas.txtNumeroFactura.setEnabled(false);
                this.fTrasladoDeFacturas.btnGrabar.setEnabled(false);
                this.fTrasladoDeFacturas.btnCancelar.setEnabled(false);

                JOptionPane.showInternalMessageDialog(this.fTrasladoDeFacturas, "Datos guardados perfectamente en la BBDD ", "Ok", JOptionPane.INFORMATION_MESSAGE);

                if (this.fTrasladoDeFacturas.isGrabado()) {
                    this.fTrasladoDeFacturas.txtManifiestoDestin.setEnabled(false);
                    this.fTrasladoDeFacturas.txtManifiestoDestin.setEditable(false);

                    this.fTrasladoDeFacturas.txtNumeroFactura.setEnabled(false);
                    this.fTrasladoDeFacturas.txtNumeroFactura.setEditable(false);
                } else {
                    this.fTrasladoDeFacturas.txtManifiestoDestin.setEnabled(true);
                    this.fTrasladoDeFacturas.txtManifiestoDestin.setEditable(false);

                    this.fTrasladoDeFacturas.txtNumeroFactura.setEnabled(true);
                    this.fTrasladoDeFacturas.txtNumeroFactura.setEditable(true);
                }

            } else {
                guardado = false;
                JOptionPane.showInternalMessageDialog(this.fTrasladoDeFacturas, "Error al guardar los Datos en la  BBDD ", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }

    }

}
