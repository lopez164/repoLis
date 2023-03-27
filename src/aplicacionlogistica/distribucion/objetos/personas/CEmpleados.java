/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos.personas;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.Threads.HiloListadoDeManifiestosSinDescargar;
import aplicacionlogistica.distribucion.formularios.Hielera.Threads.HiloGuardarAlertasSalidaDeProducto;
import aplicacionlogistica.distribucion.objetos.CFacturas;
import aplicacionlogistica.distribucion.objetos.CFacturasPorManifiesto;
import aplicacionlogistica.distribucion.objetos.CManifiestosDeDistribucion;
import aplicacionlogistica.distribucion.objetos.CProductosPorMinuta;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author Usuario
 */
public class CEmpleados extends CPersonas {

    int idCargo;
    String nomBreCargo;
    String celularCorporativo;
    Date fechaIngresoEmpresa;
    int idAgencia;
    String nombreAgencia;

    int isFree;

    int regional;
    int zona;
    int idCentroDeCosto;
    String nombreCentroDeCosto;

    File fotografia = null;
    String formatoFotografia;
    ImageIcon image;

    int idTipoDeContrato;
    String tipoContrato;

    String numeroCuenta;
    int idBanco;
    String nombreBanco;

    Date fechaDeIngreso;
    Date fechaDeRetiro;
    int empleadoActivo;

    String cargo;

    List<CManifiestosDeDistribucion> listaDeManifiestosPorConductor;
    List<CManifiestosDeDistribucion> listaDeManifiestosSinDescargarPorConductor;
    List<CManifiestosDeDistribucion> listaDeManifiestosDescargadosPorConductor;
    List<CFacturasPorManifiesto> listaDeFacturasPorConductor;
    List<CFacturasPorManifiesto> listaDeFacturasPorConductorSinDespachar;
    List<CFacturasPorManifiesto> listaFacturasDescargadas;
    List<CFacturasPorManifiesto> listaFacturasPendientesPorDescargar;

    List<CProductosPorMinuta> minutaConductor;
    CManifiestosDeDistribucion manifiesto = null;

    public String getNomBreCargo() {
        return nomBreCargo;
    }

    public void setNomBreCargo(String nomBreCargo) {
        this.nomBreCargo = nomBreCargo;
    }

    public List<CProductosPorMinuta> getMinutaConductor() {
        return minutaConductor;
    }

