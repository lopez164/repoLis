/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import aplicacionlogistica.distribucion.objetos.personas.CEmpleados;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author VLI_488
 */
public class CDocumentosPorEmpleado {

    CEmpleados empleado = null;
    int cargo = 0;
    CDocumentos documento = null;
    String numeroDocumento = null;
    String entidadEmisora = null;
    Date fechaExpedicion;
    String lugarExpedicion;
    boolean tieneVencimiento;
    Date fechaVencimiento;
    File archivoSoporteDocumento = null;
    String formatoArchivoSoporte;
    ArrayList<CDocumentos> arrDocumentosPorEmpleado = null;
   
    Inicio ini;

    public String getLugarExpedicion() {
        return lugarExpedicion;
    }

    public void setLugarExpedicion(String lugarExpedicion) {
        this.lugarExpedicion = lugarExpedicion;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public CEmpleados getEmpleado() {
        return empleado;
    }

    public String getFormatoArchivo() {
        return formatoArchivoSoporte;
    }

    public void setFormatoArchivo(String formatoArchivo) {
        this.formatoArchivoSoporte = formatoArchivo;
    }

    public int getCargo() {
        return cargo;
    }

    public void setCargo(int cargo) {
        this.cargo = cargo;
    }

    public CDocumentos getDocumento() {
        return documento;
    }

    public void setDocumento(CDocumentos documento) {
        this.documento = documento;
    }

    public String getEntidadEmisora() {
        return entidadEmisora;
    }

    public void setEntidadEmisora(String entidadEmisora) {
        this.entidadEmisora = entidadEmisora;
    }

    public Date getFechaExpedicion() {
        return fechaExpedicion;
    }

    public void setFechaExpedicion(Date fechaExpedicion) {
        this.fechaExpedicion = fechaExpedicion;
    }

    public boolean isTieneVencimiento() {
        return tieneVencimiento;
    }

    public void setTieneVencimiento(boolean tieneVencimiento) {
        this.tieneVencimiento = tieneVencimiento;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public File getArchivoSoporteDocumento() {
        return archivoSoporteDocumento;
    }

    public void setArchivoSoporteDocumento(File soporteDocumento) {
        this.archivoSoporteDocumento = soporteDocumento;
    }

    public void setEmpleado(CEmpleados empleado) {
        this.empleado = empleado;
    }

    public CDocumentosPorEmpleado(Inicio ini) {
        this.ini = ini;
    }

    public CDocumentosPorEmpleado(Inicio ini, CEmpleados empleado) throws Exception {
        this.ini = ini;
        this.empleado = empleado;
        InputStream fisSalida = null;
        FileOutputStream fosArchivoSalida;
        byte[] arreglo;
        int byteLeidos = 0;
        String sql;
        Statement st;
        ResultSet rst;
        Connection con;

        try {
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();

                sql = "select * from documentosPorEmpleado "
                        + "where "
                        + "documentosPorEmpleado.cedula=" + empleado.getCedula() + " and "
                        + "documentosPorEmpleado.idCargo=" + empleado.getCargo() + " and "
                        + "documentosPorEmpleado.activo=1";

                rst = st.executeQuery(sql);

                if (rst.next()) {
                    this.empleado = empleado;
                    this.cargo = empleado.getIdCargo();
                    this.documento = new CDocumentos(ini, rst.getInt(2));
                    this.numeroDocumento = rst.getString(5);
                    this.entidadEmisora = rst.getString(6);
                    this.fechaExpedicion = rst.getDate(7);
                    this.lugarExpedicion = rst.getString(8);
                    this.tieneVencimiento = rst.getBoolean(9);
                    this.fechaVencimiento = rst.getDate(10);
                    this.formatoArchivoSoporte = rst.getString(12);
                    Blob blob = rst.getBlob(11);
                    fisSalida = blob.getBinaryStream();
                    arreglo = new byte[2048];
                    String ruta = System.getProperty("user.dir") + "\\Temp\\" + empleado.getCedula() + "_"
                            + documento.getIdtiposDocumentos()
                            + "."
                            + this.getFormatoArchivo();
                    fosArchivoSalida = new FileOutputStream(ruta, true);
                    while ((byteLeidos = fisSalida.read(arreglo)) > -1) {
                        fosArchivoSalida.write(arreglo, 0, byteLeidos);
                    }
                    File fi = new File(ruta);
                    this.archivoSoporteDocumento = fi;
                    fisSalida.close();
                    fosArchivoSalida.close();
                }
                rst.close();
                st.close();
                con.close();
            }
        } catch (SQLException | IOException ex) {
            Logger.getLogger(CDocumentosPorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public CDocumentosPorEmpleado(CEmpleados empleado, CDocumentos documento) {
        this.empleado = empleado;
        InputStream fisSalida = null;
        FileOutputStream fosArchivoSalida;
        byte[] arreglo;
        int byteLeidos = 0;
        String sql;
        Statement st;
        ResultSet rst;
        Connection con;
        try {

            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();
                sql = "select * from documentosPorEmpleado "
                        + "where "
                        + "documentosPorEmpleado.cedula=" + empleado.getCedula() + " and "
                        + "documentosPorEmpleado.idTipoDocumento=" + documento.getIdtiposDocumentos() + " and "
                        + "documentosPorEmpleado.idCargo=" + empleado.getCargo() + " and "
                        + "documentosPorEmpleado.activo=1";

                rst = st.executeQuery(sql);

                if (rst.next()) {
                    this.empleado = empleado;
                    this.cargo = empleado.getIdCargo();
                    this.documento = documento;
                    this.numeroDocumento = rst.getString(4);
                    this.entidadEmisora = rst.getString(5);
                    this.fechaExpedicion = rst.getDate(6);
                    this.lugarExpedicion = rst.getString(7);
                    this.tieneVencimiento = rst.getBoolean(8);
                    this.fechaVencimiento = rst.getDate(9);
                    this.formatoArchivoSoporte = rst.getString(11);
                    Blob archivo = rst.getBlob(10);
                    String pathname = System.getProperty("user.dir") + "\\Temp\\" + empleado.getCedula() + "_"
                            + documento.getIdtiposDocumentos()
                            + "."
                            + this.getFormatoArchivo();
                    File file = new File(pathname);
                    FileOutputStream output = new FileOutputStream(file);

                    InputStream inStream = archivo.getBinaryStream();
                    int length = -1;
                    int size = (int) archivo.length();
                    byte[] buffer = new byte[size];
                    while ((length = inStream.read(buffer)) != -1) {
                        output.write(buffer, 0, length);
                        // output.flush(); 
                    }
                    // inStream.close();
                    this.archivoSoporteDocumento = file;
                    output.close();

                }
                rst.close();
                st.close();
                con.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(CDocumentosPorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<CDocumentos> getDocumentos(CEmpleados empleado) {
        String sql;
        Statement st = null;
        ResultSet rst = null;
        Connection con;
        ArrayList<CDocumentos> arrDocumentos = new ArrayList<>();

        try {
            con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

            if (con != null) {
                st = con.createStatement();
                sql = "Select idTipoDocumento from `documentosPorEmpleado`  where cedula='" + empleado.getCedula() + "' and activo=1";
                rst = st.executeQuery(sql);

                while (rst.next()) {
                    CDocumentos doc = new CDocumentos(ini, rst.getInt(1));
                    arrDocumentos.add(doc);
                }
            }
            rst.close();
            st.close();
            con.close();

        } catch (SQLException ex) {
            Logger.getLogger(CDocumentosPorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(CDocumentosPorEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
        return arrDocumentos;
    }

    public void setDocumentos(ArrayList<CDocumentos> documentos) {

        this.arrDocumentosPorEmpleado = documentos;
    }

    public boolean grabarDocumentosPorEmpleado() throws Exception {
        boolean grab = false;
        int caso = 0;
        FileInputStream fis = null;

        String sql = null;
        if (this.tieneVencimiento) {
            caso = 1;
        } else {
            caso = 0;
        }
        switch (caso) {
            case 0:
                sql = "INSERT INTO `documentosPorEmpleado` (`cedula`,`idTipoDocumento`, `idCargo`,"
                        + " `numeroDocumento`,`entidadEmisora`, `fechaExpedicion`,`lugarExpedicion`,`tieneVencimiento`,"
                        + " `formatoSoporteDocumento`,`activo`, `usuario`) VALUES ('"
                        + empleado.getCedula() + "',"
                        + documento.getIdtiposDocumentos() + ","
                        + empleado.getCargo() + ",'"
                        + this.numeroDocumento + "','"
                        + this.entidadEmisora + "','"
                        + this.fechaExpedicion + "','"
                        + this.lugarExpedicion + "',"
                        + caso + ",'"
                        + this.formatoArchivoSoporte + "',1,'"
                        + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "') "
                        + "ON DUPLICATE KEY UPDATE "
                        + "idTipoDocumento=" + documento.getIdtiposDocumentos() + ","
                        + "idCargo=" + empleado.getCargo() + ","
                        + "numeroDocumento='" + this.numeroDocumento + "',"
                        + "entidadEmisora='" + this.entidadEmisora + "',"
                        + "fechaExpedicion='" + this.fechaExpedicion + "',"
                        + "lugarExpedicion='" + this.lugarExpedicion + "',"
                        + "tieneVencimiento=" + caso + ","
                        + "formatoSoporteDocumento='" + this.formatoArchivoSoporte + "';";

                break;

            case 1:
                sql = "INSERT INTO `documentosPorEmpleado` (`cedula`,`idTipoDocumento`, `idCargo`,"
                        + " `numeroDocumento`,`entidadEmisora`, `fechaExpedicion`,`lugarExpedicion`, `tieneVencimiento`, `fechaVencimiento`,"
                        + " `formatoSoporteDocumento`,`activo`, `usuario`) VALUES ('"
                        + empleado.getCedula() + "',"
                        + documento.getIdtiposDocumentos() + ","
                        + empleado.getCargo() + ",'"
                        + this.numeroDocumento + "','"
                        + this.entidadEmisora + "','"
                        + this.fechaExpedicion + "','"
                        + this.lugarExpedicion + "',"
                        + caso + ",'"
                        + this.fechaVencimiento + "','"
                        + this.formatoArchivoSoporte + "',1,'"
                        + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "') ON DUPLICATE KEY UPDATE "
                        + "idTipoDocumento=" + documento.getIdtiposDocumentos() + ","
                        + "idCargo=" + empleado.getCargo() + ","
                        + "numeroDocumento='" + this.numeroDocumento + "',"
                        + "entidadEmisora='" + this.entidadEmisora + "',"
                        + "fechaExpedicion='" + this.fechaExpedicion + "',"
                        + "lugarExpedicion='" + this.lugarExpedicion + "',"
                        + "fechaVencimiento='" + this.fechaVencimiento + "',"
                        + "tieneVencimiento=" + caso + ","
                        + "formatoSoporteDocumento='" + this.formatoArchivoSoporte + "';";

                break;
        }

        if (ini.insertarDatosRemotamente(sql)) {
            if (insertarArchivoSoporteDocumento()) {
                grab = true;
            } else {
                grab = false;
            }
        }

        return grab;

    }

   
   

   
    private boolean insertarArchivoSoporteDocumento() {
        boolean grab = false;
        Connection conexion = null;
       


                conexion = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());
         

        try {

            if (archivoSoporteDocumento != null) {
                // String extension = archivoSoporteDocumento.getAbsolutePath().substring(archivoSoporteDocumento.getAbsolutePath().lastIndexOf("."));
                Statement sentencia = conexion.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
                String sql2 = "select * from documentosPorEmpleado  where cedula='" + empleado.getCedula()
                        + "' and idTipoDocumento=" + this.documento.getIdtiposDocumentos() + ";";
                ResultSet rs = sentencia.executeQuery(sql2);
                conexion.setAutoCommit(false);
                PreparedStatement pst = conexion.prepareStatement("Update documentosPorEmpleado set soporteDocumento=? where cedula=" + empleado.getCedula()
                        + " and idTipoDocumento=" + this.documento.getIdtiposDocumentos() + ";", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                while (rs.next()) {
                    //File file = new File(fotografia.getAbsolutePath());
                    FileInputStream fis = new FileInputStream(archivoSoporteDocumento);
                    pst.setBinaryStream(1, fis, archivoSoporteDocumento.length());
                    //pst.setString(2, extension);
                    // pst.setString(3, rs.getString(1)); 
                    pst.executeUpdate();
                    conexion.commit();
                    grab = true;
                }

                pst.close();
                rs.close();

            }

            conexion.close();
        } catch (FileNotFoundException ex) {
            System.out.println("Error en insertar() archivo no encontrado " + ex.getMessage());
            grab = false;
        } catch (SQLException ex) {
            System.out.println("Error en insertar() consulta sql " + ex.getMessage() + "(sql=");
            grab = false;
        }
        return grab;
    }

}
