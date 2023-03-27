/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.documentos.Threads;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.configuracion.formularios.IngresoAlSistema;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import mtto.proveedores.SucursalesPorproveedor;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class HiloListadoDeSucursales implements Runnable {

    Inicio ini = null;
    String cedula = null;
    int ciudad;

    ArrayList<SucursalesPorproveedor> listaDeSucursales = null;

    IngresoAlSistema form;

    ResultSet rst = null;
    Statement st = null;
    Connection con;

    public HiloListadoDeSucursales(Inicio ini, String cedula, int ciudad) {

        this.cedula = cedula;
        this.ini = ini;
        this.ciudad = ciudad;

    }

    public HiloListadoDeSucursales(Inicio ini) {
        this.ini = ini;
        this.ciudad = ini.getUser().getCiudad();

    }

    @Override
    public void run() {
        if (cedula == null) {
//            llenarListaCompleta();
            return;
        } else {
            LlenarListaPorProveedor();
        }

    }

    private void LlenarListaPorProveedor() throws HeadlessException {
        try {

        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            listaDeSucursales = new ArrayList();
            //String sql = "select * from  view_sucursales where activo=1 and cedula='" + this.cedula + "' and agencia='" + this.ciudad + "';";
            String sql = "select * from  view_sucursales where activo=1 and cedula='" + this.cedula + "';";
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    llenarRegistro();

                }
                rst.close();
                st.close();
                con.close();
                ini.setListaDeSucursales(listaDeSucursales);
                //flistaEmpleados.setListaDeEmpleados(listaProveedores);
                //flistaEmpleados.llenarTabla();

            } // fin try
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeSucursales.class.getName()).log(Level.SEVERE, null, ex);
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
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            String sql = "select * from view_sucursales where activo=1;";
            CEmpleados empleado = new CEmpleados(ini);
            listaDeSucursales = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    llenarRegistro();
                    
                    Thread.sleep(1);

                }
                rst.close();
                st.close();
                con.close();
                ini.setListaDeSucursales(listaDeSucursales);
                //ini.setListaDeEmpleados(arrEmpleados);

            } // fin try
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);
            //band = true;
        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeSucursales.class.getName()).log(Level.SEVERE, null, ex);
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
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
           // con = CconexionMtto.GetConnection(null, null, null);

            //String sql = "select * from view_sucursales where activo=1 and codAgencia=" + ini.getUser().getAgencia() + ";";
             String sql = "select * from view_sucursales where activo=1;";
            
             listaDeSucursales = new ArrayList();

            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    llenarRegistro();

                    Thread.sleep(1);

                }
                rst.close();
                st.close();
                con.close();
                ini.setListaDeSucursales(listaDeSucursales);
            } // fin try
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
            JOptionPane.showMessageDialog(null, "Error thread 0 :" + e, " Alerta, cerrar ventana", 1, null);

        } catch (Exception ex) {
            Logger.getLogger(HiloListadoDeSucursales.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
      private void llenarRegistro() throws SQLException {
        SucursalesPorproveedor sucursal = new SucursalesPorproveedor(ini);
        
        sucursal.setIdSucursal(rst.getInt("idSucursalProveedor"));
        sucursal.setCedula(rst.getString("nit"));
        sucursal.setNombreProveedor(rst.getString("proveedor"));
        sucursal.setNombreSucursal(rst.getString("nombreSucursal"));
        sucursal.setCodigoInterno(rst.getString("codigo"));
        sucursal.setDireccionSucursal(rst.getString("direccion"));
        sucursal.setBarrioSucursal(rst.getString("barrio"));
        sucursal.setCiudadSucursal(rst.getInt("codCiudad"));
        sucursal.setNombreCiudad(rst.getString("nombreCiudad"));
        sucursal.setTelefonoSucursal(rst.getString("telefono"));
        sucursal.setCelularSucursal(rst.getString("celular"));
        sucursal.setEmailSucursal(rst.getString("emailContacto"));
        sucursal.setContactoSucursal(rst.getString("contacto"));
        sucursal.setCelularCorporativo(rst.getString("celularcorporativo"));
        sucursal.setFechaDeIngreso(rst.getString("fechaIngreso"));
        sucursal.setAgencia(rst.getInt("codAgencia"));
        sucursal.setNombreAgencia(rst.getString("nombreAgencia"));
        sucursal.setFechaIng(rst.getString("fechaIng"));
        sucursal.setUsuario(rst.getString("usuario"));
        sucursal.setActivo(rst.getInt("activo"));
        //sucursal.setFlag(rst.getInt("flag"));
        
        sucursal.setListaDeCuentasSecundarias();
        
        listaDeSucursales.add(sucursal);
    }
    
    
}
