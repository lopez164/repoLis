/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import mtto.vehiculos.CCarros;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class HiloListadoDeVehiculos implements Runnable {

    List<CCarros> listaDeVehiculos = null;

    Inicio ini = null;
    JProgressBar barraInf = null;
    JProgressBar barraSup = null;
    int totalFilasDeConsultas;
    int contadorDeRegistros;
    int aplicacion = 1;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param barraInf
     * @param barraSup
     * @param totalFilas
     */
    public HiloListadoDeVehiculos(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;
        aplicacion = 1;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param barraInf
     * @param barraSup
     * @param totalFilas
     * @param contadorDeRegistros
     * @param aplicacion
     */
    public HiloListadoDeVehiculos(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros, int aplicacion) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;
        this.aplicacion = aplicacion;
    }

    public HiloListadoDeVehiculos(Inicio ini) {
        this.ini = ini;

    }

    @Override
    public void run() {
        if (this.barraInf != null) {

            int k = 0;
            int y = 0;
            ResultSet rst = null;
            Statement st;
            Connection con = null;
            switch (aplicacion) {
                case 1:
                    // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
                    //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoDeVehiculos");
                    con = ini.getConnRemota();
                    break;
                case 2:
                    // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoDeVehiculos");
                    con = ini.getConnRemota();
                    break;

                default:
                    // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
                    // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoDeVehiculos");
                    con = ini.getConnRemota();
                    break;

            }

            CCarros carro = new CCarros(ini);
            listaDeVehiculos = new ArrayList();

            if (con != null) {
                try { 
                    st = con.createStatement();

                    rst = st.executeQuery("select * from vst_vehiculos where activo=1 "
                            + "order by placa ;");

                    rst.last();
                    int numeroFilas = rst.getRow();
                    rst.beforeFirst();

                    this.totalFilasDeConsultas += numeroFilas;
                    if (this.totalFilasDeConsultas == 0) {
                        return;
                    }

                    while (rst.next()) {

                        carro = asignarPropiedades(rst);

                        listaDeVehiculos.add(carro);

                        k++;
                        System.out.println("Cargando vehiculos -> " + k +  " " +carro.getPlaca());

                        /*Le asigna valor a la barra inferior*/
                        y = (int) (k * 100) / numeroFilas;
                        this.barraInf.setValue(y);
                        this.barraInf.repaint();
                        Thread.sleep(1);
                    }

                    /*Asigna el valor a la Barra de progreso superior*/
                    this.contadorDeRegistros += numeroFilas;
                    y = (int) (this.contadorDeRegistros * 100) / totalFilasDeConsultas;
                    this.barraSup.setValue((int) y);//100
                    this.barraSup.repaint();

                    rst.close();
                    st.close();
                    // con.close();
                    ini.setListaDeVehiculos(listaDeVehiculos);
                } catch (SQLException | InterruptedException ex) {
                    Logger.getLogger(HiloListadoDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        } else {
            ini.setListaDeVehiculos(1);
        }

    }

    private CCarros asignarPropiedades(ResultSet rst1) throws SQLException {
        CCarros carro;
        carro = new CCarros(ini);
        carro.setPlaca(rst1.getString("placa"));
        carro.setTipoDeTracccion(1); // 1 corresponde a carros
        carro.setConductor(rst1.getString("conductor"));
        carro.setNombreConductor(rst1.getString("nombreConductor"));
        carro.setApellidosConductor(rst1.getString("apellidosConductor"));
        carro.setPesoTotalSinCarga(rst1.getDouble("pesoTotalSinCarga"));
        carro.setLargoVehiculo(rst1.getDouble("largoVehiculo"));
        carro.setAlturaVehiculo(rst1.getDouble("alturaVehiculo"));
        carro.setAnchuraVehiculo(rst1.getDouble("anchuraVehiculo"));
        carro.setLongitudVehiculo(rst1.getDouble("longitudVehiculo"));
        carro.setPesoTotalAutorizado(rst1.getDouble("pesoTotalAutorizado"));
        carro.setCapacidadInstalada(rst1.getDouble("capacidadInstalada"));
        carro.setIdLineaVehiculo(rst1.getInt("lineaVehiculo"));
        carro.setNombreLineaVehiculo(rst1.getString("nombreLineaVehiculo"));
        carro.setNombreMarcaDeVehiculo(rst1.getString("nombreMarcaDeVehiculo"));
        carro.setTipoVehiculo(rst1.getInt("tipoVehiculo"));
        carro.setTipoCarroceria(rst1.getInt("tipoCarroceria"));
        carro.setNombreTipoCarroceria(rst1.getString("nombreTipoCarroceria"));
        carro.setNombreTipoCombustible(rst1.getString("nombreTipoCombustible"));
        carro.setNombreEstadoVehiculo(rst1.getString("nombreEstadoVehiculo"));

        carro.setTipoContrato(rst1.getInt("tipoContrato"));
        carro.setPropietario(rst1.getString("propietario"));
        carro.setTarjetaPropiedad(rst1.getString("tarjetaPropiedad"));
        carro.setCantidadLLantas(rst1.getInt("cantidadLLantas"));
        carro.setTamanioLlantas(rst1.getString("tamanioLlantas"));
        carro.setSerialChasis(rst1.getString("serialChasis"));
        carro.setTrailer(rst1.getString("trailer"));
        carro.setAgencia(rst1.getInt("agencia"));
        carro.setModelo(rst1.getString("modelo"));
        carro.setTipoServicio(rst1.getInt("idTipoServicio"));
        carro.setSerialMotor(rst1.getString("serialMotor"));
        carro.setTipoCombustible(rst1.getInt("idTipoCombustible"));
        carro.setKmCambioValvulinaTrasmision(rst1.getInt("kmCambioValvulinaTrasmision"));
        carro.setKilometrajeActual(rst1.getInt("kilometrajeActual"));
        carro.setKmCambioAceiteMotor(rst1.getInt("kmCambioAceiteMotor"));
        carro.setKmCambioValvulinaCaja(rst1.getInt("kmCambioValvulinaCaja"));
        carro.setIdLineaVehiculo(rst1.getInt("lineaVehiculo"));
        carro.setTipoMime(rst1.getString("tipoMime"));
        carro.setActivoVehiculo(rst1.getInt("activo"));
        carro.setActivoCarro(rst1.getInt("activo"));
        carro.setConGps(rst1.getInt("conGps"));
        return carro;
    }
}
