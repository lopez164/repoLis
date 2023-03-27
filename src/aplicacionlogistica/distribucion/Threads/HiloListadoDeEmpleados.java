/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.consultas.FBuscarListadoDeEmpleados;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import java.awt.HeadlessException;
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
public class HiloListadoDeEmpleados implements Runnable {

    List<CEmpleados> listaDeEmpleados = null;
    int caso = 1;

    FBuscarListadoDeEmpleados flistaEmpleados = null;

    String apellido;

    Inicio ini = null;
    JProgressBar barraInf = null;
    JProgressBar barraSup = null;
    int totalFilasDeConsultas;
    int contadorDeRegistros;

    /**
     * Constructor de clase
     *
     * @param ini
     * @param barraInf
     * @param barraSup
     * @param totalFilas
     * @param contadorDeRegistros
     */
    public HiloListadoDeEmpleados(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;
        caso = 1;

    }

    public HiloListadoDeEmpleados(Inicio ini, FBuscarListadoDeEmpleados flistaEmpleados, String apellido) {
        this.flistaEmpleados = flistaEmpleados;
        this.apellido = apellido;
        this.ini = ini;
        caso = 2;

    }

    public HiloListadoDeEmpleados(Inicio ini) {
        this.ini = ini;
        caso = 3;

    }

