/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.personal;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.consultas.FBuscarListadoDeEmpleados;
import aplicacionlogistica.configuracion.formularios.IngresoAlSistema;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.Vst_empleados;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import mtto.vehiculos.CCarros;
import mtto.vehiculos.CVehiculos;

/**
 *
 * @author Usuario
 */
public class HiloListaDeVehiculos implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    Inicio ini = null;
    int caso = 0;

    List<Vst_empleados> listaDeVehiculos = null;
    FReporteDeVehiculos fReporteDeVehiculos;

    IngresoAlSistema form;
    FBuscarListadoDeEmpleados flistaEmpleados = null;

    String apellidos = null;
    String centrodecosto = null;
    String sql = null;

    ResultSet rst1 = null;
    Statement st = null;
    Connection con;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param form
     */
    public HiloListaDeVehiculos(Inicio ini) {
        this.ini = ini;
        caso = 3;

    }

    public HiloListaDeVehiculos(Inicio ini, FReporteDeVehiculos fReporteDeVehiculos) {
        this.ini = ini;
        this.fReporteDeVehiculos = fReporteDeVehiculos;
        caso = 1;
        sql = "select * from  vst_vehiculos where tipoContrato=1 ;";

    }

    @Override
    public void run() {
        /*
        if (form != null) {
            llenarListaCompleta();
            return;
        }
        
        
        if (flistaEmpleados != null) {
            llenarListaporApellidos(apellido);
            return;
        }

        if (ini != null) {
            llenarlistaCompleta2();
        }
         */

        switch (caso) {
            case 1:
               
                 {
                    try {
                        /*Lista de vehiculos propios*/
                        ini.setListaDeVehiculos(2);
                        //llenarListaCompleta();
                        this.fReporteDeVehiculos.llenarTablabitacora();
                    } catch (Exception ex) {
                        Logger.getLogger(HiloListaDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;

            case 2:

                break;
            case 3:

                break;

            case 4:
                break;

            case 5:
                break;
        }

    }

    /**
     * LLena la lista de los empleados y la asigna a al propiedad de la lista de
     * empleados en la clase Ini y en la vista de ingreso al sistema para ver
     * las barras de porentaje
     *
     */
    private void llenarListaCompleta() throws HeadlessException {
        int numeroFilas;
        double contadorDeFilas = 0;
        int porcentajeBarra;
        CCarros carro;
        try {
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            List<CCarros> listaDeVehiculos = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst1 = st.executeQuery(sql);

                rst1.last();
                numeroFilas = rst1.getRow();
                rst1.beforeFirst();

                //form.totalNumeroDeRegistros += numeroFilas;
                while (rst1.next()) {

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

                    listaDeVehiculos.add(carro);

                }
                rst1.close();
                st.close();
                con.close();
                ini.setListaDeVehiculos(listaDeVehiculos);

            } // fin try
        } catch (Exception ex) {
            Logger.getLogger(HiloListaDeVehiculos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
