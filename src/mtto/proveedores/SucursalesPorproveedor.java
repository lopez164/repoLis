/*
 * To change this license header; choose License Headers in Project Properties.
 * To change this template file; choose Tools | Templates
 * and open the template in the editor.
 */
package mtto.proveedores;

import aplicacionlogistica.configuracion.Inicio;
import mtto.ingresoDeRegistros.objetos.CCuentaSecundariaLogistica;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lelopez
 */
public class SucursalesPorproveedor {

    int idSucursal;
    String cedula;
    String nombreProveedor;
    String nombreSucursal;
    String codigoInterno;
    String contactoSucursal;
    String direccionSucursal;
    String barrioSucursal;
    int ciudadSucursal;
    String nombreCiudad	;
    String telefonoSucursal;
    String celularSucursal;
    String celularCorporativo;
    String emailSucursal;
    String latitud="0";
    String longitud="0";
    int agencia;
    String nombreAgencia ;
    String fechaDeIngreso;
    int activo;
    String fechaIng;
    String usuario;
    String coordenadas;
    
    int flag;
    
    List<CCuentaSecundariaLogistica> listaDeCuentasSecundarias = null;

    Inicio ini;

    public SucursalesPorproveedor(Inicio ini) {
        this.ini = ini;
    }

    public SucursalesPorproveedor(Inicio ini, int idSucursal) {
        this.ini = ini;
         ResultSet rst = null;
        Statement st = null;
        Connection 
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        con = ini.getConnRemota();
       
        String sql = "SELECT idSucursalProveedor,cedula, codigoInterno,nombreSucursal,"
                + "contactoSucursalProveedor,direccionSucursalProveedor,"
                + "barrioSucursalProveedor,ciudadSucursalProveedor,telefonoSucursalProveedor,"
                + "celularSucursalProveedor, celularCorporativo, emailSucursalProveedor, agencia,"
                + "fechaDeIngreso,activo,fechaIng, usuario,flag"
                + "From proveedoresSucursales "
                + "where idSucursalProveedor='" + idSucursal + "';";

        if (con != null) {
            try {
                 st = con.createStatement();
                 rst = st.executeQuery(sql);

                while (rst.next()) {
                   
                    
                    this.idSucursal=rst.getInt("idSucursalProveedor");
                    this.cedula = rst.getString("cedula");
                    this.codigoInterno = rst.getString("codigoInterno");
                    this.nombreSucursal = rst.getString("nombreSucursal");
                    this.contactoSucursal = rst.getString("contactoSucursalProveedor");
                    this.direccionSucursal = rst.getString("direccionSucursalProveedor");
                    this.barrioSucursal = rst.getString("barrioSucursalProveedor");
                    this.ciudadSucursal = rst.getInt("ciudadSucursalProveedor");
                    this.telefonoSucursal = rst.getString("telefonoSucursalProveedor");
                    this.celularSucursal = rst.getString("celularSucursalProveedor");
                    this.celularCorporativo = rst.getString("celularCorporativo");
                    this.emailSucursal = rst.getString("emailSucursalProveedor");
                    this.agencia = rst.getInt("agencia");
                    this.fechaDeIngreso = rst.getString("fechaDeIngreso");
                    this.activo = rst.getInt("activo");
                    this.fechaIng = rst.getString("fechaIng");
                    this.usuario = rst.getString("usuario");
                    this.flag = rst.getInt("flag");
                    this.coordenadas= rst.getString("coordenadas");
                    
                    String[] parts =this.coordenadas.split(",");
                    this.latitud = rst.getString(parts[0]);
                    this.longitud = rst.getString(parts[1]);
                    this.cedula = rst.getString("cedula");

                    

                }
                rst.close();
                st.close();
               // con.close();

                //flistaEmpleados.setListaDeEmpleados(listaProveedor listaDeServiciosSucursal;
            } catch (SQLException ex) {
                Logger.getLogger(Cproveedores.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    public String getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(String coordenadas) {
        this.coordenadas = coordenadas;
    }
    
    
    
    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

   
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombreSucursal() {
        return nombreSucursal;
    }

    public void setNombreSucursal(String nombreSucursal) {
        this.nombreSucursal = nombreSucursal;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getContactoSucursal() {
        return contactoSucursal;
    }

    public void setContactoSucursal(String contactoSucursal) {
        this.contactoSucursal = contactoSucursal;
    }

    public String getDireccionSucursal() {
        return direccionSucursal;
    }

    public void setDireccionSucursal(String direccionSucursal) {
        this.direccionSucursal = direccionSucursal;
    }

    public String getBarrioSucursal() {
        return barrioSucursal;
    }

    public void setBarrioSucursal(String barrioSucursal) {
        this.barrioSucursal = barrioSucursal;
    }
    
    

    public int getCiudadSucursal() {
        return ciudadSucursal;
    }

    public void setCiudadSucursal(int ciudadSucursal) {
        this.ciudadSucursal = ciudadSucursal;
    }

    public String getTelefonoSucursal() {
        return telefonoSucursal;
    }

    public void setTelefonoSucursal(String telefonoSucursal) {
        this.telefonoSucursal = telefonoSucursal;
    }

    public String getCelularSucursal() {
        return celularSucursal;
    }

    public void setCelularSucursal(String celularSucursal) {
        this.celularSucursal = celularSucursal;
    }

    public String getCelularCorporativo() {
        return celularCorporativo;
    }

    public void setCelularCorporativo(String celularCorporativo) {
        this.celularCorporativo = celularCorporativo;
    }

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public String getFechaDeIngreso() {
        return fechaDeIngreso;
    }

    public void setFechaDeIngreso(String fechaDeIngreso) {
        this.fechaDeIngreso = fechaDeIngreso;
    }

    public int getActivo() {
        return activo;
    }

    public void setActivo(int activo) {
        this.activo = activo;
    }

    public String getFechaIng() {
        return fechaIng;
    }

    public void setFechaIng(String fechaIng) {
        this.fechaIng = fechaIng;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getEmailSucursal() {
        return emailSucursal;
    }

    public void setEmailSucursal(String emailSucursal) {
        this.emailSucursal = emailSucursal;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getNombreProveedor() {
        return nombreProveedor;
    }

    public void setNombreProveedor(String proveedor) {
        this.nombreProveedor = proveedor;
    }

    public String getNombreCiudad() {
        return nombreCiudad;
    }

    public void setNombreCiudad(String nombreCiudad) {
        this.nombreCiudad = nombreCiudad;
    }

    public String getNombreAgencia() {
        return nombreAgencia;
    }

    public void setNombreAgencia(String nombreAgencia) {
        this.nombreAgencia = nombreAgencia;
    }
    
    

    public List<CCuentaSecundariaLogistica> getListaDeCuentasSecundarias() {
        return listaDeCuentasSecundarias;
    }

    public void setListaDeCuentasSecundarias() {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst = null;
       listaDeCuentasSecundarias= null;

        try {

            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        con = ini.getConnRemota();
        
            sql = "SELECT csp.idSucursalProveedor,"
                    + "csp.cedula,"
                    + "csp.idCuentaSecundaria,"
                    + "cs.nombreCuentaSecundaria,"
                    + "cs.idCuentaPrincipal,"
                    + "cp.nombreCuentaPrincipal,"
                    + "csp.activo,"
                    + "csp.fechaIng,"
                    + "csp.usuario,"
                    + "csp.flag "
                    + "FROM cuestasporsucursalproveedor csp "
                    + "join cuentassecundariaslogistica cs on cs.idCuentaSecundaria=csp.idCuentaSecundaria "
                    + "join cuentasprincipaleslogistica cp on cs.idcuentaPrincipal=cp.idCuentaPrincipal "
                    + "where "
                    + "csp.idSucursalProveedor='" + idSucursal + "';";
                   

            st = con.createStatement();
            if (con != null) {
                listaDeCuentasSecundarias = new ArrayList<>();
                rst = st.executeQuery(sql);
               while (rst.next()) {

                    CCuentaSecundariaLogistica cuenta = new CCuentaSecundariaLogistica();
                    cuenta.setIdCuentaPrincipal(rst.getInt("idCuentaPrincipal"));
                    cuenta.setNombreCuentaPrincipal(rst.getString("nombreCuentaPrincipal"));
                    cuenta.setIdCuentaSecundaria(rst.getInt("idCuentaSecundaria"));
                    cuenta.setNombreCuentaSecundaria(rst.getString("nombreCuentaSecundaria"));
                    cuenta.setActivo(rst.getInt("activo"));

                    this.listaDeCuentasSecundarias.add(cuenta);

                }
            }
            if(listaDeCuentasSecundarias.size()==0){
                listaDeCuentasSecundarias = null;
            }
            rst.close();
            st.close();
           // con.close();
           
        } catch (SQLException ex) {
            Logger.getLogger(SucursalesPorproveedor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setListaDeCuentasSecundarias(List<CCuentaSecundariaLogistica> listaDeCuentasSecundarias) {
        this.listaDeCuentasSecundarias = listaDeCuentasSecundarias;
    }

    public boolean grabarSucursalProveedor(){
        boolean grabado = false; 
        String  texto;
        Connection con = null;
        Statement st = null;
        String sq;
        ResultSet rst = null;
        
        try {
            
              texto = "INSERT INTO  proveedoressucursales (cedula,codigoInterno,nombreSucursal,"
                    + " contactoSucursalProveedor, direccionSucursalProveedor,barrioSucursalProveedor, ciudadSucursalProveedor, telefonoSucursalProveedor, "
                    + "celularSucursalProveedor,celularCorporativo,emailSucursalProveedor, agencia, "
                    + "fechaDeIngreso, usuario,coordenadas) VALUES ('"
                    + this.getCedula() + "','"
                    + this.getCodigoInterno() +  "','"  
                    + this.getNombreSucursal() + "','"
                    + this.getContactoSucursal() + "','"
                    + this.getDireccionSucursal() + "','"
                    + this.getBarrioSucursal() + "','"
                    + this.getCiudadSucursal() + "','"
                    + this.getTelefonoSucursal() + "','"
                    + this.getCelularSucursal() + "','"
                    + this.getCelularCorporativo() + "','"
                    + this.getEmailSucursal() + "','"
                    + this.getAgencia() + "',"
                    // + this.getFechaDeIngreso() + "','"
                    + "CURDATE() ,'"
                    //+ "1','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                    //+ "1','"
                    + this.getLatitud() + "," + this.getLongitud() + "')"
                    + " ON DUPLICATE KEY UPDATE "
                    + "nombreSucursal='" + this.getNombreSucursal() + "',"
                    + "codigoInterno='" + this.getCodigoInterno() + "',"
                    + "contactoSucursalProveedor='" + this.getContactoSucursal() + "',"
                    + "direccionSucursalProveedor='" + this.getDireccionSucursal() + "',"
                    + "barrioSucursalProveedor='" + this.getBarrioSucursal() + "',"
                    + "ciudadSucursalProveedor='" + this.getCiudadSucursal() + "',"
                    + "telefonoSucursalProveedor='" + this.getTelefonoSucursal() + "',"
                    + "celularSucursalProveedor='" + this.getCelularSucursal() + "',"
                    + "celularCorporativo='" + this.getCelularCorporativo() + "',"
                    + "emailSucursalProveedor='" + this.getEmailSucursal() + "',"
                    + "agencia='" + this.getAgencia() + "',"
                    + "coordenadas='" + this.getLatitud() + "," + this.getLongitud() + "',"
                    + "usuario='" + usuario + "',"
                    + "flag='-1';";
              
              grabado = ini.insertarDatosRemotamente(texto);
              
              con = ini.getConnRemota();
              
              sq = "select idSucursalProveedor from proveedoressucursales where "
                     +"codigoInterno ='" + this.getCodigoInterno() + "' and "
                     + "nombreSucursal='" +  this.getNombreSucursal() + "';";
             
            
            if (con != null) {
                st = con.createStatement();
                rst = st.executeQuery(sq);
               if (rst.next()) {
                   this.idSucursal= rst.getInt("idSucursalProveedor");
               }
            }
            st.close();
            rst.close();
              
              List  sql = new ArrayList<>();
              
              texto = "delete from cuestasporsucursalproveedor where idSucursalProveedor='" + getIdSucursal() + "';";
              sql.add(texto);
              
                for (CCuentaSecundariaLogistica cta : getListaDeCuentasSecundarias()) {
                    texto = "INSERT INTO cuestasporsucursalproveedor(idSucursalProveedor,"
                            + "cedula,idCuentaSecundaria,activo,fechaIng,usuario,flag) VALUES('"
                            + getIdSucursal() + "','"
                            + getCedula() + "','"
                            + cta.getIdCuentaSecundaria() + "','"
                            + cta.getActivo() + "',"
                            + "CURRENT_TIMESTAMP,'"
                            + usuario + "','"
                            + "1') on duplicate key update "
                            + "activo ='" + cta.getActivo() + "',"
                            + "fechaIng=CURRENT_TIMESTAMP,"
                            + "usuario ='" + usuario + "',"
                            + "flag='-1';";

                    sql.add(texto);
                }

                grabado = ini.insertarDatosRemotamente(sql, "Cproveedores");
              
              
              
        } catch (Exception ex) {
            Logger.getLogger(SucursalesPorproveedor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }
}
