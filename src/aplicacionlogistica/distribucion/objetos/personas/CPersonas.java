/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos.personas;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class CPersonas {//extends Inicio {
    
    protected String cedula = null;
    protected String nombres = null;
    protected String apellidos = null;
    protected String direccion = null;
    protected String barrio = null;
    protected int idCiudad = 0;
    protected String nombreCiudad;
    protected String telefonoFijo = null;
    protected String telefonoCelular = null;
    protected String escolaridad = null;
    protected String genero = null;
    protected Date cumpleanios = null;
    protected String lugarNacimiento = null;
    protected int idEstadoCivil = 0;
    protected String nombreEstadoCivil = null;
    protected String email = null;
    protected int activoPersona = 0;
    protected String usuario;
    protected Inicio ini;
    protected int idTipoSangre;
    protected String tipoSangre;

  protected  int caso;

    public String getTipoSangre() {
        return tipoSangre;
    }

    public void setTipoSangre(String tipoSangre) {
        this.tipoSangre = tipoSangre;
    }

    
     public int getIdTipoSangre() {
        return idTipoSangre;
    }

    public void setIdTipoSangre(int idTipoSangre) {
        this.idTipoSangre = idTipoSangre;
    }
    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public int getCiudad() {
        return idCiudad;
    }

    public void setCiudad(int ciudad) {
        this.idCiudad = ciudad;
    }

    public String getEscolaridad() {
        return escolaridad;
    }

    public void setEscolaridad(String escolaridad) {
        this.escolaridad = escolaridad;
    }

    public Date getCumpleanios() {
        return cumpleanios;
    }

    public void setCumpleanios(Date cumpleanios) {
        this.cumpleanios = cumpleanios;
    }

    public String getLugarNacimiento() {
        return lugarNacimiento;
    }

    public void setLugarNacimiento(String lugarNacimiento) {
        this.lugarNacimiento = lugarNacimiento;
    }

    public int getEstadoCivil() {
        return idEstadoCivil;
    }

    public void setIdEstadoCivil(int estadoCivil) {
        this.idEstadoCivil = estadoCivil;
    }

    public int getActivoPersona() {
        return activoPersona;
    }

    public void setActivoPersona(int activoPersona) {
        this.activoPersona = activoPersona;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getBarrio() {
        return barrio;
    }

    public void setBarrio(String barrio) {
        this.barrio = barrio;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public void setTelefonoFijo(String telefono) {
        this.telefonoFijo = telefono;
    }

    public String getTelefonoCelular() {
        return telefonoCelular;
    }

    public void setTelefonoCelular(String celular) {
        this.telefonoCelular = celular;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    
    
    public CPersonas() {

    }

    public CPersonas(Inicio ini) {
        this.ini = ini;
    }

    public CPersonas(Inicio ini, String cedula) throws Exception {// String usuario, String cedula) throws Exception {
        //super(usuario);
        this.ini = ini;
        // this.usuario=usuario;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        sql = "SELECT cedula, nombres, apellidos, direccion, barrio, ciudad, telefonoFijo, telefonoCelular, escolaridad, "
                + "genero, cumpleanios, lugarNacimiento, estadoCivil, email, tipoSangre, activo, fechaIng, usuario, flag "
                + "FROM personas "
                + "WHERE "
                + "cedula='" + cedula + "';";
        llenarDatosRst(con, sql);

    }
    
      public CPersonas(Inicio ini, String cedula, boolean mantenimientos) throws Exception {// String usuario, String cedula) throws Exception {
        //super(usuario);
        this.ini = ini;
        // this.usuario=usuario;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;

       
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        sql = "SELECT cedula, nombres, apellidos, direccion, barrio, ciudad, telefonoFijo, telefonoCelular, escolaridad, "
                + "genero, cumpleanios, lugarNacimiento, estadoCivil, email, tipoSangre, activo, fechaIng, usuario, flag "
                + "FROM personas "
                + "WHERE "
                + "cedula='" + cedula + "';";
        llenarDatosRst(con, sql);

    }

    private void llenarDatosRst(Connection con, String sql) throws SQLException {
        Statement st;
        ResultSet rst;
        st = con.createStatement();
        if (con != null) {
            rst = st.executeQuery(sql);
            try {
                if (rst.next()) {
                    this.cedula = rst.getString("cedula");
                    this.nombres = rst.getString("nombres");
                    this.apellidos = rst.getString("apellidos");
                    this.direccion = rst.getString("direccion");
                    this.barrio = rst.getString("barrio");
                    this.idCiudad = (rst.getInt("ciudad"));
                    this.telefonoFijo = rst.getString("telefonoFijo");
                    this.telefonoCelular = rst.getString("telefonoCelular");
                    this.escolaridad = rst.getString("escolaridad");
                    this.genero = rst.getString("genero");
                    this.cumpleanios = rst.getDate("cumpleanios");
                    this.lugarNacimiento = rst.getString("lugarNacimiento");
                    this.idEstadoCivil = rst.getInt("estadoCivil");
                    this.email = rst.getString("email");
                    this.activoPersona = rst.getInt("activo");
                } else {
                    this.cedula = null;
                    this.nombres = null;
                    this.apellidos = null;
                }

                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en CPersonas " + ex.getMessage());
            }
        }
    }

    public CPersonas(Inicio ini, String usuario, String cedula, boolean isActivo) throws Exception {

        this.ini = ini;
        int activo = 0;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        if (isActivo) {
            activo = 1;
        }
        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        sql = "SELECT  cedula, nombres, apellidos, direccion, barrio, ciudad, telefonoFijo, telefonoCelular, escolaridad, "
                + "genero, cumpleanios, lugarNacimiento, estadoCivil, email, tipoSangre, activo, fechaIng, usuario, flag "
                + "FROM personas "
                + "WHERE cedula='" + cedula + "' AND "
                + "activo=" + activo + ";";
        st = con.createStatement();
        if (con != null) {
            rst = st.executeQuery(sql);
            try {
                if (rst.next()) {
                    this.cedula = rst.getString("cedula");
                    this.nombres = rst.getString("nombres");
                    this.apellidos = rst.getString("apellidos");
                    this.direccion = rst.getString("direccion");
                    this.barrio = rst.getString("barrio");
                    this.idCiudad = (rst.getInt("ciudad"));
                    this.telefonoFijo = rst.getString("telefonoFijo");
                    this.telefonoCelular = rst.getString("telefonoCelular");
                    this.escolaridad = rst.getString("escolaridad");
                    this.genero = rst.getString("genero");
                    this.cumpleanios = rst.getDate("cumpleanios");
                    this.lugarNacimiento = rst.getString("lugarNacimiento");
                    this.idEstadoCivil = rst.getInt("estadoCivil");
                    this.email = rst.getString("email");
                   
                    this.activoPersona = rst.getInt("activo");
                }
                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en CPersonas 2 " + ex.getMessage().toString());
            }
        }
    }

    public boolean grabarPersonas() {
        boolean grab = false;
        try {

            String sql;
            sql = "INSERT INTO personas (cedula, nombres, apellidos, direccion, barrio, ciudad, telefonoFijo,"
                    + "telefonoCelular, escolaridad, genero,cumpleanios, lugarNacimiento, estadoCivil, email, tipoSangre, activo,"
                    + "usuario) VALUES ('"
                    + this.cedula + "','"
                    + this.nombres + "','"
                    + this.apellidos + "','"
                    + this.direccion + "','"
                    + this.barrio + "','"
                    + this.idCiudad + "','"
                    + this.telefonoFijo + "','"
                    + this.telefonoCelular + "','"
                    + this.escolaridad + "','"
                    + this.genero + "','"
                    + this.cumpleanios + "','"
                    + this.lugarNacimiento + "','"
                    + this.idEstadoCivil + "','"
                    + this.email + "','"
                    + this.idTipoSangre + "','"
                    + this.activoPersona + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "') ON DUPLICATE KEY UPDATE "
                    + "cedula='" + this.cedula + "',"
                    + "nombres='" + this.nombres + "',"
                    + "apellidos='" + this.apellidos + "',"
                    + "direccion='" + this.direccion + "',"
                    + "barrio='" + this.barrio + "',"
                    + "ciudad=" + this.idCiudad + ","
                    + "telefonoFijo='" + this.telefonoFijo + "',"
                    + "telefonoCelular='" + this.telefonoCelular + "',"
                    + "escolaridad='" + this.escolaridad + "',"
                    + "genero='" + this.genero + "',"
                    + "cumpleanios='" + this.cumpleanios + "',"
                    + "lugarNacimiento='" + this.lugarNacimiento + "',"
                    + "estadoCivil=" + this.idEstadoCivil + ","
                    + "email='" + this.email + "',"
                    + "tipoSangre='" + this.idTipoSangre + "',"
                    + "activo=" + this.activoPersona + ";";

            grab = ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabarPersonas  " + ex.getMessage());
        }
        return grab;
    }

    public boolean grabarPersonas(int caso) {
        boolean grab = false;
        this.caso = caso;
        try {

            String sql;
            sql = "INSERT INTO personas (cedula, nombres, apellidos, direccion, barrio, ciudad, telefonoFijo,"
                    + "telefonoCelular, escolaridad, genero,cumpleanios, lugarNacimiento, estadoCivil, email, tipoSangre, activo,"
                    + "usuario) VALUES ('"
                    + this.cedula + "','"
                    + this.nombres + "','"
                    + this.apellidos + "','"
                    + this.direccion + "','"
                    + this.barrio + "',"
                    + this.idCiudad + ",'"
                    + this.telefonoFijo + "','"
                    + this.telefonoCelular + "','"
                    + this.escolaridad + "','"
                    + this.genero + "','"
                    + this.cumpleanios + "','"
                    + this.lugarNacimiento + "',"
                    + this.idEstadoCivil + ",'"
                    + this.email + "',"
                   // + idTipoSangre + ","
                    + this.activoPersona + ",'"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "') ON DUPLICATE KEY UPDATE "
                    + "cedula='" + this.cedula + "',"
                    + "nombres='" + this.nombres + "',"
                    + "apellidos='" + this.apellidos + "',"
                    + "direccion='" + this.direccion + "',"
                    + "barrio='" + this.barrio + "',"
                    + "ciudad=" + this.idCiudad + ","
                    + "telefonoFijo='" + this.telefonoFijo + "',"
                    + "telefonoCelular='" + this.telefonoCelular + "',"
                    + "escolaridad='" + this.escolaridad + "',"
                    + "genero='" + this.genero + "',"
                    + "cumpleanios='" + this.cumpleanios + "',"
                    + "lugarNacimiento='" + this.lugarNacimiento + "',"
                    + "estadoCivil=" + this.idEstadoCivil + ","
                    + "email='" + this.email + "',"
                  //  + "tipoSangre=" + this.idTipoSangre + ","
                    + "activo=" + this.activoPersona + ";";

            grab = ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabarPersonas 2  " + ex.getMessage());
        }
        return grab;
    }

    public String getSentenciaInsertSQL() {
        String sql = null;

        try {
            sql = "INSERT INTO personas (cedula, nombres, apellidos, direccion, barrio, ciudad, telefonoFijo,"
                    + "telefonoCelular, escolaridad, genero,cumpleanios, lugarNacimiento, estadoCivil, email, tipoSangre, activo,"
                    + "usuario) VALUES ('"
                    + this.cedula + "','"
                    + this.nombres + "','"
                    + this.apellidos + "','"
                    + this.direccion + "','"
                    + this.barrio + "',"
                    + this.idCiudad + ",'"
                    + this.telefonoFijo + "','"
                    + this.telefonoCelular + "','"
                    + this.escolaridad + "','"
                    + this.genero + "','"
                    + this.cumpleanios + "','"
                    + this.lugarNacimiento + "',"
                    + this.idEstadoCivil + ",'"
                    + this.email + "',"
                  //  + idTipoSangre + ","
                    + this.activoPersona + ",'"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "') ON DUPLICATE KEY UPDATE "
                    + "cedula='" + this.cedula + "',"
                    + "nombres='" + this.nombres + "',"
                    + "apellidos='" + this.apellidos + "',"
                    + "direccion='" + this.direccion + "',"
                    + "barrio='" + this.barrio + "',"
                    + "ciudad=" + this.idCiudad + ","
                    + "telefonoFijo='" + this.telefonoFijo + "',"
                    + "telefonoCelular='" + this.telefonoCelular + "',"
                    + "escolaridad='" + this.escolaridad + "',"
                    + "genero='" + this.genero + "',"
                    + "cumpleanios='" + this.cumpleanios + "',"
                    + "lugarNacimiento='" + this.lugarNacimiento + "',"
                    + "estadoCivil=" + this.idEstadoCivil + ","
                    + "email='" + this.email + "',"
                 //   + "tipoSangre=" + this.idTipoSangre + ","
                    + "activo=" + this.activoPersona + ";";
        } catch (Exception ex) {
            Logger.getLogger(CPersonas.class.getName()).log(Level.SEVERE, null, ex);
        }

        return sql;
    }

}
