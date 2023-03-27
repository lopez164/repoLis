/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aplicacionlogistica.distribucion.objetos;

import aplicacionlogistica.configuracion.CConexiones;
import aplicacionlogistica.configuracion.Inicio;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luis Eduardo López Casanova
 */
public class CSoportesConsignaciones {

    int conseutivo;
    int numeroManifiesto;
    int idCuentaBancaria;
    String numeroSoporte;
    int idBanco;
    String nombreDelBanco;
    String numeroDeCuenta;
    String tipoDeCuenta;
    String medioDePago;
    Double valor;
    Date fecha;
    Date fechaOperacion;
    Inicio ini;

    public CSoportesConsignaciones() {

    }

    public CSoportesConsignaciones(Inicio ini) {
        this.ini = ini;
    }

    /**
     * Método constructor el cual recibe tres (3) parametros
     *
     * @param ini correspondiente a la clase que configura el sistema
     * @param numeroManifiesto es el numero de manifiesto donde se encuentra la
     * factura que se va a descargar
     */
    public CSoportesConsignaciones(int numeroManifiesto, String numeroSoporte, int idBanco, String nombreDelBanco, String numeroDeCuenta, String medioDePago, Double valor, Date fecha, Date fechaOperacion, Inicio ini) {
        this.numeroManifiesto = numeroManifiesto;
        this.numeroSoporte = numeroSoporte;
        this.idBanco = idBanco;
        this.nombreDelBanco = nombreDelBanco;
        this.numeroDeCuenta = numeroDeCuenta;
        this.medioDePago = medioDePago;
        this.valor = valor;
        this.fecha = fecha;
        this.fechaOperacion = fechaOperacion;
        this.ini = ini;

    }

    public int getConseutivo() {
        return conseutivo;
    }

    public void setConseutivo(int conseutivo) {
        this.conseutivo = conseutivo;
    }

    public int getNumeroManifiesto() {
        return numeroManifiesto;
    }

    public void setNumeroManifiesto(int numeroManifiesto) {
        this.numeroManifiesto = numeroManifiesto;
    }

    public String getNumeroSoporte() {
        return numeroSoporte;
    }

    public void setNumeroSoporte(String numeroSoporte) {
        this.numeroSoporte = numeroSoporte;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public String getNombreDelBanco() {
        return nombreDelBanco;
    }

    public void setNombreDelBanco(String nombreDelBanco) {
        this.nombreDelBanco = nombreDelBanco;
    }

    public String getNumeroDeCuenta() {
        return numeroDeCuenta;
    }

    public void setNumeroDeCuenta(String numeroDeCuenta) {
        this.numeroDeCuenta = numeroDeCuenta;
    }

    public String getMedioDePago() {
        return medioDePago;
    }

    public void setMedioDePago(String medioDePago) {
        this.medioDePago = medioDePago;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getFechaOperacion() {
        return fechaOperacion;
    }

    public void setFechaOperacion(Date fechaOperacion) {
        this.fechaOperacion = fechaOperacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getIdCuentaBancaria() {
        return idCuentaBancaria;
    }

    public void setIdCuentaBancaria(int idCuentaBancaria) {
        this.idCuentaBancaria = idCuentaBancaria;
    }

    public String getTipoDeCuenta() {
        return tipoDeCuenta;
    }

    public void setTipoDeCuenta(String tipoDeCuenta) {
        this.tipoDeCuenta = tipoDeCuenta;
    }

    
    public Inicio getIni() {
        return ini;
    }

    public void setIni(Inicio ini) {
        this.ini = ini;
    }

    public boolean grabarSoportesDeConsignaciones(Inicio ini) {
        boolean grabado = false;
        try {

            String sql = "INSERT INTO soportesConsignaciones "
                    + "(numeroManifiesto,numeroSoporte,idCuentaBancaria,fechaOperacion,"
                    + "medioDePago,valor,activo,usurio,flag) VALUES('"
                    + this.numeroManifiesto + "','"
                    + this.numeroSoporte + "','"
                    + this.idCuentaBancaria + "','"
                    + this.fechaOperacion + "','"
                    + this.medioDePago + "','"
                    + this.valor + "','"
                    + "-1','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                    + "-1') "
                    + " ON DUPLICATE KEY UPDATE flag='-1'; ";

            grabado = ini.insertarDatosRemotamente(sql);

        } catch (SQLException ex) {
            Logger.getLogger(CSoportesConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabarDevolucionesPorManifiesto 1 sql " + ex.getMessage().toString());
        } catch (Exception ex) {
            Logger.getLogger(CSoportesConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Error en grabarDevolucionesPorManifiesto 2 sql " + ex.getMessage().toString());
        }
        return grabado;

    }

    public String getCadenaConLosCampos() {

        String cadena;

        cadena = this.numeroManifiesto + ","
                + this.numeroSoporte + ","
                + this.idCuentaBancaria + ","
                + this.fechaOperacion + ","
                + this.medioDePago + ","
                + this.valor;

        return cadena;
    }

    /**
     * Método que devuelve una cadena con sentencia SQL para insertar Datos en
     * la BBDD ","
     *
     * @return una cadena con la sentencia SQL para insertar Datos
     */
    public String getSentenciaInsertSQL() {
        String sql = null;
        try {

            sql = "INSERT INTO soportesConsignaciones "
                    + "(numeroManifiesto,numeroSoporte,idCuentaBancaria,fechaOperacion,"
                    + "medioDePago,valor,activo,usuario,flag) VALUES('"
                    + this.numeroManifiesto + "','"
                    + this.numeroSoporte + "','"
                    + this.idCuentaBancaria + "','"
                    + this.fechaOperacion + "','"
                    + this.medioDePago + "','"
                    + this.valor + "','"
                    + "1','"
                    + Inicio.deCifrar(ini.getUser().getNombreUsuario()) + "','"
                    + "-1') "
                    + " ON DUPLICATE KEY UPDATE flag='-1'; ";
        } catch (Exception ex) {
            Logger.getLogger(CSoportesConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }

    public String validarConsignacion() {
        String mensaje = "";
        Connection con = null;
        Statement st;
        ResultSet rst;
        String sql = "select * "
                + "from `vst_soportesConsignaciones` "
                + "where "
                + "numeroSoporte ='" + numeroSoporte + "' and "
                + "idCuentaBancaria='" + idCuentaBancaria + "';";
        con = CConexiones.GetConnection(ini.getCadenaRemota(), ini.getUsuarioBDRemota(), ini.getClaveBDRemota());

        if (con != null) {
            {
                try {
                    st = con.createStatement();
                    rst = st.executeQuery(sql);
                    if (rst.next()) {
                        mensaje = "La consignacion ya esta registrada en el sistema, manifiesto #\n :"
                                + rst.getInt("numeroManifiesto")
                                + "con fecha de consignacion :" + rst.getDate("fechaOperacion") + "";

                    }
                } catch (SQLException ex) {
                    Logger.getLogger(CSoportesConsignaciones.class.getName()).log(Level.SEVERE, null, ex);
                    return mensaje;
                }
            }

        }
        return mensaje;
    }

}
