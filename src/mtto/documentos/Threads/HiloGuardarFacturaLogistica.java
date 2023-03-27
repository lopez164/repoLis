/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.organizacion.CAgencias;
import mtto.ingresoDeRegistros.IngresarFacturasDeGastos;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import mtto.documentos.objetos.GastosPorVehiculo;
import mtto.proveedores.SucursalesPorproveedor;

/**
 *
 * @author Usuario
 */
public class HiloGuardarFacturaLogistica implements Runnable {

    public IngresarFacturasDeGastos fingresarFacturasDeGastos;
    Inicio ini;

    /**
     * Constructor de clase
     *
     * @param form
     */
    public HiloGuardarFacturaLogistica(IngresarFacturasDeGastos form) {

        this.fingresarFacturasDeGastos = form;
        this.ini = form.ini;

    }

    @Override
    public void run() {

//        this.form.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/circuloDeProgreso2.gif"))); // NOI18N
//        this.form.habilitar(false);
//        this.form.estaOcupadoGrabando = true;
//        this.form.asignarValoresProveedor();
        this.fingresarFacturasDeGastos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/circuloDeprogreso.gif"))); // NOI18N

        List<String> listaDeSentenciasSQL = new ArrayList();

        if (fingresarFacturasDeGastos.actualizar) {

            actualizar(listaDeSentenciasSQL);

        } else {

            grabarNuevo();
        }

        this.fingresarFacturasDeGastos.estaOcupadoGrabando = false;
        this.fingresarFacturasDeGastos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N

    }

    private void grabarNuevo() {

        asignarValores();

        if (fingresarFacturasDeGastos.gastosFlota.guardar()) {
            this.fingresarFacturasDeGastos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
            this.fingresarFacturasDeGastos.btnGrabar.setEnabled(false);
            this.fingresarFacturasDeGastos.setEnabled(true);
            this.fingresarFacturasDeGastos.btnAgregar.setEnabled(false);

            JOptionPane.showInternalMessageDialog(fingresarFacturasDeGastos, "La factura hasido guardado con exito", "Registro guardado", JOptionPane.INFORMATION_MESSAGE);

        } else {
            this.fingresarFacturasDeGastos.btnGrabar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/aplicacionlogistica/configuracion/imagenes/img16x16/Save.png"))); // NOI18N
            JOptionPane.showInternalMessageDialog(fingresarFacturasDeGastos, "Error al guardar los datos", "Error al guardar", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void actualizar(List<String> listaDeSentenciasSQL) {

    }

    void asignarValores() throws NumberFormatException {

        /*Busca y asigna el codigo de la Sucursal*/
        for (SucursalesPorproveedor prov : this.fingresarFacturasDeGastos.listaDeSucursales) {
            if (prov.getNombreSucursal().equals(this.fingresarFacturasDeGastos.txtProveedor.getText().trim())) {
                this.fingresarFacturasDeGastos.gastosFlota.setSucursalProveedor("" + prov.getIdSucursal());
                this.fingresarFacturasDeGastos.gastosFlota.setIdProveedor("" + prov.getCedula());

                break;
            }

        }

        //  gastosFlota.setIdConsecutivo(); 
        this.fingresarFacturasDeGastos.gastosFlota.setNumeroDeOrden(this.fingresarFacturasDeGastos.txtNumeroOrden.getText().trim());
        this.fingresarFacturasDeGastos.gastosFlota.setNumeroFactura(this.fingresarFacturasDeGastos.txtNumeroFactura.getText().trim());

        Date dt = new Date();
        dt = ini.getFechaSql(this.fingresarFacturasDeGastos.dateFechaIngreso);
        this.fingresarFacturasDeGastos.gastosFlota.setFechaFactura("" + dt);
        this.fingresarFacturasDeGastos.gastosFlota.setVehiculo(this.fingresarFacturasDeGastos.txtPlaca.getText().trim());
        this.fingresarFacturasDeGastos.gastosFlota.setConductor(this.fingresarFacturasDeGastos.txtIdConductor.getText().trim());
        this.fingresarFacturasDeGastos.gastosFlota.setKilometraje(this.fingresarFacturasDeGastos.txtkilometraje.getText().trim());
        
        this.fingresarFacturasDeGastos.valorFactura= 0.0;
        for (GastosPorVehiculo gv : this.fingresarFacturasDeGastos.listaDeProductos) {
            this.fingresarFacturasDeGastos.valorFactura += Double.parseDouble(gv.getValorTotal());
        }

        if (ini.getListaDeAgencias() == null) {
            ini.setListaDeAgencias();
        }
        for (CAgencias ag : ini.getListaDeAgencias()) {
            if (ag.getIdAgencia() == this.fingresarFacturasDeGastos.vehiculo.getAgencia()) {
                this.fingresarFacturasDeGastos.gastosFlota.setZona("" + ag.getIdZona());
                this.fingresarFacturasDeGastos.gastosFlota.setRegional("" + ag.getIdRegional());
                this.fingresarFacturasDeGastos.gastosFlota.setAgencia("" + ag.getIdAgencia());
                break;
            }
        }

        //gastosFlota.setsetfotografiaFactura(); 
        this.fingresarFacturasDeGastos.gastosFlota.setFormatoFotografia(".jpg");
        this.fingresarFacturasDeGastos.gastosFlota.setActivo("" + 1);
        //gastosFlota.setfechaIng(); 
        //gastosFlota.setusuario(); 
        this.fingresarFacturasDeGastos.gastosFlota.setFlag("" + 1);

        this.fingresarFacturasDeGastos.gastosFlota.setValorfactura("" + this.fingresarFacturasDeGastos.valorFactura);
        this.fingresarFacturasDeGastos.gastosFlota.setListaGastosPorVehiculo(this.fingresarFacturasDeGastos.listaDeProductos);

    }

}
