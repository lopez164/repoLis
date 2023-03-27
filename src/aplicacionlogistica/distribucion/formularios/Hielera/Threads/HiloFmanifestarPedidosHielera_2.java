/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.formularios.Hielera.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.Hielera.FDespachoHieleraConIntegracion;
import aplicacionlogistica.distribucion.formularios.Hielera.FManifestarPedidosHielera_2;
import aplicacionlogistica.distribucion.integrador.HiloIntegradorTNSLaHielera;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.ListModel;

/**
 *
 * @author Usuario
 */
public class HiloFmanifestarPedidosHielera_2 implements Runnable {

    public NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.getDefault());
    Inicio ini = null;
    FManifestarPedidosHielera_2 fManifestarPedidosHielera_2 = null;
    String caso;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloFmanifestarPedidosHielera_2(Inicio ini) {
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param fModificarManifiesto
     * @param comando
     */
    public HiloFmanifestarPedidosHielera_2(Inicio ini, FManifestarPedidosHielera_2 fManifestarPedidosHielera_2, String comando) {
        this.ini = ini;
        this.fManifestarPedidosHielera_2 = fManifestarPedidosHielera_2;
        this.caso = comando;
    }

    @Override
    public void run() {

        if (caso != null) {
            switch (caso) {
                case "grabarManifiesto":

                    break;
                case "buscarManifiesto":

                    break;

                case "cargarmanifiestosSinDescargar":
                    cargarmanifiestosSinDescargar();
                    break;

                case "refrescarManifiestos":
                    refrescarManifiestos();
                    break;
                case "seleccionarFormularioManifiesto":
                
                    try {
                    seleccionarFormularioManifiesto();
                } catch (InterruptedException ex) {
                    Logger.getLogger(HiloFmanifestarPedidosHielera_2.class.getName()).log(Level.SEVERE, null, ex);
                }

                break;

                case "pedidosPendientes":
                    traerPedidosPendientes();
                    break;
                default:
                    JOptionPane.showInternalMessageDialog(fManifestarPedidosHielera_2, "Se ha presentado un error en el llamado del comando : " + caso, "Error, comando no existe", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    public void traerPedidosPendientes() {
        if (fManifestarPedidosHielera_2.cargado) {
            HiloIntegradorTNSLaHielera hilo = new HiloIntegradorTNSLaHielera(ini, 4);
        }

        ini.setListaDeFacturasSinMovimiento();
        
        if (ini.getListaDeFacturasSinMovimiento() != null || ini.getListaDeFacturasSinMovimiento().size() > 0) {
            System.out.print("trae " + ini.getListaDeFacturasSinMovimiento().size() + " de facturas sin movimiento");
            DefaultListModel<String> modeloPendientes = new DefaultListModel<>();
            DefaultListModel<String> modeloAgendadas = new DefaultListModel<>();
            
            

            for (CFacturas fac : ini.getListaDeFacturasSinMovimiento()) {
                if(fac.getObservaciones().contains("###")){
                    modeloAgendadas.addElement(fac.getNumeroDeFactura() + "  -->  "  + fac.getFechaIng().substring(11, fac.getFechaIng().length()) );
                }else{
                 modeloPendientes.addElement(fac.getNumeroDeFactura() + "  -->  "  + fac.getFechaIng().substring(11, fac.getFechaIng().length()) );

                }

            }
            fManifestarPedidosHielera_2.listacturasPendientes.setModel(modeloPendientes);
            fManifestarPedidosHielera_2.listaFacturasAgendadas.setModel(modeloAgendadas);
            fManifestarPedidosHielera_2.cargado = true;
            fManifestarPedidosHielera_2.repaint();
        }

    }

    public void cargarmanifiestosSinDescargar() {

        fManifestarPedidosHielera_2.listaDeConductores = null;

        fManifestarPedidosHielera_2.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif")));

        ini.setListaDeManifiestossinDescargar(3, true);

        if (fManifestarPedidosHielera_2.jListManifiestos.getModel().getSize() > 1) {
            DefaultListModel listModel = (DefaultListModel) fManifestarPedidosHielera_2.jListManifiestos.getModel();
            listModel.removeAllElements();
        }

        fManifestarPedidosHielera_2.listaDeFormulariosManifiestos = new ArrayList<>();
        fManifestarPedidosHielera_2.listaDeConductores = new ArrayList();

        if (fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion != null) {
            fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion.setVisible(false);
        }

        if (this.ini.getListaDeManifiestossinDescargar() != null || this.ini.getListaDeManifiestossinDescargar().size() > 0) {

            for (CManifiestosDeDistribucion manifiesto : this.ini.getListaDeManifiestossinDescargar()) {

                manifiesto.setListaFacturasPorManifiesto();

                if (manifiesto.getListaFacturasPorManifiesto() == null) {
                    List<CFacturasPorManifiesto> lista = new ArrayList<>();
                    manifiesto.setListaFacturasPorManifiesto(lista);

                }

                fManifestarPedidosHielera_2.listaDeConductores.add(manifiesto.getNombreConductor() + " " + manifiesto.getApellidosConductor() + "-" + manifiesto.getNumeroManifiesto());

                fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion = new FDespachoHieleraConIntegracion(this.ini, fManifestarPedidosHielera_2, manifiesto);

                fManifestarPedidosHielera_2.jPanel1.add(fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion);
                fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion.setVisible(false);
                //guiDespachoHielera.nuevo();
                fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion.moveToFront();
                fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(true);
                fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEditable(true);

                //  FpnlSalidaRutaLaHielera fpnlSalidaRutaLaHielera = new FpnlSalidaRutaLaHielera();
                fManifestarPedidosHielera_2.listaDeFormulariosManifiestos.add(fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion);
            }
//            DefaultListModel listModel = (DefaultListModel) fManifestarPedidosHielera_2.jListManifiestos.getModel();
            //          listModel.removeAllElements();

            anexarElementosAlJlist();
            fManifestarPedidosHielera_2.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png")));

        }
    }

    private void anexarElementosAlJlist() {

        // listaDeConductores = new ArrayList<>();
        final DefaultListModel model = new DefaultListModel();
        for (String obj : fManifestarPedidosHielera_2.listaDeConductores) {

            model.addElement(obj);
        }
        fManifestarPedidosHielera_2.jListManifiestos.setModel(model);

    }

    private void refrescarManifiestos() {

        if (fManifestarPedidosHielera_2 != null) {
            fManifestarPedidosHielera_2.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif")));

            ini.setListaDeManifiestossinDescargar(3, true);
            ini.setListaDeEmpleados("");
            ini.setListaDeVehiculos(0);

            cargarmanifiestosSinDescargar();
            traerPedidosPendientes();

            fManifestarPedidosHielera_2.jBtnRefrescar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Refresh.png")));
        }

    }

    private void seleccionarFormularioManifiesto() throws InterruptedException {
        String cadena = null;
        if (fManifestarPedidosHielera_2.jListManifiestos.getModel().getSize() < 1) {
            return;
        }
        if (fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion != null) {
            fManifestarPedidosHielera_2.listaDeFormulariosManifiestos.add(fManifestarPedidosHielera_2.indice, fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion);

        }

        new Thread(new HiloIntegradorTNSLaHielera(this.ini)).start();

        cadena = fManifestarPedidosHielera_2.jListManifiestos.getSelectedValue();

        //String strMain = this.ini.getPrefijos();
        String[] arrSplit = cadena.split("-");
        String nombreConductor = arrSplit[0];
        //String placa = arrSplit[1];
        String numeroManifiesto = arrSplit[1];
        int i = 0;
        for (FDespachoHieleraConIntegracion fd : fManifestarPedidosHielera_2.listaDeFormulariosManifiestos) {

            if (numeroManifiesto.equals(fd.manifiestoActual.getNumeroManifiesto())) {

                if (fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion != null) {
                    fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion.setVisible(false);
                }

                //  new Thread(new HiloFDespchoHieleraConIntegracion(ini, fd, "cargarFormulario2")).start();
                fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion = fd;

                //  FpnlSalidaRutaLaHielera fpnlSalidaRutaLaHielera = new FpnlSalidaRutaLaHielera();
                fManifestarPedidosHielera_2.jPanel1.add(fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion);
                fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion.setVisible(true);
                fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion.txtPlaca.setEnabled(true);
                fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion.txtPlaca.setEditable(true);
                fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion.moveToFront();
                if (fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion.manifiestoActual.getListaFacturasPorManifiesto() != null) {
                    fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion.btnImprimir.setEnabled(true);
                }
                fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEnabled(true);
                fManifestarPedidosHielera_2.fDespachoHieleraConIntegracion.txtNumeroDeFactura.setEditable(true);

                break;
            }

            i++;
            Thread.sleep(10);
        }

    }

}
