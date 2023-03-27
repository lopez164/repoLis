/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos.personas;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import static aplicacionlogistica.configuracion.Inicio.cifrar;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class CUsuarios extends CPersonas {

    private String nombreUsuario;
    private String claveUsuario;
    int tipoAcceso;
    int nivelAcceso;
    private int usuarioActivo;

    int agencia;
    int zona;
    int regional;

    public int getAgencia() {
        return agencia;
    }

    public void setAgencia(int agencia) {
        this.agencia = agencia;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public int getRegional() {
        return regional;
    }

    public void setRegional(int regional) {
        this.regional = regional;
    }

    public void setNivelAcceso(int nivelAcceso) {
        this.nivelAcceso = nivelAcceso;
    }

    public int getNivelAcceso() {
        return nivelAcceso;
    }

    public int getUsuarioActivo() {
        return usuarioActivo;
    }

    public void setUsuarioActivo(int usuarioActivo) {
        this.usuarioActivo = usuarioActivo;
    }

    public int getTipoAcceso() {
        return tipoAcceso;
    }

    public void setTipoAcceso(int tipoAcceso) {
        this.tipoAcceso = tipoAcceso;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    public int getActivoUsuario() {
        return usuarioActivo;
    }

    public void setActivoUsuario(int activoUsuario) {
        this.usuarioActivo = activoUsuario;
    }

    public CUsuarios(Inicio ini) throws Exception {
        super(ini);
    }

    public CUsuarios(Inicio ini, String cedula, boolean crear) throws Exception {
        super(ini, cedula);

    }

    public CUsuarios(Inicio ini, String cedula) throws Exception {
        super(ini);
        this.ini = ini;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        sql = "SELECT p.cedula, p.nombres, p.apellidos, p.direccion, p.barrio, p.ciudad,"
                + "p.telefonoFijo, p.telefonoCelular, p.escolaridad, p.genero, p.cumpleanios,"
                + "p.lugarNacimiento, p.estadoCivil, p.email, p.tipoSangre,"
                + "u.nombreUsuario, u.claveUsuario, u.tipoAcceso, u.idZona, u.idRegional,"
                + "u.idAgencia,u.activo, u.nivelAcceso "
                + "FROM personas p "
                + "left outer join usuarios u on p.cedula=u.cedula "
                + "where p.cedula='" + cedula + "';";

//                 "SELECT personas.cedula, personas.nombres, personas.apellidos, personas.direccion, personas.barrio, personas.ciudad,"
//                + " personas.telefonoFijo, personas.telefonoCelular, personas.escolaridad, personas.genero, personas.cumpleanios,"
//                + " personas.lugarNacimiento, personas.estadoCivil, personas.email, personas.tipoSangre,"
//                + "usuarios.nombreUsuario, usuarios.claveUsuario, usuarios.tipoAcceso, usuarios.idZona, usuarios.idRegional, "
//                + "usuarios.idAgencia,usuarios.activo, usuarios.nivelAcceso "
//                + "FROM usuarios,personas "
//                + "WHERE "
//                + "usuarios.cedula=personas.cedula and "
//                + "usuarios.cedula='" + cedula + "' and usuarios.activo=1;";
//                 
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            try {
                if (rst.next()) {
                    this.cedula = rst.getString("cedula");
                    this.nombres = rst.getString("nombres");
                    this.apellidos = rst.getString("apellidos");
                    this.direccion = rst.getString("direccion");
                    this.barrio = rst.getString("barrio");
                    this.idCiudad = rst.getInt("ciudad");
                    this.telefonoFijo = rst.getString("telefonoFijo");
                    this.telefonoCelular = rst.getString("telefonoCelular");
                    this.escolaridad = rst.getString("escolaridad");
                    this.genero = rst.getString("genero");
                    this.cumpleanios = rst.getDate("cumpleanios");
                    this.lugarNacimiento = rst.getString("lugarNacimiento");
                    idEstadoCivil = rst.getInt("estadoCivil");
                    this.email = rst.getString("email");
                    idTipoSangre = rst.getInt("tipoSangre");
                    this.nombreUsuario = (rst.getString("nombreUsuario"));
                    this.claveUsuario = (rst.getString("claveUsuario"));
                    this.tipoAcceso = rst.getInt("tipoAcceso");
                    this.zona = rst.getInt("idZona");
                    this.regional = rst.getInt("idRegional");
                    this.agencia = rst.getInt("idAgencia");
                    //this.usuarioActivo = rst.getInt("activo");
                    this.nivelAcceso = rst.getInt("nivelAcceso");

                } else {
                    this.cedula = null;

                }
                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en CUsuarios 1 " + ex.getMessage().toString());
            }

        }

    }

    public CUsuarios(Inicio ini, String usuario, String clave) throws Exception {
        super(ini);
        this.ini = ini;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
        sql = "SELECT  personas.cedula, personas.nombres, personas.apellidos, personas.direccion, personas.barrio, personas.ciudad,"
                + " personas.telefonoFijo, personas.telefonoCelular, personas.escolaridad, personas.genero, personas.cumpleanios,"
                + " personas.lugarNacimiento, personas.estadoCivil, personas.email, personas.tipoSangre,"
                + "usuarios.nombreUsuario, usuarios.claveUsuario, usuarios.tipoAcceso, usuarios.idZona, usuarios.idRegional, "
                + "usuarios.idAgencia, usuarios.nivelAcceso "
                + " FROM usuarios,personas "
                + "WHERE "
                + "usuarios.cedula=personas.cedula AND "
                + "usuarios.nombreUsuario='" + usuario + "' AND "
                + "usuarios.claveUsuario='" + clave + "' AND usuarios.activo=1;";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            try {
                if (rst.next()) {
                    this.cedula = rst.getString("cedula");
                    this.nombres = rst.getString("nombres");
                    this.apellidos = rst.getString("apellidos");
                    this.direccion = rst.getString("direccion");
                    this.barrio = rst.getString("barrio");
                    this.idCiudad = rst.getInt("ciudad");
                    this.telefonoFijo = rst.getString("telefonoFijo");
                    this.telefonoCelular = rst.getString("telefonoCelular");
                    this.escolaridad = rst.getString("escolaridad");
                    this.genero = rst.getString("genero");
                    this.cumpleanios = rst.getDate("cumpleanios");
                    this.lugarNacimiento = rst.getString("lugarNacimiento");
                    idEstadoCivil = rst.getInt("estadoCivil");
                    this.email = rst.getString("email");
                    idTipoSangre = rst.getInt("tipoSangre");
                    this.nombreUsuario = (rst.getString("nombreUsuario"));
                    this.claveUsuario = (rst.getString("claveUsuario"));
                    this.tipoAcceso = rst.getInt("tipoAcceso");
                    this.zona = rst.getInt("idZona");
                    this.regional = rst.getInt("idRegional");
                    this.agencia = rst.getInt("idAgencia");
                    //this.usuarioActivo = rst.getInt("activo");
                    this.nivelAcceso = rst.getInt("nivelAcceso");

                } else {
                    this.cedula = null;
                    this.nombres = null;
                    this.apellidos = null;
                    this.direccion = null;
                    this.barrio = null;
                    this.idCiudad = 0;
                    this.telefonoFijo = null;
                    this.telefonoCelular = null;
                    this.escolaridad = null;
                    this.genero = null;
                    this.cumpleanios = null;
                    this.lugarNacimiento = null;
                    idEstadoCivil = 0;
                    this.email = null;
                    idTipoSangre = 0;
                    this.nombreUsuario = null;
                    this.claveUsuario = null;
                    this.tipoAcceso = 0;
                    this.zona = 0;
                    this.regional = 0;
                    this.agencia = 0;
                    this.usuarioActivo = 0;
                    this.nivelAcceso = 0;

                }
                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en CUsuarios 2 " + ex.getMessage().toString());
            }

        }

    }

    public CUsuarios(Inicio ini, String nombreUsuario, int valor) throws Exception {
        super(ini);
        this.ini = ini;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        sql = "SELECT personas.cedula, personas.nombres, personas.apellidos, personas.direccion, personas.barrio, personas.ciudad,"
                + " personas.telefonoFijo, personas.telefonoCelular, personas.escolaridad, personas.genero, personas.cumpleanios,"
                + " personas.lugarNacimiento, personas.estadoCivil, personas.email, personas.tipoSangre,"
                + "usuarios.nombreUsuario, usuarios.claveUsuario, usuarios.tipoAcceso, usuarios.idZona, usuarios.idRegional, "
                + "usuarios.idAgencia,usuarios.activo, usuarios.nivelAcceso "
                + "FROM usuarios,personas "
                + "WHERE "
                + "usuarios.cedula=personas.cedula and "
                + "usuarios.nombreUsuario='" + Inicio.cifrar(nombreUsuario) + "';";
        if (con != null) {
            st = con.createStatement();
            rst = st.executeQuery(sql);
            try {
                if (rst.next()) {
                    this.cedula = rst.getString("cedula");
                    this.nombres = rst.getString("nombres");
                    this.apellidos = rst.getString("apellidos");
                    this.direccion = rst.getString("direccion");
                    this.barrio = rst.getString("barrio");
                    this.idCiudad = rst.getInt("ciudad");
                    this.telefonoFijo = rst.getString("telefonoFijo");
                    this.telefonoCelular = rst.getString("telefonoCelular");
                    this.escolaridad = rst.getString("escolaridad");
                    this.genero = rst.getString("genero");
                    this.cumpleanios = rst.getDate("cumpleanios");
                    this.lugarNacimiento = rst.getString("lugarNacimiento");
                    idEstadoCivil = rst.getInt("estadoCivil");
                    this.email = rst.getString("email");
                    idTipoSangre = rst.getInt("tipoSangre");
                    this.nombreUsuario = (rst.getString("nombreUsuario"));
                    this.claveUsuario = (rst.getString("claveUsuario"));
                    this.tipoAcceso = rst.getInt("tipoAcceso");
                    this.zona = rst.getInt("idZona");
                    this.regional = rst.getInt("idRegional");
                    this.agencia = rst.getInt("idAgencia");
                    //this.usuarioActivo = rst.getInt("activo");
                    this.nivelAcceso = rst.getInt("nivelAcceso");

                } else {
                    this.cedula = null;
                    this.nombres = null;
                    this.apellidos = null;
                    this.direccion = null;
                    this.barrio = null;
                    this.idCiudad = 0;
                    this.telefonoFijo = null;
                    this.telefonoCelular = null;
                    this.escolaridad = null;
                    this.genero = null;
                    this.cumpleanios = null;
                    this.lugarNacimiento = null;
                    this.idEstadoCivil = 0;
                    this.email = null;
                    this.idTipoSangre = 0;
                    this.nombreUsuario = null;
                    this.claveUsuario = null;
                    this.tipoAcceso = 0;
                    this.zona = 0;
                    this.regional = 0;
                    this.agencia = 0;
                    this.usuarioActivo = 0;
                    this.nivelAcceso = 0;

                }
                rst.close();
                st.close();
                con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en CUsuarios 1 " + ex.getMessage().toString());
            }

        }

    }

    public void BuscarUsuarios(String login) {
        try {
            this.nombreUsuario = login;
            Connection con;
            Statement st;
            String sql;
            ResultSet rst;

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            sql = "select cedula, nombreUsuario, claveUsuario, tipoAcceso, idZona,"
                    + " idRegional, idAgencia, activo, fechaIng, usuario, flag, "
                    + "nivelAcceso "
                    + "from usuarios "
                    + "where "
                    + "nombreUsuario='" + Inicio.cifrar(nombreUsuario) + "' and "
                    + "activo=1 ;";
            if (con != null) {

                try {
                    st = con.createStatement();
                    rst = st.executeQuery(sql);
                    if (rst.next()) {
                        this.cedula = (rst.getString("cedula"));
                        this.nombreUsuario = (rst.getString("nombreUsuario"));
                        this.claveUsuario = (rst.getString("claveUsuario"));
                        //this.tipoAcceso = new CTiposDeAcceso(getUsuarioDelSistema(), rst.getInt(4));
                        this.tipoAcceso = rst.getInt("tipoAcceso");
                        // this.agenciaUsuario = new CAgencias(ini, rst.getInt(7));
                        this.usuarioActivo = rst.getInt("activo");
                        //this.nivelAcceso=new CNivelesDeAcceso(getUsuarioDelSistema(), rst.getInt(12));
                        this.nivelAcceso = rst.getInt("nivelAcceso");

                    } else {
                        this.cedula = null;
                        this.nombreUsuario = "";
                        this.claveUsuario = "";
                        this.tipoAcceso = 0;
                        //this.agenciaUsuario = null;
                        this.usuarioActivo = 1;
                        this.nivelAcceso = 0;

                    }

                    rst.close();
                    st.close();
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error en BuscarUsuarios " + ex.getMessage());
                } catch (Exception ex) {
                    Logger.getLogger(CUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error en BuscarUsuarios " + ex.getMessage());
                }

            }
        } catch (Exception ex) {
            Logger.getLogger(CUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en BuscarUsuarios " + ex.getMessage());
        }

    }

    public boolean verificarUsuario(String usuario) {
        boolean verificado = false;
        try {

            Connection con;
            Statement st;
            String sql;
            ResultSet rst;
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            sql = "select nombreUsuario "
                    + "from usuarios "
                    + "where nombreUsuario='" + Inicio.cifrar(usuario) + "' and "
                    + "activo=1 ;";

            if (con != null) {

                try {
                    st = con.createStatement();
                    rst = st.executeQuery(sql);
                    if (rst.next()) {
                        verificado = true;
                    } else {
                        verificado = false;
                    }
                    rst.close();
                    st.close();
                    con.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error en verificarUsuario " + ex.getMessage().toString());
                } catch (Exception ex) {
                    Logger.getLogger(CUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println("Error en verificarUsuario " + ex.getMessage());
                }

            }

        } catch (Exception ex) {
            Logger.getLogger(CUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en verificarUsuario " + ex.getMessage());
        }
        return verificado;
    }

    public boolean grabarUsuario() {
        String sql;
        boolean insertar = false;

        try {
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
            insertar = ini.insertarDatosRemotamente(sql);

            sql = "INSERT INTO usuarios (cedula, nombreUsuario, claveUsuario, "
                    + "tipoAcceso, idZona, idRegional, idAgencia, activo,"
                    + " usuario, flag,nivelAcceso) VALUES ('"
                    + this.cedula + "','"
                    + Inicio.cifrar(this.nombreUsuario) + "','"
                    + Inicio.cifrar(this.claveUsuario) + "','"
                    + this.tipoAcceso + "','"
                    + this.zona + "','"
                    + this.regional + "','"
                    + this.agencia + "','"
                    + this.usuarioActivo + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','1','"
                    + this.nivelAcceso + "') "
                    + "ON DUPLICATE KEY UPDATE "
                    + "nombreUsuario='" + Inicio.cifrar(this.nombreUsuario) + "',"
                    + "claveUsuario='" + Inicio.cifrar(this.claveUsuario) + "',"
                    + "tipoAcceso='" + this.tipoAcceso + "',"
                    + "nivelAcceso='" + this.nivelAcceso + "',"
                    + "idZona='" + this.zona + "',"
                    + "idRegional='" + this.regional + "',"
                    + "idAgencia='" + this.agencia + "',"
                    + "activo='" + this.usuarioActivo + "'";

            insertar = ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            System.out.println("Error en grabarUsuario " + ex.getMessage());
            Logger.getLogger(CUsuarios.class.getName()).log(Level.SEVERE, null, ex);
          
        }

        return insertar;
    }

    public boolean actualizarUsuario() {
        String sql;
        boolean insertar = false;
        if (grabarPersonas()) {
            try {
                sql = "UPDATE usuarios  SET "
                        + "cedula='" + this.cedula + "',"
                        + "tipoAcceso='" + this.tipoAcceso + "',"
                        + "nivelAcceso='" + this.nivelAcceso + "',"
                        + "idZona='" + this.zona + "',"
                        + "idRegional='" + this.regional + "',"
                        + "idAgencia='" + this.agencia + "',"
                        + "activo='" + this.usuarioActivo + "'  "
                        + " WHERE "
                        + "cedula='" + this.cedula + "';";

                insertar = ini.insertarDatosRemotamente(sql);

            } catch (Exception ex) {
                Logger.getLogger(CUsuarios.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Error en grabarUsuario " + ex.getMessage());
            }
        } else {

        }

        return insertar;
    }


    public boolean cambiarClave(String usuario, String claveNueva) {
        boolean actualizado = false;
        try {
            String sql = "UPDATE usuarios SET claveUsuario='" + Inicio.cifrar(claveNueva) + "' "
                    + "WHERE nombreUsuario='" + Inicio.cifrar(usuario) + "';";

            if (ini.insertarDatosRemotamente(sql)) {
                actualizado = true;
            }

        } catch (Exception ex) {
            Logger.getLogger(CUsuarios.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en cambiarClave " + ex.getMessage());
        }

        return actualizado;
    }

    public boolean comparar(String user, String psswd) {
        boolean verificado = false;
        String sql;
        Statement st;
        ResultSet rst;

        try {

            Connection con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
            if (con != null) {
                st = con.createStatement();
                sql = "SELECT COUNT(cedula) "
                        + " FROM  usuarios "
                        + "WHERE nombreUsuario='" + cifrar(user)
                        + "' AND claveUsuario='" + cifrar(psswd) + "';";

                rst = st.executeQuery(sql);
                if (rst.next()) {
                    verificado = true;
                } else {
                    verificado = false;

                }
                rst.close();
                st.close();
                con.close();
            }

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Error en comparar() consulta sql " + e);
            JOptionPane.showMessageDialog(null, "Error al retornar dato : \n" + e);
        } catch (Exception ex) {
            Logger.getLogger(Inicio.class.getName()).log(Level.SEVERE, null, ex);
        }

        return verificado;
    }

    public static String rstListadodeUsuarios() {

        String sql = "SELECT personas.cedula, personas.nombres, personas.apellidos, personas.direccion, personas.barrio, personas.ciudad,"
                + " personas.telefonoFijo, personas.telefonoCelular, personas.escolaridad, personas.genero, personas.cumpleanios,"
                + " personas.lugarNacimiento, personas.estadoCivil, personas.email, personas.tipoSangre,"
                + "usuarios.nombreUsuario, usuarios.claveUsuario, usuarios.tipoAcceso, usuarios.idZona, usuarios.idRegional, "
                + "usuarios.idAgencia,usuarios.activo, usuarios.nivelAcceso "
                + "FROM usuarios,personas "
                + "WHERE "
                + "usuarios.cedula=personas.cedula;";

        return sql;
    }

    /**
     * Método que devuelve una cadena con sentencia SQL para
     * insertarDatosLocalmente datos en la BBDD ","
     *
     * @return una cadena con la sentencia SQL para isertar Datos
     */
    public String getSentenciaInsertSQL() {
        String sql = null;

        return sql;
    }

    public boolean resetearContraseña(String cedula) {
        boolean grabado = false;
        try {
            String sql = "UPDATE usuarios SET claveUsuario='wRYIvKPLN8RvZSaYCUs9Ag' "
                    + "WHERE cedula='" + cedula + "';";

            grabado = ini.insertarDatosRemotamente(sql);

        } catch (Exception ex) {
            Logger.getLogger(CUsuarios.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

}