    public HiloListadoDeEmpleados(Inicio ini, boolean conductores, boolean contratistas, boolean todos, boolean activos) {
        this.ini = ini;
        caso = 4;

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
                llenarListaCompleta();
                break;

            case 2:
                //llenarListaporApellidos(apellido);
                break;
            case 3:
                llenarlistaCompleta2();
                break;

            case 4:
                break;

             default:
                  llenarlistaCompleta2();
                break;
        }

    }

    private void llenarListaporApellids(String apellido) throws HeadlessException {
        try {
            ResultSet rst = null;
            Statement st;
            Connection con;
            String sql = "select * from  vst_empleados where (apellidos like '%" + apellido + "%' or "
                    + "nombres like '%" + apellido + "%');";
           // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeEmpleados");

           con = ini.getConnRemota();
           
            CEmpleados empleado = new CEmpleados(ini);
            listaDeEmpleados = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                traerDatos(rst);

                rst.close();
                st.close();
                //con.close();
                
            } // fin try
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void traerDatos(ResultSet rst) throws Exception {
        CEmpleados empleado;
        while (rst.next()) {
            empleado = new CEmpleados(ini);
            empleado.setCedula(rst.getString("cedula"));
            empleado.setNombres(rst.getString("nombres"));
            empleado.setApellidos(rst.getString("apellidos"));
            empleado.setDireccion(rst.getString("direccion"));
            empleado.setBarrio(rst.getString("barrio"));
            empleado.setCiudad(rst.getInt("ciudad"));
            empleado.setTelefonoFijo(rst.getString("telefonoFijo"));
            empleado.setTelefonoCelular(rst.getString("telefonoCelular"));
            empleado.setEscolaridad(rst.getString("escolaridad"));
            empleado.setGenero(rst.getString("genero"));
            empleado.setCumpleanios(rst.getDate("cumpleanios"));
            empleado.setLugarNacimiento(rst.getString("lugarNacimiento"));
            empleado.setIdEstadoCivil(rst.getInt("idEstadoCivil"));
            empleado.setNombreEstadoCivil(rst.getString("nombreEstadoCivil"));
            empleado.setEmail(rst.getString("eMail"));
            empleado.setIdTipoSangre(rst.getInt("idTipoSangre"));
            empleado.setTipoSangre(rst.getString("tipoSangre"));
            empleado.setIdCargo(rst.getInt("idcargo"));
            empleado.setCargo(rst.getString("cargo"));
            empleado.setCelularCorporativo(rst.getString("celularCorporativo"));
            empleado.setFechaDeIngreso(rst.getDate("fechaDeIngreso"));
            empleado.setIdAgencia(rst.getInt("idAgencia"));
            empleado.setNombreAgencia(apellido);
            // empleado.setRegional(rst.getInt("idRegional"));
            //empleado.setZona(rst.getInt("idZona"));
            empleado.setIdCentroDeCosto(rst.getInt("idCentroDeCosto"));
            empleado.setNombreCentroDeCosto(rst.getString("nombreCentroDeCosto"));
            empleado.setIdTipoDeContrato(rst.getInt("idTipoDeContrato"));

            empleado.setNumeroCuenta(rst.getString("numeroCuenta"));
            empleado.setIdBanco(rst.getInt("idBanco"));

            empleado.setEmpleadoActivo(rst.getInt("activo"));

            listaDeEmpleados.add(empleado);
            Thread.sleep(10);
           System.out.println("Colaborador xx-> " + empleado.getCedula());
        }
      
    }

    /**
     * LLena la lista de los empleados y la asigna a al propiedad de la lista de
     * empleados en la clase Ini
     *
     * @param ini
     * @param form
     */
    private void llenarlistaCompleta2() throws HeadlessException {
        try {
           
                ini.setListaDeEmpleados();
            
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * LLena la lista de los empleados y la asigna a al propiedad de la lista de
     * empleados en la clase Ini y en la vista de ingreso al sistema para ver
     * las barras de porentaje
     *
     */
    private void llenarListaCompleta() throws HeadlessException {

        try {
            int k = 0;
            int y = 0;
            ResultSet rst = null;
            Statement st;
            Connection con;
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(),"HiloListadoDeEmpleados");
            con = ini.getConnRemota();
            
            String sql = "select * from  vst_empleados;";
            CEmpleados empleado = new CEmpleados(ini);
            listaDeEmpleados = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                rst.last();
                int numeroFilas = rst.getRow();
                rst.beforeFirst();

                this.totalFilasDeConsultas += numeroFilas;

                while (rst.next()) {
                    empleado = new CEmpleados(ini);
                    empleado.setCedula(rst.getString("cedula"));
                    empleado.setNombres(rst.getString("nombres"));
                    empleado.setApellidos(rst.getString("apellidos"));
                    empleado.setDireccion(rst.getString("direccion"));
                    empleado.setBarrio(rst.getString("barrio"));
                    empleado.setCiudad(rst.getInt("ciudad"));
                    empleado.setTelefonoFijo(rst.getString("telefonoFijo"));
                    empleado.setTelefonoCelular(rst.getString("telefonoCelular"));
                    empleado.setEscolaridad(rst.getString("escolaridad"));
                    empleado.setGenero(rst.getString("genero"));
                    empleado.setCumpleanios(rst.getDate("cumpleanios"));
                    empleado.setLugarNacimiento(rst.getString("lugarNacimiento"));
                    empleado.setIdEstadoCivil(rst.getInt("idEstadoCivil"));
                    empleado.setNombreEstadoCivil(rst.getString("nombreEstadoCivil"));
                    empleado.setEmail(rst.getString("eMail"));
                    empleado.setIdTipoSangre(rst.getInt("idTipoSangre"));
                    empleado.setTipoSangre(rst.getString("tipoSangre"));
                    empleado.setIdCargo(rst.getInt("idcargo"));
                    empleado.setCargo(rst.getString("cargo"));
                    empleado.setCelularCorporativo(rst.getString("celularCorporativo"));
                    empleado.setFechaDeIngreso(rst.getDate("fechaDeIngreso"));
                    empleado.setIdAgencia(rst.getInt("idAgencia"));
                    empleado.setNombreAgencia("nombreAgencia"); //
                    empleado.setFormatoFotografia(rst.getString("formatoFotografia"));
                    //empleado.setZona(rst.getInt("idZona"));
                    empleado.setIdCentroDeCosto(rst.getInt("idCentroDeCosto"));
                    empleado.setNombreCentroDeCosto(rst.getString("nombreCentroDeCosto"));
                    empleado.setIdTipoDeContrato(rst.getInt("idTipoDeContrato"));

                    empleado.setNumeroCuenta(rst.getString("numeroCuenta"));
                    empleado.setIdBanco(rst.getInt("idBanco"));

                    empleado.setEmpleadoActivo(rst.getInt("activo"));

                    listaDeEmpleados.add(empleado);
                    k++;
                    System.out.println("Cargando empleados  yy-> " + k);

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
                //con.close();
                
                ini.setListaDeEmpleados(listaDeEmpleados);

            } // fin try
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
