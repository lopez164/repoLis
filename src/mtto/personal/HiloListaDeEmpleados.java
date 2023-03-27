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

/**
 *
 * @author Usuario
 */
public class HiloListaDeEmpleados implements Runnable {

    public static boolean band = false;
    private int tiempo = 5;
    Inicio ini=null;
    int caso=0;
   
    List<CEmpleados> listaDeEmpleados = null;
    
    IngresoAlSistema form;
    FBuscarListadoDeEmpleados flistaEmpleados=null;
    
    String apellidos=null;
    String centrodecosto=null;
    String sql=null;
            
    ResultSet rst = null; 
    Statement st=null;
    Connection con;

    /**
     * Constructor de clase
     * @param ini
     * @param form
   */
    
    public HiloListaDeEmpleados(Inicio ini) {
        this.ini = ini;
        caso=3;

    }
    
     public HiloListaDeEmpleados(Inicio ini,String centrodecosto,String apellidos, String directos,String contratistas,String todos,String activo) {
        this.ini = ini;
        this.apellidos=apellidos;
       // idtipocontrato=2 directo
       // centro de costo=6 camdun
       // centro de costo=19 distrilog
       
        caso=3;
         sql = "select * from vst_empleados ;";
         sql = "select * from vst_empleados WHERE activo=1 ;";
         sql = "select * from vst_empleados WHERE activo=0 ;";
        

         sql = "select * from vst_empleados WHERE nombreCentroDeCosto like'%" + centrodecosto + "%' and activo=1 ;";
         sql = "select * from vst_empleados WHERE nombreCentroDeCosto like'%" + centrodecosto + "%' and activo=0 ;";
         sql = "select * from vst_empleados WHERE nombreCentroDeCosto like'%" + centrodecosto + "%' ;";

         sql = "select * from vst_empleados WHERE nombreCentroDeCosto like'%" + centrodecosto + "%' and ( nombres like '%" + apellidos + "%' or apellidos like '%" + apellidos + "%') and  activo=1 ;";
         sql = "select * from vst_empleados WHERE nombreCentroDeCosto like'%" + centrodecosto + "%' and ( nombres like '%" + apellidos + "%' or apellidos like '%" + apellidos + "%') and  activo=1 ;";
         sql = "select * from vst_empleados WHERE nombreCentroDeCosto like'%" + centrodecosto + "%' and ( nombres like '%" + apellidos + "%' or apellidos like '%" + apellidos + "%') and  activo=1 ;";

         sql = "select * from vst_empleados WHERE nombreCentroDeCosto like'%" + centrodecosto + "%' and idTipoDeContrato=2 activo=1 ;";
         sql = "select * from vst_empleados WHERE nombreCentroDeCosto like'%" + centrodecosto + "%' and idTipoDeContrato=2 activo=0 ;";
         sql = "select * from vst_empleados WHERE nombreCentroDeCosto like'%" + centrodecosto + "%' and idTipoDeContrato=2;";
         
         sql = "select * from vst_empleados WHERE nombreCentroDeCosto like'%" + centrodecosto + "%' and idTipoDeContrato<>2 and activo=1 ;";
         sql = "select * from vst_empleados WHERE nombreCentroDeCosto like'%" + centrodecosto + "%' and idTipoDeContrato<>2 and activo=0 ;";
         sql = "select * from vst_empleados WHERE nombreCentroDeCosto like'%" + centrodecosto + "%' and idTipoDeContrato<>;";
         
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
                 llenarListaporApellidos(apellidos);
                break;
            case 3:
                 llenarlistaCompleta2();
                break;

            case 4:
                break;

            case 5:
                break;
        }

    }

    
     private void llenarListaporApellidos(String apellido) throws HeadlessException {
        try {
            
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            
            CEmpleados empleado = new CEmpleados(ini);
            listaDeEmpleados = new ArrayList();
            
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(CEmpleados.arrListadoDeEmpleados(apellido,false));
                
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
                    empleado.setIdEstadoCivil(rst.getInt("estadoCivil"));
                    empleado.setNombreEstadoCivil(rst.getString("nombreEstadoCivil"));
                    empleado.setEmail(rst.getString("eMail"));
                    empleado.setIdTipoSangre(rst.getInt("idTipoSangre"));
                    empleado.setTipoSangre(rst.getString("tipoSangre"));
                    empleado.setIdCargo(rst.getInt("idcargo"));
                    empleado.setCargo(rst.getString("cargo"));
                    empleado.setCelularCorporativo(rst.getString("celularCorporativo"));
                    empleado.setFechaDeIngreso(rst.getDate("fechaDeIngreso"));
                    empleado.setIdAgencia(rst.getInt("idAgencia"));
                    empleado.setNombreAgencia(rst.getString("nombreAgencia"));
                    //empleado.setRegional(rst.getInt("idRegional"));
                    //empleado.setZona(rst.getInt("idZona"));
                    empleado.setIdCentroDeCosto(rst.getInt("idCentroDeCosto"));
                    empleado.setNombreCentroDeCosto(rst.getString("nombreCentroDeCosto"));
                    empleado.setIdTipoDeContrato(rst.getInt("idTipoDeContrato"));
                    empleado.setNombreCentroDeCosto(rst.getString("tipoContrato"));
                    empleado.setNumeroCuenta(rst.getString("numeroCuenta"));
                    empleado.setIdBanco(rst.getInt("entidadBancaria"));
                    empleado.setNombreBanco(rst.getString("nombreBanco"));
                    empleado.setEmpleadoActivo(rst.getInt("activo"));
                    //empleado.setrst.getDate("fechaDeRetiro"));
                    
                    listaDeEmpleados.add(empleado);
                    Thread.sleep(10);
                    
                    
                }
                rst.close();
                st.close();
                con.close();
                flistaEmpleados.llenarTabla();
                 
            } // fin try
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            band = true;
        } catch (Exception ex) {
            Logger.getLogger(HiloListaDeEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

     /**
     * LLena la lista de los empleados y la asigna a al propiedad de la lista de empleados
     * en la clase Ini
     * @param ini
     * @param form
     */
     
    private void llenarlistaCompleta2() throws HeadlessException {
        try {
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
             String sql ="select * from vst_empleados "
                + "WHERE activo=1 ;";
            CEmpleados empleado = new CEmpleados(ini);
            listaDeEmpleados = new ArrayList();
            
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                
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
                    empleado.setIdEstadoCivil(rst.getInt("estadoCivil"));
                    empleado.setNombreEstadoCivil(rst.getString("nombreEstadoCivil"));
                    empleado.setEmail(rst.getString("eMail"));
                    empleado.setIdTipoSangre(rst.getInt("idTipoSangre"));
                    empleado.setTipoSangre(rst.getString("tipoSangre"));
                    empleado.setIdCargo(rst.getInt("idcargo"));
                    empleado.setCargo(rst.getString("cargo"));
                    empleado.setCelularCorporativo(rst.getString("celularCorporativo"));
                    empleado.setFechaDeIngreso(rst.getDate("fechaDeIngreso"));
                    empleado.setIdAgencia(rst.getInt("idAgencia"));
                    empleado.setNombreAgencia(rst.getString("nombreAgencia"));
                    //empleado.setRegional(rst.getInt("idRegional"));
                    //empleado.setZona(rst.getInt("idZona"));
                    empleado.setIdCentroDeCosto(rst.getInt("idCentroDeCosto"));
                    empleado.setNombreCentroDeCosto(rst.getString("nombreCentroDeCosto"));
                    empleado.setIdTipoDeContrato(rst.getInt("idTipoDeContrato"));
                    empleado.setNombreCentroDeCosto(rst.getString("tipoContrato"));
                    empleado.setNumeroCuenta(rst.getString("numeroCuenta"));
                    empleado.setIdBanco(rst.getInt("entidadBancaria"));
                    empleado.setNombreBanco(rst.getString("nombreBanco"));
                    empleado.setEmpleadoActivo(rst.getInt("activo"));
                    //empleado.setFechaDeRetiro(rst.getDate("fechaDeRetiro"));
                    
                    listaDeEmpleados.add(empleado);
                    Thread.sleep(1);
                    
                }
                rst.close();
                st.close();
                con.close();
                ini.setListaDeEmpleados(listaDeEmpleados);
                
            } // fin try
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            band = true;
        } catch (Exception ex) {
            Logger.getLogger(HiloListaDeEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * LLena la lista de los empleados y la asigna a al propiedad de la lista de empleados
     * en la clase Ini y en la vista de ingreso al sistema para ver las barras de porentaje
     * 
     */
    private void llenarListaCompleta() throws HeadlessException {
        int numeroFilas;
        double contadorDeFilas = 0;
        int porcentajeBarra;
                
        try {
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
             String sql ="select * from vst_empleados "
                + "WHERE activo=1 ;";
            CEmpleados empleado = new CEmpleados(ini);
            listaDeEmpleados = new ArrayList();
            
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                
                rst.last();
                numeroFilas = rst.getRow();
                rst.beforeFirst();
                
                //form.totalNumeroDeRegistros += numeroFilas;
                
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
                    empleado.setIdEstadoCivil(rst.getInt("estadoCivil"));
                    empleado.setNombreEstadoCivil(rst.getString("nombreEstadoCivil"));
                    empleado.setEmail(rst.getString("eMail"));
                    empleado.setIdTipoSangre(rst.getInt("idTipoSangre"));
                    empleado.setTipoSangre(rst.getString("tipoSangre"));
                    empleado.setIdCargo(rst.getInt("idcargo"));
                    empleado.setCargo(rst.getString("cargo"));
                    empleado.setCelularCorporativo(rst.getString("celularCorporativo"));
                    empleado.setFechaDeIngreso(rst.getDate("fechaDeIngreso"));
                    empleado.setIdAgencia(rst.getInt("idAgencia"));
                    empleado.setNombreAgencia(rst.getString("nombreAgencia"));
                    //empleado.setRegional(rst.getInt("idRegional"));
                    //empleado.setZona(rst.getInt("idZona"));
                    empleado.setIdCentroDeCosto(rst.getInt("idCentroDeCosto"));
                    empleado.setNombreCentroDeCosto(rst.getString("nombreCentroDeCosto"));
                    empleado.setIdTipoDeContrato(rst.getInt("idTipoDeContrato"));
                    empleado.setNombreCentroDeCosto(rst.getString("tipoContrato"));
                    empleado.setNumeroCuenta(rst.getString("numeroCuenta"));
                    empleado.setIdBanco(rst.getInt("entidadBancaria"));
                    empleado.setNombreBanco(rst.getString("nombreBanco"));
                    empleado.setEmpleadoActivo(rst.getInt("activo"));
                    //empleado.setFechaDeRetiro(rst.getDate("fechaDeRetiro"));
                    
                    
                    listaDeEmpleados.add(empleado);
                    
                  //  form.ContadorTotalNumeroDeRegistros++;
                    //contadorDeFilas++;
                   // porcentajeBarra = (int) (contadorDeFilas * 100) / numeroFilas;
                   
                }
                rst.close();
                st.close();
                con.close();
                ini.setListaDeEmpleados(listaDeEmpleados);
                
            } // fin try
        }  catch (Exception ex) {
            Logger.getLogger(HiloListaDeEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