    public void setMinutaConductor(List<CProductosPorMinuta> minutaConductor) {
        this.minutaConductor = minutaConductor;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTipoContrato() {
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public String getNombreAgencia() {
        return nombreAgencia;
    }

    public void setNombreAgencia(String nombreAgencia) {
        this.nombreAgencia = nombreAgencia;
    }

    public String getNombreCentroDeCosto() {
        return nombreCentroDeCosto;
    }

    public void setNombreCentroDeCosto(String nombreCentroDeCosto) {
        this.nombreCentroDeCosto = nombreCentroDeCosto;
    }

    public String getNombreBanco() {
        return nombreBanco;
    }

    public void setNombreBanco(String nombreBanco) {
        this.nombreBanco = nombreBanco;
    }

    public String getNombreEstadoCivil() {
        return nombreEstadoCivil;
    }

    public void setNombreEstadoCivil(String nombreEstadoCivil) {
        this.nombreEstadoCivil = nombreEstadoCivil;
    }

    public int getIsFree() {
        return isFree;
    }

    public void setIsFree(int isFree) {
        this.isFree = isFree;
    }

    public int getRegional() {
        return regional;
    }

    public void setRegional(int regional) {
        this.regional = regional;
    }

    public int getZona() {
        return zona;
    }

    public void setZona(int zona) {
        this.zona = zona;
    }

    public String getFormatoFotografia() {
        return formatoFotografia;
    }

    public void setFormatoFotografia(String formatoFotografia) {
        this.formatoFotografia = formatoFotografia;
    }

    public int getIdTipoDeContrato() {
        return idTipoDeContrato;
    }

    public void setIdTipoDeContrato(int tipoDeContrato) {
        this.idTipoDeContrato = tipoDeContrato;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int entidadBancaria) {
        this.idBanco = entidadBancaria;
    }

    public int getIdCargo() {
        return idCargo;
    }

    public ImageIcon getImage() {
        return image;
    }

    public ImageIcon setImage() {
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        con = ini.getConnRemota();
        
        sql = "SELECT fotografia "
                + "FROM empleados "
                + "WHERE "
                + "cedula='" + cedula + "';";

        if (con != null) {
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                try {
                    if (rst.next()) {
                        InputStream is = rst.getBinaryStream("fotografia");
                        // BufferedImage bi = ImageIO.read(is);
                        BufferedImage bi = ImageIO.read(is);
                        if (bi != null) {
                            this.image = new ImageIcon(bi);
                        } else {
                            this.image = null;
                        }

                    }

                    rst.close();
                    st.close();
                    // con.close();
                } catch (SQLException | IOException ex) {
                    Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
                }

            } catch (SQLException ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return this.image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public void setIdCargo(int cargo) {
        this.idCargo = cargo;
    }

    public String getCelularCorporativo() {
        return celularCorporativo;
    }

    public void setCelularCorporativo(String celularCorporativo) {
        this.celularCorporativo = celularCorporativo;
    }

    public Date getFechaIngresoEmpresa() {
        return fechaIngresoEmpresa;
    }

    public void setFechaDeIngreso(Date fechaIngresoEmpresa) {
        this.fechaIngresoEmpresa = fechaIngresoEmpresa;
    }

    public int getIdAgencia() {
        return idAgencia;
    }

    public void setIdAgencia(int agencia) {
        this.idAgencia = agencia;
    }

    public int getIdCentroDeCosto() {
        return idCentroDeCosto;
    }

    public void setIdCentroDeCosto(int centroDeCosto) {
        this.idCentroDeCosto = centroDeCosto;
    }

    public File getFotografia() {
        return fotografia;
    }

    public void setFotografia(File fotografia) {
        this.fotografia = fotografia;
    }

    public int getEmpleadoActivo() {
        return empleadoActivo;
    }

    public void setEmpleadoActivo(int empleadoActivo) {
        this.empleadoActivo = empleadoActivo;
    }

    public CEmpleados() {
    }

    public CEmpleados(Inicio ini) throws Exception {
        super(ini);

    }

    public CEmpleados(Inicio ini, String cedula) throws Exception {

        super(ini, cedula);
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        con= ini.getConnRemota();
//        sql = "SELECT  * "
//                + "FROM vst_empleados "
//                + "WHERE "
//                + "cedula='" + cedula + "';";
        sql = "SELECT p.cedula,  p.nombres,  p.apellidos,  p.direccion,  p.barrio,  p.ciudad,  p.telefonoFijo,"
                + "p.telefonoCelular,p.escolaridad,  p.genero, p.cumpleanios,  p.lugarNacimiento,  p.estadoCivil as idEstadoCivil,"
                + "p.email,p.tipoSangre, p.activo,p.fechaIng, p.usuario,p.flag,"
                + "e.cargo,e.celularCorporativo,e.fechaDeIngreso,e.idAgencia, e.idRegional, e.idZona, e.idCentroDeCosto,"
                + "e.fotografia,e.formatoFotografia, e.idTipoDeContrato,e.numeroCuenta,e.entidadBancaria as idBanco,"
                + "e.fechaDeRetiro,e.isFree,e.activo,e.fechaIng,e.usuario,e.flag "
                + "FROM personas p "
                + "left outer join empleados e on e.cedula=p.cedula "
                + "where p.cedula='" + cedula + "';";

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
                    this.idEstadoCivil = rst.getInt("idEstadoCivil");
                    this.email = rst.getString("eMail");
                    this.tipoSangre = rst.getString("tipoSangre");
                    this.cargo = rst.getString("cargo");
                    this.celularCorporativo = rst.getString("celularCorporativo");
                    this.fechaIngresoEmpresa = rst.getDate("fechaDeIngreso");
                    this.idAgencia = rst.getInt("idAgencia");
                    //   this.regional = rst.getInt("idRegional");
                    //   this.zona = rst.getInt("idZona");
                    this.idCentroDeCosto = rst.getInt("idCentroDeCosto");

                    //  InputStream is = rst.getBinaryStream("fotografia");
                    //  BufferedImage bi = ImageIO.read(is);
//                    if (bi != null) {
//                        image = new ImageIcon(bi);
//                    } else {
//                        image = null;
//                    }
//                    this.formatoFotografia = rst.getString("formatoFotografia");
                    this.idTipoDeContrato = rst.getInt("idTipoDeContrato");
                    this.numeroCuenta = rst.getString("numeroCuenta");
                    this.idBanco = rst.getInt("idBanco");
                    //  this.nombreBanco = rst.getString("nombreBanco");
//                    this.isFree = rst.getInt("isFree");
                    this.empleadoActivo = rst.getInt("activo");

                } else {
                    this.cedula = null;

                }
                rst.close();
                st.close();
                // con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public CEmpleados(Inicio ini, String cedula, boolean isActivo) throws Exception {

        super(ini, cedula);
        int activo = 0;
        Connection con;
        Statement st;
        String sql;
        ResultSet rst;
        if (isActivo) {
            activo = 1;
        }
        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        con = ini.getConnRemota();
        
        sql = "SELECT  * "
                + "FROM vst_empleados "
                + "WHERE "
                + "cedula='" + cedula + "' and "
                + "activo='" + activo + "' ;";

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
                    this.idEstadoCivil = rst.getInt("estadoCivil");
                    this.email = rst.getString("eMail");
                    this.idTipoSangre = rst.getInt("idTipoSangre");
                    this.idCargo = rst.getInt("idCargo");
                    this.celularCorporativo = rst.getString("celularCorporativo");
                    this.fechaIngresoEmpresa = rst.getDate("fechaDeIngreso");
                    this.idAgencia = rst.getInt("idAgencia");
                    this.regional = rst.getInt("idRegional");
                    this.zona = rst.getInt("idZona");
                    this.idCentroDeCosto = rst.getInt("idCentroDeCosto");

//                    InputStream is = rst.getBinaryStream("fotografia");
//                    BufferedImage bi = ImageIO.read(is);
//                    if (bi != null) {
//                        image = new ImageIcon(bi);
//                    } else {
//                        image = null;
//                    }
                    this.formatoFotografia = rst.getString("formatoFotografia");
                    this.idTipoDeContrato = rst.getInt("idTipoDeContrato");
                    this.numeroCuenta = rst.getString("numeroCuenta");
                    this.idBanco = rst.getInt("entidadBancaria");
                    this.isFree = rst.getInt("isFree");
                    this.empleadoActivo = rst.getInt("activo");

                } else {
                    this.cedula = null;

                }
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public boolean grabarEmpleados() {
        boolean grab = false;

        ArrayList<String> sql = new ArrayList();

        try {
              /*ingreso a tabla empleados*/
            String 
            
            /*ingreso a tabla personas*/
            texto = "INSERT INTO personas (cedula, nombres, apellidos, direccion, barrio, ciudad, telefonoFijo,"
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
                    + idTipoSangre + "','"
                    + this.activoPersona + "','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "') ON DUPLICATE KEY UPDATE "
                    + "cedula='" + this.cedula + "',"
                    + "nombres='" + this.nombres + "',"
                    + "apellidos='" + this.apellidos + "',"
                    + "direccion='" + this.direccion + "',"
                    + "barrio='" + this.barrio + "',"
                    + "ciudad='" + this.idCiudad + "',"
                    + "telefonoFijo='" + this.telefonoFijo + "',"
                    + "telefonoCelular='" + this.telefonoCelular + "',"
                    + "escolaridad='" + this.escolaridad + "',"
                    + "genero='" + this.genero + "',"
                    + "cumpleanios='" + this.cumpleanios + "',"
                    + "lugarNacimiento='" + this.lugarNacimiento + "',"
                    + "estadoCivil='" + this.idEstadoCivil + "',"
                    + "email='" + this.email + "',"
                    + "tipoSangre='" + this.idTipoSangre + "',"
                    + "activo='" + this.activoPersona + "';";
            sql.add(texto);

          texto = "INSERT INTO empleados (cedula, cargo, celularCorporativo, fechaDeIngreso, idAgencia,idRegional,idZona,"
                    + "idCentroDeCosto,formatoFotografia,idTipoDeContrato,numeroCuenta,entidadBancaria,"
                    + " activo, usuario) VALUES ('"
                    + this.cedula + "','"
                    + this.idCargo + "','"
                    + this.celularCorporativo + "','"
                    + this.fechaIngresoEmpresa + "','"
                    + this.idAgencia + "','"
                    + this.regional + "','"
                    + this.zona + "','"
                    + this.idCentroDeCosto + "','" //  aqui va la imagen
                    + this.formatoFotografia + "','"
                    + this.idTipoDeContrato + "','"
                    + this.numeroCuenta + "','"
                    + this.idBanco + "','"
                    + this.empleadoActivo + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "cargo='" + idCargo + "',"
                    + "celularCorporativo='" + this.celularCorporativo + "',"
                    + "fechaDeIngreso='" + this.fechaIngresoEmpresa + "',"
                    + "idAgencia='" + this.idAgencia + "',"
                    + "idRegional='" + this.regional + "',"
                    + "idZona='" + this.zona + "',"
                    + "idCentroDeCosto='" + this.idCentroDeCosto + "',"//  aqui va la imagen
                    + "idTipoDeContrato='" + this.idTipoDeContrato + "',"
                    + "numeroCuenta='" + this.numeroCuenta + "',"
                    + "entidadBancaria='" + this.idBanco + "',"
                    + "activo='" + this.empleadoActivo + "',"
                    + "usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "';";

            sql.add(texto);

            grab = ini.insertarDatosRemotamente(sql, "empleados");

        } catch (Exception ex) {
            Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }

        return grab;
    }

    public boolean actualizarEmpleados() {
        boolean grab = false;
        try {

        } catch (Exception ex) {
            Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grab;
    }

    public ArrayList<String> getSentenciaInsertSQL_() {

        ArrayList<String> sql = new ArrayList();

        try {
            /*ingreso a tabla personas*/
            String texto = "INSERT INTO personas (cedula, nombres, apellidos, direccion, barrio, ciudad, telefonoFijo,"
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
                    + idTipoSangre + "','"
                    + this.activoPersona + "','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "') ON DUPLICATE KEY UPDATE "
                    + "cedula='" + this.cedula + "',"
                    + "nombres='" + this.nombres + "',"
                    + "apellidos='" + this.apellidos + "',"
                    + "direccion='" + this.direccion + "',"
                    + "barrio='" + this.barrio + "',"
                    + "ciudad='" + this.idCiudad + "',"
                    + "telefonoFijo='" + this.telefonoFijo + "',"
                    + "telefonoCelular='" + this.telefonoCelular + "',"
                    + "escolaridad='" + this.escolaridad + "',"
                    + "genero='" + this.genero + "',"
                    + "cumpleanios='" + this.cumpleanios + "',"
                    + "lugarNacimiento='" + this.lugarNacimiento + "',"
                    + "estadoCivil='" + this.idEstadoCivil + "',"
                    + "email='" + this.email + "',"
                    + "tipoSangre='" + this.idTipoSangre + "',"
                    + "activo='" + this.activoPersona + "';";
            sql.add(texto);

            /*ingreso a tabla empleados*/
            texto = "INSERT INTO empleados (cedula, cargo, celularCorporativo, fechaDeIngreso, idAgencia,idRegional,idZona,"
                    + "idCentroDeCosto,formatoFotografia,idTipoDeContrato,numeroCuenta,entidadBancaria,"
                    + " activo, usuario) VALUES ('"
                    + this.cedula + "','"
                    + this.idCargo + "','"
                    + this.celularCorporativo + "','"
                    + this.fechaIngresoEmpresa + "','"
                    + this.idAgencia + "','"
                    + this.regional + "','"
                    + this.zona + "','"
                    + this.idCentroDeCosto + "','" //  aqui va la imagen
                    + this.formatoFotografia + "','"
                    + this.idTipoDeContrato + "','"
                    + this.numeroCuenta + "','"
                    + this.idBanco + "','"
                    + this.empleadoActivo + "','"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "') "
                    + " ON DUPLICATE KEY UPDATE "
                    + "cargo='" + idCargo + "',"
                    + "celularCorporativo='" + this.celularCorporativo + "',"
                    + "fechaDeIngreso='" + this.fechaIngresoEmpresa + "',"
                    + "idAgencia='" + this.idAgencia + "',"
                    + "idRegional='" + this.regional + "',"
                    + "idZona='" + this.zona + "',"
                    + "idCentroDeCosto='" + this.idCentroDeCosto + "',"//  aqui va la imagen
                    + "idTipoDeContrato='" + this.idTipoDeContrato + "',"
                    + "numeroCuenta='" + this.numeroCuenta + "',"
                    + "entidadBancaria='" + this.idBanco + "',"
                    + "activo='" + this.empleadoActivo + "',"
                    + "usuario='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "';";

            sql.add(texto);

        } catch (Exception ex) {
            Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }

    public boolean insertarFofografia() {
        boolean grab;

        try {
            Connection con = null;

            //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            con= ini.getConnRemota();
            
            if (fotografia != null) {
                String extension = fotografia.getAbsolutePath().substring(fotografia.getAbsolutePath().lastIndexOf("."));
                Statement sentencia = con.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                String sql2 = "select * from empleados  where cedula='" + this.cedula + "';";
                ResultSet rs = sentencia.executeQuery(sql2);
                con.setAutoCommit(false);

                PreparedStatement pst = con.prepareStatement("Update empleados set fotografia=?, formatoFotografia=? where cedula=?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

                while (rs.next()) {
                    //File file = new File(fotografia.getAbsolutePath());
                    FileInputStream fis = new FileInputStream(fotografia);
                    pst.setBinaryStream(1, fis, fotografia.length());
                    pst.setString(2, extension);
                    pst.setString(3, rs.getString(1));
                    pst.executeUpdate();
                    con.commit();
                    System.out.println("se guardó la imagen");
                }

                pst.close();
                rs.close();
              //  con.close();

            }

            grab = true;

           // con.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Error en insertar() archivo no encontrado " + ex.getMessage());
            grab = false;
        } catch (SQLException ex) {
            System.out.println("Error en insertar() consulta sql " + ex.getMessage() + "(sql=");
            grab = false;
        }
        return grab;
    }

    private boolean insertarFofografia(int caso) {
        boolean grab;

        try {
            Connection con = null;
            //conexion = CConexiones.GetConnection(getCadena(), getUsuarioBDLocal(), getClaveBDLocal());
            // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());

            String cadena = ini.getUrlRemota() + "://" + ini.getServerRemota() + ":" + ini.getPuertoRemota() + "/" + ini.getBdRemota();
            try (Connection conexion = CConexiones.GetConnection(cadena, ini.getUsuarioBDRemota(), ini.getClaveBDRemota())) {
                if (fotografia != null) {
                    String extension = fotografia.getAbsolutePath().substring(fotografia.getAbsolutePath().lastIndexOf("."));
                    Statement sentencia = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                    String sql2 = "select * from empleados  where cedula='" + this.cedula + "';";
                    ResultSet rs = sentencia.executeQuery(sql2);
                    conexion.setAutoCommit(false);
                    PreparedStatement pst = conexion.prepareStatement("Update empleados set fotografia=?, formatoFotografia=? where cedula=?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                    while (rs.next()) {
                        //File file = new File(fotografia.getAbsolutePath());
                        FileInputStream fis = new FileInputStream(fotografia);
                        pst.setBinaryStream(1, fis, fotografia.length());
                        pst.setString(2, extension);
                        pst.setString(3, rs.getString(1));
                        pst.executeUpdate();
                        conexion.commit();
                    }

                    pst.close();
                    rs.close();
                    conexion.close();

                }

                grab = true;

            }
        } catch (FileNotFoundException ex) {
            System.out.println("Error en insertar() archivo no encontrado " + ex.getMessage());
            grab = false;
        } catch (SQLException ex) {
            System.out.println("Error en insertar() consulta sql " + ex.getMessage() + "(sql=");
            grab = false;
        }
        return grab;
    }

    public static String arrListadoDeEmpleados() {
        String sql = "select * from vst_empleados "
                + "WHERE activo=1 ;";

        return sql;
    }

    public static String arrListadoDeEmpleados(String apellido) {
        String sql = "select * from vst_empleados "
                + "WHERE "
                + "apellidos like '%" + apellido + "%' and empleados.activo=1;";

        return sql;
    }

    public static String arrListadoDeEmpleados(String nombresApellido, boolean i) {
        String sql = "select * from vst_empleados "
                + "WHERE "
                + "apellidos like '%" + nombresApellido + "%'";

        return sql;
    }

    public synchronized void liberarEmpleado(boolean liberar) {
        String sql;
        if (liberar) {
            sql = "update empleados set isFree= 1 where cedula='" + this.cedula + "';";
        } else {
            sql = "update empleados set isFree= 0 where cedula='" + this.cedula + "';";
        }
        ini.insertarDatosRemotamente(sql);
    }

    public List<CFacturasPorManifiesto> getListaDeFacturasSinDescargar() {
        return listaDeFacturasPorConductor;
    }

    public void setListaDeFacturasSinDescargar(List<CFacturasPorManifiesto> listaDeFacturasSinDescargar) {
        this.listaDeFacturasPorConductor = listaDeFacturasSinDescargar;
    }

    public List<CFacturasPorManifiesto> getListaDeFacturaPorConductor() {
        return listaDeFacturasPorConductor;
    }

    public void setListaDeFacturaPorConductor(String fecha) {
        ResultSet rst = null;
        Statement st = null;
        Connection con;
        String fechaInicial = null;
        String fechaFinal = null;
        String sql = null;

        listaDeFacturasPorConductor = new ArrayList<>();

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CEmpleados.setListaFacturasPorConductor");

        con = ini.getConnRemota();
        sql = "select * "
                + "FROM vst_defintivofacturaspormanifiesto "
                //+ "WHERE  estadoManifiesto<='3' and "
                + "WHERE  "
                + "conductor='" + this.cedula + "' and "
                // + "estadoManifiesto<='3' and "
                + "fechaDistribucion='" + fecha + "' "
                //  + "fechaReal>='" + fechaInicial + "' and fechaReal <='" + fechaFinal + "' "
                + "order by  nombreConductor,fechaIng asc; ";

        if (ini.getPropiedades().getProperty("idOperador").equals("2")) {

            int dia = LocalDate.now().getDayOfMonth();

            if (LocalTime.now().isAfter(LocalTime.of(0, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(5, 0, 0))) {
                dia -= 1;
                fechaInicial = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + dia + " 05:00:00";
                fechaFinal = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth() + " 04:59:59";

            } else {
                dia += 1;
                fechaInicial = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth() + " 05:00:00";
                fechaFinal = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + dia + " 04:59:59";

            }

            sql = "select * "
                    + "FROM vst_defintivofacturaspormanifiesto "
                    //+ "WHERE  estadoManifiesto<='3' and "
                    + "WHERE  "
                    // + "estadoManifiesto <= '3' and "
                    + "conductor='" + this.cedula + "' and "
                    + "fechaDistribucion ='" + fecha + "' "
                    //  + "fechaReal>='" + fechaInicial + "' and fechaReal <='" + fechaFinal + "' "
                    + "order by  nombreConductor,fechaIng asc; ";
        }

        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CFacturasPorManifiesto fxm = new CFacturasPorManifiesto(ini);
                    fxm.setConsecutivo(rst.getInt("consecutivo"));
                    fxm.setAdherencia(rst.getInt("adherencia"));
                    fxm.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    fxm.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    fxm.setVehiculo(rst.getString("vehiculo"));
                    fxm.setConductor(rst.getString("conductor"));
                    fxm.setNombreConductor(rst.getString("nombreConductor"));
                    fxm.setDespachador(rst.getString("despachador"));
                    fxm.setNombreDespachador(rst.getString("nombreDespachador"));
                    fxm.setIdRuta(rst.getInt("idRuta"));
                    fxm.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    fxm.setNumeroFactura(rst.getString("numeroFactura"));
                    fxm.setValorARecaudarFactura(rst.getDouble("valorARecaudarFactura"));
                    fxm.setFechaIng(rst.getString("fechaIng"));
                    fxm.setFechaDeVenta(rst.getDate("fechaDeVenta"));
                    fxm.setCliente(rst.getString("cliente"));
                    fxm.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    fxm.setDireccionDeCliente(rst.getString("direccionDeCliente"));
                    fxm.setVendedor(rst.getString("vendedor"));
                    fxm.setFormaDePago(rst.getString("formaDePago"));
                    fxm.setIdCanal(rst.getInt("idCanal"));
                    fxm.setNombreCanal(rst.getString("nombreCanal"));
                    fxm.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    fxm.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    fxm.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    fxm.setValorRechazo(rst.getDouble("valorRechazo"));
                    fxm.setValorDescuento(rst.getDouble("valorDescuento"));
                    fxm.setValorRecaudado(rst.getDouble("valorTotalRecaudado"));
                    fxm.setSalidasDistribucion(rst.getInt("salidasDistribucion"));
                    fxm.setTrasmitido(rst.getInt("trasmitido"));
                    fxm.setNumeroDescuento(rst.getString("numeroDescuento"));
                    fxm.setNumeroRecogida(rst.getString("numeroRecogida"));
                    fxm.setPesoFactura(rst.getDouble("pesoFactura"));
                    fxm.setAdherenciaDescargue(0);
                    fxm.setIdTipoDeMovimiento(1);
                    fxm.setNombreIdmovimiento("NINGUNO");
                    fxm.setCausalDeRechazo(1);
                    fxm.setNombreCausalDeDevolucion("NINGUNO");
                    fxm.setLatitud(rst.getString("latitud"));
                    fxm.setLongitud(rst.getString("longitud"));

                    // fxm.setListaProductosPorFactura(false);
                    listaDeFacturasPorConductor.add(fxm);
                }
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public List<CFacturasPorManifiesto> getListaDeFacturasPorConductorSinDespachar() {
        return listaDeFacturasPorConductorSinDespachar;
    }

    public void setListaDeFacturasPorConductorSinDespachar(List<CFacturasPorManifiesto> listaDeFacturasPorConductorSinDespachar) {
        this.listaDeFacturasPorConductorSinDespachar = listaDeFacturasPorConductorSinDespachar;
    }

    public void setListaDeFacturasPorConductorSinDespachar() {
        ResultSet rst = null;
        Statement st = null;
        Connection con;
        String sql = null;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CEmpleados.setListaFacturasPorConductor");

        con = ini.getConnRemota();
        
        if (ini.getPropiedades().getProperty("idOperador").equals("2")) {
            sql = "select * "
                    + "FROM vst_defintivofacturaspormanifiesto "
                    + "WHERE  "
                    + "conductor='" + this.cedula + "' and  ";

            if (LocalTime.now().isAfter(LocalTime.of(0, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(5, 0, 0))) {

                sql += "fechaDistribucion=(select DATE_SUB(curdate(),INTERVAL 1 day)) and ";

            } else {
                sql += "fechaDistribucion = current_date and ";
            }
            sql += "despachado='0' "
                    + "order by  nombreConductor,fechaIng asc; ";
        } else {
            sql = "select * "
                    + "FROM vst_defintivofacturaspormanifiesto "
                    + "WHERE  "
                    + "conductor='" + this.cedula + "' and  "
                    + "fechaDistribucion = current_date and "
                    + "despachado='0' "
                    + "order by  nombreConductor,fechaIng asc; ";
        }

        if (con != null) {
            listaDeFacturasPorConductorSinDespachar = new ArrayList<>();
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CFacturasPorManifiesto fxm = new CFacturasPorManifiesto(ini);
                    fxm.setConsecutivo(rst.getInt("consecutivo"));
                    fxm.setAdherencia(rst.getInt("adherencia"));
                    fxm.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    fxm.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    fxm.setVehiculo(rst.getString("vehiculo"));
                    fxm.setConductor(rst.getString("conductor"));
                    fxm.setNombreConductor(rst.getString("nombreConductor"));
                    fxm.setDespachador(rst.getString("despachador"));
                    fxm.setNombreDespachador(rst.getString("nombreDespachador"));
                    fxm.setIdRuta(rst.getInt("idRuta"));
                    fxm.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    fxm.setNumeroFactura(rst.getString("numeroFactura"));
                    fxm.setValorARecaudarFactura(rst.getDouble("valorARecaudarFactura"));
                    fxm.setFechaIng(rst.getString("fechaIng"));
                    fxm.setFechaDeVenta(rst.getDate("fechaDeVenta"));
                    fxm.setCliente(rst.getString("cliente"));
                    fxm.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    fxm.setDireccionDeCliente(rst.getString("direccionDeCliente"));
                    fxm.setVendedor(rst.getString("vendedor"));
                    fxm.setFormaDePago(rst.getString("formaDePago"));
                    fxm.setIdCanal(rst.getInt("idCanal"));
                    fxm.setNombreCanal(rst.getString("nombreCanal"));
                    fxm.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    fxm.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    fxm.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    fxm.setValorRechazo(rst.getDouble("valorRechazo"));
                    fxm.setValorDescuento(rst.getDouble("valorDescuento"));
                    fxm.setValorRecaudado(rst.getDouble("valorTotalRecaudado"));
                    fxm.setSalidasDistribucion(rst.getInt("salidasDistribucion"));
                    fxm.setTrasmitido(rst.getInt("trasmitido"));
                    fxm.setNumeroDescuento(rst.getString("numeroDescuento"));
                    fxm.setNumeroRecogida(rst.getString("numeroRecogida"));
                    fxm.setPesoFactura(rst.getDouble("pesoFactura"));
                    fxm.setAdherenciaDescargue(0);
                    fxm.setIdTipoDeMovimiento(1);
                    fxm.setNombreIdmovimiento("NINGUNO");
                    fxm.setCausalDeRechazo(1);
                    fxm.setNombreCausalDeDevolucion("NINGUNO");
                    fxm.setLatitud(rst.getString("latitud"));
                    fxm.setLongitud(rst.getString("longitud"));
                    fxm.setTelefonoVendedor(rst.getString("telefonoVendedor"));
                    fxm.setPlazoDias(rst.getInt("plazoDias"));
                    fxm.setDespachado(rst.getInt("despachado"));
                    fxm.setFpContado(rst.getDouble("fpContado"));

                    listaDeFacturasPorConductorSinDespachar.add(fxm);
                }

                rst.close();
                st.close();
               // con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void setListaDeFacturasPorConductorSinDespachar(int x) {
        ResultSet rst = null;
        Statement st = null;
        Connection con;
        String sql = null;

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CEmpleados.setListaFacturasPorConductor");

        con = ini.getConnRemota();
        
        if (ini.getPropiedades().getProperty("idOperador").equals("2")) {
            sql = "select * "
                    + "FROM vst_defintivofacturaspormanifiesto "
                    + "WHERE  "
                    + "conductor='" + this.cedula + "' and  ";

            if (LocalTime.now().isAfter(LocalTime.of(0, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(5, 0, 0))) {

                sql += "fechaDistribucion=(select DATE_SUB(curdate(),INTERVAL 1 day)) and ";

            } else {
                sql += "fechaDistribucion = current_date and ";
            }
            sql += "despachado='0' "
                    + "order by  nombreConductor,fechaIng asc; ";
        } else {
            sql = "select * "
                    + "FROM vst_defintivofacturaspormanifiesto "
                    + "WHERE  "
                    + "conductor='" + this.cedula + "' and  "
                    + "fechaDistribucion = current_date and "
                    + "despachado='0' "
                    + "order by  nombreConductor,fechaIng asc; ";
        }

        if (con != null) {
            listaDeFacturasPorConductorSinDespachar = new ArrayList<>();
            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CFacturasPorManifiesto fxm = new CFacturasPorManifiesto(ini);
                    fxm.setConsecutivo(rst.getInt("consecutivo"));
                    fxm.setAdherencia(rst.getInt("adherencia"));
                    fxm.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    fxm.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    fxm.setVehiculo(rst.getString("vehiculo"));
                    fxm.setConductor(rst.getString("conductor"));
                    fxm.setNombreConductor(rst.getString("nombreConductor"));
                    fxm.setDespachador(rst.getString("despachador"));
                    fxm.setNombreDespachador(rst.getString("nombreDespachador"));
                    fxm.setIdRuta(rst.getInt("idRuta"));
                    fxm.setNombreDeRuta(rst.getString("nombreDeRuta"));
                    fxm.setNumeroFactura(rst.getString("numeroFactura"));
                    fxm.setValorARecaudarFactura(rst.getDouble("valorARecaudarFactura"));
                    fxm.setFechaIng(rst.getString("fechaIng"));
                    fxm.setFechaDeVenta(rst.getDate("fechaDeVenta"));
                    fxm.setCliente(rst.getString("cliente"));
                    fxm.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    fxm.setDireccionDeCliente(rst.getString("direccionDeCliente"));
                    fxm.setVendedor(rst.getString("vendedor"));
                    fxm.setFormaDePago(rst.getString("formaDePago"));
                    fxm.setIdCanal(rst.getInt("idCanal"));
                    fxm.setNombreCanal(rst.getString("nombreCanal"));
                    fxm.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    fxm.setValorIvaFactura(rst.getDouble("valorIvaFactura"));
                    fxm.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    fxm.setValorRechazo(rst.getDouble("valorRechazo"));
                    fxm.setValorDescuento(rst.getDouble("valorDescuento"));
                    fxm.setValorRecaudado(rst.getDouble("valorTotalRecaudado"));
                    fxm.setSalidasDistribucion(rst.getInt("salidasDistribucion"));
                    fxm.setTrasmitido(rst.getInt("trasmitido"));
                    fxm.setNumeroDescuento(rst.getString("numeroDescuento"));
                    fxm.setNumeroRecogida(rst.getString("numeroRecogida"));
                    fxm.setPesoFactura(rst.getDouble("pesoFactura"));
                    fxm.setAdherenciaDescargue(0);
                    fxm.setIdTipoDeMovimiento(1);
                    fxm.setNombreIdmovimiento("NINGUNO");
                    fxm.setCausalDeRechazo(1);
                    fxm.setNombreCausalDeDevolucion("NINGUNO");
                    fxm.setLatitud(rst.getString("latitud"));
                    fxm.setLongitud(rst.getString("longitud"));
                    fxm.setTelefonoVendedor(rst.getString("telefonoVendedor"));
                    fxm.setPlazoDias(rst.getInt("plazoDias"));
                    fxm.setDespachado(rst.getInt("despachado"));
                    fxm.setFpContado(rst.getDouble("fpContado"));

                    listaDeFacturasPorConductorSinDespachar.add(fxm);
                }

                rst.close();
                st.close();
               // con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public List<CFacturasPorManifiesto> getListaFacturasDescargadas() {
        return listaFacturasDescargadas;
    }

    public void setListaFacturasDescargadas(List<CFacturasPorManifiesto> listaFacturasDescargadas) {
        this.listaFacturasDescargadas = listaFacturasDescargadas;
    }

    public void setListaFacturasDescargadas(String numeroManifiesto, boolean manifiestoCerrado) {
        ResultSet rst = null;
        Statement st = null;
        Connection con;
        String fechaInicial = null;
        String fechaFinal = null;
        String sql = null;
        int estadoMannifiesto = 0;
        if (manifiestoCerrado) {
            estadoMannifiesto = 4;
        } else {
            estadoMannifiesto = 3;
        }

        listaFacturasDescargadas = new ArrayList<>();

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setListaFacturasPorManifiesto");

        con = ini.getConnRemota();
        
        sql = "select * "
                + "FROM  vst_movilizacionfacturasdescargadas "
                + "WHERE  estadoManifiesto='" + estadoMannifiesto + "' and "
                // + "WHERE "
                + "numeroManifiesto='" + numeroManifiesto + "' and "
                + "conductor='" + this.cedula + "' "
                // + "fechaReal>='" + fechaInicial + "' and fechaReal <='" + fechaFinal + "' "
                + "order by  conductor, fechaIng asc; ";

        if (ini.getPropiedades().getProperty("idOperador").equals("2")) {

            int dia = LocalDate.now().getDayOfMonth();

            if (LocalTime.now().isAfter(LocalTime.of(0, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(5, 0, 0))) {
                dia -= 1;
                fechaInicial = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + dia + " 05:00:00";
                fechaFinal = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth() + " 04:59:59";

            } else {
                dia += 1;
                fechaInicial = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth() + " 05:00:00";
                fechaFinal = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + dia + " 04:59:59";

            }

            sql = "select * "
                    + "FROM  vst_movilizacionfacturasdescargadas "
                    + "WHERE  estadoManifiesto='" + estadoMannifiesto + "' and "
                    // + "WHERE "
                    + "numeroManifiesto='" + numeroManifiesto + "' and "
                    + "conductor='" + this.cedula + "' "
                    // + "fechaReal>='" + fechaInicial + "' and fechaReal <='" + fechaFinal + "' "
                    + "order by  conductor, fechaIng asc; ";
        }

        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CFacturasPorManifiesto facdes = new CFacturasPorManifiesto(ini);

                    facdes.setConsecutivo(rst.getInt("consecutivo"));
                    facdes.setIdCanal(rst.getInt("idCanalDistribucion"));//
                    facdes.setNombreCanal(rst.getString("nombreCanalDistribucion"));
                    facdes.setNumeroManifiesto(rst.getString("numeroManifiesto"));
                    facdes.setAdherencia(rst.getInt("adherencia"));
                    facdes.setNumeroFactura(rst.getString("numeroFactura"));
                    facdes.setFechaDistribucion(rst.getString("fechaDistribucion"));
                    facdes.setVehiculo(rst.getString("vehiculo"));
                    facdes.setConductor(rst.getString("conductor"));
                    facdes.setNombreConductor(rst.getString("nombreConductor"));
                    facdes.setNombreDeCliente(rst.getString("nombreDeCliente"));
                    facdes.setDireccionDeCliente(rst.getString("direccion"));
                    facdes.setValorFacturaSinIva(rst.getDouble("valorFacturaSinIva"));
                    facdes.setValorTotalFactura(rst.getDouble("valorTotalFactura"));
                    facdes.setValorRechazo(rst.getDouble("valorRechazo"));
                    facdes.setValorDescuento(rst.getDouble("valorDescuento"));
                    facdes.setValorRecaudado(rst.getDouble("valorRecaudado"));
                    facdes.setIdTipoDeMovimiento(rst.getInt("idTipoMovimiento"));
                    facdes.setNombreIdmovimiento(rst.getString("nombreTipoDeMovimiento"));
                    facdes.setCausalDeRechazo(rst.getInt("idMotivoRechazo"));
                    facdes.setNombreCausalDeDevolucion(rst.getString("nombreCausalDeRechazo"));
                    facdes.setVendedor(rst.getString("vendedor"));
                    facdes.setUsuario(rst.getString("usuario"));
                    facdes.setFechaIng(rst.getString("fechaIng"));
                    facdes.setNombreDeRuta(rst.getString("nombreDeRuta"));

                    this.listaFacturasDescargadas.add(facdes);

                }
                rst.close();
                st.close();
               // con.close();
            } catch (SQLException ex) {
                //System.out.println("error en sql\n" + sql);
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                //System.out.println("error en sql2\n" +sql);
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    public List<CFacturasPorManifiesto> getListaFacturasPendientesPorDescargar() {
        return listaFacturasPendientesPorDescargar;
    }

    public void setListaFacturasPendientesPorDescargar(List<CFacturasPorManifiesto> listaFacturasPendientesPorDescargar) {
        this.listaFacturasPendientesPorDescargar = listaFacturasPendientesPorDescargar;
    }

    public List<CFacturasPorManifiesto> setListaFacturasPendientesPorDescargar() throws InterruptedException {
        listaFacturasPendientesPorDescargar = new ArrayList<>();
        CFacturasPorManifiesto factura;

        for (CFacturasPorManifiesto fac : listaDeFacturasPorConductor) {
            boolean existe = false;

            if (listaFacturasDescargadas != null) {
                for (CFacturasPorManifiesto facDes : listaFacturasDescargadas) {
                    if (facDes.getNumeroFactura().equals(fac.getNumeroFactura())) {
                        existe = true;
                        break;
                    }
                }
                if (!existe) {
                    listaFacturasPendientesPorDescargar.add(fac);

                }
            }
            Thread.sleep(10);
        }

        return listaFacturasPendientesPorDescargar;
    }

    /**
     * Método que permite actualizar los registros en la base de datos
     *
     * @return true sí el registro fue grabado, retorna false sí hubo errror al
     * grabar el registro en la BBDD
     * @throws java.lang.InterruptedException
     */
    public String cerrarManifiestosDelConductor() throws InterruptedException {
        boolean grabado = false;
        String mensaje = null;
        String manifiestos = null;
        if (listaFacturasPendientesPorDescargar.isEmpty()) {
            manifiestos = "('";
            for (CManifiestosDeDistribucion man : listaDeManifiestosSinDescargarPorConductor) {
                manifiestos += man.getNumeroManifiesto() + "','";
                Thread.sleep(10);
            }
            manifiestos = manifiestos.substring(0, manifiestos.length() - 2).concat(")");
            try {

                String sql = "UPDATE  manifiestosdedistribucion SET "
                        + " estadoManifiesto='4'"
                        + "WHERE  "
                        + "consecutivo in = " + manifiestos + ";";

                grabado = ini.insertarDatosRemotamente(sql, "CEmpleados.cerrarManifiestosDelConductor");

                if (grabado) {
                    mensaje = "1";
                } else {
                    mensaje = "0";
                }

            } catch (Exception ex) {
                Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            mensaje = "-1";
        }
        return mensaje;
    }

    public List<CManifiestosDeDistribucion> getListaDeManifiestosPorConductor() {
        return listaDeManifiestosPorConductor;
    }

    public void setListaDeManifiestosPorConductor(List<CManifiestosDeDistribucion> listaDeManifiestosDeDistribucions) {
        this.listaDeManifiestosPorConductor = listaDeManifiestosDeDistribucions;
    }

    public void setListaDeManifiestosPorConductor(String fecha) {
        ResultSet rst = null;
        Statement st = null;
        Connection con;
        String fechaInicial = null;
        String fechaFinal = null;
        String sql = null;

        listaDeManifiestosPorConductor = new ArrayList<>();

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setListaFacturasPorManifiesto");

        con = ini.getConnRemota();
        
        if (ini.getPropiedades().getProperty("idOperador").equals("2")) {

            int dia = LocalDate.now().getDayOfMonth();

            if (LocalTime.now().isAfter(LocalTime.of(0, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(5, 0, 0))) {
                dia -= 1;
                fechaInicial = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + dia + " 05:00:00";
                fechaFinal = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth() + " 04:59:59";

            } else {
                dia += 1;
                fechaInicial = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth() + " 05:00:00";
                fechaFinal = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + dia + " 04:59:59";

            }

            sql = "select numeroManifiesto "
                    + "FROM vst_manifiestodedistribucion "
                    + "WHERE   "
                    + "conductor='" + this.cedula + "' and "
                    + "fechaDistribucion='" + fecha + "' and "
                    + "estadoManifiesto='3' "
                    //  + "and fechaReal>='" + fechaInicial + "' and fechaReal <='" + fechaFinal + "' "
                    + "order by  nombreConductor asc; ";

        }

        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CManifiestosDeDistribucion man = new CManifiestosDeDistribucion(ini, rst.getInt("numeroManifiesto"));

                    listaDeManifiestosPorConductor.add(man);
                    Thread.sleep(10);
                }
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setListaDeManifiestosDescargadosPorConductor(String fecha) {
        ResultSet rst = null;
        Statement st = null;
        Connection con;
        String fechaInicial = null;
        String fechaFinal = null;
        String sql = null;

        listaDeManifiestosDescargadosPorConductor = new ArrayList<>();

        // con = CConexiones.GetConnection(ini.getCadenaLocal(), ini.getUsuarioBDLocal(), ini.getClaveBDLocal());
       // con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CManifiestosDeDistribucion.setListaFacturasPorManifiesto");

        con = ini.getConnRemota();
        
        if (ini.getPropiedades().getProperty("idOperador").equals("2")) {

            int dia = LocalDate.now().getDayOfMonth();

            if (LocalTime.now().isAfter(LocalTime.of(0, 0, 0)) && LocalTime.now().isBefore(LocalTime.of(5, 0, 0))) {
                dia -= 1;
                fechaInicial = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + dia + " 05:00:00";
                fechaFinal = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth() + " 04:59:59";

            } else {
                dia += 1;
                fechaInicial = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + LocalDate.now().getDayOfMonth() + " 05:00:00";
                fechaFinal = LocalDate.now().getYear() + "-" + LocalDate.now().getMonthValue() + "-" + dia + " 04:59:59";

            }

            sql = "select numeroManifiesto "
                    + "FROM vst_manifiestodedistribucion "
                    + "WHERE  estadoManifiesto<='4' and "
                    + "conductor='" + this.cedula + "' and "
                    + "fechaDistribucion='" + fecha + "' "
                    //  + "and fechaReal>='" + fechaInicial + "' and fechaReal <='" + fechaFinal + "' "
                    + "order by  nombreConductor asc; ";

        }

        if (con != null) {

            try {
                st = con.createStatement();
                rst = st.executeQuery(sql);
                while (rst.next()) {
                    CManifiestosDeDistribucion man = new CManifiestosDeDistribucion(ini, rst.getInt("numeroManifiesto"));

                    // fxm.setListaProductosPorFactura(false);
                    listaDeManifiestosDescargadosPorConductor.add(man);
                }
                rst.close();
                st.close();
                //con.close();
            } catch (SQLException ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Método que permite actualizar los registros en la base de datos
     *
     * @return true sí el registro fue grabado, retorna false sí hubo errror al
     * grabar el registro en la BBDD
     * @throws java.lang.InterruptedException
     */
    public boolean cerrarManifiestoDeDistribucions() throws InterruptedException {
        boolean grabado = false;

        String listaManifiestos = "('";
        for (CManifiestosDeDistribucion man : listaDeManifiestosPorConductor) {
            listaManifiestos += man.getNumeroManifiesto() + "','";
            Thread.sleep(10);
        }
        listaManifiestos = listaManifiestos.substring(0, listaManifiestos.length() - 2).concat(")");
        try {

            String sql = "UPDATE  manifiestosdedistribucion SET "
                    + "estadoManifiesto='4'"
                    + "WHERE  "
                    + "consecutivo in" + listaManifiestos + ";";

            grabado = ini.insertarDatosRemotamente(sql, "CEmpleados.cerrarManifiestoDeDistribucions");

            if (grabado) {
                for (CManifiestosDeDistribucion man : listaDeManifiestosPorConductor) {
                    man.setEstadoManifiesto(4);
                    Thread.sleep(10);
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(CEmpleados.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    /**
     * Método que permite guardar las facturas de los manifiestos en la BBDD
     *
     * @param isContado
     * @return devuelve true si el registro es grabado, retorna false si hubo
     * error al grabar el registro.
     */
    public boolean grabarFacturasDescargadas100(boolean isContado) {
        boolean grabado = false;
        String sql = null;
        String sqlLocal = null;
        String listaFacturas;
        Connection con;//= null;
        Statement st = null;//= null;

        //con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota(), "CEmpleados.grabarFacturasDescargadas100");
        
        con = ini.getConnRemota();
        
        try {
            if (con != null) {

                con.setAutoCommit(false);

                st = con.createStatement();
                listaFacturas = "('";

                for (CFacturasPorManifiesto fact : listaFacturasPendientesPorDescargar) {

                    sql = "INSERT INTO facturasdescargadas(consecutivo,numeroManifiesto,adherencia,numeroFactura,"
                            + "valorRechazo,valorDescuento,valorRecaudado,movimientoFactura,motivoRechazo,activo,fechaIng,usuario,flag)VALUES('"
                            + fact.getConsecutivo() + "','"
                            + fact.getNumeroManifiesto() + "','"
                            + fact.getAdherencia() + "','"
                            + fact.getNumeroFactura() + "','"
                            + "0','"
                            + "0','";
                    if (fact.getPlazoDias() > 1) {
                        sql += "0','";
                        fact.setTipoDeDEscargue("E. T. Cr");

                    } else {
                        sql += fact.getValorTotalFactura() + "','";
                        fact.setTipoDeDEscargue("E. T. Cn");
                    }
                    sql += "2','"
                            + "1','"
                            + "1',"
                            + "CURRENT_TIMESTAMP(),'"
                            + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','"
                            + "1') on duplicate key update flag='-1';";

                    st.addBatch(sql);
                    listaFacturas += fact.getNumeroFactura() + "','";
                    Thread.sleep(10);
                }
                st.executeBatch();
                grabado = true;
                con.commit();
                st.close();
               // con.close();
                listaFacturas = listaFacturas.substring(0, listaFacturas.length() - 2).concat(");");

                /*Actualiza las facturas localmente para no ser utilizadas posteriormente en otro manifiesot*/
                sql = "update facturas set estadoFactura='2' where numeroFactura in" + listaFacturas;
                grabado = ini.insertarDatosLocalmente(sql);

                new Thread(new HiloListadoDeManifiestosSinDescargar(ini)).start();
                //this.setListaFacturasDescargadas();

            }

        } catch (SQLException ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CManifiestosDeDistribucion.class.getName()).log(Level.SEVERE, null, ex);
        }
        return grabado;
    }

    public void setValorTotalFacturas(boolean algo) {

    }

    public Double getValorTotalFacturas(boolean algo) {
        double valor = 0.0;
        for (CFacturasPorManifiesto fxm : listaDeFacturasPorConductor) {
            valor += fxm.getValorTotalFactura();
        }

        return valor;
    }

//    public void sacarMinutaPorConductor(int seleccion) {
//        String listadeproductos = null;
//
//        MinutasDeDistribucion sacarMinuta = new MinutasDeDistribucion(ini);
//        String lista = "'";
//
//        /* manifiesto creado pero facturas no han sido grabadas */
//        for (CFacturasPorManifiesto facs : this.getListaDeFacturaPorConductor()) {
//            lista += facs.getNumeroFactura() + "','";
//
//        }
//
//        lista = lista.substring(0, lista.length() - 2);
//        if (seleccion == 0) {
//            sacarMinuta.setMinutaPorListaDeFacturas(lista);
//        }
//        if (seleccion == 1) {
//            ini.setListaDeProductosParaMinuta();
//            listadeproductos = ini.getListaDeProductosParaMinuta();
//            sacarMinuta.minutaPorListaDeFacturas(lista, listadeproductos);
//
//        }
//
//    }
    public boolean despachoDeProductosPorFacturas(String listadeFacturas) throws Exception {

        List<String> sentenciasSql = new ArrayList<>();

        boolean grabado = false;
        String[] strcad = listadeFacturas.split("'");
        int i = 0;
        for (CFacturasPorManifiesto factura : this.getListaDeFacturasPorConductorSinDespachar()) {
            String sql;
            if (i == 0) {
                sql = "update facturaspormanifiesto set "
                        + "despachado = '1', "
                        + "fechaDespachado= CURRENT_TIMESTAMP,"
                        + "usuariodespachador='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "'"
                        + "where "
                        + "numeroManifiesto='" + factura.getNumeroManifiesto() + "' and "
                        + "numeroFactura in (" + listadeFacturas + ");";
                sentenciasSql.add(sql);
                i++;

            }
            sql = "INSERT INTO bitacorasalidasfacturas "
                    + "(numeroManifiesto,numeroFactura,verificado,"
                    + "activo,fechaIng,usuario,flag)"
                    + "VALUES ('"
                    + factura.getNumeroManifiesto() + "','" //"<{numeroManifiesto: }>,"
                    + factura.getNumeroFactura() + "','" //<{numeroFactura: }>,"
                    + "1','" //<{verificado: 1}>,"
                    + "1'," //<{activo: 1}>,"
                    + "CURRENT_TIMESTAMP,'" // "<{fechaIng: CURRENT_TIMESTAMP}>,"
                    + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','" // "<{usuario: }>,"
                    + "1') on duplicate key update flag='-1'"; //"<{flag: 1}>);";
            sentenciasSql.add(sql);

        }
        grabado = ini.insertarDatosRemotamente(sentenciasSql, "Cempleados");

        return grabado;
    }

    public boolean despachoDeProductosPorFacturas2(String listadeFacturas) throws Exception {

        List<String> sentenciasSql = new ArrayList<>();

        boolean grabado = false;
        // String[] strcad = listadeFacturas.split("'");
        int i = 0;
        for (CFacturasPorManifiesto factura : this.getListaDeFacturasPorConductorSinDespachar()) {
            String sql;
            CFacturas fac = new CFacturas(ini, factura.getNumeroFactura());
            if (fac.getEstadoFactura() > 1) {
                String mensaje = "La factura # " + fac.getNumeroDeFactura() + " ya se encuentra descargada del sistema,"
                        + "movimiento : " + fac.getNombreEstadoFactura() + ", "
                        + "LA VERIFICACION DE LA SALIDA DEL PRODUCTO SE HACE EN FORMA INCORRECTA. ";

                new Thread(new HiloGuardarAlertasSalidaDeProducto(ini,
                        Integer.parseInt(factura.getNumeroManifiesto()),
                        fac.getNumeroDeFactura(),
                        mensaje)).start();

            }

            if (fac.getEstadoFactura() == 1) {
                sql = "update facturaspormanifiesto set "
                        + "despachado = '1', "
                        + "fechaDespachado= CURRENT_TIMESTAMP,"
                        + "usuariodespachador='" + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "' "
                        + "where "
                        + "numeroManifiesto = '" + factura.getNumeroManifiesto() + "' and "
                        + "numeroFactura = '" + factura.getNumeroFactura() + "';";
                sentenciasSql.add(sql);
           
                sql = "INSERT INTO bitacorasalidasfacturas "
                        + "(numeroManifiesto,numeroFactura,verificado,"
                        + "activo,fechaIng,usuario,flag)"
                        + "VALUES ('"
                        + factura.getNumeroManifiesto() + "','" //"<{numeroManifiesto: }>,"
                        + factura.getNumeroFactura() + "','" //<{numeroFactura: }>,"
                        + "1','" //<{verificado: 1}>,"
                        + "1'," //<{activo: 1}>,"
                        + "CURRENT_TIMESTAMP,'" // "<{fechaIng: CURRENT_TIMESTAMP}>,"
                        + Inicio.deCifrar(this.ini.getUser().getNombreUsuario()) + "','" // "<{usuario: }>,"
                        + "1') on duplicate key update flag='-1'"; //"<{flag: 1}>);";
                sentenciasSql.add(sql);
            }

        }
        if (sentenciasSql.size() > 0) {
            grabado = ini.insertarDatosRemotamente(sentenciasSql, "Cempleados");

        } else {
            JOptionPane.showMessageDialog(null,
                    "Las Facturas relcionadas ya presentan movimiento, no se pueden descargar ",
                    "No se puede Descargar",
                    JOptionPane.WARNING_MESSAGE);
            return true;

        }
        return grabado;
    }

}
