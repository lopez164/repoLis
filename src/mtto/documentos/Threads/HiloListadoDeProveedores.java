/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import mtto.proveedores.Cproveedores;
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
public class HiloListadoDeProveedores implements Runnable {

    List<Cproveedores> listaProveedores = null;

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
     */
    public HiloListadoDeProveedores(Inicio ini, JProgressBar barraInf, JProgressBar barraSup, int totalFilas, int contadorDeRegistros) {
        this.barraInf = barraInf;
        this.barraSup = barraSup;
        this.totalFilasDeConsultas = totalFilas;
        this.contadorDeRegistros = contadorDeRegistros;
        this.ini = ini;

    }

    public HiloListadoDeProveedores(Inicio ini) {
        this.ini = ini;

    }

    @Override
    public void run() {
        if (this.barraInf != null) {
            llenarListaCompleta();
            return;
        }else{
            llenarlistaCompleta2();
        }
//
//        if (flistaEmpleados != null) {
//            llenarListaporApellidos(apellido);
//            return;
//        }

//        if (flistaEmpleados != null) {
//            //llenarListaporApellidos(apellido);
//            return;
//        }
//        if (ini != null) {
//            llenarlistaCompleta2();
//        }

    }

    private void llenarListaporApellidos(String apellido) throws HeadlessException {
        try {
            int k = 0;
            int y = 0;
            ResultSet rst = null;
            Statement st;
            Connection con;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            listaProveedores = new ArrayList();
            String sql = "select * from view_proveedores where activo=1;";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    Cproveedores proveedor = new Cproveedores(ini);
                    proveedor.setCedula(rst.getString("cedula"));
                    proveedor.setNombres(rst.getString("nombres"));
                    proveedor.setApellidos(rst.getString("apellidos"));
                    proveedor.setDireccion(rst.getString("direccion"));
                    proveedor.setBarrio(rst.getString("barrio"));
                    proveedor.setCiudad(rst.getInt("ciudad"));
                    proveedor.setTelefonoFijo(rst.getString("telefonoFijo"));
                    proveedor.setTelefonoCelular(rst.getString("telefonoCelular"));
                    proveedor.setEscolaridad(rst.getString("escolaridad"));
                    proveedor.setGenero(rst.getString("genero"));
                    proveedor.setCumpleanios(rst.getDate("cumpleanios"));
                    proveedor.setLugarNacimiento(rst.getString("lugarNacimiento"));
                    proveedor.setIdEstadoCivil(rst.getInt("estadoCivil"));
                    proveedor.setEmail(rst.getString("eMail"));
                    proveedor.setIdTipoSangre(rst.getInt("tipoSangre"));
                    proveedor.setContacto(rst.getString("contacto"));
                    proveedor.setCelularCorporativo(rst.getString("celularCorporativo"));
                    proveedor.setFechaDeIngreso(rst.getString("fechaDeIngreso"));
                    proveedor.setAgencia(rst.getInt("idAgencia"));
                    proveedor.setFechaIng(rst.getString("fechaIng"));
                    proveedor.setUsuario(rst.getString("usuario"));
                    proveedor.setActivo(rst.getInt("activo"));
                    proveedor.setFlag(rst.getInt("flag"));

                    listaProveedores.add(proveedor);
                    Thread.sleep(10);

                }
                rst.close();
                st.close();
                con.close();
                ini.setListaDeProveedores(listaProveedores);
                //flistaEmpleados.setListaDeEmpleados(listaProveedores);
                //flistaEmpleados.llenarTabla();

            } // fin try
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeProveedores.class.getName()).log(Level.SEVERE, null, ex);
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
            int k = 0;
            int y = 0;
            ResultSet rst = null;
            Statement st;
            Connection con;

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        con = this.ini.getConnRemota();
        
            String sql = "select * from view_proveedores where activo=1;";
            CEmpleados empleado = new CEmpleados(ini);
            listaProveedores = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    Cproveedores proveedor = new Cproveedores(ini);
                    proveedor.setCedula(rst.getString("cedula"));
                    proveedor.setNombres(rst.getString("nombres"));
                    proveedor.setApellidos(rst.getString("apellidos"));
                    proveedor.setDireccion(rst.getString("direccion"));
                    proveedor.setBarrio(rst.getString("barrio"));
                    proveedor.setCiudad(rst.getInt("ciudad"));
                    proveedor.setTelefonoFijo(rst.getString("telefonoFijo"));
                    proveedor.setTelefonoCelular(rst.getString("telefonoCelular"));
                    proveedor.setEscolaridad(rst.getString("escolaridad"));
                    proveedor.setGenero(rst.getString("genero"));
                    proveedor.setCumpleanios(rst.getDate("cumpleanios"));
                    proveedor.setLugarNacimiento(rst.getString("lugarNacimiento"));
                    proveedor.setIdEstadoCivil(rst.getInt("estadoCivil"));
                    proveedor.setEmail(rst.getString("eMail"));
                    proveedor.setIdTipoSangre(rst.getInt("tipoSangre"));
                    proveedor.setContacto(rst.getString("contacto"));
                    proveedor.setCelularCorporativo(rst.getString("celularCorporativo"));
                    proveedor.setFechaDeIngreso(rst.getString("fechaDeIngreso"));
                    proveedor.setAgencia(rst.getInt("agencia"));
                    proveedor.setFechaIng(rst.getString("fechaIng"));
                    proveedor.setUsuario(rst.getString("usuario"));
                    proveedor.setActivo(rst.getInt("activo"));
                    proveedor.setFlag(rst.getInt("flag"));

                    listaProveedores.add(proveedor);
                    Thread.sleep(1);

                }
                rst.close();
                st.close();
               // con.close();
                ini.setListaDeProveedores(listaProveedores);
                //ini.setListaDeEmpleados(arrEmpleados);

            } // fin try
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            //band = true;
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeProveedores.class.getName()).log(Level.SEVERE, null, ex);
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
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        con = this.ini.getConnRemota();
        
            String sql = "select * from view_proveedores where activo=1;";
            listaProveedores = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                rst.last();
                int numeroFilas = rst.getRow();
                rst.beforeFirst();

                this.totalFilasDeConsultas += numeroFilas;

                while (rst.next()) {
                    Cproveedores proveedor = new Cproveedores(ini);

                    proveedor.setCedula(rst.getString("cedula"));
                    proveedor.setNombres(rst.getString("nombres"));
                    proveedor.setApellidos(rst.getString("apellidos"));
                    proveedor.setDireccion(rst.getString("direccion"));
                    proveedor.setBarrio(rst.getString("barrio"));
                    proveedor.setCiudad(rst.getInt("ciudad"));
                    proveedor.setTelefonoFijo(rst.getString("telefonoFijo"));
                    proveedor.setTelefonoCelular(rst.getString("telefonoCelular"));
                    proveedor.setEscolaridad(rst.getString("escolaridad"));
                    proveedor.setGenero(rst.getString("genero"));
                    proveedor.setCumpleanios(rst.getDate("cumpleanios"));
                    proveedor.setLugarNacimiento(rst.getString("lugarNacimiento"));
                    proveedor.setIdEstadoCivil(rst.getInt("estadoCivil"));
                    proveedor.setEmail(rst.getString("eMail"));
                    proveedor.setIdTipoSangre(rst.getInt("tipoSangre"));
                    proveedor.setContacto(rst.getString("contacto"));
                    proveedor.setCelularCorporativo(rst.getString("celularCorporativo"));
                    proveedor.setFechaDeIngreso(rst.getString("fechaDeIngreso"));
                    proveedor.setAgencia(rst.getInt("idAgencia"));
                    proveedor.setFechaIng(rst.getString("fechaIng"));
                    proveedor.setUsuario(rst.getString("usuario"));
                    proveedor.setActivo(rst.getInt("activo"));
                    proveedor.setFlag(rst.getInt("flag"));

                    listaProveedores.add(proveedor);
                    k++;
                    System.out.println("Cargando usuarios -> " + k);

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
                //con.close();
                ini.setListaDeProveedores(listaProveedores);
            } // fin try
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeProveedores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
