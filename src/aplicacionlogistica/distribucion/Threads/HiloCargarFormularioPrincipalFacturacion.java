/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.formularios.DescargarFacturas;
import aplicacionlogistica.distribucion.PrincipalFacturacion;
import aplicacionlogistica.distribucion.PrincipalFacturacion;
import mtto.documentos.Threads.HiloListadoDeTiposDeMantenimiento;
import mtto.documentos.Threads.HiloListadoDeCarrosPropios;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloCargarFormularioPrincipalFacturacion implements Runnable {

    static boolean band;

    PrincipalFacturacion form;
    Inicio ini;

    /**
     * Constructor de clase
     *
     * @param form
     */
    public HiloCargarFormularioPrincipalFacturacion(PrincipalFacturacion form) {
        this.form = form;
        this.ini = form.ini;

    }

    @Override
    public void run() {
        try {

            iniciarVariables();
            instanciarFormularios();

        } catch (Exception ex) {
            Logger.getLogger(HiloCargarFormularioPrincipalFacturacion.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void instanciarFormularios() throws Exception {

        this.form.fDescargarFActuras = new DescargarFacturas(ini);

      
//        this.form.fConsultarManifiestos = new FConsultarManifiestos(ini);
//
//        this.form.fIngresaarCArros = new IngresarCarros(ini);
//
//        this.form.fReporteRechazosTotales = new FReporteDeRechazosTotales(ini);
//
//        this.form.fAnularFacturas = new FAnularFacturas(ini);
//
//        this.form.fConsultarFacturasRemoto = new FConsultarFacturasRemoto(this.ini);
//
//        this.form.fConsultarFacturasPorCliente = new FConsultarFacturasPorCliente(this.ini);
//
//        this.form.fReporteMovilizado = new FReportePedidosMovilizadosPorPeriodo(this.ini);
//
//        this.form.fReporteSinMovimientos = new FReporteFacturasSinMovimientos(this.ini);
//
//        this.form.fReporteManifiestosEnDistribucion = new FReporteManifiestosEnDistribucion(this.ini);
//
//        this.form.fTrasladoDePedidos = new FTrasladoDeFacturas(this.ini);
//
//        this.form.fHabilitarManifiesto = new FHabilitarManifiesto(this.ini);
//
//        this.form.fImportarExcel = new FImportarArchivoExcel(ini);
//
//        this.form.fIngresarEmpleados = new IngresarEmpleados(ini);
//
//        this.form.fManifiestosConciliados = new FReporteManifiestosConcilidos(ini);
//
//        this.form.fHabilitarFacturas = new FHabilitarFacturas(ini);
//
//        this.form.fIngresarEmpleados = new IngresarEmpleados(ini);
//
//        this.form.fReporteFacturacionPorCanal = new FReporteFacturacionPorCanal(ini);
//
//        this.form.fReporteFacturacionTAT = new FReporteFacturacionTAT(ini);
//
//        this.form.freporteManifiestosSinDescargar = new FReporteManifiestosSinDescargar(ini);
//
//        this.form.fDesvincularFactura = new FBorrarFacturaManifiesto(ini);
//
//        this.form.fFacturasEnPicking = new FManifestarFacturasEnPicking(ini);
//
//        this.form.fRegistroDePedidosFinLineaDePicking = new FRegistroDePedidosFinLineaDePicking(ini);
//
//        this.form.fReporteFacturasPendientesEnPicking = new FReporteFacturasPendientesEnPicking(ini);
//
//        this.form.fGeoreferenciarClientes = new FGeoreferenciarClientes(ini);
//
//        this.form.fAdicionarPedidosNoReportados = new FAdicionarPedidosNoReportados(ini);
//
//        this.form.fReporteDescuentos_Recogidas = new FReporteDescuentos_Recogidas(ini);

    }

    private void iniciarVariables() throws InterruptedException {
        boolean isOk = true;
//        String[] lista = {"usuarios", "zonas", "regionales", "agencias", "departamentos", "ciudades", "estadosciviles", "cargos", "tiposdesangre", "tiposdeacceso", "nivelesdeacceso",
//            "centrosdecosto", "tiposcontratospersonas", "bancos", "marcasdevehiculos", "lineasvehiculos", "tiposdevehiculos", "tiposdecarrocerias", "tiposdeservicio", "tiposdecombustible",
//            "tiposcontratosvehiculos", "causalesderechazo", "tiposcanaldeventas", "rutasdedistribucion", "empleados", "carros", "tiposdemovimientosmanifiestosfacturas",
//            "manifiestosdedistribucion"};
//        //  totalNumeroDeRegistros= ini.contarRegistros(lista);
//        

        /*Verifica la conexion a la Base de Datos*/
        if (CConexiones.hayConexion(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota())) {
            new Thread(new HiloListadoDeEmpleados(ini)).start();
            Thread.sleep(300);
            new Thread(new HiloListadoDeUsuarios(ini)).start(); //ok
            new Thread(new HiloListadoDeZonas(ini)).start(); //ok
            new Thread(new HiloListadoDeRegionales(ini)).start();//ok
            new Thread(new HiloListadoDeAgencias(ini)).start();//ok
            new Thread(new HiloListadoDeDepartamentos(ini)).start();
            new Thread(new HiloListadoDeEstadosCiviles(ini)).start();
            new Thread(new HiloListadoDeCargos(ini)).start();
            new Thread(new HiloListadoDeTiposDeSangre(ini)).start();
            new Thread(new HiloListadoDeTiposDeAcceso(ini)).start();
            new Thread(new HiloListadoDeNivelesDeAcceso(ini)).start();
            new Thread(new HiloListadoDeCentrosDeCosto(ini)).start();// ok
            new Thread(new HiloListadoDeContratosPersonas(ini)).start();
            new Thread(new HiloListadoDeEntidadesBancarias(ini)).start();

            Thread.sleep(300);

            new Thread(new HiloListadoDeMarcasVehiculos(ini)).start();
            new Thread(new HiloListadoDeLineasDeVehiculos(ini)).start();
            new Thread(new HiloListadoDeTiposDeVehiculos(ini)).start();
            new Thread(new HiloListadoDeTiposDeCarrocerias(ini)).start();
            new Thread(new HiloListadoDeTiposDeServicio(ini)).start();
            new Thread(new HiloListadoDeTiposDeCombustibles(ini)).start();
            new Thread(new HiloListadoDeTiposDeContratosVehiculos(ini)).start();

            Thread.sleep(300);

            new Thread(new HiloListadoDeCausalesdeDevolucion(ini)).start();
            new Thread(new HiloListadoDeCanalesDeVenta(ini)).start();
            new Thread(new HiloListadoDeRutasDeDistribucion(ini)).start();
            new Thread(new HiloListadoDeVehiculos(ini)).start();
            new Thread(new HiloListadoDeMovimientosManifiestosfacturas(ini)).start();

            new Thread(new HiloListadoDestinosFacturas(ini)).start();

            /*Hilos Mantenimientos*/
            new Thread(new HiloListadoDeTiposDeMantenimiento(ini)).start();
            new Thread(new HiloListadoDeCarrosPropios(ini)).start();

        } else {
            JOptionPane.showMessageDialog(null, "No se pudo iniciar la aplicacion porque No hay Conexion a la BBDD", "Error...", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // Thread.sleep(300);
        //new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 5, 3)).start();
    }

}
