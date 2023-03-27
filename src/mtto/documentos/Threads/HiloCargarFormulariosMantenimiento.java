/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.Threads;

import GPS.Monitoreo.PuntosMonitoreo;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.CambiarClave;
import mtto.PrincipalMantenimiento;
import aplicacionlogistica.distribucion.clientes.FGeoreferenciarClientes;
import aplicacionlogistica.distribucion.formularios.administracion.IngresarCarros;
import mtto.Servicios.FAgregarMantenimientoVehiculo;
import mtto.ingresoDeRegistros.IngresarFacturasDeGastos;
import mtto.proveedores.IngresarSucursalDeProveedor;
import mtto.proveedores.IngresarProveedores;
import mtto.reportes.FReporteGastosFlota;
import java.util.logging.Level;
import java.util.logging.Logger;
import mtto.PrincipalAuxiliarMantenimiento;
import mtto.documentos.FIngresarDocumentosVehiculos;

/**
 *
 * @author Usuario
 */
public class HiloCargarFormulariosMantenimiento implements Runnable {

    static boolean band;

    PrincipalMantenimiento principalMantenimiento;
    PrincipalAuxiliarMantenimiento principalauxiliarMantenimiento;
    Inicio ini;

    /**
     * Constructor de clase
     *
     * @param form
     */
    public HiloCargarFormulariosMantenimiento(PrincipalMantenimiento form) {
        this.principalMantenimiento = form;
        this.ini = form.ini;

    }

    /**
     * Constructor de clase
     *
     * @param form
     */
    public HiloCargarFormulariosMantenimiento(PrincipalAuxiliarMantenimiento principalauxiliarMantenimiento) {
        this.principalauxiliarMantenimiento = principalauxiliarMantenimiento;
        this.ini = principalauxiliarMantenimiento.ini;

    }

    @Override
    public void run() {
        try {

            instanciarFormularios();

        } catch (Exception ex) {
            Logger.getLogger(HiloCargarFormulariosMantenimiento.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void instanciarFormularios() throws Exception {
        if (principalMantenimiento != null) {
            this.principalMantenimiento.fAgregarMantenimientoVehiculo = new FAgregarMantenimientoVehiculo(ini);
            this.principalMantenimiento.formCrearSucursales = new IngresarSucursalDeProveedor(ini);
            this.principalMantenimiento.fCambiarClave = new CambiarClave(ini);
            this.principalMantenimiento.formProveedores = new IngresarProveedores(ini, this.principalMantenimiento.formCrearSucursales);
            this.principalMantenimiento.fIngresaarCArros = new IngresarCarros(ini);
            this.principalMantenimiento.ingresarFacturasDeGastos = new IngresarFacturasDeGastos(ini);
            this.principalMantenimiento.fGeoreferenciarClientes = new FGeoreferenciarClientes(ini);
            this.principalMantenimiento.fReporteGastosFlota = new FReporteGastosFlota(ini);
            this.principalMantenimiento.fIngresarDocumentosVehiculos = new FIngresarDocumentosVehiculos(ini);
           // this.principalMantenimiento.puntosMonitoreo = new PuntosMonitoreo(ini);

        }
        if (principalauxiliarMantenimiento != null) {
            this.principalauxiliarMantenimiento.fAgregarMantenimientoVehiculo = new FAgregarMantenimientoVehiculo(ini);
            this.principalauxiliarMantenimiento.formCrearSucursales = new IngresarSucursalDeProveedor(ini);
            this.principalauxiliarMantenimiento.fCambiarClave = new CambiarClave(ini);
            this.principalauxiliarMantenimiento.formProveedores = new IngresarProveedores(ini, this.principalMantenimiento.formCrearSucursales);
            this.principalauxiliarMantenimiento.fIngresaarCArros = new IngresarCarros(ini);
            this.principalauxiliarMantenimiento.ingresarFacturasDeGastos = new IngresarFacturasDeGastos(ini);
            this.principalauxiliarMantenimiento.fGeoreferenciarClientes = new FGeoreferenciarClientes(ini);
            this.principalauxiliarMantenimiento.fReporteGastosFlota = new FReporteGastosFlota(ini);
            this.principalauxiliarMantenimiento.fIngresarDocumentosVehiculos = new FIngresarDocumentosVehiculos(ini);
            this.principalauxiliarMantenimiento.puntosMonitoreo = new PuntosMonitoreo(ini);

        }

    }
}
