/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.ArchivosDeTexto;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.costumerService.FDescargarDevoluciones;
import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.distribucion.formularios.FDescargarRechazosParciales;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CProductosPorFactura;
import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class hiloArmararchivoProductosPorFacturaDescargados implements Runnable {

    Inicio ini;
    public DescargarFacturas form;
    public FDescargarDevoluciones fDescargarDevoluciones;
    //CFacturasDescargadas facturaDescargada;
    FDescargarRechazosParciales formDescargueFacturas;

    int movimientoFactura;
    int consecutivoFactura;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public hiloArmararchivoProductosPorFacturaDescargados(Inicio ini, DescargarFacturas form) {

        this.form = form;
        this.ini = ini;

        this.consecutivoFactura = form.facturaActual.getConsecutivo();
        this.movimientoFactura = this.form.tipoMovimientoFactura;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     * @param facturaDescargada
     * @param movimientoFactura
     */
    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     * @param movimientoFactura
     */
    public hiloArmararchivoProductosPorFacturaDescargados(Inicio ini, FDescargarRechazosParciales form, int movimientoFactura, CFacturas facturaTemporal) {

        this.formDescargueFacturas = form;
        this.ini = ini;
        //this.facturaDescargada = facturaDescargada;
        this.movimientoFactura = movimientoFactura;

    }

    @Override
    public void run() {
        ArchivosDeTexto archivo;
        java.util.Enumeration claves;
        List<CProductosPorFactura> listaDeProductosFactura = null;

        switch (movimientoFactura) {
            case 1:

                break;

            case 2:

                entragaTotalSinDescuento();
                break;

            /*Rechazo total */
            case 3:

                for (CProductosPorFactura obj : form.facturaActual.getListaProductosPorFactura()) {

                    try {

                        obj.setValorDescuentoItem(0.0);
                        obj.setCantidadRechazadaItem(obj.getCantidad());
                        obj.setValorRechazoItem(obj.getValorProductoXCantidad());
                        obj.setCantidadEntregadaItem(0.0);
                        obj.setValorTotalLiquidacionItem(0.0);

                        obj.setEntregado(0);
                        obj.setCausalDeRechazo(form.causalrechazo);
                        obj.setActivo(1);

                        String ruta = this.form.manifiestoActual.getRutaArchivoDescargueporductosPorFactura();
                        String senteciaSqlProductosPorFacturaDescargados = obj.getCadenaConLosCamposProductoDescargado();

                        Inicio.GuardaConsultaEnFichero(senteciaSqlProductosPorFacturaDescargados, ruta);

                    } catch (Exception ex) {
                        Logger.getLogger(hiloArmararchivoProductosPorFacturaDescargados.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

                // this.form.facturaDescargada.setListaDEProductosPorFacturaDescargada(listaDEProductosPorFacturaDescargada);
                break;

            /*RECHAZO PARCIAL */
            case 4:

                for (CProductosPorFactura obj : form.facturaActual.getListaProductosPorFactura()) {

                    if (obj.getCantidadRechazadaItem() > 0) {

                        obj.setEntregado(4);

                        String ruta = this.form.manifiestoActual.getRutaArchivoDescargueporductosPorFactura();
                        String senteciaSqlProductosPorFacturaDescargados = obj.getCadenaConLosCamposProductoDescargado();

                        Inicio.GuardaConsultaEnFichero(senteciaSqlProductosPorFacturaDescargados, ruta);

                    }
                }

                break;
            /* Volver a Zonificar */
            case 6:

                File file = new File(this.form.manifiestoActual.getRutaArchivoDescargueporductosPorFactura());

                for (CProductosPorFactura obj : form.facturaActual.getListaProductosPorFactura()) {
                    try {

                        obj.setValorDescuentoItem(0.0);
                        obj.setCantidadRechazadaItem(obj.getCantidad());
                        obj.setValorRechazoItem(obj.getValorProductoXCantidad());
                        obj.setCantidadEntregadaItem(0.0);
                        obj.setValorTotalLiquidacionItem(0.0);
                        obj.setValorRechazoItem(0.0);
                        obj.setEntregado(6);

                        obj.setCausalDeRechazo(18);
                        obj.setActivo(1);

                        String ruta = this.form.manifiestoActual.getRutaArchivoDescargueporductosPorFactura();

                        String senteciaSqlProductosPorFacturaDescargados = obj.getCadenaConLosCamposProductoDescargado();

                        Inicio.GuardaConsultaEnFichero(senteciaSqlProductosPorFacturaDescargados, ruta);

                    } catch (Exception ex) {
                        Logger.getLogger(hiloArmararchivoProductosPorFacturaDescargados.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                break;

            /* No visitados  */
            case 7:

                file = new File(this.form.manifiestoActual.getRutaArchivoDescargueporductosPorFactura());

                for (CProductosPorFactura obj : form.facturaActual.getListaProductosPorFactura()) {
                    try {

                        obj.setValorDescuentoItem(0.0);
                        obj.setCantidadRechazadaItem(obj.getCantidad());
                        obj.setValorRechazoItem(obj.getValorProductoXCantidad());
                        obj.setCantidadEntregadaItem(0.0);
                        obj.setValorTotalLiquidacionItem(0.0);
                        obj.setValorRechazoItem(0.0);
                        obj.setEntregado(7);

                        obj.setCausalDeRechazo(18);
                        obj.setActivo(1);

                        String ruta = this.form.manifiestoActual.getRutaArchivoDescargueporductosPorFactura();

                        String senteciaSqlProductosPorFacturaDescargados = obj.getCadenaConLosCamposProductoDescargado();

                        Inicio.GuardaConsultaEnFichero(senteciaSqlProductosPorFacturaDescargados, ruta);

                    } catch (Exception ex) {
                        Logger.getLogger(hiloArmararchivoProductosPorFacturaDescargados.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
                break;
        }
    }

    private void entragaTotalSinDescuento() {
        /* Entrega Total*/
        for (CProductosPorFactura obj : form.facturaActual.getListaProductosPorFactura()) {
            try {
                if (form.chkDescuento.isSelected()) {

                } else {
                    obj.setValorDescuentoItem(0.0);
                    obj.setCantidadRechazadaItem(0);
                    obj.setValorRechazoItem(0.0);
                    obj.setCantidadEntregadaItem(obj.getCantidad());

                    /* se verifica que la factura haya sido a credito ó de contado para verificar el dinero recaudado */
                    if (this.form.isFacturaCredito) {
                        /* Si la factura es crédito , no recaudó dinero */
                        obj.setValorTotalLiquidacionItem(0.0);
                    } else {
                        /* Si la factura es contado , se le asigna el valor total del producto */
                        obj.setValorTotalLiquidacionItem(obj.getValorProductoXCantidad());
                    }

                    obj.setEntregado(2);
                    obj.setCausalDeRechazo(1);
                    obj.setActivo(1);

                }

                String ruta = this.form.manifiestoActual.getRutaArchivoDescargueporductosPorFactura();
                String senteciaSqlProductosPorFacturaDescargados = obj.getCadenaConLosCamposProductoDescargado();

                Inicio.GuardaConsultaEnFichero(senteciaSqlProductosPorFacturaDescargados, ruta);

            } catch (Exception ex) {
                Logger.getLogger(hiloArmararchivoProductosPorFacturaDescargados.class.getName()).log(Level.SEVERE, null, ex);

            }

        }
    }
}
