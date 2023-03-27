/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.formularios.IngresoAlSistema;
import aplicacionlogistica.distribucion.objetos.personas.CUsuarios;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

/**
 *
 * @author Usuario
 */
public class HiloListadoDeUsuarios implements Runnable {

    List<CUsuarios> listaDeUsuarios = null;

    Inicio ini = null;
    JProgressBar barraInf = null;
    JProgressBar barraSup = null;
    int totalFilasDeConsultas;
    int contadorDeRegistros;
    int aplicacion;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param barraInf
     * @param barraSup
     * @param totalFilas
     */
    public HiloListadoDeUsuarios(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;

    }

    /**
     * Constructor de clase
     *
     * @param ini
     * @param barraInf
     * @param barraSup
     * @param totalFilas
     */
    public HiloListadoDeUsuarios(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros, int aplicacion) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;
        this.aplicacion = aplicacion;

    }

    public HiloListadoDeUsuarios(Inicio ini) {
        this.ini = ini;

    }

    @Override

    public void run() {

        if (this.barraInf != null) {

            try {

                int k = 0;
                int y = 0;
                ResultSet rst = null;
                Statement st;
                Connection con = null;

                switch (aplicacion) {

                    case 1: // Logistica
                        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
                        // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoDeUsuarios");
                        if (ini.getConnRemota() == null) {
                            ini.setConnRemota();

                        }
                        if (ini.getConnLocal() == null) {
                            ini.setConnLocal();

                        }
                       
                            
                        con = ini.getConnRemota();
                        
                        break;
                        

                    case 2: // mantenimientos
                        if (ini.getConnRemota() == null) {
                            ini.setConnRemota();

                        }
                        if (ini.getConnLocal() == null) {
                            ini.setConnLocal();

                        }
                        
                          if(ini.getConnGPS() == null){
                            ini.setConnGPS();
                        }
                          
                        con = ini.getConnRemota();
                        break;
                }

                CUsuarios usu = new CUsuarios(ini);
                listaDeUsuarios = new ArrayList();

                if (con != null) {
                    st = con.createStatement();
                    rst = st.executeQuery(usu.rstListadodeUsuarios());

                    rst.last();
                    int numeroFilas = rst.getRow();
                    rst.beforeFirst();

                    ini.setRalenti(130);

                    this.totalFilasDeConsultas += numeroFilas;

                    while (rst.next()) {
                        usu = new CUsuarios(ini);

                        usu.setCedula(rst.getString("cedula"));
                        usu.setNombres(rst.getString("nombres"));
                        usu.setApellidos(rst.getString("apellidos"));
                        usu.setDireccion(rst.getString("direccion"));
                        usu.setBarrio(rst.getString("barrio"));
                        usu.setCiudad(rst.getInt("ciudad"));
                        usu.setTelefonoFijo(rst.getString("telefonoFijo"));
                        usu.setTelefonoCelular(rst.getString("telefonoCelular"));
                        usu.setEscolaridad(rst.getString("escolaridad"));
                        usu.setGenero(rst.getString("genero"));
                        usu.setCumpleanios(rst.getDate("cumpleanios"));
                        usu.setLugarNacimiento(rst.getString("lugarNacimiento"));
                        usu.setIdEstadoCivil(rst.getInt("estadoCivil"));
                        usu.setEmail(rst.getString("email"));
                        usu.setIdTipoSangre(rst.getInt("tipoSangre"));
                        usu.setNombreUsuario((rst.getString("nombreUsuario")));
                        usu.setClaveUsuario((rst.getString("claveUsuario")));
                        usu.setTipoAcceso(rst.getInt("tipoAcceso"));
                        usu.setZona(rst.getInt("idZona"));
                        usu.setRegional(rst.getInt("idRegional"));
                        usu.setAgencia(rst.getInt("idAgencia"));
                        usu.setUsuarioActivo(rst.getInt("activo"));
                        usu.setNivelAcceso(rst.getInt("nivelAcceso"));

                        listaDeUsuarios.add(usu);

                        k++;
                        System.out.println("Cargando variables -> " + k);

                        /*Le asigna aplicacion a la barra inferior*/
                        y = (int) (k * 100) / numeroFilas;
                        this.barraInf.setValue(y);
                        this.barraInf.repaint();
                        Thread.sleep(1);
                    }

                    /*Asigna el aplicacion a la Barra de progreso superior*/
                    this.contadorDeRegistros += numeroFilas;
                    y = (int) (this.contadorDeRegistros * 100) / totalFilasDeConsultas;
                    this.barraSup.setValue((int) y);//100
                    this.barraSup.repaint();

                    st.close();
                    //con.close();
                    rst.close();
                    ini.setArrUsuarios(listaDeUsuarios);

                    Thread.sleep(1);

                } // fin try
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
                IngresoAlSistema.band = true;
            } catch (Exception ex) {
                Logger.getLogger(HiloListadoDeUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {

            try {
                ResultSet rst = null;
                Statement st;
                Connection con;
                //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "HiloListadoDeUsuarios");

                con = ini.getConnRemota();
                CUsuarios usu;
                listaDeUsuarios = new ArrayList();

                if (con != null) {
                    st = con.createStatement();

                    rst = st.executeQuery(CUsuarios.rstListadodeUsuarios());
                    while (rst.next()) {
                        usu = new CUsuarios(ini);

                        usu.setCedula(rst.getString("cedula"));
                        usu.setNombres(rst.getString("nombres"));
                        usu.setApellidos(rst.getString("apellidos"));
                        usu.setDireccion(rst.getString("direccion"));
                        usu.setBarrio(rst.getString("barrio"));
                        usu.setCiudad(rst.getInt("ciudad"));
                        usu.setTelefonoFijo(rst.getString("telefonoFijo"));
                        usu.setTelefonoCelular(rst.getString("telefonoCelular"));
                        usu.setEscolaridad(rst.getString("escolaridad"));
                        usu.setGenero(rst.getString("genero"));
                        usu.setCumpleanios(rst.getDate("cumpleanios"));
                        usu.setLugarNacimiento(rst.getString("lugarNacimiento"));
                        usu.setIdEstadoCivil(rst.getInt("estadoCivil"));
                        usu.setEmail(rst.getString("email"));
                        usu.setIdTipoSangre(rst.getInt("tipoSangre"));
                        usu.setNombreUsuario((rst.getString("nombreUsuario")));
                        usu.setClaveUsuario((rst.getString("claveUsuario")));
                        usu.setTipoAcceso(rst.getInt("tipoAcceso"));
                        usu.setZona(rst.getInt("idZona"));
                        usu.setRegional(rst.getInt("idRegional"));
                        usu.setAgencia(rst.getInt("idAgencia"));
                        usu.setActivoUsuario(rst.getInt("activo"));
                        usu.setNivelAcceso(rst.getInt("nivelAcceso"));

                        listaDeUsuarios.add(usu);
                        Thread.sleep(1);

                        System.out.println("Cargando variables -> ");

                    }
                    rst.close();
                    st.close();
                    // con.close();
                    ini.setArrUsuarios(listaDeUsuarios);
                    Thread.sleep(1);

                } // fin try
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
                JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

            } catch (Exception ex) {
                Logger.getLogger(HiloListadoDeUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
