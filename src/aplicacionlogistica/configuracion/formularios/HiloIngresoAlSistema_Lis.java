/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.configuracion.formularios;

import aplicacionlogistica.configuracion.Inicio;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class HiloIngresoAlSistema_Lis implements Runnable {

    Inicio ini;
    IngresoAlSistema_LIS_ALD ingreso;

    /**
     * Constructor de clase
     *
     * @param ini
     */
    public HiloIngresoAlSistema_Lis(Inicio ini, IngresoAlSistema_LIS_ALD ingreso) {
        this.ini = ini;
        this.ingreso = ingreso;

    }

    @Override
    public void run() {
        int i = 0;
        try {
            ingreso.barraInferior.setValue(i);
            ingreso.barraSuperior.setValue(i);
            //new Thread(new HiloListadoDeUsuarios(this.ini, this.barraInferior,this.barraSuperior,this.totalFilasDeConsultas,this.contadorDeRegistros)).start(); //ok
            ini.setListaDeEmpleados();// new Thread(new HiloListadoDeEmpleados(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);
            
            ini.setListaDeZonas();// new Thread(new HiloListadoDeZonas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start(); //ok
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);
            
            ini.setListaDeRegionales();// new Thread(new HiloListadoDeRegionales(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();//ok
            i= i + (int)3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeAgencias(); //new Thread(new HiloListadoDeAgencias(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();//ok
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);
           
            ini.setListaDeEstadosCiviles();// new Thread(new HiloListadoDeEstadosCiviles(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeCargos();// new Thread(new HiloListadoDeCargos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeTiposDeSangre(); // new Thread(new HiloListadoDeTiposDeSangre(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeTiposDeAcceso();// new Thread(new HiloListadoDeTiposDeAcceso(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeNivelesDeAcceso();//new Thread(new HiloListadoDeNivelesDeAcceso(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeCentrosDeCosto();// new Thread(new HiloListadoDeCentrosDeCosto(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();// ok
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeTiposContratosPer(); // new Thread(new HiloListadoDeContratosPersonas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeEntidadesBancarias();// new Thread(new HiloListadoDeEntidadesBancarias(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setLIstaDeCuentasBancarias();//new Thread(new HiloListadoDeCuentasBancarias(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeCausalesDeDevolucion();// new Thread(new HiloListadoDeCausalesdeDevolucion(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeCanalesDeVenta();// new Thread(new HiloListadoDeCanalesDeVenta(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeRutasDeDistribucion();// new Thread(new HiloListadoDeRutasDeDistribucion(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaMovimientosManifiestosfacturas(); //new Thread(new HiloListadoDeMovimientosManifiestosfacturas(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeVehiculos(1); // new Thread(new HiloListadoDeVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeMarcasDeVehiculos(); //new Thread(new HiloListadoDeMarcasVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeLineasPorMarca(); // new Thread(new HiloListadoDeLineasDeVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeTiposDeVehiculos(); // new Thread(new HiloListadoDeTiposDeVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeTiposDeCarrocerias(); //new Thread(new HiloListadoDeTiposDeCarrocerias(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeTiposDeServicios();// new Thread(new HiloListadoDeTiposDeServicio(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeTiposDeCombustibles(); //new Thread(new HiloListadoDeTiposDeCombustibles(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeTiposDeContratosVehiculos();// new Thread(new HiloListadoDeTiposDeContratosVehiculos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeTiposDeMantenimientos(); // new Thread(new HiloListadoDeTiposDeMantenimiento(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeTiposDeDocumentos(); // new Thread(new HiloListadoDeTiposDeDocumentos(this.ini, this.barraInferior, this.barraSuperior, this.totalFilasDeConsultas, this.contadorDeRegistros)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeSucursales(); // new Thread(new HiloListadoDeSucursales(ini)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ini.setListaDeManifiestossinDescargar(0, true); //ifi new Thread(new HiloListadoDeManifiestosSinDescargar(ini, 3)).start();
            i = i + (int) 3.5;
            ingreso.barraSuperior.setValue(i);

            ingreso.cargarFormularioPpal();

//            

        } catch (Throwable ex) {
            Logger.getLogger(HiloIngresoAlSistema_Lis.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
